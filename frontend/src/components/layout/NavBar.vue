<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

/** 导航菜单：新增页面时在此登记 */
const menus = [
  { path: '/', title: '首页' },
  { path: '/health', title: '健康检查' },
]

function onLogout() {
  // TODO(认证模块): 调用 POST /api/v1/auth/logout 使服务端 Token 失效（Mock 模式下仅清本地）
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<template>
  <div class="navbar">
    <span class="brand">WinXin Blog</span>
    <el-menu mode="horizontal" :default-active="route.path" router :ellipsis="false" class="menu">
      <el-menu-item v-for="menu in menus" :key="menu.path" :index="menu.path">
        {{ menu.title }}
      </el-menu-item>
    </el-menu>
    <div class="navbar-right">
      <template v-if="userStore.user">
        <span class="nickname">{{ userStore.user.nickname }}</span>
        <el-button link type="info" @click="onLogout">退出</el-button>
      </template>
      <el-button v-else type="primary" size="small" tag="RouterLink" to="/login">登录</el-button>
    </div>
  </div>
</template>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
  max-width: 1080px;
  margin: 0 auto;
  padding: 0 16px;
}

.brand {
  font-size: 18px;
  font-weight: 700;
  margin-right: 32px;
  white-space: nowrap;
}

.menu {
  flex: 1;
  border-bottom: none;
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 96px;
  justify-content: flex-end;
}

.nickname {
  font-size: 14px;
  color: var(--el-text-color-primary);
}
</style>
