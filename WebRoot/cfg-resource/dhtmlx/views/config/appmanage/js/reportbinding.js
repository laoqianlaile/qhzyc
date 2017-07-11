/**
 * 报表绑定
 */

function loadCellReportBinding(tabbar, that) {
	var _this = this;
	var moduleUrl = AppActionURI.reportBinding;
	
	var dhxLayout = tabbar.cells("tab$report$04").attachLayout("3W");
	dhxLayout.cont.obj._offsetTop = 1;
	dhxLayout.cont.obj._offsetLeft = 1;
	dhxLayout.cont.obj._offsetHeight = -2;
	dhxLayout.cont.obj._offsetWidth = -2;
	dhxLayout.setSizes();
	
	/*var ttbar = dhxLayout.attachToolbar();
	ttbar.setIconPath(IMAGE_PATH);
	ttbar.addButton("save", 6, "保存", "save.gif");

	ttbar.attachEvent("onClick", function(itemId) {
		if ("save" == itemId) {
			_this.save();
		}
	});//*/
	
	// save report data source configuration
	this.save = function() {
		var cnt = cGrid.getRowsNum();
		if (0 == cnt) {
			dhtmlx.alert("请先进行模块绑定配置，再保存！");
			return;
		}
		var rowsValue = "";
		for (var i=0; i < cnt; i++) {
			var rowId = cGrid.getRowId(i);
			rowsValue += ";" + rowId;
		}
		rowsValue = rowsValue.substring(1);
		var url = moduleUrl + "!save?P_reportId=" + nodeId + 
				"&P_rowsValue=" + rowsValue;
		dhtmlxAjax.get(url,function(loader){
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.status) {
				dhtmlx.message(getMessage("operate_success"));
		    } else {
		    	dhtmlx.message(getMessage("operate_failure"));
		    }
		});//*/
	};
	
	
	var aLayout = dhxLayout.cells("a"); // 模块列表区
	var bLayout = dhxLayout.cells("b"); // 按钮操作区
	var cLayout = dhxLayout.cells("c"); // 绑定模块区
	// 用户表列表
	aLayout.setWidth(305);
	aLayout.hideHeader();
	
	var aGrid = aLayout.attachGrid();
	var aCfg = {
			format: {
				headers: ["<center>可选模块</center>"],
				//cols: ["show_name"],
				colWidths: ["300"],
				colTypes: ["ro"],
				colAligns: ["left"]
			}
		};
	var aUrl = moduleUrl + "!defaultBindingModule.json?E_model_name=datagrid&Q_reportId=" + nodeId;
	initGridWithoutPageable(aGrid, aCfg, aUrl);

	// create div in document body
	var divId = "DIV-oparatorArea-BD";
	createEmptyDiv(divId, 40);
	var buttonForm = new dhtmlXForm(divId, getMoveButton({saveButton:true}));
	buttonForm.attachEvent("onButtonClick", function(id){
	    if ("toRight" == id) {
	    	_this.moveSelected(aGrid, cGrid);
	    } else if ("toLeft" == id) {
	    	_this.moveSelected(cGrid, aGrid);
	    } else if ("allToLeft" == id) {
	    	_this.moveAll(cGrid, aGrid);
	    } else if ("allToRight" == id) {
	    	_this.moveAll(aGrid, cGrid);
	    } else if ("save" == id) {
	    	_this.save();
	    }
	});
	bLayout.hideHeader();
	bLayout.setWidth(105);
	bLayout.attachObject(divId);
	// 移动选中行
	this.moveSelected = function(fromGrid, toGrid) {
		var rowIds = fromGrid.getSelectedRowId();
		if (null == rowIds || "" == rowIds) {
			dhtmlx.alert("请选择模块，再移动！");
			return;
		}
		var rowIdArr = rowIds.split(",");
		for (var i = 0; i < rowIdArr.length; i++) {
			var name = fromGrid.cells(rowIdArr[i],0).getValue();
			toGrid.addRow(rowIdArr[i], name);
		}
		fromGrid.deleteSelectedRows();
	};
	// 移动所有行
	this.moveAll = function(fromGrid, toGrid) {
		fromGrid.forEachRow(function(rowId) {
			var name = fromGrid.cells(rowId,0).getValue();
			toGrid.addRow(rowId, name);
		});
		fromGrid.clearAll();
	};
	// 表字段列表
	//dLayout.setWidth(500);
	cLayout.hideHeader();
	
	var cGrid = cLayout.attachGrid();

	var cCfg = {
			format: {
				headers: ["<center>已选模块</center>"],
				//cols: ["show_name"],
				colWidths: ["300"],
				colTypes: ["ro"],
				colAligns: ["left"]
			}
		};
	var cUrl = moduleUrl + "!defineBindingModule.json?E_model_name=datagrid&Q_reportId=" + nodeId;
	initGridWithoutPageable(cGrid, cCfg, cUrl);//*/
	cGrid.enableDragAndDrop(true);
	
	// success loaded (loaded completed)
	that.bindedLoaded = true;
}
