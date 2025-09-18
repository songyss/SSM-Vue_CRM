<template>
  <div class="customers-container">
    <div class="customer-layout">
      <!-- 侧边栏导航 -->
      <div class="customer-sidebar">
        <el-menu
          :default-active="activeMenu"
          class="customer-menu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="profile">
            <el-icon><User /></el-icon>
            <span>客户档案</span>
          </el-menu-item>
          <el-menu-item index="groups">
            <el-icon><Grid /></el-icon>
            <span>客户分组</span>
          </el-menu-item>
          <el-menu-item index="tags">
            <el-icon><PriceTag /></el-icon>
            <span>客户标签</span>
          </el-menu-item>
          <el-menu-item index="followups">
            <el-icon><Clock /></el-icon>
            <span>跟进记录</span>
          </el-menu-item>
          <el-menu-item index="transactions">
            <el-icon><Document /></el-icon>
            <span>交易历史</span>
          </el-menu-item>
          <el-menu-item index="analytics">
            <el-icon><DataAnalysis /></el-icon>
            <span>客户分析</span>
          </el-menu-item>
        </el-menu>
      </div>

        <!-- 主内容区 -->
        <div class="customer-main">
          <div class="page-header">
            <h1>{{ activeMenu === 'profile' ? '客户管理' : 
                 activeMenu === 'groups' ? '客户分组' :
                 activeMenu === 'tags' ? '客户标签' :
                 activeMenu === 'followups' ? '跟进记录' :
                 activeMenu === 'transactions' ? '交易历史' : '客户分析' }}</h1>
            <el-button type="primary" @click="handleAddCustomer" v-if="activeMenu === 'profile'">
              <el-icon><Plus /></el-icon> 新增客户
            </el-button>
          </div>
          
          <!-- 动态内容区域 -->
          <div v-if="activeMenu === 'profile'">

    <!-- 搜索筛选区域 -->
    <el-card class="filter-card">
      <el-row :gutter="16">
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <el-input v-model="searchForm.name" placeholder="客户姓名" clearable />
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <el-input v-model="searchForm.phone" placeholder="手机号" clearable />
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <el-select v-model="searchForm.gender" placeholder="性别" clearable>
            <el-option label="全部" value="" />
            <el-option label="男" value="male" />
            <el-option label="女" value="female" />
          </el-select>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <el-select v-model="searchForm.source" placeholder="客户来源" clearable>
            <el-option label="全部" value="" />
            <el-option label="线上" value="online" />
            <el-option label="线下" value="offline" />
            <el-option label="转介绍" value="referral" />
          </el-select>
        </el-col>
      </el-row>
      <el-row :gutter="16" style="margin-top: 16px;">
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <el-select v-model="searchForm.callStatus" placeholder="电话筛选状态">
            <el-option label="未联系" value="1" />
            <el-option label="无意向" value="2" />
            <el-option label="有意向" value="3" />
            <el-option label="信息有误" value="4" />
          </el-select>
        </el-col>
        <el-col :xs="24" :sm="12" :md="16" :lg="12">
          <el-input 
            v-model="searchForm.remark" 
            placeholder="备注" 
            clearable 
          />
        </el-col>
        <el-col :xs="24" :sm="24" :md="24" :lg="6" class="filter-actions">
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 客户表格 -->
    <el-card class="table-card" shadow="hover">
      <el-table
        :data="customerList"
        border
        stripe
        style="width: 100%"
        :highlight-current-row="true"
        @current-change="handleCurrentChange"
      >
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="客户名称" width="200" sortable>
          <template #default="scope">
            <div class="customer-name">
              <el-avatar :size="32" class="customer-avatar">
                {{ scope.row.name.substring(0, 2) }}
              </el-avatar>
              <span>{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="contactPerson" label="联系人" width="140" />
        <el-table-column prop="phone" label="联系电话" width="160" />
        <el-table-column prop="type" label="客户类型" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.type === 'enterprise' ? 'primary' : 'info'">
              {{ scope.row.type === 'enterprise' ? '企业客户' : '个人客户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="level" label="客户等级" width="120">
          <template #default="scope">
            <el-tag :type="levelTagType[scope.row.level]">
              {{ levelMap[scope.row.level] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" sortable />
        <el-table-column prop="lastOrderTime" label="最近订单" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="small" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              size="small"
              type="text"
              text-color="#ff4d4f"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </el-card>
          </div>
          
          <!-- 其他功能区域占位 -->
          <div v-if="activeMenu !== 'profile'" class="feature-placeholder">
            <el-empty description="功能开发中，敬请期待" />
          </div>
        </div>
      </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { 
  Plus,
  User,
  Grid,
  PriceTag,
  Clock,
  Document,
  DataAnalysis
} from '@element-plus/icons-vue'
import { ElMessage, ElLoading } from 'element-plus'
import axios from 'axios' // 引入axios

// 客户等级映射
const levelMap = {
  vip: 'VIP客户',
  normal: '普通客户',
  potential: '潜在客户',
}

// 客户等级标签样式
const levelTagType = {
  vip: 'success',
  normal: 'info',
  potential: 'warning',
}

// 搜索表单
const searchForm = reactive({
  name: '',
  phone: '',
  gender: '',
  source: '',
  callStatus: '1', // 默认未联系
  remark: ''
})

// 分页配置
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
})

// 当前激活的菜单
const activeMenu = ref('profile')

// 处理菜单选择
const handleMenuSelect = (index: string) => {
  activeMenu.value = index
  // 根据选择的菜单加载不同的数据
  switch(index) {
    case 'profile':
      getCustomerList()
      break
    case 'groups':
      // TODO: 加载分组数据
      ElMessage.info('客户分组功能开发中')
      break
    case 'tags':
      // TODO: 加载标签数据
      ElMessage.info('客户标签功能开发中')
      break
    case 'followups':
      // TODO: 加载跟进记录
      ElMessage.info('跟进记录功能开发中')
      break
    case 'transactions':
      // TODO: 加载交易历史
      ElMessage.info('交易历史功能开发中')
      break
    case 'analytics':
      // TODO: 加载分析数据
      ElMessage.info('客户分析功能开发中')
      break
  }
}

// 客户列表数据
const customerList = ref([])

// 获取客户列表数据的方法
const getCustomerList = async () => {
  // 显示加载状态
  const loading = ElLoading.service({
    lock: true,
    text: '加载中...',
    background: 'rgba(0, 0, 0, 0.7)',
  })

  try {
    // 构造请求参数
    const params = {
      page: pagination.currentPage,
      size: pagination.pageSize,
      name: searchForm.name,
      phone: searchForm.phone,
      gender: searchForm.gender,
      source: searchForm.source,
      callStatus: searchForm.callStatus,
      remark: searchForm.remark
    }

    // 发起GET请求
    const response = await axios.get('/customer/allList', { params })

    // 假设接口返回格式为 { data: [], total: 0 }
    customerList.value = response.data.data
    pagination.total = response.data.total

    ElMessage.success('数据加载成功')
  } catch (error) {
    console.error('获取客户列表失败:', error)
    ElMessage.error('获取数据失败，请重试')
  } finally {
    // 关闭加载状态
    loading.close()
  }
}

// 页面加载时获取数据
onMounted(() => {
  getCustomerList()
})

// 搜索处理
const handleSearch = () => {
  pagination.currentPage = 1 // 重置到第一页
  getCustomerList() // 调用接口获取数据
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    name: '',
    phone: '',
    gender: '',
    source: '',
    callStatus: '1', // 重置为未联系
    remark: ''
  })
  pagination.currentPage = 1
  getCustomerList() // 重置后重新获取数据
}

// 分页事件
const handleSizeChange = (val: number) => {
  pagination.pageSize = val
  getCustomerList() // 页大小改变时重新获取数据
}

const handlePageChange = (val: number) => {
  pagination.currentPage = val
  getCustomerList() // 页码改变时重新获取数据
}

// 当前行变化事件
const handleCurrentChange = (currentRow: any) => {
  if (currentRow) {
    console.log('当前选中客户:', currentRow)
  }
}

// 操作按钮事件
const handleAddCustomer = async () => {
  // 示例：新增客户
  try {
    const newCustomer = {
      // 新增客户的表单数据
    }
    const response = await axios.post('/api/customers', newCustomer)
    ElMessage.success('客户新增成功')
    getCustomerList() // 重新加载列表
  } catch (error) {
    console.error('新增客户失败:', error)
    ElMessage.error('新增客户失败，请重试')
  }
}

const handleView = (row: any) => {
  // 查看客户详情
  axios
    .get(`/api/customers/${row.id}`)
    .then((response) => {
      console.log('客户详情:', response.data)
      // 处理查看逻辑
    })
    .catch((error) => {
      console.error('获取客户详情失败:', error)
      ElMessage.error('获取客户详情失败')
    })
}

const handleEdit = async (row: any) => {
  // 示例：编辑客户
  try {
    const updatedData = {
      // 编辑后的客户数据
      ...row,
      // 修改的字段
    }
    await axios.put(`/api/customers/${row.id}`, updatedData)
    ElMessage.success('客户编辑成功')
    getCustomerList() // 重新加载列表
  } catch (error) {
    console.error('编辑客户失败:', error)
    ElMessage.error('编辑客户失败，请重试')
  }
}

const handleDelete = async (row: any) => {
  // 示例：删除客户
  try {
    await axios.delete(`/api/customers/${row.id}`)
    ElMessage.success('客户删除成功')
    getCustomerList() // 重新加载列表
  } catch (error) {
    console.error('删除客户失败:', error)
    ElMessage.error('删除客户失败，请重试')
  }
}
</script>
<style scoped>
.customers-container {
  padding: 16px;
}

.customer-layout {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.customer-sidebar {
  width: 100%;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 16px;
  overflow: hidden;
}

.customer-menu {
  border-right: none;
}

.customer-menu .el-menu-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 0 4px !important;
}

.customer-menu .el-menu-item span {
  font-size: 12px;
  margin-top: 4px;
}

.customer-main {
  flex: 1;
  min-width: 0;
}

.feature-placeholder {
  padding: 24px 0;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  margin-top: 16px;
}

.page-header {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 16px;
}

.filter-card {
  margin-bottom: 16px;
  padding: 12px;
}

.filter-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

.table-card {
  overflow: auto;
}

.pagination-container {
  margin-top: 12px;
  display: flex;
  justify-content: center;
}

.customer-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.customer-avatar {
  background-color: #42b983;
}

/* 响应式布局 */
@media (min-width: 768px) {
  .customer-layout {
    flex-direction: row;
  }
  
  .customer-sidebar {
    width: 220px;
    margin-bottom: 0;
  }
  
  .customer-menu {
    display: block;
  }
  
  .customer-menu .el-menu-item {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    text-align: left;
    padding: 0 20px !important;
  }
  
  .customer-menu .el-menu-item span {
    font-size: 14px;
    margin-top: 0;
    margin-left: 8px;
  }
  
  .page-header {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }
  
  .filter-actions {
    margin-top: 0;
    justify-content: flex-end;
  }
  
  .pagination-container {
    justify-content: flex-end;
  }
}

@media (min-width: 992px) {
  .customers-container {
    padding: 20px;
  }
  
  .customer-layout {
    gap: 20px;
  }
  
  .page-header {
    margin-bottom: 20px;
  }
  
  .filter-card {
    margin-bottom: 20px;
    padding: 16px;
  }
  
  .filter-actions {
    gap: 10px;
  }
  
  .pagination-container {
    margin-top: 16px;
  }
  
  .customer-name {
    gap: 10px;
  }
}
</style>
