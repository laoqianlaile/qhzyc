<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
    request.setAttribute("basePath", basePath);
%>
<style>
    .coral-toolbar-border {
        display: block;
        /*width: auto;
        height: auto;*/
        /* overflow */
        /*overflow: hidden;*/
        position: relative;
        float: left;
        left: 0;
        top: 0;
        height: 48px;
    }
</style>
<div id="maxDiv${idSuffix}" class="fill">
    <ces:layout id="layout${idSuffix}" name="" style="width:800px;height:700px;" fit="true">
        <ces:layoutRegion region="north" split="true" style="height:190px;" title="农事项跟踪">
            <div class="fillwidth colspan2 clearfix" style="margin-top: 10px">
                <div class="app-inputdiv6">
                    <label class="app-input-label">农事项预计时间:</label>
                    <ces:datepicker id="ygStart${idSuffix}" width="36%" name="ygStart"></ces:datepicker>~
                    <ces:datepicker id="ygEnd${idSuffix}" width="36%" name="ygEnd"></ces:datepicker>
                </div>
                <div class="app-inputdiv6">
                    <label class="app-input-label">农事项完成时间:</label>
                    <ces:datepicker id="wcStart${idSuffix}" width="36%" name="wcStart"></ces:datepicker>~
                    <ces:datepicker id="wcEnd${idSuffix}" width="36%" name="wcEnd"></ces:datepicker>
                </div>
                <div class="app-inputdiv6">
                    <label class="app-input-label">农事项类型:</label>
                    <input id="nsxlx${idSuffix}" name="nsxlx"
                           data-options="valueField:'value',textField:'text',data:[{'value':'1','text':'播种'},{'value':'2','text':'灌溉'},{'value':'3','text':'施肥'},{'value':'4','text':'用药'},{'value':'5','text':'锄草'},{'value':'6','text':'采收'},{'value':'7','text':'其他'}]"
                           class="coralui-combobox"/>
                </div>
                <div class="app-inputdiv6">
                    <label class="app-input-label">负责人:</label>
                    <input id="fzr${idSuffix}" name="fzr" data-options="valueField:'DKFZRBH',textField:'DKFZR'"
                           class="coralui-combobox"/>
                </div>
                <div class="app-inputdiv6">
                    <label class="app-input-label">区域名称:</label>
                    <input id="qymc${idSuffix}" name="qymc" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv6">
                    <label class="app-input-label">地块名称:</label>
                    <input id="dkmc${idSuffix}" name="dkmc" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv6" style="display: none">
                    <label class="app-input-label" >评价状态:</label>
                    <ces:radiolist id="pjzt${idSuffix}" name="pjzt" value="1"
                                   column="3"
                                   data="[{'value':'1','text':'全部'},{'value':'2','text':'已评价'},{'value':'3','text':'待评价'}]"></ces:radiolist>
                </div>
                <div class="app-inputdiv6" style="display: none">
                    <label class="app-input-label">任务状态:</label>
                    <ces:radiolist id="rwzt${idSuffix}" name="radiolist" value="2"
                                   column="3"
                                   data="[{'value':'1','text':'全部'},{'value':'2','text':'已完成'},{'value':'3','text':'未完成'}]"></ces:radiolist>
                </div>
            </div>
            <div class="app-bsearch-tbar">
                    <%--<div style="margin-top: 10px;height:60px">--%>
                <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick1"
                             data="['->',{'label': '查询', 'id':'search','cls':'app-search-btn save', 'type': 'button'},{'label': '重置', 'id':'reset','cls':'app-reset-btn cancel', 'type': 'button'},'->']">
                </ces:toolbar>
                    <%--</div>--%>
            </div>
        </ces:layoutRegion>
        <ces:layoutRegion region="center" split="true">
            <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick2"
                         data="['->',{'label': '修改', 'id':'judge','cls':'', 'type': 'button'}]">
            </ces:toolbar>
            <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 250px;' id="nva${idSuffix}"> - 数据维护 </div></div></div>
            <div id="nsxgzGridDiv${idSuffix}" style="height:632px">
                <ces:grid rowNum="10" multiselect='true' id="nsxgzGrid${idSuffix}" height="auto"
                          url="${basePath}/zznsxgz!nsxgzSearch.json" rownumbers="true" fitStyle="fill">
                    <ces:gridCols>
                        <ces:gridCol name="ID" width="10" hidden="true">ID</ces:gridCol>
                        <ces:gridCol name="JXZ" width="10" hidden="true">JXZ</ces:gridCol>
                        <ces:gridCol name="FZRBH" width="10" hidden="true">FZRBH</ces:gridCol>
                        <ces:gridCol name="SCDABH" width="120">农事作业项编号</ces:gridCol>
                        <ces:gridCol name="SSQYMC" width="80">区域名称</ces:gridCol>
                        <ces:gridCol name="DKMC" width="80">地块名称</ces:gridCol>
                        <ces:gridCol name="NSXLX" width="60" edittype="combobox"
                                     editoptions="'data':[{'value':'1','text':'播种'},{'value':'2','text':'灌溉'},{'value':'3','text':'施肥'},{'value':'4','text':'用药'},{'value':'5','text':'锄草'},{'value':'6','text':'采收'},{'value':'7','text':'其他'}]">农事项类型</ces:gridCol>
                        <ces:gridCol name="NSZYXMC" width="80">农事项名称</ces:gridCol>
                        <ces:gridCol name="FZR" width="60">农事项负责人</ces:gridCol>
                        <ces:gridCol name="YJZYSJ" width="80">预计作业时间</ces:gridCol>
                        <ces:gridCol name="ZT" width="60" edittype="combobox"
                                     editoptions="'data':[{'value':'2','text':'已完成'},{'value':'3','text':'未完成'}]">状态</ces:gridCol>
                        <ces:gridCol name="KSSJ" width="80">农事项开始时间</ces:gridCol>
                        <ces:gridCol name="JSSJ" width="80">农事项结束时间</ces:gridCol>
                        <ces:gridCol name="CZR" width="50">操作人</ces:gridCol>
                    </ces:gridCols>
                    <ces:gridPager gridId="nsxgzGrid${idSuffix}"></ces:gridPager>
                </ces:grid>
            </div>
        </ces:layoutRegion>
    </ces:layout>
