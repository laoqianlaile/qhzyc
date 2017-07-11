MODEL_URL = contextPath + "/label/type-label";
var DEFAULT_QUERY_URL = MODEL_URL + "!search.json?Q_EQ_menuId=" + nodeId + "&P_orders=showOrder";
gridData = {
	format: {
		headers: ["&nbsp;", "<center>名称</center>", "<center>编码</center>", ""],
		cols: ["id", "name", "code"],
		colWidths: ["30", "120", "120","*"],
		colTypes: ["sub_row_form", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left"]
	}
};
detailFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "showOrder"},
			{type: "hidden", name: "menuId", value: nodeId},
			{type: "input", label: "名称:", name: "name", maxLength:100, required: true, tooltip: '名称不能为空'},
			{type: "input", label: "编码:", name: "code", maxLength:100, required: true, validate: "validCode", tooltip: '编码不能为空，只能是数字、字母、下划线'},
			{type: "newcolumn"},
			{type: "block", width: "120", offsetTop:"22", list:[
				{type: "button", name: "save", value: "保存", width:100}
			]}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
var ST_form;
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
            loadForm(subform, url);
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                var name = subform.getItemValue("name");
                var code = subform.getItemValue("code");
                var result = eval("("
                        + loadJson(MODEL_URL + "!validateFields.json?id=" + id + "&name=" + encodeURIComponent(name)
                                + "&code=" + encodeURIComponent(code) + "&menuId=" + nodeId) + ")");
                if (result.nameExist) {
                    dhtmlx.message(getMessage("form_field_exist", "名称"));
                    return;
                }
                if (result.codeExist) {
                    dhtmlx.message(getMessage("form_field_exist", "编码"));
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
                });
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
    QUERY_URL = DEFAULT_QUERY_URL;
    if (param) {
        QUERY_URL = DEFAULT_QUERY_URL + "&" + param;
    } else {
        ST_form.setItemValue("value", "");
        ST_form.setItemValue("columnName", "");
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
    dhxLayout = new dhtmlXLayoutObject("content", "1C");
    dhxLayout.cells("a").hideHeader();
    dhxLayout.setAutoSize();
    toolBar = dhxLayout.cells("a").attachToolbar();
    statusBar = dhxLayout.cells("a").attachStatusBar();
    initSelfToolBar();
    dataGrid = dhxLayout.cells("a").attachGrid();
    initSelfGrid(true);
    dataGrid.attachEvent("onDrag", function(sId, tId) {
        if (sId.indexOf(",") != -1) {
            dhtmlx.message("只能拖动一条记录!");
            return false;
        }
        return true;
    });
    dataGrid.attachEvent("onDrop", function(sId, tId) {
        loadJson(MODEL_URL + "!sort.json?start=" + sId + "&end=" + tId + "&menuId=" + nodeId);
        refreshGrid();
    });
    refreshGrid();
}