<template>
  <div class="login-page">
    <!-- 背景装饰 -->
    <div class="login-bg-decoration"></div>

    <!-- 登录卡片 -->
    <div class="login-card">
      <!-- 品牌区域 -->
      <div class="brand-section">
        <div class="logo">
          <el-icon :size="36">
            <DataLine />
          </el-icon>
        </div>
        <h1 class="system-name">CRM客户管理系统</h1>
        <p class="system-desc">高效管理客户关系，提升业务价值</p>
      </div>

      <!-- 登录表单 -->
      <el-form
        ref="loginForm"
        :model="form"
        :rules="rules"
        class="login-form"
        label-position="left"
        label-width="0"
      >
        <!-- 用户名输入 -->
        <el-form-item prop="username">
          <div class="input-wrapper">
            <el-icon class="input-icon">
              <User />
            </el-icon>
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              :prefix-icon="false"
              class="custom-input"
            />
          </div>
        </el-form-item>

        <!-- 密码输入 -->
        <el-form-item prop="password">
          <div class="input-wrapper">
            <el-icon class="input-icon">
              <Lock />
            </el-icon>
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="false"
              class="custom-input"
              @keyup.enter="handleLogin"
            />
            <button type="button" class="toggle-pwd" @click="showPassword = !showPassword">
              <el-icon :size="18">
                <View v-if="showPassword" />
                <Hide v-else />
              </el-icon>
            </button>
          </div>
        </el-form-item>

        <!-- 记住密码和忘记密码 -->
        <div class="form-actions">
          <el-checkbox v-model="form.rememberMe" class="remember-me">
            记住我（7天内免登录）
          </el-checkbox>
          <el-link href="#" class="forgot-pwd" :underline="false"> 忘记密码？ </el-link>
        </div>

        <!-- 登录按钮 -->
        <el-form-item>
          <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 其他登录方式 -->
      <div class="other-login">
        <div class="separator">
          <span class="separator-text">其他登录方式</span>
        </div>
        <div class="social-login">
          <button class="social-btn wechat">
            <el-icon :size="20"></el-icon>
          </button>
          <button class="social-btn qq">
            <el-icon :size="20"><Qq /></el-icon>
          </button>
        </div>
      </div>

      <!-- 版权信息 -->
      <div class="copyright">© 2024 CRM客户管理系统 版权所有</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElForm } from 'element-plus'
import { User, Lock, View, Hide, DataLine } from '@element-plus/icons-vue'

// 表单数据
const form = ref({
  username: '',
  password: '',
  rememberMe: false,
})

// 状态管理
const loading = ref(false)
const showPassword = ref(false)
const loginForm = ref<InstanceType<typeof ElForm>>()
const router = useRouter()

// 表单验证规则
const rules = ref({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
})

// 登录处理
const handleLogin = async () => {
  // 表单验证
  try {
    await loginForm.value?.validate()
    loading.value = true

    // 模拟登录请求
    setTimeout(() => {
      // 实际项目中替换为真实接口调用
      if (form.value.username && form.value.password) {
        // 处理记住密码
        if (form.value.rememberMe) {
          localStorage.setItem(
            'crm_user',
            JSON.stringify({
              username: form.value.username,
              password: form.value.password,
              expire: Date.now() + 7 * 24 * 60 * 60 * 1000,
            }),
          )
        } else {
          localStorage.removeItem('crm_user')
        }

        // 存储登录状态
        localStorage.setItem('crm_token', 'valid_token')
        ElMessage.success('登录成功')
        router.push('/dashboard')
      } else {
        ElMessage.error('用户名或密码错误')
        loading.value = false
      }
    }, 800)
  } catch (error) {
    console.error('表单验证失败', error)
  }
}

