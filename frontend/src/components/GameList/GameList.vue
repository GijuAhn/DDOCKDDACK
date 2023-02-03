<template>
  <div id="view">
    <div id="searchBar">
      <span>
        <button id="btn-p" :class="tabP" @click="sortGames('P')">인기순</button>
        <button id="btn-r" :class="tabR" @click="sortGames('R')">최신순</button>
      </span>
      <span>
        <select name="choice">
          <option value="1" selected>전체기간</option>
          <option value="2">1일</option>
          <option value="3">1주</option>
          <option value="4">1개월</option>
          <option value="5">6개월</option>
        </select>
      </span>
      <span>
        <select name="choice">
          <option value="1" selected>게임 제목</option>
          <option value="2">제작자</option>
        </select>
      </span>
      <span>
        <input type="text" placeholder="검색어를 입력해주세요" />
        <button id="btn-s">검색</button>
      </span>
    </div>
    <div id="list">
      <normal-game v-for="game in games" :key="game" :game="game"></normal-game>
    </div>
  </div>
</template>

<script setup>
import NormalGame from "@/components/GameList/item/NormalGame";

import { apiInstance } from "@/api/index";
import { ref } from "vue";

const tabP = ref("on");
const tabR = ref("off");

const api = apiInstance();
let games = ref();
let pageConditionReq = ref({
  order: "POPULARITY",
  period: "ALL",
  search: "GAME",
  keyword: "",
  page: 1,
});
const callApi = () => {
  api
    .get(`/api/games`, {
      params: {
        order: pageConditionReq.value.order,
        period: pageConditionReq.value.period,
        search: pageConditionReq.value.search,
        keyword: pageConditionReq.value.keyword,
        page: pageConditionReq.value.page,
      },
    })
    .then((response) => {
      console.log(response);
      games.value = response.data.content;
    })
    .catch((error) => {
      console.log(error);
    });
};

callApi();

const sortGames = (option) => {
  //변경 이벤트 발생시
  if (option === "P") {
    pageConditionReq.value.order = "POPULARITY";
    tabP.value = "on";
    tabR.value = "off";
  } else {
    pageConditionReq.value.order = "RECENT";
    tabP.value = "off";
    tabR.value = "on";
  }
  callApi();
};
</script>

<style scoped>
#view {
  border: 2px solid black;
  width: 1060px;
  position: relative;
  top: -320px;
  left: 50%;
  transform: translate(-50%, 0);
  background-color: white;
  padding: 70px;
}
#searchBar {
  margin-bottom: 70px;
}
#searchBar > span {
  margin: 0 15px;
}
#btn-p {
  margin-left: -15px;
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
  border-top: 2px solid black;
  border-left: 2px solid black;
  border-bottom: 2px solid black;
  border-right: none;
  font-size: 20px;
  font-family: "NanumSquareRoundB";
  display: inline-block;
  height: 48px;
  width: 99px;
}
#btn-p:hover {
  cursor: pointer;
}
#btn-r {
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
  border-top: 2px solid black;
  border-left: 2px solid black;
  border-bottom: 2px solid black;
  border-right: 2px solid black;
  font-size: 20px;
  font-family: "NanumSquareRoundB";
  display: inline-block;
  height: 48px;
  width: 101px;
}
#btn-r:hover {
  cursor: pointer;
}

input {
  outline: none;
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
  border-top: 2px solid black;
  border-left: 2px solid black;
  border-bottom: 2px solid black;
  border-right: none;
  font-size: 20px;
  font-family: "NanumSquareRoundB";
  padding: 0 10px;
  height: 44px;
  width: 365px;
}

#btn-s {
  background-color: white;
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
  border-top: 2px solid black;
  border-left: 2px solid black;
  border-bottom: 2px solid black;
  border-right: 2px solid black;
  font-size: 20px;
  font-family: "NanumSquareRoundB";
  display: inline-block;
  height: 48px;
  background-color: #f08383;
  color: white;
  width: 80px;
  margin-right: -15px;
}
#btn-s:hover {
  cursor: pointer;
}

select {
  width: 150px;
  border: 2px solid black;
  font-size: 20px;
  font-family: "NanumSquareRoundB";
  display: inline-block;
  height: 48px;
  border-radius: 5px;
  text-align: center;
}
select:focus {
  border-radius: 5px 5px 0 0;
}

.on {
  background-color: #f08383;
  color: white;
}
.off {
  background-color: white;
  color: black;
}

#list {
  display: grid;
  gap: 35px 0;
  grid-template-columns: repeat(3, 1fr);
  width: 1090px;
}
</style>
