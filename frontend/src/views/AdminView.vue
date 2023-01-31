<template>
  <div>
    <router-link to="/admin/game">게임 신고</router-link> |
    <router-link to="/admin/bestcut">베스트컷 신고</router-link>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: "AdminView",
};
</script>

<style></style>
