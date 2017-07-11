/**
 * 配置构件系统参数
 */
function configSystemParam() {
    var selectId = dataGrid.getSelectedRowId();
    if (selectId == undefined) {
        dhtmlx.message(getMessage("select_record"));
        return;
    }
    if (selectId.indexOf(",") != -1) {
        dhtmlx.message(getMessage("select_only_one_record"));
        return;
    }
    var componentSystemParamConfig = dataGrid.cells(selectId, 6).getValue();
    if (componentSystemParamConfig == "2") {
        dhtmlx.message("该记录无需绑定系统参数！");
        return;
    }
    if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var configWin = dhxWins.createWindow("configWin", 0, 0, 1000, 400);
    configWin.setModal(true);
    configWin.setText("系统参数绑定设置");
    configWin.center();
    systemParamConfigLayout = configWin.attachLayout("4W");
    initSystemParamConfigLayout(selectId);
}
// 构件的系统参数绑定设置layout
var systemParamConfigLayout;
// 构件的系统参数配置列表
var componentSystemParamGrid, systemParamGrid, systemParamConfigGrid;
var systemParamGridData = {
	format: {
		headers: ["<center>系统参数名称</center>", "<center>系统参数说明</center>"],
		colWidths: ["150", "150"],
		colTypes: ["ro", "ro"],
		colAligns: ["left", "left"]
	}
};
var componentSystemParamGridData = {
	format: {
		headers: ["<center>构件中系统参数名称</center>", "<center>构件中系统参数说明</center>"],
		colWidths: ["150", "150"],
		colTypes: ["ro", "ro"],
		colAligns: ["left", "left"]
	}
};
var systemParamConfigGridData = {
	format: {
		headers: ["<center>构件中系统参数名称</center>", "<center>构件中系统参数说明</center>", "<center>系统参数名称</center>", "<center>系统参数说明</center>"],
		colWidths: ["150", "150", "150", "150"],
		colTypes: ["ro", "ro", "ro", "ro"],
		colAligns: ["left", "left", "left", "left"]
	}
};
/**
 * 初始化页面构件的系统参数配置layout
 * @param {string} componentVersionId
 */
function initSystemParamConfigLayout(componentVersionId) {
    systemParamConfigLayout.cells("a").setText("构件中系统参数列表");
    systemParamConfigLayout.cells("b").setText("系统参数列表");
    systemParamConfigLayout.cells("c").hideHeader();
    systemParamConfigLayout.cells("d").setText("系统参数绑定列表");
    systemParamConfigLayout.cells("a").setWidth("230");
    systemParamConfigLayout.cells("b").setWidth("230");
    systemParamConfigLayout.cells("c").setWidth("85");
    var systemParamToolbar = systemParamConfigLayout.cells("b").attachToolbar();
    systemParamToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    systemParamToolbar.addText("searchAreaLabel", 0, "系统参数名称：");
    systemParamToolbar.addText("searchArea", 1, '<input id="titleFilter" type="text" style="width: 100px; border:1px solid gray;" onClick="(arguments[0]||window.event).cancelBubble=true;" onKeyUp="filterBy()">');
    componentSystemParamGrid = systemParamConfigLayout.cells("a").attachGrid();
    systemParamGrid = systemParamConfigLayout.cells("b").attachGrid();
    systemParamConfigGrid = systemParamConfigLayout.cells("d").attachGrid();
    initConfigGrid(componentSystemParamGrid, componentSystemParamGridData);
    initConfigGrid(systemParamGrid, systemParamGridData);
    initConfigGrid(systemParamConfigGrid, systemParamConfigGridData);
    initSystemParamButtonForm();
    loadConfigGridData(componentSystemParamGrid, COMPONENT_SYSTEM_PARAMETER_RELATION_MODEL_URL + "!getComponentSystemParamList.json?E_model_name=datagrid&componentVersionId=" + componentVersionId);
    loadConfigGridData(systemParamGrid, COMPONENT_SYSTEM_PARAMETER_RELATION_MODEL_URL + "!getSystemParamList.json?E_model_name=datagrid&componentVersionId=" + componentVersionId);
    loadConfigGridData(systemParamConfigGrid, COMPONENT_SYSTEM_PARAMETER_RELATION_MODEL_URL + "!getComponentSystemParamRelationList.json?E_model_name=datagrid&componentVersionId=" + componentVersionId);
    var paramStatusBar = systemParamConfigLayout.attachStatusBar();
    var paramConfigToolbar = new dhtmlXToolbarObject(paramStatusBar);
    paramConfigToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    paramConfigToolbar.setAlign("right");
    paramConfigToolbar.addButton("save", 0, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    paramConfigToolbar.addSeparator("septr$01", 1);
    paramConfigToolbar.addButton("close", 3, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    paramConfigToolbar.attachEvent('onClick', function(id) {
        if (id == "save") {
            var cnt = systemParamConfigGrid.getRowsNum();
            if (0 == cnt) {
                dhtmlx.message("请进行参数配置！");
                return;
            }
            var rowIds = "";
            systemParamConfigGrid.forEachRow(function(rowId) {
                rowIds += "," + rowId;
            });
            rowIds = rowIds.substring(1);
            var systemParamConfig = 0;
            if (componentSystemParamGrid.getRowsNum() == 0) {
                systemParamConfig = 1;
            }
            var url = COMPONENT_SYSTEM_PARAMETER_RELATION_MODEL_URL + "!saveComponentSystemParameterRelation.json?rowIds=" + rowIds + "&componentVersionId=" + componentVersionId + "&systemParamConfig=" + systemParamConfig;
            dhtmlxAjax.get(url, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == 'string') {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message({
                        text : getMessage("operate_success")
                    });
                } else {
                    dhtmlx.message({
                        text : getMessage("operate_failure")
                    });
                }
                refreshComponentVersionGrid();
                dhxWins.window("configWin").close();
            });
        } else {
            dhxWins.window("configWin").close();
        }
    });
}
// 参数配置左右按钮表单
var paramButtonFormConfig = [{
	type:"label",
	list: [{
		type: "button",
		name: "autoConfig",
		value: "自动",
		width: "40"
	},{
		type: "button",
		name: "toRight",
		value: "&gt;",
		width: "40"
	},{
		type: "button",
		name: "toLeft",
		value: "&lt;",
		width: "40"
	}]
}];
/**
 * 初始化系统参数配置左右按钮表单
 */
