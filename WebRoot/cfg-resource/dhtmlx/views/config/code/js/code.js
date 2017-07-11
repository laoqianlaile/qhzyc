TREE_URL = contextPath + "/code/code-type";
CODE_TYPE_MODEL_URL = contextPath + "/code/code-type";
CODE_MODEL_URL = contextPath + "/code/code";
BUSINESS_CODE_MODEL_URL = contextPath + "/code/business-code";
var currentCodeTypeCode, currentCodeParentId, systemId;
var ST_form;
var codeTypeGridData = {
	format: {
		headers: ["&nbsp;", "<center>编码类型编码</center>", "<center>编码类型名称</center>", "<center>是否系统编码</center>", "<center>是否业务表编码</center>", ""],
		cols: ["id", "code", "name", "isSystem", "isBusiness"],
		userdata: ["isSystem"],
		colWidths: ["30", "150", "150", "120", "120", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "co", "co", "ro"],
		colAligns: ["right", "left", "left", "left", "left"],
		colTooltips: ["false", "true", "true", "true", "true", "false"]
	}
};
var codeTypeFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "systemId"},
			{type: "hidden", name: "showOrder"},
			{type: "input", label: "编码类型编码:", name: "code", maxLength:100, required: true, tooltip: '编码类型编码不能为空'},
			{type: "input", label: "编码类型名称:", name: "name", maxLength:100, required: true, tooltip: '编码类型名称不能为空'},
			{type: "block", width: "750", list:[
				{type: "itemlabel", name: "isSystemLabel", label: "是否是系统编码:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
				{type: "newcolumn"},
 				{type: "radio", name: "isSystem", label: "是", value:"1", labelWidth:60, position:"label-left", labelAlign:"right"},
				{type: "newcolumn"},
 				{type: "radio", name: "isSystem", label: "否", value:"0", labelWidth:80, position:"label-left", labelAlign:"right", checked: true}
			]},
			{type: "block", width: "750", list:[
				{type: "itemlabel", name: "isBusinessLabel", label: "是否是业务表编码:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
				{type: "newcolumn"},
 				{type: "radio", name: "isBusiness", label: "是", value:"1", labelWidth:60, position:"label-left", labelAlign:"right"},
				{type: "newcolumn"},
 				{type: "radio", name: "isBusiness", label: "否", value:"0", labelWidth:80, position:"label-left", labelAlign:"right", checked: true}
			]},
			{type: "block", width: "750", list:[
				{type: "itemlabel", name: "isCacheLabel", label: "是否使用缓存:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
				{type: "newcolumn"},
 				{type: "radio", name: "isCache", label: "是", value:"1", labelWidth:60, position:"label-left", labelAlign:"right", checked: true},
				{type: "newcolumn"},
 				{type: "radio", name: "isCache", label: "否", value:"0", labelWidth:80, position:"label-left", labelAlign:"right"},
				{type: "newcolumn"},
				{type: "button", name: "save", value: "保存", offsetLeft:50, width:80}
			]}
		]}
    ],
	settings: {labelWidth: 140, inputWidth: 200}
};
var codeGridData = {
	format: {
		headers: ["&nbsp;", "<center>编码值</center>", "<center>编码名称</center>", "<center>是否系统编码</center>", "<center>最值顺序</center>", "<center>说明</center>", ""],
		cols: ["id", "value", "name", "isSystem", "mostValueShowOrder", "remark"],
		colWidths: ["30", "150", "150", "150", "150", "200", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "co", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left", "left", "left", "left"],
		colTooltips: ["false", "true", "true", "true", "true", "true", "false"]
	}
};
var codeFormData = {
	format: [
        {type: "block", width: "900", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "codeTypeCode"},
			{type: "hidden", name: "parentId"},
			{type: "hidden", name: "showOrder"},
			{type: "block", width: "700", list:[
				{type: "input", label: "编码值:", name: "value", maxLength:200, required: true, tooltip: '编码值不能为空'},
				{type: "input", label: "最值顺序:&nbsp;&nbsp;&nbsp;", name: "mostValueShowOrder", maxLength:4},
				{type: "newcolumn"},
				{type: "input", label: "编码名称:", name: "name", maxLength:100, required: true, tooltip: '编码名称不能为空'}
			]},
			{type: "block", width: "700", list:[
                {type: "itemlabel", name: "isSystemLabel", label: "是否是系统编码:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "isSystem", label: "是", value:"1", labelWidth:60, position:"label-left", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "isSystem", label: "否", value:"0", labelWidth:80, position:"label-left", labelAlign:"right", checked: true}
            ]},
			{type: "block", width: "900", list:[
				{type: "input", label: "说明:&nbsp;&nbsp;&nbsp;", name: "remark", maxLength:200, rows:3, width: 520},
				{type: "newcolumn"},
				{type: "button", name: "save", value: "保存", offsetTop:30, offsetLeft:10, width:80}
			]}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
/**
 * 初始化列表
 * @param {boolean} dragable
 */
function initSelfGrid(dragable) {
    dataGrid.setImagePath(IMAGE_PATH);
    dataGrid.setHeader(gridData.format.headers.toString());
    dataGrid.setInitWidths(gridData.format.colWidths.toString());
    dataGrid.setColTypes(gridData.format.colTypes.toString());
    dataGrid.setColAlign(gridData.format.colAligns.toString());
    if (gridData.format.colTooltips) {
        dataGrid.enableTooltips(gridData.format.colTooltips.toString());
    }
    dataGrid.setSkin(Skin);
    dataGrid.init();
    dataGrid.enableMultiselect(true);
    if (dragable) {
        dataGrid.enableDragAndDrop(true);
        dataGrid.attachEvent("onBeforeDrag", function(id) {
            return this.cells(id, 2).getValue();
        });
    }
    dataGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    dataGrid.attachEvent("onRowSelect", function(rId, cInd) {
        dataGrid.cells(rId, 0).open();
    });
    dataGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = initDetailFormFormat(detailFormData);
    });
    dataGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        if (id != "") {
            var url = MODEL_URL + "/" + id + ".json?_method=get";
            var formData = loadJson(url);
            loadFormData(subform, formData);
            if (MODEL_URL == CODE_TYPE_MODEL_URL) {
            	subform.setReadonly("code", true);
            	subform.checkItem("isSystem", formData["isSystem"]);
                subform.checkItem("isBusiness", formData["isBusiness"]);
                subform.checkItem("isCache", formData["isCache"]);
            } else {
                var codeType = loadJson(CODE_TYPE_MODEL_URL + "!getCodeType.json?codeTypeCode=" + currentCodeTypeCode);
                if (typeof codeType == 'string') {
                    codeType = eval('(' + codeType + ')');
                }
                var codeTypeIsSystem = codeType.isSystem;
                if (codeTypeIsSystem == "1") {
                    subform.disableItem('isSystem', '0');
                } else {
                    subform.enableItem('isSystem', '0');
                }
            }
        } else {
            if (MODEL_URL == CODE_MODEL_URL) {
                var codeType = loadJson(CODE_TYPE_MODEL_URL + "!getCodeType.json?codeTypeCode=" + currentCodeTypeCode);
                if (typeof codeType == 'string') {
                    codeType = eval('(' + codeType + ')');
                }
                var codeTypeIsSystem = codeType.isSystem;
                if (codeTypeIsSystem == "1") {
                    subform.disableItem('isSystem', '0');
                    subform.checkItem('isSystem', '1');
                } else {
                    subform.enableItem('isSystem', '0');
                    subform.checkItem('isSystem', '0');
                }
            }
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                if (MODEL_URL == CODE_TYPE_MODEL_URL) {
                    var id = subform.getItemValue("id");
                    var name = subform.getItemValue("name");
                    var code = subform.getItemValue("code");
                    var isBusiness = subform.getItemValue("isBusiness");
                    var result = eval("(" + loadJson(MODEL_URL + "!validateFields.json?id=" + id + "&name=" + encodeURIComponent(name) + "&code=" + encodeURIComponent(code) + "&isBusiness=" + isBusiness) + ")");
                    if (typeof result == 'string') {
                        result = eval("(" + result + ")");
                    }
                    if (result.nameExist) {
                        dhtmlx.message(getMessage("form_field_exist", "编码类型名称"));
                        return;
                    }
                    if (result.codeExist) {
                        dhtmlx.message(getMessage("form_field_exist", "编码类型编码"));
                        return;
                    }
                    if (result.cannotUpdateIsBusiness) {
                        dhtmlx.message("该编码类型存在编码，不能作为业务表编码！");
                        return;
                    }
                    if (id == "") {
                        SAVE_URL = MODEL_URL;
                        subform.setItemValue("_method", "post");
                        if( systemId != "-1" ){
                        	subform.setItemValue("systemId", systemId);
                        }else {
                        	subform.setItemValue("systemId", "");
                        }
                        
                    } else {
                        SAVE_URL = MODEL_URL + "/" + id;
                        subform.setItemValue("_method", "put");
                    }
                    subform.send(SAVE_URL, "post", function(loader, response) {
                        dhtmlx.message(getMessage("save_success"));
                        refreshGrid();
                        tree.refreshItem(currentTreeNodeId);
                    });
                } else {
                    subform.setItemValue("codeTypeCode", currentCodeTypeCode);
                    subform.setItemValue("parentId", currentCodeParentId);
                    var id = subform.getItemValue("id");
                    var name = subform.getItemValue("name");
                    var value = subform.getItemValue("value");
                    var result = eval("(" + loadJson(MODEL_URL + "!validateFields.json?id=" + id + "&codeTypeCode=" + currentCodeTypeCode + "&name=" + encodeURIComponent(name) + "&value=" + encodeURIComponent(value)) + ")");
                    if (result.nameExist) {
                        dhtmlx.message(getMessage("form_field_exist", "编码名称"));
                        return;
                    }
                    if (result.valueExist) {
                        dhtmlx.message(getMessage("form_field_exist", "编码值"));
                        return;
                    }
                    if (id == "") {
                        SAVE_URL = MODEL_URL;
                        subform.setItemValue("_method", "post");
                    } else {
                        SAVE_URL = MODEL_URL + "/" + id;
                        subform.setItemValue("_method", "put");
                    }
                    subform.send(SAVE_URL, "post", function(loader, response) {
                        dhtmlx.message(getMessage("save_success"));
                        tree.refreshItem(currentTreeNodeId);
                        refreshGrid();
                    });
                }
            }
        });
    });
    dataGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            dataGrid.forEachRow(function(rId) {
                if (id != rId) {
                    dataGrid.cells(rId, 0).close();
                }
            });
        }
    });
    if (pageable) {
        pagesize = getCookie("pagesize") || PAGE_SIZE;
        dataGrid.enablePaging(true, pagesize, 1, statusBar);
        dataGrid.setPagingSkin('toolbar', Skin);
    }
    refreshGrid();
}
/**
 * 刷新列表
 */
