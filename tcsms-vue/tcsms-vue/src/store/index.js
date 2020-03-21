import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const state = {
    operationLog: {
      deviceModel: null,
      deviceId: null,
      operator: null,
      workerId: null,
      time: null,
      longitude: null,
      latitude: null,
      radius: null,
      angle: null,
      height: null,
      torque: null,
      weight: null,
      windVelocity: null,
      magnification: null,
    },
    operationLogDate: {
      deviceModel: null,
      deviceId: null,
      operator: null,
      workerId: null,
      time: null,
      longitude: null,
      latitude: null,
      radius: null,
      angle: null,
      height: null,
      torque: null,
      weight: null,
      windVelocity: null,
      magnification: null,
    },
    monitorStatus: {
      switch: false,
      status: [
        // {
        //   name: '',
        //   living: false,
        //   running: false,
        // }
      ]
    },
    tableData: [],
    warningMessage: [],
    websock: null,
  }
;

//同步方法
const mutations = {
  CONNET_WEBSOCKET() {
    if (state.websock === null) {
      const wsuri = 'ws://localhost:8080/webSocket/' + localStorage.getItem('username');
      state.websock = new WebSocket(wsuri);
      state.websock.onopen = onopen;
      state.websock.onmessage = onmessage;
      state.websock.onerror = onerror;
      state.websock.onclose = onclose;
    } else {
      if (state.websock.readyState === 3) {
        const wsuri = 'ws://localhost:8080/webSocket/' + localStorage.getItem('username');
        state.websock = new WebSocket(wsuri);
        state.websock.onopen = onopen();
        state.websock.onmessage = onmessage();
        state.websock.onerror = onerror();
        state.websock.onclose = onclose();
      }
    }
  },
  ADD_WARNING(items) {
    let username = localStorage.getItem('username');
    let value = localStorage.getItem('warning_' + username);
    if (value === null || value === '') {
      console.info('添加到localStorage');
      state.tableData.push(formatTableData(items, state.tableData.length));
      localStorage.setItem('warning_' + username, JSON.stringify(state.tableData));
    } else {
      console.info('添加到localStorage');
      let value = localStorage.getItem('warning_' + username);
      state.tableData = JSON.parse(value);
      state.tableData.push(formatTableData(items, state.tableData.length));
      localStorage.setItem('warning_' + username, JSON.stringify(state.tableData));
    }
  },
  CLEARN_WARNING() {
    let username = localStorage.getItem('username');
    localStorage.setItem('warning_' + username, '');
    state.tableData = [];
  }
};

//异步方法
const actions = {};

const getters = {
  getTableData() {
    if (state.tableData.length === 0) {
      let username = localStorage.getItem('username');
      let value = localStorage.getItem('warning_' + username)
      if (value === null || value === '') {
        return state.tableData;
      }
      state.tableData = JSON.parse(value);
      return state.tableData;
    }
    return state.tableData;
  },
};

function formatTableData(items, order) {
  let innerData = items.data;
  return {
    order: order,
    code: items.code,
    message: items.message,
    time: innerData.time,
    read: false,
    data: innerData.data,
  };
}

function onopen() {
  //连接建立之后执行send方法发送数据
  console.log('websocket打开');
}

function onmessage(e) {
  console.log('收到信息--------------');
  let data = JSON.parse(e.data);
  console.log(data);
  if (data.message === 'warning') {
    console.log(data);
    let innerData = data.data;
    state.warningMessage.push(innerData.message);
    mutations.ADD_WARNING(innerData);
  } else if (data.message === 'operationLog') {
    state.operationLog = data.data;
  } else if (data.message === 'operationLogDate') {
    state.operationLogDate = data.data;
  } else if (data.message === 'monitorStatus') {
    state.monitorStatus = data.data;
  }
}

function onerror() {
  mutations.CONNET_WEBSOCKET();
}

function onclose(e) {
  mutations.CONNET_WEBSOCKET();
  alert("和服务器的连接关闭!");
  console.log('断开连接', e);
}

// 该部分为了获取websocket的相关方法。会发现此处跟mutations 里的写法是类似的，但是，想使用return，需要将相关数据写在getters里面。

const store = new Vuex.Store({
  state,
  mutations,
  actions,
  getters,
});
export default store;
