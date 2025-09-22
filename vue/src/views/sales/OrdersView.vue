<template>
  <div class="orders-container">
    <!-- 订单管理区域 -->
    <div class="orders-management">
      <div class="header">
        <h3>订单管理</h3>
        <el-button type="primary" @click="handleAddOrder">+ 新增订单</el-button>
      </div>

      <!-- 搜索筛选区域 -->
      <el-card class="filter-card">
        <el-form :inline="true" :model="searchForm" class="demo-form-inline">
          <el-form-item label="订单编号">
            <el-input v-model="searchForm.orderNo" placeholder="请输入订单编号" />
          </el-form-item>

          <el-form-item label="订单状态">
            <el-select v-model="searchForm.status" placeholder="请选择订单状态" clearable>
              <el-option label="全部" value="" />
              <el-option label="待支付" value="1" />
              <el-option label="执行中" value="2" />
              <el-option label="已完成" value="3" />
              <el-option label="已退款" value="4" />
              <el-option label="申请售后" value="5" />
              <el-option label="已驳回" value="6" />
              <el-option label="取消售后" value="7" />
              <el-option label="售后处理中" value="8" />
            </el-select>
          </el-form-item>

          <el-form-item label="下单日期">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
              format="YYYY-MM-DD"
              clearable
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 订单列表 -->
      <el-table
        :data="orderList"
        style="width: 100%"
        border
        :row-class-name="tableRowClassName"
      >
        <!-- 表格列定义 -->
        <el-table-column type="selection" width="55" />

        <el-table-column prop="orderNumber" label="订单编号" width="180" sortable />
        <el-table-column prop="customerName" label="客户名称" width="160" />
        <el-table-column prop="signedDate" label="下单日期" width="160" sortable />
        <el-table-column prop="totalAmount" label="订单金额" width="140" sortable>
          <template #default="scope">
            <span class="amount">¥{{ scope.row.totalAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderStatusName" label="订单状态" width="140">
          <template #default="scope">
            <el-tag
              :type="statusTagType[scope.row.orderStatus]"
              :effect="scope.row.orderStatus === 4 ? 'dark' : 'light'"
            >
              {{ scope.row.orderStatusName || getStatusName(scope.row.orderStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="small" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              size="small"
              type="text"
              text-color="#ff4d4f"
              @click="handleCancelOrder(scope.row)"
              v-if="[1, 2].includes(scope.row.orderStatus)"
            >
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      />
    </div>

    <!-- 查看订单详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="订单详情" width="600px">
      <el-form :model="currentOrder" label-width="100px">
        <el-form-item label="订单编号：">
          <span>{{ currentOrder.orderNumber }}</span>
        </el-form-item>
        <el-form-item label="客户名称：">
          <span>{{ currentOrder.customerName }}</span>
        </el-form-item>
        <el-form-item label="下单日期：">
          <span>{{ currentOrder.signedDate }}</span>
        </el-form-item>
        <el-form-item label="订单金额：">
          <span class="amount">¥{{ currentOrder.totalAmount }}</span>
        </el-form-item>
        <el-form-item label="订单状态：">
          <el-tag :type="statusTagType[currentOrder.orderStatus]">
            {{ currentOrder.orderStatusName || getStatusName(currentOrder.orderStatus) }}
          </el-tag>
        </el-form-item>
        <el-form-item label="合同文件：" v-if="currentOrder.fileUrl">
          <el-link :href="currentOrder.fileUrl" target="_blank">查看合同文件</el-link>
        </el-form-item>
        <el-form-item label="订单备注：">
          <span>{{ currentOrder.notes || '无' }}</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑订单对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑订单" width="600px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="订单编号：">
          <el-input v-model="editForm.orderNumber" disabled />
        </el-form-item>
        <el-form-item label="客户名称：">
          <el-input v-model="editForm.customerName" disabled />
        </el-form-item>
        <el-form-item label="下单日期：">
          <el-date-picker
            v-model="editForm.signedDate"
            type="date"
            value-format="YYYY-MM-DD"
            format="YYYY-MM-DD"
            disabled
          />
        </el-form-item>
        <el-form-item label="订单金额：">
          <el-input v-model.number="editForm.totalAmount" disabled />
        </el-form-item>
        <el-form-item label="订单状态：">
          <el-select v-model="editForm.orderStatus" placeholder="请选择订单状态">
            <el-option label="待支付" value="1" />
            <el-option label="执行中" value="2" />
            <el-option label="已完成" value="3" />
            <el-option label="已退款" value="4" />
            <el-option label="申请售后" value="5" />
            <el-option label="已驳回" value="6" />
            <el-option label="取消售后" value="7" />
            <el-option label="售后处理中" value="8" />
          </el-select>
        </el-form-item>
        <el-form-item label="合同文件：">
          <el-input v-model="editForm.fileUrl" placeholder="请输入合同文件URL" />
        </el-form-item>
        <el-form-item label="订单备注：">
          <el-input
            v-model="editForm.notes"
            type="textarea"
            :rows="3"
            placeholder="请输入订单备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmEdit">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 新增订单对话框 -->
    <el-dialog v-model="addDialogVisible" title="新增订单" width="600px">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
        <el-form-item label="订单编号：" prop="orderNumber">
          <el-input v-model="addForm.orderNumber" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="客户名称：" prop="customerId">
          <el-select
            v-model="addForm.customerId"
            placeholder="请选择客户"
            clearable
            filterable
          >
            <el-option
              v-for="customer in customers"
              :key="customer.id"
              :label="customer.name"
              :value="customer.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商机：" prop="opportunityId">
          <el-select v-model="addForm.opportunityId" placeholder="请选择关联商机" clearable>
            <el-option
              v-for="opportunity in opportunities"
              :key="opportunity.id"
              :label="opportunity.name"
              :value="opportunity.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="下单日期：" prop="signedDate">
          <el-date-picker
            v-model="addForm.signedDate"
            type="date"
            value-format="YYYY-MM-DD"
            format="YYYY-MM-DD"
            placeholder="请选择下单日期"
          />
        </el-form-item>
        <el-form-item label="订单金额：" prop="totalAmount">
          <el-input v-model.number="addForm.totalAmount" placeholder="请输入订单金额" />
        </el-form-item>
        <el-form-item label="合同文件：">
          <el-input v-model="addForm.fileUrl" placeholder="请输入合同文件URL" />
        </el-form-item>
        <el-form-item label="订单备注：">
          <el-input
            v-model="addForm.notes"
            type="textarea"
            :rows="3"
            placeholder="请输入订单备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmAdd">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

// 订单状态标签样式映射
const statusTagType = {
  1: 'warning',   // 待支付
  2: 'info',      // 执行中
  3: 'success',   // 已完成
  4: 'danger',    // 已退款
  5: 'primary',   // 申请售后
  6: 'warning',   // 已驳回
  7: 'success',   // 取消售后
  8: 'info'       // 售后处理中
}

// 搜索表单数据模型
const searchForm = reactive({
  orderNo: '',      // 订单编号搜索条件
  status: '',       // 订单状态筛选条件
  dateRange: [] as string[], // 下单日期范围筛选条件
})

// 分页配置
const pagination = reactive({
  currentPage: 1,   // 当前页码
  pageSize: 10,     // 每页显示条数
  total: 0,         // 总数据量
})

// 订单列表数据
const orderList = ref([])

// 对话框控制变量
const viewDialogVisible = ref(false)
const editDialogVisible = ref(false)
const addDialogVisible = ref(false)

// 客户列表数据
const customers = ref([])

// 当前查看的订单
const currentOrder = ref({} as any)

// 编辑表单数据
const editForm = ref({
  id: 0,
  orderNumber: '',
  customerName: '',
  signedDate: '',
  totalAmount: 0,
  orderStatus: '',
  fileUrl: '',
  notes: ''
})

// 新增表单数据
const addForm = ref({
  orderNumber: '',
  customerId: null,
  opportunityId: null,
  signedDate: '',
  totalAmount: 0,
  fileUrl: '',
  notes: ''
})

// 新增表单验证规则
const addFormRules = {
  orderNumber: [{ required: true, message: '请输入订单编号', trigger: 'blur' }],
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  signedDate: [{ required: true, message: '请选择下单日期', trigger: 'change' }],
  totalAmount: [
    { required: true, message: '请输入订单金额', trigger: 'blur' },
    { type: 'number', message: '订单金额必须为数字', trigger: 'blur' }
  ]
}

// 新增表单引用
const addFormRef = ref()

// 模拟商机数据（实际项目中应从接口获取）
const opportunities = ref([
  { id: 1, name: '商机一' },
  { id: 2, name: '商机二' },
  { id: 3, name: '商机三' }
])

// 表格行样式函数，为已退款和已驳回的订单添加特殊样式
const tableRowClassName = ({ row }: { row: any }) => {
  // 当订单状态为已退款(4)或已驳回(6)时添加特殊样式
  if ([4, 6].includes(row.orderStatus)) {
    return 'row-cancelled'
  }
  return ''
}

// 获取订单列表数据
const fetchOrderList = async () => {
  try {
    // 构造请求参数
    const params: any = {
      page: pagination.currentPage,
      size: pagination.pageSize
    }

    // 只有当搜索条件不为空时才添加到参数中
    if (searchForm.orderNo) {
      params.orderNo = searchForm.orderNo
    }
    if (searchForm.status) {
      params.status = searchForm.status
    }

    // 处理日期范围参数
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }

    // 调用后端分页查询接口
    const response = await request.get('/orders/pageByCondition', { params })

    // 根据request.js的响应拦截器，需要访问response.data获取实际数据
    if (response && response.data) {
      orderList.value = response.data.list || []
      pagination.total = response.data.total || 0
    } else {
      ElMessage.error('获取订单列表失败: 返回数据为空')
    }
  } catch (error) {
    console.error('获取订单列表异常:', error)
    ElMessage.error('获取订单列表失败: ' + (error.message || '网络异常'))
  }
}

// 获取客户列表数据
const fetchCustomerList = async () => {
  try {
    const response = await request.get('/customer/allList')
    if (response && response.data) {
      customers.value = response.data
    } else {
      ElMessage.error('获取客户列表失败: 返回数据为空')
    }
  } catch (error) {
    console.error('获取客户列表异常:', error)
    ElMessage.error('获取客户列表失败: ' + (error.message || '网络异常'))
  }
}

// 搜索处理函数
const handleSearch = () => {
  pagination.currentPage = 1
  fetchOrderList()
}

// 重置搜索条件
const resetSearch = () => {
  Object.assign(searchForm, {
    orderNo: '',
    status: '',
    dateRange: [],
  })
  handleSearch()
}

// 分页大小改变事件处理
const handleSizeChange = (val: number) => {
  pagination.pageSize = val
  fetchOrderList()
}

// 当前页码改变事件处理
const handleCurrentChange = (val: number) => {
  pagination.currentPage = val
  fetchOrderList()
}

// 查看订单详情
const handleView = (row: any) => {
  currentOrder.value = { ...row }
  viewDialogVisible.value = true
}

// 编辑订单
const handleEdit = (row: any) => {
  editForm.value = {
    id: row.id,
    orderNumber: row.orderNumber,
    customerName: row.customerName,
    signedDate: row.signedDate,
    totalAmount: row.totalAmount,
    orderStatus: String(row.orderStatus),
    fileUrl: row.fileUrl || '',
    notes: row.notes || ''
  }
  editDialogVisible.value = true
}

// 确认编辑订单
const confirmEdit = async () => {
  try {
    const orderData = {
      id: editForm.value.id,
      orderStatus: parseInt(editForm.value.orderStatus),
      fileUrl: editForm.value.fileUrl,
      notes: editForm.value.notes
    }

    const response = await request.patch('/orders/update', orderData)
    if (response && response.code === 200) {
      ElMessage.success('订单更新成功')
      editDialogVisible.value = false
      fetchOrderList()
    } else {
      ElMessage.error('订单更新失败: ' + (response?.message || '未知错误'))
    }
  } catch (error) {
    console.error('更新订单异常:', error)
    ElMessage.error('订单更新失败: ' + (error.message || '网络异常'))
  }
}

// 取消订单
const handleCancelOrder = (row: any) => {
  ElMessageBox.confirm(
    `确定要取消订单 ${row.orderNumber} 吗？此操作不可恢复`,
    '确认取消',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const orderData = {
        id: row.id,
        orderStatus: 4 // 已退款状态
      }

      const response = await request.patch('/orders/update', orderData)
      if (response && response.code === 200) {
        ElMessage.success('订单取消成功')
        fetchOrderList()
      } else {
        ElMessage.error('订单取消失败: ' + (response?.message || '未知错误'))
      }
    } catch (error) {
      console.error('取消订单异常:', error)
      ElMessage.error('订单取消失败: ' + (error.message || '网络异常'))
    }
  }).catch(() => {
    // 用户取消操作
  })
}

// 新增订单
const handleAddOrder = () => {
  // 重置表单数据
  addForm.value = {
    orderNumber: '',
    customerId: null,
    opportunityId: null,
    signedDate: '',
    totalAmount: 0,
    fileUrl: '',
    notes: ''
  }
  addDialogVisible.value = true
}

// 确认新增订单
const confirmAdd = async () => {
  if (!addFormRef.value) return

  await addFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        const orderData = {
          orderNumber: addForm.value.orderNumber,
          customerId: addForm.value.customerId,
          // 从客户列表中查找客户名称
          customerName: customers.value.find((c: any) => c.id === addForm.value.customerId)?.name || '',
          opportunityId: addForm.value.opportunityId,
          signedDate: addForm.value.signedDate,
          totalAmount: addForm.value.totalAmount,
          fileUrl: addForm.value.fileUrl,
          notes: addForm.value.notes,
          orderStatus: 1 // 默认为待支付状态
        }

        const response = await request.put('/orders/add', orderData)
        if (response && response.code === 200) {
          ElMessage.success('订单新增成功')
          addDialogVisible.value = false
          fetchOrderList()
        } else {
          ElMessage.error('订单新增失败: ' + (response?.message || '未知错误'))
        }
      } catch (error) {
        console.error('新增订单异常:', error)
        ElMessage.error('订单新增失败: ' + (error.message || '网络异常'))
      }
    }
  })
}

