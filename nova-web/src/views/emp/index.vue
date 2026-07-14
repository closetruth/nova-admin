<template>
  <el-card shadow="never">
    <el-form :inline="true" :model="query" class="search">
      <el-form-item label="姓名">
        <el-input v-model="query.name" clearable placeholder="姓名" />
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="query.gender" clearable placeholder="全部" style="width: 120px">
          <el-option v-for="item in GENDER_OPTIONS" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="入职时间">
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
        <el-button @click="reset">重置</el-button>
        <el-button type="success" @click="openDialog()">新增员工</el-button>
        <el-button type="danger" :disabled="!selection.length" @click="onBatchDelete">批量删除</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="rows" v-loading="loading" border stripe @selection-change="(val) => (selection = val)">
      <el-table-column type="selection" width="48" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="username" label="用户名" width="110" />
      <el-table-column label="头像" width="80">
        <template #default="{ row }">
          <el-avatar v-if="row.image" :src="row.image" :size="36" />
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="性别" width="70">
        <template #default="{ row }">{{ labelOf(GENDER_OPTIONS, row.gender) }}</template>
      </el-table-column>
      <el-table-column label="职位" width="100">
        <template #default="{ row }">{{ labelOf(JOB_OPTIONS, row.job) }}</template>
      </el-table-column>
      <el-table-column prop="deptName" label="部门" width="120" />
      <el-table-column prop="entryDate" label="入职日期" width="120" />
      <el-table-column prop="updateTime" label="更新时间" min-width="170" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button link type="danger" @click="onDelete([row.id])">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pager">
      <el-pagination
        background
        layout="total, prev, pager, next, sizes"
        :total="total"
        v-model:current-page="query.page"
        v-model:page-size="query.pageSize"
        @current-change="load"
        @size-change="load"
      />
    </div>

    <el-dialog v-model="visible" :title="form.id ? '编辑员工' : '新增员工'" width="720px" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="form.gender" style="width: 100%">
                <el-option v-for="item in GENDER_OPTIONS" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位">
              <el-select v-model="form.job" style="width: 100%">
                <el-option v-for="item in JOB_OPTIONS" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资"><el-input-number v-model="form.salary" :min="0" style="width: 100%" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门">
              <el-select v-model="form.deptId" style="width: 100%">
                <el-option v-for="d in depts" :key="d.id" :label="d.name" :value="d.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期">
              <el-date-picker v-model="form.entryDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="头像">
              <el-upload :show-file-list="false" :http-request="onUpload" accept="image/*">
                <el-button>上传图片</el-button>
              </el-upload>
              <el-image v-if="form.image" :src="form.image" style="width: 64px; height: 64px; margin-left: 12px" fit="cover" />
            </el-form-item>
          </el-col>
        </el-row>

        <div class="expr-head">
          <span>工作经历</span>
          <el-button size="small" @click="form.exprList.push({ begin: '', end: '', company: '', job: '' })">添加</el-button>
        </div>
        <div v-for="(item, index) in form.exprList" :key="index" class="expr-row">
          <el-date-picker v-model="item.begin" type="date" value-format="YYYY-MM-DD" placeholder="开始" />
          <el-date-picker v-model="item.end" type="date" value-format="YYYY-MM-DD" placeholder="结束" />
          <el-input v-model="item.company" placeholder="公司" />
          <el-input v-model="item.job" placeholder="职位" />
          <el-button link type="danger" @click="form.exprList.splice(index, 1)">删除</el-button>
        </div>
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
import { addEmp, deleteEmps, getEmp, pageEmps, updateEmp, uploadFile } from '@/api/emp'
import { listDepts } from '@/api/dept'
import { GENDER_OPTIONS, JOB_OPTIONS, labelOf } from '@/utils/dicts'

const loading = ref(false)
const saving = ref(false)
const visible = ref(false)
const rows = ref([])
const total = ref(0)
const selection = ref([])
const depts = ref([])
const dateRange = ref([])
const query = reactive({ page: 1, pageSize: 10, name: '', gender: undefined, begin: undefined, end: undefined })
const form = reactive(emptyForm())

function emptyForm() {
  return {
    id: null,
    username: '',
    name: '',
    gender: 1,
    phone: '',
    job: 1,
    salary: 0,
    image: '',
    entryDate: '',
    deptId: undefined,
    exprList: [],
  }
}

watch(dateRange, (val) => {
  query.begin = val?.[0]
  query.end = val?.[1]
})

async function load() {
  loading.value = true
  try {
    const res = await pageEmps(query)
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

function reset() {
  query.name = ''
  query.gender = undefined
  dateRange.value = []
  query.begin = undefined
  query.end = undefined
  search()
}

async function openDialog(row) {
  Object.assign(form, emptyForm())
  if (row?.id) {
    const res = await getEmp(row.id)
    Object.assign(form, {
      ...res.data,
      exprList: res.data.exprList || [],
    })
  }
  visible.value = true
}

async function onUpload({ file }) {
  const res = await uploadFile(file)
  form.image = res.data
  ElMessage.success('上传成功')
}

async function onSave() {
  saving.value = true
  try {
    const payload = { ...form }
    if (form.id) await updateEmp(payload)
    else await addEmp(payload)
    ElMessage.success('保存成功')
    visible.value = false
    load()
  } finally {
    saving.value = false
  }
}

async function onDelete(ids) {
  await ElMessageBox.confirm('确认删除选中员工？', '提示', { type: 'warning' })
  await deleteEmps(ids)
  ElMessage.success('删除成功')
  load()
}

function onBatchDelete() {
  onDelete(selection.value.map((item) => item.id))
}

onMounted(async () => {
  const deptRes = await listDepts()
  depts.value = deptRes.data || []
  load()
})
</script>

<style scoped>
.search {
  margin-bottom: 8px;
}
.pager {
  margin-top: 14px;
  display: flex;
  justify-content: flex-end;
}
.expr-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 8px 0 12px;
  font-weight: 600;
}
.expr-row {
  display: grid;
  grid-template-columns: 140px 140px 1fr 1fr auto;
  gap: 8px;
  margin-bottom: 8px;
}
</style>
