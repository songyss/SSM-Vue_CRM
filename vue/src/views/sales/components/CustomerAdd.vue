<template>
  <div class="customer-add">
    <h3>新增客户</h3>
    <el-form :model="customerForm" label-width="120px">
      <el-form-item label="客户名称" required>
        <el-input v-model="customerForm.name" placeholder="请输入客户名称" />
      </el-form-item>
      <el-form-item label="客户类型" required>
        <el-select v-model="customerForm.type" placeholder="请选择客户类型">
          <el-option label="企业客户" value="enterprise" />
          <el-option label="个人客户" value="individual" />
        </el-select>
      </el-form-item>
      <el-form-item label="联系人" required>
        <el-input v-model="customerForm.contact" placeholder="请输入联系人" />
      </el-form-item>
      <el-form-item label="联系电话" required>
        <el-input v-model="customerForm.phone" placeholder="请输入联系电话" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'

const customerForm = reactive({
  name: '',
  type: '',
  contact: '',
  phone: ''
})

const submitForm = () => {
  if (!customerForm.name || !customerForm.type || !customerForm.contact || !customerForm.phone) {
    ElMessage.error('请填写完整信息')
    return
  }
  console.log('提交客户信息:', customerForm)
  ElMessage.success('客户添加成功')
}

const resetForm = () => {
  customerForm.name = ''
  customerForm.type = ''
  customerForm.contact = ''
  customerForm.phone = ''
}
</script>

<style scoped>
.customer-add {
  padding: 20px;
}
</style>