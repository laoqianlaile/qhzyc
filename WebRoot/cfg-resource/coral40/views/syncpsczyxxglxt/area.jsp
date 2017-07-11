<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String gurl = path + "/appmanage/column-define!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=columnName,showName";
    String turl = path + "/appmanage/tree-define!tree.json?E_frame_name=coral&E_model_name=tree&P_ISPARENT=child&F_in=name,id&P_OPEN=true&P_filterId=parentId&P_CHECKED=838386ae45acc0c60145acc412bb0003";
    String card = request.getParameter("card");
    String qybm = request.getParameter("qybm");

%>

<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>农事记录触摸品子系统</title>
    <link rel="stylesheet" href="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/css/reset.css">
    <%--<link rel="stylesheet" type="text/css" href="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/css/style.css">--%>
    <link rel="stylesheet" href="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/css/style2.css">
    <link rel="stylesheet" href="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/css/win1.css">
    <link rel="stylesheet" href="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/css/win2.css">
    <link rel="stylesheet" href="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/css/minimal.css">
    <link rel="stylesheet" href="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/css/tinyscrollbar.css"
          type="text/css" media="screen"/>
</head>

<body>
<div id="wrapper">
    <!--头部-->
    <div class="head">
        <div id="exitSystem" class="exit clearfix">
            <img src="images/exit.png">
            <span>退出系统</span>
        </div>
        <div id="areaTip" class="tip">
        </div>
    </div>

    <!--内容部分，主要分为两列，左边为内容区，右边为导航区-->
    <div class="content clearfix">
        <div class="left">
            <div class="left showInfo">
                <img src="images/left-shadow.png" id="left-shadow">

                <div class="info">
                    <div class="infotopbg"></div>
                    <div class="text">
                        <dl>
                            <dt id="DKMC">7LW0101大棚</dt>
                            <dd id="SBSBH">传感器组编号：FE8E0B04004B1200</dd>
                            <dd id="JCSJ">监测时间：2014-09-28 11：30</dd>
                            <dd id="DQSD">大气湿度(%RH)：96.04</dd>
                            <dd id="DQWD">大气温度(℃)：20.84</dd>
                            <dd id="TRSD">土壤湿度(%RH)：100.00</dd>
                            <dd id="TRWD">土壤温度(℃)：23.34</dd>
                            <dd id="GZQD">光照强度（lux）：12089.32</dd>
                            <dd>二氧化碳浓度（ppm）：1201</dd>
                        </dl>
                    </div>
                    <div class="infobottombg"></div>
                </div>
            </div>

            <div class="show">
                <div class="info2">
                    <div id="areaName" class="rect">
                    </div>
                    <div class="triangle"></div>
                    <img src="images/page3-tian.png" id="areaImg">
                </div>
            </div>
            <div class="infobox">
                <div class="info">
                    <table>
                        <tr>
                            <td class="title" width="12%"><span id="all-check-select">全选</span></td>
                            <td class="title" id="nsxlx" width="14%">农事项名称</td>
                            <td class="title" width="16%">地块</td>
                            <td class="title">种植单元</td>
                            <td class="operate title">操作</td>
                        </tr>
                    </table>
                    <div id="scrollbar">
                        <div class="scrollbar">
                            <div class="track">
                                <div class="thumb">
                                    <div class="end"></div>
                                </div>
                            </div>
                        </div>
                        <div class="viewport" style="position: absolute;">
                            <div class="overview" id="nsxHeight" style="height:auto;">
                                <table id="table-content">
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="all-handle" onClick="infoShow2()">
                    批量操作
                </div>
            </div>
        </div>
        <div class="right clearfix">
            <div class="nav">
                <div lx="bz" class="square clearfix">
                    <span id="icon_1"></span>
                    <span class="text">播种</span>
                </div>
                <div lx="gg" class="square clearfix">
                    <span id="icon_2"></span>
                    <span class="text">灌溉</span>
                </div>
                <div lx="sf" class="square clearfix">
                    <span id="icon_3"></span>
                    <span class="text">施肥</span>
                </div>
                <div lx="yy" class="square clearfix">
                    <span id="icon_4"></span>
                    <span class="text">用药</span>
                </div>
                <div lx="cc" class="square clearfix">
                    <span id="icon_5"></span>
                    <span class="text">锄草</span>
                </div>
                <div lx="cs" class="square clearfix">
                     <span id="icon_6"></span>
                     <span class="text">采收</span>
                </div>
                <div lx="qt" class="square clearfix">
                    <span id="icon_7"></span>
                    <span class="text">其他</span>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="tanchuang">
    <div class="tc-title">
        <span>操作</span>
        <img src="images/close.png" id="closeBtn">
    </div>
    <div class="cnt clearfix">
        <!--内容的左边部分-->
        <div class="handle-info">
            <img src="images/left-shadow1.png" style="float:right;margin-top:100px;">

            <div class="biaoge" style="height:600px; overflow:scroll;">
                <table id="caption1">

                </table>
                <div id="tab-cnt">

                </div>
            </div>
        </div>
        <!--内容的右边部分-->
        <div class="cardIC">
            <div id="card-number"><input id="operationInput" type="text" value="请输入卡号"></div>
            <div id="confirm">
                <input type="button" id="operationButton" value="确定">
            </div>
            <%--<input type="text" id="operationInput" style="color:black;">--%>
            <%--<button style="color:black;" id="operationButton">确定</button>--%>
        </div>
    </div>
