COMPONENT_AREA_MODEL_URL = contextPath + "/component/component-area";
COMPONENT_MODEL_URL = contextPath + "/component/component";
COMPONENT_VERSION_MODEL_URL = contextPath + "/component/component-version";
COMPONENT_SYSTEM_PARAMETER_RELATION_MODEL_URL = contextPath + "/component/component-system-parameter-relation";
COMPONENT_SELF_PARAM_MODEL_URL = contextPath + "/component/component-self-param";
COMPONENT_INPUT_PARAM_MODEL_URL = contextPath + "/component/component-input-param";
COMPONENT_OUTPUT_PARAM_MODEL_URL = contextPath + "/component/component-output-param";
COMPONENT_FUNCTION_MODEL_URL = contextPath + "/component/component-function";
COMPONENT_FUNCTION_DATA_MODEL_URL = contextPath + "/component/component-function-data";
COMPONENT_CALLBACK_MODEL_URL = contextPath + "/component/component-callback";
COMPONENT_CALLBACK_PARAM_MODEL_URL = contextPath + "/component/component-callback-param";
CONSTRUCT_MODEL_URL = contextPath + "/construct/construct";
var componentLayout, componentRemarkDiv, currentTreeShowType;
/**
 * 初始化配置列表
 * @param {dhtmlxGrid} grid
 * @param {obj} gridData
 */
function initConfigGrid(grid, gridData) {
    grid.setImagePath(IMAGE_PATH);
    grid.setHeader(gridData.format.headers.toString());
    grid.setInitWidths(gridData.format.colWidths.toString());
    grid.setColTypes(gridData.format.colTypes.toString());
    grid.setColAlign(gridData.format.colAligns.toString());
    grid.setSkin(Skin);
    grid.init();
    grid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
}
/**
 * 列表加载数据
 * @param {dhtmlxGrid} grid
 * @param {string} url
 */
function loadConfigGridData(grid, url) {
    grid.clearAll();
    var dataJson = loadJson(url);
    var jsonArray = dataJson.data ? dataJson.data : dataJson;
    var datas = {};
    datas.rows = [];
    for(var i=0; i<jsonArray.length; i++) {
        var row = {};
        row.id = jsonArray[i][0];
        row.data = [];
        for (var j = 1; j < jsonArray[i].length; j++) {
            row.data[j-1] = jsonArray[i][j];
        }
        datas.rows[i] = row;
    }
    grid.parse(datas, "json");
}
// 构件导入时对应后台session中ComponentConfig的key
var componentConfigKey;
var componentAreaForm;
var componentAreaFormData = {
    format: [
        {type: "block", width: "350", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "parentId"},
            {type: "hidden", name: "showOrder"},
            {type: "hidden", name: "hasChild"},
            {type: "input", label: "构件分类名称：", labelWidth: "120", name: "name", maxLength:100, required: true, width: 200}
        ]}
    ],
    settings: {labelWidth: 80, inputWidth: 160}
};
var componentGridData = {
    format: {
        headers: ["<center>构件类名</center>", "<center>构件名称</center>", "<center>版本号</center>", "<center>导入日期</center>", "<center>类型</center>", "<center>前台</center>", "<center>按钮项</center>" ,"<center>菜单项</center>", "<center>绑定系统参数</center>", "<center>是否打包</center>", "<center>是否应用到本系统</center>",""],
        cols: ["component.name", "component.alias", "version", "importDate", "component.type", "views", "buttonUse", "menuUse", "systemParamConfig", "isPackage", "isSystemUsed"],
        userDatas: ["remark"],
        colWidths: ["150", "150", "50", "80", "80", "80", "50", "50", "100", "90", "80", "*"],
        colTypes: ["ro", "ro", "ro", "ro", "co", "ro", "ch", "ch", "co", "co", "co", "ro"],
        colAligns: ["left", "left", "left", "left", "left", "left", "center", "center", "left", "left", "left"],
        colTooltips: ["true", "true", "true", "true", "true", "true", "true", "true", "true", "false", "false", "false"]
    }
};
/**
 * 初始化构件分类表单工具条的方法
 * @param {dhtmlxToolbar} toolBar
 */
function initComponentAreaFormToolbar(toolBar) {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    toolBar.setAlign("right");
    toolBar.attachEvent("onClick", function(id) {
        if (id == "close") {
            dhxWins.window(WIN_ID).close();
        } else if (id == "save") {
            var id = componentAreaForm.getItemValue("id");
            var name = componentAreaForm.getItemValue("name");
            var parentId = componentAreaForm.getItemValue("parentId");
            var result = eval("(" + loadJson(COMPONENT_AREA_MODEL_URL + "!validateFields.json?id=" + id + "&name=" + encodeURIComponent(name) + "&parentId=" + parentId) + ")");
            if (result.nameExist) {
                dhtmlx.message(getMessage("form_field_exist", "构件分类名称"));
                return;
            }
            if (id == "") {
                SAVE_URL = COMPONENT_AREA_MODEL_URL;
                componentAreaForm.setItemValue("_method", "post");
            } else {
                SAVE_URL = COMPONENT_AREA_MODEL_URL + "/" + id;
                componentAreaForm.setItemValue("_method", "put");
            }
            componentAreaForm.send(SAVE_URL, "post", function(loader, response){
                dhxWins.window(WIN_ID).close();
                dhtmlx.message(getMessage("save_success"));
                if(id != ""){
                    var parentId = tree.getParentId(contextMenuNodeId);
                    tree.refreshItem(parentId);
                }else{
                    tree.refreshItem(contextMenuNodeId);
                }
            });
        }
    });
}
// 构件表校验的layout
var validateLayout;
var tableColumnGrid;
var columnGridData = {
        format: {
            headers: ["<center>原字段名称</center>", "<center>原字段类型</center>", "<center>原字段可为空</center>", "<center>原字段默认值</center>", "<center>原字段备注</center>", "<center>目标字段名称</center>", "<center>目标字段类型</center>", "<center>目标字段可为空</center>", "<center>目标字段默认值</center>", "<center>目标字段备注</center>"],
            colWidths: ["100", "100", "100", "100", "120", "100", "100", "100", "100", "120"],
            colTypes: ["ro", "ro", "co", "ro", "ro", "ro", "ro", "co", "ro", "ro"],
            colAligns: ["left", "left", "left", "left", "left", "left", "left", "left", "left", "left"]
        }
    };
/**
 * 构造构件相关表校验的layout
 */
