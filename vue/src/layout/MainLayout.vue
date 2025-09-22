<template>
  <el-container style="height: 100vh">
    <!-- 侧边导航栏 -->
    <el-aside width="220px" style="background-color: #304156">
      <div class="logo" style="
          height: 60px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
        ">
        <h2>CRM系统</h2>
      </div>

      <el-menu
        :default-active="activeMenu"
        class="nav-menu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <!-- 只渲染固定的3个父路由 -->
        <template v-for="parentRoute in fixedParentRoutes" :key="parentRoute.path">
          <!-- 父路由菜单 -->
          <el-sub-menu :index="parentRoute.path">
            <template #title>
              <span>{{ parentRoute.meta?.title }}</span>
            </template>
            <!-- 动态渲染有权限的子路由 -->
            <template v-for="childRoute in getAccessibleChildren(parentRoute)" :key="childRoute.path">
              <el-menu-item :index="`${parentRoute.path}/${childRoute.path}`">
                {{ childRoute.meta?.title }}
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
        ">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path" :to="item.path">
            {{ item.title }}
          </el-breadcrumb-item>
        </el-breadcrumb>
        <div class="user-actions">
          <el-button type="text" @click="handleLogout">退出登录</el-button>
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
import { ref, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { House, Promotion, Sell, Phone, Setting } from '@element-plus/icons-vue'
import { usePermissionStore } from '@/stores/permission' // 引入权限存储
import type { RouteRecordRaw } from 'vue-router'
const route = useRoute()
const router = useRouter()
const activeMenu = ref('')
const permissionStore = usePermissionStore()

// 图标映射
const iconMap = {
  house: House,
  promotion: Promotion,
  sell: Sell,
  phone: Phone,
  setting: Setting,
}


// 固定父路由（只保留这3个）
const fixedParentRoutes = computed(() => {
  return router.options.routes.find(route => route.path === '/')?.children?.filter(
    parent => ['/dashboard','/permission', '/operation', '/business'].includes(parent.path)
  ) || []
})


// 过滤出有权限的子路由
const getAccessibleChildren = (parentRoute:  RouteRecordRaw) => {
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
// 替换原有 breadcrumbs 计算属性为以下实现：
const breadcrumbs = computed(() => {
  return route.matched
    .filter((item) => item.meta?.title)
    .map((item) => ({
      path: item.path,
      title: item.meta?.title as string,
    }))
})

const handleLogout = () => {

  localStorage.removeItem('crm_token')
  localStorage.removeItem('crm_userInfo')

  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')

  ElMessage.success('已退出登录')
  router.push('/login')
}
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
}
</style>
