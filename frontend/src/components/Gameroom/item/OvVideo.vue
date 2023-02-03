<template>
  <video autoplay ref="video" />
</template>

<script setup>
import { defineProps, onMounted, ref } from "vue";

const props = defineProps({ streamManager: Object });
const video = ref(null);
onMounted(() => {
  props.streamManager.addVideoElement(video.value);
});
</script>

<style></style>
