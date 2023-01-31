<template>
  <div>안녕~</div>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

store.dispatch("commonStore/setColorAsync", "variant3");
</script>

<style></style>
