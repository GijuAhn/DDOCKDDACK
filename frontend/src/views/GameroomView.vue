<template>
  <div id="session">
    <div id="main-container" class="container">
      <div id="session-header">
        <button id="buttonLeaveSession" @click="leaveSession">
          Leave Session
        </button>
        <h3 id="session-title">
          핀번호 : {{ room.pinNumber }} 인원 :
          {{ openviduInfo.subscribers.length + 1 }}
          <span v-show="!isEnd">/ 게임 라운드 : {{ round }}</span>
          <button @click="linkShare()">
            <img
              src="https://developers.kakao.com/assets/img/about/logos/kakaotalksharing/kakaotalk_sharing_btn_medium.png"
              alt="카카오톡 공유 보내기 버튼"
            />
          </button>
        </h3>
        <h3 v-show="!isEnd">
          남은 시간 : {{ timerCount }}
          <button v-if="isHost" v-show="!isStart" @click="play">play</button>
        </h3>
      </div>
      <div id="main-video" class="col-md-6">
        <user-video
          :stream-manager="openviduInfo.publisher"
          :timerCount="timerCount"
          :isEnd="isEnd"
          :isStart="isStart"
          :round="round"
          :room="room"
          :resultMode="resultMode"
        />
      </div>
      <div id="video-container" class="col-md-6">
        <div
          v-for="sub in openviduInfo.subscribers"
          :key="sub.stream.connection.connectionId"
        >
          <user-video :stream-manager="sub" />
        </div>
      </div>
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
import { useStore } from "vuex";
import UserVideo from "@/components/Gameroom/item/UserVideo.vue";
import router from "@/router/index.js";

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
const timerEnabled = ref(false);
const isStart = ref(false);
const round = ref(1);
const isHost = ref(false);
const isEnd = ref(false);
const resultMode = ref(false);

onBeforeMount(() => {
  api
    .post(`/game-rooms/${route.params.pinNumber}`, {})
    .then((res) => {
      //access-token 없으면 닉네임 입력 받도록 수정 필요
      if (!accessToken.value) {
        nickname.value = prompt("닉네임을 입력해주세요.");
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
    });
});

onMounted(() => {
  timerCount.value = 5;
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
        .delete(`/game-rooms/${route.params.pinNumber}/sessions/${sessionId}`)
        .catch((err) => {
          console.log(err);
        });
    } else {
      // 남아있는 유저가 나 혼자인 경우 방삭제
      api.delete(`/game-rooms/${route.params.pinNumber}`).catch((err) => {
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
    .put(`/game-rooms/${route.params.pinNumber}`)
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
  const file = await convertURLtoFile(
    `/static/images/${room.value.gameId}/${room.value.gameImages[0].gameImage}`
  );
  console.log(file);
  const files = [file];

  let response = await window.Kakao.Share.uploadImage({
    file: files,
  });
  console.log(response);
  window.Kakao.Link.sendDefault({
    objectType: "feed",
    content: {
      title: `${room.value.gameTitle}`,
      description: `${room.value.gameDescription}`,
      imageUrl: response.infos.original.url,
      link: {
        mobileWebUrl: `http://localhost:8080/game-room/${room.value.pinNumber}`,
        webUrl: `http://localhost:8080/game-room/${room.value.pinNumber}`,
      },
    },
    buttons: [
      {
        title: "게임 하러가기 ",
        link: {
          mobileWebUrl: `http://localhost:8080/game-room/${room.value.pinNumber}`,
          webUrl: `http://localhost:8080/game-room/${room.value.pinNumber}`,
        },
      },
    ],
  });
};

const convertURLtoFile = async (url) => {
  const response = await fetch(url, {
    mode: "no-cors",
  });
  const data = await response.blob();
  const fileName = room.value.gameImages[0].gameImage;
  const metadata = { type: "image/jpeg" };
  return new File([data], fileName, metadata);
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
    if (value >= 0 && timerEnabled.value) {
      setTimeout(() => {
        timerCount.value--;
      }, 1000);
    }
    if (round.value > 5) {
      timerEnabled.value = false;
      isEnd.value = true;
    }
    if (value < 0) {
      resultMode.value = true;
      setTimeout(() => {
        round.value++;
        timerCount.value = 5;
        resultMode.value = false;
      }, 500);
    }
  },
  { immediate: true }
);
</script>

<style>
#video-container video {
  position: relative;
  float: left;
  width: 50%;
  cursor: pointer;
}

#video-container video + div {
  float: left;
  width: 50%;
  position: relative;
  margin-left: -50%;
}

#video-container p {
  display: inline-block;
  background: #f8f8f8;
  padding-left: 5px;
  padding-right: 5px;
  color: #777777;
  font-weight: bold;
  border-bottom-right-radius: 4px;
}

#main-video p {
  position: absolute;
  display: inline-block;
  background: #f8f8f8;
  padding-left: 5px;
  padding-right: 5px;
  font-size: 22px;
  color: #777777;
  font-weight: bold;
  border-bottom-right-radius: 4px;
}

#session img {
  width: 100%;
  height: auto;
  display: inline-block;
  object-fit: contain;
  vertical-align: baseline;
}

video {
  width: 100%;
  height: auto;
}

#session #video-container img {
  position: relative;
  float: left;
  width: 50%;
  cursor: pointer;
  object-fit: cover;
  height: 180px;
}
</style>
