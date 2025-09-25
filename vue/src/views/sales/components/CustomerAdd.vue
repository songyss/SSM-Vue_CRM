<template>
  <div class="customer-add">
    <h3>新增客户</h3>
    <el-form :model="customerForm" label-width="120px">
      <!-- 姓名 -->
      <el-form-item label="姓名" required>
        <el-input v-model="customerForm.name" placeholder="请输入姓名" />
      </el-form-item>

      <!-- 性别 -->
      <el-form-item label="性别" required>
        <el-select v-model="customerForm.sex" placeholder="请选择性别">
          <el-option label="男" :value="1" />
          <el-option label="女" :value="0" />
        </el-select>
      </el-form-item>

      <!-- 手机号 -->
      <el-form-item label="手机号" required prop="phone">
        <el-input
          v-model="customerForm.phone"
          placeholder="请输入手机号"
          @blur="validatePhone"
          :class="{ 'is-error': phoneError }"
        />
        <!-- <div v-if="phoneError" class="phone-error">{{ phoneError }}</div> -->
      </el-form-item>

      <!-- 出生日期 -->
      <el-form-item label="出生日期">
        <el-date-picker
          v-model="customerForm.borndate"
          type="date"
          placeholder="请选择出生日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>

      <!-- 公司名称 -->
      <el-form-item label="公司名称">
        <el-input v-model="customerForm.company" placeholder="请输入公司名称" />
      </el-form-item>

      <!-- 职位 -->
      <el-form-item label="职位">
        <el-input v-model="customerForm.position" placeholder="请输入职位" />
      </el-form-item>

      <!-- 客户来源 -->
      <el-form-item label="客户来源">
        <el-input v-model="customerForm.source" placeholder="请输入客户来源" />
      </el-form-item>

      <!-- 状态 -->
      <el-form-item label="状态" required>
        <el-select v-model="customerForm.status" placeholder="请选择状态">
          <el-option label="潜在客户" :value="1" />
          <el-option label="有意向" :value="2" />
          <el-option label="无意向" :value="3" />
          <el-option label="信息有误" :value="4" />
        </el-select>
      </el-form-item>

      <!-- 操作按钮 -->
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-if="permissionStore.hasButtonPermission('/customer/addCustomer')">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request' // 你自己封装的 axios
import { usePermissionStore } from '@/stores/permission'
const permissionStore = usePermissionStore()

// 表单对象
const customerForm = reactive({
  name: '',
  sex: null, // 改成数字，避免后端报错
  phone: '',
  borndate: '',
  company: '',
  position: '',
  source: '',
  status: null
})

// 手机号错误信息
const phoneError = ref('')

// 验证手机号格式
const validatePhone = () => {
  const phone = customerForm.phone
  phoneError.value = ''

  if (!phone) {
    phoneError.value = '请输入手机号'
    return false
  }

  // 中国大陆手机号正则表达式
  const phoneRegex = /^1[3-9]\d{9}$/

  if (!phoneRegex.test(phone)) {
    phoneError.value = '请输入正确的手机号格式'
    return false
  }

  return true
}

// 提交表单
const submitForm = async () => {
  if (!customerForm.name || customerForm.sex === null || !customerForm.phone || customerForm.status === null) {
    ElMessage.error('请填写完整的必填信息')
    return
  }

  // 提交前再次验证手机号
  if (!validatePhone()) {
    ElMessage.error('手机号格式不正确')
    return
  }

  try {
    // ✅ 注意接口路径要和后端一致
    const res = await request.post('/customer/addCustomer', customerForm)
    if (res.data.code === 200) {
      ElMessage.success('客户添加成功')
      resetForm()
    } else {
      ElMessage.error(res.message || '添加失败')
    }
  } catch (error) {
    console.error('提交客户失败:', error)
    ElMessage.error('服务器请求失败，请稍后重试')
  }
}

// 重置表单
const resetForm = () => {
  Object.keys(customerForm).forEach((key) => {
    customerForm[key] = (key === 'sex' || key === 'status') ? null : ''
  })
  // 清空手机号错误信息
  phoneError.value = ''
}
</script>

<style scoped>
.customer-add {
  padding: 20px;
  max-width: 600px;
  background: #fff;
  border-radius: 8px;
}

.phone-error {
  color: #f56c6c;
  font-size: 12px;
  line-height: 1;
  padding-top: 4px;
  position: absolute;
  top: 100%;
  left: 0;
}

:deep(.el-form-item.is-error .el-input__inner) {
  border-color: #f56c6c;
}
</style>
