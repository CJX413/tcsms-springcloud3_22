<template>
  <el-table
    :data="tableData.filter(data => !search ||
     data.type.toLowerCase().includes(search.toLowerCase()) || data.deviceId.toLowerCase().includes(search.toLowerCase()))"
    style="width: 100%"
    height="550">
    <el-table-column
      prop="type"
      label="监控器类型">
    </el-table-column>
    <el-table-column
      prop="deviceId"
      label="监控设备">
    </el-table-column>
    <el-table-column
      prop="living"
      label="监控器是否存活">
      <template slot-scope="props">
        <el-switch
          v-model="props.row.living"
          disabled>
        </el-switch>
      </template>
    </el-table-column>
    <el-table-column
      prop="running"
      label="监控器是否正在运行">
      <template slot-scope="props">
        <el-switch
          v-model="props.row.running"
          disabled>
        </el-switch>
      </template>
    </el-table-column>
    <el-table-column
      align="left"
      width="210">
      <template slot="header" slot-scope="scope">
        <el-input
          v-model="search"
          size="mini"
          clearable
          placeholder="输入监控器类型、设备名搜索"/>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
  export default {
    name: "SystemStatus",
    data() {
      return {
        search: '',
        tableData: [],
      };
    },
    computed: {
      monitorStatus() {
        return this.$store.state.monitorStatus;
      },
    },
    watch: {
      monitorStatus: function () {
        this.initTableData();
      }
    }
    ,
    mounted() {
      this.initTableData();
    }
    ,
    methods: {
      initTableData() {
        let monitorStatus = this.monitorStatus.status;
        let tableData = [];
        for (let i = 0; i < monitorStatus.length; i++) {
          let str = monitorStatus[i].name.split('-');
          let device = '';
          for (let i = 1; i < str.length; i++) {
            device = device + str[i] + ' ';
          }
          tableData.push(
            {
              type: str[0],
              deviceId: device,
              living: monitorStatus[i].living,
              running: monitorStatus[i].running,
            })
        }
        this.tableData = tableData;
      }
    }
  }
</script>

<style scoped>

</style>
