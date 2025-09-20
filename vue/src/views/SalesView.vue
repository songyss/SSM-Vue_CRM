<template>
  <div class="sales">

    <div class="content-area">
      <!-- 这里添加子路由出口 -->
      <RouterView />
    </div>
  </div>
</template>

<style scoped>
.sales {
  padding: 20px;
}

.action-buttons button {
  margin-right: 10px;
  padding: 8px 15px;
  background: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.content-area {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
