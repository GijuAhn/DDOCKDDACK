<template>
  <div class="white-bg">
    {{ props.round }}

    <img
      v-for="e in result"
      :key="e"
      :src="`data:image/jpeg;base64, ${e.image}`"
    />
  </div>
</template>

<script setup>
import { defineProps } from "vue";

const props = defineProps({
  round: Number,
  result: Object,
});
</script>

<style scoped>
.white-bg {
  width: 100%;
  background: white;
  padding: 20px;
  width: 1020px;
  height: 560px;
  box-shadow: 0 4px 4px -2px gray;
  position: absolute;
  top: 250px;
  color: black;
  z-index: 1;
}
</style>
