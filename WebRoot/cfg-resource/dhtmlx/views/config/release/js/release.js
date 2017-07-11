MODEL_URL = contextPath + "/release/release";
DETAIL_MODEL_URL = contextPath + "/release/release-detail";
SYSTEM_VERSION_MODEL_URL = contextPath + "/systemversion/system-version";
// 发布表单、树、列表、分页条
var releaseGrid, releaseStatusBar;
var releaseSystemForm;
var releaseUpdatePackageForm, releaseUpdatePackageTree;
var currentRootMenuId, currentRootMenuText;
var releaseTabbar, releaseUpdatePackageFormCell, releaseUpdatePackageTreeCell;
var menuTreeNodeId, componentVersionId, rootMenuId, rootMenuText;
var ST_form;
var releaseGridData = {
    format: {
        headers: ["<center>类型</center>", "<center>版本号</center>", "<center>版本</center>", "<center>发布时间</center>", "<center>发布说明</center>", ""],
        cols: ["type", "version", "systemVersionId", "releaseDate", "remark"],
        colWidths: ["150", "150", "150", "150", "300", "*"],
        colTypes: ["co", "ro", "co", "ro", "ro", "ro"],
        colAligns: ["left", "left", "left", "left", "left"],
        colTooltips: ["true", "true", "true", "true", "true", "false"]
    }
};
var releaseSystemFormData = {
    format: [
        {type: "block", name: "formBlock", width: "700", offsetLeft: "20", offsetTop: "5", list:[
            {type: "block", width: "650", list:[
                {type: "hidden", name: "rootMenuId"},
                {type: "hidden", name: "type", value: "0"},
                {type: "input", label: "系统名称：&nbsp;&nbsp;&nbsp;", labelWidth: "110", name: "rootMenuText", maxLength:50, readonly: true, width: 200},
                {type: "combo", label: "系统版本：", name: "systemVersionId", labelWidth: "110", width:202, showAll: true, required: true}
            ]},
			{type: "block", name: "viewBlock", width: "650", list:[
			    {type: "input", label: "系统版本号：", labelWidth: "110", name: "version", maxLength:200, required: true, width: 200, validate: "ValidateVersion", tooltip: '版本号格式为x.x.x.x，x为数字，例如1.0.0.0'},
			    {type: "newcolumn"},
			    {type: "itemlabel", name: "typeLabel", labelWidth: "280", label: "(版本号格式为x.x.x.x，x为数字，例如1.0.0.0)", labelAlign:"right"}
			]},
			{type: "block", width: "650", list:[
                {type: "input", label: "发布说明：&nbsp;&nbsp;&nbsp;", labelWidth: "110", name: "remark", maxLength:500, width: 500, rows: 5}
            ]}
        ]}
    ],
    settings: {labelWidth: 80, inputWidth: 160, offsetTop: "5"}
};
var releaseUpdatePackageFormData = {
        format: [
            {type: "block", name: "formBlock", width: "700", offsetLeft: "20", offsetTop: "5", list:[
                {type: "block", width: "650", list:[
                    {type: "hidden", name: "rootMenuId"},
                    {type: "hidden", name: "type", value: "1"},
                    {type: "input", label: "系统名称：&nbsp;&nbsp;&nbsp;", labelWidth: "110", name: "rootMenuText", maxLength:50, readonly: true, width: 200}
                ]},
                {type: "block", name: "viewBlock", width: "650", list:[
                   {type: "combo", label: "系统版本号：", labelWidth: "110", inputWidth: "200", name: "releaseSystemVersion", required: true, showAll: true, tooltip: '系统版本号不能为空'},
                   {type: "input", label: "更新包版本号：", labelWidth: "110", name: "version", maxLength:50, required: true, width: 200, validate: "ValidateVersion", tooltip: '版本号格式为x.x.x.x，x为数字，例如1.0.0.0'},
                   {type: "newcolumn"},
                   {type: "itemlabel", name: "typeLabel", labelWidth: "280", label: "(版本号格式为x.x.x.x，x为数字，例如1.0.0.0)", labelAlign:"right", offsetTop: 30}
                ]},
                {type: "block", width: "650", list:[
                    {type: "input", label: "发布说明：&nbsp;&nbsp;&nbsp;", labelWidth: "110", name: "remark", maxLength:500, width: 500, rows: 5}
                ]}
            ]}
        ],
        settings: {labelWidth: 80, inputWidth: 160, offsetTop: "5"}
    };
