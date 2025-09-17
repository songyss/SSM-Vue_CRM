<template>
  <div class="marketing-campaigns">
    <!-- 新建活动按钮 -->
    <button v-if="!showEditor" class="create-btn" @click="showEditor = true">
      <i class="icon-plus"></i> 新建营销活动
    </button>

    <!-- 富文本编辑器区域 - 点击按钮后显示 -->
    <div v-if="showEditor" class="editor-container">
      <!-- 编辑器标题栏 -->
      <div class="editor-header">
        <h2>营销活动内容编辑</h2>
        <button class="close-btn" @click="showEditor = false">
          <i class="icon-close"></i> 关闭
        </button>
      </div>

      <!-- 活动标题输入 -->
      <div class="campaign-title">
        <label for="campaignTitle">活动标题：</label>
        <input
          type="text"
          id="campaignTitle"
          v-model="campaignTitle"
          placeholder="请输入营销活动标题"
        />
      </div>

      <!-- 富文本编辑器 -->
      <div class="editor-wrapper">
        <Editor
          v-model="content"
          :default-config="editorConfig"
          mode="default"
          @on-change="handleContentChange"
          @on-created="handleEditorCreated"
          @on-destroyed="handleEditorDestroyed"
          style="height: 400px; border: 1px solid #e5e7eb"
        />
      </div>

      <!-- 编辑器操作按钮 -->
      <div class="editor-actions">
        <button @click="getContent" class="action-btn info-btn">
          <i class="icon-view"></i> 查看内容
        </button>
        <button @click="clearContent" class="action-btn warning-btn">
          <i class="icon-clear"></i> 清空内容
        </button>
        <button @click="submitContent" class="action-btn primary-btn">
          <i class="icon-save"></i> 保存活动内容
        </button>
      </div>

      <!-- 内容预览区域 -->
      <div class="content-preview">
        <h3><i class="icon-preview"></i> 内容预览：</h3>
        <div class="preview-content" v-html="content"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, shallowRef, onUnmounted } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'

// 控制编辑器显示/隐藏
const showEditor = ref(false)
// 活动标题
const campaignTitle = ref('')
// 编辑器内容
const content = ref('<p>请输入营销活动内容...</p>')
// 编辑器实例
const editorRef = shallowRef(null)

// 编辑器配置
const editorConfig = {
  placeholder: '请输入营销活动详情...',
  // 工具栏配置
  toolbarKeys: [
    'headerSelect',
    '|',
    'bold',
    'italic',
    'underline',
    'through',
    '|',
    'fontSize',
    'fontFamily',
    'textColor',
    'bgColor',
    '|',
    'bulletedList',
    'numberedList',
    'todo',
    '|',
    'insertLink',
    'insertImage',
    'insertVideo',
    '|',
    'codeBlock',
    'divider',
    '|',
    'justifyLeft',
    'justifyCenter',
    'justifyRight',
    'justifyJustify',
    '|',
    'undo',
    'redo',
  ],
  // 上传图片配置
  MENU_CONF: {
    uploadImage: {
      // 实际项目中替换为你的后端上传接口
      server: '/api/upload/image',
      fieldName: 'file',
      maxFileSize: 2 * 1024 * 1024, // 2MB
      allowedFileTypes: ['image/jpeg', 'image/png', 'image/gif', 'image/webp'],

      // 上传前钩子
      beforeUpload(file) {
        console.log('准备上传图片:', file)
        return true // 允许上传
      },

      // 上传成功回调
      onSuccess(file, res) {
        console.log('图片上传成功:', res)
        // 假设后端返回格式为 { url: 'xxx' }
        return res.url
      },

      // 上传失败回调
      onFailed(file, res) {
        console.error('图片上传失败:', res)
        alert('图片上传失败，请重试')
      },

      // 上传超时回调
      onTimeout(file) {
        console.error('图片上传超时:', file)
        alert('图片上传超时，请重试')
      },
    },

    // 配置链接插入
    insertLink: {
      // 自定义验证链接
      validate(url) {
        if (!url.startsWith('http://') && !url.startsWith('https://')) {
          return '链接必须以 http:// 或 https:// 开头'
        }
        return true
      },
    },
  },
}

