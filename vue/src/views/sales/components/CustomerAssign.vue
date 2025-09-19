<template>
  <div class="customer-assign">
    <h3>客户分配</h3>
    <el-form :model="assignForm" label-width="120px">
      <el-form-item label="客户名称">
        <el-input v-model="assignForm.customer" placeholder="请输入客户名称" />
      </el-form-item>
      <el-form-item label="分配给">
        <el-select v-model="assignForm.assignee" placeholder="请选择分配对象">
          <el-option label="销售员A" value="sales1" />
          <el-option label="销售员B" value="sales2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleAssign">确认分配</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'

const assignForm = reactive({
  customer: '',
  assignee: ''
})

const handleAssign = () => {
  console.log('执行分配:', assignForm)
}
</script>

<style scoped>
.customer-assign {
  padding: 20px;
}
</style>