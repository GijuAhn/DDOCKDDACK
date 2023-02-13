<template>
  <div id="ovVideoContainer" v-if="props.streamManager">
    <ov-video
      id="ovVideo"
      :class="{ blinking: captureMode }"
      :stream-manager="props.streamManager"
    />

    <div id="nickname">
      <p>{{ clientData }}</p>
    </div>
  </div>
</template>

<script setup>
import OvVideo from "./OvVideo";

import { computed, defineProps } from "vue";

const props = defineProps({
  streamManager: Object,
  captureMode: Boolean,
});
const clientData = computed(() => {
  const { clientData } = getConnectionData();
  return clientData;
});

const getConnectionData = () => {
  const { connection } = props.streamManager.stream;
  return JSON.parse(connection.data);
};
</script>

<style scoped>
#ovVideoContainer {
  position: relative;
}
#ovVideo:hover ~ #nickname {
  display: block;
}
#nickname {
  position: absolute;
  bottom: 16px;
  left: 20px;
  display: none;
  background-color: black;
  padding: 2px;
}

.blinking {
  animation: blink 0.5s ease-in-out infinite alternate;
}
@keyframes blink {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}
</style>
