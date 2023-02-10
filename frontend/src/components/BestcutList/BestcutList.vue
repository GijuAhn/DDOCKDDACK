<template>
  <div id="view">
    <bestcut-detail
      class="modal"
      v-if="detailBestcut"
      :bestcut="detailBestcut"
      @bestcutLike="(bestcutId) => bestcutLike(bestcutId)"
      @bestcutDislike="(bestcutId) => bestcutDislike(bestcutId)"
      @openReportModal="(bestcutId) => openReportModal(bestcutId)"
    ></bestcut-detail>
    <div id="searchBar">
      <span>
        <button id="btn-p" :class="tabP" @click="sortGames('P')">인기순</button>
        <button id="btn-r" :class="tabR" @click="sortGames('R')">최신순</button>
      </span>
      <span>
        <select name="choice" v-model="pageConditionReq.period">
          <option value="ALL" selected>전체기간</option>
          <option value="DAY">1일</option>
          <option value="WEEK">1주</option>
          <option value="MONTH">1개월</option>
          <option value="HALF_YEAR">6개월</option>
        </select>
      </span>
      <span>
        <select name="choice" v-model="pageConditionReq.search">
          <option value="GAME" selected>게임 제목</option>
          <option value="MEMBER">닉네임</option>
        </select>
      </span>
      <span>
        <input
          @keyup.enter="callApi"
          type="text"
          placeholder="검색어를 입력해주세요"
          v-model.trim="pageConditionReq.keyword"
        />
        <button id="btn-s" @click="callApi">검색</button>
      </span>
    </div>

    <div id="list">
      <normal-bestcut
        v-for="bestcut in bestcuts"
        :key="bestcut"
        :bestcut="bestcut"
        @bestcutDetail="detailBestcut = bestcut"
        @bestcutLike="(bestcutId) => bestcutLike(bestcutId)"
        @bestcutDislike="(bestcutId) => bestcutDislike(bestcutId)"
        @openReportModal="(bestcutId) => openReportModal(bestcutId)"
      ></normal-bestcut>
    </div>
    <page-nav
      :totalPageCount="totalPages"
      :value="pageConditionReq.page"
      @change="(num) => changePage(num)"
    ></page-nav>
  </div>
</template>

<script setup>
import PageNav from "@/components/common/PageNav.vue";
import NormalBestcut from "@/components/BestcutList/item/NormalBestcut.vue";
import BestcutDetail from "@/components/common/modal/BestcutDetailModal.vue";

import { apiInstance } from "@/api/index";
import { ref, computed } from "vue";
import { useStore } from "vuex";

const api = apiInstance();
const store = useStore();
const accessToken = computed(() => store.state.memberStore.accessToken).value;
const tabP = ref("on");
const tabR = ref("off");

let bestcuts = ref();
let pageConditionReq = ref({
  order: "POPULARITY",
  period: "MONTH",
  search: "GAME",
  keyword: "",
  page: 1,
});
let totalPages = ref();
let detailBestcut = ref();

const callApi = () => {
  api
    .get(`/api/bestcuts`, {
      params: {
        order: pageConditionReq.value.order,
        period: pageConditionReq.value.period,
        search: pageConditionReq.value.search,
        keyword: pageConditionReq.value.keyword,
        page: pageConditionReq.value.page,
      },
      headers: { "access-token": accessToken },
    })
    .then((response) => {
      bestcuts.value = response.data.content;
      totalPages = response.data.totalPages;
    })
    .catch((error) => {
      console.log(error);
    });
};

callApi();

//정렬
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

//페이징 이동
const changePage = (page) => {
  pageConditionReq.value.page = page;
  callApi();
};

//베스트컷 좋아요
const bestcutLike = (bestcutId) => {
  api
    .post(
      `/api/bestcuts/like/${bestcutId}`,
      {},
      { headers: { "access-token": accessToken } }
    )
    .then(() => {
      let bestcut = bestcuts.value.find((e) => e.bestcutId === bestcutId);
      bestcut.isLiked = true;
      bestcut.popularity++;
    })
    .catch((err) => {
      if (err.response.status === 401) {
        alert("로그인 후 이용해주세요.");
      }
    });
};

//베스트컷 좋아요 취소
const bestcutDislike = (bestcutId) => {
  if (!accessToken.value) {
    alert("로그인 후 이용해주세요.");
    return;
  }
  api
    .delete(`/api/bestcuts/dislike/${bestcutId}`, {
      headers: { "access-token": accessToken },
    })
    .then(() => {
      let bestcut = bestcuts.value.find((e) => e.bestcutId === bestcutId);
      bestcut.isLiked = false;
      bestcut.popularity--;
    });
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
  background-color: #77a4cc;
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
  background-color: #77a4cc;
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
