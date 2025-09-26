<template>
  <div class="roles">
    <h1>角色管理</h1>
    <div class="content-area">
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="角色名称">
          <el-input v-model="queryForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable style="width: 120px;">
            <el-option label="正常" value="0" />
            <el-option label="停用" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="fetchRoles">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <div class="toolbar">
        <el-button type="primary" icon="Plus" @click="handleAdd" v-if="permissionStore.hasButtonPermission('/roles/create')">新增角色</el-button>
      </div>

      <el-table :data="roles" border style="width: 100%; margin-top: 20px;">
        <el-table-column prop="id" label="角色编号" width="100" />
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="isDelete" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.isDelete === 0 ? 'success' : 'danger'">
              {{ scope.row.isDelete === 0 ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleEdit(scope.row)" v-if="permissionStore.hasButtonPermission('/roles/update')">修改</el-button>
            <el-button size="small" type="warning" @click="handleAuth(scope.row)" v-if="permissionStore.hasButtonPermission('/role/permissions')">分配权限</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)" v-if="permissionStore.hasButtonPermission('/roles/delete')">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 角色编辑对话框 -->
      <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" @close="resetForm">
        <el-form ref="roleFormRef" :model="roleForm" :rules="roleRules" label-width="100px">
          <el-form-item label="角色名称" prop="roleName">
            <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
          </el-form-item>
          <el-form-item label="状态">
            <el-radio-group v-model="roleForm.isDelete">
              <el-radio :label="0">正常</el-radio>
              <el-radio :label="1">停用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="roleForm.remark" type="textarea" placeholder="请输入备注" />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="submitRole">确 定</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 权限分配对话框 -->
<el-dialog title="分配权限" v-model="authDialogVisible" width="600px">
  <el-form :model="authForm" label-width="100px">
    <el-form-item label="角色名称">
      <el-input v-model="authForm.roleName" disabled />
    </el-form-item>
    <el-form-item label="权限分配">
      <el-tree
        ref="menuTreeRef"
        :data="menuTree"
        show-checkbox
        node-key="id"
        :props="treeProps"
        :default-expanded-keys="defaultExpandedKeys"
      />
    </el-form-item>
  </el-form>
  <template #footer>
    <span class="dialog-footer">
      <el-button @click="closeAuthDialog">取 消</el-button>
      <el-button type="primary" @click="submitAuth">确 定</el-button>
    </span>
  </template>
</el-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted,nextTick,watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { usePermissionStore } from '@/stores/permission'
const permissionStore = usePermissionStore()


// 定义类型
interface Role {
  id: number
  roleName: string
  remark: string
  isDelete: number
  createTime: string
}

interface PermissionItem {
  id: number
  permissionName: string
  permissionDesc: string
  url: string
}

interface ModelItem {
  id: number
  modelName: string
  permissions: PermissionItem[]
}

interface MenuItem {
  id: number
  label: string
  children?: MenuItem[]
}

// 查询表单
const queryForm = reactive({
  roleName: '',
  status: ''
})

// 角色列表数据
const roles = ref<Role[]>([])

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)

// 权限分配对话框
const authDialogVisible = ref(false)

// 表单引用
const roleFormRef = ref()

// 角色表单数据
const roleForm = reactive({
  id: 0,
  roleName: '',
  remark: '',
  isDelete: 0
})

// 权限分配表单
const authForm = reactive({
  roleId: 0,
  roleName: '',
  checkedKeys: [] as number[]
})

// 菜单树数据
const menuTree = ref<MenuItem[]>([])
const menuTreeRef = ref()
const defaultExpandedKeys = ref<number[]>([])

watch([menuTree, () => authForm.checkedKeys], ([newTree, newKeys]) => {
  if (newTree.length > 0 && newKeys.length > 0) {
    nextTick(() => {
      if (menuTreeRef.value) {
        menuTreeRef.value.setCheckedKeys(newKeys)
      }
    })
  }else if (newTree.length > 0 && menuTreeRef.value) {
    // 当没有选中项时，确保清空树的选中状态
    nextTick(() => {
      menuTreeRef.value.setCheckedKeys([])
    })
  }
})

// 树属性配置
const treeProps = {
  label: 'label',
  children: 'children'
}

// 表单验证规则
const roleRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
}

// 重置查询
const resetQuery = () => {
  queryForm.roleName = ''
  queryForm.status = ''
  fetchRoles()
}

// 获取角色列表
const fetchRoles = async () => {
  try {
    // 构建查询参数
    const params: any = {}

    // 角色名称查询条件处理
    if (queryForm.roleName && queryForm.roleName.trim() !== '') {
      params.roleName = queryForm.roleName.trim()
    }

    // 状态查询条件处理
    if (queryForm.status === '0' || queryForm.status === '1') {
      params.isDelete = parseInt(queryForm.status)
    }

    const response = await request.get('/roles/list', { params })

    if (response.data.code === 200) {
      roles.value = response.data.data || []
    } else {
      ElMessage.error(response.data.message || '获取角色列表失败')
    }
  } catch (error) {
    ElMessage.error('获取角色列表失败')
    console.error('获取角色列表失败:', error)
  }
}