// 页面加载时读取记住的用户信息
const loadRememberedUser = () => {
  const user = localStorage.getItem('crm_user')
  if (user) {
    try {
      const { username, password, expire } = JSON.parse(user)
      if (Date.now() < expire) {
        form.value.username = username
        form.value.password = password
        form.value.rememberMe = true
      }
    } catch (e) {
      localStorage.removeItem('crm_user')
    }
  }
}

// 初始化
loadRememberedUser()
</script>

<style scoped>
/* 基础样式 */
.login-page {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
  padding: 20px;
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.login-bg-decoration {
  position: absolute;
  width: 600px;
  height: 600px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(22, 93, 255, 0.1) 0%, rgba(255, 255, 255, 0) 70%);
  z-index: 1;
}

/* 登录卡片 */
.login-card {
  width: 100%;
  max-width: 420px;
  background-color: #fff;
  border-radius: 16px;
  box-shadow: 0 10px 30px -5px rgba(0, 0, 0, 0.1);
  padding: 40px 30px;
  position: relative;
  z-index: 2;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px -5px rgba(0, 0, 0, 0.15);
}

/* 品牌区域 */
.brand-section {
  text-align: center;
  margin-bottom: 35px;
}

.logo {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background-color: rgba(22, 93, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  color: #165dff;
}

.system-name {
  font-size: 24px;
  font-weight: 700;
  color: #1d2129;
  margin-bottom: 8px;
}

.system-desc {
  font-size: 14px;
  color: #86909c;
}

/* 表单样式 */
.login-form {
  margin-bottom: 20px;
}

.input-wrapper {
  position: relative;
  width: 100%;
}

.custom-input {
  height: 50px;
  padding: 0 16px 0 48px;
  border-radius: 8px;
  border: 1px solid #e5e6eb;
  font-size: 14px;
  transition: all 0.2s ease;
}

.custom-input:focus {
  border-color: #165dff;
  box-shadow: 0 0 0 3px rgba(22, 93, 255, 0.1);
}

.input-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: #86909c;
  width: 18px;
  height: 18px;
}

.toggle-pwd {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #86909c;
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: color 0.2s ease;
}

.toggle-pwd:hover {
  color: #165dff;
}

/* 表单操作区 */
.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: -10px 0 25px;
  font-size: 14px;
}

.remember-me {
  color: #4e5969;
  display: flex;
  align-items: center;
}

.remember-me .el-checkbox__label {
  margin-left: 6px;
}

.forgot-pwd {
  color: #165dff;
  transition: color 0.2s ease;
}

.forgot-pwd:hover {
  color: #0e42d2;
}

/* 登录按钮 */
.login-btn {
  width: 100%;
  height: 50px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  background-color: #165dff;
  border-color: #165dff;
  transition: all 0.2s ease;
}

.login-btn:hover {
  background-color: #0e42d2;
  border-color: #0e42d2;
}

.login-btn:loading {
  background-color: #165dff;
  border-color: #165dff;
}

/* 其他登录方式 */
.other-login {
  margin-top: 30px;
}

.separator {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.separator::before,
.separator::after {
  content: '';
  flex: 1;
  height: 1px;
  background-color: #e5e6eb;
}

.separator-text {
  padding: 0 16px;
  font-size: 14px;
  color: #86909c;
}

.social-login {
  display: flex;
  justify-content: center;
  gap: 24px;
}

.social-btn {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f2f3f5;
  color: #4e5969;
  cursor: pointer;
  transition: all 0.2s ease;
}

.social-btn:hover {
  background-color: rgba(22, 93, 255, 0.1);
  color: #165dff;
  transform: translateY(-3px);
}

/* 版权信息 */
.copyright {
  text-align: center;
  margin-top: 40px;
  font-size: 12px;
  color: #86909c;
}

/* 响应式调整 */
@media (max-width: 480px) {
  .login-card {
    padding: 30px 20px;
  }

  .system-name {
    font-size: 20px;
  }

  .custom-input,
  .login-btn {
    height: 46px;
  }
}
</style>
