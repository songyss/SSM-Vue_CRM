<template>
  <div class="leads-container">
    <!-- 商机管理区域 -->
    <div class="leads-management">
      <div class="header">
        <h3>商机管理</h3>
        <el-button type="primary" @click="handleAddLead">+ 新增商机</el-button>
      </div>

      <!-- 提示信息 -->
      <el-alert
        title="提示：当商机阶段更新为'赢单'时，系统将自动创建对应的订单"
        type="info"
        show-icon
        :closable="false"
        style="margin-bottom: 20px;"
      />

      <!-- 搜索筛选区域 -->
      <el-card class="filter-card">
        <el-form :inline="true" :model="searchForm" class="demo-form-inline">
          <el-form-item label="客户名称">
            <el-input v-model="searchForm.customerName" placeholder="请输入客户名称" />
          </el-form-item>

          <el-form-item label="商机阶段">
            <el-select v-model="searchForm.stage" placeholder="请选择商机阶段" clearable>
              <el-option label="全部" value="" />
              <el-option label="初步接触" value="1" />
              <el-option label="需求分析" value="2" />
              <el-option label="方案报价" value="3" />
              <el-option label="谈判审核" value="4" />
              <el-option label="赢单" value="5" />
              <el-option label="输单" value="6" />
            </el-select>
          </el-form-item>

          <el-form-item label="预计成交日期">
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

      <!-- 商机列表 -->
      <el-table
        :data="leadsList"
        style="width: 100%"
        border
        @sort-change="handleSortChange"
      >
        <!-- 表格列定义 -->
        <el-table-column type="selection" width="55" />

        <el-table-column prop="name" label="商机名称" width="180" sortable="custom" />
        <el-table-column prop="customerName" label="客户名称" width="160" sortable="custom" />
        <el-table-column prop="amount" label="预估金额" width="140" sortable="custom">
          <template #default="scope">
            <span class="amount">¥{{ scope.row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stage" label="商机阶段" width="140" sortable="custom">
          <template #default="scope">
            <el-tag
              :type="stageTagType[scope.row.stage]"
              :effect="'light'"
            >
              {{ getStageName(scope.row.stage) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="probability" label="赢率" width="100" sortable="custom">
          <template #default="scope">
            <el-progress
              :percentage="scope.row.probability * 100"
              :status="getProgressStatus(scope.row.probability)"
              :show-text="true"
            />
          </template>
        </el-table-column>
        <el-table-column prop="expectedCloseDate" label="预计成交日期" width="160" sortable="custom" />
        <el-table-column prop="assigneeName" label="负责人" width="120" sortable="custom" />

        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="small" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="text" @click="handleDelete(scope.row)">删除</el-button>
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
        background
      />
    </div>

    <!-- 商机详情对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form :model="currentLead" :rules="formRules" ref="leadForm" label-width="100px">
        <el-form-item label="商机名称" prop="name">
          <el-input v-model="currentLead.name" />
        </el-form-item>
        <el-form-item label="客户名称" prop="customerId">
          <el-select v-model="currentLead.customerId" placeholder="请选择客户" style="width: 100%">
            <el-option label="客户A" :value="101" />
            <el-option label="客户B" :value="102" />
            <el-option label="客户C" :value="103" />
          </el-select>
        </el-form-item>
        <el-form-item label="预估金额" prop="amount">
          <el-input-number v-model="currentLead.amount" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="商机阶段" prop="stage">
          <el-select v-model="currentLead.stage" placeholder="请选择商机阶段" style="width: 100%">
            <el-option label="初步接触" :value="1" />
            <el-option label="需求分析" :value="2" />
            <el-option label="方案报价" :value="3" />
            <el-option label="谈判审核" :value="4" />
            <el-option label="赢单" :value="5" />
            <el-option label="输单" :value="6" />
          </el-select>
        </el-form-item>
        <el-form-item label="赢率" prop="probability">
          <el-slider v-model="currentLead.probability" :min="0" :max="1" :step="0.1" show-input />
        </el-form-item>
        <el-form-item label="预计成交日期" prop="expectedCloseDate">
          <el-date-picker
            v-model="currentLead.expectedCloseDate"
            type="date"
            placeholder="请选择预计成交日期"
            value-format="YYYY-MM-DD"
            format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="负责人" prop="assigneeId">
          <el-select v-model="currentLead.assigneeId" placeholder="请选择负责人" style="width: 100%">
            <el-option label="张三" :value="1001" />
            <el-option label="李四" :value="1002" />
            <el-option label="王五" :value="1003" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="currentLead.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveLead">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

// 商机阶段标签样式映射
const stageTagType = {
  1: 'info',      // 初步接触
  2: 'warning',   // 需求分析
  3: 'primary',   // 方案报价
  4: 'success',   // 谈判审核
  5: 'success',   // 赢单
  6: 'danger'     // 输单
}

// 搜索表单数据模型
const searchForm = reactive({
  customerName: '',      // 客户名称搜索条件
  stage: '',             // 商机阶段筛选条件
  dateRange: [] as string[] // 预计成交日期范围筛选条件
})

// 分页配置
const pagination = reactive({
  currentPage: 1,   // 当前页码
  pageSize: 10,     // 每页显示条数
  total: 0,         // 总数据量
})

// 排序参数
const sortParams = reactive({
  prop: '',
  order: ''
})

// 商机列表数据
const leadsList = ref([])

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)

// 当前编辑的商机
const currentLead = ref({
  id: null,
  name: '',
  customerId: null,
  customerName: '',
  amount: 0,
  stage: 1,
  stageName: '',
  probability: 0.1,
  expectedCloseDate: '',
  assigneeId: null,
  assigneeName: '',
  description: '',
  createTime: '',
  updateTime: ''
})

// 表单验证规则
const formRules = {
  name: [{ required: true, message: '请输入商机名称', trigger: 'blur' }],
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  amount: [{ required: true, message: '请输入预估金额', trigger: 'blur' }],
  stage: [{ required: true, message: '请选择商机阶段', trigger: 'change' }],
  probability: [{ required: true, message: '请选择赢率', trigger: 'change' }],
  expectedCloseDate: [{ required: true, message: '请选择预计成交日期', trigger: 'change' }],
  assigneeId: [{ required: true, message: '请选择负责人', trigger: 'change' }]
}

// 获取商机列表数据（分页）
const fetchLeadsList = async () => {
  try {
    // 构造请求参数
    const params: any = {
      page: pagination.currentPage,
      size: pagination.pageSize
    }

    // 添加搜索条件
    if (searchForm.customerName) {
      params.customerName = searchForm.customerName
    }
    if (searchForm.stage) {
      params.stage = searchForm.stage
    }

    // 处理日期范围参数
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0];
      params.endDate = searchForm.dateRange[1];
    }

    // 添加排序参数
    if (sortParams.prop && sortParams.order) {
      params.sortBy = sortParams.prop;
      params.sortOrder = sortParams.order === 'ascending' ? 'asc' : 'desc';
    }

    console.log('请求参数:', params);

    // 调用后端分页接口获取商机数据
    const response = await request.get('/opportunities/page', { params })
    console.log('后端返回数据:', response);

    // 正确处理响应数据结构
    if (response && response.code === 200) {
      leadsList.value = response.data.list || [];
      pagination.total = response.data.total || 0;
    } else {
      ElMessage.error('获取商机列表失败: ' + (response?.message || '未知错误'))
      leadsList.value = [];
      pagination.total = 0;
    }
  } catch (error) {
    console.error('获取商机列表异常:', error);
    ElMessage.error('获取商机列表失败: ' + (error.message || '网络异常'))
    leadsList.value = [];
    pagination.total = 0;
  }
}

// 处理排序变化
const handleSortChange = (sortInfo: { prop: string; order: string }) => {
  sortParams.prop = sortInfo.prop;
  sortParams.order = sortInfo.order;
  fetchLeadsList(); // 重新获取数据以应用排序
}

// 搜索处理函数
const handleSearch = () => {
  pagination.currentPage = 1
  fetchLeadsList()
}

// 重置搜索条件
const resetSearch = () => {
  Object.assign(searchForm, {
    customerName: '',
    stage: '',
    dateRange: [],
  })
  handleSearch()
}

// 分页大小改变事件处理
const handleSizeChange = (val: number) => {
  pagination.pageSize = val
  pagination.currentPage = 1 // 重置到第一页
  fetchLeadsList()
}

// 当前页码改变事件处理
const handleCurrentChange = (val: number) => {
  pagination.currentPage = val
  fetchLeadsList()
}

// 操作按钮事件处理函数
const handleAddLead = () => {
  dialogTitle.value = '新增商机'
  isEdit.value = false
  Object.assign(currentLead.value, {
    id: null,
    name: '',
    customerId: null,
    customerName: '',
    amount: 0,
    stage: 1,
    stageName: '',
    probability: 0.1,
    expectedCloseDate: '',
    assigneeId: null,
    assigneeName: '',
    description: '',
    createTime: '',
    updateTime: ''
  })
  dialogVisible.value = true
}

const handleView = (row: any) => {
  dialogTitle.value = '查看商机'
  Object.assign(currentLead.value, row)
  isEdit.value = true
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑商机'
  Object.assign(currentLead.value, row)
  isEdit.value = true
  dialogVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该商机吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 调用后端删除接口
      const response = await request.delete(`/opportunities/${row.id}`)
      if (response && response.code === 200) {
        ElMessage.success('删除成功')
        fetchLeadsList()
      } else {
        ElMessage.error('删除失败: ' + (response?.message || '未知错误'))
      }
    } catch (error) {
      ElMessage.error('删除失败: ' + (error.message || '网络异常'))
    }
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

// 对话框关闭处理
const handleDialogClose = () => {
  // 可以在这里重置表单状态
}

// 保存商机
const saveLead = async () => {
  try {
    // 检查是否从其他阶段更新为"赢单"
    const isStageChangedToWin = isEdit.value &&
      currentLead.value.stage === 5 &&
      leadsList.value.find((item: any) => item.id === currentLead.value.id)?.stage !== 5;

    let response;
    if (isEdit.value) {
      // 更新商机
      response = await request.patch('/opportunities/update', currentLead.value)
    } else {
      // 新增商机
      response = await request.put('/opportunities/add', currentLead.value)
    }

    if (response && response.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
      // 如果是从其他阶段更新为"赢单"，提示用户订单已自动创建
      if (isStageChangedToWin) {
        ElMessage.success('商机已更新为赢单，系统已自动创建订单');
      }
      dialogVisible.value = false
      fetchLeadsList()
    } else {
      ElMessage.error('保存失败: ' + (response?.message || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('保存失败: ' + (error.message || '网络异常'))
  }
}

// 组件挂载时获取数据
onMounted(() => {
  fetchLeadsList()
})

// 根据商机阶段ID获取阶段名称
const getStageName = (stageId: number): string => {
  const stageMap: Record<number, string> = {
    1: '初步接触',
    2: '需求分析',
    3: '方案报价',
    4: '谈判审核',
    5: '赢单',
    6: '输单'
  }
  return stageMap[stageId] || '未知阶段'
}

// 根据赢率获取进度条状态
const getProgressStatus = (probability: number): string => {
  if (probability < 0.3) return 'exception'
  if (probability < 0.7) return 'warning'
  return 'success'
}
</script>

<style scoped>
.leads-container {
  padding: 20px;
}

.leads-management {
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
.leads-container {
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
</style>
