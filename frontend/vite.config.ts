import { fileURLToPath, URL } from 'node:url'

import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 5173,
    // 允许局域网访问（手机联调）
    host: true,
    // 备选跨域方案：若不想让浏览器直连后端（CORS），可改走 Vite 代理，
    // 并把 .env.development 中 VITE_API_BASE_URL 置空（同源）。二选一，默认直连以验证 CORS 链路。
    // proxy: {
    //   '/api': {
    //     target: 'http://localhost:8080',
    //     changeOrigin: true,
    //   },
    // },
  },
})
