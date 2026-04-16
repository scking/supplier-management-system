<template>
  <div class="saas-shell">
    <aside class="saas-aside">
      <div class="saas-brand">
        <div class="saas-brand-logo">供</div>
        <div>
          <div class="saas-brand-title">供应商管理</div>
          <div class="saas-brand-subtitle">Supplier Management</div>
        </div>
      </div>

      <el-scrollbar class="saas-aside-scroll">
        <el-menu class="saas-side-menu" :default-active="route.path" @select="handleSelect">
          <el-menu-item index="/dashboard">
            <el-icon><Odometer /></el-icon>
            <template #title>工作台</template>
          </el-menu-item>
          <el-sub-menu index="supplier">
            <template #title><el-icon><OfficeBuilding /></el-icon><span>供应商</span></template>
            <el-menu-item index="/supplier/list">供应商管理</el-menu-item>
            <el-menu-item index="/supplier/qualification">资质管理</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="product">
            <template #title><el-icon><Goods /></el-icon><span>产品</span></template>
            <el-menu-item index="/product/list">产品管理</el-menu-item>
            <el-menu-item index="/product/params">产品参数</el-menu-item>
            <el-menu-item index="/product/attachments">产品附件</el-menu-item>
            <el-menu-item index="/supplier-product/list">产品供应关系</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="procure">
            <template #title><el-icon><ShoppingCart /></el-icon><span>采购协同</span></template>
            <el-menu-item index="/purchase/list">采购需求</el-menu-item>
            <el-menu-item index="/inquiry/list">询价比价</el-menu-item>
            <el-menu-item index="/contract/list">合同管理</el-menu-item>
            <el-menu-item index="/performance/list">履约管理</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="analysis">
            <template #title><el-icon><DataAnalysis /></el-icon><span>评价分析</span></template>
            <el-menu-item index="/evaluation/list">供应商评价</el-menu-item>
            <el-menu-item index="/risk/list">风险预警</el-menu-item>
            <el-menu-item index="/stats/index">统计分析</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/audit/operation-log">
            <el-icon><Document /></el-icon>
            <template #title>操作日志</template>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>

      <div class="saas-aside-footer">v1.0 · 本地开发</div>
    </aside>

    <main class="saas-main">
      <header class="saas-header">
        <div class="header-left">
          <h1 class="saas-header-title">{{ currentTitle }}</h1>
          <div class="saas-text-mute header-desc">项目 · 采购需求 · 询价 · 合同 · 履约 全过程追溯</div>
        </div>
        <div class="saas-header-right">
          <el-button text type="primary" @click="goPortal">
            <el-icon><Back /></el-icon>
            <span>返回统一门户</span>
          </el-button>
          <div class="saas-user-chip">
            <div class="saas-avatar">{{ (authStore.realName || "U").charAt(0) }}</div>
            <span class="saas-user-name">{{ authStore.realName || "未登录" }}</span>
          </div>
        </div>
      </header>

      <div class="saas-tabs-bar">
        <div
          v-for="tab in tabsStore.tabs"
          :key="tab.path"
          class="tabs-tag-wrap"
          @contextmenu.prevent="openTabMenu($event, tab.path)"
        >
          <el-tag
            class="tabs-tag"
            :closable="tab.closable !== false"
            :type="tabsStore.activePath === tab.path ? 'primary' : 'info'"
            :effect="tabsStore.activePath === tab.path ? 'dark' : 'light'"
            @click="router.push(tab.path)"
            @close="closeTab(tab.path)"
          >
            {{ tab.title }}
          </el-tag>
        </div>
      </div>

      <div
        v-if="tabMenu.visible"
        class="tab-context-menu"
        :style="{ left: `${tabMenu.x}px`, top: `${tabMenu.y}px` }"
      >
        <button type="button" class="tab-menu-item" @click="handleTabMenu('left')">关闭左侧</button>
        <button type="button" class="tab-menu-item" @click="handleTabMenu('right')">关闭右侧</button>
        <button type="button" class="tab-menu-item danger" @click="handleTabMenu('all')">关闭全部</button>
      </div>

      <section class="saas-content">
        <router-view v-slot="{ Component, route: currentRoute }">
          <keep-alive :include="tabsStore.cachedNames">
            <component :is="Component" :key="currentRoute.name" />
          </keep-alive>
        </router-view>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onBeforeUnmount, reactive, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAuthStore } from "@/store/auth";
