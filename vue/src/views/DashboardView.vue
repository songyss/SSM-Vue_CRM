<script setup lang="ts">
import { ref, onMounted } from 'vue'
import DataChart from '@/components/DataChart.vue'
import { ElCard, ElRow, ElCol, ElButton, ElMessage } from 'element-plus'
import axios from 'axios'
import request from '@/utils/request'
// 声热力图相关变量
let map: any = null
let heatmap: any = null

const chartOptions = ref({
  title: {
    text: '销售数据分析',
  },
  tooltip: {
    trigger: 'axis',
  },
  legend: {
    data: ['销售额', '订单量', '新增客户'],
  },
  xAxis: {
    type: 'category',
    data: [],
  },
  yAxis: [
    {
      type: 'value',
      name: '销售额',
      axisLabel: {
        formatter: '{value} 万',
      },
    },
    {
      type: 'value',
      name: '数量',
      axisLabel: {
        formatter: '{value} 个',
      },
    },
  ],
  series: [
    {
      name: '销售额',
      type: 'bar',
      data: [],
    },
    {
      name: '订单量',
      type: 'line',
      yAxisIndex: 1,
      data: [],
    },
    {
      name: '新增客户',
      type: 'line',
      yAxisIndex: 1,
      data: [],
    },
  ],
})

const pieOptions = ref({
  title: {
    text: '客户来源分布',
    left: 'center',
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} ({d}%)',
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    data: ['搜索引擎', '直接访问', '邮件营销', '联盟广告', '视频广告'],
  },
  series: [
    {
      name: '客户来源',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2,
      },
      label: {
        show: true,
        formatter: '{b}: {d}%',
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '18',
          fontWeight: 'bold',
        },
      },
      labelLine: {
        show: true,
      },
      data: [],
    },
  ],
})

// 初始化热力图
const initHeatmap = () => {
  // 动态加载高德地图API
  const script = document.createElement('script')
  script.src = 'https://webapi.amap.com/maps?v=2.0&key=a312ffd15548acf115a77f01adb0cb17'
  script.onload = () => {
    // 初始化地图 - 天津市中心坐标
    map = new (window as any).AMap.Map('heatmap-container', {
      resizeEnable: true,
      center: [117.190182, 39.125596], // 天津市中心坐标
      zoom: 11 // 调整缩放级别以显示整个天津市
    })

    // 加载热力图插件
    map.plugin(['AMap.HeatMap'], () => {
      // 初始化热力图对象
      heatmap = new (window as any).AMap.HeatMap(map, {
        radius: 30, // 增加半径以适应更大的地图范围
        opacity: [0.2, 0.8],
      })

      // 天津市主要区域热力图数据
      const tianjinHeatData = {
        data: [
          // 市中心区域
          {lng: 117.186462, lat: 39.133484, count: 100},  // 天津站/意大利风情区
          {lng: 117.200263, lat: 39.117501, count: 95},   // 南市食品街/古文化街
          {lng: 117.195908, lat: 39.105553, count: 90},   // 天津大学/南开大学
          {lng: 117.217614, lat: 39.109594, count: 85},   // 和平区商业中心

          // 滨海新区
          {lng: 117.715, lat: 39.015, count: 88},  // 滨海万达广场
          {lng: 117.720, lat: 39.025, count: 85},  // 泰达MSD
          {lng: 117.700, lat: 39.010, count: 82},  // 滨海文化中心
          {lng: 117.690, lat: 39.008, count: 80},  // 于家堡高铁站
          {lng: 117.735, lat: 39.030, count: 78},  // 海昌海洋公园
          {lng: 117.705, lat: 39.012, count: 75},  // 滨海图书馆
          {lng: 117.658, lat: 39.035, count: 72},  // 金街商业区

          // 其他行政区
          {lng: 117.007647, lat: 39.135596, count: 70},   // 西青区大学城
          {lng: 117.350134, lat: 38.954406, count: 65},   // 津南区
          {lng: 117.475854, lat: 39.085136, count: 60},   // 东丽区
          {lng: 117.134852, lat: 39.365421, count: 55},   // 北辰区
          {lng: 117.049394, lat: 39.376842, count: 50},   // 武清区
          {lng: 117.775231, lat: 39.219643, count: 45},   // 宁河区
          {lng: 117.819873, lat: 38.899282, count: 40},   // 静海区
          {lng: 117.382549, lat: 38.935958, count: 35},   // 津南区海河教育园

          // 主要交通枢纽
          {lng: 117.218511, lat: 39.142124, count: 92},   // 天津西站
          {lng: 117.366613, lat: 39.000244, count: 88},   // 天津南站
          {lng: 117.698208, lat: 39.054582, count: 85},   // 滨海国际机场

          // 主要商业区
          {lng: 117.221566, lat: 39.095958, count: 90},   // 南开区商业中心
          {lng: 117.176153, lat: 39.120123, count: 88},   // 河东区万达广场
          {lng: 117.245231, lat: 39.087452, count: 82},   // 河西区商业中心
          {lng: 117.195261, lat: 39.159623, count: 80},   // 河北区商业中心

          // 主要景点
          {lng: 117.186286, lat: 39.159521, count: 75},   // 天津之眼
          {lng: 117.175692, lat: 39.085106, count: 70},   // 水上公园
          {lng: 117.201234, lat: 39.098765, count: 68},   // 天塔湖
          {lng: 117.186543, lat: 39.123456, count: 65},   // 盘山风景区
        ],
        max: 100 // 最大值为100人对应黄色
      }

      // 设置数据集
      heatmap.setDataSet(tianjinHeatData)
    })
  }
  document.head.appendChild(script)
}

