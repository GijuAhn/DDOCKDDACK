<template>
  <div id="session">
    <div id="session-header">
      <span id="session-title">
        {{ room.gameTitle }} [방 코드 - {{ room.pinNumber }}]
      </span>
      <img
        src="https://developers.kakao.com/assets/img/about/logos/kakaotalksharing/kakaotalk_sharing_btn_medium.png"
        @click="linkShare"
        id="kakaoShareButton"
      />
      <span>공유하기</span>

      <span id="subscribers-count">
        참가자 : {{ openviduInfo.subscribers.length + 1 }}명</span
      >
    </div>

    <div id="main-container">
      <capture-video id="main-video" :stream-manager="openviduInfo.publisher" />

      <div id="left-section">
        <div id="gameInfoSection">
          <div v-if="isStart && !isEnd">
            <div id="gameImageSection">
              <img
                :src="`${IMAGE_PATH}/${room.gameImages[round - 1].gameImage}`"
              />
            </div>
            <div id="gameCurrentSection">
              <span v-show="!isEnd"> 게임 라운드 : {{ round }}</span>
              <span v-show="!isEnd"> 남은 시간 : {{ timerCount }} </span>
            </div>
          </div>

          <div v-if="!isStart">
            <button v-if="isHost" v-show="!isStart" @click="play">play</button>
            대기중
          </div>

          <div v-if="isEnd">
            <button @click="getMyImages">결과보기</button>

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
                    :src="`${IMAGE_PATH}/${room.gameImages[index].gameImage}`"
                  />
                  <div class="test">
                    <img :src="image" id="bestcutImg" />
                  </div>
                  <br />
                </div>
              </div>

              <button @click="upload">베스트 컷 게시</button>
            </div>
          </div>
        </div>

        <div id="my-video">
          <user-video
            id="main-video"
            :stream-manager="openviduInfo.publisher"
            :resultMode="resultMode"
          />
        </div>
      </div>
      <div id="right-section">
        <div
          id="video-container"
          :class="{
            grid1: 1 === openviduInfo.subscribers.length,
            grid2: 2 === openviduInfo.subscribers.length,
            grid3: 3 === openviduInfo.subscribers.length,
            grid4: 4 === openviduInfo.subscribers.length,
            grid5: 5 === openviduInfo.subscribers.length,
            grid6: 6 === openviduInfo.subscribers.length,
            grid7: 7 === openviduInfo.subscribers.length,
            grid8: 8 === openviduInfo.subscribers.length,
            grid9: 9 === openviduInfo.subscribers.length,
            grid10: 10 === openviduInfo.subscribers.length,
            grid11: 11 === openviduInfo.subscribers.length,
            grid12: 12 === openviduInfo.subscribers.length,
          }"
        >
          <user-video
            v-for="sub in openviduInfo.subscribers"
            :key="sub.stream.connection.connectionId"
            :stream-manager="sub"
          />
        </div>
      </div>
    </div>

    <div id="button-container">
      <button class="btn-video-control">
        <svg-icon type="mdi" :path="path[0]" /> 음소거
      </button>
      <button class="btn-video-control">
        <svg-icon type="mdi" :path="path[2]" /> 화면 중지
      </button>
      <button type="button" class="btn-close" @click="leaveSession">
        <span class="icon-cross"></span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { apiInstance } from "@/api/index";
import {
  computed,
  onBeforeMount,
  onMounted,
  ref,
  watch,
} from "@vue/runtime-core";
import { useRoute } from "vue-router";
import { OpenVidu } from "openvidu-browser";
import UserVideo from "@/components/Gameroom/item/UserVideo.vue";
import CaptureVideo from "@/components/Gameroom/item/CaptureVideo.vue";
import { useStore } from "vuex";
import router from "@/router/index.js";
import process from "process";
import SvgIcon from "@jamescoyle/vue-icon";
import {
  mdiMicrophone,
  mdiMicrophoneOff,
  mdiVideo,
  mdiVideoOff,
} from "@mdi/js";

const IMAGE_PATH = process.env.VUE_APP_IMAGE_PATH;
const api = apiInstance();
const route = useRoute();
const store = useStore();

