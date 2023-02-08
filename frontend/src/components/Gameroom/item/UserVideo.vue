<template>
  <div>
    <div v-if="props.streamManager" style="width: 100%; height: 100%">
      <ov-video
        class="child1"
        :class="{ blinking: resultMode }"
        :stream-manager="props.streamManager"
      />

      <div v-if="false">
        <p>{{ clientData }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import OvVideo from "./OvVideo";

import { computed, defineProps } from "vue";

const props = defineProps({
  streamManager: Object,
  timerCount: Number,
  isEnd: Boolean,
  isStart: Boolean,
  round: Number,
  room: Object,
  resultMode: Boolean,
  who: String,
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
