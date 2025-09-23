<template>
  <div class="logs-view">
    <!-- 搜索筛选区域 -->
    <div class="search-bar">
      <div class="search-form">
        <div class="form-item">
          <label>操作地址：</label>
          <el-input v-model="searchParams.ipAddress" placeholder="请输入IP地址" clearable></el-input>
        </div>
        <div class="form-item">
          <label>操作内容：</label>
          <el-input v-model="searchParams.operation" placeholder="请输入操作内容" clearable></el-input>
        </div>
        <div class="form-item">
          <label>操作对象类型：</label>
          <el-input v-model="searchParams.targetType" placeholder="请输入对象类型" clearable></el-input>
        </div>
        <div class="form-item">
          <label>操作人ID：</label>
          <el-input v-model="searchParams.operatorId" placeholder="请输入操作人ID" clearable></el-input>
        </div>
        <div class="form-item">
          <label>操作时间：</label>
          <el-date-picker
            v-model="searchParams.timeRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD HH:mm:ss"
            format="YYYY-MM-DD HH:mm:ss"
            @change="handleTimeChange">
          </el-date-picker>
        </div>
        <div class="form-actions">
          <el-button type="primary" @click="searchLogs">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </div>
      </div>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-bar">
      <div class="toolbar">
        <el-button size="small" @click="refreshLogs">
          <el-icon><Refresh /></el-icon>
        </el-button>
      </div>
    </div>

    <!-- 日志表格 -->
    <div class="logs-table">
      <el-table
        :data="logs"
        border
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="id" label="日志编号" width="80"></el-table-column>
        <el-table-column prop="operation" label="操作内容" width="150"></el-table-column>
        <el-table-column prop="targetType" label="对象类型" width="120"></el-table-column>
        <el-table-column prop="targetId" label="对象ID" width="100"></el-table-column>
        <el-table-column prop="operatorId" label="操作人ID" width="100"></el-table-column>
        <el-table-column prop="ipAddress" label="操作地址" width="140"></el-table-column>
        <el-table-column prop="oldValue" label="旧值" width="120"></el-table-column>
        <el-table-column prop="newValue" label="新值" width="120"></el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="160"></el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
    <div class="pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        background>
      </el-pagination>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'

// 定义日志项类型，与后端 OperateLogs 实体类对应
interface LogItem {
  id: number
  operatorId: number
  operation: string
  targetType: string
  targetId: number
  oldValue: string
  newValue: string
  ipAddress: string
  createTime: string
}

// 搜索参数，与后端接口参数对应
interface SearchParams {
  operatorId: string
  operation: string
  targetType: string
  ipAddress: string
  timeRange: [string, string] | null
}

// 响应式数据
const logs = ref<LogItem[]>([])
const searchParams = ref<SearchParams>({
  operatorId: '',
  operation: '',
  targetType: '',
  ipAddress: '',
  timeRange: null
})
const currentPage = ref<number>(1)
const pageSize = ref<number>(10)
const total = ref<number>(0)
const loading = ref<boolean>(false)

// 新增：状态标志（关键！控制提示信息类型）
const isFirstLoad = ref<boolean>(true) // 是否首次加载全部数据
const isSearchAction = ref<boolean>(false) // 是否是搜索筛选操作

