<template>
  <div class="form-builder">
    <div class="builder-container">
      <!-- 组件库区域 -->
      <div class="components-panel">
        <h3>组件库</h3>
        <div
          class="component-item"
          v-for="comp in componentList"
          :key="comp.type"
          draggable="true"
          @dragstart="onDragStart($event, comp)"
        >
          <i :class="comp.icon"></i>
          {{ comp.name }}
        </div>
      </div>

      <!-- 表单设计区域 -->
      <div class="design-panel">
        <div class="panel-header">
          <h3>表单设计区</h3>
          <div class="actions">
            <el-button type="primary" @click="previewForm">预览</el-button>
            <el-button type="success" @click="generateCode">生成代码</el-button>
            <el-button @click="clearForm">清空</el-button>
          </div>
        </div>

        <div
          class="form-canvas"
          @dragover.prevent="isDragOver = true"
          @dragleave="isDragOver = false"
          @drop="onDrop"
          :class="{ 'drag-over': isDragOver }"
        >
          <div v-if="formConfig.fields.length === 0" class="empty-tip">
            从左侧拖拽组件到此处
          </div>

          <draggable
  v-model="formConfig.fields"
  tag="div"
  group="form-fields"
  :animation="200"
  handle=".field-handle"
  @start="dragging = true"
  @end="dragging = false"
  item-key="key"
  :force-fallback="true"
  fallback-class="dragging-fallback"
>
  <template #item="{ element: field, index }">
    <div
      class="form-field"
      :class="{ 'active': selectedField && selectedField.key === field.key }"
      @click="selectField(field)"
    >
      <div class="field-handle">
        <i class="el-icon-rank"></i>
      </div>

      <div class="field-content">
        <!-- 渲染各种表单组件 -->
        <component
          :is="getFieldComponent(field.type)"
          v-bind="field.props"
          :placeholder="field.props.placeholder || `请输入${field.props.label}`"
        />
      </div>

      <div class="field-actions">
        <i class="el-icon-copy-document" @click.stop="copyField(index)"></i>
        <i class="el-icon-delete" @click.stop="deleteField(index)"></i>
      </div>
    </div>
  </template>
</draggable>
        </div>
      </div>

      <!-- 属性配置区域 -->
      <div class="config-panel">
        <h3>属性配置</h3>
        <div class="config-content" v-if="selectedField">
          <el-form label-width="80px" size="small">
            <el-form-item label="字段名">
              <el-input v-model="selectedField.props.prop" />
            </el-form-item>
            <el-form-item label="标签">
              <el-input v-model="selectedField.props.label" />
            </el-form-item>
            <el-form-item label="占位符">
              <el-input v-model="selectedField.props.placeholder" />
            </el-form-item>

            <!-- 根据组件类型显示特定配置 -->
            <el-form-item v-if="selectedField.type === 'input'" label="类型">
              <el-select v-model="selectedField.props.type">
                <el-option label="文本" value="text" />
                <el-option label="数字" value="number" />
                <el-option label="密码" value="password" />
              </el-select>
            </el-form-item>

            <el-form-item v-if="selectedField.type === 'select'" label="选项">
              <el-input
                v-model="selectOptions"
                type="textarea"
                :rows="3"
                placeholder="每行一个选项，格式：值=标签"
                @change="updateSelectOptions"
              />
            </el-form-item>

            <el-form-item label="是否必填">
              <el-switch v-model="selectedField.props.required" />
            </el-form-item>

            <el-form-item
              v-if="selectedField.props.required"
              label="错误提示"
            >
              <el-input v-model="selectedField.props.requiredMessage" />
            </el-form-item>
          </el-form>
        </div>
        <div class="config-content" v-else>
          <p class="tip">请选择一个字段进行配置</p>
        </div>
      </div>
    </div>

    <!-- 预览对话框 -->
    <el-dialog
      title="表单预览"
      v-model="previewVisible"
      width="600px"
      :modal="true"
    >
      <el-form
        ref="previewFormRef"
        :model="previewData"
        :rules="previewRules"
        label-width="100px"
      >
        <draggable
          v-model="previewFields"
          tag="ul"
          group="preview-fields"
          :animation="isFirefox ? 120 : 150"
