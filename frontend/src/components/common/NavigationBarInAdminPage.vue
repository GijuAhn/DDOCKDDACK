<template>
  <div id="navbar">
    <my-profile />
    <div class="tab-info">
      <span :class="tab1">
        <router-link to="/admin/game">게임 신고</router-link></span
      >
      <span :class="tab2"
        ><router-link to="/admin/bestcut">베스트컷 신고</router-link></span
      >
    </div>
  </div>
</template>

<script setup>
import MyProfile from "@/components/common/MyProfile";

import { computed } from "vue";
import { useStore } from "vuex";

const store = useStore();

const tab1 = computed(() => store.state.commonStore.tab.admin[0]);
const tab2 = computed(() => store.state.commonStore.tab.admin[1]);
</script>

<style scoped>
#navbar {
  background-color: #fdf8ec;
}
a {
  text-decoration: none;
  line-height: 70px;
  margin: 40px;
  font-size: 24px;
  font-family: "NanumSquareRoundB";
  color: black;
}
.on {
  background-color: white;
  padding: 23px 0;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  border-top: 1px solid #c9c9c9;
  border-left: 1px solid #c9c9c9;
  border-right: 1px solid #c9c9c9;
  border-bottom: 1px solid white;
}
.off {
  background-color: #fdf8ec;
  padding: 23px 0;
}
.tab-info {
  height: 69px;
  border-bottom: 1px solid #c9c9c9;
}
.tab-info span {
  margin: 0 20px;
}
</style>
