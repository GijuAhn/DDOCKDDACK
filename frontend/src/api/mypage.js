import { apiInstance } from "./index.js";

const api = apiInstance();
const headers = response.headers.get("Authorization");
const userid = "";
// async function login(user, success, fail) {
//   await api.post(`/user/login`, JSON.stringify(user)).then(success).catch(fail);
// }

async function getMyBestcut(params, success, fail) {
  // api.get(`/api/members/1/games`, {
  await api
    .get(`/api/members/${userid}/games`, {
      params: params,
      headers: headers,
    })
    .then(success)
    .catch(fail);
}

async function getMygame(params, success, fail) {
  // api.get(`/api/members/1/games`, {
  await api
    .get(`/api/members/${userid}/games`, {
      params: params,
      headers: headers,
    })
    .then(success)
    .catch(fail);
}

async function getRecentGame(params, success, fail) {
  // api.get(`/api/members/1/games`, {
  await api
    .get(`/api/members/${userid}/games`, {
      params: params,
      headers: headers,
    })
    .then(success)
    .catch(fail);
}

async function getStarGame(params, success, fail) {
  // api.get(`/api/members/1/games`, {
  await api
    .get(`/api/members/${userid}/games`, {
      params: params,
      headers: headers,
    })
    .then(success)
    .catch(fail);
}

export { getMyBestcut, getMygame, getRecentGame, getStarGame };
