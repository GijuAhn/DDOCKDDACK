<template>
  <!-- <div class="white-bg">
    <div v-if="result.length">
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
        <user-video id="rank0" :stream-manager="winner[0]" />
        <user-video id="rank1" :stream-manager="winner[1]" />
        <user-video id="rank2" :stream-manager="winner[2]" />
      </div>
    </div>
    <div v-else>
      채점 중입니다.
      <div class="clock"></div>
    </div>
  </div> -->
  <div class="white-bg">
    <div v-if="true">
      <div v-if="true">
        <div v-for="(e, index) in 3" :key="e">
          <img
            :id="`rank${index}`"
            src="https://source.unsplash.com/category/portrait/"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// import { ref } from "vue";
// import { computed } from "vue";
// import { useStore } from "vuex";

// const store = useStore();
// const currentModal = computed(() => store.state.commonStore.currentModal);
// // const round = ref(currentModal.value.data[0]);
// const result = ref(currentModal.value.data[1]);
// const isEnd = ref(currentModal.value.data[2]);
// const winner = ref(currentModal.value.data[3]);
// // const publisher = ref(currentModal.value.data[4]);
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
