<!-- require(`@/../../Backend/images/profile/${myProfile.id}/${myProfile.profile}`) -->
<template>
  <div class="user-info">
    <div id="profile">
      <div id="profileImg" style="float: left">
        <img
          :src="`${IMAGE_PATH}/${myProfile.profile}`"
          alt="대표사진"
          id="image"
        />
        <div id="modifyImg">
          <input
            type="file"
            @change="fileUploadEvent"
            accept=".jpg,.jpeg,.png"
            multiple
            id="fileInput"
            style="display: none"
          />
          <label for="fileInput">
            <img
              :src="require(`@/assets/images/modify-profile-img.png`)"
              alt="대표사진"
              id="modifyProfile"
            />
          </label>
        </div>
      </div>
    </div>
    <div id="nameAndEmail">
      <div id="nickname">
        <span>{{ myProfile.nickname }}</span>
        <span v-if="!save" id="modifyName" @click="modifyByName">
          <img
            :src="require(`@/assets/images/modify-name.png`)"
            alt="이름수정버튼"
            class="modify"
          />
        </span>
      </div>
      <div id="email">
        <span>{{ myProfile.email }}</span>
      </div>
      <!-- ${{ props.profile.gameId }}, ${{ props.profile.gameId }} -->
    </div>
  </div>
</template>

<script setup>
import { useStore } from "vuex";
import { computed } from "vue";
import process from "process";

const store = useStore();

const myProfile = computed(() => store.state.memberStore.memberInfo).value;

console.log(`${process.env.VUE_APP_IMAGE_PATH}`);
const IMAGE_PATH = process.env.VUE_APP_IMAGE_PATH;

const save = false;
// console.log(myProfile.value);

// const createGame = () => {
//   //FormData에 담아 axios.post
//   const formData = new FormData();
//   formData.append("gameTitle", gameSaveReq.value.gameTitle);
//   formData.append("gameCategory", gameSaveReq.value.gameCategory);
//   formData.append("memberId", gameSaveReq.value.memberId);
//   formData.append("gameDesc", gameSaveReq.value.gameDesc);
//   for (let i = 0; i < gameSaveReq.value.images.length; i++) {
//     formData.append(
//       "images[" + i + "].gameImage",
//       gameSaveReq.value.images[i].gameImage
//     );
//     formData.append(
//       "images[" + i + "].gameImageDesc",
//       gameSaveReq.value.images[i].gameImageDesc
//     );
//   }

//   api
//     .post(`/api/games`, formData)
//     .then(() => {
//       router.push({ path: "/member/myGame" });
//     })
//     .catch((error) => {
//       console.log(error);
//     });
// };

const modifyByName = () => {
  //form형태로 변경
  console.log("click");
};
</script>

<style scoped>
#image {
  border-radius: 50%;
  width: 150px;
  height: 150px;
  margin-left: auto;
  margin-right: auto;
}
.user-info {
  height: 290px;
  display: table;
}
.user-info > div {
  display: table-cell;
  vertical-align: middle;
}
#profileImg {
  margin-right: 30px;
  position: relative;
}
#nickname {
  font-size: 48px;
  margin-bottom: 10px;
}
.modify {
  width: 20%;
  height: 20%;
}
.modifyProfile {
  width: 150%;
  height: 150%;
}
#modifyName {
  display: none;
}
#modifyImg {
  display: none;
  position: absolute;
  top: 35%;
  left: 40%;
}
#nickname:hover #modifyName {
  display: inline;
  cursor: pointer;
}
#profileImg:hover {
  cursor: pointer;
}
#profileImg:hover #modifyProfile {
  display: inline;
  cursor: pointer;
}
</style>
