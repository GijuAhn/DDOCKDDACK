<template>
  <div>
    <div id="reportModal">
      <div id="reportTitle">
        <div><span>게임 신고</span></div>
      </div>
      <div id="reportReason">
        <div @click="reportGame(`SPAM`)"><span>스팸</span></div>
        <div @click="reportGame(`SEXUAL`)"><span>선정적</span></div>
        <div @click="reportGame(`VIOLENT`)"><span>폭력적</span></div>
        <div @click="reportGame(`DISGUSTING`)"><span>혐오스러움</span></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useStore } from "vuex";
import { computed } from "vue";
import { apiInstance } from "@/api/index";

const api = apiInstance();
const store = useStore();
const currentModal = computed(() => store.state.commonStore.currentModal);

const reportGame = (reportType) => {
  api
    .post(`/api/games/report/${currentModal.value.data.gameId}`, {
      reportType: reportType,
    })
    .then(() => {
      setCurrentModalAsync();
    })
    .catch((error) => {
      console.log(error);
    });
};
const setCurrentModalAsync = () => {
  store.dispatch("commonStore/setCurrentModalAsync", {
    name: "reportComplete",
    data: "",
  });
};
</script>

<style scoped>
#reportModal {
  background-color: white;
  width: 235px;
  border-radius: 10px;
  padding: 5px 0;
}
#reportModal span {
  display: block;
}

#reportTitle {
  text-align: center;
  border-bottom: 1px solid #a0a0a0;
}
#reportTitle span {
  font-size: 19px;
  padding: 10px;
}
#reportReason span {
  font-size: 16px;
  padding: 12px;
  margin: 1px;
  border-bottom: 1px solid #eaeaea;
}
#reportReason:hover {
  cursor: pointer;
}
#reportReason div:last-child span {
  border-bottom: none;
}
</style>
