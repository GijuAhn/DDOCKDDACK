<template>
  <div id="content">
    <div id="topSection">
      <img
        :src="
          require(`@/../../Backend/images/bestcuts/${props.besctcut.bestcutId}/${props.besctcut.gameImgUrl}`)
        "
        alt="대표사진"
        class="image"
      />
    </div>
    <div id="bottomSection">
      <div id="gameTitle">
        <span>{{ props.besctcut.nickname }}</span>
      </div>
      <div id="gameDesc">
        <span>{{ props.besctcut.bestcutImgTitle }}</span>
      </div>
      <div id="gameDesc">
        <span>{{ props.besctcut.gameTitle }}</span>
        <span> | </span>
        <span>{{ props.besctcut.gameImgDesc }}</span>
      </div>
      <div id="etcSection" v-click-outside-element="onClickOutside">
        <div id="etcButton" @click="open">
          <img
            :src="require(`@/assets/images/etc-button.png`)"
            alt="대표사진"
            class="etc"
          />
        </div>
        <div id="etc" v-show="state">
          <div><span>삭제</span></div>
        </div>
      </div>
      <div id="thumbsUp">
        <span>추천</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref } from "vue";

const props = defineProps(["besctcut"]);

const onClickOutside = () => {
  // console.log(e);
  state.value = false;
};

const open = () => {
  state.value = !state.value;
};

const state = ref(false);
</script>

<style scoped>
#content {
  border: 2px solid black;
  width: 325px;
  height: 380px;
}
#createRoomButton {
  margin-top: 10px;
}
#gameTitle {
  width: 95%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
#gameDesc {
  margin-top: 5px;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
#createRoomButton button {
  border: 1px solid #da6c6b;
  background-color: white;
  font-size: 14px;
  font-family: "NanumSquareRoundB";
  width: 100%;
  padding: 10px 0;
  color: #da6c6b;
}
#createRoomButton button:hover {
  color: white;
  background-color: #da6c6b;
  cursor: pointer;
  transition: 0.3s;
}
#gameTitle span {
  font-size: 22px;
}
#gameDesc span {
  font-size: 16px;
  color: #656565;
}
.image {
  width: 325px;
  height: 260px;
  object-fit: cover;
}
#bottomSection {
  padding: 5px 10px;
  position: relative;
}
#etcButton {
  position: absolute;
  top: 0px;
  left: 290px;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  text-align: center;
}
#etcButton:hover {
  background-color: #d9d9d9;
  cursor: pointer;
  transition: 0.3s;
}
.etc {
  width: 50%;
  height: 50%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
/* #content:hover {
  box-shadow: 0 0 20px #8b8b8b;
  transition: 0.3s;
} */
#etc {
  position: absolute;
  top: 30px;
  left: 290px;
  background-color: white;
  width: 130px;
  box-shadow: 0 0 10px #8b8b8b;
  z-index: 1;
  border-radius: 10px;
  padding: 10px 0;
  display: block;
}
#etc :hover {
  cursor: pointer;
}
#etc span {
  font-size: 16px;
  display: block;
  padding: 10px;
}
#etc span:hover {
  background-color: #d9d9d9;
}

#thumbsUp {
  position: absolute;
  top: 50px;
  left: 285px;
}
</style>