// 显示热力图
const showHeatmap = () => {
  if (heatmap) {
    heatmap.show()
  }
}

// 隐藏热力图
const hideHeatmap = () => {
  if (heatmap) {
    heatmap.hide()
  }
}
// 商机转化率漏斗图
const opportunityFunnelOptions = ref({
  title: {
    text: '商机转化率漏斗',
    left: 'center',
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} ({d}%)',
  },
  legend: {
    data: ['线索', '商机', '提案', '谈判', '成交'],
    bottom: 10,
  },
  series: [
    {
      name: '转化率',
      type: 'funnel',
      left: '10%',
      top: 60,
      bottom: 60,
      width: '80%',
      min: 0,
      max: 100,
      minSize: '0%',
      maxSize: '100%',
      sort: 'descending',
      gap: 2,
      label: {
        show: true,
        position: 'inside',
      },
      labelLine: {
        length: 10,
        lineStyle: {
          width: 1,
          type: 'solid',
        },
      },
      itemStyle: {
        borderColor: '#fff',
        borderWidth: 1,
      },
      emphasis: {
        label: {
          fontSize: 20,
        },
      },
      data: [
        { value: 100, name: '线索' },
        { value: 60, name: '商机' },
        { value: 35, name: '提案' },
        { value: 20, name: '谈判' },
        { value: 10, name: '成交' },
      ],
    },
  ],
})

// 客户类型分布图
const customerTypeOptions = ref({
  title: {
    text: '客户类型分布',
    left: 'center',
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} ({d}%)',
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    data: ['新客户', '老客户', '潜在客户', '重要客户', '流失客户'],
  },
  series: [
    {
      name: '客户类型',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2,
      },
      label: {
        show: true,
        formatter: '{b}: {d}%',
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '18',
          fontWeight: 'bold',
        },
      },
      labelLine: {
        show: true,
      },
      data: [
        
      ],
    },
  ],
})

// 销售业绩排行
const salesRankOptions = ref({
  title: {
    text: '销售业绩排行',
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow',
    },
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true,
  },
  xAxis: {
    type: 'value',
    axisLabel: {
      formatter: '{value} 万',
    },
  },
  yAxis: {
    type: 'category',
    data: [],
  },
  series: [
    {
      name: '销售额',
      type: 'bar',
      data: [],
      label: {
        show: true,
        position: 'right',
        formatter: '{c} 万',
      },
    },
  ],
})
console.log("salesRankOptions", salesRankOptions.value)

