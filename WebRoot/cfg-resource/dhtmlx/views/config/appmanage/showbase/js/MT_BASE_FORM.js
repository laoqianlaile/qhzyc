/**
 * MT => module template 模块模板 FORM => form 表单
 * @param that 页面全局变量
 * @param fLayout 表单布局区域
 * @param tableId 表单对应表ID
 * @param areaIndex 区域索引位置
 * @param showInWindow 是否是弹出框的方式打开的
 */
function MT_BASE_FORM_init(that, fLayout, tableId, areaIndex, fIndex, dataId, master/*关联主列表{tableId:'', dataId:''}*/,
        showInWindow) {
    var _this = this;
    if (isEmpty(dataId) && 1 == areaIndex && ("masterId" in that) && isNotEmpty(that.masterId)) {
        var pTId = MT_P_FormTableId(areaIndex); // 列表对应的表ID
        that[pTId] = tableId; // 用于从列表关联查询
        dataId = masterId; // 链接地址传过来的ID,直接可打开表单
    }
    var fcfg = CFG_fcfg(tableId, that.moduleId);
    if (null == fcfg || null == fcfg.formJson) {
        dhtmlx.message("界面未配置！");
        return;
    }
    // 属性名
    var pGId = MT_P_GridId(tableId);
    var pFId = MT_P_FormId(tableId);
    // 表单作为弹出页面时候用
    if (that[pGId] == undefined && that.parent[pGId]) {
        that[pGId] = that.parent[pGId];
    }
    // 配置首条、上一条、下一条、末条时使用
	var rowIndex, maxRowIndex;
	if (dataId) {
		rowIndex = that[pGId].grid.getRowIndex(dataId);
		if (that[pGId].grid.currentPage == that[pGId].grid.totalPages) {
            maxRowIndex = that[pGId].grid.rowsBuffer.length % that[pGId].grid.pageSize - 1;
        } else {
            maxRowIndex = that[pGId].grid.pageSize - 1;
        }
	}
    // 工具条初始化
    var ftbar = MT_TBAR_init(that, fLayout, tableId, MT_common.L_FORM, fClickEvent);
    // 处理工具条上面的按钮
	if (dataId) {
		if (that[pGId].grid) {
			if (rowIndex == 0) {
				if (ftbar.getPosition(MT_common.P_FIRST_RECORD) != null) {
					ftbar.disableItem(MT_common.P_FIRST_RECORD);
				}
				if (ftbar.getPosition(MT_common.P_PREVIOUS_RECORD) != null) {
					ftbar.disableItem(MT_common.P_PREVIOUS_RECORD);
				}
			}
			if (rowIndex == maxRowIndex) {
				if (ftbar.getPosition(MT_common.P_NEXT_RECORD) != null) {
					ftbar.disableItem(MT_common.P_NEXT_RECORD);
				}
				if (ftbar.getPosition(MT_common.P_LAST_RECORD) != null) {
					ftbar.disableItem(MT_common.P_LAST_RECORD);
				}
			}
		}
	} else {
		if (ftbar.getPosition(MT_common.P_FIRST_RECORD) != null) {
			ftbar.hideItem(MT_common.P_FIRST_RECORD);
		}
		if (ftbar.getPosition(MT_common.P_PREVIOUS_RECORD) != null) {
			ftbar.hideItem(MT_common.P_PREVIOUS_RECORD);
		}
		if (ftbar.getPosition(MT_common.P_NEXT_RECORD) != null) {
			ftbar.hideItem(MT_common.P_NEXT_RECORD);
		}
		if (ftbar.getPosition(MT_common.P_LAST_RECORD) != null) {
			ftbar.hideItem(MT_common.P_LAST_RECORD);
		}
	}
    // 初始化表单
    var form = fLayout.attachForm(fcfg.formJson);
    // 表单初始化
    if (null != fcfg.initEvent) {
        fcfg.initEvent(form);
    }
    // 二次开发时，表单信息初始化
    MT_afterFormInit(form, tableId, that, areaIndex);
    // onchange 事件
    if (null != fcfg.changeEvent) {
        form.attachEvent("onChange", function(id, value) {
            fcfg.changeEvent(form, id, value);
        });
    }
    // 数据加载
    if (isNotEmpty(dataId)) {
        MT_FORM_load(dataId);
    } else {
        // 默认值初始化
        MT_FORM_default();
        // 初始化关联数据栏位默认值
        MT_FORM_relation();
    }
    /** **********************工作流表单控制*************************** */
    if (that.type && "9" == that.type && MT_common.BOX_APPLYFOR != that.coflow.box) {
        if (undefined == dataId) {
            dataId = that[pGId].MT_coflowId(null, MT_common.ROW_ID);
        }
        var ws = that[pGId].MT_coflowId(dataId, MT_common.W_STATUS);
        if (MT_common.BOX_TODO == that.coflow.box) {
            if (11 == ws) {
                form.lock();
                ftbar.hideItem(MT_common.P_COMPLETE);
                // ftbar.hideItem(MT_common.P_REASSIGN);
                // ftbar.hideItem(MT_common.P_DELIVER);
            } else {
                ftbar.hideItem(MT_common.P_CHECKOUT);
                MT_FORM_disable();
            }
        } else {
            form.lock();
        }
        MT_FORM_opinions();
    }
    /** **********************表单公用事件*************************** */
    that[pFId] = {};
    that[pFId].disable_toolbar = false;
    /* 表单数据加载 */
    that[pFId].MT_FORM_load = function(id) {
        MT_FORM_load(id);
    };
    /* 表单初始化 */
    that[pFId].MT_FORM_create = function() {
        MT_FORM_create();
        if (that[pFId].disable_toolbar == true) {
            that[pFId].MT_FORM_enableToolbarAndForm();
            that[pFId].disable_toolbar = false;
        }
    };
    /** **********************以下为事件定义区*************************** */
    /** 启用表单和其工具条. */
    that[pFId].MT_FORM_enableToolbarAndForm = function() {
        if (ftbar) {
            ftbar.forEachItem(function(itemId) {
                ftbar.enableItem(itemId);
            });
        }
        form.unlock();
    }
    /** 禁用表单和其工具条. */
    that[pFId].MT_FORM_disableToolbarAndForm = function() {
        if (ftbar) {
            ftbar.forEachItem(function(itemId) {
                ftbar.disableItem(itemId);
            });
        }
        form.clear();
        form.lock();
    }
    if (!showInWindow) {
        // 如果存在相关列表的话，初始化时禁用工具条
        if (that[pGId] != undefined && that[pGId] != null) {
            that[pFId].MT_FORM_disableToolbarAndForm();
            that[pFId].disable_toolbar = true;
        }
    }
    /** 表单数据加载. */
    function MT_FORM_load(id) {
        var url = MT_getAction() + "!show.json?P_tableId=" + tableId + "&P_moduleId=" + that.moduleId + "&id=" + id;
        var json = loadJson(url);
        if (null != json && "" != json) {
            form.setFormData(json);
        }
        if ("isView" in that && true === that.isView) {
            form.lock();
            fLayout.hideToolbar();
        }
    }
    /** 树节点上的条件给表单默认值. */
    function MT_FORM_default() {
        if (undefined != that.MT_tree_column_filter && null != that.MT_tree_column_filter
                && "" != that.MT_tree_column_filter) {
            var filterItemArr = that.MT_tree_column_filter.split(",");
            for (var i = 0; i < filterItemArr.length; i++) {
                var cvArr = filterItemArr[i].split(CFG_cv_split);
                var column = cvArr[0].split("_C_")[1];
                var value = cvArr[1];
                form.setItemValue(column.toUpperCase(), value);
            }
        }
    }
    /** 工作流表单配置. */
    function MT_FORM_disable() {
        if (undefined == dataId) {
            dataId = that[pGId].MT_coflowId(null, MT_common.ROW_ID);
        }
        var activityId = that[pGId].MT_coflowId(dataId, MT_common.ACTIVITY_ID);
        var url = MT_app_uri + "/workflow-form-setting!disableColumns.json?P_tableId=" + tableId + "&P_moduleId="
                + that.moduleId + "&P_activityId=" + activityId;
        var cols = loadJson(url);
        if (null != cols && cols.length > 0) {
            for (var i = 0; i < cols.length; i++) {
                form.disableItem(cols[i]);
            }
        }
    }
    /** 工作流审批意见. */
    function MT_FORM_opinions() {
        var dataId = that[pGId].MT_coflowId(null, MT_common.ROW_ID);
        var activityId = that[pGId].MT_coflowId(dataId, MT_common.ACTIVITY_ID);
        var url = MT_app_uri + "/coflow-opinion!opinions.json?P_dataId=" + dataId;
        var opinions = loadJson(url);
        if (null == opinions || "" == opinions)
            return;
        var blockWidth = form.getItemWidth("block_hidden");
        var itemData = {
            type : "block",
            name : "block_opinion",
            list : [{
                type : "itemlabel",
                label : "审批意见："
            }, {
                type : "itemdiv",
                name : "coflowOpinion",
                labelAlign : "left",
                value : opinions,
                width : (blockWidth - 200)
            }]
        };
        form.addItem(null, itemData);
    }
    /** 给表关系配置了关联字段设置默认值. */
    function MT_FORM_relation() {
        if (undefined == master || null == master) {
            return;
        }
        var url = MT_getAction() + "!relationData.json?P_tableId=" + tableId + "&P_M_tableId=" + master.tableId
                + "&P_M_dataId=" + master.dataId;
        var rlt = loadJson(url);
        if (rlt) {
            for (var p in rlt) {
                form.setItemValue(p, rlt[p]);
            }
        }
    }
    /** ********************* 工具条事件定义 ************************* */
    function fClickEvent(id) {
        if (MT_common.P_CREATE == id) {
            MT_FORM_create();
        } else if (MT_common.P_SAVE == id) {
            MT_FORM_save("save");
        } else if (MT_common.P_SAVE_AND_CREATE == id) {
            MT_FORM_save("create");
        } else if (MT_common.P_SAVE_AND_CLOSE == id) {
            MT_FORM_save("close");
        } else if (MT_common.P_RESET == id) {
            MT_FORM_reset();
        } else if (MT_common.P_FIRST_RECORD == id) {
			rowIndex = 0;
			PV_FORM_LoadByIndex(rowIndex);
		} else if (MT_common.P_PREVIOUS_RECORD == id) {
			PV_FORM_LoadByIndex(--rowIndex);
		} else if (MT_common.P_NEXT_RECORD == id) {
			PV_FORM_LoadByIndex(++rowIndex);
		} else if (MT_common.P_LAST_RECORD == id) {
			rowIndex = maxRowIndex;
			PV_FORM_LoadByIndex(rowIndex);
		}  else if (MT_common.P_START == id) {
            MT_FORM_start();
        } else if (MT_common.P_CHECKOUT == id) {
            MT_FORM_checkout();
        } else if (MT_common.P_COMPLETE == id) {
            MT_FORM_complete();
        } else if (MT_common.P_UNTREAD == id) {
            MT_FORM_untread();
        } else if (MT_common.P_RECALL == id) {
            MT_FORM_recall();
        } else if (MT_common.P_REASSIGN == id) {
            MT_FORM_reassign();
        } else if (MT_common.P_DELIVER == id) {
            MT_FORM_deliver();
        } else if (MT_common.P_HASREAD == id) {
            MT_FORM_hasread();
        } else if (MT_common.P_TRACK == id) {
            MT_FORM_track();
        } else if (id.match("CD_BUTTON_")) {
            CFG_clickToolbar(ftbar, MT_ZoneName(tableId, MT_common.L_FORM, areaIndex), id);
        } else if (id == "CFG_closeComponentZone") {
            /** 构件组装方式为内嵌的代码，内嵌构件上添加返回按钮点击事件设置 start */
            if (window.CFG_clickReturnButton) {
                CFG_clickReturnButton(id);
            }
            /** 构件组装方式为内嵌的代码，内嵌构件上添加返回按钮点击事件设置 end */
        } else {
            MT_addFormToolbarClickEvent(id, form, tableId, that, areaIndex);
        }
    }
    /** 初始化表单，只是简单的对输入框等控件进行清空. */
    function MT_FORM_create() {
    	var defaultValues = fcfg.defaultValues;
    	var defaultValueArray = defaultValues.split(",");
    	function ValidateInteger(value) {
			return value.match(/^(0|[1-9][0-9]*)$/);
		}
        form.forEachItem(function(name) {
            var increaseColumns = fcfg.increaseColumns;
            var keptColumns = fcfg.keptColumns;
            // 递增
            if ("" != increaseColumns && increaseColumns.indexOf("," + name + ",") > -1) {
            	var v = form.getItemValue(name);
            	if (ValidateInteger(v)) {
            	    form.setItemValue(name, parseInt(v) + 1);
            	}
                return;
            }
            // 连续录入字段不清空
            if ("" != keptColumns && keptColumns.indexOf("," + name + ",") > -1) {
                return;
            }
            // 字段默认值
            if ("" != defaultValues && defaultValues.indexOf("," + name + ",") > -1) {
            	for (var i=0; i<defaultValueArray.length; i=i+2) {
            		if (name == defaultValueArray[i]) {
            			form.setItemValue(name, defaultValueArray[i+1]);
            			break;
            		}
            	}
            	return;
            }
            var type = form.getItemType(name);
            if ((type == "input") || (type == "combo") || (type == "calendar") || "ID" == name) {
                form.setItemValue(name, "");
            }
        });
        // 默认值初始化
        MT_FORM_default();
        if (that[pGId].index > 1) {
            var pRTId = MT_P_GridTableId(that[pGId].index - 1); // 关联主列表表ID寄存属性
            if (undefined != that[pRTId]) {
                master = {};
                var rTableId = that[pRTId]; // 关联主列表表id值
                var pRGId = MT_P_GridId(rTableId); // 关联主列表信息寄存属性
                master.tableId = rTableId;
                master.dataId = that[pRGId].curRowId; // 关联主列表选中行的rowId值
            }
        }
        MT_FORM_relation();
    }
    /** 重置表单 */
    function MT_FORM_reset() {
        var id = form.getItemValue("ID");
        if (id != "") {
            MT_FORM_load(id);
        } else {
            var defaultValues = fcfg.defaultValues;
	    	var defaultValueArray = defaultValues.split(",");
	        form.forEachItem(function(name) {
	            // 字段默认值
	            if ("" != defaultValues && defaultValues.indexOf("," + name + ",") > -1) {
	            	for (var i=0; i<defaultValueArray.length; i=i+2) {
	            		if (name == defaultValueArray[i]) {
	            			form.setItemValue(name, defaultValueArray[i+1]);
	            			break;
	            		}
	            	}
	            	return;
	            }
	            var type = form.getItemType(name);
	            if ((type == "input") || (type == "combo") || (type == "calendar") || "ID" == name) {
	                form.setItemValue(name, "");
	            }
	        });
	        // 默认值初始化
	        MT_FORM_default();
	        if (that[pGId].index > 1) {
	            var pRTId = MT_P_GridTableId(that[pGId].index - 1); // 关联主列表表ID寄存属性
	            if (undefined != that[pRTId]) {
	                master = {};
	                var rTableId = that[pRTId]; // 关联主列表表id值
	                var pRGId = MT_P_GridId(rTableId); // 关联主列表信息寄存属性
	                master.tableId = rTableId;
	                master.dataId = that[pRGId].curRowId; // 关联主列表选中行的rowId值
	            }
	        }
	        MT_FORM_relation();
        }
    }
    /** 表单保存. */
    function MT_FORM_save(op) {
        var url = MT_getAction() + "!save.json?P_tableId=" + tableId + "&P_op=" + op;
        var checkResult = null;
        // 保存前校验
        if (fcfg.beforeSaveEvent && typeof(fcfg.beforeSaveEvent) == "function") {
            checkResult = fcfg.beforeSaveEvent(form);
            if (false == checkResult.success) {
                if (checkResult.message && "" != checkResult.message) {
                    dhtmlx.alert(checkResult.message);
                }
                return;
            }
        }
        // 二次开发：保存前校验
        var checkResult = MT_beforeFormSave(form, tableId, that, areaIndex);
        if (false == checkResult.success) {
            if (isNotEmpty(checkResult.message)) {
                dhtmlx.alert(checkResult.message);
            }
            return;
        }
        form.send(addTimestamp(url), "post", function(loader, response) {
            var entity = eval("(" + loader.xmlDoc.responseText + ")");
            if (entity.ID == null || entity.ID == "") {
                dhtmlx.alert(getMessage("save_failure"));
                return;
            }
            if ("save" == op) {
                form.setFormData(entity);
            } else {
                form.setItemValue("ID", entity.ID);
            }
            // refresh grid data
            if (that[pGId] && that[pGId].MT_GRID_reload && typeof that[pGId].MT_GRID_reload == "function") {
                that[pGId].MT_GRID_reload();
            }
            // 刷新父节点
            if (that.MT_TREE_refresh_parent && typeof(that.MT_TREE_refresh_parent) == "function") {
                that.MT_TREE_refresh_parent();
            }
            // 保存成功后回调函数
            if (fcfg.afterSaveEvent && typeof(fcfg.afterSaveEvent) == "function") {
                fcfg.afterSaveEvent(form, loader, response);
            }
            // 二次开发：保存后事件
            MT_afterFormSave(form, tableId, that, areaIndex);

            if ("create" == op) {
                MT_FORM_create();
            } else if ("close" == op && fLayout.close) {
                fLayout.close();
            }
            dhtmlx.message(getMessage("save_success"));// */
        });
    }
    /** 启动流程. */
    function MT_FORM_start() {
        var url = MT_getAction() + "!coflow.json?P_tableId=" + tableId + "&P_op=start";
        form.send(addTimestamp(url), "post", function(loader, response) {
            var entity = eval("(" + loader.xmlDoc.responseText + ")");
            if (typeof(entity.workitemId) == "number" && entity.workitemId > 0) {
            	form.setItemValue("ID", entity.ID);
                // 刷新工作箱数据
                if (that.MT_TREE_refresh_parent && typeof(that.MT_TREE_refresh_parent) == "function") {
                    that.MT_TREE_refresh_parent();
                }
                dhtmlx.message(getMessage("operate_success"));
                return;
            }
            dhtmlx.alert(getMessage("operate_failure"));
        });
        // MT_FORM_complete();
    }
    /** 工作项签收、流转等URL. */
    function MT_coflow_url(op) {
        var id = form.getItemValue("ID");
        var workitemId = that[pGId].MT_coflowId(id, MT_common.W_ID);
        return (MT_getAction() + "!coflow.json?P_op=" + op + "&P_workitemId=" + workitemId);
    }
    /** 签收. */
    function MT_FORM_checkout() {
        var url = MT_coflow_url(MT_common.P_CHECKOUT);
        dhtmlxAjax.get(addTimestamp(url), function(loader) {
            var entity = eval("(" + loader.xmlDoc.responseText + ")");
            if (typeof(entity.workitemId) == "number" && entity.workitemId > 0) {
                dhtmlx.message(getMessage("operate_success"));
                ftbar.hideItem(MT_common.P_CHECKOUT);
                ftbar.showItem(MT_common.P_COMPLETE);
                form.unlock();
                return;
            }
            dhtmlx.alert(getMessage("operate_failure"));
        });
    }
    /** 流转. */
    function MT_FORM_complete() {
        if (fLayout.close) {
            fLayout.close();
        }
        var _this = this;
        _this.ended = false;
        var workId = that[pGId].MT_coflowId(dataId, MT_common.W_ID);
        var wurl = (MT_getAction() + "!isUserControl.json?P_workitemId=" + workId);
        var isUserControl = loadJson(addTimestamp(wurl));
        if (true == isUserControl) {
            userControl();
        } else {
            defaultSubmit();
        }
    }
    /** 流转，不显示下一步页面 */
    function defaultSubmit() {
        var workitemId = that[pGId].MT_coflowId(dataId, MT_common.W_ID);
        var nurl = (MT_getAction() + "!nextActivityPerformer.json?P_workitemId=" + workitemId);
        var activities = loadJson(addTimestamp(nurl));
        var performerIds = "";
        var nextActivityId = "";
        for (var i = 1; i < activities.length; i++) {
            if ("" != performerIds) {
                performerIds += ",";
            }
            performerIds += activities[i].performerId;
            if (i == 1) {
                nextActivityId = activities[1].activityId;
            }
        }
        var url = MT_coflow_url(MT_common.P_COMPLETE);
        url += "&P_tableId=" + tableId + "&P_nextActivityId=" + nextActivityId + "&P_performerIds=" + performerIds
                + "&P_opinion=默认意见";
        form.send(encodeURI(addTimestamp(url)), "post", function(loader, response) {
            var wid = eval("(" + loader.xmlDoc.responseText + ")");
            if (typeof(wid) == "number" && wid > 0) {
                // 刷新工作箱数据
                if (that.MT_TREE_refresh_parent && typeof(that.MT_TREE_refresh_parent) == "function") {
                    that.MT_TREE_refresh_parent();
                }
                dhtmlx.message(getMessage("operate_success"));
                return;
            }
            dhtmlx.alert(getMessage("operate_failure"));
        });
    }
    /** 流转，显示下一步页面 */
    function userControl() {
        // if (fLayout.close) {
        // fLayout.close();
        // }
        // var _this = this;
        // 下一节点是否为结束节点
        // _this.ended = false;
        var win = MT_CreateDhxWindow({
            id : "COFLOW$" + tableId,
            title : "详细信息",
            width : 600,
            height : 400
        });
        // 顶部“关闭”按钮事件重写
        win.button("close").attachEvent("onClick", function() {
            close();
        });
        // 底部工具条“关闭”按钮
        var sbar = win.attachStatusBar();
        var tbar = new dhtmlXToolbarObject(sbar.id);
        tbar.setIconPath(TOOLBAR_IMAGE_PATH);
        tbar.addButton("submit", 1, "提交", "complete.gif");
        tbar.addSeparator("subwin$sep$01", 2);
        tbar.addButton("bottom$close", 4, "关闭", "close.gif");
        tbar.setAlign("right");
        tbar.attachEvent("onClick", function(id) {
            if ("bottom$close" == id) {
                close();
            } else if ("submit" == id) {
                var performerIds = "";
                var nextActivityId = "";
                var allPerformerIds = "";
                for (var k = 1; k < j; k++) {
                    allPerformerIds = "";
                    var nextActivityId = subform.getItemValue("nextActivityId_" + k);
                    var multisel = subform.getSelect("performer_" + k);
                    for (var i = 0; i < multisel.options.length; i++) {
                        performerIds += nextActivityId + "^_^" + multisel.options[i].value + ",";
                        allPerformerIds += multisel.options[i].value;
                    }
                    if (subform.isItemChecked("nextActivityName_" + k) && "" == allPerformerIds) {
                        dhtmlx.alert("请指定节点【" + subform.getItemValue("nextActivityName_" + k) + "】的审批人员！");
                        return;
                    }
                }
                // for(var k=1; k < j; k++){
                // var multisel = subform.getSelect("performer_"+k);
                // for (var i = 0; i < multisel.options.length; i++) {
                // performerIds += multisel.options[i].value + ",";
                // }
                // }
                // for (var i = 0; i < multisel.options.length; i++) {
                // performerIds += multisel.options[i].value + ",";
                // }
                // if ("" == performerIds) {
                // dhtmlx.alert("请指定审批人员！");
                // return;
                // }
                // var nextActivityId =
                // subform.getItemValue("nextActivityId");
                var opinion = subform.getItemValue("opinion");
                var url = MT_coflow_url(MT_common.P_COMPLETE);
                url += "&P_tableId=" + tableId + "&P_nextActivityId=" + nextActivityId + "&P_performerIds="
                        + performerIds + "&P_opinion=" + opinion;
                form.send(encodeURI(addTimestamp(url)), "post", function(loader, response) {
                    var wid = eval("(" + loader.xmlDoc.responseText + ")");
                    if (typeof(wid) == "number" && wid > 0) {
                        // 刷新工作箱数据
                        if (that.MT_TREE_refresh_parent && typeof(that.MT_TREE_refresh_parent) == "function") {
                            that.MT_TREE_refresh_parent();
                        }
                        dhtmlx.message(getMessage("operate_success"));
                        return;
                    }
                    dhtmlx.alert(getMessage("operate_failure"));
                });
            }
        });
        var winLayout = win.attachLayout("2U");
        var aLayout = winLayout.cells("a");
        var bLayout = winLayout.cells("b");
        var workId = that[pGId].MT_coflowId(dataId, MT_common.W_ID);
        var curl = (MT_getAction() + "!currentActivity.json?P_workitemId=" + workId);
        var activ = loadJson(curl);
        // if(true == activ.id){
        // MT_USER_init(aLayout, treeClickEvent);
        // } else {
        // MT_USER_init(aLayout);
        // }
        MT_USER_init(aLayout, treeClickEvent);
        bLayout.setText("流转到下一步");
        var formJson = [
                // {type: "fieldset", name: "block_currActivity", label:
                // "当前节点", offsetLeft:20, list:[
                // {type: "input", name: "currActivityName",
                // offsetLeft:10, readonly: true, inputWidth:220}
                // ]},
                {
                    type : "label",
                    name : "currActivityName",
                    offsetLeft : 30,
                    readonly : true,
                    inputWidth : 220
                }, {
                    type : "fieldset",
                    name : "block_next",
                    label : "下一节点",
                    offsetLeft : 20,
                    list : []
                }, {
                    type : "fieldset",
                    name : "block_yj",
                    label : "审批意见",
                    offsetLeft : 20,
                    list : [{
                        type : "input",
                        name : "opinion",
                        offsetLeft : 10,
                        rows : 6,
                        inputWidth : 220
                    }]
                }];
        subform = bLayout.attachForm(formJson);
        var j = setDefaultPerformer();
        var curMultisel = null;
        for (var k = 1; k < j; k++) {
            var multisel = subform.getSelect("performer_" + k);
            if (k == 1) {
                curMultisel = multisel;
                subform.setItemFocus("performer_" + k);
            }
            multisel.ondblclick = function() {
                var idx = this.selectedIndex;
                if ("-1" == idx)
                    idx = this.options.length - 1;
                if ("-1" != idx) {
                    this.options.remove(idx);
                }
            };
            multisel.onfocus = function() {
                curMultisel = this;
            };
            multisel.onclick = function() {
                var idx = this.selectedIndex;
                if ("-1" == idx)
                    idx = this.options.length - 1;
                if (idx != "-1" && this.options[idx].selected != true) {
                    this.options[idx].selected = true;
                }
            };
            multisel.onblur = function() {
                var idx = this.selectedIndex;
                if ("-1" == idx)
                    idx = this.options.length - 1;
                if (idx != "-1" && this.options[idx].selected != false) {
                    this.options[idx].selected = false;
                }
            };
        }
        /** 设置默认提交人. */
        function setDefaultPerformer() {
            // 列表选中ID
            if (undefined == dataId || null == dataId) {
                dataId = that[pGId].MT_coflowId(null, MT_common.ROW_ID);
            }
            var workitemId = that[pGId].MT_coflowId(dataId, MT_common.W_ID);
            var url = (MT_getAction() + "!nextActivityPerformer.json?P_workitemId=" + workitemId);
            var activities = loadJson(url);
            var i = 1;
            if (null != activities) {
                var activity = activities[0];
                // subform.setItemValue("currActivityName",
                // activity.activityName);
                subform.setItemLabel("currActivityName", "当前节点：" + activity.activityName);
                subform.removeItem("block_nextActivity");
                for (; i < activities.length; i++) {
                    var selected = false;
                    if (i == 1)
                        selected = true;
                    activity = activities[i];
                    var nextActivity = {
                        type : "block",
                        name : "block_nextActivity_" + i,
                        offsetLeft : 10,
                        list : [{
                            type : "hidden",
                            name : "nextActivityId_" + i,
                            value : activity.activityId
                        }, {
                            type : "checkbox",
                            label : activity.activityName,
                            name : "nextActivityName_" + i,
                            value : activity.activityName,
                            position : "label-right",
                            inputWidth : 220
                        }, {
                            type : "multiselect",
                            name : "performer_" + i,
                            inputHeight : 80,
                            inputWidth : 220,
                            options : [{
                                value : activity.performerId,
                                text : activity.performerName,
                                selected : selected
                            }]
                        }]
                    };
                    subform.addItem("block_next", nextActivity);
                    if (activity.checked) {
                        subform.checkItem("nextActivityName_" + i);
                    }
                    if (activity.disabled) {
                        subform.disableItem("nextActivityName_" + i);
                    }
                    if (activity.isRollback) {
                        subform.setItemLabel("nextActivityName_" + i, "<font color='red'>（" + activity.activityName
                                + "）</font>");
                    }
                    _this.ended = activity.ended;
                }
                if (true == _this.ended) {
                    subform.disableItem("performer");
                    _this.ended = true;
                }
            }
            return i;
        }
        /** 树节点单击事件. */
        function treeClickEvent(id, name, type) {
            if ("D" == type || true == _this.ended)
                return;
            // if(true != activ.id){
            // dhtmlx.alert("该节点操作者不能改变!");
            // return;
            // }
            var inOptions = false;
            for (var n = 0; n < curMultisel.options.length; n++) {
                if (id == curMultisel.options[n].value)
                    inOptions = true;
            }
            if (!inOptions) {
                curMultisel.options.add(new Option(name, id));
            }
        }
        function close() {
            win.close();
            // refresh grid data
            if (that[pGId] && that[pGId].MT_GRID_reload && typeof that[pGId].MT_GRID_reload == "function") {
                that[pGId].MT_GRID_reload();
            }
        }
    }
    /** 退回. */
    function MT_FORM_untread() {
        var win = MT_CreateDhxWindow({
            id : "COFLOW$" + tableId,
            title : "退回",
            width : 400,
            height : 320
        });
        win.button("close").attachEvent("onClick", function() {
            close();
        });
        var sbar = win.attachStatusBar();
        var tbar = new dhtmlXToolbarObject(sbar.id);
        tbar.setIconPath(TOOLBAR_IMAGE_PATH);
        tbar.addButton("submit", 1, "退回", "untread.gif");
        tbar.addSeparator("subwin$sep$01", 2);
        tbar.addButton("bottom$close", 4, "关闭", "close.gif");
        tbar.setAlign("right");
        tbar.attachEvent("onClick", function(id) {
            if ("bottom$close" == id) {
                close();
            } else if ("submit" == id) {
                var backActivityId = subform.getItemValue("backActivityId");
                var opinion = subform.getItemValue("opinion");
                var url = MT_coflow_url(MT_common.P_UNTREAD);
                url += "&P_tableId=" + tableId + "&P_opinion=" + opinion + "&id=" + form.getItemValue("ID");
                dhtmlxAjax.get(addTimestamp(url), function(loader) {
                    var wid = eval("(" + loader.xmlDoc.responseText + ")");
                    if (typeof(wid) == "number" && wid > 0) {
                        // 刷新工作箱数据
                        if (that.MT_TREE_refresh_parent && typeof(that.MT_TREE_refresh_parent) == "function") {
                            that.MT_TREE_refresh_parent();
                        }
                        dhtmlx.message(getMessage("operate_success"));
                        return;
                    }
                    dhtmlx.alert(getMessage("operate_failure"));
                });
            }
        });
        var winLayout = win.attachLayout("1C");
        var aLayout = winLayout.cells("a");
        aLayout.hideHeader();
        var formJson = [{
            type : "block",
            name : "block_currActivity",
            offsetTop : 20,
            list : [{
                type : "input",
                label : "当前节点：",
                name : "currActivityName",
                offsetLeft : 10,
                readonly : true,
                inputWidth : 280
            }]
        }, {
            type : "block",
            name : "block_yj",
            offsetLeft : 10,
            list : [{
                type : "input",
                label : "退回意见：",
                name : "opinion",
                rows : 10,
                inputWidth : 280
            }]
        }];
        subform = aLayout.attachForm(formJson);
        setDefaultPerformer();
        function setDefaultPerformer() {
            // 列表选中ID
            if (undefined == dataId || null == dataId) {
                dataId = that[pGId].MT_coflowId(null, MT_common.ROW_ID);
            }
            var workitemId = that[pGId].MT_coflowId(dataId, MT_common.W_ID);
            var url = (MT_getAction() + "!backActivityPerformer.json?P_workitemId=" + workitemId);
            var activities = loadJson(addTimestamp(url));
            if (null != activities) {
                var activity = activities[0];
                subform.setItemValue("currActivityName", activity.activityName);
            }
        }
        function close() {
            win.close();
            // refresh grid data
            if (that[pGId] && that[pGId].MT_GRID_reload && typeof that[pGId].MT_GRID_reload == "function") {
                that[pGId].MT_GRID_reload();
            }
        }
        /*var url = MT_coflow_url(MT_common.P_UNTREAD);
        dhtmlxAjax.get(url,function(loader){
        	var wid = eval("(" + loader.xmlDoc.responseText + ")");
        	if (typeof(wid) == "number" && wid > 0) {
        		// 刷新工作箱数据
        		if (that.MT_TREE_refresh_parent && typeof(that.MT_TREE_refresh_parent) == "function") {
        			that.MT_TREE_refresh_parent();
        		}
        		dhtmlx.message(getMessage("operate_success"));
        		return;
        	}
        	dhtmlx.alert(getMessage("operate_failure"));
        });*/
    }
    /** 撤回. */
    function MT_FORM_recall() {
        var url = MT_coflow_url(MT_common.P_RECALL);
        dhtmlxAjax.get(addTimestamp(url), function(loader) {
            var wid = eval("(" + loader.xmlDoc.responseText + ")");
            if (typeof(wid) == "number" && wid > 0) {
                // 刷新工作箱数据
                if (that.MT_TREE_refresh_parent && typeof(that.MT_TREE_refresh_parent) == "function") {
                    that.MT_TREE_refresh_parent();
                }
                dhtmlx.message(getMessage("operate_success"));
                return;
            }
            dhtmlx.alert(getMessage("operate_failure"));
        });
    }
    /** 转办. */
    function MT_FORM_reassign() {

    }
    /** 传阅. */
    function MT_FORM_deliver() {

    }
    /** 阅毕. */
    function MT_FORM_hasread() {

    }
    /** 跟踪. */
    function MT_FORM_track() {
        if (undefined == dataId || null == dataId) {
            dataId = that[pGId].MT_coflowId(null, MT_common.ROW_ID);
        }
        var pid = that[pGId].MT_coflowId(dataId, MT_common.P_ID);
        var url = MT_getAction() + "!coflowTrack?P_op=graph&P_processInstanceId=" + pid;
        openWin(addTimestamp(url));
    }
    function openWin(url) {
        var iWidth = window.screen.width;
        var iHeight = window.screen.height;
        var win = window.open(url, "流程跟踪", 'height=' + iHeight + ',innerHeight=' + iHeight + ',width=' + iWidth
                + ',innerWidth=' + iWidth
                + ',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
        return win;
    }
    /** 首条、上一条、下一条、末条. */
	function PV_FORM_LoadByIndex(rowIndex) {
		var rowId = that[pGId].grid.getRowId(rowIndex);
		MT_FORM_load(rowId);
		if (rowIndex == 0) {
			if (ftbar.getPosition(MT_common.P_FIRST_RECORD) != null) {
				ftbar.disableItem(MT_common.P_FIRST_RECORD);
			}
			if (ftbar.getPosition(MT_common.P_PREVIOUS_RECORD) != null) {
				ftbar.disableItem(MT_common.P_PREVIOUS_RECORD);
			}
			if (ftbar.getPosition(MT_common.P_NEXT_RECORD) != null) {
				ftbar.enableItem(MT_common.P_NEXT_RECORD);
			}
			if (ftbar.getPosition(MT_common.P_LAST_RECORD) != null) {
				ftbar.enableItem(MT_common.P_LAST_RECORD);
			}
		} else if (rowIndex == maxRowIndex) {
			if (ftbar.getPosition(MT_common.P_FIRST_RECORD) != null) {
				ftbar.enableItem(MT_common.P_FIRST_RECORD);
			}
			if (ftbar.getPosition(MT_common.P_PREVIOUS_RECORD) != null) {
				ftbar.enableItem(MT_common.P_PREVIOUS_RECORD);
			}
			if (ftbar.getPosition(MT_common.P_NEXT_RECORD) != null) {
				ftbar.disableItem(MT_common.P_NEXT_RECORD);
			}
			if (ftbar.getPosition(MT_common.P_LAST_RECORD) != null) {
				ftbar.disableItem(MT_common.P_LAST_RECORD);
			}
		} else {
			if (ftbar.getPosition(MT_common.P_FIRST_RECORD) != null) {
				ftbar.enableItem(MT_common.P_FIRST_RECORD);
			}
			if (ftbar.getPosition(MT_common.P_PREVIOUS_RECORD) != null) {
				ftbar.enableItem(MT_common.P_PREVIOUS_RECORD);
			}
			if (ftbar.getPosition(MT_common.P_NEXT_RECORD) != null) {
				ftbar.enableItem(MT_common.P_NEXT_RECORD);
			}
			if (ftbar.getPosition(MT_common.P_LAST_RECORD) != null) {
				ftbar.enableItem(MT_common.P_LAST_RECORD);
			}
		}
	}
    that[pFId].form = form;
    /*******************************(预留区、回调函数、输出参数函数)*********************************/
    var name = MT_ReturnFunctionName(areaIndex, MT_common.L_FORM);
    _M_this[name] = function() {
        return {
            /* 当前表单中ID值 */
            id : form.getItemValue("ID"),
            /* 当前表单所有栏位的值{name:value,...} */
            formData : form.getFormData(),
            /* 当前表单对应的表ID. */
            tableId : tableId,
            /* 当前表单所处的模块ID. */
            moduleId : that.moduleId,
            /* 如果表单与树存在关系，则为表单对应的树节点ID. */
            treeNodeId : that.nodeId
        };
    };
}