// 获取所有日志数据（默认加载）
const fetchAllLogs = async () => {
  loading.value = true
  try {
    console.log('开始获取所有日志数据，页码:', currentPage.value, '每页数量:', pageSize.value)

    // 发送请求获取分页数据
    const result = await request.get('/api/logs/list', {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    const response = result.data
    console.log('后端返回的原始数据:', response)

    // 处理分页数据
    if (response && response.code === 200) {
      logs.value = response.data || []
      total.value = response.total || 0

      // 提示逻辑：首次加载全部数据 → 显示总条数；翻页 → 显示当前页条数
      if (isFirstLoad.value) {
        ElMessage.success(`日志数据加载成功，共${total.value}条记录`)
        isFirstLoad.value = false // 首次加载后重置标志，后续翻页不再显示总条数
      } else {
        ElMessage.info(`当前页加载成功，共${logs.value.length}条数据`)
      }
    } else {
      logs.value = []
      total.value = 0
      ElMessage.error(response?.message || '获取日志数据失败')
    }
  } catch (error: any) {
    logs.value = []
    total.value = 0
    ElMessage.error('获取日志数据失败: ' + (error.message || '未知错误'))
    console.error('获取日志数据失败:', error)
  } finally {
    loading.value = false
    isSearchAction.value = false // 重置搜索标志（非搜索操作结束）
  }
}

// 根据条件搜索日志
const searchLogs = async () => {
  loading.value = true
  isSearchAction.value = true // 标记为搜索操作
  try {
    const params: any = {
      page: currentPage.value,
      size: pageSize.value
    }

    // 添加搜索条件
    if (searchParams.value.operatorId) {
      params.operatorId = searchParams.value.operatorId
    }
    if (searchParams.value.operation) {
      params.operation = searchParams.value.operation
    }
    if (searchParams.value.targetType) {
      params.targetType = searchParams.value.targetType
    }
    if (searchParams.value.ipAddress) {
      params.ipAddress = searchParams.value.ipAddress
    }
    if (searchParams.value.timeRange) {
      params.startTime = searchParams.value.timeRange[0]
      params.endTime = searchParams.value.timeRange[1]
    }

    const response = await request.get('/api/logs/search', { params })
    console.log('搜索返回的数据:', response)

    // 处理分页数据
    if (response && response.code === 200) {
      logs.value = response.data || []
      total.value = response.total || 0

      // 搜索操作：无论是否翻页，都显示总条数（符合需求）
      ElMessage.success(`搜索完成，共${total.value}条记录，当前页显示${logs.value.length}条`)
    } else {
      logs.value = []
      total.value = 0
      ElMessage.error(response?.message || '搜索日志数据失败')
    }
  } catch (error: any) {
    logs.value = []
    total.value = 0
    ElMessage.error('搜索日志数据失败: ' + (error.message || '未知错误'))
    console.error('搜索日志数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 重置搜索条件
const resetSearch = () => {
  searchParams.value = {
    operatorId: '',
    operation: '',
    targetType: '',
    ipAddress: '',
    timeRange: null
  }
  currentPage.value = 1
  isFirstLoad.value = false // 重置后不属于首次加载，按翻页逻辑提示
  fetchAllLogs()
}

// 处理时间范围变化
const handleTimeChange = (value: [string, string] | null) => {
  searchParams.value.timeRange = value
}

// 刷新日志（重置为首次加载全部数据，显示总条数）
const refreshLogs = () => {
  currentPage.value = 1
  isFirstLoad.value = true // 刷新视为“重新首次加载”，显示总条数
  fetchAllLogs()
}

// 处理分页大小变化
const handleSizeChange = (val: number) => {
  console.log('分页大小变化:', val)
  pageSize.value = val
  currentPage.value = 1 // 切换每页条数后重置到第一页
  if (isSearchAction.value) {
    searchLogs() // 搜索状态下切换条数 → 仍按搜索逻辑显示总条数
  } else {
    fetchAllLogs() // 非搜索状态下切换条数 → 按翻页逻辑显示当前页条数
  }
}

// 处理当前页变化
const handleCurrentChange = (val: number) => {
  console.log('当前页变化:', val)
  currentPage.value = val
  if (isSearchAction.value) {
    searchLogs() // 搜索状态下翻页 → 仍按搜索逻辑显示总条数
  } else {
    fetchAllLogs() // 非搜索状态下翻页 → 按翻页逻辑显示当前页条数
  }
}

// 检查是否有搜索条件
const hasSearchParams = () => {
  return searchParams.value.operatorId ||
         searchParams.value.operation ||
         searchParams.value.targetType ||
         searchParams.value.ipAddress ||
         searchParams.value.timeRange
}

// 组件挂载时获取所有日志
onMounted(() => {
  fetchAllLogs()
})
</script>

<style scoped>
.logs-view {
  padding: 20px;
  background-color: #fff;
}

.search-bar {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  align-items: center;
}

.form-item {
  display: flex;
  flex-direction: column;
  min-width: 200px;
}

.form-item label {
  margin-bottom: 5px;
  font-size: 12px;
  color: #666;
}

.form-actions {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 10px 0;
  border-bottom: 1px solid #ebeef5;
}

.toolbar {
  display: flex;
  gap: 10px;
}

.logs-table {
  margin-bottom: 20px;
}

.pagination {
  text-align: right;
  margin-top: 20px;
  padding: 20px 0;
}

/* 自定义样式 */
.el-table .cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.el-table th {
  background-color: #f5f7fa;
}

/* 按钮样式 */
.el-button--small {
  padding: 6px 12px;
  font-size: 12px;
}

/* 图标样式 */
.el-icon {
  font-size: 14px;
}
</style>
