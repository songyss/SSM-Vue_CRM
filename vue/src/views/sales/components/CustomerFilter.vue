<template>
  <div class="customer-filter">
    <el-card class="filter-bar">
      <div class="filter-container">
        <el-form :inline="true" :model="searchForm" class="compact-form">
          <!-- 客户姓名 -->
          <el-form-item label="姓名">
            <el-input
              v-model="searchForm.name"
              placeholder="姓名"
              clearable
              size="small"
              style="width: 90px"
            />
          </el-form-item>

          <!-- 手机号 -->
          <el-form-item label="手机号">
            <el-input
              v-model="searchForm.phone"
              placeholder="手机号"
              clearable
              size="small"
              style="width: 110px"
            />
          </el-form-item>

          <!-- 客户来源 -->
          <el-form-item label="来源">
            <el-input
              v-model="searchForm.source"
              placeholder="来源"
              clearable
              size="small"
              style="width: 90px"
            />
          </el-form-item>

          <!-- 筛选状态 -->
          <el-form-item label="状态">
            <el-select
              v-model="searchForm.status"
              placeholder="状态"
              clearable
              size="small"
              style="width: 100px"
            >
              <el-option label="未联系" value="pending" />
              <el-option label="无意向" value="contacted" />
              <el-option label="有意向" value="interested" />
              <el-option label="信息有误" value="not_interested" />
            </el-select>
          </el-form-item>

          <!-- 操作按钮 -->
          <el-form-item>
            <el-button type="primary" @click="handleSearch" size="small" icon="Search" v-if="permissionStore.hasButtonPermission('/customer/allList')">筛选</el-button>
            <el-button @click="handleReset" size="small" icon="Refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 表格 -->
    <el-table :data="paginatedCustomers" border style="width: 100%" v-loading="loading">
      <el-table-column prop="name" label="客户姓名" width="120" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="source" label="客户来源" width="120" />
      <el-table-column label="备注" width="200">
        <template #default="scope">
          <el-input
            v-model="scope.row.sdr_notes"
            placeholder="请输入备注"
            size="small"
          />
        </template>
      </el-table-column>
      <el-table-column label="客户意向" width="150">
        <template #default="scope">
          <el-select
            v-model="scope.row.sdr_status"
            placeholder="请选择状态"
            size="small"
            style="width: 100%"
          >
            <el-option label="未联系" value="pending" />
            <el-option label="无意向" value="contacted" />
            <el-option label="有意向" value="interested" />
            <el-option label="信息有误" value="not_interested" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="scope">
          <el-button
            size="small"
            type="primary"
            @click="handleSave(scope.row)"
            :loading="scope.row.saving"
            v-if="permissionStore.hasButtonPermission('/customer/updateCustomerSdrStatus')"
          >保存</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        background
        layout="total, prev, pager, next, sizes, jumper"
        :total="filteredCustomers.length"
        :page-size="pageSize"
        :current-page="currentPage"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

import { usePermissionStore } from '@/stores/permission'
const permissionStore = usePermissionStore()

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

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)

const paginatedCustomers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredCustomers.value.slice(start, start + pageSize.value)
})

const statusMap: { [key: string]: number } = {
  'pending': 1,
  'contacted': 2,
  'interested': 3,
  'not_interested': 4
}

const reverseStatusMap: { [key: number]: string } = {
  1: 'pending',
  2: 'contacted',
  3: 'interested',
  4: 'not_interested'
}

const searchForm = reactive({
  name: '',
  phone: '',
  source: '',
  status: ''
})

const loadCustomerData = async () => {
  try {
    loading.value = true
    const response = await request.get('/customer/allList')
    console.log('API响应数据:', response)

    const result = response.data
    const data =result.data

    if (!Array.isArray(data)) {
      throw new Error('API返回数据格式不正确')
    }

    customerData.value = data.map((item: any) => {
      const sdrStatusValue = item.sdrStatus !== undefined ? item.sdrStatus : (item.sdr_status !== undefined ? item.sdr_status : 1)

      return {
        id: item.id || 0,
        name: item.name || '',
        phone: item.phone || '',
        source: item.source || '',
        sdr_notes: item.sdrNotes || item.sdr_notes || '',
        sdr_status: reverseStatusMap[sdrStatusValue] || 'pending',
        saving: false
      }
    })

    filteredCustomers.value = [...customerData.value]
    ElMessage.success('获取客户数据成功')
  } catch (error) {
    ElMessage.error('获取客户数据失败: ' + (error instanceof Error ? error.message : '未知错误'))
    console.error('获取客户数据失败', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  if (!customerData.value || customerData.value.length === 0) {
    filteredCustomers.value = []
    return
  }

  filteredCustomers.value = customerData.value.filter(customer => {
    const nameMatch = searchForm.name
      ? customer.name && customer.name.toLowerCase().includes(searchForm.name.toLowerCase())
      : true
    const phoneMatch = searchForm.phone
      ? customer.phone && customer.phone.includes(searchForm.phone)
      : true
    const sourceMatch = searchForm.source
      ? customer.source && customer.source.toLowerCase().includes(searchForm.source.toLowerCase())
      : true
    const statusMatch = searchForm.status
      ? customer.sdr_status === searchForm.status
      : true

    return nameMatch && phoneMatch && sourceMatch && statusMatch
  })

  // 搜索时重置到第一页
  currentPage.value = 1

  ElMessage.success(`找到 ${filteredCustomers.value.length} 条匹配结果`)
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.phone = ''
  searchForm.source = ''
  searchForm.status = ''
  filteredCustomers.value = [...customerData.value]
  currentPage.value = 1
  ElMessage.info('已重置筛选条件')
}

const handleSave = async (row: Customer) => {
  try {
    row.saving = true
    const sdrStatusValue = Number(statusMap[row.sdr_status as keyof typeof statusMap]) || 1

    const updateData = {
      id: row.id,
      sdrStatus: sdrStatusValue,
      sdrNotes: row.sdr_notes || ''
    }

    console.log('发送更新数据:', updateData)
    await request.patch('/customer/updateCustomerSdrStatus', updateData)
    ElMessage.success('保存成功')
  } catch (error: any) {
    ElMessage.error(error.message || '保存失败')
    console.error('保存失败:', error)
  } finally {
    row.saving = false
  }
}

// 分页事件
const handlePageChange = (page: number) => {
  currentPage.value = page
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
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

.filter-container {
  display: flex;
  align-items: center;
}

.compact-form {
  display: flex;
  flex-wrap: nowrap;
  align-items: center;
  gap: 8px;
}

.compact-form :deep(.el-form-item) {
  margin-bottom: 0;
  margin-right: 0;
}

.compact-form :deep(.el-form-item__label) {
  padding-right: 4px;
  width: auto;
  font-size: 12px;
}

.result-info {
  margin-top: 15px;
  text-align: right;
}

.pagination-container {
  margin-top: 15px;
  text-align: right;
}
</style>
