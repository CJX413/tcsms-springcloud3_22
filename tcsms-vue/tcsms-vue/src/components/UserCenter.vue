<template>
  <el-row :gutter="20">
    <el-col :span="10">
      <el-card class="box-card" style="text-align: left">
        <div slot="header" class="clearfix">
          <span>用户信息</span>
          <el-button style="float: right" type="primary" size="small" @click="update">
            修改
          </el-button>
          <el-row style="padding-top: 20px">
            <el-input
              :placeholder="userInfoForm.username"
              prefix-icon="el-icon-user-solid"
              :disabled="true">
            </el-input>
          </el-row>
          <el-row style="padding-top: 20px">
            <el-input
              :disabled="true"
              maxlength="20"
              prefix-icon="el-icon-phone"
              v-model="userInfoForm.phoneNumber">
            </el-input>
          </el-row>
          <el-row style="padding-top: 20px">
            <el-input
              prefix-icon="el-icon-user"
              placeholder="请输入真实姓名！"
              v-model="userInfoForm.name"
              oninput="value=value.replace( /[^A-Za-z0-9\u4e00-\u9fa5\/?\\?\*?\s]/g, '')"
              clearable>
            </el-input>
          </el-row>
          <el-row style="padding-top: 20px">
            <el-input
              prefix-icon="el-icon-s-check"
              placeholder="请输入工人ID！"
              maxlength="20"
              oninput="value=value.replace(/[^\a-\z\A-\Z0-9]/g, '')"
              v-model="userInfoForm.workerId"
              clearable>
            </el-input>
          </el-row>
          <el-row style="padding-top: 20px">
            <el-switch
              style="display: block"
              v-model="userInfoForm.sex"
              active-color="#ff4949"
              inactive-color="#409EFF"
              active-text="女"
              inactive-text="男"
              active-value="女"
              inactive-value="男">
            </el-switch>
          </el-row>
        </div>
      </el-card>
    </el-col>
    <el-col :span="12" style="text-align: left">
      <el-row>
        <el-button type="primary" size="small" @click="applyMonitor">获取监控员权限</el-button>
      </el-row>
    </el-col>
  </el-row>
</template>

<script>
  export default {
    name: "UserCenter",
    data() {
      return {
        role: '',
        isApplyRole: null,
        userInfoForm: {
          username: '',
          name: '',
          workerId: '',
          phoneNumber: '',
          sex: '',
        },
      };
    },
    computed: {
      userInfo() {
        return this.$store.state.userInfo;
      }
    },
    mounted() {
      this.initPage();
      this.initRoleButton();
    },
    methods: {
      initPage() {
        this.userInfoForm = JSON.parse(JSON.stringify(this.$store.state.userInfo));
      },
      async initRoleButton() {
        let roleResult = await new Promise((resolve, reject) => {
          this.axios.post('/role', {})
            .then((response) => {
              if (response.data.success === true) {
                resolve(response.data.message);
              }
            });
        });
        let isApplyRoleResult = await new Promise((resolve, reject) => {
          this.axios.post('/isApplyRole', {})
            .then((response) => {
              if (response.data.message === 'true') {
                resolve(true);
              } else {
                resolve(false);
              }
            });
        });
        this.role = roleResult;
        this.isApplyRole = isApplyRoleResult;
        console.log(roleResult);
        console.log(isApplyRoleResult);
      },
      applyMonitor() {
        if (this.role === 'MONITOR') {
          this.$alert('您已经是监控员了！', '提示', {
            confirmButtonText: '确定',
          });
        } else if (this.isApplyRole === true) {
          this.$alert('您已经申请过权限了！', '提示', {
            confirmButtonText: '确定',
          });
        } else {
          this.$confirm('在申请权限之前请确保您的身份信息真实！', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.axios.post('/applyMonitor', {})
              .then((response) => {
                if (response.data.success === true) {
                  this.$message({
                    message: '成功发出MONITOR权限申请！',
                    type: 'success'
                  });
                } else {
                  this.utils.alertErrorMessage('发出申请失败！', response.data.message);
                }
              });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消申请'
            });
          });
        }
      },
      update() {
        this.axios.post('/updateUserInfo', this.userInfoForm)
          .then((response) => {
            if (response.data.success === true) {
              this.$store.state.userInfo = response.data.result;
              this.$message({
                message: '修改个人信息成功！',
                type: 'success'
              });
            } else {
              this.utils.alertErrorMessage('修改个人信息失败！', response.data.message);
            }
          });
      },

    }
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
