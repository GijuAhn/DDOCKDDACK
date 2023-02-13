<template>
  <div class="white-bg">
    <img
      v-for="(e, index) in result"
      :key="e"
      :id="`rank${index}`"
      :src="`data:image/jpeg;base64, ${e.image}`"
      class="result"
    />
  </div>
</template>

<script setup>
import { ref } from "vue";
import { computed } from "vue";
import { useStore } from "vuex";

const store = useStore();
const currentModal = computed(() => store.state.commonStore.currentModal);
// const round = ref(currentModal.value.data[0]);
const result = ref(currentModal.value.data[1]);
</script>

<style scoped>
.white-bg {
  background-color: white;
  width: 1300px;
  height: 700px;
  border-radius: 10px;
  position: relative;
}
img {
  width: 350px;
  height: 350px;
  object-fit: cover;
  position: absolute;
  border-radius: 15px;
}
#rank0 {
  top: 40%;
  left: 50%;
  transform: translate(-50%, -40%);
  border: 15px solid #fbd600;
}
#rank1 {
  top: 60%;
  left: 50px;
  transform: translate(0, -60%);
  border: 15px solid #c1c1c1;
}
#rank2 {
  top: 60%;
  right: 50px;
  transform: translate(0, -60%);
  border: 15px solid #f3b251;
}
</style>
