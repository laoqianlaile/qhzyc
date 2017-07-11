/**
 * 报表控件 object 标签
 */
function writeCellWeb() {
	var clsid = "3F166327-8030-4881-8BD2-EA25350E574A";
	var version = "65536", extentx="9710", extenty="4842", stockProps="0", wmode="opaque";
	if (_isIE || _isChrome) {
		// IE
		document.write("<OBJECT ID=\"CellWeb\" " +
			"CODEBASE=\"cellweb5.cab\" " +
			"STYLE=\"height: 100%;width: 100%;\"  " +
			"CLASSID=\"clsid:" + clsid + "\">" +
				"<PARAM NAME=\"_Version\" VALUE=\"" + version + "\">" +
				"<PARAM NAME=\"_ExtentX\" VALUE=\"" + extentx + "\">" +
				"<PARAM NAME=\"_ExtentY\" VALUE=\"" + extenty + "\">" +
				"<PARAM NAME=\"_StockProps\" VALUE=\"" + stockProps + "\">" +
				"<PARAM NAME=\"Wmode\" VALUE=\"" + wmode + "\">" +
			"</OBJECT>");
	} else {
		// OTHER
		document.write("<OBJECT TYPE=\"application/x-itst-activex\" " +
				"ID=\"CellWeb\" " +
				"CODEBASE=\"cellweb5.cab\" " +
				"STYLE=\"height: 100%;width: 100%;\" " +
				"CLSID=\"{" + clsid + "}\" " +
				"PARAM__Version=\"" + version + "\" " +
				"PARAM__ExtentX=\"" + extentx + "\" " +
				"PARAM__ExtentY=\"" + extenty + "\" " +
				"PARAM__StockProps=\"" + stockProps + "\" " +
				"PARAM_wmode=\"" + wmode + "\"></OBJECT>");
	}
}
/**
 * 菜单控件 object 标签
 */
function writeCellMenu() {
	var clsid = "F82DB98D-842D-4DAB-9312-E478D8255720";
	var version = "65536", extentx="24527", extenty="503", stockProps="0";
	
	if (_isIE || _isChrome) {
		document.write("<OBJECT ID=\"Menu1\" " +
				"CODEBASE=\"Menu.ocx\" " + 
				"STYLE=\"HEIGHT: 20px;width: 100%;\" " + 
				"CLASSID=\"clsid:" + clsid + "\">" +
					"<PARAM NAME=\"_Version\" VALUE=\"" + version + "\">" +
					"<PARAM NAME=\"_ExtentX\" VALUE=\"" + extentx + "\">" + 
					"<PARAM NAME=\"_ExtentY\" VALUE=\"" + extenty + "\">" +
					"<PARAM NAME=\"_StockProps\" VALUE=\"" + stockProps + "\">" +
				"</OBJECT>");
	} else {
		document.write("<OBJECT ID=\"Menu1\" " +
					"TYPE=\"application/x-itst-activex\" " +
					"CODEBASE=\"Menu.ocx\" " + 
					"STYLE=\"HEIGHT: 20px;width: 100%;\" " + 
					"CLASSID=\"clsid:" + clsid + "\" " +
					"PARAM__Version=\"" + version + "\" " +
					"PARAM__ExtentX=\"" + extentx + "\" " + 
					"PARAM__ExtentY=\"" + extenty + "\" " +
					"PARAM__StockProps=\"" + stockProps + "\">" +
				"</OBJECT>");
	}
}
/**
 * 初始化字体下拉框
 */
function initFontname(){
	strFontnames = CellWeb.GetDisplayFontnames();
	var arrFontname = strFontnames.split('|');
	arrFontname.sort();
	var i;
	var sysFont;
	if( CellWeb.GetSysLangID () == 2052) {
		sysFont = "宋体";
	} else {
		sysFont = "Arial";
	}
	var options = [];
	for( i =0; i < arrFontname.length;i++ ) {
		var option = [arrFontname[i],arrFontname[i]];
		options.push(option);
	}
	fontNameCombo.addOption(options);
	if (null != fontNameCombo.getOption(sysFont)) {
		fontNameCombo.setComboValue(sysFont);
	}
}

/**
 * 报表工具条
 */
