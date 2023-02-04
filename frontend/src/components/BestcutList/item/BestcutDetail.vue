<template>
  <div>
    {{ props.bestcut.bestcutImgTitle }}
    <button
      v-if="!props.bestcut.isLiked"
      @click="bestcutLike(props.bestcut.bestcutId)"
    >
      좋아요 {{ props.bestcut.popularity }}
    </button>
    <button v-else @click="bestcutDislike(props.bestcut.bestcutId)">
      좋아요 취소{{ props.bestcut.popularity }}
    </button>
    <button @click="openReportModal(props.bestcut.bestcutId)">신고</button>
    <img
      :src="`https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Default_pfp.svg/340px-Default_pfp.svg.png?20220226140232`"
      alt="유저프로필사진"
      width="30"
      height="30"
    />
    <span>{{ props.bestcut.nickname }}</span> |
    <span>{{ props.bestcut.createdDate }}</span>
    <img
      :src="`${GAMEIMAGES_PATH}/${props.bestcut.gameImgUrl}`"
      alt="원본사진"
      width="200"
      height="200"
    />
    <img
      :src="`${BESTCUTS_PATH}/${props.bestcut.bestcutImgUrl}`"
      alt="베스트컷"
      width="200"
      height="200"
    />
    {{ props.bestcut.gameTitle }}
    {{ props.bestcut.gameImgDesc }}
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";

const props = defineProps({ bestcut: Object });
const GAMEIMAGES_PATH = process.env.VUE_APP_GAMEIMAGES_PATH;
const BESTCUTS_PATH = process.env.VUE_APP_BESTCUTS_PATH;

const emit = defineEmits(["bestcutDetail", "bestcutLike", "bestcutDislike"]);

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
