<template>
  <div class="leads-container">
    <div class="header">
      <h2>营销管理</h2>
      <el-button type="primary" @click="handleAddActivity">添加活动</el-button>
    </div>

    <!-- 活动搜索区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form" label-position="left">
      <el-form-item label="活动名称">
        <el-input
          v-model="searchForm.activityName"
          placeholder="请输入活动名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="活动状态">
        <el-select
          v-model="searchForm.activityStatus"
          placeholder="请选择活动状态"
          clearable
          size="small"
        >
          <el-option label="筹备中" :value="0" />
          <el-option label="进行中" :value="1" />
          <el-option label="已结束" :value="2" />
          <el-option label="已取消" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchActivities" size="small">搜索</el-button>
        <el-button @click="resetSearch" size="small">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 活动表格 -->
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="营销活动" name="activities">
        <el-table :data="activities" border style="width: 100%" v-loading="loading">
          <el-table-column prop="id" label="ID" width="60" align="center" />
          <el-table-column prop="name" label="活动名称" min-width="150" show-overflow-tooltip />
          <el-table-column prop="type" label="活动类型" width="120" align="center" />
          <el-table-column prop="startTime" label="开始时间" width="160" align="center" />
          <el-table-column prop="endTime" label="结束时间" width="160" align="center" />
          <el-table-column label="活动状态" width="100" align="center">
            <template #default="{ row }">
              <el-tag :type="getActivityStatusType(row.status)">
                {{ getActivityStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="budget" label="预算" width="100" align="center">
            <template #default="{ row }">
              ¥{{ row.budget }}
            </template>
          </el-table-column>
          <el-table-column prop="actualCost" label="实际成本" width="100" align="center">
            <template #default="{ row }">
              ¥{{ row.actualCost }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right" align="center">
            <template #default="{ row }">
              <el-button size="small" @click="handleViewActivity(row)">查看</el-button>
              <el-button size="small" @click="handleEditActivity(row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDeleteActivity(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="推广计划" name="plans">
        <el-table :data="promotionPlans" border style="width: 100%" v-loading="loading">
          <el-table-column prop="id" label="ID" width="60" align="center" />
          <el-table-column prop="planName" label="计划名称" min-width="150" show-overflow-tooltip />
          <el-table-column prop="targetAudience" label="目标受众" width="120" align="center" />
          <el-table-column prop="channel" label="推广渠道" width="120" align="center" />
          <el-table-column prop="startDate" label="开始日期" width="120" align="center" />
          <el-table-column prop="endDate" label="结束日期" width="120" align="center" />
          <el-table-column label="状态" width="100" align="center">
            <template #default="{ row }">
              <el-tag :type="getPlanStatusType(row.status)">
                {{ getPlanStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="budget" label="预算" width="100" align="center">
            <template #default="{ row }">
              ¥{{ row.budget }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right" align="center">
            <template #default="{ row }">
              <el-button size="small" @click="handleViewPlan(row)">查看</el-button>
              <el-button size="small" @click="handleApprovePlan(row)" v-if="row.status === 0">审批</el-button>
              <el-button size="small" type="danger" @click="handleDeletePlan(row)" v-if="row.status !== 1">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 分页 -->
    <el-pagination
      v-if="activeTab === 'activities'"
      v-model:current-page="pagination.current"
      v-model:page-size="pagination.size"
      :total="pagination.total"
      @current-change="handlePageChange"
      layout="total, prev, pager, next"
      class="pagination"
    />

    <!-- 活动对话框 -->
    <el-dialog
      v-model="activityDialogVisible"
      :title="activityDialogTitle"
      width="600px"
      @close="handleActivityDialogClose"
    >
      <el-form :model="currentActivity" label-width="100px">
        <el-form-item label="活动名称" required>
          <el-input v-model="currentActivity.name" />
        </el-form-item>
        <el-form-item label="活动类型">
          <el-input v-model="currentActivity.type" />
        </el-form-item>
        <el-form-item label="开始时间" required>
          <el-date-picker
            v-model="currentActivity.startTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间" required>
          <el-date-picker
            v-model="currentActivity.endTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="预算">
          <el-input v-model.number="currentActivity.budget" type="number">
            <template #append>元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="实际成本">
          <el-input v-model.number="currentActivity.actualCost" type="number">
            <template #append>元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="活动状态">
          <el-select v-model="currentActivity.status" placeholder="请选择活动状态">
            <el-option label="筹备中" :value="0" />
            <el-option label="进行中" :value="1" />
            <el-option label="已结束" :value="2" />
            <el-option label="已取消" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动描述">
          <el-input
            v-model="currentActivity.description"
            type="textarea"
            :rows="3"
            placeholder="请输入活动描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="activityDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveActivity">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 推广计划对话框 -->
    <el-dialog
      v-model="planDialogVisible"
      title="推广计划详情"
      width="600px"
    >
      <el-form :model="currentPlan" label-width="100px">
        <el-form-item label="计划名称">
          <el-input v-model="currentPlan.planName" disabled />
        </el-form-item>
        <el-form-item label="目标受众">
          <el-input v-model="currentPlan.targetAudience" disabled />
        </el-form-item>
        <el-form-item label="推广渠道">
          <el-input v-model="currentPlan.channel" disabled />
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker
            v-model="currentPlan.startDate"
            type="date"
            disabled
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker
            v-model="currentPlan.endDate"
            type="date"
            disabled
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="预算">
          <el-input v-model.number="currentPlan.budget" disabled>
            <template #append>元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="currentPlan.responsiblePerson" disabled />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="currentPlan.status" disabled>
            <el-option label="待审批" :value="0" />
            <el-option label="已批准" :value="1" />
            <el-option label="已拒绝" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划描述">
          <el-input
            v-model="currentPlan.description"
            type="textarea"
            :rows="3"
            disabled
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="planDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog
      v-model="approvalDialogVisible"
      title="推广计划审批"
      width="500px"
    >
      <el-form :model="approvalForm" label-width="80px">
        <el-form-item label="审批结果" required>
          <el-radio-group v-model="approvalForm.status">
            <el-radio :label="1">批准</el-radio>
            <el-radio :label="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见">
          <el-input
            v-model="approvalForm.comments"
            type="textarea"
            :rows="3"
            placeholder="请输入审批意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="approvalDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitApproval">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

// 数据状态
const activities = ref([])
const promotionPlans = ref([])
const loading = ref(false)
const activeTab = ref('activities')

// 搜索表单
const searchForm = ref({
  activityName: '',
  activityStatus: ''
})

// 分页配置
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

// 活动对话框相关
const activityDialogVisible = ref(false)
const activityDialogTitle = ref('添加活动')
const currentActivity = ref({})

// 推广计划对话框相关
const planDialogVisible = ref(false)
const currentPlan = ref({})

// 审批对话框相关
const approvalDialogVisible = ref(false)
const approvalForm = ref({
  id: '',
  status: 1,
  comments: ''
})

// 获取活动状态文本
const getActivityStatusText = (status) => {
  const statusMap = {
    0: '筹备中',
    1: '进行中',
    2: '已结束',
    3: '已取消'
  }
  return statusMap[status] || '未知'
}

// 获取活动状态类型
const getActivityStatusType = (status) => {
  const typeMap = {
    0: 'info',
    1: 'primary',
    2: 'success',
    3: 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取计划状态文本
const getPlanStatusText = (status) => {
  const statusMap = {
    0: '待审批',
    1: '已批准',
    2: '已拒绝'
  }
  return statusMap[status] || '未知'
}

// 获取计划状态类型
const getPlanStatusType = (status) => {
  const typeMap = {
    0: 'warning',
    1: 'success',
    2: 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取所有活动
const getAllActivities = async () => {
  loading.value = true
  try {
    const response = await request.get('/marketActivity/getAll')
    if (response.data.code === 200) {
      console.log(response.data)
      activities.value = response.data
      pagination.value.total = response.data.length
    } else {
      ElMessage.error('获取活动列表失败')
    }
  } catch (error) {
    ElMessage.error('获取活动列表失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

// 获取推广计划
const getPromotionPlans = async () => {
  loading.value = true
  try {
    const response = await request.get('/marketing/getPromotionPlans')
    if (response.data.code === 200) {
      promotionPlans.value = response.data.data
    } else {
      ElMessage.error('获取推广计划失败')
    }
  } catch (error) {
    ElMessage.error('获取推广计划失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

// 搜索活动
const searchActivities = async () => {
  loading.value = true
  try {
    let response
    if (searchForm.value.activityName) {
      response = await request.get('/marketActivity/getByName', {
        params: { name: searchForm.value.activityName }
      })
    } else if (searchForm.value.activityStatus !== '') {
      response = await request.get('/marketActivity/getByStatus', {
        params: { status: searchForm.value.activityStatus }
      })
    } else {
      await getAllActivities()
      return
    }

    if (response.data.code === 200) {
      activities.value = response.data.data
      pagination.value.total = response.data.length
    } else {
      ElMessage.error('搜索活动失败')
    }
  } catch (error) {
    ElMessage.error('搜索活动失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    activityName: '',
    activityStatus: ''
  }
  getAllActivities()
}

// 处理标签页切换
const handleTabChange = (tabName) => {
  if (tabName === 'activities') {
    getAllActivities()
  } else if (tabName === 'plans') {
    getPromotionPlans()
  }
}

// 处理添加活动
const handleAddActivity = () => {
  activityDialogTitle.value = '添加活动'
  currentActivity.value = {
    name: '',
    type: '',
    startTime: '',
    endTime: '',
    budget: 0,
    actualCost: 0,
    status: 0,
    description: ''
  }
  activityDialogVisible.value = true
}

// 处理编辑活动
const handleEditActivity = (row) => {
  activityDialogTitle.value = '编辑活动'
  currentActivity.value = { ...row }
  activityDialogVisible.value = true
}

// 处理查看活动
const handleViewActivity = (row) => {
  activityDialogTitle.value = '查看活动'
  currentActivity.value = { ...row }
  activityDialogVisible.value = true
}

// 处理删除活动
const handleDeleteActivity = (row) => {
  ElMessageBox.confirm(
    `确定要删除活动 "${row.name}" 吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const response = await request.delete('/marketActivity/delete', {
        params: { id: row.id }
      })
      if (response.data.code === 200) {
        ElMessage.success('删除成功')
        getAllActivities()
      } else {
        ElMessage.error(response.data.message || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败: ' + error.message)
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 保存活动
const saveActivity = async () => {
  if (!currentActivity.value.name) {
    ElMessage.warning('请输入活动名称')
    return
  }

  try {
    let response
    if (currentActivity.value.id) {
      // 更新活动
      response = await request.put('/marketActivity/update', currentActivity.value)
    } else {
      // 添加活动
      response = await request.post('/marketActivity/add', currentActivity.value)
    }

    if (response.data.code === 200) {
      ElMessage.success(currentActivity.value.id ? '更新成功' : '添加成功')
      activityDialogVisible.value = false
      getAllActivities()
    } else {
      ElMessage.error(response.data.message || (currentActivity.value.id ? '更新失败' : '添加失败'))
    }
  } catch (error) {
    ElMessage.error((currentActivity.value.id ? '更新失败' : '添加失败') + ': ' + error.message)
  }
}

// 关闭活动对话框
const handleActivityDialogClose = () => {
  currentActivity.value = {}
}

// 处理查看计划
const handleViewPlan = (row) => {
  currentPlan.value = { ...row }
  planDialogVisible.value = true
}

// 处理审批计划
const handleApprovePlan = (row) => {
  approvalForm.value.id = row.id
  approvalForm.value.status = 1
  approvalForm.value.comments = ''
  approvalDialogVisible.value = true
}

// 提交审批
const submitApproval = async () => {
  try {
    const requestData = {
      id: approvalForm.value.id,
      status: approvalForm.value.status,
      approvalComments: approvalForm.value.comments
    }

    const response = await request.put('/marketing/updatePromotionPlans', requestData)

    if (response.data.code === 200) {
      ElMessage.success('审批成功')
      approvalDialogVisible.value = false
      getPromotionPlans()
    } else {
      ElMessage.error(response.data.message || '审批失败')
    }
  } catch (error) {
    ElMessage.error('审批失败: ' + error.data.message)
  }
}

// 处理删除计划
const handleDeletePlan = (row) => {
  ElMessageBox.confirm(
    `确定要删除推广计划 "${row.planName}" 吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const response = await request.delete('/activityReport/delete', {
        params: { id: row.id }
      })
      if (response.data.code === 200) {
        ElMessage.success('删除成功')
        getPromotionPlans()
      } else {
        ElMessage.error(response.data.message || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败: ' + error.message)
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 处理分页变化
const handlePageChange = (page) => {
  pagination.value.current = page
  // 这里可以添加分页逻辑
}

// 初始化加载数据
onMounted(() => {
  getAllActivities()
})
</script>

<style scoped>
.leads-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.header h2 {
  margin: 0;
  color: #1a1a1a;
  font-size: 18px;
  font-weight: 600;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 16px;
  padding: 16px;
  margin-bottom: 24px;
  background-color: #ffffff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.search-form .el-form-item {
  margin-bottom: 0;
  display: flex;
  align-items: center;
  white-space: nowrap;
}

.search-form .el-form-item__label {
  padding-right: 8px;
  margin-bottom: 0;
  white-space: nowrap;
  width: auto;
  text-align: left;
}

.search-form .el-input,
.search-form .el-select {
  width: 200px;
}

.pagination {
  margin-top: 24px;
  padding: 12px 0;
  background-color: #f8fafc;
  border-radius: 8px;
  justify-content: center;
  display: flex;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
}
</style>

