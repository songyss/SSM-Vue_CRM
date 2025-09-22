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
      <el-form-item label="订单状态" label-width="65px" class="el-form-item">
        <el-select
          v-model="searchForm.status"
          placeholder="请选择"
          clearable
          size="small"
          class="el-select"
        >
          <el-option label="待处理" value="1"/>
          <el-option label="处理中" value="2"/>
          <el-option label="已完成" value="3"/>
          <el-option label="已取消" value="4"/>
          <el-option label="已驳回" value="5"/>
        </el-select>
      </el-form-item>
      <el-form-item label="申请时间" label-width="65px" class="el-form-item">
        <el-date-picker
          v-model="searchForm.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          size="small"
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


      <el-table-column prop="afterSaleApplyTime" label="申请时间" width="180" align="center"/>
      <el-table-column prop="afterSaleCompleteTime" label="完成时间" width="180" align="center"/>
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
          <el-select v-model="currentOrder.statusName" placeholder="请选择处理状态">
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
              <span>{{ detailOrder.signedDate }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态:">
              <el-tag :type="getStatusTagType(detailOrder.statusName)">
                {{ detailOrder.statusName }}
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
        params.afterSaleStatus = searchForm.value.status;
      }

      if (searchForm.value.dateRange && searchForm.value.dateRange.length === 2) {
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
        data = await request.get('/afterSaleOrders/getAfterSaleOrderByStatus', {
          params: {
            afterSaleStatus: 1  // 默认查询待处理状态
          }
        });
      } catch (error) {
        ElMessage.error('查询默认数据失败: ' + (error.message || '网络错误'));
        return;
      }
    }

    console.log('收到数据:', data)

    // 处理响应数据
    if (data) {
      // 确保数据是数组格式
      if (Array.isArray(data.data.data)) {
        afterSales.value = data.data.data;
        pagination.value.total = data.data.length;
        ElMessage.success('数据加载成功');
      } else if (data.id) {
        // 如果是单个对象（有id属性），转换为数组
        afterSales.value = [data];
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
  const map = {1: 'danger', 2: 'warning', 3: 'success', 4: 'info'}
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
  return row.statusName
}

// 处理状态变更
const handleStatusChange = (row, value) => {
  // 保存原始状态值（只在第一次时保存）
  if (!originalStatusMap.value.has(row.orderNumber)) {
    originalStatusMap.value.set(row.orderNumber, row.statusName)
  }

  // 保存临时状态值
  tempStatusMap.value.set(row.orderNumber, value)

  // 打开对话框
  currentOrder.value = {...row, statusName: value}
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

// 处理对话框关闭事件（包括点击X按钮或按ESC键）
const onDialogClose = () => {
  cancelUpdate();
}

// 更新订单状态成功后清除临时状态
const updateOrderStatus = async () => {
  if (!currentOrder.value.statusName && currentOrder.value.statusName !== 0) {
    ElMessage.warning('请选择处理状态');
    return;
  }

  try {
    const response = await request.get('/afterSaleOrders/updateAfterSaleOrderStatus', {
      params: {
        orderNumber: currentOrder.value.orderNumber,
        afterSaleStatus: currentOrder.value.statusName
      }
    });

    // 检查响应结果
    if (response && (response.code === 200 || response.code === 0)) {
      ElMessage.success(response.message || '处理成功');
      dialogVisible.value = false;

      // 清除临时状态值（更新成功后不需要恢复）
      if (currentOrder.value.orderNumber) {
        tempStatusMap.value.delete(currentOrder.value.orderNumber);
        originalStatusMap.value.delete(currentOrder.value.orderNumber);
      }

      fetchAfterSales(); // 重新加载数据
    } else {
      ElMessage.error(response.message || '处理失败');
    }
  } catch (error) {
    console.error('完整错误:', error);
    if (error.message && error.message.includes('未登录')) {
      ElMessage.error('请先登录');
      router.push('/login');
    } else if (error.message && error.message.includes('Token')) {
      ElMessage.error('身份验证已过期，请重新登录');
      localStorage.removeItem('crm_token');
      router.push('/login');
    } else {
      ElMessage.error('处理失败: ' + (error.message || '网络错误'));
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

.pagination {
  margin-top: 24px;
  padding: 12px 0;
  background-color: #f8fafc;
  border-radius: 8px;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
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

/* 按钮样式优化 */
.el-button {
  transition: all 0.2s ease;
  border-radius: 4px;
  font-weight: 500;
}

/* 搜索按钮 */
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

/* 表格操作按钮 */
.el-table .el-button {
  padding: 6px 12px;
  font-size: 12px;
  border-radius: 4px;
}

.el-table .el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

/* 状态选择器 */
.el-table .el-select {
  width: 100%;
}

.el-table .el-select .el-input__inner {
  border-radius: 4px;
  padding: 0 10px;
}

/* 分页组件 */
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

/* 表单元素统一样式 */
.el-form-item {
  margin-bottom: 0;
  flex: 1 1 200px;
}

.el-form-item__label {
  font-weight: 500;
  color: #606266;
}

.el-input, .el-select, .el-date-picker {
  width: 100%;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.el-input__inner, .el-textarea__inner {
  border-radius: 6px;
  transition: all 0.3s ease;
  border: 1px solid #dcdfe6;
}

.el-input__inner:focus, .el-textarea__inner:focus {
  border-color: #3a57e8;
  box-shadow: 0 0 0 2px rgba(58, 87, 232, 0.1);
}

/* 表格边框优化 */
.el-table {
  --el-table-border-color: #f0f2f7;
}

.el-table td, .el-table th {
  border-right: 1px solid #f0f2f7;
}

.el-table td:last-child, .el-table th:last-child {
  border-right: none;
}

/* 对话框表单优化 */
.el-dialog__body .el-form-item {
  margin-bottom: 18px;
}

.el-dialog__body .el-form-item__label {
  padding-right: 16px;
}

.el-dialog__body .el-input,
.el-dialog__body .el-select,
.el-dialog__body .el-textarea {
  width: 100%;
}

.el-table {
  margin-top: 16px;
  border: 1px solid #f0f2f7;
  border-radius: 4px;
  overflow: hidden;
  transition: all 0.2s ease;
}

.el-table :deep(.el-table__cell) {
  padding: 16px 0;
  transition: all 0.2s ease;
}

.el-table :deep(.el-table__row) {
  height: 78px;
  transition: all 0.4s cubic-bezier(0.2, 0.8, 0.2, 1);
}

.el-table :deep(.el-table__row:hover) {
  background-color: #f8fafc;
  transform: translateY(-6px);
  box-shadow: 0 14px 32px rgba(58, 87, 232, 0.28);
  z-index: 1;
  position: relative;
}

.el-table :deep(.el-table__cell) {
  padding: 24px 0;
  border-bottom: 1px solid #f0f2f7;
}

/* 优化状态选择器交互 */
.el-table :deep(.el-select) {
  width: 100%;
}

.el-table :deep(.el-select:hover .el-input__inner) {
  border-color: #3a57e8;
  box-shadow: 0 0 0 6px rgba(58, 87, 232, 0.2);
}

.el-table :deep(.el-select .el-input__inner) {
  padding: 0 20px;
  height: 44px;
  line-height: 44px;
  border-radius: 10px;
  transition: all 0.4s ease;
  border: 1px solid #dcdfe6;
  font-size: 15px;
}

/* 优化对话框表单细节 */
.el-dialog__body .el-form-item {
  margin-bottom: 28px;
}

.el-dialog__body .el-form-item__label {
  padding-right: 24px;
  font-weight: 500;
  color: #606266;
  font-size: 16px;
}

.el-dialog__body .el-input__inner,
.el-dialog__body .el-textarea__inner {
  padding: 18px 22px;
  height: auto;
  min-height: 56px;
  border-radius: 10px;
  border: 1px solid #dcdfe6;
  transition: all 0.4s ease;
  font-size: 16px;
}

.el-dialog__body .el-textarea__inner {
  min-height: 180px;
  line-height: 1.9;
}

/* 全局阴影优化 */
.header, .search-form, .el-table, .el-dialog {
  box-shadow: 0 12px 28px rgba(58, 87, 232, 0.2);
  transition: box-shadow 0.5s ease;
}

.header:hover, .search-form:hover, .el-table:hover, .el-dialog:hover {
  box-shadow: 0 16px 40px rgba(58, 87, 232, 0.3);
}

.el-table :deep(.el-table__cell) {
  padding: 22px 0;
  border-bottom: 1px solid #f0f2f7;
}

/* 优化状态选择器交互 */
.el-table :deep(.el-select) {
  width: 100%;
}

.el-table :deep(.el-select:hover .el-input__inner) {
  border-color: #3a57e8;
  box-shadow: 0 0 0 5px rgba(58, 87, 232, 0.18);
}

.el-table :deep(.el-select .el-input__inner) {
  padding: 0 18px;
  height: 42px;
  line-height: 42px;
  border-radius: 10px;
  transition: all 0.35s ease;
  border: 1px solid #dcdfe6;
  font-size: 15px;
}

/* 优化对话框表单细节 */
.el-dialog__body .el-form-item {
  margin-bottom: 26px;
}

.el-dialog__body .el-form-item__label {
  padding-right: 22px;
  font-weight: 500;
  color: #606266;
  font-size: 16px;
}

.el-dialog__body .el-input__inner,
.el-dialog__body .el-textarea__inner {
  padding: 16px 20px;
  height: auto;
  min-height: 52px;
  border-radius: 10px;
  border: 1px solid #dcdfe6;
  transition: all 0.35s ease;
  font-size: 16px;
}

.el-dialog__body .el-textarea__inner {
  min-height: 160px;
  line-height: 1.8;
}

/* 全局阴影优化 */
.header, .search-form, .el-table, .el-dialog {
  box-shadow: 0 10px 24px rgba(58, 87, 232, 0.18);
  transition: box-shadow 0.45s ease;
}

.header:hover, .search-form:hover, .el-table:hover, .el-dialog:hover {
  box-shadow: 0 14px 36px rgba(58, 87, 232, 0.28);
}

.el-table :deep(.el-table__cell) {
  padding: 20px 0;
  border-bottom: 1px solid #f0f2f7;
}

/* 优化状态选择器交互 */
.el-table :deep(.el-select) {
  width: 100%;
}

.el-table :deep(.el-select:hover .el-input__inner) {
  border-color: #3a57e8;
  box-shadow: 0 0 0 4px rgba(58, 87, 232, 0.15);
}

.el-table :deep(.el-select .el-input__inner) {
  padding: 0 16px;
  height: 40px;
  line-height: 40px;
  border-radius: 8px;
  transition: all 0.3s ease;
  border: 1px solid #dcdfe6;
  font-size: 14px;
}

/* 优化对话框表单细节 */
.el-dialog__body .el-form-item {
  margin-bottom: 24px;
}

.el-dialog__body .el-form-item__label {
  padding-right: 20px;
  font-weight: 500;
  color: #606266;
  font-size: 15px;
}

.el-dialog__body .el-input__inner,
.el-dialog__body .el-textarea__inner {
  padding: 14px 18px;
  height: auto;
  min-height: 48px;
  border-radius: 8px;
  border: 1px solid #dcdfe6;
  transition: all 0.3s ease;
  font-size: 15px;
}

.el-dialog__body .el-textarea__inner {
  min-height: 140px;
  line-height: 1.7;
}

/* 全局阴影优化 */
.header, .search-form, .el-table, .el-dialog {
  box-shadow: 0 8px 20px rgba(58, 87, 232, 0.15);
  transition: box-shadow 0.4s ease;
}

.header:hover, .search-form:hover, .el-table:hover, .el-dialog:hover {
  box-shadow: 0 12px 32px rgba(58, 87, 232, 0.22);
}

.el-table :deep(.el-table__cell) {
  padding: 18px 0;
  border-bottom: 1px solid #f0f2f7;
}

/* 优化状态选择器交互 */
.el-table :deep(.el-select) {
  width: 100%;
}

.el-table :deep(.el-select:hover .el-input__inner) {
  border-color: #3a57e8;
  box-shadow: 0 0 0 3px rgba(58, 87, 232, 0.12);
}

.el-table :deep(.el-select .el-input__inner) {
  padding: 0 14px;
  height: 38px;
  line-height: 38px;
  border-radius: 8px;
  transition: all 0.25s ease;
  border: 1px solid #dcdfe6;
}

/* 优化对话框表单细节 */
.el-dialog__body .el-form-item {
  margin-bottom: 22px;
}

.el-dialog__body .el-form-item__label {
  padding-right: 18px;
  font-weight: 500;
  color: #606266;
  font-size: 14px;
}

.el-dialog__body .el-input__inner,
.el-dialog__body .el-textarea__inner {
  padding: 12px 16px;
  height: auto;
  min-height: 44px;
  border-radius: 8px;
  border: 1px solid #dcdfe6;
  transition: all 0.25s ease;
  font-size: 14px;
}

.el-dialog__body .el-textarea__inner {
  min-height: 120px;
  line-height: 1.6;
}

/* 全局阴影优化 */
.header, .search-form, .el-table, .el-dialog {
  box-shadow: 0 6px 16px rgba(58, 87, 232, 0.12);
  transition: box-shadow 0.35s ease;
}

.header:hover, .search-form:hover, .el-table:hover, .el-dialog:hover {
  box-shadow: 0 10px 28px rgba(58, 87, 232, 0.18);
}

.el-table :deep(.el-table__cell) {
  padding: 16px 0;
  border-bottom: 1px solid #f0f2f7;
}

/* 优化状态选择器交互 */
.el-table :deep(.el-select) {
  width: 100%;
}

.el-table :deep(.el-select:hover .el-input__inner) {
  border-color: #3a57e8;
  box-shadow: 0 0 0 2px rgba(58, 87, 232, 0.1);
}

.el-table :deep(.el-select .el-input__inner) {
  padding: 0 12px;
  height: 36px;
  line-height: 36px;
  border-radius: 6px;
  transition: all 0.2s ease;
  border: 1px solid #dcdfe6;
}

/* 优化对话框表单细节 */
.el-dialog__body .el-form-item {
  margin-bottom: 20px;
}

.el-dialog__body .el-form-item__label {
  padding-right: 16px;
  font-weight: 500;
  color: #606266;
}

.el-dialog__body .el-input__inner,
.el-dialog__body .el-textarea__inner {
  padding: 10px 14px;
  height: auto;
  min-height: 40px;
  border-radius: 6px;
  border: 1px solid #dcdfe6;
  transition: all 0.2s ease;
}

.el-dialog__body .el-textarea__inner {
  min-height: 100px;
  line-height: 1.5;
}

/* 全局阴影优化 */
.header, .search-form, .el-table, .el-dialog {
  box-shadow: 0 4px 12px rgba(58, 87, 232, 0.1);
  transition: box-shadow 0.3s ease;
}

.header:hover, .search-form:hover, .el-table:hover, .el-dialog:hover {
  box-shadow: 0 8px 24px rgba(58, 87, 232, 0.15);
}

.el-table :deep(.el-table__row td) {
  padding: 12px 0;
}

.el-table :deep(.el-select) {
  width: 50%;
  min-width: 120px;
}

.el-table :deep(.el-select .el-input__inner) {
  padding: 0 8px;
  height: 32px;
  line-height: 32px;
  border-radius: 6px;
  transition: all 0.2s ease;
}

/* 对话框表单优化 */
.el-dialog__body .el-form-item__content {
  line-height: 32px;
}

.el-dialog__body .el-input__inner,
.el-dialog__body .el-textarea__inner {
  padding: 8px 12px;
  height: auto;
  min-height: 36px;
}

.el-dialog__body .el-textarea__inner {
  min-height: 80px;
}

.el-table :deep(.el-table__row--striped) {
  background-color: #fafbfe;
}

.el-table :deep(.el-table__row--striped:hover) {
  background-color: #f5f7ff;
}
.el-table :deep(.el-table__header th) {
  background-color: #f8fafc;
  color: #5a6d8a; /* 更柔和的蓝灰色 */
  font-weight: 600;
  font-size: 14px;
  letter-spacing: 0.5px;
}

/* 状态标签样式 */
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

.el-dialog__body .el-form-item {
  margin-bottom: 18px;
}

.el-dialog__body .el-input,
.el-dialog__body .el-select,
.el-dialog__body .el-textarea {
  width: 100%;
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

/* 过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
