// 构件导入时对应后台session中组合构件信息的key
var assembleComponentConfigKey;
var currentComponentVersionAlias;
// 当构件已删除时加载构件下拉框显示名称
function loadComponentVersionComboText(id) {
    var url = COMPONENT_VERSION_MODEL_URL + "/" + id +".json?_method=get";
    var data = loadJson(url);
    var alias = obtainNonNullValue("component.alias", data);
    var version = obtainNonNullValue("version", data);
    var returnData = alias + "_" + version + "(已删除)";
    return returnData;
}
var componentGridData = {
    format: {
        headers: ["&nbsp;", "<center>构件类名</center>", "<center>构件名称</center>", "<center>版本号</center>", "<center>生成日期</center>", "<center>类型</center>", "<center>前台</center>", "<center>是否打包</center>", ""],
        cols: ["id", "component.name", "component.alias", "version", "importDate", "component.type", "views", "isPackage"],
        colWidths: ["30", "120", "120", "120", "120", "150", "120", "120", "*"],
        colTypes: ["sub_row_form", "ro", "ro", "ro", "ro", "co", "ro", "co", "ro"],
        colAligns: ["right", "left", "left", "left", "left", "left", "left", "left"],
        colTooltips: ["false", "true", "true", "true", "true", "true", "true", "true", "false"]
    }
};
var constructFormData = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "assembleComponentVersion.id"},
            {type: "hidden", name: "assembleComponentVersion.component.code"},
            {type: "hidden", name: "assembleComponentVersion.areaId", value: "1"},
            {type: "hidden", name: "assembleComponentVersion.assembleAreaId"},
            {type: "combo", label: "基础构件:", name: "baseComponentVersionId", required: true, showAll: true, width:202, tooltip: '基础构件不能为空'},
            {type: "input", label: "构件类名:", name: "assembleComponentVersion.component.name", maxLength:100, required: true, validate: "ValidateClassName", tooltip: '构件类名为字母、数字和下划线组成，首字母大写'},
            {type: "newcolumn"},
            {type: "input", label: "构件名称:", name: "assembleComponentVersion.component.alias", maxLength:100, required: true, tooltip: '构件名称不能为空'},
            {type: "input", label: "版本:", name: "assembleComponentVersion.version", maxLength:20, required: true, tooltip: '版本不能为空'}
          ]},
        {type: "block", width: "950", list:[
            {type: "input", label: "构件说明:&nbsp;&nbsp;&nbsp;", name: "assembleComponentVersion.remark", maxLength:500, rows:"3", inputWidth:"520"},
            {type: "newcolumn"},
            {type: "button", name: "save", value: "保存", width:80, offsetTop:"30"}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
function ValidateClassName (value) {
    return value.match(/^[A-Z]\w+$/);
}
/**
 * 初始化基础构件ComponentVersionCombo
 */
function initBasePageComponentVersionCombo(componentVersionCombo) {
    componentVersionCombo.clearAll(true);
    var componentVersionComboUrl = COMPONENT_VERSION_MODEL_URL + "!getBaseComponentVersionCombo.json";
    if (releasedSystem) {
        componentVersionComboUrl += "?isSystemUsed=1";
    }
    var jsonObj = loadJson(componentVersionComboUrl);
    if (jsonObj && jsonObj.length) {
        var opt_data = [];
        for (var m = 0; m < jsonObj.length; m++) {
            opt_data[m] = {
                text : jsonObj[m][1],
                value : jsonObj[m][0]
            };
        }
        componentVersionCombo.addOption(opt_data);
    }
}
/**
 * 初始化组合构件列表
 */
function initComponentGrid() {
    componentGrid.setImagePath(IMAGE_PATH);
    componentGrid.setHeader(componentGridData.format.headers.toString());
    componentGrid.setInitWidths(componentGridData.format.colWidths.toString());
    componentGrid.setColTypes(componentGridData.format.colTypes.toString());
    componentGrid.setColAlign(componentGridData.format.colAligns.toString());
    if (componentGridData.format.colTooltips) {
        componentGrid.enableTooltips(componentGridData.format.colTooltips.toString());
    }
    componentGrid.setSkin(Skin);
    componentGrid.init();
    componentGrid.enableMultiselect(true);
    componentGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    componentGrid.attachEvent("onRowSelect", function(rId, cInd) {
        componentGrid.cells(rId, 0).open();
        var subform = this.cellById(rId, 0).getSubForm();
        if (subform) {
            currentConstructId = subform.getItemValue("id");
        }
        currentComponentVersionAlias = componentGrid.cells(rId, 2).getValue();
    });
    componentGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = initDetailFormFormat(constructFormData);
    });
    componentGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        subform.setItemValue("assembleComponentVersion.assembleAreaId", currentTreeNodeId);
        var componentVersionCombo = subform.getCombo("baseComponentVersionId");
        initBasePageComponentVersionCombo(componentVersionCombo);
        if (id != "") {
            var url = CONSTRUCT_MODEL_URL + "!getConstructByAssemble.json?assembleComponentVersionId=" + id;
            loadForm(subform, url);
            var componentVersionIdCombo = subform.getCombo("baseComponentVersionId");
            if (componentVersionIdCombo.getActualValue() != "" && componentVersionIdCombo.getComboText() == componentVersionIdCombo.getActualValue()) {
                componentVersionIdCombo.setComboText(loadComponentVersionComboText(componentVersionIdCombo.getActualValue()));
            }
            currentConstructId = subform.getItemValue("id");
        } else {
            componentVersionCombo.attachEvent("onChange", function(v) {
                var baseComponentVersionId = componentVersionCombo.getActualValue();
                var baseComponentVersion = loadJson(COMPONENT_VERSION_MODEL_URL + "/" + baseComponentVersionId + ".json");
                if (baseComponentVersion) {
                    subform.setItemValue("assembleComponentVersion.component.name", baseComponentVersion.component.name + "A");
                    subform.setItemValue("assembleComponentVersion.component.alias", baseComponentVersion.component.alias + "_组合");
                }
            });
            subform.setItemValue("assembleComponentVersion.version", "V1.0");
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var _this = this;
                if (!_this.validate()) {
                    return;
                }
                var baseComponentVersionIdCombo = _this.getCombo("baseComponentVersionId");
                if (baseComponentVersionIdCombo.getComboText() == baseComponentVersionIdCombo.getActualValue()) {
                    dhtmlx.message(getMessage("selected_not_exist", "基础构件"));
                    return;
                }
                var id = _this.getItemValue("id");
                var componentVersionId = _this.getItemValue("assembleComponentVersion.id");
                var name = _this.getItemValue("assembleComponentVersion.component.name");
                var alias = _this.getItemValue("assembleComponentVersion.component.alias");
                var version = _this.getItemValue("assembleComponentVersion.version");
                var result = eval("(" + loadJson(COMPONENT_MODEL_URL + "!validateFields1.json?componentVersionId=" + componentVersionId + "&name=" + name + "&alias=" + encodeURIComponent(alias) + "&version=" + version) + ")");
                if (result.typeError) {
                    dhtmlx.message("该构件类名对应的构件类型不是组合构件，请修改构件类名！");
                    return;
                }
                if (result.updateAlias) {
                    dhtmlx.confirm({
                        type : "confirm",
                        text : "构件名称和原来的不同，是否需要加载原构件名称！",
                        ok : "是",
                        cancel : "否",
                        callback : function(flag) {
                            if (flag) {
                                _this.setItemValue("assembleComponentVersion.component.alias", result.oldAlias);
                            }
                            if (result.versionExist) {
                                dhtmlx.message(getMessage("form_field_exist", "版本"));
                                return;
                            }
                            if (id == "") {
                                SAVE_URL = CONSTRUCT_MODEL_URL + ".json";
                                _this.setItemValue("_method", "post");
                            } else {
                                SAVE_URL = CONSTRUCT_MODEL_URL + "/" + id + ".json";
                                _this.setItemValue("_method", "put");
                            }
                            _this.setItemValue("assembleComponentVersion.component.code", name);
                            _this.send(SAVE_URL, "post", function(loader, response) {
                                dhtmlx.message(getMessage("save_success"));
                                refreshComponentVersionGrid();
                                var content = loader.xmlDoc.responseText.replace(/:null/g, ":''");
                                var formData = eval("(" + content + ")");
                                loadFormData(_this, formData);
                                var baseComponentVersionIdCombo = _this.getCombo("baseComponentVersionId");
                                if (baseComponentVersionIdCombo.getActualValue() != "" && baseComponentVersionIdCombo.getComboText() == baseComponentVersionIdCombo.getActualValue()) {
                                    baseComponentVersionIdCombo.setComboText(loadComponentVersionComboText(baseComponentVersionIdCombo.getActualValue()));
                                }
                                currentConstructId = formData.id;
                                configConstruct();
                                tree.refreshItem(currentTreeNodeId);
                            });
                        }
                    });
                } else {
                    if (result.versionExist) {
                        dhtmlx.message(getMessage("form_field_exist", "版本"));
                        return;
                    }
                    if (id == "") {
                        SAVE_URL = CONSTRUCT_MODEL_URL + ".json";
                        _this.setItemValue("_method", "post");
                    } else {
                        SAVE_URL = CONSTRUCT_MODEL_URL + "/" + id + ".json";
                        _this.setItemValue("_method", "put");
                    }
                    _this.setItemValue("assembleComponentVersion.component.code", name);
                    _this.send(SAVE_URL, "post", function(loader, response) {
                        dhtmlx.message(getMessage("save_success"));
                        refreshComponentVersionGrid();
                        var content = loader.xmlDoc.responseText.replace(/:null/g, ":''");
                        var formData = eval("(" + content + ")");
                        currentConstructId = formData.id;
                        configConstruct();
                        tree.refreshItem(currentTreeNodeId);
                    });
                }
            }
        });
    });
    componentGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            componentGrid.forEachRow(function(rId) {
                if (id != rId) {
                    componentGrid.cells(rId, 0).close();
                }
            });
        }
    });
    if (pageable) {
        pagesize = getCookie("pagesize") || PAGE_SIZE;
        componentGrid.enablePaging(true, pagesize, 1, statusBar);
        componentGrid.setPagingSkin('toolbar', Skin);
    }
}
var ST_form;
/**
 * 字段检索
 * @returns {dhtmlXForm}
 */
