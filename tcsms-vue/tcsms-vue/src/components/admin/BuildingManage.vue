<template>
  <div>
    <el-row style="padding-bottom: 5px">
      <el-col :span="1">点一:</el-col>
      <el-col :span="3">
        <el-input v-model="pointOne.lat + ',' + pointOne.lng" style="width: 160px" size="mini"
                  @focus="pointOneClick" @blur="allBlur"></el-input>
      </el-col>
      <el-col :span="1">点二:</el-col>
      <el-col :span="3">
        <el-input v-model="pointTwo.lat + ',' + pointTwo.lng" style="width: 160px" size="mini"
                  @focus="pointTwoClick" @blur="allBlur"></el-input>
      </el-col>
      <el-col :span="1">点三:</el-col>
      <el-col :span="3">
        <el-input v-model="pointThree.lat + ',' + pointThree.lng" style="width: 160px"
                  size="mini"
                  @focus="pointThreeClick" @blur="allBlur"></el-input>
      </el-col>
      <el-col :span="1">点四:</el-col>
      <el-col :span="3">
        <el-input v-model="pointFour.lat + ',' + pointFour.lng" style="width: 160px"
                  size="mini"
                  @focus="pointFourClick" @blur="allBlur"></el-input>
      </el-col>
      <el-col :span="1">高度:</el-col>
      <el-col :span="2">
        <el-input v-model="height" style="width: 70px" size="mini"></el-input>
      </el-col>
      <el-col :span="1">建筑ID:</el-col>
      <el-col :span="2">
        <el-input v-model="buildingId" style="width: 70px" size="mini"></el-input>
      </el-col>
      <el-col :span="1">
        <el-button type="primary" size="mini" @click="addBuilding">添加</el-button>
      </el-col>
    </el-row>
    <el-row>
      <baidu-map id="map" :center="{lng: deviceList[0].longitude, lat: deviceList[0].latitude}" :zoom="zoom"
                 @ready="handler" :scroll-wheel-zoom="true"
                 style="height: 500px">
        <!--缩放-->
        <bm-navigation anchor="BMAP_ANCHOR_TOP_LEFT"></bm-navigation>
        <!--定位-->
        <bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :showAddressBar="true"
                        :autoLocation="true"></bm-geolocation>
        <!--点-->
        <bml-marker-clusterer :average-center="true">
          <bm-marker v-for="device of deviceList" :key="device.deviceId"
                     :position="{lng: device.longitude, lat: device.latitude}">
            <bm-label :content="device.deviceId" :labelStyle="{color: 'red', fontSize : '15px'}"
                      :offset="{width: -20, height: 20}"/>
            <bm-circle :center="{lng: device.longitude, lat: device.latitude}" :radius="device.bigLength"
                       stroke-color="blue" :stroke-opacity="0.5" :stroke-weight="2"
                       :editing="false">
            </bm-circle>
          </bm-marker>
          <div v-for="building of buildingList">
            <bm-marker :position="building.pointOne">
              <bm-label :content="building.buildingId" :labelStyle="{color: 'red', fontSize : '15px'}"
                        :offset="{width: -20, height: 20}"/>
            </bm-marker>
            <bm-marker :position="building.pointTwo">
              <bm-label :content="building.buildingId" :labelStyle="{color: 'red', fontSize : '15px'}"
                        :offset="{width: -20, height: 20}"/>
            </bm-marker>
            <bm-marker :position="building.pointThree">
              <bm-label :content="building.buildingId" :labelStyle="{color: 'red', fontSize : '15px'}"
                        :offset="{width: -20, height: 20}"/>
            </bm-marker>
            <bm-marker :position="building.pointFour">
              <bm-label :content="building.buildingId" :labelStyle="{color: 'red', fontSize : '15px'}"
                        :offset="{width: -20, height: 20}"/>
            </bm-marker>
            <bm-polyline
              :path="[building.pointOne,building.pointTwo,building.pointThree,building.pointFour,building.pointOne]"
              stroke-color="blue"
              :stroke-opacity="0.5" :stroke-weight="2">
            </bm-polyline>
          </div>
          <bm-marker :position="pointOne"></bm-marker>
          <bm-marker :position="pointTwo"></bm-marker>
          <bm-marker :position="pointThree"></bm-marker>
          <bm-marker :position="pointFour"></bm-marker>
          <bm-polyline :path="[pointOne,pointTwo,pointThree,pointFour,pointOne]" stroke-color="blue"
                       :stroke-opacity="0.5" :stroke-weight="2">
          </bm-polyline>
        </bml-marker-clusterer>
      </baidu-map>
    </el-row>
    <el-row>
      <el-col>
        <el-table
          :data="buildingList.filter(data => !search || data.buildingId.toLowerCase().includes(search.toLowerCase()))"
          style="width: 100%"
          height="550">
          <el-table-column
            label="建筑ID"
            width="70"
            prop="buildingId">
          </el-table-column>
          <el-table-column
            label="点一(纬度,经度)"
            :formatter="formatPoint"
            prop="pointOne">
          </el-table-column>
          <el-table-column
            label="点二(纬度,经度)"
            :formatter="formatPoint"
            prop="pointTwo">
          </el-table-column>
          <el-table-column
            label="点三(纬度,经度)"
            :formatter="formatPoint"
            prop="pointThree">
          </el-table-column>
          <el-table-column
            label="点四(纬度,经度)"
            :formatter="formatPoint"
            prop="pointFour">
          </el-table-column>
          <el-table-column
            label="高度(m)"
            prop="height">
          </el-table-column>
          <el-table-column
            align="left">
            <template slot="header" slot-scope="scope">
              <el-input
                v-model="search"
                size="mini"
                clearable
                placeholder="输入建筑ID"/>
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
  import BmlMarkerClusterer from "vue-baidu-map/components/extra/MarkerClusterer";

  export default {
    name: "BuildingManage",
    components: {
      BmlMarkerClusterer,
    },
    data() {
      return {
        search: '',
        focus: 0,
        pointOne: {lat: null, lng: null},
        pointTwo: {lat: null, lng: null},
        pointThree: {lat: null, lng: null},
        pointFour: {lat: null, lng: null},
        height: '',
        buildingId: '',
        deviceList: [
          {
            deviceId: null,
            isRegistered: null,
            deviceModel: null,
            longitude: null,
            latitude: null,
            rlt: null,
            bigHeight: null,
            smallHeight: null,
            bigLength: null,
            smallLength: null
          }
        ],
        buildingList: [],
        zoom: 18,
      };
    },
    mounted() {
      this.initPage();
    },
    methods: {
      handler({BMap, map}) {
        let that = this;
        this.map = map;
        map.addEventListener('click', function (e) {
          that.addline(e);
        })
      },
      addline(e) {
        switch (this.focus) {
          case 1:
            this.pointOne = e.point;
            break;
          case 2:
            this.pointTwo = e.point;
            break;
          case 3:
            this.pointThree = e.point;
            break;
          case 4:
            this.pointFour = e.point;
            break;
          default:
            break;
        }
      },
      initPage() {
        this.axios.post('/registeredDeviceInfo', {})
          .then((response) => {
            if (response.data.success === true) {
              this.deviceList = response.data.result;
            }
          });
        this.axios.post('/allBuilding', {})
          .then((response) => {
            console.log(response.data);
            if (response.data.success === true) {
              this.buildingList = response.data.result;
              console.log(this.buildingList)
            }
          });
      },
      pointOneClick() {
        this.focus = 1;
      },
      pointTwoClick() {
        this.focus = 2;
      },
      pointThreeClick() {
        this.focus = 3;
      },
      pointFourClick() {
        this.focus = 4;
      },
      allBlur() {
        this.focus = 0;
      },
      addBuilding() {
        if (this.pointOne.lat === null || this.pointOne.lng === null
          || this.pointTwo.lat === null || this.pointTwo.lng === null
          || this.pointThree.lat === null || this.pointThree.lng === null
          || this.pointFour.lat === null || this.pointFour.lng === null
          || this.height === '' || this.buildingId === '') {
          this.$message.error('内容不能为空！');
        } else {
          this.axios.post('/addBuilding', {
            buildingId: this.buildingId,
            height: this.height,
            pointOneLat: this.pointOne.lat,
            pointOneLng: this.pointOne.lng,

            pointTwoLat: this.pointTwo.lat,
            pointTwoLng: this.pointTwo.lng,

            pointThreeLat: this.pointThree.lat,
            pointThreeLng: this.pointThree.lng,

            pointFourLat: this.pointFour.lat,
            pointFourLng: this.pointFour.lng,
          })
            .then((response) => {
              if (response.data.success === true) {
                this.$message({
                  message: '添加障碍建筑成功！',
                  type: 'success'
                });
                this.buildingList.push({
                  pointOne: this.pointOne,
                  pointTwo: this.pointTwo,
                  pointThree: this.pointThree,
                  pointFour: this.pointFour,
                  height: this.height,
                  buildingId: this.buildingId,
                })
              } else {
                this.utils.alertErrorMessage('添加障碍建筑失败！', response.data.message);
              }
            });
        }
      },
      formatPoint(row, column) {
        let point = row[column.property]
        return point.lat + ',' + point.lng;
      },
      handleDelete(index, row) {
        this.$confirm('此操作将永久删除该设备, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.axios.post('/deleteBuilding', {
            'buildingId': row.buildingId
          })
            .then((response) => {
              if (response.data.success === true) {
                this.buildingList.splice(index, 1);
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                });
                this.restartMonitorSystem();
              } else {
                this.utils.alertErrorMessage('删除失败！', response.data.message);
              }
            });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      restartMonitorSystem() {
        if (this.$store.state.monitorStatus.switch === true) {
          this.$confirm('进行此操作需要重启监控系统, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '稍后',
            type: 'warning'
          }).then(() => {
            this.axios.post('/restartSecuritySystem', {})
              .then((response) => {
                if (response.data.success === true) {
                  this.$message({
                    message: '重启系统成功！',
                    type: 'success'
                  });
                } else {
                  this.utils.alertErrorMessage('重启失败！', response.data.message)
                }
              });
          }).catch(() => {
          });
        }
      },
    },
  }
</script>

<style scoped>

</style>