:delay="isFirefox ? 70 : 50"
:delayOnTouchOnly="true"
          item-key="key"
        >
          <template #item="{ element: field }">
            <el-form-item
              :label="field.props.label"
              :prop="field.props.prop"
              :rules="getRules(field)"
            >
              <component
                :is="getFieldComponent(field.type)"
                v-model="previewData[field.props.prop]"
                v-bind="field.props"
              />
            </el-form-item>
          </template>
        </draggable>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="previewVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitPreview">提 交</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 代码生成对话框 -->
    <el-dialog
      title="生成代码"
      v-model="codeVisible"
      width="800px"
      :modal="true"
    >
      <el-tabs v-model="activeCodeTab">
        <el-tab-pane label="Vue Template" name="template">
          <pre class="code-block">{{ generatedTemplate }}</pre>
        </el-tab-pane>
        <el-tab-pane label="Vue Script" name="script">
          <pre class="code-block">{{ generatedScript }}</pre>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="codeVisible = false">关 闭</el-button>
          <el-button type="primary" @click="copyCode">复 制</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, nextTick, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import draggable from 'vuedraggable'

// 检测是否为Firefox浏览器
const isFirefox = ref(false)

// 组件列表
const componentList = [
  { type: 'input', name: '单行文本', icon: 'el-icon-edit' },
  { type: 'textarea', name: '多行文本', icon: 'el-icon-document' },
  { type: 'select', name: '下拉选择', icon: 'el-icon-arrow-down' },
  { type: 'radio', name: '单选框', icon: 'el-icon-circle-check' },
  { type: 'checkbox', name: '复选框', icon: 'el-icon-check' },
  { type: 'date', name: '日期选择', icon: 'el-icon-date' },
  { type: 'number', name: '计数器', icon: 'el-icon-plus' },
  { type: 'switch', name: '开关', icon: 'el-icon-open' },
  { type: 'file', name: '文件上传', icon: 'el-icon-upload' },
  { type: 'time', name: '时间选择', icon: 'el-icon-time' },
  { type: 'email', name: '邮箱输入', icon: 'el-icon-email' },
  { type: 'url', name: 'URL输入', icon: 'el-icon-link' },
  { type: 'phone', name: '手机号输入', icon: 'el-icon-phone' },
  { type: 'captcha', name: '验证码输入', icon: 'el-icon-check' },
]

// 表单配置
const formConfig = reactive({
  name: '自定义表单',
  fields: []
})

// 选中字段
const selectedField = ref(null)

// 拖拽状态
const isDragOver = ref(false)
const dragging = ref(false)

// 预览相关
const previewVisible = ref(false)
const previewFormRef = ref(null)
const previewData = ref({})
const previewRules = ref({})
const previewFields = ref([]) // 用于预览的独立字段数组

// 代码生成相关
const codeVisible = ref(false)
const activeCodeTab = ref('template')
const generatedTemplate = ref('')
const generatedScript = ref('')

// 下拉选项临时存储
const selectOptions = computed({
  get() {
    if (!selectedField.value || selectedField.value.type !== 'select') return ''
    return selectedField.value.props.options
      .map(opt => `${opt.value}=${opt.label}`)
      .join('\n')
  },
  set(value) {
    // 在 updateSelectOptions 中处理
  }
})

// 拖拽开始
const onDragStart = (e, component) => {
  if (!e.dataTransfer) {
    console.error('当前浏览器不支持拖拽API');
    return;
  }
  try {
    // 设置拖拽数据为JSON字符串
    const jsonData = JSON.stringify(component);
    e.dataTransfer.setData('application/json', jsonData);

    // 设置拖拽效果
    e.dataTransfer.effectAllowed = 'copyMove';

    // 标记为拖拽中状态
    if (e.target) {
      e.target.setAttribute('dragging', 'true');
      // 拖拽结束时移除标记
      setTimeout(() => {
        if (e.target) {
          e.target.removeAttribute('dragging');
        }
      }, 0);
    }

    // 可选：设置拖拽图像（显示一个半透明的组件图标）
    // const dragImage = document.createElement('div');
    // dragImage.innerHTML = `<i class="${component.icon}"></i> ${component.name}`;
    // dragImage.style.padding = '10px';
    // dragImage.style.backgroundColor = '#f5f7fa';
    // dragImage.style.borderRadius = '4px';
    // document.body.appendChild(dragImage);
    // e.dataTransfer.setDragImage(dragImage, 15, 15);
    // setTimeout(() => document.body.removeChild(dragImage), 0);
  } catch (error) {
    console.error('拖拽开始错误:', error);
    ElMessage.error('拖拽初始化失败');
  }
};

