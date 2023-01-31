export const memberStore = {
  namespaced: true,
  state: {
    accessToken: 1,
  },
  getters: {},
  mutations: {
    setToken(state, value) {
      state.accessToken = value;
    },
  },
  actions: {
    setTokensAsync({ commit }, value) {
      commit("setToken", value);
    },
  },
};
