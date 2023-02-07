<template>
  <div class="user-info">
    <div id="profile">
      <div id="profileImg" style="float: left">
        <img
          :src="
            require(`@/../../Backend/images/profile/${myProfile.memberId}/${myProfile.profile}`)
          "
          alt="대표사진"
          class="image"
        />
      </div>
    </div>
    <div id="nameAndEmail">
      <div id="nickname">
        <span>{{ myProfile.nickname }}</span>
      </div>
      <div id="email">
        <span>{{ myProfile.email }}</span>
      </div>
      <!-- ${{ props.profile.gameId }}, ${{ props.profile.gameId }} -->
    </div>
  </div>
</template>

<script setup>
import { useStore } from "vuex";
import { computed } from "vue";

const store = useStore();

const myProfile = computed(() => store.state.memberStore.memberInfo);

console.log("profile", myProfile.value);
</script>

<style scoped>
img {
  border-radius: 50%;
  width: 150px;
  height: 150px;
  margin-left: auto;
  margin-right: auto;
}
.user-info {
  height: 290px;
  display: table;
}
.user-info > div {
  display: table-cell;
  vertical-align: middle;
}
#profileImg {
  margin-right: 30px;
}
#nickname {
  font-size: 48px;
  margin-bottom: 10px;
}
</style>
