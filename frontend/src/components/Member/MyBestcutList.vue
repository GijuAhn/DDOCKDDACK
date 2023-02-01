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

onCreated(() => {
  const callApi = () => {
    api
      .get(`/members/games`, {
        headers: {
          "access-token": access-token // 변수로 가지고있는 AccessToken
        },
      })
      .then((response) => {
        console.log(response);
        myGameList.value = response.data.content;
      })
      .catch((error) => {
        if(error.get("code") == 401){ //401 access 만료 에러가 뜨면 
          getAccessTokenByRefreshToken(); // refresh 토큰으로 다시 
          this.callApi(); //갱신된 access-token으로 다시 요청
        } else {
          console.log(error);
        }
      });
    }
})


//공통 메서드로 만들기 mixin??
const getByRefreshToken = () => { //AccessToken 만료되면 refresh로
  api
    .get(`/token/refresh`, {
      headers: {
        "refresh-token": refresh-token
      },
    })
    .then((response) => {
      console.log(response);
      myGameList.value = response.data.content;
    })
    .catch((error) => {
      console.log(error);
    });
}


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
