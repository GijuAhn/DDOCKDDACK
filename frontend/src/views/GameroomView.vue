<template>
  <div id="session">
    <modal-frame v-if="currentModal.length !== 0" />
    <div>
      <result-modal v-if="resultMode" :round="round" :result="result" />
    </div>

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
      <!--우측 공간-->
      <span></span>
    </div>

    <div id="main-container">
      <capture-video id="main-video" :stream-manager="openviduInfo.publisher" />

      <div id="left-section">
        <div id="gameInfoSection">
          <div v-if="isStart && !isEnd">
            <div id="gameImageSection">
              <img
                class="game-image"
                :src="`${IMAGE_PATH}/${room.gameImages[round - 1].gameImage}`"
              />
            </div>
            <div id="gameCurrentSection">
              <span v-show="!isEnd"> 게임 라운드 : {{ round }}</span>
              <span v-show="!isEnd"> 남은 시간 : {{ timerCount }} </span>
            </div>
          </div>

          <div v-if="!isStart">
            <div id="gameWaitSection">
              <button v-if="isHost" v-show="!isStart" @click="play">
                play
              </button>
              <div id="waitDesc">
                <span>잠시만 기다려주세요</span><br />
                <span
                  >{{ openviduInfo.subscribers.length + 1 }}명 참가중...</span
                >
              </div>
            </div>
          </div>

          <div v-if="isEnd">
            <div id="gameResultSection">
              <button @click="setCurrentModalAsync(`bestcutUpload`)">
                내 사진 보기
              </button>
            </div>
          </div>
        </div>

        <div id="my-video">
          <user-video
            id="main-video"
            :stream-manager="openviduInfo.publisher"
            :captureMode="captureMode"
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
      <button class="btn-video-control">음소거</button>
      <button class="btn-video-control">화면 중지</button>
      <button class="btn-close" @click="leaveSession">
        <img
          :src="require(`@/assets/images/close.png`)"
          width="18"
          height="18"
        />
      </button>
    </div>
  </div>
</template>

<script setup>
import { apiInstance } from "@/api/index";
import { computed, onBeforeMount, ref } from "@vue/runtime-core";
import { useRoute } from "vue-router";
import { OpenVidu } from "openvidu-browser";
import UserVideo from "@/components/Gameroom/item/UserVideo.vue";
import CaptureVideo from "@/components/Gameroom/item/CaptureVideo.vue";
import { useStore } from "vuex";
import router from "@/router/index.js";
import process from "process";
import ModalFrame from "@/components/common/ModalFrame";
import ResultModal from "@/components/Gameroom/item/ResultModal.vue";
import html2canvas from "html2canvas";

const currentModal = computed(() => store.state.commonStore.currentModal);
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
const timerCount = ref(5);
const isStart = ref(false);
const round = ref(1);
const isHost = ref(false);
const isEnd = ref(false);
const resultMode = ref(false);
const captureMode = ref(false);
const result = ref([]);
const resultImages = ref([]);

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

      openviduInfo.value.session.on("signal:roundStart", (signal) => {
        round.value = signal.data;
        if (signal.data == 1) {
          isStart.value = true;
          console.log("게임 시작");
        }

        const timer = setInterval(() => {
          timerCount.value--;
          if (timerCount.value === 0) {
            clearInterval(timer);
            capture(signal.data - 1);
            timerCount.value = 5;
          }
        }, 1000);
      });

      openviduInfo.value.session.on("roundResult", (signal) => {
        resultMode.value = true;
        result.value = JSON.parse(signal.data);
        setTimeout(() => {
          resultMode.value = false;
          if (round.value < 5 && isHost.value) {
            api.get(`/api/game-rooms/${route.params.pinNumber}/round`);
          } else if (round.value == 5) {
            isEnd.value = true;
          }
        }, 5000);
      });

      openviduInfo.value.session.on("signal:host", () => {
        isHost.value = true;
      });

      if (!accessToken) {
        console.log(typeof res.data);
      }

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
        alert("중복 참가는 불가능 합니다.");
        router.replace("/");
      }
    });
});

const leaveSession = () => {
  // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
  if (openviduInfo.value.session) {
    let socketId = openviduInfo.value.session.connection.connectionId;
    throwHost();
    // 방에 유저가 있는 경우 멤버삭제
    if (openviduInfo.value.subscribers.length) {
      api
        .delete(
          `/api/game-rooms/${route.params.pinNumber}/sessions/${socketId}`
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

const throwHost = () => {
  const nextHost =
    openviduInfo.value.subscribers[0].stream.connection.connectionId;
  openviduInfo.value.session.signal({
    data: "",
    to: [nextHost],
    type: "host",
  });
};

const play = () => {
  api
    .put(`/api/game-rooms/${route.params.pinNumber}`)
    .then()
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

const capture = async (index) => {
  let me = document.getElementById("myVideo2");
  captureMode.value = true;
  setTimeout(() => {
    captureMode.value = false;
  }, 500);
  html2canvas(me)
    .then((canvas) => {
      let myImg;
      const socketId =
        openviduInfo.value.publisher.session.connection.connectionId;
      const pinNumber = openviduInfo.value.publisher.session.sessionId;
      myImg = canvas.toDataURL("image/jpeg");
      let byteString = myImg.replace("data:image/jpeg;base64,", "");

      let param = {
        gameImage: room.value.gameImages[index].gameImage,
        memberGameImage: byteString,
      };
      api.post(`/api/game-rooms/${pinNumber}/${socketId}/images`, param);
      resultImages.value.push(myImg);
    })
    .catch((err) => {
      console.log(err);
    });
};

const setCurrentModalAsync = (what) => {
  store.dispatch("commonStore/setCurrentModalAsync", {
    name: what,
    data: [resultImages, openviduInfo.value.publisher, room],
  });
};
</script>

<style scoped>
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
#session-header span:last-child {
  margin-left: auto;
}
#main-container {
  /* border: 1px solid red; */
  height: calc(100vh - 105px);
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
}
#gameImageSection,
#gameWaitSection,
#gameResultSection {
  border-radius: 20px;
  position: absolute;
  top: 10px;
  left: 10px;
  right: 10px;
  bottom: 10px;
  border: 1px solid #464646;
}
#gameImageSection img {
  height: 100%;
  width: 100%;
  object-fit: contain;
}
#gameWaitSection,
#gameResultSection {
  background-color: #d9d9d9;
}
#waitDesc > span {
  color: black;
  font-size: 50px;
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

#kakaoShareButton {
  width: 30px;
  height: 30px;
}
#kakaoShareButton:hover {
  cursor: pointer;
}
.game-image {
  transform: scaleX(-1);
}
#button-container {
  height: 70px;
  /* background-color: rgb(150, 216, 255); */
  display: flex;
  align-items: center;
  justify-content: center;
}
.btn-video-control {
  border: 1px solid #464646;
  font-size: 18px;
  font-family: "NanumSquareRoundB";
  padding: 10px 0;
  background: none;
  color: white;
  width: 140px;
  height: 45px;
  border-radius: 45px;
}
.btn-close {
  border: 1px solid #db1f2e;
  background-color: #db1f2e;
  width: 45px;
  height: 45px;
  border-radius: 45px;
}
.btn-video-control:hover,
.btn-close:hover {
  cursor: pointer;
}
#button-container button {
  margin: 0 10px;
}
</style>
