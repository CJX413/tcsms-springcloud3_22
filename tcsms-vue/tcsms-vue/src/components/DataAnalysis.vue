<template>
  <div>
    <el-row>
      <el-collapse accordion>
        <el-collapse-item>
          <template slot="title">
            <i class="el-icon-s-operation" style="font-size: 15px"><span>所有设备</span></i>
          </template>
          <el-col :span="1" v-for="device in deviceList" v-bind:key="device.deviceId">
            <el-button size="small" @click="handleClick(device)" type="primary" plain>{{device.deviceId}}</el-button>
          </el-col>
        </el-collapse-item>
      </el-collapse>
    </el-row>
    <el-row style="text-align: left">
      <el-date-picker
        value-format="yyyy_MM_dd"
        format="yyyy 年 MM 月 dd 日"
        size="small"
        v-model="date"
        align="right"
        type="date"
        placeholder="选择日期">
      </el-date-picker>
    </el-row>
    <el-row>
      <el-col :span="12">
        <v-charts id="line-chart" :options="option" style="width: 600px;height: 400px"></v-charts>
      </el-col>
      <el-col :span="12">
        <v-charts id="bar-chart" :options="option2" style="width: 600px;height: 400px"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import DeviceCard from '../components/DeviceCard'
  import VCharts from "vue-echarts";
  import echarts from 'echarts';
  import "echarts/lib/chart/line"; // 线图
  import "echarts/lib/chart/bar"; // 柱图
  import 'echarts/lib/component/markLine'
  import 'echarts/lib/component/legend' // 图例
  import 'echarts/lib/component/tooltip' //提示框
  export default {
    name: "DataAnalysis",
    components: {DeviceCard, VCharts},
    data() {
      return {
        lineChart: null,
        barChart: null,
        date: '',
        device: null,
        deviceList: [],
        option: {
          title: {
            text: '',
          },
          tooltip: {
            trigger: 'axis',
          },
          legend: {
            selectedMode: 'single',
            data:
              ['radius', 'angle', 'height', 'torque', 'weight', 'windVelocity']
          },
          grid: {
            top: '10%',
            left: '3%',
            right: '5%',
            bottom: '10%',
            containLabel:
              true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            minInterval: 1,
            boundaryGap: false,
            data: null
          },
          yAxis: {
            type: 'value'
          },
          dataZoom: [
            {
              show: true,
              realtime: true,
              start: 0,
              end: 10,
            },
          ],
          series: [
            {
              name: 'radius',
              type: 'line',
              data: null,
            },
            {
              name: 'angle',
              type: 'line',
              data: null,
            },
            {
              name: 'height',
              type: 'line',
              data: null,
            },
            {
              name: 'weight',
              type: 'line',
              data: null,
            },
            {
              markLine: {
                symbol: 'none',
                lineStyle: {
                  normal: {
                    color: 'red',
                    type: 'solid',
                    width: 1
                  }
                },
                data: [
                  {
                    yAxis: 0, name: '额定力矩', label: {
                      normal: {
                        position: 'middle',
                        formatter: '额定力矩',
                      }
                    }
                  },
                ]
              },
              name: 'torque',
              type: 'line',
              data: null,
            },
            {
              markLine: {
                symbol: 'none',
                lineStyle: {
                  normal: {
                    color: 'red',
                    type: 'solid',
                    width: 1
                  }
                },
                data: [
                  {
                    yAxis: 5.5, name: '四级风', label: {
                      normal: {
                        position: 'middle',
                        formatter: '四级风基准线',
                      }
                    }
                  },
                  {
                    yAxis: 10.8, name: '六级风', label: {
                      normal: {
                        position: 'middle',
                        formatter: '六级风基准线',
                      }
                    }
                  },
                ]
              },
              name: 'windVelocity',
              type: 'line',
              data: null,
            },
          ],
        },
        option2: {
          title: {
            text: '设备警报比例统计图',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: [],
          },
          series: [
            {
              name: '所占比',
              type: 'pie',
              radius: '55%',
              center: ['50%', '60%'],
              data: [],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        },
      }
        ;
    },
    mounted() {
      this.initPage();
      this.initECharts();
    },
    methods: {
      initPage() {
        this.date = this.$moment(new Date().toString()).format('YYYY_MM_DD');
        this.axios.post('/registeredDeviceInfo', {})
          .then((response) => {
            this.deviceList = response.data.result;
          });
      },
      resetOption() {
        this.option.xAxis.data = null;
        this.option.series[0].data = null;
        this.option.series[1].data = null;
        this.option.series[2].data = null;
        this.option.series[3].data = null;
        this.option.series[4].data = null;
        this.option.series[5].data = null;
        this.option.series[4].markLine.data[0].yAxis = null;
        this.option.dataZoom[0].end = 10;
        this.option.dataZoom[0].start = 0;

        this.option2.legend.data = [];
        this.option2.series[0].data = [];
      },
      initECharts() {
        let that = this;
        this.lineChart = echarts.init(document.getElementById("line-chart"));
        this.lineChart.on('dataZoom', function (params) {
          console.log(params);
          if (params.end === 100) {
            that.refreshData();
          }
        });
        this.barChart = echarts.init(document.getElementById("bar-chart"));
      },
      handleClick(device) {
        this.resetOption();
        this.device = device;
        this.initLineChart(device);
        this.initBarChart(device);
      },
      initLineChart(device) {
        this.axios.post('/lineChartOfOperationLog', {
          'deviceId': device.deviceId,
          'date': this.date,
          'time': '',
        })
          .then((response) => {
            let data = response.data;
            console.log(data);
            console.log(data.success + '---------------------------');
            if (data.success === true) {
              let result = data.result;
              this.option.xAxis.data = result.time.map(function (str) {
                return str.replace(' ', '\n');
              });
              this.option.series[0].data = result.radius;
              this.option.series[1].data = result.angle;
              this.option.series[2].data = result.height;
              this.option.series[3].data = result.weight;
              this.option.series[4].data = result.torque;
              this.option.series[5].data = result.windVelocity;
              this.option.series[4].markLine.data[0].yAxis = device.rlt;
            } else {
              this.utils.alertErrorMessage('获取折线图数据失败！', data.message);
            }
          });
      },
      async initBarChart(device) {
        await this.axios.post('/barChartOfWarningLog', {
          'deviceId': device.deviceId,
          'date': this.date,
        })
          .then((response) => {
            let data = response.data;
            console.log(data);
            console.log(data.success + '---------------------------');
            if (data.success === true) {
              let result = data.result;
              let innerData = result.data;
              let size = result.length;
              let warningCount = 0;
              let legendData = [];
              let seriesData = [];
              for (let i = 0; i < innerData.length; i++) {
                let name = innerData[i].message;
                legendData.push(name);
                seriesData.push({value: innerData[i].count, name: name});
                warningCount = warningCount + innerData[i].count;
              }
              this.option2.legend.data = legendData;
              this.option2.series[0].data = seriesData;
              this.option2.legend.data.push('正常运行！');
              this.option2.series[0].data.push({value: size - warningCount, name: '正常运行！'})
            } else {
              this.utils.alertErrorMessage('获取饼图数据失败！', data.message);
            }
          });
      },
      refreshData() {
        console.log('刷新-------------')
        let time = this.option.xAxis.data[this.option.xAxis.data.length - 1].replace('\n', ' ');
        this.axios.post('/lineChartOfOperationLog', {
          'deviceId': this.device.deviceId,
          'date': this.date,
          'time': time,
        })
          .then((response) => {
            if (response.data.success === true) {
              let data = response.data.result;
              let length = data.time.length;
              this.option.xAxis.data = this.option.xAxis.data.concat(data.time.map(function (str) {
                return str.replace(' ', '\n');
              }));
              this.option.series[0].data = this.option.series[0].data.concat(data.radius);
              this.option.series[1].data = this.option.series[1].data.concat(data.angle);
              this.option.series[2].data = this.option.series[2].data.concat(data.height);
              this.option.series[3].data = this.option.series[3].data.concat(data.weight);
              this.option.series[4].data = this.option.series[4].data.concat(data.torque);
              this.option.series[5].data = this.option.series[5].data.concat(data.windVelocity);
              let end = (this.option.xAxis.data.length - length) / this.option.xAxis.data.length * 100;
              let start = end - 10;
              this.option.dataZoom[0].end = end;
              this.option.dataZoom[0].start = start;
            } else {
              this.utils.alertErrorMessage('获取数据失败！',response.data.message);
            }
          });
      },
    },
  }
</script>

<style scoped>

</style>
