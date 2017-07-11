<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("gurl", "");
    request.setAttribute("turl", "");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<style type="text/css">
    .app-input-label {
        float: left;
    }
    .coral-grid .coral-textbox-default {
        left: 12px;
    }
</style>
<div id="max${idSuffix}" class="fill">
    <div class="fill">
        <div class="toolbarsnav clearfix">
            <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                         data="['->',{'label': '关闭', 'id':'CFG_closeComponentZone','cls':'return_tb', 'disabled': 'false','type': 'button'}]">
            </ces:toolbar>
            <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> - 出场管理 - 详情 </div></div></div>
        </div>
        <form id="pcxxForm${idSuffix}" action="${basePath}"
              enctype="multipart/form-data" method="post" class="coralui-form">
            <div class="app-inputdiv4" style="height:32px;display: none">
                <input id="ID${idSuffix}" class="coralui-textbox" data-options="readonly:true" name="ID"/>
            </div>
            <div class="app-inputdiv4" style="height:32px;display: none">
                <input id="QYBM${idSuffix}" class="coralui-textbox" data-options="readonly:true" name="QYBM"/>
            </div>
            <div class="app-inputdiv4" style="height:32px;display: none">
                <input id="QYMC${idSuffix}" class="coralui-textbox" data-options="readonly:true" name="QYMC"/>
            </div>
            <div class="app-inputdiv4" style="height:32px;display: none">
                <input id="DKMC${idSuffix}" class="coralui-textbox" data-options="readonly:true" name="DKMC"/>
            </div>
            <div class="app-inputdiv4" style="height:32px;display: none">
                <input id="PLBH${idSuffix}" class="coralui-textbox" data-options="readonly:true" name="PLBH"/>
            </div>
            <div class="app-inputdiv4" style="height:32px;display: none">
                <input id="PZBH${idSuffix}" class="coralui-textbox" data-options="readonly:true" name="PZBH"/>
            </div>

            <div class="fillwidth colspan3 clearfix">
                <!------------------ 第一排开始---------------->
                <div class="app-inputdiv4" style="height:32px;">
                    <label class="app-input-label">出场流水号：</label>
                    <input id="CCLSH${idSuffix}" name="CCLSH" data-options="readonly:true" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">区域名称：</label>
                    <input id="QYBH${idSuffix}" name="QYBH"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">地块名称：</label>
                    <input id="DKBH${idSuffix}" name="DKBH"/>
                </div>
                <!------------------ 第一排结束---------------->

                <!------------------ 第二排开始---------------->

                <div class="app-inputdiv4">
                    <label class="app-input-label">生产档案编号：</label>
                    <input id="SCDABH${idSuffix}" name="SCDABH" />
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">采收批次号：</label>
                    <input id="PCH${idSuffix}" name="PCH"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">品类：</label>
                    <input id="PL${idSuffix}" name="PL" class="coralui-textbox" data-options="required:true,readonly:true"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">品种：</label>
                    <input id="PZ${idSuffix}" name="PZ" class="coralui-textbox" data-options="required:true,readonly:true"/>
                </div>
                <!------------------ 第二排结束---------------->

                <!------------------ 第三排开始---------------->
                <div class="app-inputdiv4">
                    <label class="app-input-label">质量等级：</label>
                    <input id="ZLDJ${idSuffix}" name="ZLDJ" />
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">采收重量：</label>
                    <input id="ZL${idSuffix}" name="ZL" class="coralui-textbox" data-options="required:true,readonly:true"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">库存重量：</label>
                    <input id="KCZL${idSuffix}" name="KCZL" class="coralui-textbox" data-options="required:true,readonly:true"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">出场时间：</label>
                    <input id="CCSJ${idSuffix}" name="CCSJ"  class="coralui-datepicker" data-options="required:true,dateFormat:'yyyy-MM-dd HH:mm:ss',srcDateFormat:'yyyy-MM-dd HH:mm:ss'"/>
                </div>
                <div class="app-inputdiv8">
                    <label class="app-input-label">备注：</label>
                    <textarea id="BZ${idSuffix}" name="BZ" class="coralui-textbox"/>
                </div>
                <!------------------ 第三排结束---------------->
            </div>
        </form>
        <%--<div class="toolbarsnav clearfix">--%>
            <%--<ces:toolbar id="toolbarId1${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick1"--%>
                         <%--data="['->',{'label': '添加', 'id':'add', 'disabled': 'false','type': 'button'},{'label': '常用客户', 'id':'addDefault', 'disabled': 'false','type': 'button'},'','','','','','']">--%>
            <%--</ces:toolbar>--%>
        <%--</div>--%>
        <ces:grid id="gridId${idSuffix}" shrinkToFit="true" forceFit="true" fitStyle="fill" datatype="local" rownumbers="true"
                  clicksToEdit="1">
            <ces:gridCols>
                <ces:gridCol name="ID" editable="false" hidden="true" width="100">ID</ces:gridCol>
                <ces:gridCol name="PID" editable="false" hidden="true" width="80">PID</ces:gridCol>
                <ces:gridCol name="KHMC" formatter="text"     formatoptions="required: true,readonly:true" hidden="true" width="80">客户名称</ces:gridCol>
                <ces:gridCol name="KHBH" formatter="combobox" formatoptions="required: true,readonly:true" width="100">客户名称</ces:gridCol>
                <ces:gridCol name="DDBH" formatter="combobox" formatoptions="required: true,readonly:true" width="100">销售订单号</ces:gridCol>
                <ces:gridCol name="PSFS" formatter="combobox" formatoptions="required: true,readonly:true"   width="100">配送方式</ces:gridCol>
                <ces:gridCol name="PSDZ" formatter="text" formatoptions="required: true,readonly:true" width="100">配送地址</ces:gridCol>
                <ces:gridCol name="CCSJ" formatter="datepicker" formatoptions="required: true,readonly:true"  width="100">出场时间</ces:gridCol>
                <ces:gridCol name="CCZL" formatter="text" formatoptions="readonly:true,required: true,validType:'float'" width="100">出场重量</ces:gridCol>
            </ces:gridCols>
        </ces:grid>
    </div>
