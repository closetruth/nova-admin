<template>
  <div class="login-page">
    <div class="panel">
      <div class="intro">
        <p class="eyebrow">Nova Admin</p>
        <h1>企业后台管理系统</h1>
        <p class="desc">JWT 鉴权 · 组织人事 · 教务业务 · 数据统计 · 操作审计</p>
      </div>
      <el-form class="form" :model="form" @submit.prevent>
        <h2>登录</h2>
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" size="large" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" show-password placeholder="密码" size="large" />
        </el-form-item>
        <el-button type="primary" size="large" class="submit" :loading="loading" @click="onSubmit">
          进入系统
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const loading = ref(false)
const form = reactive({
  username: '',
  password: '',
})

async function onSubmit() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    await userStore.login(form)
    ElMessage.success('登录成功')
    router.replace(route.query.redirect || '/index')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100%;
  display: grid;
  place-items: center;
  background:
    radial-gradient(circle at 20% 20%, rgba(37, 99, 235, 0.18), transparent 40%),
    radial-gradient(circle at 80% 0%, rgba(15, 23, 42, 0.12), transparent 35%),
    linear-gradient(160deg, #eef2ff, #f8fafc 45%, #e2e8f0);
  padding: 24px;
}
.panel {
  width: min(920px, 100%);
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  background: #fff;
  border-radius: 18px;
  overflow: hidden;
  box-shadow: 0 24px 60px rgba(15, 23, 42, 0.12);
}
.intro {
  padding: 48px 40px;
  background: #0f172a;
  color: #fff;
}
.eyebrow {
  margin: 0;
  color: #93c5fd;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  font-size: 12px;
}
.intro h1 {
  margin: 12px 0 16px;
  font-size: 32px;
  line-height: 1.2;
}
.desc {
  margin: 0;
  color: #94a3b8;
  line-height: 1.7;
}
.form {
  padding: 48px 36px;
}
.form h2 {
  margin: 0 0 24px;
}
.submit {
  width: 100%;
  margin-top: 8px;
}
@media (max-width: 800px) {
  .panel {
    grid-template-columns: 1fr;
  }
  .intro {
    padding: 28px 24px;
  }
}
</style>
