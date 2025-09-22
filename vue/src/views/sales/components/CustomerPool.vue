<template>
  <div class="customer-pool-container">
    <!-- 标题区域 -->
    <div class="header">
      <div class="title-section">
        <h2>客户池</h2>
        <p>可通过姓名或手机号搜索客户</p>
      </div>
    </div>

    <!-- 筛选区域 -->
    <div class="filter-card">
      <div class="filter-container">
        <div class="filter-item">
          <label>姓名:</label>
          <el-input
            v-model="searchForm.name"
            placeholder="输入姓名"
            clearable
            size="small"
            class="compact-input"
          />
        </div>

        <div class="filter-item">
          <label>手机号:</label>
          <el-input
            v-model="searchForm.phone"
            placeholder="输入手机号"
            clearable
            size="small"
            class="compact-input"
          />
        </div>

        <div class="filter-item">
          <el-button type="primary" @click="handleSearch" size="small" icon="Search">查询</el-button>
          <el-button @click="handleReset" size="small" icon="Refresh">重置</el-button>
        </div>
      </div>
    </div>

    <!-- 表格区域 -->
    <div class="table-card">
      <div class="table-header">
        <div class="table-title">客户列表</div>
        <el-button type="primary" @click="loadCustomerData" size="small" :loading="loading">
          刷新数据
        </el-button>
      </div>

      <!-- 使用 el-table -->
      <div class="table-wrapper">
        <el-table
          :data="paginatedCustomers"
          border
          stripe
          height="500"
          style="width: 100%; min-width: 1200px;"
        >
          <el-table-column prop="name" label="客户姓名" width="120" fixed="left" />
          <el-table-column prop="phone" label="手机号" width="150" fixed="left" />
          <el-table-column prop="sex" label="性别" width="80">
            <template #default="scope">
            <span :class="scope.row.sex === 1 ? 'male' : 'female'">
              {{ scope.row.sex === 1 ? '男' : (scope.row.sex === 0 ? '女' : '-') }}
            </span>
            </template>
          </el-table-column>
          <el-table-column prop="companyName" label="公司名称" width="200" />
          <el-table-column prop="position" label="职位" width="150" />
          <el-table-column prop="poolTime" label="进入客户池时间" width="180">
            <template #default="scope">{{ formatDate(scope.row.poolTime) }}</template>
          </el-table-column>
          <el-table-column prop="lastFollowTime" label="最后跟进时间" width="180">
            <template #default="scope">{{ formatDate(scope.row.lastFollowTime) }}</template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180">
            <template #default="scope">{{ formatDate(scope.row.createTime) }}</template>
          </el-table-column>
          <el-table-column prop="updateTime" label="更新时间" width="180">
            <template #default="scope">{{ formatDate(scope.row.updateTime) }}</template>
          </el-table-column>
          <el-table-column prop="salesRemark" label="销售备注" width="250">
            <template #default="scope">
            <span class="notes-cell" :title="scope.row.salesRemark">
              {{ scope.row.salesRemark || '-' }}
            </span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleAssign(scope.row)">分配</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          :total="filteredCustomers.length"
          layout="total, sizes, prev, pager, next, jumper"
          background
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from "vue";
import { ElMessage, ElMessageBox, ElNotification } from "element-plus";
import axios from "axios";

const customerData = ref([]);
const filteredCustomers = ref([]);
const loading = ref(false);

const searchForm = reactive({
  name: "",
  phone: ""
});

const pagination = reactive({
  currentPage: 1,
  pageSize: 10
});

// 格式化日期显示
const formatDate = (dateString) => {
  if (!dateString) return "-";
  return dateString.length > 10 ? dateString.substring(0, 10) : dateString;
};

// 计算分页后的数据
const paginatedCustomers = computed(() => {
  const start = (pagination.currentPage - 1) * pagination.pageSize;
  const end = start + pagination.pageSize;
  return filteredCustomers.value.slice(start, end);
});

