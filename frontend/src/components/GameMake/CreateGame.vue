<template>
  <div id="view">
    <div id="stateIsZero" v-show="step === 0">
      <form>
        <div>
          <div class="label">
            <label for="gameTitle" class="input-name">제목</label>
          </div>
          <div>
            <input
              id="gameTitle"
              type="text"
              v-model="gameSaveReq.gameTitle"
              placeholder="게임 제목을 입력해 주세요."
            />
          </div>
        </div>
        <div>
          <div class="label">
            <label for="gameDesc" class="input-name">설명</label>
          </div>
          <div>
            <input
              id="gameDesc"
              type="text"
              v-model="gameSaveReq.gameDesc"
              placeholder="게임 설명을 입력하세요."
            />
          </div>
        </div>
        <div>
          <div class="label">
            <label class="input-name">이미지 업로드</label>
          </div>
          <input
            type="file"
            @change="storeImage"
            accept=".jpg,.jpeg"
            multiple
            id="fileInput"
            style="display: none"
          />

          <div id="uploadSection">
            <div id="uploadDescSection">
              <label
                for="fileInput"
                class="file-label"
                @dragover="dragover"
                @dragleave="dragleave"
                @drop="drop"
              >
                <span v-if="gameSaveReq.images.length === 0"
                  >이곳을 클릭하거나 파일을 드래그하여 업로드 하세요.</span
                >
                <span v-else> </span
              ></label>
            </div>
            <div id="previewSection">
              <img
                v-for="item in gameSaveReq.images"
                :key="item"
                :src="convertFile(item.gameImage)"
                alt="이미지 미리보기..."
                width="180"
                height="180"
              />
            </div>
          </div>
        </div>
      </form>

      <div class="move-btn">
        <button @click="changeStep" class="yellow">다음</button>
      </div>
    </div>
    <div id="stateIsOne" v-show="step === 1">
      <div>
        <div class="label"><label class="input-name">사진 설명</label></div>
        <div id="descSection">
          <div v-for="item in gameSaveReq.images" :key="item">
            <img
              :src="convertFile(item.gameImage)"
              alt="이미지 미리보기..."
              width="120"
              height="120"
            />
            <input
              id="title"
              type="text"
              v-model="item.gameImageDesc"
              placeholder="설명을 입력하세요."
            />
            <img
              :src="require(`@/assets/images/minus.png`)"
              width="40"
              height="40"
              @click="removeLine(item)"
              class="minus"
            />
          </div>
        </div>
      </div>
      <div class="move-btn">
        <button @click="changeStep" class="gray">이전</button>
        <button @click="submit" class="yellow">완료</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { apiInstance } from "@/api/index";
import { useRouter } from "vue-router";

const router = useRouter();
const api = apiInstance();

const step = ref(0); // 0 : 이전, 1: 다음
const isDragging = ref(false);

const changeStep = () => {
  //이전<->다음
  step.value = (step.value + 1) % 2;
};

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
    files.forEach((file) => {
      gameSaveReq.value.images.push({
        gameImage: file,
        gameImageDesc: "",
      });
    });
  }
};

const convertFile = (file) => {
  //파일 미리보기
  return URL.createObjectURL(file);
};

const removeLine = (item) => {
  //images의 한 줄 삭제
  const index = gameSaveReq.value.images.indexOf(item);
  gameSaveReq.value.images.splice(index, 1);
};

const submit = () => {
  //완료버튼 클릭

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
    .post(`/api/games`, formData)
    .then(() => {
      router.push({ path: "/member/myGame" });
    })
    .catch((error) => {
      console.log(error);
    });
};
const dragover = (e) => {
  e.preventDefault();
  isDragging.value = true;
};
const dragleave = () => {
  isDragging.value = false;
};
const drop = (e) => {
  e.preventDefault();
  // console.log(e.dataTransfer.files);
  if (e.dataTransfer.files) {
    const files = Array.from(e.dataTransfer.files);
    files.forEach((file) => {
      gameSaveReq.value.images.push({
        gameImage: file,
        gameImageDesc: "",
      });
    });
  }
  isDragging.value = false;
};
</script>

<style scoped>
#view {
  border: 2px solid black;
  width: 1060px;
  position: relative;
  top: -320px;
  left: 50%;
  transform: translate(-50%, 0);
  background-color: white;
  padding: 70px;
}
form > div {
  margin-bottom: 40px;
}
.label {
  margin-bottom: 3px;
}
label {
  font-size: 20px;
}
#stateIsZero input[type="text"] {
  outline: none;
  border-radius: 5px;
  border: 2px solid black;
  font-size: 20px;
  font-family: "NanumSquareRoundB";
  padding: 0 10px;
  height: 44px;
  width: 1040px;
}
#stateIsOne input[type="text"] {
  outline: none;
  border-radius: 5px;
  border: 2px solid black;
  font-size: 20px;
  font-family: "NanumSquareRoundB";
  padding: 0 10px;
  height: 44px;
  width: 680px;
}
#uploadSection {
  border-radius: 5px;
  border: 2px solid black;
  width: 100%;
  height: 820px;
  position: relative;
}
#uploadDescSection {
  /* border: 1px solid red; */
  width: 100%;
  height: 100%;
  position: absolute;
}
#uploadDescSection label:hover {
  cursor: pointer;
}
#uploadDescSection label {
  /* border: 1px solid blue; */
  width: 100%;
  height: 100%;
  position: absolute;
  font-size: 36px;
}
#uploadDescSection label span {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 780px;
}
#previewSection {
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-rows: repeat(4, 1fr);
  grid-template-columns: repeat(5, 1fr);
  place-items: center;
}

#descSection {
  border-radius: 5px;
  border: 2px solid black;
  width: 1000px;
  padding: 30px;
  margin-bottom: 40px;
}
#descSection > div {
  padding-right: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 0 0 40px 0;
}

#descSection > div:last-child {
  margin: 0 !important;
}

/* #descSection > div > * {
  margin: auto;
} */
.move-btn {
  text-align: right;
}
.yellow {
  background-color: white;
  border-radius: 5px;
  border: 2px solid black;
  font-size: 20px;
  font-family: "NanumSquareRoundB";
  height: 48px;
  background-color: #ffb800;
  color: white;
  width: 120px;
  margin: 20px 0 0 0;
}
.yellow:hover {
  cursor: pointer;
}
.gray {
  background-color: white;
  border-radius: 5px;
  border: 2px solid black;
  font-size: 20px;
  font-family: "NanumSquareRoundB";
  height: 48px;
  background-color: #515151;
  color: white;
  width: 120px;
  margin: 20px 50px 0 0;
}
.gray:hover {
  cursor: pointer;
}
img {
  object-fit: cover;
  border-radius: 5px;
}
.minus:hover {
  cursor: pointer;
}
</style>
