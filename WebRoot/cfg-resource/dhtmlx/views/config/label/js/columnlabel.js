TREE_URL = contextPath + "/label/column-label-category";
COLUMN_LABEL_CATEGORY_MODEL_URL = contextPath + "/label/column-label-category";
COLUMN_LABEL_MODEL_URL = contextPath + "/label/column-label";
var currentCategory;
var ST_form;
var columnLabelCategoryGridData = {
	format: {
		headers: ["&nbsp;", "<center>分类名称</center>", ""],
		cols: ["id", "name"],
		colWidths: ["30", "120","*"],
		colTypes: ["sub_row_form", "ro", "ro"],
		colAligns: ["right", "left"]
	}
};
var columnLabelCategoryFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "showOrder"},
			{type: "hidden", name: "menuId", value: nodeId},
			{type: "input", label: "分类名称:", name: "name", maxLength:100, required: true, tooltip: '名称不能为空'},
			{type: "newcolumn"},
			{type: "block", width: "120", list:[
				{type: "button", name: "save", value: "保存", width:100}
			]}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
var columnLabelGridData = {
	format: {
		headers: ["&nbsp;", "<center>字段标签名称</center>", "<center>字段标签编码</center>", ""],
		cols: ["id", "name", "code"],
		colWidths: ["30", "120", "120","*"],
		colTypes: ["sub_row_form", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left"]
	}
};

var url = contextPath +"/code/code-type!getCodeTypeSelect.json";
var opts = loadJson(url);
opts.unshift({value: "", text: "请选择编码",selected:true});

var columnLabelFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "categoryId"},
			{type: "hidden", name: "showOrder"},
			{type: "input", label: "字段标签名称:", name: "name", maxLength:100, required: true, tooltip: '名称不能为空'},
			{type: "input", label: "字段标签编码:", name: "code", maxLength:100, required: true, validate: "validCode", tooltip: '编码不能为空，只能是数字、字母、下划线'},
			{type: "combo", label: "默认关联编码:", name: "codeTypeCode", readonly: true, width: 200, options:opts},
			{type: "newcolumn"},
			{type: "block", width: "120", offsetTop:"48", list:[
				{type: "button", name: "save", value: "保存", width:100}
			]}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
