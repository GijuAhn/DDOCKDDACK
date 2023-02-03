<template>
  <div>
    <div v-if="props.streamManager">
      <span v-if="props.isStart && !props.isEnd">
        <img
          :src="`http://localhost:9999/static/images/${props.room.gameId}/${
            props.room.gameImages[round - 1].gameImage
          }`"
        />
      </span>
      <div class="parent">
        <ov-video
          class="child1"
          :class="{ blinking: resultMode }"
          :stream-manager="props.streamManager"
        />
      </div>

      <div>
        <p>{{ clientData }}</p>
      </div>
    </div>
    <br />
    <br />
    <div v-if="props.isEnd">
      <button @click="getMyImages">결과보기</button>
    </div>

    <div v-if="isShow">
      <div v-for="(image, index) in resultImages" :key="index">
        <div>
          <input
            type="checkbox"
            :value="index"
            @change="check(index)"
          />체크박스
          <br />
          <input
            id="bestcutTitle"
            type="text"
            v-model="inputs[index]"
            placeholder="제목을 입력하세요"
          />
          <img
            :src="`http://localhost:9999/static/images/${props.room.gameId}/${props.room.gameImages[index].gameImage}`"
          />
          <img :src="image" id="bestcutImg" /> <br />
        </div>
      </div>

      <button @click="upload">베스트 컷 게시</button>
    </div>
  </div>
</template>

<script setup>
import OvVideo from "./OvVideo";
import html2canvas from "html2canvas";
import { apiInstance } from "@/api/index";
import { computed, defineProps, watch, ref, onMounted } from "vue";

const api = apiInstance();
const props = defineProps({
  streamManager: Object,
  timerCount: Number,
  isEnd: Boolean,
  isStart: Boolean,
  round: Number,
  room: Object,
  resultMode: Boolean,
});

const resultImages = ref([]);
const bestcutSaveReq = ref({
  pinNumber: undefined,
  sessionId: undefined,
  gameTitle: undefined,
  images: [],
});
const inputs = ref([]);
const isChecked = ref([]);
const rankingImage = ref([]);
const isShow = ref(false);

const clientData = computed(() => {
  const { clientData } = getConnectionData();
  return clientData;
});

onMounted(() => {
  init();
});

watch(
  () => props.timerCount,
  (value) => {
    if (value === 0) {
      capture();
    }
  },
  { immediate: true }
);

const capture = () => {
  let me = document.getElementById("local-video-undefined");
  html2canvas(me).then((canvas) => {
    let myImg;
    const sessionId = props.streamManager.session.connection.connectionId;
    const pinNumber = props.streamManager.session.sessionId;
    myImg = canvas.toDataURL("image/jpeg");
    let byteString = myImg.replace("data:image/jpeg;base64,", "");

    let param = {
      memberGameImage: byteString,
    };
    api.post(`/game-rooms/${pinNumber}/${sessionId}/images`, param);

    resultImages.value.push(myImg);
  });
};

const getConnectionData = () => {
  const { connection } = props.streamManager.stream;
  return JSON.parse(connection.data);
};

const init = () => {
  resultImages.value = [];
  isShow.value = false;
  bestcutSaveReq.value = {
    pinNumber: undefined,
    sessionId: undefined,
    gameTitle: undefined,
    images: [],
  };
  inputs.value = ["", "", "", "", "", "", "", "", "", ""];
  isChecked.value = [
    false,
    false,
    false,
    false,
    false,
    false,
    false,
    false,
    false,
    false,
  ];
  rankingImage.value = [];
};

const getMyImages = () => {
  isShow.value = true;
};

const check = (index) => {
  isChecked.value[index] = !isChecked.value[index];
};

const upload = () => {
  bestcutSaveReq.value.pinNumber = props.streamManager.session.sessionId;
  bestcutSaveReq.value.sessionId =
    props.streamManager.session.connection.connectionId;
  bestcutSaveReq.value.gameTitle = props.room.gameTitle;
  isChecked.value.forEach((element, index) => {
    if (element) {
      bestcutSaveReq.value.images.push({
        bestcutIndex: index,
        bestcutImgTitle: inputs.value[index],
        gameImgUrl: props.room.gameImages[index].gameImage,
        gameImgDesc: props.room.gameImages[index].gameImageDesc,
      });
    }
  });
  api
    .post("/bestcuts", bestcutSaveReq.value)
    .then(() => {
      this.$swal({
        text: "업로드가 완료 되었습니다.",
      });
    })
    .catch((err) => {
      err;
      this.$swal({
        icon: "error",
        title: "Oops...",
        text: "업로드 실패 ㅠㅠ",
      });
    });
};
</script>

<style scoped>
.blinking {
  animation: blink 0.5s ease-in-out infinite alternate;
}
@keyframes blink {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}

.parent {
  position: relative;
}
.child,
.child {
  position: absolute;
}
</style>
