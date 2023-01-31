<template>
  <div>
    <router-link to="/member/recentGame">최근 플레이</router-link> |
    <router-link to="/member/starGame">즐겨찾기</router-link> |
    <router-link to="/member/myGame">내가 만든 게임</router-link> |
    <router-link to="/member/myBestcut">나의 베스트 컷</router-link>
    <router-view></router-view>
  </div>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

store.dispatch("commonStore/setColorAsync", "default");
</script>

<style></style>
