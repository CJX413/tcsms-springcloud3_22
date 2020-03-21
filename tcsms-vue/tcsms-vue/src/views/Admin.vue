<template>
  <el-container v-role="'isAdmin'">
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
          <el-col :span="3" :offset="19">
            <el-submenu index="1">
              <template slot="title">
                <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
                <span>{{this.name}}</span>
              </template>
              <el-menu-item index="1-1">
                <i class="el-icon-user-solid"></i>
                <span>个人中心</span>
              </el-menu-item>
              <el-menu-item index="1-2">
                <i class="el-icon-switch-button"></i>
                <span>注销</span>
              </el-menu-item>
            </el-submenu>
          </el-col>
          <el-col :span="2">
            <el-menu-item index="2">
              <el-link type="primary" href="/index">监控系统</el-link>
            </el-menu-item>
          </el-col>
        </el-row>
      </el-menu>
    </el-header>
    <el-row>
      <el-col style="text-align: right">
        <el-switch
          style="text-align: right;padding-top: 10px;padding-right: 20px"
          v-model="monitorStatus.switch"
          active-color="#13ce66"
          inactive-color="#ff4949" @change="changeStatus">
        </el-switch>
      </el-col>
    </el-row>
    <el-divider></el-divider>
    <el-container style="padding-top: 0px">
      <el-aside width="200px" style="height: 600px">
        <el-menu
          :default-active="activeAside"
          class="el-menu-vertical-demo"
          @select="asideHandleSelect" style="text-align: left;height: 100%">
          <el-menu-item index="1">
            <i class="el-icon-s-platform"></i>
            <span>监控系统状态</span>
          </el-menu-item>
          <el-menu-item index="2">
            <i class="el-icon-office-building"></i>
            <span>障碍物建筑管理</span>
          </el-menu-item>
          <el-menu-item index="3">
            <i class="el-icon-s-custom"></i>
            <span>监控员权限管理</span>
          </el-menu-item>
          <el-menu-item index="4">
            <i class="el-icon-user-solid"></i>
            <span>塔吊驾驶员管理</span>
          </el-menu-item>
          <el-menu-item index="5">
            <i class="el-icon-coordinate"></i>
            <span>塔吊设备管理</span>
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
    name: "Admin",
    components: {
      UserCenter: () => import("../components/UserCenter.vue"),
      SystemStatus: () => import("../components/admin/SystemStatus.vue"),
      BuildingManage: () => import("../components/admin/BuildingManage.vue"),
      DeviceManage: () => import("../components/admin/DeviceManage"),
      MonitorManage: () => import("../components/admin/MonitorManage.vue"),
      OperatorManage: () => import("../components/admin/OperatorManage.vue"),
    },
    data() {
      return {
        name: '',
        activeHeader: null,
        activeAside: '1',
        menuSwitch: ['-1', '0'],
        switchFlagA: 0,
        switchFlagH: 0,
        componentType: 'system-status',
      };
    },
    computed: {
      monitorStatus() {
        return this.$store.state.monitorStatus;
      },
    },
    mounted() {
      this.initPage();
      this.initMonitorStatus();
    },
    methods: {
      changeStatus($event) {
        this.axios.post('/securitySystemSwitch', {})
          .then((response) => {
            console.log(response.data);
            this.$store.state.monitorStatus = response.data;
            $event = this.$store.state.monitorStatus.switch;
          });
      },
      initPage() {
        this.axios.post('/userInfo', {})
          .then((response) => {
            let userInfo = response.data;
            localStorage.setItem('username', userInfo.username);
            this.name = userInfo.name;
            this.$store.commit('CONNET_WEBSOCKET');
          });
      },
      initMonitorStatus() {
        this.axios.post('/monitorStatus', {})
          .then((response) => {
            console.log(response.data);
            this.$store.state.monitorStatus = response.data;
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
          case "1-1":
            this.componentType = 'user-center';
            break;
          case "1-2":
            this.logout();
            break;
          case "2":

            break;
          default:
        }
      },
      asideHandleSelect(key) {
        this.switchFlagH = (this.switchFlagH + 1) % 2;
        this.activeHeader = this.menuSwitch[this.switchFlagH];
        switch (key) {
          case "1":
            this.componentType = 'system-status';
            break;
          case "2":
            this.componentType = 'building-manage';
            break;
          case "3":
            this.componentType = 'monitor-manage';
            break;
          case "4":
            this.componentType = 'operator-manage';
            break;
          case "5":
            this.componentType = 'device-manage';
            break;
          default:
        }
      },
    },
  }
</script>

<style scoped>

</style>
