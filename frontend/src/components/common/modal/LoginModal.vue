<template>
  <div>
    <div id="login-modal">
      <div id="close-button" @click="setCurrentModalAsync">
        <img
          :src="require(`@/assets/images/close-button.svg`)"
          width="18"
          height="18"
        />
      </div>

      <div id="game-title">
        <span>똑딱</span>
      </div>
      <div id="login-button">
        <img :src="require(`@/assets/images/kakao-login-btn.png`)" />
        <img :src="require(`@/assets/images/google-login-btn.png`)" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

const setCurrentModalAsync = (value) => {
  if (value === "then") {
    store.dispatch("commonStore/setCurrentModalAsync", {
      name: "reportComplete",
      data: "",
    });
  } else {
    store.dispatch("commonStore/setCurrentModalAsync", "");
  }
};
</script>

<style scoped>
#login-modal {
  background-color: white;
  width: 377px;
  height: 426px;
  border-radius: 10px;
  text-align: center;
}

#login-modal span {
  font-family: "Gugi-Regular";
  font-size: 96px;
}

#close-button {
  text-align: right;
  margin-right: 13px;
}
</style>
