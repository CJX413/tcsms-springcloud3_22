<template>
  <el-table
    :data="tableData.filter(data => !search ||
     data.deviceId.toLowerCase().includes(search.toLowerCase()) || data.deviceModel.toLowerCase().includes(search.toLowerCase()))"
    style="width: 100%"
    height="550">
    <el-table-column
      label="设备名"
      width="70"
      prop="deviceId">
    </el-table-column>
    <el-table-column
      label="设备型号"
      prop="deviceModel">
    </el-table-column>
    <el-table-column
      label="纬度(°)"
      prop="latitude">
    </el-table-column>
    <el-table-column
      label="经度(°)"
      prop="longitude">
    </el-table-column>
    <el-table-column
      width="150"
      label="额定力矩(N/m)"
      prop="rlt">
      <template slot-scope="props">
        <el-input maxLength='7' size="mini" v-model="props.row.rlt"
                  placeholder="请输入内容" oninput="value=value.replace(/[^0-9.]/g,'')"></el-input>
      </template>
    </el-table-column>
    <el-table-column
      label="大臂长(m)"
      prop="bigLength">
      <template slot-scope="props">
        <el-input maxLength='7' size="mini" v-model="props.row.bigLength"
                  placeholder="请输入内容" oninput="value=value.replace(/[^0-9.]/g,'')"></el-input>
      </template>
    </el-table-column>
    <el-table-column
      label="小臂长(m)"
      prop="smallLength">
      <template slot-scope="props">
        <el-input maxLength='7' size="mini" v-model="props.row.smallLength"
                  placeholder="请输入内容" oninput="value=value.replace(/[^0-9.]/g,'')"></el-input>
      </template>
    </el-table-column>
    <el-table-column
      label="吊臂高度(m)"
      prop="bigHeight">
      <template slot-scope="props">
        <el-input maxLength='7' size="mini" v-model="props.row.bigHeight"
                  placeholder="请输入内容" oninput="value=value.replace(/[^0-9.]/g,'')"></el-input>
      </template>
    </el-table-column>
    <el-table-column
      label="顶部高度(m)"
      prop="smallHeight">
      <template slot-scope="props">
        <el-input maxLength='7' size="mini" v-model="props.row.smallHeight"
                  placeholder="请输入内容" oninput="value=value.replace(/[^0-9.]/g,'')"></el-input>
      </template>
    </el-table-column>
    <el-table-column
      prop="isRegistered"
      label="是否注册">
      <template slot-scope="props">
        <el-switch
          v-model="props.row.isRegistered"
          active-color="#13ce66"
          inactive-color="#ff4949"
          active-value="1"
          inactive-value="0">
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
          placeholder="输入设备名、设备型号搜索"/>
      </template>
      <template slot-scope="props">
        <el-button
          size="mini"
          @click="handleEdit(props.row)">编辑
        </el-button>
        <el-button
          size="mini"
          type="danger"
          @click="handleDelete(props.row)">删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
  export default {
    name: "DeviceManage",
    data() {
      return {
        search: '',
        tableData: [],
      };
    },
    mounted() {
      this.initTableData();
    },
    methods: {
      initTableData() {
        this.axios.post('/deviceInfo', {})
          .then((response) => {
            this.tableData = response.data;
            console.log(response.data);
          });
      },
      handleEdit(row) {
        if (this.rowIsNotNull(row)) {
          console.log(row);
          this.$confirm('是否要修改该设备的信息?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(() => {
            this.axios.post('/updateDevice', row)
              .then((response) => {
                if (response.data.success === true) {
                  this.$message({
                    type: 'success',
                    message: '修改成功!'
                  });
                } else {
                  this.$message.error('修改失败!');
                }
              });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消修改'
            });
          });
        } else {
          this.$message.error('输入不能为空');
        }
      },
      handleDelete(row) {
        if (this.rowIsNotNull(row)) {
          this.$confirm('此操作将永久删除该设备, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(() => {
            this.axios.post('/deleteDevice', row)
              .then((response) => {
                if (response.data.success === true) {
                  this.$message({
                    type: 'success',
                    message: '删除成功!'
                  });
                } else {
                  this.$message.error('删除失败!');
                }
              });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });
        } else {
          this.$message.error('输入不能为空');
        }
      },
      rowIsNotNull(row) {
        return (row.isRegistered !== '' && row.rlt !== '' && row.bigHeight !== ''
          && row.smallHeight !== '' && row.bigLength !== '' && row.smallLength !== '');
      },
    },
  }
</script>

<style scoped>

</style>
