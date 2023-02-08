<template>
  <div id="view">
    <h1>최근 플레이 목록 입니다.</h1>
    <div id="list">
      <normal-game
        v-for="game in recentGames"
        :key="game"
        :game="game"
      ></normal-game>
    </div>
  </div>
</template>

<script setup>
import { apiInstance } from "@/api/index";
import { useStore } from "vuex";
import { ref, computed } from "vue";

const store = useStore();

let pageConditionReq = ref({
  order: "RECENT",
  period: "ALL",
  search: "MEMBER",
  keyword: "",
  page: 1,
});

const api = apiInstance();
const memberId = computed(() => store.state.memberStore.memberInfo.id).value;
const accessToken = computed(() => store.state.memberStore.accessToken).value;

const myGames = ref();
const callApi = () => {
  api
    .get(`/api/members/${memberId}/records`, {
      params: {
        order: pageConditionReq.value.order,
        period: pageConditionReq.value.period,
        search: pageConditionReq.value.search,
        keyword: pageConditionReq.value.keyword,
        page: pageConditionReq.value.page,
      },
      headers: {
        "access-token": accessToken, // 변수로 가지고있는 AccessToken
      },
    })
    .then((response) => {
      // console.log("access-games: ", response.data.content);
      myGames.value = response.data.content;
    })
    .catch((error) => {
      console.log(error);
      // if (error.response.status == 401) {
      //   getAccessTokenByRefreshToken(); // refresh 토큰으로 다시
      // }
    });
};

callApi();

store.dispatch("commonStore/setMemberTabAsync", 0);
</script>

<style scoped>
#view {
  border: 2px solid black;
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
</style>
