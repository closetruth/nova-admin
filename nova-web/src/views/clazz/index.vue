<template>
  <el-card shadow="never">
    <el-form :inline="true" :model="query" class="search">
      <el-form-item label="班级名称">
        <el-input v-model="query.name" clearable />
      </el-form-item>
      <el-form-item label="结课时间">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          value-format="YYYY-MM-DD"
          start-placeholder="开始"
          end-placeholder="结束"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="success" @click="openDialog()">新增班级</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="rows" v-loading="loading" border stripe>
      <el-table-column prop="name" label="班级名称" min-width="140" />
      <el-table-column prop="room" label="教室" width="100" />
      <el-table-column label="学科" width="100">
        <template #default="{ row }">{{ labelOf(SUBJECT_OPTIONS, row.subject) }}</template>
      </el-table-column>
      <el-table-column prop="masterName" label="班主任" width="110" />
      <el-table-column prop="beginDate" label="开课时间" width="120" />
      <el-table-column prop="endDate" label="结课时间" width="120" />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button link type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
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

    <el-dialog v-model="visible" :title="form.id ? '编辑班级' : '新增班级'" width="560px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="班级名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="教室"><el-input v-model="form.room" /></el-form-item>
        <el-form-item label="学科">
          <el-select v-model="form.subject" style="width: 100%">
            <el-option v-for="item in SUBJECT_OPTIONS" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="班主任">
          <el-select v-model="form.masterId" style="width: 100%" filterable>
            <el-option v-for="e in emps" :key="e.id" :label="e.name" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="开课时间">
          <el-date-picker v-model="form.beginDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结课时间">
          <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { onMounted, reactive, ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { addClazz, deleteClazz, getClazz, pageClazzs, updateClazz } from '@/api/clazz'
import { listEmps } from '@/api/emp'
import { SUBJECT_OPTIONS, labelOf } from '@/utils/dicts'

const loading = ref(false)
const saving = ref(false)
const visible = ref(false)
const rows = ref([])
const total = ref(0)
const emps = ref([])
const dateRange = ref([])
const query = reactive({ page: 1, pageSize: 10, name: '', begin: undefined, end: undefined })
const form = reactive({
  id: null,
  name: '',
  room: '',
  subject: 1,
  masterId: undefined,
  beginDate: '',
  endDate: '',
})

watch(dateRange, (val) => {
  query.begin = val?.[0]
  query.end = val?.[1]
})

async function load() {
  loading.value = true
  try {
    const res = await pageClazzs(query)
    rows.value = res.data?.rows || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

function search() {
  query.page = 1
  load()
}

async function openDialog(row) {
  Object.assign(form, {
    id: null,
    name: '',
    room: '',
    subject: 1,
    masterId: undefined,
    beginDate: '',
    endDate: '',
  })
  if (row?.id) {
    const res = await getClazz(row.id)
    Object.assign(form, res.data)
  }
  visible.value = true
}

async function onSave() {
  saving.value = true
  try {
    if (form.id) await updateClazz({ ...form })
    else await addClazz({ ...form })
    ElMessage.success('保存成功')
    visible.value = false
    load()
  } finally {
    saving.value = false
  }
}

async function onDelete(row) {
  await ElMessageBox.confirm(`确认删除班级「${row.name}」？`, '提示', { type: 'warning' })
  await deleteClazz(row.id)
  ElMessage.success('删除成功')
  load()
}

onMounted(async () => {
  const empRes = await listEmps()
  emps.value = empRes.data || []
  load()
})
</script>

<style scoped>
.search { margin-bottom: 8px; }
.pager { margin-top: 14px; display: flex; justify-content: flex-end; }
</style>
