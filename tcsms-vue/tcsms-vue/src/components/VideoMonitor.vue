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
                <el-button type="text" @click="switchDevice(device)">{{device.deviceId}}</el-button>
              </el-menu-item>
            </el-col>
          </el-menu>
        </el-collapse-item>
      </el-collapse>
    </el-row>
    <el-row style="background-color: gray">
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
  </div>
</template>

<script>
  import MonitorCard from './MonitorCard';

  export default {
    name: "VideoMonitor",
    components: {
      MonitorCard,
    },
    data() {
      return {
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
        let D1 = newVal.radius - oldVal.radius;
        let D2 = newVal.angle - oldVal.angle;
        let K1 = Math.abs(D1);
        let K2 = Math.abs(D2);
        let start = 0;
        let end = Math.max(K1, K2);
        while (start <= end && end !== 0) {
          if (K1 < K2) {
            if (D2 > 0) {
              oldVal.angle = (oldVal.angle + 0.1) % 360;
            } else if (D2 < 0) {
              oldVal.angle = (360 + oldVal.angle - 0.1) % 360;
            }
            if (D1 > 0) {
              oldVal.radius = (oldVal.radius + 0.1 * (K1 / K2)) % this.device.bigLength;
            } else if (D1 < 0) {
              oldVal.radius = (this.device.bigLength + oldVal.radius - 0.1 * (K1 / K2)) % this.device.bigLength;
            }
          } else {
            if (D2 > 0) {
              oldVal.angle = (oldVal.angle + 0.1 * (K2 / K1)) % 360;
            } else if (D2 < 0) {
              oldVal.angle = (360 + oldVal.angle - 0.1 * (K2 / K1)) % 360;
            }
            if (D1 > 0) {
              oldVal.radius = (oldVal.radius + 0.1) % this.device.bigLength;
            } else if (D1 < 0) {
              oldVal.radius = (this.device.bigLength + oldVal.radius - 0.1) % this.device.bigLength;
            }
          }
          setTimeout(this.paint(oldVal), 100);
          start = start + 0.1;
        }
      },
    },
    mounted() {
      this.initDevice();
      this.initCanvas();
    },
    destroyed() {
      this.$store.state.operationLog = null;
      this.closeOperationLog();
    }
    ,
    methods: {
      initCanvas() {
        this.canvas = document.getElementById("myCanvas");
        this.context = this.canvas.getContext("2d");
        this.context.lineWidth = 3;
        this.context.strokeStyle = "#2aff30";
        this.context.fillStyle = "#f8ff14";
        this.paint(this.$store.state.operationLog);
      },
      initDevice() {
        this.axios.post('/deviceInfo', {})
          .then((response) => {
            this.deviceList = response.data;
            this.device = this.deviceList[0];
            this.openOperationLog();
          });
      },
      openOperationLog() {
        this.axios.post('/openOperationLog', {
          "deviceId": this.device.deviceId,
        })
          .then((response) => {
            console.log(response.data);
          });
      },
      closeOperationLog() {
        this.axios.post('/closeOperationLog', {})
          .then((response) => {
            console.log(response.data);
          });
      },
      switchDevice(device) {
        this.device = device;
        this.openOperationLog();
      },
      paint(operationLog) {
        console.log('画画---------------')
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
        let L = this.R * (operationLog.radius / this.device.bigLength);
        let Angle = (operationLog.angle / 180) * Math.PI;
        let Ax = this.X + this.R * Math.cos(Angle);
        let Ay = this.Y + this.R * Math.sin(Angle);
        let Bx = this.X + L * Math.cos(Angle);
        let By = this.Y + L * Math.sin(Angle);

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
