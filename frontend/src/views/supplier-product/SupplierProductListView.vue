<template>
  <el-card>
    <template #header>
      <div class="card-header-row">
        <span>产品供应关系管理</span>
        <el-button type="primary" @click="openCreate">新增供应关系</el-button>
      </div>
    </template>

    <div class="toolbar-row">
      <el-input v-model="query.supplierId" placeholder="供应商ID" class="toolbar-select" clearable />
      <el-input v-model="query.productId" placeholder="产品ID" class="toolbar-select" clearable />
      <el-button type="primary" @click="loadData">查询</el-button>
    </div>

    <el-table :data="rows" stripe v-loading="loading">
      <el-table-column prop="id" label="关系ID" width="100" />
      <el-table-column prop="supplierId" label="供应商ID" width="110" />
      <el-table-column prop="productId" label="产品ID" width="100" />
      <el-table-column prop="supplyStatus" label="供货状态" width="120" />
      <el-table-column prop="quotePrice" label="报价" width="140" />
      <el-table-column prop="currency" label="币种" width="100" />
      <el-table-column prop="deliveryCycleDays" label="供货周期(天)" width="130" />
      <el-table-column prop="warrantyMonths" label="质保(月)" width="120" />
      <el-table-column prop="taxRate" label="税率" width="100" />
      <el-table-column prop="priceRemark" label="备注" min-width="180" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-divider>历史价格</el-divider>
    <div class="toolbar-row">
      <el-input v-model="priceHistoryQuery.supplierProductId" placeholder="供应关系ID" class="toolbar-select" clearable />
      <el-button @click="loadPriceHistory">查询历史价格</el-button>
      <el-button type="primary" @click="openPriceHistoryCreate">新增历史价格</el-button>
    </div>
    <el-table :data="priceHistories" stripe>
      <el-table-column prop="supplierProductId" label="关系ID" width="100" />
      <el-table-column prop="quotePrice" label="价格" width="120" />
      <el-table-column prop="taxRate" label="税率" width="100" />
      <el-table-column prop="deliveryCycleDays" label="交期(天)" width="110" />
      <el-table-column prop="warrantyMonths" label="质保(月)" width="110" />
      <el-table-column prop="effectiveDate" label="生效日期" width="140" />
      <el-table-column prop="expireDate" label="失效日期" width="140" />
      <el-table-column prop="sourceType" label="来源" width="120" />
      <el-table-column prop="remark" label="备注" min-width="180" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openPriceHistoryEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handlePriceHistoryDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="720px">
    <el-form :model="form" label-width="110px">
      <div class="form-grid-2">
        <el-form-item label="供应商ID"><el-input v-model="form.supplierId" /></el-form-item>
        <el-form-item label="产品ID"><el-input v-model="form.productId" /></el-form-item>
        <el-form-item label="供货状态"><el-input v-model="form.supplyStatus" /></el-form-item>
        <el-form-item label="报价"><el-input v-model="form.quotePrice" /></el-form-item>
        <el-form-item label="币种"><el-input v-model="form.currency" /></el-form-item>
        <el-form-item label="生效日期"><el-input v-model="form.priceEffectiveDate" placeholder="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="供货周期(天)"><el-input v-model="form.deliveryCycleDays" /></el-form-item>
        <el-form-item label="质保(月)"><el-input v-model="form.warrantyMonths" /></el-form-item>
        <el-form-item label="最小起订量"><el-input v-model="form.minOrderQty" /></el-form-item>
        <el-form-item label="税率"><el-input v-model="form.taxRate" /></el-form-item>
      </div>
      <el-form-item label="价格备注"><el-input v-model="form.priceRemark" type="textarea" :rows="3" /></el-form-item>
    </el-form>
    <template #footer>
      <div class="drawer-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="priceHistoryDialogVisible" :title="priceHistoryDialogTitle" width="720px">
    <el-form :model="priceHistoryForm" label-width="110px">
      <div class="form-grid-2">
        <el-form-item label="供应关系ID"><el-input v-model="priceHistoryForm.supplierProductId" /></el-form-item>
        <el-form-item label="价格"><el-input v-model="priceHistoryForm.quotePrice" /></el-form-item>
        <el-form-item label="税率"><el-input v-model="priceHistoryForm.taxRate" /></el-form-item>
        <el-form-item label="交期(天)"><el-input v-model="priceHistoryForm.deliveryCycleDays" /></el-form-item>
        <el-form-item label="质保(月)"><el-input v-model="priceHistoryForm.warrantyMonths" /></el-form-item>
        <el-form-item label="生效日期"><el-input v-model="priceHistoryForm.effectiveDate" placeholder="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="失效日期"><el-input v-model="priceHistoryForm.expireDate" placeholder="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="来源"><el-input v-model="priceHistoryForm.sourceType" /></el-form-item>
        <el-form-item label="来源ID"><el-input v-model="priceHistoryForm.sourceId" /></el-form-item>
      </div>
      <el-form-item label="备注"><el-input v-model="priceHistoryForm.remark" type="textarea" :rows="3" /></el-form-item>
    </el-form>
    <template #footer>
      <div class="drawer-footer">
        <el-button @click="priceHistoryDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPriceHistory">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { supplierProductApi } from "@/api/supplierProduct";

