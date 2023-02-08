<template>
  <tr>
    <td>{{ props.reportedBestCut.bestcut }}</td>
    <td>{{ props.reportedBestCut.reportType }}</td>
    <td>{{ props.reportedBestCut.reportMember }}</td>
    <td>{{ props.reportedBestCut.reportedMember }}</td>
    <td><input v-model="visible" type="checkbox" /></td>
    <td><button>처리</button></td>
  </tr>
  <tr v-if="visible">
    <td colspan="6"></td>
  </tr>
</template>

<script setup>
import { defineProps, ref, computed } from "vue";
// import reportedModalPreview from "@/components/Admin/item/reportedModalPreview.vue";

const props = defineProps({ reportedGame: Object });
const visible = ref();
const bestcutId = computed(() => {
  return props.reportedBestCut.bestcut;
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
