<template>
  <div>
    <div id="reportModal">
      <div v-if="true">
        <div v-for="(image, index) in currentModal.data[0]" :key="index">
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
            <img :src="`${IMAGE_PATH}/${room.gameImages[index].gameImage}`" />
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
</template>

<script setup>
import { apiInstance } from "@/api/index";
import { ref } from "vue";
import { computed } from "vue";
import { useStore } from "vuex";

const store = useStore();
const currentModal = computed(() => store.state.commonStore.currentModal);
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
  bestcutSaveReq.value.pinNumber = currentModal.value.data[1].session.sessionId;
  bestcutSaveReq.value.sessionId =
    currentModal.value.data[1].session.connection.connectionId;
  bestcutSaveReq.value.gameTitle = currentModal.value.data[2].gameTitle;
  isChecked.value.forEach((element, index) => {
    if (element) {
      bestcutSaveReq.value.images.push({
        bestcutIndex: index,
        bestcutImgTitle: inputs.value[index],
        gameImgUrl: currentModal.value.data[2].gameImages[index].gameImage,
        gameImgDesc: currentModal.value.data[2].gameImages[index].gameImageDesc,
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
  border-radius: 10px;
  padding: 5px 0;
}
</style>