function tableLayout() {
    validateLayout.cells('a').setText('本构件相关表字段和已存在的字段对比列表');
    tableColumnGrid = validateLayout.cells('a').attachGrid();
    var isNullCombo1 = tableColumnGrid.getCombo(2);
    isNullCombo1.put("0", "否");
    isNullCombo1.put("1", "是");
    var isNullCombo2 = tableColumnGrid.getCombo(7);
    isNullCombo2.put("0", "否");
    isNullCombo2.put("1", "是");
    initConfigGrid(tableColumnGrid, columnGridData);
    var tableToolbar = validateLayout.attachToolbar();
    tableToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    tableToolbar.addText("tableTxt", 0, "构件表:");
    tableToolbar.addText("tableCombo", 1, "<div id='tableCombo'></div>");
    tableToolbar.addText("tableCombo", 2, "<div id='tableMsg'></div>");
    var tableCombo = new dhtmlXCombo("tableCombo", "tableC", 200);
    var tableComboUrl = contextPath + "/component/component-table!getComponentTables.json?E_model_name=combo&F_in=name&componentConfigKey=" + componentConfigKey;
    var jsonObj = loadJson(tableComboUrl);
    if (jsonObj && jsonObj.data && jsonObj.data.length) {
        var opt_data = [];
        for (var m = 0; m < jsonObj.data.length; m++) {
            opt_data[m] = {
                text : jsonObj.data[m].name,
                value : jsonObj.data[m].name
            };
        }
        tableCombo.addOption(opt_data);
    }
    tableCombo.attachEvent("onChange", function() {
        var tableName = this.getComboText();
        dhtmlxAjax.get(contextPath + "/component/component-table!getComponentTableByName.json?tableName=" + tableName, function(loader) {
            var result = eval("(" + eval(loader.xmlDoc.responseText) + ")");
            if (result.exist) {
                var tableMsg = document.getElementById("tableMsg");
                if (tableMsg) {
                    tableMsg.innerHTML = "";
                }
                var tableId = result.id;
                var TABLE_COLUMN_QUERY_URL = contextPath + "/component/component-column!getComponentColumns.json?tableId=" + tableId + "&tableName=" + tableName + "&componentConfigKey=" + componentConfigKey;
                loadConfigGridData(tableColumnGrid, TABLE_COLUMN_QUERY_URL);
            } else {
                tableColumnGrid.clearAll();
                var tableMsg = document.getElementById("tableMsg");
                if (tableMsg) {
                    tableMsg.innerHTML = "<font color='red'>" + result.message + "</font>";
                }
            }
        });
    });
}
var selfDefineColumnGridData = {
        format: {
            headers: ["<center>原字段名称</center>", "<center>原字段类型</center>", "<center>原字段长度</center>", "<center>原字段默认值</center>", "<center>原字段备注</center>", "<center>目标字段名称</center>", "<center>目标字段类型</center>", "<center>目标字段长度</center>", "<center>目标字段默认值</center>", "<center>目标字段备注</center>"],
            colWidths: ["100", "100", "100", "100", "120", "100","100",  "100", "100", "120"],
            colTypes: ["ro", "co", "ro", "ro", "ro", "ro", "co", "ro", "ro", "ro"],
            colAligns: ["left", "left", "left", "left", "left", "left", "left", "left", "left", "left"]
        }
    };
/**
 * 构造自定义构件相关表校验的layout
 */
function selfDefineTableLayout() {
    validateLayout.cells('a').setText('本构件相关表字段和已存在的字段对比列表');
    tableColumnGrid = validateLayout.cells('a').attachGrid();
    var dataTypeCombo1 = tableColumnGrid.getCombo(1);
    dataTypeCombo1.put("c", "字符型");
    dataTypeCombo1.put("d", "日期型");
    dataTypeCombo1.put("n", "数字型");
    dataTypeCombo1.put("e", "编码型");
    dataTypeCombo1.put("u", "用户型");
    dataTypeCombo1.put("p", "部门型");
    var dataTypeCombo2 = tableColumnGrid.getCombo(6);
    dataTypeCombo2.put("c", "字符型");
    dataTypeCombo2.put("d", "日期型");
    dataTypeCombo2.put("n", "数字型");
    dataTypeCombo2.put("e", "编码型");
    dataTypeCombo2.put("u", "用户型");
    dataTypeCombo2.put("p", "部门型");
    initConfigGrid(tableColumnGrid, selfDefineColumnGridData);
    var tableToolbar = validateLayout.attachToolbar();
    tableToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    tableToolbar.addText("tableTxt", 0, "构件表:");
    tableToolbar.addText("tableCombo", 1, "<div id='tableCombo'></div>");
    tableToolbar.addText("tableCombo", 2, "<div id='tableMsg'></div>");
    var tableCombo = new dhtmlXCombo("tableCombo", "tableC", 200);
    var tableComboUrl = contextPath + "/appmanage/physical-table-define!getComponentTables.json?E_model_name=combo&F_in=tableName&componentConfigKey=" + componentConfigKey;
    var jsonObj = loadJson(tableComboUrl);
    if(jsonObj && jsonObj.data && jsonObj.data.length) {
        var opt_data = [];
        for (var m=0; m<jsonObj.data.length; m++) {
            opt_data[m] = {text: jsonObj.data[m].tableName, value: jsonObj.data[m].tableName};
        };
        tableCombo.addOption(opt_data);
    }
    tableCombo.attachEvent("onChange", function() {
        var tableName = this.getComboText();
        dhtmlxAjax.get(contextPath + "/appmanage/physical-table-define!getComponentTableByName.json?tableName=" + tableName, function(loader) {
            var result = eval("(" + eval(loader.xmlDoc.responseText) + ")");
            if (result.exist) {
                var tableMsg = document.getElementById("tableMsg");
                if (tableMsg) {
                    tableMsg.innerHTML = "";
                }
                var tableId = result.id;
                var TABLE_COLUMN_QUERY_URL = contextPath + "/appmanage/column-define!getComponentColumns.json?tableId=" + tableId + "&realTableName=" + tableName + "&componentConfigKey=" + componentConfigKey;
                loadConfigGridData(tableColumnGrid, TABLE_COLUMN_QUERY_URL);
            } else {
                tableColumnGrid.clearAll();
                var tableMsg = document.getElementById("tableMsg");
                if (tableMsg) {
                    tableMsg.innerHTML = "<font color='red'>" + result.message + "</font>";
                }
            }
        });
    });
}
/**
 * 导入构件
 * @param {obj} result
 */
function importComp(result) {
    if (result.existOldComponentVersion) {
        dhtmlx.confirm({
            type:"confirm",
            text: "该构件已经存在，是否覆盖？",
            ok: "是",
            cancel: "否",
            callback: function(flag) {
                if (flag) {
                    if (!result.validTables) {
                        dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!saveComponentVersion.json?componentConfigKey=" + componentConfigKey + "&areaId=" + currentTreeNodeId, function(loader) {
                            refreshComponentVersionGrid();
                            var result = eval("(" + loader.xmlDoc.responseText + ")");
                            if (typeof result == 'string') {
                                result = eval("(" + result + ")");
                            }
                            if (result.success) {
                                dhtmlx.message("构件导入成功，构件生效需要重启系统！");
                                if (result.systemParamConfig == "0") {
                                    dataGrid.selectRowById(result.id);
                                    configSystemParam();
                                }
                            } else {
                                dhtmlx.message(result.message);
                            }
                        });
                    } else {
                        var validateWin = dhxWins.createWindow("validateWin", 0, 0, 1000, 400);
                        validateWin.setModal(true);
                        validateWin.setText("构件预检-相关表预检");
                        validateWin.center();
                        var validateStatusBar = validateWin.attachStatusBar();
                        var validateToolbar = new dhtmlXToolbarObject(validateStatusBar);
                        validateToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
                        validateToolbar.setAlign("right");
                        validateToolbar.addButton("validatePass", 0, "&nbsp;&nbsp;继续导入&nbsp;&nbsp;");
                        validateToolbar.addSeparator("septr$01", 1);
                        validateToolbar.addButton("validateFailure", 2, "&nbsp;&nbsp;终止导入&nbsp;&nbsp;");
                        validateToolbar.attachEvent('onClick', function(id) {
                            if (id == "validatePass") {
                                var importUrl = COMPONENT_VERSION_MODEL_URL + "!saveComponentVersion.json?componentConfigKey=" + componentConfigKey + "&areaId=" + currentTreeNodeId;
                                dhtmlxAjax.get(importUrl, function(loader) {
                                    validateWin.close();
                                    refreshComponentVersionGrid();
                                    var result = eval("(" + loader.xmlDoc.responseText + ")");
                                    if (typeof result == 'string') {
                                        result = eval("(" + result + ")");
                                    }
                                    if (result.success) {
                                        dhtmlx.message("构件导入成功，构件生效需要重启系统！");
                                        if (result.systemParamConfig == "0") {
                                            dataGrid.selectRowById(result.id);
                                            configSystemParam();
                                        }
                                    } else {
                                        dhtmlx.message(result.message);
                                    }
                                });
                            } else {
                                dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!validateFailure.json?componentConfigKey=" + componentConfigKey, function(loader) {
                                    validateWin.close();
                                });
                            }
                        });
                        validateLayout = validateWin.attachLayout('1C');
                        if (result.isSelfDefineComponent) {
                            selfDefineTableLayout();
                        } else {
                            tableLayout();
                        }
                        validateWin.attachEvent("onClose", function(){
                            refreshComponentVersionGrid();
                            return true;
                        });
                    }
                }
            }
        });
    } else if (!result.validTables) {
        dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!saveComponentVersion.json?componentConfigKey=" + componentConfigKey + "&areaId=" + currentTreeNodeId, function(loader) {
            refreshComponentVersionGrid();
            var result = eval("(" + loader.xmlDoc.responseText + ")");
            if (typeof result == 'string') {
                result = eval("(" + result + ")");
            }
            if (result.success) {
                dhtmlx.message("构件导入成功，构件生效需要重启系统！");
                if (result.systemParamConfig == "0") {
                    dataGrid.selectRowById(result.id);
                    configSystemParam();
                }
            } else {
                dhtmlx.message(result.message);
            }
        });
    } else {
        var validateWin = dhxWins.createWindow("validateWin", 0, 0, 1000, 400);
        validateWin.setModal(true);
        validateWin.setText("构件预检-相关表预检");
        validateWin.center();
        var validateStatusBar = validateWin.attachStatusBar();
        var validateToolbar = new dhtmlXToolbarObject(validateStatusBar);
        validateToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
        validateToolbar.setAlign("right");
        validateToolbar.addButton("validatePass", 0, "&nbsp;&nbsp;继续导入&nbsp;&nbsp;");
        validateToolbar.addSeparator("septr$01", 1);
        validateToolbar.addButton("validateFailure", 2, "&nbsp;&nbsp;终止导入&nbsp;&nbsp;");
        validateToolbar.attachEvent('onClick', function(id) {
            if (id == "validatePass") {
                var importUrl = COMPONENT_VERSION_MODEL_URL + "!saveComponentVersion.json?componentConfigKey=" + componentConfigKey + "&areaId=" + currentTreeNodeId;
                dhtmlxAjax.get(importUrl, function(loader) {
                    validateWin.close();
                    refreshComponentVersionGrid();
                    var result = eval("(" + loader.xmlDoc.responseText + ")");
                    if (typeof result == 'string') {
                        result = eval("(" + result + ")");
                    }
                    if (result.success) {
                        dhtmlx.message("构件导入成功，构件生效需要重启系统！");
                        if (result.systemParamConfig == "0") {
                            dataGrid.selectRowById(result.id);
                            configSystemParam();
                        }
                    } else {
                        dhtmlx.message(result.message);
                    }
                });
            } else {
                dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!validateFailure.json?componentConfigKey=" + componentConfigKey, function(loader) {
                    validateWin.close();
                });
            }
        });
        validateLayout = validateWin.attachLayout('1C');
        if (result.isSelfDefineComponent) {
            selfDefineTableLayout();
        } else {
            tableLayout();
        }
        validateWin.attachEvent("onClose", function(){
            refreshComponentVersionGrid();
            return true;
        });
    }
}
var ST_form;
/**
 * 字段检索
 * @returns {dhtmlXForm}
 */
