export const gameMakeStore = {
  namespaced: true,
  state: {
    images: null,
  },
  getters: {},
  mutations: {
    clearProblems(state) {
      state.images = null;
    },
    setProblems(state, value) {
      state.images = value;
    },
    removeLine(state, value) {
      const index = state.images.indexOf(value);
      state.images.splice(index, 1);
    },
  },
  actions: {
    clearProblemsAsync({ commit }) {
      commit("clearProblems");
    },
    setProblemsAsync({ commit }, array) {
      commit("setProblems", array);
    },
    removeLineAsync({ commit }, item) {
      commit("removeLine", item);
    },
  },
};
