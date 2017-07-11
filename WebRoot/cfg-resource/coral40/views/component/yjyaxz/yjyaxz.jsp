<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String resourceFolder = path + "/cfg-resource/coral40/common";
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
    request.setAttribute("menuId", request.getParameter("menuId"));
    request.setAttribute("basePath", basePath);
    request.setAttribute("path", path);
%>

<div id="maxDiv${idSuffix}" class="fill">
    <ces:layout id="layoutId${idSuffix}" name="" fit="true">
        <ces:layoutRegion region="north" style="height:200px;">
            <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                         data="[{'label': '保存', 'id':'saveBtn', 'disabled': 'false','type': 'button'},{'label': '返回', 'id':'return', 'disabled': 'false','type': 'button'}]">
            </ces:toolbar>
            <div id="formDiv${idSuffix}" style="margin-left: 20px;overflow: hidden;height: 170; ">
                <form id="form${idSuffix}" enctype="multipart/form-data" method="post" class="coralui-form">
                    <div class="fillwidth colspan3 clearfix">
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">预案名称:</label>
                            <input id="yamc${idSuffix}" class="coralui-textbox" name="yamc"
                                   data-options="required:true">
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">预案类型:</label>
                            <input id="yalx${idSuffix}" class="coralui-combobox" name="yalx"
                                   data-options="required:true">
                        </div>
                        <div class = "app-inputdiv4">
                            <span class = "appform-placeholder"></span>
                        </div>
                        <div class="app-inputdiv8">
                            <label class="app-input-label" style="width:18%;">预案描述:</label>
                            <textarea id="yams${idSuffix}" class="coralui-textbox" name="yams"/>
                        </div>
                        <input type="hidden" id="yabh${idSuffix}" class="coralui-textbox" name = "yabh">
                    </div>
                </form>
            </div>
        </ces:layoutRegion>
        <ces:layoutRegion region="west" split="true" style="width:400px;">
            <div id="leftGridDiv${idSuffix}" style="height: 100%;">
                <ces:grid id="leftGrid${idSuffix}" fitStyle="fill" multiselect="true">
                    <ces:gridCols>
                        <ces:gridCol name="xsmc" width="80">显示名称</ces:gridCol>
                        <ces:gridCol name="zdmc" hidden="true" width="80">字段名称</ces:gridCol>
                        <ces:gridCol name="ID" hidden="true" width="80">ID</ces:gridCol>
                    </ces:gridCols>
                </ces:grid>
            </div>
        </ces:layoutRegion>
        <ces:layoutRegion region="center" split="true" style="width:300px;">
            <div style="align:center;margin-top:100px;">
                <p align="center" style="margin:10px">
                    <button id="allToRight${idSuffix}" style="width:80px"
                            onclick="$.ns('namespaceId${idSuffix}').move('allToRight')"> >>
                    </button>
                </p>
                <p align="center" style="margin:10px">
                    <button id="toRight${idSuffix}" style="width:80px"
                            onclick="$.ns('namespaceId${idSuffix}').move('toRight')"> >
                    </button>
                </p>
                <p align="center" style="margin:10px">
                    <button id="toLeft${idSuffix}" style="width:80px"
                            onclick="$.ns('namespaceId${idSuffix}').move('toLeft')"> <
                    </button>
                </p>
                <p align="center" style="margin:10px">
                    <button id="allToLeft${idSuffix}" style="width:80px"
                            onclick="$.ns('namespaceId${idSuffix}').move('allToLeft')"> <<
                    </button>
                </p>
            </div>
        </ces:layoutRegion>
        <ces:layoutRegion region="east" split="true" style="width:400px;">
            <div id="rightGridDiv${idSuffix}" style="height: 100%;" rowattr="rowattr">
                <ces:grid id="rightGrid${idSuffix}" fitStyle="fill" multiselect="true" beforeSelectRow = "$.ns('namespaceId${idSuffix}').rightGridBeforeSelectRow">
                    <ces:gridCols>
                        <ces:gridCol name="xsmc" width="80">显示名称</ces:gridCol>
                        <ces:gridCol name="zdmc" hidden="true" width="80">字段名称</ces:gridCol>
                        <ces:gridCol name="ID" hidden="true" width="80">ID</ces:gridCol>
                    </ces:gridCols>
                </ces:grid>
            </div>
        </ces:layoutRegion>
    </ces:layout>
