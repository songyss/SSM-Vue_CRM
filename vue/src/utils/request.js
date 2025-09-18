// src/utils/request.js（Axios 实例配置）
import axios from 'axios'
import { ElMessage } from 'element-plus' // 统一提示组件（需确保项目已引入）

// 创建 Axios 实例
const request = axios.create({
  baseURL: 'http://localhost:8080/', // 接口基础路径（根据部署环境调整）
  timeout: 5000,
})

// 请求拦截器：添加 Token 到请求头
request.interceptors.request.use(
  (config) => {
    // 1. 排除登录接口（无需携带 Token）
    if (config.url !== '/user/login') {
      // 2. 从 localStorage 获取 Token 并添加到请求头
      const token = localStorage.getItem('crm_token')
      if (token) {
        config.headers.Authorization = 'Bearer ' + token // 推荐加"Bearer "前缀（标准 JWT 格式）
      } else {
        // 2.4 无 Token 时拦截请求（未登录场景，需求文档"未登录不可访问业务接口"）
        ElMessage.warning('请先登录后再操作')
        return Promise.reject(new Error('未登录，无法访问接口'))
      }
    }
    return config
  },
  (error) => {
    // 请求发送前错误（如参数格式错误），匹配需求"数据规范"
    ElMessage.error('请求参数错误，请检查后重试')
    return Promise.reject(error)
  },
)

// 响应拦截器：处理 Token 过期等错误
request.interceptors.response.use(
  (response) => {
    // 3.1 统一处理后端业务响应（需求文档"数据规范"：后端返回格式含 code/msg/data）
    if (!response.data) {
      return Promise.reject(new Error('服务器返回数据异常'))
    }

    const { code, message, data } = response.data
    // 3.2 非 200 状态码（后端业务错误，如客户状态非法、参数缺失）
    if (code !== 200) {
      ElMessage.error(message || '操作失败，请重试')
      return Promise.reject(new Error(message || '业务错误'))
    }
    // 3.3 200 状态码：返回核心数据（简化前端使用，无需每次解析 response.data.data）
    return data
  },
  (error) => {
    if (error.response && error.response.data && error.response.data.message) {
      if (error.response.data.message.includes('Token 无效或已过期')) {
        localStorage.removeItem('crm_token')
        localStorage.removeItem('crm_userInfo')
        window.location.href = '/' // 匹配路由配置（你的路由中"/"为登录页）
        ElMessage.error('登录已过期，请重新登录') // 替换alert，统一提示风格
      } else {
        ElMessage.error(error.response.data.message)
      }
    } else {
      ElMessage.error('网络异常，请稍后重试')
    }
    return Promise.reject(error)
  },
)

export default request
