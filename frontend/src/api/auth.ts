import { ElMessage } from 'element-plus'

import { post } from './request'
import type { Result } from './types'

/**
 * 认证模块 API（契约：docs/03-接口文档.md §1）。
 *
 * 后端认证接口未实现期间，通过 VITE_AUTH_MOCK=true 走本地模拟数据，
 * 用于联调登录页 / 状态持久化 / 导航栏登录态；实现 POST /api/v1/auth/login
 * 后把开关改为 false 即切换到真实接口，页面代码零改动。
 */
export const AUTH_MOCK_ENABLED = import.meta.env.VITE_AUTH_MOCK === 'true'

export interface LoginDTO {
  username: string
  password: string
}

export interface LoginUserVO {
  id: string
  username: string
  nickname: string
  avatar: string | null
}

export interface LoginVO {
  token: string
  /** Token 有效期（秒），用于前端预留续期逻辑 */
  expiresIn: number
  user: LoginUserVO
}

/** 登录：POST /api/v1/auth/login */
export function login(data: LoginDTO): Promise<Result<LoginVO>> {
  return AUTH_MOCK_ENABLED ? mockLogin(data) : post<LoginVO>('/api/v1/auth/login', data)
}

// ---------------------------------------------------------------------------
// Mock 实现（仅演示前端流程，鉴权逻辑不存在，勿在真实环境开启）
// ---------------------------------------------------------------------------

const MOCK_USER: LoginUserVO = { id: '1', username: 'admin', nickname: '管理员', avatar: null }

async function mockLogin(data: LoginDTO): Promise<Result<LoginVO>> {
  await new Promise((resolve) => setTimeout(resolve, 400))
  if (data.username === 'admin' && data.password === '12345678') {
    return {
      code: 0,
      message: 'success',
      data: { token: `mock-token-${Date.now()}`, expiresIn: 86400, user: MOCK_USER },
    }
  }
  ElMessage.error('用户名或密码错误（Mock）')
  return Promise.reject(new Error('mock login failed'))
}
