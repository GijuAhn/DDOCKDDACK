<template>
  <div id="view">
    <div id="list">
      <normal-bestcut
        v-for="bestcut in myBestcuts"
        :key="bestcut"
        :bestcut="bestcut"
      ></normal-bestcut>
    </div>
    <loading-spinner id="imgLoading" v-show="isLoading">
      <!-- 이미지 로딩 중 -->
    </loading-spinner>
    <span
      id="noItem"
      v-show="(!myBestcuts || !myBestcuts.length) && !isLoading"
    >
      베스트컷을 등록 해주세요!
    </span>
  </div>
</template>

<script setup>
import NormalBestcut from "@/components/BestcutList/item/NormalBestcut";
import { apiInstance } from "@/api/index";
import { useStore } from "vuex";
import { ref, computed } from "vue";
import LoadingSpinner from "./item/LoadingSpinner.vue";

const store = useStore();

const memberId = computed(() => store.state.memberStore.memberInfo.id).value;

const api = apiInstance();
const accessToken = computed(() => store.state.memberStore.accessToken).value;

const isLoading = ref(true);

let myBestcuts = ref();
const callApi = async () => {
  await api
    .get(`/api/members/${memberId}/bestcuts`, {
      headers: {
        "access-token": accessToken, // 변수로 가지고있는 AccessToken
      },
    })
    .then((response) => {
      console.log("access-bestcuts: ", response.data.content); //bestcut은 pageimpl이기때문에 content까지 붙여줌
      myBestcuts.value = response.data.content;
    })
    .catch((error) => {
      console.log(error);
      // if (error.response.status == 401) {
      //   getAccessTokenByRefreshToken(); // refresh 토큰으로 다시
      // }
    })
    .finally(() => {
      isLoading.value = false;
    });
};

callApi();

store.dispatch("commonStore/setMemberTabAsync", 3);
</script>

<style scoped>
#view {
  /* border: 2px solid black; */
  width: 1200px;
  position: relative;
  left: 50%;
  transform: translate(-50%, 0);
  background-color: white;
}

#list {
  display: grid;
  gap: 35px 0;
  grid-template-columns: repeat(3, 1fr);
  width: 1090px;
  margin: 2%;
}
#noItem {
  font-size: 20px;
  margin-left: 25%;
}
#imgLoading {
  margin-left: 30%;
}
</style>
