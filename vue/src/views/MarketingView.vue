<template>
  <div class="marketing">
    <div class="content-area">
      <RouterView />
    </div>
  </div>
</template>

<style scoped>
.marketing {
  padding: 20px;
}

.content-area {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
