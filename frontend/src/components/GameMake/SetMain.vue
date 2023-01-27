<template>
  <div v-show="state.step === 0" style="border: 1px solid red">
    <h1>제목, 설명, 사진 업로드</h1>
    <form>
      <div>
        <label for="title">제목</label>
        <input id="title" type="text" v-model="game.title" />
      </div>
      <div>
        <label for="description">설명</label>
        <input id="description" type="text" v-model="game.description" />
      </div>
      <div>
        <input type="file" @change="storeImage" accept="image/*" multiple />
      </div>
    </form>
    <div>
      <h2>사진 업로드</h2>
      <ul>
        <li v-for="item in savedProblems" :key="item">
          <img
            :src="convertFile(item.image)"
            alt="이미지 미리보기..."
            width="200"
            height="200"
          />
        </li>
      </ul>
    </div>
    <button @click="changeStep">다음</button>
  </div>
  <div v-show="state.step === 1" style="border: 1px solid blue">
    <div>
      <h2>사진 설명</h2>
      <ul>
        <li v-for="item in savedProblems" :key="item">
          <img
            :src="convertFile(item.image)"
            alt="이미지 미리보기..."
            width="200"
            height="200"
          />
          <input id="title" type="text" v-model="item.message" />
          <button @click="removeLine(item)">삭제</button>
        </li>
      </ul>
    </div>
    <button @click="changeStep">이전</button>
    <button @click="submit">완료</button>
  </div>
</template>

<script>
import { ref } from "vue";
import { computed } from "vue";
import { useStore } from "vuex";
import { apiInstance } from "@/api/index";

export default {
  setup() {
    const api = apiInstance();
    const store = useStore();
    const state = ref({
      //사실 이건 객체임 ...
      uploadImages: null,
      step: 0,
    });
    const game = ref({
      title: "",
      description: "",
      problemList: [], //이미지와 설명
    });

    const convertFile = (file) => {
      return URL.createObjectURL(file);
    };

    const changeStep = () => {
      state.value.step = (state.value.step + 1) % 2;
    };

    const storeImage = (e) => {
      if (e.target.files) {
        const files = Array.from(e.target.files);
        //console.log(files);
        const problems = [];
        files.forEach((file) => {
          problems.push({
            image: file,
            message: "테스트",
          });
        });
        //console.log(problems);
        // files 를 problems에 저장
        saveImageAsync(problems);
      }
    };

    const saveImageAsync = (problems) =>
      store.dispatch("gameMakeStore/setProblemsAsync", problems);
    const savedProblems = computed(
      () => store.state.gameMakeStore.savedProblems
    );

    const removeLine = (item) => {
      store.dispatch("gameMakeStore/removeLineAsync", item);
    };

    const submit = () => {
      //유효성 검사 필요

      game.value.problemList = savedProblems;

      let param = {
        gameTitle: game.value.title,
        gameCategory: "PICTURE",
        memberId: "TEST_USER",
        gameDesc: game.value.description,
        images: {
          gameImage: game.value.problemList.image,
          gameImageDesc: game.value.problemList.message,
        },
      };

      api
        .post(`/games`, JSON.stringify(param))
        .then(({ data }) => {
          // let msg = "등록 처리시 문제가 발생했습니다.";
          // if (data === "success") {
          //   msg = "등록이 완료되었습니다.";
          // }
          //alert(msg);
          console.log("결과--------------");
          console.log(data);
          // this.moveList();
        })
        .catch((error) => {
          console.log(error);
        });
    };

    return {
      state,
      changeStep,
      game,
      storeImage,
      saveImageAsync,
      savedProblems,
      submit,
      convertFile,
      removeLine,
      api,
    };
  },
};
</script>

<style></style>
