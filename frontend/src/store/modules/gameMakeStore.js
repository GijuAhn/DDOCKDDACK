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
  },
  actions: {
    setProblemsAsync({ commit }, array) {
      commit("setProblems", array);
    },
  },
};
