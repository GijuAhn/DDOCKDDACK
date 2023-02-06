<template>
  <navigation-bar-in-member-page />
  <router-view></router-view>
</template>

<script setup>
import NavigationBarInMemberPage from "@/components/common/NavigationBarInMemberPage";
import { useStore } from "vuex";

const store = useStore();

store.dispatch("commonStore/setColorAsync", "default");
</script>

<style></style>
