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
        //市场部路由
        {
          path: '/marketing',
          name: 'marketing',
          component: () => import('../views/MarketingView.vue'),
          meta: { title: '市场部', icon: 'promotion', requiresAuth: true },
          children: [
            {
              path: 'campaigns',
              name: 'marketing.campaigns',
              component: () => import('../views/marketing/CampaignsView_temp.vue'),
              meta: { title: '营销活动', requiresAuth: true },
              // parentPath: '/marketing',
            },
            {
              path: 'leads',
              name: 'marketing.leads',
              component: () => import('../views/marketing/LeadsView.vue'),
              meta: { title: '潜在客户', requiresAuth: true },
              parentPath: '/marketing',
            },
          ],
        },
        {
          path: '/sales',
          name: 'sales',
          component: () => import('../views/SalesView.vue'),
          meta: { title: '销售部', icon: 'sell' },
          children: [
            {
              path: 'orders',
              name: 'sales.orders',
              component: () => import('../views/sales/OrdersView.vue'),
              meta: { title: '订单管理', requiresAuth: true },
              parentPath: '/sales',
            },
            {
              path: 'customers',
              name: 'sales.customers',
              component: () => import('../views/sales/CustomersView.vue'),
              meta: { title: '客户管理', requiresAuth: true },
              parentPath: '/sales',
            },
            {
              path: 'invoices',
              name: 'sales.invoices',
              component: () => import('../views/sales/InvoicesView.vue'),
              meta: { title: '发票管理', requiresAuth: true },
              parentPath: '/sales',
            },
          ],
        },
        {
          path: '/telesales',
          name: 'telesales',
          component: () => import('../views/TeleSalesView.vue'),
          meta: { title: '电销部', icon: 'phone', requiresAuth: true },
          children: [
            {
              path: 'calls',
              name: 'telesales.calls',
              component: () => import('../views/telesales/CallsView.vue'),
              meta: { title: '通话记录', requiresAuth: true },
              parentPath: '/telesales',
            },
            {
              path: 'tasks',
              name: 'telesales.tasks',
              component: () => import('../views/telesales/TasksView.vue'),
              meta: { title: '任务管理', requiresAuth: true },
              parentPath: '/telesales',
            },
            // 新增话术管理子路由
            {
              path: 'scripts',
              name: 'telesales.scripts',
              component: () => import('../views/telesales/ScriptView.vue'),
              meta: { title: '话术管理', requiresAuth: true },
              parentPath: '/telesales',
            },
          ],
        },
        {
          path: '/console',
          name: 'console',
          component: () => import('../views/ConsoleView.vue'),
          meta: { title: '系统控制台', icon: 'setting', requiresAuth: true },
          // 新增系统控制台子路由
          children: [
            {
              path: 'users',
              name: 'console.users',
              component: () => import('../views/console/UsersView.vue'),
              meta: { title: '用户管理', requiresAuth: true },
              parentPath: '/console',
            },
            {
              path: 'roles',
              name: 'console.roles',
              component: () => import('../views/console/RolesView.vue'),
              meta: { title: '角色管理', requiresAuth: true },
              parentPath: '/console',
            },
          ],
        },
      ],
    },
  ],
})

//路由守卫，检查用户是否登录
<<<<<<< HEAD
// 路由守卫，检查用户是否登录
=======
>>>>>>> 0e5380c6c0bb9606823c793a4ff3b5bb711a6a04
router.beforeEach((to: RouteLocationNormalized, from, next) => {
  if (to.meta.requiresAuth) {
    // 检查用户是否有登录令牌
    const token = localStorage.getItem('crm_token')
    if (!token) {
<<<<<<< HEAD
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
=======
      return '/login' // 用户未登录，重定向到登录页面
    }
  }
  next()
>>>>>>> 0e5380c6c0bb9606823c793a4ff3b5bb711a6a04
})

export default router