// 放置组件
const onDrop = (e) => {

  e.preventDefault(); // 必须阻止默认行为
  isDragOver.value = false;
  if (!e.dataTransfer) {
    console.error('当前浏览器不支持拖拽API');
    return;
  }

  try {
    let componentData = null;

    // 尝试获取不同格式的数据
    const formats = ['application/json', 'text/plain', 'Text'];
    for (const format of formats) {
      const data = e.dataTransfer.getData(format);
      if (data) {
        try {
          componentData = JSON.parse(data);
          break;
        } catch (e) {
          console.warn(`无法解析格式为 ${format} 的数据:`, data);
        }
      }
    }

    if (componentData) {
      addField(componentData);
    } else {
      console.error('未获取到有效的拖拽数据');
      ElMessage.warning('无法识别拖拽的组件');
    }
  } catch (error) {
    console.error('放置错误:', error);
    ElMessage.error('放置组件失败');
  }
};

// 添加字段
const addField = (component) => {
  const key = `${component.type}_${Date.now()}`
  const defaultProps = {
    prop: `${component.type}${formConfig.fields.length + 1}`,
    label: component.name,
    placeholder: '',
    required: false,
    requiredMessage: ''
  }

  let field = {
    key,
    type: component.type,
    props: { ...defaultProps }
  }

  // 根据组件类型设置特定属性
  switch (component.type) {
    case 'input':
      field.props.type = 'text'
      break
    case 'select':
      field.props.options = [
        { value: 'option1', label: '选项1' },
        { value: 'option2', label: '选项2' }
      ]
      break
    case 'radio':
      field.props.options = [
        { value: 'radio1', label: '选项1' },
        { value: 'radio2', label: '选项2' }
      ]
      break
    case 'checkbox':
      field.props.options = [
        { value: 'checkbox1', label: '选项1' },
        { value: 'checkbox2', label: '选项2' }
      ]
      break
    case 'number':
      field.props.min = 0
      field.props.max = 100
      field.props.step = 1
      break
  }

  formConfig.fields.push(field)
  selectField(field)
}

// 选择字段
const selectField = (field) => {
  selectedField.value = field
}

// 获取字段对应的组件
const getFieldComponent = (type) => {
  const componentMap = {
    input: 'el-input',
    textarea: 'el-input',
    select: 'el-select',
    radio: 'el-radio-group',
    checkbox: 'el-checkbox-group',
    date: 'el-date-picker',
    number: 'el-input-number',
    switch: 'el-switch'
  }

  return componentMap[type] || 'el-input'
}

// 更新下拉选项
const updateSelectOptions = () => {
  if (!selectedField.value || selectedField.value.type !== 'select') return

  const options = selectOptions.value
    .split('\n')
    .filter(line => line.trim() !== '')
    .map(line => {
      const [value, label] = line.split('=')
      return { value: value.trim(), label: label ? label.trim() : value.trim() }
    })

  selectedField.value.props.options = options
}

// 复制字段
const copyField = (index) => {
  const field = JSON.parse(JSON.stringify(formConfig.fields[index]))
  field.key = `${field.type}_${Date.now()}`
  field.props.prop = `${field.type}${formConfig.fields.length + 1}`
  formConfig.fields.splice(index + 1, 0, field)
  selectField(field)
}

// 删除字段
const deleteField = (index) => {
  ElMessageBox.confirm('确定要删除这个字段吗？', '提示', {
    type: 'warning'
  }).then(() => {
    formConfig.fields.splice(index, 1)
    if (selectedField.value && selectedField.value.key === formConfig.fields[index]?.key) {
      selectedField.value = null
    }
  }).catch(() => {
    // 取消删除
  })
}

// 清空表单
const clearForm = () => {
  ElMessageBox.confirm('确定要清空所有字段吗？', '提示', {
    type: 'warning'
  }).then(() => {
    formConfig.fields = []
    selectedField.value = null
  }).catch(() => {
    // 取消清空
  })
}

// 获取验证规则
const getRules = (field) => {
  const rules = []
  if (field.props.required) {
    rules.push({
      required: true,
      message: field.props.requiredMessage || `${field.props.label}不能为空`,
      trigger: ['blur', 'change']
    })
  }
  return rules
}

