<template>
  <el-container>
    <el-header style="padding: 0">
      <el-menu
        :default-active="activeHeader"
        @select="headerHandleSelect"
        class="el-menu-demo"
        mode="horizontal"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        style="height: inherit">
        <el-row>
          <el-col :span="1" :offset="18">
            <el-menu-item index="1">
              <el-badge :value="warningMessage.length" class="item">
                <i class="el-icon-message" style="font-size: 25px;"></i></el-badge>
            </el-menu-item>
          </el-col>
          <el-col :span="3">
            <el-submenu index="2">
              <template slot="title">
                <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
                <span>{{this.name}}</span>
              </template>
              <el-menu-item index="2-1">
                <i class="el-icon-user-solid"></i>
                <span>个人中心</span>
              </el-menu-item>
              <el-menu-item index="2-2">
                <i class="el-icon-switch-button"></i>
                <span>注销</span>
              </el-menu-item>
            </el-submenu>
          </el-col>
          <el-col :span="2">
            <el-menu-item index="3">
              <el-link type="primary" v-admin="()=>{this.$router.push({path:'/admin'})}">管理系统</el-link>
            </el-menu-item>
          </el-col>
        </el-row>
      </el-menu>
    </el-header>
    <el-divider></el-divider>
    <el-container style="padding-top: 0px">
      <el-aside width="200px" style="height: 600px">
        <el-menu
          :default-active="activeAside"
          class="el-menu-vertical-demo"
          @select="asideHandleSelect" style="text-align: left;height: 100%">
          <el-menu-item index="1">
            <i class="el-icon-location"></i>
            <span>设备位置</span>
          </el-menu-item>
          <el-menu-item index="2" v-monitor="()=>{this.componentType=video-monitor}">
            <i class="el-icon-view"></i>
            <span>视频监控</span>
          </el-menu-item>
          <el-menu-item index="3">
            <i class="el-icon-coordinate"></i>
            <span>注册驾驶员</span>
          </el-menu-item>
          <el-menu-item index="4" v-monitor="()=>{this.componentType='video-playback'}">
            <i class="el-icon-video-camera-solid"></i>
            <span>监控回放</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main style="padding-top: 0px">
        <component v-bind:is="componentType"></component>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  export default {
    name: "Index",
    components: {
      DeviceLocation: () => import("../components/DeviceLocation.vue"),
      VideoMonitor: () => import("../components/VideoMonitor.vue"),
      UserCenter: () => import("../components/UserCenter.vue"),
      WarningMessage: () => import("../components/WarningMessage.vue"),
      OperatorPage: () => import("../components/OperatorPage.vue"),
      VideoPlayback: () => import("../components/VideoPlayback.vue"),
    },
    data() {
      return {
        name: '',
        componentType: 'device-location',
        activeHeader: null,
        activeAside: '1',
        menuSwitch: ['-1', '0'],
        switchFlagA: 0,
        switchFlagH: 0,
      };
    },
    computed: {
      warningMessage() {
        return this.$store.state.warningMessage;
      },
    },
    watch: {
      warningMessage(newVal, oldVal) {
        if (newVal.length !== 0) {
          this.$alert(newVal[newVal.length - 1], '新消息', {
            confirmButtonText: '确定',
          });
        }
      },
    },
    mounted() {  //初始化页面要在mounted方法中调用自己也得初始化方法
      this.initPage();
    },
    methods: {
      initPage() {
        this.axios.post('/userInfo', {})
          .then((response) => {
            this.name = response.data.name;
            this.$store.commit('CONNET_WEBSOCKET');
          });
      },
      logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        this.$router.push({path: '/auth/login'});
      },
      headerHandleSelect(key) {
        this.switchFlagA = (this.switchFlagA + 1) % 2;
        this.activeAside = this.menuSwitch[this.switchFlagA];
        console.log(key);
        switch (key) {
          case "1":
            this.componentType = 'warning-message';
            break;
          case "2-1":
            this.componentType = 'user-center';
            break;
          case "2-2":
            this.logout();
            break;
          case "3":
            break;
          default:
        }
      },
      asideHandleSelect(key) {
        this.switchFlagH = (this.switchFlagH + 1) % 2;
        this.activeHeader = this.menuSwitch[this.switchFlagH];
        switch (key) {
          case "1":
            this.componentType = 'device-location';
            break;
          case "2":

            break;
          case "3":
            this.componentType = 'operator-page';
            break;
          case "4":

            break;
          case "5":
            break;
          default:
        }
      },
      index4() {
        this.componentType = 'video-playback';
      }
    },
  }
</script>
<style scoped>

</style>