function initSearchColumn() {
    var sformJson;
    if (currentTreeShowType == "area") {
        sformJson = [
            {type: "combo", name: "componentType", className: "dhx_toolbar_form", label: "构件类型：", labelWidth: 80, labelAlign:"right", style:"font-size:11px;", width: 110, readonly:"true",
                 options:[{value:'all',text:'全部类型',selected:true},
                          {value:'develop',text:'　开发的构件'},
                          {value:'page',text:'　　页面构件'},
                          {value:'logic',text:'　　逻辑构件'},
                          {value:'common',text:'　　公用构件'},
                          {value:'selfdefine',text:'　自定义构件'},
                          {value:'tree',text:'　　树构件'},
                          {value:'physical-table',text:'　　物理表构件'},
                          {value:'logic-table',text:'　　逻辑表构件'},
                          {value:'common-table',text:'　　通用表构件'},
                          {value:'tab',text:'　　标签页构件'},
                          {value:'assemble',text:'　组合构件'}]
            },
            {type: "newcolumn"},
            {type: "checkbox", label: "显示子分类下构件：", name: "showType", checked: false, position:"label-left", labelAlign:"right", labelWidth: 110},
            {type: "newcolumn"},
            {type: "input",label: "构件类名或名称：", name: "value", className: "dhx_toolbar_form", width:100, inputHeight:17}
        ];
    } else {
        sformJson = [
            {type: "input",label: "构件类名或名称：", name: "value", className: "dhx_toolbar_form", width:100, inputHeight:17}
        ];
    }
    var form = new dhtmlXForm("top$searchTextdiv", sformJson);
    var scInp = form.getInput("value");
    scInp.onfocus = function() {
        form.setItemValue("value", "");
    };
    scInp.onkeydown = function(e) {
        e = e || window.event;
        var keyCode = e.keyCode || e.which;
        if (13 == keyCode) {
            condtionSearch(form);
        }
    };
    form.attachEvent("onChange", function(id, value) {
        if (id == "componentType" || id == "showType") {
            condtionSearch(form);
        }
    });
    return form;
}
/**
 * 条件查询
 * @param form
 */
function condtionSearch(form) {
    var value = form.getItemValue("value"), componentType = form.getItemValue("componentType"), showType = form.getItemValue("showType"), param = "";
    value = encodeURIComponent(value);
    if (value !== "") {
        if (value.match(/^\w+$/)) {
            param = "Q_LIKE_component.name=" + value;
        } else {
            param = "Q_LIKE_component.alias=" + value;
        }
    }
    if (componentType) {
        var componentTypeCondition = "";
        if (componentType == 'develop') {
            componentTypeCondition = "Q_LT_component.type=3";
        } else if (componentType == 'selfdefine') {
            componentTypeCondition = "Q_GT_component.type=2&Q_LT_component.type=8";
        } else if (componentType == 'assemble') {
            componentTypeCondition = "Q_EQ_component.type=9";
        } else if (componentType == 'common') {
            componentTypeCondition = "Q_EQ_component.type=0";
        } else if (componentType == 'page') {
            componentTypeCondition = "Q_EQ_component.type=1";
        } else if (componentType == 'logic') {
            componentTypeCondition = "Q_EQ_component.type=2";
        } else if (componentType == 'tree') {
            componentTypeCondition = "Q_EQ_component.type=3";
        } else if (componentType == 'physical-table') {
            componentTypeCondition = "Q_EQ_component.type=4";
        } else if (componentType == 'logic-table') {
            componentTypeCondition = "Q_EQ_component.type=5";
        } else if (componentType == 'common-table') {
            componentTypeCondition = "Q_EQ_component.type=6";
        } else if (componentType == 'tab') {
            componentTypeCondition = "Q_EQ_component.type=7";
        }
        if (param) {
            param += "&" + componentTypeCondition;
        } else {
            param = componentTypeCondition;
        }
    }
    if (showType) {
        if (param) {
            param += "&Q_LIKE_areaPath=" + currentTreeNodeId;
        } else {
            param = "Q_LIKE_areaPath=" + currentTreeNodeId;
        }
    }
    refreshComponentVersionGrid(param);
}
/**
 * 初始化构件列表工具条
 */