const accessToken = computed(() => store.state.memberStore.accessToken);
const nickname = ref();
const openviduInfo = ref({
  // OpenVidu objects
  OV: undefined,
  session: undefined,
  mainStreamManager: undefined,
  publisher: undefined,
  subscribers: [],
});

// room object
const room = ref({
  pinNumber: undefined,
  gameId: undefined,
  gameTitle: undefined,
  gameDescription: undefined,
  gameImages: undefined,
});
const timerCount = ref(2);
const timerEnabled = ref(false);
const isStart = ref(false);
const round = ref(1);
const isHost = ref(false);
const isEnd = ref(false);
const resultMode = ref(false);
const path = ref([mdiMicrophone, mdiMicrophoneOff, mdiVideo, mdiVideoOff]);

onBeforeMount(() => {
  api
    .post(`/api/game-rooms/${route.params.pinNumber}`, {})
    .then((res) => {
      //access-token 없으면 닉네임 입력 받도록 수정 필요
      if (!accessToken.value) {
        do {
          nickname.value = prompt("닉네임을 입력해주세요.");
          if (nickname.value == null) {
            router.replace("/");
          }
        } while (nickname.value.trim() == "");
      }

      openviduInfo.value.OV = new OpenVidu();
      openviduInfo.value.session = openviduInfo.value.OV.initSession();
      // On every new Stream received...
      openviduInfo.value.session.on("streamCreated", ({ stream }) => {
        const subscriber = openviduInfo.value.session.subscribe(stream);
        openviduInfo.value.subscribers.push(subscriber);
      });

      // On every Stream destroyed...
      openviduInfo.value.session.on("streamDestroyed", ({ stream }) => {
        const index = openviduInfo.value.subscribers.indexOf(
          stream.streamManager,
          0
        );
        if (index >= 0) {
          openviduInfo.value.subscribers.splice(index, 1);
        }
      });

      // On every asynchronous exception...
      openviduInfo.value.session.on("exception", ({ exception }) => {
        console.warn(exception);
      });

      openviduInfo.value.session.on("signal", () => {
        timerEnabled.value = true;
        isStart.value = true;
      });

      openviduInfo.value.session
        .connect(res.data.token, {
          clientData: nickname.value,
        })
        .then(() => {
          // --- 5) Get your own camera stream with the desired properties ---

          // Init a publisher passing undefined as targetElement (we don't want OpenVidu to insert a video
          // element: we will manage it on our own) and with the desired properties
          let publisher = openviduInfo.value.OV.initPublisher(undefined, {
            audioSource: undefined, // The source of audio. If undefined default microphone
            videoSource: undefined, // The source of video. If undefined default webcam
            publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
            publishVideo: true, // Whether you want to start publishing with your video enabled or not
            resolution: "640x480", // The resolution of your video
            frameRate: 30, // The frame rate of your video
            insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
            mirror: true, // Whether to mirror your local video or not
          });

          // Set the main video in the page to display our webcam and store our Publisher
          openviduInfo.value.mainStreamManager = publisher;
          openviduInfo.value.publisher = publisher;

          // --- 6) Publish your stream ---

          openviduInfo.value.session.publish(openviduInfo.value.publisher);
          room.value.pinNumber = res.data.pinNumber;
          room.value.gameId = res.data.gameId;
          room.value.gameTitle = res.data.gameTitle;
          room.value.gameDescription = res.data.gameDescription;
          room.value.gameImages = res.data.gameImages;
          isHost.value = res.data.isHost;
        })
        .catch((error) => {
          console.log(
            "There was an error connecting to the session:",
            error.code,
            error.message
          );
        });
      window.addEventListener("beforeunload", leaveSession);
    })
    .catch((err) => {
      if (err.response.status === 400) {
        alert("이미 시작된 게임방입니다.");
      }
      if (err.response.status === 404) {
        alert("존재하지 않는 게임방입니다.");
        router.replace("/");
      }
      if (err.response.status === 401) {
        console.log(err.response);
      }
    });
});

onMounted(() => {
  timerCount.value = 2;
  timerEnabled.value = false;
  isStart.value = false;
  round.value = 1;
});

