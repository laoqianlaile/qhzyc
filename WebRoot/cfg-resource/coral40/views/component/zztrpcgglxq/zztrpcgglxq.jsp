<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
    String id = request.getParameter("ID");
    request.setAttribute("id",id);
%>
<style type="text/css">
    .app-input-label {
        float: left;
        width: 100px;
    }

    .min-div {
        width: 20%;
        float: left;
    }

    .min-div2 {
        width: 30%;
        float: left;
    }
</style>
<div id="max${idSuffix}" class="fill">
    <div class="fill">
        <div class="toolbarsnav clearfix">
            <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                         data="['->',{'label': '关闭', 'id':'CFG_closeComponentZone','cls':'return_tb', 'disabled': 'false','type': 'button'}]">
            </ces:toolbar>
            <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> - 投入品采购管理 - 详情 </div></div></div>
            <%--[{'label': '保存', 'id':'save', 'disabled': 'false','type': 'button'}]--%>
        </div>
        <form id="trpcgForm${idSuffix}" action="${basePath}zzkhxxg!saveKhxx.json" method="post" class="coralui-form">

            <div class="app-inputdiv6" style="height:32px;display:none">
                <input id="ID${idSuffix}" class="coralui-textbox" name="ID"/>
                <input id="RKLSH${idSuffix}" name="RKLSH" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" style="height:32px;display: none">
                <input id="QYBM${idSuffix}" name="QYBM" class="coralui-textbox"/>
            </div>
            <!-- <div class="fillwidth colspan3 clearfix">-->
            <div class="app-inputdiv6">
                <label class="app-input-label">投入品类型：</label>
                <input id="TRPLX${idSuffix}" name="TRPLX" data-options="required:true"/>
            </div>
            <div class="app-inputdiv6" id="line2${idSuffix}">
                <label class="app-input-label">投入品名称：</label>
                <input id="TRPBH${idSuffix}" name="TRPBH" data-options="required:true"/>
            </div>
            <div class="app-inputdiv6" id="line3${idSuffix}" style="display:none">
                <label class="app-input-label">通用名：</label>
                <input id="TYM${idSuffix}" name="TYM" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" id="line28${idSuffix}" style="display:none">
                <label class="app-input-label">品牌：</label>
                <input id="PP${idSuffix}" name="PP" data-options="required:true" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" id="line29${idSuffix}" style="display:none">
                <label class="app-input-label">型号：</label>
                <input id="XH${idSuffix}" name="XH" data-options="required:true" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" id="line30${idSuffix}" style="display:none">
                <label class="app-input-label">马力：</label>
                <input id="ML${idSuffix}" name="ML" data-options="required:true" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" id="line4${idSuffix}">
                <label class="app-input-label">包装规格：</label>
                <input id="BZGG${idSuffix}" name="BZGG" class="coralui-textbox" data-options="required:true"/>
            </div>
            <div class="app-inputdiv6" id="line5${idSuffix}">
                <label class="app-input-label">用途：</label>
                <input id="YT${idSuffix}" name="YT" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" id="line6${idSuffix}">
                <label class="app-input-label">有效成分：</label>
                <input id="CF${idSuffix}" name="CF" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" id="line7${idSuffix}">
                <label class="app-input-label">推荐用量：</label>
                <span class="coral-textbox" aria-disabled="false">
                    <input id="TJYL${idSuffix}" name="TJYL" class="coralui-textbox"/>
                <span class="innerRight coral-textbox-btn-icons coral-buttonset coral-corner-right" style="right:0px;">
                 <button type="button" component-role="button" class="ctrl-init ctrl-init-button coral-button coral-component
                 coral-state-default coral-button-text-only coral-corner-right" role="button" aria-disabled="false">
                     <span class="coral-button-text" >千克/亩</span>
                 </button>
                </span>
                </span>
            </div>
            <div class="app-inputdiv6" id="line8${idSuffix}">
                <label class="app-input-label">肥料类型：</label>
                <input id="FLLX${idSuffix}" name="FLLX"/>
            </div>
            <div class="app-inputdiv6" id="line9${idSuffix}">
                <label class="app-input-label">供应商名称：</label>
                <input id="GYSBH${idSuffix}" name="GYSBH"/>
            </div>

            <div class="app-inputdiv6" id="line23${idSuffix}" style="display: none">
                <label class="app-input-label">育种基地：</label>
                <input id="YZJD${idSuffix}" name="YZJD" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" id="line24${idSuffix}" style="display:none">
                <label class="app-input-label">检疫证号：</label>
                <input id="JYZH${idSuffix}" name="JYZH" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" id="line25${idSuffix}" style="display: none">
                <label class="app-input-label">检疫证号有效期：</label>
                <input id="JYZHYXQ${idSuffix}" name="JYZHYXQ" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" id="line26${idSuffix}" style="display: none">
                <label class="app-input-label">引种编号：</label>
                <input id="YZBH${idSuffix}" name="YZBH" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" id="line27${idSuffix}" style="display: none">
                <label class="app-input-label">引种编号有效期：</label>
                <input id="YZBHYXQ${idSuffix}" name="YZBHYXQ" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" id="line10${idSuffix}">
                <label class="app-input-label">入库数量：</label>

                <div class="min-div">
                    <input id="RKSL${idSuffix}" name="RKSL" data-options="required:true" class="coralui-textbox"/>
                </div>
                <div class="min-div2"><input id="RKSLDW${idSuffix}" name="RKSLDW"/></div>
            </div>
            <div class="app-inputdiv6" id="line11${idSuffix}">
                <label class="app-input-label">入库日期：</label>
                <ces:datepicker id="RKRQ${idSuffix}" name="RKRQ"/>
            </div>
            <div class="app-inputdiv6" id="line12${idSuffix}">
                <label class="app-input-label">生产企业名称：</label>
                <input id="SCQYMC${idSuffix}" name="SCQYMC" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" id="line13${idSuffix}">
                <label class="app-input-label">生产企业证件号：</label>
                <input id="SCQYZJH${idSuffix}" name="SCQYZJH" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6" id="line14${idSuffix}">
                <label class="app-input-label">生产许可证号：</label>
                <input id="SCXKZH${idSuffix}" name="SCXKZH" class="coralui-textbox"></input>
            </div>
            <div class="app-inputdiv6" id="line15${idSuffix}">
                <label class="app-input-label">产品合格证号：</label>
                <input id="CPHGZH${idSuffix}" name="CPHGZH" class="coralui-textbox"></input>
            </div>
            <div class="app-inputdiv6" id="line16${idSuffix}">
                <label class="app-input-label">产品标准号：</label>
                <input id="CPBZH${idSuffix}" name="CPBZH" class="coralui-textbox"></input>
            </div>
            <div class="app-inputdiv6" id="line17${idSuffix}">
                <label class="app-input-label">生产批次号：</label>
                <input id="CPPCH${idSuffix}" name="CPPCH" class="coralui-textbox" data-options="required:true"></input>
            </div>
            <div class="app-inputdiv6" id="line18${idSuffix}">
                <label class="app-input-label">生产日期：</label>
                <ces:datepicker required="true" id="SCRQ${idSuffix}" name="SCRQ"/>
            </div>
            <div class="app-inputdiv6" id="line19${idSuffix}">
                <label class="app-input-label">到期日：</label>
                <ces:datepicker required="true" id="DQR${idSuffix}" name="DQR"/>
            </div>
            <div class="app-inputdiv6" id="line20${idSuffix}">
                <label class="app-input-label">农残期（天）：</label>
                <input id="NCQ${idSuffix}" name="NCQ" class="coralui-textbox" data-options="required:true"/>
            </div>

            <div class="app-inputdiv6" id="line21${idSuffix}">
                <label class="app-input-label">物料来源：</label>
                <input id="WLLY${idSuffix}" name="WLLY"/>
            </div>
            <div class="app-inputdiv8" id="line22${idSuffix}">
                <label class="app-input-label">备注：</label>
                <textarea id="SYSM${idSuffix}" name="SYSM" class="coralui-textbox"/>
            </div>


            <!--
        <div class="app-inputdiv4" style="display: none">
          <label class="app-input-label" >投入品编号：</label>
          <input id="TRPBH${idSuffix}" name="TRPBH" class="coralui-textbox"/>
        </div>
        -->

            <!--
        <div class="app-inputdiv4" style="display: none">
          <label class="app-input-label" >供应商编号：</label>
          <input id="GYSBH${idSuffix}" name="GYSBH" class="coralui-textbox"/>
        </div>
       !-->
            <!--
            <div class="app-inputdiv4" style ="height:32px;">
            </div>

            <div class="app-inputdiv4" style ="height:32px;">
            </div>
            -->
        </form>
    </div>
