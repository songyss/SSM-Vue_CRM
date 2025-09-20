<template>
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
    </div>
  </div>
</template>

<script setup lang="ts">
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
      const { token, userInfo } = response.data
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
}
</style>