// 处理新增
const handleAdd = () => {
  dialogTitle.value = '添加角色'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 处理编辑
const handleEdit = (row: Role) => {
  dialogTitle.value = '修改角色'
  isEdit.value = true
  Object.assign(roleForm, row)
  dialogVisible.value = true
}

// 处理删除
const handleDelete = (row: Role) => {
  ElMessageBox.confirm(`是否确认删除角色 "${row.roleName}"?`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await request.post('/roles/delete', null, {
        params: { id: row.id }
      })
      if (response.data.code === 200) {
        ElMessage.success('删除成功')
        fetchRoles()
      } else {
        ElMessage.error(response.data.message || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败')
      console.error('删除角色失败:', error)
    }
  }).catch(() => {})
}

// 重置表单
const resetForm = () => {
  roleForm.id = 0
  roleForm.roleName = ''
  roleForm.remark = ''
  roleForm.isDelete = 0
}

// 提交角色
const submitRole = () => {
  roleFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        let response
        if (isEdit.value) {
          // 更新角色
          response = await request.post('/roles/update', roleForm, {
            params: { id: roleForm.id }
          })
        } else {
          // 新增角色
          response = await request.post('/roles/create', roleForm)
        }

        if (response.data.code === 200) {
          ElMessage.success(response.data.message || `${isEdit.value ? '修改' : '新增'}成功`)
          dialogVisible.value = false
          fetchRoles()
        } else {
          ElMessage.error(response.data.message || `${isEdit.value ? '修改' : '新增'}失败`)
        }
      } catch (error) {
        ElMessage.error(`${isEdit.value ? '修改' : '新增'}失败`)
        console.error(`${isEdit.value ? '修改' : '新增'}角色失败:`, error)
      }
    }
  })
}


// 处理权限分配
const handleAuth = async (row: Role) => {
  authForm.roleId = row.id
  authForm.roleName = row.roleName
  authForm.checkedKeys = [] // 重置选中项
  authDialogVisible.value = false

  // 重置树的选中状态
  if (menuTreeRef.value) {
    menuTreeRef.value.setCheckedKeys([])
  }

  // 再获取菜单树
  await fetchMenuTree()


  // 先获取角色已有的权限
  await fetchRoleMenus(row.id)
  authDialogVisible.value = true

}

// 在<script setup>中添加一个新的函数来处理对话框关闭
const closeAuthDialog = () => {
  authDialogVisible.value = false
  // 清空相关状态
  authForm.roleId = 0
  authForm.roleName = ''
  authForm.checkedKeys = []
  // 重置树的选中状态
  nextTick(() => {
    if (menuTreeRef.value) {
      menuTreeRef.value.setCheckedKeys([])
    }
  })
}

/**
* 异步获取菜单树数据
*
* @returns 无返回值
*/
// 获取角色已分配的菜单权限
// 获取角色已分配的菜单权限
// 获取角色已分配的菜单权限
// 修改 fetchRoleMenus 函数中的 URL 映射部分
const fetchRoleMenus = async (roleId: number) => {
  try {
    const response = await request.get('/permission/getPermission', {
      params: { roleId: roleId }
    })

    console.log('获取角色权限完整响应:', response.data)

    if (response.data.code === 200) {
      // 返回的数据结构是 {1: ['/employee/list', ...], 2: [...], ...}
      const permissionData = response.data.data || {}
      console.log('权限数据:', permissionData)

      // 收集所有已分配的权限URL（过滤null值）
      const assignedUrls: string[] = []
      for (const moduleId in permissionData) {
        if (Array.isArray(permissionData[moduleId])) {
          permissionData[moduleId].forEach(url => {
            if (url) assignedUrls.push(url) // 过滤null值
          })
        }
      }

      console.log('已分配的权限URL:', assignedUrls)

      // 构建URL到权限ID的映射（使用递归处理所有节点）
      const urlToIdMap: {[key: string]: number} = {}

      const buildUrlMap = (nodes: MenuItem[]) => {
        nodes.forEach(node => {
          if (node.children && node.children.length > 0) {
            buildUrlMap(node.children) // 递归处理子节点
          } else {
            // 从标签中提取URL，格式为 "描述 (URL)"
            const match = node.label.match(/\(([^)]+)\)$/)
            if (match && match[1]) {
              const url = match[1]
              urlToIdMap[url] = node.id
            }
          }
        })
      }

      buildUrlMap(menuTree.value) // 调用递归函数

      console.log('URL到ID映射:', urlToIdMap)

      // 将URL转换为权限ID
      const assignedPermissionIds = assignedUrls
        .map(url => {
          console.log(`映射URL: ${url} -> ID: ${urlToIdMap[url]}`)
          return urlToIdMap[url]
        })
        .filter(id => id !== undefined)

      authForm.checkedKeys = assignedPermissionIds
      console.log('映射后的权限ID:', authForm.checkedKeys)

      // 确保DOM更新后设置选中状态
      nextTick(() => {
        if (menuTreeRef.value) {
          console.log('设置选中项:', authForm.checkedKeys)
          menuTreeRef.value.setCheckedKeys(authForm.checkedKeys)
        }
      })
    } else {
      ElMessage.error(response.data.message || '获取角色菜单权限失败')
      resetAuthSelection()
    }
    return Promise.resolve()
  } catch (error) {
    ElMessage.error('获取角色菜单权限失败')
    console.error('获取角色菜单权限失败:', error)
    resetAuthSelection()
    return Promise.resolve()
  }
}

