<template>
  <el-card>
    <template #header>
      <div class="card-header-row">
        <span>产品参数管理</span>
        <el-button type="primary" @click="openCreate">新增参数</el-button>
      </div>
    </template>

    <div class="toolbar-row">
      <el-input v-model="query.productId" placeholder="输入产品ID筛选" class="toolbar-select" clearable />
      <el-button type="primary" @click="loadData">查询</el-button>
    </div>

    <el-table :data="rows" stripe v-loading="loading">
      <el-table-column prop="productId" label="产品ID" width="100" />
      <el-table-column prop="paramName" label="参数名称" min-width="180" />
      <el-table-column prop="paramValue" label="参数值" min-width="180" />
      <el-table-column prop="paramUnit" label="单位" width="120" />
      <el-table-column prop="sortNo" label="排序" width="100" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="640px">
    <el-form :model="form" label-width="100px">
      <div class="form-grid-2">
        <el-form-item label="产品ID"><el-input v-model="form.productId" /></el-form-item>
        <el-form-item label="参数名称"><el-input v-model="form.paramName" /></el-form-item>
        <el-form-item label="参数值"><el-input v-model="form.paramValue" /></el-form-item>
        <el-form-item label="参数单位"><el-input v-model="form.paramUnit" /></el-form-item>
        <el-form-item label="排序"><el-input v-model="form.sortNo" /></el-form-item>
      </div>
    </el-form>
    <template #footer>
      <div class="drawer-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { productApi } from "@/api/product";

const loading = ref(false);
const dialogVisible = ref(false);
const currentId = ref<number | null>(null);
const rows = ref<any[]>([]);
const query = reactive({ productId: "" });
const form = reactive({
  productId: "",
  paramName: "",
  paramValue: "",
  paramUnit: "",
  sortNo: 0,
});
const dialogTitle = computed(() => (currentId.value ? "编辑产品参数" : "新增产品参数"));

async function loadData() {
  loading.value = true;
  try {
    const res = await productApi.paramList({ productId: query.productId || undefined });
    rows.value = res.data || [];
  } finally {
    loading.value = false;
  }
}

function openCreate() {
  currentId.value = null;
  Object.assign(form, { productId: "", paramName: "", paramValue: "", paramUnit: "", sortNo: 0 });
  dialogVisible.value = true;
}

function openEdit(row: any) {
  currentId.value = row.id;
  Object.assign(form, {
    productId: String(row.productId || ""),
    paramName: row.paramName || "",
    paramValue: row.paramValue || "",
    paramUnit: row.paramUnit || "",
    sortNo: Number(row.sortNo || 0),
  });
  dialogVisible.value = true;
}

async function submitForm() {
  const payload = {
    ...form,
    productId: Number(form.productId),
    sortNo: Number(form.sortNo || 0),
  };
  if (currentId.value) {
    await productApi.paramUpdate(currentId.value, payload);
    ElMessage.success("产品参数已更新");
  } else {
    await productApi.paramCreate(payload);
    ElMessage.success("产品参数已新增");
  }
  dialogVisible.value = false;
  await loadData();
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm("确认删除该产品参数吗？", "删除确认", { type: "warning" });
  await productApi.paramDelete(id);
  ElMessage.success("产品参数已删除");
  await loadData();
}

onMounted(loadData);
</script>