/**
 * 校验版本号
 */
function ValidateVersion(value) {
	var reg = /^\d+(.\d+)*$/;
	return reg.test(value);
}
/**
 * 初始化发布界面
 */
function initReleaseSystemLayout(win) {
    releaseSystemForm = win.attachForm(initDetailFormFormat(releaseSystemFormData));
    releaseSystemForm.setItemValue("rootMenuText", currentRootMenuText);
    var systemVersionIdCombo = releaseSystemForm.getCombo("systemVersionId");
    if (systemVersionIdCombo) {
        initSystemVersionIdCombo(systemVersionIdCombo);
    }
    var statusBar = win.attachStatusBar();
    var releaseToolbar = new dhtmlXToolbarObject(statusBar);
    initReleaseSystemFormToolbar(releaseToolbar);
}
/**
 * 初始化发布系统表单工具条
 * @param {dhtmlxToolbar} toolBar
 */
function initReleaseSystemFormToolbar(toolBar) {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("submit", 1, "&nbsp;&nbsp;确定&nbsp;&nbsp;");
    toolBar.addButton("cancel", 2, "&nbsp;&nbsp;取消&nbsp;&nbsp;");
    toolBar.setAlign("right");
    toolBar.attachEvent("onClick", function(buttonName) {
        if (buttonName == "cancel") {
            dhxWins.window(WIN_ID).close();
        } else if (buttonName == "submit") {
            var version = releaseSystemForm.getItemValue("version");
            var remark = releaseSystemForm.getItemValue("remark");
            var type = releaseSystemForm.getItemValue("type");
            var systemVersionId = releaseSystemForm.getItemValue("systemVersionId");
            if (!releaseSystemForm.validate()) {
                return;
            }
            releaseSystemForm.setItemValue("rootMenuId", currentRootMenuId);
            var result = eval("(" + loadJson(MODEL_URL + "!validateVersions.json?rootMenuId=" + currentRootMenuId + "&version=" + version) + ")");
            if (result.exist) {
                if (type != result.type) {
                    if (type == "0") {
                        dhtmlx.message("该版本为更新包，不能用系统进行覆盖！");
                    } else if (type == "1") {
                        dhtmlx.message("该版本为系统，不能用更新包进行覆盖！");
                    }
                    return;
                }
                dhtmlx.confirm({
                    type : "confirm",
                    text : "该版本已经存在，是否覆盖？",
                    ok : "是",
                    cancel : "否",
                    callback : function(flag) {
                        if (flag) {
                            releaseProject(version, remark, systemVersionId);
                        }
                    }
                });
            } else {
                releaseProject(version, remark, systemVersionId);
            }
        }
    });
}
/**
 * 初始化发布更新包界面
 */
function initReleaseUpdatePackageLayout(win) {
	releaseTabbar = win.attachTabbar();
	releaseTabbar.setImagePath(IMAGE_PATH);
	releaseTabbar.enableAutoReSize();
	releaseTabbar.addTab("releaseUpdatePackageForm", "第一步：基本信息", "150px");
	releaseTabbar.addTab("releaseUpdatePackageTree", "第二步：选择菜单和构件", "150px");
    var statusBar = win.attachStatusBar();
    var releaseToolbar = new dhtmlXToolbarObject(statusBar);
    initReleaseUPFormToolbar(releaseToolbar);
    releaseUpdatePackageForm = releaseTabbar.cells("releaseUpdatePackageForm").attachForm(initDetailFormFormat(releaseUpdatePackageFormData));
    releaseUpdatePackageForm.setItemValue("rootMenuText", currentRootMenuText);
    var releaseSystemVersionCombo = releaseUpdatePackageForm.getCombo("releaseSystemVersion");
    if (releaseSystemVersionCombo) {
        initReleaseSystemVersionCombo(releaseSystemVersionCombo);
        releaseSystemVersionCombo.attachEvent("onChange", function() {
            var result = eval("(" + loadJson(MODEL_URL + "!getNewUpdatePackageVersion.json?rootMenuId=" + currentRootMenuId + "&releaseSystemVersion=" + releaseSystemVersionCombo.getActualValue()) + ")");
            if (result && result.newUpdatePackageVersion) {
                releaseUpdatePackageForm.setItemValue("version", result.newUpdatePackageVersion);
            }
        });
    }
    initReleaseTree(releaseTabbar.cells("releaseUpdatePackageTree"));
    releaseTabbar.setTabActive("releaseUpdatePackageForm");
}
/**
 * 初始化菜单树
 */
