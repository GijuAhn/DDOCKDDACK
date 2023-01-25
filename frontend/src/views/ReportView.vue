<template>
  <div>
    <router-link to="/report/game">게임 신고</router-link> |
    <router-link to="/report/bestcut">베스트컷 신고</router-link>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: "ReportView",
};
</script>

<style></style>
