/**
 * 工作流与模块绑定
 * @param that
 * @returns {initModuleBinded}
 */
function initModuleBinded(that) {
	var _this = this;
	var moduleUrl = AppActionURI.workflowBinding;
	var tbbar = that.tabbar.cells("tab$02");
	var curLayout = tbbar.attachLayout("4W");
	initLayout(curLayout);
	
	// save report data source configuration
	this.save = function() {
		var cnt = dGrid.getRowsNum();
		if (0 == cnt) {
			dhtmlx.alert("请先进行模块绑定配置，再保存！");
			return;
		}
		var rowsValue = "";
		for (var i=0; i < cnt; i++) {
			var rowId = dGrid.getRowId(i);
			rowsValue += ";" + rowId.replace(/(;)/g,",");
		}
		rowsValue = rowsValue.substring(1);
		var url = moduleUrl + "!save?P_workflowId=" + that.nodeId + "&P_rowsValue=" + rowsValue;
		//dhtmlx.alert(url);
		dhtmlxAjax.get(url, function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.status) {
				dhtmlx.message(getMessage("operate_success"));
		    } else {
		    	dhtmlx.message(getMessage("operate_failure"));
		    }
		});//*/
	};
	
	
	var aLayout = curLayout.cells("a"); // 节点列表区
	var bLayout = curLayout.cells("b"); // 模块列表区
	var cLayout = curLayout.cells("c"); // 按钮操作区
	var dLayout = curLayout.cells("d"); // 绑定模块区
	// 节点列表区
	aLayout.setWidth(200);
	aLayout.hideHeader();
	// 
	this.getActivityUrl = function() {
		return AppActionURI.workflowActivity + "!cfActivities.json?E_model_name=datagrid&Q_workflowId=" + that.nodeId;
	};
	var aGrid = aLayout.attachGrid();
	aGrid.enableDragAndDrop(true);
	var aCfg = {
			format: {
				headers: ["<center>流程节点</center>"],
				cols: ["activityName"],
				userdata: ["activityId"],
				colWidths: ["*"],
				colTypes: ["ro"],
				colAligns: ["left"]
			},
			multselect: false
		};
	initGridWithoutPageable(aGrid, aCfg, _this.getActivityUrl());
	// 
	aGrid.attachEvent("onDrop", function(sId,tId,dId,sObj,tObj,sCol,tCol){
		if(undefined != tId){
			var adjustUrl = AppActionURI.workflowActivity + "!adjustShowOrder?P_workflowId=" + that.nodeId + "&P_sourceIds=" + sId + "&P_targetId=" + tId;
			dhtmlxAjax.get(adjustUrl, function(loader) {
				loadGridData(aGrid, aCfg, _this.getActivityUrl());
				aGrid.selectRowById(sId);
			});	
		}		
	});
	// 模块列表区
	bLayout.setWidth(200);
	bLayout.hideHeader();
	// 
	this.getModuleUrl = function() {
		return AppActionURI.module + "!getWorkflowModule.json?P_tableId=" + that.tableId;
	};
	var bGrid = bLayout.attachGrid();
	var bCfg = {
			format: {
				headers: ["<center>模块名称</center>"],
				cols: ["name"],
				colWidths: ["*"],
				colTypes: ["ro"],
				colAligns: ["left"]
			},
			multselect: false
		};
	initGridWithoutPageable(bGrid, bCfg, _this.getModuleUrl());
	// create div in document body
	var divId = "DIV-oparatorArea-BD";
	createEmptyDiv(divId, 35);
	var buttonForm = new dhtmlXForm(divId, getMoveButton({allToRight:false, saveButton:true}));
	buttonForm.attachEvent("onButtonClick", function(id) {
	    if ("toRight" == id) {
	    	_this.moveSelected(aGrid, bGrid, dGrid);
	    } else if ("toLeft" == id) {
	    	_this.cancelMove(dGrid, false);
	    } else if ("allToLeft" == id) {
	    	_this.cancelMove(dGrid, true);
	    } else if ("save" == id) {
	    	_this.save();
	    }
	});
	cLayout.hideHeader();
	cLayout.setWidth(105);
	cLayout.attachObject(divId);
	// 移动选中行
	this.moveSelected = function(fGrid, sGrid, tGrid) {
		var fRowId = fGrid.getSelectedRowId();
		var sRowId = sGrid.getSelectedRowId();
		if ((null == fRowId || "" == fRowId) && (null == sRowId || "" == sRowId)) {
			dhtmlx.alert("请选择流程节点和模块，再进行移动配置！");
			return;
		}
		if ((null == fRowId || "" == fRowId)) {
			dhtmlx.alert("请选择流程节点，再进行移动配置！");
			return;
		}
		if ((null == sRowId || "" == sRowId)) {
			dhtmlx.alert("请选择模块，再进行移动配置！");
			return;
		}
		var activityId = fGrid.getUserData(fRowId, "activityId");
		var tRowId = activityId + ";" + sRowId;
		if (tGrid.getRowIndex(tRowId) > -1) {
			tGrid.selectRowById(tRowId);
			dhtmlx.alert("此关系已经配好，请重新设置！");
			return;
		}
		tGrid.addRow(tRowId, [fGrid.cells(fRowId,0).getValue(), sGrid.cells(sRowId,0).getValue()]);
		
		fGrid.clearSelection();
		sGrid.clearSelection();
	};
	// 移除
	this.cancelMove = function(tGrid, isAll) {
		if (isAll) {
			tGrid.clearAll();
		} else {
			var tRowIds = tGrid.getSelectedRowId();
			if (null == tRowIds || "" == tRowIds) {
				dhtmlx.alert("请在右边列表中选择要移除的记录！");
				return;
			}
			tGrid.deleteSelectedRows();
		}
	};
	// 表字段列表
	dLayout.hideHeader();
	var dGrid = dLayout.attachGrid();		// 
	dGrid.enableDragAndDrop(true);
	this.getBindedUrl = function() {
		return moduleUrl + "!bindedModule.json?E_model_name=datagrid&Q_workflowId=" + that.nodeId;
	};

	var dCfg = {
			format: {
				headers: ["<center>流程节点</center>","<center>模块名称</center>",""],
				cols: ["activityName","moduleName"],
				colWidths: ["160","160","*"],
				colTypes: ["ro","ro","ro"],
				colAligns: ["left","left","left"]
			}
		};
	initGridWithoutPageable(dGrid, dCfg, _this.getBindedUrl());//*/
	// reload binded module
	that.reloadModuleBinded = function() {
		loadGridData(aGrid, aCfg, _this.getActivityUrl());
		loadGridData(bGrid, bCfg, _this.getModuleUrl());
		loadGridData(dGrid, dCfg, _this.getBindedUrl());
		/*var obj = document.getElementById(divId);
		if (obj) {
			cLayout.attachObject(obj);
		}//*/
	};
	// 标记已加载
	that.moduleBindedInited = true;
}