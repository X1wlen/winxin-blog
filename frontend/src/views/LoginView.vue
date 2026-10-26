<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { CollectionTag, EditPen, Lock, MagicStick, User } from '@element-plus/icons-vue'

import { AUTH_MOCK_ENABLED, login, type LoginDTO } from '@/api/auth'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const formRef = ref<FormInstance>()
const loading = ref(false)
const form = reactive<LoginDTO>({ username: '', password: '' })

/** 校验规则与接口契约一致（docs/03-接口文档.md §1.1） */
const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 32, message: '长度为 4~32 个字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, max: 64, message: '长度为 8~64 个字符', trigger: 'blur' },
  ],
}

async function onSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const result = await login({ ...form })
    userStore.setLogin(result.data)
    ElMessage.success(`欢迎回来，${result.data.user.nickname}`)
    // 支持登录后回跳来源页（后续路由守卫会用到 ?redirect=）
    router.push((route.query.redirect as string) || '/')
  } catch {
    // 失败提示已由 Axios 拦截器 / Mock 统一弹出
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <!-- 背景装饰光斑（纯装饰，不参与交互） -->
    <div class="blob blob-a" aria-hidden="true"></div>
    <div class="blob blob-b" aria-hidden="true"></div>
    <div class="blob blob-c" aria-hidden="true"></div>

    <div class="login-card">
      <!-- 左侧品牌区：窄屏隐藏 -->
      <aside class="brand-pane">
        <div class="brand-row">
          <span class="logo">W</span>
          <span class="brand-name">WinXin Blog</span>
        </div>
        <h2 class="slogan">把想法写成文章<br />把文章沉淀成自己</h2>
        <ul class="features">
          <li>
            <el-icon><EditPen /></el-icon>
            <span>沉浸式 Markdown 写作体验</span>
          </li>
          <li>
            <el-icon><CollectionTag /></el-icon>
            <span>分类与标签，让知识成体系</span>
          </li>
          <li>
            <el-icon><MagicStick /></el-icon>
            <span>AI 写作助手，灵感不断线</span>
          </li>
        </ul>
        <p class="copyright">© 2026 WinXin Blog</p>
      </aside>

      <!-- 右侧表单区 -->
      <main class="form-pane">
        <div class="form-brand">
          <span class="logo logo-sm">W</span>
          <span>WinXin Blog</span>
        </div>

        <h1 class="welcome">欢迎回来</h1>
        <p class="form-subtitle">登录你的账号，继续你的写作</p>

        <el-alert
          v-if="AUTH_MOCK_ENABLED"
          class="mock-tip"
          type="info"
          :closable="false"
          title="Mock 模式：账号 admin / 密码 12345678"
        />

        <el-form ref="formRef" :model="form" :rules="rules" size="large" @submit.prevent>
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              :prefix-icon="User"
              placeholder="用户名"
              autocomplete="username"
              @keyup.enter="onSubmit"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              :prefix-icon="Lock"
              type="password"
              show-password
              placeholder="密码"
              autocomplete="current-password"
              @keyup.enter="onSubmit"
            />
          </el-form-item>
          <el-button class="submit" type="primary" size="large" :loading="loading" @click="onSubmit">
            登 录
          </el-button>
        </el-form>
      </main>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  /* 覆盖 Element 主色为靛蓝，聚焦描边/链接等自动跟随 */
  --el-color-primary: #6366f1;

  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #ec4899 100%);
}

/* ---- 背景光斑 ---- */
.blob {
  position: absolute;
  border-radius: 50%;
  filter: blur(70px);
  opacity: 0.4;
  pointer-events: none;
}

.blob-a {
  width: 420px;
  height: 420px;
  background: #22d3ee;
  top: -120px;
  left: -100px;
  animation: drift-a 16s ease-in-out infinite alternate;
}

.blob-b {
  width: 380px;
  height: 380px;
  background: #f472b6;
  right: -100px;
  bottom: -140px;
  animation: drift-b 18s ease-in-out infinite alternate;
}

.blob-c {
  width: 240px;
  height: 240px;
  background: #facc15;
  top: 56%;
  left: 56%;
  opacity: 0.25;
  animation: drift-a 22s ease-in-out infinite alternate-reverse;
}

@keyframes drift-a {
  to {
    transform: translate(60px, 44px) scale(1.12);
  }
}

@keyframes drift-b {
  to {
    transform: translate(-56px, -40px) scale(1.15);
  }
}

/* ---- 卡片 ---- */
.login-card {
  position: relative;
  display: flex;
  width: min(880px, 100%);
  min-height: 540px;
  border-radius: 20px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 24px 64px rgba(30, 15, 90, 0.35);
}

/* ---- 左侧品牌区 ---- */
.brand-pane {
  flex: 1 1 46%;
  display: flex;
  flex-direction: column;
  padding: 44px 40px 32px;
  color: #fff;
  background: linear-gradient(165deg, #312e81 0%, #4c1d95 55%, #6d28d9 100%);
}

.brand-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 42px;
  height: 42px;
  border-radius: 12px;
  background: linear-gradient(135deg, #22d3ee, #6366f1);
  color: #fff;
  font-size: 20px;
  font-weight: 800;
  box-shadow: 0 6px 16px rgba(34, 211, 238, 0.35);
}

.brand-name {
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.slogan {
  margin: 56px 0 24px;
  font-size: 26px;
  line-height: 1.55;
  font-weight: 700;
}

.features {
  list-style: none;
  margin: 0;
  padding: 0;
}

.features li {
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 16px 0;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.88);
}

.features .el-icon {
  flex: none;
  width: 30px;
  height: 30px;
  padding: 7px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.14);
}

.copyright {
  margin-top: auto;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.55);
}

/* ---- 右侧表单区 ---- */
.form-pane {
  flex: 1 1 54%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 44px 48px 36px;
}

.form-brand {
  display: none;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  font-weight: 700;
}

.logo-sm {
  width: 30px;
  height: 30px;
  border-radius: 9px;
  font-size: 15px;
}

.welcome {
  margin: 0 0 6px;
  font-size: 24px;
  color: #111827;
}

.form-subtitle {
  margin: 0 0 24px;
  font-size: 14px;
  color: var(--el-text-color-secondary);
}

.mock-tip {
  margin-bottom: 18px;
}

.form-pane :deep(.el-input__wrapper) {
  border-radius: 10px;
}

.submit {
  width: 100%;
  margin-top: 4px;
  border: none;
  background: linear-gradient(90deg, #6366f1, #8b5cf6);
  font-weight: 600;
  letter-spacing: 4px;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.submit:hover {
  transform: translateY(-1px);
  box-shadow: 0 10px 22px rgba(99, 102, 241, 0.4);
}

.submit:active {
  transform: translateY(0);
}

/* ---- 响应式 ---- */
@media (max-width: 820px) {
  .brand-pane {
    display: none;
  }

  .form-brand {
    display: flex;
  }

  .form-pane {
    padding: 40px 28px 32px;
  }
}

@media (prefers-reduced-motion: reduce) {
  .blob {
    animation: none;
  }
}
</style>
