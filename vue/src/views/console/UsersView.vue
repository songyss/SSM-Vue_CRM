<!-- src/views/console/UsersView.vue -->
<template>
  <div class="app-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
        <el-form-item label="登录用户名" prop="username">
          <el-input
            v-model="queryParams.username"
            placeholder="请输入登录用户名"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="真实姓名" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入真实姓名"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input
            v-model="queryParams.phone"
            placeholder="请输入手机号码"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="queryParams.email"
            placeholder="请输入邮箱"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="queryParams.status"
            placeholder="用户状态"
            clearable
            style="width: 240px"
          >
            <el-option
              v-for="dict in sys_normal_disable"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <template #header>
        <div class="clearfix">
          <span>用户列表</span>
          <div style="float: right">
            <el-button
              type="primary"
              plain
              icon="Plus"
              @click="handleAdd()"
              v-hasPermi="['system:user:add']"
            >新增用户</el-button>
          </div>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="userList"
        row-key="id"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="用户ID" align="center" prop="id" width="100" />
        <el-table-column label="登录用户名" align="center" prop="username" :show-overflow-tooltip="true" width="120" />
        <el-table-column label="真实姓名" align="center" prop="name" :show-overflow-tooltip="true" width="120" />
        <el-table-column label="性别" align="center" prop="sex" width="100">
          <template #default="scope">
            <span>{{ scope.row.sex === '1' ? '男' : scope.row.sex === '0' ? '女' : '未知' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="手机号码" align="center" prop="phone" width="120" />
        <el-table-column label="邮箱" align="center" prop="email" width="150" />
        <el-table-column label="所属部门" align="center" prop="department" width="120">
          <template #default="scope">
            <span>{{ departmentMap[scope.row.department] || '未知' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="角色" align="center" prop="role" width="120">
          <template #default="scope">
            <span>{{ roleMap[scope.row.role] || '未知' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="create_time" width="180">
          <template #default="scope">
            <span>{{ scope.row.create_time }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="220" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
              type="primary"
              link
              icon="Edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:user:edit']"
            >修改</el-button>
            <el-button
              type="primary"
              link
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:user:remove']"
            >删除</el-button>
            <el-dropdown trigger="click" v-hasPermi="['system:user:resetPwd', 'system:user:edit']">
              <el-button type="primary" link>更多</el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    icon="Unlock"
                    @click="handleResetPwd(scope.row)"
                    v-hasPermi="['system:user:resetPwd']"
                  >重置密码</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <!-- 添加或修改用户对话框 -->
    <el-dialog :title="title" v-model="open" width="700px" append-to-body>
      <el-form ref="userRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="登录用户名" prop="username">
              <el-input v-model="form.username" placeholder="请输入登录用户名" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入真实姓名" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号码" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="18" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-radio-group v-model="form.sex">
                <el-radio label="1">男</el-radio>
                <el-radio label="0">女</el-radio>
                <el-radio label="2">未知</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属部门">
              <el-select v-model="form.department" placeholder="请选择部门">
                <el-option
                  v-for="item in departmentOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="角色">
              <el-select v-model="form.role" placeholder="请选择角色">
                <el-option
                  v-for="item in roleOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="直属上级">
              <el-select v-model="form.superior_id" placeholder="请选择直属上级">
                <el-option
                  v-for="item in superiorOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElForm, ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import request from '@/utils/request'


// 定义数据类型
interface User {
  id: number
  username: string
  password: string
  name: string
  sex: string
  phone: string
  borndate: string
  email: string
  department: number
  role: number
  superior_id: number
  create_time: string
  update_time: string
  is_delete: number
}

// 字典映射
const departmentMap = {
  1: '技术部',
  2: '销售部',
  3: '人事部',
  4: '财务部'
}

const roleMap = {
  1: '管理员',
  2: '普通用户',
  3: '超级管理员'
}

// 模拟字典数据
const sys_normal_disable = [
  { value: '0', label: '正常' },
  { value: '1', label: '停用' }
]

// 页面加载状态
const loading = ref(true)
const ids = ref<Array<number>>([])
const single = ref(true)
const multiple = ref(true)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  username: '',
  name: '',
  phone: '',
  email: '',
  status: '',
  department: undefined
})

// 总条数
const total = ref(0)

// 表单参数
const form = reactive<User>({
  id: 0,
  username: '',
  password: '',
  name: '',
  sex: '0',
  phone: '',
  borndate: '',
  email: '',
  department: 1,
  role: 2,
  superior_id: 0,
  create_time: '',
  update_time: '',
  is_delete: 0
})

// 表单校验规则
const rules = {
  username: [
    { required: true, message: "登录用户名不能为空", trigger: "blur" },
    { min: 2, max: 50, message: "登录用户名长度必须介于 2 和 50 之间", trigger: "blur" }
  ],
  name: [
    { required: true, message: "真实姓名不能为空", trigger: "blur" },
    { min: 2, max: 50, message: "真实姓名长度必须介于 2 和 50 之间", trigger: "blur" }
  ],
  phone: [
    {
      pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
      message: "请输入正确的手机号码",
      trigger: "blur"
    }
  ],
  email: [
    {
      type: "email",
      message: "请输入正确的邮箱地址",
      trigger: ["blur", "change"]
    }
  ]
}

// 对话框标题
const title = ref("")

// 是否显示弹出层
const open = ref(false)

// 用户表格数据
const userList = ref<User[]>([])

// 部门选项
const departmentOptions = ref([
  { value: 1, label: '技术部' },
  { value: 2, label: '销售部' },
  { value: 3, label: '人事部' },
  { value: 4, label: '财务部' }
])

// 角色选项
const roleOptions = ref([
  { value: 1, label: '管理员' },
  { value: 2, label: '普通用户' },
  { value: 3, label: '超级管理员' }
])

// 直属上级选项
const superiorOptions = ref([
  { value: 0, label: '无' },
  { value: 1, label: '张三' },
  { value: 2, label: '李四' }
])

// 查询表单引用
const queryRef = ref<FormInstance>()

// 表单引用
const userRef = ref<FormInstance>()

// 查询用户列表
const getList = async () => {
  loading.value = true
  try {
    const response = await request.get('/employee/list', {
      params: queryParams
    })
    console.log(response)
    userList.value = response.data.rows
    total.value = response.data.total
  } catch (error) {
    ElMessage.error('获取用户数据失败')
  } finally {
    loading.value = false
  }
}

// 取消按钮
const cancel = () => {
  open.value = false
  reset()
}

// 表单重置
const reset = () => {
  Object.assign(form, {
    id: 0,
    username: '',
    password: '',
    name: '',
    sex: '0',
    phone: '',
    borndate: '',
    email: '',
    department: 1,
    role: 2,
    superior_id: 0,
    create_time: '',
    update_time: '',
    is_delete: 0
  })
  if (userRef.value) {
    userRef.value.resetFields()
  }
}

// 搜索按钮操作
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置按钮操作
const resetQuery = () => {
  if (queryRef.value) {
    queryRef.value.resetFields()
  }
  handleQuery()
}

// 多选框选中数据
const handleSelectionChange = (selection: User[]) => {
  ids.value = selection.map(item => item.id)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

// 新增按钮操作
const handleAdd = () => {
  reset()
  open.value = true
  title.value = "添加用户"
  form.password = "123456"
}

// 修改按钮操作
const handleUpdate = (row: User) => {
  reset()
  Object.assign(form, row)
  open.value = true
  title.value = "修改用户"
}

// 提交按钮
const submitForm = () => {
  if (userRef.value) {
    userRef.value.validate(async (valid: boolean) => {
      if (valid) {
        try {
          if (form.id) {
            // 修改操作
            await request.put('/employee', form)
            ElMessage.success("修改成功")
          } else {
            // 添加操作
            await request.post('/employee', form)
            ElMessage.success("新增成功")
          }
          open.value = false
          getList()
        } catch (error) {
          ElMessage.error(form.id ? "修改失败" : "新增失败")
        }
      }
    })
  }
}

// 删除按钮操作
const handleDelete = (row: User) => {
  const userIds = row.id || ids.value
  ElMessageBox.confirm(
    `是否确认删除用户编号为"${userIds}"的数据项？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      // 执行删除操作
      await request.delete(`/employee/${userIds}`)
      ElMessage.success("删除成功")
      getList()
    } catch (error) {
      ElMessage.error("删除失败")
    }
  }).catch(() => {})
}

// 重置密码按钮操作
const handleResetPwd = (row: User) => {
  ElMessageBox.prompt('请输入"' + row.username + '"的新密码', "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    closeOnClickModal: false,
    inputPattern: /^.{5,20}$/,
    inputErrorMessage: "用户密码长度必须介于 5 和 20 之间"
  }).then(({ value }) => {
    request.put('/employee/resetPwd', {
      id: row.id,
      password: value
    }).then(() => {
      ElMessage.success("修改成功，新密码是：" + value)
    }).catch(() =>{})
  }).catch(() => {})
}

// 页面加载时获取数据
onMounted(() => {
  getList()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}
</style>
