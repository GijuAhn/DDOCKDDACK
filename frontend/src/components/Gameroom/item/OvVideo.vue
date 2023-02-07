<template>
  <div id="parent">
    <video autoplay ref="video" />
  </div>
</template>

<script setup>
import { defineProps, onMounted, ref } from "vue";

const props = defineProps({ streamManager: Object });
const video = ref(null);
onMounted(() => {
  props.streamManager.addVideoElement(video.value);
});
</script>

<style>
#parent {
  border: 1px solid red;
  width: 100%;
  height: 100%;
  position: relative;
}
video {
  /* border-radius: 20px; */

  /* width: 150px;
  height: 150px; */
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  height: 100%;
  width: 100%;
  /*border: 1px solid red;*/
  object-fit: contain;
}
</style>
