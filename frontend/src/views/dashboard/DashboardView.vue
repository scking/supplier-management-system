<template>
  <div class="saas-dashboard">
    <div class="saas-kpi-grid">
      <div class="saas-kpi" v-for="item in cards" :key="item.title">
        <div class="saas-kpi-icon" :class="`tone-${item.tone}`">
          <el-icon :size="20"><component :is="item.icon" /></el-icon>
        </div>
        <div class="saas-kpi-body">
          <div class="saas-kpi-label">{{ item.title }}</div>
          <div class="saas-kpi-value">{{ item.value }}</div>
          <div class="saas-kpi-sub">{{ item.sub }}</div>
        </div>
      </div>
    </div>

    <section class="saas-card">
      <h3 class="saas-card-title">
        <span>风险与待办</span>
        <el-tag size="small" type="warning" effect="light">共 {{ warnings.length }} 条</el-tag>
      </h3>
      <div v-if="warnings.length" class="warn-list">
        <div v-for="(warn, idx) in warnings" :key="idx" class="warn-item">
          <span class="warn-dot" :class="warn.level === 'danger' ? 'is-danger' : 'is-warning'"></span>
          <div class="warn-main">
            <div class="warn-title">{{ warn.title }}</div>
            <div class="warn-sub" v-if="warn.desc">{{ warn.desc }}</div>
          </div>
          <el-tag size="small" :type="warn.level === 'danger' ? 'danger' : 'warning'" effect="light">
            {{ warn.level === 'danger' ? '高风险' : '提醒' }}
          </el-tag>
        </div>
      </div>
      <el-empty v-else description="暂无风险或待办事项" :image-size="72" />
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { dashboardApi } from "@/api/dashboard";
import { Bell, OfficeBuilding, Warning, Money } from "@element-plus/icons-vue";

interface KpiCard {
  title: string;
  value: string | number;
  sub: string;
  icon: any;
  tone: "brand" | "success" | "warning" | "danger" | "info";
}

const cards = ref<KpiCard[]>([
  { title: "待办事项", value: "-", sub: "采购与审批流", icon: Bell, tone: "brand" },
  { title: "供应商总数", value: "-", sub: "正常 / 冻结 / 黑名单", icon: OfficeBuilding, tone: "info" },
  { title: "风险预警", value: "-", sub: "资质、合同、履约", icon: Warning, tone: "warning" },
  { title: "本月采购金额", value: "-", sub: "含询价转合同金额", icon: Money, tone: "success" },
]);

const warnings = ref<{ title: string; level: string; desc?: string }[]>([]);

onMounted(async () => {
  try {
    const res = await dashboardApi.overview();
    cards.value = [
      { title: "待办事项", value: res.data.todoCount ?? 0, sub: "采购与审批流", icon: Bell, tone: "brand" },
      { title: "供应商总数", value: res.data.supplierCount ?? 0, sub: "正常 / 冻结 / 黑名单", icon: OfficeBuilding, tone: "info" },
      { title: "风险预警", value: res.data.riskCount ?? 0, sub: "资质、合同、履约", icon: Warning, tone: "warning" },
      { title: "本月采购金额", value: `¥${res.data.monthPurchaseAmount ?? 0}`, sub: "含询价转合同金额", icon: Money, tone: "success" },
    ];
    warnings.value = res.data.warnings || [];
  } catch {
    /* ignore */
  }
});
</script>

<style scoped>
.saas-dashboard { display: flex; flex-direction: column; gap: var(--saas-space-5); }

.saas-kpi-sub {
  margin-top: 4px;
  font-size: var(--saas-fs-xs);
  color: var(--saas-text-4);
}

.warn-list { display: flex; flex-direction: column; }
.warn-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 4px;
  border-bottom: 1px solid var(--saas-border-3);
}
.warn-item:last-child { border-bottom: none; }

.warn-dot {
  width: 10px; height: 10px; border-radius: 50%;
  flex: none;
}
.warn-dot.is-danger { background: var(--saas-danger); box-shadow: 0 0 0 4px var(--saas-danger-bg); }
.warn-dot.is-warning { background: var(--saas-warning); box-shadow: 0 0 0 4px var(--saas-warning-bg); }

.warn-main { flex: 1; min-width: 0; }
.warn-title {
  font-size: var(--saas-fs-base);
  color: var(--saas-text-1);
  font-weight: 500;
}
.warn-sub { margin-top: 3px; font-size: var(--saas-fs-xs); color: var(--saas-text-3); }
</style>
