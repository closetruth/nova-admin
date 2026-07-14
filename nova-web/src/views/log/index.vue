<template>
  <el-card shadow="never">
    <el-table :data="rows" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="operateEmpId" label="操作人ID" width="100" />
      <el-table-column prop="operateTime" label="操作时间" width="170" />
      <el-table-column prop="className" label="类名" min-width="220" show-overflow-tooltip />
      <el-table-column prop="methodName" label="方法" width="120" />
      <el-table-column prop="methodParams" label="请求参数" min-width="180" show-overflow-tooltip />
      <el-table-column prop="returnValue" label="返回值" min-width="180" show-overflow-tooltip />
      <el-table-column prop="costTime" label="耗时(ms)" width="100" />
    </el-table>
    <div class="pager">
      <el-pagination
        background
        layout="total, prev, pager, next"
        :total="total"
        v-model:current-page="query.page"
        v-model:page-size="query.pageSize"
        @current-change="load"
      />
    </div>
  </el-card>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { pageLogs } from '@/api/log'

const loading = ref(false)
const rows = ref([])
const total = ref(0)
const query = reactive({ page: 1, pageSize: 10 })

async function load() {
  loading.value = true
  try {
    const res = await pageLogs(query)
    rows.value = res.data?.rows || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.pager {
  margin-top: 14px;
  display: flex;
  justify-content: flex-end;
}
</style>
