import { createRouter, createWebHistory, type RouteLocationNormalized } from 'vue-router'
import { usePermissionStore } from '@/stores/permission'
import { moduleRouteMap } from '@/utils/moduleRouteMap'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 登录
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: {
        requiresAuth: false,
        layout: 'none', // 不使用主布局
      },
    },

    // 主布局
    {
      path: '/',
      component: () => import('../layout/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        // 默认跳转
        { path: '', redirect: 'dashboard' },

        // 控制台
        {
          path: '/dashboard',
          name: 'dashboard',
          component: () => import('../views/DashboardView.vue'),
          meta: { title: '控制台', icon: 'house', requiresAuth: true },
        },

        // 权限管理
        {
          path: '/permission',
          name: 'permission',
          component: () => import('../views/ConsoleView.vue'),
          meta: { title: '权限管理', icon: 'setting', requiresAuth: true },
          children: [
            moduleRouteMap[1], // 用户管理
            moduleRouteMap[2], // 角色管理
            moduleRouteMap[3], // 部门管理
            moduleRouteMap[4], // 日志管理
          ],
        },

        // 运营管理
        {
          path: '/operation',
          name: 'operation',
          component: () => import('../views/MarketingView.vue'),
          meta: { title: '运营管理', icon: 'operation', requiresAuth: true },
          children: [
            moduleRouteMap[5], // 客户管理
            moduleRouteMap[6], // 活动管理
          ],
        },

        // 业务管理
        {
          path: '/business',
          name: 'business',
          component: () => import('../views/SalesView.vue'),
          meta: { title: '业务管理', icon: 'promotion', requiresAuth: true },
          children: [
            moduleRouteMap[8],  // 订单管理
            moduleRouteMap[9],  // 突发事件
            moduleRouteMap[10], // 售后管理
            moduleRouteMap[7],  // 商机管理

            // 👉 突发事件父容器
            {
              path: 'emergencies',
              name: 'business.emergencies',
              component: () => import('../views/sales/EmergenciesView.vue'),
              meta: { title: '突发事件管理', requiresAuth: true },
              children: [
                {
                  path: 'list',
                  name: 'business.emergencies.list',
                  component: () => import('../views/emergency/EmergencyList.vue'),
                  meta: { title: '事件列表', requiresAuth: true },
                },
                {
                  path: 'report',
                  name: 'business.emergencies.report',
                  component: () => import('../views/emergency/EmergencyReport.vue'),
                  meta: { title: '新增突发事件', requiresAuth: true },
                },
                {
                  path: 'approval',
                  name: 'business.emergencies.approval',
                  component: () => import('../views/emergency/EmergencyApproval.vue'),
                  meta: { title: '突发事件审批', requiresAuth: true },
                },
              ],
            },
          ],
        },

        {
          path: '/formCreate',
          name: 'formCreate',
          component: () => import('../views/FormView.vue'),
          meta: { title: '表单创建', icon: 'edit', requiresAuth: true },

        },


        {
          path: '/about',
          name: 'about',
          component: () => import('../views/AboutView.vue'),
          meta: { title: 'about', icon: 'edit', requiresAuth: true },
        }
      ],
    },
  ],
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  if (!to.meta.requiresAuth) {
    next()
    return
  }

  // 登录校验
  const token = localStorage.getItem('crm_token')
  if (!token) {
    next('/login')
    return
  }

  // 权限校验
  const permissionStore = usePermissionStore()
  if (to.meta.requiresAuth && permissionStore.accessibleModuleIds.length === 0) {
    const userInfoStr = localStorage.getItem('crm_userInfo')
    if (userInfoStr) {
      const userInfo = JSON.parse(userInfoStr)
      if (userInfo.role) {
        await permissionStore.fetchPermissions(userInfo.role)
      }
    }
  }

  if (to.meta.moduleId) {
    if (!permissionStore.accessibleModuleIds.includes(Number(to.meta.moduleId))) {
      next('/dashboard')
      return
    }
  }

  next()
})

export default router