// 预览表单
const previewForm = () => {
  if (formConfig.fields.length === 0) {
    ElMessage.warning('请先添加表单字段')
    return
  }

  // 初始化预览数据
  const data = {}
  const rules = {}
  
  // 深拷贝字段配置到预览字段，避免预览时修改影响实际配置
  previewFields.value = JSON.parse(JSON.stringify(formConfig.fields))

  formConfig.fields.forEach(field => {
    data[field.props.prop] = ''
    rules[field.props.prop] = getRules(field)
  })

  previewData.value = data
  previewRules.value = rules
  previewVisible.value = true
}

// 提交预览表单
const submitPreview = () => {
  previewFormRef.value.validate((valid) => {
    if (valid) {
      ElMessage.success('表单提交成功！')
      console.log('表单数据：', previewData.value)
      previewVisible.value = false
    } else {
      ElMessage.error('请完善表单信息')
    }
  })
}

// 生成代码
const generateCode = () => {
  if (formConfig.fields.length === 0) {
    ElMessage.warning('请先添加表单字段')
    return
  }

  // 生成模板代码
  let template = `<template>\n  <el-form\n    ref="formRef"\n    :model="formData"\n    :rules="formRules"\n    label-width="100px"\n  >\n`

  formConfig.fields.forEach(field => {
    template += `    <el-form-item label="${field.props.label}" prop="${field.props.prop}">\n`

    switch (field.type) {
      case 'input':
        template += `      <el-input v-model="formData.${field.props.prop}" placeholder="${field.props.placeholder}" />\n`
        break
      case 'textarea':
        template += `      <el-input v-model="formData.${field.props.prop}" type="textarea" placeholder="${field.props.placeholder}" />\n`
        break
      case 'select':
        template += `      <el-select v-model="formData.${field.props.prop}" placeholder="${field.props.placeholder}">\n`
        field.props.options.forEach(opt => {
          template += `        <el-option label="${opt.label}" value="${opt.value}" />\n`
        })
        template += `      </el-select>\n`
        break
      case 'radio':
        template += `      <el-radio-group v-model="formData.${field.props.prop}">\n`
        field.props.options.forEach(opt => {
          template += `        <el-radio :label="'${opt.value}'">${opt.label}</el-radio>\n`
        })
        template += `      </el-radio-group>\n`
        break
      case 'checkbox':
        template += `      <el-checkbox-group v-model="formData.${field.props.prop}">\n`
        field.props.options.forEach(opt => {
          template += `        <el-checkbox :label="'${opt.value}'">${opt.label}</el-checkbox>\n`
        })
        template += `      </el-checkbox-group>\n`
        break
      case 'date':
        template += `      <el-date-picker v-model="formData.${field.props.prop}" type="date" placeholder="${field.props.placeholder}" />\n`
        break
      case 'number':
        template += `      <el-input-number v-model="formData.${field.props.prop}" :min="${field.props.min}" :max="${field.props.max}" />\n`
        break
      case 'switch':
        template += `      <el-switch v-model="formData.${field.props.prop}" />\n`
        break
    }

    template += `    </el-form-item>\n`
  })

  template += `    <el-form-item>\n      <el-button type="primary" @click="submitForm">提交</el-button>\n      <el-button @click="resetForm">重置</el-button>\n    </el-form-item>\n`
  template += `  </el-form>\n</template>\n`

  generatedTemplate.value = template

  // 生成脚本代码
  let script = `<script setup>\nimport { ref, reactive } from 'vue'\nimport { ElMessage } from 'element-plus'\n\n`
  script += `const formRef = ref()\n\n`
  script += `const formData = reactive({\n`

  formConfig.fields.forEach((field, index) => {
    const isLast = index === formConfig.fields.length - 1
    script += `  ${field.props.prop}: ''${isLast ? '' : ','}\n`
  })

  script += `})\n\n`
  script += `const formRules = reactive({\n`

  formConfig.fields.forEach((field, index) => {
    const isLast = index === formConfig.fields.length - 1
    if (field.props.required) {
      script += `  ${field.props.prop}: [\n`
      script += `    { required: true, message: '${field.props.requiredMessage || field.props.label + '不能为空'}', trigger: 'blur' }\n`
      script += `  ]${isLast ? '' : ','}\n`
    } else {
      script += `  ${field.props.prop}: []${isLast ? '' : ','}\n`
    }
  })

  script += `})\n\n`
  script += `const submitForm = () => {\n  formRef.value.validate((valid) => {\n`
  script += `    if (valid) {\n      ElMessage.success('提交成功!')\n      console.log('表单数据:', formData)\n    }\n  })\n}\n\n`
  script += `const resetForm = () => {\n  formRef.value.resetFields()\n}\n`
  script += `<\/script>`

  generatedScript.value = script

  codeVisible.value = true
}

