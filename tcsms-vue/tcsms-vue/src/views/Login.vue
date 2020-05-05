<template>
  <el-card class="login-box" content="">
    <!-- 通过:rules="loginFormRules"来绑定输入内容的校验规则 -->
    <el-form ref="loginForm" :model="loginForm" label-position="right" label-width="auto"
             show-message>
      <span class="login-title">欢迎登录</span>
      <div style="margin-top: 5px"></div>
      <el-form-item label="用户名:">
        <el-col :span="22">
          <el-input placeholder="手机号" type="text" v-model="loginForm.loginName"
                    oninput="value=value.replace(/[^\a-\z\A-\Z0-9]/g, '')"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="密码:">
        <el-col :span="22">
          <el-input type="password" v-model="loginForm.loginPassword"
                    oninput="value=value.replace(/[^\a-\z\A-\Z0-9]/g, '')"></el-input>
        </el-col>
      </el-form-item>
      <el-row style="text-align: right">
        <el-checkbox v-model="rememberMe">记住密码</el-checkbox>
      </el-row>
      <el-form-item>
        <el-col :span="22">
          <el-button type="primary" style="width: inherit" @click="loginSubmit('loginForm')">登录</el-button>
        </el-col>
      </el-form-item>
      <el-row style="text-align: center; margin-top: 0px;;">
        <el-link type="primary" href="/auth/forgetPassword" @click="">忘记密码</el-link>
        <el-link type="primary" href="/auth/register">用户注册</el-link>
      </el-row>
    </el-form>
  </el-card>
</template>
<script>

  export default {
    name: "login",
    data() {
      return {
        loginForm: {
          loginName: '',
          loginPassword: '',
        },
        data: {
          code: 0,
          token: '',
          success: false,
          message: ''
        },
        rememberMe: false,
      }
    },
    methods: {
      toRegister() {
        this.$router.push({path: '/auth/register'});
      },
      loginSubmit() {
        console.log(this.loginForm.loginName + this.loginForm.loginPassword);
        if (this.loginForm.loginName === '' || this.loginForm.loginPassword === '') {
          this.$alert('账号和密码不能为空！', '提示', {
            confirmButtonText: '确定',
          });
        } else {
          this.axios.post('/auth/login', {
            username: this.loginForm.loginName,
            password: this.loginForm.loginPassword,
            rememberMe: this.rememberMe,
          }).then(response => {
            console.log(response);
            let res = response.data;
            console.log(res.message);
            if (res.success === true) {
              console.log(res.message);
              localStorage.setItem('username', res.message);
              localStorage.setItem('token', res.token);
              this.$router.push({path: '/index'});
            } else {
              this.$alert(res.message, '登录失败', {
                confirmButtonText: '确定',
              });
            }
          });
        }
      },
    }
  }
</script>
<style scoped>
  .login-box {
    border: 1px solid #DCDFE6;
    width: 350px;
    margin: 180px auto;
    padding: 35px 35px 15px 35px;
    border-radius: 5px;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    box-shadow: 0 0 25px palegreen;
    text-align-all: center;
  }

  .login-title {
    text-align: center;
    margin: 0 auto 40px auto;
    color: #66CD00;
    font-size: 30px;
    font-weight: bold;
  }
</style>
