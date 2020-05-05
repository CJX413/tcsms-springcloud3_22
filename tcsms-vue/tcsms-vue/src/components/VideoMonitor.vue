<template>
  <div>
    <el-row>
      <el-collapse accordion>
        <el-collapse-item>
          <template slot="title">
            <i class="el-icon-s-operation" style="font-size: 15px"><span>所有设备</span></i>
          </template>
          <el-menu class="el-menu-demo" mode="horizontal">
            <el-col :span="2" v-for="device in deviceList" v-bind:key="device.deviceId">
              <el-menu-item>
                <el-button type="text" @click="switchDevice(device)" :loading="loading">{{device.deviceId}}</el-button>
              </el-menu-item>
            </el-col>
            <el-col :span="2">
              <el-menu-item>
                <el-button type="text" @click="allDevice()">全景模式</el-button>
              </el-menu-item>
            </el-col>
          </el-menu>
        </el-collapse-item>
      </el-collapse>
    </el-row>
    <el-row v-show="singleShow" style="background-color: gray">
      <el-col :span="10">
        <canvas id="myCanvas" :width="500" :height="500"></canvas>
      </el-col>
      <el-col :span="14">
        <el-container>
          <el-header></el-header>
          <el-main>
            <monitor-card :device="device" :operationLog="operationLog"></monitor-card>
          </el-main>
          <el-footer></el-footer>
        </el-container>
      </el-col>
    </el-row>
    <el-row v-show="!singleShow">
      <panorama-monitor :deviceList="deviceList"></panorama-monitor>
    </el-row>
  </div>
</template>

