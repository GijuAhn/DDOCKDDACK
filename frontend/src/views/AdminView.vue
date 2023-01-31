<template>
  <div>
    <router-link to="/admin/game">게임 신고</router-link> |
    <router-link to="/admin/bestcut">베스트컷 신고</router-link>
    <router-view></router-view>
  </div>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

store.dispatch("commonStore/setColorAsync", "default");
</script>

<style></style>
