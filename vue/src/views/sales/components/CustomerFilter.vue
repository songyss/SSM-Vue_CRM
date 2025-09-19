<template>
  <div class="customer-filter">
    <el-card class="filter-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="筛选状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="全部状态" 
            clearable
            style="width: 200px"
          >
            <el-option label="未联系" value="pending" />
            <el-option label="无意向" value="contacted" />
            <el-option label="有意向" value="interested" />
            <el-option label="信息有误" value="not_interested" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">筛选</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-table :data="filteredCustomers" border style="width: 100%">
      <el-table-column prop="name" label="客户姓名" width="150" />
      <el-table-column prop="phone" label="手机号" width="150" />
      <el-table-column prop="source" label="客户来源" width="120" />
      <el-table-column label="备注" width="200">
        <template #default="scope">
          <el-input 
            v-model="scope.row.sdr_notes" 
            placeholder="请输入备注"
          />
        </template>
      </el-table-column>
      <el-table-column label="客户意向" width="180">
        <template #default="scope">
          <el-select 
            v-model="scope.row.sdr_status" 
            placeholder="请选择状态"
          >
            <el-option label="未联系" value="pending" />
            <el-option label="无意向" value="contacted" />
            <el-option label="有意向" value="interested" />
            <el-option label="信息有误" value="not_interested" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="scope">
          <el-button 
            size="small" 
            type="primary" 
            @click="handleSave(scope.row)"
          >保存</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

interface Customer {
  id: number
  name: string
  phone: string
  source: string
  sdr_notes: string
  sdr_status: string
  saving?: boolean
}

const customerData = ref<Customer[]>([])
const filteredCustomers = ref<Customer[]>([])
const loading = ref(false)

const statusMap = {
  'pending': 0,
  'contacted': 1,
  'interested': 2,
  'not_interested': 3
}

const reverseStatusMap = {
  0: 'pending',
  1: 'contacted',
  2: 'interested',
  3: 'not_interested'
}

const loadCustomerData = async () => {
  try {
    loading.value = true
    const response = await request.get('/customer/allList')
    customerData.value = response.data.map((item: any) => ({
      ...item,
      sdr_notes: item.sdrNotes || '',
      sdr_status: reverseStatusMap[item.sdrStatus] || 'pending',
    }))
    filteredCustomers.value = [...customerData.value]
    ElMessage.success('获取客户数据成功')
  } catch (error) {
    ElMessage.error('获取客户数据失败')
    console.error('获取客户数据失败', error)
  } finally {
    loading.value = false
  }
}

const searchForm = reactive({
  name: '',
  status: ''
})

const handleSearch = () => {
  filteredCustomers.value = customerData.value.filter(customer => {
    const nameMatch = customer.name.includes(searchForm.name)
    let statusMatch = true
    
    if (searchForm.status) {
      const statusValue = statusMap[searchForm.status as keyof typeof statusMap]
      statusMatch = customer.sdr_status === statusValue
    }
    
    return nameMatch && statusMatch
  })
}

const handleSave = async (row: Customer) => {
  try {
    row.saving = true
    const updateData = {
      id: row.id,
      sdrStatus: row.sdr_status,
      sdrNotes: row.sdr_notes
    }
    await request.patch('/customer/updateCustomerSdrStatus', updateData)
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
    console.error('保存失败:', error)
  } finally {
    row.saving = false
  }
}

onMounted(() => {
  loadCustomerData()
})
</script>

<style scoped>
.customer-filter {
  padding: 15px;
}

.filter-bar {
  margin-bottom: 15px;
}

.filter-bar .el-form-item {
  margin-bottom: 0;
  margin-right: 15px;
}

.filter-bar .el-form-item:last-child {
  margin-right: 0;
}
</style>