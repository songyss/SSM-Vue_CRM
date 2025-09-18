import { createRouter, createWebHistory, type RouteLocationNormalized } from 'vue-router'

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
        //权限管理
        {
          path: '/permission',
          name: 'permission',
          component: () => import('../views/ConsoleView.vue'),
          meta: { title: '权限管理', icon: 'setting', requiresAuth: true },
          children: [
            {
              path: 'roles',
              name: 'permission.roles',
              component: () => import('../views/console/RolesView.vue'),
              meta: { title: '角色管理', requiresAuth: true },
              parentPath: '/permission',
            },
            {
              path: 'departments',
              name: 'permission.departments',
              component: () => import('../views/console/UsersView.vue'),
              meta: { title: '部门管理', requiresAuth: true },
              parentPath: '/permission',
            },
            {
              path: 'users',
              name: 'permission.users',
              component: () => import('../views/console/UsersView.vue'),
              meta: { title: '用户管理', requiresAuth: true },
              parentPath: '/permission',
            },
            {
              path: 'logs',
              name: 'permission.logs',
              component: () => import('../views/console/UsersView.vue'),
              meta: { title: '系统日志', requiresAuth: true },
              parentPath: '/permission',
            },
          ],
        },
        //运营管理
        {
          path: '/operation',
          name: 'operation',
          component: () => import('../views/MarketingView.vue'),
          meta: { title: '运营管理', icon: 'promotion', requiresAuth: true },
          children: [
            {
              path: 'customers',
              name: 'operation.customers',
              component: () => import('../views/sales/CustomersView.vue'),
              meta: { title: '客户管理', requiresAuth: true },
              parentPath: '/operation',
            },
            {
              path: 'campaigns',
              name: 'operation.campaigns',
              component: () => import('../views/marketing/CampaignsView_temp.vue'),
              meta: { title: '营销活动', requiresAuth: true },
              parentPath: '/operation',
            },
          ],
        },
        //业务管理
        {
          path: '/business',
          name: 'business',
          component: () => import('../views/SalesView.vue'),
          meta: { title: '业务管理', icon: 'sell', requiresAuth: true },
          children: [
            {
              path: 'orders',
              name: 'business.orders',
              component: () => import('../views/sales/OrdersView.vue'),
              meta: { title: '订单管理', requiresAuth: true },
              parentPath: '/business',
            },
            {
              path: 'emergencies',
              name: 'business.emergencies',
              component: () => import('../views/telesales/CallsView.vue'),
              meta: { title: '突发事件', requiresAuth: true },
              parentPath: '/business',
            },
            {
              path: 'aftersales',
              name: 'business.aftersales',
              component: () => import('../views/telesales/TasksView.vue'),
              meta: { title: '售后管理', requiresAuth: true },
              parentPath: '/business',
            },
            {
              path: 'opportunities',
              name: 'business.opportunities',
              component: () => import('../views/marketing/LeadsView.vue'),
              meta: { title: '商机管理', requiresAuth: true },
              parentPath: '/business',
            },
          ],
        },
      ],
    },
  ],
})

//路由守卫，检查用户是否登录
// 路由守卫，检查用户是否登录
router.beforeEach((to: RouteLocationNormalized, from, next) => {
  if (to.meta.requiresAuth) {
    // 检查用户是否有登录令牌
    const token = localStorage.getItem('crm_token')
    if (!token) {
      // 用户未登录，重定向到登录页面
      next('/login')
    } else {
      // 用户已登录，允许访问
      next()
    }
  } else {
    // 不需要认证的路由直接放行
    next()
  }
})

export default router
