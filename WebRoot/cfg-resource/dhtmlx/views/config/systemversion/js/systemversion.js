MODEL_URL = contextPath + "/systemversion/system-version";
var tabbar, versionGrid, versionToolbar, versionStatusBar, versionResourceGrid, versionResourceGridData, versionResourceToolbar, versionResourceGridSize;
var versionIds; // 资源授予列表中版本ID列数组
var versionFormData = [{
    type : "settings",
    labelWidth : 120,
    inputWidth : 270,
    labelAlign : "right"
}, {
    type : "block",
    list : [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "systemId"},
            {type: "hidden", name: "isDefault", value:"0"},
            {type: "input", label: "版本名称:", name: "name", maxLength:100, required: true, tooltip: '版本名称不能为空'},
            {type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "remark", maxLength:500, rows:3},
            {type: "newcolumn"},
            {type: "button", name: "save", value: "保存", offsetLeft:5, offsetTop:55, width:80}
        ]}
    ]
}];
var versionGridData = {
    format: {
        headers: ["&nbsp;", "<center>名称</center>", "<center>备注</center>", ""],
        cols: ["id", "name", "remark"],
        userDatas: ["isDefault"],
        colWidths: ["30", "150", "300", "*"],
        colTypes: ["sub_row_form", "ro", "ro", "ro"],
        colAligns: ["right", "left", "left"],
        colTooltips: ["false", "true", "true", "false"]
    }
};
function initTabbar(tabbar) {
    tabbar.setImagePath(IMAGE_PATH);
    tabbar.enableAutoReSize();
    tabbar.addTab("versionLabel", "版本管理", "150px");
    tabbar.addTab("versionResourceLabel", "资源授予", "150px");
    versionGrid = tabbar.cells("versionLabel").attachGrid();
    versionToolbar = tabbar.cells("versionLabel").attachToolbar();
    initVersionToolBar(versionToolbar);
    versionStatusBar = tabbar.cells("versionLabel").attachStatusBar();
    initVersionSelfGrid(versionGrid);
    tabbar.setTabActive("versionLabel");
    tabbar.attachEvent("onSelect", function(id, last_id) {
        if (id == "versionResourceLabel") {
            if (!versionResourceToolbar) {
                versionResourceToolbar = tabbar.cells("versionResourceLabel").attachToolbar();
                initResourceVersionToolBar(versionResourceToolbar);
            }
            versionResourceGrid = tabbar.cells("versionResourceLabel").attachGrid();
            versionResourceGridData = loadJson(MODEL_URL + "!getVersionResourceGridFormat.json?systemId=" + currentTreeNodeId);
            initVersionResourceSelfGrid(versionResourceGrid);
            var versionResourceGridURL = MODEL_URL + "!getVersionResourceGridData.json?systemId=" + currentTreeNodeId;
            loadVersionResourceGridData(versionResourceGrid, versionResourceGridURL);
        }
        return true;
    });
}
/**
 * 初始化版本列表
 */
function initVersionSelfGrid(versionGrid) {
    versionGrid.setImagePath(IMAGE_PATH);
    versionGrid.setHeader(versionGridData.format.headers.toString());
    versionGrid.setInitWidths(versionGridData.format.colWidths.toString());
    versionGrid.setColTypes(versionGridData.format.colTypes.toString());
    versionGrid.setColAlign(versionGridData.format.colAligns.toString());
    if (versionGridData.format.colTooltips) {
        versionGrid.enableTooltips(versionGridData.format.colTooltips.toString());
    }
    versionGrid.setSkin(Skin);
    versionGrid.init();
    versionGrid.enableMultiselect(true);
    versionGrid.enableDragAndDrop(true);
    versionGrid.attachEvent("onBeforeDrag", function(id) {
        return this.cells(id, 1).getValue();
    });
    versionGrid.attachEvent("onDrag", function(sId, tId) {
        if (sId.indexOf(",") != -1) {
            dhtmlx.message(getMessage("drag_one_record"));
            return false;
        }
        return true;
    });
    versionGrid.attachEvent("onDrop", function(sId, tId) {
        loadJson(MODEL_URL + "!sort.json?start=" + sId + "&end=" + tId);
        refreshVersionGrid();
    });
    versionGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    versionGrid.attachEvent("onRowSelect", function(rId, cInd) {
        versionGrid.cells(rId, 0).open();
    });
    versionGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = versionFormData;
    });
    versionGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        if (id != "") {
            var url = MODEL_URL + "/" + id + ".json?_method=get";
            var formData = loadJson(url);
            loadFormData(subform, formData);
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                if (!subform.validate()) {
                    return;
                }
                var id = subform.getItemValue("id");
                var name = subform.getItemValue("name");
                var result = eval("(" + loadJson(MODEL_URL + "!validateSystemVersion.json?id=" + id + "&systemId=" + currentTreeNodeId + "&name=" + encodeURIComponent(name)) + ")");
                if (result.nameExist) {
                    dhtmlx.message("该版本名称已经存在，请修改！");
                    return;
                }
                if (id == "") {
                    SAVE_URL = MODEL_URL + ".json";
                    subform.setItemValue("_method", "post");
                } else {
                    SAVE_URL = MODEL_URL + "/" + id + ".json";
                    subform.setItemValue("_method", "put");
                }
                subform.setItemValue("systemId", currentTreeNodeId);
                subform.send(SAVE_URL, "post", function(loader, response) {
                    dhtmlx.message(getMessage("save_success"));
                    refreshVersionGrid();
                });
            }
        });
    });
    versionGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            versionGrid.forEachRow(function(rId) {
                if (id != rId) {
                    versionGrid.cells(rId, 0).close();
                }
            });
        }
    });
    if (pageable) {
        pagesize = getCookie("pagesize") || PAGE_SIZE;
        versionGrid.enablePaging(true, pagesize, 1, versionStatusBar);
        versionGrid.setPagingSkin('toolbar', Skin);
    }
}
/**
 * 刷新版本列表
 */
