<template>
  <div class="telesales">
    <h1>电销部管理</h1>
    <div class="action-buttons">
      <button>拨号列表</button>
      <button>通话记录</button>
    </div>

    <div class="content-area">
      <!-- 这里添加子路由出口 -->
      <RouterView />
    </div>
  </div>
</template>

<style scoped>
.telesales {
  padding: 20px;
}

.action-buttons {
  margin-bottom: 20px;
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
