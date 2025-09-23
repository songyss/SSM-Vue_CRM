<template>
  <div class="after-sales-container">
    <div class="header">
      <h2>售后订单管理</h2>
    </div>

    <!-- 搜索区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form" label-position="left">
      <el-form-item label="订单号" label-width="65px">
        <el-input
          v-model="searchForm.orderNumber"
          placeholder="输入订单号"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="订单状态" label-width="65px">
        <el-select
          v-model="searchForm.status"
          placeholder="请选择"
          clearable
          size="small"
        >
          <el-option label="待处理" value="1"/>
          <el-option label="处理中" value="2"/>
          <el-option label="已完成" value="3"/>
          <el-option label="已取消" value="4"/>
          <el-option label="已驳回" value="5"/>
        </el-select>
      </el-form-item>
      <el-form-item label="申请时间" label-width="65px">
        <el-date-picker
          v-model="searchForm.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          size="small"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchAfterSales" size="small">搜索</el-button>
      </el-form-item>
    </el-form>

    <!-- 售后订单表格 -->
    <el-table :data="afterSales" border class="after-sales-table">
      <el-table-column prop="id" label="ID" width="50" align="center"/>
      <el-table-column prop="orderNumber" label="订单号" width="150" align="center" fixed="left"/>
      <el-table-column prop="customerName" label="客户名称" width="90" align="center"/>
      <el-table-column prop="totalAmount" label="金额" width="120" align="center">
        <template #default="{ row }">
          ¥{{ parseFloat(row.totalAmount).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="signedDate" label="签约日期" width="120" align="center"/>

      <!-- 状态列，使用下拉选框并根据状态显示不同颜色 -->
      <el-table-column label="状态" width="180" align="center">
        <template #default="{ row }">
          <el-select
            :model-value="getStatusValue(row)"
            placeholder="选择状态"
            size="small"
            :class="['status-select', `status-select--${getStatusValue(row)}`]"
            @change="(value) => handleStatusChange(row, value)"
            :disabled="[3, 4, 5].includes(getStatusValue(row))"
          >
            <el-option
              v-for="option in statusOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </template>
      </el-table-column>

      <el-table-column prop="afterSaleApplyTime" label="申请时间" width="180" align="center"/>
      <el-table-column prop="afterSaleCompleteTime" label="完成时间" width="180" align="center"/>
      <el-table-column prop="afterSaleHandlerId" label="处理人ID" width="100" align="center"/>
      <el-table-column prop="notes" label="订单备注" width="200" show-overflow-tooltip align="center"/>
      <el-table-column label="操作" width="80" fixed="right" align="center">
        <template #default="{ row }">
          <el-button size="small" @click="handleDetail(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="pagination.current"
      v-model:page-size="pagination.size"
      :total="pagination.total"
      @current-change="fetchAfterSales"
      layout="total, prev, pager, next"
      class="pagination"
    />

    <!-- 处理售后订单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="处理售后订单"
      width="500px"
      @close="onDialogClose"
    >
      <el-form :model="currentOrder" label-width="100px" class="dialog-form">
        <el-form-item label="ID">
          <el-input v-model="currentOrder.id" disabled/>
        </el-form-item>
        <el-form-item label="订单号">
          <el-input v-model="currentOrder.orderNumber" disabled/>
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="currentOrder.customerName" disabled/>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select
            v-model="currentOrder.statusName"
            placeholder="请选择处理状态"
          >
            <el-option
              v-for="option in statusOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="处理备注">
          <el-input
            v-model="currentOrder.afterSaleNotes"
            type="textarea"
            placeholder="请输入处理备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancelUpdate">取消</el-button>
          <el-button
            type="primary"
            @click="updateOrderStatus"
          >
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 订单详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="订单详情" width="600px">
      <el-form label-width="120px" label-position="left" class="detail-form">
        <el-row>
          <el-col :span="12">
            <el-form-item label="ID:">
              <span>{{ detailOrder.id }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单号:">
              <span>{{ detailOrder.orderNumber }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="客户名称:">
              <span>{{ detailOrder.customerName }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单金额:">
              <span>¥{{ parseFloat(detailOrder.totalAmount).toFixed(2) }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="签约日期:">
              <span>{{ detailOrder.signedDate }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态:">
              <el-tag :type="getStatusTagType(detailOrder.statusName)">
                {{ getStatusText(getStatusValueFromText(detailOrder.statusName)) }}
              </el-tag>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="申请时间:">
              <span>{{ detailOrder.afterSaleApplyTime }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="完成时间:">
              <span>{{ detailOrder.afterSaleCompleteTime || '未完成' }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="处理人ID:">
              <span>{{ detailOrder.afterSaleHandlerId || '未分配' }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="订单备注:">
          <span>{{ detailOrder.notes || '无' }}</span>
        </el-form-item>
        <el-form-item label="合同文件:">
          <span v-if="detailOrder.fileUrl">
            <a :href="detailOrder.fileUrl" target="_blank">查看文件</a>
          </span>
          <span v-else>无</span>
        </el-form-item>
        <el-form-item label="创建时间:">
          <span>{{ detailOrder.createTime }}</span>
        </el-form-item>
        <el-form-item label="更新时间:">
          <span>{{ detailOrder.updateTime }}</span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { useRouter } from 'vue-router'

// 获取路由实例
const router = useRouter()

// 售后订单数据
const afterSales = ref([])

// 搜索表单
const searchForm = ref({
  orderNumber: '',
  status: '',
  dateRange: []
})

// 分页配置
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

// 对话框相关
const dialogVisible = ref(false)
const currentOrder = ref({})

// 详情对话框
const detailDialogVisible = ref(false)
const detailOrder = ref({})

// 定义状态选项
const statusOptions = [
  { label: '待处理', value: 1 },
  { label: '处理中', value: 2 },
  { label: '已完成', value: 3 },
  { label: '已取消', value: 4 },
  { label: '已驳回', value: 5 }
]

// 搜索售后订单
const searchAfterSales = async () => {
  pagination.value.current = 1
  await fetchAfterSales()
}

// 获取售后订单列表
const fetchAfterSales = async () => {
  try {
    console.log('开始获取售后订单数据...')

    let response;

    // 如果有任何搜索条件，使用组合查询接口
    if (searchForm.value.orderNumber || searchForm.value.status ||
      (searchForm.value.dateRange && searchForm.value.dateRange.length === 2)) {

      const params: any = {}

      if (searchForm.value.orderNumber) {
        params.orderNumber = searchForm.value.orderNumber.trim()
      }

      if (searchForm.value.status) {
        params.afterSaleStatus = searchForm.value.status
      }

      if (searchForm.value.dateRange && searchForm.value.dateRange.length === 2) {
        params.startDate = searchForm.value.dateRange[0]
        params.endDate = searchForm.value.dateRange[1]
      }

      try {
        response = await request.get('/afterSaleOrders/getAfterSaleOrderByCondition', { params })
      } catch (error) {
        console.error('组合查询失败:', error)
        ElMessage.error('查询失败: ' + (error.message || '网络错误'))
        return
      }
    } else {
      try {
        response = await request.get('/afterSaleOrders/getAfterSaleOrderByStatus', {
          params: { afterSaleStatus: 1 }
        })
      } catch (error) {
        ElMessage.error('查询默认数据失败: ' + (error.message || '网络错误'))
        return
      }
    }

    const data = response.data || response

    if (data) {
      if (Array.isArray(data)) {
        afterSales.value = data
        pagination.value.total = data.length
        ElMessage.success('数据加载成功')
      } else if (data.id) {
        afterSales.value = [data]
        pagination.value.total = 1
        ElMessage.success('数据加载成功')
      } else {
        afterSales.value = []
        pagination.value.total = 0
        ElMessage.info('暂无售后订单数据')
      }
    } else {
      afterSales.value = []
      pagination.value.total = 0
      ElMessage.info('暂无售后订单数据')
    }

    console.log('设置的afterSales数据:', afterSales.value)
  } catch (error) {
    console.error('请求异常:', error)

    // 特别处理未登录的情况
    if (error.message && error.message.includes('未登录')) {
      ElMessage.error('请先登录')
      router.push('/login')
      return
    }

    if (error.message && error.message.includes('Token')) {
      ElMessage.error('身份验证已过期，请重新登录')
      localStorage.removeItem('crm_token')
      router.push('/login')
      return
    }

    ElMessage.error('请求失败: ' + (error.message || '未知错误'))

    afterSales.value = []
    pagination.value.total = 0
  }
}

// 状态标签样式
const getStatusTagType = (status) => {
  const map = {
    1: 'danger',
    2: 'warning',
    3: 'success',
    4: 'info',
    5: 'info'
  }
  return map[status] || ''
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    1: '待处理',
    2: '处理中',
    3: '已完成',
    4: '已取消',
    5: '已驳回'
  }
  return statusMap[status] || '未知状态'
}

// 从状态文本获取状态值
const getStatusValueFromText = (statusText) => {
  const statusMap = {
    '待处理': 1,
    '处理中': 2,
    '已完成': 3,
    '已取消': 4,
    '已驳回': 5
  }
  return statusMap[statusText] || statusText
}

// 查看详情
const handleDetail = (row) => {
  detailOrder.value = { ...row }
  detailDialogVisible.value = true
}

// 保存原始订单状态的映射
const originalStatusMap = ref(new Map())
const tempStatusMap = ref(new Map())

// 获取当前显示的状态值（优先使用临时值）
const getStatusValue = (row) => {
  if (tempStatusMap.value.has(row.orderNumber)) {
    return tempStatusMap.value.get(row.orderNumber)
  }
  const statusMap = {
    '待处理': 1,
    '处理中': 2,
    '已完成': 3,
    '已取消': 4,
    '已驳回': 5
  }
  return statusMap[row.statusName] || row.statusName
}

// 处理状态变更
const handleStatusChange = (row, value) => {
  const status = getStatusValue(row)
  if ([3, 4, 5].includes(status)) {
    ElMessage.warning('已完成、已取消或已驳回的订单状态不能修改')
    return
  }

  if (!originalStatusMap.value.has(row.orderNumber)) {
    originalStatusMap.value.set(row.orderNumber, row.statusName)
  }

  // 保存临时状态值
  tempStatusMap.value.set(row.orderNumber, value)
  currentOrder.value = { ...row, statusName: value }
  dialogVisible.value = true
}

// 取消更新
const cancelUpdate = () => {
  // 清除临时状态值
  if (currentOrder.value.orderNumber) {
    tempStatusMap.value.delete(currentOrder.value.orderNumber)
    originalStatusMap.value.delete(currentOrder.value.orderNumber)
  }

  // 清除当前订单引用
  currentOrder.value = {}
  dialogVisible.value = false
}

// 处理对话框关闭事件
const onDialogClose = () => {
  cancelUpdate()
}

// 更新订单状态
const updateOrderStatus = async () => {
  if (!currentOrder.value.statusName && currentOrder.value.statusName !== 0) {
    ElMessage.warning('请选择处理状态')
    return
  }

  try {
    const response = await request.get('/afterSaleOrders/updateAfterSaleOrderStatus', {
      params: {
        orderNumber: currentOrder.value.orderNumber,
        afterSaleStatus: currentOrder.value.statusName
      }
    })

    if (response && (response.code === 200 || response.code === 0)) {
      ElMessage.success(response.message || '处理成功')
      dialogVisible.value = false
      if (currentOrder.value.orderNumber) {
        tempStatusMap.value.delete(currentOrder.value.orderNumber)
        originalStatusMap.value.delete(currentOrder.value.orderNumber)
      }
      fetchAfterSales()
    } else {
      ElMessage.error(response.message || '处理失败')
    }
  } catch (error) {
    console.error('完整错误:', error)
    if (error.message && error.message.includes('未登录')) {
      ElMessage.error('请先登录')
      router.push('/login')
    } else if (error.message && error.message.includes('Token')) {
      ElMessage.error('身份验证已过期，请重新登录')
      localStorage.removeItem('crm_token')
      router.push('/login')
    } else {
      ElMessage.error('处理失败: ' + (error.message || '网络错误'))
    }
  }
}

// 初始化加载数据
onMounted(() => {
  console.log('页面初始化，开始获取售后订单数据')
  fetchAfterSales()
})
</script>

<style scoped>
/* 容器样式 */
.after-sales-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

/* 头部样式 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px 0;
  background-color: #ffffff;
  border-radius: 8px;
}

.header h2 {
  margin: 0;
  color: #1a1a1a;
  font-size: 18px;
  font-weight: 600;
  padding-bottom: 0;
}

/* 搜索表单样式 */
.search-form {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 16px;
  padding: 16px;
  margin-bottom: 24px;
  background-color: #ffffff;
  border-radius: 4px;
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
  width: 180px;
  min-width: 180px;
}

.search-form .el-date-picker {
  width: 240px;
  min-width: 240px;
}

.search-form .el-form-item:last-child {
  margin-left: auto;
}

/* 确保日期选择器不换行 */
.search-form .el-date-editor--daterange {
  display: flex;
  white-space: nowrap;
}

/* 表格样式 */
.after-sales-table {
  width: 100%;
  margin-top: 16px;
  border: 1px solid #f0f2f7;
  border-radius: 4px;
  overflow: hidden;
  transition: all 0.2s ease;
}

.after-sales-table :deep(.el-table__row) {
  height: 78px;
  transition: all 0.4s cubic-bezier(0.2, 0.8, 0.2, 1);
}

.after-sales-table :deep(.el-table__row:hover) {
  background-color: #f8fafc;
  transform: translateY(-6px);
  box-shadow: 0 14px 32px rgba(58, 87, 232, 0.28);
  z-index: 1;
  position: relative;
}

.el-table :deep(.el-table__cell) {
  padding: 24px 0;
  border-bottom: 1px solid #f0f2f7;
  transition: all 0.2s ease;
}

.after-sales-table :deep(.el-table__row--striped) {
  background-color: #fafbfe;
}

.after-sales-table :deep(.el-table__row--striped:hover) {
  background-color: #f5f7ff;
}

.after-sales-table :deep(.el-table__header th) {
  background-color: #f8fafc;
  color: #5a6d8a;
  font-weight: 600;
  font-size: 14px;
  letter-spacing: 0.5px;
}

/* 状态选择器样式 - 线框风格 */
.status-select {
  width: 100%;
}

.status-select :deep(.el-input__inner) {
  border: 1px solid var(--el-border-color);
  background-color: #ffffff;
  color: var(--el-text-color-primary);
  border-radius: 4px;
  padding: 0 10px;
  height: 32px;
  line-height: 32px;
  font-size: 12px;
  transition: all 0.3s ease;
}

.status-select--1 :deep(.el-input__inner) {
  --el-border-color: #f56c6c;
  --el-text-color-primary: #f56c6c;
}

.status-select--2 :deep(.el-input__inner) {
  --el-border-color: #e6a23c;
  --el-text-color-primary: #e6a23c;
}

.status-select--3 :deep(.el-input__inner) {
  --el-border-color: #67c23a;
  --el-text-color-primary: #67c23a;
}

.status-select--4 :deep(.el-input__inner) {
  --el-border-color: #409eff;
  --el-text-color-primary: #409eff;
}

.status-select--5 :deep(.el-input__inner) {
  --el-border-color: #909399;
  --el-text-color-primary: #909399;
}

.status-select :deep(.el-input__inner):hover {
  box-shadow: 0 0 0 2px rgba(0, 0, 0, 0.05);
}

.status-select :deep(.el-input__inner):focus {
  box-shadow: 0 0 0 2px rgba(0, 0, 0, 0.1);
}

.status-select :deep(.el-select__caret) {
  color: var(--el-text-color-primary);
}

.status-select :deep(.el-select-dropdown) {
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.status-select :deep(.el-select-dropdown__item) {
  padding: 8px 16px;
  color: var(--el-text-color-primary);
}

.status-select :deep(.el-select-dropdown__item.hover) {
  background-color: #f0f9eb;
  color: #67c23a;
}

/* 分页样式 */
.pagination {
  margin-top: 24px;
  padding: 12px 0;
  background-color: #f8fafc;
  border-radius: 8px;
  justify-content: center;
}

.pagination .btn-prev,
.pagination .btn-next,
.pagination .number {
  min-width: 32px;
  height: 32px;
  line-height: 32px;
  border-radius: 4px;
}

.pagination .number.active {
  background-color: #3a57e8;
  color: white;
}

.pagination .number:hover:not(.active) {
  color: #3a57e8;
}

/* 按钮样式 */
.el-button {
  transition: all 0.2s ease;
  border-radius: 4px;
  font-weight: 500;
}

.search-form .el-button--primary {
  background-color: #3a57e8;
  border-color: #3a57e8;
  color: #ffffff;
  padding: 8px 16px;
}

.search-form .el-button--primary:hover {
  background-color: #2c46d8;
  border-color: #2c46d8;
}

.el-table .el-button {
  padding: 6px 12px;
  font-size: 12px;
  border-radius: 4px;
}

.el-table .el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

/* 对话框样式 */
.el-dialog {
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(58, 87, 232, 0.15);
  border: none;
}

.el-dialog__header {
  padding: 18px 24px;
  border-bottom: 1px solid #f0f2f7;
  background-color: #f8fafc;
  border-radius: 12px 12px 0 0;
}

.el-dialog__title {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
}

.el-dialog__body {
  padding: 24px;
}

.el-dialog__footer {
  padding: 16px 24px;
  background-color: #f8fafc;
  border-radius: 0 0 12px 12px;
  text-align: right;
}

.el-dialog__footer .el-button {
  min-width: 80px;
  padding: 8px 16px;
}

.el-dialog__footer .el-button + .el-button {
  margin-left: 12px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
}

/* 对话框表单样式 */
.dialog-form .el-form-item,
.detail-form .el-form-item {
  margin-bottom: 18px;
}

.dialog-form .el-form-item__label,
.detail-form .el-form-item__label {
  padding-right: 16px;
  font-weight: 500;
  color: #606266;
}

/* 标签样式 */
.el-tag {
  margin: 2px;
  font-weight: 500;
}

.el-tag.el-tag--danger {
  background-color: #fef0f0;
  border-color: #fde2e2;
  color: #f56c6c;
}

.el-tag.el-tag--warning {
  background-color: #fdf6ec;
  border-color: #faecd8;
  color: #e6a23c;
}

.el-tag.el-tag--success {
  background-color: #f0f9eb;
  border-color: #e1f3d8;
  color: #67c23a;
}

.el-tag.el-tag--info {
  background-color: #f4f4f5;
  border-color: #e9e9eb;
  color: #909399;
}
</style>
