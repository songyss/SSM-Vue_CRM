<template>
  <div>
    <div class="header">
      <h2>售后订单管理</h2>
    </div>

    <!-- 搜索区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form" label-position="left">
      <el-form-item label="订单号" label-width="65px" class="el-form-item">
        <el-input
          v-model="searchForm.orderNumber"
          placeholder="输入订单号"
          clearable
          size="small"
          class="el-input"
        />
      </el-form-item>
      <el-form-item label="订单状态" label-width="70px" class="el-form-item">
        <el-select
          v-model="searchForm.status"
          placeholder="请选择"
          clearable
          size="small"
          class="el-select"
          style="width: 120px"
        >
          <el-option label="待处理" value="1"/>
          <el-option label="处理中" value="2"/>
          <el-option label="已完成" value="3"/>
          <el-option label="已取消" value="4"/>
          <el-option label="已驳回" value="5"/>
        </el-select>
      </el-form-item>
      <el-form-item label="申请时间" label-width="70px" class="el-form-item">
        <el-date-picker
          v-model="searchForm.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          size="small"
          value-format="YYYY-MM-DD"
          format="YYYY-MM-DD"
          class="el-date-picker"
        />
      </el-form-item>
      <el-form-item class="el-form-item">
        <el-button type="primary" @click="fetchAfterSales" size="small">搜索</el-button>
      </el-form-item>
    </el-form>


    <!-- 售后订单表格 -->
    <el-table :data="afterSales" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="50" align="center"/>
      <el-table-column prop="orderNumber" label="订单号" width="150" align="center" fixed="left"/>
      <el-table-column prop="customerName" label="客户名称" width="90" align="center"/>
      <el-table-column prop="totalAmount" label="金额" width="120" align="center">
        <template #default="{ row }">
          ¥{{ parseFloat(row.totalAmount).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="signedDate" label="签约日期" width="120" align="center"/>

      <!-- 修改后的状态列，使用下拉选框 -->
      <el-table-column label="状态" width="180" align="center">
        <template #default="{ row }">
          <el-select
            :model-value="getStatusValue(row)"
            placeholder="选择状态"
            size="small"
            class="el-select"
            @change="(value) => handleStatusChange(row, value)"
          >
            <el-option label="待处理" :value="1"/>
            <el-option label="处理中" :value="2"/>
            <el-option label="已完成" :value="3"/>
            <el-option label="已取消" :value="4"/>
            <el-option label="已驳回" :value="5"/>
          </el-select>
        </template>
      </el-table-column>


      <el-table-column prop="afterSaleApplyTime" label="申请时间" width="180" align="center">
        <template #default="{ row }">
          {{ formatDateTime(row.afterSaleApplyTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="afterSaleCompleteTime" label="完成时间" width="180" align="center">
        <template #default="{ row }">
          {{ formatDateTime(row.afterSaleCompleteTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="afterSaleHandlerId" label="处理人ID" width="100" align="center"/>
      <el-table-column prop="notes" label="订单备注" width="200" show-overflow-tooltip
                       align="center"/>
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
      <el-form :model="currentOrder" label-width="100px">
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
          <el-select v-model="currentOrder.afterSaleStatus" placeholder="请选择处理状态">
            <el-option label="待处理" :value="1"/>
            <el-option label="处理中" :value="2"/>
            <el-option label="已完成" :value="3"/>
            <el-option label="已取消" :value="4"/>
            <el-option label="已驳回" :value="5"/>
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
        <el-button type="primary" @click="updateOrderStatus">确认</el-button>
      </span>
      </template>
    </el-dialog>


    <!-- 订单详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="订单详情" width="600px">
      <el-form label-width="120px" label-position="left">
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
              <span>{{ formatDate(detailOrder.signedDate) }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态:">
              <el-tag :type="getStatusTagType(detailOrder.afterSaleStatus)">
                {{ getStatusName(detailOrder.afterSaleStatus) }}
              </el-tag>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="申请时间:">
              <span>{{ formatDateTime(detailOrder.afterSaleApplyTime) }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="完成时间:">
              <span>{{ formatDateTime(detailOrder.afterSaleCompleteTime) || '未完成' }}</span>
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
          <span>{{ formatDateTime(detailOrder.createTime) }}</span>
        </el-form-item>
        <el-form-item label="更新时间:">
          <span>{{ formatDateTime(detailOrder.updateTime) }}</span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">import {ref, onMounted} from 'vue'
import {ElMessage} from 'element-plus'
import request from '@/utils/request'
import {useRouter} from 'vue-router'

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

// 搜索售后订单
const searchAfterSales = async () => {
  // 重置分页
  pagination.value.current = 1
  await fetchAfterSales()
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return '-';

  // 如果是日期字符串，直接返回
  if (typeof dateTime === 'string') {
    // 如果包含时间部分且不是默认的 00:00:00，则显示完整时间
    if (dateTime.includes(':') && !dateTime.includes('00:00:00')) {
      return dateTime;
    }
    // 否则只显示日期部分
    return dateTime.split(' ')[0];
  }

  return dateTime;
}

// 格式化日期（只显示日期部分）
const formatDate = (date) => {
  if (!date) return '-';

  if (typeof date === 'string') {
    return date.split(' ')[0];
  }

  return date;
}

// 获取状态名称
const getStatusName = (status) => {
  const statusMap = {
    1: '待处理',
    2: '处理中',
    3: '已完成',
    4: '已取消',
    5: '已驳回'
  }
  return statusMap[status] || '未知状态'
}

// 获取售后订单列表
const fetchAfterSales = async () => {
  try {
    console.log('开始获取售后订单数据...')

    let data;

    // 如果有任何搜索条件，使用组合查询接口
    if (searchForm.value.orderNumber || searchForm.value.status ||
      (searchForm.value.dateRange && searchForm.value.dateRange.length === 2)) {

      // 准备查询参数
      const params: any = {};

      if (searchForm.value.orderNumber) {
        params.orderNumber = searchForm.value.orderNumber.trim();
      }

      if (searchForm.value.status) {
        params.afterSaleStatus = parseInt(searchForm.value.status);
      }

      // 处理日期范围参数
      if (searchForm.value.dateRange && searchForm.value.dateRange.length === 2) {
        // 确保日期格式正确
        params.startDate = searchForm.value.dateRange[0];
        params.endDate = searchForm.value.dateRange[1];
      }

      try {
        data = await request.get('/afterSaleOrders/getAfterSaleOrderByCondition', {
          params
        });
      } catch (error) {
        console.error('组合查询失败:', error);
        ElMessage.error('查询失败: ' + (error.message || '网络错误'));
        return;
      }
    }
    // 默认查询待处理状态
    else {
      try {
        data = await request.get('/afterSaleOrders/getAfterSaleOrderByStatus');
      } catch (error) {
        ElMessage.error('查询默认数据失败: ' + (error.message || '网络错误'));
        return;
      }
    }

    console.log('收到数据:', data)

    // 处理响应数据
    if (data && data.data) {
      // 确保数据是数组格式
      if (Array.isArray(data.data)) {
        afterSales.value = data.data;
        pagination.value.total = data.data.length;
        ElMessage.success('数据加载成功');
      } else if (data.data.data && Array.isArray(data.data.data)) {
        afterSales.value = data.data.data;
        pagination.value.total = data.data.total || data.data.length;
        ElMessage.success('数据加载成功');
      } else if (data.data.id) {
        // 如果是单个对象（有id属性），转换为数组
        afterSales.value = [data.data];
        pagination.value.total = 1;
        ElMessage.success('数据加载成功');
      } else {
        // 数据为空或其他情况
        afterSales.value = [];
        pagination.value.total = 0;
        ElMessage.info('暂无售后订单数据');
      }
    } else {
      // 数据为空
      afterSales.value = [];
      pagination.value.total = 0;
      ElMessage.info('暂无售后订单数据');
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

    if (error.message) {
      ElMessage.error(error.message)
    } else {
      ElMessage.error('请求失败: ' + (error.message || '未知错误'))
    }

    afterSales.value = []
    pagination.value.total = 0
  }
}


// 状态标签样式
const getStatusTagType = (status) => {
  const map = {1: 'danger', 2: 'warning', 3: 'success', 4: 'info', 5: 'info'}
  return map[status] || ''
}

// 查看详情
const handleDetail = (row) => {
  detailOrder.value = {...row}
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
  return row.afterSaleStatus
}

// 处理状态变更
const handleStatusChange = (row, value) => {
  // 保存当前修改的状态到临时映射中
  tempStatusMap.value.set(row.orderNumber, value)

  // 显示对话框进行确认
  currentOrder.value = {...row, afterSaleStatus: value}
  dialogVisible.value = true
}

// 取消更新
const cancelUpdate = () => {
  // 清除临时状态映射
  if (currentOrder.value.orderNumber) {
    tempStatusMap.value.delete(currentOrder.value.orderNumber)
  }
  dialogVisible.value = false
}

// 更新订单状态
const updateOrderStatus = async () => {
  try {
    const response = await request.get('/afterSaleOrders/updateAfterSaleOrderStatus', {
      params: {
        id: currentOrder.value.id,
        afterSaleStatus: currentOrder.value.afterSaleStatus,
        orderNumber: currentOrder.value.orderNumber
      }
    })

    if (response.data.code === 200) {
      ElMessage.success('状态更新成功')

      // 更新本地数据
      const index = afterSales.value.findIndex(item => item.id === currentOrder.value.id)
      if (index !== -1) {
        afterSales.value[index].afterSaleStatus = currentOrder.value.afterSaleStatus
      }

      // 清除临时状态映射
      tempStatusMap.value.delete(currentOrder.value.orderNumber)

      // 关闭对话框
      dialogVisible.value = false

      // 重新获取数据
      await fetchAfterSales()
    } else {
      ElMessage.error(response.data.message || '状态更新失败')
    }
  } catch (error) {
    console.error('更新状态失败:', error)
    ElMessage.error('状态更新失败: ' + (error.message || '网络错误'))
  }
}

// 对话框关闭时的处理
const onDialogClose = () => {
  // 清除临时状态映射
  if (currentOrder.value.orderNumber) {
    tempStatusMap.value.delete(currentOrder.value.orderNumber)
  }
  currentOrder.value = {}
}

// 组件挂载时获取数据
onMounted(() => {
  fetchAfterSales()
})
</script>

<style scoped>
.header {
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.el-form-item {
  margin-right: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

.dialog-footer {
  text-align: right;
}
</style>
