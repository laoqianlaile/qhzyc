/**
 * MT   => module template
 *       模块模板
 * FORM => form
 *       表单
 * @param that
 *       页面全局变量
 * @param fLayout
 *       表单布局区域
 * @param tableId
 *       表单对应表ID
 * @param areaIndex
 *       区域索引位置
 */
function PV_FORM_init(that, fLayout, tableId, areaIndex, dataId, master/*关联主列表{tableId:'', dataId:''}*/) {
	fLayout.hideHeader();
	// 属性名
	var pGId = PV_P_GridId(tableId);
	var pFId = PV_P_FormId(tableId);
	var _this = this;
	// 配置首条、上一条、下一条、末条时使用
	var rowIndex, maxRowIndex;
	if (dataId) {
		rowIndex = that[pGId].grid.getRowIndex(dataId);
		maxRowIndex = that[pGId].grid.rowsBuffer.length%that[pGId].grid.pageSize - 1;
	}
	/** 表单结构配置(JSON格式).*/
	function PV_GetFormCfg() {
		var url = contextPath + "/appmanage/app-form!dhtmlxForm.json?P_tableId=" + tableId + "&P_moduleId=" + that.moduleId;
		var jsonObj = loadJson(url);
		if (null == jsonObj || "" == jsonObj) {
			return null;
		}
		var cfg = {};
		cfg.formJson = eval("(" + jsonObj.formJson + ")");
		cfg.keptColumns = jsonObj.keptColumns;
		cfg.increaseColumns = jsonObj.increaseColumns;
		cfg.inheritColumns = jsonObj.inheritColumns;
		cfg.defaultValues = jsonObj.defaultValues;
		if ("" == jsonObj.initJs) {
			cfg.initEvent = null;
		} else {
			try {
				cfg.initEvent = eval("(" + jsonObj.initJs + ")");
			} catch (e) {
				dhtmlx.alert("onload事件语法有问题，请联系管理员！");
			}
		}
		if ("" == jsonObj.changeJs) {
			cfg.changeEvent = null;
		} else {
			try {
				cfg.changeEvent = eval("(" + jsonObj.changeJs + ")");
			} catch (e) {
				dhtmlx.alert("onchange事件语法有问题，请联系管理员！");
			}
		}
		if ("" == jsonObj.beforeSaveJs) {
			cfg.beforSaveEvent = null;
		} else {
			try {
				cfg.beforeSaveEvent = eval("(" + jsonObj.beforeSaveJs + ")");
			} catch (e) {
				dhtmlx.alert("beforeSave事件语法有问题，请联系管理员！");
			}
		}
		if ("" == jsonObj.afterSaveJs) {
			cfg.afterSaveEvent = null;
		} else {
			try {
				cfg.afterSaveEvent = eval("(" + jsonObj.afterSaveJs + ")");
			} catch (e) {
				dhtmlx.alert("afterSave事件语法有问题，请联系管理员！");
			}
		}
		return cfg;
	}
	/** 表单数据加载.*/
	function PV_FORM_load (id) {
		var url = contextPath + "/appmanage/show-module!show.json?P_tableId=" + tableId + 
				"&P_moduleId=" + that.moduleId + 
				"&id=" + id;
		var json = loadJson(url);
		if (null != json && "" != json) {
			form.setFormData(json);
		}
	};
	/** 工作流表单配置.*/
	function PV_FORM_disable() {
		if (undefined == dataId) {
			dataId = that[pGId].PV_coflowId(null, PV_common.ROW_ID);
		}
		var activityId = that[pGId].PV_coflowId(dataId, PV_common.ACTIVITY_ID);
		var url = PV_app_uri + "/workflow-form-setting!disableColumns.json?P_tableId=" + tableId + "&P_moduleId=" + that.moduleId
					+ "&P_activityId=" + activityId;
		var cols = loadJson(url);
		if (null != cols && cols.length > 0) {
			for (var i = 0; i < cols.length; i++) {
				form.disableItem(cols[i]);
			}
		}
	};
	/** 工作流审批意见.*/
	function PV_FORM_opinions() {
		var dataId = that[pGId].PV_coflowId(null, PV_common.ROW_ID);
		var activityId = that[pGId].PV_coflowId(dataId, PV_common.ACTIVITY_ID);
		var url = PV_app_uri + "/coflow-opinion!opinions.json?P_dataId=" + dataId;
		var opinions = loadJson(url);
		if (null == opinions || "" == opinions)  return;
		var blockWidth = form.getItemWidth("block_hidden");
		var itemData = {type: "block", name: "block_opinion", list:[
                {type: "itemlabel", label: "审批意见："},
        		{type: "itemdiv", name:"coflowOpinion", labelAlign: "left", value: opinions, width: (blockWidth-200)}//*/
            ]};
		form.addItem(null, itemData);
	};
	/** 给表关系配置了关联字段设置默认值.*/
	function PV_FORM_relation() {
		var url = PV_app_uri + "/show-module!relationData.json?P_tableId=" + tableId + "&P_M_tableId=" + master.tableId + "&P_M_dataId=" + master.dataId;
		var rlt = loadJson(url);
		if (rlt) { for (var p in rlt) { form.setItemValue(p, rlt[p]);}}
	};
	
	var fcfg = PV_GetFormCfg();
	if (null == fcfg || null == fcfg.formJson) {
		dhtmlx.alert("界面未配置！"); return;
	}
	/*****************************(工具条事件)*******************************/
	this.clickEvent = function(id) {
		if (PV_common.P_CREATE == id) {
			PV_FORM_create();
		} else if (PV_common.P_SAVE == id) {
			PV_FORM_save();
		} else if (PV_common.P_SUBMIT == id) {
			PV_FORM_submit();
		} else if (PV_common.P_COLSE == id) {
			PV_FORM_close();
		} else if (PV_common.P_SAVE_AND_CREATE == id) {
			PV_FORM_SaveAndCreate();
		} else if (PV_common.P_SAVE_AND_UPDATE == id) {
			PV_FORM_SaveAndUpate();
		} else if (PV_common.P_SAVE_AND_SUBMIT == id) {
			PV_FORM_SaveAndSubmit();
		} else if (PV_common.P_SAVE_AND_CLOSE == id) {
			PV_FORM_SaveAndClose();
		} else if (PV_common.P_RESET == id) {
			PV_FORM_Reset();
		} else if (PV_common.P_FIRST_RECORD == id) {
			rowIndex = 0;
			PV_FORM_LoadByIndex(rowIndex);
		} else if (PV_common.P_PREVIOUS_RECORD == id) {
			PV_FORM_LoadByIndex(--rowIndex);
		} else if (PV_common.P_NEXT_RECORD == id) {
			PV_FORM_LoadByIndex(++rowIndex);
		} else if (PV_common.P_LAST_RECORD == id) {
			rowIndex = maxRowIndex;
			PV_FORM_LoadByIndex(rowIndex);
		} 
	};
	// 工具条初始化
	var ftbar = PV_TBAR_init(that, fLayout, tableId, PV_common.L_FORM, _this.clickEvent);
	// 初始化表单
	var form  = fLayout.attachForm(fcfg.formJson);
	// 表单初始化
	if (null != fcfg.initEvent) {
		fcfg.initEvent();
	}
	// onchange 事件
	if (null != fcfg.changeEvent) {
		form.attachEvent("onChange", fcfg.changeEvent);
	}
		
	// 数据加载
	if (undefined != dataId && null != dataId) {
		PV_FORM_load(dataId);
	} else if (undefined != master && null != master) {
		// 初始化关联数据栏位默认值
		PV_FORM_relation();
	}
	/************************表单公用事件****************************/
	that[pFId] = {};
	
	/* 表单数据加载*/
	that[pFId].PV_FORM_load  = function(id) {
		PV_FORM_load(id);
	};
	/* 表单初始化*/
	that[pFId].PV_FORM_create = function() {
		PV_FORM_create();
	};

	// 处理工具条上面的按钮
	if (dataId) {
		if (that[pGId].grid) {
			if (rowIndex == 0) {
				if (ftbar.getPosition(PV_common.P_FIRST_RECORD) != null) {
					ftbar.disableItem(PV_common.P_FIRST_RECORD);
				}
				if (ftbar.getPosition(PV_common.P_PREVIOUS_RECORD) != null) {
					ftbar.disableItem(PV_common.P_PREVIOUS_RECORD);
				}
			} else if (rowIndex == maxRowIndex) {
				if (ftbar.getPosition(PV_common.P_NEXT_RECORD) != null) {
					ftbar.disableItem(PV_common.P_NEXT_RECORD);
				}
				if (ftbar.getPosition(PV_common.P_LAST_RECORD) != null) {
					ftbar.disableItem(PV_common.P_LAST_RECORD);
				}
			}
		}
	} else {
		if (ftbar.getPosition(PV_common.P_FIRST_RECORD) != null) {
			ftbar.hideItem(PV_common.P_FIRST_RECORD);
		}
		if (ftbar.getPosition(PV_common.P_PREVIOUS_RECORD) != null) {
			ftbar.hideItem(PV_common.P_PREVIOUS_RECORD);
		}
		if (ftbar.getPosition(PV_common.P_NEXT_RECORD) != null) {
			ftbar.hideItem(PV_common.P_NEXT_RECORD);
		}
		if (ftbar.getPosition(PV_common.P_LAST_RECORD) != null) {
			ftbar.hideItem(PV_common.P_LAST_RECORD);
		}
	}
	
	/*********************** 工具条事件定义 **************************/
	/** 初始化表单，只是简单的对输入框等控件进行清空.*/
	function PV_FORM_create() {
	    var defaultValues = fcfg.defaultValues;
    	var defaultValueArray = defaultValues.split(",");
    	function ValidateInteger(value) {
			return value.match(/^(0|[1-9][0-9]*)$/);
		}
	    form.forEachItem(function(name) {
	    	var increaseColumns = fcfg.increaseColumns;
	    	// 递增
            if ("" != increaseColumns && increaseColumns.indexOf("," + name + ",") > -1) {
            	var v = form.getItemValue(name);
            	if (ValidateInteger(v)) {
            	    form.setItemValue(name, parseInt(v) + 1);
            	}
                return;
            }
	    	// 连续录入字段不清空
	    	var keptColumns = fcfg.keptColumns;
	    	if ("" != keptColumns && keptColumns.indexOf("," + name + ",") > -1) return;
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
	};
	/** 表单保存.*/
	function PV_FORM_save() {
		var url = contextPath + "/appmanage/show-module!save.json?P_tableId=" + tableId;
		var checkResult = null;
		// 保存前校验
		if (fcfg.beforeSaveEvent && typeof(fcfg.beforeSaveEvent) == "function") {
			checkResult = fcfg.beforeSaveEvent();
			if (false == checkResult.success) {
				if (checkResult.message && "" != checkResult.message) {
					dhtmlx.alert(checkResult.message);
				}
				return;
			}
		}
		form.send(url, "post", function(loader, response) {
			var entity = eval("(" + loader.xmlDoc.responseText + ")");
			if (entity.ID == null || entity.ID == "") {
				dhtmlx.message(getMessage("save_failure"));
				return;
			}
			form.setItemValue("ID", entity.id);
			// refresh grid data
			if (that[pGId] && that[pGId].PV_GRID_reload 
					&& typeof that[pGId].PV_GRID_reload == "function") {
				that[pGId].PV_GRID_reload();
			}
			// 保存成功后回调函数
			if (fcfg.afterSaveEvent && typeof(fcfg.afterSaveEvent) == "function") {
				fcfg.afterSaveEvent(loader, response);
			}
			dhtmlx.message(getMessage("save_success"));//*/
		});
	};
	/** 提交.*/
	function PV_FORM_submit() {
		dhtmlx.alert("submit is doing!");
	}
	/** 关闭.*/
	function PV_FORM_close() {
		if (fLayout.close) {
			if (that[pGId] && that[pGId].PV_GRID_reload 
					&& typeof that[pGId].PV_GRID_reload == "function") {
				that[pGId].PV_GRID_reload();
			}
			fLayout.close();
		}
	};
	/** 保存并新增.*/
	function PV_FORM_SaveAndCreate() {
		var url = contextPath + "/appmanage/show-module!save.json?P_tableId=" + tableId;
		var checkResult = null;
		// 保存前校验
		if (fcfg.beforeSaveEvent && typeof(fcfg.beforeSaveEvent) == "function") {
			checkResult = fcfg.beforeSaveEvent();
			if (false == checkResult.success) {
				if (checkResult.message && "" != checkResult.message) {
					dhtmlx.alert(checkResult.message);
				}
				return;
			}
		}
		form.send(url, "post", function(loader, response) {
			var entity = eval("(" + loader.xmlDoc.responseText + ")");
			if (entity.ID == null || entity.ID == "") {
				dhtmlx.message(getMessage("save_failure"));
				return;
			}
			form.setItemValue("ID", entity.id);
			// refresh grid data
			if (that[pGId] && that[pGId].PV_GRID_reload 
					&& typeof that[pGId].PV_GRID_reload == "function") {
				that[pGId].PV_GRID_reload();
			}
			// 保存成功后回调函数
			if (fcfg.afterSaveEvent && typeof(fcfg.afterSaveEvent) == "function") {
				fcfg.afterSaveEvent(loader, response);
			}
			dhtmlx.message(getMessage("save_success"));//*/
			PV_FORM_create();
		});
	};
	/** 保存并修改.*/
	function PV_FORM_SaveAndUpate() {
		var url = contextPath + "/appmanage/show-module!save.json?P_tableId=" + tableId;
		form.send(url, "post", function(loader, response) {
			var entity = eval("(" + loader.xmlDoc.responseText + ")");
			if (entity.id == null || entity.id == "") {
				dhtmlx.message(getMessage("save_failure"));
				return;
			}
			//form.setItemValue("ID", entity.id);
			// refresh grid data
			if (that[pGId] && that[pGId].PV_GRID_reload 
					&& typeof that[pGId].PV_GRID_reload == "function") {
				that[pGId].PV_GRID_reload();
			}
			dhtmlx.message(getMessage("save_success"));//*/
		});
	};
	/** 保存并提交.*/
	function PV_FORM_SaveAndSubmit() {
		dhtmlx.message("save and submit is doing!");
	};
	/** 保存并关闭.*/
	function PV_FORM_SaveAndClose() {
		var url = contextPath + "/appmanage/show-module!save.json?P_tableId=" + tableId;
		var checkResult = null;
		// 保存前校验
		if (fcfg.beforeSaveEvent && typeof(fcfg.beforeSaveEvent) == "function") {
			checkResult = fcfg.beforeSaveEvent();
			if (false == checkResult.success) {
				if (checkResult.message && "" != checkResult.message) {
					dhtmlx.alert(checkResult.message);
				}
				return;
			}
		}
		form.send(url, "post", function(loader, response) {
			var entity = eval("(" + loader.xmlDoc.responseText + ")");
			if (entity.ID == null || entity.ID == "") {
				dhtmlx.message(getMessage("save_failure"));
				return;
			}
			form.setItemValue("ID", entity.id);
			// refresh grid data
			if (that[pGId] && that[pGId].PV_GRID_reload 
					&& typeof that[pGId].PV_GRID_reload == "function") {
				that[pGId].PV_GRID_reload();
			}
			// 保存成功后回调函数
			if (fcfg.afterSaveEvent && typeof(fcfg.afterSaveEvent) == "function") {
				fcfg.afterSaveEvent(loader, response);
			}
			dhtmlx.message(getMessage("save_success"));//*/
			PV_FORM_close();
		});
	};
	/** 重置.*/
	function PV_FORM_Reset() {
		var id = form.getItemValue("ID");
		if (id) {
			PV_FORM_load(dataId);
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
		}
	}
	/** 首条、上一条、下一条、末条. */
	function PV_FORM_LoadByIndex(rowIndex) {
		var rowId = that[pGId].grid.getRowId(rowIndex);
		PV_FORM_load(rowId);
		if (rowIndex == 0) {
			if (ftbar.getPosition(PV_common.P_FIRST_RECORD) != null) {
				ftbar.disableItem(PV_common.P_FIRST_RECORD);
			}
			if (ftbar.getPosition(PV_common.P_PREVIOUS_RECORD) != null) {
				ftbar.disableItem(PV_common.P_PREVIOUS_RECORD);
			}
			if (ftbar.getPosition(PV_common.P_NEXT_RECORD) != null) {
				ftbar.enableItem(PV_common.P_NEXT_RECORD);
			}
			if (ftbar.getPosition(PV_common.P_LAST_RECORD) != null) {
				ftbar.enableItem(PV_common.P_LAST_RECORD);
			}
		} else if (rowIndex == maxRowIndex) {
			if (ftbar.getPosition(PV_common.P_FIRST_RECORD) != null) {
				ftbar.enableItem(PV_common.P_FIRST_RECORD);
			}
			if (ftbar.getPosition(PV_common.P_PREVIOUS_RECORD) != null) {
				ftbar.enableItem(PV_common.P_PREVIOUS_RECORD);
			}
			if (ftbar.getPosition(PV_common.P_NEXT_RECORD) != null) {
				ftbar.disableItem(PV_common.P_NEXT_RECORD);
			}
			if (ftbar.getPosition(PV_common.P_LAST_RECORD) != null) {
				ftbar.disableItem(PV_common.P_LAST_RECORD);
			}
		} else {
			if (ftbar.getPosition(PV_common.P_FIRST_RECORD) != null) {
				ftbar.enableItem(PV_common.P_FIRST_RECORD);
			}
			if (ftbar.getPosition(PV_common.P_PREVIOUS_RECORD) != null) {
				ftbar.enableItem(PV_common.P_PREVIOUS_RECORD);
			}
			if (ftbar.getPosition(PV_common.P_NEXT_RECORD) != null) {
				ftbar.enableItem(PV_common.P_NEXT_RECORD);
			}
			if (ftbar.getPosition(PV_common.P_LAST_RECORD) != null) {
				ftbar.enableItem(PV_common.P_LAST_RECORD);
			}
		}
	}
	
	that[pFId].form = form;
}

