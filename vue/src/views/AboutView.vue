<template>
  <div class="home">
    <!-- 图表容器 -->
    <div ref="chartRef" style="width: 600px; height: 400px"></div>
  </div>
  <h2>富文本编辑器</h2>
  <div class="editor-container">
    <quill-editor
      ref="quillEditorRef"
      v-model:content="content"
      :options="editorOptions"
      style="height: 300px"
    />
    <div>
      <button @click="getContent">获取内容</button>
      <button @click="clearContent">清空内容</button>
    </div>
    <div class="content-preview">
      <h3>内容预览：</h3>
      <div v-html="content"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, shallowRef } from 'vue'
//导入富文本编辑器
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'

import * as echarts from 'echarts'

// 获取 DOM 元素引用
const chartRef = ref<HTMLElement | null>(null)
const quillEditorRef = shallowRef()

// 富文本编辑器内容
const content = ref('<p>请输入内容。。。</p>')

// 富文本编辑器配置
const editorOptions = {
  theme: 'snow',
  placeholder: '请输入内容...',
  modules: {
    toolbar: [
      ['bold', 'italic', 'underline', 'strike'],
      ['blockquote', 'code-block'],
      [{ header: 1 }, { header: 2 }],
      [{ list: 'ordered' }, { list: 'bullet' }],
      [{ script: 'sub' }, { script: 'super' }],
      [{ indent: '-1' }, { indent: '+1' }],
      [{ direction: 'rtl' }],
      [{ size: ['small', false, 'large', 'huge'] }],
      [{ header: [1, 2, 3, 4, 5, 6, false] }],
      [{ color: [] }, { background: [] }],
      [{ font: [] }],
      [{ align: [] }],
      ['clean'],
      ['link', 'image', 'video'],
    ],
  },
}

const getContent = () => {
  console.log('编辑内容：', content.value)
  alert('内容已输出到控制台')
}

const clearContent = () => {
  content.value = '<p><br></p>'
}
let myChart: echarts.ECharts | null = null

// 初始化图表
const initChart = () => {
  if (chartRef.value) {
    // 初始化 ECharts 实例
    myChart = echarts.init(chartRef.value)

    // 配置图表选项
    const option = {
      title: {
        text: 'ECharts 入门示例',
      },
      tooltip: {},
      legend: {
        data: ['销量'],
      },
      xAxis: {
        data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子'],
      },
      yAxis: {},
      series: [
        {
          name: '销量',
          type: 'bar',
          data: [5, 20, 36, 10, 10, 20],
        },
      ],
    }

    // 设置图表配置
    myChart.setOption(option)
  }
}

// 组件挂载后初始化图表
onMounted(() => {
  initChart()

  // 监听窗口大小变化，自适应图表
  window.addEventListener('resize', () => {
    myChart?.resize()
  })
})
</script>
