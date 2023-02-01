<template>
  <navigation-bar-in-admin-page />
  <router-view></router-view>
</template>

<script setup>
import NavigationBarInAdminPage from "@/components/common/NavigationBarInAdminPage";
import { useStore } from "vuex";

const store = useStore();

store.dispatch("commonStore/setColorAsync", "default");
</script>

<style></style>
