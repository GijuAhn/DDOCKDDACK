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
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzUzMTcwNjgsImV4cCI6MTY3NTMyMDA2OH0.akKr_lBhfIKu-1zwqGn-wD-3mcEsUKDeFjah8ccoE0U", // 변수로 가지고있는 AccessToken
      },
    })
    .then((response) => {
      console.log(response);
      myGameList.value = response.data.content;
    })
    .catch((error) => {
      if (error.get("code") == 401) {
        //401 access 만료 에러가 뜨면
        // getAccessTokenByRefreshToken(); // refresh 토큰으로 다시
        // this.callApi(); //갱신된 access-token으로 다시 요청
      } else {
        console.log(error);
      }
    });
};

callApi();

//공통 메서드로 만들기 mixin??
// const getAccessTokenByRefreshToken = () => {
//   //AccessToken 만료되면 refresh로
//   api
//     .get(`/token/refresh`, {
//       headers: {
//         "refresh-token":
//           "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzUzMDI1NzQsImV4cCI6MTY3NjUxMjE3NH0.cCNUNiVX52feLQbLjqnck-lrScXGmDOTxE8Zt5tCdtU",
//       },
//     })
//     .then((response) => {
//       console.log(response);
//       myGameList.value = response.data.content;
//     })
//     .catch((error) => {
//       console.log(error);
//     });
// };

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
