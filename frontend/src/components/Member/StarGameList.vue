<template>
  <div id="view">
    <div id="list">
      <normal-game
        v-for="game in starredGames"
        :key="game"
        :game="game"
      ></normal-game>
      <span id="noItem" v-if="!starredGames">
        즐겨찾기된 게임이 없습니다!
      </span>
    </div>
  </div>
</template>

<script setup>
import NormalGame from "@/components/GameList/item/NormalGame";
import { apiInstance } from "@/api/index";
import { useStore } from "vuex";
import { ref, computed } from "vue";

const store = useStore();

const api = apiInstance();
const memberId = computed(() => store.state.memberStore.memberInfo.id).value;
const accessToken = computed(() => store.state.memberStore.accessToken).value;

const starredGames = ref();
const callApi = () => {
  api
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
    })
    .catch((error) => {
      console.log(error);
      // if (error.response.status == 401) {
      //   getAccessTokenByRefreshToken(); // refresh 토큰으로 다시
      // }
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
}
</style>
