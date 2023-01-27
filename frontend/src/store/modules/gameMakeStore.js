export const gameMakeStore = {
  namespaced: true,
  state: {
    savedProblems: null,
    uploadImages: null,
  },
  getters: {},
  mutations: {
    setProblems(state, value) {
      state.savedProblems = value;
    },
    removeLine(state, value) {
      const index = state.savedProblems.indexOf(value);
      state.savedProblems.splice(index, 1);
    },
  },
  actions: {
    setProblemsAsync({ commit }, array) {
      commit("setProblems", array);
    },
    removeLineAsync({ commit }, item) {
      commit("removeLine", item);
    },
  },
};
