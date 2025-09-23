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

      <!-- 客户跟进信息表 -->
      <el-card class="customer-followup-card" style="margin-bottom: 20px;">
        <template #header>
          <div class="card-header">
            <span>我的客户跟进</span>
            <el-button class="button" type="text" @click="fetchMyCustomers">刷新</el-button>
          </div>
        </template>
        <el-table
          :data="myCustomers"
          style="width: 100%"
          max-height="300"
          :loading="customerLoading"
        >
          <el-table-column prop="name" label="客户名称" width="150" />
          <el-table-column prop="position" label="职位" width="120" />
          <el-table-column prop="phone" label="联系电话" width="120" />
          <el-table-column prop="company" label="公司" width="150" />
          <el-table-column prop="status" label="状态" width="120">
            <template #default="scope">
              <el-tag :type="getStatusTagType(scope.row.status)">
                {{ getStatusName(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="lastFollowUPTime" label="最后联系时间" width="120">
            <template #default="scope">
              {{ formatDateTime(scope.row.lastFollowUPTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" type="primary" link @click="handleCustomerView(scope.row)">查看</el-button>
              <el-button size="small" type="primary" link @click="handleViewFollowUps(scope.row)">跟进记录</el-button>
              <el-button size="small" type="primary" link @click="handleCustomerEdit(scope.row)">跟进</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-container" style="margin-top: 15px; display: flex; justify-content: flex-end;">
          <el-pagination
            v-model:current-page="customerPagination.currentPage"
            v-model:page-size="customerPagination.pageSize"
            :page-sizes="[5, 10, 20]"
            :total="customerPagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleCustomerSizeChange"
            @current-change="handleCustomerCurrentChange"
          />
        </div>
      </el-card>

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

        <el-table-column prop="name" label="商机名称" width="150" sortable="custom" />
        <el-table-column prop="customerName" label="客户名称" width="120" sortable="custom" />
        <el-table-column prop="amount" label="预估金额" width="120" sortable="custom">
          <template #default="scope">
            <span class="amount">¥{{ scope.row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stage" label="商机阶段" width="120" sortable="custom">
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
        <el-table-column prop="expectedCloseDate" label="预计成交日期" width="130" sortable="custom" />
        <el-table-column prop="assigneeName" label="负责人" width="100" sortable="custom" />

        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" link @click="handleView(scope.row)">查看</el-button>
            <el-button size="small" type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" link @click="handleDelete(scope.row)">删除</el-button>
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
      <el-form
        :model="currentLead"
        :rules="formRules"
        ref="leadForm"
        label-width="100px"
        :disabled="!isEdit"
      >
        <el-form-item label="商机名称" prop="name">
          <el-input v-model="currentLead.name" />
        </el-form-item>
        <el-form-item label="客户名称" prop="customerId">
          <el-select
            v-model="currentLead.customerId"
            placeholder="请选择客户"
            style="width: 100%"
            filterable
            clearable
          >
            <el-option
              v-for="customer in customers"
              :key="customer.id"
              :label="customer.name"
              :value="customer.id"
            />
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
            format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="负责人" prop="assigneeId">
          <el-select
            v-model="currentLead.assigneeId"
            placeholder="请选择负责人"
            style="width: 100%"
            filterable
            clearable
          >
            <el-option
              v-for="employee in employees"
              :key="employee.id"
              :label="employee.name"
              :value="employee.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="currentLead.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button v-if="isEdit" type="primary" @click="saveLead">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 客户详情对话框 -->
    <el-dialog
      title="客户详情"
      v-model="customerDialogVisible"
      width="600px"
    >
      <el-form :model="currentCustomer" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="客户名称：">
              <span>{{ currentCustomer.name }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别：">
              <span>{{ currentCustomer.sex === 1 ? '男' : '女' }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="联系电话：">
              <span>{{ currentCustomer.phone }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期：">
              <span>{{ currentCustomer.borndate }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="公司：">
              <span>{{ currentCustomer.company }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位：">
              <span>{{ currentCustomer.position }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="来源：">
          <span>{{ currentCustomer.source }}</span>
        </el-form-item>

        <el-form-item label="备注：">
          <span>{{ currentCustomer.notes }}</span>
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item label="状态：">
              <span>{{ getStatusName(currentCustomer.status) }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最后联系时间：">
              <span>{{ formatDateTime(currentCustomer.lastFollowUPTime) }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="customerDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

      <!-- 客户跟进对话框 -->
      <el-dialog
        title="客户跟进"
        v-model="followUpDialogVisible"
        width="800px"
        @close="handleFollowUpClose"
      >
        <el-form
          :model="followUpForm"
          :rules="followUpRules"
          ref="followUpFormRef"
          label-width="120px"
          label-position="left"
          class="follow-up-form"
        >
          <el-row :gutter="20">
            <!-- 第一行：客户名称和当前状态 -->
            <el-col :span="12">
              <el-form-item label="客户名称：">
                <el-input v-model="followUpForm.customerName" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="客户当前状态：">
                <el-tag :type="getStatusTagType(followUpForm.customerStatus)">
                  {{ getStatusName(followUpForm.customerStatus) }}
                </el-tag>
              </el-form-item>
            </el-col>

            <!-- 第二行：更新客户状态 -->
            <el-col :span="24">
              <el-form-item label="更新客户状态：">
                <el-select v-model="followUpForm.newCustomerStatus" placeholder="请选择客户状态" style="width: 100%">
                  <el-option label="无效" :value="0" />
                  <el-option label="新客户" :value="1" />
                  <el-option label="有意向" :value="2" />
                  <el-option label="已成交" :value="3" />
                </el-select>
              </el-form-item>
            </el-col>

            <!-- 第三行：跟进方式 -->
            <el-col :span="24">
              <el-form-item label="跟进方式：" prop="type">
                <el-select v-model="followUpForm.type" placeholder="请选择跟进方式" style="width: 100%">
                  <el-option label="电话" :value="1" />
                  <el-option label="面谈" :value="2" />
                  <el-option label="邮件" :value="3" />
                  <el-option label="微信" :value="4" />
                  <el-option label="其他" :value="5" />
                </el-select>
              </el-form-item>
            </el-col>

            <!-- 第四行：跟进内容 -->
            <el-col :span="24">
              <el-form-item label="跟进内容：" prop="content">
                <el-input
                  v-model="followUpForm.content"
                  type="textarea"
                  :rows="6"
                  placeholder="请输入跟进内容"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>

            <!-- 第五行：下次计划 -->
            <el-col :span="24">
              <el-form-item label="下次计划：">
                <el-input
                  v-model="followUpForm.nextPlan"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入下次计划"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>

            <!-- 第六行：下次联系时间 -->
            <el-col :span="24">
              <el-form-item label="下次联系时间：" prop="nextContactTime">
                <el-date-picker
                  v-model="followUpForm.nextContactTime"
                  type="datetime"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  format="YYYY-MM-DD HH:mm:ss"
                  placeholder="请选择下次联系时间"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="followUpDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitFollowUp">提交</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 跟进记录对话框 -->
      <el-dialog
        title="客户跟进记录"
        v-model="followUpRecordsDialogVisible"
        width="800px"
      >
        <el-table :data="followUpRecords" style="width: 100%" max-height="400">
          <el-table-column prop="type" label="跟进方式" width="100">
            <template #default="scope">
              {{ getFollowUpTypeName(scope.row.type) }}
            </template>
          </el-table-column>
          <el-table-column prop="content" label="跟进内容" min-width="200" show-overflow-tooltip />
          <el-table-column prop="nextPlan" label="下次计划" min-width="150" show-overflow-tooltip />
          <el-table-column prop="nextContactTime" label="下次联系时间" width="150">
            <template #default="scope">
              {{ formatDateTime(scope.row.nextContactTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="150">
            <template #default="scope">
              {{ formatDateTime(scope.row.createTime) }}
            </template>
          </el-table-column>
        </el-table>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="followUpRecordsDialogVisible = false">关闭</el-button>
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

// 客户和员工数据
const customers = ref([])
const employees = ref([])

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
      // 格式化日期范围
      const startDate = searchForm.dateRange[0];
      const endDate = searchForm.dateRange[1];

      if (startDate) {
        if (startDate instanceof Date) {
          params.startDate = startDate.toISOString().split('T')[0];
        } else if (typeof startDate === 'string' && startDate.includes('T')) {
          params.startDate = startDate.split('T')[0];
        } else {
          params.startDate = startDate;
        }
      }

      if (endDate) {
        if (endDate instanceof Date) {
          params.endDate = endDate.toISOString().split('T')[0];
        } else if (typeof endDate === 'string' && endDate.includes('T')) {
          params.endDate = endDate.split('T')[0];
        } else {
          params.endDate = endDate;
        }
      }
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
    if (response && response.data.code === 200) {
      leadsList.value = response.data.data.list || [];
      pagination.total = response.data.data.total || 0;
    } else {
      ElMessage.error('获取商机列表失败: ' + (response?.data.message || '未知错误'))
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

// 获取客户列表数据
const fetchCustomers = async () => {
  try {
    const response = await request.get('/customer/allList')
    if (response && response.data.code === 200) {
      customers.value = response.data.data || []
    } else {
      ElMessage.error('获取客户列表失败: ' + (response?.data.message || '未知错误'))
    }
  } catch (error) {
    console.error('获取客户列表异常:', error)
    ElMessage.error('获取客户列表失败: ' + (error.message || '网络异常'))
  }
}

// 获取员工列表数据
const fetchEmployees = async () => {
  try {
    const response = await request.get('/employee/allOnList')
    if (response && response.data.code === 200) {
      employees.value = response.data || []
    } else {
      ElMessage.error('获取员工列表失败: ' + (response?.data.message || '未知错误'))
    }
  } catch (error) {
    console.error('获取员工列表异常:', error)
    ElMessage.error('获取员工列表失败: ' + (error.message || '网络异常'))
  }
}

// 客户跟进相关数据
const myCustomers = ref([])
const customerLoading = ref(false)
const customerPagination = reactive({
  currentPage: 1,
  pageSize: 5,
  total: 0
})

// 获取我的客户列表
const fetchMyCustomers = async () => {
  try {
    customerLoading.value = true
    console.log('开始获取客户列表...')

    // 这里需要根据实际的用户信息获取当前员工ID
    // 暂时使用模拟数据，实际项目中应从用户信息中获取
    const userInfoStr = localStorage.getItem('crm_userInfo')
    let employeeId = 1 // 默认值

    if (userInfoStr) {
      const userInfo = JSON.parse(userInfoStr)
      employeeId = userInfo.id || 1
    }

    const params = {
      page: customerPagination.currentPage,
      size: customerPagination.pageSize,
      employeeId: employeeId // 根据当前员工ID获取分配的客户
    }

    console.log('请求参数:', params)

    // 调用后端接口获取分配给当前员工的客户列表
    const response = await request.get('/customer/myCustomers', { params })

    console.log('响应数据:', response)

    if (response && response.data.code === 200) {
      myCustomers.value = response.data.data.list || []
      customerPagination.total = response.data.data.total || 0
      console.log('成功获取客户列表:', myCustomers.value)
    } else {
      ElMessage.error('获取客户列表失败: ' + (response?.data.message || '未知错误'))
      myCustomers.value = []
      customerPagination.total = 0
    }
  } catch (error) {
    console.error('获取客户列表异常:', error)
    ElMessage.error('获取客户列表失败: ' + (error.message || '网络异常'))
    myCustomers.value = []
    customerPagination.total = 0
  } finally {
    customerLoading.value = false
  }
}

// 客户分页相关方法
const handleCustomerSizeChange = (val: number) => {
  customerPagination.pageSize = val
  customerPagination.currentPage = 1
  fetchMyCustomers()
}

const handleCustomerCurrentChange = (val: number) => {
  customerPagination.currentPage = val
  fetchMyCustomers()
}

// 客户详情对话框相关数据
const customerDialogVisible = ref(false)
const currentCustomer = ref({} as any)

// 客户操作方法
const handleCustomerView = (row: any) => {
  currentCustomer.value = { ...row }
  customerDialogVisible.value = true
}

// 客户跟进对话框相关数据
const followUpDialogVisible = ref(false)
const followUpForm = ref({
  customerId: 0,
  customerName: '',
  customerStatus: 1, // 客户当前状态
  newCustomerStatus: null, // 新的客户状态
  executorId: 0, // 跟进人ID
  type: '', // 跟进方式（1-电话，2-面谈，3-邮件，4-微信，5-其他）
  content: '', // 跟进内容
  nextPlan: '', // 下次计划
  nextContactTime: '' // 下次联系时间
})

// 跟进记录相关数据
const followUpRecordsDialogVisible = ref(false)
const followUpRecords = ref([])

const followUpFormRef = ref()

// 跟进表单验证规则
const followUpRules = {
  type: [{ required: true, message: '请选择跟进方式', trigger: 'change' }],
  content: [{ required: true, message: '请输入跟进内容', trigger: 'blur' }],
  nextContactTime: [{ required: true, message: '请选择下次联系时间', trigger: 'change' }]
}

// 客户操作方法

const handleCustomerEdit = (row: any) => {
  // 获取当前用户ID（实际项目中应从用户信息中获取）
  const userInfoStr = localStorage.getItem('crm_userInfo')
  let userId = 1 // 默认值

  if (userInfoStr) {
    try {
      const userInfo = JSON.parse(userInfoStr)
      userId = userInfo.id || 1
    } catch (e) {
      console.error('解析用户信息失败:', e)
      userId = 1 // 使用默认值
    }
  }

  followUpForm.value = {
    customerId: row.id,
    customerName: row.name,
    customerStatus: row.status || 1, // 客户当前状态
    newCustomerStatus: null, // 新的客户状态
    executorId: userId, // 设置当前用户为跟进人
    type: '',
    content: '',
    nextPlan: '',
    nextContactTime: ''
  }
  followUpDialogVisible.value = true
}

// 跟进对话框关闭处理
const handleFollowUpClose = () => {
  // 重置表单
  followUpForm.value = {
    customerId: 0,
    executorId: 0,
    type: '',
    content: '',
    nextPlan: '',
    nextContactTime: ''
  }

  // 清除表单验证
  if (followUpFormRef.value) {
    followUpFormRef.value.resetFields()
  }
}

// 提交跟进记录
const submitFollowUp = async () => {
  if (!followUpFormRef.value) return

  await followUpFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        // 确保必填字段都有值
        if (!followUpForm.value.executorId) {
          ElMessage.error('无法获取当前用户信息，请重新登录')
          return
        }

        // 创建要提交的数据对象，只包含必要的字段
        const formData = {
          customerId: followUpForm.value.customerId,
          executorId: followUpForm.value.executorId,
          type: followUpForm.value.type,
          content: followUpForm.value.content,
          nextPlan: followUpForm.value.nextPlan,
          nextContactTime: followUpForm.value.nextContactTime
        }

        // 调用后端接口保存跟进记录
        const followUpResponse = await request.put('/customerFollows/add', formData)

        if (followUpResponse && followUpResponse.data.code !== 200) {
          ElMessage.error('跟进记录提交失败: ' + (followUpResponse?.data.message || '未知错误'))
          return
        }

        // 如果选择了新的客户状态，则更新客户状态
        if (followUpForm.value.newCustomerStatus !== null &&
            followUpForm.value.newCustomerStatus !== undefined &&
            followUpForm.value.newCustomerStatus !== followUpForm.value.customerStatus) {

          const statusUpdateResponse = await request.patch('/customer/status', {
            id: followUpForm.value.customerId,
            status: followUpForm.value.newCustomerStatus
          })

          if (statusUpdateResponse && statusUpdateResponse.data.code === 200) {
            ElMessage.success('跟进记录提交成功，客户状态更新成功')
          } else {
            ElMessage.success('跟进记录提交成功，但客户状态更新失败: ' + (statusUpdateResponse?.data.message || '未知错误'))
          }
        } else {
          ElMessage.success('跟进记录提交成功')
        }

        followUpDialogVisible.value = false
        // 刷新客户列表和分页信息
        await fetchMyCustomers()
      } catch (error) {
        console.error('提交跟进记录异常:', error)
        ElMessage.error('跟进记录提交失败: ' + (error.message || '网络异常'))
      }
    }
  })
}

// 查看客户跟进记录
const handleViewFollowUps = async (row: any) => {
  try {
    // 获取客户的跟进记录
    const response = await request.get(`/customerFollows/customer/${row.id}`)

    if (response && response.data.code === 200) {
      followUpRecords.value = response.data.data || []
      followUpRecordsDialogVisible.value = true
    } else {
      ElMessage.error('获取跟进记录失败: ' + (response?.data.message || '未知错误'))
    }
  } catch (error) {
    console.error('获取跟进记录异常:', error)
    ElMessage.error('获取跟进记录失败: ' + (error.message || '网络异常'))
  }
}

// 根据跟进方式ID获取名称
const getFollowUpTypeName = (typeId: number): string => {
  const typeMap: Record<number, string> = {
    1: '电话',
    2: '面谈',
    3: '邮件',
    4: '微信',
    5: '其他'
  }
  return typeMap[typeId] || '未知'
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
  isEdit.value = true  // 修改为true，允许编辑和保存
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
  isEdit.value = false  // 查看模式
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
      if (response && response.data.code === 200) {
        ElMessage.success('删除成功')
        fetchLeadsList()
      } else {
        ElMessage.error('删除失败: ' + (response?.data.message || '未知错误'))
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
    // 格式化日期字段
    const leadToSave = { ...currentLead.value };

    // 处理预计成交日期格式
    if (leadToSave.expectedCloseDate) {
      if (leadToSave.expectedCloseDate instanceof Date) {
        // 如果是Date对象，格式化为YYYY-MM-DD
        leadToSave.expectedCloseDate = leadToSave.expectedCloseDate.toISOString().split('T')[0];
      } else if (typeof leadToSave.expectedCloseDate === 'string' && leadToSave.expectedCloseDate.includes('T')) {
        // 如果是ISO字符串，提取日期部分
        leadToSave.expectedCloseDate = leadToSave.expectedCloseDate.split('T')[0];
      }
    }

    // 处理创建时间和更新时间格式（仅在新增时设置创建时间）
    const now = new Date();
    const formattedNow = now.toISOString().split('T')[0] + ' ' + now.toTimeString().split(' ')[0].split('.')[0];

    if (!leadToSave.id) {
      // 新增时设置创建时间
      if (!leadToSave.createTime) {
        leadToSave.createTime = formattedNow;
      }
    }

    // 更新时总是设置更新时间
    leadToSave.updateTime = formattedNow;

    // 检查是否从其他阶段更新为"赢单"
    const isStageChangedToWin = leadToSave.id && // 确保是更新操作（有ID）
      leadToSave.stage === 5 &&
      leadsList.value.find((item: any) => item.id === leadToSave.id)?.stage !== 5;

    let response;
    if (leadToSave.id) {
      // 更新商机
      response = await request.patch('/opportunities/update', leadToSave)
    } else {
      // 新增商机
      response = await request.put('/opportunities/add', leadToSave)
    }

    // 确保响应存在并具有code属性
    if (response && typeof response === 'object' && 'code' in response) {
      if (response.code === 200) {
        ElMessage.success(response.message || (leadToSave.id ? '更新成功' : '新增成功'))
        // 如果是从其他阶段更新为"赢单"，提示用户订单已自动创建
        if (isStageChangedToWin) {
          ElMessage.success('商机已更新为赢单，系统已自动创建订单');
        }
        dialogVisible.value = false
        fetchLeadsList()
      } else {
        ElMessage.error('保存失败: ' + (response.message || '未知错误'))
      }
    } else {
      // 处理响应格式不正确的情况
      ElMessage.error('保存失败: 服务器响应格式不正确')
    }
  } catch (error: any) {
    console.error('保存商机异常:', error)
    ElMessage.error('保存失败: ' + (error.message || '网络异常'))
  }
}

// 组件挂载时获取数据
onMounted(() => {
  fetchLeadsList()
  fetchCustomers()
  fetchEmployees()
  fetchMyCustomers() // 获取我的客户列表

  // 初始化分页信息
  customerPagination.currentPage = 1
  customerPagination.pageSize = 5
  customerPagination.total = 0
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

// 根据状态获取状态标签类型
const getStatusTagType = (status: number): string => {
  switch (status) {
    case 0: // 无效
      return 'danger'
    case 1: // 新客户
      return 'info'
    case 2: // 有意向
      return 'warning'
    case 3: // 已成交
      return 'success'
    default:
      return 'info'
  }
}

// 根据状态获取状态名称
const getStatusName = (status: number): string => {
  switch (status) {
    case 0:
      return '无效'
    case 1:
      return '新客户'
    case 2:
      return '有意向'
    case 3:
      return '已成交'
    default:
      return '未知'
  }
}

// 根据赢率获取进度条状态
const getProgressStatus = (probability: number): string => {
  if (probability < 0.3) return 'exception'
  if (probability < 0.7) return 'warning'
  return 'success'
}

// 格式化日期时间
const formatDateTime = (dateTime: string) => {
  if (!dateTime) return '-'
  // 如果是日期字符串，直接返回
  if (typeof dateTime === 'string') {
    return dateTime.split('.')[0] // 移除毫秒部分
  }
  // 如果是Date对象，格式化为YYYY-MM-DD HH:mm:ss
  if (dateTime instanceof Date) {
    const year = dateTime.getFullYear()
    const month = String(dateTime.getMonth() + 1).padStart(2, '0')
    const day = String(dateTime.getDate()).padStart(2, '0')
    const hours = String(dateTime.getHours()).padStart(2, '0')
    const minutes = String(dateTime.getMinutes()).padStart(2, '0')
    const seconds = String(dateTime.getSeconds()).padStart(2, '0')
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
  }
  return '-'
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

/* 客户跟进卡片样式 */
.customer-followup-card {
  border: 1px solid #ebeef5;
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 筛选卡片样式 */
.filter-card {
  margin-bottom: 20px;
  padding: 15px;
}

/* 筛选表单样式 */
:deep(.demo-form-inline .el-form-item) {
  margin-right: 20px;
  margin-bottom: 10px;
}

:deep(.demo-form-inline .el-form-item:last-child) {
  margin-right: 0;
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

/* 操作按钮样式 */
:deep(.el-table .el-button--small) {
  padding: 0;
  margin-right: 10px;
}

:deep(.el-table .el-button--small:last-child) {
  margin-right: 0;
}

/* 订单编号单元格样式 */
.order-no-cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}

/* 订单金额样式 */
.amount-text {
  color: #30b6a8;
  font-weight: bold;
}

/* 订单备注单元格样式 */
.remark-cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}

/* 订单状态标签样式 */
.order-status-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

/* 不同状态的颜色 */
.order-status-pending-pay {
  background-color: #f5f5f5;
  color: #999;
  border: 1px solid #ddd;
}

.order-status-after-sales {
  background-color: #e6f7ff;
  color: #1890ff;
  border: 1px solid #91d5ff;
}

.order-status-refunded {
  background-color: #fff1f0;
  color: #f5222d;
  border: 1px solid #ffe5e5;
}

.order-status-executing {
  background-color: #f5f5f5;
  color: #999;
  border: 1px solid #ddd;
}

.order-status-completed {
  background-color: #f0f9fb;
  color: #52c414;
  border: 1px solid #b3e5fc;
}

/* 跟进表单样式 */
.follow-up-form {
  padding: 20px;
}

/* 表单标签样式 */
.follow-up-form .el-form-item__label {
  font-weight: 500;
  color: #303133;
}

/* 表单输入框样式 */
.follow-up-form .el-form-item__content {
  margin-left: 0;
}

/* 输入框容器样式 */
.follow-up-form .el-input,
.follow-up-form .el-select {
  border-radius: 4px;
}

/* 文本区域样式 */
.follow-up-form .el-textarea {
  border-radius: 4px;
}

/* 日期选择器样式 */
.follow-up-form .el-date-editor {
  border-radius: 4px;
}

/* 标签样式 */
.follow-up-form .el-tag {
  margin-top: 4px;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

/* 按钮容器样式 */
.dialog-footer {
  text-align: right;
  padding: 20px 0;
}

/* 按钮样式 */
.dialog-footer .el-button {
  padding: 8px 16px;
  border-radius: 4px;
}

/* 优化表单整体布局 */
.follow-up-form .el-form-item {
  margin-bottom: 15px;
}

/* 优化表单标签宽度 */
.follow-up-form .el-form-item__label {
  width: 120px !important;
  text-align: right;
  padding-right: 10px;
}

/* 优化输入框宽度 */
.follow-up-form .el-form-item__content {
  flex: 1;
}

/* 优化文本区域样式 */
.follow-up-form .el-textarea__inner {
  border-radius: 4px;
  resize: none;
}

/* 优化对话框样式 */
.el-dialog__body {
  padding: 20px;
}

/* 优化对话框标题 */
.el-dialog__header {
  padding: 20px 20px 10px;
}

/* 优化对话框底部按钮间距 */
.dialog-footer {
  padding: 20px 20px 20px;
}

/* 响应式布局 */
@media (max-width: 768px) {
  :deep(.demo-form-inline .el-form-item) {
    width: 100%;
    margin-right: 0;
  }

  :deep(.demo-form-inline .el-form-item__content) {
    width: 100%;
  }

  :deep(.demo-form-inline .el-input, .demo-form-inline .el-select) {
    width: 100%;
  }
}
</style>
