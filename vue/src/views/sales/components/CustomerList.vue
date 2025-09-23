<template>
  <div class="customer-list">
    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="姓名">
        <el-input v-model="searchForm.name" placeholder="输入姓名" clearable />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="searchForm.phone" placeholder="输入手机号" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch" v-if="permissionStore.hasButtonPermission('/customer/listByEmployee')">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 客户表格 -->
    <el-table :data="customers" border stripe style="width: 100%; margin-top: 10px;">
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column label="性别" width="80">
        <template #default="scope">
          <span>{{ scope.row.sex === 1 ? '男' : '女' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="companyName" label="公司" />
      <el-table-column prop="position" label="职位" />
      <el-table-column label="状态">
        <template #default="scope">
          <span>
            {{
              scope.row.status === 0 ? '无效'
                : scope.row.status === 1 ? '新客户'
                  : scope.row.status === 2 ? '有意向'
                    : scope.row.status === 3 ? '已成交'
                      : '未知'
            }}
          </span>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="260">
        <template #default="scope">
          <div class="action-buttons">
            <el-button size="small" type="primary" text @click="showDetail(scope.row)" v-if="permissionStore.hasButtonPermission('/customer/detail/${row.id}')">
              查看
            </el-button>
            <el-button size="small" type="warning" text @click="openEdit(scope.row)" v-if="permissionStore.hasButtonPermission('/customer/update')">
              修改
            </el-button>
            <el-button size="small" type="success" text @click="openAddToPoolDialog(scope.row)" v-if="permissionStore.hasButtonPermission('/customer/addToPool')">
              加入客户池
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="pagination.currentPage"
      v-model:page-size="pagination.pageSize"
      :total="pagination.total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      style="margin-top: 15px; text-align: right;"
    />

    <!-- 加入客户池弹窗 -->
    <el-dialog v-model="addDialogVisible" title="加入客户池" width="500px">
      <el-input
        type="textarea"
        v-model="reason"
        placeholder="请输入放入客户池的原因"
        rows="4"
      />
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAddToPool">确定</el-button>
      </template>
    </el-dialog>

    <!-- 详情 / 编辑弹窗 -->
    <el-dialog
      v-model="detailVisible"
      :title="editMode ? '编辑客户' : '客户详情'"
      width="600px"
      @close="handleClose"
    >
      <!-- 客户详情 -->
      <div v-if="!editMode">
        <p><b>姓名：</b>{{ detailData.name }}</p>
        <p><b>手机号：</b>{{ detailData.phone }}</p>
        <p><b>性别：</b>{{ detailData.sex === 1 ? '男' : '女' }}</p>
        <p><b>公司：</b>{{ detailData.companyName || '无' }}</p>
        <p><b>职位：</b>{{ detailData.position || '无' }}</p>
        <p><b>客户来源：</b>{{ detailData.source || '无' }}</p>
        <p><b>销售备注：</b>{{ detailData.salesRemark || '无' }}</p>
        <!-- 订单 -->
        <div v-if="detailData.orders && detailData.orders.length">
          <b>订单：</b>
          <ul>
            <li v-for="order in detailData.orders" :key="order.id">
              {{ order.orderNumber }}
            </li>
          </ul>
        </div>

        <!-- 商机 -->
        <div v-if="detailData.opportunities && detailData.opportunities.length">
          <b>商机：</b>
          <ul>
            <li v-for="opp in detailData.opportunities" :key="opp.id">
              {{ opp.name }}
            </li>
          </ul>
        </div>
      </div>

      <!-- 编辑表单 -->
      <el-form v-else :model="editForm" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editForm.phone" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="editForm.sex" placeholder="请选择">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="公司">
          <el-input v-model="editForm.companyName" />
        </el-form-item>
        <el-form-item label="职位">
          <el-input v-model="editForm.position" />
        </el-form-item>
        <el-form-item label="客户来源">
          <el-input v-model="editForm.source" />
        </el-form-item>
        <el-form-item label="销售备注">
          <el-input v-model="editForm.salesRemark" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="editForm.status" placeholder="请选择">
            <el-option label="无效" :value="0" />
            <el-option label="新客户" :value="1" />
            <el-option label="有意向" :value="2" />
            <el-option label="已成交" :value="3" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="detailVisible = false">取消</el-button>
        <el-button v-if="editMode" type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from "vue";
import {ElMessage} from "element-plus";
import request from "@/utils/request";
import { usePermissionStore } from '@/stores/permission'
const permissionStore = usePermissionStore()

// 数据
const customers = ref([]);
const searchForm = reactive({name: "", phone: ""});
const pagination = reactive({currentPage: 1, pageSize: 10, total: 0});

// 用户信息
let userInfo = {};
try {
  userInfo = JSON.parse(localStorage.getItem("crm_userInfo") || "{}");
} catch (e) {
  userInfo = {};
}

// 详情 / 编辑
const detailVisible = ref(false);
const detailData = reactive({});
const editMode = ref(false);
const editForm = reactive({});

// 加入客户池
const addDialogVisible = ref(false);
const reason = ref("");
let currentCustomerId = null;

const openAddToPoolDialog = (row) => {
  currentCustomerId = row.id;
  reason.value = "";
  addDialogVisible.value = true;
};

const submitAddToPool = async () => {
  if (!reason.value) {
    ElMessage.error("请输入原因");
    return;
  }
  try {
    const res = await request.post("/customer/addToPool", {
      customerId: currentCustomerId,
      reason: reason.value,
      operatorId: userInfo.userId
    });
    if (res.code === 200) {
      ElMessage.success("客户已加入客户池");
      addDialogVisible.value = false;
      const idx = customers.value.findIndex((c) => Number(c.id) === Number(currentCustomerId));
      if (idx !== -1) {
        customers.value.splice(idx, 1);
        pagination.total = Math.max(0, pagination.total - 1);
      }
      if (customers.value.length === 0 && pagination.currentPage > 1) {
        pagination.currentPage -= 1;
        loadData();
      }
    } else {
      ElMessage.error(res.message || "加入客户池失败");
    }
  } catch (e) {
    console.error(e);
    ElMessage.error("加入客户池失败");
  }
};

// ✅ 加载客户列表
const loadData = async () => {
  try {
    const res = await request.get("/customer/listByEmployee", {
      params: {
        employeeId: userInfo.userId,
        pageNum: pagination.currentPage,
        pageSize: pagination.pageSize,
        name: searchForm.name,
        phone: searchForm.phone
      }
    });
    console.log("res ===>", res);
    if (res.data.code === 200) {
      customers.value = res.data.data.list;
      pagination.total = res.data.total;
      ElMessage.success("加载客户列表成功");
    }

  } catch (e) {
    console.error(e);
    ElMessage.error("加载客户列表失败");
  }
};

const handleSearch = () => {
  pagination.currentPage = 1;
  loadData();
};

const handleReset = () => {
  searchForm.name = "";
  searchForm.phone = "";
  pagination.currentPage = 1;
  loadData();
};

const handleSizeChange = (val) => {
  pagination.pageSize = val;
  loadData();
};

const handleCurrentChange = (val) => {
  pagination.currentPage = val;
  loadData();
};

// ✅ 查看详情：调用后端 /customer/detail/{id}
const showDetail = async (row) => {
  try {
    const res = await request.get(`/customer/detail/${row.id}`);
    if (res.data.code === 200) {
      Object.assign(detailData, res.data.data);
      console.log("detailData ===>", res.data);
      detailVisible.value = true;
      editMode.value = false;
    } else {
      ElMessage.error(res.message || "加载客户详情失败");
    }
  } catch (e) {
    console.error(e);
    ElMessage.error("加载客户详情失败");
  }
};

const openEdit = (row) => {
  Object.assign(editForm, row);
  detailVisible.value = true;
  editMode.value = true;
};

const submitEdit = async () => {
  try {
    const res = await request.post("/customer/update", editForm);
    if (res.data.code === 200) {
      ElMessage.success("更新成功");
      detailVisible.value = false;
      loadData();
    } else {
      ElMessage.error(res.message || "更新失败");
    }
  } catch (e) {
    console.error(e);
    ElMessage.error("更新失败");
  }
};

const handleClose = () => {
  editMode.value = false;
};

onMounted(loadData);
</script>

<style scoped>
.customer-list {
  padding: 20px;
}

.search-form {
  margin-bottom: 10px;
}

.action-buttons {
  display: flex;
  gap: 8px;
}
</style>
