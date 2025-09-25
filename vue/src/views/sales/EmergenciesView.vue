<template>
  <div class="emergencies-view">
    <!-- 页面头部 -->
    <div class="page-header">
      <h2 class="page-title">
        <el-icon><Warning /></el-icon>
        <span>突发事件</span>
      </h2>
    </div>

    <div class="emergency-layout">
      <!-- 左侧菜单 -->
      <el-menu
        class="side-menu"
        :default-active="activeMenu"
        router
        background-color="#f8fafc"
        active-text-color="#409eff"
      >
        <el-menu-item index="/business/emergencies/list">
          <el-icon><List /></el-icon>
          <span>事件列表</span>
        </el-menu-item>

        <el-menu-item index="/business/emergencies/report">
          <el-icon><Plus /></el-icon>
          <span>新增报告</span>
        </el-menu-item>

        <el-menu-item index="/business/emergencies/approval">
          <el-icon><Check /></el-icon>
          <span>审批处理</span>
        </el-menu-item>
      </el-menu>

      <!-- 右侧内容：子路由出口 -->
      <div class="emergency-content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import { List, Plus, Check, Warning } from '@element-plus/icons-vue'

const route = useRoute()

// 当前激活菜单
const activeMenu = computed(() => route.path)
</script>

<style scoped>
.emergencies-view {
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

.emergency-layout {
  display: flex;
  min-height: 600px;
}

.side-menu {
  width: 220px;
  border-right: none;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  margin-right: 20px;
}

.emergency-content {
  flex: 1;
  padding: 20px;
  background: #ffffff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}
</style>