function initSearchColumn() {
    var sformJson = [
        {type: "input",label: "构件类名或名称：", name: "value", className: "dhx_toolbar_form", width:120, inputHeight:17}
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
            var value = form.getItemValue("value"), param = "";
            value = encodeURIComponent(value);
            if (value !== "") {
                if (value.match(/^\w+$/)) {
                    param = "Q_LIKE_component.name=" + value;
                } else {
                    param = "Q_LIKE_component.alias=" + value;
                }
            }
            refreshComponentVersionGrid(param);
        }
    };
    return form;
}
/**
 * 导入构件
 * @param {obj} result
 */
function importComp(result) {
    if (result.existOldComponentVersion) {
        dhtmlx.confirm({
            type:"confirm",
            text: "该组合构件已经存在，是否覆盖！",
            ok: "是",
            cancel: "否",
            callback: function(flag) {
                if (flag) {
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
                    dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!saveAssembleComponentVersion.json?assembleComponentConfigKey=" + assembleComponentConfigKey + "&assembleAreaId=" + currentTreeNodeId, function(loader) {
                        msgWin.close();
                        refreshComponentVersionGrid();
                        tree.refreshItem(currentTreeNodeId);
                        var result = eval("(" + loader.xmlDoc.responseText + ")");
                        if (typeof result == 'string') {
                            result = eval("(" + result + ")");
                        }
                        if (result.success) {
                            dhtmlx.message("构件导入成功，构件生效需要重启系统！");
                        } else {
                            if (result.message) {
                                dhtmlx.message(result.message);
                            } else {
                                dhtmlx.message("构件导入失败！");
                            }
                        }
                    });
                }
            }
        });
    } else {
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
        dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!saveAssembleComponentVersion.json?assembleComponentConfigKey=" + assembleComponentConfigKey + "&assembleAreaId=" + currentTreeNodeId, function(loader) {
            msgWin.close();
            refreshComponentVersionGrid();
            tree.refreshItem(currentTreeNodeId);
            var result = eval("(" + loader.xmlDoc.responseText + ")");
            if (typeof result == 'string') {
                result = eval("(" + result + ")");
            }
            if (result.success) {
                dhtmlx.message("构件导入成功，构件生效需要重启系统！");
            } else {
                if (result.message) {
                    dhtmlx.message(result.message);
                } else {
                    dhtmlx.message("构件导入失败！");
                }
            }
        });
    }
}
/**
 * 初始化toolBar
 */