const loading = ref(false);
const dialogVisible = ref(false);
const priceHistoryDialogVisible = ref(false);
const currentId = ref<number | null>(null);
const currentPriceHistoryId = ref<number | null>(null);
const rows = ref<any[]>([]);
const priceHistories = ref<any[]>([]);
const query = reactive({ supplierId: "", productId: "" });
const priceHistoryQuery = reactive({ supplierProductId: "" });
const form = reactive({
  supplierId: "",
  productId: "",
  supplyStatus: "ACTIVE",
  quotePrice: "",
  currency: "CNY",
  priceEffectiveDate: "",
  deliveryCycleDays: "",
  warrantyMonths: "",
  minOrderQty: "",
  taxRate: "",
  priceRemark: "",
});
const priceHistoryForm = reactive({
  supplierProductId: "",
  quotePrice: "",
  taxRate: "",
  deliveryCycleDays: "",
  warrantyMonths: "",
  effectiveDate: "",
  expireDate: "",
  sourceType: "MANUAL",
  sourceId: "",
  remark: "",
});
const dialogTitle = computed(() => (currentId.value ? "编辑供应关系" : "新增供应关系"));
const priceHistoryDialogTitle = computed(() => (currentPriceHistoryId.value ? "编辑历史价格" : "新增历史价格"));

async function loadData() {
  loading.value = true;
  try {
    const res = await supplierProductApi.list({
      supplierId: query.supplierId || undefined,
      productId: query.productId || undefined,
    });
    rows.value = res.data || [];
  } finally {
    loading.value = false;
  }
}

async function loadPriceHistory() {
  const res = await supplierProductApi.priceHistoryList({
    supplierProductId: priceHistoryQuery.supplierProductId ? Number(priceHistoryQuery.supplierProductId) : undefined,
  });
  priceHistories.value = res.data || [];
}

function openCreate() {
  currentId.value = null;
  Object.assign(form, {
    supplierId: "",
    productId: "",
    supplyStatus: "ACTIVE",
    quotePrice: "",
    currency: "CNY",
    priceEffectiveDate: "",
    deliveryCycleDays: "",
    warrantyMonths: "",
    minOrderQty: "",
    taxRate: "",
    priceRemark: "",
  });
  dialogVisible.value = true;
}

function openEdit(row: any) {
  currentId.value = row.id;
  Object.assign(form, {
    supplierId: String(row.supplierId || ""),
    productId: String(row.productId || ""),
    supplyStatus: row.supplyStatus || "ACTIVE",
    quotePrice: String(row.quotePrice || ""),
    currency: row.currency || "CNY",
    priceEffectiveDate: row.priceEffectiveDate || "",
    deliveryCycleDays: String(row.deliveryCycleDays || ""),
    warrantyMonths: String(row.warrantyMonths || ""),
    minOrderQty: String(row.minOrderQty || ""),
    taxRate: String(row.taxRate || ""),
    priceRemark: row.priceRemark || "",
  });
  dialogVisible.value = true;
}

