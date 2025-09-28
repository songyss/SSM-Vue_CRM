// src/utils/request.ts
import axios from 'axios'
import  type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

// 定义响应数据结构接口
interface ResponseData<T = unknown> {
  code: number
  message: string
  data: T
  total?: number
  pages?: number
}

// 1. 创建 Axios 实例
const request: AxiosInstance = axios.create({
  baseURL: 'http://192.168.120.120:8080/',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// 2. 请求拦截器
request.interceptors.request.use(
  (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
    // 排除登录接口
    if (config.url !== '/user/login') {
      const token = localStorage.getItem('crm_token')
      if (token) {
        if (config.headers) {
          config.headers.Authorization = 'Bearer ' + token
        }
      } else {
        ElMessage.warning('请先登录后再操作')
        throw new Error('未登录，无法访问接口') // 使用 throw 替代 return Promise.reject
      }
    }
    return config
  },
  (error: unknown) => {
    ElMessage.error('请求参数错误，请检查后重试')
    return Promise.reject(error)
  }
)

// 3. 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse): AxiosResponse | Promise<never> => {
    if (!response.data) {
      ElMessage.error('服务器返回数据异常')
      return Promise.reject(new Error('服务器返回数据为空'))
    }

    const res: ResponseData = response.data
    const { code, message } = res

    if (code !== 200) {
      ElMessage.error(message || '操作失败，请重试')
      return Promise.reject(new Error(message || '业务错误'))
    }

    return response
  },
    (error: unknown) => {
    // 类型守卫：确认 error 是 AxiosError 类型
    if (axios.isAxiosError(error)) {
      const resData = error.response?.data
      if (resData?.message?.includes('Token 无效或已过期')) {
        localStorage.removeItem('crm_token')
        localStorage.removeItem('crm_userInfo')
        window.location.href = '/'
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
export type { ResponseData }