function initReleaseTree(releaseUpdatePackageTreeCell) {
    releaseUpdatePackageTree = releaseUpdatePackageTreeCell.attachTree();
    releaseUpdatePackageTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    releaseUpdatePackageTree.attachEvent("onMouseIn", function(id) {
        releaseUpdatePackageTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    releaseUpdatePackageTree.attachEvent("onMouseOut", function(id) {
        releaseUpdatePackageTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    var treeJson = {
        id : 0,
        item : [{
            id : "M_" + currentRootMenuId,
            text : currentRootMenuText,
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            child : 1
        }]
    };
    releaseUpdatePackageTree.setDataMode("json");
    releaseUpdatePackageTree.enableSmartXMLParsing(true);
    releaseUpdatePackageTree.enableCheckBoxes(true, true);
    releaseUpdatePackageTree.enableThreeStateCheckboxes(true);
    releaseUpdatePackageTree.setXMLAutoLoading(MODEL_URL + "!getReleaseTree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentId&P_OPEN=false");
    releaseUpdatePackageTree.loadJSONObject(treeJson);
    releaseUpdatePackageTree.refreshItem("M_" + currentRootMenuId);
    releaseUpdatePackageTree.attachEvent("onBeforeCheck", function(id, state) {
        var type = releaseUpdatePackageForm.getItemValue("type");
        // 发布系统时只可以选择菜单，不可以选择构件
        if (type == "0" && id.indexOf("M_") != 0) {
        	dhtmlx.message("发布系统时只可以选择菜单！");
            return false;
        }
        return true;
    });
}
/**
 * 初始化系统版本下拉框
 */
function initSystemVersionIdCombo(systemVersionIdCombo) {
    systemVersionIdCombo.clearAll(true);
    var systemVersionIdComboUrl = SYSTEM_VERSION_MODEL_URL + "!search.json?E_model_name=combo&F_in=id,name&Q_EQ_systemId=" + currentRootMenuId + "&P_orders=isDefault,desc,name";
    var jsonObj = loadJson(systemVersionIdComboUrl);
    if (jsonObj && jsonObj.data && jsonObj.data.length) {
        var opt_data = [];
        for (var m = 0; m < jsonObj.data.length; m++) {
            opt_data[opt_data.length] = {
                text : jsonObj.data[m].name,
                value : jsonObj.data[m].id
            };
        }
        systemVersionIdCombo.addOption(opt_data);
    }
}
function initSystemVersionIdGridCombo(systemVersionIdCombo) {
    var systemVersionIdComboUrl = SYSTEM_VERSION_MODEL_URL + "!search.json?E_model_name=combo&F_in=id,name&Q_EQ_systemId=" + currentRootMenuId + "&P_orders=isDefault,desc,name";
    var jsonObj = loadJson(systemVersionIdComboUrl);
    if (jsonObj && jsonObj.data && jsonObj.data.length) {
        var opt_data = [];
        for (var m = 0; m < jsonObj.data.length; m++) {
            systemVersionIdCombo.put(jsonObj.data[m].id, jsonObj.data[m].name);
        }
    }
}
/**
 * 初始化系统版本号下拉框
 */
function initReleaseSystemVersionCombo(releaseSystemVersionCombo) {
    releaseSystemVersionCombo.clearAll(true);
    var rootMenuIdComboUrl = MODEL_URL + "!search.json?E_model_name=combo&F_in=id,version&Q_EQ_rootMenuId=" + currentRootMenuId + "&Q_EQ_type=0&P_orders=version,desc";
    var jsonObj = loadJson(rootMenuIdComboUrl);
    if (jsonObj && jsonObj.data && jsonObj.data.length) {
        var opt_data = [];
        for (var m = 0; m < jsonObj.data.length; m++) {
            if (jsonObj.data[m].id != 'sys_0') {
                opt_data[opt_data.length] = {
                    text : jsonObj.data[m].version,
                    value : jsonObj.data[m].version
                };
            }
        }
        releaseSystemVersionCombo.addOption(opt_data);
    }
}
/**
 * 初始化发布表单工具条
 * @param {dhtmlxToolbar} toolBar
 */
function initReleaseUPFormToolbar(toolBar) {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("submit", 1, "&nbsp;&nbsp;确定&nbsp;&nbsp;");
    toolBar.addButton("cancel", 2, "&nbsp;&nbsp;取消&nbsp;&nbsp;");
    toolBar.setAlign("right");
    toolBar.attachEvent("onClick", function(buttonName) {
        if (buttonName == "cancel") {
            dhxWins.window(WIN_ID).close();
        } else if (buttonName == "submit") {
            var version = releaseUpdatePackageForm.getItemValue("version");
            var remark = releaseUpdatePackageForm.getItemValue("remark");
            var type = releaseUpdatePackageForm.getItemValue("type");
            if (!releaseUpdatePackageForm.validate()) {
                return;
            }
            var checkedIds = releaseUpdatePackageTree.getAllCheckedBranches();
            if (!checkedIds) {
		        dhtmlx.message("请选择菜单！");
		        return;
		    }
		    releaseUpdatePackageForm.setItemValue("rootMenuId", currentRootMenuId);
            var result = eval("(" + loadJson(MODEL_URL + "!validateVersions.json?rootMenuId=" + currentRootMenuId + "&version=" + version) + ")");
            if (result.exist) {
            	if (type != result.type) {
            	    if (type == "0") {
            	        dhtmlx.message("该版本为更新包，不能用系统进行覆盖！");
            	    } else if (type == "1") {
            	        dhtmlx.message("该版本为系统，不能用更新包进行覆盖！");
            	    }
            	    return;
            	}
                dhtmlx.confirm({
                    type : "confirm",
                    text : "该版本已经存在，是否覆盖？",
                    ok : "是",
                    cancel : "否",
                    callback : function(flag) {
                        if (flag) {
                    	    releaseUpdatePackage(version, remark);
                        }
                    }
                });
            } else {
        	    releaseUpdatePackage(version, remark);
            }
        }
    });
}
/**
 * 初始化查看界面
 */
function initViewLayout(win, systemReleaseId) {
	var viewLayout = win.attachLayout("1C");
    var statusBar = win.attachStatusBar();
    viewLayout.cells("a").hideHeader();
    var viewToolbar = new dhtmlXToolbarObject(statusBar);
    viewToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    viewToolbar.addButton("close", 1, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    viewToolbar.setAlign("right");
    viewToolbar.attachEvent("onClick", function(buttonName) {
        if (buttonName == "close") {
            dhxWins.window(WIN_ID).close();
        }
    });
    initViewTree(viewLayout.cells("a"), systemReleaseId);
}
/**
 * 初始化查看树
 */
function initViewTree(viewTreeCell, systemReleaseId) {
    var viewTree = viewTreeCell.attachTree();
    viewTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    viewTree.attachEvent("onMouseIn", function(id) {
        viewTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    viewTree.attachEvent("onMouseOut", function(id) {
        viewTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    var releaseDetail = loadJson(DETAIL_MODEL_URL + "!getReleaseDetailRootNode.json?systemReleaseId=" + systemReleaseId + "&dataId=M_" + currentRootMenuId);
    var treeDetailJson = {
        id : 0,
        item : [{
            id : releaseDetail.nodeId,
            text : releaseDetail.name,
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            child : 1
        }]
    };
    viewTree.setDataMode("json");
    viewTree.enableSmartXMLParsing(true);
    viewTree.setXMLAutoLoading(DETAIL_MODEL_URL + "!tree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentNodeId");
    viewTree.loadJSONObject(treeDetailJson);
    viewTree.refreshItem(releaseDetail.nodeId);
}
/**
 * 初始化发布列表工具条
 */
function initReleaseToolBar() {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    var index = 0;
    toolBar.addButton("releaseSystem", index++, "发布", "release.gif");
    //toolBar.addSeparator("septr$01", index++);
    //toolBar.addButton("releaseUpdatePackage", index++, "发布更新包", "release.gif");
    toolBar.addSeparator("septr$01", index++);
    toolBar.addButton("download", index++, "下载", "download.gif");
    toolBar.addSeparator("septr$01", index++);
    toolBar.addButton("view", index++, "查看", "view.gif");
    
	toolBar.addButton("reset", index++, "", "reset.gif", "reset.gif", "right");
	toolBar.addSeparator("septr$01", index++, "right");
	toolBar.addButton("sous", index++, "", "search.gif", "search.gif", "right");
    toolBar.addDiv("top$searchTextDiv", index++, "right");
	ST_form = initSearchColumn();
    toolBar.attachEvent('onClick', function(buttonName) {
        if (buttonName == "releaseSystem") {
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var winWidth = 750;
            var winHeight = 380;
            if (winHeight > document.body.clientHeight) {
                winHeight = document.body.clientHeight;
            }
            var win = dhxWins.createWindow(WIN_ID, 0, 0, winWidth, winHeight);
            win.setModal(true);
            win.setText("发布系统");
            win.center();
            initReleaseSystemLayout(win);
        } else if (buttonName == "releaseUpdatePackage") {
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var winWidth = 750;
            var winHeight = 500;
            if (winHeight > document.body.clientHeight) {
                winHeight = document.body.clientHeight;
            }
            var win = dhxWins.createWindow(WIN_ID, 0, 0, winWidth, winHeight);
            win.setModal(true);
            win.setText("发布更新包");
            win.center();
            initReleaseUpdatePackageLayout(win);
        } else if (buttonName == "download") {
            var selectId = releaseGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId.indexOf(',') != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            download(MODEL_URL + "!downloadProject?systemReleaseId=" + selectId);
        } else if (buttonName == "view") {
            var selectId = releaseGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId.indexOf(',') != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var winWidth = 750;
            var winHeight = 500;
            if (winHeight > document.body.clientHeight) {
                winHeight = document.body.clientHeight;
            }
            var win = dhxWins.createWindow(WIN_ID, 0, 0, winWidth, winHeight);
            win.setModal(true);
            win.setText("查看");
            win.center();
            initViewLayout(win, selectId)
        } else if (buttonName == "sous") {
        	var type  = ST_form.getItemValue("type"),
			    param = "";
			if (type) {
			    if (param != "") {
			        param += "&Q_EQ_type=" + type;
			    } else {
			        param += "Q_EQ_type=" + type;
			    }
			}
			refreshReleaseGrid(param);
        } else if (buttonName == "reset") {
			refreshReleaseGrid();
        }
    });
}
/**
 * 刷新系统发布列表
 */
function refreshReleaseGrid(param) {
	var url = MODEL_URL + "!search.json?P_orders=releaseDate,desc&Q_EQ_rootMenuId=" + currentRootMenuId;
	if (param) {
		url += "&" + param;
	} else {
		ST_form.setItemValue("type", "");
	}
    search(releaseGrid, releaseGridData, url);
}
/**
 * 字段检索
 * @returns {dhtmlXForm}
 */
function initSearchColumn() {
	var sformJson = [
		{type: "combo", name: "type", className: "dhx_toolbar_form", label: "类型：", labelWidth: 40, labelAlign:"right", style:"font-size:11px;", width: 120, readonly:"true",
        	options:[{value:'',text:'请选择'},{value:'0',text:'系统'},{value:'1',text:'更新包'}]}
	];
    return new dhtmlXForm("top$searchTextDiv", sformJson);
}
/**
 * 发布系统
 * @param {string} version 版本号
 * @param {string} remark 发布说明
 */
function releaseProject(version, remark, systemVersionId) {
    params = "rootMenuId=" + currentRootMenuId + "&version=" + encodeURIComponent(version) + "&remark="
            + encodeURIComponent(remark) + "&systemVersionId=" + systemVersionId;
    dhxWins.window(WIN_ID).close();
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
        obj = document.createElement("SPAN");
        obj.setAttribute("id", "DIV-msg");
        obj.innerHTML = "<img src='"+DHX_RES_PATH+"/common/images/loading.gif'/ style='vertical-align:middle'> 正在发布，请稍后...";
        obj.setAttribute("style", "position:relative;top:25%;left:15%;font-size:11px;height:16px;");
        document.body.appendChild(obj);
    }
    msgWin.attachObject(obj);
    dhtmlxAjax.post(MODEL_URL + "!releaseProject.json", params, function(loader) {
        msgWin.close();
        var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
        if (typeof jsonObj == 'string') {
            jsonObj = eval("(" + jsonObj + ")");
        }
        if (jsonObj.success) {
            dhtmlx.message("发布成功！");
        } else {
            dhtmlx.message("发布失败！");
        }
        refreshReleaseGrid();
    });
}
/**
 * 发布更新包
 * @param {string} version 版本号
 * @param {string} remark 发布说明
 */
function releaseUpdatePackage(version, remark) {
    var checkedIds = releaseUpdatePackageTree.getAllCheckedBranches();
    var params = "checkedIds=" + checkedIds;
    if (params.indexOf("CD_") == -1 && params.indexOf("RZ_") == -1) {
        var loader = dhtmlxAjax.postSync(MODEL_URL + "!validateComponents.json", params);
        var rst = eval("(" + loader.xmlDoc.responseText + ")");
        if (!rst.hasComponent) {
            dhtmlx.message("选择的菜单中没有包含任何构件！");
            return;
        }
    }
    dhtmlx.message("正在发布，请稍后！");
    var releaseSystemVersion = releaseUpdatePackageForm.getItemValue("releaseSystemVersion");
    params = "rootMenuId=" + currentRootMenuId + "&version=" + encodeURIComponent(version) + "&remark="
            + encodeURIComponent(remark) + "&checkedIds=" + checkedIds + "&releaseSystemVersion=" + releaseSystemVersion;
    dhtmlxAjax.post(MODEL_URL + "!releaseUpdatePackage.json", params, function(loader) {
        var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
        if (typeof jsonObj == 'string') {
            jsonObj = eval("(" + jsonObj + ")");
        }
        dhxWins.window(WIN_ID).close();
        if (jsonObj.success) {
            dhtmlx.message("发布成功！");
        } else {
            dhtmlx.message("发布失败！");
        }
        refreshReleaseGrid();
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
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	tree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
	var treeJson = {id:0, item:[{id:-1,text:"系统", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif",item:[]}]};
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	tree.setXMLAutoLoading(contextPath + "/menu/menu!getRootMenuTree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentId&P_orders=showOrder");
	tree.loadJSONObject(treeJson);
	tree.refreshItem("-1");
	tree.attachEvent("onClick", function(id) {
	    if (id == "-1") {
	    	currentRootMenuId = null;
	    	currentRootMenuText = null;
			if (toolBar) {
				dhxLayout.cells("b").detachToolbar();
				toolBar = null;
				releaseStatusBar = null;
				releaseGrid = null;
			}
			dhxLayout.cells("b").showHeader();
			dhxLayout.cells("b").setText("操作说明");
			dhxLayout.cells("b").attachObject(createHelpDiv());
		} else {
			currentRootMenuId = id;
			currentRootMenuText = tree.getItemText(id);
			dhxLayout.cells("b").hideHeader();
			if (!toolBar) {
				toolBar = dhxLayout.cells("b").attachToolbar();
			    initReleaseToolBar();
		    }
		    if (!releaseGrid) {
			    releaseGrid = dhxLayout.cells("b").attachGrid();
			    releaseStatusBar = dhxLayout.cells("b").attachStatusBar();
                var typeCombo = releaseGrid.getCombo("0");
                typeCombo.put("0", "系统");
                typeCombo.put("1", "更新包");
                initGrid(releaseGrid, releaseGridData, releaseStatusBar);
		    }
		    var systemVersionIdCombo = releaseGrid.getCombo("2");
		    initSystemVersionIdGridCombo(systemVersionIdCombo);
		    refreshReleaseGrid();
    	}
	});
	tree.selectItem("-1", true);
}
/**
 * 创建系统发布模块说明
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
			+ "<p><b>系统发布模块说明：</b><br></p> \n"
			+ "<p>1. 系统节点对应的列表为该系统的发布的版本和更新包<br></p> \n"
			+ "<p>2. 更新包是针对具体的系统版本打包的<br></p> \n"
			+ "</li> \n"
			+ "</ul> \n";
	}
	return obj;
}