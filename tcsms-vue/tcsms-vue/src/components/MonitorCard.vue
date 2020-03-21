<template xmlns:el-col="http://www.w3.org/1999/html">
  <el-card class="box-card" style="width: 650px;height: 350px;text-align: left">
    <div slot="header" class="clearfix">
      <span>{{operationLog.time}}</span>
      <el-col :span="12">
        <span>设备名：{{device.deviceId}}</span>
      </el-col>
    </div>
    <el-row>
      <el-col :span="12"><span>工人ID：{{operationLog.workerId}}</span></el-col>
      <el-col :span="12"><span>驾驶员：{{operationLog.operator}}</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>设备型号：{{device.deviceModel}}</span></el-col>
      <el-col :span="12"><span>倍率：{{operationLog.magnification}}</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>吊臂高度：{{device.bigHeight}}m</span></el-col>
      <el-col :span="12"><span>顶部高度：{{device.bigHeight+device.smallHeight}}m</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>大臂长：{{device.bigLength}}m</span></el-col>
      <el-col :span="12"><span>小臂长：{{device.smallLength}}m</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>吊车高度：{{operationLog.height}}m</span></el-col>
      <el-col :span="12"><span>吊车重量：{{operationLog.weight}}Kg</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>角度：{{operationLog.angle}}°</span></el-col>
      <el-col :span="12"><span>幅度：{{operationLog.radius}}m</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>力矩：{{((operationLog.weight*10*operationLog.radius)/device.rlt).toFixed(2)}}%</span>
      </el-col>
      <el-col :span="12"><span>风速：{{operationLog.windVelocity}}m/s</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>额定力矩：{{device.rlt}}N/m</span>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
  export default {
    name: "MonitorCard",
    data() {
      return {
        operationLog: {
          deviceModel: null,
          deviceId: null,
          operator: null,
          workerId: null,
          time: null,
          longitude: null,
          latitude: null,
          radius: null,
          angle: null,
          height: null,
          torque: null,
          weight: null,
          windVelocity: null,
          magnification: null,
        },
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
      };
    },
    props: ['operationLog', 'device'],
    methods: {}
  }
</script>

<style scoped>

</style>
