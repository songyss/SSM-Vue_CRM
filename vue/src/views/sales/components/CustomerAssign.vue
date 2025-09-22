<template>
  <div class="customer-assign">
    <h3>客户分配</h3>

    <!-- 待分配客户表格 -->
    <el-table :data="customers" border style="width: 100%">
      <el-table-column prop="name" label="客户姓名" width="120" />
      <el-table-column prop="phone" label="手机号" width="150" />
      <el-table-column prop="sex" label="性别" width="80">
        <template #default="{ row }">
          {{ row.sex === 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="source" label="客户来源" width="120" />
      <el-table-column label="操作" width="220">
        <template #default="{ row }">
          <!-- 分配下拉框 -->
          <el-select
            v-model="selectedAssignee[row.id]"
            placeholder="请选择分配对象"
            style="width: 150px"
          >
            <el-option
              v-for="emp in employees"
              :key="emp.id"
              :label="emp.name"
              :value="emp.id"
            />
          </el-select>
          <el-button type="primary" size="small" @click="assign(row.id)">
            分配
          </el-button>
        </template>
      </el-table-column>
    </el-table>

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
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 客户数据
const customers = ref<any[]>([])
// 员工列表
const employees = ref<any[]>([])
// 每个客户选择的员工
const selectedAssignee = reactive<Record<number, number | null>>({})
// 客户详情对话框
const dialogVisible = ref(false)
const detail = reactive<any>({})

// 获取待分配客户（assigneeId为空 且 isPool=0）
const fetchCustomers = async () => {
  try {
    const res = await axios.get('http://localhost:8080/customer/unAssigned', {
      params: { status: 1 } // 有意向客户
    })
    customers.value = res.data
  } catch (e) {
    console.error('获取客户失败:', e)
  }
}

// 获取员工
const fetchEmployees = async () => {
  try {
    const res = await axios.get('http://localhost:8080/employee/allList')
    employees.value = res.data.data
  } catch (e) {
    console.error('获取员工失败:', e)
  }
}

// 分配客户
const assign = async (customerId: number) => {
  const employeeId = selectedAssignee[customerId]
  if (!employeeId) {
    ElMessage.warning('请选择分配对象')
    return
  }
  try {
    await axios.post('http://localhost:8080/customer/assign', {
      employeeId,
      customerIds: [customerId]
    })
    ElMessage.success('分配成功')
    fetchCustomers()
  } catch (e) {
    console.error('分配失败:', e)
    ElMessage.error('分配失败')
  }
}

// 查看客户详情
const showDetail = (row: any) => {
  Object.assign(detail, row)
  dialogVisible.value = true
}

onMounted(() => {
  fetchCustomers()
  fetchEmployees()
})
</script>

<style scoped>
.customer-assign {
  padding: 20px;
}
</style>
