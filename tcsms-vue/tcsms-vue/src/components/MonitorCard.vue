<template xmlns:el-col="http://www.w3.org/1999/html">
  <el-card class="box-card" style="width: 650px;height: 350px;text-align: left">
    <div slot="header" class="clearfix">
      <span>{{this.operationLog.time}}</span>
      <el-col :span="12">
        <span>设备名：{{this.device.deviceId}}</span>
      </el-col>
    </div>
    <el-row>
      <el-col :span="12"><span>工人ID：{{this.operationLog.workerId}}</span></el-col>
      <el-col :span="12"><span>驾驶员：{{this.operationLog.operator}}</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>设备型号：{{this.device.deviceModel}}</span></el-col>
      <el-col :span="12"><span>倍率：{{this.operationLog.magnification}}</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>吊臂高度：{{this.device.bigHeight}}m</span></el-col>
      <el-col :span="12"><span>顶部高度：{{this.device.bigHeight+this.device.smallHeight}}m</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>大臂长：{{this.device.bigLength}}m</span></el-col>
      <el-col :span="12"><span>小臂长：{{this.device.smallLength}}m</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>吊车高度：{{this.operationLog.height}}m</span></el-col>
      <el-col :span="12"><span>吊车重量：{{this.operationLog.weight}}Kg</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>角度：{{this.operationLog.angle}}°</span></el-col>
      <el-col :span="12"><span>幅度：{{this.operationLog.radius}}m</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>力矩：{{(((this.operationLog.weight*10*this.operationLog.radius)/this.device.rlt)*100).toFixed(0)}}%</span>
      </el-col>
      <el-col :span="12"><span>风速：{{this.operationLog.windVelocity}}m/s</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>额定力矩：{{this.device.rlt}}N/m</span>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
  export default {
    name: "MonitorCard",
    data() {
      return {
      };
    },
    props: ['operationLog', 'device'],
    methods: {}
  }
</script>

<style scoped>

</style>
