<template>
  <tr>
    <td>{{ props.reportedBestCut.bestcutId }}</td>
    <td>{{ props.reportedBestCut.reason }}</td>
    <td>{{ props.reportedBestCut.reportMemberId }}</td>
    <td>{{ props.reportedBestCut.reportedMemberId }}</td>
    <td><input v-model="visible" type="checkbox" /></td>
    <td><button>처리</button></td>
  </tr>
  <tr v-if="visible">
    <td colspan="6">
      <reported-best-cut-preview
        :bestcutId="bestcutId"
      ></reported-best-cut-preview>
    </td>
  </tr>
</template>

<script setup>
import { defineProps, ref, computed } from "vue";
import reportedBestCutPreview from "@/components/Admin/item/reportedBestCutPreview.vue";

const props = defineProps({ reportedBestCut: Object });
const visible = ref();
const bestcutId = computed(() => {
  return props.reportedBestCut.bestcutId;
});
bestcutId;
</script>

<style scoped>
td {
  height: 50px;
  border-bottom: 1px solid #737373;
}

input {
  margin-top: 3px;
  width: 25px;
  height: 25px;
}
</style>
