<template>
  <div class="customers-container">
    <div class="page-header">
      <h1>客户管理</h1>
      <el-button type="primary" @click="handleAddCustomer">
        <el-icon><Plus /></el-icon> 新增客户
      </el-button>
    </div>

    <!-- 搜索筛选区域 -->
    <el-card class="filter-card">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.name" placeholder="客户名称" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.type" placeholder="客户类型" clearable>
            <el-option label="全部" value="" />
            <el-option label="企业客户" value="enterprise" />
            <el-option label="个人客户" value="individual" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.level" placeholder="客户等级" clearable>
            <el-option label="全部" value="" />
            <el-option label="VIP客户" value="vip" />
            <el-option label="普通客户" value="normal" />
            <el-option label="潜在客户" value="potential" />
          </el-select>
        </el-col>
        <el-col :span="6" class="filter-actions">
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
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
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
  type: '',
  level: '',
})

// 分页配置
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
})

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
      type: searchForm.type,
      level: searchForm.level,
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
    type: '',
    level: '',
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

.customer-name {
  display: flex;
  align-items: center;
  gap: 10px;
}

.customer-avatar {
  background-color: #42b983;
}
</style>
