import { defineStore } from 'pinia'
import request from '@/utils/request'
import type { RouteRecordRaw } from 'vue-router'
import { moduleRouteMap } from '@/utils/moduleRouteMap'
// import { useUserStore } from './user' // 引入用户状态管理

// // 权限结构定义
// interface Permission {
//   moduleId: number
//   buttonPermissions: string[] // 按钮级权限列表
// }

// 权限结构定义（根据实际返回调整，这里后端直接返回moduleId映射的权限数组）
interface PermissionResponseData {
  [moduleId: number]: string[] // 键为moduleId，值为该模块的按钮权限数组
  data: Record<number, string[]>
}

export const usePermissionStore = defineStore('permission', {
  state: () => ({
    // 权限数据结构: {模块ID: 按钮权限列表}
    permissions: {} as Record<number, string[]>,
    // // 可访问的模块路由列表
    accessibleModuleIds: [] as number[],
    // 所有按钮权限的扁平化列表
    allButtonPermissions: [] as string[]
  }),
   getters: {
    // 获取有权限的路由列表
    accessibleRoutes(): RouteRecordRaw[] {
      return this.accessibleModuleIds
        .map(id => moduleRouteMap[id])
        .filter(route => !!route) as RouteRecordRaw[]
    }
  },
  actions: {
    // 获取权限列表
    async fetchPermissions(roleId: number) {
      console.log('请求权限的roleId：', roleId, '类型：', typeof roleId); // 新增日志
      const res = await request.get<PermissionResponseData>('/permission/getPermission', {
        params: { roleId }
      })

      // 构建权限映射表
      const permissionMap: Record<number, string[]> = {}
      console.log(res.data)
      // 遍历对象的键值对（键是moduleId字符串，值是权限数组）
      Object.entries(res.data.data).forEach(([moduleIdStr, buttonPermissions]) => {
        const moduleId = Number(moduleIdStr) // 将键转为数字类型的moduleId
        permissionMap[moduleId] = buttonPermissions // 赋值权限数组
      })

      this.permissions = permissionMap
      this.accessibleModuleIds = Object.keys(permissionMap).map(Number)
      this.allButtonPermissions = Object.values(permissionMap).flat()

      console.log('权限列表：', this.permissions)
      console.log('可访问的模块ID列表：', this.accessibleModuleIds)
      console.log('所有按钮权限：', this.allButtonPermissions)

      return res.data.data
    },

    // 检查是否有模块访问权限
    hasModulePermission(moduleId: number): boolean {
      return this.accessibleModuleIds.includes(moduleId)
    },

    // 检查是否有按钮权限
    hasButtonPermission(permission: string): boolean {
      return this.allButtonPermissions.includes(permission)
    },

    // 检查模块下的特定按钮权限
    hasModuleButtonPermission(moduleId: number, permission: string): boolean {
      return (this.permissions[moduleId] || []).includes(permission)
    }
  }
})
