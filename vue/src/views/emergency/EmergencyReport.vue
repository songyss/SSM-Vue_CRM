<template>
  <div class="emergency-report">
    <el-card>
      <template #header>
        <div class="card-header">新建突发事件报告</div>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <!-- 标题 -->
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入事件标题" />
        </el-form-item>

        <!-- 类型 -->
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="折扣问题" value="DISCOUNT" />
            <el-option label="付款问题" value="PAYMENT" />
            <el-option label="交付问题" value="DELIVERY" />
            <el-option label="合同问题" value="CONTRACT" />
          </el-select>
        </el-form-item>

        <!-- 客户 -->
        <el-form-item label="客户" prop="customerId">
          <el-select
            v-if="!customerIdFromRoute"
            v-model="form.customerId"
            placeholder="请选择客户"
            filterable
          >
            <el-option
              v-for="c in customerList"
              :key="c.id"
              :label="c.name"
              :value="c.id"
            />
          </el-select>
          <el-input v-else v-model="form.customerName" disabled />
        </el-form-item>

        <!-- 描述 -->
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" />
        </el-form-item>

        <!-- 客户诉求 -->
        <el-form-item label="客户诉求">
          <el-input v-model="form.customerRequest" type="textarea" :rows="3" />
        </el-form-item>

        <!-- 紧急程度 -->
        <el-form-item label="紧急程度" prop="urgency">
          <el-select v-model="form.urgency" placeholder="请选择紧急程度" style="width: 100%">
            <el-option label="特急" :value="1" />
            <el-option label="紧急" :value="2" />
            <el-option label="普通" :value="3" />
            <el-option label="低" :value="4" />
          </el-select>
        </el-form-item>

        <!-- 风险等级 -->
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="form.riskLevel" placeholder="请选择风险等级" style="width: 100%">
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>

        <!-- 成本影响 -->
        <el-form-item label="成本影响">
          <el-input v-model="form.costImpact" placeholder="请输入成本影响" />
        </el-form-item>

        <!-- 解决方案 -->
        <el-form-item label="解决方案" prop="solutionContent">
          <el-input v-model="form.solutionContent" type="textarea" :rows="4" />
        </el-form-item>

        <!-- 发生时间 -->
        <el-form-item label="发生时间" prop="occurTime">
          <el-date-picker
            v-model="form.occurTime"
            type="datetime"
            placeholder="选择发生时间"
            style="width: 100%"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <!-- 截止时间 -->
        <el-form-item label="截止时间">
          <el-date-picker
            v-model="form.deadline"
            type="datetime"
            placeholder="选择截止时间"
            style="width: 100%"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <!-- 🔵 提交给谁 -->
        <el-form-item label="提交给">
          <el-input v-model="leaderName" disabled placeholder="提交时会自动带出直属上级" />
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import request from '@/utils/request'

// 表单引用
const formRef = ref<FormInstance>()
const route = useRoute()

// 从登录缓存读取 userInfo
const userInfo = JSON.parse(localStorage.getItem('crm_userInfo') || '{}')
const empId = Number(userInfo.userId) || null

// 从路由获取可能传入的 customerId / customerName
const customerIdFromRoute = route.query.customerId ? Number(route.query.customerId) : null
const customerNameFromRoute = route.query.customerName ? String(route.query.customerName) : null

// 客户列表
const customerList = ref<{ id: number; name: string }[]>([])

// 🔵 直属上级信息
const leaderId = ref<number | null>(null)
const leaderName = ref<string>('')

// 表单数据
const form = reactive({
  title: '',
  type: '',
  description: '',
  customerId: customerIdFromRoute || null,
  customerName: customerNameFromRoute || '',
  customerRequest: '',
  urgency: null,
  riskLevel: '',
  costImpact: '',
  solutionContent: '',
  occurTime: '',
  deadline: ''
})

// 校验规则
const rules: FormRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  description: [{ required: true, message: '请输入描述', trigger: 'blur' }],
  urgency: [{ required: true, message: '请选择紧急程度', trigger: 'change' }],
  riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
  occurTime: [{ required: true, message: '请选择发生时间', trigger: 'change' }]
}

// 拉取直属上级
const fetchLeader = async () => {
  if (!empId) return
  try {
    const res = await request.get(`/employee/${empId}/leader`)
    const leader = res.data
    leaderId.value = leader.id
    leaderName.value = leader.name
  } catch (err) {
    console.error('fetchLeader error', err)
    ElMessage.warning('未能获取直属上级，提交时可能失败')
  }
}

// 拉取客户列表
const fetchCustomerList = async () => {
  if (!empId) return
  try {
    const res = await request.get('/customer/myCustomers', {
      params: { employeeId: empId, page: 1, size: 1000 }
    })
    const pageInfo = res.data?.data || {}
    customerList.value = pageInfo.list || []
  } catch (err) {
    console.error('fetchCustomerList error', err)
  }
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value?.validate()
    if (!empId) {
      ElMessage.error('未检测到登录用户信息，无法提交')
      return
    }

    const payload = { ...form }
    delete (payload as any).customerName

    // 🔵 带上直属上级（虽然后端也会查，但这里一起传）
    payload.currentHandlerId = leaderId.value

    const res = await request.post('/api/salesEmergency', payload, {
      params: { empId }
    })

    // 🔵 成功后显示“提交给了谁”
    const leader = res.data?.data?.leaderName
    if (leader) {
      ElMessage.success(`提交成功，提交给了：${leader}`)
    } else {
      ElMessage.success(res.data?.message || '提交成功')
    }

    resetForm()
  } catch (err) {
    console.error('submitForm error', err)
    ElMessage.error('提交失败，请检查输入或查看后端日志')
  }
}

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields()
  form.customerId = customerIdFromRoute || null
  form.customerName = customerNameFromRoute || ''
}

onMounted(() => {
  fetchCustomerList()
  fetchLeader() // 🔵 页面加载时获取直属上级
})
</script>

<style scoped>
.emergency-report {
  padding: 20px;
}
.card-header {
  font-size: 18px;
  font-weight: bold;
}
</style>
