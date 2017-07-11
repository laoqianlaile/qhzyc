var syncButtonToTree, syncWinLayout, syncgrid;
/**
 * 初始化应用到窗口
 */
function initSyncButtonToWin(syncButtonToWin, constructDetailIds) {
	syncWinLayout = syncButtonToWin.attachLayout("2U");
	syncWinLayout.cells("a").hideHeader();
    syncWinLayout.cells("b").hideHeader();
    syncWinLayout.cells("b").setWidth(300);
    syncButtonToWin.setModal(true);
    syncButtonToWin.setText("复制到构件");
    syncButtonToWin.center();
    var syncButtonToTopToolBar = syncButtonToWin.attachToolbar();
    syncButtonToTopToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    syncButtonToTopToolBar.addDiv("SyncToDiv", 0, "left");
    var controlForm = initSyncToToolbarForm();
    var syncButtonToStatusBar = syncButtonToWin.attachStatusBar();
    var syncButtonToToolBar = new dhtmlXToolbarObject(syncButtonToStatusBar);
    syncButtonToToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    syncButtonToToolBar.setAlign("right");
    syncButtonToToolBar.addButton("confirm", 1, "&nbsp;&nbsp;确定&nbsp;&nbsp;");
    syncButtonToToolBar.addButton("cancel", 2, "&nbsp;&nbsp;取消&nbsp;&nbsp;");
    syncButtonToToolBar.attachEvent("onClick", function(id) {
        if (id == "confirm") {
            var constructIds = syncButtonToTree.getAllCheckedBranches();
            if (constructIds == undefined || constructIds =="") {
                dhtmlx.message("请选择构件！");
                return;
            }
            var constructIdStr = "";
            var constructIdArray = constructIds.split(",");
            for (var i in constructIdArray) {
                if (syncButtonToTree.getAttribute(constructIdArray[i], "prop0") == "Component") {
                    constructIdStr += constructIdArray[i] + ",";
                }
            }
            var syncToAllComponent = controlForm.getItemValue("syncToAllComponent");
            if (syncToAllComponent == "0") {
                if (constructIdStr =="") {
                    dhtmlx.message("请选择构件！");
                    return;
                } else {
                    constructIdStr.substring(0, constructIdStr.length-1);
                }
            }
            var itemChecks="";
            syncgrid.forEachRow(function(id){
            	var isCheck = syncgrid.cells(id, 1).getValue();
            	itemChecks +="," + (isCheck=="1" ? true : false);
            });
            itemChecks = itemChecks.substring(1);
            var res = eval("("
                    + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!syncConstructDetailToConstruct.json?constructDetailIds="
                            + constructDetailIds + "&constructIds=" + constructIdStr + "&itemChecks=" +itemChecks + "&syncToAllComponent=" + syncToAllComponent) + ")");
            if (res.success) {
                dhtmlx.message("复制到构件成功！");
            } else {
                dhtmlx.message("复制到构件失败！");
            }
            syncButtonToWin.close();
        } else if (id == "cancel") {
            syncButtonToWin.close();
        }
    });
    
    syncButtonToTree = syncWinLayout.cells("a").attachTree();
    initSyncButtonToTree(syncButtonToTree);
    syncgrid = syncWinLayout.cells("b").attachGrid();
    var gcfg = {
    		format: {
    			headers: ["<center>选项名</center>","<center>选项</center>"],
    			colWidths: ["140", "*"],
    			colTypes: ["ro", "ch"],
    			colAligns: ["left", "center"]
    		},
    		multselect: false
    	};
    syncgrid.enableDragAndDrop(false);
    var datas =[["1","按钮显示名称", "1"],
                ["2","页面组装类型", "1"],
                ["3","宽度", "1"],
                ["4","高度", "1"],
                ["5","构件自身参数", "1"],
                ["6","构件传参数", "1"],
                ["7","构件回调", "1"],
                ["8","前置事件", "1"]
                ];
    initGridWithoutPageable(syncgrid, gcfg, datas);
}
/**
 * 工具条表单
 * @returns {dhtmlXForm}
 */
function initSyncToToolbarForm() {
    var formJson = [
        {type: "checkbox", name: "syncToAllComponent", className: "dhx_toolbar_form", label: "复制到所有使用该预留区的构件", position: "label-right", labelAlign: "left", checked: false, labelWidth: 200}
    ];
    var form = new dhtmlXForm("SyncToDiv", formJson);
    form.attachEvent("onChange", function(id, value, state) {
        if (id == "syncToAllComponent") {
            if (state) {
                syncButtonToTree.setCheck("-1", 1);
                syncButtonToTree.lockTree(true);
            } else {
                syncButtonToTree.lockTree(false);
            }
        }
    });
    return form;
}
/**
 * 初始化复制树
 */
function initSyncButtonToTree(syncButtonToTree) {
    syncButtonToTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    syncButtonToTree.attachEvent("onMouseIn", function(id) {
        syncButtonToTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    syncButtonToTree.attachEvent("onMouseOut", function(id) {
        syncButtonToTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    syncButtonToTree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
    syncButtonToTree.setDataMode("json");
    syncButtonToTree.enableSmartXMLParsing(true);
    syncButtonToTree.enableCheckBoxes(true, true);
    syncButtonToTree.enableThreeStateCheckboxes(true);
    var url = TREE_URL + "!getSyncButtonTree.json?E_model_name=tree&reserveZoneId=" + currentReserveZoneId;
    if (currentTreeNodeId != 'CommonBinding') {
        url += "&currentComponentVersionId=" + currentTreeNodeId;
    }
    syncButtonToTree.setXMLAutoLoading(url);
    var syncButtonToTreeJson = {
            id : 0,
            item : [{
                id : "-1",
                text : "组合构件",
                im0 : "safe_close.gif",
                im1 : "safe_open.gif",
                im2 : "safe_close.gif",
                open : true,
                item : []
            }]
        };
        syncButtonToTree.loadJSONObject(syncButtonToTreeJson);
        syncButtonToTree.refreshItem("-1");
        syncButtonToTree.setCheck("-1", 1);
}