// 客户留存率分析
const retentionOptions = ref({
  title: {
    text: '客户留存率分析',
  },
  tooltip: {
    trigger: 'axis',
  },
  legend: {
    data: ['7日留存率', '30日留存率', '90日留存率'],
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true,
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: [],
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      formatter: '{value}%',
    },
  },
  series: [
    {
      name: '7日留存率',
      type: 'line',
      data: [],
      smooth: true,
    },
    {
      name: '30日留存率',
      type: 'line',
      data: [],
      smooth: true,
    },
    {
      name: '90日留存率',
      type: 'line',
      data: [],
      smooth: true,
    },
  ],
})

// 客户生命周期价值分析
const clvOptions = ref({
  title: {
    text: '客户生命周期价值分析',
  },
  tooltip: {
    trigger: 'axis',
  },
  legend: {
    data: ['新客户', '活跃客户', '忠诚客户'],
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true,
  },
  xAxis: {
    type: 'category',
    data: [],
  },
  yAxis: {
    type: 'value',
    name: '平均客单价(元)',
  },
  series: [
    {
      name: '新客户',
      type: 'line',
      data: [],
      smooth: true,
    },
    {
      name: '活跃客户',
      type: 'line',
      data: [],
      smooth: true,
    },
    {
      name: '忠诚客户',
      type: 'line',
      data: [],
      smooth: true,
    },
  ],
})

// 销售预测分析
const salesForecastOptions = ref({
  title: {
    text: '销售预测分析',
  },
  tooltip: {
    trigger: 'axis',
  },
  legend: {
    data: ['实际销售额', '预测销售额'],
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true,
  },
  xAxis: {
    type: 'category',
    data: [],
  },
  yAxis: {
    type: 'value',
    name: '销售额(万元)',
  },
  series: [
    {
      name: '实际销售额',
      type: 'line',
      data: [],
      markLine: {
        data: [{
          xAxis: '6月',
          lineStyle: {
            color: '#f56c6c',
            type: 'dashed'
          },
          label: {
            formatter: '预测分界线'
          }
        }]
      }
    },
    {
      name: '预测销售额',
      type: 'line',
      data: [],
      lineStyle: {
        type: 'dashed'
      },
      areaStyle: {
        opacity: 0.3
      }
    },
  ],
})

// 产品销售分布热力图
const productHeatmapOptions = ref({
  title: {
    text: '产品销售热力分布',
  },
  tooltip: {
    position: 'top',
  },
  grid: {
    height: '50%',
    top: '10%',
  },
  xAxis: {
    type: 'category',
    data: ['华东', '华北', '华南', '西南', '西北', '东北'],
    splitArea: {
      show: true,
    },
  },
  yAxis: {
    type: 'category',
    data: ['产品A', '产品B', '产品C', '产品D', '产品E'],
    splitArea: {
      show: true,
    },
  },
  visualMap: {
    min: 0,
    max: 10000,
    calculable: true,
    orient: 'horizontal',
    left: 'center',
    bottom: '15%',
  },
  series: [
    {
      name: '销售额',
      type: 'heatmap',
      data: [
        [0, 0, 8500],
        [0, 1, 6200],
        [0, 2, 9800],
        [0, 3, 5200],
        [0, 4, 7800],
        [1, 0, 6500],
        [1, 1, 7800],
        [1, 2, 5200],
        [1, 3, 4800],
        [1, 4, 6300],
        [2, 0, 7200],
        [2, 1, 8500],
        [2, 2, 6800],
        [2, 3, 5400],
        [2, 4, 7100],
        [3, 0, 5200],
        [3, 1, 6500],
        [3, 2, 4800],
        [3, 3, 7200],
        [3, 4, 5600],
        [4, 0, 4800],
        [4, 1, 5900],
        [4, 2, 4200],
        [4, 3, 6100],
        [4, 4, 4500],
        [5, 0, 5600],
        [5, 1, 6800],
        [5, 2, 5100],
        [5, 3, 5900],
        [5, 4, 6200],
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowColor: 'rgba(0, 0, 0, 0.5)',
        },
      },
    },
  ],
})

const userInfo = ref<{ role: number } | null>(null)

// 仪表盘基本统计数据
const dashboardStats = ref({
  totalSales: 0,
  totalSalesGrowth: 0,
  totalOrders: 0,
  totalOrdersGrowth: 0,
  newCustomers: 0,
  newCustomersGrowth: 0,
  conversionRate: 0,
  conversionRateGrowth: 0
})

