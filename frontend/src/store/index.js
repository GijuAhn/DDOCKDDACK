import { createStore } from "vuex";
import { memberStore } from "@/store/modules/memberStore";
import { commonStore } from "@/store/modules/commonStore";

export default createStore({
  modules: { memberStore, commonStore },
});
