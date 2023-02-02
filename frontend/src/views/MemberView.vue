<template>
  <navigation-bar-in-member-page />
  <router-view></router-view>
</template>

<script setup>
import NavigationBarInMemberPage from "@/components/common/NavigationBarInMemberPage";
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
      params: {
        order: "RECENT",
        period: "DAY",
        search: "MEMBER",
        keyword: "",
        page: "1",
      },
      headers: {
        "access-token":
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzUzMjI2OTgsImV4cCI6MTY3NTMyMzI5OH0.MyEjlj58rLiAWm7Ky8gxkI1ZA_zBR8i67JHjm71JQbU", // 변수로 가지고있는 AccessToken
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

store.dispatch("commonStore/setColorAsync", "default");
</script>

<style></style>
