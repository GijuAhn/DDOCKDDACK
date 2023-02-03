<template>
  <div>
    <content-header />
    <game-list></game-list>
    <div v-if="currentModal.length !== 0" id="modal0">
      <div id="modal" @click="test2"></div>
      <div v-if="currentModal.name === `preview`" id="modal2">
        {{ currentModal.data.gameId }}
      </div>
    </div>
  </div>
</template>

<script setup>
import GameList from "@/components/GameList/GameList.vue";
import ContentHeader from "@/components/common/ContentHeader";

import { useStore } from "vuex";

const store = useStore();

store.dispatch("commonStore/setColorAsync", "variant1");

import { computed } from "vue";

const currentModal = computed(() => store.state.commonStore.currentModal);

const test2 = () => {
  store.dispatch("commonStore/setCurrentModalAsync", "");
};
</script>

<style scoped>
#modal0 {
  position: fixed;
  min-width: calc(1920px - 100px);
  width: 100%;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}
#modal {
  width: 100%;
  height: 100%;
  background-color: black;
  opacity: 0.5;
}
#modal2 {
  background-color: white;
  position: absolute;
  width: 500px;
  height: 500px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
