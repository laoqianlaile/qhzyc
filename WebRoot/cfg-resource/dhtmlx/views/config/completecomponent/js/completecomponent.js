COMPLETE_COMPONENT_AREA_MODEL_URL = contextPath + "/completecomponent/complete-component-area";
COMPLETE_COMPONENT_MODEL_URL = contextPath + "/completecomponent/complete-component";
COMPLETE_COMPONENT_VERSION_MODEL_URL = contextPath + "/completecomponent/complete-component-version";
var componentLayout, componentRemarkDiv;
var completeComponentAreaForm;
var completeComponentAreaFormData = {
    format : [{
        type : "block",
        width : "350",
        list : [{
            type : "hidden",
            name : "_method"
        }, {
            type : "hidden",
            name : "id"
        }, {
            type : "hidden",
            name : "parentId"
        }, {
            type : "hidden",
            name : "showOrder"
        }, {
            type : "hidden",
            name : "hasChild"
        }, {
            type : "input",
            label : "构件分类名称：",
            labelWidth : "120",
            name : "name",
            maxLength : 100,
            required : true,
            width : 200
        }]
    }],
    settings : {
        labelWidth : 80,
        inputWidth : 160
    }
};
var completeComponentGridData = {
    format : {
        headers : ["<center>构件类名</center>", "<center>构件名称</center>", "<center>版本号</center>", "<center>导入日期</center>",
                "<center>类型</center>", "<center>前台</center>", ""],
        cols : ["component.name", "component.alias", "version", "importDate", "component.type", "views"],
        userDatas : ["remark"],
        colWidths : ["120", "120", "120", "120", "150", "120", "*"],
        colTypes : ["ro", "ro", "ro", "ro", "co", "ro", "ro"],
        colAligns : ["left", "left", "left", "left", "left", "left"],
        colTooltips : ["true", "true", "true", "true", "true", "true", "false"]
    }
};
var completeComponentConfigKey;
/**
 * 保存构件
 */
function saveComp() {
    var importUrl = COMPLETE_COMPONENT_VERSION_MODEL_URL
            + "!saveCompleteComponentVersion.json?completeComponentConfigKey=" + completeComponentConfigKey
            + "&areaId=" + currentTreeNodeId;
    dhtmlxAjax.get(importUrl, function(loader) {
        refreshCompleteComponentVersionGrid();
        var result = eval("(" + loader.xmlDoc.responseText + ")");
        if (typeof result == 'string') {
            result = eval("(" + result + ")");
        }
        dhtmlx.message(result.message);
    });
}
/**
 * 导入构件
 * @param {object} result
 */
function importComp(result) {
    if (result.existOldCompleteComponentVersion) {
        dhtmlx.confirm({
            type : "confirm",
            text : "该构件已经存在，是否覆盖！",
            ok : "是",
            cancel : "否",
            callback : function(flag) {
                if (flag) {
                    saveComp();
                }
            }
        });
    } else {
        saveComp();
    }
}
/**
 * 初始化构件列表工具条
 */
