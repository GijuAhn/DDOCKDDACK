import { createStore } from "vuex";
import { commonStore } from "@/store/modules/commonStore";

export default createStore({
  modules: { commonStore },
});