</div>
<script type="text/javascript">

    $.extend($.ns("namespaceId${idSuffix}"), {
        trpClick: function () {
            CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "trpmc", "投入品名称", 2, $.ns("namespaceId${idSuffix}"));
        },
        gysClick: function () {
            CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "gysmc", "供应商名称", 2, $.ns("namespaceId${idSuffix}"));
        },
        setComboGridValue_trpmc: function (o) {
            if (null == o) return;
            var obj = o.rowData;
            if (null == obj) return;
            $("#TRPMC${idSuffix}").combogrid("setValue", obj.TRPMC);
            $("#TRPBH${idSuffix}").textbox("setValue", obj.TRPBH);
            $("#NCQ${idSuffix}").textbox("setValue", obj.AQQ);
            $("#YT${idSuffix}").textbox("setValue", obj.YT);
        },
        setComboGridValue_gysmc: function (o) {
            if (null == o) return;
            var obj = o.rowData;
            if (null == obj) return;
            $("#GYSBH${idSuffix}").combogrid("setValue", obj.GYSBH);
            $("#GYSMC${idSuffix}").textbox("setValue", obj.GYSMC);
        },
        toolbarClick: function (event, button) {
            if (button.id == "save") {//保存的方法
                $("#trpcgForm${idSuffix}").form().submit();

            } else if (button.id == "CFG_closeComponentZone" || button.id == "CFG_closeComponentDialog") {//返回 或关闭
                CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
            }
        },
        /* 构件方法和回调函数 */
        getCategoryId: function () {
            alert("调用方法getCategoryId！");
            var o = {
                status: true
            };
            o.categoryId = '1';
            return o;
        },
        refreshGrid: function (cbParam1) {
            alert("调用回到函数refreshGrid(" + cbParam1 + ")");
        },
        showFl: function () {
            $("#line2${idSuffix}").css("display", "block");  //投入品名称
            $("#line3${idSuffix}").css("display", "block"); //通用名
            $("#line4${idSuffix}").css("display", "block"); //包装规格
            $("#line5${idSuffix}").css("display", "block"); //用途
            $("#line6${idSuffix}").css("display", "block"); //有效成分
            $("#line7${idSuffix}").css("display", "block"); //推荐用量
            $("#line8${idSuffix}").css("display", "block"); //肥料类型
            $("#line9${idSuffix}").css("display", "block"); //供应商名称
            $("#line10${idSuffix}").css("display", "block");//入库数量
            $("#line11${idSuffix}").css("display", "block"); //入库日期
            $("#line12${idSuffix}").css("display", "block"); //生产企业名称
            $("#line13${idSuffix}").css("display", "block"); //生产企业证件号
            $("#line14${idSuffix}").css("display", "block"); //生产许可证号
            $("#line15${idSuffix}").css("display", "block"); //产品合格证号
            $("#line16${idSuffix}").css("display", "block"); //产品标准号
            $("#line17${idSuffix}").css("display", "block"); //生产批次号
            $("#line18${idSuffix}").css("display", "block"); //生产日期
            $("#line19${idSuffix}").css("display", "block"); //到期日
            $("#line20${idSuffix}").css("display", "block"); //农残期
            $("#line21${idSuffix}").css("display", "block"); //物料来源
            $("#line22${idSuffix}").css("display", "block"); //备注

            $("#line23${idSuffix}").css("display", "none");    //育种基地
            $("#line24${idSuffix}").css("display", "none");   //检疫证号
            $("#line25${idSuffix}").css("display", "none");   //检疫证号有效期
            $("#line26${idSuffix}").css("display", "none");   //引种编号
            $("#line27${idSuffix}").css("display", "none");   //引种编号有效期
            $("#line28${idSuffix}").css("display", "none");   //品牌
            $("#line29${idSuffix}").css("display", "none");   //型号
            $("#line30${idSuffix}").css("display", "none");   //马力
        },
        showZz: function () {
            $("#line2${idSuffix}").css("display", "block");  //投入品名称
            $("#line3${idSuffix}").css("display", "block"); //通用名
            $("#line4${idSuffix}").css("display", "block"); //包装规格
            $("#line23${idSuffix}").css("display", "block"); //育种基地
            $("#line24${idSuffix}").css("display", "block");  //检疫证号
            $("#line25${idSuffix}").css("display", "block"); //检疫证号有效期
            $("#line26${idSuffix}").css("display", "block"); //引种编号
            $("#line27${idSuffix}").css("display", "block");//引种编号有效期
            $("#line10${idSuffix}").css("display", "block");//入库数量
            $("#line11${idSuffix}").css("display", "block"); //入库日期
            $("#line17${idSuffix}").css("display", "block"); //生产批次号
            $("#line18${idSuffix}").css("display", "block"); //生产日期
            $("#line21${idSuffix}").css("display", "block"); //物料来源
            $("#line22${idSuffix}").css("display", "block"); //备注

            $("#line5${idSuffix}").css("display", "none");
            $("#line6${idSuffix}").css("display", "none");
            $("#line7${idSuffix}").css("display", "none");
            $("#line8${idSuffix}").css("display", "none");
            $("#line9${idSuffix}").css("display", "none");
            $("#line12${idSuffix}").css("display", "none");
            $("#line13${idSuffix}").css("display", "none");
            $("#line14${idSuffix}").css("display", "none");
            $("#line15${idSuffix}").css("display", "none");
            $("#line16${idSuffix}").css("display", "none");
            $("#line19${idSuffix}").css("display", "none");
            $("#line20${idSuffix}").css("display", "none");
        },
        showNjj: function () {
            $("#line2${idSuffix}").css("display", "block");  //投入品名称
            $("#line3${idSuffix}").css("display", "block"); //通用名
            $("#line28${idSuffix}").css("display", "block");//品牌
            $("#line29${idSuffix}").css("display", "block");//型号
            $("#line30${idSuffix}").css("display", "block");//马力
            $("#line22${idSuffix}").css("display", "block"); //备注

            $("#line9${idSuffix}").css("display", "block"); //供应商名称
            $("#line11${idSuffix}").css("display", "block"); //入库日期
            $("#line10${idSuffix}").css("display", "block");//入库数量
            $("#line4${idSuffix}").css("display", "none");
            $("#line5${idSuffix}").css("display", "none");
            $("#line6${idSuffix}").css("display", "none");
            $("#line7${idSuffix}").css("display", "none");
            $("#line8${idSuffix}").css("display", "none");
            //$("#line9${idSuffix}").css("display", "none");
            $("#line12${idSuffix}").css("display", "none");
            $("#line13${idSuffix}").css("display", "none");
            $("#line14${idSuffix}").css("display", "none");
            $("#line15${idSuffix}").css("display", "none");
            $("#line16${idSuffix}").css("display", "none");
            $("#line17${idSuffix}").css("display", "none");
            $("#line18${idSuffix}").css("display", "none");  //生产日期
            $("#line19${idSuffix}").css("display", "none");
            $("#line20${idSuffix}").css("display", "none");
            $("#line21${idSuffix}").css("display", "none");
            $("#line23${idSuffix}").css("display", "none");
            $("#line24${idSuffix}").css("display", "none");
            $("#line25${idSuffix}").css("display", "none");
            $("#line26${idSuffix}").css("display", "none");
            $("#line27${idSuffix}").css("display", "none");
        },
        showNy: function () {
            $("#line2${idSuffix}").css("display", "block");  //投入品名称
            $("#line3${idSuffix}").css("display", "block"); //通用名
            $("#line4${idSuffix}").css("display", "block"); //包装规格
            $("#line5${idSuffix}").css("display", "block"); //用途
            $("#line6${idSuffix}").css("display", "block"); //有效成分
            $("#line7${idSuffix}").css("display", "block"); //推荐用量
            $("#line8${idSuffix}").css("display", "block"); //肥料类型
            $("#line9${idSuffix}").css("display", "block"); //供应商名称
            $("#line10${idSuffix}").css("display", "block");//入库数量
            $("#line11${idSuffix}").css("display", "block"); //入库日期
            $("#line12${idSuffix}").css("display", "block"); //生产企业名称
            $("#line13${idSuffix}").css("display", "block"); //生产企业证件号
            $("#line14${idSuffix}").css("display", "block"); //生产许可证号
            $("#line15${idSuffix}").css("display", "block"); //产品合格证号
            $("#line16${idSuffix}").css("display", "block"); //产品标准号
            $("#line17${idSuffix}").css("display", "block"); //生产批次号
            $("#line18${idSuffix}").css("display", "block"); //生产日期
            $("#line19${idSuffix}").css("display", "block"); //到期日
            $("#line20${idSuffix}").css("display", "block"); //农残期
            $("#line21${idSuffix}").css("display", "block"); //物料来源
            $("#line22${idSuffix}").css("display", "block"); //备注

            $("#line23${idSuffix}").css("display", "none");
            $("#line24${idSuffix}").css("display", "none");
            $("#line25${idSuffix}").css("display", "none");
            $("#line26${idSuffix}").css("display", "none");
            $("#line27${idSuffix}").css("display", "none");
            $("#line28${idSuffix}").css("display", "none");
            $("#line29${idSuffix}").css("display", "none");
            $("#line30${idSuffix}").css("display", "none");
        }
    });

    var buttonOptsTrp = {
        id: "combogrid_buttonId",
        label: "构件按钮",
        icons: "icon-checkmark-circle",
        text: false,
        onClick: $.ns('namespaceId${idSuffix}').trpClick
    };
    var _colNames = ["投入品编号", "投入品名称", "通用名", "包装规格", "安全期", "用途","有效成分","推荐用量"];
    var _colModel = [{name: "TRPBH", width: 65, sortable: true, align: "left"},
                    {name: "TRPMC", width: 65, sortable: true, align: "left"},
                    {name: "TYM", width: 65, sortable: true, align: "left"},
                    {name: "BZGG", width: 55, sortable: true, align: "right"},
                    {name: "AQQ", width: 55, sortable: true, align: "right"},
                    {name: "YT", width: 10, sortable: true, hidden: true},
                    {name: "YXCF", width: 55, sortable: true,hidden: true},
                    {name: "TJYL", width: 55, sortable: true,hidden: true}

    ];
    var buttonOptsGys = {
        id: "combogrid_buttonId2",
        label: "构件按钮",
        icons: "icon-checkmark-circle",
        text: false,
        onClick: $.ns('namespaceId${idSuffix}').gysClick
    };
    var _colNamesGys = ["供应商编号", "供应商名称", "供应商类型"];
    var _colModelGys = [{name: "GYSBH", width: 120, sortable: true, align: "left"}, {
        name: "GYSMC",
        width: 155,
        sortable: true,
        align: "left"
    }, {name: "GYSLB", width: 80, sortable: true, align: "left"}];
    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zztrpcgglxq.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                //return $('#layoutId${idSuffix}').layout('panel', 'center');
                return $('#max${idSuffix}');
            },
            /** 初始化预留区 */
            'initReserveZones': function (configInfo) {
                //CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength"));
            },
            /** 获取返回按钮添加的位置 */
            'setReturnButton': function (configInfo) {
                //CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
                CFG_setCloseButton(configInfo, $('#toolbarId${idSuffix}'));
            },
            /** 获取关闭按钮添加的位置 */
            'setCloseButton': function (configInfo) {
                CFG_setCloseButton(configInfo, $('#toolbarId${idSuffix}'));
            },
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {

                //页面初始化进行默认设置
                var inputParamId = "";
                if("${id}" != ""){
                    inputParamId = "${id}";
                    $("div.coral-toolbar").css("display", "none");
                }else{
                    inputParamId= CFG_getInputParamValue(configInfo, 'rowDataId');
                }
                var selfParam = "";
                //入库编号默认为只读文本框
                $("#RKLSH${idSuffix}").textbox("option", "readonly", true);
                //农残期默认为只读文本框
                //$("#NCQ${idSuffix}").textbox("option","readonly",true);
                //通用名默认为只读文本框
                $("#TYM${idSuffix}").textbox("option","readonly",true);
                //用途默认为只读文本框
                $("#YT${idSuffix}").textbox("option", "readonly", true);
                //包装规格默认为只读文本框
                $("#BZGG${idSuffix}").textbox("option", "readonly", true);
                //有效成分默认为只读文本框
                $("#CF${idSuffix}").textbox("option", "readonly", true);
                //推荐用量默认为只读文本框
                $("#TJYL${idSuffix}").textbox("option", "readonly", true);
                //初始化投入品类型下拉框
                $("#TRPLX${idSuffix}").combobox({
                    valueField: 'value',
                    textField: 'name',
                    sortable: true,
                    url: $.contextPath + "/zztrpcglb!searchTrplx.json"
                });
                $("#TRPLX${idSuffix}").combobox("option", "onChange", function (e, data) {
                    if (data.value == 'FL') {
                        $.ns("namespaceId${idSuffix}").showFl();
                        $("#TRPMC${idSuffix}").combogrid("setValue", "");
                        $("#TYM${idSuffix}").textbox("setValue","");
                        $("#BZGG${idSuffix}").textbox("setValue","");
                        $("#YT${idSuffix}").textbox("setValue","");
                        $("#CF${idSuffix}").textbox("setValue","");
                        $("#TJYL${idSuffix}").textbox("setValue","");
                        $("#NCQ${idSuffix}").textbox("setValue","");
                        $("#GYSMC${idSuffix}").combogrid("setValue","");
                    } else if (data.value == 'NY') {
                        $.ns("namespaceId${idSuffix}").showNy();
                        $("#TRPMC${idSuffix}").combogrid("setValue", "");
                        $("#TYM${idSuffix}").textbox("setValue","");
                        $("#BZGG${idSuffix}").textbox("setValue","");
                        $("#YT${idSuffix}").textbox("setValue","");
                        $("#CF${idSuffix}").textbox("setValue","");
                        $("#TJYL${idSuffix}").textbox("setValue","");
                        $("#NCQ${idSuffix}").textbox("setValue","");
                        $("#GYSMC${idSuffix}").combogrid("setValue","");
                    } else if (data.value == 'ZZ') {
                        $.ns("namespaceId${idSuffix}").showZz();
                        $("#TRPMC${idSuffix}").combogrid("setValue", "");
                        $("#TYM${idSuffix}").textbox("setValue","");
                        $("#BZGG${idSuffix}").textbox("setValue","");
                    } else if (data.value == 'NJJ') {
                        $.ns("namespaceId${idSuffix}").showNjj();
                        $("#TRPMC${idSuffix}").combogrid("setValue", "");
                        $("#TYM${idSuffix}").textbox("setValue","");
                    }
                    var trpJson = $.loadJson($.contextPath + "/tcszztrpxx!searchTrpxxData.json?trplx=" + data.value);
                    $("#TRPMC${idSuffix}").combogrid("reload", trpJson.data);
                });

                //初始化入库数量单位下拉框
                $("#RKSLDW${idSuffix}").combobox({
                    valueField: 'value',
                    textField: 'name',
                    url: $.contextPath + "/zztrpcglb!searchRksldw.json"
                });
                //初始化物料来源下拉框
                $("#WLLY${idSuffix}").combobox({
                    valueField: 'value',
                    textField: 'name',
                    sortable: true,
                    // value: ""
                    width: "auto",
                    url: $.contextPath + "/zztrpcglb!searchWlly.json"
                });
                //初始化肥料类型来源下拉框
                $("#FLLX${idSuffix}").combobox({
                    valueField: 'value',
                    textField: 'name',
                    sortable: true,
                    // value: ""
                    width: "auto",
                    url: $.contextPath + "/zztrpcglb!searchFllx.json"
                });
                //初始化投入品名称复合框
                $("#TRPBH${idSuffix}").combogrid({
                    url: $.contextPath + "/tcszztrpxx!searchTrpxxData.json",
                    multiple: false,
                    sortable: true,
                    colModel: _colModel,
                    colNames: _colNames,
                    width: "auto",
                    panelWidth: 455,
                    textField: "TRPMC",
                    valueField: "TRPBH",
                    height: "auto",
                    buttonOptions: buttonOptsTrp
                });
                //添加onchange事件
                var $trpmc = $("#TRPMC${idSuffix}");
                $trpmc.combogrid("option", "onChange", function (e, data) {
                    //获得当前的下拉列表中的是
                    var grid = $trpmc.combogrid("grid");
                    var selectedRowId = grid.grid("option", "selrow");
                    var rowData = grid.grid("getRowData", selectedRowId);
                    $("#TRPMC${idSuffix}").combogrid("setValue", data.text);
                    $("#TRPBH${idSuffix}").textbox("setValue", data.value);
                    $("#TYM${idSuffix}").textbox("setValue",rowData.TYM);
                    $("#NCQ${idSuffix}").textbox("setValue", rowData.AQQ);
                    $("#BZGG${idSuffix}").textbox("setValue", rowData.BZGG);
                    $("#YT${idSuffix}").textbox("setValue", rowData.YT);
                    $("#CF${idSuffix}").textbox("setValue",rowData.YXCF);
                    $("#TJYL${idSuffix}").textbox("setValue",rowData.TJYL);
                });
                //添加onchange事件
                var $rkrq=$("#RKRQ${idSuffix}");
                var $scrq=$("#SCRQ${idSuffix}");
                var $dqr=$("#DQR${idSuffix}");
                //比较生产日期、入库日期,生产日期、到期日
                $scrq.datepicker("option","onChange",function(e,data){
                    var rkrqValue=$rkrq.datepicker("getDate").getTime();
                    var scrqValue=$(e.target).datepicker("getDate").getTime();
                  //  var dqrValue=$dqr.datepicker("getDate").getTime();
                    if(scrqValue>rkrqValue){
                        $.alert("生产日期不可大于入库日期");
                        scrqValue=$scrq.datepicker("setValue","");
                        rkrqValue=$rkrq.datepicker("setValue","");
                   }
                    var dqrValue=$dqr.datepicker("getDate").getTime();
                    if(scrqValue>dqrValue){
                        $.alert("生产日期不可大于到期日");
                        scrqValue=$scrq.datepicker("setValue","");
                        dqrValue=$dqr.datepicker("setValue","");
                    }
                });
                //比较入库日期、生产日期
                $rkrq.datepicker("option","onChange",function(e,data){
                    var rkrqValue=$(e.target).datepicker("getDate").getTime();
                    var scrqValue=$scrq.datepicker("getDate").getTime();
                    if(rkrqValue<scrqValue){
                        $.alert("入库日期不可小于生产日期");
                        rkrqValue=$rkrq.datepicker("setValue","");
                        scrqValue=$scrq.datepicker("setValue","");
                    }
                });
                //比较到期日、生产日期
                $dqr.datepicker("option","onChange",function(e,data){
                    var scrqValue=$scrq.datepicker("getDate").getTime();
                    var dqrValue=$dqr.datepicker("getDate").getTime();
                    if(dqrValue<scrqValue){
                        $.alert("到期日不可小于生产日期");
                        dqrValue=$dqr.datepicker("setValue","");
                        scrqValue=$scrq.datepicker("setValue","");
                    }
                });

                //添加onchange事件

                <%--var $trplx=$("#TRPLX${idSuffix}");--%>
                <%--$trplx.combogrid("option","onChange",function(e,data){--%>

                <%--//获得当前下拉列表中的是--%>
                <%--var trplx=$trplx.combogrid("getValue");--%>

                <%--var grid = $trplx.combogrid("grid");--%>
                <%--var selectedRowId = grid.grid("option","selrow");--%>
                <%--var rowData = grid.grid("getRowData",selectedRowId);--%>
                <%--});--%>



                //初始化供应商复合框
                //初始化投入品名称复合框
                $("#GYSBH${idSuffix}").combogrid({
                    url: $.contextPath + "/tcszzgysxx!searchGysxxData.json",
                    multiple: false,
                    sortable: true,
                    colModel: _colModelGys,
                    colNames: _colNamesGys,
                    width: "auto",
                    panelWidth: 355,
                    textField: "GYSMC",
                    valueField: "GYSBH",
                    height: "auto",
                    buttonOptions: buttonOptsGys
                });
                var $gysmc = $("#GYSMC${idSuffix}");
                $("#GYSBH${idSuffix}").combogrid("option", "onChange", function (e, data) {
                    $("#GYSMC${idSuffix}").combogrid("setValue", data.text);
                    $("#GYSBH${idSuffix}").textbox("setValue", data.value);
                });

                if (isEmpty(inputParamId)) {
                    var qyObj = $.loadJson($.contextPath + "/trace!getQybmAndQymc.json");
                    $("#QYBM${idSuffix}").textbox("setValue", qyObj.qybm);

                } else {//修改操作
                    <%--if(data.value == "FL"){--%>
                    <%--$.ns("namespaceId${idSuffix}").showFl();--%>
                    <%--}else if(data.value=='NY'){--%>
                    <%--$.ns("namespaceId${idSuffix}").showNy();--%>
                    <%--}else if(data.value=='ZZ'){--%>
                    <%--$.ns("namespaceId${idSuffix}").showZz();--%>
                    <%--}else if(data.value=='NJJ'){--%>
                    <%--$.ns("namespaceId${idSuffix}").showNjj();--%>
                    <%--}--%>
                    //初始化投入品名称
                    var obj = $.loadJson($.contextPath + "/zztrpcglb!searchTrpcgxx.json?id=" + inputParamId);
                    if (obj.TRPLX == 'FL') {
                        $.ns("namespaceId${idSuffix}").showFl();
                    } else if (obj.TRPLX == 'NY') {
                        $.ns("namespaceId${idSuffix}").showNy();
                    } else if (obj.TRPLX == 'ZZ') {
                        $.ns("namespaceId${idSuffix}").showZz();
                    } else if (obj.TRPLX == 'NJJ') {
                        $.ns("namespaceId${idSuffix}").showNjj();
                    }
                    $("#trpcgForm${idSuffix}").form("loadData", obj);
                    if (selfParam == "detail") {
                        $("#trpcgForm${idSuffix}").form("disable");
                        $("#toolbarId${idSuffix}").toolbar("remove", "save");
                    }
                }
                //将表单设为只读
                $("#trpcgForm${idSuffix}").form("setReadOnly",true);

            }
        });
        if (configInfo) {
            //alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue(configInfo, 'sysParam1')
            //		+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue(configInfo, 'selfParam1')
            //		+ "\n构件入参：\t" + "inputParamName_1=" + CFG_getInputParamValue(configInfo, 'inputParamId'));
            //alert(CFG_getInputParamValue(configInfo, 'inputParamId'));
            //alert(CFG_getInputParamValue(configInfo, 'inputParamId1'));

        }
        configInfo.CFG_outputParams = {'success': 'otp'};
    });

    $("#trpcgForm${idSuffix}").submit(function () {
        if (!$("#trpcgForm${idSuffix}").form("valid")) {
            CFG_message("页面校验不通过", "error");
            return false;
        }
        var formdata = $("#trpcgForm${idSuffix}").form("formData", false); //new FormData(this);//$("#trpcgForm${idSuffix}").serialize();
        $.ajax({
            type: 'POST',
            url: $.contextPath + "/zztrpcglb!save.json",
            data: {E_entityJson: $.config.toString(formdata)},
            timestamp: false,
            dataType: "json",
            success: function (data) {
                $("#ID${idSuffix}").textbox("setValue", data.ID);
                CFG_message("操作成功！", "success");
            },
            error: function () {
                CFG_message("操作失败！", "error");
            }
        })

        return false;
    })

</script>
