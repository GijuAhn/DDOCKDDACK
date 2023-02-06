<template>
  <div id="view">
    <div id="list">
      <normal-bestcut
        v-for="besctcut in myBestcuts"
        :key="besctcut"
        :besctcut="besctcut"
      ></normal-bestcut>
    </div>
  </div>
</template>

<script setup>
import NormalBestcut from "@/components/BestcutList/item/NormalBestcut";
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

const memberId = computed(() => store.state.memberStore.memberInfo.id).value;

const api = apiInstance();
const accessToken = computed(() => store.state.memberStore.accessToken).value;

const myBestcuts = ref();
const callApi = () => {
  console.log("베스트 컷!");
  api
    .get(`/api/members/${memberId}/bestcuts`, {
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
      console.log("access-MyBestcut: ", response.data.content);
      myBestcuts.value = response.data.content;
    })
    .catch((error) => {
      console.log(error);
      // if (error.response.status == 401) {
      //   getAccessTokenByRefreshToken(); // refresh 토큰으로 다시
      // }
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
