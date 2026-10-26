<script setup lang="ts">
import {
  ChatDotRound,
  CollectionTag,
  EditPen,
  Key,
  MagicStick,
  UserFilled,
} from '@element-plus/icons-vue'
import type { Component } from 'vue'

import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

/** 开发路线：待实现业务模块（icon 以组件形式注入模板） */
interface RoadmapModule {
  icon: Component
  title: string
  desc: string
  status: string
  color: string
}

const modules: RoadmapModule[] = [
  { icon: EditPen, title: '文章', desc: '发布 / 编辑 / 删除 / 分页列表 / 详情', status: '待实现', color: '#6366f1' },
  { icon: CollectionTag, title: '分类与标签', desc: '分类管理、标签管理、文章关联', status: '待实现', color: '#8b5cf6' },
  { icon: ChatDotRound, title: '评论', desc: '发表 / 回复 / 列表，楼中楼回复', status: '待实现', color: '#ec4899' },
  { icon: Key, title: '认证', desc: '注册 / 登录 / 登出（Spring Security + JWT）', status: '待实现', color: '#14b8a6' },
  { icon: UserFilled, title: '用户中心', desc: '资料修改 / 头像上传', status: '待实现', color: '#f59e0b' },
  { icon: MagicStick, title: 'AI 助手', desc: '文章摘要 / 写作助手（Spring AI）', status: '待实现', color: '#0ea5e9' },
]

const stacks = [
  'Spring Boot 4.1.2',
  'Java 21 · 虚拟线程',
  'MyBatis-Plus',
  'MySQL 8',
  'Redis',
  'Vue 3',
  'Element Plus',
]
</script>

<template>
  <div class="home">
    <!-- Hero 横幅 -->
    <section class="hero">
      <div class="hero-blob hero-blob-a" aria-hidden="true"></div>
      <div class="hero-blob hero-blob-b" aria-hidden="true"></div>

      <span class="badge">骨架 v0.1.0 · 已就绪</span>
      <h1 class="hero-title">
        你好，我是<span class="accent"> WinXin</span>
        <br />
        把想法写成文章，把文章沉淀成自己
      </h1>
      <p class="hero-desc">
        前后端分离的个人博客系统，单体架构、接口先行。基础设施已全部打通：
        统一响应、全局异常、CORS、接口文档与登录链路均已验证。
      </p>

      <div class="hero-actions">
        <el-button class="btn-light" size="large" tag="RouterLink" to="/health">
          验证前后端连通性
        </el-button>
        <el-button
          v-if="!userStore.user"
          class="btn-outline"
          size="large"
          tag="RouterLink"
          to="/login"
        >
          登录 / 注册
        </el-button>
        <el-button v-else class="btn-outline" size="large" tag="RouterLink" to="/health">
          欢迎回来，{{ userStore.user.nickname }}
        </el-button>
      </div>

      <div class="stacks">
        <span v-for="stack in stacks" :key="stack" class="stack-chip">{{ stack }}</span>
      </div>
    </section>

    <!-- 路线图 -->
    <section class="roadmap">
      <h2 class="section-title">开发路线</h2>
      <p class="section-subtitle">按模块推进 · 每个模块先定契约（接口文档）再实现</p>

      <el-row :gutter="16" class="module-grid">
        <el-col v-for="module in modules" :key="module.title" :xs="24" :sm="12" :md="8">
          <div class="module-card">
            <span
              class="module-icon"
              :style="{ background: `${module.color}1a`, color: module.color }"
            >
              <el-icon :size="20"><component :is="module.icon" /></el-icon>
            </span>
            <div class="module-head">
              <span class="module-title">{{ module.title }}</span>
              <el-tag size="small" type="info" effect="plain" round>{{ module.status }}</el-tag>
            </div>
            <p class="module-desc">{{ module.desc }}</p>
          </div>
        </el-col>
      </el-row>
    </section>
  </div>
</template>

<style scoped>
/* ---- Hero ---- */
.hero {
  position: relative;
  overflow: hidden;
  padding: 56px 48px 44px;
  border-radius: 20px;
  color: #fff;
  background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 55%, #db2777 100%);
  box-shadow: 0 16px 48px rgba(79, 70, 229, 0.35);
}

.hero-blob {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
  opacity: 0.35;
  pointer-events: none;
}

.hero-blob-a {
  width: 300px;
  height: 300px;
  background: #22d3ee;
  top: -110px;
  right: -60px;
  animation: hero-drift 16s ease-in-out infinite alternate;
}

.hero-blob-b {
  width: 240px;
  height: 240px;
  background: #facc15;
  bottom: -120px;
  left: 30%;
  opacity: 0.22;
  animation: hero-drift 20s ease-in-out infinite alternate-reverse;
}

@keyframes hero-drift {
  to {
    transform: translate(40px, 30px) scale(1.1);
  }
}

.badge {
  display: inline-block;
  padding: 4px 14px;
  border-radius: 999px;
  font-size: 13px;
  background: rgba(255, 255, 255, 0.18);
  border: 1px solid rgba(255, 255, 255, 0.35);
  backdrop-filter: blur(4px);
}

.hero-title {
  margin: 18px 0 14px;
  font-size: 34px;
  line-height: 1.4;
  font-weight: 800;
}

.accent {
  background: linear-gradient(90deg, #fde68a, #f472b6);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.hero-desc {
  max-width: 560px;
  margin: 0 0 24px;
  font-size: 15px;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.88);
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.btn-light {
  border: none;
  background: #fff;
  color: #4f46e5;
  font-weight: 600;
}

.btn-light:hover {
  transform: translateY(-1px);
  box-shadow: 0 10px 22px rgba(0, 0, 0, 0.18);
  background: #fff;
  color: #4338ca;
}

.btn-outline {
  border: 1px solid rgba(255, 255, 255, 0.65);
  background: transparent;
  color: #fff;
}

.btn-outline:hover {
  background: rgba(255, 255, 255, 0.14);
  border-color: #fff;
  color: #fff;
}

.stacks {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 28px;
}

.stack-chip {
  padding: 4px 12px;
  border-radius: 999px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.92);
  background: rgba(255, 255, 255, 0.14);
  border: 1px solid rgba(255, 255, 255, 0.22);
}

/* ---- 路线图 ---- */
.roadmap {
  margin-top: 40px;
}

.section-title {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
  color: #111827;
}

.section-title::after {
  content: '';
  display: block;
  width: 48px;
  height: 4px;
  margin-top: 8px;
  border-radius: 2px;
  background: linear-gradient(90deg, #6366f1, #db2777);
}

.section-subtitle {
  margin: 10px 0 20px;
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.module-grid {
  row-gap: 16px;
}

.module-card {
  height: 100%;
  padding: 20px;
  border-radius: 14px;
  background: #fff;
  border: 1px solid var(--el-border-color-lighter);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.module-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 28px rgba(79, 70, 229, 0.12);
}

.module-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  border-radius: 12px;
  margin-bottom: 14px;
}

.module-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.module-title {
  font-size: 15px;
  font-weight: 600;
}

.module-desc {
  margin: 8px 0 0;
  color: var(--el-text-color-secondary);
  font-size: 13px;
  line-height: 1.7;
}

/* ---- 响应式 ---- */
@media (max-width: 640px) {
  .hero {
    padding: 36px 24px 32px;
  }

  .hero-title {
    font-size: 26px;
  }
}

@media (prefers-reduced-motion: reduce) {
  .hero-blob {
    animation: none;
  }
}
</style>
