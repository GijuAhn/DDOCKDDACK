<template>
  <div id="view">
    <div id="list">
      <normal-bestcut
        v-for="besctcut in bestcuts"
        :key="besctcut"
        :besctcut="besctcut"
      ></normal-bestcut>
    </div>
  </div>
</template>

<script setup>
import NormalBestcut from "@/components/BestcutList/item/NormalBestcut";

import { useStore } from "vuex";
import { computed } from "vue";

const store = useStore();

const userid = computed(() => store.state.memberStore.memberInfo.id).value;
const pageConditionReq = {
  order: "RECENT",
  period: "ALL",
  search: "MEMBER",
  keyword: "",
  page: 1,
};

store.dispatch("mypageStore/getMyBestcutList", { pageConditionReq, userid });

// const myBestcuts = JSON.parse(
//   JSON.stringify(computed(() => store.state.mypageStore.myBestcutList).value)
// );

const bestcuts = computed(() => store.getters.myBestcutList);

console.log("@#@#@", bestcuts.value);

// console.log("mybestcuts!! ", this.myBestcuts);

// const memberId = 1;
// const callApi = () => {
//   console.log("베스트 컷!");
//   api
//     .get(`/api/members/1/bestcuts`, {
//       params: {
//         order: "RECENT",
//         period: "WEEK",
//         search: "MEMBER",
//         keyword: "",
//         page: "1",
//       },
//       headers: {
//         "access-token":
//           "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzU1MjIzODQsImV4cCI6MTY3NjI0MjM4NH0.6kuEJj0P3ybQ6Cu4AgtqLNf3HJxWGSYyJc9m6T7wvO0", // 변수로 가지고있는 AccessToken
//       },
//     })
//     .then((response) => {
//       console.log("access-MyBestcut: " + response.data);
//       myBestcuts.value = response.data.content;
//     })
//     .catch((error) => {
//       console.log(error);
//       if (error.response.status == 401) {
//         getAccessTokenByRefreshToken(); // refresh 토큰으로 다시
//       }
//     });
// };

// callApi();

//공통 메서드로 만들기 mixin??
// const getAccessTokenByRefreshToken = () => {
//   //AccessToken 만료되면 refresh로
//   api
//     .get(`/token/refresh`, {
//       headers: {
//         "refresh-token":
//           "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzUzMjI2OTgsImV4cCI6MTY3NTMyMzI5OH0.MyEjlj58rLiAWm7Ky8gxkI1ZA_zBR8i67JHjm71JQbU",
//       },
//     })
//     .then((response) => {
//       console.log("refresh-MyBestcut: " + response);
//       myBestcuts.value = response.data.content;
//     })
//     .catch((error) => {
//       console.log(error);
//       moveLoginPage();
//     });
// };

// const moveLoginPage = () => {
//   //refreshToken도 만료되면 로그아웃
//   console.log("move Page");
// };

store.dispatch("commonStore/setMemberTabAsync", 3);
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
