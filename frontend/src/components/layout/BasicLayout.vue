<script setup lang="ts">
import NavBar from './NavBar.vue'
</script>

<template>
  <el-container class="layout">
    <el-header class="layout-header" height="60px">
      <NavBar />
    </el-header>
    <el-main class="layout-main">
      <RouterView />
    </el-main>
    <el-footer class="layout-footer" height="48px">
      WinXin Blog · 个人博客系统（骨架阶段 v0.1.0）
    </el-footer>
  </el-container>
</template>

<style scoped>
.layout {
  min-height: 100vh;
}

.layout-header {
  padding: 0;
  border-bottom: 1px solid var(--el-border-color-light);
  background: #fff;
}

.layout-main {
  max-width: 1080px;
  width: 100%;
  margin: 0 auto;
  background: var(--el-fill-color-blank);
}

.layout-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--el-text-color-secondary);
  font-size: 13px;
  border-top: 1px solid var(--el-border-color-light);
}
</style>
