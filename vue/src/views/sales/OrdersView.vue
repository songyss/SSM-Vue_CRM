<template>
  <div class="orders-container">
    <div class="page-header">
      <h1>订单管理</h1>
      <el-button type="primary" @click="handleAddOrder">
        <el-icon><Plus /></el-icon> 新增订单
      </el-button>
    </div>

    <!-- 搜索筛选区域 -->
    <el-card class="filter-card">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.orderNo" placeholder="订单编号" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.status" placeholder="订单状态" clearable>
            <el-option label="全部" value="" />
            <el-option label="待付款" value="pending" />
            <el-option label="已付款" value="paid" />
            <el-option label="已发货" value="shipped" />
            <el-option label="已完成" value="completed" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            clearable
          />
        </el-col>
        <el-col :span="6" class="filter-actions">
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 订单表格 -->
    <el-card class="table-card" shadow="hover">
      <el-table
        :data="orderList"
        border
        stripe
        :row-class-name="tableRowClassName"
        style="width: 100%"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="orderNo" label="订单编号" width="180" sortable />
        <el-table-column prop="customerName" label="客户名称" width="160" />
        <el-table-column prop="orderDate" label="下单日期" width="160" sortable />
        <el-table-column prop="amount" label="订单金额" width="140" sortable>
          <template #default="scope">
            <span class="amount">{{ scope.row.amount | formatCurrency }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="140">
          <template #default="scope">
            <el-tag
              :type="statusTagType[scope.row.status]"
              :effect="scope.row.status === 'completed' ? 'dark' : 'light'"
            >
              {{ statusMap[scope.row.status] }}
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
              @click="handleDelete(scope.row)"
              v-if="['pending', 'paid'].includes(scope.row.status)"
            >
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
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
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

// 订单状态映射
const statusMap = {
  pending: '待付款',
  paid: '已付款',
  shipped: '已发货',
  completed: '已完成',
  cancelled: '已取消',
}

// 状态标签样式映射
const statusTagType = {
  pending: 'warning',
  paid: 'info',
  shipped: 'primary',
  completed: 'success',
  cancelled: 'danger',
}

// 搜索表单
const searchForm = reactive({
  orderNo: '',
  status: '',
  dateRange: [] as Date[],
})

// 分页配置
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 100,
})

// 订单列表数据
const orderList = ref([
  {
    id: 1,
    orderNo: 'ORD202305001',
    customerName: '北京某某科技有限公司',
    orderDate: '2023-05-10',
    amount: 12500,
    status: 'completed',
  },
  {
    id: 2,
    orderNo: 'ORD202305002',
    customerName: '上海某某贸易公司',
    orderDate: '2023-05-12',
    amount: 8900,
    status: 'shipped',
  },
  {
    id: 3,
    orderNo: 'ORD202305003',
    customerName: '广州某某企业',
    orderDate: '2023-05-15',
    amount: 5600,
    status: 'paid',
  },
  {
    id: 4,
    orderNo: 'ORD202305004',
    customerName: '深圳某某集团',
    orderDate: '2023-05-18',
    amount: 23000,
    status: 'pending',
  },
  {
    id: 5,
    orderNo: 'ORD202305005',
    customerName: '杭州某某工作室',
    orderDate: '2023-05-20',
    amount: 3200,
    status: 'cancelled',
  },
])

// 表格行样式
const tableRowClassName = ({ row }: { row: any }) => {
  return row.status === 'cancelled' ? 'row-cancelled' : ''
}

// 搜索处理
const handleSearch = () => {
  // 实际项目中这里会调用API
  pagination.currentPage = 1
  ElMessage.success('搜索成功')
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    orderNo: '',
    status: '',
    dateRange: [],
  })
}

// 分页事件
const handleSizeChange = (val: number) => {
  pagination.pageSize = val
}

const handleCurrentChange = (val: number) => {
  pagination.currentPage = val
}

// 行点击事件
const handleRowClick = (row: any) => {
  console.log('点击了行:', row)
}

// 操作按钮事件
const handleAddOrder = () => {
  // 新增订单逻辑
}

const handleView = (row: any) => {
  console.log('查看订单:', row)
}

const handleEdit = (row: any) => {
  console.log('编辑订单:', row)
}

const handleDelete = (row: any) => {
  console.log('取消订单:', row)
}

// 过滤器：格式化金额
const formatCurrency = (value: number) => {
  return '¥' + value.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')
}
</script>

<style scoped>
.orders-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-card {
  margin-bottom: 20px;
  padding: 15px;
}

.filter-actions {
  display: flex;
  gap: 10px;
}

.table-card {
  overflow: hidden;
}

.pagination-container {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}

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
