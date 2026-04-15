<template>
  <div class="page-grid">
    <el-card class="overview-card" v-for="item in cards" :key="item.title">
      <div class="overview-title">{{ item.title }}</div>
      <div class="overview-value">{{ item.value }}</div>
      <div class="overview-sub">{{ item.sub }}</div>
    </el-card>
    <el-card class="wide-card">
      <template #header>风险与待办</template>
      <el-timeline>
        <el-timeline-item v-for="warn in warnings" :key="warn.title" :type="warn.level === 'danger' ? 'danger' : 'warning'">
          {{ warn.title }}
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { dashboardApi } from "@/api/dashboard";

const cards = ref([
  { title: "待办事项", value: "-", sub: "采购与审批流" },
  { title: "供应商总数", value: "-", sub: "正常 / 冻结 / 黑名单" },
  { title: "风险预警", value: "-", sub: "资质、合同、履约" },
  { title: "本月采购金额", value: "-", sub: "含询价转合同金额" },
]);

const warnings = ref<{ title: string; level: string }[]>([]);

onMounted(async () => {
  const res = await dashboardApi.overview();
  cards.value = [
    { title: "待办事项", value: res.data.todoCount, sub: "采购与审批流" },
    { title: "供应商总数", value: res.data.supplierCount, sub: "正常 / 冻结 / 黑名单" },
    { title: "风险预警", value: res.data.riskCount, sub: "资质、合同、履约" },
    { title: "本月采购金额", value: `¥${res.data.monthPurchaseAmount}`, sub: "含询价转合同金额" },
  ];
  warnings.value = res.data.warnings || [];
});
</script>

