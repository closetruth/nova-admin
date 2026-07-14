<template>
  <div class="report" v-loading="loading">
    <el-card shadow="never">
      <template #header>员工职位统计</template>
      <div ref="jobRef" class="chart" />
    </el-card>
    <el-card shadow="never">
      <template #header>员工性别统计</template>
      <div ref="genderRef" class="chart" />
    </el-card>
  </div>
</template>

<script setup>
import { nextTick, onMounted, onBeforeUnmount, ref } from 'vue'
import * as echarts from 'echarts'
import { empGenderData, empJobData } from '@/api/report'

const loading = ref(false)
const jobRef = ref()
const genderRef = ref()
let jobChart
let genderChart

async function load() {
  loading.value = true
  try {
    const [jobRes, genderRes] = await Promise.all([empJobData(), empGenderData()])
    await nextTick()
    jobChart = echarts.init(jobRef.value)
    genderChart = echarts.init(genderRef.value)
    jobChart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: jobRes.data?.jobList || [] },
      yAxis: { type: 'value' },
      series: [{ type: 'bar', data: jobRes.data?.dataList || [], itemStyle: { color: '#2563eb' } }],
    })
    genderChart.setOption({
      tooltip: { trigger: 'item' },
      series: [
        {
          type: 'pie',
          radius: ['40%', '68%'],
          data: (genderRes.data || []).map((item) => ({ name: item.name, value: item.value })),
        },
      ],
    })
  } finally {
    loading.value = false
  }
}

function onResize() {
  jobChart?.resize()
  genderChart?.resize()
}

onMounted(() => {
  load()
  window.addEventListener('resize', onResize)
})
onBeforeUnmount(() => {
  window.removeEventListener('resize', onResize)
  jobChart?.dispose()
  genderChart?.dispose()
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
