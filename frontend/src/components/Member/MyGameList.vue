<template>
  <div id="view">
    <!-- <h1>내가 만든 게임 목록 입니다.</h1> -->
    <div id="list">
      <normal-game
        v-for="game in myGames"
        :key="game"
        :game="game"
      ></normal-game>
    </div>
    <loading-spinner id="imgLoading" v-show="isLoading">
      <!-- 이미지 로딩 중 -->
    </loading-spinner>
    <div id="noItem" v-show="(!myGames || !myGames.length) && !isLoading">
      게임을 등록 해주세요!
    </div>
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
const accessToken = computed(() => store.state.memberStore.accessToken).value;

const isLoading = ref(true);
const myGames = ref();

const callApi = () => {
  api
    .get(`/api/members/games`, {
      headers: {
        "access-token": accessToken, // 변수로 가지고있는 AccessToken
      },
    })
    .then((response) => {
      console.log("access-games: ", response.data);
      myGames.value = response.data.content;
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

store.dispatch("commonStore/setMemberTabAsync", 2);
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
  margin-top: 10%;
  margin-left: 25%;
}

#imgLoading {
  margin-left: 30%;
}
</style>
