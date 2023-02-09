<template>
  <div>
    <div id="reportModal">
      <div>
        <div
          v-for="(image, index) in resultImages"
          :key="index"
          style="border: 1px solid red"
        >
          <div v-if="true">
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
            <img :src="`${IMAGE_PATH}/${room.gameImages[index].gameImage}`" />

            <br />
          </div>
          <div class="test">
            <img :src="image" id="bestcutImg" />
          </div>
        </div>

        <button @click="upload">베스트 컷 게시</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { apiInstance } from "@/api/index";
import { ref } from "vue";
import { computed } from "vue";
import { useStore } from "vuex";
import process from "process";

const IMAGE_PATH = process.env.VUE_APP_IMAGE_PATH;
const store = useStore();
const currentModal = computed(() => store.state.commonStore.currentModal);
const resultImages = ref(currentModal.value.data[0]);
const streamManager = ref(currentModal.value.data[1]);
const room = ref(currentModal.value.data[2]);

const api = apiInstance();

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
  bestcutSaveReq.value.pinNumber = streamManager.value.session.sessionId;
  bestcutSaveReq.value.sessionId =
    streamManager.value.session.connection.connectionId;
  bestcutSaveReq.value.gameTitle = room.value.gameTitle;
  isChecked.value.forEach((element, index) => {
    if (element) {
      bestcutSaveReq.value.images.push({
        bestcutIndex: index,
        bestcutImgTitle: inputs.value[index],
        gameImgUrl: room.value.gameImages[index].gameImage,
        gameImgDesc: room.value.gameImages[index].gameImageDesc,
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
</script>

<style scoped>
#reportModal {
  background-color: white;
  width: 1000px;
  height: 500px;
  border-radius: 10px;
  padding: 5px 0;
  overflow: scroll;
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
</style>
