<!-- require(`@/../../Backend/images/profile/${myProfile.id}/${myProfile.profile}`) -->
<template>
  <div class="user-info">
    <div id="profile">
      <div id="profileImg" style="float: left">
        <img :src="`${IMAGE_PATH}/${myProfile.profile}`" alt="" id="image" />
        <div id="div_modifyImg">
          <input
            type="file"
            @change="imgUploadEvent"
            accept=".jpg,.jpeg,.png"
            id="imgInput"
            style="display: none"
          />
          <label for="imgInput">
            <img
              :src="require(`@/assets/images/modify-profile-img.png`)"
              id="modifyProfile"
            />
          </label>
        </div>
      </div>
    </div>
    <div id="nameAndEmail">
      <div id="nickname" v-if="!save">
        <span id="name">{{ name }}</span>
        <span @click="modifyNameInput">
          <img
            :src="require(`@/assets/images/modify-name.png`)"
            alt="이름수정버튼"
            class="modify"
            id="btn_modifyName"
          />
        </span>
      </div>
      <div v-else>
        <form id="modifyInput" @submit="modifyName" onsubmit="return false">
          <input type="text" name="site" v-model="name" /><br />
        </form>
        <span v-if="checkNickname" id="checkNickname">
          2~15글자내외, 특수문자 사용 불가능
        </span>
      </div>

      <div id="email">
        <span>{{ myProfile.email }}</span>
      </div>
      <div id="deleteMember">
        <span id="btn_withdrawal" @click="withdrawal">회원탈퇴</span>
      </div>
      <!-- ${{ props.profile.gameId }}, ${{ props.profile.gameId }} -->
    </div>
  </div>
</template>

<script setup>
import { apiInstance } from "@/api/index";
import { useStore } from "vuex";
import { computed, ref } from "vue";
import process from "process";

const store = useStore();
const api = apiInstance();
const accessToken = computed(() => store.state.memberStore.accessToken);
let myProfile = computed(() => store.state.memberStore.memberInfo).value;
const IMAGE_PATH = process.env.VUE_APP_IMAGE_PATH;
const maxSize = 2 * 1024 * 1024;

let name = myProfile.nickname;
let profile = myProfile.profile;

let save = ref(false);
let checkNickname = ref(false);
const modifyNameInput = () => {
  //form형태로 변경
  console.log("click ", save.value);
  console.log("profile ", myProfile);
  save.value = !save.value;
};

// let memberReq = ref();

let reg_nickname = /^[A-z가-힣0-9_-]{2,15}$/;
const modifyName = () => {
  console.log("enter ", name);
  if (reg_nickname.test(name)) {
    console.log("OK!", name, " ", profile);
    api
      .put(
        `/api/members/nickname`,
        {
          nickname: name,
        },
        { headers: { "access-token": accessToken.value } }
      )
      .then(() => {
        console.log("성공");
        myProfile.nickname = name;
      })
      .catch((err) => {
        console.log("err ", err);
        if (err.response.status === 401) {
          alert("로그인 후 이용해주세요.");
        }
      });

    save.value = !save.value;
    // checkNickname.value = !checkNickname.value;
  } else {
    console.log("닉네임 규칙에 맞게");
    if (!checkNickname.value) {
      checkNickname.value = !checkNickname.value;
    }
  }
};

const imgUploadEvent = (e) => {
  console.log("i e ", e);
  modifyProfileImg(e.target.files);
};

let reg_img = ["jpg", "jpeg", "png"];
const modifyProfileImg = (f) => {
  const modifyImgName = f[0].name;

  let formData = new FormData();
  formData.append("profileImg", f[0]);

  console.log(formData);

  const ext = modifyImgName.split(".").pop().toLowerCase();
  if (reg_img.includes(ext) && f[0].size < maxSize) {
    api
      .put(`/api/members/profile`, formData, {
        headers: { "access-token": accessToken.value },
      })
      .then(() => {
        store.dispatch("memberStore/getMemberInfo");
        myProfile = computed(() => store.state.memberStore.memberInfo).value;
      })
      .catch((err) => {
        if (err.response.status === 401) {
          alert("로그인 후 이용해주세요.");
        }
      });
  } else {
    alert("2MB이하의 jpg, jpeg, png 이미지만 가능합니다!");
    console.log("이미지 규칙에 맞게");
  }
};

const withdrawal = () => {
  console.log("탈퇴!");
  api
    .delete(`/api/members`, {
      headers: { "access-token": accessToken.value },
    })
    .then(() => {
      store.state.memberStore.accessToken = null;
      store.state.memberStore.memberInfo = {};
      window.location.assign(`/`);
    })
    .catch((err) => {
      console.log(err);
      window.location.assign(`/`);
    });
};
</script>

<style scoped>
#image {
  border-radius: 75%;
  box-shadow: 0 0px 3px #999;
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
  margin-bottom: 10px;
  width: 350px;
}
#name {
  font-size: 48px;
}
.modify {
  width: 20%;
  height: 20%;
}
#btn_modifyName {
  display: none;
}
#div_modifyImg {
  display: none;
  position: absolute;
  top: 50%;
  left: 52%;
  transform: translate(-50%, -50%);
}
#nickname:hover #btn_modifyName {
  width: 30px;
  height: 30px;
  display: inline;
  cursor: pointer;
}
#profileImg:hover #div_modifyImg,
#modifyProfile {
  display: inline;
  cursor: pointer;

  width: 50px;
  height: 50px;
}
#checkNickname {
  color: crimson;
}

#email {
  padding-bottom: 5px;
}

#btn_withdrawal {
  display: inline-block;
  cursor: pointer;
  color: #666666;
  font-size: 13px;
  padding-top: 5px;
  /* padding: 12px 0px; */
}
input {
  outline: none;
  border-radius: 5px;
  border: 2px solid black;
  font-size: 40px;
  font-family: "NanumSquareRoundB";
  padding: 0 10px;
  height: 46px;
  width: 300px;
  margin-bottom: 20px;
}
</style>
