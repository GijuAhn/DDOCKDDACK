<template>
  <div>
    <img
      :src="
        require(`@/../../Backend/images/game-images/${props.bestcut.gameImgUrl}`)
      "
      alt="원본사진"
      width="200"
      height="200"
      @click="bestcutDetail"
    />
    <img
      :src="
        require(`@/../../Backend/images/bestcuts/${props.bestcut.bestcutImgUrl}`)
      "
      alt="베스트컷"
      width="200"
      height="200"
    />
    <img
      :src="
        require(`@/../../Backend/images/bestcuts/${props.bestcut.profileImgUrl}`)
      "
      alt="유저프로필사진"
      width="30"
      height="30"
    />
    {{ props.bestcut.bestcutId }}
    {{ props.bestcut.nickname }}
    {{ props.bestcut.bestcutImgTitle }}
    {{ props.bestcut.gameTitle }}
    {{ props.bestcut.gameImgDesc }}
    <button @click="openReportModal(props.bestcut.bestcutId)">신고</button>
    <button
      v-if="!props.bestcut.isLiked"
      @click="bestcutLike(props.bestcut.bestcutId)"
    >
      좋아요 {{ props.bestcut.popularity }}
    </button>
    <button v-else @click="bestcutDislike(props.bestcut.bestcutId)">
      좋아요 취소{{ props.bestcut.popularity }}
    </button>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";

const props = defineProps({ bestcut: Object });
const emit = defineEmits(["bestcutDetail", "bestcutLike", "bestcutDislike"]);

const bestcutDetail = () => {
  emit("bestcutDetail");
};

const bestcutLike = (bestcutId) => {
  emit("bestcutLike", bestcutId);
};

const bestcutDislike = (bestcutId) => {
  emit("bestcutDislike", bestcutId);
};

const openReportModal = (bestcutId) => {
  emit("openReportModal", bestcutId);
};
</script>

<style></style>