function initComponentToolBar() {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    var toolBarItemIndex = 0;
    toolBar.addButton("new", toolBarItemIndex++, "新增", "new.gif");
    toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    toolBar.addButton("config", toolBarItemIndex++, "配置", "setup.gif");
    toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    toolBar.addButton("delete", toolBarItemIndex++, "删除", "delete.gif");
    toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    if (!releasedSystem) {
        var previewOpts = [['preview', 'obj', '&nbsp;发布预览', "preview.gif"],
                           ['navigationPreview', 'obj', '&nbsp;导航条预览', "preview.gif"],
                           ['tabPreview', 'obj', '&nbsp;标签页预览', "preview.gif"]];
        toolBar.addButtonSelect("repeatPreview", toolBarItemIndex++, "本地预览", previewOpts, "preview.gif");
    } else {
        toolBar.addButton("preview", toolBarItemIndex++, "预览", "preview.gif");
    }
    toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    toolBar.addButton("import", toolBarItemIndex++, "导入构件", "upload.gif");
    toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    toolBar.addButton("package", toolBarItemIndex++, "打包", "package.gif");
    toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    toolBar.addButton("download", toolBarItemIndex++, "下载", "download.gif");
    toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    toolBar.addButton("configFilter", toolBarItemIndex++, "配置过滤条件", "setup.gif");
    toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    toolBar.addButton("changeArea", toolBarItemIndex++, "更改分类", "update.gif");
    toolBar.addSeparator("septr$" + toolBarItemIndex, toolBarItemIndex++);
    toolBar.addButton("refresh", toolBarItemIndex++, "刷新", "refresh.gif");
    toolBar.addButton("sous", toolBarItemIndex++, "", "search.gif", null, "right");
    toolBar.addDiv("top$searchTextdiv", toolBarItemIndex++, "right");
    ST_form = initSearchColumn();
    toolBar.attachEvent('onClick', function(id) {
        if (id == "new") {
            componentGrid.addSubRow();
        } else if (id == "config") {
            var selectIds = componentGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds == "") {
                refreshComponentVersionGrid();
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            configConstruct();
        } else if (id == "delete") {
            var selectIds = componentGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds == "") {
                refreshComponentVersionGrid();
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!deleteValid.json?componentVersionId=" + selectIds, function(loader) {
                            var result = eval("(" + eval(loader.xmlDoc.responseText) + ")");
                            if (!result.success) {
                                dhtmlx.message(result.message);
                            } else {
                                dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!deleteComponentVersion.json?componentVersionId=" + selectIds, function(loader) {
                                    var result = eval(loader.xmlDoc.responseText);
                                    if (result != "") {
                                        dhtmlx.message(result);
                                    }
                                    refreshComponentVersionGrid();
                                    tree.refreshItem(currentTreeNodeId);
                                });
                            }
                        });
                    }
                }
            });
        } else if (id == "preview") {
            var selectIds = componentGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds == "") {
                refreshComponentVersionGrid();
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
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
            var componentViews = componentGrid.cells(selectIds, 6).getValue();
            dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!preview.json?assembleComponentVersionId=" + selectIds, function(loader) {
                var result = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof result == 'string') {
                    result = eval("(" + result + ")");
                }
                if (result.status == false) {
                    dhtmlx.message(result.message);
                } else if (result.status == true) {
                    var componentUrl = result.url;
                    if (componentViews == "coral40") {
                        if (componentUrl.indexOf(previewSystemPath) != -1) {
                            componentUrl = componentUrl.substring(previewSystemPath.length);
                            var w = window.open(previewSystemPath + "/cfg-resource/coral40/views/config/preview.jsp?CFG_componentUrl=" + componentUrl,
                                'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                            w.focus();
                        } else {
                            componentUrl = componentUrl.substring(contextPath.length);
                            var w = window.open(contextPath + "/cfg-resource/coral40/views/config/preview.jsp?CFG_componentUrl=" + componentUrl,
                                'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                            w.focus();
                        }
                    } else {
                        var w = window.open(componentUrl, 'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height=" + (screen.availHeight - 50) + ",width=" + (screen.availWidth - 10) + ",status=yes");
                        w.focus();
                    }
                } else {
                    dhtmlx.message("预览失败！");
                }
                msgWin.close();
            });
        } else if (id == "repeatPreview") {
            var selectIds = componentGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds == "") {
                refreshComponentVersionGrid();
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            var componentViews = componentGrid.cells(selectIds, 6).getValue();
            dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!repeatPreview.json?assembleComponentVersionId=" + selectIds, function(loader) {
                var result = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof result == 'string') {
                    result = eval("(" + result + ")");
                }
                var componentUrl = result.url;
                if (componentViews == "coral40") {
                    if (componentUrl.indexOf(previewSystemPath) != -1) {
                        componentUrl = componentUrl.substring(previewSystemPath.length);
                        var w = window.open(previewSystemPath + "/cfg-resource/coral40/views/config/preview.jsp?CFG_componentUrl=" + componentUrl,
                            'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                        w.focus();
                    } else {
                        componentUrl = componentUrl.substring(contextPath.length);
                        var w = window.open(contextPath + "/cfg-resource/coral40/views/config/preview.jsp?CFG_componentUrl=" + componentUrl,
                            'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
                        w.focus();
                    }
                } else {
                    var w = window.open(componentUrl, 'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height=" + (screen.availHeight - 50) + ",width=" + (screen.availWidth - 10) + ",status=yes");
                    w.focus();
                }
            });
        } else if (id == "navigationPreview") {
            var selectIds = componentGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds == "") {
                refreshComponentVersionGrid();
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!navigationPreview.json?assembleComponentVersionId=" + selectIds, function(loader) {
                var result = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof result == 'string') {
                    result = eval("(" + result + ")");
                }
                var w = window.open(result.url, 'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height=" + (screen.availHeight - 50) + ",width=" + (screen.availWidth - 10) + ",status=yes");
                w.focus();
            });
        } else if (id == "tabPreview") {
            var selectIds = componentGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds == "") {
                refreshComponentVersionGrid();
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!tabPreview.json?assembleComponentVersionId=" + selectIds, function(loader) {
                var result = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof result == 'string') {
                    result = eval("(" + result + ")");
                }
                var w = window.open(result.url, 'preview', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height=" + (screen.availHeight - 50) + ",width=" + (screen.availWidth - 10) + ",status=yes");
                w.focus();
            });
        } else if (id == "import") {
            var current = new Date();
            assembleComponentConfigKey = current.getTime() + "_" + Math.floor(Math.random()*100);
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
            var UPLOAD_URL = CONSTRUCT_MODEL_URL + "!uploadHandler";
            var GET_INFO_URL = CONSTRUCT_MODEL_URL + "!getInfoHandler";
            var GET_ID_URL = CONSTRUCT_MODEL_URL + "!getIdHandler";
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
                dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!getUploadMessage.json?assembleComponentConfigKey=" + assembleComponentConfigKey, function(loader) {
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
            vault.setFormField("assembleComponentConfigKey", assembleComponentConfigKey);
            dataWin.attachObject(vaultDiv);
        } else if (id == "package") {
            var selectIds = componentGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
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
            dhtmlxAjax.get(CONSTRUCT_MODEL_URL + "!packageComponent.json?id=" + selectIds, function(loader){
                var result = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof result == 'string') {
                    result = eval("(" + result + ")");
                }
                if (result.success) {
                    dhtmlx.message(result.message);
                } else {
                    dhtmlx.alert(result.message);
                }
                refreshComponentVersionGrid();
                msgWin.close();
            });
        } else if (id == "download") {
            var selectIds = componentGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            } else if (componentGrid.cells(selectIds, 7).getValue() == "0") {
                dhtmlx.message("该构件未打包，请先打包！");
                return;
            }    
            download(COMPONENT_VERSION_MODEL_URL + "!downloadComponent.json?id=" + selectIds);
        } else if (id == "configFilter") {
            var selectIds = componentGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            configFilter(selectIds);
        } else if (id == "changeArea") {
            var selectIds = componentGrid.getSelectedRowId();
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
            areaTree.attachEvent("onMouseIn", function(id) {
                areaTree.setItemStyle(id, "background-color:#D5E8FF;");
            });
            areaTree.attachEvent("onMouseOut", function(id) {
                areaTree.setItemStyle(id, "background-color:#FFFFFF;");
            });
            areaTree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
            areaTree.setDataMode("json");
            areaTree.enableSmartXMLParsing(true);
            areaTree.loadJSONObject(eval("(" + loadJson(COMPONENT_ASSEMBLE_AREA_MODEL_URL+"!getAreaTree.json?currentAreaId="+currentTreeNodeId) + ")"));
            var areaStatusBar = areaWin.attachStatusBar();
            var areaToolBar = new dhtmlXToolbarObject(areaStatusBar);
            areaToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
            areaToolBar.addButton("submit", 1, "&nbsp;&nbsp;确定&nbsp;&nbsp;");
            areaToolBar.addButton("cancle", 2, "&nbsp;&nbsp;取消&nbsp;&nbsp;");
            areaToolBar.setAlign("right");
            areaToolBar.attachEvent('onClick', function(id) {
                if (id == "submit") {
                    var selectAssembleAreaId = areaTree.getSelectedItemId();
                    if (!selectAssembleAreaId) {
                        dhtmlx.message("请先选择构件分类！");
                        return;
                    } else if (selectAssembleAreaId == currentTreeNodeId) {
                        dhtmlx.message("该构件已经在当前分类中，请选择其他分类！");
                        return;
                    } else if (selectAssembleAreaId == -1) {
                        dhtmlx.message("请选择其他分类！");
                        return;
                    }
                    dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL+"!changeAssembleArea.json?componentVersionIds="+selectIds+"&assembleAreaId="+selectAssembleAreaId, function(loader) {
                        var result = eval(loader.xmlDoc.responseText);
                        if (result != "") {
                            dhtmlx.message(result);
                        }
                        areaWin.close();
                        refreshComponentVersionGrid();
                        tree.refreshItem(currentTreeNodeId);
                    });
                } else {
                    areaWin.close();
                }
            });
        } else if (id == "refresh") {
            refreshComponentVersionGrid();
        } else if (id == "sous") {
            var value = ST_form.getItemValue("value"),
                param = "";
            value = encodeURIComponent(value);
            if (value !== "") {
                if (value.match(/^\w+$/)) {
                    param = "Q_LIKE_component.name=" + value;
                } else {
                    param = "Q_LIKE_component.alias=" + value;
                }
            }
            refreshComponentVersionGrid(param);
        }
    });
}
/**
 * 配置组合构件配置信息
 */
