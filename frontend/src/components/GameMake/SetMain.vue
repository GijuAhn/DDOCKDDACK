<template>
  <div>
    <h1>제목, 설명, 사진 업로드</h1>
    <form>
      <div>
        <label for="title">제목</label>
        <input id="title" type="text" />
      </div>
      <div>
        <label for="description">설명</label>
        <input id="description" type="text" />
      </div>
      <div>
        <input
          type="file"
          :id="'uploadFile' + index"
          ref="uploadThumbFile"
          @change="uploadFile"
          accept="image/*"
          multiple
        />
      </div>
    </form>
    <div style="border: 1px solid red">
      <tr v-for="item in fileList" v-bind:key="item">
        <td><img v-bind:src="item.url" style="height: 80px; width: 80px" /></td>
        <td>{{ item.name }}</td>
        <td>{{ item.size }}</td>
      </tr>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";

export default {
  setup() {
    const state = ref({
      fileList: [],
    });

    const uploadFile = (event) => {
      if (event.target.files) {
        const files = Array.from(event.target.files);
        files.forEach((item) => {
          console.log(item);
          var reader = new FileReader();
          reader.onload = (e) => {
            console.log(e);
            // var img = {
            //   url: e.target.result,
            //   name: item.name,
            //   size: item.size,
            // };
            // state.value.fileList.push(img);
            // console.log(state.value.fileList.length);
          };
        });
      }
    };

    return {
      state,
      uploadFile,
    };
  },
};
</script>

<style></style>
