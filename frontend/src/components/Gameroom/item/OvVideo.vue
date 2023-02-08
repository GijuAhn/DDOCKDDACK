<template>
  <div id="parent">
    <video autoplay ref="video" id="myVideo1" />
    <video autoplay ref="video2" id="myVideo2" />
  </div>
</template>

<script setup>
import { defineProps, onMounted, ref } from "vue";

const props = defineProps({ streamManager: Object, who: String });
const video = ref(null);
const video2 = ref(null);
onMounted(() => {
  props.streamManager.addVideoElement(video.value);
  props.streamManager.addVideoElement(video2.value);
});
</script>

<style>
#parent {
  border: 1px solid red;
  width: 100%;
  height: 100%;
  position: relative;
}
#myVideo1 {
  border-radius: 20px;
  border: 1px solid aqua;
  /* visibility: hidden; */

  /* width: 150px;
  height: 150px; */

  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  height: calc(100% - 20px);
  width: calc(100% - 20px);
  margin: 10px;
  object-fit: contain;
}
#myVideo2 {
  border-radius: 20px;
  border: 1px solid yellowgreen;
  /* visibility: hidden; */

  /* width: 150px;
  height: 150px; */

  position: absolute;
  top: -300px;
  right: -500px;
  /* visibility: hidden; */
}
</style>
