// src/stores/user.ts
import { defineStore } from 'pinia'

interface UserInfo {
  id: number
  name: string
  roleId: number // 角色ID
  department: number // 部门ID
  // 其他用户信息字段
}

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: null as UserInfo | null,
    token: localStorage.getItem('crm_token') || ''
  }),
  actions: {
    // 初始化用户信息（从本地存储加载）
    initUserInfo() {
      const storedUser = localStorage.getItem('crm_userInfo')
      if (storedUser) {
        this.userInfo = JSON.parse(storedUser)
      }
    },
    // 清除用户信息（退出登录时使用）
    clearUserInfo() {
      this.userInfo = null
      this.token = ''
      localStorage.removeItem('crm_token')
      localStorage.removeItem('crm_userInfo')
    }
  }
})
