<template>
  <div>
    <img
      :src="
        require(`@/../../Backend/images/${props.game.gameId}/${props.game.thumbnail}`)
      "
      alt="대표사진"
      width="200"
      height="200"
    />
    {{ props.game.gameTitle }}
    {{ props.game.gameDesc }}
    {{ props.game.popularity }}
    <button>방 생성</button>
  </div>
</template>

<script setup>
import { defineProps } from "vue";

const props = defineProps(["game"]);

// console.log(props.game);
</script>

<style></style>