function initSystemParamButtonForm() {
    var obj = document.getElementById("DIV-oparatorArea1");
    if (obj == null) {
        obj = document.createElement("DIV");
        obj.setAttribute("id", "DIV-oparatorArea1");
        obj.setAttribute("style", "position:relative;top:" + 35 + "%;left:20px;display: none;");
        document.body.appendChild(obj);
    } else {
        obj.innerHTML = "";
    }
    var buttonForm = new dhtmlXForm("DIV-oparatorArea1", paramButtonFormConfig);
    systemParamConfigLayout.cells("c").attachObject("DIV-oparatorArea1");
    buttonForm.attachEvent("onButtonClick", function(id) {
        if ("autoConfig" == id) {
            var ids = systemParamGrid.getAllRowIds();
            var systemParamIds = ids.split(",");
            componentSystemParamGrid.forEachRow(function(componentSystemParamId) {
                var componentSystemParamName = componentSystemParamGrid.cells(componentSystemParamId, 0).getValue();
                for (var i = 0; i < systemParamIds.length; i++) {
                    var systemParamName = systemParamGrid.cells(systemParamIds[i], 0).getValue();
                    if (componentSystemParamName == systemParamName) {
                        var componentSystemParamRemark = componentSystemParamGrid.cells(componentSystemParamId, 1).getValue();
                        var systemParamRemark = systemParamGrid.cells(systemParamIds[i], 1).getValue();
                        var configId = componentSystemParamId + "-" + systemParamIds[i];
                        systemParamConfigGrid.addRow(configId, [componentSystemParamName, componentSystemParamRemark, systemParamName, systemParamRemark]);
                        componentSystemParamGrid.deleteRow(componentSystemParamId);
                        break;
                    }
                }
            });
        } else if ("toRight" == id) {
            var componentSystemParamId = componentSystemParamGrid.getSelectedRowId();
            if (null == componentSystemParamId)
                return;
            var componentSystemParamName = componentSystemParamGrid.cells(componentSystemParamId, 0).getValue();
            var componentSystemParamRemark = componentSystemParamGrid.cells(componentSystemParamId, 1).getValue();
            var systemParamId = systemParamGrid.getSelectedRowId();
            if (null == systemParamId)
                return;
            var systemParamName = systemParamGrid.cells(systemParamId, 0).getValue();
            var systemParamRemark = systemParamGrid.cells(systemParamId, 1).getValue();
            var configId = componentSystemParamId + "-" + systemParamId;
            systemParamConfigGrid.addRow(configId, [componentSystemParamName, componentSystemParamRemark, systemParamName, systemParamRemark]);
            componentSystemParamGrid.deleteSelectedRows();
        } else if ("toLeft" == id) {
            var rowId = systemParamConfigGrid.getSelectedRowId();
            if (null == rowId)
                return;
            var componentSystemParamName = systemParamConfigGrid.cells(rowId, 0).getValue();
            var componentSystemParamRemark = systemParamConfigGrid.cells(rowId, 1).getValue();
            var rId = rowId.split("-");
            componentSystemParamGrid.addRow(rId[0], [componentSystemParamName, componentSystemParamRemark]);
            systemParamConfigGrid.deleteSelectedRows();
        }
    });
}
function filterBy(e) {
    if ((e || window.event).keyCode == 13) {
        var tVal = document.getElementById("titleFilter").value.toLowerCase();
        for (var i = 0; i < systemParamGrid.getRowsNum(); i++) {
            var tStr = systemParamGrid.cells2(i, 1).getValue().toString().toLowerCase();
            if (tVal == "" || tStr.indexOf(tVal) != -1) {
                systemParamGrid.setRowHidden(systemParamGrid.getRowId(i), false);
            } else {
                systemParamGrid.setRowHidden(systemParamGrid.getRowId(i), true);
            }
        }
    }
}
/**
 * 配置自定义构件的相关信息（自身参数、入参、出参、方法、回调函数）
 */
function configRelationInfo(readonly) {
    var selectId = dataGrid.getSelectedRowId();
    if (selectId == undefined) {
        dhtmlx.message(getMessage("select_record"));
        return;
    }
    if (selectId.indexOf(",") != -1) {
        dhtmlx.message(getMessage("select_only_one_record"));
        return;
    }
    var readonly = false;
    var componentType = dataGrid.cells(selectId, 4).getValue();
    if (componentType != "3" && componentType != "4" && componentType != "5" && componentType != "6") {
        readonly = true;
    }
    if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var configRelationWin = dhxWins.createWindow("configRelationWin", 0, 0, 1000, 400);
    configRelationWin.setModal(true);
    configRelationWin.setText("配置关联信息");
    configRelationWin.center();
    var configRelationTabbar = configRelationWin.attachTabbar();
    configRelationTabbar.setImagePath(IMAGE_PATH);
    configRelationTabbar.addTab("selfParams", "构件自身参数", "120px");
    configRelationTabbar.addTab("inputParams", "构件入参", "120px");
    configRelationTabbar.addTab("outputParams", "构件出参", "120px");
    configRelationTabbar.addTab("functions", "方法", "120px");
    configRelationTabbar.addTab("callbacks", "回调函数", "120px");
    configRelationTabbar.setTabActive("selfParams");
    selfParamLayout = configRelationTabbar.cells("selfParams").attachLayout("1C");
    manageSelfParams(readonly);
    inputParamLayout = configRelationTabbar.cells("inputParams").attachLayout("1C");
    manageInputParams(readonly);
    outputParamLayout = configRelationTabbar.cells("outputParams").attachLayout("1C");
    manageOutputParams(readonly);
    functionLayout = configRelationTabbar.cells("functions").attachLayout("2E");
    manageFunctions(readonly);
    callbackLayout = configRelationTabbar.cells("callbacks").attachLayout("2E");
    manageCallbacks(readonly);
}
var selfParamLayout, inputParamLayout, outputParamLayout, functionLayout, callbackLayout;
var selfParamGrid, selfParamToolbar;
var selfParamFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "type", value: "0"},
			{type: "hidden", name: "componentVersionId"},
			{type: "input", label: "名称:", name: "name", maxLength:100, required: true, tooltip: '名称不能为空'},
			{type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "value", maxLength:100},
			{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "remark", maxLength:500, rows:"3"},
			{type: "newcolumn"},
			{type: "block", width: "120", offsetTop:"80", list:[
				{type: "button", name: "save", value: "保存", width:100}
			]}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
