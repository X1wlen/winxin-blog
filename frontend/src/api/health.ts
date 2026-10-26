import { get } from './request'

/** GET /ping 的 data 结构（对应后端 PingController） */
export interface PingVO {
  service: string
  message: string
  javaVersion: string
  timestamp: string
}

/** 健康检查：验证 后端连通性 + CORS + Axios 拦截器 链路 */
export function ping() {
  return get<PingVO>('/ping')
}
