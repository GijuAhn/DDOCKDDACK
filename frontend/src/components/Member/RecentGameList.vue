<template>
  <div id="view"><h1>최근 플레이 목록 입니다.</h1></div>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

store.dispatch("commonStore/setMemberTabAsync", 0);
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
