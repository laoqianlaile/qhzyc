var appUrl, appGrid, appCfg;
function loadAppDefine(layout) {
	layout.hideHeader();
    layout.detachToolbar();
    var tbar = layout.attachToolbar();
    tbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    //tbar.addButton("preview", 1, "预览", "preview.gif");
    //tbar.addSeparator("septr$01", 2);
    tbar.addButton("applyTo", 3, "应用到", "reassign.gif");
    tbar.addSeparator("septr$02", 4);
    tbar.attachEvent('onClick', function(id) {
        if ("preview" == id) {
            var rowId = appGrid.getSelectedRowId();
            if (null == rowId || "" == rowId) {
                dhtmlx.message("请选择一个构件！");
                return;
            }
            if (rowId.indexOf(",") > 0) {
                dhtmlx.message("一次只能预览一个构件！");
                return;
            }
            var componentVersionId = appGrid.getUserData(rowId, "componentVersionId");
            window.open(DHX_RES_PATH + "/views/config/appmanage/preview/AppPreview.jsp?P_tableId="
                    + app.tableId + "&P_componentVersionId=" + componentVersionId, "应用自定义预览", "left=0,top=0,width="
                    + (screen.availWidth - 10) + ",height=" + (screen.availHeight - 50)
                    + ",toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes");
        } else if ("applyTo" == id) {
            var rowId = appGrid.getSelectedRowId();
            var componentVersionId = appGrid.getUserData(rowId, "componentVersionId");
            if (isEmpty(componentVersionId)) {
                dhtmlx.message("请选择一个构件！");
                return;
            }
            if (componentVersionId.indexOf(",") > 0) {
                dhtmlx.message("一次只能选择一个构件！");
                return;
            }
            var winto = createDhxWindow({
                id : "win$applyto",
                title : "设置应用到",
                width : 300,
                height : 400
            });
            winto.button("close").attachEvent("onClick", function() {
                reloadAppGrid();
                winto.close();
            });
            initApplyToWin(winto, componentVersionId);
        }
    });
    appUrl = AppActionURI.appDefine + "!query.json?P_tableId=" + app.tableId + "&P_menuId=" + app.menuId;
    appGrid = layout.attachGrid();
	
	/*appCfg = {
			format: {
				headers: ["<center>构件名称</center>","<center>检索定义</center>","<center>列表字段定义</center>","<center>列表过滤条件</center>","<center>列表排序定义</center>","<center>界面定义</center>","<center>报表定义</center>",""],
				cols   : ["name","searchedAlias","columnedAlias","filteredAlias","sortedAlias","formedAlias","reportedAlias"],
				userdata:["componentVersionId"],
				colWidths: ["200","80","80","80","80","80","80","*"],
				colTypes: ["ro","img","img","img","img","img","img","ro"],
				colAligns: ["left","center","center","center","center","center","center","center"]
			}
		};*/
    appCfg = {
            format: {
                headers: ["<center>构件名称</center>","<center>检索定义</center>","<center>列表字段定义</center>","<center>列表排序定义</center>","<center>界面定义</center>","<center>报表定义</center>",""],
                cols   : ["name","searchedAlias","columnedAlias","sortedAlias","formedAlias","reportedAlias"],
                userdata:["componentVersionId"],
                colWidths: ["200","80","80","80","80","80","*"],
                colTypes: ["ro","img","img","img","img","img","ro"],
                colAligns: ["left","center","center","center","center","center","center"]
            }
        };
	appGrid.enableTooltips("true,false,false,false,false,false");
	initGridWithoutPageable(appGrid, appCfg, appUrl);
}

/**
 * refresh app define grid
 */
function reloadAppGrid() {
    if (null == appUrl)
        return;
    appGrid.clearAll();
    var datas = loadJson(appUrl);
    var cfg = {
        "rows" : datas,
        "columns" : appCfg.format.cols,
        "userdata" : appCfg.format.userdata
    };
    appGrid.parse(jsonArray2DhtmlxJsonGridData(cfg), "json");
}
function opencfg(type, componentVersionId) {
    var win, w, h, title;
    if (0 == type) {
        w = APP_wincfg.search.width;
        h = APP_wincfg.search.height;
        title = APP_wincfg.search.title;
    } else if (1 == type) {
        // w = 710; h = 600;
        w = APP_wincfg.column.width;
        h = APP_wincfg.column.height;
        title = APP_wincfg.column.title;
    } else if (2 == type) {
        w = APP_wincfg.sort.width;
        h = APP_wincfg.sort.height;
        title = APP_wincfg.sort.title;
    } else if (6 == type) {
        // w = 710; h = 560;
        w = 850;
        h = 600;
        title = "列表过滤条件配置";
    } else if (3 == type) {
        w = 800;
        h = 400;
        title = "列表按钮配置";
    } else if (4 == type) {
        w = 1000;
        h = 600;
        title = "界面配置";
    } else if (5 == type) {
        w = 800;
        h = 400;
        title = "界面按钮配置";
    } else if (7 == type) {
        w = 620;
        h = 600;
        title = "报表配置";
    } else {
        return;
    }
    if (h > document.body.clientHeight) {
        h = document.body.clientHeight;
    }
    win = createDhxWindow({
        id : "win$appdefine",
        title : title,
        width : w,
        height : h
    });
    win.button("close").attachEvent("onClick", function() {
        closeDhtmlxWin();
    });
    if (0 == type) { // 列表检索定义
        initAppSearchWin(win, componentVersionId, closeDhtmlxWin);
    } else if (1 == type) { // 列表字段定义
        initAppColumnWin(win, componentVersionId, closeDhtmlxWin);
    } else if (2 == type) { // 列表排序定义
        initAppSortWin(win, componentVersionId, closeDhtmlxWin);
    } else if (3 == type) { // 列表按钮定义
        initAppButtonWin(win, componentVersionId, "1", closeDhtmlxWin);
    } else if (4 == type) { // 界面定义
        initAppFormWin(win, componentVersionId, closeDhtmlxWin);
    } else if (5 == type) { // 界面按钮定义
        initAppButtonWin(win, componentVersionId, "0", closeDhtmlxWin);
    } else if (6 == type) { // 列表过滤条件配置
        initAppFilterWin(win, componentVersionId, closeDhtmlxWin);
    } else if (7 == type) { // 报表配置
        initAppReportWin(win, componentVersionId, closeDhtmlxWin);
    } else {
        return;
    }

    function closeDhtmlxWin() {
        if (app.modified === true) {
            dhtmlx.confirm({
                type : "confirm",
                text : "配置信息未保存，确定要关闭吗？",
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag)
                        _close();
                }
            });
        } else {
            _close();
        }
    }

    function _close() {
        win.close();
        reloadAppGrid();
        app.modified = false;
    }
}

