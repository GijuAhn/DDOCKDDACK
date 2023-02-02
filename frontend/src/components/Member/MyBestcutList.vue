<template>
  <div id="view"><h1>나의 베스트 컷 목록 입니다.</h1></div>
</template>

<script setup>
import { useStore } from "vuex";
import { apiInstance } from "@/api/index";
import { ref } from "vue";

const api = apiInstance();
const store = useStore();
const myGameList = ref();
// const memberId = 1;
const callApi = () => {
  api
    .get(`/members/1/bestcuts`, {
      params: {
        order: "RECENT",
        period: "DAY",
        search: "MEMBER",
        keyword: "",
        page: "1",
      },
      headers: {
        "access-token":
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzUzMjY3OTQsImV4cCI6MTY3NTMyNzM5NH0.qc1XXQLZaUpcj0HMQdBqVcjJtfl1mVJwKgeonr-QAbU", // 변수로 가지고있는 AccessToken
      },
    })
    .then((response) => {
      console.log(response);
      myGameList.value = response.data.content;
    })
    .catch((error) => {
      if (error.response.status == 401) {
        getAccessTokenByRefreshToken(); // refresh 토큰으로 다시
      }
    });
};

callApi();

//공통 메서드로 만들기 mixin??
const getAccessTokenByRefreshToken = () => {
  //AccessToken 만료되면 refresh로
  api
    .get(`/token/refresh`, {
      headers: {
        "refresh-token":
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzUzMjI2OTgsImV4cCI6MTY3NTMyMzI5OH0.MyEjlj58rLiAWm7Ky8gxkI1ZA_zBR8i67JHjm71JQbU",
      },
    })
    .then((response) => {
      console.log(response);
      myGameList.value = response.data.content;
    })
    .catch((error) => {
      console.log(error);
    });
};

store.dispatch("commonStore/setMemberTabAsync", 3);
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