</div>
<div id="tanchuang1">
    <div class="tc-title1">
        <span>操作记录</span>
        <img src="images/close.png" id="closeBtn1">
    </div>
    <div class="cnt1">
        <div class="box">
            <div class="handle-info1">
                <table id="caption2">
                    <tr>
                        <td class="bor left-padding" width="12%">序号</td>
                        <td class="bor" width="35%">操作时间</td>
                        <td class="bor" width="10%" align="center" id="person">操作人</td>
                        <td width="20%" id="forthTd"></td>
                        <td class="bor" align="center">评价</td>
                    </tr>
                </table>
                <div id="scrollbar1">
                    <div class="scrollbar">
                        <div class="track">
                            <div class="thumb">
                                <div class="end"></div>
                            </div>
                        </div>
                    </div>
                    <div class="viewport">
                        <div class="overview">
                            <table id="tab-cnt1">

                                <%--<tr>--%>
                                <%--<td class="left-rad1" width="12%">1</td>--%>
                                <%--<td width="35%">2015-06-12 13:23:31</td>--%>
                                <%--<td width="10%" align="center">李四</td>--%>
                                <%--<td></td>--%>
                                <%--<td class="right-rad1">评价</td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                <%--<td class="fengxi"></td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                <%--<td class="left-rad1" width="12%">1</td>--%>
                                <%--<td width="35%">2015-06-12 13:23:31</td>--%>
                                <%--<td width="10%" align="center">李四</td>--%>
                                <%--<td></td>--%>
                                <%--<td class="right-rad1">评价</td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                <%--<td class="fengxi"></td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                <%--<td class="left-rad1" width="12%">1</td>--%>
                                <%--<td width="35%">2015-06-12 13:23:31</td>--%>
                                <%--<td width="10%" align="center">李四</td>--%>
                                <%--<td></td>--%>
                                <%--<td class="right-rad1">评价</td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                <%--<td class="fengxi"></td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                <%--<td class="left-rad1" width="12%">1</td>--%>
                                <%--<td width="35%">2015-06-12 13:23:31</td>--%>
                                <%--<td width="10%" align="center">李四</td>--%>
                                <%--<td></td>--%>
                                <%--<td class="right-rad1">评价</td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                <%--<td class="fengxi"></td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                <%--<td class="left-rad1" width="12%">1</td>--%>
                                <%--<td width="35%">2015-06-12 13:23:31</td>--%>
                                <%--<td width="10%" align="center">李四</td>--%>
                                <%--<td></td>--%>
                                <%--<td class="right-rad1">评价</td>--%>
                                <%--</tr>--%>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="cardIC" style="float:left;">
                <%--<img src="images/3.png" class="cardICanimate">--%>
                <%--<img src="images/4-1.png" class="cardICtooltip">--%>
                <%--<img src="images/4-2.png" class="cardIChand">--%>
                <%--<input type="text" id="pjInput" style="color:black;">--%>
                <%--<button style="color:black;" id="pjButton">确定</button>--%>
                    <div id="card-number"><input id="pjInput" type="text" value="请输入卡号"></div>
                    <div id="confirm">
                        <input type="button" id="pjButton" value="确定">
                    </div>
            </div>
        </div>
    </div>
</div>
</body>
<style>
    .bor {
        line-height: 50px;
    }

    #exitSystem:hover{
        cursor: pointer;
    }
