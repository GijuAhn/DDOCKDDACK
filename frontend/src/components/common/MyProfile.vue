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
      <div id="nickname" v-if="!save">
        <span>{{ name }}</span>
        <span id="modifyName" @click="modifyByName">
          <img
            :src="require(`@/assets/images/modify-name.png`)"
            alt="이름수정버튼"
            class="modify"
          />
        </span>
      </div>
      <div id="nickname2" v-else>
        <form id="modifyInput" @submit="abc" onsubmit="return false">
          <input type="text" name="site" v-model="name" /><br />
        </form>
        <span id="modifyName" @click="modifyByName">
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
// import { apiInstance } from "@/api/index";
import { useStore } from "vuex";
import { computed, ref } from "vue";
import process from "process";

const store = useStore();
// const api = apiInstance();

const myProfile = computed(() => store.state.memberStore.memberInfo).value;
const IMAGE_PATH = process.env.VUE_APP_IMAGE_PATH;

let name = myProfile.nickname;

let save = ref(false);
const modifyByName = () => {
  //form형태로 변경
  console.log("click ", save.value);
  save.value = !save.value;
};

let reg_id1 = /^[A-z가-힣0-9_-]{2,15}$/;
const abc = () => {
  console.log("enter ", name);
  if (reg_id1.test(name)) {
    console.log("OK!");
    // api
    //   .put(
    //     `/api/members`,
    //     {},
    //     { headers: { "access-token": accessToken.value } }
    //   )
    //   .then(() => {
    //     let bestcut = bestcuts.value.find((e) => e.bestcutId === bestcutId);
    //     bestcut.isLiked = true;
    //     bestcut.popularity++;
    //   })
    //   .catch((err) => {
    //     if (err.response.status === 401) {
    //       alert("로그인 후 이용해주세요.");
    //     }
    //   });

    save.value = !save.value;
  } else {
    console.log("닉네임 규칙에 맞게");
  }
};

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
</script>

<style scoped>
#image {
  border-radius: 75%;
  border: 1px solid #c4c4c4;
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
#modifyName {
  display: none;
}
#modifyImg {
  display: none;
  position: absolute;
  top: 50%;
  left: 52%;
  transform: translate(-50%, -50%);
}
#nickname:hover #modifyName {
  display: inline;
  cursor: pointer;
}
/* #profileImg:hover {
  cursor: pointer;
} */
#profileImg:hover #modifyImg,
#modifyProfile {
  display: inline;
  cursor: pointer;
}
</style>