// 实时销售数据
const realTimeData = ref({
  currentSales: 0,
  ordersToday: 0,
  customersToday: 0,
  conversionRate: 0
})

// 模拟实时数据更新
const simulateRealTimeData = () => {
  setInterval(() => {
    realTimeData.value.currentSales = (Math.random() * 5000 + 1280000).toFixed(0)
    realTimeData.value.ordersToday = Math.floor(Math.random() * 5 + 1024)
    realTimeData.value.customersToday = Math.floor(Math.random() * 3 + 256)
    realTimeData.value.conversionRate = (Math.random() * 2 + 32).toFixed(1)
  }, 5000)
}

// 获取完整的仪表盘数据
const fetchDashboardData = async () => {
  try {
    const response = await request.get('/dashboard/complete-data')
    console.log("获取完整的仪表盘数据", response.data)
    if (response.data.code === 200 && response.data.data) {
      const data = response.data.data
      console.log("获取完整的仪表盘数据", data)
      // 更新仪表盘基本统计数据
      if (data.basicStats) {
        dashboardStats.value.totalSales = data.basicStats.totalSales || 0
        dashboardStats.value.totalSalesGrowth = data.basicStats.totalSalesGrowth || 0
        dashboardStats.value.totalOrders = data.basicStats.totalOrders || 0
        dashboardStats.value.totalOrdersGrowth = data.basicStats.totalOrdersGrowth || 0
        dashboardStats.value.newCustomers = data.basicStats.newCustomers || 0
        dashboardStats.value.newCustomersGrowth = data.basicStats.newCustomersGrowth || 0
        dashboardStats.value.conversionRate = data.basicStats.conversionRate || 0
        dashboardStats.value.conversionRateGrowth = data.basicStats.conversionRateGrowth || 0
      }
      
      // 更新销售趋势分析图表数据
      if (data.basicStats && data.basicStats.salesData) {
        const months = data.basicStats.salesData.map((item: any) => item.month)
        const sales = data.basicStats.salesData.map((item: any) => item.sales / 10000) // 转换为万元
        const orders = data.basicStats.salesData.map((item: any) => item.orders)
        
        chartOptions.value.xAxis.data = months
        chartOptions.value.series[0].data = sales
        chartOptions.value.series[1].data = orders
        // 假设新增客户数据和订单数据相似
        chartOptions.value.series[2].data = orders.map((num: number) => Math.floor(num * 1.5))
      }
      
      // 更新客户来源分布图表数据
      if (data.basicStats && data.basicStats.customerSources) {
        const customerSourceData = data.basicStats.customerSources.map((item: any) => ({
          name: item.name,
          value: item.value
        }))
        const customerSourceNames = data.basicStats.customerSources.map((item: any) => item.name)
        
        pieOptions.value.legend.data = customerSourceNames
        pieOptions.value.series[0].data = customerSourceData
      }
      
      // 更新商机转化率漏斗图数据
      if (data.opportunityConversions) {
        // 定义商机阶段映射
        const stageMap: Record<string, string> = {
          '0': '线索',
          '1': '初步接触',
          '2': '需求确认',
          '3': '方案制定',
          '4': '商务谈判',
          '5': '成交'
        }
        
        const opportunityData = data.opportunityConversions.map((item: any) => ({
          name: stageMap[item.stage] || item.stage,
          value: item.value
        }))
        const opportunityNames = data.opportunityConversions.map((item: any) => stageMap[item.stage] || item.stage)
        
        opportunityFunnelOptions.value.legend.data = opportunityNames
        opportunityFunnelOptions.value.series[0].data = opportunityData
      }
      
      // 更新客户类型分布图表数据
      if (data.customerTypes) {
        // 定义客户类型映射
        const typeMap: Record<string, string> = {
          '0': '潜在客户',
          '1': '新客户',
          '2': '活跃客户',
          '3': '重要客户',
          '4': '流失客户'
        }
        
        const customerTypeData = data.customerTypes.map((item: any) => ({
          name: typeMap[item.type] || item.type,
          value: item.count
        }))
        const customerTypeNames = data.customerTypes.map((item: any) => typeMap[item.type] || item.type)
        
        customerTypeOptions.value.legend.data = customerTypeNames
        customerTypeOptions.value.series[0].data = customerTypeData
      }
      
      // 更新销售业绩排行图表数据
      console.log("销售业绩排行图表数据", data.salesRanks)
      if (data.salesRanks) {
        const salesPersonData = data.salesRanks.map((item: any) => ({
          name: item.salesperson,
          value: item.salesAmount / 10000 // 转换为万元
        }))
        
        // 按照销售额排序
        salesPersonData.sort((a: any, b: any) => b.value - a.value)
        
        const salesPersonNames = salesPersonData.map((item: any) => item.name)
        const salesAmounts = salesPersonData.map((item: any) => item.value)
        
        salesRankOptions.value.yAxis.data = salesPersonNames
        salesRankOptions.value.series[0].data = salesAmounts
      }
      
      // 更新客户留存率分析图表数据
      if (data.retentionData) {
        const months = data.retentionData.map((item: any) => item.month)
        const sevenDayRetention = data.retentionData.map((item: any) => item.sevenDayRetention)
        const thirtyDayRetention = data.retentionData.map((item: any) => item.thirtyDayRetention)
        const ninetyDayRetention = data.retentionData.map((item: any) => item.ninetyDayRetention)
        
        retentionOptions.value.xAxis.data = months
        retentionOptions.value.series[0].data = sevenDayRetention
        retentionOptions.value.series[1].data = thirtyDayRetention
        retentionOptions.value.series[2].data = ninetyDayRetention
      }
      
      // 更新客户生命周期价值分析图表数据
      if (data.customerLifetimeValues) {
        const months = data.customerLifetimeValues.map((item: any) => item.month)
        const newCustomerValue = data.customerLifetimeValues.map((item: any) => item.newCustomerValue)
        const activeCustomerValue = data.customerLifetimeValues.map((item: any) => item.activeCustomerValue)
        const loyalCustomerValue = data.customerLifetimeValues.map((item: any) => item.loyalCustomerValue)
        
        clvOptions.value.xAxis.data = months
        clvOptions.value.series[0].data = newCustomerValue
        clvOptions.value.series[1].data = activeCustomerValue
        clvOptions.value.series[2].data = loyalCustomerValue
      }
      
      // 更新销售预测分析图表数据
      if (data.forecastData) {
        const months = data.forecastData.map((item: any) => item.month)
        const forecastSales = data.forecastData.map((item: any) => item.sales / 10000) // 转换为万元
        
        // 分离实际数据和预测数据（根据数据中的月份，假设当前月及之前的是实际数据）
        const currentMonth = new Date().getMonth() + 1
        const actualSales = forecastSales.map((value: any, index: number) => {
          // 从月份字符串中提取月份数字
          const monthNum = parseInt(months[index].replace('月', ''))
          return monthNum <= currentMonth ? value : null
        })
        const predictedSales = forecastSales.map((value: any, index: number) => {
          // 从月份字符串中提取月份数字
          const monthNum = parseInt(months[index].replace('月', ''))
          return monthNum > currentMonth ? value : null
        })
        
        salesForecastOptions.value.xAxis.data = months
        salesForecastOptions.value.series[0].data = actualSales
        salesForecastOptions.value.series[1].data = predictedSales
      }
      
      // 更新产品销售热力分布图表数据
      if (data.productSalesHeatmaps) {
        // 处理热力图数据
        const regions = [...new Set(data.productSalesHeatmaps.map((item: any) => item.region))]
        const products = [...new Set(data.productSalesHeatmaps.map((item: any) => item.product))]
        
        // 创建数据矩阵
        const heatmapData: any[] = []
        data.productSalesHeatmaps.forEach((item: any) => {
          const regionIndex = regions.indexOf(item.region)
          const productIndex = products.indexOf(item.product)
          heatmapData.push([regionIndex, productIndex, item.salesAmount])
        })
        
        productHeatmapOptions.value.xAxis.data = regions
        productHeatmapOptions.value.yAxis.data = products
        productHeatmapOptions.value.series[0].data = heatmapData
      }
      
    } else {
      ElMessage.error('获取仪表盘数据失败')
    }
  } catch (error) {
    console.error('获取仪表盘数据时出错:', error)
    ElMessage.error('获取数据时发生网络错误')
  }
}

