<template>
  <div id="view"><h1>최근 플레이 목록 입니다.</h1></div>
</template>

<script setup>
import { useStore } from "vuex";

import { apiInstance } from "@/api/index";
import { ref } from "vue";

const api = apiInstance();
const store = useStore();
const starredList = ref();

const callApi = () => {
  api
    .get(`/api/members/1/records`, {
      headers: {
        "access-token":
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzU0MDExODMsImV4cCI6MTY3NTQwOTgyM30.S1aYJDH4C4evRPKA5pO56MY5EM5pYz75VaHHYEnJJAk", // 변수로 가지고있는 AccessToken
      },
    })
    .then((response) => {
      console.log(response);
      starredList.value = response.data.content;
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