</style>
<%--<%@include file="../../common/jsp/_cui_library.jsp" %>--%>
<script src="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/js/jquery.js"></script>
<script src="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/js/common.js"></script>
<script src="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/js/icheck.min.js"></script>
<script src="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/js/jquery.tinyscrollbar.js"></script>
<script src="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/js/page4.js"></script>

<script type="text/javascript">
    //qyData：当前刷卡的区域信息
    //dkData：当前刷卡的地块信息
    //lx:当前操作的农事项类型
    //operationId:当前操作的农事项ID
    //cz:批量评价
    //readCard:是否刷卡
    //czlx:操作类型:1,操作。2评价。
    var qybm;
    var _data = {
        qyData: "",
        dkData: "",
        lx: "",
        operationId: "",
        cz: {},
        readCard: "false",
        czlx: ""
    };

    $("#operationButton").click(function(){
        if(_data.czlx == "1"){
            operationButtonClick($("#operationInput").val());
        }else if(_data.czlx == "2"){
            pjButtonClick($("#operationInput").val());
        }
    });

    $("#pjButton").click(function(){
        if(_data.czlx == "1"){
            operationButtonClick($("#pjInput").val());
        }else if(_data.czlx == "2"){
            pjButtonClick($("#pjInput").val());
        }
    });

    function touchReadCard() {
        var data = __touch__("readCard");
        if(_data.readCard == "false"){
            return false;
        }
        var json = JSON.parse(data);
        if (json.status == true || json.status == "true")　{
//            alert(json.value);
            _data.readCard = "false";
            if(_data.czlx == "1"){
                operationButtonClick(json.value);
            }else if(_data.czlx == "2"){
                pjButtonClick(json.value);
            }
        } else {
//            alert(json.msg);
            if(_data.readCard != "false"){
                window.setTimeout(touchReadCard,1000);
            }
        }
    }


    $(function () {
        var card = '<%=card%>';
        qybm = '<%=qybm%>';
//        alert(card);
//        alert(qybm);
        if (card != null) {
            var url = $contextPath + baseUrl + "/queryCardInfo";
            $.ajax({
                url: url,
                data: {
                    qybm: qybm,
                    card: card
                },
                type: "GET",
                dataType: "json",
                success: function (data) {
                    var wlwxx = data.wlwxx;
                    data = data.data;
                    for (var i in data) {
                        if (data[i].QYICK == card) {
                            $("#areaName").html(data[i].QYMC);
                            $("#areaImg").prop("src", "images/page2-tian.png");
                            $(".left .showInfo").remove();
                            _data.qyData = data[i];
                        }
                    }
                    if (_data.qyData.toString().length == 0) {
                        for (var i in data) {
                            if (data[i].DKICK == card) {
                                $("#areaName").html(data[i].DKMC);
                                $("#areaImg").prop("src", "images/page3-tian.png");
                                _data.dkData = data[i];
                                if(wlwxx.toString().length != 0){
                                    $("#SBSBH").html("传感器组编号："+wlwxx.SBSBH);
                                    $("#JCSJ").html("监测时间："+wlwxx.JQSJ);
                                    $("#DKMC").html("大气湿度(%RH)："+data[i].DKMC);
                                    $("#DQSD").html("大气温度(℃)："+wlwxx.DQSD);
                                    $("#DQWD").html("大气温度(℃)："+wlwxx.DQWD);
                                    $("#TRSD").html("土壤湿度(%RH)："+wlwxx.TRSD);
                                    $("#TRWD").html("土壤温度(℃)："+wlwxx.TRWD);
                                    $("#GZQD").html("光照强度（lux）："+wlwxx.GZQD);
                                }
                            }
                        }
                    }
                },
                error: function () {
                    alert("出错了！");
                }
            });
        }
    });
    //初始化复选框CSS
    function initCheck() {
        $('.skin-minimal input').iCheck({
            checkboxClass: 'icheckbox_minimal',
            increaseArea: '20%'
        });
    }
    //全选按钮的点击事件
    $("#all-check-select").click(
            function () {
                var allCkecked = true;
                $("[name='checkbox']").each(function () {
                    if ($(this).parent().prop("class").indexOf("checked") == -1) {
                        allCkecked = false;
                    }
                });
                if (!allCkecked) {
                    $(this).css("background-color", "#114100");
                    $("[name='checkbox']").iCheck("check");
                } else {
                    $(this).css("background-color", "transparent");
                    $("[name='checkbox']").iCheck("uncheck");
                }
            }
    );
    //添加一行数据
    function addRowData(data) {
        $('#table-content').append(
                "<tr>" +
                "<td class=\"left-radius\">" +
                "<div class=\"skin skin-minimal\">" +
                "<input type=\"checkbox\" bid=\"" + data.BID + "\" name=\"checkbox\"/>" +
                "</div>" +
                "</td>" +
                "<td width=\"14%\">" + data.NSZYXMC + "</td>" +
                "<td width=\"16%\">" + data.DKMC + "</td>" +
                "<td>" + data.ZZDYMC + "</td>" +
                "<td class=\"right-radius\">" +
                "<div class=\"handle\" onClick=\"infoShow('" + data.BID + "')\">操作</div>" +
                "<div class=\"repeat\" onClick=\"doOneAgain('" + data.BID + "')\"><img src=\"images/repeat.png\"><span>重复</span></div>" +
                "<div class=\"save\" onClick=\"infoShow1('" + data.BID + "')\"><img src=\"images/save.png\"><span>操作记录</span></div>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td class=\"fengxi\"></td>" +
                "</tr>");
                picmessageScroll("scrollbar");
                initCheck();
    }
    //左边导航条点击事件
    $(".square").click(
            function () {
                $(this).removeClass("new-square1");
                $(this).siblings().removeClass("new-square");
                $(".tip").css("display", "block");
                if (_data.qyData.toString().length != 0) {
                    $("#areaTip").html("当前：" + _data.qyData.QYMC);
                } else if (_data.dkData.length != 0) {
                    $("#areaTip").html("当前：" + _data.dkData.QYMC + "/" + _data.dkData.DKMC);
                }
                $(this).addClass("new-square");
                $(this).siblings().addClass("new-square1");
                $(".show").remove();
                $(".left .showInfo").remove();
                $(".infobox").show();
                $('#scrollbar').tinyscrollbar();
                $(this).children().css("background-position","center bottom");
                $(this).siblings().children().css("background-position","center top");
                showNsx($(this).attr("lx"));
            }
    );

    //刷新滚动条
    function picmessageScroll(id){
        var $scrollbar = $("#" + id);
        $scrollbar.tinyscrollbar();
        var scrollbar = $scrollbar.data("plugin_tinyscrollbar");
        scrollbar.update();
    }

    //加载农事项
    function showNsx(lx) {
        _data.lx = lx;
        var url = $contextPath + baseUrl + "/queryNsx";
        var data;
        if (_data.dkData.toString().length > 0) {
            data = {
                qybm: qybm,
                qybh: "",
                dkbh: _data.dkData.DKBH,
                lx: lx
            };
        } else if (_data.qyData.toString().length > 0) {
            data = {
                qybm: qybm,
                qybh: _data.qyData.QYBH,
                dkbh: "",
                lx: lx
            };
        }
        $.ajax({
            url: url,
            data: data,
            type: "GET",
            dataType: "json",
            success: function (data) {
                //清空原来的列表
                $('#table-content').empty();
                var height = (data.length * (105)) + "px";
                $.each(data, function (e, data) {
                    addRowData(data);
                });
            },
            error: function () {

            }
        });
    }

    //点击操作记录
    function operationButtonClick(card) {
        var operationCard = card;
        if (operationCard.length == 0) {
            alert("IC卡信息错误，请检查IC卡！");
        } else {
            var url = $contextPath + baseUrl + "/operation";
            var data = {
                qybm: qybm,
                card: operationCard,
                ids: _data.operationId,
                lx: _data.lx
            }
            $.ajax({
                url: url,
                data: data,
                type: "GET",
                dataType: "json",
                success: function (data) {
                    if (data.result == "success") {
                        alert("操作成功！");
                        showCzjl();
                    } else if (data.result == "error") {
                        alert("操作失败！");
                    }
                },
                error: function () {

                }
            });
        }
    };

    function showCzjl() {
        $("#caption1").empty();
        $("#caption1").append("<tr>" +
                "<td class=\"bor left-padding\" width=\"10%\" style=\"font-size:18px;padding:0px\">序号</td>" +
                "<td class=\"bor\" width=\"20%\" style=\"font-size:18px\">农事项名称</td>" +
                "<td class=\"bor\" width=\"28%\" style=\"font-size:18px\">开始操作时间</td>" +
                "<td class=\"bor\" width=\"28%\" style=\"font-size:18px\">结束操作时间</td>" +
                "<td class=\"bor\"  width=\"14%\" align=\"center\" style=\"font-size:18px\">操作人</td>" +
                "</tr>");
        var url = $contextPath + baseUrl + "/queryCzjl";
        var data = {
            qybm: qybm,
            ids: _data.operationId,
            lx: _data.lx
        }
        $.ajax({
            url: url,
            data: data,
            type: "GET",
            dataType: "json",
            success: function (jsonData) {
                $.each(jsonData, function (index, data) {
                    $("#caption1").append("<tr>" +
                            "<td class=\"bor left-padding\" width=\"10%\" style=\"font-size:18px;padding:0px\">" + (index + 1) + "</td>" +
                            "<td class=\"bor\" width=\"20%\" style=\"font-size:18px;\">" + data.NSZYXMC + "</td>" +
                            "<td class=\"bor\" width=\"28%\" style=\"font-size:18px;\">" + data.KSSJ + "</td>" +
                            "<td class=\"bor\" width=\"28%\" style=\"font-size:18px;\">" + data.JSSJ + "</td>" +
                            "<td class=\"bor\" width=\"14%\" align=\"center\" style=\"font-size:18px;\">" + data.CZR + "</td>" +
                            "</tr>");
                });
            },
            error: function () {

            }
        });

    }

    function doOneAgain(id) {
        var url = $contextPath + baseUrl + "/doOneAgain";
        var data = {
            ids: id,
            lx: _data.lx
        }
        $.ajax({
            url: url,
            data: data,
            type: "GET",
            dataType: "json",
            success: function (jsonData) {
                if (jsonData.result = "SUCCESS") {
                    alert("操作成功");
                    showNsx(_data.lx);
                } else if (jsonData.result = "ERROR") {
                    alert("操作失败");
                }
            },
            error: function () {

            }
        });
    }

    function showPjCzjl() {
        $("#tab-cnt1").empty();
        var url = $contextPath + baseUrl + "/queryCzjl";
        var data = {
            qybm: qybm,
            ids: _data.operationId,
            lx: _data.lx
        };
        $.ajax({
            url: url,
            data: data,
            type: "GET",
            dataType: "json",
            success: function (jsonData) {
                var index;
                $.each(jsonData, function (idx, data) {
                    if (data.JSSJ.length != 0) {
                        if (data.PJ.toString().length == 0) {
                            data.PJ = 0;
                            $("#tab-cnt1").append("<tr>" +
                                    "<td class=\"left-rad1\" width=\"12%\" style=\"font-size: 16px\">" + (idx + 1) + "</td>" +
                                    "<td width=\"35%\" style=\"font-size: 16px\">开始时间：" + data.KSSJ + "<br/>结束时间：" + data.JSSJ + "</td>" +
                                    "<td width=\"10%\" align=\"center\" style=\"font-size: 16px\">" + data.CZR + "</td>" +
                                    "<td style=\"font-size: 16px\"><div class=\"starts\"><ul val='" + data.PJ.toString() + "'  czjlId='" + data.ID + "'><li rel=\"1\"></li><li rel=\"2\"></li><li rel=\"3\"></li></ul><input type=\"hidden\" value=\"\" ></div></td>" +
                                    "<td class=\"right-rad1\" style=\"font-size: 16px\"><input type=\"button\" value=\"评价\" class=\"commentbtn\"></td>" +
                                    "</tr>");
                        } else if (data.PJ.toString().length > 0) {
                            $("#tab-cnt1").append("<tr>" +
                                    "<td class=\"left-rad1\" width=\"12%\" style=\"font-size: 16px\">" + (idx + 1) + "</td>" +
                                    "<td width=\"35%\" style=\"font-size: 16px;\">开始时间：" + data.KSSJ + "<br/>结束时间：" + data.JSSJ + "</td>" +
                                    "<td width=\"10%\" align=\"center\" style=\"font-size: 16px\">" + data.CZR + "</td>" +
                                    "<td><div class=\"starts\"><ul  val='" + data.PJ.toString() + "'  czjlId='" + data.ID + "'><li rel=\"1\"></li><li rel=\"2\"></li><li rel=\"3\"></li></ul><input type=\"hidden\" value=\"\" ></div></td>" +
                                    "<td class=\"right-rad1\"><input type=\"button\" value=\"确定\" class=\"commentbtn\"></td>" +
                                    "</tr>");
                        }
                    }
                });
                var ulDoc = $(".starts ul");
                $.each(ulDoc, function (idx, data) {
                    var iindex = $(this).attr("val");
                    $.each($(this).children("li"), function (aidx, adata) {
                        if ((aidx + 1) <= iindex) {
                            $(this).css("background-position", "center -240px").css({width: "27px"});
                        } else {
                            $(this).css("background-position", "center -160px").css({width: "27px"});
                        }
                    });
                    $(this).parent().parent().next().children().parent().prev().children().animate({marginRight: "-70%"}, 20);
                    $(this).parent().parent().next().children().parent().removeClass("current");
                    if (iindex == 1) {
                        $(this).parent().parent().next().children().val("");
                        $(this).parent().parent().next().children().parent().css("background-position", "center -80px");
                    }
                    if (iindex == 2) {
                        $(this).parent().parent().next().children().val("");
                        $(this).parent().parent().next().children().parent().css("background-position", "center -160px");
                    }
                    if (iindex == 3) {
                        $(this).parent().parent().next().children().val("");
                        $(this).parent().parent().next().children().parent().css("background-position", "center -240px");
                    }

                });
                $(".handle-info1").animate({width: "760px"}, 80, function () {
                    $("#scrollbar1").css({width: "750px"});
                    $("#tab-cnt1").css({width: "720px"});
                    $(".cnt1 td").css({fontSize: "16px"});
                    $("#person").css({width: "10%"});
                    $("#forthTd").css({width: "17%"});
                    $(".starts").css({width: "45%"});
                });
                $.each($(".starts ul li"), function (idx, data) {
                    $(this).eq(idx).css("background-position", "center -160px").css({width: "27px"})
                });

                $(".commentbtn").click(function () {
                    if ($(this).attr("value") == "评价") {
                        $(this).attr("value", "确定");
                        $(this).attr("font-size", "20px");
                        $(this).parent().prev().children().animate({marginRight: "0"}, 200);
                        $(this).parent().prev().children().children().children().css("background-position", "center -160px").css({width: "27px"});
                        $(this).parent().addClass("current");
                        //$(this).parents('table').siblings().find('right-rad1').attr('data-role','readonly');

                    } else {
                    }

                });

                $(".starts ul li").click(
                        function () {
                            index = $(this).index();
                            _data.cz[$(this).parent().attr("czjlId")] = index + 1;
                            $(this).prevAll().css("background-position", "center -240px").css({width: "27px"});
                            $(this).css("background-position", "center -240px").css({width: "27px"});
                            $(this).nextAll().css("background-position", "center -160px").css({width: "27px"});
                            $(".handle-info1").animate({width: "760px"}, 80, function () {
                                //calback
                                $("#scrollbar1").css({width: "750px"});
                                $("#tab-cnt1").css({width: "720px"});
                                $(".cnt1 td").css({fontSize: "16px"});
                                $("#person").css({width: "10%"});
                                $("#forthTd").css({width: "17%"});
                                $(".starts").css({width: "45%"});
                            });

                        }
                );
                _data.czlx = "2";
                _data.readCard = "true";
                window.setTimeout(touchReadCard,1000);
                $("#closeBtn1").click(
                        function(){
                            $("#pjInput").val();
                            $("#operationInput").val();
                            _data.readCard = "false";
                            document.body.removeChild(bgObj);
                            $("#tanchuang1").css("display","none");
                            $("#wrapper").css("webkitFilter","none");
                            $("#wrapper").css("mozFilter","none");
                            $("#wrapper").css("msFilter","none");
                            $("#wrapper").css("oFilter","none");
                            $("#wrapper").css("filter","none");
                        }
                );

            },
            error: function () {

            }
        });
    }


    //评价刷卡
    function pjButtonClick(ickbh) {
        var card = ickbh;
        var jsonData = "{";

        $.each(_data.cz, function (idx, data) {
            jsonData += "\"" + idx + "\":\"" + data + "\",";
        });
        jsonData += "\"NULL\":\"NULL\"}";
        var data = {
            qybm: qybm,
            card: card,
            cz: jsonData
        }
        var url = $contextPath + baseUrl + "/commentNsx";
        $.ajax({
            url: url,
            data: data,
            type: "GET",
            dataType: "json",
            success: function (jsonData) {
                if (jsonData.result == "SUCCESS") {
                    alert("操作成功");
                    showPjCzjl();
                    _data.cz = {};
                } else if (jsonData.result == "ERROR") {
                    alert("操作失败");
                }
            },
            error: function () {
            }
        });
    };

    $("#exitSystem").click(function(){
        window.location.href="<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/index.jsp";
    });


</script>
</html>
