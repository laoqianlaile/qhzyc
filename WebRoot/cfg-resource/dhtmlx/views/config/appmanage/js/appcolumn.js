/**
 * 初始化列表字段定义窗口
 */
function initAppColumnWin(win, componentVersionId, f_close/*function*/) {
    APP_columncfg(win, app, componentVersionId, f_close);
}
/**
 * 初始化列表字段定义窗口布局
 */
function APP_columncfg(win, app/*{modified:boolean, tableId:string}*/, componentVersionId, f_close/*function*/) {
    var initLayout = win.attachLayout("2E");
    var initLayoutA = initLayout.cells("a");
    initLayoutA.setText("预览界面");
    initLayoutA.hideHeader();
    initLayoutA.setHeight(60);
    var initLayoutB = initLayout.cells("b");
    var dhxLayout = initLayoutB.attachLayout("3W");
    var tableId = app.tableId;
	// 工作流配置（tableId为工作流对应的视图ID）
	if (true === app.isWorkflow) {
		tableId = app.viewId;
	}
    
    // var dhxLayout = win.attachLayout("3W");
    dhxLayout.cont.obj._offsetTop = 1;
    dhxLayout.cont.obj._offsetLeft = 1;
    dhxLayout.cont.obj._offsetHeight = -2;
    dhxLayout.cont.obj._offsetWidth = -2;
    dhxLayout.setSizes();

    var grid = initLayoutA.attachGrid();
    function getGridCfg() {
        var url = AppActionURI.appColumn + "!dhtmlxGrid.json?P_tableId=" + tableId + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + app.menuId;
        return loadJson(addTimestamp(url));
    }
    var gcfg;
    function initGrid() {
        gcfg = getGridCfg();
        if (null != gcfg && 0 != gcfg.headers.length) {
            grid.setImagePath(IMAGE_PATH);
            grid.setHeader(gcfg.headers.toString());
            grid.setInitWidths(gcfg.widths.toString());
            grid.setColTypes(gcfg.types.toString());
            grid.setColAlign(gcfg.aligns.toString());
            grid.setStyle("font-weight:bold;", "", "", "");
            grid.setSkin(Skin);
            grid.init();
            grid.enableColumnMove(true);
        }
    }
    initGrid();
    var types = "";
    var headers = gcfg.headers.toString().split(",");
    var colIds = gcfg.columnIds.toString().split(",");
    var widths = gcfg.widths.toString().split(",");
    var aligns = gcfg.aligns.toString().split(",");
    var sbar = dhxLayout.attachStatusBar();
    var btbar = new dhtmlXToolbarObject(sbar.id);
    btbar.setIconPath(TOOLBAR_IMAGE_PATH);
    /*if (!app.isCustom && "combogrid" !== app.usage) {
        var addColumnOpts = [['addViewForm', 'obj', '添加详细', "new.gif"],
    	                   ['addViewGrid', 'obj', '添加明细', "new.gif"]];
    	btbar.addButtonSelect("addcolumn", 0, "添加列", addColumnOpts, "new.gif");
    	btbar.addSeparator("bottom$septr$00", 1);
    }*/
    btbar.addButton("bottom$setting", 0, "高级配置", "setup.gif");
    btbar.addSeparator("bottom$septr$00", 1);
    btbar.addButton("bottom$preview", 2, "预览", "preview.gif");
    btbar.addSeparator("bottom$septr$01", 3);
    btbar.addButton("bottom$save", 4, "保存", "save.gif");
    btbar.addSeparator("botton$septr$02", 5);
    btbar.addButton("bottom$clear", 6, "清空配置", "delete.gif");
    btbar.addSeparator("bottom$septr$03", 7);
    btbar.addButton("bottom$close", 8, "关闭", "close.gif");
    btbar.setAlign("right");
    btbar.attachEvent("onClick", function(itemId) {
        if ("addcolumn" == itemId) {
            var rRowId = rGrid.uid();
            rGrid.addRow(rRowId, [rRowId, "操作", "120", "center", "link", ""]);
            rGrid.setUserData(rRowId, "columnId", "-1"); // columnId
            rGrid.setUserData(rRowId, "columnName", ""); // columnName
            rGrid.setUserData(rRowId, "columnType", "2"); // columnType
            rGrid.setRowHidden(rRowId, true);
            app.modified = true;
        } else if ("addViewForm" == itemId) {
            var rRowId = rGrid.uid();
            rGrid.addRow(rRowId, [rRowId, "操作", "120", "center", "link", "/ces/Q1!viewDetailForm?title=详细"]);
            rGrid.setUserData(rRowId, "columnId", "-1"); // columnId
            rGrid.setUserData(rRowId, "columnName", "详细"); // columnName
            rGrid.setUserData(rRowId, "columnType", "2"); // columnType
            rGrid.setRowHidden(rRowId, true);
            app.modified = true;
        } else if ("addViewGrid" == itemId) {
            var rRowId = rGrid.uid();
            rGrid.addRow(rRowId, [rRowId, "操作", "120", "center", "link", "/ces/Q1!viewDetailGrid?title=明细&tableName=关联表表名"]);
            rGrid.setUserData(rRowId, "columnId", "-1"); // columnId
            rGrid.setUserData(rRowId, "columnName", "明细"); // columnName
            rGrid.setUserData(rRowId, "columnType", "2"); // columnType
            rGrid.setRowHidden(rRowId, true);
            app.modified = true;
        } else if ("bottom$setting" == itemId){
        	createSettingWin();
        } else if ("bottom$preview" == itemId) {
            var cnt = rGrid.getRowsNum();
            if (0 == cnt) {
                dhtmlx.alert("请先进行列表字段配置，再预览！");
                return;
            }
            grid.clearAll(true);
            var header = "";
            var width = "";
            var type = "";
            var align = "";
            for (var i = 0; i < cnt; i++) {
                var rowId = rGrid.getRowId(i);
                var name = rGrid.cells(rowId, 1).getValue();
                header += ",<center>" + name + "</center>";
                var wid = rGrid.cells(rowId, 2).getValue();
                width += "," + ((isEmpty(wid)) ? "*" : wid);
                var ali = rGrid.cells(rowId, 3).getValue();
                align += "," + ali;
                type += ",ro";
            }
            grid.setHeader(header.substring(1));
            grid.setInitWidths(width.substring(1));
            grid.setColTypes(type.substring(1));
            grid.setColAlign(align.substring(1));
            grid.setStyle("font-weight:bold;", "", "", "");
            grid.setSkin(Skin);
            grid.init();
            grid.enableColumnMove(true);
        } else if ("bottom$close" == itemId) {
            if (typeof f_close == "function")
                f_close();
        } else if ("bottom$save" == itemId) {
            var cnt = rGrid.getRowsNum();
            if (0 == cnt) {
                dhtmlx.alert("请先进行列表字段配置，再保存！");
                return;
            }
            if ("V" == app.classification) {
                var rlt = loadJson(AppActionURI.columnDefine + "!checkUnique.json?id=&Q_EQ_columnName=ID&Q_EQ_tableId="
                        + tableId);
                if (rlt.success) {
                    dhtmlx.alert("视图（" + app.tableComment + "）中没有 <font color='red'>ID</font> 字段，无法配置列表！");
                    return;
                }
            }
            //var fd  = pform.getFormData();
            var rowsValue = "";
            for (var i = 0; i < cnt; i++) {
                var rowId = rGrid.getRowId(i);
                rGrid.cells(rowId, 0).close();
                var subform = rGrid.cells(rowId, 0).getSubForm();
                // var columnId = rGrid.cells(rowId, 0).getValue();
                var showName, columnType, columnName, url;
                if (subform) {
                    showName = subform.getItemValue("showName");
                    columnType = subform.getItemValue("columnType");
                    columnName = subform.getItemValue("columnName");
                    url = subform.getItemValue("url");
                } else {
                    showName = rGrid.cells(rowId, 1).getValue();
                    columnType = rGrid.getUserData(rowId, "columnType");
                    columnName = rGrid.getUserData(rowId, "columnName");
                    url = rGrid.cells(rowId, 5).getValue();
                }
                var width = rGrid.cells(rowId, 2).getValue();
                var align = rGrid.cells(rowId, 3).getValue();
                var type = rGrid.cells(rowId, 4).getValue();
                var columnId = rGrid.getUserData(rowId, "columnId");
                if (isEmpty(columnType))
                    columnType = "0";
                rowsValue += ";" + columnId + "|" + encodeURI(columnName) + "|" + encodeURI(showName) + "|" + width
                        + "|" + align + "|" + type + "|" + encodeURIComponent(url) + "|" + columnType;
            }
            rowsValue = rowsValue.substring(1);
            var url = AppActionURI.appGrid + "!save.json";
            var params = "tableId=" + tableId + "&componentVersionId=" + componentVersionId + "&menuId=" + app.menuId
	            + "&P_rowsValue=" + rowsValue + "&P_isDefault=0";
            if (true === app.isWorkflow) { params += "&userId=1";} // 工作流时,没有个性化设置
            dhtmlxAjax.post(addTimestamp(url), params, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (jsonObj.success) {
                    dhtmlx.message(getMessage("operate_success"));
                    initGrid();
                    app.modified = false;
                } else {
                    dhtmlx.message(getMessage("operate_failure"));
                }
                loadGridData(rGrid, rcfg, rurl);
            });
        } else if ("bottom$clear" == itemId) {
            dhtmlx.confirm({
                type : "confirm",
                text : "确定要清空列表字段配置？",
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        var url = AppActionURI.appGrid + "!clear.json?tableId=" + tableId
                                + "&componentVersionId=" + componentVersionId+ "&menuId=" + app.menuId;
                        dhtmlxAjax.get(addTimestamp(url), function(loader) {
                            var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                            if (jsonObj.success) {
                                rGrid.forEachRow(function(rowId) {
                                    toLeft(rowId);
                                });
                                rGrid.clearAll();
                                //pform.uncheckItem("hasRowNumber");
                                //pform.setItemValue("dblclick", 0);
                                dhtmlx.message(getMessage("operate_success"));
                                grid.clearAll(true);
                            } else {
                                dhtmlx.message(getMessage("operate_failure"));
                            }
                            app.modified = false;
                        });
                    }
                }
            });
        }
    });

    var lLayout = dhxLayout.cells("a");
    var cLayout = dhxLayout.cells("b");
    var rLayout = dhxLayout.cells("c");
    lLayout.setWidth(205);
    cLayout.setWidth(115);
    lLayout.hideHeader();
    cLayout.hideHeader();
    rLayout.hideHeader();
    var ttbar = dhxLayout.attachToolbar();
    ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    ttbar.addDiv("top$columndiv", 0);
	ttbar.addSeparator("top$septr$01", 1);
	//ttbar.addDiv("top$gridpaneldiv", 2);
    // ttbar.addLabel("warnning", 1, "<font color=\"red\"><strong>注：宽度为-1表示自适应<strong></font>");

    var sform = initSearchArea();
    //var pform = initGridPanel();
    /*ttbar.addDiv("inpselcetDiv", 3);
    var inpselcet = new dhtmlXCombo("inpselcetDiv", "inpselcet", 193);
    inpselcet.readonly(true);
    var od = [{value : "0", text : "请选择列表双击事件"}, 
    	{value : "1", text : "双击事件 - 弹出修改界面"},
    	{value : "2", text : "双击事件 - 弹出浏览界面"}];
    inpselcet.addOption(od);*/

    var lcfg = {
        format : {
            headers : ["<center>字段名称</center>"],
            cols : ["showName"], // showName
            id : ["id"], // columnId
            userdata : ["columnId", "width", "align", "type", "columnName"], // width,
            colWidths : ["200"],
            colTypes : ["ro"],
            colAligns : ["left"]
        }
    };
    var rcfg = {
        format : {
            headers : ["&nbsp;", "<center>表头名称</center>", "<center>宽度</center>", "<center>对齐</center>", "<center>类型</center>", "<center>链接地址</center>"],
            cols : ["id", "showName", "width", "align", "type", "url"], // columnId,
            id : ["id"], // id
            userdata : ["columnId", "columnName", "columnType"], // columnId,
            colWidths : ["40", "200", "80", "80", "80", "*"],
            colTypes : ["sub_row_form", "ro", "ed", "co", "co", "ro"],
            colAligns : ["center", "left", "right", "center", "center", "left"]
        }
    };

    var lGrid = dhxLayout.cells("a").attachGrid();
    var rGrid = dhxLayout.cells("c").attachGrid();
    rGrid.enableDragAndDrop(true);
    var rcombo = rGrid.getCombo(3);
    rcombo.put("left", "靠左");
    rcombo.put("center", "居中");
    rcombo.put("right", "靠右");
    var tcombo = rGrid.getCombo(4);
    tcombo.put("ro", "文本");
    tcombo.put("hidden", "隐藏");
    tcombo.put("editable", "可编辑");
    tcombo.put("value", "显示编码");
    tcombo.put("card", "缩略图信息");
    tcombo.put("ro_card", "文本和缩略图信息");
    tcombo.put("link", "链接");
    //tcombo.put("img", "图片");
    if (app.isCustom)
        rGrid.setColumnHidden(0, true);
    var params = "E_model_name=datagrid&E_frame_name=coral&P_tableId=" + tableId + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + app.menuId;
    var lurl = AppActionURI.appColumn + "!defaultColumn.json?" + params;
    var rurl = AppActionURI.appColumn + "!defineColumn.json?" + params;
    initGridWithoutColumnsAndPageable(lGrid, lcfg, lurl);
    initGridWithoutColumnsAndPageable(rGrid, rcfg, rurl);
    lGrid.attachEvent("onRowDblClicked", function(rowId, cInd) {
        if (null == rowId)
            return;
        // add row in right grid
        var showName = lGrid.cells(rowId, 0).getValue();
        var width = lGrid.getUserData(rowId, "width");
        var align = lGrid.getUserData(rowId, "align");
        var type = lGrid.getUserData(rowId, "type");
        var columnName = lGrid.getUserData(rowId, "columnName");
        var columnId = lGrid.getUserData(rowId, "columnId");
        var url = lGrid.getUserData(rowId, "url");
        if (isEmpty(type))
            type = "ro";
        if (isEmpty(url))
            url = "";
        var rRowId = rGrid.uid();
        rGrid.addRow(rRowId, [rRowId, showName, width, align, type, url]);
        rGrid.setUserData(rRowId, "columnId", columnId); // columnId
        rGrid.setUserData(rRowId, "columnName", columnName); // columnName
        rGrid.setUserData(rRowId, "columnType", "0"); // columnType
        // delete current row
        lGrid.deleteRow(rowId);
        app.modified = true;
    });

    rGrid.attachEvent("onDrop", function(sId, tId, dId, sObj, tObj, sCol, tCol) {
        app.modified = true;
    });
    rGrid.attachEvent("onEditCell", function(stage, rId, cInd, nValue, oValue) {
        if (cInd == 2 && isNotEmpty(nValue) && !nValue.match(/^[1-9][0-9]*$/)) { // 宽度检查
            dhtmlx.alert("宽度：请输入大于0的整数或为空（为空表示自适应）！");
            return false;
        }
        if (nValue != oValue)
            app.modified = true;
        return true;
    });

    createEmptyDiv("DIV-oparatorArea-column", 33);
    var buttonForm = new dhtmlXForm("DIV-oparatorArea-column", getMoveButton());
    dhxLayout.cells("b").attachObject("DIV-oparatorArea-column");
    buttonForm.attachEvent("onButtonClick", function(id) {
        if ("toRight" == id) {
            var rowIds = lGrid.getSelectedRowId();
            if (null == rowIds)
                return;
            var rowArray = rowIds.split(",");
            for (var i = 0; i < rowArray.length; i++) {
                toRight(rowArray[i]);
            }
            lGrid.deleteSelectedRows();
        } else if ("allToRight" == id) {
            lGrid.forEachRow(function(rowId) {
                toRight(rowId);
            });
            lGrid.clearAll();
        } else if ("toLeft" == id) {
            var rowIds = rGrid.getSelectedRowId();
            if (null == rowIds)
                return;
            var rowArray = rowIds.split(",");
            for (var i = 0; i < rowArray.length; i++) {
                toLeft(rowArray[i]);
            }
            // rGrid.deleteSelectedRows();
        } else if ("allToLeft" == id) {
            var cnt = rGrid.getRowsNum();
            for (var i = cnt - 1; i > -1; i--) {
                var rowId = rGrid.getRowId(i);
                toLeft(rowId);
            }
            /*rGrid.forEachRow(function(rowId) {
            	toLeft(rowId);
            });
            rGrid.clearAll();*/
        }
        app.modified = true;
    });
    // 向右移
    function toRight(lRowId) {
        var showName = lGrid.cells(lRowId, 0).getValue();
        var width = lGrid.getUserData(lRowId, "width");
        var align = lGrid.getUserData(lRowId, "align");
        var type = lGrid.getUserData(lRowId, "type");
        var columnName = lGrid.getUserData(lRowId, "columnName");
        var columnId = lGrid.getUserData(lRowId, "columnId");
        var url = lGrid.getUserData(lRowId, "url");
        if (isEmpty(type))
            type = "ro";
        if (isEmpty(url))
            url = "";
        var rRowId = rGrid.uid();
        rGrid.addRow(rRowId, [rRowId, showName, width, align, type, url]);
        rGrid.setUserData(rRowId, "columnId", columnId); // columnId
        rGrid.setUserData(rRowId, "columnName", columnName); // columnName
        rGrid.setUserData(rRowId, "columnType", "0"); // columnType
    }
    // 向左移
    function toLeft(rRowId) {
        var name = rGrid.cells(rRowId, 1).getValue();
        var columnType = rGrid.getUserData(rRowId, "columnType");
        if ("3" == columnType) {
            dhtmlx.message("超链接预留区不能删除！");
            return;
        }
        var columnId = rGrid.getUserData(rRowId, "columnId");
        if ("-1" == columnId) {
            if (app.isCustom) {
                dhtmlx.message("自定义列【" + name + "】不能删除！");
            } else {
                rGrid.deleteRow(rRowId);
            }
            return;
        }
        var width = rGrid.cells(rRowId, 2).getValue();
        var align = rGrid.cells(rRowId, 3).getValue();
        var type = rGrid.cells(rRowId, 4).getValue();
        var url = rGrid.cells(rRowId, 5).getValue();
        var columnName = rGrid.getUserData(rRowId, "columnName");
        var columnType = rGrid.getUserData(rRowId, "columnType");
        var lRowId = rGrid.uid();
        lGrid.addRow(lRowId, name);
        lGrid.setUserData(lRowId, "width", width); // width
        lGrid.setUserData(lRowId, "align", align); // align
        lGrid.setUserData(lRowId, "type", type); // type
        lGrid.setUserData(lRowId, "columnId", columnId); // columnId
        lGrid.setUserData(lRowId, "columnName", columnName); // columnName
        lGrid.setUserData(lRowId, "url", url); // url
        lGrid.setUserData(lRowId, "columnType", columnType); // columnType
        rGrid.deleteRow(rRowId);
    }

    grid.attachEvent("onResize", function(cInd, cWidth, obj) {
        obj["resizeColumnIndex"] = cInd;
        return true;
    });
    grid.attachEvent("onResizeEnd", function(obj) {
        var index = obj["resizeColumnIndex"];
        if (undefined != index) {
            var width = obj.getColWidth(index);
            var rowId = rGrid.getRowId(index);
            rGrid.cellById(rowId, 2).setValue(width);
        }
        app.modified = true;
    });
    /*var names = new Array();
    rGrid.forEachRow(function(rowId) {
    	names[rowId] = rGrid.cells(rowId, 1).getValue();
    });*/
    grid.attachEvent("onAfterCMove", function(cInd, posInd) {
        if (cInd == posInd)
            return;
        var sRowId = rGrid.getRowId(cInd);
        var tRowId = rGrid.getRowId(posInd);
        rGrid.moveRowTo(sRowId, tRowId, "move");
        if (cInd > posInd) {
            rGrid.moveRowUp(sRowId);
        }
        app.modified = true;
    });

    /**
    * 字段检索
    * @returns {dhtmlXForm}
    */
    function initSearchArea() {
        var sformJson = [{
            type : "input",
            name : "searchcolumn",
            className : "dhx_toolbar_form",
            value : "字段检索(支持拼音)",
            width : 195,
            inputHeight : 17
        }];
        var form = new dhtmlXForm("top$columndiv", sformJson);
        var scInp = form.getInput("searchcolumn");
        scInp.onfocus = function() {
            form.setItemValue("searchcolumn", "");
        };
        scInp.onblur = function() {
            form.setItemValue("searchcolumn", "字段检索(支持拼音)");
        };
        scInp.onkeydown = function(e) {
            e = e || window.event;
            var keyCode = e.keyCode || e.which;
            if (13 == keyCode) {
                var value = form.getItemValue("searchcolumn");
                searchInGrid(lGrid, value, 0);
            }
        };
        return form;
    }
    /**
     * 字段检索
     * @returns {dhtmlXForm}
     */
    function initGridPanel() {
    	var canCreateIndex = loadJson(AppActionURI.physicalTableDefine + "!canCreateIndex.json?id=" + app.tableId);
    	 var formJson = [
             {type: "block", width:400, offsetLeft: 80, list:[
 				{type: "itemlabel", label: "双击事件:", labelWidth:120, labelAlign:"right"},
 				{type: "newcolumn"},
 				{type: "combo", name: "dblclick", className: "dhx_toolbar_form", label: "　　",style:"font-size:11px;", width: 160, 
	 	        	options:[{value : "0", text : "请选择列表双击事件"}, 
	 	        	    	{value : "1", text : "双击事件 - 弹出修改界面"},
	 	        	    	{value : "2", text : "双击事件 - 弹出浏览界面"}]}
              ]},
             {type: "block", width:400, offsetLeft: 80, list:[
				{type: "itemlabel", label: "显示序列号:", labelWidth:120, labelAlign:"right"},
				{type: "newcolumn"},
                {type: "checkbox", name: "hasRowNumber", offsetLeft: 20, className: "dhx_toolbar_form", position: "label-right", labelAlign:"left" }
             ]},
             {type: "block", width:400, offsetLeft: 80, list:[
	            {type: "itemlabel", label: "启用表头拖动设置:", labelWidth:120, labelAlign:"right"},
	            {type: "newcolumn"},
	            {type: "checkbox", name: "headerSetting", offsetLeft: 20, className: "dhx_toolbar_form", position: "label-right", labelAlign:"left" }
             ]}, 
             {type: "block", width:400, offsetLeft: 80, list:[
	            {type: "itemlabel", label: "选择模式:", labelWidth:120, labelAlign:"right"},
	            {type: "newcolumn"},
				{type: "radio", name: "selectModel", value: 2, checked: true, offsetLeft: 20, className: "dhx_toolbar_form", label: "多选", position: "label-right", labelAlign:"left", labelWidth: 60},
				{type: "newcolumn"},
				{type: "radio", name: "selectModel", value: 1, className: "dhx_toolbar_form", label: "单选", position: "label-right", labelAlign:"left", labelWidth: 60},
				{type: "newcolumn"},
				{type: "radio", name: "selectModel", value: 0, className: "dhx_toolbar_form", label: "无", position: "label-right", labelAlign:"left", labelWidth: 60}
				]},
             {type: "block", width:400, offsetLeft: 80, list:[	
				{type: "itemlabel", label: "编辑模式:", labelWidth:120, labelAlign:"right"},  
				{type: "newcolumn"},
				{type: "radio", name: "editModel", value: 0, checked: true, offsetLeft: 20, className: "dhx_toolbar_form", label: "直接编辑", position: "label-right", labelAlign:"left", labelWidth: 60},
				{type: "newcolumn"},
				{type: "radio", name: "editModel", value: 1, className: "dhx_toolbar_form", label: "单击编辑", position: "label-right", labelAlign:"left", labelWidth: 60},
				{type: "newcolumn"},
				{type: "radio", name: "editModel", value: 2, className: "dhx_toolbar_form", label: "双击编辑", position: "label-right", labelAlign:"left", labelWidth: 60}
			 ]}, 
             {type: "block", width:400, offsetLeft: 80, list:[
                {type: "itemlabel", label: "启用翻页条:", labelWidth:120, labelAlign:"right"},
                {type: "newcolumn"},
                {type: "checkbox", name: "pageable", offsetLeft: 20, className: "dhx_toolbar_form", position: "label-right", labelAlign:"left"}
             ]},
             {type: "block", width:400, offsetLeft: 80, list:[
                {type: "itemlabel", label: "启用自适应:", labelWidth:120, labelAlign:"right"},
                {type: "newcolumn"},
                {type: "checkbox", name: "adaptive", offsetLeft: 20, className: "dhx_toolbar_form", position: "label-right", labelAlign:"left"}
             ]},
             {type: "block", width:400, offsetLeft: 80, list:[	
				{type: "itemlabel", label: "操作列位置:", labelWidth:120, labelAlign:"right"},  
				{type: "newcolumn"},
				{type: "radio", name: "opeColPosition", value: 1, checked: true, offsetLeft: 20, className: "dhx_toolbar_form", label: "最后一列", position: "label-right", labelAlign:"left", labelWidth: 60},
				{type: "newcolumn"},
				{type: "radio", name: "opeColPosition", value: 0, className: "dhx_toolbar_form", label: "第一列", position: "label-right", labelAlign:"left", labelWidth: 60}
			 ]},  
			 {type: "block", width:400, offsetLeft: 80, list:[	
  				{type: "itemlabel", label: "操作列名称:", labelWidth:120, labelAlign:"right"},  
  				{type: "newcolumn"},
  				{type: "input", name: "opeColName", value: "操作", offsetLeft: 20, className: "dhx_toolbar_form", position: "label-right", labelAlign:"left"}
  			 ]},
			 {type: "block", width:400, offsetLeft: 80, list:[	
				{type: "itemlabel", label: "操作列宽度:", labelWidth:120, labelAlign:"right"},  
				{type: "newcolumn"},
				{type: "input", name: "opeColWidth", value: 120, offsetLeft: 20, className: "dhx_toolbar_form", position: "label-right", labelAlign:"left"}
			 ]},
             {type: "block", width:400, offsetLeft: 80, list:[
              	{type: "itemlabel", label: "检索方式:", labelWidth:120, labelAlign:"right"},  
              	{type: "newcolumn"},
	 	   	 	{type: "radio", name: "searchType", value: 0, checked: true, offsetLeft: 20, className: "dhx_toolbar_form", label: "数据库检索", position: "label-right", labelAlign:"left", labelWidth: 60},
	 	   	 	{type: "newcolumn"},
	 	   	 	{type: "radio", name: "searchType", value: 1, disabled: !canCreateIndex.success, className: "dhx_toolbar_form", label: "索引库检索", position: "label-right", labelAlign:"left", labelWidth: 60}
             ]}   
	 	   	 	
	        ];
	      	return formJson;
     }
    /******************************(列表中嵌套表单)******************************/
    rGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform, rowId) {
        var type = rGrid.cells(rowId, 4).getValue();
        subform.c = getColumnFormJson();
    });
    rGrid.attachEvent("onSubFormLoaded", function(subform, rowId, index) {
        subform.attachEvent("onBeforeChange", function (id, old_value, new_value) {
        	if ("columnType" == id) {
        		var rId = subform.getItemValue("id");
        		var columnId = rGrid.getUserData(rId, "columnId");
        		if ("3" == new_value) {
        		    dhtmlx.alert("不能改成超链接预留区！");
        			return false;
        		}
        		if ("-1" != columnId && ("1" == new_value || "2" == new_value)) {
        			dhtmlx.alert("该列为表字段，不可做为自定义SQL语句或超链接！");
        			return false;
        		}
        		if ("-1" == columnId && "0" == new_value) {
        			dhtmlx.alert("该列无法转换成表字段！");
        			return false;
        		}
        		if (new_value == "1") {
        			subform.enableItem("columnName");
        			subform.setItemLabel("columnName", "SQL语句");
        		} else if (new_value == "2") {
        			subform.enableItem("columnName");
        			subform.setItemLabel("columnName", "超链接名称");
        		}
        	}
        	return true;
        });
        subform.attachEvent("onChange", function(id, value) {
            var rId = subform.getItemValue("id");
            app.modified = true;
            if ("showName" == id) {
                rGrid.cells(rId, 1).setValue(value);
            } else if ("columnName" == id) {
                rGrid.setUserData(rId, "columnName", value);
            } else if ("url" == id) {
                rGrid.cells(rId, 5).setValue(value);
            } else if ("columnType" == id) {
                rGrid.setUserData(rId, "columnType", value);
                if (value == "1") {
                	rGrid.cells(rId, 4).setValue("ro");
                	rGrid.cells(rId, 5).setValue("");
                } else if (value == "2") {
                	rGrid.cells(rId, 4).setValue("link");
                }
            }
        });
        var type = rGrid.cells(rowId, 4).getValue();
        var columnType = rGrid.getUserData(rowId, "columnType");
        if (isEmpty(columnType))
            columnType = "0";
        setFormData(subform, rowId);
        if (columnType == "0") {
        	subform.disableItem("columnType");
            subform.disableItem("columnName");
			subform.setItemLabel("columnName", "字段名称：");
			subform.enableItem("url");
        } else if (columnType == "1") {
        	subform.enableItem("columnName");
			subform.setItemLabel("columnName", "SQL语句：");
			subform.enableItem("url");
        } else if (columnType == "2") {
        	subform.enableItem("columnName");
			subform.setItemLabel("columnName", "超链接名称：");
			subform.enableItem("url");
        } else if (columnType == "3") {
        	subform.disableItem("columnType");
            subform.disableItem("columnName");
            subform.setItemValue("columnName", "");
            subform.disableItem("url");
            subform.hideItem("linkEvent");
        }
    });
    rGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            rGrid.selectRowById(id);
            rGrid.forEachRow(function(rId) {
                if (id != rId) {
                    rGrid.cells(rId, 0).close();
                }
            });
        }
    });
    function setFormData(form, rowId) {
        var data = {
            id : rowId,
            showName : rGrid.cells(rowId, 1).getValue(),
            columnName : rGrid.getUserData(rowId, "columnName"),
            columnType : rGrid.getUserData(rowId, "columnType"),
            url : rGrid.cells(rowId, 5).getValue()
        };
        if (isNotEmpty(data.url))
            data.url = data.url.replace("&amp;", "&");
        form.setFormData(data);
    }
	function getColumnFormJson() {
		var formJson = [{
			    type: "settings",
			    position: "label-left",
			    labelWidth: 80,
			    inputWidth: 200,
			    labelAlign: "right",
			    offsetTop: 5
			}, 
			{type: "hidden", name: "id"}, 
			{type: "block", width: 580, list:[
				{type: "input", label: "表头名称：", name: "showName"},   
			    {type:"newcolumn"},
				{type: "combo", label: "字段类型：", name: "columnType", options: [
                   {value:"0", text:"表字段"},
                   {value:"1", text:"自定义SQL语句"},
                   {value:"2", text:"超链接"},
                   {value:"3", text:"超链接预留区"}
                ]}
		    ]},
			{type: "block", width: 580, list:[
	  			{type: "input", label: "字段名称：", name: "columnName", width: 480},   
	  	    ]},
			{type: "block", name: "linkEvent", width: 580, list:[
			    {type: "itemlabel", label: "链接事件：",  labelWidth: 80},
			    {type: "newcolumn"},
			    {type: "radio", labelAlign: "left", position : "label-right", label: "无", name: "url", value:"", checked: true, width: 60},
			    {type: "newcolumn"},
			    {type: "radio", labelAlign: "left", position : "label-right", label: "查看", name: "url", value:"linkViewDForm", width: 60},
			    {type: "newcolumn"},
			    {type: "radio", labelAlign: "left", position : "label-right", label: "修改", name: "url", value:"linkUpdate", width: 60}
		    ]}/*,
		    {type: "itemlabel", label: "<font color='red'>注：多个链接请用英文逗号分隔！如：字段：查看,删除；链接地址：url1,url2",  labelWidth: 480},
		    {type: "itemlabel", label: "<font color='red'>　　如果是自定义SQL语句且需要关联当前表(当前表别名为t_)，请使用别名t_.XXX",  labelWidth: 480}*/];
		return formJson;
	}
	//高级配置win
	function createSettingWin() {
		if (!window.settingWin) {
    		settingWin = new dhtmlXWindows();
        }
        settingWin.setImagePath(IMAGE_PATH);
        var w =  500;
        var h =  350;
        if (h > document.body.clientHeight) {
            h = document.body.clientHeight;
        }
        innerwin = settingWin.createWindow("highSetting", 0, 0, w, h);
        innerwin.setModal(true);
        innerwin.button("park").hide();
        innerwin.button("minmax1").hide();
        innerwin.button("minmax2").hide();
        innerwin.setText("高级配置");
        innerwin.center();
        
        settingLayout = innerwin.attachLayout("1C");
        settingLayout.cells("a").hideHeader();
        var form = settingLayout.cells("a").attachForm(initGridPanel());
      	if ("combogrid" === app.usage) {
      		form.hideItem("dblclick");
      		form.hideItem("searchType", 0);
      		form.hideItem("searchType", 1);
      	}
      	var sUrl = AppActionURI.appGrid + "!show.json?tableId=" + tableId + "&componentVersionId=" + componentVersionId + "&menuId=" + app.menuId;
      	var entity = loadJson(sUrl);
      	form.setFormData(entity);
		
		var sbar = settingLayout.attachStatusBar();
	    var btbar = new dhtmlXToolbarObject(sbar.id);
	    btbar.setIconPath(TOOLBAR_IMAGE_PATH)
	    btbar.addButton("settingSave", 4, "保存", "save.gif");
	    btbar.addSeparator("setting$septr$02", 5);
	    btbar.addButton("settingClose", 6, "关闭", "close.gif");
	    btbar.setAlign("right");
	    btbar.attachEvent("onClick", function(id) {
	    	if(id == "settingSave") {
	    		/*var fd = form.getFormData();
	    		var url = AppActionURI.appGrid + "!saveHighSetting.json?tableId=" + tableId + "&componentVersionId=" + componentVersionId + "&menuId=" + app.menuId
                + "&dblclick=" + fd.dblclick + "&hasRowNumber=" + fd.hasRowNumber  + "&searchType=" + fd.searchType + "&pageable=" + fd.pageable 
                + "&adaptive=" +fd.adaptive + "&opeColPosition=" + fd.opeColPosition + "&opeColName=" + fd.opeColName + "&opeColWidth=" + fd.opeColWidth;
	    		dhtmlxAjax.get(addTimestamp(url), function(loader) {
	                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
	                if (jsonObj.success) {
	                    dhtmlx.message(getMessage("operate_success"));
	                } else {
	                    dhtmlx.message(getMessage("operate_failure"));
	                }
	            });*/
	    		form.send(AppActionURI.appGrid + "!saveHighSetting.json", "post", function(loader, response) {
	    			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
	                if (jsonObj.success) {
	                    dhtmlx.message(getMessage("operate_success"));
	                } else {
	                    dhtmlx.message(getMessage("operate_failure"));
	                }
	    		});
	    	}else if(id == "settingClose") {
	    		innerwin.close();
	    	}
	    });
	}
	
}