var selfParamGridData = {
	format: {
		headers: ["&nbsp;", "<center>名称</center>", "<center>默认值</center>", "<center>备注</center>" ,""],
		cols: ["id", "name", "value", "remark"],
		colWidths: ["30", "120", "120", "200", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left", "left"]
	}
};
/**
 * 刷新构件自身参数列表
 */
function refreshSelfParamGrid() {
    var componentVersionId = dataGrid.getSelectedRowId();
    search(selfParamGrid, selfParamGridData, COMPONENT_SELF_PARAM_MODEL_URL + "!search.json?Q_EQ_componentVersionId=" + componentVersionId);
}
/**
 * 初始化构件自身参数列表
 */
function initSelfParamGrid(readonly) {
    selfParamGrid.setImagePath(IMAGE_PATH);
    selfParamGrid.setHeader(selfParamGridData.format.headers.toString());
    selfParamGrid.setInitWidths(selfParamGridData.format.colWidths.toString());
    selfParamGrid.setColTypes(selfParamGridData.format.colTypes.toString());
    selfParamGrid.setColAlign(selfParamGridData.format.colAligns.toString());
    selfParamGrid.setSkin(Skin);
    selfParamGrid.init();
    selfParamGrid.enableMultiselect(true);
    selfParamGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    selfParamGrid.attachEvent("onRowSelect", function(rId, cInd) {
        selfParamGrid.cells(rId, 0).open();
    });
    selfParamGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = initDetailFormFormat(selfParamFormData);
    });
    selfParamGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        if (readonly) {
            subform.hideItem("save");
        }
        var componentVersionId = dataGrid.getSelectedRowId();
        if (id != "") {
            var url = COMPONENT_SELF_PARAM_MODEL_URL + "/" + id + ".json?_method=get";
            loadForm(subform, url);
        } else {
            subform.setItemValue("componentVersionId", componentVersionId);
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                var name = subform.getItemValue("name");
                var result = eval("(" + loadJson(COMPONENT_SELF_PARAM_MODEL_URL + "!validateFields.json?id=" + id + "&name=" + name + "&componentVersionId=" + componentVersionId) + ")");
                if (result.nameExist) {
                    dhtmlx.message(getMessage("form_field_exist", "自身参数名称"));
                    return;
                }
                if (id == "") {
                    SAVE_URL = COMPONENT_SELF_PARAM_MODEL_URL;
                    subform.setItemValue("_method", "post");
                } else {
                    SAVE_URL = COMPONENT_SELF_PARAM_MODEL_URL + "/" + id;
                    subform.setItemValue("_method", "put");
                }
                subform.send(SAVE_URL, "post", function(loader, response) {
                    dhtmlx.message(getMessage("save_success"));
                    refreshSelfParamGrid();
                });
            }
        });
    });
    selfParamGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            selfParamGrid.forEachRow(function(rId) {
                if (id != rId) {
                    selfParamGrid.cells(rId, 0).close();
                }
            });
        }
    });
    refreshSelfParamGrid();
}
/**
 *  初始化构件自身参数工具条
 */
function initSelfParamToolBar() {
    selfParamToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    selfParamToolbar.addButton("add", 0, "新增", "new.gif");
    selfParamToolbar.addSeparator("septr$01", 1);
    selfParamToolbar.addButton("delete", 2, "删除", "delete.gif");
    selfParamToolbar.attachEvent('onClick', function(buttonName) {
        if (buttonName == "add") {
            selfParamGrid.addSubRow();
        } else if (buttonName == "delete") {
            var selectId = selfParamGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                refreshSelfParamGrid();
                return;
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        dhtmlxAjax.get(COMPONENT_SELF_PARAM_MODEL_URL + "/" + selectId + "?_method=delete", function(loader) {
                            dhtmlx.message(getMessage("delete_success"));
                            selfParamGrid.clearAll();
                            refreshSelfParamGrid();
                        });
                    }
                }
            });
        }
    });
}
/**
 * 管理构件自身参数
 */
function manageSelfParams(readonly) {
    selfParamLayout.cells("a").hideHeader();
    selfParamGrid = selfParamLayout.cells("a").attachGrid();
    initSelfParamGrid(readonly);
    if (!readonly) {
	    selfParamToolbar = selfParamLayout.cells("a").attachToolbar();
	    initSelfParamToolBar();
    }
}
var inputParamGrid, inputParamToolbar;
var inputParamFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "componentVersionId"},
			{type: "input", label: "名称:", name: "name", maxLength:100, required: true, tooltip: '名称不能为空'},
			{type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "value", maxLength:100},
			{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "remark", maxLength:500, rows:"3"},
			{type: "newcolumn"},
			{type: "block", width: "120", offsetTop:"80", list:[
				{type: "button", name: "save", value: "保存", width:100}
			]}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