// 复制代码
const copyCode = () => {
  const code = activeCodeTab.value === 'template'
    ? generatedTemplate.value
    : generatedScript.value

  navigator.clipboard.writeText(code).then(() => {
    ElMessage.success('代码已复制到剪贴板')
  })
}

// 在 script setup 顶部添加
onMounted(() => {
  // 检查浏览器是否支持拖拽API
  const supportsDragDrop = 'draggable' in document.createElement('div') &&
                          'dataTransfer' in new DragEvent('dragstart');

  if (!supportsDragDrop) {
    ElMessage.warning('当前浏览器可能不完全支持拖拽功能，建议使用Chrome或Firefox');
  }
  
  // 检测是否为Firefox浏览器
  isFirefox.value = navigator.userAgent.includes('Firefox')
});

</script>

<style scoped>


.component-item[draggable="true"] {
  cursor: grab;
}
/* 拖拽时的视觉效果 */
.component-item[draggable="true"]:active {
  cursor: grabbing;
  opacity: 0.7;
}

/* 拖拽中的元素样式 */
.component-item[draggable="true"][dragging="true"] {
  opacity: 0.5;
  background-color: #e6f7ff;
}

/* 拖拽占位符样式 */
.dragging-fallback {
  background-color: #e6f7ff;
  border: 2px dashed #409eff;
  border-radius: 4px;
  padding: 10px;
  margin-bottom: 10px;
}
.form-builder {
  height: 100%;
  background-color: #f0f2f5;
}

.builder-container {
  display: flex;
  height: 100%;
  padding: 20px;
  gap: 20px;
}

.components-panel {
  width: 250px;
  background: white;
  border-radius: 4px;
  padding: 15px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  user-select: none;
}

.components-panel h3 {
  margin-top: 0;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.component-item {
  padding: 10px 15px;
  margin-bottom: 10px;
  background: #f5f7fa;
  border-radius: 4px;
  cursor: grab;
  user-select: none;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s;
  user-select: none;
  touch-action: none;
}

.component-item:hover {
  background: #ecf5ff;
  transform: translateX(5px);
}

.design-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
}

.panel-header h3 {
  margin: 0;
}

.form-canvas {
  flex: 1;
  padding: 20px;
  min-height: 400px;
  position: relative;
  background-image: linear-gradient(90deg, rgba(190, 190, 190, 0.1) 3%, transparent 0),
                    linear-gradient(rgba(190, 190, 190, 0.1) 3%, transparent 0);
  background-size: 20px 20px;
  background-position: 50%;
  user-select: none;
  touch-action: none;
}

.form-canvas.drag-over {
  background-color: #ecf5ff;
  border: 2px dashed #409eff;
  background-color: rgba(64, 158, 255, 0.1);
}

.empty-tip {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #909399;
  font-size: 16px;
}

.form-field {
  display: flex;
  align-items: center;
  padding: 10px 15px;
  margin-bottom: 10px;
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.form-field:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.form-field.active {
  border-color: #409eff;
  box-shadow: 0 0 0 2px #c6e2ff;
  background-color: #ecf5ff;
}

.field-handle {
  cursor: move;
  padding: 0 10px;
  color: #909399;
}

.field-content {
  flex: 1;
  padding: 0 15px;
}

.field-actions {
  display: flex;
  gap: 10px;
  color: #909399;
}

.field-actions i {
  cursor: pointer;
  padding: 5px;
}

.field-actions i:hover {
  color: #409eff;
}

.config-panel {
  width: 300px;
  background: white;
  border-radius: 4px;
  padding: 15px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.config-panel h3 {
  margin-top: 0;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.config-content .tip {
  text-align: center;
  color: #909399;
  margin-top: 50px;
}

.code-block {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  max-height: 500px;
  overflow: auto;
  white-space: pre-wrap;
  font-family: monospace;
  font-size: 14px;
}

.dialog-footer {
  text-align: right;
}
</style>

