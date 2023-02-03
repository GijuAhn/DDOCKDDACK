<template>
  <div>
    <img
      :src="`${GAMEIMAGES_PATH}/${props.game.gameId}/${props.game.thumbnail}`"
      alt="대표사진"
      width="200"
      height="200"
    />
    {{ props.game.gameTitle }}
    {{ props.game.gameDesc }}
    {{ props.game.popularity }}
    <button @click="createSession(props.game.gameId)">방 생성</button>
    <button>즐겨찾기</button>
    <button>베스트컷</button>
    <button>문제 미리보기</button>
    <button>신고</button>
  </div>
</template>

<script setup>
import { defineProps, onMounted } from "vue";
import { apiInstance } from "@/api/index";
import router from "@/router/index.js";
import process from "process";

const props = defineProps(["game"]);
const api = apiInstance();
const GAMEIMAGES_PATH = process.env.VUE_APP_GAMEIMAGES_PATH;

onMounted(() => {
  console.log(process.env);
});

const createSession = (gameId) => {
  api.post("/api/game-rooms", { gameId }).then((res) => {
    router.replace(`/gameroom/${res.data}`);
  });
};
// console.log(props.game);
</script>

<style></style>