// 加载客户池数据
const loadCustomerData = async () => {
  try {
    loading.value = true;
    const response = await axios.get("http://localhost:8080/customer/poolList");
    const data = response.data;

    if (data.code === 200 && Array.isArray(data.data)) {
      customerData.value = data.data;
      filteredCustomers.value = [...customerData.value];
      pagination.currentPage = 1; // 重置页码
      ElNotification.success({
        title: "成功",
        message: "数据加载成功"
      });
    } else {
      throw new Error("API返回数据格式不正确");
    }
  } catch (error) {
    console.error("获取客户池数据失败:", error);
    ElMessage.error("获取客户池数据失败");

    // 模拟数据
    customerData.value = Array.from({ length: 35 }).map((_, index) => ({
      name: `客户${index + 1}`,
      phone: "1380000" + (1000 + index),
      sex: index % 2,
      poolTime: "2023-07-15 14:30:22",
      lastFollowTime: "2023-08-20 09:45:11",
      createTime: "2023-06-10 10:20:33",
      updateTime: "2023-08-20 09:45:11",
      salesRemark: "测试数据-" + (index + 1),
      companyName: "公司-" + (index + 1),
      position: "职位-" + (index + 1)
    }));
    filteredCustomers.value = [...customerData.value];
    pagination.currentPage = 1;
  } finally {
    loading.value = false;
  }
};

// 查询
const handleSearch = () => {
  filteredCustomers.value = customerData.value.filter((customer) => {
    const nameMatch = searchForm.name
      ? customer.name && customer.name.includes(searchForm.name)
      : true;
    const phoneMatch = searchForm.phone
      ? customer.phone && customer.phone.includes(searchForm.phone)
      : true;
    return nameMatch && phoneMatch;
  });

  pagination.currentPage = 1; // 重置页码
  ElMessage.success(`找到 ${filteredCustomers.value.length} 条记录`);
};

// 重置
const handleReset = () => {
  searchForm.name = "";
  searchForm.phone = "";
  filteredCustomers.value = [...customerData.value];
  pagination.currentPage = 1;
  ElMessage.info("已重置搜索条件");
};

// 分配客户
const handleAssign = (row) => {
  ElMessageBox.confirm("确定要分配该客户吗?", "分配客户", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "info"
  })
    .then(() => {
      ElMessage.success("分配成功");
    })
    .catch(() => {
      // 取消操作
    });
};

onMounted(() => {
  loadCustomerData();
});
</script>

<style scoped>
.customer-pool-container {
  padding: 12px;
  width: 100%;
  margin: 0 auto;
  box-sizing: border-box;
}

@media (min-width: 768px) {
  .customer-pool-container {
    padding: 16px;
    max-width: 1200px;
  }
}

.header {
  margin-bottom: 16px;
}

.title-section h2 {
  font-size: 20px;
  font-weight: 600;
  color: #1f2d3d;
  margin-bottom: 4px;
}

.title-section p {
  color: #8492a6;
  font-size: 13px;
}

.filter-card {
  background: white;
  border-radius: 6px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.filter-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

@media (min-width: 768px) {
  .filter-container {
    gap: 12px;
  }
}

.filter-item {
  display: flex;
  align-items: center;
  margin-right: 8px;
}

.filter-item label {
  font-size: 13px;
  margin-right: 6px;
  color: #606266;
  white-space: nowrap;
}

.compact-input {
  width: 120px;
}

@media (min-width: 768px) {
  .compact-input {
    width: 140px;
  }
}

.table-card {
  background: white;
  border-radius: 6px;
  padding: 12px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  overflow: auto;
  margin: 0 -12px;
  width: calc(100% + 24px);
}

.table-wrapper {
  overflow-x: auto;
  width: 100%;
  -webkit-overflow-scrolling: touch;
}

@media (min-width: 768px) {
  .table-card {
    padding: 16px;
    margin: 0;
    width: 100%;
  }
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.table-title {
  font-size: 16px;
  font-weight: 500;
  color: #1f2d3d;
}

.pagination-wrapper {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}

.notes-cell {
  max-width: 150px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
}

@media (min-width: 768px) {
  .notes-cell {
    max-width: 200px;
  }
}

.male {
  color: #409eff;
}

.female {
  color: #f672b5;
}
</style>
