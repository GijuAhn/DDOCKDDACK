import { apiInstance } from "./index.js";

const api = apiInstance();

// async function login(user, success, fail) {
//   await api.post(`/user/login`, JSON.stringify(user)).then(success).catch(fail);
// }

async function findByAccessToken(accessToken, success, fail) {
  console.log(accessToken);
  await api
    .get(`/api/members/`, {
      headers: {
        "access-token": accessToken,
      },
    })
    .then(success)
    .catch(fail);
}

async function accesstokenRegeneration(success, fail) {
  await api.get(`/api/token/refresh`).then(success).catch(fail);
}

async function logout(userid, success, fail) {
  await api.get(`/api/token/logout/${userid}`).then(success).catch(fail);
}

async function deleteUser(id, success, fail) {
  await api.delete(`/api/user/delete/${id}`).then(success).catch(fail);
}

export { findByAccessToken, accesstokenRegeneration, logout, deleteUser };
