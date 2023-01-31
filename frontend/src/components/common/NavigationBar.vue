<template>
  <div id="navbar" :class="view">
    <span class="left">
      <router-link to="/">똑딱</router-link>
    </span>
    <span class="mid">
      <router-link to="/gameList">게임 목록</router-link>
      <router-link to="/gameMake">게임 만들기</router-link>
      <router-link to="/bestcut">베스트 컷</router-link>
    </span>
    <span class="right">
      <router-link to="/member">마이 페이지</router-link>
      <router-link to="/admin">관리자 페이지</router-link>
    </span>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useStore } from "vuex";

const store = useStore();

const view = computed(() => store.state.commonStore.view);
</script>

<style scoped>
#navbar {
  height: 95px;
  position: relative;
}
.default {
  background-color: #fdf8ec;
}
.variant1 {
  background-color: #f87c7b;
}
.variant2 {
  background-color: #ffb800;
}
.variant3 {
  background-color: #77a4cc;
}
.default a {
  color: black;
}
.variant1 a,
.variant2 a,
.variant3 a {
  color: white;
}
a {
  text-decoration: none;
  line-height: 95px;
  margin: 40px;
}
.left a {
  font-family: "Gugi-Regular";
  font-size: 48px;
}
.mid a {
  font-size: 24px;
  font-family: "NanumSquareRoundEB";
}
.right a {
  font-size: 24px;
  font-family: "NanumSquareRoundEB";
}

.left {
  float: left;
}

.mid {
  text-align: center;
  position: absolute;
  left: 50%;
  transform: translate(-50%, 0);
}

.right {
  float: right;
}
</style>
