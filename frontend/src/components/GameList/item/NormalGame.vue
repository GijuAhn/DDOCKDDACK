<template>
  <div id="content">
    <div>
      <img
        :src="
          require(`@/../../Backend/images/${props.game.gameId}/${props.game.thumbnail}`)
        "
        alt="대표사진"
        width="325"
        height="260"
      />
    </div>
    <div id="gameTitle">
      <span>{{ props.game.gameTitle }}</span>
    </div>
    <div id="gameDesc">
      <span>{{ props.game.gameDesc }}</span>
    </div>
    <div id="createRoomButton"><button>방 생성</button></div>
    <div id="etcButton"><button>etc</button></div>
  </div>
</template>

<script setup>
import { defineProps } from "vue";

const props = defineProps(["game"]);

// console.log(props.game);
</script>

<style scoped>
#content {
  border: 2px solid black;
  width: 325px;
  height: 380px;
}
#createRoomButton button {
  border: 1px solid #da6c6b;
  background-color: white;
  font-size: 14px;
  font-family: "NanumSquareRoundB";
  width: 300px;
  padding: 10px 0;
  color: #da6c6b;
}
#createRoomButton button:hover {
  color: white;
  background-color: #da6c6b;
}
#gameTitle span {
  font-size: 22px;
}
#gameDesc span {
  font-size: 16px;
  color: #656565;
}
</style>
