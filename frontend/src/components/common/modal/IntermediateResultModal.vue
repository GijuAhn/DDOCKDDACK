<template>
  <div class="white-bg">
    <div v-if="result.length">
      <div v-if="!isEnd" id="rankContainer">
        <div id="box1">
          <div>
            <span>{{ rankTitle[1] }}</span>
          </div>
          <div v-if="result[1]">
            <img
              id="rank1"
              :src="`data:image/jpeg;base64, ${result[1].image}`"
            />
          </div>
          <div v-if="result[1]"><span>닉네임</span></div>
        </div>
        <div id="box0">
          <div>
            <span>{{ rankTitle[0] }}</span>
          </div>
          <div v-if="result[0]">
            <img
              id="rank0"
              :src="`data:image/jpeg;base64, ${result[0].image}`"
            />
          </div>
          <div v-if="result[0]"><span>닉네임</span></div>
        </div>
        <div id="box2">
          <div>
            <span>{{ rankTitle[2] }}</span>
          </div>
          <div v-if="result[2]">
            <img
              id="rank2"
              :src="`data:image/jpeg;base64, ${result[2].image}`"
            />
          </div>
          <div v-if="result[2]"><span>닉네임</span></div>
        </div>
      </div>
      <div v-else>
        <user-video id="rank0" :stream-manager="winner[0]" />
        <user-video id="rank1" :stream-manager="winner[1]" />
        <user-video id="rank2" :stream-manager="winner[2]" />
      </div>
    </div>
    <div v-else>
      채점 중입니다.
      <div class="clock"></div>
    </div>
  </div>
  <!-- <div class="white-bg">
    <div v-if="true">
      <div v-if="true" id="rankContainer">
        <div v-for="(e, index) in 3" :key="e" :id="`box${index}`">
          <div>
            <span>{{ rankTitle[index] }}</span>
          </div>
          <div>
            <img
              :id="`rank${index}`"
              src="https://source.unsplash.com/category/portrait/"
            />
          </div>
          <div><span>닉네임</span></div>
        </div>
      </div>
    </div>
  </div> -->
</template>

<script setup>
import { ref } from "vue";
import { computed } from "vue";
import { useStore } from "vuex";

const store = useStore();
const currentModal = computed(() => store.state.commonStore.currentModal);
// const round = ref(currentModal.value.data[0]);
const result = ref(currentModal.value.data[1]);
const isEnd = ref(currentModal.value.data[2]);
const winner = ref(currentModal.value.data[3]);
// const publisher = ref(currentModal.value.data[4]);

const rankTitle = ["1st", "2nd", "3rd"];
</script>

<style scoped>
.white-bg {
  background-color: white;
  width: 1300px;
  height: 750px;
  border-radius: 10px;
  position: relative;
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
#rankContainer {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  height: 750px;
}
#rankContainer > div {
  width: 400px;
  height: 700px;
  border: 2px solid black;
  position: relative;
  box-shadow: 0 0 10px #8b8b8b;
}
#box0 {
  background-color: #fbd600;
}
#box1 {
  background-color: #c1c1c1;
}
#box2 {
  background-color: #f3b251;
}
#rankContainer > div > * {
  left: 50%;
  transform: translateX(-50%);
  position: absolute;
}
#rankContainer > div > div:nth-child(1) {
  top: 50px;
}
#rankContainer > div > div:nth-child(1) > span {
  font-family: Gugi-Regular;
  text-shadow: -2px 0 #000, 0 2px #000, 2px 0 #000, 0 -2px #000;
  color: white;
  font-size: 80px;
}
#rankContainer > div > div:nth-child(2) {
  top: 170px;
}
#rankContainer > div > div:nth-child(3) {
  top: 585px;
}
#rankContainer > div > div:nth-child(3) > span {
  font-size: 24px;
  border: 2px solid black;
  background-color: white;
  width: 200px;
  height: 50px;
  display: inline-block;
  border-radius: 50px;
  text-align: center;
  line-height: 50px;
}

img {
  width: 350px;
  height: 350px;
  object-fit: cover;
  border: 2px solid black;
}
</style>