function refreshGrid(param) {
    if (MODEL_URL == CODE_TYPE_MODEL_URL) {
        QUERY_URL = MODEL_URL + "!search.json";
	    if (param) {
	    	QUERY_URL += "?" + param;
	    } else {
	        ST_form.setItemValue("value", "");
			ST_form.setItemValue("columnName", "");
	    }
	    if (systemId !="") {
	    	if(systemId !="-1" ) {
		    	if (QUERY_URL.indexOf("?") > -1) {
		    		QUERY_URL +="&Q_EQ_systemId=" + systemId;
		    	}else {
		    		QUERY_URL +="?Q_EQ_systemId=" + systemId;
		    	}
	    	}
	    } else {
	    	if (QUERY_URL.indexOf("?") > -1) {
	    		QUERY_URL +="&Q_NULL_systemId";
	    	} else {
	    		QUERY_URL +="?Q_NULL_systemId";
	    	}
	    }
	    if (QUERY_URL.indexOf("?") > -1) {
	        QUERY_URL += "&P_orders=showOrder";
        } else {
            QUERY_URL += "?P_orders=showOrder";
        }
    } else {
        QUERY_URL = MODEL_URL + "!search.json?Q_EQ_codeTypeCode=" + currentCodeTypeCode + "&P_orders=showOrder";
	    if (param) {
	    	QUERY_URL += "&" + param;
	    } else {
	        ST_form.setItemValue("value", "");
			ST_form.setItemValue("columnName", "");
	    }
    	if (currentCodeParentId) {
    		QUERY_URL += "&Q_EQ_parentId=" + currentCodeParentId;
    	} else {
    		QUERY_URL += "&Q_NULL_parentId";
    	}
    }
    search();
}
/**
 * 初始化列表工具条
 */
