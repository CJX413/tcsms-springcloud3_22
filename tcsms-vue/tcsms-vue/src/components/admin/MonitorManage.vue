<template>
  <div>
    <el-row>
      <el-col :span="12">
        权限申请表
      </el-col>
      <el-col :span="12">
        用户权限表
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-table
          :data="applyTableData.filter(data => !applyTableSearch ||
      data.username.toLowerCase().includes(applyTableSearch.toLowerCase()) ||
      data.userInfo.workerId.toLowerCase().includes(applyTableSearch.toLowerCase()) ||
      data.userInfo.name.toLowerCase().includes(applyTableSearch.toLowerCase()))"
          height="550"
          style="width: 100%">
          <el-table-column type="expand" prop="userInfo">
            <template slot-scope="props">
              <el-card class="box-card" style="width: 500px;height: 150px;text-align: left" shadow="always">
                <div slot="header" class="clearfix">
                  <span>用户名：{{props.row.userInfo.username}}</span>
                </div>
                <el-row>
                  <el-col :span="12"><span>姓名：{{props.row.userInfo.name}}</span></el-col>
                  <el-col :span="12"><span>性别：{{props.row.userInfo.sex}}</span></el-col>
                </el-row>
                <el-row>
                  <el-col :span="12"><span>工人号：{{props.row.userInfo.workerId}}</span></el-col>
                  <el-col :span="12"><span>手机号：{{props.row.userInfo.phoneNumber}}</span></el-col>
                </el-row>
              </el-card>
            </template>
          </el-table-column>
          <el-table-column
            prop="username"
            width="120"
            label="用户名">
          </el-table-column>
          <el-table-column
            prop="role"
            width="100"
            label="角色权限">
          </el-table-column>
          <el-table-column
            width="180"
            sortable
            prop="createTime"
            label="申请时间">
          </el-table-column>
          <el-table-column>
            <template slot="header" slot-scope="scope">
              <el-input
                v-model="search"
                size="small"
                clearable
                placeholder="用户名、工人号、姓名搜索"/>
            </template>
            <template slot-scope="props">
              <el-button
                size="mini"
                type="primary"
                @click="handleAgree(props.$index,props.row)">同意
              </el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleRefuse(props.$index,props.row)">拒绝
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span="12">
        <el-table
          :data="tableData.filter(data => !search ||
      data.username.toLowerCase().includes(search.toLowerCase()) ||
      data.userInfo.workerId.toLowerCase().includes(search.toLowerCase()) ||
      data.userInfo.name.toLowerCase().includes(search.toLowerCase()))"
          height="550"
          style="width: 100%">
          <el-table-column type="expand" prop="userInfo">
            <template slot-scope="props">
              <el-card class="box-card" style="width: 500px;height: 150px;text-align: left" shadow="always">
                <div slot="header" class="clearfix">
                  <span>用户名：{{props.row.userInfo.username}}</span>
                </div>
                <el-row>
                  <el-col :span="12"><span>姓名：{{props.row.userInfo.name}}</span></el-col>
                  <el-col :span="12"><span>性别：{{props.row.userInfo.sex}}</span></el-col>
                </el-row>
                <el-row>
                  <el-col :span="12"><span>工人号：{{props.row.userInfo.workerId}}</span></el-col>
                  <el-col :span="12"><span>手机号：{{props.row.userInfo.phoneNumber}}</span></el-col>
                </el-row>
              </el-card>
            </template>
          </el-table-column>
          <el-table-column
            prop="username"
            width="120"
            label="用户名">
          </el-table-column>
          <el-table-column
            prop="role"
            width="200"
            label="角色权限">
            <template slot-scope="props">
              <el-select v-model="props.row.role" placeholder="请选择" size="small"
                         :disabled="props.row.role==='ADMIN'||props.row.role==='SERVER'">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                  :disabled="item.disabled">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column width="200">
            <template slot="header" slot-scope="scope">
              <el-input
                v-model="search"
                size="small"
                clearable
                placeholder="用户名、工人号、姓名搜索"/>
            </template>
            <template slot-scope="props">
              <el-button
                :disabled="props.row.role==='ADMIN'||props.row.role==='SERVER'"
                size="mini"
                type="primary"
                @click="handleUpdate(props.$index,props.row)">修改
              </el-button>
              <el-button
                :disabled="props.row.role==='ADMIN'||props.row.role==='SERVER'"
                size="mini"
                type="danger"
                @click="handleDelete(props.$index,props.row)">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: "MonitorManage",
    data() {
      return {
        applyTableSearch: '',
        search: '',
        options: [
          {
            value: 'ADMIN',
            label: 'ADMIN',
            disabled: true,
          },
          {
            value: 'MONITOR',
            label: 'MONITOR'
          },
          {
            value: 'SERVER',
            label: 'SERVER',
            disabled: true,
          },
          {
            value: 'USER',
            label: 'USER'
          },
        ],
        tableData: [],
        applyTableData: [
          // {
          //   username: '18078347150',
          //   role: 'ADMIN',
          //   createTime: '2020-04-13 12:12:12',
          //   userInfo: {
          //     username: '18078347150',
          //     name: '陈嘉兴',
          //     workerId: '1600300211',
          //     phoneNumber: '18078347150',
          //     sex: '男',
          //   },
          // }
        ],
      };
    },
    mounted() {
      this.axios.post('allRoleApply', {})
        .then(response => {
          if (response.data.success === true) {
            this.applyTableData = response.data.result;
          }
        });
      this.axios.post('allUsersRole', {})
        .then(response => {
          if (response.data.success === true) {
            this.tableData = response.data.result;
          }
        });
    },
    methods: {
      handleAgree(index, row) {
        console.log(index)
        this.axios.post('/agreeApplyRole', row)
          .then((response) => {
            if (response.data.success === true) {
              this.applyTableData.splice(index, 1);
              this.tableData.filter(data => {
                if (data.username === row.username) {
                  data.role = row.role;
                }
              });
              this.$message({
                message: '已同意！',
                type: 'success'
              });
            } else {
              this.$message.error('操作失败！' + '报错信息：' + response.data.message);
            }
          });
      },
      handleRefuse(index, row) {
        console.log(index)
        this.axios.post('/refuseApplyRole', row)
          .then((response) => {
            if (response.data.success === true) {
              this.applyTableData.splice(index, 1);
              this.$message({
                message: '已拒绝！',
                type: 'success'
              });
            } else {
              this.$message.error('操作失败！' + '报错信息：' + response.data.message);
            }
          });
      },
      handleUpdate(index, row) {
        this.$confirm('是否要修改该该用户的权限?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.axios.post('/updateUsersRole', row)
            .then((response) => {
              if (response.data.success === true) {
                this.$message({
                  type: 'success',
                  message: '修改成功!'
                });
              } else {
                this.$message.error('修改失败!' + '报错信息：' + response.data.message);
              }
            });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消修改'
          });
        });
      },
      handleDelete(index, row) {
        this.$confirm('此操作将永久删除该设备, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.axios.post('/deleteUser', row)
            .then((response) => {
              if (response.data.success === true) {
                this.tableData.splice(index, 1);
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                });
              } else {
                this.$message.error('删除失败!' + '报错信息：' + response.data.message);
              }
            });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
    }
  }
</script>

<style scoped>

</style>