function openPriceHistoryCreate() {
  currentPriceHistoryId.value = null;
  Object.assign(priceHistoryForm, {
    supplierProductId: priceHistoryQuery.supplierProductId,
    quotePrice: "",
    taxRate: "",
    deliveryCycleDays: "",
    warrantyMonths: "",
    effectiveDate: "",
    expireDate: "",
    sourceType: "MANUAL",
    sourceId: "",
    remark: "",
  });
  priceHistoryDialogVisible.value = true;
}

function openPriceHistoryEdit(row: any) {
  currentPriceHistoryId.value = row.id;
  Object.assign(priceHistoryForm, {
    supplierProductId: String(row.supplierProductId || ""),
    quotePrice: String(row.quotePrice || ""),
    taxRate: String(row.taxRate || ""),
    deliveryCycleDays: String(row.deliveryCycleDays || ""),
    warrantyMonths: String(row.warrantyMonths || ""),
    effectiveDate: row.effectiveDate || "",
    expireDate: row.expireDate || "",
    sourceType: row.sourceType || "MANUAL",
    sourceId: String(row.sourceId || ""),
    remark: row.remark || "",
  });
  priceHistoryDialogVisible.value = true;
}

async function submitForm() {
  const payload = {
    supplierId: Number(form.supplierId),
    productId: Number(form.productId),
    supplyStatus: form.supplyStatus,
    quotePrice: Number(form.quotePrice || 0),
    currency: form.currency,
    priceEffectiveDate: form.priceEffectiveDate,
    deliveryCycleDays: Number(form.deliveryCycleDays || 0),
    warrantyMonths: Number(form.warrantyMonths || 0),
    minOrderQty: Number(form.minOrderQty || 0),
    taxRate: Number(form.taxRate || 0),
    priceRemark: form.priceRemark,
  };
  if (currentId.value) {
    await supplierProductApi.update(currentId.value, payload);
    ElMessage.success("供应关系已更新");
  } else {
    await supplierProductApi.create(payload);
    ElMessage.success("供应关系已创建");
  }
  dialogVisible.value = false;
  await loadData();
  await loadPriceHistory();
}

async function submitPriceHistory() {
  const payload = {
    supplierProductId: Number(priceHistoryForm.supplierProductId),
    quotePrice: Number(priceHistoryForm.quotePrice || 0),
    taxRate: Number(priceHistoryForm.taxRate || 0),
    deliveryCycleDays: Number(priceHistoryForm.deliveryCycleDays || 0),
    warrantyMonths: Number(priceHistoryForm.warrantyMonths || 0),
    effectiveDate: priceHistoryForm.effectiveDate,
    expireDate: priceHistoryForm.expireDate || null,
    sourceType: priceHistoryForm.sourceType,
    sourceId: Number(priceHistoryForm.sourceId || 0) || null,
    remark: priceHistoryForm.remark,
  };
  if (currentPriceHistoryId.value) {
    await supplierProductApi.priceHistoryUpdate(currentPriceHistoryId.value, payload);
    ElMessage.success("历史价格已更新");
  } else {
    await supplierProductApi.priceHistoryCreate(payload);
    ElMessage.success("历史价格已新增");
  }
  priceHistoryDialogVisible.value = false;
  await loadData();
  await loadPriceHistory();
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm("确认删除该供应关系吗？", "删除确认", { type: "warning" });
  await supplierProductApi.delete(id);
  ElMessage.success("供应关系已删除");
  await loadData();
}

async function handlePriceHistoryDelete(id: number) {
  await ElMessageBox.confirm("确认删除该历史价格记录吗？", "删除确认", { type: "warning" });
  await supplierProductApi.priceHistoryDelete(id);
  ElMessage.success("历史价格已删除");
  await loadPriceHistory();
}

onMounted(async () => {
  await loadData();
  await loadPriceHistory();
});
</script>
