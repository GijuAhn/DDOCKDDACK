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
    redirect: "/gameMake/setMain",
    children: [
      {
        path: "setMain",
        name: "setMain",
        component: () => import("@/components/GameMake/SetMain.vue"),
      },
    ],
  },
  {
    path: "/bestcut",
    name: "bestcut",
    component: () => import("@/views/BestcutView.vue"),
  },
  {
    path: "/gameroom",
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
    path: "/report",
    name: "report",
    component: () => import("@/views/ReportView.vue"),
    redirect: "/report/game",
    children: [
      {
        path: "game",
        name: "game",
        component: () => import("@/components/Report/GameList.vue"),
      },
      {
        path: "bestcut",
        name: "bestcut",
        component: () => import("@/components/Report/BestcutList.vue"),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
