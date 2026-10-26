import axios, {
  AxiosError,
  type AxiosRequestConfig,
  type AxiosResponse,
  type InternalAxiosRequestConfig,
} from 'axios'
import { ElMessage } from 'element-plus'

import type { Result } from './types'
import { TOKEN_KEY } from '@/stores/user'

/** 后端约定的成功业务码（ResultCode.SUCCESS） */
export const SUCCESS_CODE = 0

/**
 * Axios 统一封装。
 *
 * 设计要点：
 * 1. baseURL 来自环境变量 VITE_API_BASE_URL，实现开发/生产 API 地址切换；
 * 2. 请求拦截器：统一附加登录凭证（认证模块接入后在此携带 Token）；
 * 3. 响应拦截器：把"业务失败（HTTP 200 但 code !== 0）"和"HTTP 异常（4xx/5xx/网络错误）"
 *    两条失败路径全部收口为 ElMessage 提示 + Promise.reject，业务代码只写成功分支。
 */
const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  timeout: 10_000,
})

instance.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  // 已登录则统一附加凭证；Token 的签发/校验/失效由后端认证模块负责
  const token = localStorage.getItem(TOKEN_KEY)
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

const HTTP_ERROR_MESSAGES: Record<number, string> = {
  400: '请求参数错误',
  401: '未登录或登录已过期',
  403: '无权访问该资源',
  404: '请求的资源不存在',
  405: '请求方法不支持',
  408: '请求超时',
  429: '请求过于频繁，请稍后再试',
  500: '服务器内部错误',
  502: '网关错误',
  503: '服务暂不可用',
}

instance.interceptors.response.use(
  (response: AxiosResponse) => {
    const result = response.data as Result
    if (result.code === SUCCESS_CODE) {
      // 绕过 AxiosResponse 类型：拦截器已把响应"解包"为 Result，调用方拿到的是统一结构
      return result as unknown as AxiosResponse
    }
    ElMessage.error(result.message || '请求失败')
    return Promise.reject(new Error(result.message || '请求失败'))
  },
  (error: AxiosError<Result>) => {
    let message: string
    if (error.response) {
      const { status, data } = error.response
      // 后端异常也返回 Result 结构，优先用它的 message
      message = data?.message || HTTP_ERROR_MESSAGES[status] || `请求失败（HTTP ${status}）`
      if (status === 401) {
        // TODO(认证模块): 清除登录态并跳转登录页
      }
    } else if (error.code === 'ECONNABORTED') {
      message = '请求超时'
    } else {
      message = '网络异常，请检查后端服务是否启动'
    }
    ElMessage.error(message)
    return Promise.reject(error)
  },
)

/** 以下泛型包装让调用方直接拿到 Promise<Result<T>>，与拦截器的解包行为对齐 */
export function get<T = unknown>(url: string, params?: object, config?: AxiosRequestConfig): Promise<Result<T>> {
  return instance.get(url, { params, ...config }) as Promise<Result<T>>
}

export function post<T = unknown>(url: string, data?: object, config?: AxiosRequestConfig): Promise<Result<T>> {
  return instance.post(url, data, config) as Promise<Result<T>>
}

export function put<T = unknown>(url: string, data?: object, config?: AxiosRequestConfig): Promise<Result<T>> {
  return instance.put(url, data, config) as Promise<Result<T>>
}

export function del<T = unknown>(url: string, config?: AxiosRequestConfig): Promise<Result<T>> {
  return instance.delete(url, config) as Promise<Result<T>>
}

export default instance
