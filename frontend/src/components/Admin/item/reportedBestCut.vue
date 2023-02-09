<template>
  <tr>
    <td>{{ props.reportedBestCut.bestcutTitle }}</td>
    <td>{{ props.reportedBestCut.reason }}</td>
    <td>{{ props.reportedBestCut.reportMemberNickname }}</td>
    <td>{{ props.reportedBestCut.reportedMemberNickname }}</td>
    <td><input v-model="visible" type="checkbox" /></td>
    <td>
      <button @click="setCurrentModalAsync(`admin`)">처리</button>
      <button>취소</button>
    </td>
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
import { useStore } from "vuex";
import { defineProps, ref, computed } from "vue";
import reportedBestCutPreview from "@/components/Admin/item/reportedBestCutPreview.vue";

const props = defineProps({ reportedBestCut: Object });
const visible = ref();
const store = useStore();
const bestcutId = computed(() => {
  return props.reportedBestCut.bestcutId;
});

const setCurrentModalAsync = (what) => {
  store.dispatch("commonStore/setCurrentModalAsync", {
    name: what,
    data: {
      target: "bestcut",
      reportId: props.reportedBestCut.reportId,
      targetId: props.reportedBestCut.bestcutId,
      banMemberId: props.reportedBestCut.reportedMemberId,
    },
  });
};
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
