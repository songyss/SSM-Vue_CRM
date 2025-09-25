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
          <el-button type="primary" @click="handleSearch" size="small" icon="Search" v-if="permissionStore.hasButtonPermission('/customer/poolSearch')">查询</el-button>
          <el-button @click="handleReset" size="small" icon="Refresh">重置</el-button>
        </div>
      </div>
    </div>

    <!-- 表格区域 -->
    <div class="table-card">
      <div class="table-header">
        <div class="table-title">客户列表</div>
        <el-button type="primary" @click="loadCustomerData" size="small" :loading="loading" v-if="permissionStore.hasButtonPermission('/customer/poolListAdd')">
          刷新数据
        </el-button>
      </div>

      <div class="table-wrapper">
        <el-table
          :data="paginatedCustomers"
          border
          stripe
          height="500"
          style="width: 100%; min-width: 1000px;"
        >
          <el-table-column prop="name" label="客户姓名" width="120" fixed="left" />
          <el-table-column prop="phone" label="手机号" width="150" fixed="left" />
          <el-table-column prop="sex" label="性别" width="80" />
          <el-table-column prop="company" label="公司名称" width="200" />
          <el-table-column prop="position" label="职位" width="150" />

          <!-- 放入原因 -->
          <el-table-column prop="reason" label="原因" width="250">
            <template #default="scope">
              <span class="notes-cell" :title="scope.row.reason">
                {{ scope.row.reason || '-' }}
              </span>
            </template>
          </el-table-column>

          <!-- 销售备注 -->
          <el-table-column prop="salesRemark" label="销售备注" width="250">
            <template #default="scope">
              <span class="notes-cell" :title="scope.row.salesRemark">
                {{ scope.row.salesRemark || '-' }}
              </span>
            </template>
          </el-table-column>

          <!-- 操作 -->
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleLock(scope.row)" v-if="permissionStore.hasButtonPermission('/customer/lock/customerId')">锁定</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

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
import request from "@/utils/request";
import { usePermissionStore } from '@/stores/permission'
const permissionStore = usePermissionStore()

// ================== 登录用户信息 ==================

let userInfo = {};
try {
  userInfo = JSON.parse(localStorage.getItem("crm_userInfo") || "{}");
} catch (error) {
  console.error("解析用户信息失败", error);
}


// ================== 数据状态 ==================
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

// 计算分页后的数据
const paginatedCustomers = computed(() => {
  const start = (pagination.currentPage - 1) * pagination.pageSize;
  const end = start + pagination.pageSize;
  return filteredCustomers.value.slice(start, end);
});

// ================== 加载客户池数据 ==================
const loadCustomerData = async () => {
  try {
    // 开启加载状态
    loading.value = true;
    const res = await request.get("/customer/poolList");

    // 发送请求获取客户数据


    const data = res.data;

    if (data.code === 200 && Array.isArray(data.data)) {
      customerData.value = data.data;
      filteredCustomers.value = [...customerData.value];
      pagination.currentPage = 1;
      ElNotification.success({ title: "成功", message: "数据加载成功" });
    } else {
      throw new Error("API返回数据格式不正确");
    }
  } catch (error) {
    console.error("获取客户池数据失败:", error);
    ElMessage.error("获取客户池数据失败");
  } finally {
    loading.value = false;
  }
};

// ================== 查询 ==================
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

  pagination.currentPage = 1;
  ElMessage.success(`找到 ${filteredCustomers.value.length} 条记录`);
};

// ================== 重置 ==================
const handleReset = () => {
  searchForm.name = "";
  searchForm.phone = "";
  filteredCustomers.value = [...customerData.value];
  pagination.currentPage = 1;
  ElMessage.info("已重置搜索条件");
};

// ================== 锁定客户 ==================
const handleLock = (row) => {
  ElMessageBox.confirm("确定要锁定该客户吗?", "锁定客户", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(async () => {
      try {
        console.log('锁定客户请求参数:', {
          customerId: row.id,
          employeeId: userInfo.userId
        });
        const res = await request.post(`/customer/lock/${row.id}`, {
          customerId: row.id,
          employeeId: userInfo.userId
        });
        console.log('锁定客户响应:', res);
        if (res.data.code === 200) {
          ElMessage.success("锁定成功");
          // 移除该客户（前端立即消失）
          loadCustomerData(); // 刷新客户池
        } else {
          ElMessage.error(res.data.message || "锁定失败");
        }
      } catch (e) {
        console.error(e);
        ElMessage.error("锁定失败");
      }
    })
    .catch(() => {});
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
</style>
