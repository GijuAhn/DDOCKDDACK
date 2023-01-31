import { createStore } from "vuex";
import { memberStore } from "./modules/memberStore";

export default createStore({
  modules: { memberStore },
});
