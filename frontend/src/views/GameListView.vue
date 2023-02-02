<template>
  <content-header />
  <game-list></game-list>
  <div v-if="currentModal === `preview`" id="modal"></div>
</template>

<script setup>
import GameList from "@/components/GameList/GameList.vue";
import ContentHeader from "@/components/common/ContentHeader";

import { useStore } from "vuex";
import { computed } from "vue";

const currentModal = computed(() => store.state.commonStore.currentModal);

const store = useStore();

console.log(currentModal);

store.dispatch("commonStore/setColorAsync", "variant1");
</script>

<style scoped>
#modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: green;
  opacity: 0.5;
}
</style>
