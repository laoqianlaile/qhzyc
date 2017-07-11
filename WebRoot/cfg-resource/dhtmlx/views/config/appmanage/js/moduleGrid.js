/**
 * 列表初始化
 */
function initModuleGrid(layout, that) {
    if (that.createModuleGrid) {
        that.reloadModuleGrid();
        return;
    }
	// 隐藏标题
    layout.hideHeader();
    // 初始变量
    var ttbar = layout.attachToolbar();
    var stbar = layout.attachStatusBar();
    var grid = layout.attachGrid();
    var gcfg = {
            format: {
                headers: ["<center>构件类名</center>","<center>构件名称</center>","<center>构件类型</center>","<center>构件说明<center/>",""],
                cols:["componentClassName","name","type", "remark"],
                userDatas: ["componentVersionId"],
                colWidths: ["200","200","200","400","*"],
                colTypes: ["ro","ro","co","ro","ro"],
                colAligns: ["left","left","left","left","left"]
            }
        };
    var gridUrl = function(param) {
        var url = that.MODEL_URL + "!search.json?Q_EQ_componentAreaId=" + that.nodeId + "&P_orders=showOrder";
        if (param) {
		    url += "&" + param;
		} else {
		    ST_form.setItemValue("value", "");
		}
        return url;
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
	                    param = "Q_LIKE_componentClassName=" + value;
	            	} else {
	            	    param = "Q_LIKE_name=" + value;
	            	}
	            }
	            that.reloadModuleGrid(param);
	        }
	    };
	    return form;
	}
    ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    ttbar.addButton("add", 1, "新增", "new.gif");
    ttbar.addSeparator("top$septr$01", 2);
    ttbar.addButton("update", 3, "修改", "update.gif");
    ttbar.addSeparator("top$septr$01", 4);
    ttbar.addButton("del", 5, "删除", "delete.gif");
    
    ttbar.addButton("sous", 7, "", "search.gif", null, "right");
    ttbar.addDiv("top$searchTextdiv", 6, "right");
    ST_form = initSearchColumn();
    ttbar.attachEvent("onClick", function(itemId) {
        if ("add" == itemId) {
            initModuleForm(that);
        } else if ("update" == itemId) {
            var rowId = grid.getSelectedRowId();
            if (isEmpty(rowId)) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            if ((rowId.indexOf(",") > 0)) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            initModuleForm(that, rowId);
        } else if ("del" == itemId) {
            var rowId = grid.getSelectedRowId();
            if (isEmpty(rowId)) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            if ((rowId.indexOf(",") > 0)) {
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
                        var cvId = grid.getUserData(rowId, "componentVersionId");
                        if (isEmpty(cvId)) {
                            deleteModuleById(rowId);
                        } else {
                            var cUrl = contextPath + "/component/component-version!deleteValid.json?componentVersionId=" + cvId;
                            dhtmlxAjax.get(addTimestamp(cUrl), function(loader) {
                                var str = eval("(" + loader.xmlDoc.responseText + ")");
                                var obj = eval("(" + str + ")");
                                if (obj.success) {
                                    deleteModuleById(rowId);
                                } else {
                                    dhtmlx.message(obj.message);
                                }
                            });
                        }
                    }
                }
            });
        } else if (itemId == "sous") {
        	var value = ST_form.getItemValue("value"),
			    param = "";
			value = encodeURIComponent(value);
			if (value !== "") {
            	if (value.match(/^\w+$/)) {
                    param = "Q_LIKE_componentClassName=" + value;
            	} else {
            	    param = "Q_LIKE_name=" + value;
            	}
            }
            that.reloadModuleGrid(param);
        }
    });
    /**删除*/
    function deleteModuleById(id) {
        var deleteUrl = that.MODEL_URL + "/" + id + ".json?_method=delete";
        dhtmlxAjax.get(deleteUrl, function(loader) {
            var obj = eval("(" + loader.xmlDoc.responseText + ")");
            if (obj.status == false) {
                var id = obj.message;
                var name = grid.cells(id, 1).getValue();
                dhtmlx.alert("【" + name + "】有子节点，请先删除子节点！");
                return;
            } else {
                dhtmlx.message(getMessage("delete_success"));
                that.reloadModuleGrid();
                that.reloadTreeItem();
            }
        });
    }
    grid.enableDragAndDrop(true);
    var typeCombo = grid.getCombo(2);
	typeCombo.put("3", "树构件");
	typeCombo.put("4", "物理表构件");
	typeCombo.put("5", "逻辑表构件");
	typeCombo.put("6", "通用表构件");
	typeCombo.put("7", "标签页构件");
    initGrid(grid, gcfg, stbar);
    search(grid, gcfg, gridUrl());
    grid.attachEvent("onDrag", function(sIds, tId) {
        // 判断选中的记录是否连续，如果不连续，则不能调整顺序
        if (sIds.indexOf(",") != -1) {
            var idArr = sIds.split(",");
            var preIdx = null, curIdx = null;
            for (var i = 0; i < idArr.length; i++) {
                curIdx = grid.getRowIndex(idArr[i]);
                if (null == preIdx) {
                    preIdx = curIdx;
                    continue;
                }
                if (Math.abs(curIdx - preIdx) != 1) {
                    dhtmlx.message("请选择连续的记录进行调整顺序！");
                    return false;
                }
                preIdx = curIdx;
            }
        }
        return true;
    });
    grid.attachEvent("onDrop", function(sId, tId, dId, sObj, tObj, sCol, tCol) {
        if (undefined != tId) {
            var adjustUrl = that.MODEL_URL + "!adjustShowOrder?P_componentAreaId=" + that.nodeId + "&P_sourceIds="
                    + sId + "&P_targetId=" + tId;
            dhtmlxAjax.get(adjustUrl, function(loader) {
                that.reloadTreeItem();
                that.reloadModuleGrid();
                grid.selectRowById(sId);
            });
        }
    });
    // 列表数据刷新
    that.reloadModuleGrid = function(param) {
        search(grid, gcfg, gridUrl(param));
    };
    that.createModuleGrid = true;
}
