<template>
  <div id="view">
    <div id="list">
      <normal-game
        v-for="game in starredList"
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
const starredList = ref();

const callApi = () => {
  api
    .get(`/api/members/1/starred`, {
      headers: {
        "access-token":
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzU1MjIzODQsImV4cCI6MTY3NjI0MjM4NH0.6kuEJj0P3ybQ6Cu4AgtqLNf3HJxWGSYyJc9m6T7wvO0", // 변수로 가지고있는 AccessToken
      },
    })
    .then((response) => {
      console.log(response);
      starredList.value = response.data;
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
      console.log(response);
      starredList.value = response.data.content;
    })
    .catch((error) => {
      //로그인 페이지로
      console.log(error);
      moveLoginPage();
    });
};

const moveLoginPage = () => {
  //refreshToken도 만료되면 로그아웃
  console.log("move Page");
};

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
</style>