function validCode(text) {
    var reg = new RegExp(/^[0-9a-zA-Z_]+$/);
    return reg.test(text);
}
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
            return this.cells(id, 1).getValue();
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
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                if (MODEL_URL == COLUMN_LABEL_CATEGORY_MODEL_URL) {
                    var id = subform.getItemValue("id");
                    var name = subform.getItemValue("name");
                    var result = eval("(" + loadJson(MODEL_URL + "!validateFields.json?id=" + id + "&name=" + encodeURIComponent(name) + "&menuId=" + nodeId) + ")");
                    if (typeof result == 'string') {
                        result = eval("(" + result + ")");
                    }
                    if (result.nameExist) {
                        dhtmlx.message(getMessage("form_field_exist", "分类名称"));
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
                        refreshGrid();
                        tree.refreshItem(currentTreeNodeId);
                    });
                } else {
                    subform.setItemValue("categoryId", currentCategory);
                    var id = subform.getItemValue("id");
                    var name = subform.getItemValue("name");
                    var code = subform.getItemValue("code");
                    var result = eval("(" + loadJson(MODEL_URL + "!validateFields.json?id=" + id + "&name=" + encodeURIComponent(name) + "&code=" + encodeURIComponent(code)) + ")");
                    if (result.nameExist) {
                        dhtmlx.message(getMessage("form_field_exist", "字段标签名称"));
                        return;
                    }
                    if (result.codeExist) {
                        dhtmlx.message(getMessage("form_field_exist", "字段标签编码"));
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
    if (MODEL_URL == COLUMN_LABEL_CATEGORY_MODEL_URL) {
        QUERY_URL = MODEL_URL + "!search.json?Q_EQ_menuId=" + nodeId + "&P_orders=showOrder";
    } else {
        QUERY_URL = MODEL_URL + "!search.json?Q_EQ_categoryId=" + currentCategory + "&P_orders=showOrder";
        if (param) {
            QUERY_URL += "&" + param;
        } else {
            ST_form.setItemValue("value", "");
            ST_form.setItemValue("columnName", "");
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
    toolBar.addSeparator("septr$" + index, index++);
    toolBar.addButton("delete", index++, "删除", "delete.gif");
    toolBar.addSeparator("septr$" + index, index++);
    toolBar.addButton("changeCategory", index++, "更改分类", "update.gif");
    toolBar.addSeparator("septr$" + index, index++);
    toolBar.addButton("refresh", index++, "刷新", "refresh.gif");
    
    toolBar.addButton("reset", index++, "", "reset.gif", null, "right");
    toolBar.addSeparator("septr$01", index++, "right");
    toolBar.addButton("sous", index++, "", "search.gif", null, "right");
    toolBar.addDiv("top$searchTextdiv", index++, "right");
    ST_form = initSearchColumn();
    toolBar.attachEvent('onClick', function(id) {
        if (id == "add") {
        	dataGrid.addSubRow();
        } else if (id == "delete") {
            var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message("请先选择记录！");
                return;
            } else if (selectId == "") {
                refreshGrid();
                return;
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
        } else if (id == "changeCategory") {
        	var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            if (!dhxWins) {
		    	dhxWins = new dhtmlXWindows();
		    }
	    	var categoryWin = dhxWins.createWindow("categoryWin", 0, 0, 500, 400);
		    categoryWin.setModal(true);
		    categoryWin.setText("更改分类");
		    categoryWin.center();
		    categoryWin.button("park").hide();
		    categoryWin.button("minmax1").hide();
		    categoryWin.button("minmax2").hide();
			
			var categoryTree = categoryWin.attachTree();
			categoryTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
			categoryTree.attachEvent("onMouseIn", function(id) {
				categoryTree.setItemStyle(id, "background-color:#D5E8FF;");
			});
			categoryTree.attachEvent("onMouseOut", function(id) {
				categoryTree.setItemStyle(id, "background-color:#FFFFFF;");
			});
			categoryTree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
			var treeJson = {id:0, item:[{id:-1,text:"字段标签定义",im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item:[]}]};
			categoryTree.setDataMode("json");
    		categoryTree.enableSmartXMLParsing(true);
   		    categoryTree.setXMLAutoLoading(TREE_URL + "!tree.json?E_model_name=tree&F_in=name,child&view=dhtmlx&menuId=" + nodeId);
            categoryTree.loadJSONObject(treeJson);
            categoryTree.refreshItem("-1");
            var categoryStatusBar = categoryWin.attachStatusBar();
			var categoryToolBar = new dhtmlXToolbarObject(categoryStatusBar);
			categoryToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
		    categoryToolBar.addButton("submit", 1, "&nbsp;&nbsp;确定&nbsp;&nbsp;");
		    categoryToolBar.addButton("cancle", 2, "&nbsp;&nbsp;取消&nbsp;&nbsp;");
		    categoryToolBar.setAlign("right");
		    categoryToolBar.attachEvent('onClick', function(id) {
		    	if (id == "submit") {
		    		var selectCategoryId = categoryTree.getSelectedItemId();
		    		if (!selectCategoryId) {
		                dhtmlx.message("请先选择分类！");
		                return;
		            } else if (selectCategoryId == currentTreeNodeId) {
		            	dhtmlx.message("该字段标签已经在当前分类中，请选择其他分类！");
		                return;
		            } else if (selectCategoryId == -1) {
		            	dhtmlx.message("请选择其他分类！");
		                return;
		            }
		            dhtmlxAjax.get(COLUMN_LABEL_MODEL_URL+"!changeCategory.json?columnLabelIds="+selectIds+"&categoryId="+selectCategoryId, function(loader) {
						var result = eval(loader.xmlDoc.responseText);
						if (result != "") {
							dhtmlx.message(result);
						}
						categoryWin.close();
						refreshGrid();
					});
		    	} else {
		    		categoryWin.close();
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
            options:[{value:'',text:'请选择',selected:true},
                     {value:'name',text:'名称',selected:false},
                     {value:'code',text:'编码',selected:false}]
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
	var treeJson = {id:0, item:[{id:-1,text:"字段标签定义",im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item:[]}]};
	tree.setDataMode("json");
    tree.enableSmartXMLParsing(true);
    tree.setXMLAutoLoading(TREE_URL + "!tree.json?E_model_name=tree&F_in=name,child&view=dhtmlx&menuId=" + nodeId);
    tree.loadJSONObject(treeJson);
    tree.refreshItem("-1");
    tree.attachEvent("onClick", function(id) {
        currentTreeNodeId = id;
        if (id == "-1") {
        	currentCategory = null;
            if (MODEL_URL != COLUMN_LABEL_CATEGORY_MODEL_URL) {
                MODEL_URL = COLUMN_LABEL_CATEGORY_MODEL_URL;
                gridData = columnLabelCategoryGridData;
                detailFormData = columnLabelCategoryFormData;
                if (!toolBar) {
                    toolBar = dhxLayout.cells("b").attachToolbar();
                    statusBar = dhxLayout.cells("b").attachStatusBar();
                    initSelfToolBar();
                }
                toolBar.hideItem("changeCategory");
                toolBar.hideItem("reset");
                toolBar.hideItem("sous");
                toolBar.hideItem("top$searchTextdiv");
                dataGrid = dhxLayout.cells("b").attachGrid();
                initSelfGrid(true);
                dataGrid.attachEvent("onDrag", function(sId, tId) {
                    if (sId.indexOf(",") != -1) {
                        dhtmlx.message("只能拖动一条记录!");
                        return false;
                    }
                    return true;
                });
                dataGrid.attachEvent("onDrop", function(sId, tId) {
                    loadJson(COLUMN_LABEL_CATEGORY_MODEL_URL + "!sort.json?start=" + sId + "&end=" + tId + "&menuId=" + nodeId);
                    tree.refreshItem("-1");
                    refreshGrid();
                });
            } else {
                refreshGrid();
            }
        } else {
        	currentCategory = id;
            if (MODEL_URL != COLUMN_LABEL_MODEL_URL) {
                MODEL_URL = COLUMN_LABEL_MODEL_URL;
                gridData = columnLabelGridData;
                detailFormData = columnLabelFormData;
                if (!toolBar) {
                    toolBar = dhxLayout.cells("b").attachToolbar();
                    statusBar = dhxLayout.cells("b").attachStatusBar();
                    initSelfToolBar();
                }
                toolBar.showItem("changeCategory");
                toolBar.showItem("reset");
                toolBar.showItem("sous");
                toolBar.showItem("top$searchTextdiv");
                dataGrid = dhxLayout.cells("b").attachGrid();
                initSelfGrid(true);
                dataGrid.attachEvent("onDrag", function(sId, tId) {
                    if (sId.indexOf(",") != -1) {
                        dhtmlx.message("只能拖动一条记录!");
                        return false;
                    }
                    return true;
                });
                dataGrid.attachEvent("onDrop", function(sId, tId) {
                    loadJson(COLUMN_LABEL_MODEL_URL + "!sort.json?start=" + sId + "&end=" + tId + "&categoryId=" + currentCategory);
                    refreshGrid();
                });
            } else {
                refreshGrid();
            }
        }
    });
    tree.selectItem("-1", true);
}