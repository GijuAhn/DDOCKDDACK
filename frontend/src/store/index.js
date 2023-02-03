import { createStore } from "vuex";
import { gameMakeStore } from "@/store/modules/gameMakeStore";
import { memberStore } from "@/store/modules/memberStore";
import { commonStore } from "@/store/modules/commonStore";

export default createStore({
  modules: { gameMakeStore, memberStore, commonStore },
});