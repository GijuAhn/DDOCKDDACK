<template>
  <tr>
    <td>{{ props.reportedBestCut.bestcutTitle }}</td>
    <td>{{ props.reportedBestCut.reason }}</td>
    <td>{{ props.reportedBestCut.reportMemberNickname }}</td>
    <td>{{ props.reportedBestCut.reportedMemberNickname }}</td>
    <td><input v-model="visible" type="checkbox" /></td>
    <td><button @click="punishmentApi">처리</button><button>취소</button></td>
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
import { apiInstance } from "@/api/index";
import reportedBestCutPreview from "@/components/Admin/item/reportedBestCutPreview.vue";

const api = apiInstance();
const props = defineProps({ reportedBestCut: Object });
const visible = ref();
const store = useStore();
const admin_api_url = `/api/admin`;
const accessToken = computed(() => store.state.memberStore.accessToken);

const reportId = computed(() => {
  return props.reportedBestCut.reportId;
});
const bestcutId = computed(() => {
  return props.reportedBestCut.bestcutId;
});

const punishmentApi = () => {
  api
    .delete(
      admin_api_url + `/remove/bestcut/${reportId.value}/${bestcutId.value}`,
      {
        headers: { "access-token": accessToken.value },
        params: {},
      }
    )
    .then((response) => {
      console.log(response);
    })
    .catch((error) => {
      error;
      console.log(error);
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
