<template>
  <div class="stats-page">
    <div class="page-grid">
      <el-card class="overview-card" v-for="card in cards" :key="card.title">
        <div class="overview-title">{{ card.title }}</div>
        <div class="overview-value">{{ card.value }}</div>
        <div class="overview-sub">{{ card.sub }}</div>
      </el-card>
    </div>

    <div class="stats-grid">
      <el-card>
        <template #header>供应商分类分布</template>
        <div v-for="item in supplierTypeStats" :key="item.name" class="stats-row">
          <span>{{ item.name }}</span>
          <el-tag type="primary" effect="plain">{{ item.value }}</el-tag>
        </div>
      </el-card>

      <el-card>
        <template #header>合作金额 Top 5</template>
        <div v-for="item in cooperationTop" :key="item.supplierId" class="stats-row">
          <span>{{ item.supplierName }}</span>
          <strong>¥{{ item.amount }}</strong>
        </div>
      </el-card>
    </div>

    <div class="stats-grid">
      <el-card>
        <template #header>风险等级分布</template>
        <div v-for="item in riskLevelStats" :key="item.name" class="stats-row">
          <span>{{ item.name }}</span>
          <el-tag effect="plain">{{ item.value }}</el-tag>
        </div>
      </el-card>

      <el-card>
        <template #header>合同状态分布</template>
        <div v-for="item in contractStatusStats" :key="item.name" class="stats-row">
          <span>{{ item.name }}</span>
          <el-tag type="success" effect="plain">{{ item.value }}</el-tag>
        </div>
      </el-card>
    </div>

    <el-card>
      <template #header>近 6 个月价格趋势</template>
      <div class="trend-list">
        <div v-for="item in priceTrend" :key="item.month" class="trend-row">
          <span class="trend-month">{{ item.month }}</span>
          <div class="trend-bar-wrap">
            <div class="trend-bar" :style="{ width: `${resolveTrendWidth(item.amount)}%` }"></div>
          </div>
          <strong class="trend-value">¥{{ item.amount }}</strong>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { statsApi } from "@/api/stats";

const cards = ref([
  { title: "供应商数量", value: "-", sub: "全部供应商" },
  { title: "合同数量", value: "-", sub: "采购合同台账" },
  { title: "待处理风险", value: "-", sub: "当前未关闭预警" },
  { title: "履约节点", value: "-", sub: "履约链路节点总数" },
  { title: "合同总金额", value: "-", sub: "累计合同金额" },
]);
const supplierTypeStats = ref<Array<{ name: string; value: number }>>([]);
const cooperationTop = ref<Array<{ supplierId: number; supplierName: string; amount: string }>>([]);
const riskLevelStats = ref<Array<{ name: string; value: number }>>([]);
const contractStatusStats = ref<Array<{ name: string; value: number }>>([]);
const priceTrend = ref<Array<{ month: string; amount: number }>>([]);

function resolveTrendWidth(amount: number) {
  const max = Math.max(...priceTrend.value.map((item) => Number(item.amount || 0)), 1);
  return Math.max((Number(amount || 0) / max) * 100, 8);
}

onMounted(async () => {
  const [overviewRes, analysisRes] = await Promise.all([statsApi.overview(), statsApi.analysis()]);
  cards.value = [
    { title: "供应商数量", value: overviewRes.data.supplierCount, sub: "全部供应商" },
    { title: "合同数量", value: overviewRes.data.contractCount, sub: "采购合同台账" },
    { title: "待处理风险", value: overviewRes.data.openRiskCount, sub: "当前未关闭预警" },
    { title: "履约节点", value: overviewRes.data.performanceCount, sub: "履约链路节点总数" },
    { title: "合同总金额", value: `¥${overviewRes.data.contractAmount}`, sub: "累计合同金额" },
  ];
  supplierTypeStats.value = analysisRes.data.supplierTypeStats || [];
  cooperationTop.value = analysisRes.data.cooperationTop || [];
  riskLevelStats.value = analysisRes.data.riskLevelStats || [];
  contractStatusStats.value = analysisRes.data.contractStatusStats || [];
  priceTrend.value = (analysisRes.data.priceTrend || []).map((item: { month: string; amount: string | number }) => ({
    month: item.month,
    amount: Number(item.amount || 0),
  }));
});
</script>

<style scoped>
.stats-page {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18px;
}

.stats-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eef2f7;
}

.stats-row:last-child {
  border-bottom: none;
}

.trend-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.trend-row {
  display: grid;
  grid-template-columns: 90px 1fr 120px;
  gap: 12px;
  align-items: center;
}

.trend-bar-wrap {
  height: 12px;
  background: #edf2ff;
  border-radius: 999px;
  overflow: hidden;
}

.trend-bar {
  height: 100%;
  border-radius: 999px;
  background: linear-gradient(90deg, #60a5fa 0%, #2563eb 100%);
}

.trend-month,
.trend-value {
  color: #303133;
}

@media (max-width: 900px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .trend-row {
    grid-template-columns: 72px 1fr 96px;
  }
}
</style>
