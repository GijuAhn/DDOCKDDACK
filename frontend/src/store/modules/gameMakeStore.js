export const gameMakeStore = {
  namespaced: true,
  state: {
    images: [],
  },
  getters: {},
  mutations: {
    clearProblems(state) {
      state.images = [];
    },
    setProblems(state, value) {
      value.forEach((e) => {
        state.images.push(e);
      });
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
