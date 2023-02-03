<template>
  <div v-if="game">
    <img
      v-for="image in game.images"
      :key="image"
      :src="require(`@/../../Backend/images/${game.gameId}/${image.gameImage}`)"
      width="120"
      height="120"
    />
    <!-- <div v-for="image in game.images" :key="image">{{ image }}</div> -->
  </div>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

import { computed, ref } from "vue";

const currentModal = computed(() => store.state.commonStore.currentModal);

import { apiInstance } from "@/api/index";

const api = apiInstance();

const game = ref();

api
  .get(`/games/${currentModal.value.data.gameId}`)
  .then((response) => {
    console.log(response.data);
    game.value = response.data;
  })
  .catch((error) => {
    console.log(error);
  });
</script>

<style scoped></style>
