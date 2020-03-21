<template>
  <el-card class="box-card" style="width: 650px;height: 350px;text-align: left">
    <div slot="header" class="clearfix">
      <span>{{operationLog.time}}</span>
      <el-col :span="12">
        <span>设备名：{{operationLog.deviceId}}</span>
      </el-col>
    </div>
    <el-row>
      <el-col :span="12"><span>工人ID：{{operationLog.workerId}}</span></el-col>
      <el-col :span="12"><span>驾驶员：{{operationLog.operator}}</span></el-col>
    </el-row>
    <el-row>
      <el-col :span="12"><span>设备型号：{{operationLog.deviceModel}}</span></el-col>
      <el-col :span="12"><span>倍率：{{operationLog.magnification}}</span></el-col>
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
      <el-col :span="12"><span>力矩：{{operationLog.weight*10*operationLog.radius}}</span>
      </el-col>
      <el-col :span="12"><span>风速：{{operationLog.windVelocity}}m/s</span></el-col>
    </el-row>
  </el-card>
</template>

<script>
    export default {
        name: "OperationLogCard",
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
          props: ['operationLog'],
        };
      },
    }
</script>

<style scoped>

</style>
