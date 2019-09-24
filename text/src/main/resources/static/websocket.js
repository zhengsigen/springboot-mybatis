var websocket = null;
var _top = 80;
var index = 0;

var host = window.location.host;
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket("ws://" + "192.168.6.182:9090" + "/mywebsocket");
} else {
    alert("Not Support WebSocket!");
}

//连接发生错误的回调方法
websocket.onerror = function () {
    setMessageInnerHTML("error");
};

//连接成功建立的回调方法
websocket.onopen = function (event) {
    setMessageInnerHTML("open");
};

//接收到消息的回调方法
websocket.onmessage = function () {
    setMessageInnerHTML(event.data);
};

//连接关闭的回调方法
websocket.onclose = function () {
    setMessageInnerHTML("close");
};

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    websocket.close();
};

//将消息显示在网页上
function setMessageInnerHTML(innerHTML) {
    $(".d_show").append("<div id='" + index + "'>" + innerHTML + "</div>");
    launch();
}

//发送消息
function send() {
//var message = document.getElementById('text').value;
    var message = $(".s_text").val();
    $(".s_text").val("");
    websocket.send(message);
}