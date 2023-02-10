<template>
  <tr>
    <td>{{ props.reportedBestCut.bestcutTitle }}</td>
    <td>{{ props.reportedBestCut.reason }}</td>
    <td>{{ props.reportedBestCut.reportMemberNickname }}</td>
    <td>{{ props.reportedBestCut.reportedMemberNickname }}</td>
    <td><input v-model="visible" type="checkbox" /></td>
    <td>
      <select v-model="banLevel">
        <option value="noPenalty">noPenalty</option>
        <option value="oneWeek" selected>oneWeek</option>
        <option value="oneMonth">oneMonth</option>
        <option value="sixMonth">sixMonth</option>
        <option value="oneYear">oneYear</option>
        <option value="endless">endless</option>
      </select>
      <button @click="punishmentApi()">처리</button>
      <button @click="reportcancelApi()">취소</button>
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
import { apiInstance } from "@/api/index";
import { useStore } from "vuex";
import { defineProps, ref, computed, defineEmits } from "vue";
import reportedBestCutPreview from "@/components/Admin/item/reportedBestCutPreview.vue";

const api = apiInstance();
const props = defineProps({ reportedBestCut: Object });
const visible = ref();
const store = useStore();
const admin_api_url = `/api/admin`;
const accessToken = computed(() => store.state.memberStore.accessToken);
const banLevel = "noPenalty";
const emit = defineEmits(["deleteProps"]);
const bestcutId = computed(() => {
  return props.reportedBestCut.bestcutId;
});

const punishmentApi = () => {
  api
    .delete(
      admin_api_url +
        `/remove/bestcut/${props.reportedBestCut.reportId}/${props.reportedBestCut.bestcutId}`,
      {
        headers: {
          "access-token": accessToken.value,
          banMemberId: props.reportedBestCut.reportedMemberId,
          banLevel: banLevel,
        },
      }
    )
    .then((response) => {
      console.log(response);
      emit("deleteProps", { index: props.index });
    })
    .catch((error) => {
      error;
      console.log(error);
    });
};

const reportcancelApi = () => {
  api
    .delete(
      admin_api_url + `/remove/bestcut/${props.reportedBestCut.reportId}`,
      {
        headers: {
          "access-token": accessToken.value,
        },
      }
    )
    .then((response) => {
      console.log(response);
      emit("deleteProps", { index: props.index });
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