function initSelfToolBar() {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    var toolBarItemIndex = 0;
    toolBar.addButton("import", toolBarItemIndex++, "导入构件", "upload.gif");
    toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    //toolBar.addButton("config", toolBarItemIndex++, "绑定系统参数", "binding.gif");
    //toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    //toolBar.addButton("configRelation", toolBarItemIndex++, "配置关联信息", "binding.gif");
    //toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    //toolBar.addButton("configCommonCompRelation", toolBarItemIndex++, "关联公用构件", "binding.gif");
    //toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    toolBar.addButton("delete", toolBarItemIndex++, "删除", "delete.gif");
    toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    if (!releasedSystem) {
        var previewOpts = [['preview', 'obj', '&nbsp;发布预览', "preview.gif"]];
        toolBar.addButtonSelect("repeatPreview", toolBarItemIndex++, "本地预览", previewOpts, "preview.gif");
    } else {
        toolBar.addButton("preview", toolBarItemIndex++, "预览", "preview.gif");
    }
    toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    toolBar.addButton("changeArea", toolBarItemIndex++, "更改分类", "update.gif");
    toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    //toolBar.addButton("package", toolBarItemIndex++, "打包", "package.gif");
    //toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    //toolBar.addButton("download", toolBarItemIndex++, "下载", "download.gif");
    //toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    //toolBar.addButton("viewBindingMenu", toolBarItemIndex++, "查看关联菜单", "view.gif");
    //toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    //toolBar.addButton("systemUse", toolBarItemIndex++, "应用到本系统", "sysuse.gif");
    //toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    //toolBar.addButton("refresh", toolBarItemIndex++, "刷新", "refresh.gif");
    //toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    //toolBar.addButton("scan", toolBarItemIndex++, "扫描更新", "refresh.gif");
    //toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    var listButtons = [
        ['package', 'obj', '打包', "package.gif"],
        ['download', 'obj', '下载', "download.gif"],
        //['refresh', 'obj', '刷新', "refresh.gif"],
        ['config', 'obj', '绑定系统参数', "binding.gif"],
        ['systemUse', 'obj', '应用到本系统', "sysuse.gif"],
        ['configRelation', 'obj', '配置关联信息', "binding.gif"],
        ['configCommonCompRelation', 'obj', '关联公用构件', "binding.gif"],
        ['viewBindingMenu', 'obj', '查看关联菜单', "view.gif"],
        ['scan', 'obj', '扫描更新', "refresh.gif"]
    ];
    toolBar.addButtonSelect("more", toolBarItemIndex++, "更多", listButtons, "release.gif", null, "disabled", true);
    
    toolBar.addButton("sous", toolBarItemIndex++, "", "search.gif", null, "right");
    toolBar.addDiv("top$searchTextdiv", toolBarItemIndex++, "right");
    ST_form = initSearchColumn();
    toolBar.attachEvent('onClick', function(id) {
        if (id == "import") {
            var current = new Date();
            componentConfigKey = current.getTime() + "_" + Math.floor(Math.random()*100);
            GWIN_WIDTH = 450;
            GWIN_HEIGHT = 280;
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            dataWin = dhxWins.createWindow(WIN_ID, 0, 0, GWIN_WIDTH, GWIN_HEIGHT);
            dataWin.setModal(true);
            dataWin.setText("构件导入");
            dataWin.center();
            dataWin.button('park').hide();
            dataWin.button('minmax1').hide();
            dataWin.denyResize();
            var vaultDiv = document.createElement("div");
            vaultDiv.setAttribute("id", "vaultDiv");
            document.body.appendChild(vaultDiv);
            var UPLOAD_URL = COMPONENT_VERSION_MODEL_URL + "!uploadHandler";
            var GET_INFO_URL = COMPONENT_VERSION_MODEL_URL + "!getInfoHandler";
            var GET_ID_URL = COMPONENT_VERSION_MODEL_URL + "!getIdHandler";
            var vault = new dhtmlXVaultObject();
            vault.setImagePath(DHX_RES_PATH + "/common/css/imgs/");
            vault.setServerHandlers(UPLOAD_URL, GET_INFO_URL, GET_ID_URL);
            vault.setFilesLimit(1);
            vault.strings.btnAdd = "添加构件";
            vault.strings.btnUpload = "上传";
            vault.strings.btnClean = "清空";
            vault.strings.remove = "删除";
            vault.strings.done = "完成";
            vault.strings.error = "错误"; 
            vault.onAddFile = function(fileName) { 
                var ext = this.getFileExtension(fileName); 
                if (ext.toLowerCase() != "zip") {
                    dhtmlx.message("只能上传ZIP文件！");
                    return false;
                } else {
                    return true;
                }
            };
            vault.onFileUploaded = function(file) { 
                dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!getUploadMessage.json?componentConfigKey=" + componentConfigKey, function(loader) {
                    dataWin.close();
                    var result = eval("(" + eval(loader.xmlDoc.responseText) + ")");
                    if (!result.success) {
                        dhtmlx.message(result.message);
                    } else if (result.validVersion) {
                        dhtmlx.message("公用构件和自定义构件只支持V1.0版本！");
                    }/* else if (result.componentVersionUsed) {
                        dhtmlx.message("该构件已经存在且已经被使用，不能被覆盖，请导入新的版本！");
                    }*/ else if (result.validWorkflow) {
                        dhtmlx.confirm({
                            type:"confirm",
                            text: "构件包含工作流，导入前需要手工导入工作流文件，工作流文件是否已经导入？",
                            ok: "是",
                            cancel: "否",
                            callback: function(flag) {
                                if (flag) {
                                    importComp(result);
                                }
                            }
                        });
                    } else {
                        importComp(result);
                    }
                });
            };
            vault.create("vaultDiv");
            vault.setFormField("componentConfigKey", componentConfigKey);
            dataWin.attachObject(vaultDiv);
        } else if (id == "config") {
            configSystemParam();
        } else if (id == "configRelation") {
            configRelationInfo();
        } else if (id == "configCommonCompRelation") {
            var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            if (selectId.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            var componentType = dataGrid.cells(selectId, 4).getValue();
            if (componentType == "9") {
                dhtmlx.message("组合构件不能关联公用构件！");
                return;
            }
            configCommonComponentRelation();
        } else if (id == "preview") {
            var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            if (selectId.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            var componentType = dataGrid.cells(selectId, 4).getValue();
            var componentViews = dataGrid.cells(selectId, 5).getValue();
            if (componentType == "0") {
                dhtmlx.message("公用构件不能预览！");
            } else if (componentType == "9") {
                if (!dhxWins) {
                    dhxWins = new dhtmlXWindows();
                }
                var msgWin = dhxWins.createWindow("msgWin", 0, 0, 200, 50);
                msgWin.setModal(true);
                msgWin.hideHeader();
                msgWin.center();
                msgWin.button("park").hide();
                msgWin.button("minmax1").hide();
                msgWin.button("minmax2").hide();
                msgWin.button("close").hide();
                var obj = document.getElementById("DIV-msg");
                if (obj == null) {
                    obj = document.createElement("DIV");
                    obj.setAttribute("id", "DIV-msg");
                    obj.innerHTML = "正在处理，请稍后...";
                    obj.setAttribute("style", "position:relative;top:35%;left:20%;font-size:11px;");
                    document.body.appendChild(obj);
                }
                msgWin.attachObject(obj);
                dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!preview.json?assembleComponentVersionId=" + selectId, function(loader) {
                    var result = eval("(" + loader.xmlDoc.responseText + ")");
                    if (typeof result == 'string') {
                        result = eval("(" + result + ")");
                    }
                    if (result.status == false) {
                        dhtmlx.message(result.message);
                    } else if (result.status == true) {
                        var componentUrl = result.url;
                        if (componentViews == "coral40") {
                            if (componentUrl.indexOf(previewSystemPath) != -1) {
                                componentUrl = componentUrl.substring(previewSystemPath.length);
                                var w = window.open(previewSystemPath + "/cfg-resource/coral40/views/config/preview.jsp?CFG_componentUrl=" + componentUrl,
                                    'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                                w.focus();
                            } else {
                                componentUrl = componentUrl.substring(contextPath.length);
                                var w = window.open(contextPath + "/cfg-resource/coral40/views/config/preview.jsp?CFG_componentUrl=" + componentUrl,
                                    'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                                w.focus();
                            }
                        } else {
                            var w = window.open(componentUrl, 'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height=" + (screen.availHeight - 50) + ",width=" + (screen.availWidth - 10) + ",status=yes");
                            w.focus();
                        }
                    } else {
                        dhtmlx.message("预览失败！");
                    }
                    msgWin.close();
                });
            } else if (componentType == "1") {
                if (!dhxWins) {
                    dhxWins = new dhtmlXWindows();
                }
                var msgWin = dhxWins.createWindow("msgWin", 0, 0, 200, 50);
                msgWin.setModal(true);
                msgWin.hideHeader();
                msgWin.center();
                msgWin.button("park").hide();
                msgWin.button("minmax1").hide();
                msgWin.button("minmax2").hide();
                msgWin.button("close").hide();
                var obj = document.getElementById("DIV-msg");
                if (obj == null) {
                    obj = document.createElement("DIV");
                    obj.setAttribute("id", "DIV-msg");
                    obj.innerHTML = "正在处理，请稍后...";
                    obj.setAttribute("style", "position:relative;top:35%;left:20%;font-size:11px;");
                    document.body.appendChild(obj);
                }
                msgWin.attachObject(obj);
                dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!preview.json?componentVersionId=" + selectId, function(loader){
                    var result = eval("(" + loader.xmlDoc.responseText + ")");
                    if (typeof result == 'string') {
                        result = eval("(" + result + ")");
                    }
                    if (result.status == false) {
                        dhtmlx.message(result.message);
                    } else if (result.status == true) {
                        var componentUrl = result.url;
                        if (componentViews == "coral40") {
                            if (componentUrl.indexOf(previewSystemPath) != -1) {
                                componentUrl = componentUrl.substring(previewSystemPath.length);
                                var w = window.open(previewSystemPath + "/cfg-resource/coral40/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()) + "&previewUrl=" + componentUrl,
                                    'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                                w.focus();
                            } else {
                                componentUrl = componentUrl.substring(contextPath.length);
                                var w = window.open(contextPath + "/cfg-resource/coral40/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()) + "&previewUrl=" + componentUrl,
                                    'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                                w.focus();
                            }
                        } else {
                            var w = window.open(DHX_RES_PATH + "/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()) + "&previewUrl=" + componentUrl,
                                'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                            w.focus();
                        }
                    } else {
                        dhtmlx.message("预览失败！");
                    }
                    msgWin.close();
                });
            } else if (componentType == "2") {
                var w = window.open(DHX_RES_PATH + "/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()),
                    'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                w.focus();
            } else if (componentType == "3" || componentType == "4" || componentType == "5" || componentType == "6" || componentType == "7") {
                if (!dhxWins) {
                    dhxWins = new dhtmlXWindows();
                }
                var msgWin = dhxWins.createWindow("msgWin", 0, 0, 200, 50);
                msgWin.setModal(true);
                msgWin.hideHeader();
                msgWin.center();
                msgWin.button("park").hide();
                msgWin.button("minmax1").hide();
                msgWin.button("minmax2").hide();
                msgWin.button("close").hide();
                var obj = document.getElementById("DIV-msg");
                if (obj == null) {
                    obj = document.createElement("DIV");
                    obj.setAttribute("id", "DIV-msg");
                    obj.innerHTML = "正在处理，请稍后...";
                    obj.setAttribute("style", "position:relative;top:35%;left:20%;font-size:11px;");
                    document.body.appendChild(obj);
                }
                msgWin.attachObject(obj);
                dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!previewSelfDefine.json?componentVersionId=" + selectId, function(loader){
                    var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                    var result = eval("(" + jsonObj + ")");
                    if (result.status == false) {
                        dhtmlx.message(result.message);
                    } else if (result.status == true) {
                        var componentUrl = result.url;
                        if (componentViews == "coral40") {
                            if (componentUrl.indexOf(previewSystemPath) != -1) {
                                componentUrl = componentUrl.substring(previewSystemPath.length);
                                var w = window.open(previewSystemPath + "/cfg-resource/coral40/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()) + "&previewUrl=" + componentUrl,
                                    'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                                w.focus();
                            } else {
                                componentUrl = componentUrl.substring(contextPath.length);
                                var w = window.open(contextPath + "/cfg-resource/coral40/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()) + "&previewUrl=" + componentUrl,
                                    'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                                w.focus();
                            }
                        } else {
                            var w = window.open(DHX_RES_PATH + "/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()) + "&previewUrl=" + componentUrl,
                                'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                            w.focus();
                        }
                    } else {
                        dhtmlx.message("预览失败！");
                    }
                    msgWin.close();
                });
            }
        } else if (id == "repeatPreview") {
            var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            if (selectId.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            var componentType = dataGrid.cells(selectId, 4).getValue();
            var componentViews = dataGrid.cells(selectId, 5).getValue();
            if (componentType == "0") {
                dhtmlx.message("公用构件不能预览！");
            } else if (componentType == "9") {
                dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!repeatPreview.json?assembleComponentVersionId=" + selectId, function(loader) {
                    var result = eval("(" + loader.xmlDoc.responseText + ")");
                    if (typeof result == 'string') {
                        result = eval("(" + result + ")");
                    }
                    var componentUrl = result.url;
                    if (componentViews == "coral40") {
                        if (componentUrl.indexOf(previewSystemPath) != -1) {
                            componentUrl = componentUrl.substring(previewSystemPath.length);
                            var w = window.open(previewSystemPath + "/cfg-resource/coral40/views/config/preview.jsp?CFG_componentUrl=" + componentUrl,
                                'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                            w.focus();
                        } else {
                            componentUrl = componentUrl.substring(contextPath.length);
                            var w = window.open(contextPath + "/cfg-resource/coral40/views/config/preview.jsp?CFG_componentUrl=" + componentUrl,
                                'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                            w.focus();
                        }
                    } else {
                        var w = window.open(componentUrl, 'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height=" + (screen.availHeight - 50) + ",width=" + (screen.availWidth - 10) + ",status=yes");
                        w.focus();
                    }
                });
            } else if (componentType == "1") {
                dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!repeatPreview.json?componentVersionId=" + selectId, function(loader){
                    var result = eval("(" + loader.xmlDoc.responseText + ")");
                    if (typeof result == 'string') {
                        result = eval("(" + result + ")");
                    }
                    if (result.status == false) {
                        dhtmlx.message(result.message);
                    } else if (result.status == true) {
                        var componentUrl = result.url;
                        if (componentViews == "coral40") {
                            if (componentUrl.indexOf(previewSystemPath) != -1) {
                                componentUrl = componentUrl.substring(previewSystemPath.length);
                                var w = window.open(previewSystemPath + "/cfg-resource/coral40/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()) + "&previewUrl=" + componentUrl,
                                    'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                                w.focus();
                            } else {
                                componentUrl = componentUrl.substring(contextPath.length);
                                var w = window.open(contextPath + "/cfg-resource/coral40/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()) + "&previewUrl=" + componentUrl,
                                    'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                                w.focus();
                            }
                        } else {
                            var w = window.open(DHX_RES_PATH + "/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()) + "&previewUrl=" + componentUrl,
                                'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                            w.focus();
                        }
                    } else {
                        dhtmlx.message("预览失败！");
                    }
                });
            } else if (componentType == "2") {
                var w = window.open(DHX_RES_PATH + "/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()),
                    'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                w.focus();
            } else if (componentType == "3" || componentType == "4" || componentType == "5" || componentType == "6" || componentType == "7") {
                if (!dhxWins) {
                    dhxWins = new dhtmlXWindows();
                }
                var msgWin = dhxWins.createWindow("msgWin", 0, 0, 200, 50);
                msgWin.setModal(true);
                msgWin.hideHeader();
                msgWin.center();
                msgWin.button("park").hide();
                msgWin.button("minmax1").hide();
                msgWin.button("minmax2").hide();
                msgWin.button("close").hide();
                var obj = document.getElementById("DIV-msg");
                if (obj == null) {
                    obj = document.createElement("DIV");
                    obj.setAttribute("id", "DIV-msg");
                    obj.innerHTML = "正在处理，请稍后...";
                    obj.setAttribute("style", "position:relative;top:35%;left:20%;font-size:11px;");
                    document.body.appendChild(obj);
                }
                msgWin.attachObject(obj);
                dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!previewSelfDefine.json?componentVersionId=" + selectId, function(loader){
                    var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                    var result = eval("(" + jsonObj + ")");
                    msgWin.close();
                    if (result.status == false) {
                        dhtmlx.message(result.message);
                    } else if (result.status == true) {
                        var componentUrl = result.url;
                        if (componentViews == "coral40") {
                            if (componentUrl.indexOf(previewSystemPath) != -1) {
                                componentUrl = componentUrl.substring(previewSystemPath.length);
                                var w = window.open(previewSystemPath + "/cfg-resource/coral40/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()) + "&previewUrl=" + componentUrl,
                                    'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                                w.focus();
                            } else {
                                componentUrl = componentUrl.substring(contextPath.length);
                                var w = window.open(contextPath + "/cfg-resource/coral40/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()) + "&previewUrl=" + componentUrl,
                                    'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                                w.focus();
                            }
                        } else {
                            var w = window.open(DHX_RES_PATH + "/views/config/component/preview.jsp?componentVersionId=" + selectId + "&componentType=" + componentType + "&componentAlias=" + encodeURIComponent(dataGrid.cells(selectId, 1).getValue()) + "&previewUrl=" + componentUrl,
                                'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                            w.focus();
                        }
                    } else {
                        dhtmlx.message("预览失败！");
                    }
                });
            }
        } else if (id == "delete") {
            var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            var componentType = dataGrid.cells(selectIds, 4).getValue();
            if (componentType == "9") {
                dhtmlx.confirm({
                    type : "confirm",
                    text : getMessage("delete_warning"),
                    ok : "确定",
                    cancel : "取消",
                    callback : function(flag) {
                        if (flag) {
                            dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!deleteValid.json?componentVersionId=" + selectIds, function(loader) {
                                var result = eval("(" + eval(loader.xmlDoc.responseText) + ")");
                                if (!result.success) {
                                    dhtmlx.message(result.message);
                                } else {
                                    dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!deleteComponentVersion.json?componentVersionId=" + selectIds, function(loader) {
                                        var result = eval(loader.xmlDoc.responseText);
                                        if (result != "") {
                                            dhtmlx.message(result);
                                        }
                                        refreshComponentVersionGrid();
                                        tree.refreshItem(currentTreeNodeId);
                                    });
                                }
                            });
                        }
                    }
                });
            } else {
                dhtmlx.confirm({
                    type:"confirm",
                    text: getMessage("delete_warning"),
                    ok: "确定",
                    cancel: "取消",
                    callback: function(flag) {
                        if (flag) {
                            dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!deleteValid.json?componentVersionId=" + selectIds, function(loader) {
                                var result = eval("(" + eval(loader.xmlDoc.responseText) + ")");
                                if (!result.success) {
                                    dhtmlx.message(result.message);
                                } else {
                                    dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!deleteComponentVersion.json?componentVersionId=" + selectIds, function(loader) {
                                        var result = eval(loader.xmlDoc.responseText);
                                        if (result != "") {
                                            dhtmlx.message(result);
                                        }
                                        refreshComponentVersionGrid();
                                    });
                                }
                            });
                        }
                    }
                });
            }
        } else if (id == "changeArea") {
            var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var areaWin = dhxWins.createWindow("areaWin", 0, 0, 500, 400);
            areaWin.setModal(true);
            areaWin.setText("更改分类");
            areaWin.center();
            areaWin.button("park").hide();
            areaWin.button("minmax1").hide();
            areaWin.button("minmax2").hide();
            
            var areaTree = areaWin.attachTree();
            areaTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
            areaTree.attachEvent("onMouseIn", function(id) {
                areaTree.setItemStyle(id, "background-color:#D5E8FF;");
            });
            areaTree.attachEvent("onMouseOut", function(id) {
                areaTree.setItemStyle(id, "background-color:#FFFFFF;");
            });
            areaTree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
            areaTree.setDataMode("json");
            areaTree.enableSmartXMLParsing(true);
            areaTree.loadJSONObject(eval("(" + loadJson(COMPONENT_AREA_MODEL_URL+"!getAreaTree.json?currentAreaId="+currentTreeNodeId) + ")"));
            var areaStatusBar = areaWin.attachStatusBar();
            var areaToolBar = new dhtmlXToolbarObject(areaStatusBar);
            areaToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
            areaToolBar.addButton("submit", 1, "&nbsp;&nbsp;确定&nbsp;&nbsp;");
            areaToolBar.addButton("cancle", 2, "&nbsp;&nbsp;取消&nbsp;&nbsp;");
            areaToolBar.setAlign("right");
            areaToolBar.attachEvent('onClick', function(id) {
                if (id == "submit") {
                    var selectAreaId = areaTree.getSelectedItemId();
                    if (!selectAreaId) {
                        dhtmlx.message("请先选择构件分类！");
                        return;
                    } else if (selectAreaId == currentTreeNodeId) {
                        dhtmlx.message("该构件已经在当前分类中，请选择其他分类！");
                        return;
                    } else if (selectAreaId == -1) {
                        dhtmlx.message("请选择其他分类！");
                        return;
                    }
                    dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL+"!changeArea.json?componentVersionIds="+selectIds+"&areaId="+selectAreaId, function(loader) {
                        var result = eval(loader.xmlDoc.responseText);
                        if (result != "") {
                            dhtmlx.message(result);
                        }
                        areaWin.close();
                        refreshComponentVersionGrid();
                    });
                } else {
                    areaWin.close();
                }
            });
        } else if (id == "package") {
            var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            var componentType = dataGrid.cells(selectIds, 4).getValue();
            if (componentType != 3 && componentType != 4 && componentType != 5 && componentType != 9) {
                dhtmlx.message("只能打包树构件、物理表构件、逻辑表和组合构件！");
                return;
            }
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var msgWin = dhxWins.createWindow("msgWin", 0, 0, 200, 50);
            msgWin.setModal(true);
            msgWin.hideHeader();
            msgWin.center();
            msgWin.button("park").hide();
            msgWin.button("minmax1").hide();
            msgWin.button("minmax2").hide();
            msgWin.button("close").hide();
            var obj = document.getElementById("DIV-msg");
            if (obj == null) {
                obj = document.createElement("DIV");
                obj.setAttribute("id", "DIV-msg");
                obj.innerHTML = "正在处理，请稍后...";
                obj.setAttribute("style", "position:relative;top:35%;left:20%;font-size:11px;");
                document.body.appendChild(obj);
            }
            msgWin.attachObject(obj);
            if (componentType == 9) {
                dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!packageComponent.json?id=" + selectIds, function(loader){
                    var result = eval("(" + loader.xmlDoc.responseText + ")");
                    if (typeof result == 'string') {
                        result = eval("(" + result + ")");
                    }
                    if (result.success) {
                        dhtmlx.message(result.message);
                    } else {
                        dhtmlx.alert(result.message);
                    }
                    refreshComponentVersionGrid();
                    msgWin.close();
                });
            } else {
                dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!packageComponent.json?id=" + dataGrid.getSelectedRowId(), function(loader){
                    var result = eval("(" + loader.xmlDoc.responseText + ")");
                    if (result != "") {
                        dhtmlx.message(result);
                        if ("打包成功！" == result) {
                            dataGrid.cells(dataGrid.getSelectedRowId(), 9).setValue("1");
                        }
                    }
                    //refreshComponentVersionGrid();
                    msgWin.close();
                });
            }
        } else if (id == "download") {
            var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            } else if (dataGrid.cells(selectIds, 9).getValue() == "0") {
                dhtmlx.message("该构件未打包，请先打包！");
                return;
            }
            download(COMPONENT_VERSION_MODEL_URL + "!downloadComponent.json?id=" + dataGrid.getSelectedRowId());
        } else if (id == "viewBindingMenu") {
            var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var moduleWin = dhxWins.createWindow("moduleWin", 0, 0, 500, 400);
            moduleWin.setModal(true);
            moduleWin.setText("查看关联菜单");
            moduleWin.center();
            moduleWin.button("park").hide();
            moduleWin.button("minmax1").hide();
            moduleWin.button("minmax2").hide();
            var bindingMenuTree = moduleWin.attachTree();
            bindingMenuTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
            bindingMenuTree.setDataMode("json");
            bindingMenuTree.enableCheckBoxes(1);
            bindingMenuTree.enableSmartXMLParsing(true);
            bindingMenuTree.loadJSONObject(eval("(" + loadJson(COMPONENT_VERSION_MODEL_URL+"!getMenuTree.json?componentVersionId=" + selectIds) + ")"));
            bindingMenuTree.showItemCheckbox(1, 0);
        } else if (id == "systemUse") {
            var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            } else if (dataGrid.cells(selectIds, 9).getValue() == "1") {
                dhtmlx.message("该构件已经应用到本系统！");
                return;
            }
            var result = loadJson(COMPONENT_VERSION_MODEL_URL+"!systemUse.json?componentVersionId=" + selectIds);
            if (typeof result == 'string') {
                result = eval("(" + result + ")");
            }
            if (result.success) {
                refreshComponentVersionGrid();
            }
            dhtmlx.message(result.message);
        } else if (id == "refresh") {
            refreshComponentVersionGrid();
        } else if (id == "scan") {
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var msgWin = dhxWins.createWindow("msgWin", 0, 0, 200, 50);
            msgWin.setModal(true);
            msgWin.hideHeader();
            msgWin.center();
            msgWin.button("park").hide();
            msgWin.button("minmax1").hide();
            msgWin.button("minmax2").hide();
            msgWin.button("close").hide();
            var obj = document.getElementById("DIV-msg");
            if (obj == null) {
                obj = document.createElement("DIV");
                obj.setAttribute("id", "DIV-msg");
                obj.innerHTML = "正在处理，请稍后...";
                obj.setAttribute("style", "position:relative;top:35%;left:20%;font-size:11px;");
                document.body.appendChild(obj);
            }
            msgWin.attachObject(obj);
            dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!scanComponentLib.json?areaId=" + currentTreeNodeId, function(loader){
                var result = eval(loader.xmlDoc.responseText);
                if (result != "") {
                    dhtmlx.alert(result);
                }
                refreshComponentVersionGrid();
                msgWin.close();
            });
        } else if (id == "sous") {
            condtionSearch(ST_form);
        }
    });
}
/**
 * 创建构件生产库模块说明
 * @return {div}
 */
