var copyReserveZoneTree, copyReserveZoneTreeJson, copyWinLayout, copygrid;
/**
 * 初始化复制窗口
 */
function initCopyWin(copyWin, constructDetailIds) {
    copyWinLayout = copyWin.attachLayout("2U");
    copyWinLayout.cells("a").hideHeader();
    copyWinLayout.cells("b").hideHeader();
    copyWinLayout.cells("b").setWidth(300);
    copyWin.setModal(true);
    copyWin.setText("复制到其他预留区");
    copyWin.center();    
    var copyStatusBar = copyWin.attachStatusBar();
    var copyToolBar = new dhtmlXToolbarObject(copyStatusBar);
    copyToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    // copyToolBar.setAlign("right");
    var controlForm;
    if (currentTreeNodeId == "CommonBinding") {
        copyToolBar.addDiv("CopyDiv", 0, "left");
        controlForm = initCopyToolbarForm();
    }
    copyToolBar.addButton("cancel", 1, "&nbsp;&nbsp;取消&nbsp;&nbsp;", null, null, "right");
    copyToolBar.addButton("confirm", 2, "&nbsp;&nbsp;确定&nbsp;&nbsp;", null, null, "right");
    copyToolBar.attachEvent("onClick", function(id) {
        if (id == "confirm") {
            var reserveZoneIds = copyReserveZoneTree.getAllCheckedBranches();
            if (reserveZoneIds == undefined) {
                dhtmlx.message("请选择预留区！");
                return;
            }
            var syncToAllComponent;
            if (controlForm) {
                syncToAllComponent = controlForm.getItemValue("syncToAllComponent");
            }
            var itemChecks="";
            copygrid.forEachRow(function(id){
                var isCheck = copygrid.cells(id, 1).getValue();
                itemChecks +="," + (isCheck=="1" ? true : false);
            });
            itemChecks = itemChecks.substring(1);
            var res = eval("("
                    + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!copyConstructDetail.json?constructDetailIds="
                            + constructDetailIds + "&reserveZoneIds=" + reserveZoneIds + "&itemChecks=" +itemChecks + "&syncToAllComponent=" + syncToAllComponent) + ")");
            if (res.success) {
                dhtmlx.message("复制成功！");
            } else {
                dhtmlx.message("复制失败！");
            }
            copyWin.close();
        } else if (id == "cancel") {
            copyWin.close();
        }
    });
    copyReserveZoneTree = copyWinLayout.cells("a").attachTree();
    initCopyReserveZoneTree(copyReserveZoneTree);
    
    copygrid = copyWinLayout.cells("b").attachGrid();
    var gcfg = {
            format: {
                headers: ["<center>选项名</center>","<center>选项</center>"],
                colWidths: ["140", "*"],
                colTypes: ["ro", "ch"],
                colAligns: ["left", "center"]
            },
            multselect: false
        };
    copygrid.enableDragAndDrop(false);
    var datas =[["1","按钮显示名称", "1"],
                ["2","页面组装类型", "1"],
                ["3","宽度", "1"],
                ["4","高度", "1"],
                ["5","构件自身参数", "1"],
                ["6","构件传参数", "1"],
                ["7","构件回调", "1"],
                ["8","前置事件", "1"]
                ];
    initGridWithoutPageable(copygrid, gcfg, datas);
}
/**
 * 工具条表单
 * @returns {dhtmlXForm}
 */
function initCopyToolbarForm() {
    var formJson = [
        {type: "checkbox", name: "syncToAllComponent", className: "dhx_toolbar_form", label: "同时复制到所有使用选中的预留区的构件", position: "label-right", labelAlign: "left", checked: false, labelWidth: 200}
    ];
    var form = new dhtmlXForm("CopyDiv", formJson);
    return form;
}
/**
 * 初始化复制树
 */
function initCopyReserveZoneTree(copyReserveZoneTree) {
    copyReserveZoneTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    copyReserveZoneTree.attachEvent("onMouseIn", function(id) {
        copyReserveZoneTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    copyReserveZoneTree.attachEvent("onMouseOut", function(id) {
        copyReserveZoneTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    copyReserveZoneTree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
    copyReserveZoneTree.setDataMode("json");
    copyReserveZoneTree.enableSmartXMLParsing(true);
    copyReserveZoneTree.enableCheckBoxes(true, true);
    copyReserveZoneTree.enableThreeStateCheckboxes(true);
    copyReserveZoneTree.setXMLAutoLoading(TREE_URL
            + "!getReserveZoneTree.json?E_model_name=tree&assembleComponentVersionId="
            + currentAssembleComponentVersionIdOfTree + "&sourceReserveZoneId=" + currentReserveZoneId);
    var copyReserveZoneTreeJson;
    if (currentTreeNodeId == "CommonBinding") {
        copyReserveZoneTreeJson = {
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
        copyReserveZoneTree.loadJSONObject(copyReserveZoneTreeJson);
        copyReserveZoneTree.refreshItem("Common");
    } else {
        if (baseComponentVersionType == "3") {
            // 基础构件是树构件
            copyReserveZoneTreeJson = {
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
            copyReserveZoneTree.loadJSONObject(copyReserveZoneTreeJson);
            copyReserveZoneTree.refreshItem("Component");
        } else if (baseComponentVersionType == "1" || baseComponentVersionType == "4") {
            // 基础构件是页面构件 或 物理表构件
            copyReserveZoneTreeJson = {
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
            copyReserveZoneTree.loadJSONObject(copyReserveZoneTreeJson);
            copyReserveZoneTree.refreshItem("Component");
        } else {
            // 基础构件是逻辑表组构件
            copyReserveZoneTreeJson = {
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
            copyReserveZoneTree.loadJSONObject(copyReserveZoneTreeJson);
            copyReserveZoneTree.refreshItem("Common");
        }
    }
}