function configConstruct() {
    if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var baseConfigWin = dhxWins.createWindow("baseConfigWin", 0, 0, 1000, 400);
    baseConfigWin.setModal(true);
    baseConfigWin.setText("组合构件配置(预览组合构件时使用)");
    baseConfigWin.center();
    var configTabbar = baseConfigWin.attachTabbar();
    configTabbar.setImagePath(IMAGE_PATH);
    configTabbar.addTab("baseSelfConfig", "构件自身参数设置", "120px");
    configTabbar.addTab("baseInputParamConfig", "构件输入参数配置", "120px");
    configTabbar.setTabActive("baseSelfConfig");
    baseSelfConfigLayout = configTabbar.cells("baseSelfConfig").attachLayout("1C");
    initbaseSelfConfigLayout();
    baseInputParamConfigLayout = configTabbar.cells("baseInputParamConfig").attachLayout("1C");
    initbaseInputParamConfigLayout();
}
var baseInputParamConfigGridData = {
    format: {
        headers: ["&nbsp;", "<center>名称</center>", "<center>参数说明</center>", "<center>值</center>", ""],
        colWidths: ["30", "150", "250", "150", "*"],
        colTypes: ["sub_row_form", "ro", "ro", "ro", "ro"],
        colAligns: ["right", "left", "left", "left"],
        colTooltips: ["false", "true", "true", "true", "false"]
    }
};
var baseInputParamFormData = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "inputParamId"},
            {type: "input", label: "名称:", name: "name", readonly:true},
            {type: "input", label: "值:", name: "value", maxLength:200},
            {type: "newcolumn"},
            {type: "block", width: "120", offsetTop:"22", list:[
                {type: "button", name: "save", value: "保存", width:80}
            ]}
        ]}
    ],
    settings: {labelWidth: 80, inputWidth: 200}
};
/**
 * 初始化菜单输出参数配置layout
 */