var inputParamGridData = {
	format: {
		headers: ["&nbsp;", "<center>名称</center>", "<center>默认值</center>", "<center>备注</center>" ,""],
		cols: ["id", "name", "value", "remark"],
		colWidths: ["30", "120", "120", "200", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left", "left"]
	}
};
/**
 * 刷新构件入参列表
 */
function refreshInputParamGrid() {
    var componentVersionId = dataGrid.getSelectedRowId();
    search(inputParamGrid, inputParamGridData, COMPONENT_INPUT_PARAM_MODEL_URL + "!search.json?Q_EQ_componentVersionId=" + componentVersionId);
}
/**
 * 初始化构件入参列表
 */
function initInputParamGrid(readonly) {
    inputParamGrid.setImagePath(IMAGE_PATH);
    inputParamGrid.setHeader(inputParamGridData.format.headers.toString());
    inputParamGrid.setInitWidths(inputParamGridData.format.colWidths.toString());
    inputParamGrid.setColTypes(inputParamGridData.format.colTypes.toString());
    inputParamGrid.setColAlign(inputParamGridData.format.colAligns.toString());
    inputParamGrid.setSkin(Skin);
    inputParamGrid.init();
    inputParamGrid.enableMultiselect(true);
    inputParamGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    inputParamGrid.attachEvent("onRowSelect", function(rId, cInd) {
        inputParamGrid.cells(rId, 0).open();
    });
    inputParamGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = initDetailFormFormat(inputParamFormData);
    });
    inputParamGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        if (readonly) {
            subform.hideItem("save");
        }
        var componentVersionId = dataGrid.getSelectedRowId();
        if (id != "") {
            var url = COMPONENT_INPUT_PARAM_MODEL_URL + "/" + id + ".json?_method=get";
            loadForm(subform, url);
        } else {
            subform.setItemValue("componentVersionId", componentVersionId);
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                var name = subform.getItemValue("name");
                var result = eval("(" + loadJson(COMPONENT_INPUT_PARAM_MODEL_URL + "!validateFields.json?id=" + id + "&name=" + name + "&componentVersionId=" + componentVersionId) + ")");
                if (result.nameExist) {
                    dhtmlx.message(getMessage("form_field_exist", "输入参数名称"));
                    return;
                }
                if (id == "") {
                    SAVE_URL = COMPONENT_INPUT_PARAM_MODEL_URL;
                    subform.setItemValue("_method", "post");
                } else {
                    SAVE_URL = COMPONENT_INPUT_PARAM_MODEL_URL + "/" + id;
                    subform.setItemValue("_method", "put");
                }
                subform.send(SAVE_URL, "post", function(loader, response) {
                    dhtmlx.message(getMessage("save_success"));
                    refreshInputParamGrid();
                });
            }
        });
    });
    inputParamGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            inputParamGrid.forEachRow(function(rId) {
                if (id != rId) {
                    inputParamGrid.cells(rId, 0).close();
                }
            });
        }
    });
    refreshInputParamGrid();
}
/**
 *  初始化构件入参工具条
 */
function initInputParamToolBar() {
    inputParamToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    inputParamToolbar.addButton("add", 0, "新增", "new.gif");
    inputParamToolbar.addSeparator("septr$01", 1);
    inputParamToolbar.addButton("delete", 2, "删除", "delete.gif");
    inputParamToolbar.attachEvent('onClick', function(buttonName) {
        if (buttonName == "add") {
            inputParamGrid.addSubRow();
        } else if (buttonName == "delete") {
            var selectId = inputParamGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                refreshInputParamGrid();
                return;
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        dhtmlxAjax.get(COMPONENT_INPUT_PARAM_MODEL_URL + "/" + selectId + "?_method=delete", function(loader) {
                            dhtmlx.message(getMessage("delete_success"));
                            inputParamGrid.clearAll();
                            refreshInputParamGrid();
                        });
                    }
                }
            });
        }
    });
}
/**
 * 管理构件入参
 */
function manageInputParams(readonly) {
    inputParamLayout.cells("a").hideHeader();
    inputParamGrid = inputParamLayout.cells("a").attachGrid();
    initInputParamGrid(readonly);
    if (!readonly) {
	    inputParamToolbar = inputParamLayout.cells("a").attachToolbar();
	    initInputParamToolBar();
    }
}
var outputParamGrid, outputParamToolbar;
var outputParamFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "componentVersionId"},
			{type: "input", label: "名称:", name: "name", maxLength:100, required: true, tooltip: '名称不能为空'},
			{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "remark", maxLength:500, rows:"3"},
			{type: "newcolumn"},
			{type: "block", width: "120", offsetTop:"55", list:[
				{type: "button", name: "save", value: "保存", width:100}
			]}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
