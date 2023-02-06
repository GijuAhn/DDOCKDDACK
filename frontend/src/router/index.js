import { createRouter, createWebHistory } from "vue-router";
// import { useStore } from "vuex";
import { computed } from "vue";
import store from "@/store";
import MainView from "@/views/MainView.vue";

const authMember = async (to, from, next) => {
  // const store = useStore();
  console.log("Asdfasf");
  const checkMemberInfo = computed(
    () => store.state.memberStore.memberInfo
  ).value;
  const checkToken = computed(() => store.state.memberStore.isValidToken).value;
  let accessToken = computed(() => store.state.memberStore.accessToken).value;
  console.log("로그인 처리 전", checkMemberInfo, accessToken);

  if (checkMemberInfo != null && accessToken) {
    await store.dispatch("memberStore/getMemberInfo", checkMemberInfo.id);
  }
  if (!checkToken || checkMemberInfo === null) {
    alert("로그인이 필요한 페이지입니다..");
    // router.push({ name: "login" }); //login 모달로
  } else {
    console.log("로그인 했다!!!!!!!!!!!!!.");
    next();
  }
  next();
};

const routes = [
  {
    path: "/",
    name: "main",
    component: MainView,
  },
  {
    path: "/gameList",
    name: "gameList",
    component: () => import("@/views/GameListView.vue"),
  },
  {
    path: "/gameMake",
    name: "gameMake",
    component: () => import("@/views/GameMakeView.vue"),
    redirect: "/gameMake/createGame",
    children: [
      {
        path: "createGame",
        name: "createGame",
        // beforeEnter: authMember, --> 로그인된 회원만 됨
        component: () => import("@/components/GameMake/CreateGame.vue"),
      },
    ],
  },
  {
    path: "/bestcut",
    name: "bestcutList", //bestcut 중복 체크
    component: () => import("@/views/BestcutView.vue"),
  },
  {
    path: "/gameroom/:pinNumber",
    name: "gameroom",
    component: () => import("@/views/GameroomView.vue"),
  },
  {
    path: "/member",
    name: "member",
    component: () => import("@/views/MemberView.vue"),
    redirect: "/member/myBestcut", // /member/recentGame 기본
    children: [
      {
        path: "recentGame",
        name: "recentGame",
        beforeEnter: authMember,
        component: () => import("@/components/Member/RecentGameList.vue"),
      },
      {
        path: "starGame",
        name: "starGame",
        beforeEnter: authMember,
        component: () => import("@/components/Member/StarGameList.vue"),
      },
      {
        path: "myGame",
        name: "myGame",
        beforeEnter: authMember,
        component: () => import("@/components/Member/MyGameList.vue"),
      },
      {
        path: "myBestcut",
        name: "myBestcut",
        beforeEnter: authMember,
        component: () => import("@/components/Member/MyBestcutList.vue"),
      },
    ],
  },
  {
    path: "/admin",
    name: "admin",
    component: () => import("@/views/AdminView.vue"),
    redirect: "/admin/game",
    children: [
      {
        path: "game",
        name: "game",
        beforeEnter: authMember,
        component: () => import("@/components/Admin/GameList.vue"),
      },
      {
        path: "bestcut",
        name: "bestcut",
        beforeEnter: authMember,
        component: () => import("@/components/Admin/BestcutList.vue"),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