</div>

<script type="text/javascript">
    //    var url = "/services/jaxrs/register/captcha?t="+timestamp;
    var barCode = "";
    $.extend($.ns("namespaceId${idSuffix}"), {
        toolbarClick: function (event, button) {
            if (button.id == "save") {//保存的方法
                $("#pcxxForm${idSuffix}").form().submit();

            } else if (button.id == "CFG_closeComponentZone") {
                CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
            } else if (button.id == "readCode") {
                showDialog();
            }
        },
        toolbarClick1: function (e, ui) {
            if (ui.id == "readCode") {
                showDialog();
            } else if(ui.id == "add"){
                var $grid = $("#gridId${idSuffix}");
                if (!$grid.grid('valid')) {
                    CFG_message("请先输入完上一条客户！", "error");
                    return false;
                }
                var $cGrid = $("#gridId${idSuffix}");
                var gridData = $cGrid.grid("getRowData");
                var xh = generateId("tmp");
                $cGrid.grid("addRowData", xh, {}, "last");
            } else if("delDetailGridData" == ui.id){
                $.confirm("确定删除", function (r) {
                    if (r) {
                        $("#" + e.data.gridId).grid("delRowData", e.data.rowId);
                    }
                });
            } else if("addDefault" == ui.id){
                var pch = $("#PCH${idSuffix}").combobox("getValue");
                if(pch.length == 0){
                    CFG_message("请先输入批次号！", "error");
                    return false;
                }
                var $grid = $("#gridId${idSuffix}");
                //删除旧数据
                var $delGridIds = $grid.grid("getDataIDs");
                $.each($delGridIds, function(e,data){
                    $grid.grid("delRowData",data);
                });
                //添加新数据
                $grid.grid("option", "datatype", "json");
                var $cykhData = $.loadJson($.contextPath + "/zzcc!getCykh.json?pch=" + pch);
                $.each($cykhData.data, function(e,khData){
                    var xh = generateId("tmp");
                    $grid.grid("addRowData", xh, khData, "last");
                });
                //加载相应的客户订单
                var ids = $grid.grid("getDataIDs");
                $.each(ids,function(i,id){
                    var $ddbhCombobox = $grid.grid("getCellComponent",  id, "DDBH");
                    var $khbhCombobox = $grid.grid("getCellComponent",  id, "KHBH");
                    var $gridData = $grid.grid("getRowData");
                    var ddbhs = "";
                    $.each($gridData,function(i,d){
                        if(d.DDBH != "" && d.DDBH != "null"){
                            ddbhs += d.DDBH + ",";
                        }
                    });
                    var $ddbhData = $.loadJson($.contextPath + "/zzcc!getXsddbhByKhbh.json?khbh=" + $khbhCombobox.combobox("getValue") +"&ddbh=" + ddbhs);
                    $ddbhCombobox.combobox("reload",$ddbhData);
                });
            }
        },
        init: function(){
            //初始区域编号
            var $qyData = $.loadJson($.contextPath + "/zzcc!getQybh.json");
            $('#QYBH${idSuffix}').combobox({
                valueField:'QYBH',
                textField:'QYMC',
                data:$qyData,
                onChange: function(e,data){
                    /********************************表单初始化 begin****************************/
                    $('#QYMC${idSuffix}').textbox("setValue",data.text);
                    var $dkData = $.loadJson($.contextPath + "/zzcc!getDkbhByQybh.json?qybh=" + data.value);
                    $("#DKBH${idSuffix}").combobox("reload",$dkData);
                    //地块置空
                    $("#DKBH${idSuffix}").combobox("setValue","");
                    $("#DKMC${idSuffix}").textbox("setValue","");
                    //生产档案编号置空
                    $("#SCDABH${idSuffix}").combobox("setValue","");
                    //批次号置空
                    $("#PCH${idSuffix}").combobox("setValue","");
                    //采收重量、质量等级、库存重量置空
                    $("#ZL${idSuffix}").textbox("setValue","");
                    $("#ZLDJ${idSuffix}").combobox("setValue","");
                    $("#KCZL${idSuffix}").textbox("setValue","");
                }
            });
            //初始地块编号
            $('#DKBH${idSuffix}').combobox({
                valueField:'DKBH',
                textField:'DKMC',
                onChange: function(e,data){
                    $('#DKMC${idSuffix}').textbox("setValue",data.text);
                    var $scdabhData = $.loadJson($.contextPath + "/zzcc!getScdabhByDkbh.json?dkbh=" + data.value);
                    $("#SCDABH${idSuffix}").combobox("reload",$scdabhData);
                    //生产档案编号置空
                    $("#SCDABH${idSuffix}").combobox("setValue","");
                    //批次号置空
                    $("#PCH${idSuffix}").combobox("setValue","");
                    //采收重量、质量等级、库存重量置空
                    $("#ZL${idSuffix}").textbox("setValue","");
                    $("#ZLDJ${idSuffix}").combobox("setValue","");
                    $("#KCZL${idSuffix}").textbox("setValue","");
                }
            });
            //初始生产档案编号
            $('#SCDABH${idSuffix}').combobox({
                valueField:'SCDABH',
                textField:'SCDABH',
                onChange: function(e,data){
                    var $pchData = $.loadJson($.contextPath + "/zzcc!getCspchByScdabh.json?scdabh=" + data.value);
                    $("#PCH${idSuffix}").combobox("reload",$pchData);
                    //批次号置空
                    $("#PCH${idSuffix}").combobox("setValue","");
                    //采收重量、质量等级、库存重量置空
                    $("#ZL${idSuffix}").textbox("setValue","");
                    $("#ZLDJ${idSuffix}").combobox("setValue","");
                    $("#KCZL${idSuffix}").textbox("setValue","");
                }
            });
            //初始批次号
            $('#PCH${idSuffix}').combobox({
                valueField:'PCH',
                textField:'PCH',
                onChange: function(e,data){
                    var $pchData = $.loadJson($.contextPath + "/zzcc!getApcccByCspch.json?pch=" + data.value);
                    //采收重量、质量等级、库存重量置空
                    $("#ZL${idSuffix}").textbox("setValue",$pchData.ZL);
                    $("#ZLDJ${idSuffix}").combobox("setValue",$pchData.ZLDJ);
                    $("#KCZL${idSuffix}").textbox("setValue",$pchData.KCZL);
                    $("#PZ${idSuffix}").textbox("setValue",$pchData.PZ);
                    $("#PZBH${idSuffix}").textbox("setValue",$pchData.PZBH);
                    $("#PL${idSuffix}").textbox("setValue",$pchData.PL);
                    $("#PLBH${idSuffix}").textbox("setValue",$pchData.PLBH);
                }
            });
            //初始化质量等级
            $('#ZLDJ${idSuffix}').combobox({
                valueField:'VALUE',
                textField:'TEXT',
                readonly:"true",
                data:[{"VALUE":1,"TEXT":"优"},{"VALUE":2,"TEXT":"良"},{"VALUE":3,"TEXT":"差"},{"VALUE":4,"TEXT":"损耗"}]
            });
            /********************************表单初始化 end****************************/

            /********************************列表初始化 begin****************************/
            var $khData = $.loadJson($.contextPath + "/zzcc!getKhxx.json");
            var $cGrid = $("#gridId${idSuffix}");
            $cGrid.grid("setColProp", "KHBH", {
                "formatoptions": {
                    valueField: 'KHBH',
                    textField: 'KHMC',
                    required: true,
                    readonly: true,
                    "data": $khData,
                    enableFilter: true,
                    onChange: function (e, data) {
                        var $khmcTextbox = $("#" + e.data.gridId).grid("getCellComponent",  e.data.rowId, "KHMC");
                        $khmcTextbox.textbox("setValue",data.text);
                        var $ddbhCombobox = $("#" + e.data.gridId).grid("getCellComponent",  e.data.rowId, "DDBH");
                        var $gridData = $("#" + e.data.gridId).grid("getRowData");
                        var ddbhs = "";
                        $.each($gridData,function(i,d){
                            if(d.DDBH != "" && d.DDBH != "null"){
                                ddbhs += d.DDBH + ",";
                            }
                        });
                        var $ddbhData = $.loadJson($.contextPath + "/zzcc!getXsddbhByKhbh.json?khbh=" + data.value +"&ddbh=" + ddbhs);
                        $ddbhCombobox.combobox("reload",$ddbhData);
                    }
                }
            });
            var $cGrid = $("#gridId${idSuffix}");
            $cGrid.grid("setColProp", "DDBH", {
                "formatoptions": {
                    valueField: 'DDBH',
                    textField: 'DDBH',
                    required: true,
                    readonly: true,
                    enableFilter: true,
                }
            });
            var $psfsData = $.loadJson($.contextPath + "/zzcc!getPsfs.json");
            $cGrid.grid("setColProp", "PSFS", {
                "formatoptions": {
                    valueField: 'SJBM',
                    textField: 'SJMC',
                    required: true,
                    readonly: true,
                    "data": $psfsData,
                    enableFilter: true
                }
            });
            /********************************列表初始化 end****************************/

        }
    });
    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zzapccc.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                <!--return $('#layoutId${idSuffix}').layout('panel', 'center');-->
                return $('#max${idSuffix}');
            },
            /** 初始化预留区 */
            'initReserveZones': function (configInfo) {
                //CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength"));
            },
            /** 获取返回按钮添加的位置 */
            'setReturnButton': function (configInfo) {
                <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
                CFG_setCloseButton(configInfo, $('#toolbarId${idSuffix}'));
            },
            /** 获取关闭按钮添加的位置 */
            'setCloseButton': function (configInfo) {
                CFG_setCloseButton(configInfo, $('#toolbarId1${idSuffix}'));
            },

            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                $.ns("namespaceId${idSuffix}").init();
                var id = CFG_getInputParamValue(configInfo, 'rowDataId');
                var jsonData = $.loadJson($.contextPath + "/sczzpccc!queryDetails.json?id=" + id);
                var $form = $("#pcxxForm${idSuffix}");
                $form.form("loadData", jsonData.formData);
                $form.form("setReadOnly", true);
                var $grid = $("#gridId${idSuffix}");
                $grid.grid("option", "datatype", "local");
                $grid.grid("option", "data", jsonData.gridData);
                $grid.grid("reload");
