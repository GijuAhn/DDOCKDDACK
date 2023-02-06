import router from "@/router";
import {
  login,
  findByAccessToken,
  tokenRegeneration,
  logout,
} from "@/api/member";

export const memberStore = {
  namespaced: true,
  state: {
    accessToken:
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2NzU2ODIzMDUsImV4cCI6MTY3NTY5MDk0NX0.QWOv54vIg9v2r4AVZ1S8BK2If385XZCkMmfJTY2BEZ4",
    memberInfo: {
      id: 1,
      nickname: "이종민",
      email: "l6778@naver.com",
      profile: "abc2.jpg",
      Role: "MEMBER",
    },
    isLogin: false,
    isValidToken: false,
  },
  getters: {
    getAccessToken(state) {
      return state.accessToken;
    },
    checkToken(state) {
      return state.isValidToken;
    },
    checkMemberInfo(state) {
      return state.memberInfo;
    },
    getMemberInfo(state) {
      return state.memberInfo;
    },
  },
  mutations: {
    setToken(state, value) {
      state.accessToken = value;
    },
    setMemberInfo(state, memberInfo) {
      state.memberInfo = memberInfo;
    },
    setIsLogin(state, isLogin) {
      state.isLogin = isLogin;
    },
    setIsValidToken(state, isValidToken) {
      state.isValidToken = isValidToken;
    },
  },
  actions: {
    setTokensAsync({ commit }, value) {
      commit("setToken", value);
    },
    // setMemberInfo({ commit }, value) {
    //   console.log("setMemberInfo: ", value);
    //   commit("setMemberInfo", value);
    // },
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

    async getMemberInfo({ commit, dispatch, state }, id) {
      let accessToken = state.accessToken;
      await findByAccessToken(
        id,
        accessToken,
        ({ data }) => {
          console.log(data);
          if (data) {
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
    async accesstokenReissue({ commit, state }) {
      await tokenRegeneration(
        JSON.stringify(state.memberInfo),
        ({ data }) => {
          if (data.status === 200) {
            let accessToken = data.accessToken;
            console.log("재발급 완료 >> 새로운 토큰 : {}", accessToken);
            commit("SET_ACCESSTOKEN", data.memberInfo);
            commit("SET_IS_VALID_TOKEN", true);
          }
        },
        async (error) => {
          //AccessToken 갱신 실패시 refreshToken이 문제임 >> 다시 로그인해야함
          if (error.reembersponse.status === 401) {
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
                commit("SET_IS_LOGIN", false);
                commit("SET_MEMBER_INFO", null);
                commit("SET_IS_VALID_TOKEN", false);
                router.push({ name: "login" });
              },
              (error) => {
                console.log(error);
                commit("SET_IS_LOGIN", false);
                commit("SET_MEMBER_INFO", null);
              }
            );
          }
        }
      );
    },
  },

  async userLogout({ commit }, id) {
    await logout(
      id,
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
