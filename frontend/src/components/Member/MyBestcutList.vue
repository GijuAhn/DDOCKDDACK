<template>
  <div id="view">
    <div id="list">
      <normal-bestcut
        v-for="bestcut in myBestcuts"
        :key="bestcut"
        :bestcut="bestcut"
      ></normal-bestcut>
      <span id="noItem" v-if="!myBestcuts"> 베스트컷을 등록 해주세요! </span>
    </div>
  </div>
</template>

<script setup>
import NormalBestcut from "@/components/BestcutList/item/NormalBestcut";
import { apiInstance } from "@/api/index";
import { useStore } from "vuex";
import { ref, computed } from "vue";

const store = useStore();

const memberId = computed(() => store.state.memberStore.memberInfo.id).value;

const api = apiInstance();
const accessToken = computed(() => store.state.memberStore.accessToken).value;

const myBestcuts = ref();
const callApi = () => {
  api
    .get(`/api/members/${memberId}/bestcuts`, {
      headers: {
        "access-token": accessToken, // 변수로 가지고있는 AccessToken
      },
    })
    .then((response) => {
      // console.log("access-bestcuts: ", response.data.content);
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
#noItem {
  font-size: 20px;
}
</style>
