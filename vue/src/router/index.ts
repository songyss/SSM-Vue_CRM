import { createRouter, createWebHistory, type RouteLocationNormalized } from 'vue-router'
import { usePermissionStore } from '@/stores/permission'
import { moduleRouteMap } from '@/utils/moduleRouteMap'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    //添加登录路由
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: {
        requiresAuth: false,
        layout: 'none', // 标记为不需要布局
      },
    },

    // 主布局路由容器，所有子路由都将在此布局下渲染
    {
      path: '/',
      component: () => import('../layout/MainLayout.vue'), // 新增主布局组件
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: 'dashboard',
        },

        {
          path: '/dashboard',
          name: 'dashboard',
          component: () => import('../views/DashboardView.vue'),
          meta: { title: '控制台', icon: 'house', requiresAuth: true },
        },
        // 权限管理路由
        {
          path: '/permission',
          name: 'permission',
          component: () => import('../views/ConsoleView.vue'),
          meta: { title: '权限管理', icon: 'lock', requiresAuth: true },
          children: [
             // 动态关联子路由
            moduleRouteMap[1],  // 用户管理
            moduleRouteMap[2],  // 角色管理
            moduleRouteMap[3],  // 部门管理
            moduleRouteMap[4],  // 日志管理
          ],
        },
        // 运营管理路由
        {
          path: '/operation',
          name: 'operation',
          component: () => import('../views/MarketingView.vue'),
          meta: { title: '运营管理', icon: 'operation', requiresAuth: true },
          children: [
             moduleRouteMap[5],  // 客户管理
            moduleRouteMap[6],  // 活动管理
          ],
        },
        // 业务管理路由
        {
          path: '/business',
          name: 'business',
          component: () => import('../views/SalesView.vue'),
          meta: { title: '业务管理', icon: 'business', requiresAuth: true },
          children: [
            moduleRouteMap[8],  // 订单管理
            moduleRouteMap[9],  // 突发事件
            moduleRouteMap[10], // 售后管理
            moduleRouteMap[7],  // 商机管理
          ],
        },
      ],
    },
  ],
})


// 路由守卫，检查用户是否登录
router.beforeEach(async (to, from, next) => {
  if (!to.meta.requiresAuth) {
    // 不需要认证的路由直接放行
    next()
    return
  }
  // 检查用户是否有登录令牌
  const token = localStorage.getItem('crm_token')
    if (!token) {
      // 用户未登录，重定向到登录页面
      next('/login')
      return
    }

  // 权限检查 (已登录状态)
  const permissionStore = usePermissionStore()
  if (to.meta.requiresAuth && permissionStore.accessibleModuleIds.length === 0) {
    // 假设从用户信息中获取roleId
    const userInfoStr = localStorage.getItem('crm_userInfo')
      if (userInfoStr) {
        const userInfo = JSON.parse(userInfoStr)
        if (userInfo.role) {
          await permissionStore.fetchPermissions(userInfo.role) // 加载权限
        }
      }
  }

  // 检查是否有权限访问当前路由
  if (to.meta.moduleId) {
    if (!permissionStore.accessibleModuleIds.includes(Number(to.meta.moduleId))) {
      // 没有权限，重定向到 dashboard 或无权限页面
      next('/dashboard')
      return
    }
  }

  next()
})

export default router