function createHelpDiv() {
    var obj = document.getElementById("DIV-help");
    if (null == obj) {
        obj = document.createElement("DIV");
        obj.setAttribute("id", "DIV-help");
        obj.setAttribute("style", "font-family: Tahoma; font-size: 11px;display: none;");
        obj.innerHTML = "<ul> \n"
            + "<li type=\"square\">"
            + "<p><b>构件树操作说明：</b><br></p> \n"
            + "<p>1. 根节点下<br></p> \n"
            + "<p>&nbsp;&nbsp;&nbsp;&nbsp;1.1 可以新增构件分类节点<br></p> \n"
            + "<p>2. 在构件分类节点下<br></p> \n"
            + "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.1 可以新增构件分类节点<br></p> \n"
            + "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.1 可以修改构件分类节点<br></p> \n"
            + "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.1 可以删除构件分类节点<br></p> \n"
            + "</li> \n"
            + "</ul> \n"
            + "<ul> \n"
            + "<li type=\"square\">"
            + "<p><b>构件树操作步骤：</b><br></p> \n"
            + "<p>1. 【新增】选择一个节点，右键->弹出右键菜单->选择“新增构件分类”，则在该节点下新增一个构件分类<br></p> \n"
            + "<p>2. 【修改】选择一个节点，右键->弹出右键菜单->选择“修改构件分类”<br></p> \n"
            + "<p>3. 【删除】选择一个节点，右键->弹出右键菜单->选择“删除构件分类”，若该构件分类下有子分类或有构件，则不能删除<br></p> \n"
            + "<p>4. 点击根节点时，右侧页面是操作说明<br></p> \n"
            + "</li> \n"
            + "</ul> \n";
    }
    return obj;
}
/**
 * 创建构件说明
 * @return {div}
 */
