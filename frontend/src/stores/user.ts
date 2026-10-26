import { ref } from 'vue'
import { defineStore } from 'pinia'

import type { LoginUserVO, LoginVO } from '@/api/auth'

/** localStorage 键名：请求拦截器（api/request.ts）据此给请求附加 Token */
export const TOKEN_KEY = 'wx_blog_token'
const USER_KEY = 'wx_blog_user'

function readStoredUser(): LoginUserVO | null {
  try {
    return JSON.parse(localStorage.getItem(USER_KEY) || 'null') as LoginUserVO | null
  } catch {
    return null
  }
}

/**
 * 登录态管理：token + 当前用户，localStorage 持久化（刷新不丢）。
 *
 * 说明：此处只管理"前端持有的登录态"；Token 签发/校验/服务端失效
 * （登出、改密踢下线等）属于认证模块（后端 Security/JWT），由你实现后
 * 把 api/auth.ts 的 Mock 开关关掉、并在登出处补调 POST /api/v1/auth/logout。
 */
export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem(TOKEN_KEY) || '')
  const user = ref<LoginUserVO | null>(readStoredUser())

  /** 登录成功后写入状态并持久化 */
  function setLogin(vo: LoginVO) {
    token.value = vo.token
    user.value = vo.user
    localStorage.setItem(TOKEN_KEY, vo.token)
    localStorage.setItem(USER_KEY, JSON.stringify(vo.user))
  }

  /** 清除本地登录态 */
  function logout() {
    token.value = ''
    user.value = null
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  }

  return { token, user, setLogin, logout }
})
