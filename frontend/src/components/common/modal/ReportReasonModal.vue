<template>
  <div>
    <div id="reportModal">
      <div id="reportTitle">
        <div><span>게임 신고</span></div>
      </div>
      <div id="reportReason">
        <div @click="setCurrentModalAsync"><span>스팸</span></div>
        <div @click="setCurrentModalAsync"><span>선정적</span></div>
        <div @click="setCurrentModalAsync"><span>폭력적</span></div>
        <div @click="setCurrentModalAsync"><span>혐오스러움</span></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

const setCurrentModalAsync = () => {
  store.dispatch("commonStore/setCurrentModalAsync", {
    name: "reportComplete",
    data: "",
  });
};
</script>

<style scoped>
#reportModal {
  background-color: white;
  width: 235px;
  border-radius: 10px;
  padding: 5px 0;
}
#reportModal span {
  display: block;
}

#reportTitle {
  text-align: center;
  border-bottom: 1px solid #a0a0a0;
}
#reportTitle span {
  font-size: 19px;
  padding: 10px;
}
#reportReason span {
  font-size: 16px;
  padding: 12px;
  margin: 1px;
  border-bottom: 1px solid #eaeaea;
}
#reportReason:hover {
  cursor: pointer;
}
#reportReason div:last-child span {
  border-bottom: none;
}
</style>