function refreshVersionGrid() {
    QUERY_URL = MODEL_URL + "!search.json?Q_EQ_systemId=" + currentTreeNodeId + "&P_orders=isDefault,desc,showOrder";
    search(versionGrid, versionGridData);
}
/**
 * 初始化版本列表工具条
 */
function initVersionToolBar(toolBar) {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("add", 0, "新增", "new.gif");
    toolBar.addSeparator("septr$01", 2);
    toolBar.addButton("delete", 3, "删除", "delete.gif");
    toolBar.addSeparator("septr$04", 4);
    toolBar.addButton("refresh", 5, "刷新", "refresh.gif");
    toolBar.attachEvent('onClick', function(id) {
        if (id == "add") {
            versionGrid.addSubRow();
        } else if (id == "delete") {
            var selectId = versionGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                refreshVersionGrid();
                return;
            }
            var selectIdArr = selectId.split(",");
            for (var i in selectIdArr) {
                if (versionGrid.getUserData(selectIdArr[i], "isDefault") == "1") {
                    dhtmlx.message('"' + versionGrid.cells(selectIdArr[i], 1).getValue() + '"是默认版本，不能删除！');
                    return;
                }
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        dhtmlxAjax.get(MODEL_URL + "/" + selectId + "?_method=delete", function() {
                        	dhtmlx.message(getMessage("delete_success"));
                            refreshVersionGrid();
                            tree.refreshItem(currentTreeNodeId);
                        });
                    }
                }
            });
        } else if (id == "refresh") {
            refreshVersionGrid();
        }
    });
}
/**
 * 初始化版本资源列表
 */
function initVersionResourceSelfGrid(versionResourceGrid) {
    versionIds = versionResourceGridData.format.versionIds;
    versionResourceGrid.setImagePath(IMAGE_PATH);
    versionResourceGrid.setHeader(versionResourceGridData.format.headers.toString());
    versionResourceGrid.setInitWidths(versionResourceGridData.format.colWidths.toString());
    versionResourceGrid.setColTypes(versionResourceGridData.format.colTypes.toString());
    versionResourceGrid.setColAlign(versionResourceGridData.format.colAligns.toString());
    versionResourceGrid.setSkin(Skin);
    versionResourceGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    versionResourceGrid.init();
    versionResourceGrid.attachEvent("onCheck", function(rId, cInd, state) {
        if (cInd == 1) {
            dhtmlx.message('"默认版本"的资源不允许更改！');
            this.cells(rId, cInd).setValue("1");
            return;
        }
        var resourceName = this.cells(rId, '0').getValue();
        var rInd = this.getRowIndex(rId);
        var stepLevel = getStepLevel(resourceName);
        if (state) {
            // 将上层节点都选中
            if (rInd != 0) {
                var temStepLevel = stepLevel;
                for (var i=rInd-1; i>=0; i--) {
                    if (getStepLevel(this.cellByIndex(i, '0').getValue()) == temStepLevel-1) {
                        temStepLevel--;
                        this.cellByIndex(i, cInd).setValue("1");
                    }
                }
            }
            // 下层节点都不选中
            for (var i=rInd+1; i<=versionResourceGridSize-1; i++) {
                var nextStepLevel = getStepLevel(this.cellByIndex(i, '0').getValue());
                if (nextStepLevel == stepLevel) {
                    break;
                }
                if (nextStepLevel > stepLevel) {
                    this.cellByIndex(i, cInd).setValue("1");
                }
            }
        } else {
            // 下层节点都不选中
            for (var i=rInd+1; i<=versionResourceGridSize-1; i++) {
                var nextStepLevel = getStepLevel(this.cellByIndex(i, '0').getValue());
                if (nextStepLevel == stepLevel) {
                    break;
                }
                if (nextStepLevel > stepLevel) {
                    this.cellByIndex(i, cInd).setValue("0");
                }
            }
        }
    });
}
function getStepLevel(str) {
    var level = 0;
    var step = "　  ";
    var trimStr = str.trim();
    var stepAll = str.replace(trimStr, "");
    if (stepAll != "") {
        var steps = "";
        for (var i=0; i<10; i++) {
            steps += step;
            if (steps == stepAll) {
                level = i + 1;
            }
        }
    }
    return level;
}
/**
 * 初始化版本资源列表工具条
 */
