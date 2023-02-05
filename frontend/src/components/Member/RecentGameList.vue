<template>
  <div id="view">
    <h1>최근 플레이 목록 입니다.</h1>
    <div id="list">
      <normal-game
        v-for="game in recentGames"
        :key="game"
        :game="game"
      ></normal-game>
    </div>
  </div>
</template>

<script setup>
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

store.dispatch("mypageStore/getRecentGameList", pageConditionReq);
const recentGames = computed(() => store.state.mypageStore.recentGameList);

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

#list {
  display: grid;
  gap: 35px 0;
  grid-template-columns: repeat(3, 1fr);
  width: 1090px;
  margin: 2%;
}
</style>
