<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    String path = request.getContextPath();
%>
<!doctype html>
<html>
    <head>
        <meta charset="utf-8">
        <title>触摸屏Demo</title>
        <link rel="stylesheet" href="css/reset.css">
        <link rel="stylesheet" href="css/hdty-new.css">
        <script src="js/jquery.js"></script>
    </head>
    
    <body>
        <div class="wrapper">
            <!--头部-->
            <div class="head">
            	
            </div>
            
            <!--内容部分，主要分为两列，左边为内容区，右边为导航区-->
            <div class="content clearfix">
				<div class="login">
                	<div class="number-input">
                    	<img src="images/please.png">
                        <div class="number"><input type="input" value="" id="number" /></div>
                    </div>
                    <div class="button-box">
                    	<input type="submit" value="" id="login-button" />
                    </div>
                </div>
            </div>
            
            <!--页脚部分-->
            <div class="footer">
            	<div class="time-weather-info">
                	<span id="date"></span>
                    <span id="time"></span>
                    <span id="weather">温度 29～23°C 阴天 西北风微风</span>
                </div>
                <div class="version">
                	版本号 V1.0
                </div>
            </div>
            <div class="keyboard">
            	
                <div class="num-buttons clearfix">
                	<div class="num-button" id="num1">1</div>
                    <div class="num-button" id="num2">2</div>
                    <div class="num-button" id="num3">3</div>
                    <div class="num-button" id="num4">4</div>
                    <div class="num-button" id="num5">5</div>
                    <div class="num-button" id="num6">6</div>
                    <div class="num-button" id="num7">7</div>
                    <div class="num-button" id="num8">8</div>
                    <div class="num-button" id="num9">9</div>
                    <div class="num-button" id="num0">0</div>
                    <div class="num-button backspace" id="backspace"></div>
                </div>
            </div>
        </div>
        <script src="js/hdty-login.js"></script>
    </body>
</html>

<script>
    var baseUrl = "/config1.0/services/jaxrs/farmOperationService";

    $(function () {
        /*****************刷卡 begin********************/
        function touchReadCard() {
            var data = __touch__("readCard");
            var json = JSON.parse(data);
            if (json.status == true || json.status == "true") {
                clickReadCard(json.value);
            } else {
                window.setTimeout(touchReadCard, 1000)
            }
        }

        window.setTimeout(touchReadCard, 1000);

        /*****************刷卡 end********************/
        /*****************输卡 begin*************/
        $("#login-button").click(function () {
            var card = $("#number").val();
            clickReadCard(card);
        });
        /*****************输卡 end*************/
        /********************获取服务器时间 begin******************/
        function getWebTime(){
            var url = baseUrl + "/getWebTime";
            $.ajax({
                url: url,
                type: "GET",
                dataType: "json",
                success: function (data) {
                    document.getElementById("time").innerHTML=data.time;
                    document.getElementById("date").innerHTML=data.date;
                    window.setTimeout(getWebTime, 1000);
                },
                error: function () {
                    alert("出错了！");
                }
            });
        }
        window.setTimeout(getWebTime, 1000);
        /*********************获取服务器时间 end*******************/

        function clickReadCard(card) {
            var card = card;
            if (card.length > 0) {
                var url = baseUrl + "/touchIn";
                $.ajax({
                    url: url,
                    data: {
                        card: card
                    },
                    type: "GET",
                    dataType: "json",
                    success: function (data) {
                        if (data.RESULT == "ERROR") {
                            alert(data.MESSAGE);
                            window.setTimeout(touchReadCard, 1000);
                        } else if (data.RESULT == "SUCCESS") {
                            window.location.href = "<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/v2.0/hdty-operation.jsp?card=" + card;
                        }
                    },
                    error: function () {
                        alert("出错了！");
                    }
                });
            } else {
                alert("请输入IC卡号！");
            }
        }
    });

</script>
