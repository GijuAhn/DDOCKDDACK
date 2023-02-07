<template>
  <div id="view">
    <h1>베스트 컷 신고 관리</h1>
    <table>
      <colgroup span="6" class="columns"></colgroup>
      <thead>
        <tr>
          <th></th>
          <th>게임 ID</th>
          <th>신고 사유</th>
          <th>신고한 유저</th>
          <th>신고당한 유저</th>
          <th></th>
        </tr>
      </thead>
      <tfoot>
        <tr>
          <td><input type="checkbox" /></td>
          <td>게임 ID</td>
          <td>신고 사유</td>
          <td>신고한 유저</td>
          <td>신고당한 유저</td>
          <td><button>처리</button></td>
        </tr>
      </tfoot>
    </table>
  </div>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

store.dispatch("commonStore/setAdminTabAsync", 1);
</script>

<style scoped>
#view {
  background-color: white;
  padding: 80px;
}

table {
  margin-top: 80px;
  text-align: center;
}

thead {
  font-size: 30px;
}

tfoot {
  font-size: 20px;
}

th {
  width: 10vw;
  height: 10vh;
}

td {
  height: 50px;
}

td,
tr,
th {
  border-bottom: 1px solid #737373;
}

input {
  margin-top: 3px;
  width: 25px;
  height: 25px;
}
</style>
