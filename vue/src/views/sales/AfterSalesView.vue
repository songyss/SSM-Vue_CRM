<template>
  <div class="after-sales-container">
    <el-card>
      <div class="header">
        <h2>售后订单管理</h2>
      </div>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="客户名称">
          <el-input v-model="searchForm.customerName" placeholder="输入客户名称" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable>
            <el-option label="待处理" value="1" />
            <el-option label="处理中" value="2" />
            <el-option label="已完成" value="3" />
            <el-option label="已关闭" value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请时间">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchAfterSales">搜索</el-button>
        </el-form-item>
      </el-form>

      <!-- 售后订单表格 -->
      <el-table :data="afterSales" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNumber" label="订单号" width="120" />
        <el-table-column prop="customerName" label="客户名称" width="150" />
        <el-table-column prop="totalAmount" label="金额" width="120" align="right">
          <template #default="{ row }">
            ¥{{ parseFloat(row.totalAmount).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="signedDate" label="签约日期" width="120" />
        <el-table-column prop="statusName" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.afterSaleStatus)">
              {{ row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="afterSaleApplyTime" label="申请时间" width="180" />
        <el-table-column prop="afterSaleCompleteTime" label="完成时间" width="180" />
        <el-table-column prop="afterSaleHandlerId" label="处理人ID" width="100" />
        <el-table-column prop="notes" label="订单备注" width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleDetail(row)">详情</el-button>
            <el-button
              v-if="row.afterSaleStatus == 1"
              size="small"
              type="primary"
              @click="handleProcess(row)"
            >
              处理
            </el-button>
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
      <el-dialog v-model="dialogVisible" title="处理售后订单" width="500px">
        <el-form :model="currentOrder" label-width="100px">
          <el-form-item label="ID">
            <el-input v-model="currentOrder.id" disabled />
          </el-form-item>
          <el-form-item label="订单号">
            <el-input v-model="currentOrder.orderNumber" disabled />
          </el-form-item>
          <el-form-item label="客户名称">
            <el-input v-model="currentOrder.customerName" disabled />
          </el-form-item>
          <el-form-item label="处理状态">
            <el-select v-model="currentOrder.afterSaleStatus" placeholder="请选择处理状态">
              <el-option label="处理中" value="2" />
              <el-option label="已完成" value="3" />
              <el-option label="已关闭" value="4" />
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
            <el-button @click="dialogVisible = false">取消</el-button>
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
                <el-tag :type="getStatusTagType(detailOrder.afterSaleStatus)">
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
    </el-card>
  </div>
</template>

<script setup lang="ts">import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { useRouter } from 'vue-router'

// 获取路由实例
const router = useRouter()

// 售后订单数据
const afterSales = ref([])

// 搜索表单
const searchForm = ref({
  customerName: '',
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

// 获取售后订单列表
const fetchAfterSales = async () => {
  try {
    console.log('开始获取售后订单数据...')

    // 页面初始化时或未选择状态时，调用默认接口
    const data = await request.get('/afterSaleOrders/getAfterSaleOrderByStatus')

    console.log('收到数据:', data)

    // 由于拦截器已经处理了响应，直接使用返回的数据
    if (data) {
      // 确保数据是数组格式
      if (Array.isArray(data)) {
        afterSales.value = data
        pagination.value.total = data.length
        ElMessage.success('数据加载成功')
      } else if (data.id) {
        // 如果是单个对象（有id属性），转换为数组
        afterSales.value = [data]
        pagination.value.total = 1
        ElMessage.success('数据加载成功')
      } else {
        // 数据为空或其他情况
        afterSales.value = []
        pagination.value.total = 0
        ElMessage.info('暂无售后订单数据')
      }
    } else {
      // 数据为空
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
  const map = { 1: 'danger', 2: 'warning', 3: 'success', 4: 'info' }
  return map[status] || ''
}

// 查看详情
const handleDetail = (row) => {
  detailOrder.value = { ...row }
  detailDialogVisible.value = true
}

// 处理订单
const handleProcess = (row) => {
  currentOrder.value = { ...row }
  dialogVisible.value = true
}

// 更新订单状态
const updateOrderStatus = async () => {
  if (!currentOrder.value.afterSaleStatus) {
    ElMessage.warning('请选择处理状态')
    return
  }

  try {
    await request.get('/afterSaleOrders/updateAfterSaleOrderStatus', {
      params: {
        id: currentOrder.value.id,
        afterSaleStatus: currentOrder.value.afterSaleStatus
      }
    })

    ElMessage.success('处理成功')
    dialogVisible.value = false
    fetchAfterSales() // 重新加载数据
  } catch (error) {
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
    console.error(error)
  }
}

// 初始化加载数据
onMounted(() => {
  console.log('页面初始化，开始获取售后订单数据')
  fetchAfterSales()
})
</script>







<style scoped>
.after-sales-container {
  padding: 20px;
}
.header {
  margin-bottom: 20px;
}
.pagination {
  margin-top: 20px;
  justify-content: center;
}
</style>
