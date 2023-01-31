<template>
  <div>
    <select name="choice" v-model="reason">
      <option value="DISGUSTING" selected>혐오감을 유발하는 컨텐츠</option>
      <option value="SPAM">스팸 또는 혼동을 야기하는 컨텐츠</option>
      <option value="VIOLENT">폭력적인 컨텐츠</option>
      <option value="SEXUAL">선정적인 컨텐츠</option>
    </select>

    <button @click="report">신고</button>
    <button @click="cancel">취소</button>
  </div>
</template>

<script setup>
import { ref, defineEmits } from "vue";

let reason = ref("DISGUSTING");

const emit = defineEmits(["report", "cancel"]);

const report = () => {
  emit("report", reason.value);
};

const cancel = () => {
  emit("cancel");
};
</script>

<style></style>
