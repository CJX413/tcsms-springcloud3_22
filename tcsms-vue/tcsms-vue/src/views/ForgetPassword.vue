<template>
  <el-card class="register-box" content="">
    <el-page-header @back="goBack">
    </el-page-header>
    <span class="register-title">找回密码</span>
    <el-form label-position="right" :model="updateForm" status-icon :rules="updateRules" ref="updateForm"
             label-width="100px"
             class="demo-ruleForm">
      <el-form-item
        label="手机号"
        prop="phone">
        <el-row>
          <el-col :span="16">
            <el-input v-model="updateForm.phone"
                      oninput="value=value.replace(/[^\d]/g,'')"
                      autocomplete="off" maxLength='11'>
            </el-input>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" size="small" style="padding-left: 5px;padding-right: 5px" :loading="send"
                       @click="verificationCode()">{{sendButtonView}}
            </el-button>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item
        label="验证码"
        prop="verificationCode">
        <el-col :span="8">
          <el-input v-model="updateForm.verificationCode"
                    oninput="value=value.replace(/[^\d]/g,'')"
                    maxLength="6"
                    autocomplete="off"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="密码" prop="pass">
        <el-input type="password"
                  v-model="updateForm.pass"
                  oninput="value=value.replace(/[^\a-\z\A-\Z0-9]/g, '')"
                  autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
        <el-input type="password" v-model="updateForm.checkPass"
                  oninput="value=value.replace(/[^\a-\z\A-\Z0-9]/g, '')"
                  autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="updatePassword()" style="width: 200px">修改</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
  export default {
    name: "ForgetPassword",
    data() {
      let validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.updateForm.checkPass !== '') {
            this.$refs.updateForm.validateField('checkPass');
          }
          callback();
        }
      };
      let validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.updateForm.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        count: 60,
        send: false,
        sendButtonView: '获取验证码',
        updateRules: {
          phone: [
            {required: true, message: '手机号不能为空'},
          ],
          verificationCode: [
            {required: true, message: '验证码不能为空'},
          ],
          pass: [
            {required: true, message: '密码不能为空'},
            {validator: validatePass, trigger: 'blur'}
          ],
          checkPass: [
            {required: true, message: '请再次确认密码'},
            {validator: validatePass2, trigger: 'blur'}
          ],
        },
        updateForm: {
          phone: '',
          verificationCode: '',
          pass: '',
          checkPass: ''
        },
      }
    },
    methods: {
      verificationCode() {
        setInterval(() => {
          if (this.count < 1) {
            this.send = false;
            this.sendButtonView = '获取验证码';
          } else {
            this.send = true;
            this.sendButtonView = this.count-- + 's后重发';
          }
        }, 1000);
        this.sendVerificationCode();
      },
      sendVerificationCode() {
        if (this.updateForm.phone === '') {
          this.$message.error('手机号不能为空');
        } else {
          this.axios.post('/auth/resetPwdVerificationCode', {
            phone: this.updateForm.phone,
          })
            .then((response) => {
              if (response.data.success === true) {
                this.$message({
                  message: '发送短信验证码成功！',
                  type: 'success'
                });
              } else {
                this.utils.alertErrorMessage('发送短信验证码失败！', response.data.message);
              }
            });
        }
      },
      updatePassword() {
        this.$refs.updateForm.validate((valid) => {
          if (valid) {
            console.log('/auth/updatePassword')
            this.axios.post('/auth/updatePassword', this.updateForm)
              .then((response) => {
                if (response.data.success === true) {
                  this.$message({
                    message: '修改成功！',
                    type: 'success'
                  });
                  setTimeout(() => {
                    this.goBack()
                  }, 3000);
                } else {
                  this.utils.alertErrorMessage("修改密码失败！", response.data.message);
                }
              });
          } else {
            this.$message.error('提交失败！请按规定填写后再提交。');
          }
        });
      },
      goBack() {
        this.$router.push({path: '/auth/login'});
      }
    },
  }
</script>

<style scoped>
  .register-box {
    border: 1px solid #DCDFE6;
    height: 450px;
    width: 500px;
    margin: 180px auto;
    padding: 35px 35px 15px 35px;
    border-radius: 5px;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    box-shadow: 0 0 25px palegreen;
    text-align-all: center;
  }

  .register-title {
    text-align: center;
    margin: 0 auto 40px auto;
    color: #66CD00;
    font-size: 30px;
    font-weight: bold;
  }
</style>
