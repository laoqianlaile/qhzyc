COMPONENT_ASSEMBLE_AREA_MODEL_URL = contextPath + "/component/component-assemble-area";
CONSTRUCT_MODEL_URL = contextPath + "/construct/construct";
CONSTRUCT_INPUT_PARAM_MODEL_URL = contextPath + "/construct/construct-input-param";
CONSTRUCT_SELF_PARAM_MODEL_URL = contextPath + "/construct/construct-self-param";
CONSTRUCT_DETAIL_SELF_PARAM_MODEL_URL = contextPath + "/construct/construct-detail-self-param";
CONSTRUCT_FUNCTION_MODEL_URL = contextPath + "/construct/construct-function";
CONSTRUCT_CALLBACK_MODEL_URL = contextPath + "/construct/construct-callback";
CONSTRUCT_SYSTEM_PARAMETER_MODEL_URL = contextPath + "/construct/construct-system-parameter";
CONSTRUCT_DETAIL_MODEL_URL = contextPath + "/construct/construct-detail";
COMPONENT_MODEL_URL = contextPath + "/component/component";
COMPONENT_VERSION_MODEL_URL = contextPath + "/component/component-version";
CONSTRUCT_FILTER_MODEL_URL = contextPath + "/construct/construct-filter";
CONSTRUCT_FILTER_DETAIL_MODEL_URL = contextPath + "/construct/construct-filter-detail";
TREE_URL = CONSTRUCT_MODEL_URL;
// 构件组装主界面layout、构件绑定预留区Layout
var constructLayout, constructDetailLayout;
// 预留区树
var reserveZoneTree;
// 树中选中节点的组合构件版本ID、树中选中节点的ConstructID、树中选中节点中的基础构件的构件类型、树中选中节点中的基础构件版本ID
var currentAssembleComponentVersionIdOfTree, currentConstructIdOfTree, baseComponentVersionType, currentBaseComponentVersionIdOfTree;
// 当前选中的预留区ID、当前选中的预留区类型、是否是公用预留区、预留区名称、当前选中的树预留区的节点类型
var currentReserveZoneId, currentReserveZoneType, isCommonReserveZone, currentReserveZoneName, currentTreeNodeType;
// 当前选中的一条 组合构件配置关系ID
var currentConstructId;
// 基础构件自身配置layout、基础构件输入参数配置layout
var baseSelfConfigLayout, baseInputParamConfigLayout;
// 基础构件的自身配置列表、基础构件输入配置列表
var baseSelfConfigGrid, baseInputParamConfigGrid;
// 预留区关联构件的列表工具条、列表
var constructDetailGridToolbar, constructDetailGrid;
// 预留区和构件绑定关系表单
var constructDetailForm;
// 绑定预留区的构件的自身配置layout、参数配置layout、绑定预留区的构件关闭时调用的方法配置layout
var selfConfigLayout, paramConfigLayout, callbackConfigLayout;
// 绑定预留区的构件的自身配置列表
var selfConfigGrid;
// 绑定预留区的构件的参数配置列表
var paramFunctionDataGrid, paramInputGrid, paramConfigGrid;
// 绑定预留区的构件关闭时调用的方法配置列表
var paramOutputGrid, callbackParamGrid, callbackConfigGrid;
/**
 * 初始化绑定预留区的构件的参数配置Grid
 * @param {dhtmlxGrid} grid
 * @param {obj} gridcfg
 */
function initParamGrid(grid, gridcfg) {
    grid.setImagePath(IMAGE_PATH);
    grid.setHeader(gridcfg.format.headers.toString());
    grid.setInitWidths(gridcfg.format.colWidths.toString());
    grid.setColTypes(gridcfg.format.colTypes.toString());
    grid.setColAlign(gridcfg.format.colAligns.toString());
    grid.setSkin(Skin);
    grid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    grid.init();
}
/**
 * 加载构件的参数配置GridData
 * @param {dhtmlxGrid} grid
 * @param {string} url
 */