function initResourceVersionToolBar(toolBar) {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("save", 0, "保存", "save.gif");
    toolBar.attachEvent('onClick', function(id) {
        if (id == "save") {
            if (versionIds.length == 2) {
                dhtmlx.message('不存在"默认版本"外的其他版本，无需保存!');
                return;
            }
            var resouceIdsArray = new Array();
            for (var i=2; i<versionIds.length; i++) {
                resouceIdsArray[i-2] = versionIds[i];
            }
            versionResourceGrid.forEachRow(function(id) {
                for (var i=2; i<versionIds.length; i++) {
                    if (this.cells(id, i).getValue() == "1") {
                        resouceIdsArray[i-2] += "," + id;
                    }
                }
            });
            var checkedIds = "";
            for (var i=0; i<resouceIdsArray.length; i++) {
                checkedIds += resouceIdsArray[i] + ";";
            }
            checkedIds = checkedIds.substring(0, checkedIds.length-1);
            var params = "systemId=" + currentTreeNodeId + "&checkedIds=" + checkedIds;
            dhtmlxAjax.post(MODEL_URL + "!saveVersionResource.json", params, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == 'string') {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message(getMessage("save_success"));
                } else {
                    dhtmlx.message(getMessage("save_failure"));
                }
            });
        }
    });
}
/**
 * 加载版本资源列表GridData
 * @param {dhtmlxGrid} grid
 * @param {string} url
 */
function loadVersionResourceGridData(grid, url) {
    grid.clearAll();
    var dataJson = loadJson(url);
    var jsonArray = dataJson.data ? dataJson.data : dataJson;
    var datas = {};
    datas.rows = [];
    for (var i = 0; i < jsonArray.length; i++) {
        var row = {};
        row.id = jsonArray[i][0];
        row.data = [];
        for (var j = 1; j < jsonArray[i].length; j++) {
            row.data[j - 1] = jsonArray[i][j];
        }
        datas.rows[i] = row;
    }
    grid.parse(datas, "json");
    versionResourceGridSize = datas.rows.length;
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
	tree.setDataMode("json");
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	tree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
    var treeJson = {id:0, item:[{id:-1,text:"版本", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif",item:[]}]};
    tree.setDataMode("json");
    tree.enableSmartXMLParsing(true);
    tree.setXMLAutoLoading(contextPath + "/menu/menu!getRootMenuTree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentId&P_orders=showOrder");
    tree.loadJSONObject(treeJson);
    tree.refreshItem("-1");
	tree.attachEvent("onClick", function(id) {
		currentTreeNodeId = "" + id;
		if (id == "-1") {
            if (tabbar) {
                dhxLayout.cells("b").detachObject();
                tabbar = null;
            }
            dhxLayout.cells("b").showHeader();
            dhxLayout.cells("b").setText("操作说明");
            dhxLayout.cells("b").attachObject(createHelpDiv());
        } else {
            dhxLayout.cells("b").hideHeader();
            if (!tabbar) {
                tabbar = dhxLayout.cells("b").attachTabbar();
                initTabbar(tabbar);
            } else {
                tabbar.setTabActive("versionLabel");
            }
            refreshVersionGrid();
        }
	});
	tree.selectItem("-1", true);
}
/**
 * 创建模块说明
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
            + "<p><b>版本模块说明：</b><br></p> \n"
            + "<p>1. 系统节点对应的列表为该系统的版本<br></p> \n"
            + "<p>2. 资源授予标签页中配置系统版本与资源的关联关系<br></p> \n"
            + "</li> \n"
            + "</ul> \n";
    }
    return obj;
}