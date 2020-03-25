<template xmlns:el-col="http://www.w3.org/1999/html">
  <div>
    <el-row style="padding-bottom: 10px;text-align: left">
      <el-button type="danger" size="mini" plain @click="cleanTable" icon="el-icon-delete">清空数据</el-button>
    </el-row>
    <el-table
      ref="filterTable"
      :data="tableData.slice((currentPage - 1) * pagesize, currentPage * pagesize)"
      @sort-change="sortChange"
      @expand-change="readHandle"
      height="490"
      style="width: 100%">
      <el-table-column type="expand" width="50" prop="data">
        <template slot-scope="props">
          <el-row :gutter="10">
            <el-col :span="12" v-for="items in props.row.data" :key="items.deviceId">
              <el-card class="box-card" style="width: 500px;height: 200px;text-align: left" shadow="always">
                <div slot="header" class="clearfix">
                  <span>{{items.time}}</span>
                  <el-col :span="12">
                    <span>设备名：{{items.deviceId}}</span>
                  </el-col>
                </div>
                <el-row>
                  <el-col :span="12"><span>工人ID：{{items.workerId}}</span></el-col>
                  <el-col :span="12"><span>驾驶员：{{items.operator}}</span></el-col>
                </el-row>
                <el-row>
                  <el-col :span="12"><span>设备型号：{{items.deviceModel}}</span></el-col>
                  <el-col :span="12"><span>倍率：{{items.magnification}}</span></el-col>
                </el-row>
                <el-row>
                  <el-col :span="12"><span>吊车高度：{{items.height}}m</span></el-col>
                  <el-col :span="12"><span>吊车重量：{{items.weight}}Kg</span></el-col>
                </el-row>
                <el-row>
                  <el-col :span="12"><span>角度：{{items.angle}}°</span></el-col>
                  <el-col :span="12"><span>幅度：{{items.radius}}m</span></el-col>
                </el-row>
                <el-row>
                  <el-col :span="12"><span>力矩：{{items.weight*10*items.radius}}N/m</span>
                  </el-col>
                  <el-col :span="12"><span>风速：{{items.windVelocity}}m/s</span></el-col>
                </el-row>
              </el-card>
            </el-col>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column
        @sort-change="sortChange"
        sortable='custom'
        label="序号"
        width="200"
        prop="order">
      </el-table-column>
      <el-table-column
        prop="code"
        label="警报代码"
        width="200">
      </el-table-column>
      <el-table-column
        prop="message"
        label="警报类型"
        width="400"
        :filters="warningFilters"
        :filter-method="filterTag"
        filter-placement="bottom-end">
        <template slot-scope="props">
          <el-tag :type="warningType(props.row.message)"
                  disable-transitions>{{props.row.message}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="time"
        label="日期"
        sortable='custom'
        width="300"
        column-key="time"
        :filters="dateFilters"
        :filter-method="filterHandler">
      </el-table-column>
      <el-table-column
        label=""
        width="100">
        <template slot-scope="props">
          <el-badge value="new" class="item" :hidden="props.row.read">
            <span></span>
          </el-badge>
        </template>
      </el-table-column>
    </el-table>
    <el-divider></el-divider>
    <el-row>
      <el-pagination
        background
        layout="prev, pager, next, sizes"
        :page-sizes="[7, tableData.length]"
        :page-size="pagesize"
        :total="tableData.length"
        :current-page.sync="currentPage"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange">
      </el-pagination>
    </el-row>
  </div>
</template>

<script>
  import OperationLogCard from '../components/OperationLogCard'

  export default {
    name: "WarningMessage",
    components: {
      OperationLogCard,
    },
    data() {
      return {
        warningFilters: [],
        dateFilters: [],
        pagesize: 7,
        currentPage: 0,
      };
    },
    computed: {
      tableData() {
        return this.$store.getters.getTableData;
      },
    },
    watch: {
      tableData: function (newVal, oldVal) {
      }
    },
    mounted() {
      this.initTable();
      this.resetNewMessage()
    },
    methods: {
      readHandle(row, rowList) {
        row.read = true;
      },
      cleanTable() {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.$store.commit('CLEARN_WARNING');
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          this.dateFilters = [];
          this.warningFilters = {};
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      resetNewMessage() {
        this.$store.state.warningMessage = [];
      },
      sortChange(column) {
        console.log('远程排序-----------------');
        let fieldName = column.prop;
        let sortingType = column.order;
        if (fieldName === 'order') {
          if (sortingType === "descending") {
            //按照降序排序
            this.tableData = this.tableData.sort((a, b) => b[fieldName] - a[fieldName]);
          } else {
            //按照升序排序
            this.tableData = this.tableData.sort((a, b) => a[fieldName] - b[fieldName]);
          }
        } else if (fieldName === 'date') {
          if (sortingType === "descending") {
            //按照降序排序
            this.tableData = this.tableData.sort((a, b) => b[fieldName] - a[fieldName]);
          } else {
            //按照升序排序
            this.tableData = this.tableData.sort((a, b) => a[fieldName] - b[fieldName]);
          }
        }

      },
      handleCurrentChange(currentPage) {
        this.currentPage = currentPage;
      },
      handleSizeChange(pagesize) {
        this.pagesize = pagesize;
      },
      warningType(message) {
        let reg_1 = new RegExp('红色警报');
        let reg_2 = new RegExp('黄色警报');
        if (reg_1.test(message)) {
          return 'danger';
        } else {
          if (reg_2.test(message)) {
            return 'warning';
          }
          return 'info';
        }
      },
      initTable() {
        let tableData = this.$store.getters.getTableData;
        let warningFilters = {};
        let dateFilters = {};
        for (let i = 0; i < tableData.length; i++) {
          this.$set(warningFilters, tableData[i].code,
            {
              text: tableData[i].message,
              value: tableData[i].code,
            });
          this.$set(dateFilters, this.$moment(tableData[i].time).format('YYYY-MM-DD'),
            {
              text: this.$moment(tableData[i].time).format('YYYY-MM-DD'),
              value: this.$moment(tableData[i].time).format('YYYY-MM-DD'),
            });
        }
        this.warningFilters = Object.values(warningFilters);
        this.dateFilters = Object.values(dateFilters);
        console.log(this.warningFilters);
        console.log(this.dateFilters);
        this.currentPage = (tableData.length + this.pagesize - 1) / this.pagesize;
      },
      filterTag(value, row) {
        return row.code === value;
      },
      filterHandler(value, row, column) {
        const property = column['property'];
        return this.$moment(row[property]).format('YYYY-MM-DD') === value;
        //return row[property] === value;
      }
    }
  }
</script>

<style scoped>

</style>
