import {Message,MessageBox} from 'element-ui';

function restoreIllegalChar(string) {
  return string.replace(/\/n/g, '<br/>').replace(/\/t/g, '&nbsp;&nbsp;').replace(/\/r/g, '</br>');
}

function alertErrorMessage(description, error) {
  Message({
    dangerouslyUseHTMLString: true,
    type: 'error',
    message: '<strong>' + description + '<br/>报错信息：'
      + restoreIllegalChar(error) + '</strong>'
  });
}

// 将四个全局公共方法，组合成一个对象，并暴露出去
export default {
  restoreIllegalChar,
  alertErrorMessage,
}
