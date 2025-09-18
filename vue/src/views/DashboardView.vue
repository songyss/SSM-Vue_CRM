<script setup lang="ts">
import { ref } from 'vue'
import DataChart from '@/components/DataChart.vue'
import { ElCard, ElRow, ElCol, ElButton } from 'element-plus'

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
  </div>
</template>

<style scoped>
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
