// src/utils/request.js（Axios 实例配置 - 修复版）
import axios from 'axios'
import { ElMessage } from 'element-plus' // 确保项目已安装并引入 element-plus

// 1. 创建 Axios 实例
const request = axios.create({
  baseURL: 'http://localhost:8080/', // 接口基础路径（按实际部署环境调整）
  timeout: 5000, // 超时时间（可按需调整）
  headers: {
    'Content-Type': 'application/json;charset=utf-8' // 默认请求头（适配 JSON 格式）
  }
})

// 2. 请求拦截器：添加 Token 到请求头（原有逻辑保留，适配登录验证）
request.interceptors.request.use(
  (config) => {
    // 排除登录接口（无需携带 Token）
    if (config.url !== '/user/login') {
      const token = localStorage.getItem('crm_token')
      if (token) {
        config.headers.Authorization = 'Bearer ' + token // 标准 JWT 格式
      } else {
        ElMessage.warning('请先登录后再操作')
        return Promise.reject(new Error('未登录，无法访问接口'))
      }
    }
    return config
  },
  (error) => {
    ElMessage.error('请求参数错误，请检查后重试')
    return Promise.reject(error)
  }
)

// 3. 响应拦截器：保留完整响应体（关键修复！）
request.interceptors.response.use(
  (response) => {
    // 3.1 确保响应数据存在
    if (!response.data) {
      ElMessage.error('服务器返回数据异常')
      return Promise.reject(new Error('服务器返回数据为空'))
    }

    const res = response.data // 保留完整响应体（含 code/message/data/total/pages 等）
    const { code, message } = res

    // 3.2 处理后端业务错误（非 200 状态码）
    if (code !== 200) {
      ElMessage.error(message || '操作失败，请重试')
      return Promise.reject(new Error(message || '业务错误'))
    }

    // 3.3 成功：返回完整响应体（前端可通过 res.data 拿列表，res.total 拿总记录数）
    return res
  },
  (error) => {
    // 3.4 处理网络错误或 Token 过期
    if (error.response) {
      const resData = error.response.data
      // Token 过期逻辑（原有逻辑保留）
      if (resData?.message?.includes('Token 无效或已过期')) {
        localStorage.removeItem('crm_token')
        localStorage.removeItem('crm_userInfo')
        window.location.href = '/' // 跳转到登录页（按实际路由调整）
        ElMessage.error('登录已过期，请重新登录')
      } else {
        ElMessage.error(resData?.message || '服务器内部错误')
      }
    } else {
      ElMessage.error('网络异常，请检查网络连接后重试')
    }
    return Promise.reject(error)
  }
)

export default request