<script>
  import MonitorCard from './MonitorCard';
  import PanoramaMonitor from './PanoramaMonitor';

  export default {
    name: "VideoMonitor",
    components: {
      MonitorCard, PanoramaMonitor
    },
    data() {
      return {
        loading: false,
        singleShow: true,
        websocket: null,
        canvas: null,
        context: null,
        R: 240,
        X: 250,
        Y: 250,
        deviceList: [
          {
            deviceId: null,
            isRegistered: null,
            deviceModel: null,
            longitude: 0,
            latitude: 0,
            rlt: null,
            bigHeight: null,
            smallHeight: null,
            bigLength: null,
            smallLength: null
          }
        ],
        device: {
          deviceId: null,
          isRegistered: null,
          deviceModel: null,
          longitude: null,
          latitude: null,
          rlt: null,
          bigHeight: null,
          smallHeight: null,
          bigLength: null,
          smallLength: null
        },
      }
    },
    computed: {
      operationLog() {
        return this.$store.state.operationLog;
      },
    },
    watch: {
      operationLog: function (newVal, oldVal) {
        this.paint(newVal);
        // let D1 = newVal.radius - oldVal.radius;
        // let D2 = newVal.angle - oldVal.angle;
        // let K1 = Math.abs(D1);
        // let K2 = Math.abs(D2);
        // let start = 0;
        // let end = Math.max(K1, K2);
        // while (start <= end && end !== 0) {
        //   if (K1 < K2) {
        //     if (D2 > 0) {
        //       oldVal.angle = (oldVal.angle + 0.1) % 360;
        //     } else if (D2 < 0) {
        //       oldVal.angle = (360 + oldVal.angle - 0.1) % 360;
        //     }
        //     if (D1 > 0) {
        //       oldVal.radius = (oldVal.radius + 0.1 * (K1 / K2)) % this.device.bigLength;
        //     } else if (D1 < 0) {
        //       oldVal.radius = (this.device.bigLength + oldVal.radius - 0.1 * (K1 / K2)) % this.device.bigLength;
        //     }
        //   } else {
        //     if (D2 > 0) {
        //       oldVal.angle = (oldVal.angle + 0.1 * (K2 / K1)) % 360;
        //     } else if (D2 < 0) {
        //       oldVal.angle = (360 + oldVal.angle - 0.1 * (K2 / K1)) % 360;
        //     }
        //     if (D1 > 0) {
        //       oldVal.radius = (oldVal.radius + 0.1) % this.device.bigLength;
        //     } else if (D1 < 0) {
        //       oldVal.radius = (this.device.bigLength + oldVal.radius - 0.1) % this.device.bigLength;
        //     }
        //   }
        //   setTimeout(this.paint(oldVal), 100);
        //   start = start + 0.1;
        // }
      },
    },
    mounted() {
      this.initDevice();
      this.initCanvas();
    },
    destroyed() {
      this.$store.commit('CLEAR_OPERATION');
      this.closeAllKindOfOperationLogSend();
    }
    ,
    methods: {
      initCanvas() {
        this.canvas = document.getElementById("myCanvas");
        this.context = this.canvas.getContext("2d");
        this.context.lineWidth = 3;
        this.context.strokeStyle = "#2aff30";
        this.context.fillStyle = "#f8ff14";
      },
      initDevice() {
        this.axios.post('/registeredDeviceInfo', {})
          .then((response) => {
            if (response.data.success === true) {
              let data = response.data.result;
              this.deviceList = data;
              this.device = data[0];
              this.openOperationLog(this.device);
            }
          });
      },
      async openAllOperationLog() {
        await this.axios.post('/openAllOperationLog', {})
          .then(response => {
            let data = response.data;
            if (data.success === false) {
              this.$message({
                dangerouslyUseHTMLString: true,
                type: 'error',
                message: '<strong>获取所有设备当前的运行信息失败！<br/>报错信息：'
                  + this.utils.restoreIllegalChar(data.message) + '</strong>'
              });
            }
          });
      },
      async openOperationLog(device) {
        await this.axios.post('/openOperationLog', {
          "deviceId": device.deviceId,
        }).then(response => {
          if (response.data.success === false) {
            this.$message({
              dangerouslyUseHTMLString: true,
              type: 'error',
              message: '<strong>获取该设备当前的运行信息失败！<br/>报错信息：'
                + this.utils.restoreIllegalChar(data.message) + '</strong>'
            });
          }
        });
      },
      closeAllKindOfOperationLogSend() {
        this.axios.post('/closeAllKindOfOperationLogSend', {})
          .then((response) => {
            console.log(response.data);
          });
      },
      switchDevice(device) {
        this.singleShow = true;
        this.$store.commit('CLEAR_OPERATION');
        this.device = device;
        this.loading = true;
        this.openOperationLog(device);
        this.loading = false;
      },
      allDevice() {
        this.loading = true;
        this.openAllOperationLog();
        this.loading = false;
        this.singleShow = false;
      },
      paint(operationLog) {
        console.log('画画---------------')
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
        let L = this.R * (operationLog.radius / this.device.bigLength);
        let Angle = Math.PI / 2 - (operationLog.angle / 180) * Math.PI; //>>>>>>>>>旋转90度并以顺时针为正方向
        let Ax = parseInt(this.X + this.R * Math.cos(Angle));
        let Ay = parseInt(this.Y - this.R * Math.sin(Angle));
        let Bx = parseInt(this.X + L * Math.cos(Angle));
        let By = parseInt(this.Y - L * Math.sin(Angle));
        //画圆
        this.context.beginPath();
        this.context.arc(this.X, this.Y, this.R, 0, 2 * Math.PI);
        this.context.stroke();
        //画直线
        this.context.beginPath();
        this.context.moveTo(this.X, this.Y);
        this.context.lineTo(Ax, Ay);
        this.context.stroke();
        //画圆的O点
        this.context.beginPath();
        this.context.arc(this.X, this.Y, 6, 0, Math.PI * 2);
        this.context.fill();
        // 画圆周上的A点
        this.context.beginPath();
        this.context.arc(Ax, Ay, 6, 0, Math.PI * 2);
        this.context.fill();
        //画直线上的B点
        this.context.beginPath();
        this.context.arc(Bx, By, 6, 0, Math.PI * 2);
        this.context.fill();
      }
    }
  }
</script>

<style scoped>
</style>
