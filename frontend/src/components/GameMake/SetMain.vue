<template>
  <div v-show="state.step === 0" style="border: 1px solid red">
    <h1>제목, 설명, 사진 업로드</h1>
    <form>
      <div>
        <label for="title">제목</label>
        <input id="title" type="text" />
      </div>
      <div>
        <label for="description">설명</label>
        <input id="description" type="text" />
      </div>
      <div>
        <input type="file" @change="storeImage" accept="image/*" multiple />
      </div>
    </form>
    <div>
      <h2>사진 업로드</h2>
      <ul>
        <li v-for="item in state.uploadImages" :key="item">
          <img :src="item" alt="이미지 미리보기..." width="200" height="200" />
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
            :src="item.image"
            alt="이미지 미리보기..."
            width="200"
            height="200"
          />
          <input id="title" type="text" :value="item.message" />
          <button>삭제</button>
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
import { toRaw } from "vue";

export default {
  setup() {
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

    const onFileSelected = () => {
      // console.log(savedProblems);
      // console.log(typeof savedProblems.value);
      // console.log(savedProblems.value.length);
      // console.log(state.value.step + 100);

      let problemList = toRaw(savedProblems.value);
      console.log(problemList);

      if (problemList) {
        const files = problemList;
        Promise.all(
          files.map((file) => {
            console.log(file.image);
            return new Promise((resolve, reject) => {
              let reader = new FileReader();

              reader.onload = (ev) => {
                resolve(ev.target.result);
              };

              reader.onerror = () => {
                reject();
              };
              reader.readAsDataURL(file.image);
            });
          })
        ).then(
          (images) => {
            state.value.uploadImages = images; //state 값에 저장
          },
          (error) => {
            console.error(error);
          }
        );
      }
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
        onFileSelected();
      }
    };

    const saveImageAsync = (problems) =>
      store.dispatch("gameMakeStore/setProblemsAsync", problems);
    const savedProblems = computed(
      () => store.state.gameMakeStore.savedProblems
    );

    const submit = () => {
      game.value.problemList = savedProblems;
    };

    return {
      state,
      onFileSelected,
      changeStep,
      game,
      storeImage,
      saveImageAsync,
      savedProblems,
      submit,
    };
  },
};
</script>

<style></style>
