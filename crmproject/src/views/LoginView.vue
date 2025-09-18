<template>
<<<<<<< HEAD
  <div class="login-container">
    <div class="login-box">
      <h2 class="title">CRM 客户关系管理系统</h2>

      <!-- 表单：保留基础校验，新增加载状态 -->
      <el-form :model="form" :rules="rules" ref="loginForm" label-position="top" class="login-form"
        :disabled="isSubmitting">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" clearable @keyup.enter.prevent="handleLogin" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" show-password @keyup.enter.prevent="handleLogin" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin" :loading="isSubmitting">
            登 录
          </el-button>
        </el-form-item>
      </el-form>
=======
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
>>>>>>> 0e5380c6c0bb9606823c793a4ff3b5bb711a6a04
    </div>
  </div>
</template>

<script setup lang="ts">
<<<<<<< HEAD
import { reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

// 1. 路由实例：用于登录后跳转
const router = useRouter()

// 2. 表单相关：保留基础校验，新增提交加载状态
const loginForm = ref(null)
const isSubmitting = ref(false) // 防止重复提交

const form = reactive({
  username: '', // 对应employees表的username字段（需求文档2.1.1：登录名）
  password: ''  // 对应employees表的password字段（需求文档3.1：MD5+盐值加密存储）
})

// 表单校验规则：保留必填校验，匹配需求文档"数据准确性"
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度需在2-20字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 3. 核心登录逻辑：对接后端接口+Token存储+按角色跳转
const handleLogin = async () => {
  // 第一步：前端表单基础校验
  loginForm.value.validate(async (valid) => {
    if (!valid) return // 校验失败，不发起请求

    try {
      isSubmitting.value = true // 开启加载状态

      // 第二步：调用后端登录接口（需求文档"用户身份验证"：从employees表校验）
      const response = await request.post(
        '/user/login', // 后端登录接口地址（需与后端约定）
        {
          username: form.username,
          password: form.password // 注意：实际项目需加密后传递（如MD5），避免明文
        }
      )

      // 第三步：处理后端响应（按需求文档返回格式解析）
      const { token, userInfo } = response
      if (token && userInfo) {
        // 3.1 存储Token（需求文档"数据安全性"：后续接口需携带）
        localStorage.setItem('crm_token', token) // 长期存储，支撑单次登录持续访问

        // 3.2 存储用户核心信息（用于前端权限控制，匹配需求文档"权限精细化"）
        localStorage.setItem('crm_userInfo', JSON.stringify(userInfo))

        // 3.3 按"角色+部门"跳转至对应模块（严格匹配需求文档各角色功能范围）
        const { role, department } = userInfo
        // 角色枚举（需求文档2.1.1/2.2.6/2.4.1：1=普通员工，2=经理，3=超级管理员）
        // 部门枚举（需求文档2.2.1/2.3.1/2.4.1：1=市场部，2=电话销售部，3=销售部）
        switch (true) {
          // 超级管理员（仅可访问管理员模块，需求文档2.1）
          case role === 3:
            router.push('/admin/employee-manage') // 员工管理页（需求2.1.1）
            break
          // 市场部经理（可访问市场部模块，需求文档2.2）
          case role === 2 && department === 1:
            router.push('/market/customer/pending-audit') // 客户待审核页（需求2.2.1）
            break
          // 电话销售部经理（可访问电话销售部模块，需求文档2.3）
          case role === 2 && department === 2:
            router.push('/sdr/customer/assign') // 客户分配页（需求2.3.1）
            break
          // 销售部经理（可访问销售部模块，需求文档2.4）
          case role === 2 && department === 3:
            router.push('/sales/customer/pending-allocate') // 客户待分配页（需求2.4.2）
            break
          // 市场部普通员工（仅可访问客户收集等基础功能，需求文档2.2.1）
          case role === 1 && department === 1:
            router.push('/market/customer/collect') // 客户信息收集页（需求2.2.1）
            break
          // 电话销售普通员工（仅可访问电话拜访功能，需求文档2.3.2）
          case role === 1 && department === 2:
            router.push('/sdr/customer/follow') // 客户电话跟进页（需求2.3.2）
            break
          // 销售部普通员工（仅可访问客户跟进等功能，需求文档2.4.4）
          case role === 1 && department === 3:
            router.push('/sales/customer/my-follow') // 我的客户跟进页（需求2.4.4）
            break
          // 默认：跳转至系统首页（避免未定义角色/部门的异常）
          default:
            router.push('/dashboard')
        }

        ElMessage.success(`登录成功，欢迎您：${userInfo.name}`)
      } else {
        // 后端返回业务错误（如用户名不存在、密码错误，需求文档"数据准确性"）
        ElMessage.error('登录失败，请重试')
      }
    } catch (error) {
      // 处理网络异常（需求文档"数据安全性"：网络错误提示）
      console.error('登录接口异常：', error)
      if (error.message) {
        ElMessageBox.alert(
          error.message || '登录失败，请重试',
          '登录失败',
          { type: 'error' }
        )
      } else {
        ElMessageBox.alert(
          '网络异常，请检查网络连接后重试',
          '登录失败',
          { type: 'error' }
        )
      }
    } finally {
      isSubmitting.value = false // 关闭加载状态
    }
  })
}
</script>

<style scoped>
/* 样式保留基础结构，仅优化细节以匹配需求文档"用户体验" */
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: linear-gradient(135deg, #3a7bd5 0%, #00d2ff 100%);
}

.login-box {
  width: 420px;
  padding: 40px;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.login-box:hover {
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.25);
}

.title {
  text-align: center;
  font-size: 22px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 30px;
  letter-spacing: 1px;
}

.login-form {
  margin-top: 15px;
}

.login-btn {
  width: 100%;
  font-size: 16px;
  padding: 12px 0;
  border-radius: 8px;
  background: #3a7bd5;
  border: none;
}

.login-btn:hover:not(:disabled) {
  background: #2962ff;
}

/* 输入框聚焦样式优化，提升用户体验 */
.el-input__wrapper:focus-within {
  box-shadow: 0 0 0 2px rgba(58, 123, 213, 0.2) !important;
  border-color: #3a7bd5 !important;
=======
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
>>>>>>> 0e5380c6c0bb9606823c793a4ff3b5bb711a6a04
}
</style>