import { useTabsStore } from "@/store/tabs";
import { getPortalOrigin, syncPortalOrigin } from "@/composables/usePortalSso";
import {
  Odometer, OfficeBuilding, Goods, ShoppingCart, DataAnalysis, Document, Back,
} from "@element-plus/icons-vue";

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const tabsStore = useTabsStore();
const tabMenu = reactive({ visible: false, x: 0, y: 0, path: "" });

const currentTitle = computed(() => String(route.meta.title || "供应商管理系统"));

function handleSelect(index: string) { router.push(index); }
function goPortal() { window.location.href = getPortalOrigin(); }

function closeTab(path: string) {
  const isActive = tabsStore.activePath === path;
  tabsStore.remove(path);
  if (isActive) router.push(tabsStore.activePath || "/dashboard");
}
function openTabMenu(event: MouseEvent, path: string) {
  tabMenu.visible = true; tabMenu.x = event.clientX; tabMenu.y = event.clientY; tabMenu.path = path;
}
function hideTabMenu() { tabMenu.visible = false; }
function handleTabMenu(action: "left" | "right" | "all") {
  const currentPath = tabMenu.path;
  if (!currentPath) return;
  if (action === "left") tabsStore.closeLeft(currentPath);
  else if (action === "right") tabsStore.closeRight(currentPath);
  else tabsStore.closeAll();
  hideTabMenu();
  router.push(tabsStore.activePath || "/dashboard");
}

onMounted(() => {
  syncPortalOrigin();
  window.addEventListener("click", hideTabMenu);
  tabsStore.restore();
  tabsStore.open({
    name: String(route.name), path: route.path,
    title: String(route.meta.title || "页面"),
    cacheKey: String(route.meta.cacheKey || ""),
    closable: route.path !== "/dashboard",
  });
  const token = localStorage.getItem("supplier_access_token");
  if (token && !authStore.realName) {
    authStore.loadCurrentUser().catch(() => {
      authStore.logout();
      router.replace("/login");
    });
  }
});

watch(
  () => route.fullPath,
  () => {
    tabsStore.open({
      name: String(route.name), path: route.path,
      title: String(route.meta.title || "页面"),
      cacheKey: String(route.meta.cacheKey || ""),
      closable: route.path !== "/dashboard",
    });
  },
  { immediate: true }
);

onBeforeUnmount(() => { window.removeEventListener("click", hideTabMenu); });
</script>

<style scoped>
.header-left { display: flex; flex-direction: column; gap: 2px; }
.header-desc { font-size: 12px; line-height: 1.3; }

.saas-aside-scroll { flex: 1; min-height: 0; }
:deep(.saas-aside-scroll .el-scrollbar__view) { padding: 0 8px; }

.saas-tabs-bar {
  display: flex;
  gap: 8px;
  align-items: center;
  padding: 10px var(--saas-space-6) 0;
  flex-wrap: wrap;
  background: var(--saas-bg-card);
  border-bottom: 1px solid var(--saas-border-3);
}
.tabs-tag-wrap { display: inline-flex; }
.tabs-tag { cursor: pointer; }

.tab-context-menu {
  position: fixed;
  z-index: 3000;
  min-width: 132px;
  padding: 6px;
  border: 1px solid var(--saas-border-1);
  border-radius: var(--saas-radius-md);
  background: var(--saas-bg-card);
  box-shadow: var(--saas-shadow-lg);
}
.tab-menu-item {
  width: 100%;
  border: none;
  background: transparent;
  text-align: left;
  padding: 8px 12px;
  border-radius: var(--saas-radius-sm);
  cursor: pointer;
  color: var(--saas-text-2);
  font-size: var(--saas-fs-sm);
}
.tab-menu-item:hover { background: var(--saas-brand-50); color: var(--saas-brand-600); }
.tab-menu-item.danger:hover { background: var(--saas-danger-bg); color: var(--saas-danger); }
</style>