// 组件挂载后初始化热力图和实时数据
onMounted(() => {
  const info = localStorage.getItem('crm_userInfo')
  if (info) {
    try {
      userInfo.value = JSON.parse(info)
      console.log('用户信息:', userInfo.value)

    } catch (e) {
      console.error('用户信息解析失败:', e)
    }
  }
  initHeatmap()
  simulateRealTimeData()
  fetchDashboardData()
})

</script>

<template>
  <div class="dashboard">
    <!-- 数据概览卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="data-card">
            <div class="data-title">总销售额</div>
            <div class="data-value">¥ {{ dashboardStats.totalSales.toLocaleString() }}</div>
            <div class="data-trend">
              <span :class="dashboardStats.totalSalesGrowth >= 0 ? 'up' : 'down'">
                {{ dashboardStats.totalSalesGrowth >= 0 ? '↑' : '↓' }}{{ Math.abs(dashboardStats.totalSalesGrowth) }}%
              </span> 同比上月
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="data-card">
            <div class="data-title">总订单量</div>
            <div class="data-value">{{ dashboardStats.totalOrders }}</div>
            <div class="data-trend">
              <span :class="dashboardStats.totalOrdersGrowth >= 0 ? 'up' : 'down'">
                {{ dashboardStats.totalOrdersGrowth >= 0 ? '↑' : '↓' }}{{ Math.abs(dashboardStats.totalOrdersGrowth) }}%
              </span> 同比上月
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="data-card">
            <div class="data-title">新增客户</div>
            <div class="data-value">{{ dashboardStats.newCustomers }}</div>
            <div class="data-trend">
              <span :class="dashboardStats.newCustomersGrowth >= 0 ? 'up' : 'down'">
                {{ dashboardStats.newCustomersGrowth >= 0 ? '↑' : '↓' }}{{ Math.abs(dashboardStats.newCustomersGrowth) }}%
              </span> 同比上月
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="data-card">
            <div class="data-title">转化率</div>
            <div class="data-value">{{ dashboardStats.conversionRate.toFixed(2) }}%</div>
            <div class="data-trend">
              <span :class="dashboardStats.conversionRateGrowth >= 0 ? 'up' : 'down'">
                {{ dashboardStats.conversionRateGrowth >= 0 ? '↑' : '↓' }}{{ Math.abs(dashboardStats.conversionRateGrowth) }}%
              </span> 同比上月
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 主图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>销售趋势分析</span>
            </div>
          </template>
          <data-chart :options="chartOptions" />
        </el-card>
      </el-col>
      <el-col :span="8" v-if="userInfo?.role === 1">
        <el-card shadow="hover" >
          <template #header >
            <div class="card-header">
              <span>客户来源分布</span>
            </div>
          </template>
          <data-chart :options="pieOptions" style="height: 350px" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>商机转化率漏斗</span>
            </div>
          </template>
          <data-chart :options="opportunityFunnelOptions" style="height: 400px" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>客户类型分布</span>
            </div>
          </template>
          <data-chart :options="customerTypeOptions" style="height: 350px" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>销售业绩排行</span>
            </div>
          </template>
          <data-chart :options="salesRankOptions" style="height: 350px" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 第四行图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>客户留存率分析</span>
            </div>
          </template>
          <data-chart :options="retentionOptions" style="height: 350px" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>客户生命周期价值分析</span>
            </div>
          </template>
          <data-chart :options="clvOptions" style="height: 350px" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 第五行图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>销售预测分析</span>
            </div>
          </template>
          <data-chart :options="salesForecastOptions" style="height: 350px" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>产品销售热力分布</span>
            </div>
          </template>
          <data-chart :options="productHeatmapOptions" style="height: 350px" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 实时数据监控卡片 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card shadow="hover" class="real-time-monitor">
          <template #header>
            <div class="card-header">
              <span>实时销售数据监控</span>
              <span class="update-time">最后更新: {{ new Date().toLocaleTimeString() }}</span>
            </div>
          </template>
          <div class="real-time-data">
            <div class="real-time-item">
              <div class="real-time-label">当前总销售额</div>
              <div class="real-time-value">¥ {{ realTimeData.currentSales.toLocaleString() }}</div>
              <div class="real-time-change">
                <i class="el-icon-arrow-up"></i> 实时更新中
              </div>
            </div>
            <div class="real-time-item">
              <div class="real-time-label">今日订单量</div>
              <div class="real-time-value">{{ realTimeData.ordersToday }}</div>
              <div class="real-time-change">
                <i class="el-icon-arrow-up"></i> 实时更新中
              </div>
            </div>
            <div class="real-time-item">
              <div class="real-time-label">今日新增客户</div>
              <div class="real-time-value">{{ realTimeData.customersToday }}</div>
              <div class="real-time-change">
                <i class="el-icon-arrow-up"></i> 实时更新中
              </div>
            </div>
            <div class="real-time-item">
              <div class="real-time-label">今日转化率</div>
              <div class="real-time-value">{{ realTimeData.conversionRate }}%</div>
              <div class="real-time-change">
                <i class="el-icon-arrow-up"></i> 实时更新中
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 天津人口热力图 -->
    <el-row style="margin-top: 20px">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>天津全市人口热力图</span>
            </div>
          </template>
          <div id="heatmap-container" style="width: 100%; height: 600px;"></div>
          <div class="heatmap-controls" style="margin-top: 10px; text-align: center;">
            <el-button type="primary" @click="showHeatmap">显示热力图</el-button>
            <el-button @click="hideHeatmap">关闭热力图</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>

