<template>
  <div class="white-bg">
    {{ props.round }}

    <img
      v-for="(e, index) in result"
      :key="e"
      :id="`rank${index}`"
      :src="`data:image/jpeg;base64, ${e.image}`"
      class="result"
    />
    <img :src="require(`@/assets/images/rank1.png`)" id="podium0" />
    <img :src="require(`@/assets/images/rank2.png`)" id="podium1" />
    <img :src="require(`@/assets/images/rank3.png`)" id="podium2" />
  </div>
</template>

<script setup>
import { defineProps } from "vue";

const props = defineProps({
  round: Number,
  result: Object,
});
</script>

<style scoped>
.white-bg {
  width: 100%;
  height: 60%;
  background: white;
  padding: 20px;
  box-shadow: 0 4px 4px -2px gray;
  position: absolute;
  color: black;
  top: 200px;
  z-index: 1;
}

.result {
  width: 320px;
  height: 240px;
}

#rank0 {
  position: absolute;
  left: 800px;
}

#rank1 {
  position: absolute;
  left: 400px;
  top: 150px;
}

#rank2 {
  position: absolute;
  left: 1200px;
  top: 280px;
}

#podium0 {
  position: absolute;
  left: 800px;
  top: 270px;
}

#podium1 {
  position: absolute;
  left: 400px;
  top: 400px;
}

#podium2 {
  position: absolute;
  left: 1200px;
  top: 530px;
}
</style>