function initSelfToolBar() {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("import", 0, "构件导入", "upload.gif");
    toolBar.addSeparator("septr$01", 1);
    toolBar.addButton("delete", 2, "删除", "delete.gif");
    toolBar.addSeparator("septr$03", 3);
    toolBar.addButton("changeArea", 4, "更改分类", "update.gif");
    toolBar.addSeparator("septr$05", 5);
    toolBar.addButton("download", 6, "下载", "download.gif");
    toolBar.addSeparator("septr$07", 7);
    toolBar.addButton("refresh", 8, "刷新", "refresh.gif");
    toolBar.attachEvent('onClick', function(id) {
        if (id == "import") {
            var current = new Date();
            completeComponentConfigKey = current.getTime() + "_" + Math.floor(Math.random() * 100);
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
            var UPLOAD_URL = COMPLETE_COMPONENT_VERSION_MODEL_URL + "!uploadHandler";
            var GET_INFO_URL = COMPLETE_COMPONENT_VERSION_MODEL_URL + "!getInfoHandler";
            var GET_ID_URL = COMPLETE_COMPONENT_VERSION_MODEL_URL + "!getIdHandler";
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
                dhtmlxAjax.get(COMPLETE_COMPONENT_VERSION_MODEL_URL
                        + "!getUploadMessage.json?completeComponentConfigKey=" + completeComponentConfigKey, function(
                        loader) {
                    dataWin.close();
                    var result = eval("(" + eval(loader.xmlDoc.responseText) + ")");
                    if (!result.success) {
                        dhtmlx.message(result.message);
                    } else {
                        importComp(result);
                    }
                });
            };
            vault.create("vaultDiv");
            vault.setFormField("completeComponentConfigKey", completeComponentConfigKey);
            dataWin.attachObject(vaultDiv);
        } else if (id == "delete") {
            var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        dhtmlxAjax.get(COMPLETE_COMPONENT_VERSION_MODEL_URL
                                + "!deleteCompleteComponentVersion.json?ids=" + selectIds, function(loader) {
                            var result = eval(loader.xmlDoc.responseText);
                            if (result != "") {
                                dhtmlx.message(result);
                            }
                            refreshCompleteComponentVersionGrid();
                        });
                    }
                }
            });
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
            areaTree.setDataMode("json");
            areaTree.enableSmartXMLParsing(true);
            areaTree.loadJSONObject(eval("("
                    + loadJson(COMPLETE_COMPONENT_AREA_MODEL_URL + "!getAreaTree.json?currentAreaId="
                            + currentTreeNodeId) + ")"));
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
                    dhtmlxAjax.get(COMPLETE_COMPONENT_VERSION_MODEL_URL
                            + "!changeArea.json?completeComponentVersionIds=" + selectIds + "&areaId=" + selectAreaId,
                            function(loader) {
                                var result = eval(loader.xmlDoc.responseText);
                                if (result != "") {
                                    dhtmlx.message(result);
                                }
                                areaWin.close();
                                refreshCompleteComponentVersionGrid();
                            }
                    );
                } else {
                    areaWin.close();
                }
            });
        } else if (id == "download") {
            var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            download(COMPLETE_COMPONENT_VERSION_MODEL_URL + "!downloadCompleteComponent.json?id="
                    + dataGrid.getSelectedRowId());
        } else if (id == "refresh") {
            refreshCompleteComponentVersionGrid();
        }
    });
}
/**
 * 初始化构件分类表单工具条
 * @param {dhtmlxToolbar} toolBar
 */
function initCompleteComponentAreaFormToolbar(toolBar) {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    toolBar.setAlign("right");
    toolBar.attachEvent("onClick", function(id) {
        if (id == "close") {
            dhxWins.window(WIN_ID).close();
        } else if (id == "save") {
            var id = completeComponentAreaForm.getItemValue("id");
            var name = completeComponentAreaForm.getItemValue("name");
            var result = eval("("
                    + loadJson(COMPLETE_COMPONENT_AREA_MODEL_URL + "!validateFields.json?id=" + id + "&name="
                            + encodeURIComponent(name)) + ")");
            if (result.nameExist) {
                dhtmlx.message("构件分类名称已经存在，请修改！");
                return;
            }
            if (id == "") {
                SAVE_URL = COMPLETE_COMPONENT_AREA_MODEL_URL;
                completeComponentAreaForm.setItemValue("_method", "post");
            } else {
                SAVE_URL = COMPLETE_COMPONENT_AREA_MODEL_URL + "/" + id;
                completeComponentAreaForm.setItemValue("_method", "put");
            }
            completeComponentAreaForm.send(SAVE_URL, "post", function(loader, response) {
                dhxWins.window(WIN_ID).close();
                dhtmlx.message(getMessage("save_success"));
                if (id != "") {
                    var parentId = tree.getParentId(contextMenuNodeId);
                    tree.refreshItem(parentId);
                } else {
                    tree.refreshItem(contextMenuNodeId);
                }
            });
        }
    });
}
/**
 * 创建构件库模块说明
 * @return {div}
 */
