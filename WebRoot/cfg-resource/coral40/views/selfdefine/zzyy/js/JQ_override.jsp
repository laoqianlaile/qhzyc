<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @author qiucs 
 * @date   2014-7-15
 * 系统配置平台应用自定义二次开发JS模板 
 ***************************************************/
 
 
(function(subffix) {

/**
 * 二次开发定位自己controller
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return $.contextPath + "/appmanage/show-module";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () { 
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义列表
 */
var masterTableId = null;
    //是否初始加载
var isInitOp = true;
    var masterGridUI = null;
function _override_grid (ui) {
	//获取修改传入的方案id
	var _faid = CFG_getInputParamValue(ui.assembleData.parentConfigInfo.parentConfigInfo, "FAID");
    //是否为详细查看
    var isView = CFG_getInputParamValue(ui.assembleData.parentConfigInfo.parentConfigInfo, "VIEW");
    var isReadonly = false;
    if(isView == "VIEW"||isView=="DETAIL"){
        isReadonly = true;
    }
    //是否初始加载
    if(ui.options.number == 1) {
        //列表过滤条件
        ui.options.columns = "(EQ_C_PID≡" + _faid + ")AND(NOT_C_IS_DELETE≡1)";
        masterTableId = ui.options.tableId;
        //修改列属性
        ui.beforeInitGrid = function (setting) {
            setting.onSelectRow = function (e , data){
//                var selectedRowIds = ui.getSelectedRowId();
//                var selectedNsxbh = [];
//                for (var i in selectedRowIds) {
//                    var rowData = ui.getRowData(selectedRowIds[i]);
//                    selectedNsxbh.push(rowData.NSZYXBH);
//                }
//                var detailGrid = ui.getDetailGrid();
            };
            //设置列表列下拉框数据
            //农事作业项名称
            var zyxmcIndex = $.inArray("农事作业项名称", setting.colNames);
            setting.colModel[zyxmcIndex].formatter = 'text';
            setting.colModel[zyxmcIndex].formatoptions = {
                'required' : true,
                'readonly' : isReadonly
            };
            //农事项间隔时间
            var jgsjIndex = $.inArray("农事项间隔时间", setting.colNames);
            setting.colModel[jgsjIndex].formatter = 'text';
            setting.colModel[jgsjIndex].formatoptions = {
                'required' : true,
                'readonly' : isReadonly
            };
            //操作浮动时间
            var czfdsjIndex = $.inArray("操作浮动时间", setting.colNames);
            setting.colModel[czfdsjIndex].formatter = 'text';
            setting.colModel[czfdsjIndex].formatoptions = {
                'required': true,
                'readonly' : isReadonly
            };
            //是否起始项
            var isStartIndex = $.inArray("是否起始项",setting.colNames);
            setting.colModel[isStartIndex].formatter = 'combobox';
            setting.colModel[isStartIndex].formatoptions = {
                'data' : [{'text':'是','value':'1'},{'text':'否','value':'0'}],
                'readonly' : true
            };
            //用药时段
            var yysd = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZYYSD");
            var yysdIndex = $.inArray("用药时段",setting.colNames);
            setting.colModel[yysdIndex].formatter = 'combobox';
            setting.colModel[yysdIndex].revertCode = true;//存编码
            setting.colModel[yysdIndex].formatoptions = {
                'emptyText': '请选择',
                'required': true,
                'data' : yysd,
                'readonly' : isReadonly
            };
            //重复间隔时间
            var cfjgsjIndex = $.inArray("重复间隔时间", setting.colNames);
            setting.colModel[cfjgsjIndex].formatter = 'text';
            setting.colModel[cfjgsjIndex].formatoptions = {
                'required': true,
                'readonly' : isReadonly
            };
            //重复次数
            var cfcsIndex = $.inArray("重复次数", setting.colNames);
            setting.colModel[cfcsIndex].formatter = 'text';
            setting.colModel[cfcsIndex].formatoptions = {
                'required': true,
                'readonly' : isReadonly
            };
            if (isView == "VIEW"||isView=="DETAIL") {
                var delIndex = $.inArray("操作", setting.colNames);
                setting.colNames.splice(delIndex, 1);
                setting.colModel.splice(delIndex, 1);
            }
            setting.datatype = "local";
            return setting;
        };
        ui._init = function () {
            ui.assembleData.cMasterGridDivId = ui.element.attr("id");
            masterGridUI = ui;
        };
        //自定义按钮
        ui.clickSecondDev = function (id) {
            if (id == $.config.preButtonId + "add") {
                var $grid = ui.uiGrid;
                var rowId = generateId("temp");
                var rowData = ui.getRowData();
                var currentMaxNo = 0;
                if(rowData.length>0){
                    currentMaxNo = parseInt(rowData[rowData.length - 1].NSZYXBH);
                }
                $grid.grid("addRowData", rowId,{ID:rowId,PID:_faid,NSZYXBH:currentMaxNo + 1,IS_START:"0",IS_START1:"否", CFJGSJ:"0", CFCS:1, CZFDSJ:0}, "last");
            }
            if (id == $.config.preButtonId + "setStart") {
                var selectedId = ui.getSelectedRowId();
                if (selectedId.length!=1) {
                    CFG_message("请选择一条记录","warning");
                    return;
                }
                if(_faid != false && _faid != ""){
                    if(ui.uiGrid.grid("getCellComponent", selectedId[0], "IS_START").combobox("getValue") != "1"){
                        $.confirm("是否修改起始农事项，确定吗？", function(sure) {
                            if (sure) {
                                updateQsnsx(selectedId);
                            }
                        });
                    }
                }else{
                    if(ui.uiGrid.grid("getCellComponent", selectedId[0], "IS_START").combobox("getValue") != "1"){
                        var gridConfigInfos = ui.assembleData.parentConfigInfo.childConfigInfos;
                        for (var i in gridConfigInfos) {
                            var gridId = gridConfigInfos[i].cMasterGridDivId;
                            var jqGrid = $("#" + gridId);
                            var rowData = jqGrid.cgrid("getRowData");
                            var bool = true;
                            for (var j in rowData) {
                                if (rowData[j].IS_START == "1") {
                                    bool = false;
                                    $.confirm("是否修改起始农事项，确定吗？", function(sure) {
                                        if (sure) {
                                            updateQsnsx(selectedId);
                                        }
                                    });
                                }
                            }
                            if(bool){
                                updateQsnsx(selectedId);
                            }
                        }
                    }
                }
            }
        };

        function updateQsnsx(selectedId){
            var $isStartCommbobox = ui.uiGrid.grid("getCellComponent", selectedId[0], "IS_START");
            $isStartCommbobox.combobox("setValue", "1");
            ui.uiGrid.grid("setCell",selectedId[0], "IS_START1", "是");
            var $nsxjgsjTextbox = ui.uiGrid.grid("getCellComponent", selectedId[0], "NSXJGSJ");
            $nsxjgsjTextbox.textbox("setValue", "0");
            $nsxjgsjTextbox.textbox({readonly:true});
            var gridConfigInfos = ui.assembleData.parentConfigInfo.childConfigInfos;
            for (var i in gridConfigInfos) {
                var gridId = gridConfigInfos[i].cMasterGridDivId;
                var jqGrid = $("#" + gridId);
                var rowData = jqGrid.cgrid("getRowData");
                for (var j in rowData) {
                    if (rowData[j].IS_START == "1") {
                        if (rowData[j].ID == selectedId[0]) continue;
                        var $grid = jqGrid.cgrid("getGridJQ");
                        var $isStart = $grid.grid("getCellComponent", rowData[j].ID, "IS_START");
                        $isStart.combobox("setValue", "0");
                        $grid.grid("setCell", rowData[j].ID, "IS_START1", "否");
                        var $nsxjgsj = $grid.grid("getCellComponent", rowData[j].ID, "NSXJGSJ");
                        $nsxjgsj.textbox({readonly:false});
                    }
                }
            }
        }

        //复写列表删除方法(添加后台删除)
        ui.linkDeleteGD = function (id) {
            var _this = ui;
            $.confirm("当前记录将从列表移除，确定吗？", function (sure) {
                if (sure) {
                    var rowData = ui.getRowData(id);
                    var nsxbh = rowData.NSZYXBH;
                    var detailGrid = ui.getDetailGrid();
                    var detailGridRowData = detailGrid.getRowData();
                    if (id.substr(0, 5) == "temp_") {
                        _this.uiGrid.grid("delRowData", id);
                    } else {
                        var delStatus = $.loadJson($.contextPath + '/zzyy!deleteYy.json?dataId=' + id);
                        if (delStatus == "SUCCESS") _this.uiGrid.grid("delRowData", id);
                        else CFG_message("删除失败", "error");
                    }
                    for (var i in detailGridRowData) {
                        if (nsxbh == detailGridRowData[i].NSZYXBH) {
                            if (detailGridRowData[i].ID.substr(0,5) == "temp_") {
                                detailGrid.uiGrid.grid("delRowData",detailGridRowData[i].ID);
                            } else {
                                var delStatus = $.loadJson($.contextPath + '/zzyy!deleteYyTrp.json?dataId=' + detailGridRowData[i].ID);
                                if(delStatus == "SUCCESS") detailGrid.uiGrid.grid("delRowData", detailGridRowData[i].ID);
                                else CFG_message("删除失败", "error");
                            }
                        }
                    }
                }
            });
        }
    } else if (ui.options.number == 2) {
        ui.beforeInitGrid = function (setting) {
            var zyxbhIndex = $.inArray("农事作业项编号", setting.colNames);
            var trptymIndex = $.inArray("投入品通用名", setting.colNames);
            var trpxx = $.loadJson($.contextPath + '/zzbzfa!getTrpxx.json?lx=NY');
            var trpTymArray = [];
            for (var i in trpxx) {
                trpTymArray.push({'text':trpxx[i].TEXT,'value':trpxx[i].TEXT});
            }
            //投入品通用名
            setting.colModel[trptymIndex].formatter = 'combobox';
            setting.colModel[trptymIndex].revertCode = true;//存编码
            setting.colModel[trptymIndex].formatoptions = {
                'emptyText': '请选择',
                'required': true,
                'enableFilter': true,
                'data' : trpTymArray,
                'onChange' : function (e,data) {
                    var tym = data.newText;
                    for (var i in trpxx) {
                        if (tym == trpxx[i].TEXT) {
//                            ui.uiGrid.grid("setCell", e.data.rowId, "YT", trpxx[i].YT);
                            ui.uiGrid.grid("setCell", e.data.rowId, "TJYL", trpxx[i].TJYL);
                            break;
                        }
                    }
                },
                'readonly' : isReadonly
            };
            //农事作业项编号
            var zyxbhArray = [];
            if(_faid != false && _faid != ""){
                zyxbhArray = $.loadJson($.contextPath + "/zzfalb!getZyxbh.json?id=" + _faid + "&tablename=T_ZZ_YY");
            }
            setting.colModel[zyxbhIndex].formatter = 'combobox';
            setting.colModel[zyxbhIndex].revertCode = true;//存编码
            setting.colModel[zyxbhIndex].formatoptions = {
                'emptyText': '请选择',
                'required': true,
                data : zyxbhArray,
                'readonly' : isReadonly
            };
            if (isView == "VIEW"||isView=="DETAIL") {
                var delIndex = $.inArray("操作", setting.colNames);
                setting.colNames.splice(delIndex, 1);
                setting.colModel.splice(delIndex, 1);
            }
            setting.dataType = "local";
            return setting;
        };
        ui._init = function () {
            ui.assembleData.cDetailGridDivId = ui.element.attr("id");
        };
        ui.clickSecondDev = function(id){
            if(id == $.config.preButtonId + "add1"){
                var $grid = ui.uiGrid;
                var rowId = generateId("temp");
                var nsxbhArray = [];
                var masterGrid = ui.getMasterGrid();
                var rowData = masterGrid.getRowData();
                for (var i in rowData) {
                    var nsxbh = rowData[i].NSZYXBH;
                    nsxbhArray.push({'text':nsxbh,'value':nsxbh});
                }
                $grid.grid("addRowData", rowId,{ID:rowId,PID:""}, "last");
                var $nsxbhCommbobox = ui.uiGrid.grid("getCellComponent", rowId, "NSZYXBH");
                $nsxbhCommbobox.combobox("reload", nsxbhArray);
            }
        };
        ui.linkDeleteGD = function (id) {
            var _this = ui;
            $.confirm("当前记录将从列表移除，确定吗？", function(sure) {
                if (sure) {
                    if(id.substr(0,5)=="temp_") {
                        _this.uiGrid.grid("delRowData", id);
                    } else {
                        var delStatus = $.loadJson($.contextPath + '/zzyy!deleteYyTrp.json?dataId='+id);
                        if(delStatus == "SUCCESS") _this.uiGrid.grid("delRowData", id);
                        else CFG_message("删除失败", "error");
                    }
                }
            });
        };
    }
};
/**
 *  二次开发：复写自定义树
 */
function _override_tree (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override tree!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
    var isView = CFG_getInputParamValue(ui.assembleData.parentConfigInfo.parentConfigInfo, "VIEW");
    if (ui.options.number == 1) {
        ui.processItems = function (data) {
            var btns = [];
            for (var i = 0; i < data.length; i++) {
                btns.push(data[i]);
            }
            if (isView != "VIEW"&&isView!="DETAIL") {
//                btns.push(
//                        "->"
//                );
                btns.push({
                    id: $.config.preButtonId + "add",
                    label: "添加",
                    type: "button"
                });
                btns.push({
                    id : $.config.preButtonId + "setStart",
                    label : "设为起始项",
                    type : "button"
                });
            }
            return btns;
        };
    } else if (ui.options.number == 2) {
        ui.processItems = function (data) {
            var btns = [];
            for (var i = 0; i < data.length; i++) {
                btns.push(data[i]);
            }
            if (isView != "VIEW"&&isView!="DETAIL") {
                btns.push({
                    id: $.config.preButtonId + "add1",
                    label: "添加",
                    type: "button"
                });
            }
            return btns;
        };
    }
};
/**
 *  二次开发：复写自定义布局
 */
function _override_layout (ui) {
    ui._init = function () {
        var masterGrid = ui.getGrid(masterTableId);
        masterGrid.setGridOption("onComplete",function (e,mData_) {
            if (isInitOp) {
                var gridConfigInfos = masterGridUI.assembleData.parentConfigInfo.childConfigInfos;
                var qsnsxcount = 0;
                for (var i in gridConfigInfos) {
                    var gridId = gridConfigInfos[i].cMasterGridDivId;
                    var jqGrid = $("#" + gridId);
                    var rowData = jqGrid.cgrid("getRowData");
                    for (var j in rowData) {
                        if (rowData[j].IS_START == "1") {
                            qsnsxcount++;
                            if (qsnsxcount == 2) {
                                $.each(masterGridUI.uiGrid.grid("getDataIDs"), function (e, data) {
                                    var $isStart = masterGridUI.uiGrid.grid("getCellComponent", data, "IS_START");
                                    if ($isStart.combobox("getValue") == "1") {
                                        $isStart.combobox("setValue", "0");
                                        masterGridUI.uiGrid.grid("setCell", data, "IS_START1", "否");
                                    }
                                });
                            }
                        }
                    }
                }
                $.each($(masterGrid.uiGrid).grid("getDataIDs"),function(e,data){
                    var $isStart = $(masterGrid.uiGrid).grid("getCellComponent", data, "IS_START");
                    if($isStart.combobox("getValue") == "1"){
                        var $nsxjgsj = $(masterGrid.uiGrid).grid("getCellComponent", data, "NSXJGSJ");
                        $nsxjgsj.textbox({readonly:true});
                    }
                });
                var rowData = masterGrid.getRowData();
                var detailGrid = masterGrid.getDetailGrid();
                var idArray = [];
                //播种投入品数据过滤
                var filterStr = "((EQ_C_PID≡'')";
                for (var i in rowData) {
                    filterStr += "OR(EQ_C_PID≡" + rowData[i].ID + ")";
                }
                filterStr += ")AND(NOT_C_IS_DELETE≡1)";
                detailGrid.options.columns = filterStr;
                detailGrid.load();
                detailGrid.enableToolbar();
            }
            isInitOp = false;
        });
        masterGrid.load();
    }
};








/**
 * 在此可以复写所有自定义JS类
 * @param selector
 * @returns {JQ_override}
 */
window[CFG_overrideName(subffix)] = function () {
	
	//var startTime = new Date().getTime();
	
	if (this instanceof $.config.cform) {
		_override_form(this);
	} else if (this instanceof $.config.cgrid) {
		_override_grid(this);
	} else if (this instanceof $.config.ctree) {
		_override_tree(this);
	} else if (this instanceof $.config.ctbar) {
		_override_tbar(this);
	} else if (this instanceof $.config.clayout) {
		_override_layout(this);
	}
	
	//console.log("over ride cost time: " + (new Date().getTime() - startTime));
};

	
	
	
	
})("${timestamp}");
</script>