function initbaseInputParamConfigLayout() {
    baseInputParamConfigLayout.cells('a').hideHeader();
    baseInputParamConfigGrid = baseInputParamConfigLayout.cells('a').attachGrid();
    baseInputParamConfigGrid.setImagePath(IMAGE_PATH);
    baseInputParamConfigGrid.setHeader(baseInputParamConfigGridData.format.headers.toString());
    baseInputParamConfigGrid.setInitWidths(baseInputParamConfigGridData.format.colWidths.toString());
    baseInputParamConfigGrid.setColTypes(baseInputParamConfigGridData.format.colTypes.toString());
    baseInputParamConfigGrid.setColAlign(baseInputParamConfigGridData.format.colAligns.toString());
    if (baseInputParamConfigGridData.format.colTooltips) {
        baseInputParamConfigGrid.enableTooltips(baseInputParamConfigGridData.format.colTooltips.toString());
    }
    baseInputParamConfigGrid.setSkin(Skin);
    baseInputParamConfigGrid.init();
    baseInputParamConfigGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    baseInputParamConfigGrid.attachEvent("onRowSelect", function(rId, cInd) {
        baseInputParamConfigGrid.cells(rId, 0).open();
    });
    baseInputParamConfigGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = initDetailFormFormat(baseInputParamFormData);
    });
    baseInputParamConfigGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        var url = CONSTRUCT_INPUT_PARAM_MODEL_URL + "/" + id + ".json?_method=get";
        loadForm(subform, url);
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                subform.setItemValue("_method", "put");
                subform.send(CONSTRUCT_INPUT_PARAM_MODEL_URL + "/" + id, "post", function(loader, response) {
                    dhtmlx.message(getMessage("save_success"));
                    baseInputParamConfigGridLoadData();
                });
            }
        });
    });
    baseInputParamConfigGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            baseInputParamConfigGrid.forEachRow(function(rId) {
                if (id != rId) {
                    baseInputParamConfigGrid.cells(rId, 0).close();
                }
            });
        }
    });
    baseInputParamConfigGridLoadData();
}
/**
 * baseInputParamConfigGrid加载数据
 */