/**
 * 应用到
 */
function initApplyToWin(win, componentVersionId) {
    this.moduleNodeId = "-1";
    var moduleUrl = AppActionURI.appDefine;
    var curLayout = win.attachLayout("1C");
    var mbsbar = curLayout.attachStatusBar();
    var mbbtbar = new dhtmlXToolbarObject(mbsbar.id);
    mbbtbar.setIconPath(IMAGE_PATH);
    mbbtbar.addButton("bottom$save", 0, "保存", "save.gif");
    mbbtbar.addSeparator("bottom$septr$01", 1);
    mbbtbar.addButton("bottom$close", 4, "关闭", "default/close.png");
    mbbtbar.setAlign("right");
    mbbtbar.attachEvent("onClick", function(itemId) {
        if ("bottom$close" == itemId) {
            win.close();
        } else if ("bottom$save" == itemId) {
            var menuIds = tree.getAllChecked(); // 应用到的componentVersionIds
            if (isEmpty(menuIds)) {
                dhtmlx.message("请选择要应用到的菜单！");
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
            dhtmlx.confirm({
                type : "confirm",
                text : "确定要应用到所选菜单吗？",
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        var Apply_Url = moduleUrl + "!appApplyTo.json?P_tableId=" + app.tableId
                                + "&P_componentVersionId=" + componentVersionId+ "&P_menuId=" + app.menuId + "&P_appApplyToMenuIds=" + menuIds;
                        dhtmlxAjax.get(Apply_Url, function(loader) {
                        	var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                        	if (!jsonObj.success) {
                        		dhtmlx.message("操作失败！")
                        	} else {
                        		reloadAppGrid();
                        		win.close();
                        	}
                        	msgWin.close();
                        });
                    } else {
                    	msgWin.close();
                    }
                }
            });
        }
    });
    var alayout = curLayout.cells("a");
    alayout.setText("菜单树");
    var tree = alayout.attachTree();
    /*var blayout = curLayout.cells("b");
	blayout.hideHeader();
    var grid = blayout.attachGrid();
    
    var gurl  = AppActionURI.appDefine + "!query.json?P_tableId=" + app.tableId;
    var gcfg = {
            format: {
                headers: ["<center>构件名称</center>"],
                cols   : ["name"],
                userdata:["componentVersionId"],
                colWidths: ["*"],
                colTypes: ["ro"],
                colAligns: ["left"]
            }
        };
	initGridWithoutPageable(grid, gcfg);*/
	

    initCopyMenuTree(tree/*, grid, gcfg, gurl*/);
}

/**
 * @author qiucs
 * @date   2014-12-23 上午11:46:34
 * 工作流定义中的应用定义配置
 * @param layout
 */
function loadCoflowAppDefine(layout) {
    appUrl = AppActionURI.appDefine + "!coflowQuery.json?P_workflowVersionId=" + app.menuId;
    appGrid = layout.attachGrid();
	
	appCfg = {
			format: {
				headers: ["<center>工作箱名称</center>","<center>检索定义</center>","<center>列表字段定义</center>","<center>列表排序定义</center>","<center>列表工具条定义</center>","<center>界面工具条定义</center>",""],
				cols   : ["name","searchedAlias","columnedAlias","sortedAlias","gridButtonedAlias","formButtonedAlias"],
				userdata:["componentVersionId"],
				colWidths: ["200","80","100","100","120","120","*"],
				colTypes: ["ro","img","img","img","img","img","ro"],
				colAligns: ["left","center","center","center","center","center","center"]
			}
		};
	appGrid.enableTooltips("true,false,false,false,false,false");
	initGridWithoutPageable(appGrid, appCfg, appUrl);
}

/**
 * @author qiucs
 * @date   2015-06-10 上午09:46:34
 * 应用到构件列表
 * @param layout
 */
function loadCopyComponents(grid) {
	//layout.hideHeader();
    
    var gurl  = AppActionURI.appDefine + "!query.json?P_tableId=" + app.tableId + "&P_menuId=" + app.menuId;
    var gcfg = {
            format: {
                headers: ["<center>构件名称</center>"],
                cols   : ["name"],
                userdata:["componentVersionId"],
                colWidths: ["*"],
                colTypes: ["ro"],
                colAligns: ["left"]
            }
        };
	initGridWithoutPageable(grid, gcfg, url);
}