//                $grid.grid("disable");

                $.each($grid.grid("getDataIDs"),function(e,data){
                    var $khmcTextbox = $grid.grid("getCellComponent",  data, "KHMC");
                    var $khbhCombobox = $grid.grid("getCellComponent",  data, "KHBH");
                    var $rowGridData = $grid.grid("getRowData",data);
                    var $rowGridDatas = [];
                    $rowGridDatas.push($rowGridData);
                    $khbhCombobox.combobox("reload",$rowGridDatas);
                    $khbhCombobox.combobox("setValue", $rowGridData.KHBH);
                    $khmcTextbox.textbox("setValue", $rowGridData.KHMC);
                    var $ddbhCombobox = $grid.grid("getCellComponent", data, "DDBH");
                    var $gridData = $grid.grid("getRowData");
                    var ddbhs = "";
                    $.each($gridData,function(i,d){
                        if(d.DDBH != "" && d.DDBH != "null"){
                            ddbhs += d.DDBH + ",";
                        }
                    });
                    var $ddbhData = $.loadJson($.contextPath + "/zzcc!getXsddbhByKhbh.json?khbh=" + $khbhCombobox.combobox("getValue")  +"&ddbh=" + ddbhs);
                    $.each(jsonData.gridData,function(e, data){
                        if(data.ID == $rowGridData.ID){
                            $ddbhData.push(data);
                            $rowGridData = data;
                        }
                    });
                    $ddbhCombobox.combobox("reload",$ddbhData);
                    $ddbhCombobox.combobox("setValue", $rowGridData.DDBH);
                });
            }
        });

    });
    /********************保存 begin********************************/
    $("#pcxxForm${idSuffix}").submit(function () {
        if (!$("#pcxxForm${idSuffix}").form("valid")) {
            CFG_message("操作失败！", "error");
            return false;
        }
        var $grid = $("#gridId${idSuffix}");
        if($grid.grid("getRowData").length == 0){
            CFG_message("请添加至少一条客户信息！", "error");
            return false;
        }
        if (!$grid.grid('valid')) {
            CFG_message("操作失败！", "error");
            return false;
        }
        var $gridData = $("#gridId${idSuffix}").grid("getRowData");
        var cczzl = 0;
        $.each($gridData,function(e,data){
            cczzl += parseFloat(data.CCZL);
        });
        var kczl = $("#KCZL${idSuffix}").textbox("getValue");
        if(parseFloat(cczzl) > parseFloat(kczl)){
            CFG_message("出场总重量大于库存重量！", "error");
            return false
        }
        var editrow = $grid.grid("option", "editrow");
        var formData = $("#pcxxForm${idSuffix}").form("formData", false);
        var gridData = $grid.grid("getRowData");
        var postData = {
            formData: $.config.toString(formData),
            gridData: $.config.toString(gridData)
        };
        $.ajax({
            type: 'POST',
            url: $.contextPath + "/zzcc!savePcccxx.json",
            data: postData,
            dataType: "json",
            success: function (data) {
                $("#id${idSuffix}").textbox("setValue", data);
                CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
                CFG_message("操作成功！", "success");
            },
            error: function () {
                CFG_message("操作失败！", "error");
            }
        });
        return false;
    });
    /********************保存 begin********************************/
    /************************读码dialog begin**************************************/
    function showDialog() {
        $("#jqUC").remove();
        var jqGlobal = $("body");
        var jqUC = $("<div id=\"jqUC\"></div>").appendTo("body");
        jqUC.dialog({
            appendTo: jqGlobal,
            modal: true,
            title: "请输入条码",
            width: 240,
            height: 80,
            resizable: false,
            position: {
                at: "center top+200"
            },
            onClose: function () {
                jqUC.remove();
            },
            onCreate: function () {
                var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\"></div>").appendTo(this);
                var jq = $("<input id=\"UNTREAD_OPINION_READCODE\" name=\"opinion\"/>").appendTo(jqDiv);
                jq.textbox({width: 200,
//                    maxlength: 22
                });
            },
            onOpen: function () {
                var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
                        jqDiv = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
                jqDiv.toolbar({
                    data: ["->", {id: "sure", label: "确定", type: "button"}, {
                        id: "cancel",
                        label: "取消",
                        type: "button"
                    }],
                    onClick: function (e, ui) {
                        if ("sure" === ui.id) {
                            barCode = $("#UNTREAD_OPINION_READCODE").val();
                            setGridData(barCode);
                        }
                        jqUC.dialog("close");
                        $("#UNTREAD_OPINION_READCODE").remove();
                    }
                });
            }
        });
    }
    /************************读码dialog begin**************************************/

    /************************读码 begin**************************************/
    function setGridData(barCode){
        var dataJson = $.loadJson($.contextPath + "/zzcc!getApcccByCspch.json?pch="+barCode);
        if(dataJson.RESULT == "ERROR"){
            CFG_message("请输入有效的批次号!", "warning");
            return;
        }else{
            $("#pcxxForm${idSuffix}").form("loadData",dataJson);
        }
    }
    /************************读码 end**************************************/



</script>