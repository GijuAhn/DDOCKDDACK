<template>
  <div v-show="step === 0">
    <h1>제목, 설명, 사진 업로드</h1>
    <form>
      <div>
        <label for="gameTitle">제목</label>
        <input
          id="gameTitle"
          type="text"
          v-model="gameSaveReq.gameTitle"
          placeholder="게임 제목을 입력해 주세요."
        />
      </div>
      <div>
        <label for="gameDesc">설명</label>
        <input
          id="gameDesc"
          type="text"
          v-model="gameSaveReq.gameDesc"
          placeholder="게임 설명을 입력하세요."
        />
      </div>
      <div>
        <input type="file" @change="storeImage" accept="image/*" multiple />
      </div>
    </form>
    <div>
      <h2>사진 업로드</h2>
      <ul>
        <li v-for="item in images" :key="item">
          <img
            :src="convertFile(item.gameImage)"
            alt="이미지 미리보기..."
            width="200"
            height="200"
          />
        </li>
      </ul>
    </div>
    <button @click="changeStep">다음</button>
  </div>
  <div v-show="step === 1">
    <div>
      <h2>사진 설명</h2>
      <ul>
        <li v-for="item in images" :key="item">
          <img
            :src="convertFile(item.gameImage)"
            alt="이미지 미리보기..."
            width="200"
            height="200"
          />
          <input
            id="title"
            type="text"
            v-model="item.gameImageDesc"
            placeholder="설명을 입력하세요."
          />
          <button @click="removeLineAsync(item)">삭제</button>
        </li>
      </ul>
    </div>
    <button @click="changeStep">이전</button>
    <button @click="submit">완료</button>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { computed } from "vue";
import { useStore } from "vuex";
import { apiInstance } from "@/api/index";
import { useRouter } from "vue-router";

const router = useRouter();
const api = apiInstance();
const store = useStore();

const step = ref(0); // 0 : 이전, 1: 다음

const changeStep = () => {
  //이전<->다음
  step.value = (step.value + 1) % 2;
};

store.dispatch("gameMakeStore/clearImagesAsync"); // vuex에 저장된 images 초기화

const gameSaveReq = ref({
  gameTitle: "",
  gameCategory: "PICTURE",
  memberId: 1, //현재 로그인 유저 데이터 필요
  gameDesc: "",
  images: [],
});

const storeImage = (e) => {
  //파일 이벤트 발생
  if (e.target.files) {
    const files = Array.from(e.target.files);
    const images = [];
    files.forEach((file) => {
      images.push({
        gameImage: file,
        gameImageDesc: "",
      });
    });
    setImagesAsync(images);
  }
};

const setImagesAsync = (
  images //images List vuex에 저장
) => store.dispatch("gameMakeStore/setImagesAsync", images);

const images = computed(() => store.state.gameMakeStore.images); //vuex에 저장된 images

const convertFile = (file) => {
  //파일 미리보기
  return URL.createObjectURL(file);
};

const removeLineAsync = (item) => {
  //images의 한 줄 삭제
  store.dispatch("gameMakeStore/removeLineAsync", item);
};

const submit = () => {
  //완료버튼 클릭
  gameSaveReq.value.images = images;

  //유효성 검사
  let error = false;
  if (!gameSaveReq.value.gameTitle) {
    error = true;
    alert("게임 제목을 입력해 주세요.");
    return;
  }
  if (gameSaveReq.value.gameDesc.length > 50) {
    error = true;
    alert("게임 설명은 최대 50자 까지 작성해 주세요.");
    return;
  }
  if (
    gameSaveReq.value.images.length < 10 ||
    gameSaveReq.value.images.length > 20
  ) {
    error = true;
    alert("사진은 최소 10장, 최대 20장 등록해 주세요.");
    return;
  }
  for (let i = 0; i < gameSaveReq.value.images.length; i++) {
    if (!gameSaveReq.value.images[i].gameImage) {
      error = true;
      alert("이미지를 등록해 주세요");
      return;
    } else if (gameSaveReq.value.images[i].gameImageDesc.length > 50) {
      error = true;
      alert("이미지 설명은 최대 50자 까지 작성해 주세요.");
      return;
    }
  }
  if (!error) createGame();
};

const createGame = () => {
  //FormData에 담아 axios.post
  const formData = new FormData();
  formData.append("gameTitle", gameSaveReq.value.gameTitle);
  formData.append("gameCategory", gameSaveReq.value.gameCategory);
  formData.append("memberId", gameSaveReq.value.memberId);
  formData.append("gameDesc", gameSaveReq.value.gameDesc);
  for (let i = 0; i < gameSaveReq.value.images.length; i++) {
    formData.append(
      "images[" + i + "].gameImage",
      gameSaveReq.value.images[i].gameImage
    );
    formData.append(
      "images[" + i + "].gameImageDesc",
      gameSaveReq.value.images[i].gameImageDesc
    );
  }

  api
    .post(`/games`, formData)
    .then(() => {
      router.push({ path: "/member/myGame" });
    })
    .catch((error) => {
      console.log(error);
    });
};
</script>

<style></style>
