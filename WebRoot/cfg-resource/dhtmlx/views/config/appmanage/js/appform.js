/**
 * 界面配置
 * @param win
 * @param componentVersionId
 * @returns {initAppFormWin}
 */
function initAppFormWin(win, componentVersionId, f_close/*function*/) {
	var _this = this;
	var dhxLayout = win.attachLayout("2U");
	dhxLayout.cont.obj._offsetTop = 1;
	dhxLayout.cont.obj._offsetLeft = 1;
	dhxLayout.cont.obj._offsetHeight = -2;
	dhxLayout.cont.obj._offsetWidth = -2;
	dhxLayout.setSizes();
	
	var wLayoutA = dhxLayout.cells("a");
	var wLayoutB = dhxLayout.cells("b");
	wLayoutA.hideHeader();
	wLayoutB.hideHeader();
	wLayoutB.setWidth(320);
	
	var pvtbar = wLayoutA.attachToolbar();
	pvtbar.setIconPath(TOOLBAR_IMAGE_PATH);
	pvtbar.addDiv("masterFormDiv", 92);
	var masterForm = new dhtmlXForm("masterFormDiv", getMasterFormJson());
	
    //初始化表单结构
	initFormMaster();
	
	var  subLayout = wLayoutB.attachLayout("2E");
	
	var gctbar = wLayoutB.attachToolbar(); //列表字段工具条
	gctbar.setIconPath(TOOLBAR_IMAGE_PATH);
	gctbar.addInputText("filterName",1,"字段名称(支持拼音)",107);
	gctbar.addSeparator("separator$01",6);
	gctbar.addButton("save", 7, "保存", "save.gif");
	gctbar.addSeparator("separator$02",10);
	gctbar.addButton("preview",11,"预览","preview.gif");
	gctbar.addSeparator("separator$03",12);
	gctbar.addButtonSelect("advance",13,"高级",[]);
	gctbar.addListOption("advance","addsubfield",14, "button","添加分栏符","new.gif");
	gctbar.addListOption("advance","addplaceholder",15, "button","添加占位符","new.gif");
	gctbar.addListOption("advance","delsubfield",16, "button","删除分栏符","delete.gif");
	gctbar.addListOption("advance","delplaceholder",17, "button","删除占位符","delete.gif");
	gctbar.addListOption("advance","newcolumn",18, "button","新增录入字段","new.gif");
	gctbar.addListOption("advance","addcolumn",19, "button","增设录入字段","new.gif");
	gctbar.addListOption("advance","clear", 20, "button", "清空配置", "delete.gif");
	gctbar.attachEvent("onClick", function(itemId) {
		saveElement();
		if ("save" == itemId) {
			save();
		} else if ("clear" == itemId) {
			clear();
		} else if ("preview" == itemId) {
			if (checkPercents()) {
				preViewForm();
			}
		} else if (itemId == "newcolumn") {
			newInputColumn(grid);
		} else if (itemId == "addcolumn") {
			addInputColumn(grid);
		} else if (itemId == "addsubfield") {
			addSubfield(grid);
		} else if (itemId == "delsubfield") {
			delSubfield(grid);
		} else if (itemId == "addplaceholder") {
			addPlaceholder(grid);
		} else if (itemId == "delplaceholder") {
			delPlaceholder(grid);
		}
	});
	gctbar.attachEvent("onEnter", function(itemId, value) {
		if ("filterName" == itemId) {
			searchInGrid(grid, value, 0);
		}
	});
	gctbar.attachEvent("onFocus", function(itemId) {
		if ("filterName" == itemId) {
			gctbar.setValue(itemId, "");
		}
	});
	gctbar.attachEvent("onBlur", function(itemId, value) {
		if ("filterName" == itemId) {
			if ("" == value) {
				gctbar.setValue(itemId, "字段名称(支持拼音)");
			}
		}
	});
	var subLayoutA = subLayout.cells("a");
	var subLayoutB = subLayout.cells("b");
	
	subLayoutA.hideHeader();
	subLayoutB.setHeight(210);
	subLayoutB.collapse();
	subLayoutB.setText("栏位配置");
	
	var grid = subLayoutA.attachGrid();	
	var rcombo = grid.getCombo(2);
	rcombo.put("1", "1列");
	rcombo.put("2", "2列");
	rcombo.put("3", "3列");
	rcombo.put("4", "4列");
	var gurl = AppActionURI.appFormElement + "!elements.json?P_tableId=" + app.tableId + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + app.menuId;
	var gcfg = {
		format: {
			headers: ["<center>字段名称</center>","<center>界面项</center>","<center>列数</center>"],
			cols: ["showName","formable","colspan"],
			userdata: ["columnName", "dataType", "codeTypeCode", "length", "required", "spacePercent", "readonly", "hidden", "inputType", "defaultValue", "kept", "validation", "tooltip", "columnId", "increase", "inherit", "dataTypeExtend", "pattern"],
			colWidths: ["160", "80","*"],
			colTypes: ["ro", "ch","coro"],
			colAligns: ["left", "center", "center"]
		},
		multselect: false
	};
	grid.enableDragAndDrop(true);
	initGridWithoutPageable(grid, gcfg, gurl);
	var lastCheckRowIdx = getLastSelectRowIdx();
	grid.attachEvent("onCheck", checkEvent);
	
	grid.attachEvent("onRowSelect", rowSelectEvent);

	grid.attachEvent("onDrag",function(sId,tId) {
		var sCellObj = grid.cells(sId, 1);
		var tCellObj = grid.cells(tId, 1);
		if ("0" == sCellObj.getValue()) return false;
		if ("0" == tCellObj.getValue()) return false;
		return true;
	});
	grid.attachEvent("onDrop", function (sId, tId, dId, sObj, tObj, sCol, tCol) {
		app.modified = true;
	});
	grid.attachEvent("onEditCell", function (stage, rId, cInd, nValue, oValue) {
		colspanModifyEvent(rId, cInd);
		if (nValue != oValue) app.modified = true;
		return true;
	});
	var form = subLayoutB.attachForm(getElementFormJson());

	/*form.attachEvent("onBeforeChange", function (itemId, value){
		if ("showName" === itemId && isNotEmpty(value) && (/[^,]/g).test(value)) {
			dhtmlx.message("显示名称不能包含英文逗号(,)！");
			return false;
		}
		return true;
	});*/
	
	form.attachEvent("onChange", function (itemId, value){
		if (!form.validate()) return false;
		var rowId = form.getItemValue("id");
		if ("" == rowId) return;
		if ("required" == itemId || "readonly" == itemId || "hidden" == itemId
				/*|| "textarea" == itemId*/) {
			value = form.getItemValue(itemId);
		}
		if ("kept" == itemId) {
			value = form.getItemValue(itemId);
		    if (value == 1 && form.getItemValue("increase") == 1) {
		        dhtmlx.alert("连续录入、继承、递增只能选择一个！");
		        form.setItemValue("increase", 0);
		    }
		    if (value == 1 && form.getItemValue("inherit") == 1) {
		        dhtmlx.alert("连续录入、继承、递增只能选择一个！");
		        form.setItemValue("inherit", 0);
		    }
		}
		if ("increase" == itemId) {
			value = form.getItemValue(itemId);
		    if (value == 1 && form.getItemValue("kept") == 1) {
		        dhtmlx.alert("连续录入、继承、递增只能选择一个！");
		        form.setItemValue("kept", 0);
		    }
		    if (value == 1 && form.getItemValue("inherit") == 1) {
		        dhtmlx.alert("连续录入、继承、递增只能选择一个！");
		        form.setItemValue("inherit", 0);
		    }
		}
		if ("inherit" == itemId) {
			value = form.getItemValue(itemId);
		    if (value == 1) {
		    	var columnId = grid.getUserData(rowId, "columnId");
		    	if (columnId != -1) {
		    	    var columnDefine = loadJson(AppActionURI.columnDefine + "/" + columnId + ".json");
		    	    if (isEmpty(columnDefine.columnLabel)) {
		    	        dhtmlx.message("该字段未设置字段标签，请设置！");
		    	        form.setItemValue("inherit", 0);
		    	        value = 0
		    	    }
		    	}
		    	if (value == 1 && form.getItemValue("kept") == 1) {
			        dhtmlx.alert("连续录入、继承、递增只能选择一个！");
			        form.setItemValue("kept", 0);
		    	}
			    if (value == 1 && form.getItemValue("increase") == 1) {
			        dhtmlx.alert("连续录入、继承、递增只能选择一个！");
			        form.setItemValue("increase", 0);
			    }
		    }
		}
		if ("showName" == itemId) {
			var columnId = grid.getUserData(rowId, "columnId");
			if ("-1" == columnId || "-2" == columnId) {
				grid.cells(rowId,0).setValue("<font color=\"blue\">" + value + "</font>");
			} else {
				grid.cells(rowId,0).setValue(value);
			}
		} 
		if ("validation" == itemId) {
			var dateType = grid.getUserData(rowId, "dateType");
			if ("d" != dateType) {
				if ("pattern" == value) {
					form.showItem("pattern");
				} else {
					form.hideItem("pattern");
				}
			}
		}
		else {
			grid.setUserData(rowId, itemId, value);
		}
    	//
    	app.modified = true;
	});//*/
	form.lock();
	
	preViewForm();
	
	/**
	 * 表单结构初始化
	 */
	function initFormMaster() {
		var url = AppActionURI.appForm + "!edit.json?tableId=" + app.tableId + "&componentVersionId=" + componentVersionId + "&menuId=" + app.menuId;
		var loader = loadJson(url);
		if (null != loader) {
			masterForm.setItemValue("colspan", loader.colspan);
			//masterForm.checkItem("type", jsonObj.type);			
			if ("1" == loader.border) masterForm.checkItem("border");
		}
	};
	function getMasterFormJson() {
		return [{type: "combo", label: "列数：", labelAlign:"right", name: "colspan", className: "dhx_toolbar_form", labelWidth: 40, width:60,
			options: [{value:1, text:"1列"},
	                  {value:2, text:"2列", selected: true},
	                  {value:3, text:"3列"},
	                  {value:4, text:"4列"}]},
	        {type: "newcolumn"},
	        {type: "hidden", name: "type", value: "0"},
	        //{type: "radio", name: "type", className: "dhx_toolbar_form", value: 0, position: "label-right", label: "弹出式", labelAlign:"left", labelWidth: 60, offsetLeft: 20},
	        //{type: "newcolumn"},
	        //{type: "radio", name: "type", className: "dhx_toolbar_form", value: 1, position: "label-right", label: "嵌入式", labelAlign:"left", labelWidth: 60, checked: true},
	        //{type: "newcolumn"},
	        {type: "checkbox", name: "border", className: "dhx_toolbar_form",  position: "label-right", label: "加粗", labelAlign:"left", labelWidth: 40, width: 60}];
	}
 	
	/**
	 * 界面项事件
	 * @param rowId
	 * @param colIndex
	 * @param state
	 */
	function checkEvent(rowId, colIndex, state) {
		if (1 == colIndex) {
			subLayoutB.collapse();
			resetForm();
			var idx = grid.getRowIndex(rowId);
			var tRowId = null;
			var sCellObj = grid.cells(rowId, 2);
			//var cCellObj = grid.cells(rowId, 3);
			if (-1 == lastCheckRowIdx) {
				tRowId = grid.getRowId(0);
			} else {
				tRowId = grid.getRowId(lastCheckRowIdx);
			}
			grid.clearSelection();
			grid.selectRowById(rowId);
			if (state) {
				//grid.setRowTextStyle(rowId,"color:red");
				sCellObj.setValue("1");
				sCellObj.setDisabled(false);
				//cCellObj.setDisabled(false);
				if (0 == idx) {
					lastCheckRowIdx++;
					return;
				}
				grid.moveRowTo(rowId, tRowId, "move");
				if (-1 == lastCheckRowIdx) {
					grid.moveRowUp(rowId);
				}
				lastCheckRowIdx++;
			} else {
				//grid.setRowTextStyle(rowId,"color:black");
				sCellObj.setValue("");
				sCellObj.setDisabled(true);
				//cCellObj.setChecked(false);
				//cCellObj.setDisabled(true);
				if (idx == lastCheckRowIdx) {
					lastCheckRowIdx--;
					return;
				}
				grid.moveRowTo(rowId, tRowId, "move");
				lastCheckRowIdx--;
			}
		}
	}
	/**
	 * 获取表单中最后一个栏位索引，并初始化编辑框
	 * @returns {Number}
	 */
	function getLastSelectRowIdx() {
		var last = -1, ttlcnt = grid.getRowsNum();
		for (var i = 0; i < ttlcnt; i++) {
			var rowId = grid.getRowId(i);
			if ("0" == grid.cells(rowId, 1).getValue()) {
				grid.cells(rowId, 2).setDisabled(true);
				//grid.cells(rowId, 3).setDisabled(true);
			} else {
				//grid.setRowTextStyle(rowId,"color:red");
				last = i;
			}
		}
		return last;
	}
	/**
	 * 重置界面配置
	 */
	function resetConfig() {
		grid.forEachRow(function(rowId) {
			if ("1" == grid.cells(rowId, 1).getValue()) {
				grid.cells(rowId, 1).setValue("0");
				grid.cells(rowId, 2).setValue("");
				grid.cells(rowId, 2).setDisabled(true);
			}
		});
		masterForm.setItemValue("colspan", 2);
		//masterForm.checkItem("type", 1);
		masterForm.uncheckItem("border");
		lastCheckRowIdx = -1;
		resetForm();
		//iJsForm.setItemValue("initJs", iNote);
		//cJsForm.setItemValue("changeJs", cNote);
		//bJsForm.setItemValue("beforeSaveJs", bNote);
		//aJsForm.setItemValue("afterSaveJs", aNote);
		preViewForm();
	}
	/**
	 * 防止失焦时，数据没保存
	 */
	function saveElement() {
		if (form.isLocked() || subLayoutB.isCollapsed()) return;
		var rowId = form.getItemValue("id");
		if ("" == rowId) return;
		form.forEachItem(function(id) {
			var type = form.getItemType(id);
			if ("id" != id && "showName" != id && ("input" == type || "combo" == type)) {
				var value = form.getItemValue(id);
				grid.setUserData(rowId, id, value);
			}
		});
	}
	/**
	 * 表单栏位配置表单
	 * @returns {Array}
	 */
	function getElementFormJson() {
        var opts = [];
		var opt_data = loadJson(AppActionURI.columnDefine + "!getRegex.json");
	    if (opt_data) {
	        for (var m = 0; m < opt_data.length; m++) {
	        	opts[m] = {
	                text : opt_data[m].name,
	                value : opt_data[m].value
	            };
	        }
	    }
		var formJson = [{
		    type : "settings",
		    position : "label-left",
		    labelWidth : 80,
		    inputWidth : 200,
		    labelAlign : "right",
		    offsetTop:"5"
		}, 
		{type: "hidden", name: "id"}, 
		{type: "input", label: "显示名称：",  name: "showName", validate: "validateShowName", tooltip: "显示名称不能包含英文逗号(,)"}, 
		{type: "input", label: "字段名称：",  name: "columnName", readonly: true}, 
		{type: "block", name: "percent_block", width: 300, list:[
			{type: "itemlabel", label: "占用比例：", labelWidth: 80, labelAlign:"right"},
			{type:"newcolumn"},
			{type: "radio", label: "整列", name: "spacePercent", value:"100", labelWidth: 40, labelAlign:"left", position:"label-right"},
			{type:"newcolumn"},
			{type: "radio", label: "1/4列", name: "spacePercent", value:"25", labelWidth: 40, labelAlign:"left", position:"label-right"},
			{type:"newcolumn"},
			{type: "radio", label: "3/4列", name: "spacePercent",value:"75", labelWidth: 40, labelAlign:"left", position:"label-right"},
		]},
		{type: "block", name: "validte_block", width: 300, list:[
			{type: "combo", label: "界面校验：",  name: "validation", tooltip:"可以自定义正则表达式来检验！",
				options:[
					{value: "", text: "请选择校验规则",selected:true},
				    {value: "integer", text: "整数"},
				    {value: "naturalnumber", text: "自然数"},
					{value: "number", text: "数字"},
					{value: "zhOrNumOrLett", text: "汉字、数字、英文"},
					{value: "email", text: "电子邮箱地址"},
					{value: "mobile", text: "手机"},
					{value: "idno", text: "身份证号码"},
					{value: "zipcode", text: "邮政编码"},
					{value: "pattern", text: "正则表达式"}
				]
			}
		]},
		{type: "combo", label: "正则表达式：", name: "pattern", options: opts},
		//{type:"newcolumn"},
		{type: "block", name: "dv_block", width: 300, list:[
	        {type: "input", label: "默认值：", name: "defaultValue", maxLength:25}
	        ]},
	    {type: "input", label: "提示信息：", name: "tooltip", maxLength:50},
		{type: "block", name: "check_block", width: 300, list:[
		    {type: "checkbox", label: "必输：", name: "required", width: 20},
		    //{type: "checkbox", label: "文本域：", name: "textarea", width: 20},
		    {type: "checkbox", label: "递增：", name: "increase", width: 20},
		    {type:"newcolumn"},
		    {type: "checkbox", label: "只读：", name: "readonly", width: 20},
		    {type: "checkbox", label: "连续录入：", name: "kept", width: 20},
		    {type:"newcolumn"},
		    {type: "checkbox", label: "隐藏：", name: "hidden", width: 20},
		    {type: "checkbox", label: "继承：", name: "inherit", width: 20}
		    //{type: "button", value: "保存", name: "detailsave", width: 60, offsetLeft: 40}
		    ]}
		];
		
		return formJson;
	}
	/**
	 * 重置配置表单
	 */
	function resetForm() {
		form.forEachItem(function(name) {
	        var type = form.getItemType(name);
	        if ((type == "hidden") || (type == "input") || (type == "combo") || (type == "calendar") || (type == "password")) {
	            form.setItemValue(name, "");
	        } else if (type == "checkbox"){
	        	form.uncheckItem(name);
	        }
	    });
	}
	
	/**
	 * 更改占用列数
	 */
	function colspanModifyEvent(rowId, colIndex) {
		//add
		var columnId = grid.getUserData(rowId, "columnId");
		if ("-1" == columnId || "-2" == columnId) {
			form.hideItem("percent_block");
		} else {
			form.showItem("percent_block");
		}
		var inputType = grid.getUserData(rowId, "inputType");
		var colsnum = grid.cells(rowId,2).getValue();
		if ("textarea" == inputType || "datepicker" == inputType || "checkbox" == inputType || "radio" == inputType || 1 != colsnum) {
			form.disableItem("spacePercent", "25");
			form.disableItem("spacePercent", "75");
			form.setItemValue("spacePercent", "100");
			grid.setUserData(rowId, "spacePercent", "100");
		} else {
			form.enableItem("spacePercent", "25");
			form.enableItem("spacePercent", "75");
		}
	}
	
	/**
	 * 校验百分比
	 */
	function checkPercents() {
		if (lastCheckRowIdx < 0) {
			dhtmlx.alert("请先进行界面字段配置，再预览！");
			return false;
		}
		var percentCount = 0;
		var percent = 0;
		for (var i = 0; i < lastCheckRowIdx + 1; i++) {
			percent = parseInt(grid.getUserData(grid.getRowId(i), "spacePercent"));
			if (percent != 100) {
				percentCount += percent;
				if (percentCount > 100){
					dhtmlx.alert(grid.cellByIndex(i, 0).getValue() + " 占用比例设置错误", grid.selectRow(i));
					return false;
				}
				if (percentCount == 99 || percentCount == 100)
					percentCount = 0;
			} else if (percentCount < 99 && percentCount != 0) {
				dhtmlx.alert(grid.cellByIndex(i, 0).getValue() + " 占用比例设置错误", grid.selectRow(i));
				return false;
			}
		}
		if (percentCount < 99 && percentCount != 0) {
			dhtmlx.alert(grid.cellByIndex(i-1, 0).getValue() + " 占用比例设置错误", grid.selectRow(i-1));
			return false;
		}
		return true;
	}
	
	/**
	 * 列表单击事件
	 * @param rowId
	 * @param colIndex
	 */
	function rowSelectEvent(rowId, colIndex) {
		saveElement();
		var formable = grid.cells(rowId,1).getValue();
		if ("0" == formable) {
			resetForm();
			if (!form.isLocked()) form.lock();
			subLayoutB.collapse();
			return;
		}
		if (form.isLocked()) form.unlock();
		subLayoutB.expand();
		var datatype = grid.getUserData(rowId, "dataType");
		var codeTypeCode = grid.getUserData(rowId, "codeTypeCode");
		var columnId = grid.getUserData(rowId, "columnId");
		var inputType = grid.getUserData(rowId, "inputType");
		var dvItem = {type: "input", label: "默认值：", name: "defaultValue", maxLength:25};
		var validation = {type: "combo", label: "界面校验：",  name: "validation", tooltip:"可以自定义正则表达式来检验！",
							options:[
								{value: "", text: "请选择校验规则",selected:true},
							    {value: "integer", text: "整数"},
							    {value: "naturalnumber", text: "自然数"},
								{value: "number", text: "数字"},
								{value: "zhOrNumOrLett", text: "汉字、数字、英文"},
								{value: "email", text: "电子邮箱地址"},
								{value: "mobile", text: "手机"},
								{value: "idno", text: "身份证号码"},
								{value: "zipcode", text: "邮政编码"},
								{value: "pattern", text: "正则表达式"}
							]
						};
		form.removeItem("validation");
		form.addItem("validte_block", validation);
		form.hideItem("pattern");
		colspanModifyEvent(rowId, colIndex);
		if ("-1" == columnId) {
			form.setItemLabel("showName", "分栏标题：");
			form.setReadonly("showName", false);
			form.hideItem("columnName");
			form.hideItem("tooltip");
			form.hideItem("validation");
			form.hideItem("defaultValue");
			form.hideItem("check_block");
		} else if ("-2" == columnId) {
			form.setItemLabel("showName", "名称：");
			form.hideItem("columnName");
			form.setReadonly("showName", true);
			form.setItemValue("showName", "占位符");
			form.hideItem("tooltip");
			form.hideItem("validation");
			form.hideItem("check_block");
			dvItem.label = "编码：";
			form.removeItem("defaultValue");
			form.addItem("dv_block", dvItem);
		} else {
			if (isNotEmpty(codeTypeCode)) {
				dvItem.type = "combo";
				form.removeItem("defaultValue");
				form.addItem("dv_block", dvItem);
				var opts = loadJson(contextPath + "/code/code!combobox.json?codeTypeCode=" + codeTypeCode);
				if ("AUTH_USER" === codeTypeCode) {
					opts.unshift({value: "_CURRENT_USER_", text: "当前人员"});
				} else if ("AUTH_DEPT" === codeTypeCode) {
					opts.unshift({value: "_CURRENT_DEPT_", text: "当前部门"});
				}
				opts.unshift({value: "", text: "请选择", selected: true});
				form.getCombo("defaultValue").addOption(opts);
			} else {
				if ("d" == datatype) { 
					dvItem.type = "combo";
					dvItem.options = [{value: "", text: "请选择", selected: true},{value: "_CURRENT_DATE_", text: "当前日期"}];
					form.removeItem("defaultValue");
					form.addItem("dv_block", dvItem);
					var validation = {type: "combo", label: "界面校验：",  name: "validation", tooltip:"可以自定义正则表达式来检验！",
							options:[
								{value: "", text: "请选择校验规则",selected:true},
							    {value: "max", text: "小于当前日期"},
							    {value: "min", text: "大于当前日期"}
							]
						};
					form.removeItem("validation");
					form.addItem("validte_block", validation);
				}
				form.removeItem("defaultValue");
				form.addItem("dv_block", dvItem);
			}
			form.setItemLabel("showName", "显示名称：");
			form.setReadonly("showName", false);
			form.showItem("columnName");
			form.showItem("check_block");
			form.showItem("tooltip");
//			if ("c" == datatype && ("textbox" == inputType || "textarea" == inputType)) {
//				//form.setItemLabel("tooltip", "提示信息：");
//				//form.setReadonly("tooltip", false);
//				//form.showItem("showName");
//				form.showItem("validation");
//				form.showItem("defaultValue");
//				form.showItem("check_block");
//			} else {
//				//form.setItemLabel("tooltip", "提示信息：");
//				//form.setReadonly("tooltip", false);
//				//form.showItem("showName");
//				form.hideItem("validation");
//				form.showItem("defaultValue");
//				form.showItem("check_block");
//			}
		}
		/*if ("c" == datatype) {
			form.enableItem("textarea");
		} else {
			form.disableItem("textarea");
		}*/
		var formData = {
				id: rowId,
				showName: grid.cells(rowId,0).getValue(),
				columnName: grid.getUserData(rowId,"columnName"),
				validation: grid.getUserData(rowId,"validation"),
				defaultValue: grid.getUserData(rowId,"defaultValue"),
				tooltip: grid.getUserData(rowId,"tooltip"),
				percent: grid.getUserData(rowId,"spacePercent"),
				//textarea: grid.getUserData(rowId,"textarea"),
				required: grid.getUserData(rowId,"required"),
				readonly: grid.getUserData(rowId,"readonly"),
				hidden: grid.getUserData(rowId,"hidden"),
				kept: grid.getUserData(rowId,"kept"),
				increase: grid.getUserData(rowId,"increase"),
				inherit: grid.getUserData(rowId,"inherit"),
				pattern: grid.getUserData(rowId,"pattern")
		};
		if ("-1" == columnId || "-2" == columnId) {
			formData.showName = formData.showName.replace(/<font color=\"blue\">|<\/font>/g, "");
		}
		form.setFormData(formData);
		if (form.getItemValue("validation") == "pattern") {
			form.showItem("pattern");
		}
	}
	
	/**
	 * 保存界面配置
	 */
	function save() {
		var cnt = lastCheckRowIdx + 1;
		var colspan = masterForm.getItemValue("colspan");
		if (0 == cnt) {
			dhtmlx.alert("请先进行界面字段配置，再保存！");
			return;
		}
		if (!checkPercents()) {
			return;
		}
		var rowsValue = "";
		for (var i = 0; i < cnt; i++) {
			//add
			var rowId = grid.getRowId(i);
			var showName = grid.cells(rowId,0).getValue();
			var gridColspan = grid.cells(rowId,2).getValue();
			if (gridColspan > colspan) {
				dhtmlx.alert(showName + "：占用列不能大于自定义显示列数（" + colspan + "）");
				grid.selectRow(i);
				return;
			}
			var columnId = grid.getUserData(rowId,"columnId");
			if ("-1" == columnId || "-2" == columnId) {
				showName = showName.replace(/<font color=\"blue\">|<\/font>/g, "");
			}
			// ["显示名称","字段ID","占用列","必须输入","只读","隐藏","文本域","默认值","连续录入","递增","继承"]
			rowsValue += ";" + showName + "," + columnId + ","
					+ grid.cells(rowId,2).getValue() + ","
					+ null2zero(grid.getUserData(rowId,"required")) + ","
					+ null2zero(grid.getUserData(rowId,"readonly")) + ","
					+ null2zero(grid.getUserData(rowId,"hidden")) + ","
					//+ null2empty(grid.getUserData(rowId,"textarea")) + ","
					+ null2empty(grid.getUserData(rowId,"defaultValue"))  + ","
					+ null2empty(grid.getUserData(rowId,"validation")) + ","
					+ null2empty(grid.getUserData(rowId,"tooltip")) + ","
					+ null2zero(grid.getUserData(rowId,"kept")) + ","
					+ null2zero(grid.getUserData(rowId,"increase")) + ","
					+ null2zero(grid.getUserData(rowId,"inherit")) + ","
					+ null2empty(grid.getUserData(rowId,"pattern")).replace(/,/g,"，") + ","
					+ null2zero(grid.getUserData(rowId,"spacePercent"));
		}
		rowsValue = rowsValue.substring(1);
		//var type    = masterForm.getCheckedValue("type");
		var border  = masterForm.getItemValue("border");
		if (null == border) {
			border = "0";
		}
		var url = AppActionURI.appForm + "!save.json";
		var params = "tableId=" + app.tableId + "&componentVersionId=" + componentVersionId 
		    + "&menuId=" + app.menuId + "&P_elementsValue=" + encodeURIComponent(rowsValue)
			+ "&colspan=" + colspan + "&border=" + border + "&type=0";
		dhtmlxAjax.post(url, params, function(loader) {
			var msg = eval("(" + loader.xmlDoc.responseText + ")");
			if (msg && msg.success) {
				dhtmlx.message(getMessage("operate_success"));
		    } else {
		    	dhtmlx.message(getMessage("operate_failure"));
		    }
			//
			app.modified = false;
		});//*/
	};
	/**
	 * 删除配置
	 */
	function clear() {
		dhtmlx.confirm({
			text: "确定要删除界面配置吗？",
			ok  : "确定",
			cancel: "取消",
			callback: function(flag) {
				if (!flag) return;
				var url = AppActionURI.appForm + "!clear.json?tableId=" + app.tableId + "&componentVersionId=" + componentVersionId + "&menuId=" + app.menuId;
				dhtmlxAjax.get(encodeURI(url),function(loader){
					var msg = eval("(" + loader.xmlDoc.responseText + ")");
					if (msg && msg.success == true) {
						resetConfig();
						dhtmlx.message(getMessage("operate_success"));
				    } else {
				    	dhtmlx.message(getMessage("operate_failure"));
				    }
					//
					app.modified = false;
				});
			}
		});
	}
	
	/**
	 * 表单预览
	 */
	function preViewForm () {
		//
		var subLayout = wLayoutA.attachLayout("1C");
		var fLayoutA = subLayout.cells("a");
		fLayoutA.hideHeader();
		//
		var labelWidth = 100;
		var inputWidth = 200;
		var formJson = [{
            type : "settings",
            position : "label-left",
            labelWidth : labelWidth,
            inputWidth : inputWidth,
            labelAlign : "right",
            offsetTop:"2"
        }];
		//var formcolspan = parseInt(colspanCombo.getSelectedValue());//rtbar.getValue("colspan");
		//var border = pvtbar.getValue("border");
		var formcolspan= parseInt(masterForm.getItemValue("colspan"));
		var border     = masterForm.getItemValue("border");
		var blockWidth = (labelWidth + inputWidth) * formcolspan + 40;
		var block = {type: "block", width: blockWidth, list:[]};
		var newcolumn = {type:"newcolumn"};
		var preIdx = 0;
		var curIdx = 0;
		var formBody = {type: "block", width: blockWidth, list:[]};
		var ttlNum = lastCheckRowIdx + 1;
		if (0 == ttlNum) return;
		var rowIdx = 0;
		var percentCount = 0;
		for (var rowIdx = 0; rowIdx < ttlNum; rowIdx++) {
			//headers: ["名称","占用列","必须输入","只读","隐藏","文本域","默认值","连续录入"],
			var rowId = grid.getRowId(rowIdx);
			var label     = grid.cells(rowId, 0).getValue();
    		var columnId  = grid.getUserData(rowId, "columnId");
    		var tooltip   = grid.getUserData(rowId, "tooltip");
    		var required  = grid.getUserData(rowId, "required");
    		var readonly  = grid.getUserData(rowId, "readonly");
    		var hidden    = grid.getUserData(rowId, "hidden");
    		var inputType = grid.getUserData(rowId, "inputType");
    		var value     = grid.getUserData(rowId, "defaultValue");
    		var name      = grid.getUserData(rowId, "columnName");
    		var datatype  = grid.getUserData(rowId, "dataType");
    		var maxLength = grid.getUserData(rowId, "length");
    		var validation= grid.getUserData(rowId, "validation");
    		var percent   = parseInt(grid.getUserData(rowId, "spacePercent"));
    		var type     = getFormItemType(hidden, inputType);
    		var dataTypeExtend = grid.getUserData(rowId, "dataTypeExtend");
    		var colspan  = parseInt(grid.cells(rowId, 2).getValue());
    		var onerow = {};
    		if ("-1" == columnId) {
    			label  = label.replace(/<font color=\"blue\">|<\/font>/g, "");
    			if (block.list.length > 0) {
        			formBody.list.push(block);
    			}
    			block = {type: "block", width: blockWidth, list:[]};
    			block.list.push({type: "subfield", label: label, width: blockWidth});
    			formBody.list.push(block);
    			block = {type: "block", width: blockWidth, list:[]};
    			curIdx = preIdx = 0;// reset to 0
    			continue;
    		} else if ("-2" == columnId) {
    			label  = label.replace(/<font color=\"blue\">|<\/font>/g, "");
    			if (colspan > formcolspan) {
        			dhtmlx.alert(label + "：占用列不能大于自定义显示列数（" + formcolspan + "）");
        			grid.selectRowById(rowId);
        			return;
        		}
    			onerow.type = "placeholder";
    			if (colspan > 1) {
        			onerow.width = (inputWidth * colspan + labelWidth * (colspan - 1));
        		}
        		curIdx += colspan;
    		} else {
        		var splitIdx = label.indexOf(" (");
        		if (splitIdx > 0) label = label.substring(0, splitIdx);
        		if (colspan > formcolspan) {
        			dhtmlx.alert(label+"：占用列不能大于自定义显示列数（" + formcolspan + "）");
        			grid.selectRowById(rowId);
        			return;
        		}
        		if ((percent == 100 || (percent != 100 && percentCount == 0) )) {
        			if ("1" != required) {
        				label += "：";
        			}
        		} else {
        			label = "";
        		}
        		if (percent != 100 && percentCount != 0) {
        			onerow.label = label;
        		} else {
        			onerow.label = (("1" == border) ? "<strong>" + label + "</strong>" : label);
        		}
        		onerow.name = name;
        		onerow.value = value;
        		if ("hidden" != type) {
        			onerow.required = ("1" == required ? true : false);
            		onerow.readonly = ("1" == readonly ? true : false);
            		onerow.maxLength= maxLength;
            		if ("" != validation) onerow.validate = validation;
            		if ("" != tooltip) onerow.tooltip = tooltip;
            		if ("textarea" == type) {
            			type = "input";
            			onerow.rows = 3;
            			//colspan = formcolspan;
            		} else if ("checkbox" === inputType) {
            			type = "checkboxlist";
            			onerow.url = contextPath + "/appmanage/show-module!page.json?E_frame_name=coral&E_model_name=code&id=" + grid.getUserData(rowId, "codeTypeCode");
            			//onerow.data = [{value: "0", text: "测试一"},{value: "1", text: "测试二"}];
            		} else if ("radio" === inputType) {
            			type = "radiolist";
            			onerow.url = contextPath + "/appmanage/show-module!page.json?E_frame_name=coral&E_model_name=code&id=" + grid.getUserData(rowId, "codeTypeCode");
            			//onerow.data = [{value: "0", text: "测试一"},{value: "1", text: "测试二"}];
            		} else if ("combo" == type) {
            			onerow.options = [{value: "", text: "请选择"}];
            		} else if ("calendar" == type) {
            			if ("t" == dataTypeExtend) {
            				onerow.dateFormat = "%Y-%m-%d %H:%i:%s";
                			onerow.readonly = true;
						} else if ("m" == dataTypeExtend) {
	            			onerow.dateFormat = "%Y-%m";
	            			onerow.readonly = true;
						} else {
							onerow.dateFormat = "%Y-%m-%d";
	            			onerow.readonly = true;
						}
            		}
            		if (percent == 100 || (percent != 100 && percentCount == 0)) {
            			curIdx += colspan;
            		}
            		if (percent != 100) {
            			onerow.width = (inputWidth * colspan * percent / 100 + labelWidth * (colspan - 1));
            			percentCount += percent;
            		} else {
            			onerow.width = (inputWidth * colspan + labelWidth * (colspan - 1));
            		}
            		if (percentCount == 99 || percentCount == 100) {
            			percentCount = 0;
            		}
        		}    		
        		onerow.type = type;
    		}
    		if (curIdx > formcolspan) {
    			formBody.list.push(block);
    			block = {type: "block", width: blockWidth, list:[]};
    			curIdx = colspan; preIdx = 0;// reset to 0
    		}
    		block.list.push(onerow);
//    		if (curIdx > preIdx) {
    			block.list.push(newcolumn);
//    		}
    		preIdx = curIdx;
    		// 
    		if (ttlNum == (1 + rowIdx)) {
    			formBody.list.push(block);
    		}
		}
		
		formJson.push(formBody);
		//formJson.push({type: "block", width: blockWidth, list:[{type:"button", name:"submit", value:"保存", width: 60}]});
		//var preViewForm = wLayoutA.attachForm(formJson);
		
		var ftbar = fLayoutA.attachToolbar();
		ftbar.setIconPath(TOOLBAR_IMAGE_PATH);
		ftbar.addButton("previewsave", 7, "验证", "checkout.gif");
		ftbar.addSeparator("previewseparator$02",10);
		var preViewForm = fLayoutA.attachForm(formJson);
		ftbar.attachEvent("onClick", function(itemId) {
			if ("previewsave" == itemId) {
				var url = APP_PATH + "/app-form!test.json";
				var checkResult = null;
				if (null != beforeSaveEvent) {
					checkResult = beforeSaveEvent(preViewForm);
				}
				if (null == checkResult || (true == checkResult.success)) {
					preViewForm.send(url, "post", function(loader, response) {
						if (null != afterSaveEvent) {
							afterSaveEvent(preViewForm, loader, response);
						}
					});
				} else if (false == checkResult.success) {
					dhtmlx.message(checkResult.message);
				}
			}
		});
		
	};
	
	/**
	 * 检查JS是否有效
	 * @param msg
	 * @returns {Boolean}
	 */
	function checkJs(msg) {
		var rowArr = msg.split("\n");
		for (var i = 0; i < rowArr.length; i++) {
			var row = rowArr[i].replace(/(^\s*)/g,"");
			if (row.indexOf("//") < 0 || row.indexOf("//") > 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 
	 * @param hidden
	 * @param inputType
	 * @returns {String}
	 */
	function getFormItemType (hidden, inputType) {
		if (hidden && "1" == hidden) return "hidden";
		if ("textarea" == inputType) return "textarea";
		if ("datepicker" == inputType) return "calendar";
		if ("textbox" == inputType) return "input";
		if ("checkbox" == inputType) return "checkboxlist";
		if ("radio" == inputType) return "radiolist";
		return "combo";
	};
	/*********************** 添加字段开始 *************************/
	/**
	 * 添加录入字段
	 * @param targetGrid
	 */
	function addInputColumn(targetGrid) {
		var _this = this; 
		var h = 500;
		if (h > document.body.clientHeight) {
			h = document.body.clientHeight;
		}
		var subWin = createDhxWindow({id:"win$form$add$input$column",title:"添加字段",width:240,height:h});
		var subWinTtbar = subWin.attachToolbar();
		subWinTtbar.addInput("subwin$search",0,"字段检索(支持拼音)",160);
		subWinTtbar.attachEvent("onEnter", function(itemId, value) {
			if ("subwin$search" == itemId) {
				searchInGrid(cGrid, value, 0);
			}
		});
		subWinTtbar.attachEvent("onFocus", function(itemId) {
			if ("subwin$search" == itemId) {
				subWinTtbar.setValue(itemId, "");
			}
		});
		var subWinSbar = subWin.attachStatusBar();
		var subWinBtbar = new dhtmlXToolbarObject(subWinSbar.id);
		subWinBtbar.setIconPath(TOOLBAR_IMAGE_PATH);
		subWinBtbar.addButton("subwin$bottom$close", 5, "关闭", "close.gif");
		subWinBtbar.setAlign("right");
		subWinBtbar.attachEvent("onClick", function(itemId) {
			if ("subwin$bottom$close" == itemId) {
				add2grid();
				subWin.close();
			} 
		});
		subWin.button("close").attachEvent("onClick", function() {
			add2grid();
			subWin.close();
		});
		var cGrid = subWin.attachGrid();
		var cCfg = {
				format: {
					headers: ["字段名称","录入项"],
					cols: ["showName","inputable"],
					userdata: ["columnName","length","dataType","codeTypeCode"],
					colWidths: ["120","60"],
					colTypes: ["ro","ch"],
					colAligns: ["left","center"]
				}
			};
		var cUrl = AppActionURI.columnDefine + "!search.json?Q_EQ_tableId=" + app.tableId + 
				"&Q_EQ_inputable=0" + 
				"&F_in=id,showName,inputable,columnName,length,dataType,codeTypeCode";
		initGridWithoutColumnsAndPageable(cGrid, cCfg, cUrl);
		
		cGrid.attachEvent("onCheckbox", function(rId,cInd,state){
			updateInputable(rId,cInd,state);
		});
		// 更新录入字段
		function updateInputable(rId,cInd,state) {
			var url = AppActionURI.columnDefine + "!getCDByAlltable?id=" + rId + "&columName=inputable&value=" + (state ? 1 : 0);
			dhtmlxAjax.get(url, function(loader){
				var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
				if (!jsonObj.status) {
					cGrid.cells(rId, cInd).setValue((state ? 0 : 1));
				}
			});
		};
		// 向列表中添加录入项字段
		function add2grid() {
			cGrid.forEachRow(function(rowId) {
				var inputable = cGrid.cells(rowId, 1).getValue();
				if ("1" == inputable) {
					var label = cGrid.cells(rowId, 0).getValue();
					var name  = cGrid.getUserData(rowId, "columnName");
					var length  = cGrid.getUserData(rowId, "length");
					var dataType  = cGrid.getUserData(rowId, "dataType");
					var codeTypeCode  = cGrid.getUserData(rowId, "codeTypeCode");
					targetGrid.deleteRow(rowId);
					targetGrid.addRow(rowId,[label + " (" + name.toUpperCase() + ")", "0", "", "0"], (lastCheckRowIdx + 1));
					targetGrid.setUserData(rowId, "columnName",name);
					targetGrid.setUserData(rowId, "dataType",dataType);
					targetGrid.setUserData(rowId, "length",length);
					targetGrid.setUserData(rowId, "codeTypeCode",codeTypeCode);

					targetGrid.setUserData(rowId, "required", "0");
					targetGrid.setUserData(rowId, "readonly", "0");
					targetGrid.setUserData(rowId, "textarea", "0");
					targetGrid.setUserData(rowId, "defaultValue", "");
					targetGrid.setUserData(rowId, "kept", "0");
					targetGrid.setUserData(rowId, "increase", "0");
					targetGrid.setUserData(rowId, "inherit", "0");
					targetGrid.setUserData(rowId, "validation", "");
					targetGrid.setUserData(rowId, "tooltip", "");
					targetGrid.setUserData(rowId, "columnId", rowId);
					targetGrid.cells(rowId, 2).setDisabled(true);
					//targetGrid.cells(rowId, 3).setDisabled(true);
				}
			});
		};
	};
	
	/**
	 * 新增录入字段
	 * @param targetGrid
	 */
	function newInputColumn(targetGrid) {
		var _this = this;

        var h = document.body.clientHeight,
        height = 600;
        if (height > h) {
        	height = h;
		}
		var subWin = createDhxWindow({id:"win$form$new$input$column",title:"新增字段",width:820,height:height});
		newColumnForm = subWin.attachForm(initFormFormat(getFormItemJson()));
		var subWinSbar = subWin.attachStatusBar();
		var subWinBtbar = new dhtmlXToolbarObject(subWinSbar.id);
		subWinBtbar.setIconPath(TOOLBAR_IMAGE_PATH);
		subWinBtbar.addButton("new$subwin$bottom$save", 0, "保存", "save.gif");
		subWinBtbar.addSeparator("new$subwin$bottom$septr$01", 2);
		subWinBtbar.addButton("new$subwin$bottom$close", 5, "关闭", "close.gif");
		subWinBtbar.setAlign("right");
		subWinBtbar.attachEvent("onClick", function(itemId) {
			if ("new$subwin$bottom$close" == itemId) {
				add2grid();
				subWin.close();
			} else if ("new$subwin$bottom$save" == itemId) {
				submit();
			}
		});
		subWin.button("close").attachEvent("onClick", function() {
			add2grid();
			subWin.close();
		});

		var columns_data;
		var loadopts = loadJson(AppActionURI.columnLabel + "!getUnBindedLabel.json?tableId=" + app.tableId);
		columns_data = new Array({value:"",text:"请选择"});
		for (var i = 0; i < loadopts.length; i++) {
			columns_data.push({value: loadopts[i][0], text: loadopts[i][1]});
		}
		newColumnForm.setItemValue("tableId",app.tableId);
		var cp_combo = newColumnForm.getCombo("columnLabel");
		cp_combo.addOption(columns_data);
		cp_combo.selectOption(0);
    	dataExist = 0;
		formChange();
		
		// 保存自定义字段
		function submit() {
            var columnName = newColumnForm.getItemValue("columnName").toUpperCase();
            var showName = newColumnForm.getItemValue("showName").toUpperCase();
            // 判断字段标识的合法性
            if (!columnName.match(/^[a-zA-Z]+\w*$/)){
            	dhtmlx.alert('字段名称以字母开始，由字母、数字或下划线组成！');
                return;
            }
			//判断输入的字段名称是否为关键字            
			if(DatabaseKeyWord.isKeyWord(columnName)){
				dhtmlx.alert('字段名称是关键字，请重新输入!');
				return;
			};
            var CHE_showName_Url = AppActionURI.columnDefine + "!checkUnique.json?Q_EQ_showName=" + showName + "&Q_EQ_tableId=" + app.tableId;
            newColumnForm.send(CHE_showName_Url, "post", function(loader, response) {
                var cheJsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (cheJsonObj.success != true) {
                	dhtmlx.message(getMessage("form_field_exist", "显示名称"));
                    return;
                } else {
		            var CHE_Name_Url = AppActionURI.columnDefine + "!checkUnique.json?Q_EQ_columnName=" + columnName + "&Q_EQ_tableId=" + app.tableId;
		            newColumnForm.send(CHE_Name_Url, "post", function(loader, response) {
		                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
		                if (jsonObj.success != true) {
		                	dhtmlx.message(getMessage("form_field_exist", "字段名称"));
		                } else {
		                    newColumnForm.send(AppActionURI.columnDefine + ".json", "post", function(loader, response) {
		                    	jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			            		if (jsonObj.id && "" != jsonObj.id) {
			            			newColumnForm.setItemValue("id",jsonObj.id);
			            			newColumnForm.setItemValue("created",jsonObj.created);
			            			dhtmlx.message(getMessage("save_success"));
			            		} else {
			            			dhtmlx.message(getMessage("save_failure"));
			            		}
		                    });
		                }
		            });
                }
            });
	    };
		// 把新增的字段加到列表中
		function add2grid () {
			var id = newColumnForm.getItemValue("id");
			if (null == id || "" == id) return;
			var inputable = newColumnForm.getItemValue("inputable");
			if ("1" != inputable) return;
			var label = newColumnForm.getItemValue("showName");
			var name = newColumnForm.getItemValue("columnName");
			var length = newColumnForm.getItemValue("length");
			var datatype = newColumnForm.getItemValue("dataType");
			var codeTypeCode = newColumnForm.getItemValue("codeTypeCode");
			targetGrid.deleteRow(id);
			targetGrid.addRow(id,[label + " (" + name.toUpperCase() + ")","0","","0"], (lastCheckRowIdx + 1));
			//["columnName", "dataType", "codeTypeCode", "length", "required", "readonly", "textarea", "defaultValue", "kept", "increase", "inherit", "validation", "tooltip"],
			targetGrid.setUserData(id,"columnName", name);
			targetGrid.setUserData(id,"dataType", datatype);
			targetGrid.setUserData(id,"length", length);
			targetGrid.setUserData(id,"codeTypeCode", codeTypeCode);

			targetGrid.setUserData(id,"required", "0");
			targetGrid.setUserData(id,"readonly", "0");
			targetGrid.setUserData(id,"textarea", "0");
			targetGrid.setUserData(id,"defaultValue", "");
			targetGrid.setUserData(id,"kept", "0");
			targetGrid.setUserData(id,"increase", "0");
			targetGrid.setUserData(id,"inherit", "0");
			targetGrid.setUserData(id,"validation", "");
			targetGrid.setUserData(id,"tooltip", "");
			targetGrid.setUserData(id,"columnId", id);
			targetGrid.cells(id, 2).setDisabled(true);
			//targetGrid.cells(id, 3).setDisabled(true);
		};
	};	
	/**
	 * 新增表字段表单JSON
	 * @returns {Array}
	 */
	var url = contextPath +"/code/code-type!getCodeTypeSelect.json";
	var opts = loadJson(url);
	opts.unshift({value: "", text: "请选择编码",selected:true});
	function getFormItemJson() {
		var formJson = {
				format: [
							{type: "hidden", name: "_method"},
							{type: "hidden", name: "id"},
							{type: "hidden", name: "created",value:"0"}, 
							{type: "hidden", name: "columnType",value:"0"}, // 0-业务字段  1-基础字段 2-系统字段
							{type: "hidden", name: "showOrder"},
							{type: "hidden", name: "tableId"},
							{type:"fieldset", label:"系统项", width:750, offsetLeft:"10", list:[
								{type: "block",  name: "columnInfo", width:750, list:[
										{type: "input", label: "显示名称:", name: "showName", required:true, width: "230", maxLength:25, tooltip:"请输入显示名称!"},
										{type: "combo", label: "数据类型:&nbsp;&nbsp;&nbsp;",  name: "dataType",readonly:"true",width: "230",
											 options:[
													    {value: "c", text: "字符型",selected:true},
														{value: "d", text: "日期型"},
														{value: "n", text: "数字型"}
													]
										},
										{type: "combo", label: "字段标签:&nbsp;&nbsp;&nbsp;", name: "columnLabel", maxLength:25, width: "230"},
										{type:"newcolumn"},
										{type: "input", label: "字段名称:", name: "columnName", required:true, width: "230", offsetLeft:"50", maxLength:30, tooltip:"请输入字段名称，以字母开始，由字母、数字、下划线组成!"},
										{type: "input", label: "长度:", name: "length", value: 50, required:true,validate:"ValidNumeric",width: "230", offsetLeft:"50", maxLength:4},
										{type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "defaultValue",width: "230", maxLength:20,maxLength:25,offsetLeft:"50"}
								]}
							]},
							{type:"fieldset", label:"界面项", width:750, offsetLeft:"10", list:[
								{type: "block", width:750, list:[
										{type: "itemlabel", label: "组件类型:&nbsp;&nbsp;&nbsp;",  labelWidth: 100},
										{type:"newcolumn"},
										{type: "radio", label: "文本框", name: "inputType", value: "textbox", checked: true, position:"label-right", labelAlign:"left",labelWidth: 70},
//											{type:"newcolumn"},
//											{type: "radio", label: "文本框(按钮)", name: "inputType", value: "textboxbutton", position:"label-right", labelAlign:"left",labelWidth: 100},
										{type:"newcolumn"},
										{type: "radio", label: "日期框", name: "inputType", value: "datepicker", position:"label-right", labelAlign:"left",labelWidth: 70},
										{type:"newcolumn"},
										{type: "radio", label: "文本域", name: "inputType", value: "textarea", position:"label-right", labelAlign:"left",labelWidth: 70},
										{type:"newcolumn"},
										{type: "radio", label: "下拉框", name: "inputType", value: "combobox", position:"label-right", labelAlign:"left",labelWidth: 70},
										{type:"newcolumn"},
										{type: "radio", label: "下拉列表", name: "inputType", value: "combogrid", position:"label-right", labelAlign:"left",labelWidth: 70},
										{type:"newcolumn"},
										{type: "radio", label: "树组件", name: "inputType", value: "combotree", position:"label-right", labelAlign:"left",labelWidth: 70},
										{type:"newcolumn"},	
										{type: "radio", label: "复选框", name: "inputType", value: "checkbox", position:"label-right", labelAlign:"left",labelWidth: 70},
										{type:"newcolumn"},
										{type: "radio", label: "单选框", name: "inputType", value: "radio", position:"label-right", labelAlign:"left",labelWidth: 70}
								]},	
								{type: "block", name: "columnTypeExpend", width:750, list:[
								   		{type: "combo", label: " 样式类型:&nbsp;&nbsp;&nbsp;", name: "inputOption", maxLength:25, readonly: true, width: "230",
								   			options:[
	   										         	{value: "textbox", text: "文本框",selected:true},
	   										         	{value: "textboxbutton", text: "文本框+按钮"},
	   										         	{value: "textboxlabel", text: "文本框+标签"}
	   										         ]},
										{type: "combo", label: " 编码:&nbsp;&nbsp;&nbsp;", name: "codeTypeCode", maxLength:25, width: "230",options:opts},
										{type:"newcolumn"},
										{type: "combo", label: " 扩展属性:&nbsp;&nbsp;&nbsp;", name: "dataTypeExtend", maxLength:25, readonly: true, width: "230", offsetLeft:"50",options:[]},
										{type: "input", label: "标签值:&nbsp;&nbsp;&nbsp;", name: "label", maxLength:10, width: "230",offsetLeft:"50"},
										{type: "input", label: "精度:&nbsp;&nbsp;&nbsp;", validate:"ValidNumeric", name: "precision", maxLength:1, width: "230",offsetLeft:"50"}
								]},
							]}
						],
						settings: {labelWidth: 100, inputWidth: 120}
					};
			formJson.format.splice(3, 1);
			formJson.format.push(
					{type:"fieldset", label:"其他", width:750, offsetLeft:"10", list:[
						{type: "block", width:750, list:[
								{type: "itemlabel", label: "其他项:&nbsp;&nbsp;&nbsp;", width: "230"},
								{type:"newcolumn"},
								{type: "checkbox", label: "录入项", name: "inputable", checked: true, position:"label-right", labelAlign:"left",labelWidth: 75, readonly:"true"},
								{type:"newcolumn"},
								{type: "checkbox", label: "修改项", name: "updateable", checked: true, position:"label-right", labelAlign:"left",labelWidth: 75},
								{type:"newcolumn"},
								{type: "checkbox", label: "检索项", name: "searchable", checked: true, position:"label-right", labelAlign:"left",labelWidth: 75},
								{type:"newcolumn"},
								{type: "checkbox", label: "列表项", name: "listable", checked: true, position:"label-right", labelAlign:"left",labelWidth: 75},
								{type:"newcolumn"},
								{type: "checkbox", label: "排序项", name: "sortable", checked: true, position:"label-right", labelAlign:"left",labelWidth: 75},
								{type:"newcolumn"},
								{type: "checkbox", label: "一体化检索项", name: "phraseable", checked: true, position:"label-right", labelAlign:"left",labelWidth: 100}
						]},
						{type: "block", width:750, list:[
 								{type: "combo", label: "对齐方式:&nbsp;&nbsp;&nbsp;",  name: "align",readonly:"true",width: "230",
 										options:[
 												    {value: "left", text: "左对齐"},
 													{value: "center", text: "居中",selected:true},
 													{value: "right", text: "右对齐"}
 												]
 								},
 								{type: "combo", label: "查询方式:&nbsp;&nbsp;&nbsp;",  name: "filterType",readonly:"true",width: "230",
 									 options:[
 										 		{value: "LIKE", text: "包含",selected:true},
 											    {value: "EQ", text: "等于"},
 												{value: "GT", text: "大于"},
 												{value: "GTE", text: "大于等于"},
 												{value: "LT", text: "小于"},
 												{value: "LTE", text: "小于等于"},
 												{value: "NOT", text: "不等于"},
 												{value: "BT", text: "介于"},
 												{value: "NLL", text: "为空"}
 											]
 								},
 								{type: "newcolumn"},
 								{type: "input", label: "列表宽度:&nbsp;&nbsp;&nbsp;", name: "width", value: 80, offsetLeft:"50",validate:"ValidNumeric",width:"230",maxLength:4}
 						]},
						{type: "block", name: "columnType", width:750, list:[
      							{type: "itemlabel", label: "字段类型:&nbsp;&nbsp;&nbsp;", width: "230"},
     							{type:"newcolumn"},
     							{type: "radio", label: "业务字段", name: "columnType", value:"0", checked: true, labelWidth: 100,position:"label-right", labelAlign:"left"},
     							{type:"newcolumn"},
     							{type: "radio", label: "系统字段", name: "columnType", value:"2", labelWidth: 100,position:"label-right", labelAlign:"left", offsetLeft:"20"}
     					]},
     					{type: "block", width:750, list:[
               					{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "remark",rows:"3",inputWidth:"610",maxLength:125 }
               			]}
					]});	
		return formJson;
	}
	
	function formChange() {
		var dataType = newColumnForm.getItemValue("dataType");
		var defaultValue = newColumnForm.getItemValue("defaultValue");
		var dataTypeExtend = newColumnForm.getItemValue("dataTypeExtend");
		var inputType = newColumnForm.getCheckedValue("inputType");
		var inputOption = newColumnForm.getItemValue("inputOption");
		var comboItem = {type: "combo", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "defaultValue",width: "230",maxLength:25,offsetLeft:"50"};
		var labelItem = {type: "input", label: "标签值:&nbsp;&nbsp;&nbsp;", name: "dataTypeExtend", maxLength:10, width: "230",offsetLeft:"50"};
		var numberItem = {type: "input", label: "精度:&nbsp;&nbsp;&nbsp;", validate:"ValidNumeric", name: "dataTypeExtend", maxLength:1, width: "230",offsetLeft:"50"};
		var dateItem = {type: "combo", label: " 存入格式:&nbsp;&nbsp;&nbsp;", name: "dataTypeExtend", maxLength:25, readonly: true, width: "230", offsetLeft:"50",options:[]};
		var codeTypeCode = newColumnForm.getItemValue("codeTypeCode");
		newColumnForm.hideItem("dataTypeExtend");
		newColumnForm.hideItem("label");
		newColumnForm.hideItem("precision");
			if ("d" === dataType) {
				var dataTypeExtend = newColumnForm.getItemValue("dataTypeExtend");
				newColumnForm.removeItem("dataTypeExtend");
				newColumnForm.addItem("columnTypeExpend", dateItem);
				initDateFormatCombo(newColumnForm, "dataTypeExtend");
				if (dataTypeExtend) newColumnForm.setItemValue("dataTypeExtend",dataTypeExtend);
			} else if ("n" === dataType && "floating" === inputOption) {
				newColumnForm.removeItem("dataTypeExtend");
				newColumnForm.addItem("columnTypeExpend", numberItem);
				newColumnForm.setItemValue("dataTypeExtend", dataTypeExtend);
			} else if ("c" === dataType && "textboxlabel" === inputOption) {
				newColumnForm.removeItem("dataTypeExtend");
				newColumnForm.addItem("columnTypeExpend", labelItem);
				newColumnForm.setItemValue("dataTypeExtend", dataTypeExtend);
			}
		if (isNotEmpty(codeTypeCode)) {
			newColumnForm.removeItem("defaultValue");
			newColumnForm.addItem("columnInfo", comboItem);
			var combo = newColumnForm.getCombo("defaultValue");
			var opts  = loadJson(contextPath + "/code/code!combobox.json?codeTypeCode=" + codeTypeCode);
			opts.unshift({value: "", text: "请选择", selected: true});
			combo.addOption(opts);
		}
		newColumnForm.setItemValue("defaultValue", defaultValue);
		// 
		changeInputType(newColumnForm, dataType);
		newColumnForm.checkItem("inputType", inputType);
		// 
		changeInterface(newColumnForm, inputType, dataType);
		
		newColumnForm.attachEvent("onChange", function (id, value){
			if ("dataType" == id) {
				var comboItem = {type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "defaultValue",width: "230",maxLength:25,offsetLeft:"50"};
				var combo = newColumnForm.getCombo("codeTypeCode");
				if ("d" == value) {
					newColumnForm.checkItem("inputType", "datepicker");
					newColumnForm.setItemValue("inputOption", null);
					changeInterface(newColumnForm, "datepicker", value);
					var dataTypeExtend = newColumnForm.getItemValue("dataTypeExtend");
					newColumnForm.removeItem("dataTypeExtend");
					newColumnForm.addItem("columnTypeExpend", dateItem);
					initDateFormatCombo(newColumnForm, "dataTypeExtend");
					if (dataTypeExtend) newColumnForm.setItemValue("dataTypeExtend",dataTypeExtend);
				}else if ("n" == value) {
					var length = newColumnForm.getItemValue("length");
					var dataTypeExtend = newColumnForm.getItemValue("dataTypeExtend");
					if ("" == length || length > 18) {
						newColumnForm.setItemValue("length", 18);
					}
					newColumnForm.checkItem("inputType", "textbox");
					changeInterface(newColumnForm, "textbox", value);
				} else {
					newColumnForm.checkItem("inputType", "textbox");
					changeInterface(newColumnForm, "textbox", value);
					newColumnForm.hideItem("dataTypeExtend");
				}
				newColumnForm.removeItem("defaultValue");
				newColumnForm.addItem("columnInfo", comboItem);
				combo.setComboValue("");
				//newColumnForm.disableItem("codeTypeCode");
				changeInputType(newColumnForm, value);
			} else if ("length" == id) {
				var dt = newColumnForm.getItemValue("dataType");
				if ("n" == dt && value > 18) {
					dhtmlx.message("数字类型的长度不能大于18！");
					newColumnForm.setItemValue("length", 18);
				} else if (value > 4000) {
					dhtmlx.message("数据类型的长度不能大于4000！");
					newColumnForm.setItemValue("length", 4000);
				}
				if (dataExist == 1) {
					if (value < oldLength) {
						dhtmlx.message("该字段已保存数据，长度不能小于原字段长度！");
						newColumnForm.setItemValue("length", oldLength);
					}
				}
			} else if ("codeTypeCode" == id) {
				var combo = null;
				if (newColumnForm.getItemType("defaultValue") === "combo") {
					combo = newColumnForm.getCombo("defaultValue");
					combo.clearAll();
				} else {
					newColumnForm.setItemValue("defaultValue", "");
				}
				//console.log("codeTypeCode: " + value);
				if (isEmpty(value)) {
//					controlInputType(newColumnForm, ["text", "textarea"], "enable", "text");
//					return;
				} else if (newColumnForm.getItemType("defaultValue") !== "combo") {
					var comboItem = {type: "combo", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "defaultValue",width: "230",maxLength:25,offsetLeft:"50"};
					newColumnForm.removeItem("defaultValue");
					newColumnForm.addItem("columnInfo", comboItem);
				}
				//controlInputType(newColumnForm, ["datepicker"], "hide", "textbox");
				
				var opts  = loadJson(contextPath + "/code/code!combobox.json?codeTypeCode=" + value);
				opts.unshift({value: "", text: "请选择", selected: true});
				combo = newColumnForm.getCombo("defaultValue");
				combo.addOption(opts);
				combo.selectOption(0);
			} else if ("dataTypeExtend" == id && newColumnForm.getItemValue("dataType") == "n") {
				if (value > 10) {
					dhtmlx.message("精度不能大于10！");
					newColumnForm.setItemValue("dataTypeExtend", 10);
				}
			} else if ("inputType" == id) {
				newColumnForm.setItemValue("inputOption", null);
				newColumnForm.setItemValue("dataTypeExtend", null);
				newColumnForm.setItemValue("label", null);
				newColumnForm.setItemValue("precision", null);
				newColumnForm.hideItem("label");
				newColumnForm.hideItem("precision");
				newColumnForm.getCombo("codeTypeCode").selectOption(0);
				var defaultItem = {type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "defaultValue",width: "230", maxLength:20,maxLength:25,offsetLeft:"50"};
				newColumnForm.removeItem("defaultValue");
				newColumnForm.addItem("columnInfo", defaultItem);
				changeInterface(newColumnForm, value, newColumnForm.getItemValue("dataType"));
			} else if ("columnLabel" == id) {
				var url_ = AppActionURI.columnLabel + "!search.json?F_in=codeTypeCode&Q_EQ_code=" + value;
				var datas = loadJson(url_).data[0];
				if (datas.codeTypeCode != null) {
					var combo = newColumnForm.getCombo("codeTypeCode");
					combo.selectOption(combo.getIndexByValue(datas.codeTypeCode));
				} else {
					combo.selectOption(0);
				}
			} else if ("inputOption" == id) {
				if (value == "textboxlabel") {
					newColumnForm.removeItem("dataTypeExtend");
					newColumnForm.addItem("columnTypeExpend", labelItem);
					newColumnForm.showItem("dataTypeExtend");
					newColumnForm.hideItem("label");
					newColumnForm.hideItem("precision");
				} else if (value == "floating") {
					newColumnForm.removeItem("dataTypeExtend");
					newColumnForm.addItem("columnTypeExpend", numberItem);
					newColumnForm.showItem("dataTypeExtend");
					newColumnForm.hideItem("label");
					newColumnForm.hideItem("precision");
				} else if (value == "integerlabel") {
					newColumnForm.hideItem("dataTypeExtend");
					newColumnForm.showItem("label");
					newColumnForm.hideItem("precision");
				} else if (value == "floatinglabel") {
					newColumnForm.hideItem("dataTypeExtend");
					newColumnForm.showItem("label");
					newColumnForm.showItem("precision");
				} else if (newColumnForm.getItemValue("dataType") == "d") {
					newColumnForm.showItem("dataTypeExtend");
					newColumnForm.hideItem("label");
					newColumnForm.hideItem("precision");
				} else {
					newColumnForm.hideItem("dataTypeExtend");
					newColumnForm.hideItem("label");
					newColumnForm.hideItem("precision");
				}
			}
		});
	};
	
	function controlInputType(form, controlValues, op/*show or hide*/, defaultValue) {
		var v = ["textbox", "datepicker", "textarea", "combobox", "combogrid", "combotree", "radio", "checkbox"],
		    i = 0,
		    allProp = "showItem", toProp  = "hideItem";
		if ("show" === op) {
			allProp = "hideItem"; toProp  = "showItem";
		}
		for (i = 0; i < v.length; i++) form[allProp]("inputType", v[i]);
		if (!controlValues || 0 === controlValues.length) return;
		for (i = 0; i < controlValues.length; i++) {
			form[toProp]("inputType", controlValues[i]);
		}
		if (!defaultValue && "show" === op) defaultValue = controlValues[0];
		if (defaultValue) form.checkItem("inputType", defaultValue);
	}
	
	function changeInputType(form, value) {
		if ("d" === value) {
			controlInputType(form, ["datepicker"], "show");
		} else if ("n" === value) {
			controlInputType(form, ["datepicker"], "hide", "textbox");
		} else {
			controlInputType(form, ["datepicker"], "hide", "textbox");
		}
	}
	
	function changeInterface(newColumnForm, inputType, dataType) {
		var comboItem = {type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "defaultValue",width: "230",maxLength:25,offsetLeft:"50"};
		var labelItem = {type: "input", label: "标签值:&nbsp;&nbsp;&nbsp;", name: "dataTypeExtend", maxLength:10, width: "230",offsetLeft:"50"};
		var numberItem = {type: "input", label: "精度:&nbsp;&nbsp;&nbsp;", validate:"ValidNumeric", name: "dataTypeExtend", maxLength:1, width: "230",offsetLeft:"50"};
		var rowHeight = {type: "input", label: "高度:&nbsp;&nbsp;&nbsp;", validate:"ValidNumeric", name: "dataTypeExtend", maxLength:5, width: "230"};
		var dateItem = {type: "combo", label: " 存入格式:&nbsp;&nbsp;&nbsp;", name: "dataTypeExtend", maxLength:25, readonly: true, width: "230", offsetLeft:"50",options:[]};
		if ("textbox" === inputType) {
			if ("c" === dataType) {
				var combo = newColumnForm.getCombo("inputOption");
				var value = newColumnForm.getItemValue("inputOption");
				combo.clearAll();
				var opts = [{value: "textbox", text: "文本框"},
							{value: "textboxbutton", text: "文本框+按钮"},
							{value: "textboxlabel", text: "文本框+标签"}];
				combo.addOption(opts);
				if (value == null || (value != "textbox" && value != "textboxbutton" && value != "textboxlabel")) {
					combo.selectOption(0);
				} 
				if ("textboxlabel" == combo.getSelectedValue()) {
					newColumnForm.showItem("dataTypeExtend");
				} else {
					newColumnForm.hideItem("dataTypeExtend");
				}
				newColumnForm.showItem("inputOption");
				newColumnForm.hideItem("codeTypeCode");
			} else if ("n" === dataType) {
				var dataTypeExtend = newColumnForm.getItemValue("dataTypeExtend");
				var combo = newColumnForm.getCombo("inputOption");
				var value = newColumnForm.getItemValue("inputOption");
				var opts = [{value: "integer", text: "整型"},
							{value: "floating", text: "浮点型"},
							{value: "integerlabel", text: "整型+标签"},
							{value: "floatinglabel", text: "浮点型+标签"}];
				combo.clearAll();
				newColumnForm.removeItem("dataTypeExtend");
				newColumnForm.addItem("columnTypeExpend", numberItem);
				combo.addOption(opts);
				newColumnForm.showItem("inputOption");
				newColumnForm.hideItem("codeTypeCode");
				if ("floating" == value) {
					combo.selectOption(1);
					newColumnForm.removeItem("dataTypeExtend");
					newColumnForm.addItem("columnTypeExpend", numberItem);
					newColumnForm.setItemValue("dataTypeExtend", dataTypeExtend);
					newColumnForm.showItem("dataTypeExtend");
					newColumnForm.hideItem("label");
					newColumnForm.hideItem("precision");
				} else if ("integerlabel" == value) {
					combo.selectOption(2);
					newColumnForm.removeItem("dataTypeExtend");
					newColumnForm.addItem("columnTypeExpend", labelItem);
					newColumnForm.setItemValue("dataTypeExtend", dataTypeExtend);
					newColumnForm.showItem("dataTypeExtend");
					newColumnForm.hideItem("label");
					newColumnForm.hideItem("precision");
				} else if ("floatinglabel" == value) {
					combo.selectOption(3);
					newColumnForm.hideItem("dataTypeExtend");
					newColumnForm.showItem("label");
					newColumnForm.showItem("precision");
				} else {
					combo.selectOption(0);
					newColumnForm.hideItem("dataTypeExtend");
					newColumnForm.hideItem("label");
					newColumnForm.hideItem("precision");
				}
			}
		} else if ("textarea" === inputType) {
			var dataTypeExtend = newColumnForm.getItemValue("dataTypeExtend");
			newColumnForm.removeItem("dataTypeExtend");
			newColumnForm.addItem("columnTypeExpend", rowHeight);
			newColumnForm.setItemValue("dataTypeExtend", dataTypeExtend);
			newColumnForm.showItem("dataTypeExtend");
			newColumnForm.hideItem("inputOption");
			newColumnForm.hideItem("codeTypeCode");
		} else if ("combobox" === inputType) {
			newColumnForm.hideItem("inputOption");
			newColumnForm.showItem("codeTypeCode");
			newColumnForm.hideItem("dataTypeExtend");
		} else if ("combotree" === inputType) {
			var combo = newColumnForm.getCombo("inputOption");
			var value = newColumnForm.getItemValue("inputOption");
			combo.clearAll();
			var opts = [{value: "popup", text: "弹出树"},
						{value: "pulldown", text: "下拉树"}];
			combo.addOption(opts);
			if (value == null || (value != "popup" && value != "pulldown")) {
				combo.selectOption(0);
			} 
			newColumnForm.showItem("inputOption");
			newColumnForm.showItem("codeTypeCode");
			newColumnForm.hideItem("dataTypeExtend");
		} else if ("combogrid" === inputType) {
			var combo = newColumnForm.getCombo("inputOption");
			var value = newColumnForm.getItemValue("inputOption");
			combo.clearAll();
			var opts = [{value: "combogrid", text: "下拉列表"},
						{value: "combogridbutton", text: "下拉列表+按钮"}];
			combo.addOption(opts);
			if (value == null || (value != "combogrid" && value != "combogridbutton")) {
				combo.selectOption(0);
			} 
			newColumnForm.showItem("inputOption");
			newColumnForm.showItem("codeTypeCode");
			newColumnForm.hideItem("dataTypeExtend");
		} else if("checkbox" === inputType || "radio" === inputType) {
			newColumnForm.hideItem("inputOption");
			newColumnForm.showItem("codeTypeCode");
			newColumnForm.hideItem("dataTypeExtend");
		} else if ("datepicker" === inputType) {
			var combo = newColumnForm.getCombo("inputOption");
			var value = newColumnForm.getItemValue("inputOption");
			initDateFormatCombo(newColumnForm, "inputOption");
			if (value) newColumnForm.setItemValue("inputOption", value);
			newColumnForm.setItemValue("codeTypeCode", "");
			newColumnForm.showItem("inputOption");
			newColumnForm.hideItem("codeTypeCode");
			newColumnForm.showItem("dataTypeExtend");
		}
	}
	
	function addSubfield(grid) {
		var rowId = grid.getSelectedRowId();
		if (isEmpty(rowId)) {
			dhtmlx.alert("请选择一条记录，指定分栏符插入位置！");
			return;
		}
		if (rowId.indexOf(",") > 0) {
			dhtmlx.alert("只能选择一条记录，指定分栏符插入位置！");
			return;
		}
        var uid = grid.uid();
        var idx = grid.getRowIndex(rowId);
        grid.addRow(uid, ["<font color=\"blue\">分栏符</font>", "1", "1"], idx);
		//grid.setUserData(uid, "tooltip",  "分栏符");
		grid.setUserData(uid, "columnId", "-1");
		grid.cells(uid, 2).setDisabled(true);
		++lastCheckRowIdx;
	}
	
	function delSubfield(grid) {
		var rowId = grid.getSelectedRowId();
		if (isEmpty(rowId)) {
			dhtmlx.alert("请选择分栏符，再删除！");
			return;
		}
		if (rowId.indexOf(",") > 0) {
			dhtmlx.alert("只能选择一个分栏符，再删除！");
			return;
		}
        var columnId = grid.getUserData(rowId, "columnId");
        if ("-1" == columnId) {
        	grid.deleteRow(rowId);
    		--lastCheckRowIdx;
        } else {
        	dhtmlx.alert("您选择的不是分栏符，无法删除！");
        }
	}
	
	function addPlaceholder(grid) {
		var rowId = grid.getSelectedRowId();
		if (isEmpty(rowId)) {
			dhtmlx.alert("请选择一条记录，指定占位符插入位置！");
			return;
		}
		if (rowId.indexOf(",") > 0) {
			dhtmlx.alert("只能选择一条记录，指定占位符插入位置！");
			return;
		}
        var uid = grid.uid();
        var idx = grid.getRowIndex(rowId);
        grid.addRow(uid, ["<font color=\"blue\">占位符</font>", "1", "1"], idx);
		//grid.setUserData(uid, "tooltip",  "占位符");
		grid.setUserData(uid, "columnId", "-2");
		//grid.setUserData(uid, "defaultValue", ("PH"+uid));
		//grid.cells(uid, 2).setDisabled(true);
		++lastCheckRowIdx;
	}
	
	function delPlaceholder(grid) {
		var rowId = grid.getSelectedRowId();
		if (isEmpty(rowId)) {
			dhtmlx.alert("请选择占位符，再删除！");
			return;
		}
		if (rowId.indexOf(",") > 0) {
			dhtmlx.alert("只能选择一个占位符，再删除！");
			return;
		}
        var columnId = grid.getUserData(rowId, "columnId");
        if ("-2" == columnId) {
        	grid.deleteRow(rowId);
    		--lastCheckRowIdx;
        } else {
        	dhtmlx.alert("您选择的不是占位符，无法删除！");
        }
	}
}

/**
 * 事件帮助说明
 * @returns {___obj0}
 */
function getHelpContent() {
	var obj = document.getElementById("DIV-help");
	if (null == obj) {
		var h = 600;
		if(h > document.body.clientHeight){
			h = document.body.clientHeight;
		}
		h = h - 95;
		obj = document.createElement("DIV");
		obj.setAttribute("id", "DIV-help");
		obj.setAttribute("style", "font-family: Tahoma; font-size: 11px;display: none;overflow-y:auto; overflow-x:auto;height:" + h + "px;");
		obj.innerHTML = "<ul> \n"
			+ "<li type=\"square\">"
			+ "<p><b>关于DHTMLXFORM操作：</b></p> \n"
			+ "<p>1、关于如何操作DHTMLXFORM，请到“系统管理-->DHTMLX帮助”模块中查看API及具体的示例</p> \n"
			+ "</li> \n"
			+ "</ul> \n"
			+ "<ul> \n"
			+ "<li type=\"square\">"
			+ "<p><b>界面事件说明：</b></p> \n"
			+ "<p>1、表单FORM变量：每个方法第一个参数就是form变量</p> \n"
			+ "<p>2、提示消息窗口（统一样式）</p> \n"
			+ "<p>&emsp;&emsp;2.1、dhtmlx.alert(msg)为消息提示窗口，相当alert(msg)</p> \n"
			+ "<p>&emsp;&emsp;2.2、dhtmlx.message(msg)为消息窗口，没有确定按钮，在右上方显示，会自动消失</p> \n"
			+ "<p>&emsp;&emsp;2.3、dhtmlx.confirm(msg)为消息确定窗口，相当于confirm(msg)</p> \n"
			+ "<p>3. 事件方法说明</p> \n"
			+ "<p>&emsp;&emsp;3.1、onload事件方法没参数，不需要返回值</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;3.1.1、example:</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<font color='blue'>function</font> initEvent(form) {</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;form.setItemValue(\"YEAR_CODE\",2013)</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;//获取status控件下拉框</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;var combo = form.getCombo(\"STATUS\");</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;//status控件下拉框的值</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;var opts = [{text:\"未审批\", value:\"0\"},{text:\"审批中\", value:\"1\"},{text:\"审批通过\", value:\"2\"}]</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;//给status控件下拉框设值</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;combo.addOption(opts);</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;}</p> \n"
			+ "<p>&emsp;&emsp;3.2、onchange事件方法有三个可以用参数(form, itemId, value)，不需要返回值</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;3.2.1、itemId：为表单控件ID</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;3.2.2、value：为表单控件的值</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;3.2.3、example:</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<font color='blue'>function</font> changeEvent(form, itemId, value) {</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;if (\"NAME\" == itemId && \"admin\" == value) {</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;dhtmlx.alert(\"不能使用内置admin名称！\");</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;} else if (\"AGE\" == itemId && value > 160) {</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;//把AGE控件的值设为20 </p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;form.setItemValue(\"AGE\", 20); </p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;}</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;}</p> \n"
			+ "<p>&emsp;&emsp;3.3、beforesave事件方法没参数，<font color='red'>需要返回值({success:true/false, message:\"提示信息\"})</font></p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;3.1.1、example:</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<font color='blue'>function</font> beforeSaveEvent(form) {</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;//保存前校验</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;...</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;//校验结果</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;//return {success:true};</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;return {success:false, message:\"检查失败！\"};</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;}</p> \n"
			+ "<p>&emsp;&emsp;3.4、aftersave事件方法有参数，不需要返回值</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;3.4.1、example:</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<font color='blue'>function</font> afterSaveEvent(form) {</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;//保存后操作</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;...</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;//校验结果</p> \n"
			+ "<p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;}</p> \n"
			+ "</li> \n"
			+ "</ul> \n";
	}
	
	return obj;
}

function validateShowName(value) {
	if (isEmpty(value)) return true;
	return (/[^,]/g).test(value);
}

/**
 * 初始化日期格式下拉框
 */
function initDateFormatCombo(newColumnForm, item) {
    var dateFormatCombo = newColumnForm.getCombo(item);
    if (!dateFormatCombo) return;
    dateFormatCombo.clearAll(true);
    var dateFormatComboUrl = AppActionURI.columnDefine + "!getDateFormat.json";
    var dateFormatJsonObj = loadJson(dateFormatComboUrl);
    if (dateFormatJsonObj) {
        var opt_data = [];
        for (var m = 0; m < dateFormatJsonObj.length; m++) {
            opt_data[m] = {
                text : dateFormatJsonObj[m].name,
                value : dateFormatJsonObj[m].value
            };
        }
        dateFormatCombo.addOption(opt_data);
        dateFormatCombo.selectOption(0);
    }
}
