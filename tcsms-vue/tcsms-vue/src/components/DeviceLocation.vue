<template>
  <div>
    <el-collapse accordion>
      <el-collapse-item>
        <template slot="title">
          <i class="el-icon-s-operation" style="font-size: 15px"><span>所有设备</span></i>
        </template>
        <el-menu class="el-menu-demo" mode="horizontal">
          <el-col :span="2" v-for="device in deviceList" v-bind:key="device.deviceId">
            <el-popover
              placement="right"
              trigger="click">
              <device-card :device="device"/>
              <el-button slot="reference" style="border-color: white;visibility: visible">{{device.deviceId}}
              </el-button>
            </el-popover>
          </el-col>
        </el-menu>
      </el-collapse-item>
    </el-collapse>
    <baidu-map class="map" :center="{lng: deviceList[0].longitude, lat: deviceList[0].latitude}" :zoom="zoom"
               @ready="handler" :scroll-wheel-zoom="true"
               style="height: 500px">
      <!--缩放-->
      <bm-navigation anchor="BMAP_ANCHOR_TOP_LEFT"></bm-navigation>
      <!--定位-->
      <bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :showAddressBar="true" :autoLocation="true"></bm-geolocation>
      <!--点-->
      <bml-marker-clusterer :average-center="true">
        <bm-marker v-for="device of deviceList" :key="device.deviceId"
                   :position="{lng: device.longitude, lat: device.latitude}">
          <bm-label :content="device.deviceId" :labelStyle="{color: 'red', fontSize : '15px'}"
                    :offset="{width: -20, height: 20}"/>
          <bm-circle :center="{lng: device.longitude, lat: device.latitude}" :radius="device.bigLength"
                     stroke-color="blue" :stroke-opacity="0.5" :stroke-weight="2"
                     :editing="false"></bm-circle>
        </bm-marker>
      </bml-marker-clusterer>
    </baidu-map>
  </div>
</template>

<script>
  import DeviceCard from '../components/DeviceCard'
  import BmlMarkerClusterer from "vue-baidu-map/components/extra/MarkerClusterer";

  export default {
    name: "DeviceLocation",
    components: {
      BmlMarkerClusterer,
      DeviceCard,
    },
    data() {
      return {
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
        zoom: 18,
      };
    },
    mounted() {   //初始化页面要在mounted方法中调用自己也得初始化方法
      this.initPage();
    },
    methods: {
      handler({BMap, map}) {
        map.addEventListener('click', function (e) {
          console.log(e.point.lng, e.point.lat)
        })
      },
      initPage() {
        this.axios.post('/deviceInfo', {})
          .then((response) => {
            this.deviceList = response.data;
          });
      },
    },
  }
</script>
<style scoped>

  .map {
    width: 100%;
    height: 400px;
  }
</style>