function createHelpDiv() {
    var obj = document.getElementById("DIV-help");
    if (null == obj) {
        obj = document.createElement("DIV");
        obj.setAttribute("id", "DIV-help");
        obj.setAttribute("style", "font-family: Tahoma; font-size: 11px;display: none;");
        obj.innerHTML = "<ul> \n" + "<li type=\"square\">" + "<p><b>构件树操作说明：</b><br></p> \n" + "<p>1. 根节点下<br></p> \n"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;1.1 可以新增构件分类节点<br></p> \n" + "<p>2. 在构件分类节点下<br></p> \n"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.1 可以新增构件分类节点<br></p> \n"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.1 可以修改构件分类节点<br></p> \n"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.1 可以删除构件分类节点<br></p> \n" + "</li> \n" + "</ul> \n" + "<ul> \n"
                + "<li type=\"square\">" + "<p><b>构件树操作步骤：</b><br></p> \n"
                + "<p>1. 【新增】选择一个节点，右键->弹出右键菜单->选择“新增构件分类”，则在该节点下新增一个构件分类<br></p> \n"
                + "<p>2. 【修改】选择一个节点，右键->弹出右键菜单->选择“修改构件分类”<br></p> \n"
                + "<p>3. 【删除】选择一个节点，右键->弹出右键菜单->选择“删除构件分类”，若该构件分类下有子分类或有构件，则不能删除<br></p> \n"
                + "<p>4. 点击根节点时，右侧页面是操作说明<br></p> \n" + "</li> \n" + "</ul> \n";
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
function refreshCompleteComponentVersionGrid() {
    QUERY_URL = COMPLETE_COMPONENT_VERSION_MODEL_URL + "!search.json?Q_EQ_areaId=" + tree.getSelectedItemId()
            + "&P_orders=componentId,version";
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
    treeMenu
            .loadXMLString("<menu><item id='1' text='新增构件分类'/><item id='2' text='修改构件分类'/><item id='3' text='删除构件分类'/></menu>");
    treeMenu.attachEvent("onClick", function(id) {
        MODEL_URL = COMPLETE_COMPONENT_AREA_MODEL_URL;
        GWIN_WIDTH = 400;
        GWIN_HEIGHT = 240;
        if (id == "1") {
            completeComponentAreaForm = openNewWindow(completeComponentAreaFormData,
                    initCompleteComponentAreaFormToolbar, 400, 200
            );
            completeComponentAreaForm.setItemValue("parentId", contextMenuNodeId);
        } else if (id == "2") {
            completeComponentAreaForm = openEditWindow(completeComponentAreaFormData, contextMenuNodeId,
                    initCompleteComponentAreaFormToolbar, 400, 200
            );
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
                    dhtmlx.message(isNotEmpty(jsonObj.message) ? jsonObj.message : getMessage("delete_failure"));
                }
            });
        }
    });
    tree.enableContextMenu(treeMenu);
    tree.setDataMode("json");
    tree.enableSmartXMLParsing(true);
    tree.setXMLAutoLoading(COMPLETE_COMPONENT_AREA_MODEL_URL
            + "!tree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentId&P_orders=showOrder");
    var treeJson = {
        id : 0,
        item : [{
            id : -1,
            text : "构件库",
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            open : true,
            item : []
        }]
    };
    tree.loadJSONObject(treeJson);
    tree.refreshItem("-1");
    tree.enableDragAndDrop(true, true);
    tree.setDragBehavior("complex", true);
    tree.attachEvent("onDrag", function(sId, tId, id) {
        if (tId == "0" || tId == "1" || tId == "2") {
            return false;
        }
        return true;
    });
    tree.attachEvent("onDrop", function(sId, tId, id) {
        var url = COMPLETE_COMPONENT_AREA_MODEL_URL + "!sort.json?start=" + sId + "&targetId=" + tId;
        if (id) {
            url += "&end=" + id;
        }
        var result = loadJson(url);
        dhtmlx.message(result);
    });
    tree.attachEvent("onClick", function(id) {
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
                componentLayout = dhxLayout.cells("b").attachLayout("2U");
                componentLayout.cells("a").hideHeader();
                componentLayout.cells("b").setWidth(200);
                componentLayout.cells("b").setText("构件说明");
                componentRemarkDiv = createComponentRemarkDiv();
                componentLayout.cells("b").attachObject(componentRemarkDiv);
                toolBar = dhxLayout.cells("b").attachToolbar();
                statusBar = dhxLayout.cells("b").attachStatusBar();
                initSelfToolBar();
            }
            if (!dataGrid) {
                gridData = completeComponentGridData;
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
                initGrid();
                dataGrid.attachEvent("onSelectStateChanged", function(ids) {
                    if (componentRemarkDiv) {
                        if (!ids || ids.indexOf(",") != -1) {
                            componentRemarkDiv.innerHTML = "";
                        } else {
                            componentRemarkDiv.innerHTML = this.getUserData(ids, "remark");
                        }
                    }
                });
            }
            refreshCompleteComponentVersionGrid();
        }
    });
    tree.attachEvent("onBeforeContextMenu", function(nId) {
        contextMenuNodeId = nId;
        if (nId == "-1") {
            treeMenu.showItem("1");
            treeMenu.hideItem("2");
            treeMenu.hideItem("3");
        } else if (nId == "1" || nId == "2") {
            return false;
        } else {
            treeMenu.showItem("1");
            treeMenu.showItem("2");
            treeMenu.showItem("3");
        }
        return true;
    });
    tree.selectItem("-1", true);
}