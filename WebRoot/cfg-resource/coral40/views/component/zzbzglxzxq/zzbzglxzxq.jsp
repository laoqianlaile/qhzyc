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
</style>
<div id="max${idSuffix}" class="fill">
    <div class="fill">
        <div class="toolbarsnav clearfix">
            <div id="toolbarId${idSuffix}"></div>
            <%--<ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"--%>
                         <%--data="['->',{'label': '承重详情', 'id':'js','cls':'save_tb', 'disabled': 'false','type': 'button'},{'label': '关闭', 'id':'CFG_closeComponentZone','cls':'return_tb', 'disabled': 'false','type': 'button'}]">--%>
            <%--</ces:toolbar>--%>
            <%--[{'label': '保存', 'id':'save', 'disabled': 'false','type': 'button'}]--%>
            <div class='homeSpan' style="margin-top: -23px;">
                <div>
                    <div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> - 包装管理 - 详情</div>
                </div>
            </div>
        </div>
        <form id="bzForm${idSuffix}" action="${basePath}"
              enctype="multipart/form-data" method="post" class="coralui-form">
            <div class="app-inputdiv4" style="height:32px;display: none">
                <input id="id${idSuffix}" class="coralui-textbox" data-options="readonly:true" name="ID"/>
            </div>
            <div class="app-inputdiv4" style="height:32px;display: none">
                <input id="qybm${idSuffix}" class="coralui-textbox" data-options="readonly:true" name="QYBM"/>
            </div>
            <div class="app-inputdiv4" style="height:32px;display: none">
                <input id="qymc${idSuffix}" class="coralui-textbox" data-options="readonly:true" name="QYMC"/>
            </div>


            <div class="fillwidth colspan3 clearfix">
                <!------------------ 第一排开始---------------->
                <div class="app-inputdiv4" style="height:32px;display: none">
                    <label class="app-input-label">包装流水号：</label>
                    <input id="bzlsh${idSuffix}" name="BZLSH" data-options="readonly:true" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4" style="height:32px;display: none">
                    <label class="app-input-label">产品追溯码：</label>
                    <input id="cpzsm${idSuffix}" name="CPZSM" data-options="readonly:true" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">产品名称：</label>
                    <input id="cpmc${idSuffix}" name="CPMC" data-options="required: true"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">产品编号：</label>
                    <input id="cpbh${idSuffix}" data-options="readonly:true" name="CPBH" class="coralui-textbox"/>
                </div>
                <!------------------ 第一排结束---------------->

                <!------------------ 第二排开始---------------->
                <div class="app-inputdiv4">
                    <label class="app-input-label">包装形式：</label>
                    <input id="cpxs${idSuffix}" data-options="readonly:true" name="BZXS" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">产品规格：</label>
                    <input id="cpgg${idSuffix}" data-options="readonly:true,texticons:'公斤'" name="BZGG"
                           class="coralui-textbox"/>
                </div>
                <%--<div class="app-inputdiv4" style="height:32px;display: none">--%>
                <%--<label class="app-input-label">产品配料：</label>--%>
                <%--<input id="cppl${idSuffix}" name="CPPL" data-options="readonly:true" class="coralui-textbox"/>--%>
                <%--</div>--%>
                <!------------------ 第二排结束---------------->

                <!------------------ 第三排开始---------------->
                <div class="app-inputdiv4">
                    <label class="app-input-label">产品等级：</label>
                    <input id="cpdj${idSuffix}" name="CPDJ" data-options="readonly:true" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">产品数量：</label>
                    <input id="cpsl${idSuffix}" name="CPSL" class="coralui-textbox" data-options="required:true"/>
                </div>
                <div class="app-inputdiv4" style="display:none">
                    <label class="app-input-label">库存件数：</label>
                    <input id="kcjs${idSuffix}" name="KCJS" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">包装时间：</label>
                    <input id="bzsj${idSuffix}" name="BZSJ" class="coralui-datepicker"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">客户名称：</label>
                    <input id="khbh${idSuffix}" name="KHBH"/>
                </div>
                <div class="app-inputdiv4" style="display:none;">
                    <label class="app-input-label">门店名称：</label>
                    <input id="mdbh${idSuffix}" name="MDBH"/>
                </div>
                <!------------------ 第三排结束---------------->
            </div>
        </form>
        <div class="toolbarsnav clearfix">
            <ces:toolbar id="toolbarId1${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick1"
                         data="[]">
            </ces:toolbar>
        </div>
        <ces:grid id="gridId${idSuffix}" shrinkToFit="true" forceFit="true" fitStyle="fill" datatype="local"
                  clicksToEdit="1">
            <ces:gridCols>
                <ces:gridCol name="ID" editable="false" hidden="true" width="100">ID</ces:gridCol>
                <ces:gridCol name="PID" editable="false" hidden="true" width="80">PID</ces:gridCol>
                <!-- <ces:gridCol name="CSPCH" editable="false" hidden="true" width="100">CSPCH</ces:gridCol>-->
                <ces:gridCol name="BZPCZSM" hidden="true"
                             width="100">包装批次追溯码</ces:gridCol>
                <ces:gridCol name="CSLSH" editable="false" formatoptions="required: true"
                             width="100">采收流水号</ces:gridCol>
                <ces:gridCol name="CSPCH" editable="false" formatoptions="required: true"
                             width="100">采收批次号</ces:gridCol>

                <ces:gridCol name="QYMC" editable="false" width="100">区域名称</ces:gridCol>
                <ces:gridCol name="DKBH" editable="false" hidden="true" width="100">地块编号</ces:gridCol>
                <ces:gridCol name="DKMC" editable="false" width="100">地块名称</ces:gridCol>
                <ces:gridCol name="PZ" editable="false" width="100">品种</ces:gridCol>
                <ces:gridCol name="PZBH" editable="true" hidden="true" width="100">品种编号</ces:gridCol>
                <%--<ces:gridCol name="CSZZL" editable="true" formatter="text" formatoption="validType:'float'"--%>
                <%--width="100">采收总重量</ces:gridCol>--%>
                <ces:gridCol name="KCZL" editable="true" formatter="text"
                             formatoptions="validType:'float',readonly:true"
                             width="100">库存重量</ces:gridCol>
                <ces:gridCol name="JGQZL" editable="true" formatter="text"
                             formatoptions="validType:'float',readonly:true"
                             width="100">加工前重量</ces:gridCol>
                <ces:gridCol name="JGHZL" editable="true" formatter="text"
                             formatoptions="validType:'float',readonly:true"
                             width="100">加工后重量</ces:gridCol>
                <%--<ces:gridCol name="DJZL" editable="true" formatter="text" formatoption="validType:'float'"--%>
                <%--width="100">单件重量</ces:gridCol>--%>
                <ces:gridCol name="SHZL" editable="true" formatter="text"
                             formatoptions="validType:'float',readonly:true"
                             width="100">损耗重量</ces:gridCol>
                <%--<ces:gridCol name="BZJS" editable="true" width="100" formatter="sel">包装件数</ces:gridCol>--%>
            </ces:gridCols>
        </ces:grid>
    </div>