const leaveSession = () => {
  // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
  if (openviduInfo.value.session) {
    let sessionId = openviduInfo.value.session.connection.connectionId;
    // 방에 유저가 있는 경우 멤버삭제
    if (openviduInfo.value.subscribers.length) {
      api
        .delete(
          `/api/game-rooms/${route.params.pinNumber}/sessions/${sessionId}`
        )
        .catch((err) => {
          console.log(err);
        });
    } else {
      // 남아있는 유저가 나 혼자인 경우 방삭제
      api.delete(`/api/game-rooms/${route.params.pinNumber}`).catch((err) => {
        console.log(err);
      });
    }
    openviduInfo.value.session.disconnect();
    openviduInfo.value.session = undefined;
    openviduInfo.value.mainStreamManager = undefined;
    openviduInfo.value.publisher = undefined;
    openviduInfo.value.subscribers = [];
    openviduInfo.value.OV = undefined;
    room.value.pinNumber = undefined;
  }

  // Remove beforeunload listener
  window.removeEventListener("beforeunload", leaveSession);
  router.replace("/");
};
const play = () => {
  api
    .put(`/api/game-rooms/${route.params.pinNumber}`)
    .then(() => {
      setTimeout(() => {
        openviduInfo.value.session.signal({
          data: "", // Any string (optional)
        });
      }, 1000);
    })
    .catch(() => {
      alert("게임시작에 실패 하였습니다.");
    });
};

const linkShare = async () => {
  window.Kakao.Link.sendDefault({
    objectType: "feed",
    content: {
      title: `${room.value.gameTitle}`,
      description: `${room.value.gameDescription}`,
      imageUrl: `${IMAGE_PATH}/${room.value.gameImages[0].gameImage}`,
      link: {
        mobileWebUrl: `http://localhost:8080/gameroom/${room.value.pinNumber}`,
        webUrl: `http://localhost:8080/gameroom/${room.value.pinNumber}`,
      },
    },
    buttons: [
      {
        title: "게임 하러가기 ",
        link: {
          mobileWebUrl: `http://localhost:8080/gameroom/${room.value.pinNumber}`,
          webUrl: `http://localhost:8080/gameroom/${room.value.pinNumber}`,
        },
      },
    ],
  });
};

watch(
  timerEnabled,
  () => {
    timerCount.value--;
  },
  { immediate: true }
);

watch(
  timerCount,
  (value) => {
    if (value > 0 && timerEnabled.value) {
      setTimeout(() => {
        timerCount.value--;
      }, 1000);
    }
    if (round.value > 5) {
      timerEnabled.value = false;
      isEnd.value = true;
    }
    if (value === 0) {
      capture();
      resultMode.value = true;
      setTimeout(() => {
        round.value++;
        timerCount.value = 2;
        resultMode.value = false;
      }, 500);
    }
  },
  { immediate: true }
);
const isShow = ref(false);
const getMyImages = () => {
  isShow.value = true;
};
const check = (index) => {
  isChecked.value[index] = !isChecked.value[index];
};
const isChecked = ref([
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
]);
const inputs = ref(["", "", "", "", "", "", "", "", "", ""]);

const upload = () => {
  bestcutSaveReq.value.pinNumber =
    openviduInfo.value.publisher.session.sessionId;
  bestcutSaveReq.value.sessionId =
    openviduInfo.value.publisher.session.connection.connectionId;
  bestcutSaveReq.value.gameTitle = room.gameTitle;
  isChecked.value.forEach((element, index) => {
    if (element) {
      bestcutSaveReq.value.images.push({
        bestcutIndex: index,
        bestcutImgTitle: inputs.value[index],
        gameImgUrl: room.gameImages[index].gameImage,
        gameImgDesc: room.gameImages[index].gameImageDesc,
      });
    }
  });
  api
    .post("/api/bestcuts", bestcutSaveReq.value)
    .then(() => {
      alert("업로드가 완료 되었습니다.");
    })
    .catch((err) => {
      err;
      alert("업로드 실패");
    });
};
const bestcutSaveReq = ref({
  pinNumber: undefined,
  sessionId: undefined,
  gameTitle: undefined,
  images: [],
});
const resultImages = ref([]);

