<template>
  <div v-if="game">
    <div id="previewImages">
      <img
        v-for="image in game.images"
        :key="image"
        :src="`${GAMEIMAGES_PATH}/${game.gameId}/${image.gameImage}`"
        width="120"
        height="120"
      />
    </div>
  </div>
</template>

<script setup>
import { useStore } from "vuex";
import process from "process";

const GAMEIMAGES_PATH = process.env.VUE_APP_GAMEIMAGES_PATH;

const store = useStore();

import { computed, ref } from "vue";

const currentModal = computed(() => store.state.commonStore.currentModal);

import { apiInstance } from "@/api/index";

const api = apiInstance();

const game = ref();

api
  .get(`/api/games/${currentModal.value.data.gameId}`)
  .then((response) => {
    console.log(response.data);
    game.value = response.data;
  })
  .catch((error) => {
    console.log(error);
  });
</script>

<style scoped>
#previewImages {
  background-color: white;
  display: grid;
  grid-template-rows: repeat(4, 1fr);
  grid-template-columns: repeat(5, 1fr);
  padding: 5px;
}
img {
  margin: 2px;
}
</style>
