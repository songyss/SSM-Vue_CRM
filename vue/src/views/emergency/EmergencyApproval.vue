<template>
  <div class="emergency-approval">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>突发事件审批</span>
          <el-button type="primary" @click="loadPendingEmergencies">刷新</el-button>
        </div>
      </template>

      <el-table :data="pendingEmergencies" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" width="180" />
        <el-table-column prop="type" label="类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)">
              {{ getTypeName(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="customerName" label="客户" width="120" />
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
        <el-table-column label="操作" width="280">
          <template #default="{ row }">
            <el-button size="small" @click="viewDetail(row)">查看</el-button>
            <el-button size="small" type="success" @click="approve(row)">批准</el-button>
            <el-button size="small" type="danger" @click="reject(row)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="事件详情" width="700px">
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
            <el-tag :type="getUrgencyTagType(detailData.urgency)">
              {{ getUrgencyName(detailData.urgency) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="getRiskLevelTagType(detailData.riskLevel)">
              {{ getRiskLevelName(detailData.riskLevel) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">{{ detailData.description }}</el-descriptions-item>
          <el-descriptions-item label="客户需求" :span="2">{{ detailData.customerRequest }}</el-descriptions-item>
          <el-descriptions-item label="解决方案" :span="2">{{ detailData.solutionContent }}</el-descriptions-item>
          <el-descriptions-item label="成本影响" :span="2">{{ detailData.costImpact }}</el-descriptions-item>
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

    <!-- 拒绝原因弹窗 -->
    <el-dialog v-model="rejectDialogVisible" title="拒绝原因" width="500px">
      <el-form :model="rejectForm">
        <el-form-item label="拒绝原因">
          <el-input
            v-model="rejectForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请输入拒绝原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmReject">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue' // 添加 reactive 导入
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import type { ResponseData } from '@/utils/request'

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
    case 'DISCOUNT': return 'warning'
    case 'PAYMENT': return 'danger'
    case 'DELIVERY': return 'primary'
    case 'CONTRACT': return 'success'
    default: return 'info'
  }
}

// 获取状态标签样式
const getStatusTagType = (status: string): string => {
  switch (status) {
    case 'DRAFT': return 'info'
    case 'PENDING': return 'warning'
    case 'APPROVED': return 'success'
    case 'REJECTED': return 'danger'
    case 'SOLVED': return 'success'
    default: return 'info'
  }
}

// 获取紧急程度标签样式
const getUrgencyTagType = (urgency: number): string => {
  switch (urgency) {
    case 1: return 'danger'  // 特急 - 红色
    case 2: return 'warning' // 紧急 - 橙色
    case 3: return 'primary' // 普通 - 蓝色
    case 4: return 'info'    // 低 - 灰色
    default: return 'info'
  }
}

// 获取风险等级标签样式
const getRiskLevelTagType = (riskLevel: string): string => {
  switch (riskLevel) {
    case 'HIGH': return 'danger'   // 高风险 - 红色
    case 'MEDIUM': return 'warning' // 中风险 - 橙色
    case 'LOW': return 'success'   // 低风险 - 绿色
    default: return 'info'
  }
}

// 使用 ref 定义响应式数据
const pendingEmergencies = ref<any[]>([])
const detailVisible = ref(false)
const detailData = ref<any>(null)

// 拒绝相关
const rejectDialogVisible = ref(false)
const rejectForm = reactive({ reason: '' })
const currentRejectRow = ref<any>(null)

// 加载待审批列表
const loadPendingEmergencies = async () => {
  try {
    const res = await request.get<ResponseData>('/api/salesEmergency/pending')
    if (res.data.code === 200) {
      pendingEmergencies.value = res.data.data || []
      console.log('加载待审批列表:', pendingEmergencies.value)
    } else {
      ElMessage.error(res.data.message || '加载待审批列表失败')
    }
  } catch (e: any) {
    console.error('加载待审批列表失败:', e)
    ElMessage.error('加载待审批列表失败: ' + (e.message || '网络错误'))
  }
}

// 查看详情
const viewDetail = (row: any) => {
  detailData.value = row
  detailVisible.value = true
}

// 批准
const approve = async (row: any) => {
  try {

    const userInfo = localStorage.getItem('crm_userInfo')
    const managerId = userInfo ? JSON.parse(userInfo).userId : null

    if (!managerId) {
      ElMessage.error('未检测到登录用户信息')
      return
    }
    await ElMessageBox.confirm('确定要批准该突发事件吗？', '确认批准', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res = await request.put<ResponseData>(`/api/salesEmergency/${row.id}/approve?managerId=${managerId}`)
    if (res.data.code === 200) {
      ElMessage.success('批准成功')
      // 从列表中移除
      pendingEmergencies.value = pendingEmergencies.value.filter(item => item.id !== row.id)
    } else {
      ElMessage.error(res.data.message || '批准失败')
    }
  } catch (e: any) {
    if (e !== 'cancel') {
      console.error('批准失败:', e)
      ElMessage.error('批准失败: ' + (e.message || '网络错误'))
    }
  }
}

// 拒绝
const reject = (row: any) => {
  currentRejectRow.value = row
  rejectForm.reason = ''
  rejectDialogVisible.value = true
}

// 确认拒绝
const confirmReject = async () => {
  if (!currentRejectRow.value) return

  try {

    const userInfo = localStorage.getItem('crm_userInfo')
    const managerId = userInfo ? JSON.parse(userInfo).userId : null
    console.log('managerId:', managerId)
    if (!managerId) {
      ElMessage.error('未检测到登录用户信息')
      return
    }
    const res = await request.put<ResponseData>(`/api/salesEmergency/${currentRejectRow.value.id}/reject?managerId=${managerId}&reason=${rejectForm.reason}`)

    if (res.data.code === 200) {
      ElMessage.success('拒绝成功')
      // 从列表中移除
      pendingEmergencies.value = pendingEmergencies.value.filter(item => item.id !== currentRejectRow.value.id)
      rejectDialogVisible.value = false
    } else {
      ElMessage.error(res.data.message || '拒绝失败')
    }
  } catch (e: any) {
    console.error('拒绝失败:', e)
    ElMessage.error('拒绝失败: ' + (e.message || '网络错误'))
  }
}

onMounted(() => {
  loadPendingEmergencies()
})
</script>

<style scoped>
.emergency-approval {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
