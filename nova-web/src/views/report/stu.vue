<template>
  <div class="report" v-loading="loading">
    <el-card shadow="never">
      <template #header>班级人数统计</template>
      <div ref="countRef" class="chart" />
    </el-card>
    <el-card shadow="never">
      <template #header>学员学历统计</template>
      <div ref="degreeRef" class="chart" />
    </el-card>
  </div>
</template>

<script setup>
import { nextTick, onMounted, onBeforeUnmount, ref } from 'vue'
import * as echarts from 'echarts'
import { studentCountData, studentDegreeData } from '@/api/report'

const loading = ref(false)
const countRef = ref()
const degreeRef = ref()
let countChart
let degreeChart

async function load() {
  loading.value = true
  try {
    const [countRes, degreeRes] = await Promise.all([studentCountData(), studentDegreeData()])
    await nextTick()
    countChart = echarts.init(countRef.value)
    degreeChart = echarts.init(degreeRef.value)
    countChart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: countRes.data?.clazzList || [] },
      yAxis: { type: 'value' },
      series: [{ type: 'bar', data: countRes.data?.dataList || [], itemStyle: { color: '#0f766e' } }],
    })
    degreeChart.setOption({
      tooltip: { trigger: 'item' },
      series: [
        {
          type: 'pie',
          radius: ['40%', '68%'],
          data: (degreeRes.data || []).map((item) => ({ name: item.name, value: item.value })),
        },
      ],
    })
  } finally {
    loading.value = false
  }
}

function onResize() {
  countChart?.resize()
  degreeChart?.resize()
}

onMounted(() => {
  load()
  window.addEventListener('resize', onResize)
})
onBeforeUnmount(() => {
  window.removeEventListener('resize', onResize)
  countChart?.dispose()
  degreeChart?.dispose()
})
</script>

<style scoped>
.report {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
}
.chart {
  height: 360px;
}
@media (max-width: 960px) {
  .report {
    grid-template-columns: 1fr;
  }
}
</style>
