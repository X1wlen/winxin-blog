<script setup lang="ts">
/** 骨架阶段首页：展示项目定位与待实现模块清单 */
const modules = [
  { title: '文章', desc: '发布 / 编辑 / 删除 / 分页列表 / 详情', status: '待实现' },
  { title: '分类与标签', desc: '分类管理、标签管理、文章关联', status: '待实现' },
  { title: '评论', desc: '发表 / 回复 / 列表', status: '待实现' },
  { title: '认证', desc: '注册 / 登录 / 登出（Spring Security + JWT）', status: '待实现' },
  { title: '用户中心', desc: '资料修改 / 头像上传', status: '待实现' },
  { title: 'AI 助手', desc: '文章摘要 / 写作助手（Spring AI）', status: '待实现' },
]
</script>

<template>
  <div class="home">
    <el-card shadow="never" class="hero">
      <h1 class="hero-title">WinXin Blog</h1>
      <p class="hero-desc">
        前后端分离的个人博客系统 · Spring Boot 4.1.2 + Java 21（虚拟线程） + MyBatis-Plus + MySQL/Redis
        + Vue 3 + Element Plus。当前为基础骨架：/ping 链路已打通，业务模块按下表排期实现。
      </p>
      <el-button type="primary" tag="RouterLink" to="/health">验证前后端连通性</el-button>
    </el-card>

    <el-row :gutter="16" class="module-grid">
      <el-col v-for="module in modules" :key="module.title" :xs="24" :sm="12" :md="8">
        <el-card shadow="hover" class="module-card">
          <div class="module-head">
            <span class="module-title">{{ module.title }}</span>
            <el-tag type="info" size="small">{{ module.status }}</el-tag>
          </div>
          <p class="module-desc">{{ module.desc }}</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.hero {
  margin-bottom: 16px;
}

/* el-button 以 <a> 渲染时去掉默认下划线 */
.hero :deep(a.el-button) {
  text-decoration: none;
}

.hero-title {
  margin: 4px 0 8px;
  font-size: 26px;
}

.hero-desc {
  color: var(--el-text-color-regular);
  line-height: 1.8;
  margin-bottom: 16px;
}

.module-grid {
  row-gap: 16px;
}

.module-card {
  height: 100%;
}

.module-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.module-title {
  font-weight: 600;
}

.module-desc {
  margin: 8px 0 0;
  color: var(--el-text-color-secondary);
  font-size: 13px;
}
</style>