function loadParamGridData(grid, url) {
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
}
/**
 * 创建构件组织模块说明
 * @return {div}
 */
function createHelpDiv() {
    var obj = document.getElementById("DIV-help");
    if (null == obj) {
        obj = document.createElement("DIV");
        obj.setAttribute("id", "DIV-help");
        obj.setAttribute("style", "font-family: Tahoma; font-size: 11px;display: none;");
        obj.innerHTML = "<ul> \n" + "<li type=\"square\">" + "<p><b>构件组装模块说明：</b><br></p> \n"
                + "<p>1. 左侧为构件分类树，通过右键菜单来新增、修改、删除构件分类<br></p> \n"
                + "<p>2. 逻辑表与按钮预设是用于配置公用预留区上的按钮<br></p> \n"
                + "<p>3. 点击构件组装节点时，右侧页面是操作说明<br></p> \n"
                + "<p>4. 点击构件分类节点时，右侧页面是组合构件列表，用于新增、修改、删除及预览构件<br></p> \n"
                + "<p>5. 点击组合构件节点时，右侧页面用于在该组合构件的预留区上组装构件<br></p> \n" + "</li> \n" + "</ul> \n";
    }
    return obj;
}
/**
 * 刷新构件列表
 */
function refreshComponentVersionGrid(param) {
    var url = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_EQ_assembleAreaId=" + tree.getSelectedItemId()
            + "&Q_EQ_component.type=9&P_orders=componentName,version";
    if (param) {
	    url += "&" + param;
	} else {
	    ST_form.setItemValue("value", "");
	}
    search(componentGrid, componentGridData, url);
}
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
        	var result = eval("(" + loadJson(COMPONENT_ASSEMBLE_AREA_MODEL_URL + "!validateFields.json?id=" + id + "&name=" + encodeURIComponent(name) + "&parentId=" + parentId) + ")");
        	if (result.nameExist) {
        		dhtmlx.message(getMessage("form_field_exist", "构件分类名称"));
        		return;
        	}
        	if (id == "") {
        		SAVE_URL = COMPONENT_ASSEMBLE_AREA_MODEL_URL;
        		componentAreaForm.setItemValue("_method", "post");
        	} else {
        		SAVE_URL = COMPONENT_ASSEMBLE_AREA_MODEL_URL + "/" + id;
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
/**
 * 初始化页面方法
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
    // 初始右键菜单
	var treeMenu = new dhtmlXMenuObject();
	treeMenu.renderAsContextMenu();
	treeMenu.loadXMLString("<menu><item id='1' text='新增构件分类'/><item id='2' text='修改构件分类'/><item id='3' text='删除构件分类'/></menu>");
	treeMenu.attachEvent("onClick", function(id) {
		MODEL_URL = COMPONENT_ASSEMBLE_AREA_MODEL_URL;
		GWIN_WIDTH = 400;
		GWIN_HEIGHT = 240;
		if (id == "1") {
			componentAreaForm = openNewWindow(componentAreaFormData, initComponentAreaFormToolbar, 400, 200);
			componentAreaForm.setItemValue("parentId", contextMenuNodeId);
		} else if (id == "2") {
			componentAreaForm = openEditWindow(componentAreaFormData, contextMenuNodeId, initComponentAreaFormToolbar, 400, 200);
		} else if (id == "3") {
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
    tree.setXMLAutoLoading(TREE_URL + "!getConstructTree.json?E_model_name=tree");
    var treeJson = {
        id : 0,
        item : [{
            id : 'CommonBinding',
            text : "逻辑表与按钮预设",
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            open : true,
            item : []
        }, {
            id : -1,
            text : "构件组装",
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            open : true,
            item : []
        }]
    };
    tree.loadJSONObject(treeJson);
    tree.refreshItem("-1");
    tree.enableDragAndDrop(true, false);
	tree.setDragBehavior("complex", true);
	tree.attachEvent("onDrag", function(sId, tId, id){
        if (tId == "CommonBinding") {
            dhtmlx.message("不能放置到预设节点上！");
        	return false;
        }
	    var sTreeNodeType = tree.getAttribute(sId, "prop0");
	    var tTreeNodeType = tree.getAttribute(tId, "prop0");
	    if (sTreeNodeType == "Component") {
	        if (tree.getParentId(sId) == tId) {
	            dhtmlx.message("构件节点只支持改变分类，不支持分类内排序！");
	            return false;
	        }
	    }
        if (tTreeNodeType == "Component") {
            dhtmlx.message("不能放置到构件节点上！");
        	return false;
        }
        return true;
	});
	tree.attachEvent("onDrop", function(sId, tId, id){
		var url = CONSTRUCT_MODEL_URL + "!treeSort.json?start=" + sId + "&targetId=" + tId;
		if (id) {
			url += "&end=" + id;
		}
		var result = loadJson(url);
		dhtmlx.message(result);
	});
    tree.attachEvent("onClick", function(id) {
        currentTreeNodeId = id;
        if (id == "CommonBinding") {
            currentAssembleComponentVersionIdOfTree = null;
            currentConstructIdOfTree = null;
            baseComponentVersionType = null;
            currentBaseComponentVersionIdOfTree = null;
            initConstructDetailLayout(true);
        } else if (id == "-1") {
            if (constructLayout) {
                constructLayout = null;
            }
            if (constructDetailLayout) {
                constructDetailLayout = null;
            }
            currentAssembleComponentVersionIdOfTree = null;
            currentConstructIdOfTree = null;
            baseComponentVersionType = null;
            currentBaseComponentVersionIdOfTree = null;
            dhxLayout.cells("b").showHeader();
            dhxLayout.cells("b").setText("操作说明");
            dhxLayout.cells("b").attachObject(createHelpDiv());
        } else {
            var treeNodeType = tree.getAttribute(id, "prop0");
            if (treeNodeType == "Area") {
                if (constructDetailLayout) {
                    constructDetailLayout = null;
                }
                currentAssembleComponentVersionIdOfTree = null;
                currentConstructIdOfTree = null;
                baseComponentVersionType = null;
                currentBaseComponentVersionIdOfTree = null;
                if (!constructLayout) {
                    constructLayout = dhxLayout.cells("b").attachLayout("1C");
                    constructLayout.cells("a").hideHeader();
                    constructLayout.setAutoSize();
                    toolBar = constructLayout.cells("a").attachToolbar();
                    statusBar = constructLayout.cells("a").attachStatusBar();
                    initComponentToolBar();
                    componentGrid = constructLayout.cells("a").attachGrid();
                    var typeCombo = componentGrid.getCombo(5);
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
                    var isPackageCombo = componentGrid.getCombo(7);
                    isPackageCombo.put("0", "否");
                    isPackageCombo.put("1", "是");
                    initComponentGrid();
                }
                refreshComponentVersionGrid();
            } else if (treeNodeType == "Component") {
                currentAssembleComponentVersionIdOfTree = id;
                currentConstructIdOfTree = tree.getAttribute(id, "prop1");
                baseComponentVersionType = tree.getAttribute(id, "prop2");
                currentBaseComponentVersionIdOfTree = tree.getAttribute(id, "prop3");
                initConstructDetailLayout(false);
            }
        }
    });
    tree.attachEvent("onBeforeContextMenu", function(nId) {
		contextMenuNodeId = nId;
		if (nId == "-1") {
			treeMenu.showItem("1");
			treeMenu.hideItem("2");
			treeMenu.hideItem("3");
		} else if (nId == "CommonBinding" || tree.getAttribute(nId, "prop0") == "Component") {
			treeMenu.hideItem("1");
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
/**
 * 初始化构件绑定预留区Layout
 * @param isCommonBinding true:是逻辑表与按钮预设 false:组合构件中绑定
 */
function initConstructDetailLayout(isCommonBinding) {
    if (constructLayout) {
        constructLayout = null;
    }
    if (constructDetailLayout) {
        constructDetailLayout = null;
    }
    constructDetailLayout = dhxLayout.cells("b").attachLayout("2U");
    constructDetailLayout.cells("a").hideHeader();
    constructDetailLayout.cells("b").hideHeader();
    constructDetailLayout.cells("a").setWidth(240);
    constructDetailLayout.setAutoSize();
    reserveZoneTree = constructDetailLayout.cells("a").attachTree();
    reserveZoneTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    reserveZoneTree.attachEvent("onMouseIn", function(id) {
        reserveZoneTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    reserveZoneTree.attachEvent("onMouseOut", function(id) {
        reserveZoneTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    reserveZoneTree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
    reserveZoneTree.setDataMode("json");
    reserveZoneTree.enableSmartXMLParsing(true);
    reserveZoneTree.setXMLAutoLoading(TREE_URL
            + "!getReserveZoneTree.json?E_model_name=tree&assembleComponentVersionId="
            + currentAssembleComponentVersionIdOfTree);
    var reserveZoneTreeJson;
    if (isCommonBinding) {
        reserveZoneTreeJson = {
            id : 0,
            item : [{
                id : 'Common',
                text : "公用预留区",
                im0 : "safe_close.gif",
                im1 : "safe_open.gif",
                im2 : "safe_close.gif",
                open : true,
                item : []
            }]
        };
        reserveZoneTree.loadJSONObject(reserveZoneTreeJson);
        reserveZoneTree.refreshItem("Common");
    } else {
        if (baseComponentVersionType == "3") {
            // 基础构件是树构件
            reserveZoneTreeJson = {
                id : 0,
                item : [{
                    id : 'TREE',
                    text : "树预留区（公用配置）",
                    im0 : "safe_close.gif",
                    im1 : "safe_open.gif",
                    im2 : "safe_close.gif",
                    open : true,
                    item : [{
                        id : 'T_ROOT',
                        text : "根节点",
                        im0 : "folderClosed.gif",
                        im1 : "folderOpen.gif",
                        im2 : "folderClosed.gif",
                        open : true,
                        item : []
                    }, {
                        id : 'T_EMPTY',
                        text : "空节点",
                        im0 : "folderClosed.gif",
                        im1 : "folderOpen.gif",
                        im2 : "folderClosed.gif",
                        open : true,
                        item : []
                    }, {
                        id : 'T_COLUMN_EMP',
                        text : "字段节点（跨表）",
                        im0 : "folderClosed.gif",
                        im1 : "folderOpen.gif",
                        im2 : "folderClosed.gif",
                        open : true,
                        item : []
                    }, {
                        id : 'T_TABLE_GROUP',
                        text : "物理表组节点",
                        im0 : "folderClosed.gif",
                        im1 : "folderOpen.gif",
                        im2 : "folderClosed.gif",
                        open : true,
                        item : []
                    }, {
                        id : 'T_TABLE',
                        text : "表节点",
                        im0 : "folderClosed.gif",
                        im1 : "folderOpen.gif",
                        im2 : "folderClosed.gif",
                        open : true,
                        item : []
                    }]
                }]
            };
            reserveZoneTree.loadJSONObject(reserveZoneTreeJson);
            reserveZoneTree.setAttribute("TREE", "prop0", "TREE");
            reserveZoneTree.setAttribute("T_ROOT", "prop0", "TREE");
            reserveZoneTree.setAttribute("T_EMPTY", "prop0", "TREE");
            reserveZoneTree.setAttribute("T_COLUMN_EMP", "prop0", "TREE");
            reserveZoneTree.setAttribute("T_TABLE", "prop0", "TREE");
            reserveZoneTree.setAttribute("T_TABLE_GROUP", "prop0", "TREE");
            reserveZoneTree.setUserData("TREE", "treeNodeType", "TREE");
            reserveZoneTree.setUserData("T_ROOT", "treeNodeType", "0");
            reserveZoneTree.setUserData("T_EMPTY", "treeNodeType", "1");
            reserveZoneTree.setUserData("T_COLUMN_EMP", "treeNodeType", "4");
            reserveZoneTree.setUserData("T_TABLE", "treeNodeType", "2");
            reserveZoneTree.setUserData("T_TABLE_GROUP", "treeNodeType", "5");
        } else if (baseComponentVersionType == "5") {
            // 基础构件是逻辑表构件
            reserveZoneTreeJson = {
                id : 0,
                item : [{
                    id : 'Common',
                    text : "公用预留区",
                    im0 : "safe_close.gif",
                    im1 : "safe_open.gif",
                    im2 : "safe_close.gif",
                    open : true,
                    item : []
                }]
            };
            reserveZoneTree.loadJSONObject(reserveZoneTreeJson);
            reserveZoneTree.refreshItem("Common");
        } else {
            // 基础构件是页面构件 或 物理表构件
            reserveZoneTreeJson = {
                id : 0,
                item : [{
                    id : 'Component',
                    text : "构件预留区",
                    im0 : "safe_close.gif",
                    im1 : "safe_open.gif",
                    im2 : "safe_close.gif",
                    open : true,
                    item : []
                }]
            };
            reserveZoneTree.loadJSONObject(reserveZoneTreeJson);
            reserveZoneTree.refreshItem("Component");
        }
    }
    reserveZoneTree.attachEvent("onClick", function(id) {
        if (id == "Common" || id == "Component") {
            lockConstructDetailConfig();
            currentReserveZoneType = null;
            changeConstructDetailGrid();
            currentTreeNodeType = null;
        } else {
            currentReserveZoneType = reserveZoneTree.getAttribute(id, "prop0");
            currentReserveZoneName = reserveZoneTree.getAttribute(id, "prop1");
            if (reserveZoneTree.getParentId(id) == "Common") {
                isCommonReserveZone = "1";
                if (isCommonBinding) {
                    showConstructDetailToolbar("CommonBinding");
                } else {
                    showConstructDetailToolbar("ComponentCommon");
                }
                currentReserveZoneId = id;
                currentTreeNodeType = null;
            } else if (reserveZoneTree.getParentId(id) == "Component") {
                isCommonReserveZone = "0";
                showConstructDetailToolbar("Component");
                currentReserveZoneId = id;
                currentTreeNodeType = null;
            } else {
            	isCommonReserveZone = "0";
                showConstructDetailToolbar("TREE");
                currentReserveZoneId = "TREE";
                currentTreeNodeType = reserveZoneTree.getUserData(id, "treeNodeType");
                initTreeNodeTypeComboOfGrid();
                initTreeNodePropertyComboOfGrid();
            }
            changeConstructDetailGrid();
            constructDetailGridLoadData();
        }
    });
    constructDetailGridToolbar = constructDetailLayout.cells("b").attachToolbar();
    initConstructDetailGridToolbar();
    constructDetailGrid = constructDetailLayout.cells("b").attachGrid();
    var buttonTypeCombo = constructDetailGrid.getCombo(4);
    buttonTypeCombo.put("0", "一级按钮");
    buttonTypeCombo.put("1", "按钮组");
    buttonTypeCombo.put("2", "二级按钮");
    initPositionComboOfGrid();
    initConstructDetailGrid();
    currentReserveZoneType = null;
    changeConstructDetailGrid();
    lockConstructDetailConfig();
}
/**
 * 锁定预留区和构件绑定关系区域
 */
function lockConstructDetailConfig() {
    constructDetailGridToolbar.hideItem("newDefault");
    constructDetailGridToolbar.hideItem("new");
    constructDetailGridToolbar.hideItem("config");
    constructDetailGridToolbar.hideItem("delete");
    constructDetailGridToolbar.hideItem("copy");
    constructDetailGridToolbar.hideItem("sync");
    constructDetailGridToolbar.hideItem("syncTo");
    constructDetailGridToolbar.hideItem("comboboxSearch");
    constructDetailGrid.clearAll();
}
/**
 * 控制预留区和构件绑定关系工具条上的按钮显示
 */
function showConstructDetailToolbar(type) {
    if (type == "CommonBinding") {
        constructDetailGridToolbar.showItem("newDefault");
        if (currentReserveZoneType == "0") {
            if ("GRID" == getToolbarReserveType(currentReserveZoneName)) {
                constructDetailGridToolbar.showItem("comboboxSearch");
            } else {
                constructDetailGridToolbar.hideItem("comboboxSearch");
            }
        } else {
            constructDetailGridToolbar.hideItem("comboboxSearch");
        }
        constructDetailGridToolbar.showItem("new");
        constructDetailGridToolbar.showItem("copy");
        constructDetailGridToolbar.hideItem("sync");
        constructDetailGridToolbar.showItem("syncTo");
    } else if (type == "ComponentCommon") {
        constructDetailGridToolbar.hideItem("newDefault");
        if (currentReserveZoneType == "0") {
            if ("GRID" == getToolbarReserveType(currentReserveZoneName)) {
            	constructDetailGridToolbar.showItem("comboboxSearch");
            } else {
                constructDetailGridToolbar.hideItem("comboboxSearch");
            }
        } else {
            constructDetailGridToolbar.hideItem("comboboxSearch");
        }
        constructDetailGridToolbar.hideItem("new");
        constructDetailGridToolbar.hideItem("copy");
        constructDetailGridToolbar.showItem("sync");
        constructDetailGridToolbar.showItem("syncTo");
        if (currentReserveZoneType == "2") {
        	lockConstructDetailConfig();
        	constructDetailGridToolbar.showItem("new");
            constructDetailGridToolbar.showItem("config");
            constructDetailGridToolbar.showItem("delete");
            constructDetailGridToolbar.showItem("copy");
		}
    } else if (type == "Component") {
        if (baseComponentVersionType == "4" || baseComponentVersionType == "6") {
            if (currentReserveZoneType == "0") {
                constructDetailGridToolbar.showItem("newDefault");
                if ("GRID" == getToolbarReserveType(currentReserveZoneName)) {
                    constructDetailGridToolbar.showItem("comboboxSearch");
                } else {
                    constructDetailGridToolbar.hideItem("comboboxSearch");
                }
            } else if (currentReserveZoneType == "1") {
                constructDetailGridToolbar.showItem("newDefault");
                constructDetailGridToolbar.hideItem("comboboxSearch");
            } else {
                constructDetailGridToolbar.hideItem("newDefault");
                constructDetailGridToolbar.hideItem("comboboxSearch");
            }
        } else {
            constructDetailGridToolbar.hideItem("newDefault");
            constructDetailGridToolbar.hideItem("comboboxSearch");
        }
        constructDetailGridToolbar.showItem("new");
        constructDetailGridToolbar.showItem("copy");
        constructDetailGridToolbar.hideItem("sync");
        constructDetailGridToolbar.hideItem("syncTo");
    } else if (type == "TREE") {
        constructDetailGridToolbar.hideItem("newDefault");
        constructDetailGridToolbar.showItem("new");
        constructDetailGridToolbar.hideItem("copy");
        constructDetailGridToolbar.hideItem("sync");
        constructDetailGridToolbar.hideItem("syncTo");
    }
    constructDetailGridToolbar.showItem("config");
    constructDetailGridToolbar.showItem("delete");
}
/**
 * 判断工具条预留区类型，列表或表单
 */
function getToolbarReserveType(reserveZoneName) {
    var strs = reserveZoneName.split("_");
    var isCommon = (strs.length <= 4);
    if (isCommon) {
        if (reserveZoneName.indexOf("FORM") != -1) {
            return "FROM";
        } else if (reserveZoneName.indexOf("GRID") != -1) {
            return "GRID";
        }
    } else {
        if (strs && strs.length && strs.length > 4) {
            if (strs[3] == "0") {
                return "FROM";
            } else if (strs[3] == "1") {
                return "GRID";
            }
        }
    }
    return null;
}