var outputParamGridData = {
	format: {
		headers: ["&nbsp;", "<center>名称</center>", "<center>备注</center>" ,""],
		cols: ["id", "name", "remark"],
		colWidths: ["30", "120", "200", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left"]
	}
};
/**
 * 刷新构件出参列表
 */
function refreshOutputParamGrid() {
    var componentVersionId = dataGrid.getSelectedRowId();
    search(outputParamGrid, outputParamGridData, COMPONENT_OUTPUT_PARAM_MODEL_URL + "!search.json?Q_EQ_componentVersionId=" + componentVersionId);
}
/**
 * 初始化构件出参列表
 */
function initOutputParamGrid(readonly) {
    outputParamGrid.setImagePath(IMAGE_PATH);
    outputParamGrid.setHeader(outputParamGridData.format.headers.toString());
    outputParamGrid.setInitWidths(outputParamGridData.format.colWidths.toString());
    outputParamGrid.setColTypes(outputParamGridData.format.colTypes.toString());
    outputParamGrid.setColAlign(outputParamGridData.format.colAligns.toString());
    outputParamGrid.setSkin(Skin);
    outputParamGrid.init();
    outputParamGrid.enableMultiselect(true);
    outputParamGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    outputParamGrid.attachEvent("onRowSelect", function(rId, cInd) {
        outputParamGrid.cells(rId, 0).open();
    });
    outputParamGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = initDetailFormFormat(outputParamFormData);
    });
    outputParamGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        if (readonly) {
            subform.hideItem("save");
        }
        var componentVersionId = dataGrid.getSelectedRowId();
        if (id != "") {
            var url = COMPONENT_OUTPUT_PARAM_MODEL_URL + "/" + id + ".json?_method=get";
            loadForm(subform, url);
        } else {
            subform.setItemValue("componentVersionId", componentVersionId);
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                var name = subform.getItemValue("name");
                var result = eval("(" + loadJson(COMPONENT_OUTPUT_PARAM_MODEL_URL + "!validateFields.json?id=" + id + "&name=" + name + "&componentVersionId=" + componentVersionId) + ")");
                if (result.nameExist) {
                    dhtmlx.message(getMessage("form_field_exist", "输出参数名称"));
                    return;
                }
                if (id == "") {
                    SAVE_URL = COMPONENT_OUTPUT_PARAM_MODEL_URL;
                    subform.setItemValue("_method", "post");
                } else {
                    SAVE_URL = COMPONENT_OUTPUT_PARAM_MODEL_URL + "/" + id;
                    subform.setItemValue("_method", "put");
                }
                subform.send(SAVE_URL, "post", function(loader, response) {
                    dhtmlx.message(getMessage("save_success"));
                    refreshOutputParamGrid();
                });
            }
        });
    });
    outputParamGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            outputParamGrid.forEachRow(function(rId) {
                if (id != rId) {
                    outputParamGrid.cells(rId, 0).close();
                }
            });
        }
    });
    refreshOutputParamGrid();
}
/**
 * 初始化构件出参工具条
 */
function initOutputParamToolBar() {
    outputParamToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    outputParamToolbar.addButton("add", 0, "新增", "new.gif");
    outputParamToolbar.addSeparator("septr$01", 1);
    outputParamToolbar.addButton("delete", 2, "删除", "delete.gif");
    outputParamToolbar.attachEvent('onClick', function(buttonName) {
        if (buttonName == "add") {
            outputParamGrid.addSubRow();
        } else if (buttonName == "delete") {
            var selectId = outputParamGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                refreshOutputParamGrid();
                return;
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        dhtmlxAjax.get(COMPONENT_OUTPUT_PARAM_MODEL_URL + "/" + selectId + "?_method=delete", function(loader) {
                            dhtmlx.message(getMessage("delete_success"));
                            outputParamGrid.clearAll();
                            refreshOutputParamGrid();
                        });
                    }
                }
            });
        }
    });
}
/**
 * 管理构件出参
 */
function manageOutputParams(readonly) {
    outputParamLayout.cells("a").hideHeader();
    outputParamGrid = outputParamLayout.cells("a").attachGrid();
    initOutputParamGrid(readonly);
    if (!readonly) {
	    outputParamToolbar = outputParamLayout.cells("a").attachToolbar();
	    initOutputParamToolBar();
    }
}
var functionGrid, functionToolbar, functionDataGrid, functionDataToolbar;
var functionFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "componentVersionId"},
			{type: "hidden", name: "page"},
			{type: "input", label: "名称:", name: "name", maxLength:100, required: true, tooltip: '名称不能为空'},
			{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "remark", maxLength:500, rows:"3"},
			{type: "newcolumn"},
			{type: "block", width: "120", offsetTop:"55", list:[
				{type: "button", name: "save", value: "保存", width:100}
			]}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
var functionGridData = {
	format: {
		headers: ["&nbsp;", "<center>名称</center>", "<center>备注</center>" ,""],
		cols: ["id", "name", "remark"],
		colWidths: ["30", "120", "200", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left"]
	}
};
var functionDataFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "functionId"},
			{type: "input", label: "名称:", name: "name", maxLength:100, required: true, tooltip: '名称不能为空'},
			{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "remark", maxLength:500, rows:"3"},
			{type: "newcolumn"},
			{type: "block", width: "120", offsetTop:"55", list:[
				{type: "button", name: "save", value: "保存", width:100}
			]}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
var functionDataGridData = {
	format: {
		headers: ["&nbsp;", "<center>名称</center>", "<center>备注</center>" ,""],
		cols: ["id", "name", "remark"],
		colWidths: ["30", "120", "200", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left"]
	}
};
/**
 * 刷新构件方法列表
 */
function refreshFunctionGrid() {
    var componentVersionId = dataGrid.getSelectedRowId();
    search(functionGrid, functionGridData, COMPONENT_FUNCTION_MODEL_URL + "!search.json?Q_EQ_componentVersionId=" + componentVersionId);
}
/**
 * 初始化构件方法列表
 */
function initFunctionGrid(readonly) {
    functionGrid.setImagePath(IMAGE_PATH);
    functionGrid.setHeader(functionGridData.format.headers.toString());
    functionGrid.setInitWidths(functionGridData.format.colWidths.toString());
    functionGrid.setColTypes(functionGridData.format.colTypes.toString());
    functionGrid.setColAlign(functionGridData.format.colAligns.toString());
    functionGrid.setSkin(Skin);
    functionGrid.init();
    functionGrid.enableMultiselect(true);
    functionGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    functionGrid.attachEvent("onRowSelect", function(rId, cInd) {
        functionGrid.cells(rId, 0).open();
        var functionIds = functionGrid.getSelectedRowId();
        if (readonly) {
            if (functionIds && functionIds.indexOf(',') == -1) {
	            refreshFunctionDataGrid();
	        }
        } else {
	        if (functionIds && functionIds.indexOf(',') == -1) {
	            unlockFunctionData();
	            refreshFunctionDataGrid();
	        } else {
	            lockFunctionData();
	        }
        }
    });
    functionGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = initDetailFormFormat(functionFormData);
    });
    functionGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        if (readonly) {
            subform.hideItem("save");
        }
        var componentVersionId = dataGrid.getSelectedRowId();
        if (id != "") {
            var url = COMPONENT_FUNCTION_MODEL_URL + "/" + id + ".json?_method=get";
            loadForm(subform, url);
        } else {
            subform.setItemValue("componentVersionId", componentVersionId);
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                var name = subform.getItemValue("name");
                var result = eval("(" + loadJson(COMPONENT_FUNCTION_MODEL_URL + "!validateFields.json?id=" + id + "&name=" + name + "&componentVersionId=" + componentVersionId) + ")");
                if (result.nameExist) {
                    dhtmlx.message(getMessage("form_field_exist", "方法名称"));
                    return;
                }
                if (id == "") {
                    SAVE_URL = COMPONENT_FUNCTION_MODEL_URL;
                    subform.setItemValue("_method", "post");
                } else {
                    SAVE_URL = COMPONENT_FUNCTION_MODEL_URL + "/" + id;
                    subform.setItemValue("_method", "put");
                }
                subform.send(SAVE_URL, "post", function(loader, response) {
                    dhtmlx.message(getMessage("save_success"));
                    refreshFunctionGrid();
                });
            }
        });
    });
    functionGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            functionGrid.forEachRow(function(rId) {
                if (id != rId) {
                    functionGrid.cells(rId, 0).close();
                }
            });
        }
    });
    refreshFunctionGrid();
}
/**
 * 初始化构件方法工具条
 */
