<template>
  <div id="modal">
    <div id="modalBackground" @click="setCurrentModalAsync"></div>
    <preview-modal
      class="modalContent"
      v-if="currentModal.name === `preview`"
    />
    <report-reason-modal
      class="modalContent"
      v-if="currentModal.name === `reportReason`"
    />
    <report-complete-modal
      class="modalContent"
      v-if="currentModal.name === `reportComplete`"
    />
  </div>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

store.dispatch("commonStore/setColorAsync", "variant1");

import { computed } from "vue";

const currentModal = computed(() => store.state.commonStore.currentModal);

const setCurrentModalAsync = () => {
  store.dispatch("commonStore/setCurrentModalAsync", "");
};

import PreviewModal from "@/components/common/modal/PreviewModal";
import ReportReasonModal from "@/components/common/modal/ReportReasonModal";
import ReportCompleteModal from "@/components/common/modal/ReportCompleteModal";
</script>

<style scoped>
#modal {
  position: fixed;
  min-width: calc(1920px - 100px);
  width: 100%;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}
#modalBackground {
  width: 100%;
  height: 100%;
  background-color: black;
  opacity: 0.5;
}
.modalContent {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
