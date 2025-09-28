<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'
import { ElIcon } from 'element-plus'
import { Loading } from '@element-plus/icons-vue'

const props = defineProps({
  options: {
    type: Object,
    required: true,
  },
  style: {
    type: Object,
    default: () => ({ width: '100%', height: '400px' }),
  },
  // 允许外部控制是否显示loading
  showLoading: {
    type: Boolean,
    default: true
  }
})

const chartRef = ref<HTMLElement | null>(null)
let chartInstance: ECharts | null = null

const initChart = () => {
  if (chartRef.value) {
    chartInstance = echarts.init(chartRef.value)
    
    // 根据配置决定是否显示loading效果
    if (props.showLoading) {
      chartInstance.showLoading({
        text: '加载中...',
        color: '#409eff',
        textColor: '#000',
        maskColor: 'rgba(255, 255, 255, 0.8)',
        zlevel: 0,
      })
    }

    // 设置图表配置
    chartInstance.setOption(props.options)

    // 隐藏loading
    if (props.showLoading) {
      chartInstance.hideLoading()
    }

    // 添加图表点击事件
    chartInstance.on('click', (params) => {
      console.log('图表点击事件:', params)
    })
  }
}

// 监听options变化，更新图表
watch(() => props.options, (newOptions) => {
  if (chartInstance && newOptions) {
    // 根据配置决定是否显示loading效果
    if (props.showLoading) {
      chartInstance.showLoading()
    }
    
    // 使用true参数强制更新所有配置
    chartInstance.setOption(newOptions, true)
    
    // 隐藏loading
    if (props.showLoading) {
      chartInstance.hideLoading()
    }
  }
}, { deep: true })

const resizeChart = () => {
  if (chartInstance) {
    // 添加防抖处理
    clearTimeout(resizeTimer)
    resizeTimer = setTimeout(() => {
      chartInstance?.resize()
    }, 200)
  }
}

let resizeTimer: number

onMounted(() => {
  initChart()
  window.addEventListener('resize', resizeChart)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeChart)
  chartInstance?.dispose()
})

defineExpose({
  resize: resizeChart,
})
</script>

<template>
  <div class="chart-container">
    <div ref="chartRef" :style="style"></div>
    <div v-if="!chartInstance" class="chart-loading">
      <el-icon class="is-loading"><Loading /></el-icon>
      实时更新中
    </div>
  </div>
</template>

<style scoped>
.chart-container {
  position: relative;
  width: 100%;
  height: 100%;
  min-height: 300px;
}

.chart-loading {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  color: var(--el-color-primary);
  font-size: 14px;
}

.chart-loading .el-icon {
  margin-bottom: 8px;
  font-size: 24px;
  width: 24px;
  height: 24px;
  animation: rotating 2s linear infinite;
  display: flex;
  justify-content: center;
  align-items: center;
}

@keyframes rotating {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