function initFunctionToolBar() {
    functionToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    functionToolbar.addButton("add", 0, "新增", "new.gif");
    functionToolbar.addSeparator("septr$01", 1);
    functionToolbar.addButton("delete", 2, "删除", "delete.gif");
    functionToolbar.attachEvent('onClick', function(buttonName) {
        if (buttonName == "add") {
            functionGrid.addSubRow();
        } else if (buttonName == "delete") {
            var selectId = functionGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                refreshFunctionGrid();
                return;
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        dhtmlxAjax.get(COMPONENT_FUNCTION_MODEL_URL + "/" + selectId + "?_method=delete", function(loader) {
                            dhtmlx.message(getMessage("delete_success"));
                            functionGrid.clearAll();
                            refreshFunctionGrid();
                            lockFunctionData();
                        });
                    }
                }
            });
        }
    });
}
/**
 * 刷新构件方法返回值列表
 */
function refreshFunctionDataGrid() {
    var functionId = functionGrid.getSelectedRowId();
    search(functionDataGrid, functionDataGridData, COMPONENT_FUNCTION_DATA_MODEL_URL + "!search.json?Q_EQ_functionId=" + functionId);
}
/**
 * 初始化构件方法返回值列表
 */
function initFunctionDataGrid(readonly) {
    functionDataGrid.setImagePath(IMAGE_PATH);
    functionDataGrid.setHeader(functionDataGridData.format.headers.toString());
    functionDataGrid.setInitWidths(functionDataGridData.format.colWidths.toString());
    functionDataGrid.setColTypes(functionDataGridData.format.colTypes.toString());
    functionDataGrid.setColAlign(functionDataGridData.format.colAligns.toString());
    functionDataGrid.setSkin(Skin);
    functionDataGrid.init();
    functionDataGrid.enableMultiselect(true);
    functionDataGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    functionDataGrid.attachEvent("onRowSelect", function(rId, cInd) {
        functionDataGrid.cells(rId, 0).open();
    });
    functionDataGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = initDetailFormFormat(functionDataFormData);
    });
    functionDataGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        if (readonly) {
            subform.hideItem("save");
        }
        var functionId = functionGrid.getSelectedRowId();
        if (id != "") {
            var url = COMPONENT_FUNCTION_DATA_MODEL_URL + "/" + id + ".json?_method=get";
            loadForm(subform, url);
        } else {
            subform.setItemValue("functionId", functionId);
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                var name = subform.getItemValue("name");
                var result = eval("(" + loadJson(COMPONENT_FUNCTION_DATA_MODEL_URL + "!validateFields.json?id=" + id + "&name=" + name + "&functionId=" + functionId) + ")");
                if (result.nameExist) {
                    dhtmlx.message(getMessage("form_field_exist", "方法返回值名称"));
                    return;
                }
                if (id == "") {
                    SAVE_URL = COMPONENT_FUNCTION_DATA_MODEL_URL;
                    subform.setItemValue("_method", "post");
                } else {
                    SAVE_URL = COMPONENT_FUNCTION_DATA_MODEL_URL + "/" + id;
                    subform.setItemValue("_method", "put");
                }
                subform.send(SAVE_URL, "post", function(loader, response) {
                    dhtmlx.message(getMessage("save_success"));
                    refreshFunctionDataGrid();
                });
            }
        });
    });
    functionDataGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            functionDataGrid.forEachRow(function(rId) {
                if (id != rId) {
                    functionDataGrid.cells(rId, 0).close();
                }
            });
        }
    });
}
/**
 * 初始化构件方法返回值工具条
 */
function initFunctionDataToolBar() {
    functionDataToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    functionDataToolbar.addButton("add", 0, "新增", "new.gif");
    functionDataToolbar.addSeparator("septr$01", 1);
    functionDataToolbar.addButton("delete", 2, "删除", "delete.gif");
    functionDataToolbar.attachEvent('onClick', function(buttonName) {
        if (buttonName == "add") {
            functionDataGrid.addSubRow();
        } else if (buttonName == "delete") {
            var selectId = functionDataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                refreshFunctionDataGrid();
                return;
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        dhtmlxAjax.get(COMPONENT_FUNCTION_DATA_MODEL_URL + "/" + selectId + "?_method=delete", function(loader) {
                            dhtmlx.message(getMessage("delete_success"));
                            functionDataGrid.clearAll();
                            refreshFunctionDataGrid();
                        });
                    }
                }
            });
        }
    });
}
/**
 * 锁定方法返回值区域
 */
