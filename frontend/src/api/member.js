import { apiInstance } from "./index.js";

const api = apiInstance();

// async function login(user, success, fail) {
//   await api.post(`/user/login`, JSON.stringify(user)).then(success).catch(fail);
// }

async function findByAccessToken(userid, accessToken, success, fail) {
  console.log(userid);
  console.log(accessToken);
  // api.defaults.headers["access-token"] = accessToken;
  console.log(api.defaults.headers);
  await api
    .get(`/api/members/${userid}`, {
      headers: {
        "access-token": accessToken,
      },
    })
    .then(success)
    .catch(fail);
}

async function accesstokenReissue(user, success, fail) {
  api.defaults.headers["refresh-token"] =
    sessionStorage.getItem("refresh-token"); //axios header에 refresh-token 셋팅
  await api.post(`/user/refresh`, user).then(success).catch(fail);
}

async function logout(userid, success, fail) {
  await api.get(`/user/logout/${userid}`).then(success).catch(fail);
}

async function deleteUser(id, success, fail) {
  await api.delete(`/user/delete/${id}`).then(success).catch(fail);
}

export { findByAccessToken, accesstokenReissue, logout, deleteUser };
