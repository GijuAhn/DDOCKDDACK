<template>
  <div id="view">
    <div id="list">
      <normal-game
        v-for="game in starredList"
        :key="game"
        :game="game"
      ></normal-game>
    </div>
  </div>
</template>

<script setup>
import NormalGame from "@/components/GameList/item/NormalGame";

import { useStore } from "vuex";
import { ref, computed } from "vue";

const store = useStore();

let pageConditionReq = ref({
  order: "RECENT",
  period: "ALL",
  search: "GAME",
  keyword: "",
  page: 1,
});

store.dispatch("mypageStore/getStarGameList", pageConditionReq);
const starredList = computed(() => store.state.mypageStore.starredList);

store.dispatch("commonStore/setMemberTabAsync", 1);
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
