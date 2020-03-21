<template>
  <div>
    <el-row style="text-align: left" :gutter="20">
      <el-col :span="4">
        <el-select v-model="device" placeholder="选择回放的设备" value-key="deviceId">
          <el-option
            v-for="device in deviceList"
            :key="device.deviceId"
            :label="device.deviceId"
            :value="device">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="5">
        <el-date-picker
          v-model="time"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetime"
          placeholder="选择日期时间">
        </el-date-picker>
      </el-col>
      <el-col :span="2">
        <el-button type="primary" @click="openOperationLogDate">确认</el-button>
      </el-col>
    </el-row>
    <el-divider></el-divider>
    <el-row style="background-color: gray;">
      <el-col :span="10">
        <canvas id="myCanvas" :width="500" :height="500"></canvas>
      </el-col>
      <el-col :span="14">
        <el-container>
          <el-header></el-header>
          <el-main>
            <monitor-card :device="device" :operationLog="operationLogDate"></monitor-card>
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
    components: {
      MonitorCard,
    },
    name: "VideoPlayback",
    data() {
      return {
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
        time: '',
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
      }
    },
    computed: {
      operationLogDate() {
        return this.$store.state.operationLogDate;
      },
    },
    watch: {
      operationLogDate: function (newVal, oldVal) {
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
      console.log('销毁了---------')
      this.$store.state.operationLogDate = null;
      this.closeOperationLogDate();
    },
    methods: {
      initCanvas() {
        this.canvas = document.getElementById("myCanvas");
        this.context = this.canvas.getContext("2d");
        this.context.lineWidth = 3;
        this.context.strokeStyle = "#2aff30";
        this.context.fillStyle = "#f8ff14";
        this.paint(this.operationLogDate);
      },
      initDevice() {
        this.axios.post('/deviceInfo', {})
          .then((response) => {
            this.deviceList = response.data;
          });
      },
      openOperationLogDate() {
        if (this.device !== null && this.time !== '') {
          this.axios.post('/openOperationLogDate', {
            "deviceId": this.device.deviceId,
            "time": this.time,
          })
            .then((response) => {
              this.paint(this.operationLogDate);
            });
        } else {
          this.$message.error('请选择设备和时间！');
        }
      },
      closeOperationLogDate() {
        this.axios.post('/closeOperationLogDate', {})
          .then((response) => {
            console.log(response.data);
          });
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
