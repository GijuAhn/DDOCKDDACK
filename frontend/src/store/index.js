import { createStore } from "vuex";
import { gameMakeStore } from "@/store/modules/gameMakeStore";

export default createStore({
  modules: { gameMakeStore },
});
