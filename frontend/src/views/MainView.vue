<template>
  <div id="view">
    <div class="box">
      <div class="logo"><span>똑딱</span></div>
      <div class="form">
        <div class="input">게임 PIN</div>
        <div class="button">입력</div>
      </div>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
#view {
  background-color: #fdf8ec;
  height: calc(100vh - 95px + 1000px);
  position: relative;
}
.box {
  /* border: 1px solid red; */
  position: relative;
  position: absolute;
  left: 50%;
  top: calc((100vh - 95px) * 0.5);
  transform: translate(-50%, -50%);
}

.logo {
  font-family: "Gugi", cursive;
  font-size: 96px;
  text-align: center;
}

.form {
  width: 300px;
  font-size: 20px;
  background-color: #f9cf5c;
  border-radius: 10px;
  height: 200px;
  margin: 0 auto;
}

.input,
.button {
  border-radius: 10px;
  margin: 0 auto;
}

.input {
  background-color: white;
  width: 220px;
  height: 50px;
  line-height: 50px;
  text-align: center;
  position: relative;
  top: 45px;
}

.button {
  background-color: #333333;
  color: white;
  width: 220px;
  height: 50px;
  line-height: 50px;
  text-align: center;
  position: relative;
  top: 55px;
}
</style>
