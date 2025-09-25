<template>
  <div class="emergency-list">
    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="标题">
        <el-input v-model="searchForm.title" placeholder="请输入标题" clearable />
      </el-form-item>
      <el-form-item label="类型">
        <el-select v-model="searchForm.type" placeholder="请选择" clearable style="width: 120px">
          <el-option label="合同问题" value="CONTRACT" />
          <el-option label="付款问题" value="PAYMENT" />
          <el-option label="交付问题" value="DELIVERY" />
          <el-option label="折扣问题" value="DISCOUNT" />
          <el-option label="其他" value="OTHER" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 120px">
          <el-option label="草稿" value="DRAFT" />
          <el-option label="待审批" value="PENDING" />
          <el-option label="已批准" value="APPROVED" />
          <el-option label="已拒绝" value="REJECTED" />
          <el-option label="已解决" value="SOLVED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 列表 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>突发事件列表</span>
          <el-button type="primary" @click="loadData">刷新</el-button>
        </div>
      </template>

      <el-table :data="emergencies" border style="width: 100%">
        <el-table-column prop="emergencyNo" label="编号" width="180">
          <template #default="{ row }">
            <span class="emergency-no">{{ row.emergencyNo }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="180" />
        <el-table-column prop="customerName" label="客户" min-width="120" />
        <el-table-column prop="type" label="类型" min-width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)">
              {{ getTypeName(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" min-width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>


        <el-table-column prop="urgency" label="紧急程度" width="120">
          <template #default="{ row }">
            <el-tag :type="getUrgencyTagType(row.urgency)">
              {{ getUrgencyName(row.urgency) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="riskLevel" label="风险等级" width="120">
          <template #default="{ row }">
            <el-tag :type="getRiskLevelTagType(row.riskLevel)">
              {{ getRiskLevelName(row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="occurTime" label="发生时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
      <el-button size="small" @click="viewDetail(row.id)">查看</el-button>
      <!-- 添加提交按钮，仅在草稿状态时显示 -->
      <el-button
        v-if="row.status === 'DRAFT'"
        size="small"
        type="primary"
        @click="submitForApproval(row)"
      >
        提交
      </el-button>

      <!-- 添加解决按钮，仅在已批准状态时显示 -->
      <el-button
        v-if="row.status === 'APPROVED'"
        size="small"
        type="success"
        @click="markAsSolved(row)"
      >
        标记解决
      </el-button>
    </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 15px; text-align: right"
      />
    </el-card>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="事件详情" width="600px">
      <div v-if="detailData">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="ID">{{ detailData.id }}</el-descriptions-item>
          <el-descriptions-item label="编号">{{ detailData.emergencyNo }}</el-descriptions-item>
          <el-descriptions-item label="标题">{{ detailData.title }}</el-descriptions-item>
          <el-descriptions-item label="客户">{{ detailData.customerName }}</el-descriptions-item>
          <el-descriptions-item label="类型">
            <el-tag :type="getTypeTagType(detailData.type)">
              {{ getTypeName(detailData.type) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTagType(detailData.status)">
              {{ getStatusName(detailData.status) }}
            </el-tag>
          </el-descriptions-item>

          <el-descriptions-item label="紧急程度">
            {{ getUrgencyName(detailData.urgency) }}
          </el-descriptions-item>
          <el-descriptions-item label="风险等级">
            {{ getRiskLevelName(detailData.riskLevel) }}
          </el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">{{
            detailData.description
          }}</el-descriptions-item>
          <el-descriptions-item label="客户需求" :span="2">{{
            detailData.customerRequest
          }}</el-descriptions-item>
          <el-descriptions-item label="解决方案" :span="2">{{
            detailData.solutionContent
          }}</el-descriptions-item>
          <el-descriptions-item label="成本影响" :span="2">{{
            detailData.costImpact
          }}</el-descriptions-item>
          <el-descriptions-item label="发生时间">{{ detailData.occurTime }}</el-descriptions-item>
          <el-descriptions-item label="截止时间">{{ detailData.deadline }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ detailData.createdTime }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ detailData.updatedTime }}</el-descriptions-item>
          <el-descriptions-item label="解决时间">{{ detailData.solvedTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import type { ResponseData } from '@/utils/request'

// 数据
const emergencies = ref<any[]>([])
const searchForm = reactive({ title: '', type: '', status: '' })
const pagination = reactive({ currentPage: 1, pageSize: 10, total: 0 })

// 详情
const detailVisible = ref(false)
const detailData = ref<any>(null)

// 类型映射
const typeMap: Record<string, string> = {
  DISCOUNT: '折扣问题',
  PAYMENT: '付款问题',
  DELIVERY: '交付问题',
  CONTRACT: '合同问题',
  OTHER: '其他',
}

// 状态映射
const statusMap: Record<string, string> = {
  DRAFT: '草稿',
  PENDING: '待审批',
  APPROVED: '已批准',
  REJECTED: '已拒绝',
  SOLVED: '已解决',
}

// 添加紧急程度标签样式函数
const getUrgencyTagType = (urgency: number): string => {
  switch (urgency) {
    case 1: return 'danger'  // 特急 - 红色
    case 2: return 'warning' // 紧急 - 橙色
    case 3: return 'primary' // 普通 - 蓝色
    case 4: return 'info'    // 低 - 灰色
    default: return 'info'
  }
}

// 添加风险等级标签样式函数
const getRiskLevelTagType = (riskLevel: string): string => {
  switch (riskLevel) {
    case 'HIGH': return 'danger'   // 高风险 - 红色
    case 'MEDIUM': return 'warning' // 中风险 - 橙色
    case 'LOW': return 'success'   // 低风险 - 绿色
    default: return 'info'
  }
}

// 紧急程度映射
const urgencyMap: Record<number, string> = {
  1: '特急',
  2: '紧急',
  3: '普通',
  4: '低',
}

// 风险等级映射
const riskLevelMap: Record<string, string> = {
  HIGH: '高',
  MEDIUM: '中',
  LOW: '低',
}

// 获取类型名称
const getTypeName = (type: string): string => {
  return typeMap[type] || type
}

// 获取状态名称
const getStatusName = (status: string): string => {
  return statusMap[status] || status
}

// 获取紧急程度名称
const getUrgencyName = (urgency: number): string => {
  return urgencyMap[urgency] || String(urgency)
}

// 获取风险等级名称
const getRiskLevelName = (riskLevel: string): string => {
  return riskLevelMap[riskLevel] || riskLevel
}

// 获取类型标签样式
const getTypeTagType = (type: string): string => {
  switch (type) {
    case 'DISCOUNT':
      return 'warning'
    case 'PAYMENT':
      return 'danger'
    case 'DELIVERY':
      return 'primary'
    case 'CONTRACT':
      return 'success'
    default:
      return 'info'
  }
}

// 获取状态标签样式
const getStatusTagType = (status: string): string => {
  switch (status) {
    case 'DRAFT':
      return 'info'
    case 'PENDING':
      return 'warning'
    case 'APPROVED':
      return 'success'
    case 'REJECTED':
      return 'danger'
    case 'SOLVED':
      return 'success'
    default:
      return 'info'
  }
}

// 添加提交审批函数
const submitForApproval = async (row: any) => {
  try {
    const userInfo = localStorage.getItem('crm_userInfo')
    const empId = userInfo ? JSON.parse(userInfo).userId : null
    //console.log('empId:', empId)
    if (!empId) {
      ElMessage.error('用户信息获取失败，请重新登录')
      return
    }
    const res = await request.put<ResponseData>(`/api/salesEmergency/${row.id}/submit?empId=${empId}`)
    console.log('提交审批响应:', res)
    if (res.data.code === 200) {
      ElMessage.success('提交成功')
      // 更新本地数据状态
      row.status = 'PENDING'
      // 重新加载数据
      loadData()
    } else {
      ElMessage.error(res.data.message || '提交失败')
    }
  } catch (e) {
    console.error('提交失败:', e)
    ElMessage.error('提交失败: ' + (e.message || '网络错误'))
  }
}

// 添加标记为已解决函数
const markAsSolved = async (row: any) => {
  try {

    const userInfo = localStorage.getItem('crm_userInfo')
    const empId = userInfo ? JSON.parse(userInfo).userId : null
    if (!empId) {
      ElMessage.error('用户信息获取失败，请重新登录')
      return
    }
    // 弹出对话框要求填写解决描述
    const { value } = await ElMessageBox.prompt('请输入解决描述', '标记为已解决', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '请详细描述解决方案和结果'
    })

    // 提交解决状态和描述
    const res = await request.put<ResponseData>(`/api/salesEmergency/${row.id}/solve?empId=${empId}&solutionDescription=${value}`)

    if (res.data.code === 200) {
      ElMessage.success('标记成功')
      // 更新本地数据状态
      row.status = 'SOLVED'
      // 重新加载数据
      loadData()
    } else {
      ElMessage.error(res.data.message || '标记失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      console.error('标记失败:', e)
      ElMessage.error('标记失败: ' + (e.message || '网络错误'))
    }
  }
}

// 加载数据
const loadData = async () => {
  const userInfo = localStorage.getItem('crm_userInfo')
  const userId = userInfo ? JSON.parse(userInfo).userId : null
  console.log('用户ID:', userId)
  try {
    const res = await request.get<ResponseData>('/api/salesEmergency/search', {
      params: {
        pageNum: pagination.currentPage,
        pageSize: pagination.pageSize,
        title: searchForm.title || undefined,
        type: searchForm.type || undefined,
        status: searchForm.status || undefined,
        employeeId: userId,
      },
    })

    console.log('完整响应:', res)
    const data = res.data
    console.log('数据部分:', data)

    if (data.code === 200) {
      // 检查返回的数据结构
      if (data.data && Array.isArray(data.data)) {
        // 如果直接返回数组
        emergencies.value = data.data
        pagination.total = data.total || data.data.length
      } else if (data.data && data.data.list && Array.isArray(data.data.list)) {
        // 如果返回分页对象 {list: [...], total: ...}
        emergencies.value = data.data.list
        pagination.total = data.data.total || data.total || data.data.list.length
      } else {
        emergencies.value = []
        pagination.total = 0
      }

      console.log('处理后的表格数据:', emergencies.value)
      console.log('分页总数:', pagination.total)
    } else {
      ElMessage.error(data.message || '加载事件列表失败')
      emergencies.value = []
      pagination.total = 0
    }
  } catch (e) {
    console.error('加载事件列表失败:', e)
    ElMessage.error('加载事件列表失败: ' + (e.message || '网络错误'))
    emergencies.value = []
    pagination.total = 0
  }
}

const handleSearch = () => {
  pagination.currentPage = 1
  loadData()
}

const handleReset = () => {
  searchForm.title = ''
  searchForm.type = ''
  searchForm.status = ''
  pagination.currentPage = 1
  loadData()
}

const handleSizeChange = (val: number) => {
  pagination.pageSize = val
  pagination.currentPage = 1
  loadData()
}

const handleCurrentChange = (val: number) => {
  pagination.currentPage = val
  loadData()
}

const viewDetail = async (id: number) => {
  try {
    const res = await request.get<ResponseData>(`/api/salesEmergency/${id}`)
    const data = res.data
    if (data.code === 200) {
      detailData.value = data.data
      detailVisible.value = true
    } else {
      ElMessage.error(data.message || '加载事件详情失败')
    }
  } catch (e) {
    console.error('加载事件详情失败:', e)
    ElMessage.error('加载事件详情失败: ' + (e.message || '网络错误'))
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.emergency-list {
  padding: 20px;
}

.search-form {
  margin-bottom: 15px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.emergency-no {
  font-family: 'Courier New', Courier, monospace;
  font-weight: bold;
  color: #409eff;
}
</style>
