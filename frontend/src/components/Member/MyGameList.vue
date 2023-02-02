<template>
  <div id="view"><h1>내가 만든 게임 목록 입니다.</h1></div>
</template>

<script setup>
import { useStore } from "vuex";
import { apiInstance } from "@/api/index";
import { ref } from "vue";

const api = apiInstance();
const store = useStore();
const myGameList = ref();

const callApi = () => {
  console.log("myGameList PRINT");
  api
    .get(`/members/1/games`, {
      headers: {
        "access-token": "access-token!", // 변수로 가지고있는 AccessToken
      },
    })
    .then((response) => {
      console.log(response);
      myGameList.value = response.data.content;
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
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzUzMTQ1NDEsImV4cCI6MTY3NjUyNDE0MX0.SUIUrChHBtf4j70z8T4-kUXyD9MmEJoJArG6vcIAnM8",
      },
    })
    .then((response) => {
      console.log(response);
      myGameList.value = response.data.content;
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
  border: 2px solid black;
  width: 1200px;
  position: relative;
  left: 50%;
  transform: translate(-50%, 0);
  background-color: white;
}
</style>
