<template>
  <el-card>
    <template #header>
      <div class="card-header-row">
        <span>供应商资质管理</span>
        <el-button type="primary" @click="openCreate">新增资质</el-button>
      </div>
    </template>

    <div class="toolbar-row">
      <el-input v-model="query.supplierId" placeholder="输入供应商ID筛选" class="toolbar-select" clearable />
      <el-button type="primary" @click="loadData">查询</el-button>
    </div>

    <el-table :data="rows" stripe v-loading="loading">
      <el-table-column prop="supplierId" label="供应商ID" width="110" />
      <el-table-column prop="qualificationType" label="资质类型" width="160" />
      <el-table-column prop="qualificationName" label="资质名称" min-width="220" />
      <el-table-column prop="qualificationNo" label="证书编号" width="180" />
      <el-table-column prop="issuedBy" label="发证机构" width="180" />
      <el-table-column prop="expireDate" label="到期日期" width="140" />
      <el-table-column prop="status" label="状态" width="120" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="680px">
    <el-form :model="form" label-width="100px">
      <div class="form-grid-2">
        <el-form-item label="供应商ID"><el-input v-model="form.supplierId" /></el-form-item>
        <el-form-item label="资质类型"><el-input v-model="form.qualificationType" /></el-form-item>
        <el-form-item label="资质名称"><el-input v-model="form.qualificationName" /></el-form-item>
        <el-form-item label="证书编号"><el-input v-model="form.qualificationNo" /></el-form-item>
        <el-form-item label="发证机构"><el-input v-model="form.issuedBy" /></el-form-item>
        <el-form-item label="状态"><el-input v-model="form.status" /></el-form-item>
        <el-form-item label="发证日期"><el-input v-model="form.issueDate" placeholder="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="到期日期"><el-input v-model="form.expireDate" placeholder="YYYY-MM-DD" /></el-form-item>
      </div>
      <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="3" /></el-form-item>
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
import { supplierApi } from "@/api/supplier";

const loading = ref(false);
const dialogVisible = ref(false);
const currentId = ref<number | null>(null);
const rows = ref<any[]>([]);
const query = reactive({ supplierId: "" });
const form = reactive({
  supplierId: "",
  qualificationType: "",
  qualificationName: "",
  qualificationNo: "",
  issuedBy: "",
  issueDate: "",
  expireDate: "",
  status: "VALID",
  remark: "",
});
const dialogTitle = computed(() => (currentId.value ? "编辑资质" : "新增资质"));

async function loadData() {
  loading.value = true;
  try {
    const res = await supplierApi.qualificationList({ supplierId: query.supplierId || undefined });
    rows.value = res.data || [];
  } finally {
    loading.value = false;
  }
}

function openCreate() {
  currentId.value = null;
  Object.assign(form, {
    supplierId: "",
    qualificationType: "",
    qualificationName: "",
    qualificationNo: "",
    issuedBy: "",
    issueDate: "",
    expireDate: "",
    status: "VALID",
    remark: "",
  });
  dialogVisible.value = true;
}

function openEdit(row: any) {
  currentId.value = row.id;
  Object.assign(form, {
    supplierId: String(row.supplierId || ""),
    qualificationType: row.qualificationType || "",
    qualificationName: row.qualificationName || "",
    qualificationNo: row.qualificationNo || "",
    issuedBy: row.issuedBy || "",
    issueDate: row.issueDate || "",
    expireDate: row.expireDate || "",
    status: row.status || "VALID",
    remark: row.remark || "",
  });
  dialogVisible.value = true;
}

async function submitForm() {
  const payload = {
    ...form,
    supplierId: Number(form.supplierId),
  };
  if (currentId.value) {
    await supplierApi.qualificationUpdate(currentId.value, payload);
    ElMessage.success("资质已更新");
  } else {
    await supplierApi.qualificationCreate(payload);
    ElMessage.success("资质已新增");
  }
  dialogVisible.value = false;
  await loadData();
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm("确认删除该资质吗？", "删除确认", { type: "warning" });
  await supplierApi.qualificationDelete(id);
  ElMessage.success("资质已删除");
  await loadData();
}

onMounted(loadData);
</script>