</div>
<script>
    //在预留区配置超链接，再加onclick事件，cpmcClick(\''+rawObject.BZPCZSM+'\')里面是获取选中行中的BZPCZSM传值
    <%--function sel(cellvalue, options, rawObject) {--%>
    <%--debugger--%>
    <%--return '<a href="#" onclick="$.ns(\'namespaceId${idSuffix}\').cpmcClick(\'' + rawObject.BZPCZSM + '\');return false;">件数</a>';--%>
    <%--}--%>
</script>
<script type="text/javascript">

    var barCode = "";
    $.extend($.ns("namespaceId${idSuffix}"), {
        rawObject: "",
        //点击调用预留区物理构件
        cpmcClick: function (rawObjects) {
            $.ns("namespaceId${idSuffix}").rawObject = rawObjects;
            CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "bzjsxq", "包装件数详情", 1, $.ns("namespaceId${idSuffix}"));
        },
        //根据选中行的追溯码，对包装件数做过滤
        getBzjs: function (o) {
            o = {
                status: true,
                P_columns: "EQ_C_ZSM≡" + $.ns("namespaceId${idSuffix}").rawObject
            }
            return o;
        },

        setComboGridValue_cpmc: function (o) {
            if (null == o) return;
            var obj = o.rowData;
            if (null == obj) return;
            $("#cpmc${idSuffix}").combogrid("setValue", obj.CPMC);
            $("#cpbh${idSuffix}").textbox("setValue", obj.CPBH);
            var cpxxJson = $.loadJson($.contextPath + "/zzcpxxgl!getCpxxByCpbh.json?cpbh=" + obj.CPBH);
            $("#cpxs${idSuffix}").textbox("setValue", cpxxJson.BZXS);
            $("#cpgg${idSuffix}").textbox("setValue", cpxxJson.GG);
            $("#cpdj${idSuffix}").textbox("setValue", cpxxJson.DJ);
            var $grid = $("#gridId${idSuffix}");
            $grid.grid("option", "datatype", "json");
            $grid.grid("option", "url", $.contextPath + "/zzcpxxgl!getPlxxByCpbh.json?cpbh=" + obj.CPBH);
            $grid.grid("reload");
        },
        toolbarClick: function (event, button) {
            if (button.id == "save") {//保存的方法
                $("#bzForm${idSuffix}").form().submit();

            } else if (button.id == "CFG_closeComponentZone") {
                CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
            } else if (button.id == "js") {
                $.ns("namespaceId${idSuffix}").cpmcClick($("#cpzsm${idSuffix}").textbox("getValue"));
            }
        },
        toolbarClick1: function (event, button) {
            if (button.id == "readCode") {
                showDialog();
            }
        }
    });
    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zzbzglxzxq.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            /** 获取构件嵌入的区域 */

            /** 初始化预留区 */
            'initReserveZones': function (configInfo) {
            },
            'bodyOnLoad': function (configInfo) {
                /****************产品名称复合框 begin**************************/
                var kh_data = $.loadJson($.contextPath + "/zzcpxxgl!getKhxx.json");
                $('#khbh${idSuffix}').combobox({
                    textField: 'KHMC',
                    valueField: 'KHBH'
                });
                $('#khbh${idSuffix}').combobox('reload', kh_data);
                $('#mdbh${idSuffix}').combobox({
                    textField: 'MDMC',
                    valueField: 'MDDZ',
                    readonly: true
                });
                $('#khbh${idSuffix}').combobox('option', 'onChange', function () {
                    var mdbh = $('#khbh${idSuffix}').combobox('getValue');
                    var md_data = $.loadJson($.contextPath + "/zzcpxxgl!getMdxx.json?khbh=" + mdbh);
                    $('#mdbh${idSuffix}').combobox({
                        readonly: false
                    });
                    $('#mdbh${idSuffix}').combobox('setValue', '')
                    $('#mdbh${idSuffix}').combobox('reload', md_data);

                })
                var _colName = ["产品名称", "产品编号"];
                var _colModel = [
                    {name: "CPBH", sortable: true, width: 100},
                    {name: "CPMC", sortable: true, width: 180, fixed: true}
                ];
                var combo_data = $.loadJson($.contextPath + "/zzcpxxgl!getCpxx.json");
                $("#cpmc${idSuffix}").combogrid({
                    textField: "CPMC",
                    valueField: "CPBH",
                    data: combo_data,
                    multiple: false,
                    sortable: true,
                    editable: true,
                    recordtext: "{0} - {1}",
                    colModel: _colModel,
                    colNames: _colName,
                    width: "auto",
                    height: "atuo",
                    buttonOptions: {
                        id: "combogrid_buttonId",
                        label: "构件按钮",
                        icons: "icon-checkmark-circle",
                        text: false,
                        onClick: $.ns('namespaceId${idSuffix}').cpmcClick
                    }
                });
                $("#cpmc${idSuffix}").combogrid("option", "onChange", function (e, data) {
                    $("#cpmc${idSuffix}").combogrid("setValue", data.text);
                    $("#cpbh${idSuffix}").textbox("setValue", data.value);
                    var cpxxJson = $.loadJson($.contextPath + "/zzcpxxgl!getCpxxByCpbh.json?cpbh=" + data.value);
                    $("#cpxs${idSuffix}").textbox("setValue", cpxxJson.BZXS);
                    $("#cpgg${idSuffix}").textbox("setValue", cpxxJson.GG);
                    $("#cpdj${idSuffix}").textbox("setValue", cpxxJson.DJ);
                    var $grid = $("#gridId${idSuffix}");
                    $grid.grid("option", "datatype", "json");
                    $grid.grid("option", "url", $.contextPath + "/zzcpxxgl!getPlxxByCpbh.json?cpbh=" + data.value);
                    $grid.grid("reload");
                });
                $("#cpsl${idSuffix}").textbox("option", "onChange", function (e, data) {
                    $("#cpsl${idSuffix}").textbox("setValue", data.value);
                    var cpslValue = data.value;
                    $("#kcjs${idSuffix}").textbox("setValue", cpslValue);
                });
                /****************产品名称复合框 end**************************/
                /****************初始化数据 begin**************************/
                var qyData = $.loadJson($.contextPath + "/trace!getQybmAndQymc.json?sysName=ZZ");
                $("#qybm${idSuffix}").textbox("setValue", qyData.qybm);
                $("#qymc${idSuffix}").textbox("setValue", qyData.qymc);
                var id = CFG_getInputParamValue(configInfo, 'rowDataId');
                //初始化完成后，将其设为只读。
                $("#bzForm${idSuffix}").form("setReadOnly", true);
                $("#bzForm${idSuffix}")
                if (isEmpty(id)) {
                    $("#bzsj${idSuffix}").datepicker("setDate", new Date());
                } else {
                    var jsonData = $.loadJson($.contextPath + "/zzbzgl!getBzxx.json?id=" + id);
                    var $grid = $("#gridId${idSuffix}");
                    $grid.grid("option", "datatype", "json");
                    $grid.grid("option", "url", $.contextPath + "/zzbzgl!getBzxxPlxx.json?id=" + id);
                    $grid.grid("reload");
                    $("#id${idSuffix}").textbox("setValue",
                            (jsonData.ID == "null" || jsonData.ID == null) ? "" : jsonData.ID);
                    $("#bzlsh${idSuffix}").textbox("setValue",
                            (jsonData.BZLSH == "null" || jsonData.BZLSH == null) ? "" : jsonData.BZLSH);
                    $("#cpmc${idSuffix}").combogrid("setValue",
                            (jsonData.CPMC == "null" || jsonData.CPMC == null) ? "" : jsonData.CPMC);
                    $("#cpbh${idSuffix}").textbox("setValue",
                            (jsonData.CPBH == "null" || jsonData.CPBH == null) ? "" : jsonData.CPBH);
                    $("#cpxs${idSuffix}").textbox("setValue",
                            (jsonData.BZXS == "null" || jsonData.BZXS == null) ? "" : jsonData.BZXS);
                    $("#cpzsm${idSuffix}").textbox("setValue",
                            (jsonData.CPZSM == "null" || jsonData.CPZSM == null) ? "" : jsonData.CPZSM);
                    $("#cpgg${idSuffix}").textbox("setValue",
                            (jsonData.BZGG == "null" || jsonData.BZGG == null) ? "" : jsonData.BZGG);
                    $("#cpdj${idSuffix}").textbox("setValue",
                            (jsonData.CPDJ == "null" || jsonData.CPDJ == null) ? "" : jsonData.CPDJ);
                    $("#cpsl${idSuffix}").textbox("setValue",
                            (jsonData.CPSL == "null" || jsonData.CPSL == null) ? "" : jsonData.CPSL);
                    $("#kcjs${idSuffix}").textbox("setValue",
                            (jsonData.KCJS == "null" || jsonData.KCJS == null) ? "" : jsonData.KCJS);
                    <%--var cpslValue=$("#cpsl${idSuffix}").textbox("getValue");--%>
                    <%--var kcjsValue=cpslValue;--%>
                    $("#bzsj${idSuffix}").datepicker("setDate",
                            (jsonData.BZSJ == "null" || jsonData.BZSJ == null) ? "" : jsonData.BZSJ);
                    $('#khbh${idSuffix}').combobox("setValue",
                            (jsonData.KHBH == "null" || jsonData.KHBH == null) ? "" : jsonData.KHBH);
                    $('#mdbh${idSuffix}').combobox("setValue",
                            (jsonData.MDBH == "null" || jsonData.MDBH == null) ? "" : jsonData.MDBH);
                    if ($("#cpxs${idSuffix}").textbox("getValue") == "散装预包装") {
                        $("#toolbarId${idSuffix}").toolbar({
                            'onClick': "$.ns('namespaceId${idSuffix}').toolbarClick",
                            'data': ['->', {
                                'label': '称重详情',
                                'id': 'js',
                                'cls': 'save_tb',
                                'disabled': 'false',
                                'type': 'button'
                            }, {
                                'label': '关闭',
                                'id': 'CFG_closeComponentZone',
                                'cls': 'return_tb',
                                'disabled': 'false',
                                'type': 'button'
                            }]
                        });
                    } else {
                        $("#toolbarId${idSuffix}").toolbar({
                            'onClick': "$.ns('namespaceId${idSuffix}').toolbarClick",
                            'data': ['->', {
                                'label': '关闭',
                                'id': 'CFG_closeComponentZone',
                                'cls': 'return_tb',
                                'disabled': 'false',
                                'type': 'button'
                            }]
                        });
                    }

                }
                /****************初始化数据 end**************************/
            }
        });

    });
    /********************保存 begin********************************/
    $("#bzForm${idSuffix}").submit(function () {
        if (!$("#bzForm${idSuffix}").form("valid")) {
            CFG_message("操作失败！", "error");
            return false;
        }
        var $grid = $("#gridId${idSuffix}");
        if (!$grid.grid('valid')) {
            CFG_message("操作失败！", "error");
            return false;
        }
        var editrow = $grid.grid("option", "editrow");
        var formData = $("#bzForm${idSuffix}").form("formData", false);
        var gridData = $grid.grid("getRowData");
        var postData = {
            formData: $.config.toString(formData),
            gridData: $.config.toString(gridData)
        };
        $.ajax({
            type: 'POST',
            url: $.contextPath + "/zzbzgl!saveBzxx.json",
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
                jq.textbox({width: 200});
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
    function setGridData(barCode) {
        var dataJson = $.loadJson($.contextPath + "/zzbzgl!getCsgl.json?barCode=" + barCode);
        if (dataJson == false || dataJson == "false") {
            CFG_message("请输入有效的条形码!", "warning");
            return;
        } else {
            var $grid = $("#gridId${idSuffix}");
            var gridDataIDs = $grid.grid("getDataIDs");
            var bool = 0;
            for (var i in gridDataIDs) {
                var gridData = $grid.grid("getRowData", gridDataIDs[i]);
                if (gridData.PZBH == dataJson.PZBH) {
                    $grid.grid("setCell", gridDataIDs[i], 'CSLSH', dataJson.CSLSH);
                    $grid.grid("setCell", gridDataIDs[i], 'QYMC', dataJson.QVMC);
                    $grid.grid("setCell", gridDataIDs[i], 'DKBH', dataJson.DKBH);
                    $grid.grid("setCell", gridDataIDs[i], 'DKMC', dataJson.DKMC);
                    $grid.grid("setCell", gridDataIDs[i], 'CSPCH', dataJson.PCH);
                    bool = 1;
                }
            }
            if (bool == 0) {
                CFG_message("没有采收批次对应的成品", "warning");
                return;
            }


        }
    }
    /************************读码 end**************************************/


</script>