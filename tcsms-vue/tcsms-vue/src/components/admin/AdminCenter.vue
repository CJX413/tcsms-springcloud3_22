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
              maxlength="11"
              prefix-icon="el-icon-phone"
              v-model="userInfoForm.phoneNumber"
              oninput="value=value.replace(/[^\d]/g,'')">
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
    <el-col :span="8">
      <el-card class="box-card" style="text-align: left">
        <div slot="header" class="clearfix">
          <span>邀请码</span>
          <el-button style="float: right" type="primary" size="small" @click="updateInvitationCode">
            修改
          </el-button>
          <el-row style="padding-top: 20px">
            <el-input
              maxLength="6"
              v-model="invitationCode"
              oninput="value=value.replace(/[^\d]/g,'')">
            </el-input>
          </el-row>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
  export default {
    name: "AdminCenter",
    data() {
      return {
        invitationCode: '',
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
    },
    methods: {
      initPage() {
        this.userInfoForm = JSON.parse(JSON.stringify(this.$store.state.userInfo));
        this.axios.post('/invitationCode', {})
          .then((response) => {
            console.log(response.data);
            if (response.data.success === true) {
              this.invitationCode = response.data.result.code;
            }
          });
      },
      updateInvitationCode() {
        this.axios.post('/updateInvitationCode', {
          invitationCode: this.invitationCode,
        })
          .then((response) => {
            console.log(response.data);
            if (response.data.success === true) {
              this.$message.success('修改邀请码成功！');
            } else {
              this.utils.alertErrorMessage('修改验证码失败！', response.data.message);
            }
          });
      },
      update() {
        console.log(this.userInfoForm);
        if (this.userInfoForm.phoneNumber !== '') {
          this.axios.post('/updateAdminInfo', this.userInfoForm)
            .then((response) => {
              if (response.data.success === true) {
                this.$store.state.userInfo = response.data.result;
                this.$message({
                  message: '修改个人信息成功！',
                  type: 'success'
                });
              } else {
                this.$message.error('修改个人信息失败！' + '报错信息：' + response.data.message);
              }
            });
        } else {
          this.$message.error('手机号不能为空！');
        }

      },
    },
  }
</script>

<style scoped>

</style>
