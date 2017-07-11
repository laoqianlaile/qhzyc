<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String gurl = path + "/appmanage/column-define!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=columnName,showName";
    String turl = path + "/appmanage/tree-define!tree.json?E_frame_name=coral&E_model_name=tree&P_ISPARENT=child&F_in=name,id&P_OPEN=true&P_filterId=parentId&P_CHECKED=838386ae45acc0c60145acc412bb0003";
    Date date = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String yearDate = df.format(date);
    df = new SimpleDateFormat("hh:mm:ss");
    String time = df.format(date);
%>

<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>种植业农产品生产数据采集软件V1.0</title>
    <link rel="stylesheet" type="text/css" href="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/css/reset.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/css/style.css">
</head>
<body>
<div id="wrapper">
    <!--头部-->
    <div class="head"></div>
    <!--内容部分，主要分为三列，中间列宽560px，左右两列宽440px-->
    <div class="content clearfix">
        <div class="left">
            <img src="images/left-shadow.png" id="left-shadow">

            <div class="info">
                <div class="infotopbg"></div>
                <div class="text">
                    <dl>
                        <dt>7LW0101大棚</dt>
                        <dd>传感器组编号：FE8E0B04004B1200</dd>
                        <dd>监测时间：2014-09-28 11：30</dd>
                        <dd>大气湿度(%RH)：96.04</dd>
                        <dd>大气温度(℃)：20.84</dd>
                        <dd>土壤湿度(%RH)：100.00</dd>
                        <dd>土壤温度(℃)：23.34</dd>
                        <dd>光照强度（lux）：12089.32</dd>
                        <dd>二氧化碳浓度（ppm）：1201</dd>
                    </dl>
                </div>
                <div class="infobottombg"></div>
            </div>
        </div>
        <div class="center">
            <div class="cardIC">
                <%--<a href="page2.html"><img src="images/1.png" class="cardICanimate"></a>--%>
                <%--<img src="images/2-1.png" class="cardICtooltip">--%>
                <%--<img src="images/2-2.png" class="cardIChand">--%>
                    <div id="card-number"><input id="readCardInput" type="text" value="请输入卡号"></div>
                    <div id="confirm">
                        <input id="readCardButton" type="button" value="确定">
                    </div>
            </div>
        </div>
        <div class="right">
            <img src="images/right-shadow.png" id="right-shadow">

            <div class="weather">
                <img src="images/weather-icon.png" id="icon"/>
                <span>29～23°C</span><br/>
                <span>阴天 西北风微风</span>
            </div>
            <div class="time">
                <div class="time-show">
                    <div id="hour">10</div>
                    <img src="images/maohao.png"></span>
                    <div id="minute">15</div>
                    <img src="images/maohao.png"></span>
                    <div id="second">00</div>
                </div>
                <span id="date">周日 2015年09月06日</span>
            </div>
            <%--<form id="enterForm${idSuffix}" action=""--%>
            <%--enctype="multipart/form-data" method="post" class="coralui-form">--%>
            <%--<div class="fillwidth colspan3 clearfix">--%>
            <%--<div id="readCardDiv" style="margin-top: 30px;width:500px" class="app-inputdiv4">--%>
                <%--<input type="text" id="readCardInput" style="color:black;">--%>
                <%--<button style="color:black;" id="readCardButton" onclick="clickReadCard()">确定</button>--%>
            <%--</div>--%>
            <%--</div>--%>
            <%--</form>--%>
        </div>
    </div>
</div>

</body>
<%--<%@include file="../../common/jsp/_cui_library.jsp" %>--%>
<script src="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/js/jquery.js"></script>
<script src="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/js/common.js"></script>
<script src="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/js/page1.js"></script>
<script type="text/javascript">
    var qybm;
    /*****************刷卡 begin********************/
    $(function(){
        function touchReadCard() {
            var data = __touch__("readCard");
            var json = JSON.parse(data);
            if (json.status == true || json.status == "true")　{
//                alert(json.value);
                $.ajax({
                    url:$contextPath + baseUrl + "/getQybm",
                    data:{
                        ickbh:json.value
                    },
                    type:"GET",
                    dataType:"json",
                    success:function(data){
                        qybm = data.QYBM;
                        clickReadCard(json.value);
                    },
                    error:function(){
                        alert("出错了！");
                    }
                });
            } else {
                window.setTimeout(touchReadCard,1000)
            }
        }

        window.setTimeout(touchReadCard,1000);


    });
    /*****************刷卡 end********************/
    /*****************输卡 begin*************/
    $("#readCardButton").click(function(){
        var card = $("#readCardInput").val();
        $.ajax({
            url:$contextPath + baseUrl + "/getQybm",
            data:{
                ickbh:card
            },
            type:"GET",
            dataType:"json",
            success:function(data){
                qybm = data.QYBM;
                clickReadCard(card);
            },
            error:function(){
                alert("出错了！");
            }
        });
    });
    /*****************输卡 end*************/
    /******************点击进入 begin**************/
    $("#readCardButton").click(function(){
        var card = $("#readCardInput").val();
        alert(card);
        clickReadCard(card);
    });

    /******************点击进入 end**************/

    function clickReadCard(card) {
//        var card = $("#readCardInput").val();
        var card = card;
        if(card.length > 0){
        var url = $contextPath + baseUrl + "/queryCardInfo";
            $.ajax({
                url:url,
                data:{
                  qybm:qybm,
                    card:card
                },
                type:"GET",
                dataType:"json",
                success:function(data){
                    var wlwxx = data.wlwxx;
                    data = data.data;
                    if(data.length == 0){
                        alert("IC卡信息错误，请检查IC卡！");
                        window.setTimeout(touchReadCard,1000);
                    }else{
                        window.location.href="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/area.jsp?card="+card+"&qybm="+qybm;
                    }
                },
                error:function(){
                    alert("出错了！");
                }
            });
        }else{
            alert("请输入IC卡号！");
        }
    }


</script>
</html>
