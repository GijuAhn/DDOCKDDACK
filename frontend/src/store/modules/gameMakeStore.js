export const gameMakeStore = {
  namespaced: true,
  state: {
    images: [],
  },
  getters: {},
  mutations: {
    clearImages(state) {
      state.images = [];
    },
    setImages(state, value) {
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
    clearImagesAsync({ commit }) {
      commit("clearImages");
    },
    setImagesAsync({ commit }, array) {
      commit("setImages", array);
    },
    removeLineAsync({ commit }, item) {
      commit("removeLine", item);
    },
  },
};
