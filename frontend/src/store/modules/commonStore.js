export const commonStore = {
  namespaced: true,
  state: {
    view: "",
  },
  getters: {},
  mutations: {
    setColor(state, value) {
      state.view = value;
    },
  },
  actions: {
    setColorAsync({ commit }, value) {
      commit("setColor", value);
    },
  },
};