// 新增重置选中状态的工具函数
const resetAuthSelection = () => {
  authForm.checkedKeys = []
  nextTick(() => {
    if (menuTreeRef.value) {
      menuTreeRef.value.setCheckedKeys([])
    }
  })
}

// 获取权限模型列表并构建树形结构
// 修改 fetchMenuTree 函数
const fetchMenuTree = async () => {
  try {
    const response = await request.get('/permission/tree')

    if (response.data.code === 200) {
      // 将模块和权限数据转换为树形结构，处理空权限模块
      const treeData = response.data.data.map((model: ModelItem) => {
        return {
          id: model.id,
          label: model.modelName,
          children: model.permissions.length > 0
            ? model.permissions.map(permission => ({
                id: permission.id,
                label: `${permission.permissionDesc} (${permission.url})`,
                isLeaf: true // 明确标记为叶子节点
              }))
            : [{
                id: model.id + 1000, // 为无权限模块添加占位节点
                label: '无可用权限',
                disabled: true,
                isLeaf: true
              }]
        }
      })

      menuTree.value = treeData

      // 默认展开所有模块节点
      defaultExpandedKeys.value = response.data.data.map((model: ModelItem) => model.id)

      console.log('权限树数据:', treeData)

      // 确保树渲染后设置选中状态
      nextTick(() => {
        if (menuTreeRef.value) {
          menuTreeRef.value.setCheckedKeys(authForm.checkedKeys)
        }
      })
    } else {
      ElMessage.error(response.data.message || '获取权限模型失败')
      menuTree.value = []
      defaultExpandedKeys.value = []
    }
  } catch (error) {
    ElMessage.error('获取权限模型失败')
    console.error('获取权限模型失败:', error)
    menuTree.value = []
    defaultExpandedKeys.value = []
  }
}

// 修改 submitAuth 函数
const submitAuth = async () => {
  try {
    // 获取选中的权限ID（只取叶子节点）
    const checkedKeys = menuTreeRef.value.getCheckedKeys(true)
    // 获取所有有选中权限的模块ID
    const moduleIds = new Set<number>()

    // 递归查找选中权限所属的模块ID
    const findParentModules = (nodes: MenuItem[], childId: number, parentId?: number) => {
      for (const node of nodes) {
        if (node.id === childId && parentId) {
          moduleIds.add(parentId)
          return true
        }
        if (node.children && node.children.length > 0) {
          if (findParentModules(node.children, childId, node.id)) {
            return true
          }
        }
      }
      return false
    }

    // 为每个选中的权限找到对应的模块
    checkedKeys.forEach(key => {
      findParentModules(menuTree.value, key)
    })

    console.log('选中的权限ID:', checkedKeys)
    console.log('关联的模块ID:', Array.from(moduleIds))

    if (checkedKeys.length === 0) {
      ElMessage.warning('请至少选择一个权限')
      return
    }

    // 构造请求参数
    const params = new URLSearchParams()
    params.append('roleId', authForm.roleId.toString())
    params.append('modelsId', Array.from(moduleIds).join(','))
    params.append('permissionsId', checkedKeys.join(','))

    // 调用后端接口保存权限
    const response = await request.post('/permission/savePermission', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })

    console.log('权限分配响应:', response)

    if (response.data.code === 200) {
      ElMessage.success('权限分配成功')
      closeAuthDialog()
    } else {
      ElMessage.error(response.data.message || '权限分配失败')
    }
  } catch (error) {
    ElMessage.error('权限分配失败')
    console.error('权限分配失败:', error)
  }
}
// 初始化
onMounted(() => {
  fetchRoles()
})
</script>

<style scoped>
.roles {
  padding: 20px;
}

.content-area {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.query-form {
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
}
</style>
