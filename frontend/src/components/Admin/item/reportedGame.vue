<template>
  <tr>
    <td>{{ props.reportedGame.gameId }}</td>
    <td>{{ props.reportedGame.reason }}</td>
    <td>{{ props.reportedGame.reportMemberId }}</td>
    <td>{{ props.reportedGame.reportedMemberId }}</td>
    <td><input v-model="visible" type="checkbox" /></td>
    <td><button>처리</button></td>
  </tr>
  <tr v-if="visible">
    <td colspan="6">
      <div class="preview">
        <reported-modal-preview :gameId="gameId"></reported-modal-preview>
      </div>
    </td>
  </tr>
</template>

<script setup>
import { defineProps, ref, computed } from "vue";
import reportedModalPreview from "@/components/Admin/item/reportedModalPreview.vue";

const props = defineProps({ reportedGame: Object });
const visible = ref();
const gameId = computed(() => {
  return props.reportedGame.gameId;
});
</script>

<style scoped>
.preview {
  display: flex;
  justify-content: center; /* flex-start / center / flex-end */
}

td {
  height: 50px;
  border-bottom: 1px solid #737373;
}

input {
  margin-top: 3px;
  width: 25px;
  height: 25px;
}
</style>
