import { createStore } from "vuex";
import { gameMakeStore } from "@/store/modules/gameMakeStore";
import { memberStore } from "@/store/modules/memberStore";

export default createStore({
  modules: { gameMakeStore, memberStore },
});
