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

<script>
// import HelloWorld from "@/components/HelloWorld.vue";

export default {
  name: "HomeView",
  components: {
    // HelloWorld,
  },
};
</script>
