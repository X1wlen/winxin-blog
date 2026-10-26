<script setup lang="ts">
import { onMounted, ref } from 'vue'

import { ping, type PingVO } from '@/api/health'
import type { Result } from '@/api/types'
import { formatDateTime } from '@/utils/format'

const loading = ref(false)
const result = ref<Result<PingVO> | null>(null)
const baseUrl = import.meta.env.VITE_API_BASE_URL || '(同源)'

/** 失败提示由 Axios 拦截器统一弹出，这里只处理成功分支 */
async function check() {
  loading.value = true
  try {
    result.value = await ping()
  } catch {
    result.value = null
  } finally {
    loading.value = false
  }
}

onMounted(check)
</script>

<template>
  <el-card shadow="never">
    <template #header>
      <div class="header">
        <span>后端连通性检测</span>
        <el-tag size="small" type="info">GET {{ baseUrl }}/ping</el-tag>
      </div>
    </template>

    <el-button type="primary" :loading="loading" @click="check">重新检测</el-button>

    <el-alert
      v-if="result"
      class="result-alert"
      type="success"
      show-icon
      :closable="false"
      title="链路正常：CORS 跨域 + Axios 拦截器 + 统一 Result 结构均已生效"
      :description="`服务: ${result.data.service} · Java: ${result.data.javaVersion} · 服务器时间: ${formatDateTime(result.data.timestamp)}`"
    />
    <el-alert
      v-else
      class="result-alert"
      type="info"
      show-icon
      :closable="false"
      title="尚无结果：点击「重新检测」发起请求（失败时拦截器会弹出具体原因）"
    />

    <h4 class="raw-title">原始响应（统一 Result 结构）</h4>
    <pre class="raw">{{ result ? JSON.stringify(result, null, 2) : '-' }}</pre>
  </el-card>
</template>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.result-alert {
  margin-top: 16px;
}

.raw-title {
  margin: 16px 0 8px;
}

.raw {
  margin: 0;
  padding: 12px;
  background: var(--el-fill-color-light);
  border-radius: 4px;
  font-size: 12px;
  line-height: 1.6;
  overflow: auto;
}
</style>
