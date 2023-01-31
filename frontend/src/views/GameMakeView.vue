<template>
  <div>
    <router-view></router-view>
  </div>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

store.dispatch("commonStore/setColorAsync", "variant2");
</script>

<style></style>
