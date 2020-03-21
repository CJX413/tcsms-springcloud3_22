<template>
  <el-card class="box-card" style="text-align: left">
    <div slot="header" class="clearfix">
      <span>{{this.userInfo.name}}</span>
    </div>
    <div class="text item">
      {{'用户名：' + this.userInfo.username }}
    </div>
    <div class="text item">
      {{'工号：' + this.userInfo.workerId }}
    </div>
    <div class="text item">
      {{'手机号：' + this.userInfo.phoneNumber }}
    </div>
    <div class="text item">
      {{'性别：' + this.userInfo.sex }}
    </div>
  </el-card>
</template>

<script>
  export default {
    name: "UserCenter",
    data() {
      return {
        userInfo: null,
      };
    },
    mounted() {
      this.axios.post('/userInfo', {})
        .then((response) => {
          this.userInfo = response.data;
        });
    },
  }
</script>

<style scoped>
  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }

  .box-card {
    width: 480px;
  }
</style>