// 编辑器创建时回调
const handleEditorCreated = (editor) => {
  editorRef.value = editor // 记录编辑器实例
  console.log('编辑器创建成功')
}

// 编辑器销毁时回调
const handleEditorDestroyed = () => {
  console.log('编辑器已销毁')
}

// 内容变化时回调
const handleContentChange = (newContent) => {
  content.value = newContent
  console.log('内容已更新')
}

// 获取编辑器内容
const getContent = () => {
  const editor = editorRef.value
  if (editor) {
    const html = editor.getHtml()
    const text = editor.getText()
    console.log('HTML内容:', html)
    console.log('纯文本内容:', text)
    alert('内容已输出到控制台')
  }
}

// 清空编辑器内容
const clearContent = () => {
  const editor = editorRef.value
  if (editor) {
    if (confirm('确定要清空所有内容吗？')) {
      editor.clear()
      content.value = ''
    }
  }
}

// 提交内容
const submitContent = () => {
  // 验证标题和内容
  if (!campaignTitle.value.trim()) {
    alert('请输入活动标题')
    return
  }

  const editor = editorRef.value
  if (editor) {
    const htmlContent = editor.getHtml()
    const textContent = editor.getText()

    // 简单验证内容是否为空
    if (!textContent.trim()) {
      alert('请输入活动内容')
      return
    }

    // 构建提交的数据
    const campaignData = {
      title: campaignTitle.value,
      content: htmlContent,
      textContent: textContent,
      createdAt: new Date().toISOString(),
    }

    // 实际项目中这里会调用API提交数据
    console.log('提交的营销活动数据:', campaignData)

    // 模拟API请求成功
    setTimeout(() => {
      alert('营销活动内容保存成功！')
      // 可以在这里重置表单或关闭编辑器
      // showEditor.value = false
    }, 500)
  }
}

// 组件卸载时销毁编辑器
onUnmounted(() => {
  const editor = editorRef.value
  if (editor) {
    editor.destroy()
  }
})
</script>

<style scoped>
.marketing-campaigns {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

/* 新建活动按钮 */
.create-btn {
  padding: 10px 20px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.create-btn:hover {
  background-color: #66b1ff;
  transform: translateY(-2px);
}

/* 编辑器容器 */
.editor-container {
  margin-top: 20px;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  animation: fadeIn 0.3s ease;
}

/* 编辑器标题栏 */
.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e5e7eb;
}

.editor-header h2 {
  margin: 0;
  color: #333;
  font-size: 1.5rem;
}

.close-btn {
  padding: 6px 12px;
  background-color: #f5f7fa;
  color: #606266;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.2s;
}

.close-btn:hover {
  background-color: #e9ecef;
}

/* 活动标题样式 */
.campaign-title {
  margin-bottom: 20px;
}

.campaign-title label {
  display: block;
  margin-bottom: 8px;
  color: #606266;
  font-weight: 500;
}

.campaign-title input {
  width: 100%;
  padding: 10px 15px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.2s;
}

.campaign-title input:focus {
  outline: none;
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

/* 编辑器操作按钮 */
.editor-actions {
  margin: 20px 0;
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.action-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  transition: all 0.2s;
}

.action-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.primary-btn {
  background-color: #409eff;
  color: white;
}

.info-btn {
  background-color: #909399;
  color: white;
}

.warning-btn {
  background-color: #e6a23c;
  color: white;
}

/* 内容预览区域 */
.content-preview {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #e5e7eb;
}

.content-preview h3 {
  margin: 0 0 15px 0;
  color: #606266;
  display: flex;
  align-items: center;
  gap: 8px;
}

.preview-content {
  padding: 15px;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
  min-height: 100px;
  line-height: 1.6;
}

/* 图标样式 */
.icon-plus::before {
  content: '+';
  font-weight: bold;
}

.icon-close::before {
  content: '×';
}

.icon-view::before {
  content: '👁️';
}

.icon-clear::before {
  content: '🗑️';
}

.icon-save::before {
  content: '💾';
}

.icon-preview::before {
  content: '👀';
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .editor-actions {
    flex-direction: column;
  }

  .action-btn {
    width: 100%;
    justify-content: center;
  }
}
</style>