function initReportToolbar() {
	var _this = this;
	var reportImagePath = DHX_RES_PATH + "/views/config/appmanage/report/images/";
	// first tool bar 
	var ttbar_1 = new dhtmlXToolbarObject("reporttoolbar$1$div", Skin); 
	ttbar_1.setIconPath(reportImagePath);
	ttbar_1._addItem({id:"cmdFilePrint", type:"button", title:"打印", img:"print.gif"}, 3);
	ttbar_1._addItem({id:"cmdFilePrintPreview", type:"button", title:"打印预览", img:"printpreview.gif"}, 4);
	ttbar_1.attachEvent("onClick", function(itemId){
		_this.toolbarClick(ttbar_1, itemId);
	});
	this.toolbarClick = function(ttbar, itemId) {
		var type = ttbar.getType(itemId);
		if ("button" == type) {
			if(window.event != null) {
				window.event.cancelBubble = true;
			}
			// Regular push button
			onCbClick(itemId, true);
			return(false);
		}
	};
}
/**
 * 
 */
function initObj() {
	// init menu
	//var href = basePath + "views/config/appmanage/report/emenu.clm";
	//Menu1.ReadMenuFromRemoteFile(href);
	// set widht and height
	var tbarObj = document.getElementById("reporttoolbar$3$div");
	var cellWebObj = document.getElementById("cellweb$div");
	var accordionObj = document.getElementById("accordion$div");
	var cw = tbarObj.offsetWidth;
	var ch = document.body.offsetHeight - 100;
	CellWeb.Login("中信报表", "", 13040409, "5160-0447-0112-5004");
	CellWeb.ExtendPaste = 1; // 要粘贴的区域超过表格边界,是否扩大寸
	CellWeb.style.width = cw;
	cellWebObj.style.width = cw;
	CellWeb.style.height = ch;
	cellWebObj.style.height = ch;
	accordionObj.style.height = ch;
	// bind event
	if (_isIE) {
	/*	Menu1.attachEvent('ClickMenuItem', function(name) {
			if (name == "FileSave") {
				CellUtil.save();
			}
		});*/
		/*CellWeb.attachEvent("SelChanged", function(oc, or, nc, nr) {
			if (curSelect) {
				curSelect.collapse();
				curSelect.fireEvent("blur");
				curSelect = null;
			}
		});//*/
		CellWeb.attachEvent("KeyDown", function(keyCode, shift) {
			//alert(keyCode+"=============================");
			if (keyCode == 46) {
				CellUtil.delField();
			}
		});
	} else {
		/*Menu1.addEventListener("ClickMenuItem", function(name) {
			//alert(name);
			if (name == "FileSave") {
				CellUtil.save();
			}
		}, false);*/
		//alert(CellWeb.KeyDown);
		CellWeb.addEventListener("KeyDown", function(keyCode, shift) {
			//alert(keyCode+"=============================");
			if (keyCode == 46) {
				CellUtil.delField();
			}
		}, false);
	}
	// font name
	//initFontname(); 
	var rlt = CellWeb.OpenFile(cllPath, "");	
}


function initAccordion() {
	var _this = this;
	this.bInitDs = this.bInitPage = this.bInitSort = this.bInitCond = this.bInitGroup = false;
	
	var dhxAccord = new dhtmlXAccordion("accordion$div", Skin);
	
	var ds = dhxAccord.addItem("acc$ds", "数据源");
	var page = dhxAccord.addItem("acc$page", "页面设置");
	var cond = dhxAccord.addItem("acc$cond", "条件设置");
	var group= dhxAccord.addItem("acc$group", "分组设置");
	var sort = dhxAccord.addItem("acc$sort", "排序设置");
	dhxAccord.openItem("acc$ds");
	initDs(ds, _this);
	
	dhxAccord.setSizes();
	
	this.doInitActive = function(itemId) {
		if ("acc$ds" == itemId && !_this.bInitDs) {
			initDs(ds, _this); 
		} else if ("acc$page" == itemId && !_this.bInitPage) {
			initPage(page, _this); 
		} else if ("acc$sort" == itemId && !_this.bInitSort) {
			initSetting(sort, 3, _this);
		} else if ("acc$cond" == itemId && !_this.bInitCond) {
			initSetting(cond, 1, _this);
		} else if ("acc$group" == itemId && !_this.bInitGroup) {
			initSetting(group, 2, _this);
		} 
		return true;
	};
	dhxAccord.attachEvent("onBeforeActive", _this.doInitActive);

}
/**
 * CELL报表数据源树
 * @param ds
 */
