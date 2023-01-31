<template>
  <game-list></game-list>
</template>

<script setup>
import GameList from "@/components/GameList/GameList.vue";

import { useStore } from "vuex";

const store = useStore();

store.dispatch("commonStore/setColorAsync", "variant1");
</script>

<style></style>
