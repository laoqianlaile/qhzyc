<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String card = request.getParameter("card");
%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>触摸屏Demo</title>
    <link rel="stylesheet" href="css/reset.css">
    <link rel="stylesheet" href="jquery-cui/4.0.2/css/jquery.coral.min.css">
    <link rel="stylesheet" href="css/hdty-operation.css">
    <link rel="stylesheet" href="css/hdty-operation1.css">
    <link rel="stylesheet" href="css/minimal.css">
    <link rel="stylesheet" href="css/tinyscrollbar.css" media="screen"/>
</head>

<body>
<div class="wrapper">
    <!--头部-->
    <div class="head">
        <img src="images/opt-out.png">
    </div>

    <!--内容部分，主要分为两列，左边为内容区，右边为导航区-->
    <div id="tabs1">
        <ul>
            <li>
                <a href="#fragment-1" id="first-tab">
                    <div class="tab-info">
                        <div class="img"></div>
                        <div class="tab-name tab-name1">任务</div>
                        <div class="tips tips1" id="tasks"></div>
                    </div>
                </a>
            </li>
            <li>
                <a href="#fragment-2" id="second-tab">
                    <div class="tab-info">
                        <div class="img"></div>
                        <div class="tab-name">完成</div>
                        <div class="tips" id="complete"></div>
                    </div>
                </a>
            </li>
        </ul>
        <div id="fragment-1" class="clearfix">


        </div>
        <div id="fragment-2">
            <div class="buttons clearfix">
                <div class="button backbutton" id="back-button1">
                    <img src="images/back-icon.png" style="margin-left:-6%;">
                    <span>回退</span>
                </div>
            </div>
            <table class="table-head">
                <thead>
                <tr>
                    <td width="8.37%" class="left-td">
                        <div class="all-select">全选</div>
                    </td>
                    <td width="13.55%" class="relative">
                        <div class="relative"><div class="bg"></div>
                        <input id="department"/></div></td>
                    <td width="14.62%" class="relative">
                        <div class="relative"><div class="bg"></div>
                        <input id="name"/></div></td>
                    <td width="16.0%" class="relative">
                        <div class="relative"><div class="bg"></div>
                        <input id="combobox1" name="combobox" value="1"></div></td>
                    <td width="18.28%">作业名称</td>
                    <td width="13.07%">状态</td>
                    <td width="16.11%" class="right-td">操作</td>
                </tr>
                </thead>
            </table>
            <div id="scrollbar2">
                <div class="scrollbar">
                    <div class="track">
                        <div class="thumb">
                            <div class="end"></div>
                        </div>
                    </div>
                </div>
                <div class="viewport">
                    <div class="overview">
                        <table class="table-body">
                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">有机肥</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>

                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">浇水</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>

                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">播种</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>

                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">有机肥</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>

                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">有机肥</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>

                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">有机肥</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>

                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">有机肥</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>

                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">有机肥</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>

                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">有机肥</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>

                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">有机肥</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>

                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">有机肥</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>

                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">有机肥</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>

                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">有机肥</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>

                            <tr class="showTr1">
                                <td width="7.66%" class="left-td1">
                                    <div class="skin skin-minimal">
                                        <input type="checkbox" id="minimal-checkbox-1 " name="checkbox">
                                    </div>
                                </td>
                                <td width="13.17%">A区</td>
                                <td width="16.87%">地块101</td>
                                <td width="14.79%">
                                    <img src="images/fertilize.png" class="icon1"><span>施肥</span>
                                </td>
                                <td width="21.71%">有机肥</td>
                                <td width="12.15%">
                                    已完成
                                </td>
                                <td width="13.65%" class="right-td1" style="padding-left:4.3%;">
                                    <div class="backOpr">
                                        <img src="images/back-icon.png">
                                        <span class="textTip">回退</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fold1">
                                    <span class="record1">操作记录</span>
                                    <span class="start-time1">开始时间：<span class="start-record">2015-06-12 13:23:21</span></span>
                                    <span class="end-time1">结束时间：<span class="end-record">无</span></span>
                                    <span class="people1">责任人:<span class="people-record">张三</span></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="7" class="fx"></td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<div class="alertBox">
    <div class="alert-head">
        警告
    </div>
    <div class="alert-body">
        <div class="text">提示文字</div>
        <div class="selection">
            <input type="button" value="取消" class="cancel"/>
            <input type="button" value="确定" class="confirm"/>
        </div>
    </div>
