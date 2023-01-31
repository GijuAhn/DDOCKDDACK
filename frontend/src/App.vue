<template>
  <navigation-bar />
  <router-view />
</template>

<script setup>
import NavigationBar from "@/components/common/NavigationBar";
</script>

<style>
@font-face {
  font-family: "NanumSquareRoundR";
  src: url("@/assets/fonts/NanumSquareRoundR.ttf") format("truetype");
}

@font-face {
  font-family: "NanumSquareRoundB";
  src: url("@/assets/fonts/NanumSquareRoundB.ttf") format("truetype");
}

@font-face {
  font-family: "NanumSquareRoundEB";
  src: url("@/assets/fonts/NanumSquareRoundEB.ttf") format("truetype");
}

@font-face {
  font-family: "Gugi-Regular";
  src: url("@/assets/fonts/Gugi-Regular.ttf") format("truetype");
}

#app {
  font-family: "NanumSquareRoundR";
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

* {
  margin: 0;
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
