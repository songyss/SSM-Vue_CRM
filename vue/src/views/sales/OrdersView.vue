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
            <el-option label="待付款" value="1" />
            <el-option label="已付款" value="2" />
            <el-option label="已发货" value="3" />
            <el-option label="已完成" value="4" />
            <el-option label="已取消" value="5" />
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
              {{ scope.row.orderStatusName }}
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
              v-if="[1, 2].includes(scope.row.orderStatus)"
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
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

// 订单状态标签样式映射
const statusTagType = {
  1: 'warning',  // 待付款
  2: 'info',     // 已付款
  3: 'primary',  // 已发货
  4: 'success',  // 已完成
  5: 'danger',   // 已取消
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
  total: 0,
})

// 订单列表数据
const orderList = ref([])

// 表格行样式
const tableRowClassName = ({ row }: { row: any }) => {
  return row.orderStatus === 5 ? 'row-cancelled' : ''
}

// 获取订单列表
const fetchOrderList = async () => {
  try {
    // 调用后端查询所有订单的接口
    const response = await request.get('/orders/list')
    if (response) {
      orderList.value = response
      pagination.total = response.length
    }
  } catch (error) {
    ElMessage.error('获取订单列表失败')
    console.error('获取订单列表失败:', error)
  }
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
  fetchOrderList()
}

const handleCurrentChange = (val: number) => {
  pagination.currentPage = val
  fetchOrderList()
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

// 组件挂载时获取数据
onMounted(() => {
  fetchOrderList()
})
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
