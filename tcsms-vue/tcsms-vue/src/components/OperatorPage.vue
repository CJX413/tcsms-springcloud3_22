<template>
  <el-row :gutter="40">
    <el-col :span="12">
      <el-card class="box-card" style="text-align: left" :hidden="applyCardHidden">
        <div slot="header" class="clearfix">
          <span>申请表</span>
          <el-button style="float: right" type="primary" size="small" @click="applyOperator" :disabled="applyDisabled">
            提交申请
          </el-button>
          <el-row style="padding-top: 20px">
            <el-input
              :placeholder="username"
              prefix-icon="el-icon-user-solid"
              :disabled="true">
            </el-input>
          </el-row>
          <el-row style="padding-top: 20px">
            <el-input
              :disabled="applyDisabled"
              placeholder="请输入特殊驾驶证号！"
              maxlength="20"
              v-model="applyForm.specialOperationCertificateNumber"
              oninput="value=value.replace(/[^\a-\z\A-\Z0-9]/g, '')"
              clearable>
            </el-input>
          </el-row>
          <el-row style="padding-top: 20px">
            <el-input
              :disabled="applyDisabled"
              placeholder="请输入真实姓名！"
              v-model="applyForm.name"
              clearable>
            </el-input>
          </el-row>
          <el-row style="padding-top: 20px">
            <el-input
              :disabled="applyDisabled"
              placeholder="请输入工人ID！"
              maxlength="20"
              oninput="value=value.replace(/[^\a-\z\A-\Z0-9]/g, '')"
              v-model="applyForm.workerId"
              clearable>
            </el-input>
          </el-row>
        </div>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card class="box-card" style="text-align: left" :hidden="applyTableCardHidden">
        <div slot="header" class="clearfix">
          <span>您的申请</span>
          <el-button style="float: right" type="danger" size="small" @click="updateApply">修改申请</el-button>
          <el-row style="padding-top: 20px">
            <el-input
              :placeholder="username"
              prefix-icon="el-icon-user-solid"
              :disabled="true">
            </el-input>
          </el-row>
          <el-row style="padding-top: 20px">
            <el-input
              placeholder="请输入特殊驾驶证号！"
              maxlength="20"
              v-model="applyForm.specialOperationCertificateNumber"
              oninput="value=value.replace(/[^\a-\z\A-\Z0-9]/g, '')"
              clearable>
            </el-input>
          </el-row>
          <el-row style="padding-top: 20px">
            <el-input
              placeholder="请输入真实姓名！"
              v-model="applyForm.name"
              clearable>
            </el-input>
          </el-row>
          <el-row style="padding-top: 20px">
            <el-input
              placeholder="请输入工人ID！"
              maxlength="20"
              oninput="value=value.replace(/[^\a-\z\A-\Z0-9]/g, '')"
              v-model="applyForm.workerId"
              clearable>
            </el-input>
          </el-row>
          <el-row style="padding-top: 20px">
            <i class="el-icon-time"><span>提交申请时间：{{applyForm.createTime}}</span></i>
          </el-row>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
  export default {
    name: "OperatorPage",
    data() {
      return {
        applyDisabled: false,
        username: localStorage.getItem('username'),
        applyForm: {
          specialOperationCertificateNumber: '',
          name: '',
          workerId: '',
          createTime: '',
        },
        applyCardHidden: false,
        applyTableCardHidden: false,
      };
    },
    mounted() {
      this.initPage();
    },
    methods: {
      initPage() {
        this.isOperator().then(result => {
          console.log(result)
          if (result) {
            this.applyTableCardHidden = true;
            this.applyDisabled = true;
            this.$alert('你已经是驾驶员了！', '提示', {
              confirmButtonText: '确定',
            });
          } else {
            this.isAppliedOperator().then(result => {
              if (result) {
                this.applyTableCardHidden = false;
                this.applyCardHidden = true;
              } else {
                this.applyCardHidden = false;
                this.applyTableCardHidden = true;
              }
            });
          }
        });
      },
      async isAppliedOperator() {
        return await new Promise((resolve, reject) => {
          this.axios.post('/isAppliedOperator', {})
            .then((response) => {
              if (response.data.message === 'true') {
                this.applyForm = response.data.result;
                resolve(true);
              } else {
                resolve(false);
              }
            });
        });
      },
      async isOperator() {
        return await new Promise((resolve, reject) => {
          this.axios.post('/isOperator', {})
            .then((response) => {
              if (response.data.message === 'true') {
                this.applyForm = response.data.result;
                resolve(true);
              } else {
                resolve(false);
              }
            });
        });
      },
      updateApply() {
        if (this.applyForm.specialOperationCertificateNumber !== ''
          && this.applyForm.name !== '' && this.applyForm.workerId !== '') {
          this.axios.post('/updateOperatorApply', {
            'specialOperationCertificateNumber': this.applyForm.specialOperationCertificateNumber,
            'name': this.applyForm.name,
            'workerId': this.applyForm.workerId,
          }).then((response) => {
            if (response.data.code === 200) {
              this.$message({
                message: response.data.message,
                type: 'success'
              });
              setTimeout(this.initPage(), 1000);
            } else {
              this.$message.error(response.data.message);
            }
          });
        } else {
          this.$message.error('输入不能为空！');
        }
      },
      applyOperator() {
        if (this.applyForm.specialOperationCertificateNumber !== ''
          && this.applyForm.name !== '' && this.applyForm.workerId !== '') {
          this.axios.post('/applyOperator', {
            'specialOperationCertificateNumber': this.applyForm.specialOperationCertificateNumber,
            'name': this.applyForm.name,
            'workerId': this.applyForm.workerId,
          }).then((response) => {
            if (response.data.code === 200) {
              this.$message({
                message: response.data.message,
                type: 'success'
              });
              setTimeout(this.initPage(), 1000);
            } else {
              this.$message.error(response.data.message);
            }
          });
        } else {
          this.$message.error('输入不能为空！');
        }
      },
    },
  }
</script>

<style scoped>

</style>