function lockFunctionData() {
    if (functionDataToolbar) {
        functionDataToolbar.disableItem("add");
        functionDataToolbar.disableItem("delete");
    }
    functionDataGrid.clearAll();
}
/**
 * 解锁方法返回值区域
 */
function unlockFunctionData() {
    if (functionDataToolbar) {
        functionDataToolbar.enableItem("add");
        functionDataToolbar.enableItem("delete");
    }
}
/**
 * 管理构件方法
 */
function manageFunctions(readonly) {
    var componentVersionId = dataGrid.getSelectedRowId();
    functionLayout.cells("a").hideHeader();
    functionGrid = functionLayout.cells("a").attachGrid();
    initFunctionGrid(readonly);
    if (!readonly) {
	    functionToolbar = functionLayout.cells("a").attachToolbar();
	    initFunctionToolBar();
    } else {
        functionToolbar = null;
        functionDataToolbar = null;
    }
    functionLayout.cells("b").hideHeader();
    functionDataGrid = functionLayout.cells("b").attachGrid();
    initFunctionDataGrid(readonly);
    if (!readonly) {
	    functionDataToolbar = functionLayout.cells("b").attachToolbar();
	    initFunctionDataToolBar();
    } else {
        lockFunctionData();
    }
}
var callbackGrid, callbackToolbar, callbackParamGrid, callbackParamToolbar;
var callbackFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "componentVersionId"},
			{type: "hidden", name: "page"},
			{type: "input", label: "名称:", name: "name", maxLength:100, required: true, tooltip: '名称不能为空'},
			{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "remark", maxLength:500, rows:"3"},
			{type: "newcolumn"},
			{type: "block", width: "120", offsetTop:"55", list:[
				{type: "button", name: "save", value: "保存", width:100}
			]}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
var callbackGridData = {
	format: {
		headers: ["&nbsp;", "<center>名称</center>", "<center>备注</center>" ,""],
		cols: ["id", "name", "remark"],
		colWidths: ["30", "120", "200", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left"]
	}
};
var callbackParamFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "callbackId"},
			{type: "input", label: "名称:", name: "name", maxLength:100, required: true, tooltip: '名称不能为空'},
			{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "remark", maxLength:500, rows:"3"},
			{type: "newcolumn"},
			{type: "block", width: "120", offsetTop:"55", list:[
				{type: "button", name: "save", value: "保存", width:100}
			]}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
var callbackParamGridData = {
	format: {
		headers: ["&nbsp;", "<center>名称</center>", "<center>备注</center>" ,""],
		cols: ["id", "name", "remark"],
		colWidths: ["30", "120", "200", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left"]
	}
};
/**
 * 刷新构件回调函数列表
 */
function refreshCallbackGrid() {
    var componentVersionId = dataGrid.getSelectedRowId();
    search(callbackGrid, callbackGridData, COMPONENT_CALLBACK_MODEL_URL + "!search.json?Q_EQ_componentVersionId=" + componentVersionId);
}
/**
 * 初始化构件回调函数列表
 */
function initCallbackGrid(readonly) {
    callbackGrid.setImagePath(IMAGE_PATH);
    callbackGrid.setHeader(callbackGridData.format.headers.toString());
    callbackGrid.setInitWidths(callbackGridData.format.colWidths.toString());
    callbackGrid.setColTypes(callbackGridData.format.colTypes.toString());
    callbackGrid.setColAlign(callbackGridData.format.colAligns.toString());
    callbackGrid.setSkin(Skin);
    callbackGrid.init();
    callbackGrid.enableMultiselect(true);
    callbackGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    callbackGrid.attachEvent("onRowSelect", function(rId, cInd) {
        callbackGrid.cells(rId, 0).open();
        var callbackIds = callbackGrid.getSelectedRowId();
        if (readonly) {
            if (callbackIds && callbackIds.indexOf(',') == -1) {
	            refreshCallbackParamGrid();
	        }
        } else {
	        if (callbackIds && callbackIds.indexOf(',') == -1) {
	            unlockCallbackParam();
	            refreshCallbackParamGrid();
	        } else {
	            lockCallbackParam();
	        }
        }
    });
    callbackGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = initDetailFormFormat(callbackFormData);
    });
    callbackGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        if (readonly) {
            subform.hideItem("save");
        }
        var componentVersionId = dataGrid.getSelectedRowId();
        if (id != "") {
            var url = COMPONENT_CALLBACK_MODEL_URL + "/" + id + ".json?_method=get";
            loadForm(subform, url);
        } else {
            subform.setItemValue("componentVersionId", componentVersionId);
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                var name = subform.getItemValue("name");
                var result = eval("(" + loadJson(COMPONENT_CALLBACK_MODEL_URL + "!validateFields.json?id=" + id + "&name=" + name + "&componentVersionId=" + componentVersionId) + ")");
                if (result.nameExist) {
                    dhtmlx.message(getMessage("form_field_exist", "回调函数名称"));
                    return;
                }
                if (id == "") {
                    SAVE_URL = COMPONENT_CALLBACK_MODEL_URL;
                    subform.setItemValue("_method", "post");
                } else {
                    SAVE_URL = COMPONENT_CALLBACK_MODEL_URL + "/" + id;
                    subform.setItemValue("_method", "put");
                }
                subform.send(SAVE_URL, "post", function(loader, response) {
                    dhtmlx.message(getMessage("save_success"));
                    refreshCallbackGrid();
                });
            }
        });
    });
    callbackGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            callbackGrid.forEachRow(function(rId) {
                if (id != rId) {
                    callbackGrid.cells(rId, 0).close();
                }
            });
        }
    });
    refreshCallbackGrid();
}
/**
 * 初始化构件回调函数工具条
 */
