<template>
  <el-card class="box-card" style="width: 480px">
    <div slot="header" class="clearfix">
      <span>设备名：{{device.deviceId}}</span>
    </div>
    <el-row>
      <el-col :span="12">
        <span v-if="device.isRegistered">是否注册：是</span>
        <span v-else>是否注册：否</span>
      </el-col>
      <el-col :span="12"><span>设备型号：{{device.deviceModel}}</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>纬度：{{device.latitude}}</span></el-col>
      <el-col :span="12"><span>经度：{{device.longitude}}</span></el-col>
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
      <el-col :span="12"><span>额定力矩：{{device.rlt}}N/m</span></el-col>
    </el-row>
  </el-card>
</template>

<script>
  export default {
    name: "DeviceCard",
    data() {
      return {};
    },
    props: ['device'],
    methods: {}
  }
</script>

<style scoped>

</style>
