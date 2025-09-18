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
      <el-menu :default-active="activeMenu" class="nav-menu" background-color="#304156" text-color="#bfcbd9"
        active-text-color="#409EFF" router>
        <!-- 菜单内容保持不变 -->
        <template v-for="route in $router.options.routes" :key="route.path">
          <!-- 有子路由的菜单项 -->
          <template v-if="route.path === '/'">
            <template v-for="child in route.children" :key="child.path">
              <el-sub-menu v-if="child.children && child.children.length" :index="child.path">
                <template #title>
                  <span>{{ child.meta?.title }}</span>
                </template>
                <el-menu-item v-for="grandChild in child.children" :key="grandChild.path"
                  :index="`${child.path}/${grandChild.path}`">
                  {{ grandChild.meta?.title }}
                </el-menu-item>
              </el-sub-menu>

              <!-- 无子路由的菜单项 -->
              <el-menu-item v-else :index="child.path">
                <span>{{ child.meta?.title }}</span>
              </el-menu-item>
            </template>
          </template>
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

const route = useRoute()
const router = useRouter()
const activeMenu = ref('')

// 图标映射
const iconMap = {
  house: House,
  promotion: Promotion,
  sell: Sell,
  phone: Phone,
  setting: Setting,
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
