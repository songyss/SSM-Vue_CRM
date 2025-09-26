import type { RouteRecordRaw } from 'vue-router'

// 模块ID与路由的映射关系（需与后端模块ID对应）
export const moduleRouteMap: Record<number, RouteRecordRaw>  = {
  1: {
    path: 'users',
    name: 'permission.users',
    component: () => import('../views/console/UsersView.vue'),
    meta: {
      title: '用户管理',
      icon: 'user',
      permission: 'employee:module',
      moduleId: 1
    },
  },
  2: {
    path: 'roles',
    name: 'permission.roles',
    component: () => import('../views/console/RolesView.vue'),
    meta: {
      title: '角色管理',
      icon: 'connection',
      permission: 'role:module',
      moduleId: 2
    },
  },
  3: {
    path: 'departments',
    name: 'permission.departments',
    component: () => import('../views/console/DepartmentsView.vue'),
    meta: {
      title: '部门管理',
      icon: 'menu',
      permission: 'department:module',
      moduleId: 3
    },
  },
  4: {
    path: 'logs',
    name: 'permission.logs',
    component: () => import('../views/console/LogsView.vue'),
    meta: {
      title: '日志管理',
      icon: 'notification',
      permission: 'log:module',
      moduleId: 4
    },
  },
  5: {
    path: 'customers',
    name: 'operation.customers',
    component: () => import('../views/sales/CustomersView.vue'),
    meta: {
      title: '客户管理',
      icon: 'avatar',
      permission: 'customer:module',
      moduleId: 5
    },
  },
  6: {
    path: 'campaigns',
    name: 'operation.campaigns',
    component: () => import('../views/marketing/CampaignsView_temp.vue'),
    meta: {
      title: '活动管理',
      icon: 'calendar',
      permission: 'campaign:module',
      moduleId: 6
    },
  },
  7: {
    path: 'opportunities',
    name: 'business.opportunities',
    component: () => import('../views/marketing/LeadsView.vue'),
    meta: {
      title: '商机管理',
      icon: 'coin',
      permission: 'opportunity:module',
      moduleId: 7
    },
  },
  8: {
    path: 'orders',
    name: 'business.orders',
    component: () => import('../views/sales/OrdersView.vue'),
    meta: {
      title: '订单管理',
      icon: 'shoppingBag',
      permission: 'order:module',
      moduleId: 8
    },
  },
  9: {
    path: 'emergencies',
    name: 'business.emergencies',
    component: () => import('../views/sales/EmergenciesView.vue'),
    meta: {
      title: '突发事件',
      icon: 'management',
      permission: 'emergency:module',
      moduleId: 9
    },
  },
  10: {
    path: 'aftersales',
    name: 'business.aftersales',
    component: () => import('../views/sales/AfterSalesView.vue'),
    meta: {
      title: '售后管理',
      icon: 'headset',
      permission: 'aftersale:module',
      moduleId: 10
    },
  },


}
