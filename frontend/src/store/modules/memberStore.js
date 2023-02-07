import router from "@/router";
import {
  login,
  findByAccessToken,
  accesstokenRegeneration,
  logout,
} from "@/api/member";

export const memberStore = {
  namespaced: true,
  state: {
    accessToken: undefined,
    memberInfo: {
      memberId: "",
      email: "",
      nickname: "",
      profile: "",
      role: "",
    },
  },
  getters: {
    getAccessToken(state) {
      return state.accessToken;
    },
  },
  mutations: {
    setToken(state, accessToken) {
      state.accessToken = accessToken;
    },
    setMemberInfo(state, memberInfo) {
      state.memberInfo = memberInfo;
    },
  },
  actions: {
    setTokensAsync({ commit }, accessToken) {
      commit("setToken", accessToken);
    },
    setMemberInfo({ commit }, memberInfo) {
      console.log("setMemberInfo: ", memberInfo);
      commit("setMemberInfo", memberInfo);
    },
    async userConfirm({ commit }, user) {
      // console.log(user,"^^");
      await login(
        //accessToken과 refreshToken이 생성되게
        user,
        ({ data }) => {
          console.log(data, "^^");
          if (data.status === 200) {
            // let accessToken = data["access-token"];
            // let refreshToken = data["refresh-token"];
            // console.log("login success token created!!!! >> ", accessToken, refreshToken);
            commit("SET_IS_LOGIN", true);
            commit("SET_IS_LOGIN_ERROR", false);
            commit("SET_IS_VALID_TOKEN", true);

            // sessionStorage.setItem("access-token", accessToken); //변수에
            // sessionStorage.setItem("refresh-token", refreshToken); //cookie에
          } else {
            commit("SET_IS_LOGIN", false);
            commit("SET_IS_LOGIN_ERROR", true);
            commit("SET_IS_VALID_TOKEN", false);
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },

    async getMemberInfo({ commit, dispatch, state }) {
      let accessToken = state.accessToken;
      console.log("get member", accessToken);
      await findByAccessToken(
        accessToken,
        ({ data }) => {
          console.log(data);
          if (data.message === "success") {
            // console.log("getMemberInfo data >> ", data);
            commit("setMemberInfo", data);
          } else {
            console.log("유저 정보 없음!!!!");
          }
        },
        async (error) => {
          console.log(
            "getMemberInfo() error code [토큰 만료되어 사용 불가능.] ::: ",
            error.response.status
          );
          commit("SET_IS_VALID_TOKEN", false);
          await dispatch("accesstokenReissue");
        }
      );
    },

    async accesstokenReissue({ commit, state }, isAuthPage) {
      await accesstokenRegeneration(
        ({ data }) => {
          console.log("!@#@#@#", data);
          if (data) {
            let accessToken = data;
            console.log("재발급 완료 >> 새로운 토큰 : {}", accessToken);
            commit("setToken", accessToken);
          }
        },
        async (error) => {
          //AccessToken 갱신 실패시 refreshToken이 문제임 >> 다시 로그인해야함
          if (error.status === 401 && isAuthPage) {
            console.log("갱신 실패");
            await logout(
              state.memberInfo.id,
              ({ data }) => {
                if (data.status === 200) {
                  console.log("리프레시 토큰 제거 성공");
                } else {
                  console.log("리프레시 토큰 제거 실패");
                }
                alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
                router.push({ name: "login" });
              },
              (error) => {
                console.log(error);
              }
            );
          }
        }
      );
    },
  },

  async userLogout({ commit }, accessToken) {
    await logout(
      accessToken,
      ({ data }) => {
        if (data.status === 200) {
          commit("SET_IS_LOGIN", false);
          commit("SET_USER_INFO", null);
          commit("SET_IS_VALID_TOKEN", false);
        } else {
          console.log("유저 정보 없음!!!!");
        }
      },
      (error) => {
        console.log(error);
      }
    );
  },
};