const capture = () => {
  let me = document.getElementById("myVideo2");
  html2canvas(me).then((canvas) => {
    let myImg;
    const sessionId =
      openviduInfo.value.publisher.session.connection.connectionId;
    const pinNumber = openviduInfo.value.publisher.session.sessionId;
    myImg = canvas.toDataURL("image/jpeg");
    let byteString = myImg.replace("data:image/jpeg;base64,", "");

    let param = {
      memberGameImage: byteString,
    };
    api.post(`/api/game-rooms/${pinNumber}/${sessionId}/images`, param);

    resultImages.value.push(myImg);
  });
};
import html2canvas from "html2canvas";
</script>

<style>
body {
  overflow-y: hidden;
}
#session {
  background-color: black;
  color: #ffffff;
  height: 100vh;
}
#session-header {
  height: 35px;
  /* background-color: rgb(255, 150, 150); */
  display: flex;
  align-items: center;
  padding: 0 10px;
}
#session-header span {
  font-size: 20px;
}
#session-header span:first-child {
}
#session-header span:last-child {
  margin-left: auto;
}
#main-container {
  /* border: 1px solid red; */
  height: calc(100vh - 100px);
  display: flex;
  position: relative;
}
#left-section {
  width: 50%;
}
#gameInfoSection {
  /* background-color: rgb(104, 104, 0); */
  height: 50%;
  flex-direction: column;
  position: relative;
  overflow: scroll;
}
#gameImageSection {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}
#gameImageSection img {
  height: 100%;
  width: 100%;
  object-fit: contain;
}
#gameCurrentSection {
  position: absolute;
  top: 0;
  left: 0;
}
#my-video {
  /* background-color: rgb(104, 0, 87); */
  height: 50%;
  flex-direction: column;
}
#my-video > * {
  height: 100%;
}
#right-section {
  width: 50%;
  /* background-color: rgb(0, 0, 99); */
}
#video-container {
  height: 100%;
  display: grid;
}
.grid1 {
  grid-template-columns: repeat(1, 1fr);
  grid-template-rows: repeat(1, 1fr);
}
.grid2 {
  grid-template-columns: repeat(1, 1fr);
  grid-template-rows: repeat(2, 1fr);
}
.grid3 {
  grid-template-columns: repeat(2, 1fr);
  grid-template-rows: repeat(2, 1fr);
}
.grid4 {
  grid-template-columns: repeat(2, 1fr);
  grid-template-rows: repeat(2, 1fr);
}
.grid5 {
  grid-template-columns: repeat(2, 1fr);
  grid-template-rows: repeat(3, 1fr);
}
.grid6 {
  grid-template-columns: repeat(2, 1fr);
  grid-template-rows: repeat(3, 1fr);
}
.grid7 {
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(3, 1fr);
}
.grid8 {
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(3, 1fr);
}
.grid9 {
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(3, 1fr);
}
.grid10 {
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(4, 1fr);
}
.grid11 {
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(4, 1fr);
}
.grid12 {
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(4, 1fr);
}
#video-container > * {
  height: 100%;
}
#button-container {
  height: 65px;
  /* background-color: rgb(150, 216, 255); */
  text-align: center;
}

.btn-video-control {
  border-color: #ffffff;
  color: white;
  background-color: rgba(0, 0, 0, 0);
  border-radius: 50px;
  width: 150px;
  font-size: 21px;
}

.btn-close {
  border: 0;
  background: #ff0000;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: inline;
  flex-flow: column nowrap;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 150ms;
}
.btn-close .icon-cross {
  border: 0;
  background: none;
  position: absolute;
  width: 10px;
  height: 10px;
}
.btn-close .icon-cross:before,
.btn-close .icon-cross:after {
  content: "";
  position: absolute;
  top: 15px;
  left: -10px;
  right: 0;
  height: 6px;
  background: #fff;
  border-radius: 6px;
}
.btn-close .icon-cross:before {
  transform: rotate(45deg);
}
.btn-close .icon-cross:after {
  transform: rotate(-45deg);
}
.btn-close .icon-cross span {
  display: block;
}
.btn-close:hover,
.btn-close:focus {
  transform: rotateZ(90deg);
  background: #ff0000;
}
.test {
  width: 400px;
  height: 400px;
  border: 1px solid blue;
}
.test img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
#kakaoShareButton {
  width: 30px;
  height: 30px;
}
#kakaoShareButton:hover {
  cursor: pointer;
}
</style>
