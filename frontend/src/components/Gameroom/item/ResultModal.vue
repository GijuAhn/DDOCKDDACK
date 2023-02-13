<template>
  <div class="white-bg">
    <div v-if="props.result.length">
      <div v-if="!isEnd">
        <img
          v-for="(e, index) in result"
          :key="e"
          :id="`rank${index}`"
          :src="`data:image/jpeg;base64, ${e.image}`"
          class="result"
        />
      </div>
      <div v-else>
        <user-video id="rank0" :stream-manager="props.winner[0]" />
        <user-video id="rank1" :stream-manager="props.winner[1]" />
        <user-video id="rank2" :stream-manager="props.winner[2]" />
      </div>
      <img :src="require(`@/assets/images/rank1.png`)" id="podium0" />
      <img :src="require(`@/assets/images/rank2.png`)" id="podium1" />
      <img :src="require(`@/assets/images/rank3.png`)" id="podium2" />
    </div>
    <div v-else>
      채점 중입니다.
      <div class="clock"></div>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from "vue";
import UserVideo from "@/components/Gameroom/item/UserVideo.vue";

const props = defineProps({
  round: Number,
  result: Object,
  isEnd: Boolean,
  winner: Object,
  publisher: Object,
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

.clock {
  border-radius: 60px;
  border: 3px solid #000;
  height: 80px;
  width: 80px;
  position: relative;

  top: 28%;
  top: -webkit-calc(50% - 43px);
  top: calc(50% - 43px);
  left: 35%;
  left: -webkit-calc(50% - 43px);
  left: calc(50% - 43px);
}
.clock:after {
  content: "";
  position: absolute;
  background-color: #000;
  top: 2px;
  left: 48%;
  height: 38px;
  width: 4px;
  border-radius: 5px;
  -webkit-transform-origin: 50% 97%;
  transform-origin: 50% 97%;
  -webkit-animation: grdAiguille 2s linear infinite;
  animation: grdAiguille 2s linear infinite;
}

@-webkit-keyframes grdAiguille {
  0% {
    -webkit-transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
  }
}

@keyframes grdAiguille {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.clock:before {
  content: "";
  position: absolute;
  background-color: #000;
  top: 6px;
  left: 48%;
  height: 35px;
  width: 4px;
  border-radius: 5px;
  -webkit-transform-origin: 50% 94%;
  transform-origin: 50% 94%;
  -webkit-animation: ptAiguille 12s linear infinite;
  animation: ptAiguille 12s linear infinite;
}

@-webkit-keyframes ptAiguille {
  0% {
    -webkit-transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
  }
}

@keyframes ptAiguille {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