/* 天津人口热力图样式 */
.heatmap-controls {
  margin-top: 10px;
  text-align: center;
}

.heatmap-controls .el-button {
  margin: 0 5px;
}

.dashboard {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
}

.update-time {
  font-size: 12px;
  color: #6c757d;
  margin-left: auto;
}

.data-card {
  padding: 15px;
  text-align: center;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.data-card:hover {
  background: linear-gradient(135deg, #e9ecef 0%, #dee2e6 100%);
  transform: translateY(-2px);
}

.data-title {
  font-size: 14px;
  color: #6c757d;
  margin-bottom: 10px;
  font-weight: 500;
}

.data-value {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #2c3e50;
}

.data-trend {
  font-size: 12px;
  font-weight: 500;
}

.up {
  color: #4caf50;
}

.down {
  color: #f44336;
}

/* 图表卡片样式增强 */
.el-card {
  transition: all 0.3s ease;
  border-radius: 8px;
  overflow: hidden;
  background: #ffffff;
}

.el-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

/* 实时数据监控样式 */
.real-time-monitor {
  background: linear-gradient(135deg, #2c3e50 0%, #1a252f 100%);
  color: white;
}

.real-time-monitor .card-header span {
  color: white;
}

.real-time-data {
  display: flex;
  justify-content: space-around;
  padding: 20px;
}

.real-time-item {
  text-align: center;
  padding: 15px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  min-width: 200px;
  transition: all 0.3s ease;
}

.real-time-item:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-2px);
}

.real-time-label {
  font-size: 14px;
  color: #95a5a6;
  margin-bottom: 10px;
}

.real-time-value {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 5px;
  color: #ffffff;
  animation: pulse 2s infinite;
}

.real-time-change {
  font-size: 12px;
  color: #4caf50;
  display: flex;
  align-items: center;
  justify-content: center;
}

.real-time-change i {
  margin-right: 5px;
  animation: blink 2s infinite;
}

/* 动画效果 */
@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

@keyframes blink {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}

/* 响应式布局调整 */
@media (max-width: 1200px) {
  .el-col {
    margin-bottom: 20px;
  }
  .real-time-data {
    flex-wrap: wrap;
  }
  .real-time-item {
    margin-bottom: 10px;
  }
}

@media (max-width: 768px) {
  .dashboard {
    padding: 10px;
  }
  .real-time-item {
    min-width: 100%;
  }
  .data-value {
    font-size: 24px;
  }
  .real-time-value {
    font-size: 24px;
  }
}

/* 图表容器样式优化 */
:deep(.data-chart-container) {
  height: 100%;
  width: 100%;
}

/* 数据概览卡片增强 */
.el-col :first-child {
  animation: fadeInUp 0.5s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
