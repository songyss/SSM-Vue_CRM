<template>
  <div class="customer-pool">
    <h3>客户池</h3>
    <el-table :data="poolData" border style="width: 100%">
      <el-table-column prop="name" label="客户名称" width="180" />
      <el-table-column prop="type" label="客户类型" width="120" />
      <el-table-column prop="contact" label="联系人" width="120" />
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button type="text" size="small" @click="handleAssign(scope.row)">分配</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const poolData = ref([
  { name: '客户A', type: '企业', contact: '张三' },
  { name: '客户B', type: '个人', contact: '李四' }
])

const handleAssign = (row: any) => {
  console.log('分配客户:', row)
}
</script>

<style scoped>
.customer-pool {
  padding: 20px;
}
</style>