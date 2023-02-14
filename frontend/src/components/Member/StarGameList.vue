<template>
  <div id="view">
    <div id="list">
      <normal-game
        v-for="game in starredGames"
        :key="game"
        :game="game"
      ></normal-game>
    </div>
    <loading-spinner id="imgLoading" v-show="isLoading">
      <!-- 이미지 로딩 중 -->
    </loading-spinner>
    <span id="noItem" v-show="!starredGames && !isLoading">
      즐겨찾기한 게임이 없습니다!
    </span>
  </div>
</template>

<script setup>
import NormalGame from "@/components/GameList/item/NormalGame";
import { apiInstance } from "@/api/index";
import { useStore } from "vuex";
import { ref, computed } from "vue";
import LoadingSpinner from "./item/LoadingSpinner.vue";

const store = useStore();

const api = apiInstance();
const memberId = computed(() => store.state.memberStore.memberInfo.id).value;
const accessToken = computed(() => store.state.memberStore.accessToken).value;
const isLoading = ref(true);

const starredGames = ref();
const callApi = async () => {
  await api
    .get(`/api/members/${memberId}/starred`, {
      headers: {
        "access-token": accessToken, // 변수로 가지고있는 AccessToken
      },
    })
    .then((response) => {
      console.log("access-star: ", response.data);
      if (response.data.length > 0) {
        starredGames.value = response.data;
      }
      console.log(starredGames.value == undefined);
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

store.dispatch("commonStore/setMemberTabAsync", 1);
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