// 组件挂载时获取数据
onMounted(() => {
  fetchOrderList()
  fetchCustomerList()
})

// 根据订单状态ID获取状态名称
const getStatusName = (statusId: number): string => {
  const statusMap: Record<number, string> = {
    1: '待支付',
    2: '执行中',
    3: '已完成',
    4: '已退款',
    5: '申请售后',
    6: '已驳回',
    7: '取消售后',
    8: '售后处理中'
  }
  return statusMap[statusId] || '未知状态'
}
</script>

<style scoped>
.orders-container {
  padding: 20px;
}

.orders-management {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.filter-card {
  margin-bottom: 20px;
}

.demo-form-inline {
  margin: 20px auto;
  width: 80%;
  max-width: 1200px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.el-form-item {
  margin-right: 20px;
  margin-bottom: 10px;
}

.el-form-item:last-child {
  margin-right: 0;
}

.el-form-item .el-input,
.el-form-item .el-select,
.el-form-item .el-date-editor {
  width: 200px;
}

.el-form-item .el-button {
  margin-left: 10px;
}

/* 页面主容器样式 */
.orders-container {
  padding: 20px;
}

/* 页面头部样式 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

/* 筛选卡片样式 */
.filter-card {
  margin-bottom: 20px;
  padding: 15px;
}

/* 筛选操作按钮容器样式 */
.filter-actions {
  display: flex;
  gap: 10px;
}

/* 表格卡片样式 */
.table-card {
  overflow: hidden;
}

/* 分页容器样式 */
.pagination-container {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}

/* 金额文本样式 */
.amount {
  color: #27ae60;
  font-weight: 500;
}

/* 取消订单行样式 */
:deep(.row-cancelled) {
  opacity: 0.7;
}

:deep(.row-cancelled td) {
  text-decoration: line-through;
}
</style>