function createComponentRemarkDiv() {
    var obj = document.getElementById("DIV-component-remark");
    if (null == obj) {
        obj = document.createElement("DIV");
        obj.setAttribute("id", "DIV-component-remark");
        obj.setAttribute("style", "font-family: Tahoma; font-size: 11px;display: none;");
        obj.innerHTML = "";
    }
    return obj;
}
/**
 * 刷新构件列表
 */
function refreshComponentVersionGrid(param) {
    QUERY_URL = COMPONENT_VERSION_MODEL_URL + "!search.json?P_orders=componentName,version";
    if (currentTreeNodeId == 'develop') {
        QUERY_URL = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_GT_component.type=-1&Q_LT_component.type=3&P_orders=componentName,version";
    } else if (currentTreeNodeId == 'selfdefine') {
        QUERY_URL = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_GT_component.type=2&Q_LT_component.type=8&P_orders=componentName,version";
    } else if (currentTreeNodeId == 'assemble') {
        QUERY_URL = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_EQ_component.type=9&P_orders=componentName,version";
    } else if (currentTreeNodeId == 'common') {
        QUERY_URL = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_EQ_component.type=0&P_orders=componentName,version";
    } else if (currentTreeNodeId == 'page') {
        QUERY_URL = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_EQ_component.type=1&P_orders=componentName,version";
    } else if (currentTreeNodeId == 'logic') {
        QUERY_URL = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_EQ_component.type=2&P_orders=componentName,version";
    } else if (currentTreeNodeId == 'tree') {
        QUERY_URL = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_EQ_component.type=3&P_orders=componentName,version";
    } else if (currentTreeNodeId == 'physical-table') {
        QUERY_URL = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_EQ_component.type=4&P_orders=componentName,version";
    } else if (currentTreeNodeId == 'logic-table') {
        QUERY_URL = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_EQ_component.type=5&P_orders=componentName,version";
    } else if (currentTreeNodeId == 'common-table') {
        QUERY_URL = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_EQ_component.type=6&P_orders=componentName,version";
    } else if (currentTreeNodeId == 'tab') {
        QUERY_URL = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_EQ_component.type=7&P_orders=componentName,version";
    } else {
        var showType = ST_form.getItemValue("showType");
        if (!showType) {
            QUERY_URL += "&Q_EQ_areaId=" + tree.getSelectedItemId();
        }
    }
    if (param) {
        QUERY_URL += "&" + param;
    } else {
        if (ST_form) {
            if (ST_form.isItem("componentType")) {
                ST_form.setItemValue("componentType", "all");
            }
            if (ST_form.isItem("showType")) {
                ST_form.setItemValue("showType", "0");
            }
            ST_form.setItemValue("value", "");
        }
    }
    search();
    if (componentRemarkDiv) {
        componentRemarkDiv.innerHTML = "";
    }
}
/**
 * 页面初始化方法
 */
