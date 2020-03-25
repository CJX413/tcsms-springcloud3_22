<template>
  <div>
    <el-row>
      <el-col :span="12"><span>申请表</span></el-col>
      <el-col :span="4">
        <span>&nbsp;</span>
      </el-col>
      <el-col :span="4">
        <span>驾驶员表</span>
      </el-col>
      <el-col :span="4" style="text-align: right">
        <el-popover style="text-align: left"
                    placement="left"
                    width="700"
                    trigger="click">
          <el-row style="text-align: center">
            <span>非驾驶员用户</span>
          </el-row>
          <el-row>
            <el-table
              :data="usersTableData.filter(data => !userSearch
              || data.username.toLowerCase().includes(userSearch.toLowerCase())
              || data.workerId.toLowerCase().includes(userSearch.toLowerCase())
              || data.name.toLowerCase().includes(userSearch.toLowerCase()))"
              style="width: 100%"
              height="600">
              <el-table-column
                label="用户名"
                width="120"
                prop="username">
              </el-table-column>
              <el-table-column
                label="工人号"
                prop="workerId">
                <template slot-scope="props">
                  <el-input maxLength='20' size="mini" v-model="props.row.workerId"
                            placeholder="请输入内容" oninput="value=value.replace(/[^\a-\z\A-\Z0-9]/g, '')"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                label="姓名"
                prop="name">
                <template slot-scope="props">
                  <el-input size="mini" v-model="props.row.name"
                            placeholder="请输入内容"
                            oninput="value=value.replace( /[^A-Za-z0-9\u4e00-\u9fa5\/?\\?\*?\s]/g, '')">
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column
                width="180"
                label="特殊驾驶证号"
                prop="specialOperationCertificateNumber">
                <template slot-scope="props">
                  <el-input maxLength='20' size="mini" v-model="props.row.specialOperationCertificateNumber"
                            placeholder="请输入内容" oninput="value=value.replace(/[^\a-\z\A-\Z0-9]/g, '')"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                align="left"
                width="150">
                <template slot="header" slot-scope="scope">
                  <el-input
                    v-model="userSearch"
                    size="mini"
                    clearable
                    placeholder="输入用户名、姓名、工人号搜索"/>
                </template>
                <template slot-scope="props">
                  <el-button
                    size="mini"
                    type="primary"
                    @click="handleRegister(props.$index,props.row)">注册
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-row>
          <el-button slot="reference" size="small" type="primary">手动注册</el-button>
        </el-popover>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-table
          :data="applyTableData.filter(data => !applyTableSearch ||
     data.name.toLowerCase().includes(applyTableSearch.toLowerCase()) || data.workerId.toLowerCase().includes(applyTableSearch.toLowerCase()))"
          height="550"
          style="width: 100%">
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="用户名：">
                  <span>{{ props.row.username }}</span>
                </el-form-item>
                <el-form-item label="特殊驾驶证号：">
                  <span>{{ props.row.specialOperationCertificateNumber }}</span>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            prop="workerId"
            label="工人号">
          </el-table-column>
          <el-table-column
            prop="name"
            width="100"
            label="姓名">
          </el-table-column>
          <el-table-column
            width="200"
            sortable
            prop="createTime"
            label="申请时间">
          </el-table-column>
          <el-table-column width="150">
            <template slot="header" slot-scope="scope">
              <el-input
                v-model="applyTableSearch"
                size="small"
                clearable
                placeholder="输入工人号、姓名搜索"/>
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
          :data="tableData.filter(data => !tableSearch ||
     data.name.toLowerCase().includes(tableSearch.toLowerCase()) || data.workerId.toLowerCase().includes(tableSearch.toLowerCase()))"
          height="550"
          style="width: 100%">
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="用户名：">
                  <span>{{ props.row.username }}</span>
                </el-form-item>
                <el-form-item label="特殊驾驶证号：">
                  <span>{{ props.row.specialOperationCertificateNumber }}</span>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            prop="workerId"
            label="工人号">
          </el-table-column>
          <el-table-column
            prop="name"
            label="姓名">
          </el-table-column>
          <el-table-column>
            <template slot="header" slot-scope="scope">
              <el-input
                v-model="tableSearch"
                size="small"
                clearable
                placeholder="输入工人号、姓名搜索"/>
            </template>
            <template slot-scope="props">
              <el-button
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
    name: "OperatorManage",
    data() {
      return {
        applyTableSearch: '',
        tableSearch: '',
        userSearch: '',
        applyTableData: [
          {
            username: '',
            name: '',
            specialOperationCertificateNumber: '',
            workerId: '',
            createTime: '',

          }],
        tableData: [
          {
            username: '',
            name: '',
            specialOperationCertificateNumber: '',
            workerId: '',
          }],
        usersTableData: [
          {
            username: '18078347150',
            name: '陈嘉兴',
            specialOperationCertificateNumber: '500101199804135252',
            workerId: '1600300211',
          }
        ],
      };
    },
    mounted() {
      this.initPage();
    },
    methods: {
      initPage() {
        this.axios.post('/operatorApplyInfo', {})
          .then((response) => {
            if (response.data.success === true) {
              this.applyTableData = response.data.result;
            }
          });
        this.axios.post('/operatorInfo', {})
          .then((response) => {
            if (response.data.success === true) {
              this.tableData = response.data.result;
            }
          });
        this.axios.post('/usersIsNotOperator', {})
          .then((response) => {
            if (response.data.success === true) {
              console.log(response.data.result);
              this.usersTableData = response.data.result;
            }
          });
      },
      handleDelete(index, row) {
        console.log(index)
        this.axios.post('/deleteOperator', row)
          .then((response) => {
            if (response.data.success === true) {
              this.tableData.splice(index, 1);
              this.$message({
                message: '删除成功！',
                type: 'success'
              });
            } else {
              this.$message.error('删除失败！' + '报错信息：' + response.data.message);
            }
          });
      },
      handleAgree(index, row) {
        console.log(index)
        this.axios.post('/agreeApplyOperator', row)
          .then((response) => {
            if (response.data.success === true) {
              this.applyTableData.splice(index, 1);
              this.tableData.push(
                {
                  username: row.username,
                  name: row.name,
                  specialOperationCertificateNumber: row.specialOperationCertificateNumber,
                  workerId: row.workerId,
                }
              );
              this.$message({
                message: '已同意！',
                type: 'success'
              });
            } else {
              this.$message.error('操作失败！' + '报错信息：' + response.data.message);
            }
          });
      },
      handleRegister(index, row) {
        console.log(index)
        console.log(row)
        if (row.name !== '' && row.specialOperationCertificateNumber !== '' &&
          row.workerId !== '') {
          this.axios.post('/addOperator', row)
            .then((response) => {
              if (response.data.success === true) {
                this.usersTableData.splice(index, 1);
                this.tableData.push(
                  {
                    username: row.username,
                    name: row.name,
                    specialOperationCertificateNumber: row.specialOperationCertificateNumber,
                    workerId: row.workerId,
                  }
                );
                this.$message({
                  message: '注册成功！',
                  type: 'success'
                });
              } else {
                this.$message.error('注册失败！' + '报错信息：' + response.data.message);
              }
            });
        } else {
          this.$message.error('信息不能为空！');
        }
      },
      handleRefuse(index, row) {
        console.log(index)
        this.axios.post('/refuseApplyOperator', row)
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
    },
  }
</script>

<style scoped>
  .demo-table-expand {
    font-size: 0;
  }

  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }

  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
