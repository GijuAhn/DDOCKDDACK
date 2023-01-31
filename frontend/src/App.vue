<template>
  <nav>
    <router-link to="/">메인</router-link> |
    <router-link to="/gameList">게임 목록</router-link> |
    <router-link to="/gameMake">게임 만들기</router-link> |
    <router-link to="/bestcut">베스트컷</router-link> |
    <router-link to="/gameroom">게임 룸</router-link> |
    <router-link to="/member">마이 페이지</router-link> |
    <router-link to="/admin">관리자 페이지</router-link>
  </nav>
  <router-view />
</template>

<style>
@font-face {
  font-family: "NanumSquareRound";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_two@1.0/NanumSquareRound.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}

#app {
  font-family: "NanumSquareRound";
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
} */
</style>