function initDs(ds, that) {
	var _this = this;

	/*var sbar = ds.attachStatusBar();
	var tbar = new dhtmlXToolbarObject(sbar.id, Skin);
	tbar.addDiv("op$div",0);
	var combo = new dhtmlXCombo("op$div","operator",80);
	var opts = [["+", "加"],
	            ["-", "减"],
	            ["*", "乘"],
	            ["/", "除"],
	            ["+|", "前连接"],
	            ["|+", "后连接"],
	            ["**", "补位"]];
	combo.addOption(opts);
	combo.attachEvent("onSelectionChange", function() {
		
	});
	tbar.addInput("value",1,"",80);//*/
	
	var ttbar = ds.attachToolbar();
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addButton("ds$save", 0, "保存", "save.gif");
	ttbar.addSeparator("ds$septr$01", 1);
	ttbar.addButton("ds$cancel", 2, "取消绑定", "cancle.gif");
	ttbar.attachEvent("onClick", function(id){
		if (id == "ds$save") {
			CellUtil.save();
		} else if (id == "ds$cancel") {
			CellUtil.cancelField();
		}
	});
	// 这几个分类是需求中固定的
	var treeJson = {id:0, item:[
        {id:'-1',text:"报表数据源", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child:1, type:"ROOT"}
    ]};
	var tree = ds.attachTree();
	tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	var treeUrl = AppActionURI.reportDataSource + "!tree.json?E_model_name=tree&F_in=text,child&P_UD=type&P_reportId=" + reportId;
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	tree.setXMLAutoLoading(treeUrl);
	tree.loadJSONObject(treeJson);
	tree.refreshItem("-1");
	//tree.loadJSON(treeUrl + "&id=0");
	
	tree.attachEvent("onClick", function(nId) {
		var type = tree.getAttribute(nId,"type");
		if ("C" != type) return;
		var text = tree.getItemText(nId);
		CellUtil.bindedField(nId, text);
	});
	//把数据源设置初始化标识置为TRUE
	that.bInitDs = true;
}
/**
 * 页面设置
 */
function initPage(page, that) {
	var _this = this;
	var moduleUrl = AppActionURI.reportDefine;
	var caption = "<p>表头、表尾、循环、末页行可以不止一行，但必须是连续行号</p>" +
			"<p>设置格式为：起始行号 到 终止行号</p>" + 
			"<p>如要设置第1行和第2行为表头，则在表头行号：前面文本框输入1，后面文本框输入2</p>";
	var formJson = [{type : "settings", position : "label-left", 
		labelWidth : 60, inputWidth : 50, labelAlign : "left", offsetTop:"5"
    }, {type: "block", list:[
         	{type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "reportId", value: reportId}
        ]
    }, {type: "fieldset", name: "page", label: "页面设置", width: 230, list:[
            {type: "input", label: "表头行号", name: "headerStart", validate:"ValidNumeric", maxLength:3, offsetLeft: 5},
            {type: "input", label: "循环行号", name: "cycleStart", validate:"ValidNumeric", maxLength:3, offsetLeft: 5},
            {type: "input", label: "表尾行号", name: "tailStart", validate:"ValidNumeric", maxLength:3, offsetLeft: 5},
            {type: "input", label: "末页行号", name: "lastStart", validate:"ValidNumeric", maxLength:3, offsetLeft: 5},
            {type: "newcolumn"},
            {type: "input", label: "到", labelWidth : 15, name: "headerEnd", validate:"ValidNumeric", maxLength:3},
            {type: "input", label: "到", labelWidth : 15, name: "cycleEnd", validate:"ValidNumeric", maxLength:3},
            {type: "input", label: "到", labelWidth : 15, name: "tailEnd", validate:"ValidNumeric", maxLength:3},
            {type: "input", label: "到", labelWidth : 15, name: "lastEnd", validate:"ValidNumeric", maxLength:3},
        ]
    }, {type: "fieldset", name: "caption", label: "说明", width: 230, list:[
            {type: "itemlabel", label: caption, labelWidth: 200}
        ]
    }];
	var form = page.attachForm(formJson);
	
	var ttbar = page.attachToolbar();
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addButton("page$save", 0, "保存", "save.gif");
	ttbar.attachEvent("onClick", function(id){
		if (id == "page$save") {
			_this.save();
		}
	});
	
	this.save = function() {
		var url = moduleUrl;
		var method = "post";
		var id = form.getItemValue("id");
		if (id != "") {
			url += "/" + id;
			method = "put";
		}
		form.setItemValue("_method", method);
		form.send(url + ".json", "post", function(loader, response) {
			var formData = eval("(" + loader.xmlDoc.responseText + ")");
			//alert(formData.id);
			form.setItemValue("id", formData.id);
		});//*/
	};
	//
	this.loadFormData = function() {
		var url = moduleUrl + "!edit.json?P_reportId=" + reportId;
		var formData = loadJson(url);
		form.setFormData(formData);
	};
	_this.loadFormData();
	//把页面设置初始化标识置为TRUE
	that.bInitPage = true;
}

