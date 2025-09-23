<script setup lang="ts">
import { ref, onMounted } from 'vue'
import DataChart from '@/components/DataChart.vue'
import { ElCard, ElRow, ElCol, ElButton } from 'element-plus'

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
    data: ['销售额', '订单量'],
  },
  xAxis: {
    type: 'category',
    data: ['1月', '2月', '3月', '4月', '5月', '6月'],
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
      name: '订单量',
      axisLabel: {
        formatter: '{value} 个',
      },
    },
  ],
  series: [
    {
      name: '销售额',
      type: 'bar',
      data: [120, 200, 150, 80, 70, 110],
    },
    {
      name: '订单量',
      type: 'line',
      yAxisIndex: 1,
      data: [12, 20, 15, 8, 7, 11],
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
      data: [
        { value: 1048, name: '搜索引擎' },
        { value: 735, name: '直接访问' },
        { value: 580, name: '邮件营销' },
        { value: 484, name: '联盟广告' },
        { value: 300, name: '视频广告' },
      ],
    },
  ],
})

const funnelOptions = ref({
  title: {
    text: '销售渠道占比',
    left: 'center',
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c}%',
  },
  legend: {
    data: ['线上商城', '线下门店', '代理商', '大客户'],
    bottom: 10,
  },
  series: [
    {
      name: '销售渠道',
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
        { value: 60, name: '线上商城' },
        { value: 40, name: '线下门店' },
        { value: 20, name: '代理商' },
        { value: 10, name: '大客户' },
      ],
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

// 组件挂载后初始化热力图
onMounted(() => {
  initHeatmap()
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
            <div class="data-value">¥ 1,280,000</div>
            <div class="data-trend"><span class="up">↑12%</span> 同比上月</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="data-card">
            <div class="data-title">总订单量</div>
            <div class="data-value">1,024</div>
            <div class="data-trend"><span class="up">↑8%</span> 同比上月</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="data-card">
            <div class="data-title">新增客户</div>
            <div class="data-value">256</div>
            <div class="data-trend"><span class="up">↑5%</span> 同比上月</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="data-card">
            <div class="data-title">转化率</div>
            <div class="data-value">32%</div>
            <div class="data-trend"><span class="down">↓2%</span> 同比上月</div>
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
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
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
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>销售渠道占比</span>
            </div>
          </template>
          <data-chart :options="funnelOptions" style="height: 350px" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>快速操作</span>
            </div>
          </template>
          <div style="padding: 20px">
            <el-button type="primary" style="width: 100%; margin-bottom: 15px">新增客户</el-button>
            <el-button type="success" style="width: 100%; margin-bottom: 15px">创建订单</el-button>
            <el-button type="warning" style="width: 100%; margin-bottom: 15px">查看报表</el-button>
            <el-button type="info" style="width: 100%">数据导出</el-button>
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
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
}

.data-card {
  padding: 15px;
  text-align: center;
}
.data-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}
.data-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}
.data-trend {
  font-size: 12px;
}
.up {
  color: #67c23a;
}
.down {
  color: #f56c6c;
}
</style>
