<!-- src/views/console/DepartmentsView.vue -->
<template>
  <div class="app-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
        <el-form-item label="部门名称" prop="department_name">
          <el-input
            v-model="queryParams.department_name"
            placeholder="请输入部门名称"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="is_delete">
          <el-select
            v-model="queryParams.is_delete"
            placeholder="部门状态"
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
          <span>部门列表</span>
          <div style="float: right">
            <el-button
              type="primary"
              plain
              icon="Plus"
              @click="handleAdd()"
              v-hasPermi="['system:dept:add']"
            >新增</el-button>
          </div>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="deptList"
        row-key="department_id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        default-expand-all
      >
        <el-table-column prop="department_name" label="部门名称" width="300"></el-table-column>
        <el-table-column prop="orderNum" label="排序" width="200"></el-table-column>
        <el-table-column prop="is_delete" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.is_delete === 0 ? 'primary' : 'info'">
              {{ statusFormat(scope.row) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="create_time" width="200">
          <template #default="scope">
            <span>{{ scope.row.create_time }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
              type="primary"
              link
              icon="Edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:dept:edit']"
            >修改</el-button>
            <el-button
              type="primary"
              link
              icon="Plus"
              @click="handleAdd(scope.row)"
              v-hasPermi="['system:dept:add']"
            >新增</el-button>
            <el-button
              type="primary"
              link
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:dept:remove']"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="deptRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="form.super_depart_id !== 0">
            <el-form-item label="上级部门" prop="super_depart_id">
              <el-tree-select
                v-model="form.super_depart_id"
                :data="deptOptions"
                :props="{ value: 'department_id', label: 'department_name', children: 'children' }"
                value-key="department_id"
                placeholder="选择上级部门"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="department_name">
              <el-input v-model="form.department_name" placeholder="请输入部门名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门状态">
              <el-radio-group v-model="form.is_delete">
                <el-radio
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}</el-radio>
              </el-radio-group>
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
import axios from 'axios'

// 定义数据类型
interface Dept {
  department_id: number
  super_depart_id: number | null
  department_name: string
  orderNum?: number
  phone?: string
  is_delete: number
  create_time: string
  modify_time: string
  children?: Dept[]
}

// 模拟字典数据
const sys_normal_disable = [
  { value: 0, label: '正常' },
  { value: 1, label: '停用' }
]

// 状态格式化
const statusFormat = (row: Dept) => {
  const status = sys_normal_disable.find(dict => dict.value === row.is_delete)
  return status ? status.label : ''
}

// 页面加载状态
const loading = ref(true)

// 查询参数
const queryParams = reactive({
  department_name: '',
  is_delete: undefined
})

// 表单参数
const form = reactive<Dept>({
  department_id: 0,
  super_depart_id: 0,
  department_name: '',
  orderNum: 0,
  phone: '',
  is_delete: 0,
  create_time: '',
  modify_time: ''
})

// 表单校验规则
const rules = {
  super_depart_id: [{ required: true, message: "上级部门不能为空", trigger: "blur" }],
  department_name: [{ required: true, message: "部门名称不能为空", trigger: "blur" }],
  orderNum: [{ required: true, message: "显示排序不能为空", trigger: "blur" }],
  phone: [
    {
      pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
      message: "请输入正确的手机号码",
      trigger: "blur"
    }
  ]
}

// 对话框标题
const title = ref("")

// 是否显示弹出层
const open = ref(false)

// 部门表格数据
const deptList = ref<Dept[]>([])

// 部门树选项
const deptOptions = ref<Dept[]>([])

// 查询表单引用
const queryRef = ref<FormInstance>()

// 表单引用
const deptRef = ref<FormInstance>()

// 查询部门列表
const getList = async () => {
  loading.value = true
  try {
    const response = await axios.get('/department/list', {
      params: queryParams
    })

    // 处理后端返回的数据结构
    const rawData = response.data.data
    const processedData: Dept[] = []

    // 遍历返回的对象，将键值对转换为树形结构
    for (const key in rawData) {
      if (rawData.hasOwnProperty(key)) {
        // 从键中提取父部门信息
        const match = key.match(/Department\((.*?)\)/)
        if (match) {
          const props = match[1].split(', ').reduce((acc: any, prop) => {
            const [propName, propValue] = prop.split('=')
            acc[propName] = propName.includes('Id') || propName === 'isDelete' ?
              (propValue === 'null' ? null : Number(propValue)) : propValue
            return acc
          }, {})

          // 创建父部门对象
          const parentDept: Dept = {
            department_id: props.departmentId,
            department_name: props.departmentName,
            phone: props.phone,
            super_depart_id: props.superDepartId,
            is_delete: props.isDelete,
            create_time: props.createTime,
            modify_time: props.modifyTime,
            children: rawData[key].map((child: any) => ({
              department_id: child.departmentId,
              department_name: child.departmentName,
              phone: child.phone,
              super_depart_id: child.superDepartId,
              is_delete: child.isDelete,
              create_time: child.createTime,
              modify_time: child.modifyTime
            }))
          }

          processedData.push(parentDept)
        }
      }
    }

    deptList.value = processedData
  } catch (error) {
    ElMessage.error('获取部门数据失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 获取部门下拉树列表
const getTreeselect = async () => {
  try {
    const response = await axios.get('/department/tree')

    // 处理后端返回的数据结构
    const rawData = response.data.data
    const processedData: Dept[] = []

    // 遍历返回的对象，将键值对转换为树形结构
    for (const key in rawData) {
      if (rawData.hasOwnProperty(key)) {
        // 从键中提取父部门信息
        const match = key.match(/Department\((.*?)\)/)
        if (match) {
          const props = match[1].split(', ').reduce((acc: any, prop) => {
            const [propName, propValue] = prop.split('=')
            acc[propName] = propName.includes('Id') || propName === 'isDelete' ?
              (propValue === 'null' ? null : Number(propValue)) : propValue
            return acc
          }, {})

          // 创建父部门对象
          const parentDept: Dept = {
            department_id: props.departmentId,
            department_name: props.departmentName,
            phone: props.phone,
            super_depart_id: props.superDepartId,
            is_delete: props.isDelete,
            create_time: props.createTime,
            modify_time: props.modifyTime,
            children: rawData[key].map((child: any) => ({
              department_id: child.departmentId,
              department_name: child.departmentName,
              phone: child.phone,
              super_depart_id: child.superDepartId,
              is_delete: child.isDelete,
              create_time: child.createTime,
              modify_time: child.modifyTime
            }))
          }

          processedData.push(parentDept)
        }
      }
    }

    deptOptions.value = processedData
  } catch (error) {
    ElMessage.error('获取部门选项失败')
    console.error(error)
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
    department_id: 0,
    super_depart_id: 0,
    department_name: '',
    orderNum: 0,
    phone: '',
    is_delete: 0,
    create_time: '',
    modify_time: ''
  })
  // 正确调用表单重置方法
  if (deptRef.value) {
    deptRef.value.resetFields()
  }
}

// 搜索按钮操作
const handleQuery = () => {
  getList()
}

// 重置按钮操作
const resetQuery = () => {
  // 正确调用表单重置方法
  if (queryRef.value) {
    queryRef.value.resetFields()
  }
  handleQuery()
}

// 新增按钮操作
const handleAdd = (row?: Dept) => {
  reset()
  getTreeselect()
  if (row && row.department_id) {
    form.super_depart_id = row.department_id
  } else {
    form.super_depart_id = 0
  }
  open.value = true
  title.value = "添加部门"
}

// 修改按钮操作
const handleUpdate = (row: Dept) => {
  reset()
  getTreeselect()
  Object.assign(form, {
    department_id: row.department_id,
    super_depart_id: row.super_depart_id || 0,
    department_name: row.department_name,
    orderNum: row.orderNum || 0,
    phone: row.phone || '',
    is_delete: row.is_delete,
    create_time: row.create_time,
    modify_time: row.modify_time
  })
  open.value = true
  title.value = "修改部门"
}

// 提交按钮
const submitForm = () => {
  // 正确调用表单验证方法
  if (deptRef.value) {
    deptRef.value.validate(async (valid: boolean) => {
      if (valid) {
        try {
          // 准备提交数据
          const submitData = {
            departmentId: form.department_id,
            superDepartId: form.super_depart_id,
            departmentName: form.department_name,
            orderNum: form.orderNum,
            phone: form.phone,
            isDelete: form.is_delete,
            createTime: form.create_time,
            modifyTime: form.modify_time
          }

          if (form.department_id) {
            // 修改操作
            await axios.put('/department', submitData)
            ElMessage.success("修改成功")
          } else {
            // 添加操作
            await axios.post('/department', submitData)
            ElMessage.success("新增成功")
          }
          open.value = false
          getList()
        } catch (error) {
          ElMessage.error(form.department_id ? "修改失败" : "新增失败")
          console.error(error)
        }
      }
    })
  }
}

// 删除按钮操作
const handleDelete = (row: Dept) => {
  ElMessageBox.confirm(
    `是否确认删除名称为"${row.department_name}"的数据项？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      // 执行删除操作
      await axios.delete(`/department/${row.department_id}`)
      ElMessage.success("删除成功")
      getList()
    } catch (error) {
      ElMessage.error("删除失败")
      console.error(error)
    }
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
