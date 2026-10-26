<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()

/** 导航菜单：新增页面时在此登记（骨架阶段仅两条） */
const menus = [
  { path: '/', title: '首页' },
  { path: '/health', title: '健康检查' },
]
</script>

<template>
  <div class="navbar">
    <span class="brand">WinXin Blog</span>
    <el-menu mode="horizontal" :default-active="route.path" router :ellipsis="false" class="menu">
      <el-menu-item v-for="menu in menus" :key="menu.path" :index="menu.path">
        {{ menu.title }}
      </el-menu-item>
    </el-menu>
    <!-- 预留：登录态展示区（认证模块接入后放头像/登录按钮） -->
    <div class="navbar-right"></div>
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
  min-width: 64px;
}
</style>
