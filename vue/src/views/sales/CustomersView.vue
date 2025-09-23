<template>
  <div class="customers-container">
    <div class="page-header">
      <h1 class="page-title">
        <el-icon><User /></el-icon>
        <span>客户管理</span>
      </h1>
    </div>

    <div class="customer-layout">
      <!-- 左侧菜单 -->
      <el-menu
        class="customer-menu"
        :default-active="activeTab"
        @select="handleMenuSelect"
        background-color="#f8fafc"
        active-text-color="#409eff"
      >
        <el-menu-item index="filter">
          <el-icon><Search /></el-icon>
          <span>客户筛选</span>
        </el-menu-item>
        <el-menu-item index="assign">
          <el-icon><Share /></el-icon>
          <span>客户分配</span>
        </el-menu-item>
        <el-menu-item index="pool">
          <el-icon><Box /></el-icon>
          <span>客户池</span>
        </el-menu-item>
        <el-menu-item index="add">
          <el-icon><Plus /></el-icon>
          <span>新增客户</span>
        </el-menu-item>
        <el-menu-item index="list">
          <el-icon><User /></el-icon>
          <span>客户详情</span>
        </el-menu-item>
      </el-menu>

      <!-- 右侧内容区域 -->
      <div class="customer-content">
        <CustomerFilter v-if="activeTab === 'filter'" />
        <CustomerAssign v-if="activeTab === 'assign'" />
        <CustomerPool v-if="activeTab === 'pool'" />
        <CustomerAdd v-if="activeTab === 'add'" />
        <CustomerList v-if="activeTab === 'list'" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Search, Share, Box, Plus, User } from '@element-plus/icons-vue'
import CustomerFilter from './components/CustomerFilter.vue'
import CustomerAssign from './components/CustomerAssign.vue'
import CustomerPool from './components/CustomerPool.vue'
import CustomerAdd from './components/CustomerAdd.vue'
import CustomerList from './components/CustomerList.vue'  // ✅ 新增导入

onMounted(() => {
  console.log('CustomersView组件已加载')
  console.log('当前activeTab:', activeTab.value)
  console.log('Token:', localStorage.getItem('crm_token'))
})

const activeTab = ref('filter')

const handleMenuSelect = (index: string) => {
  activeTab.value = index
}
</script>

<style scoped>
.customers-container {
  padding: 20px;
}

.page-header {
  padding: 15px 20px;
  background: #ffffff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
  border-radius: 4px;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: #2c3e50;
  display: flex;
  align-items: center;
  gap: 8px;
}

.customer-layout {
  display: flex;
  min-height: 500px;
}

.customer-menu {
  width: 220px;
  border-right: none;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  margin-right: 20px;
}

.customer-content {
  flex: 1;
  padding: 15px;
  background: #ffffff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}
</style>
