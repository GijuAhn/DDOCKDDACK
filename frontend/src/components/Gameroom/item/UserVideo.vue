<template>
  <div id="ovVideoContainer" v-if="props.streamManager">
    <ov-video
      id="ovVideo"
      :class="{ blinking: captureMode }"
      :stream-manager="props.streamManager"
    />

    <div id="nickname" v-if="props.isSub">
      <span>{{ clientData }}</span> /
      <span>
        <svg-icon
          v-if="isSubVideoEnable"
          @click="subVideoChange"
          type="mdi"
          :path="path[0]"
        ></svg-icon>
        <svg-icon
          v-if="!isSubVideoEnable"
          @click="subVideoChange"
          type="mdi"
          :path="path[1]"
        ></svg-icon>
      </span>
      <span>
        <svg-icon
          v-if="isSubAudioEnable"
          @click="subAudioChange"
          type="mdi"
          :path="path[2]"
        ></svg-icon>
        <svg-icon
          v-if="!isSubAudioEnable"
          @click="subAudioChange"
          type="mdi"
          :path="path[3]"
        ></svg-icon>
      </span>
    </div>
  </div>
</template>

<script setup>
import OvVideo from "./OvVideo";

import { computed, defineProps, ref } from "vue";

import SvgIcon from "@jamescoyle/vue-icon";
import {
  mdiMonitor,
  mdiMonitorOff,
  mdiVolumeHigh,
  mdiVolumeOff,
} from "@mdi/js";

const isSubVideoEnable = ref(true);
const isSubAudioEnable = ref(true);
const props = defineProps({
  streamManager: Object,
  captureMode: Boolean,
  isSub: Boolean,
});
const path = ref([mdiMonitor, mdiMonitorOff, mdiVolumeHigh, mdiVolumeOff]);

const clientData = computed(() => {
  const { clientData } = getConnectionData();
  return clientData;
});

const getConnectionData = () => {
  const { connection } = props.streamManager.stream;
  return JSON.parse(connection.data);
};

const subVideoChange = () => {
  isSubVideoEnable.value = !isSubVideoEnable.value;
  props.streamManager.subscribeToVideo(isSubVideoEnable.value);
};

const subAudioChange = () => {
  isSubAudioEnable.value = !isSubAudioEnable.value;
  props.streamManager.subscribeToAudio(isSubAudioEnable.value);
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
#nickname:hover {
  display: block;
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
#img {
  height: 10px;
  width: 10px;
  background: white;
}
</style>
