<template>
  <div class="customer-assign">
    <h3>客户分配</h3>

    <!-- 工具栏 -->
    <div style="margin-bottom: 15px; display: flex; gap: 10px; align-items: center;">
      <el-button type="primary" @click="refresh" v-if="permissionStore.hasButtonPermission('/customer/unAssignedList')">刷新</el-button>
      <el-select
        v-model="batchAssignee"
        placeholder="请选择分配对象"
        style="width: 200px"
        clearable
      >
        <el-option
          v-for="emp in employees"
          :key="emp.id"
          :label="emp.name"
          :value="emp.id"
        />
      </el-select>
      <el-button
        type="success"
        @click="batchAssign"
        :disabled="!batchAssignee || selectedCustomers.length === 0"
        v-if="permissionStore.hasButtonPermission('/customer/assign')"
      >
        批量分配 (已选 {{ selectedCustomers.length }} 项)
      </el-button>
    </div>

    <!-- 待分配客户表格 -->
    <el-table
      :data="customers"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="name" label="客户姓名" width="120" />
      <el-table-column prop="phone" label="手机号" width="150" />
      <el-table-column prop="sex" label="性别" width="80">
        <template #default="{ row }">
          {{ row.sex === 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="source" label="客户来源" width="120" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="showDetail(row)">
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        @current-change="fetchCustomers"
        @size-change="handleSizeChange"
      />
    </div>

    <!-- 查看客户详情对话框 -->
    <el-dialog v-model="dialogVisible" title="客户详情" width="50%">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">{{ detail.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detail.sex === 1 ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.phone }}</el-descriptions-item>
        <el-descriptions-item label="来源">{{ detail.source }}</el-descriptions-item>
        <el-descriptions-item label="公司">{{ detail.company }}</el-descriptions-item>
        <el-descriptions-item label="职位">{{ detail.position }}</el-descriptions-item>
        <el-descriptions-item label="最后跟进时间">{{ detail.lastFollowUPTime }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detail.notes }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { usePermissionStore } from '@/stores/permission'
const permissionStore = usePermissionStore()
import request from '@/utils/request'

// 客户数据
const customers = ref<any[]>([])
// 员工列表
const employees = ref<any[]>([])
// 批量分配选择的员工
const batchAssignee = ref<number | null>(null)
// 选中的客户
const selectedCustomers = ref<number[]>([])
// 客户详情对话框
const dialogVisible = ref(false)
const detail = reactive<any>({})
// 定时器 ID
let refreshTimer: number | null = null

// 分页参数
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 获取待分配客户（assignee_id = null）
const fetchCustomers = async () => {
  try {
    const res = await request.get('/customer/unAssignedList', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value
      }
    })
    customers.value = res.data.data?.list || res.data.data || []
    total.value = res.data.data?.total || customers.value.length
  } catch (e) {
    console.error('获取客户失败:', e)
  }
}

// 获取员工
const fetchEmployees = async () => {
  try {
    const res = await request.get('/employee/allList')
    employees.value = res.data.data
  } catch (e) {
    console.error('获取员工失败:', e)
  }
}

// 处理表格选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedCustomers.value = selection.map(item => item.id)
}

// 批量分配客户
const batchAssign = async () => {
  if (!batchAssignee.value) {
    ElMessage.warning('请选择分配对象')
    return
  }

  if (selectedCustomers.value.length === 0) {
    ElMessage.warning('请至少选择一个客户')
    return
  }

  try {
    await request.post('/customer/assign', {
      employeeId: batchAssignee.value,
      customerIds: selectedCustomers.value
    })
    ElMessage.success('批量分配成功')
    // 清空选择
    batchAssignee.value = null
    selectedCustomers.value = []
    // 刷新列表
    fetchCustomers()
  } catch (e) {
    console.error('批量分配失败:', e)
    ElMessage.error('批量分配失败')
  }
}

// 查看客户详情
const showDetail = (row: any) => {
  Object.assign(detail, row)
  dialogVisible.value = true
}

// 刷新按钮方法
const refresh = () => {
  pageNum.value = 1
  fetchCustomers()
}

// 处理分页大小变化
const handleSizeChange = (val: number) => {
  pageSize.value = val
  pageNum.value = 1
  fetchCustomers()
}

onMounted(() => {
  fetchCustomers()
  fetchEmployees()

  // 自动刷新，每 30 秒执行一次
  refreshTimer = window.setInterval(() => {
    fetchCustomers()
  }, 30000)
})

onUnmounted(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
  }
})
</script>

<style scoped>
.customer-assign {
  padding: 20px;
}

.operation-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
