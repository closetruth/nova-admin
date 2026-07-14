<template>
  <div class="layout">
    <aside class="side">
      <div class="brand">
        <span class="logo">N</span>
        <div>
          <strong>Nova Admin</strong>
          <p>企业后台管理系统</p>
        </div>
      </div>
      <el-menu
        :default-active="active"
        background-color="#0f172a"
        text-color="#94a3b8"
        active-text-color="#ffffff"
        router
      >
        <el-menu-item index="/index">
          <el-icon><House /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-sub-menu index="org">
          <template #title>
            <el-icon><OfficeBuilding /></el-icon>
            <span>组织人事</span>
          </template>
          <el-menu-item index="/dept">部门管理</el-menu-item>
          <el-menu-item index="/emp">员工管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="edu">
          <template #title>
            <el-icon><School /></el-icon>
            <span>教务业务</span>
          </template>
          <el-menu-item index="/clazz">班级管理</el-menu-item>
          <el-menu-item index="/stu">学员管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="report">
          <template #title>
            <el-icon><DataAnalysis /></el-icon>
            <span>数据统计</span>
          </template>
          <el-menu-item index="/report/emp">员工信息统计</el-menu-item>
          <el-menu-item index="/report/stu">学员信息统计</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/log">
          <el-icon><Document /></el-icon>
          <span>操作日志</span>
        </el-menu-item>
      </el-menu>
    </aside>
    <section class="main">
      <header class="top">
        <div class="crumb">{{ route.meta.title || 'Nova Admin' }}</div>
        <div class="user">
          <span>{{ userStore.displayName }}</span>
          <el-button link type="primary" @click="onLogout">退出</el-button>
        </div>
      </header>
      <main class="content">
        <router-view />
      </main>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const active = computed(() => route.path)

function onLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100%;
}
.side {
  width: 232px;
  background: var(--nova-side);
  color: #fff;
  flex-shrink: 0;
}
.brand {
  display: flex;
  gap: 12px;
  align-items: center;
  padding: 20px 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}
.brand .logo {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  background: var(--nova-accent);
  display: grid;
  place-items: center;
  font-weight: 700;
}
.brand strong {
  display: block;
  font-size: 15px;
}
.brand p {
  margin: 2px 0 0;
  font-size: 12px;
  color: var(--nova-side-text);
}
.side :deep(.el-menu) {
  border-right: none;
}
.main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}
.top {
  height: 56px;
  background: #fff;
  border-bottom: 1px solid #e2e8f0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}
.crumb {
  font-weight: 600;
}
.user {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #475569;
}
.content {
  padding: 18px;
  flex: 1;
}
</style>