</div>
<script type="text/javascript">
    var _url = "${basePath}/zznsxgz!nsxgzSearch.json";
    $.extend($.ns("namespaceId${idSuffix}"), {
        toolbarClick1: function (e, data) {
            if (data.id == 'search') {
                if (!$.ns("namespaceId${idSuffix}").checkDate()) {
                    CFG_message("请检查查询条件!", "warning");
                    return;
                }
                $.ns("namespaceId${idSuffix}").setUrl();
                $.ns("namespaceId${idSuffix}").loadGridData();
            } else if (data.id == 'reset') {
                $("#ygStart${idSuffix}").datepicker("setDate", "");
                $("#ygEnd${idSuffix}").datepicker("setDate", "");
                $("#wcStart${idSuffix}").datepicker("setDate", "");
                $("#wcEnd${idSuffix}").datepicker("setDate", "");
                $("#nsxlx${idSuffix}").combobox("setValue", "");
                $("#fzr${idSuffix}").combobox("setValue", "");
                $("#qymc${idSuffix}").textbox("setValue", "");
                $("#dkmc${idSuffix}").textbox("setValue", "");
            }
        },
        toolbarClick2: function (e, data) {
            if (data.id == 'edit') {
                var selArrId = $("#nsxgzGrid${idSuffix}").grid("option", "selarrrow");
                if (selArrId.length != 1) {
                    CFG_message("请选择一条记录", "warning");
                    return;
                }
                var rowData = $("#nsxgzGrid${idSuffix}").grid("getRowData",selArrId);
                if (rowData.ZT=="已完成") {
                    CFG_message("该农事项已经完成,不可修改", "warning");
                    return;
                }
                if (rowData.JXZ=="2") {
                    CFG_message("该农事项正在进行中,不可修改", "warning");
                    return;
                }
                $.ns("namespaceId${idSuffix}").showEditDiv(rowData.ID, rowData.FZRBH, rowData.YJZYSJ, rowData.NSXLX);
            } else if (data.id == 'delete') {
                var selArrId = $("#nsxgzGrid${idSuffix}").grid("option", "selarrrow");
                if (selArrId.length < 1) {
                    CFG_message("请选择记录", "warning");
                    return;
                }
                var ids = [];
                for (var id in selArrId) {
                    var rowData = $("#nsxgzGrid${idSuffix}").grid("getRowData",selArrId[id]);
                    if (rowData.ZT=="已完成") {
                        CFG_message("农事项已经完成,不可删除", "warning");
                        return;
                    }
                    if (rowData.JXZ=="2") {
                        CFG_message("农事项正在进行中,不可删除", "warning");
                        return;
                    }
                    ids.push({lx:rowData.NSXLX,id:rowData.ID});
                }
                $.confirm("所选择的记录将被删除，确定吗？", function(sure) {
                    if (sure) {
                        $.ajax({
                            dataType: 'json',
                            url: $.contextPath + '/zznsxgz!deleteNsx.json?',
                            data: {ids:JSON.stringify(ids)},
                            success: function(data){
                                CFG_message("删除成功","success");
                                $.ns("namespaceId${idSuffix}").loadGridData();
                            },
                            error: function(data){
                                CFG_message("操作失败","error");
                            }
                        });
                    }
                });
            } else if (data.id == 'judge') {
                var selArrId = $("#nsxgzGrid${idSuffix}").grid("option", "selarrrow");
                if (selArrId.length < 1) {
                    CFG_message("请选择记录", "warning");
                    return;
                }else if(selArrId.length > 1){
                    CFG_message("请只选择一条记录", "warning");
                    return;
                }
                var ids = [];
                for (var id in selArrId) {
                    var rowData = $("#nsxgzGrid${idSuffix}").grid("getRowData",selArrId[id]);
                    if (rowData.KSSJ=="") {
                        CFG_message("农事项未操作,不可修改", "warning");
                        return;
                    }
                    ids.push(rowData.ID);
                }
                $.ns("namespaceId${idSuffix}").showJudgeDiv(ids);
            }
        },
        showEditDiv: function (_id, _fzr, _ygsj,_lx) {
            var jqGlobal = $("#maxDiv${idSuffix}");
            var content = "";
            content += "<div id='showDialog'>";
            content += "<div class ='toolbarsnav clearfix'>";
            content += "<div id = 'toolbar'></div>";
            content += "</div>";
            content += "<div class = 'fillwidth colspan2 clearfix' style='margin-top:20px'>";
            content += "<div class='app-inputdiv6'>";
            content += "<label class='app-input-label'>负责人:</label>";
            content += "<input id='fzrEdit${idSuffix}' width='60%'/>";
            content += "</div>";
            content += "<div class='app-inputdiv6'>";
            content += "<label class='app-input-label'>预估时间:</label>";
            content += "<input id = 'ygsjEdit${idSuffix}' width='60%' />";
            content += "</div>";
            content += "</div>";
            content += "</div>";
            var showDiv = $(content).appendTo(jqGlobal);
            showDiv.dialog({
                appendTo: jqGlobal,
                modal: true,
                autoOpen: true,
                title: "农事项修改",
                maxWidth: 600,
                width: 600,
                height: 250,
                maxHeight: 250,
                resizable: false,
                position:  ['center','center'],
                onOpen: function () {
                    var fzrData = $.loadJson($.contextPath + '/zznsxgz!getFzr.json');
                    $("#fzrEdit${idSuffix}").combobox({
                        valueField: 'DKFZRBH',
                        textField: 'DKFZR',
                        data: fzrData
                    });
                    $("#fzrEdit${idSuffix}").combobox("setValue",_fzr);
                    $("#ygsjEdit${idSuffix}").datepicker({});
                    $("#ygsjEdit${idSuffix}").datepicker("setDate",_ygsj);
                    var toolBarBtn = [{
                        "id": "confirmBtn",
                        "label": "确认",
                        "disabled": "false",
                        "onClick": updateNsx,
                        "type": "button",
                        "cls": "greenbtn"
                    }];
                    $("#toolbar").toolbar({
                        data: toolBarBtn
                    });
                    function updateNsx() {

                        var fzrbh = $("#fzrEdit${idSuffix}").combobox("getValue");
                        var fzr = $("#fzrEdit${idSuffix}").combobox("getText");
                        var ygsj = $("#ygsjEdit${idSuffix}").datepicker("getDateValue");
                        if (fzrbh == "" || ygsj == "") {
                            CFG_message("请填写完整信息", "warning");
                            return;
                        }
                        $.ajax({
                            dataType: "json",
                            url: $.contextPath + '/zznsxgz!updateNsx.json?id=' + _id + '&fzr=' + fzr + '&ygsj=' + ygsj + '&lx=' + _lx + '&fzrbh=' + fzrbh,
                            success: function (data) {
                                CFG_message("修改成功", "success");
                                showDiv.remove();
                                $.ns("namespaceId${idSuffix}").loadGridData();
                            },
                            error: function (data) {
                                CFG_message("操作失败", "error");
                            }
                        });
                    }
                },
                onClose: function () {
                    showDiv.remove();
                }
            });
        },
        showJudgeDiv: function (_ids) {
            var jqGlobal = $("#maxDiv${idSuffix}");
            var content = "";
            content += "<div id='showDialog'>";
            content += "<div class ='toolbarsnav clearfix'>";
            content += "<div id = 'toolbar'></div>";
            content += "</div>";
            content += "<div class = 'fillwidth colspan1 clearfix' style='margin-top:-20px'>";
            content += "<div class='app-inputdiv12'>";
            content += "<label class='app-input-label' style='margin-left: 80px'>开始时间:</label>";
            content += "<input id='KSSJ${idSuffix}'/>";
            content += "</div>";
            content += "</div>";
            content += "<div class = 'fillwidth colspan1 clearfix' style='margin-top:20px'>";
            content += "<div class='app-inputdiv12'>";
            content += "<label class='app-input-label' style='margin-left: 80px'>结束时间:</label>";
            content += "<input id='JSSJ${idSuffix}'/>";
            content += "</div>";
            content += "</div>";
            content += "<div class = 'fillwidth colspan1 clearfix' style='margin-top:20px'>";
            content += "<div class='app-inputdiv12'>";
            content += "<label class='app-input-label' style='margin-left: 80px'>操作人:</label>";
            content += "<input id='CZR${idSuffix}'/>";
            content += "</div>";
            content += "</div>";
            content += "</div>";
            var showDiv = $(content).appendTo(jqGlobal);
            showDiv.dialog({
                appendTo: jqGlobal,
                modal: true,
                autoOpen: true,
                title: "修改",
                maxWidth: 450,
                width: 450,
                height: 250,
                maxHeight: 250,
                resizable: false,
                position:  ['center','center'],
                onOpen: function () {
                    $("#KSSJ${idSuffix}").datepicker({
                        dateFormat:'yyyy-MM-dd HH:mm:ss',
                        srcDateFormat:'yyyy-MM-dd HH:mm:ss',
                        width:200
                    });
                    $("#JSSJ${idSuffix}").datepicker({
                        dateFormat:'yyyy-MM-dd HH:mm:ss',
                        srcDateFormat:'yyyy-MM-dd HH:mm:ss',
                        width:200
                    });
                    var czrData = $.loadJson($.contextPath + "/zznsxgz!getCzr.json");
                    $("#CZR${idSuffix}").combobox({
                        valueField: 'VALUE',
                        textField: 'TEXT',
                        data:czrData,
                        width:200
                    });
                    var toolBarBtn = [{
                        "id": "confirmBtn",
                        "label": "确认",
                        "disabled": "false",
                        "onClick": judge,
                        "type": "button",
                        "cls": "greenbtn"
                    }];
                    $("#toolbar").toolbar({
                        data: toolBarBtn
                    });
                    function judge() {
                        var czrbh = $("#CZR${idSuffix}").combobox("getValue");
                        var czr = $("#CZR${idSuffix}").combobox("getText");
                        if (czr == "") {
                            CFG_message("请选择操作人", "warning");
                            return;
                        }
                        var kssj = $("#KSSJ${idSuffix}").datepicker("getValue")
                        var jssj = $("#JSSJ${idSuffix}").datepicker("getValue")
                        if(kssj == ""){
                            CFG_message("请选择开始时间", "warning");
                            return;
                        }
                        $.ajax({
                            dataType: "json",
                            url: $.contextPath + '/zznsxgz!sjwhUpdate.json',
                            data: {id: _ids[0],czrbh: czrbh, czr:czr, kssj: kssj, jssj: jssj},
                            success: function (data) {
                                CFG_message("修改成功", "success");
                                showDiv.remove();
                                $.ns("namespaceId${idSuffix}").loadGridData();
                            },
                            error: function (data) {
                                CFG_message("操作失败", "error");
                            }
                        });
                    }
                },
                onClose: function () {
                    showDiv.remove();
                }
            });
        },
        showDelDiv: function (_ids) {
            var jqGlobal = $("#maxDiv${idSuffix}");
            var content = "";
            content += "<div id='showDialog'>";
            content += "<div id='message' class = 'coral-component-content'>"
            content += "<span class='coral-messager-box'>"
            content += "<span class='icon-info'>"
            content += "</span>"
            content += "<span class='coral-messager-content'>所选择的记录将被删除，确定吗？"
            content += "</span>"
            content += "</span>"
            content += "</div>";
            content += "<div class ='coral-dialog-buttonset'>";
            content += "<div id = 'toolbar'></div>";
            content += "</div>";
            content += "</div>";
            var showDiv = $(content).appendTo(jqGlobal);
            showDiv.dialog({
                appendTo: jqGlobal,
                modal: true,
                autoOpen: true,
                title: "确认提示",
                maxWidth: 300,
                width: 300,
                height: 150,
                maxHeight: 150,
                resizable: false,
                position:  ['center','center'],
                onOpen: function () {
                    var toolBarBtn = [{
                        "id": "confirmBtn",
                        "label": "确认",
                        "disabled": "false",
                        "onClick": judge,
                        "type": "button",
                        "cls": "coral-btn-primary"
                    },{
                        "id": "cancel",
                        "label": "取消",
                        "disabled": "false",
                        "onClick": cancel,
                        "type": "button",
                        "cls": ""
                    }];
                    $("#toolbar").toolbar({
                        data: toolBarBtn
                    });
                    function judge() {
                        $.ajax({
                            dataType: 'json',
                            url: $.contextPath + '/zznsxgz!deleteNsx.json?ids='+JSON.stringify(_ids),
                            success: function(data){
                                CFG_message("删除成功","success");
                                showDiv.remove();
                                $.ns("namespaceId${idSuffix}").loadGridData();
                            },
                            error: function(data){
                                CFG_message("操作失败","error");
                            }
                        });
                    }
                    function cancel() {
                        showDiv.remove();
                    }
                },
                onClose: function () {
                    showDiv.remove();
                }
            });
        },
        setPj: function (cellvalue, options, rawObject) {
            var goodStar = "<div style='height:30px;width:33px;float: left;background: url(\"cfg-resource/coral40/views/syncpsczyxxglxt/images/star.png\") no-repeat -14px -265px'></div>";
            var badStar = "<div style='height:30px;width:33px;float: left;background: url(\"cfg-resource/coral40/views/syncpsczyxxglxt/images/star.png\") no-repeat -14px -185px'></div>";
            if (cellvalue == null) {
                return "";
            } else if (cellvalue == "1") {
                return goodStar + goodStar + goodStar;
            } else if (cellvalue == "2") {
                return goodStar + goodStar + badStar;
            } else if (cellvalue == "3") {
                return goodStar + badStar + badStar;
            } else {
                return null;
            }
        },
        loadGridData: function () {
            $("#nsxgzGrid${idSuffix}").grid("option", "datatype", "json");
            $("#nsxgzGrid${idSuffix}").grid("option", "url", _url);
            $("#nsxgzGrid${idSuffix}").grid("reload");
        },
        setUrl: function () {
            var ygStart = $("#ygStart${idSuffix}").datepicker("getDateValue");
            var ygEnd = $("#ygEnd${idSuffix}").datepicker("getDateValue");
            var wcStart = $("#wcStart${idSuffix}").datepicker("getDateValue");
            var wcEnd = $("#wcEnd${idSuffix}").datepicker("getDateValue");
            var nsxlx = $("#nsxlx${idSuffix}").combobox("getValue");
            var fzr = $("#fzr${idSuffix}").combobox("getValue");
            var qymc = $("#qymc${idSuffix}").textbox("getValue");
            var dkmc = $("#dkmc${idSuffix}").textbox("getValue");
            var pjzt = $("#pjzt${idSuffix}").radiolist("getValue");
            var rwzt = $("#rwzt${idSuffix}").radiolist("getValue");
            _url = "${basePath}/zznsxgz!nsxgzSearch.json?ygStart=" + ygStart
                    + "&ygEnd=" + ygEnd + "&wcStart=" + wcStart + "&wcEnd=" + wcEnd + "&nsxlx=" + nsxlx
                    + "&fzr=" + fzr + "&qymc=" + qymc + "&dkmc=" + dkmc + "&pjzt=" + pjzt + "&rwzt=" + rwzt;
        },
        checkDate: function () {
            var ygStart = $("#ygStart${idSuffix}").datepicker("getDateValue");
            var ygEnd = $("#ygEnd${idSuffix}").datepicker("getDateValue");
            var wcStart = $("#wcStart${idSuffix}").datepicker("getDateValue");
            var wcEnd = $("#wcEnd${idSuffix}").datepicker("getDateValue");
            var ygStartDate = new Date(ygStart);
            var ygEndDate = new Date(ygEnd);
            var wcStartDate = new Date(wcStart);
            var wcEndDate = new Date(wcEnd);
            if (ygStartDate > ygEndDate || wcStartDate > wcEndDate) {
                return false;
            } else {
                return true;
            }
        }
    });
    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'sczzsjwh.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#maxDiv${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                <%--return $('#layoutId${idSuffix}').layout('panel', 'center');--%>
            },
            /** 初始化预留区 */
            'initReserveZones': function (configInfo) {
                <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength"));--%>
            },
            /** 获取返回按钮添加的位置 */
            'setReturnButton': function (configInfo) {
                //CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
                CFG_setCloseButton(configInfo, $('#toolbarId${idSuffix}'));
            },
            /** 获取关闭按钮添加的位置 */
            'setCloseButton': function (configInfo) {
                CFG_setCloseButton(configInfo, $('#toolbarId1${idSuffix}'));
            },
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                //加载负责人数据
                var fzr = $("#fzr${idSuffix}");
                fzr.combobox("reload", $.contextPath + '/zznsxgz!getFzr.json');
                //翻页事件
                $("#nsxgzGrid${idSuffix}").grid("option", "onPaging", function (e, data) {
                    $.ns("namespaceId${idSuffix}").loadGridData();
                });
                //radiolist 改变事件
                $("#pjzt${idSuffix}").radiolist("option", "onChange", function (e, data) {
                    if (!$.ns("namespaceId${idSuffix}").checkDate()) {
                        CFG_message("请检查查询条件!", "warning");
                        return;
                    }
                    $.ns("namespaceId${idSuffix}").setUrl();
                    $.ns("namespaceId${idSuffix}").loadGridData();
                });
                $("#rwzt${idSuffix}").radiolist("option", "onChange", function (e, data) {
                    if (!$.ns("namespaceId${idSuffix}").checkDate()) {
                        CFG_message("请检查查询条件!", "warning");
                        return;
                    }
                    $.ns("namespaceId${idSuffix}").setUrl();
                    $.ns("namespaceId${idSuffix}").loadGridData();
                });
                $.each($(".coral-toolbar-border"),function(e,data){
                    if(e == 1){
                        $(this).css("height","28px");
                    }
                });

                $.ns("namespaceId${idSuffix}").setUrl();
                $.ns("namespaceId${idSuffix}").loadGridData();
            }
        });
    });
</script>
