<template>
  <el-card shadow="never">
    <el-form :inline="true" :model="query" class="search">
      <el-form-item label="姓名"><el-input v-model="query.name" clearable /></el-form-item>
      <el-form-item label="学历">
        <el-select v-model="query.degree" clearable style="width: 120px">
          <el-option v-for="item in DEGREE_OPTIONS" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="班级">
        <el-select v-model="query.clazzId" clearable style="width: 160px">
          <el-option v-for="c in clazzList" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="success" @click="openDialog()">新增学员</el-button>
        <el-button type="danger" :disabled="!selection.length" @click="onBatchDelete">批量删除</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="rows" v-loading="loading" border stripe @selection-change="(val) => (selection = val)">
      <el-table-column type="selection" width="48" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="no" label="学号" width="120" />
      <el-table-column label="性别" width="70">
        <template #default="{ row }">{{ labelOf(GENDER_OPTIONS, row.gender) }}</template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column label="学历" width="90">
        <template #default="{ row }">{{ labelOf(DEGREE_OPTIONS, row.degree) }}</template>
      </el-table-column>
      <el-table-column prop="clazzName" label="班级" min-width="120" />
      <el-table-column prop="violationCount" label="违纪次数" width="90" />
      <el-table-column prop="violationScore" label="违纪扣分" width="90" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button link type="warning" @click="onViolation(row)">违纪处理</el-button>
          <el-button link type="danger" @click="onDelete([row.id])">删除</el-button>
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

    <el-dialog v-model="visible" :title="form.id ? '编辑学员' : '新增学员'" width="640px">
      <el-form :model="form" label-width="90px">
        <el-row :gutter="12">
          <el-col :span="12"><el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="学号"><el-input v-model="form.no" /></el-form-item></el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="form.gender" style="width: 100%">
                <el-option v-for="item in GENDER_OPTIONS" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="身份证号"><el-input v-model="form.idCard" /></el-form-item></el-col>
          <el-col :span="12">
            <el-form-item label="是否院校">
              <el-select v-model="form.isCollege" style="width: 100%">
                <el-option label="是" :value="1" />
                <el-option label="否" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学历">
              <el-select v-model="form.degree" style="width: 100%">
                <el-option v-for="item in DEGREE_OPTIONS" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班级">
              <el-select v-model="form.clazzId" style="width: 100%">
                <el-option v-for="c in clazzList" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="毕业时间">
              <el-date-picker v-model="form.graduationDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24"><el-form-item label="地址"><el-input v-model="form.address" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { addStudent, addViolation, deleteStudents, getStudent, pageStudents, updateStudent } from '@/api/student'
import { listClazzs } from '@/api/clazz'
import { DEGREE_OPTIONS, GENDER_OPTIONS, labelOf } from '@/utils/dicts'

const loading = ref(false)
const saving = ref(false)
const visible = ref(false)
const rows = ref([])
const total = ref(0)
const selection = ref([])
const clazzList = ref([])
const query = reactive({ page: 1, pageSize: 10, name: '', degree: undefined, clazzId: undefined })
const form = reactive(emptyForm())

function emptyForm() {
  return {
    id: null,
    name: '',
    no: '',
    gender: 1,
    phone: '',
    idCard: '',
    isCollege: 1,
    address: '',
    degree: 4,
    graduationDate: '',
    clazzId: undefined,
  }
}

async function load() {
  loading.value = true
  try {
    const res = await pageStudents(query)
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
  Object.assign(form, emptyForm())
  if (row?.id) {
    const res = await getStudent(row.id)
    Object.assign(form, res.data)
  }
  visible.value = true
}

async function onSave() {
  saving.value = true
  try {
    if (form.id) await updateStudent({ ...form })
    else await addStudent({ ...form })
    ElMessage.success('保存成功')
    visible.value = false
    load()
  } finally {
    saving.value = false
  }
}

async function onDelete(ids) {
  await ElMessageBox.confirm('确认删除选中学员？', '提示', { type: 'warning' })
  await deleteStudents(ids)
  ElMessage.success('删除成功')
  load()
}

function onBatchDelete() {
  onDelete(selection.value.map((item) => item.id))
}

async function onViolation(row) {
  const { value } = await ElMessageBox.prompt('请输入违纪扣分（将累加）', '违纪处理', {
    inputPattern: /^\d+$/,
    inputErrorMessage: '请输入数字',
  })
  await addViolation(row.id, value)
  ElMessage.success('已记录违纪')
  load()
}

onMounted(async () => {
  const res = await listClazzs()
  clazzList.value = res.data || []
  load()
})
</script>

<style scoped>
.search { margin-bottom: 8px; }
.pager { margin-top: 14px; display: flex; justify-content: flex-end; }
</style>