</div>
<script type="text/javascript">
    var _yalx = "";
    $.extend($.ns("namespaceId${idSuffix}"), {
        rightGridBeforeSelectRow : function (e,data) {
            var $rightGrid = $("#rightGrid${idSuffix}");
            var rowData = $rightGrid.grid("getRowData",data.rowId);
            if (rowData.zdmc == "PCH"||rowData.zdmc == "CPZSM"||rowData.zdmc == "CSLSH"||rowData.zdmc == "CPMC") {
                return false;
            }
            return true;
        },
        move: function (op) {
            var $leftGrid = $("#leftGrid${idSuffix}");
            var $rightGrid = $("#rightGrid${idSuffix}");
            if (op == "allToRight") {
                var leftGridData = $leftGrid.grid("getRowData");
                $leftGrid.grid("clearGridData");
                for (var index in leftGridData) {
                    $rightGrid.grid("addRowData", leftGridData[index].ID, leftGridData[index]);
                }
            }
            if (op == "allToLeft") {
                var rightGridData = $rightGrid.grid("getRowData");
                for (var index in rightGridData) {
                    if (rightGridData[index].zdmc != 'CPZSM' && rightGridData[index].zdmc != 'PCH'&& rightGridData[index].zdmc != 'CSLSH' &&rightGridData[index].zdmc != 'CPMC') {
                        $leftGrid.grid("addRowData", rightGridData[index].ID, rightGridData[index]);
                        $rightGrid.grid("delRowData",rightGridData[index].ID);
                    }
                }
            }
            if (op == "toRight") {
                var leftSelArrRow = $leftGrid.grid("option", "selarrrow").concat();
                if (leftSelArrRow.length == 0) {
                    CFG_message("请选择记录", "warning");
                    return;
                }
                var leftSelRowData = [];
                for (var index in leftSelArrRow) {
                    leftSelRowData.push($leftGrid.grid("getRowData", leftSelArrRow[index]));
                    $leftGrid.grid("delRowData", leftSelArrRow[index]);
                }
                for (var index in leftSelRowData) {
                    $rightGrid.grid("addRowData", leftSelRowData[index].ID, leftSelRowData[index]);
                }
            }
            if (op == "toLeft") {
                var rightSelArrRow = $rightGrid.grid("option", "selarrrow").concat();
                if (rightSelArrRow.length == 0) {
                    CFG_message("请选择记录", "warning");
                    return;
                }
                var rightSelRowData = [];
                for (var index in rightSelArrRow) {
                    rightSelRowData.push($rightGrid.grid("getRowData", rightSelArrRow[index]));
                    $rightGrid.grid("delRowData", rightSelArrRow[index]);
                }
                for (var index in rightSelRowData) {
                    $leftGrid.grid("addRowData", rightSelRowData[index].ID, rightSelRowData[index]);
                }
            }
        },
        toolbarClick: function (e, data) {
            if (data.id == "saveBtn") {
                var $form = $("#form${idSuffix}");
                if (!$form.form("valid")) {
                    return;
                }
                var $rightGrid = $("#rightGrid${idSuffix}");
                var rightGridData = $rightGrid.grid("getRowData");
                if (rightGridData.length == 0) {
                    CFG_message("请配置字段", "warning");
                    return;
                }
                var allData = {};
                allData.griddata = rightGridData;
                allData.formdata = $form.form("getData");
                $.ajax({
                    url: $.contextPath + "/yjyaxz!saveYjya.json",
                    type: "POST",
                    dataType: "json",
                    async: false,
                    data: {
                        yjya: JSON.stringify(allData)
                    },
                    success: function () {
                        CFG_message("保存成功","success");
                        var configInfo = $("#maxDiv${idSuffix}").data('configInfo');
                        CFG_clickCloseButton(configInfo);
                    },
                    error: function () {
                        CFG_message("操作失败","error");
                    }
                });
            } else if (data.id == "return") {
                var configInfo = $("#maxDiv${idSuffix}").data('configInfo');
                CFG_clickCloseButton(configInfo);
            }
        },
        loadYaData : function (yaid) {
            var $leftGrid = $("#leftGrid${idSuffix}");
            var $rightGrid = $("#rightGrid${idSuffix}");
            var yaData = $.loadJson( $.contextPath + "/yjyaxz!getYaData.json?yaid="+yaid );
            var leftGridList = yaData.leftMapList;
            var rightGridList = yaData.rightMapList;
            var masterFormData = yaData.masterFormData;
            _yalx = masterFormData.YALX;
            $("#yabh${idSuffix}").textbox("setValue",masterFormData.YABH);
            $("#yamc${idSuffix}").textbox("setValue",masterFormData.YAMC);
            $("#yalx${idSuffix}").combobox("setValue",masterFormData.YALX);
            $("#yams${idSuffix}").textbox("setValue",masterFormData.YAMS);

            $.each (leftGridList,function(index) {
                var id = generateId("temp");
                $leftGrid.grid("addRowData",id,{
                    ID:id,
                    zdmc:leftGridList[index].ZDMC,
                    xsmc:leftGridList[index].XSMC
                });
            });
            $.each (rightGridList,function(index){
            //debugger
                var id = generateId("temp");
                $rightGrid.grid("addRowData",id,{
                    ID:id,
                    zdmc:rightGridList[index].ZDMC,
                    xsmc:rightGridList[index].XSMC
                });
            })
        }
    });
    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'yjyaxz.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#maxDiv${idSuffix}'),
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                $("#allToRight${idSuffix}").button();
                $("#toRight${idSuffix}").button();
                $("#toLeft${idSuffix}").button();
                $("#allToLeft${idSuffix}").button();
                $("#yalx${idSuffix}").combobox("reload", [{'text': '用药信息', 'value': 'yyxx'},{'text':'销售去向','value':'xsqx'}]);
                $("#yalx${idSuffix}").combobox("option", "onChange", function (e, data) {
                    var $leftGrid = $("#leftGrid${idSuffix}");
                    var $rightGrid = $("#rightGrid${idSuffix}");
                    var rightGridData = $rightGrid.grid("getRowData");
                    for (var index in rightGridData) {
                        if (rightGridData[index].zdmc != 'CPZSM' && rightGridData[index].zdmc != 'PCH'&& rightGridData[index].zdmc != 'CSLSH'&&rightGridData[index].zdmc != 'CPMC') {
                            $rightGrid.grid("delRowData",rightGridData[index].ID);
                        }
                    }
                    $leftGrid.grid("clearGridData");
                    var fieldData = $.loadJson($.contextPath + '/yjyaxz!getFieldByYalx.json?yalx=' + data.value);
                    for (var index in fieldData) {
                        var field = fieldData[index];
                        var id = generateId("temp");
                        if (field.COLUMN_NAME == "ID") {
                            continue;
                        }
                        $leftGrid.grid("addRowData", id, {
                            xsmc: field.SHOW_NAME,
                            zdmc: field.COLUMN_NAME,
                            ID: id
                        });
                    }
                    _yalx = data.value;
                });
                $("#yalx${idSuffix}").combobox("option","beforeSelect",function (e,data) {
                    if (_yalx = data.value) {
                        return false
                    }
                    return true;
                });
                var $rightGrid = $("#rightGrid${idSuffix}");
                var cpmcid = generateId("temp");
                $rightGrid.grid("addRowData",cpmcid,{ID:cpmcid,zdmc:'CPMC',xsmc:'产品名称'});
                $rightGrid.grid("setRowData", cpmcid, null, 'coral-readonly');
                var zsmid = generateId("temp");
                $rightGrid.grid("addRowData",zsmid,{ID:zsmid,zdmc:'CPZSM',xsmc:'产品追溯码'});
                $rightGrid.grid("setRowData", zsmid, null, 'coral-readonly');
                var pchid = generateId("temp");
                $rightGrid.grid("addRowData",pchid,{ID:pchid,zdmc:'PCH',xsmc:'批次号'});
                $rightGrid.grid("setRowData", pchid, null,'coral-readonly');
                var cslshid = generateId("temp");
                $rightGrid.grid("addRowData",cslshid,{ID:cslshid,zdmc:'CSLSH',xsmc:'采收流水号'});
                $rightGrid.grid("setRowData", cslshid, null, 'coral-readonly');

                var yaid = CFG_getInputParamValue(configInfo,'yaid');
                if (yaid!="") $.ns("namespaceId${idSuffix}").loadYaData(yaid) ;
            }
        });
    });
</script>
