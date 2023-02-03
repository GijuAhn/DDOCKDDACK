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
    .get(`/api/members/1`, {
      headers: {
        "access-token":
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzU0MDExODMsImV4cCI6MTY3NTQwOTgyM30.S1aYJDH4C4evRPKA5pO56MY5EM5pYz75VaHHYEnJJAk", // 변수로 가지고있는 AccessToken
      },
    })
    .then((response) => {
      console.log(response);
      myGameList.value = response.data.content;
    })
    .catch((error) => {
      console.log(error);
      if (error.response.status != 401) {
        // getAccessTokenByRefreshToken(); // refresh 토큰으로 다시
      }
    });
};

callApi();

// 공통 메서드로 만들기 mixin??
// const getAccessTokenByRefreshToken = () => {
//   //AccessToken 만료되면 refresh로
//   api
//     .get(`http://localhost:9999/token/refresh`, {
//       headers: {
//         "refresh-token":
//           "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzUzODkyMDksImV4cCI6MTY3NTM5Nzg0OX0.RJOrSdRvTzkfZUNC-QBFj9gf000oPWwVpZU3krFsgnc",
//       },
//     })
//     .then((response) => {
//       console.log(response);
//       myGameList.value = response.data.content;
//     })
//     .catch((error) => {
//       //로그인 페이지로
//       console.log(error);
//       moveLoginPage();
//     });
// };

// const moveLoginPage = () => {
//   //AccessToken 만료되면 refresh로
//   console.log("move Page");
// };

store.dispatch("commonStore/setColorAsync", "default");
</script>

<style></style>
