// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import VueResource from 'vue-resource'
import VueWechatTitle from 'vue-wechat-title'
import BaiduMap from 'vue-baidu-map'
import store from './store' //引入store
import moment from 'moment'
import utils from './utils'

Vue.prototype.utils = utils;
Vue.use(VueWechatTitle);
Vue.use(VueResource);
Vue.prototype.axios = axios;    //全局注册，使用方法为:this.$axios
Vue.use(ElementUI);
Vue.config.productionTip = false;
Vue.prototype.$moment = moment;

Vue.use(BaiduMap, {
  ak: 'rOsG2lx0to18nZrBCaxAjWdzBYASHGaL'
});


axios.defaults.baseURL = 'http://localhost:8080';
// http request 请求拦截器
axios.interceptors.request.use(request => {
  // 在发送请求之前做些什么
  if (!request.url.match(/^\/auth*/)) {
    let token = localStorage.getItem('token');
    if (token != null || token !== '') {
      request.headers.authorization = 'Bearer ' + token;
    }
  }
  return request;
}, error => {
  // 对请求错误做些什么
  return Promise.reject(error);
});

// http response 响应拦截器
axios.interceptors.response.use(response => {
  return response;
}, error => {
  if (error.response) {
    switch (error.response.status) {
      // 返回403，清除token信息并跳转到登录页面
      case 403:
        let reg = /\*?token超时了\?*/;
        if (reg.test(error.response.data)) {
          router.replace({
            path: '/auth/login'
          });
        }
        break;
      case 404:
        router.replace({
          //跳转到404页面
          path: '/404'
        });
    }
    // 返回接口返回的错误信息
    return Promise.reject(error.response.data);
  }
});
//路由判断是否登录
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requireAuth)) {  // 判断该路由是否需要登录权限
    let token = localStorage.getItem('token');
    if (token === null || token === '') {  // 判断当前的token是否存在 ； 登录存入的token
      next('/auth/login');
    } else {
      console.log('发送登录验证请求');
      axios.post('/isLogin', {})
        .then((response) => {
          console.log(response);
          next();
        }).catch((error) => {
        console.log(error);
        next('/auth/login');
      });
    }
  } else if (to.matched.some(record => record.meta.requireAdmin)) {
    console.log('admin验证++++++++++')
    let token = localStorage.getItem('token');
    if (token === null || token === '') {  // 判断当前的token是否存在 ； 登录存入的token
      next('/auth/login');
    } else {
      axios.post('/isAdmin', {})
        .then((response) => {
          console.log(response);
          next();
        }).catch((error) => {
        ElementUI.MessageBox.alert('对不起，您没有权限进行此操作！', '消息', {
          confirmButtonText: '确定',
        });
      });
    }
  }
  else {
    next();
  }
});
/**
 * 自定义指令
 */

/**
 * 角色验证指令
 */
Vue.directive("admin", function (el, binding, vnode) {
  el.onclick = async function () {
    let result = await new Promise((resolve, reject) => {
      axios.post('/isAdmin', {}).then(response => {
        resolve(true);
      }).catch(error => {
        resolve(false);
      })
    });
    console.log(result);
    if (result === false) {
      ElementUI.MessageBox.alert('对不起，您没有权限进行此操作！', '消息', {
        confirmButtonText: '确定',
      });
    } else {
      binding.value();
    }
  }
});
Vue.directive("monitor", function (el, binding, vnode) {
  el.onclick = async function () {
    let result = await new Promise((resolve, reject) => {
      axios.post('/isMonitor', {}).then(response => {
        resolve(true);
      }).catch(error => {
        resolve(false);
      })
    });
    console.log(result);
    if (result === false) {
      ElementUI.MessageBox.alert('对不起，您没有权限进行此操作！', '消息', {
        confirmButtonText: '确定',
      });
    } else {
      binding.value();
    }
  }
});


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,  //使用store
  components: {App},
  template: '<App/>'
});


