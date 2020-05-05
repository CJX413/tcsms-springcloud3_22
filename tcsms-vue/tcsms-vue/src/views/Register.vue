<template>
  <el-card class="register-box">
    <el-page-header @back="goBack">
    </el-page-header>
    <span class="register-title">注册</span>
    <el-form label-position="right" :model="registerForm" status-icon :rules="registerRules" ref="registerForm"
             label-width="100px"
             class="demo-ruleForm">
      <el-form-item
        label="手机号"
        prop="phone">
        <el-row>
          <el-col :span="16">
            <el-input v-model="registerForm.phone"
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
          <el-input v-model="registerForm.verificationCode"
                    oninput="value=value.replace(/[^\d]/g,'')"
                    maxLength="6"
                    autocomplete="off"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item
        label="邀请码"
        maxLength="6"
        prop="invitationCode">
        <el-input v-model="registerForm.invitationCode"
                  oninput="value=value.replace(/[^\d]/g,'')"
                  maxLength="6"
                  autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="pass">
        <el-input type="password"
                  v-model="registerForm.pass"
                  oninput="value=value.replace(/[^\a-\z\A-\Z0-9]/g, '')"
                  autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
        <el-input type="password" v-model="registerForm.checkPass"
                  oninput="value=value.replace(/[^\a-\z\A-\Z0-9]/g, '')"
                  autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doRegister()" style="width: 200px">注册</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
  export default {
    name: "Register",
    data() {
      let validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.registerForm.checkPass !== '') {
            this.$refs.registerForm.validateField('checkPass');
          }
          callback();
        }
      };
      let validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.registerForm.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        count: 60,
        send: false,
        sendButtonView: '获取验证码',
        registerRules: {
          phone: [
            {required: true, message: '手机号不能为空'},
          ],
          verificationCode: [
            {required: true, message: '验证码不能为空'},
          ],
          invitationCode: [
            {required: true, message: '邀请码不能为空'},
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
        registerForm: {
          phone: '',
          verificationCode: '',
          invitationCode: '',
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
        if (this.registerForm.phone === '') {
          this.$message.error('手机号不能为空');
        } else {
          console.log(this.registerForm.phone);
          this.axios.post('/auth/registerVerificationCode', {
            phone: this.registerForm.phone,
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
      doRegister() {
        this.$refs.registerForm.validate((valid) => {
          if (valid) {
            this.axios.post('/auth/register', this.registerForm)
              .then((response) => {
                if (response.data.success === true) {
                  this.$message({
                    message: '注册成功！',
                    type: 'success'
                  });
                  setTimeout(() => {
                    this.goBack()
                  }, 3000);
                } else {
                  this.utils.alertErrorMessage("注册账号失败！", response.data.message);
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