function initCallbackToolBar() {
    callbackToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    callbackToolbar.addButton("add", 0, "新增", "new.gif");
    callbackToolbar.addSeparator("septr$01", 1);
    callbackToolbar.addButton("delete", 2, "删除", "delete.gif");
    callbackToolbar.attachEvent('onClick', function(buttonName) {
        if (buttonName == "add") {
            callbackGrid.addSubRow();
        } else if (buttonName == "delete") {
            var selectId = callbackGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                refreshCallbackGrid();
                return;
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        dhtmlxAjax.get(COMPONENT_CALLBACK_MODEL_URL + "/" + selectId + "?_method=delete", function(loader) {
                            dhtmlx.message(getMessage("delete_success"));
                            callbackGrid.clearAll();
                            refreshCallbackGrid();
                            lockCallbackParam();
                        });
                    }
                }
            });
        }
    });
}
/**
 * 刷新构件回调函数参数列表
 */
function refreshCallbackParamGrid() {
    var callbackId = callbackGrid.getSelectedRowId();
    search(callbackParamGrid, callbackParamGridData, COMPONENT_CALLBACK_PARAM_MODEL_URL + "!search.json?Q_EQ_callbackId=" + callbackId);
}
/**
 * 初始化构件回调函数参数列表
 */
function initCallbackParamGrid(readonly) {
    callbackParamGrid.setImagePath(IMAGE_PATH);
    callbackParamGrid.setHeader(callbackParamGridData.format.headers.toString());
    callbackParamGrid.setInitWidths(callbackParamGridData.format.colWidths.toString());
    callbackParamGrid.setColTypes(callbackParamGridData.format.colTypes.toString());
    callbackParamGrid.setColAlign(callbackParamGridData.format.colAligns.toString());
    callbackParamGrid.setSkin(Skin);
    callbackParamGrid.init();
    callbackParamGrid.enableMultiselect(true);
    callbackParamGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    callbackParamGrid.attachEvent("onRowSelect", function(rId, cInd) {
        callbackParamGrid.cells(rId, 0).open();
    });
    callbackParamGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = initDetailFormFormat(callbackParamFormData);
    });
    callbackParamGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        if (readonly) {
            subform.hideItem("save");
        }
        var callbackId = callbackGrid.getSelectedRowId();
        if (id != "") {
            var url = COMPONENT_CALLBACK_PARAM_MODEL_URL + "/" + id + ".json?_method=get";
            loadForm(subform, url);
        } else {
            subform.setItemValue("callbackId", callbackId);
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                var name = subform.getItemValue("name");
                var result = eval("(" + loadJson(COMPONENT_CALLBACK_PARAM_MODEL_URL + "!validateFields.json?id=" + id + "&name=" + name + "&callbackId=" + callbackId) + ")");
                if (result.nameExist) {
                    dhtmlx.message(getMessage("form_field_exist", "回调函数参数名称"));
                    return;
                }
                if (id == "") {
                    SAVE_URL = COMPONENT_CALLBACK_PARAM_MODEL_URL;
                    subform.setItemValue("_method", "post");
                } else {
                    SAVE_URL = COMPONENT_CALLBACK_PARAM_MODEL_URL + "/" + id;
                    subform.setItemValue("_method", "put");
                }
                subform.send(SAVE_URL, "post", function(loader, response) {
                    dhtmlx.message(getMessage("save_success"));
                    refreshCallbackParamGrid();
                });
            }
        });
    });
    callbackParamGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            callbackParamGrid.forEachRow(function(rId) {
                if (id != rId) {
                    callbackParamGrid.cells(rId, 0).close();
                }
            });
        }
    });
}
/**
 * 初始化构件回调函数参数工具条
 */
function initCallbackParamToolBar() {
    callbackParamToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    callbackParamToolbar.addButton("add", 0, "新增", "new.gif");
    callbackParamToolbar.addSeparator("septr$01", 1);
    callbackParamToolbar.addButton("delete", 2, "删除", "delete.gif");
    callbackParamToolbar.attachEvent('onClick', function(buttonName) {
        if (buttonName == "add") {
            callbackParamGrid.addSubRow();
        } else if (buttonName == "delete") {
            var selectId = callbackParamGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                refreshCallbackParamGrid();
                return;
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        dhtmlxAjax.get(COMPONENT_CALLBACK_PARAM_MODEL_URL + "/" + selectId + "?_method=delete", function(loader) {
                            dhtmlx.message(getMessage("delete_success"));
                            callbackParamGrid.clearAll();
                            refreshCallbackParamGrid();
                        });
                    }
                }
            });
        }
    });
}
/**
 * 锁定回调函数参数区域
 */
function lockCallbackParam() {
    if (callbackParamToolbar) {
        callbackParamToolbar.disableItem("add");
        callbackParamToolbar.disableItem("delete");
    }
    callbackParamGrid.clearAll();
}
/**
 * 解锁回调函数参数区域
 */
function unlockCallbackParam() {
    if (callbackParamToolbar) {
        callbackParamToolbar.enableItem("add");
        callbackParamToolbar.enableItem("delete");
    }
}
/**
 * 管理构件回调函数
 */
function manageCallbacks(readonly) {
    var componentVersionId = dataGrid.getSelectedRowId();
    callbackLayout.cells("a").hideHeader();
    callbackGrid = callbackLayout.cells("a").attachGrid();
    initCallbackGrid(readonly);
    if (!readonly) {
	    callbackToolbar = callbackLayout.cells("a").attachToolbar();
	    initCallbackToolBar();
    } else {
        callbackToolbar = null;
        callbackParamToolbar = null;
    }
    callbackLayout.cells("b").hideHeader();
    callbackParamGrid = callbackLayout.cells("b").attachGrid();
    initCallbackParamGrid(readonly);
    if (!readonly) {
	    callbackParamToolbar = callbackLayout.cells("b").attachToolbar();
	    initCallbackParamToolBar();
    } else {
        lockCallbackParam();
    }
}