/**
 * 条件、分组、排序设置
 */
function initSetting(item, type, that) {
	//TO-DO
	var _this = this;
	var moduleUrl = AppActionURI.reportPrintSetting;
	// tool bar init
	//var layout = item.attachLayout("1C");
	var ttbar = item.attachToolbar();
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addButton("group$save", 1, "保存", "save.gif");
	ttbar.addSeparator("group$septr$01", 2);
	ttbar.addButton("group$new", 3, "添加", "new.gif");
	ttbar.addSeparator("group$septr$02", 4);
	ttbar.addButton("group$delete", 5, "移除", "delete.gif");
	ttbar.attachEvent("onClick", function(id){
		if (id == "group$save") {
			_this.save();
		} else if (id == "group$new") {
			_this.add();
		} else if (id == "group$delete") {
			_this.remove();
		}
	});//*/
	this.save = function() {
		// 1. 
		grid.forEachRow(function(rId) {
			var columnId = grid.cells(rId, 0).getValue();
			if (columnId == "-1") {
				grid.deleteRow(rId);
			}
		});
		// 2. 
		var rowsNum = grid.getRowsNum();
		if (null == rowsNum || 0 == rowsNum) {
			alert("请先设置，再保存！");
			return;
		}
		// 3. 
		var rowsValue = "";
		for (var i = 0; i < rowsNum; i++) {
			var rId = grid.getRowId(i);
			rowsValue += ";" + grid.cells(rId,0).getValue() + "," + grid.cells(rId,1).getValue();
		}
		rowsValue = rowsValue.substring(1);
		
		var url = moduleUrl + "!save?P_type=" + type + "&P_reportId=" + reportId + "&P_rowsValue=" + rowsValue;
		dhtmlxAjax.get(url,function(loader){
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.status) {
		    	alert(getMessage("save_success"));
		    } else {
		    	alert(getMessage("save_failure"));
		    }
		});
	};
	this.add  = function() {
		var rowId = new Date().getTime();
		if (1 == type) {
			grid.addRow(rowId, ["-1", ""]);
		} else {
			grid.addRow(rowId, ["-1", "asc"]);
		}
	};
	this.remove  = function() {
		var rowIds = grid.getSelectedRowId();
    	if (null == rowIds) {
    		alert(getMessage("select_record"));
    		return;
    	}
		grid.deleteSelectedRows();
	};
	// grid init
	var grid = item.attachGrid();
	var header_1 = "默认值";
	var type_1 = "co";
	if (1 == type) {
		type_1 = "ed";
	}
	if (2 == type) {
		header_1 = "分组方式";
	} else if (3 == type) {
		header_1 = "排序方式";
	}
	var cfg = {
			format: {
				headers: ["<center>字段名称</center>","<center>" + header_1 + "</center>"],
				cols: ["columnId","value"],
				//userdata: [2, 3],
				colWidths: ["*", "60"],
				colTypes: ["co",type_1],
				colAligns: ["left","left"]
			}
		};
	var url = moduleUrl + "!search.json?P_orders=showOrder&Q_EQ_type=" + type + "&Q_EQ_reportId=" + reportId;
	
	var cCombo = grid.getCombo(0);
	cCombo.put("-1", "请选择字段");
	this.initColumns = function() {
		var url = AppActionURI.reportDataSource + "!columnsOfReport.json?Q_reportId=" + reportId;
		var data= loadJson(url);
		if (data) {
			//cCombo.addOption(data);
			for (var i = 0; i < data.length; i++) {
				var col = data[i];
				cCombo.put(col.value, col.text);
			}//*/
		}
	};
	_this.initColumns();
	if (1 != type) {
		var sCombo = grid.getCombo(1);
		sCombo.put("asc", "升序");
		sCombo.put("desc", "降序");
	}
	
	// init grid
	initGridWithoutPageable(grid, cfg, url);
	//grid.enableDragAndDrop(true);
	//把条件设置初始化标识置为TRUE
	if (1 == type) {
		that.bInitCond = true;
	} else if (2 == type) {
		that.bInitGroup = true;
	} else if (3 == type) {
		that.bInitSort = true;
	};
};
