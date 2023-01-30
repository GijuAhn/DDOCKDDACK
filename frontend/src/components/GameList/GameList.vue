<template>
  <div id="searchBar">
    <div>
      <button @click="sortGames('P')">인기순</button>
      <button @click="sortGames('R')">최신순</button>
    </div>
    <div>
      <select name="choice">
        <option value="1" selected>전체기간</option>
        <option value="2">1일</option>
        <option value="3">1주</option>
        <option value="4">1개월</option>
        <option value="5">6개월</option>
      </select>
    </div>
    <div>
      <select name="choice">
        <option value="1" selected>게임 제목</option>
        <option value="2">제작자</option>
      </select>
    </div>
    <div>
      <input type="text" />
      <button>검색</button>
    </div>
  </div>
  <div id="list">
    <normal-game v-for="game in games" :key="game" :game="game"></normal-game>
  </div>
</template>

<script setup>
import NormalGame from "@/components/GameList/item/NormalGame";

import { apiInstance } from "@/api/index";
import { ref } from "vue";

const api = apiInstance();
let games = ref();
let selectedOption = ref("POPULARITY");
const callApi = () => {
  api
    .get(`/games`, {
      params: {
        order: selectedOption.value,
        // period: "ALL", //작동안됨
        // search: "GAME",
        // keyword: "",
        // page: 1,
      },
    })
    .then(({ data }) => {
      //console.log(data.content);
      games.value = data.content;
    })
    .catch((error) => {
      console.log(error);
    });
};

callApi();

const sortGames = (option) => {
  if (option === "P") {
    selectedOption.value = "POPULARITY";
  } else {
    selectedOption.value = "RECENT";
  }
  callApi();
};
</script>

<style></style>
