import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vueClickOutsideElement from "vue-click-outside-element";
import VueCookies from "vue3-cookies";

createApp(App)
  .use(store)
  .use(router)
  .use(vueClickOutsideElement)
  .use(VueCookies)
  .mount("#app");