function initSelfToolBar() {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    var index = 0;
    toolBar.addButton("add", index++, "新增", "new.gif");
    toolBar.addSeparator("septr$01", index++);
    toolBar.addButton("config", index++, "配置业务表编码", "setup.gif");
    toolBar.addSeparator("septr$01", index++);
    toolBar.addButton("syncBusinessCode", index++, "同步业务表编码", "sync.gif");
    toolBar.addSeparator("septr$01", index++);
    toolBar.addButton("delete", index++, "删除", "delete.gif");
    toolBar.addSeparator("septr$01", index++);
    toolBar.addButton("refresh", index++, "刷新", "refresh.gif");
    
	toolBar.addButton("reset", index++, "", "reset.gif", null, "right");
	toolBar.addSeparator("septr$01", index++, "right");
	toolBar.addButton("sous", index++, "", "search.gif", null, "right");
    toolBar.addDiv("top$searchTextdiv", index++, "right");
	ST_form = initSearchColumn();
    toolBar.attachEvent('onClick', function(id) {
        if (id == "add") {
        	dataGrid.addSubRow();
        } else if (id == "config") {
        	var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds == "") {
            	refreshMenuGrid();
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            if (dataGrid.cellById(selectIds, 4).getValue() != "1") {
            	dhtmlx.message("不是业务表编码！");
                return;
            }
        	configBusinessCode(dataGrid.cellById(selectIds, 1).getValue());
        } else if (id == "syncBusinessCode") {
        	var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds == "") {
            	refreshMenuGrid();
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            if (dataGrid.cellById(selectIds, 4).getValue() != "1") {
            	dhtmlx.message("不是业务表编码！");
                return;
            }
            var result = eval("(" + loadJson(BUSINESS_CODE_MODEL_URL + "!syncBusinessCode.json?codeTypeCode=" + encodeURIComponent(dataGrid.cellById(selectIds, 1).getValue())) + ")");
            if (!result.success) {
                dhtmlx.message("同步业务表编码失败！");
            } else {
            	dhtmlx.message("同步业务表编码成功！");
            }
        } else if (id == "delete") {
            var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message("请先选择记录！");
                return;
            } else if (selectId == "") {
                refreshGrid();
                return;
            }
            if (MODEL_URL == CODE_TYPE_MODEL_URL) {
            	var selectIds = selectId.split(",");
            	for (var i in selectIds) {
            		if (dataGrid.getUserData(selectIds[i], "isSystem") == 1) {
            			dhtmlx.message("系统编码不能删除！");
            			return;
            		}
            	}
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        deleteById(selectId, function() {
                            dhtmlx.message(getMessage("delete_success"));
                            tree.refreshItem(currentTreeNodeId);
                            refreshGrid();
                        });
                    }
                }
            });
        } else if (id == "refresh") {
            refreshGrid();
        } else if (id == "sous") {
        	var value = ST_form.getItemValue("value"),
			    name  = ST_form.getItemValue("columnName"),
			    param = "";
			value = encodeURIComponent(value);
			if (name == "") {
				dhtmlx.message("过滤字段不能为空，请选择！");
				return;
			}
			if (value) {
				param = "Q_LIKE_" + name + "=" + value;
			}
			refreshGrid(param);
        } else if (id == "reset") {
			refreshGrid();
        }
    });
}
/**
 * 字段检索
 * @returns {dhtmlXForm}
 */
