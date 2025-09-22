<template>
  <el-container style="height: 100vh">
    <!-- 侧边导航栏 -->
    <el-aside
      width="220px"
      style="background-color: #304156"
      :class="{ 'collapsed': isCollapsed }"
    >
      <div class="logo" style="
          height: 60px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
        ">
        <h2 v-if="!isCollapsed">CRM系统</h2>
        <h2 v-else style="font-size: 20px">CRM</h2>
      </div>

      <el-menu
        :default-active="activeMenu"
        class="nav-menu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
        :collapse="isCollapsed"
        :collapse-transition="false"
      >
        <!-- 单独处理 Dashboard 菜单项 -->
        <el-menu-item index="/dashboard">
          <el-icon><House /></el-icon>
          <span v-if="!isCollapsed">控制台</span>
        </el-menu-item>

        <!-- 只渲染固定的3个父路由 -->
        <template v-for="parentRoute in fixedParentRoutes" :key="parentRoute.path">
          <!-- 父路由菜单 -->
          <el-sub-menu :index="parentRoute.path">
            <template #title>
              <el-icon v-if="parentRoute.meta?.icon">
                <component :is="iconMap[parentRoute.meta?.icon as keyof typeof iconMap]" />
              </el-icon>
              <span v-if="!isCollapsed">{{ parentRoute.meta?.title }}</span>
            </template>
            <!-- 动态渲染有权限的子路由 -->
            <template v-for="childRoute in getAccessibleChildren(parentRoute)" :key="childRoute.path">
              <el-menu-item :index="`${parentRoute.path}/${childRoute.path}`">
                <el-icon v-if="childRoute.meta?.icon">
                  <component :is="iconMap[childRoute.meta.icon as keyof typeof iconMap]" />
                </el-icon>
                <span v-if="!isCollapsed">{{ childRoute.meta?.title }}</span>
                <!-- 折叠状态下使用title属性显示完整文字 -->
                <template v-if="isCollapsed" #title>
                  {{ childRoute.meta?.title }}
                </template>
              </el-menu-item>
            </template>
          </el-sub-menu>
        </template>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 顶部导航栏 -->
      <el-header style="
          height: auto;
          padding: 10px 20px;
          border-bottom: 1px solid #eee;
          display: flex;
          justify-content: space-between;
          align-items: center;
          background-color: 3C8DBC;
        ">
        <div class="header-left">
          <el-button
            @click="toggleSidebar"
            link
            :icon="isCollapsed ? Expand : Fold"
            style="font-size: 20px; margin-right: 15px; "
          />
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path" :to="item.path">
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="user-actions">
          <!-- 添加欢迎文字 -->
          <span class="welcome-text" v-if="userInfo" >欢迎您：{{ userInfo.name }}</span>
          <el-button type="text" @click="handleLogout" style="margin-left: 15px;">退出登录</el-button>
        </div>
      </el-header>

      <!-- 主内容区 -->
      <el-main style="padding: 20px; background-color: #f0f2f5">
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, watch, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  House,
  Promotion,
  Phone,
  Setting,
  Operation,
  Coin,
  User,
  Avatar,
  Connection,
  Management,
  Menu,
  Notification,
  Calendar,
  ShoppingBag,
  Headset,
  Expand,
  Fold
} from '@element-plus/icons-vue'
import { usePermissionStore } from '@/stores/permission'
import type { RouteRecordRaw } from 'vue-router'

const route = useRoute()
const router = useRouter()
const activeMenu = ref('')
const permissionStore = usePermissionStore()

// 控制侧边栏折叠状态
const isCollapsed = ref(false)

// 用户信息
const userInfo = ref<{
  id: number
  name: string
  roleId: number
  department: number
} | null>(null)

// 图标映射
const iconMap = {
  house: House,
  promotion: Promotion,
  headset : Headset,
  phone: Phone,
  setting: Setting,
  operation:Operation,
  bussiness:Coin,
  coin:Coin,
  user:User,
  avatar:Avatar,
  connection:Connection,
  notification:Notification,
  calendar:Calendar,
  shoppingBag:ShoppingBag,
  menu:Menu,
  management:Management,
}

// 切换侧边栏折叠状态
const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}

// 固定父路由（只保留这3个，排除/dashboard）
const fixedParentRoutes = computed(() => {
  return router.options.routes.find(route => route.path === '/')?.children?.filter(
    parent => ['/permission', '/operation', '/business'].includes(parent.path)
  ) || []
})

// 过滤出有权限的子路由
const getAccessibleChildren = (parentRoute: RouteRecordRaw) => {
  // 如果没有子路由，直接返回空
  if (!parentRoute.children) return []

  // 过滤条件：子路由的moduleId在权限列表中
  return parentRoute.children.filter((child:RouteRecordRaw) => {
    // 子路由必须配置了moduleId，且在accessibleModuleIds中
    return (
      typeof child.meta?.moduleId=== 'number' &&
      permissionStore.accessibleModuleIds.includes(child.meta.moduleId)
    )
  })
}

// 监听路由变化
watch(
  () => route.path,
  (newPath) => {
    activeMenu.value = newPath
  },
  { immediate: true },
)

// 生成面包屑导航
const breadcrumbs = computed(() => {
  return route.matched
    .filter((item) => item.meta?.title)
    .map((item) => ({
      path: item.path,
      title: item.meta?.title as string,
    }))
})

// 获取用户信息
const getUserInfo = () => {
  const userInfoStr = localStorage.getItem('crm_userInfo')
  if (userInfoStr) {
    try {
      userInfo.value = JSON.parse(userInfoStr)
    } catch (e) {
      console.error('解析用户信息失败:', e)
      userInfo.value = null
    }
  }
}

// 退出登录
const handleLogout = () => {
  localStorage.removeItem('crm_token')
  localStorage.removeItem('crm_userInfo')
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')

  ElMessage.success('已退出登录')
  router.push('/login')
}

// 组件挂载时获取用户信息
onMounted(() => {
  getUserInfo()
})
</script>

<style scoped>
/* 保持原有的样式 */
.nav-menu :deep(.el-menu-item .el-icon),
.nav-menu :deep(.el-sub-menu__title .el-icon),
.nav-menu :deep(.el-menu-item [class*='el-icon']),
.nav-menu :deep(.el-sub-menu__title [class*='el-icon']) {
  font-size: 18px !important;
  margin-right: 8px !important;
  width: 18px !important;
  height: 18px !important;
  vertical-align: middle !important;
}

.user-actions {
  margin-left: auto;
  display: flex;
  align-items: center;
}

/* 添加欢迎文字样式 */
.welcome-text {
  color: #606266;
  font-size: 14px;
}

/* 折叠状态样式 */
.collapsed {
  width: 64px !important;
}

.header-left {
  display: flex;
  align-items: center;
}

/* 折叠状态下菜单项的样式 */
.nav-menu :deep(.el-menu--popup) .el-menu-item {
  display: flex;
  align-items: center;
}

.nav-menu :deep(.el-menu--popup) .el-menu-item .el-icon {
  margin-right: 8px !important;
}
</style>
