/// <reference types="vite/client" />

interface ImportMetaEnv {
  /** 后端 API 地址：开发为 http://localhost:8080，生产留空（同源） */
  readonly VITE_API_BASE_URL: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
