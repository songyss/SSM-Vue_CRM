<template>
  <RouterView v-slot="{ Component, route }">
    <!-- 如果路由需要布局，则使用主布局包裹 -->
    <template v-if="route.meta.layout !== 'none'">
      <MainLayout>
        <Component :is="Component" />
      </MainLayout>
    </template>
    <!-- 登录页等不需要布局的页面直接渲染 -->
    <template v-else>
      <Component :is="Component" />
    </template>
  </RouterView>
</template>

<script setup lang="ts">
import MainLayout from './layout/MainLayout.vue'
</script>

<style scoped>
/* 可以清空原有样式或保留必要的全局样式 */
</style>
