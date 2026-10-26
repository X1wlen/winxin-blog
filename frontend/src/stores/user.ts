import { ref } from 'vue'
import { defineStore } from 'pinia'

/**
 * 用户状态（预留占位，不实现）。
 *
 * TODO(认证模块): 由你实现 token 持久化、userInfo、login()/logout() action，
 * 请求拦截器（api/request.ts）将从此处读取 token 附加到请求头。
 */
export const useUserStore = defineStore('user', () => {
  const token = ref('')

  return { token }
})
