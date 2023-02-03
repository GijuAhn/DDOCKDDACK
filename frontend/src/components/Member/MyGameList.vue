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
  </div>
</template>

<script setup>
import NormalGame from "@/components/GameList/item/NormalGame";

import { useStore } from "vuex";
import { apiInstance } from "@/api/index";
import { ref } from "vue";

const api = apiInstance();
const store = useStore();
const myGames = ref();

const callApi = () => {
  console.log("myGameList PRINT");
  api
    .get(`/api/members/1/games`, {
      params: {
        order: "RECENT",
        period: "WEEK",
        search: "MEMBER",
        keyword: "",
        page: "1",
      },
      headers: {
        "access-token":
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzU0MDExODMsImV4cCI6MTY3NTQwOTgyM30.S1aYJDH4C4evRPKA5pO56MY5EM5pYz75VaHHYEnJJAk", // 변수로 가지고있는 AccessToken
      },
    })
    .then((response) => {
      console.log(response.data.content);
      myGames.value = response.data.content;
    })
    .catch((error) => {
      console.log(error.response.status);
      if (error.response.status != 401) {
        getAccessTokenByRefreshToken(); // refresh 토큰으로 다시
      }
    });
};

callApi();

// 공통 메서드로 만들기 mixin??
const getAccessTokenByRefreshToken = () => {
  //AccessToken 만료되면 refresh로
  api
    .get(`http://localhost:9999/token/refresh`, {
      headers: {
        "refresh-token":
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0Iiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzUzNDA3NTcsImV4cCI6MTY3NTM0MTM1N30.uibFshwPFV5W18KO1pkgcgO7UdYjhCUaK-n0GY1bodY",
      },
    })
    .then((response) => {
      console.log("access-MyGameList " + response.data.content.content);
      myGames.value = response.data.content;
    })
    .catch((error) => {
      //로그인 페이지로
      console.log(error);
      moveLoginPage();
    });
};

const moveLoginPage = () => {
  //AccessToken 만료되면 refresh로
  console.log("move Page");
};

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
</style>
