<template>
  <div class="home">
    <el-card shadow="never">
      <h2>欢迎使用 Nova Admin</h2>
      <p>多模块企业后台脚手架：组织人事、教务业务、数据统计与操作审计均已接入后端 API。</p>
    </el-card>
    <div class="cards">
      <el-card v-for="item in cards" :key="item.path" shadow="hover" class="card" @click="$router.push(item.path)">
        <h3>{{ item.title }}</h3>
        <p>{{ item.desc }}</p>
      </el-card>
    </div>
  </div>
</template>

<script setup>
const cards = [
  { title: '部门管理', desc: '维护组织架构', path: '/dept' },
  { title: '员工管理', desc: '员工档案与工作经历', path: '/emp' },
  { title: '班级 / 学员', desc: '教务业务全流程', path: '/clazz' },
  { title: '数据统计', desc: '职位、性别、学历看板', path: '/report/emp' },
]
</script>

<style scoped>
.home h2 {
  margin: 0 0 8px;
}
.home p {
  margin: 0;
  color: #64748b;
}
.cards {
  margin-top: 16px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 14px;
}
.card {
  cursor: pointer;
}
.card h3 {
  margin: 0 0 8px;
}
.card p {
  margin: 0;
  color: #64748b;
  font-size: 13px;
}
</style>
