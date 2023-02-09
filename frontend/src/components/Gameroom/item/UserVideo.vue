<template>
  <div>
    <div v-if="props.streamManager">
      <ov-video
        class="child1"
        :class="{ blinking: captureMode }"
        :stream-manager="props.streamManager"
      />
      <div>
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
