import { createRouter, createWebHistory } from "vue-router";
import MainView from "@/views/MainView.vue";

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
    redirect: "/member/recentGame",
    children: [
      {
        path: "recentGame",
        name: "recentGame",
        component: () => import("@/components/Member/RecentGameList.vue"),
      },
      {
        path: "starGame",
        name: "starGame",
        component: () => import("@/components/Member/StarGameList.vue"),
      },
      {
        path: "myGame",
        name: "myGame",
        component: () => import("@/components/Member/MyGameList.vue"),
      },
      {
        path: "myBestcut",
        name: "myBestcut",
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
        component: () => import("@/components/Admin/GameList.vue"),
      },
      {
        path: "bestcut",
        name: "bestcut",
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