function baseInputParamConfigGridLoadData() {
    var url = CONSTRUCT_INPUT_PARAM_MODEL_URL + "!getInputParamList.json?E_model_name=datagrid&constructId=" + currentConstructId;
    loadParamGridData(baseInputParamConfigGrid, url);
}
var baseSelfConfigGridData = {
    format: {
        headers: ["&nbsp;", "<center>名称</center>", "<center>参数说明</center>", "<center>值</center>", ""],
        cols: ["id", "name", "remark", "text"],
        colWidths: ["30", "150", "250", "150", "*"],
        colTypes: ["sub_row_form", "ro", "ro", "ro", "ro"],
        colAligns: ["right", "left", "left", "left"],
        colTooltips: ["false", "true", "true", "true", "false"]
    }
};
var baseSelfConfigFormData;
/**
 * 初始化基础构件的自身配置layout
 */
function initbaseSelfConfigLayout() {
    baseSelfConfigLayout.cells('a').hideHeader();
    baseSelfConfigGrid = baseSelfConfigLayout.cells('a').attachGrid();
    baseSelfConfigGrid.setImagePath(IMAGE_PATH);
    baseSelfConfigGrid.setHeader(baseSelfConfigGridData.format.headers.toString());
    baseSelfConfigGrid.setInitWidths(baseSelfConfigGridData.format.colWidths.toString());
    baseSelfConfigGrid.setColTypes(baseSelfConfigGridData.format.colTypes.toString());
    baseSelfConfigGrid.setColAlign(baseSelfConfigGridData.format.colAligns.toString());
    if (baseSelfConfigGridData.format.colTooltips) {
        baseSelfConfigGrid.enableTooltips(baseSelfConfigGridData.format.colTooltips.toString());
    }
    baseSelfConfigGrid.setSkin(Skin);
    baseSelfConfigGrid.init();
    baseSelfConfigGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    baseSelfConfigGrid.attachEvent("onRowSelect", function(rId, cInd) {
        baseSelfConfigGrid.cells(rId, 0).open();
    });
    baseSelfConfigGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform, selectId) {
        GET_BY_ID_URL = CONSTRUCT_SELF_PARAM_MODEL_URL + "/" + selectId + ".json?_method=get";
        var formObj = loadJson(GET_BY_ID_URL);
        if (formObj.type == "1") {
            baseSelfConfigFormData = {
                format: [
                    {type: "block", width: "800", list:[
                        {type: "hidden", name: "id"},
                        {type: "input", label: "名称:", name: "name", readonly: true},
                        {type: "combo", label: "值:", name: "value", options: eval(formObj.options)},
                        {type: "newcolumn"},
                        {type: "block", width: "120", offsetTop:"22", list:[
                            {type: "button", name: "save", value: "保存", width:80}
                        ]}
                    ]}
                ],
                settings: {labelWidth: 80, inputWidth: 200}
            };
        } else if (formObj.type == "2") {
            baseSelfConfigFormData = {
                format: [
                    {type: "block", width: "800", list:[
                        {type: "hidden", name: "id"},
                        {type: "input", label: "名称:", name: "name", readonly: true},
                        {type: "multiselect", label: "值:", name: "value", inputHeight:90, inputWidth:130, options: eval(formObj.options)},
                        {type: "newcolumn"},
                        {type: "block", width: "120", offsetTop:"22", list:[
                            {type: "button", name: "save", value: "保存", width:80}
                        ]}
                    ]}
                ],
                settings: {labelWidth: 80, inputWidth: 200}
            };
        } else {
            baseSelfConfigFormData = {
                format: [
                    {type: "block", width: "800", list:[
                        {type: "hidden", name: "id"},
                        {type: "input", label: "名称:", name: "name", readonly: true},
                        {type: "input", label: "值:", name: "value", maxLength:200},
                        {type: "newcolumn"},
                        {type: "block", width: "120", offsetTop:"22", list:[
                            {type: "button", name: "save", value: "保存", width:80}
                        ]}
                    ]}
                ],
                settings: {labelWidth: 80, inputWidth: 200}
            };
        }
        subform.c = initDetailFormFormat(baseSelfConfigFormData);
    });
    baseSelfConfigGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        var url = CONSTRUCT_SELF_PARAM_MODEL_URL + "/" + id + ".json?_method=get";
        loadForm(subform, url);
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                var value = subform.getItemValue("value");
                var valueType = subform.getItemType("value");
                var text;
                if (valueType == "combo") {
                    var valueCombo = subform.getCombo("value");
                    if (value == '') {
                        text = "";
                    } else {
                        text = valueCombo.getComboText();
                    }
                } else if (valueType == "multiselect") {
                    var k = [];
                    var opts = subform.getOptions("value");
                    for (var i = 0; i < opts.length; i++) {
                        if (opts[i].value == '') {
                            continue;
                        }
                        if (opts[i].selected) {
                            k.push(opts[i].text);
                        }
                    }
                    text = k.toString();
                } else {
                    text = subform.getItemValue("value");
                }
                dhtmlxAjax.get(CONSTRUCT_SELF_PARAM_MODEL_URL + "!saveConstructSelfParam.json?id=" + id + "&value=" + encodeURIComponent(value) + "&text=" + encodeURIComponent(text), function() {
                    dhtmlx.message(getMessage("save_success"));
                    baseSelfConfigGridLoadData();
                });
            }
        });
    });
    baseSelfConfigGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            baseSelfConfigGrid.forEachRow(function(rId) {
                if (id != rId) {
                    baseSelfConfigGrid.cells(rId, 0).close();
                }
            });
        }
    });
    baseSelfConfigGridLoadData();
}
/**
 * grid加载数据
 */
function baseSelfConfigGridLoadData() {
    baseSelfConfigGrid.clearAll();
    var url = CONSTRUCT_SELF_PARAM_MODEL_URL + "!search.json?E_model_name=datagrid&F_in=" + baseSelfConfigGridData.format.cols.toString() + "&Q_EQ_constructId=" + currentConstructId;
    var loader = dhtmlxAjax.getSync(url);
    if (loader.xmlDoc.responseText == "")
        return;
    // 替换列表显示为“null” 的列 --wm
    var reg = new RegExp("\"null\"", "g");
    var loaderDoc = loader.xmlDoc.responseText;
    if (loaderDoc.indexOf("null") != -1) {
        var dataJson = loaderDoc.replace(reg, "\"\"");
    }
    var rows = eval('(' + dataJson + ')');
    // 如果查询结果为空
    if (rows.rows == undefined) {
        //dhtmlx.message("结果集为空！");
        return;
    }
    baseSelfConfigGrid.parse(rows, "json");
}