<template>
  <baidu-map class="map" :center="{lng: innerDeviceList[0].longitude, lat: innerDeviceList[0].latitude}" :zoom="zoom"
             :scroll-wheel-zoom="true"
             style="height: 600px">
    <!--缩放-->
    <bm-navigation anchor="BMAP_ANCHOR_TOP_LEFT"></bm-navigation>
    <!--定位-->
    <bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :showAddressBar="true" :autoLocation="true"></bm-geolocation>
    <!--点-->
    <bml-marker-clusterer :average-center="true">
      <bm-marker v-for="device of innerDeviceList" :key="device.deviceId"
                 :position="{lng: device.longitude, lat: device.latitude}">
        <bm-label :content="device.deviceId" :labelStyle="{color: 'red', fontSize : '15px'}"
                  :offset="{width: -20, height: 20}"/>
        <bm-circle :center="{lng: device.longitude, lat: device.latitude}" :radius="device.bigLength"
                   stroke-color="blue" :stroke-opacity="0.5" :stroke-weight="2"
                   :editing="false">
        </bm-circle>
      </bm-marker>
      <div v-for="device of devicePath">
        <bm-polyline :path="device.path" stroke-color="blue"
                     :stroke-opacity="0.5" :stroke-weight="2">
        </bm-polyline>
        <bm-marker :icon="point" :position="device.point"></bm-marker>
      </div>

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
    </bml-marker-clusterer>
  </baidu-map>
</template>

<script>
  import BmlMarkerClusterer from "vue-baidu-map/components/extra/MarkerClusterer";

  export default {
    name: "PanoramaMonitor",
    components: {
      BmlMarkerClusterer,
    },
    props: ['deviceList'],
    data() {
      return {
        buildingList: [],
        point: {url: require('../../static/img/point.png'), size: {width: 12, height: 12}},
        R: 6371e3,
        innerDeviceList: null,
        zoom: 18,
        deviceMap: null,
        devicePath: [
          {
            point: {
              lng: null,
              lat: null,
            },
            path: []
          },
        ],
      };
    },
    computed: {
      allOperationLog() {
        return this.$store.state.allOperationLog;
      },
    },
    mounted() {
      this.initPage();
    },
    watch: {
      deviceList: function (newVal, oldVal) {
        this.innerDeviceList = newVal;
        let deviceMap = new Map();
        for (let i = 0; i < newVal.length; i++) {
          deviceMap.set(newVal[i].deviceId, newVal[i]);
        }
        this.deviceMap = deviceMap;
        console.log(this.deviceMap)
      },
      allOperationLog: function (newVal, oldVal) {
        this.devicePath = [];
        newVal.forEach(operationLog => {
          let device = this.deviceMap.get(operationLog.deviceId);
          if (device !== null) {
            let result = this.calLocationByDistanceAndLocationAndDirection(operationLog.angle,
              device.longitude, device.latitude, device.bigLength, operationLog.radius);
            this.devicePath.push(
              {
                point: result.point,
                path: [
                  {
                    lng: device.longitude,
                    lat: device.latitude,
                  },
                  result.line
                ]
              });
          }
        });
      }
    },
    methods: {
      initPage() {
        this.axios.post('/allBuilding', {})
          .then((response) => {
            console.log(response.data);
            if (response.data.success === true) {
              this.buildingList = response.data.result;
              console.log(this.buildingList)
            }
          });
      },
      calLocationByDistanceAndLocationAndDirection(angle, startLong, startLat, bigLength, radius) {
        //将距离转换成经度的计算公式
        let L = bigLength / this.R;
        let P = radius / this.R;
        // 转换为radian，否则结果会不正确
        angle = this.toRadians(angle);
        startLong = this.toRadians(startLong);
        startLat = this.toRadians(startLat);
        let latLine = Math.asin(Math.sin(startLat) * Math.cos(L) + Math.cos(startLat) * Math.sin(L) * Math.cos(angle));
        let lonLine = startLong + Math.atan2(Math.sin(angle) * Math.sin(L) * Math.cos(startLat), Math.cos(L) - Math.sin(startLat) * Math.sin(latLine));
        // 转为正常的10进制经纬度
        lonLine = this.toDegrees(lonLine);
        latLine = this.toDegrees(latLine);

        let latPoint = Math.asin(Math.sin(startLat) * Math.cos(P) + Math.cos(startLat) * Math.sin(P) * Math.cos(angle));
        let lonPoint = startLong + Math.atan2(Math.sin(angle) * Math.sin(P) * Math.cos(startLat), Math.cos(P) - Math.sin(startLat) * Math.sin(latPoint));
        // 转为正常的10进制经纬度
        lonPoint = this.toDegrees(lonPoint);
        latPoint = this.toDegrees(latPoint);
        return {
          line: {
            lng: parseFloat(lonLine.toFixed(6)),
            lat: parseFloat(latLine.toFixed(6)),
          },
          point: {
            lng: parseFloat(lonPoint.toFixed(6)),
            lat: parseFloat(latPoint.toFixed(6)),
          },
        };
      },
      toRadians(angdeg) {
        return angdeg / 180.0 * Math.PI;
      },
      toDegrees(angrad) {
        return angrad * 180.0 / Math.PI;
      },
    }
  }
</script>

<style scoped>

</style>
