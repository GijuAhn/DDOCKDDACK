<template>
  <div class="navigation-bar">
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

<script setup></script>

<style scoped>
.navigation-bar {
  background-color: #fdf8ec;
  height: 95px;
}
a {
  text-decoration: none;
  color: black;
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
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.right {
  float: right;
}
</style>
