<template>
  <div id="app">
    <router-view v-wechat-title="$route.meta.title"></router-view>
  </div>
</template>

<script>
  export default {
    name: 'App',
    created() {
      let token = localStorage.getItem('token');
      if (token !== null) {
        this.axios.post('/isLogin', {})
          .then((response) => {
            this.$store.commit('CONNET_WEBSOCKET')
          }).catch((error) => {
          this.$router.push({path: '/auth/login'});
        });
      }
    },
    methods: {}
  }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 10px;
  }
</style>