</div>
<script src="jquery-cui/4.0.2/js/jquery.coral.min.js"></script>
<script src="js/jquery.tinyscrollbar.js"></script>
<script src="js/icheck.min.js"></script>
<script src="js/hdty-operation1.js"></script>
<script>

    /*********************************自定义全局变量 begin****************************************************/
    var _$data = {
        data: {},
        company: ""
    };
    var _url = "";
    var _data = {};
    var baseUrl = "/config1.0/services/jaxrs/farmOperationService";
    /*********************************自定义全局变量 end****************************************************/
//初始化复选框CSS
    function initCheck() {
        $('.skin-minimal input').iCheck({
            checkboxClass: 'icheckbox_minimal',
            increaseArea: '20%'
        });
    }

    $('#tabs1').tabs({
        cls: 'coral-tabs-right',
        active: '1'
    });

    var combo_data7;
    var land_Data;
    function queryArea() {
        _url = baseUrl + "/queryArea";
        _data = {
            operatorIcCode: "<%=card%>"
        };
        $.ajax({
            url: _url,
            data: _data,
            type: "GET",
            dataType: "json",
            async: false,
            success: function (data) {
                combo_data7 = data;
            },
            error: function () {
                alert("出错了！");
            }
        });
        return combo_data7;
    }
    queryArea();
    //地块初始化
    function queryLand(areaCode) {
        _url = baseUrl + "/queryLand";
        _data = {
            operatorIcCode: "<%=card%>",
            areaCode: areaCode
        };
        $.ajax({
            url: _url,
            data: _data,
            type: "GET",
            dataType: "json",
            async: false,
            success: function (data) {
                land_Data = data;
            },
            error: function () {
                alert("出错了！");
            }
        });
        return land_Data;
    }
    var $name = $("#name");

    $.parseDone(function () {
        $("#department").combobox({
            valueField: 'QYBH',
            textField: 'QYMC',
            onChange: "onChange",
            data: combo_data7,
            value: "all",
            panelComponentCls: "areaBlock",
            onShowPanel:"styleChange",
            onHidePanel:"styleChange1"
        });
        queryLand("all");
        $("#name").combobox({
            valueField: 'DKBH',
            textField: 'DKMC',
            onChange: "onChange2",
            data: land_Data,
            value: "all",
            panelComponentCls: "areaBlock",
            onShowPanel:"styleChange",
            onHidePanel:"styleChange1"
        });
    });
    function onChange(e, ui) {
        $("#name").combobox("reload", queryLand(ui.value));
        $("#name").combobox("setValue", "all");
        queryCompleteFarming();
    }

    function onChange2(e, ui) {
        queryCompleteFarming();
    }

    $('#combobox1').combobox({
        valueField: 'value',
        textField: 'text',
        data: [{
            "value": "all",
            "text": "作业类型（全部）",
            "selected": true
        }, {
            "value": "bz",
            "text": "播种"
        }, {
            "value": "sf",
            "text": "施肥",
        }, {
            "value": "gg",
            "text": "灌溉"
        }, {
            "value": "cc",
            "text": "锄草"
        }, {
            "value": "yy",
            "text": "用药"
        }, {
            "value": "qt",
            "text": "其它",
            icon: "opeicon6"
        }, {
            "value": "cs",
            "text": "采收",
            icon: "opeicon7"
        }],
        onShowPanel:"styleChange",
        onHidePanel:"styleChange1",
        onSelect: "getData",
        onChange: "onChange3",
        formatter: "formatter",
        panelComponentCls: "workBlock"
    });
    function onChange3(e, ui) {
        queryCompleteFarming();
    }

    $(document).ready(function () {
        setTimeout(function () {
            $('#scrollbar2').tinyscrollbar();
        }, 500)//设置延时，作用是等左边数据加载完再进行滚动条的初始化，以免出现滚不到底的现象
    });
    //icheck插件中的jq部分
    $(document).ready(function () {
        $('.skin-minimal input').iCheck({
            checkboxClass: 'icheckbox_minimal',
            increaseArea: '20%'
        });
    });


    /****************************获取企业编码 begin************************/
    _url = baseUrl + "/touchIn";
    _data = {
        card: "<%=card%>"
    };
    $.ajax({
        url: _url,
        data: _data,
        type: "GET",
        dataType: "json",
        async: false,
        success: function (data) {
            if (data.RESULT == "ERROR") {
                window.location.href = "<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/v2.0/hdty-login.jsp?card=" + card;
            } else if (data.RESULT == "SUCCESS") {
                _$data.data = data.DATA;
                setTimeout(queryCompleteFarming, 200);
            }
        },
        error: function () {
            alert("出错了！");
        }
    });
    /****************************获取企业编码 end************************/

    /******************************加载完成农事项 begin**********************/
    function queryCompleteFarming() {
        _data = {
            company: _$data.data.QYBM,
            area: $("#department").combobox("getValue"),
            land: $("#name").combobox("getValue"),
            farmingType: $("#combobox1").combobox("getValue"),
            is_end: "1",
            operatorIcCode: "<%=card%>"
        };
        _url = baseUrl + "/queryFarmingItem";
        $.ajax({
            url: _url,
            data: _data,
            type: "GET",
            dataType: "json",
            async: false,
            success: function (data) {
                $("#tasks").html(data.COUNT);
                addCompleteFarmingtItem(data.DATA);
            },
            error: function () {
                alert("出错了！");
            }
        });
    }

    function formatDate(date) {
        return date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate();
    }


    function addCompleteFarmingtItem(data) {
        var table_bodyJQ = $(".table-body tbody");
        table_bodyJQ.empty();
        //设置需要操作农事项条数
        $("#complete").html(data.length);
        //循环加载每一条需操作农事项
        $.each(data, function (e, data) {
            var farmingType = ""
            var farmingTypeImg = "";
            if (data.TYPE == "bz") {
                farmingType = "播种";
                farmingTypeImg = "images/plant.png";
            } else if (data.TYPE == "gg") {
                farmingType = "灌溉";
                farmingTypeImg = "images/watering.png";
            } else if (data.TYPE == "sf") {
                farmingType = "施肥";
                farmingTypeImg = "images/fertilize.png";
            } else if (data.TYPE == "yy") {
                farmingType = "用药";
                farmingTypeImg = "images/drug.png";
            } else if (data.TYPE == "cs") {
                farmingType = "采收";
                farmingTypeImg = "images/recovery.png";
            } else if (data.TYPE == "cc") {
                farmingType = "锄草";
                farmingTypeImg = "images/weeding.png";
            } else if (data.TYPE == "qt") {
                farmingType = "其他";
                farmingTypeImg = "images/detection.png";
            }
            var is_rollback = 1;
            var endOperationTime = formatDate(new Date(data.YGSJ2.replace(/-/g, "/")));
            var nowTime = formatDate(new Date());
            if (Date.parse(endOperationTime) < Date.parse(nowTime)) {
                is_rollback = 0
            }
            var html1 = html1 = "<tr class=\"showTr1\">" +
                    "<td width=\"7.66%\" class=\"left-td1\">" +
                    "<div class=\"skin skin-minimal\">";
            if (is_rollback == 1) {
                html1 += "<input type=\"checkbox\" id=\"minimal-checkbox-1\" name=\"checkbox\" flag=\"farmingCheckbox\" sid=\"" + data.SID + "\" farmingType=\"" + data.TYPE + "\">";
            }
                   html1 +=  "</div>" +
                    "</td>" +
                    "<td width=\"13.17%\">" + data.SSQYMC + "</td>" +
                    "<td width=\"16.87%\">" + data.DKMC + "</td>" +
                    "<td width=\"14.79%\">" +
                    "<img src=\"" + farmingTypeImg + "\" class=\"icon1\"><span>" + farmingType + "</span>" +
                    "</td>" +
                    "<td width=\"21.71%\">" + data.NSZYXMC + "</td>" +
                    "<td width=\"12.15%\">" +
                    "已完成" +
                    "</td>" +
                    "<td width=\"13.65%\" class=\"right-td1\" style=\"padding-left:4.3%;\">" ;
            var html2 = "";
            if (is_rollback == 1) {
                html2 = "<div class=\"backOpr\"  flag=\"rowRollBack\" sid=\"" + data.SID + "\" farmingType=\"" + data.TYPE + "\">" +
                        "<img src=\"images/back-icon.png\">" +
                        "<span class=\"textTip\">回退</span>" +
                        "</div>";
            }
            var html3 = "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td colspan=\"7\" class=\"fold1\">" +
                    "<span class=\"record1\">操作记录</span>" +
                    "<span class=\"start-time1\">开始时间：<span class=\"start-record\">" + data.BEGINTIME + "</span></span>" +
                    "<span class=\"end-time1\">结束时间：<span class=\"end-record\">" + data.ENDTIME + "</span></span>" +
                    "<span class=\"people1\">责任人:<span class=\"people-record\">" + data.OPERATOR + "</span></span>" +
                    "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td colspan=\"7\" class=\"fx\"></td>" +
                    "</tr>;";

            table_bodyJQ.append(html1 + html2 + html3);
        });
        $(".backOpr").css("width", $(".button").eq(0).width());
        $('.backOpr').css({'height': $('.backOpr').width() * 0.45, 'line-height': $('.backOpr').width() * 0.45 + 'px'});
        //初始化复选框样式
        initCheck();
        //刷新滚动条
        $(".overview").height(($(".table-body").height() + 50));
        $('#scrollbar2').tinyscrollbar();

        /*******************************查看操作人 begin*********************************************************/
        $(".showTr1").click(function () {
            $(".showTr1").children("td").removeClass("activeTd1");
            $(".showTr1").next("tr").children(".fold1").css("background-color", "#114100");
            $(".showTr1").next("tr").find(".record1, .start-time1, .end-time1, .people1").css("display", "none");
            $(this).children("td").addClass("activeTd1");
            $(this).find("[name='checkbox']").iCheck("check");
            $(this).next("tr").children(".fold1").css("background-color", "#fff");
            $(this).next("tr").find(".record1, .start-time1, .end-time1, .people1").css("display", "inline-block");
            //刷新滚动条
            $('#scrollbar2').tinyscrollbar();
        });
        /*******************************查看操作人 end*********************************************************/


        /*******************************回退操作点击事件 begin****************************************/
        $("div[flag=rowRollBack]").click(function (event) {
            event.stopPropagation();
            _this = $(this);
            _url = baseUrl + "/rollBack";
            _data = {
                sid: $(this).attr("sid"),
                ickbh: "<%=card%>",
                farmingType: $(this).attr("farmingType")
            }
            _this = $(this);
            $.ajax({
                url: _url,
                data: _data,
                type: "GET",
                dataType: "json",
                success: function (data) {
                    queryCompleteFarming();
                },
                error: function () {
                    alert("出错了！");
                }
            });
        });
        /*******************************回退操作点击事件 end****************************************/

    }
    /******************************加载完成农事项 end**********************/

    /*************************批量回退 begin*************************************************/
    $("#back-button1").click(function () {
        var sid = "";
        var farmingType = "";
        $.each($("input[flag=farmingCheckbox]"), function (e) {
            if ($(this).parents("div").attr("class").indexOf("checked") >= 0) {
                sid += $(this).attr("sid") + ",";
                farmingType += $(this).attr("farmingType") + ",";
            }
        });
        _url = baseUrl + "/rollBack";
        _data = {
            sid: sid,
            ickbh: "<%=card%>",
            farmingType: farmingType
        }
        $.ajax({
            url: _url,
            data: _data,
            type: "GET",
            dataType: "json",
            async: false,
            success: function (data) {
                if (data.RESULT != "ERROR") {
                    queryCompleteFarming();
                    if ($('.all-select').css("color") == "rgb(255, 255, 255)")
                        $('.all-select').click();
                }
            },
            error: function () {
                alert("出错了！");
            }
        });
    });
    /*************************批量回退 end*************************************************/


    $('#first-tab').click(function () {
        window.location.href = "<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/v2.0/hdty-operation.jsp?card=<%=card%>";
    });

    $(".head img").click(function(){
        window.location.href = "<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/v2.0/hdty-login.jsp";
    });
</script>
</body>
</html>


