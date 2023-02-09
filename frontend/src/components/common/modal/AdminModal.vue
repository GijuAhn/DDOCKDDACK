<template>
  <div>
    <div id="admin-modal">
      <div id="close-button">
        <img
          :src="require(`@/assets/images/close-button.svg`)"
          width="18"
          height="18"
          id="close-icon"
          @click="setCurrentModalAsync"
        />
      </div>
      <select v-model="banLevel">
        <option value="noPenalty">noPenalty</option>
        <option value="oneWeek" selected>oneWeek</option>
        <option value="oneMonth">oneMonth</option>
        <option value="sixMonth">sixMonth</option>
        <option value="oneYear">oneYear</option>
        <option value="endless">endless</option>
      </select>
      <button @click="punishmentApi">확인</button>
    </div>
  </div>
</template>

<script setup>
import { apiInstance } from "@/api/index";
import { useStore } from "vuex";
import { computed } from "vue";
const api = apiInstance();
const store = useStore();
const admin_api_url = `/api/admin`;
const accessToken = computed(() => store.state.memberStore.accessToken);
const currentModal = computed(() => store.state.commonStore.currentModal);
const banLevel = "noPenalty";

const setCurrentModalAsync = (value) => {
  if (value === "then") {
    store.dispatch("commonStore/setCurrentModalAsync", {
      name: "reportComplete",
      data: "",
    });
  } else {
    store.dispatch("commonStore/setCurrentModalAsync", "");
  }
};

const punishmentApi = () => {
  api
    .delete(
      admin_api_url +
        `/remove/${currentModal.value.data.target}/${currentModal.value.data.reportId}/${currentModal.value.data.targetId}`,
      {
        headers: {
          "access-token": accessToken.value,
          banMemberId: currentModal.value.data.banMemberId,
          banLevel: banLevel,
        },
      }
    )
    .then((response) => {
      console.log(response);
    })
    .catch((error) => {
      error;
      console.log(error);
    });
  setCurrentModalAsync();
};
</script>

<style scoped>
#admin-modal {
  background-color: white;
  width: 377px;
  height: 426px;
  border-radius: 10px;
  text-align: center;
}

#admin-modal span {
  font-family: "Gugi-Regular";
  font-size: 96px;
}

#close-button {
  text-align: right;
  margin-right: 13px;
  margin-top: 50px;
}

#close-icon {
  margin-top: 15px;
}
</style>