function initSearchColumn() {
	var sformJson = [
		{type: "itemlabel", label: "",labelWidth: 15},
        {type: "newcolumn"},
		{type: "combo", name: "columnName", className: "dhx_toolbar_form", label: "过滤字段：", labelWidth: 80, labelAlign:"right", style:"font-size:11px;", width: 120, readonly:"true",
        	options:[{value:'',text:'请选择',selected:false}]
        },
        {type: "newcolumn"},
		{type: "input",label: "&nbsp;&nbsp;值：", name: "value", className: "dhx_toolbar_form", width:120, inputHeight:17}
	];
	var form = new dhtmlXForm("top$searchTextdiv", sformJson);
    var scInp = form.getInput("value");
    scInp.onfocus = function() {
        form.setItemValue("value", "");
    };
    scInp.onkeydown = function(e) {
        e = e || window.event;
        var keyCode = e.keyCode || e.which;
        if (13 == keyCode) {
            var value = form.getItemValue("value"), name = form.getItemValue("columnName"), param = "";
            value = encodeURIComponent(value);
            if (name == "") {
                dhtmlx.message("过滤字段不能为空，请选择！");
                return;
            }
            if (value !== "") {
                param = "Q_LIKE_" + name + "=" + value;
            }
            refreshGrid(param);
        }
    };
    return form;
}
var businessCodeFormData = {
	format: [
        {type: "block", width: "350", offsetTop: "10", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "codeTypeCode"},
			{type: "block", width: "320", list:[
				{type: "itemlabel", label: "业务编码类型:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
				{type: "newcolumn"},
 				{type: "radio", name: "businessCodeType", label: "业务表", value:"0", labelWidth:60, position:"label-right", labelAlign:"left", checked: true},
				{type: "newcolumn"},
 				{type: "radio", name: "businessCodeType", label: "JAVA", value:"1", labelWidth:60, position:"label-right", labelAlign:"left"}
			]},
			{type: "block", name: "BJ_block", width: "340", list:[
			    {type: "input", label: "类名称:", name: "className", rows:4, maxLength: 500, required: true, tooltip: '类全名不能为空'}
			]},
			{type: "block", name: "BT_block", width: "340", list:[
				{type: "input", label: "业务表名称:", name: "tableName", maxLength:100, required: true, tooltip: '业务表名称不能为空'},
				{type: "input", label: "编码名称字段:", name: "codeNameField", maxLength:100, required: true, tooltip: '编码名称字段不能为空'},
				{type: "input", label: "编码值字段:", name: "codeValueField", maxLength:100, required: true, tooltip: '编码值字段不能为空'},
				{type: "input", label: "显示顺序字段:&nbsp;&nbsp;&nbsp;", name: "showOrderField", maxLength:100},
				{type: "input", label: "ID字段:&nbsp;&nbsp;&nbsp;", name: "idField", maxLength:100},
				{type: "input", label: "PARENT_ID字段:&nbsp;&nbsp;&nbsp;", name: "parentIdField", maxLength:100},
				{type: "block", width: "320", list:[
					{type: "itemlabel", name: "isAuthLabel", label: "系统管理平台表:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
					{type: "newcolumn"},
	 				{type: "radio", name: "isAuth", label: "是", value:"1", labelWidth:60, position:"label-right", labelAlign:"left"},
					{type: "newcolumn"},
	 				{type: "radio", name: "isAuth", label: "否", value:"0", labelWidth:60, position:"label-right", labelAlign:"left", checked: true}
				]}
			]},
			{type: "block", width: "320", list:[
				{type: "itemlabel", name: "isTimingUpdateLabel", label: "定时更新:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
				{type: "newcolumn"},
 				{type: "radio", name: "isTimingUpdate", label: "是", value:"1", labelWidth:60, position:"label-right", labelAlign:"left"},
				{type: "newcolumn"},
 				{type: "radio", name: "isTimingUpdate", label: "否", value:"0", labelWidth:60, position:"label-right", labelAlign:"left", checked: true}
			]},
			{type: "input", label: "定时间隔:&nbsp;&nbsp;&nbsp;", name: "period", maxLength:100, validate:"ValidInteger", tooltip: '分钟'}
		]}
    ],
	settings: {labelWidth: 140, inputWidth: 180}
};

function changeBusinessCodeType(form, value) {
	if ("0" === value) { 
		// 业务表
		form.hideItem("BJ_block");
		form.showItem("BT_block");
		form.setRequired("className", false);
		form.setRequired("tableName", true);
		form.setRequired("codeNameField", true);
		form.setRequired("codeValueField", true);
	} else {
		// JAVA
		form.hideItem("BT_block");
		form.showItem("BJ_block");
		form.setRequired("className", true);
		form.setRequired("tableName", false);
		form.setRequired("codeNameField", false);
		form.setRequired("codeValueField", false);
	}
	form.checkItem("isAuth", "0");
}

function clearByBusinessCodeType(form, value) {
	if ("0" === value) { 
		// 业务表
		form.setItemValue("className", "");
	} else {
		// JAVA
		form.setItemValue("tableName", "");
		form.setItemValue("codeNameField", "");
		form.setItemValue("codeValueField", "");
		form.setItemValue("showOrderField", "");
		form.setItemValue("idField", "");
		form.setItemValue("parentIdField", "");
	}
}
/**
 * 配置业务表编码
 */
function configBusinessCode(codeTypeCode) {
    if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var businessCodeWin = dhxWins.createWindow("baseConfigWin", 0, 0, 420, 340);
    businessCodeWin.setModal(true);
    businessCodeWin.setText("配置业务表编码");
    businessCodeWin.center();
    var businessCodeForm = businessCodeWin.attachForm(initDetailFormFormat(businessCodeFormData));
    var businessCodeFormStatusBar = businessCodeWin.attachStatusBar();
    var businessCodeFormToolbar = new dhtmlXToolbarObject(businessCodeFormStatusBar);
    initBusinessCodeToolBar(businessCodeFormToolbar, businessCodeWin, businessCodeForm);
    var formData = loadJson(BUSINESS_CODE_MODEL_URL + "!getByCodeTypeCode.json?_method=get&codeTypeCode=" + codeTypeCode);
    
    changeBusinessCodeType(businessCodeForm, formData.businessCodeType)
    loadFormData(businessCodeForm, formData);
    businessCodeForm.attachEvent("onChange", function(id, value, state) {
    	if ("businessCodeType" === id) {
    		changeBusinessCodeType(businessCodeForm, value);
    	}
    });
    if (isNotEmpty(formData.id)) {
		businessCodeForm.disableItem("businessCodeType", "1" === formData.businessCodeType ? "0" : "1");
    }
}
/**
 * 初始化菜单图标表单工具条
 */
function initBusinessCodeToolBar(toolBar, businessCodeWin, businessCodeForm) {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.setAlign("right");
    toolBar.addButton("save", 2, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    toolBar.addSeparator("septr$02", 3);
    toolBar.addButton("close", 4, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    toolBar.attachEvent('onClick', function(id) {
        if (id == "save") {
        	if (!businessCodeForm.validate()) {
        		return;
        	}
        	var id = businessCodeForm.getItemValue("id");
        	var businessCodeType = businessCodeForm.getItemValue("businessCodeType");
        	// 清空相应值
        	clearByBusinessCodeType(businessCodeForm, businessCodeType);
        	if ("0" === businessCodeType) {
        		// 业务表后台校验
        		var tableName = businessCodeForm.getItemValue("tableName");
                var codeNameField = businessCodeForm.getItemValue("codeNameField");
                var codeValueField = businessCodeForm.getItemValue("codeValueField");
                var showOrderField = businessCodeForm.getItemValue("showOrderField");
                var idField = businessCodeForm.getItemValue("idField");
                var parentIdField = businessCodeForm.getItemValue("parentIdField");
                var isAuth = businessCodeForm.getItemValue("isAuth");
                var result = eval("(" + loadJson(BUSINESS_CODE_MODEL_URL + "!validateFields.json?id=" + id + "&tableName=" + tableName
                    + "&codeNameField=" + codeNameField + "&codeValueField=" + codeValueField + "&showOrderField=" + showOrderField
                    + "&idField=" + idField + "&parentIdField=" + parentIdField + "&isAuth=" + isAuth) + ")");
                if (result.tableNameExist) {
                    dhtmlx.message("该业务表的编码已经存在！");
                    return;
                }
                if (result.dbError) {
                    dhtmlx.message(result.dbErrorMsg);
                    return;
                }
        	}
            
            if (id == "") {
                SAVE_URL = BUSINESS_CODE_MODEL_URL + ".json";
                businessCodeForm.setItemValue("_method", "post");
            } else {
                SAVE_URL = BUSINESS_CODE_MODEL_URL + "/" + id + ".json";
                businessCodeForm.setItemValue("_method", "put");
            }
            businessCodeForm.send(SAVE_URL, "post", function(loader, response) {
                dhtmlx.message(getMessage("save_success"));
                var content = loader.xmlDoc.responseText.replace(/:null/g, ":''");
                var formData = eval("(" + content + ")");
                loadFormData(businessCodeForm, formData);
            });
        } else if (id == "close") {
        	businessCodeWin.close();
        }
    });
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

    tree = dhxLayout.cells("a").attachTree();
    tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    tree.attachEvent("onMouseIn", function(id) {
        tree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    tree.attachEvent("onMouseOut", function(id) {
        tree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    tree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
	var treeJson = {id:0, item:[{id:-1,text:"编码类型定义",im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item:[]}]};
	tree.setDataMode("json");
    tree.enableSmartXMLParsing(true);
    tree.setXMLAutoLoading(TREE_URL + "!tree.json?E_model_name=tree&F_in=name,child&view=dhtmlx");
    tree.loadJSONObject(treeJson);
    tree.refreshItem("-1");
    tree.attachEvent("onClick", function(id, prevId) {
    	if (tree.getAttribute(id, "prop2") == "1") {
            tree.selectItem(prevId, false);
            dhtmlx.message("业务表编码无法手动添加编码！");
            return;
        }
        currentTreeNodeId = id;
        // 系统systemId
        var prop3 = tree.getAttribute(id,"prop3");
        if( typeof prop3=="undefined" ||  prop3==null || prop3=="" ) {
        	systemId = "";
        }else {
        	systemId = prop3;
        }
        if (id == "-1" || id=="COMMON_-1" || id.indexOf("SYSTEM_")>-1 ) {
        	currentCodeParentId = null;
        	currentCodeTypeCode = null;
        	if( id =="-1" ) {
        		systemId = "-1";
        	}
            if (MODEL_URL != CODE_TYPE_MODEL_URL) {
                MODEL_URL = CODE_TYPE_MODEL_URL;
                gridData = codeTypeGridData;
                detailFormData = codeTypeFormData;
                if (!toolBar) {
                    toolBar = dhxLayout.cells("b").attachToolbar();
                    statusBar = dhxLayout.cells("b").attachStatusBar();
                    initSelfToolBar();
                }
                toolBar.showItem("config");
                toolBar.showItem("syncBusinessCode");
            	ST_form.getCombo("columnName").deleteOption("value");
            	ST_form.getCombo("columnName").deleteOption("name");
            	ST_form.getCombo("columnName").addOption([{value:'code',text:'类型编码',selected:false},{value:'name',text:'类型名称',selected:false}]);
                dataGrid = dhxLayout.cells("b").attachGrid();
                var isSystemCombo = dataGrid.getCombo(3);
                isSystemCombo.put("1", "是");
                isSystemCombo.put("0", "否");
                var isBusinessCombo = dataGrid.getCombo(4);
                isBusinessCombo.put("1", "是");
                isBusinessCombo.put("0", "否");
                initSelfGrid(true);
                dataGrid.attachEvent("onDrag", function(sId, tId) {
                    if (sId.indexOf(",") != -1) {
                        dhtmlx.message("只能拖动一条记录!");
                        return false;
                    }
                    return true;
                });
                dataGrid.attachEvent("onDrop", function(sId, tId) {
                    loadJson(CODE_TYPE_MODEL_URL + "!sort.json?start=" + sId + "&end=" + tId);
                    refreshGrid();
                });
            } else {
                refreshGrid();
            }
        } else {
        	currentCodeTypeCode = tree.getAttribute(id, "prop0");
        	currentCodeParentId = tree.getAttribute(id, "prop1");
            if (MODEL_URL != CODE_MODEL_URL) {
                MODEL_URL = CODE_MODEL_URL;
                gridData = codeGridData;
                detailFormData = codeFormData;
                if (!toolBar) {
                    toolBar = dhxLayout.cells("b").attachToolbar();
                    statusBar = dhxLayout.cells("b").attachStatusBar();
                    initSelfToolBar();
                }
                toolBar.hideItem("config");
                toolBar.hideItem("syncBusinessCode");
            	ST_form.getCombo("columnName").deleteOption("code");
            	ST_form.getCombo("columnName").deleteOption("name");
            	ST_form.getCombo("columnName").addOption([{value:'value',text:'编码值',selected:false},{value:'name',text:'编码名称',selected:false}]);
                dataGrid = dhxLayout.cells("b").attachGrid();
                var isSystemCombo = dataGrid.getCombo(3);
                isSystemCombo.put("1", "是");
                isSystemCombo.put("0", "否");
                initSelfGrid(true);
                dataGrid.attachEvent("onDrag", function(sId, tId) {
                    if (sId.indexOf(",") != -1) {
                        dhtmlx.message("只能拖动一条记录!");
                        return false;
                    }
                    return true;
                });
                dataGrid.attachEvent("onDrop", function(sId, tId) {
                    loadJson(CODE_MODEL_URL + "!sort.json?start=" + sId + "&end=" + tId);
                    refreshGrid();
                });
            } else {
                refreshGrid();
            }
        }
        if (ST_form) {
        	ST_form.setItemValue('columnName', '');
        	ST_form.setItemValue('value', '');
        }
    });
    tree.selectItem("-1", true);
}