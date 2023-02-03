<template>
  <img alt="Vue logo" src="../assets/logo.png" />
  <div>
    <label>PIN</label>
    <input v-model="pinNumber" class="form-control" type="text" required />
  </div>
  <div>
    <button class="btn btn-lg btn-success" @click="join(pinNumber)">
      Join!
    </button>
  </div>
</template>

<script setup>
import { ref } from "vue";
import router from "@/router/index.js";

const pinNumber = ref(undefined);

const join = (pinNumber) => {
  router.replace(`/gameroom/${pinNumber.trim()}`);
};
</script>