function init() {
    dhxLayout = new dhtmlXLayoutObject("content", "2U");
    dhxLayout.cells("a").hideHeader();
    dhxLayout.cells("b").hideHeader();
    dhxLayout.cells("a").setWidth(240);
    dhxLayout.setAutoSize();
    var aToolbar = dhxLayout.cells("a").attachToolbar();
    aToolbar.setIconPath(IMAGE_PATH);
    aToolbar.addDiv("form$pos", 0);
    initTreeShowType();
    initAreaTree();
}
function initTreeShowType() {
    var formJson = [{
        type : "combo",
        name : "treeStruct",
        className : "dhx_toolbar_form",
        style : "font-size:11px;",
        width : 160,
        options : [{
            value : "area",
            text : "以构件分类方式"
        }, {
            value : "type",
            text : "以构件类型方式"
        }]
    }];
    var form = new dhtmlXForm("form$pos", formJson);
    form.attachEvent("onChange", function(itemId, value) {
        if ("area" == value) {
            currentTreeShowType = "area";
            initAreaTree();
        } else if ("type" == value) {
            initTypeTree();
            currentTreeShowType = "type";
        }
    });
    currentTreeShowType = "area";
}
function initAreaTree() {
    tree = dhxLayout.cells("a").attachTree();
    tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    tree.attachEvent("onMouseIn", function(id) {
        tree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    tree.attachEvent("onMouseOut", function(id) {
        tree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    tree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
    // 初始右键菜单
    var treeMenu = new dhtmlXMenuObject();
    treeMenu.renderAsContextMenu();
    treeMenu.loadXMLString("<menu><item id='1' text='新增构件分类'/><item id='2' text='修改构件分类'/><item id='3' text='删除构件分类'/></menu>");
    treeMenu.attachEvent("onClick", function(id) {
        MODEL_URL = COMPONENT_AREA_MODEL_URL;
        GWIN_WIDTH = 400;
        GWIN_HEIGHT = 240;
        if (id == "1") {
            componentAreaForm = openNewWindow(componentAreaFormData, initComponentAreaFormToolbar, 400, 200);
            componentAreaForm.setItemValue("parentId", contextMenuNodeId);
        } else if (id == "2") {
            componentAreaForm = openEditWindow(componentAreaFormData, contextMenuNodeId, initComponentAreaFormToolbar, 400, 200);
        } else if (id == "3") {
            if (contextMenuNodeId == "1") {
                dhtmlx.message("\"待分类\"节点不能删除！");
                return;
            }
            dhtmlxAjax.get(MODEL_URL + "/" + contextMenuNodeId + ".json?_method=delete", function(loader) {
                jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == 'string') {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message(getMessage("delete_success"));
                    var parId = tree.getParentId(contextMenuNodeId);
                    tree.refreshItem(parId);
                } else {
                    dhtmlx.message(isNotEmpty(jsonObj.message) ? jsonObj.message : getMessage("save_failure"));
                }
            });
        }
    });
    tree.enableContextMenu(treeMenu);
    tree.setDataMode("json");
    tree.enableSmartXMLParsing(true);
    tree.setXMLAutoLoading(COMPONENT_AREA_MODEL_URL+"!tree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentId&P_orders=showOrder");
    var treeJson = {id:0, item:[{id:-1,text:"构件生产库",im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item:[]}]};
    tree.loadJSONObject(treeJson);
    tree.refreshItem("-1");
    tree.enableDragAndDrop(true, false);
    tree.setDragBehavior("complex", true);
    tree.attachEvent("onDrop", function(sId, tId, id){
        var url = COMPONENT_AREA_MODEL_URL + "!sort.json?start=" + sId + "&targetId=" + tId;
        if (id) {
            url += "&end=" + id;
        }
        var result = loadJson(url);
        dhtmlx.message(result);
    });
    tree.attachEvent("onClick", treeClick);
    tree.attachEvent("onBeforeContextMenu", function(nId) {
        contextMenuNodeId = nId;
        if (nId == "-1") {
            treeMenu.showItem("1");
            treeMenu.hideItem("2");
            treeMenu.hideItem("3");
        } else {
            treeMenu.showItem("1");
            treeMenu.showItem("2");
            treeMenu.showItem("3");
        }
        return true;
    });
    tree.selectItem("-1", true);
}
function initTypeTree() {
    tree = dhxLayout.cells("a").attachTree();
    tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    tree.attachEvent("onMouseIn", function(id) {
        tree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    tree.attachEvent("onMouseOut", function(id) {
        tree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    tree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
    tree.setDataMode("json");
    tree.enableSmartXMLParsing(true);
    tree.setXMLAutoLoading(COMPONENT_AREA_MODEL_URL+"!tree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentId&P_orders=showOrder");
    var treeJson = {id:0, item:[{id:-1,text:"构件生产库",im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item:[
            {id:'develop',text:"开发的构件", open:true, item:[
                {id:'page', text:"页面构件", child:0},
                {id:'logic', text:"逻辑构件", child:0},
                {id:'common', text:"公用构件", child:0}
            ]}, 
            {id:'selfdefine',text:"自定义构件", open:true, item:[
                {id:'tree', text:"树构件", child:0},
                {id:'physical-table', text:"物理表构件", child:0},
                {id:'logic-table', text:"逻辑表构件", child:0},
                {id:'common-table', text:"通用表构件", child:0},
                {id:'tab', text:"标签页构件", child:0}
            ]},
            {id:'assemble',text:"组合构件", child:0}
        ]}]};
    tree.loadJSONObject(treeJson);
    tree.attachEvent("onClick", treeClick);
    tree.selectItem("-1", true);
}
function treeClick(id) {
    currentTreeNodeId = id;
    if (id == "-1") {
        if (componentLayout) {
            dhxLayout.cells("b").detachToolbar();
            dhxLayout.cells("b").detachStatusBar();
            toolBar = null;
            statusBar = null;
            dataGrid = null;
            componentLayout = null;
        }
        dhxLayout.cells("b").showHeader();
        dhxLayout.cells("b").setText("操作说明");
        dhxLayout.cells("b").attachObject(createHelpDiv());
    } else {
        dhxLayout.cells("b").hideHeader();
        if (!componentLayout) {
            componentLayout = dhxLayout.cells("b").attachLayout("1C");
            //componentLayout = dhxLayout.cells("b").attachLayout("2U");
            componentLayout.cells("a").hideHeader();
            //componentLayout.cells("b").setWidth(200);
            //componentLayout.cells("b").setText("构件说明");
            //componentRemarkDiv = createComponentRemarkDiv();
            //componentLayout.cells("b").attachObject(componentRemarkDiv);
            toolBar = dhxLayout.cells("b").attachToolbar();
            statusBar = dhxLayout.cells("b").attachStatusBar();
            initSelfToolBar();
        }
        if (!dataGrid) {
            gridData = componentGridData;
            dataGrid = componentLayout.cells("a").attachGrid();
            var typeCombo = dataGrid.getCombo(4);
            typeCombo.put("0", "公用构件");
            typeCombo.put("1", "页面构件");
            typeCombo.put("2", "逻辑构件");
            typeCombo.put("3", "树构件");
            typeCombo.put("4", "物理表构件");
            typeCombo.put("5", "逻辑表构件");
            typeCombo.put("6", "通用表构件");
            typeCombo.put("7", "标签页构件");
            typeCombo.put("8", "中转器构件");
            typeCombo.put("9", "组合构件");
            var systemParamConfigCombo = dataGrid.getCombo(8);
            systemParamConfigCombo.put("0", "<font color='red'>未完成</font>");
            systemParamConfigCombo.put("1", "<font color='green'>完成</font>");
            systemParamConfigCombo.put("2", "<font color='green'>无需绑定</font>");
            var isPackageCombo = dataGrid.getCombo(9);
            isPackageCombo.put("0", "否");
            isPackageCombo.put("1", "是");
            var isSystemUsedCombo = dataGrid.getCombo(10);
            isSystemUsedCombo.put("0", "否");
            isSystemUsedCombo.put("1", "是");
            initGrid();
            dataGrid.attachEvent("onSelectStateChanged", function(ids){
                if (componentRemarkDiv) {
                    if (!ids || ids.indexOf(",") != -1) {
                        componentRemarkDiv.innerHTML = "";
                    } else {
                        componentRemarkDiv.innerHTML = this.getUserData(ids, "remark");
                    }
                }
            });
            dataGrid.attachEvent("onCheckbox", function(rId, cInd, state){
                var tValue, tColumn;
                if (state){
                    tValue = "1";
                } else {
                    tValue = "0";
                }
                if (cInd == "6"){
                    tColumn = "buttonUse";
                } else if (cInd == "7"){
                    tColumn = "menuUse";
                }
                var url = COMPONENT_VERSION_MODEL_URL +"!updateStatus.json?id=" + rId + "&columName=" + tColumn + "&value=" + tValue;
                dhtmlxAjax.get(url,function(loader) {
                    var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                    if (typeof jsonObj == 'string') {
                        jsonObj = eval("(" + jsonObj + ")");
                    }
                    if (!jsonObj.success) {
                        dataGrid.cells(rId, cInd).setValue(tValue == "0" ? "1" : "0");
                        dhtmlx.message(jsonObj.message);
                    }
                });
           });
        }
        if (releasedSystem) {
            toolBar.showItem("systemUse");
            dataGrid.setColumnHidden(10, false);
        } else {
            toolBar.hideItem("systemUse");
            dataGrid.setColumnHidden(10, true);
        }
        if (ST_form && ST_form.isItem("componentType")) {
            ST_form.setItemValue("componentType", "all");
        }
        if (ST_form && ST_form.isItem("showType")) {
            ST_form.setItemValue("showType", "0");
        }
        refreshComponentVersionGrid();
    }
}