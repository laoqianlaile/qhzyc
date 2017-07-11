var COLUMN_MODEL_URL = AppActionURI.columnDefine;
var _flag ;

function loadColumn(tabbar,flag) {
	if(flag){
		_flag = flag;
	}
	var _this = this;
	var cbrId,cbcId,cbState;
	var dataExist = 0;
	var oldLength;
	pageable = false;
	dataGrid = tabbar.cells("tab$table$01").attachGrid();
	tabbar.cells("tab$table$01").detachToolbar();
	var toolbar = tabbar.cells("tab$table$01").attachToolbar();
	toolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	if (flag) {
		columnClassification=classification;
		toolbar.addButton("add", 1, "新增", "new.gif");
		toolbar.addSeparator("septr$01", 2);
		toolbar.addButton("modify", 3, "修改", "update.gif");
		toolbar.addSeparator("septr$02", 4);
		toolbar.addButton("delete", 5, "删除", "delete.gif");
		toolbar.addSeparator("septr$03", 6);
		toolbar.addButton("copy", 7, "字段复制", "copy.gif");
		toolbar.addSeparator("septr$05", 10);
		toolbar.addButton("sous", 12, "", "search.gif", null, "right");
		toolbar.addDiv("top$searchTextdiv", 11, "right");
		var ST_form = initSearchColumn();
		document.getElementById("top$searchTextdiv").style.fontSize = "11px";
		if (classification == 'V') {
			toolbar.hideItem("add");
			toolbar.hideItem("septr$01");
			//toolbar.hideItem("modify");
			//toolbar.hideItem("septr$02");
			toolbar.hideItem("delete");
			toolbar.hideItem("septr$03");
			toolbar.hideItem("copy");
		} else if (classification == 'C') {//基础表
			toolbar.showItem("add");
			toolbar.showItem("septr$01");
			toolbar.showItem("delete");
			toolbar.hideItem("septr$03");
			toolbar.hideItem("copy");
		} else {
			toolbar.showItem("add");
			toolbar.showItem("septr$01");
			toolbar.showItem("delete");
			toolbar.showItem("septr$03");
			toolbar.showItem("copy");
		}
	} else {
		toolbar.addButton("save", 1, "保存", "true.gif");
		toolbar.addSeparator("septr$01", 2);
		toolbar.addButton("cancel", 3, "取消", "false.gif");
	}
	gridData  = {
			format: {
				headers: ["<center>显示名称</center>","<center>字段名称</center>","<center>数据类型</center>","<center>长度</center>","<center>字段类型</center>","<center>录入项</center>","<center>修改项</center>","<center>检索项</center>","<center>列表项</center>","<center>排序项</center>","<center>一体化检索项</center>","<center>对齐方式</center>","<center>查询方式</center>","<center>默认列宽</center>","<center>默认值</center>","<center>备注</center>",""],
				   cols: ["showName","columnName","dataType","length","columnType","inputable","updateable","searchable","listable","sortable","phraseable","align","filterType","width","defaultValue","remark"],
				colWidths: ["100","100","70","50","70","50","50","50","50","50","100","70","70","70","70","70","*"],
				colTypes: ["ro","ro","co","ro","co","ch","ch","ch","ch","ch","ch","co","co","ro","ro","ro","ro"],
				colAligns: ["left","left","center","right","center","center","center","center","center","center","center","center","center","center","center","left","left"]
			}
		};
	var colTypeCombo = dataGrid.getCombo(4);
	colTypeCombo.put("0","业务字段");
	colTypeCombo.put("1","基础字段");
	colTypeCombo.put("2","系统字段");
		
	var alignCombo = dataGrid.getCombo(11);
	alignCombo.put("left","左对齐");
	alignCombo.put("right","右对齐");
	alignCombo.put("center","居中");
			
	var filterTypeCombo = dataGrid.getCombo(12);
	filterTypeCombo.put( "LIKE",  "包含");
	filterTypeCombo.put( "EQ",  "等于");
	filterTypeCombo.put( "GT",  "大于");
	filterTypeCombo.put( "GTE",  "大于等于");
	filterTypeCombo.put( "LT",  "小于");
	filterTypeCombo.put( "LTE",  "小于等于");
	filterTypeCombo.put( "NOT",  "不等于");
	filterTypeCombo.put( "BT",  "介于");
	filterTypeCombo.put( "NLL",  "为空");
			    
	var queryType = dataGrid.getCombo(2);
    queryType.put("c","字符型");
    queryType.put("d","日期型");
    queryType.put("n","数字型");
    queryType.put("e","编码型");
    queryType.put("u","用户型");
    queryType.put("p","部门型");
			    
	MODEL_URL = COLUMN_MODEL_URL;
	initGrid();
	dataGrid.enableDragAndDrop(true);
	QUERY_URL = AppActionURI.columnDefine + "!search.json?Q_EQ_tableId=" + nodeId + 
		("-C" === nodeId ? "" : "&Q_EQ_columnType=0") + "&P_orders=showOrder";
	search();
	/** 列表拖拽调整顺序*/
    dataGrid.attachEvent("onDrag",function(sId,tId) {
		if (sId.indexOf(",") != -1) {
			dhtmlx.message(getMessage("drag_one_record"));
			return false;
		}
		return true;
	});
    
	dataGrid.attachEvent("onDrop", function(sId, tId) {
    	if(undefined != tId){
			var sortUrl = AppActionURI.columnDefine + "!sortShowOrder?start=" + sId + "&end=" + tId+"&tableId="+nodeId;
			dhtmlxAjax.get(sortUrl, function(loader) {
			});
		}	
    });
	
	dataGrid.attachEvent("onCheckbox", function(rId,cInd,state){
		var tValue,tColumn,tc_MODEL_URL;
		cbrId = rId;
		cbcId = cInd;
		cbState = state;
		if(state==true){
			tValue = "1";
		}else if(cbState==false){
			tValue = "0";
		}
		if(cbcId=="5"){
			tColumn="inputable";
		}else if(cbcId=="6"){
			tColumn="updateable";
		}else if(cbcId=="7"){
			tColumn="searchable";
		}else if(cbcId=="8"){
			tColumn="listable";
		}else if(cbcId=="9"){
			tColumn="sortable";
		}else if (cbcId=="10") {
			tColumn="phraseable";
		}
		var url = COLUMN_MODEL_URL +"!updateStatus?id="+cbrId+"&columName="+tColumn+"&value="+tValue;
		dhtmlxAjax.get(url,function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (true == jsonObj.status) {
				dataGrid.cells(rId, cInd).setValue(tValue == "0" ? "1" : "0");
			}
		});
   });
			
	var win;	
	//判断表类型是否为 null 不为空则加载'字段标签'下来框数据 
	
	var copyToLogicForm;
	var copyToLogicFormData = {
			format: [
				{type: "block", width: "350", list:[
					{type: "hidden", name: "_method"},
					{type: "hidden", name: "columnIds"},
	 				{type: "combo", label: "逻辑表：", name: "logicCode", labelWidth:80, width:200, readonly:true, required:true}
				]}
			],
			settings: {labelWidth: 80, inputWidth: 160}
		};
	/**
	 * 初始化构件分类表单工具条的方法
	 * @param {dhtmlxToolbar} toolBar
	 */
	function initCopyLogicFormToolbar(toolBar) {
		toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
		toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
		toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
		toolBar.setAlign("right");
		toolBar.attachEvent("onClick", function(id) {
			if (id == "close") {
				dhxWins.window(WIN_ID).close();
			} else if (id == "save") {
				if (!copyToLogicForm.validate())
					return;
				copyToLogicForm.setItemValue("_method", "post");
				copyToLogicForm.send(AppActionURI.columnDefine + "!copyToLogic.json", "post", function(loader, response){
					dhxWins.window(WIN_ID).close();
					dhtmlx.message(getMessage("save_success"));
	    		});
			}
		});
	}
	
	toolbar.attachEvent("onClick", function(id){
		
		GWIN_WIDTH = 700;
		GWIN_HEIGHT = 380;
		MODEL_URL = COLUMN_MODEL_URL;
		var url_ = AppActionURI.columnLabel + "!search.json?F_in=code,name&P_orders=showOrder";
		var datas = loadJson(url_).data;
		if (id == "add") {
			var columns_data;
			var loadopts = loadJson(AppActionURI.columnLabel + "!getUnBindedLabel.json?tableId=" + nodeId);
			columns_data = new Array({value:"",text:"请选择"});
			for (var i = 0; i < loadopts.length; i++) {
				columns_data.push({value: loadopts[i][0], text: loadopts[i][1]});
			}
            var columnType = ST_form.isItemChecked("columnType"),
            QUERY_URL = AppActionURI.columnDefine + "!search.json?Q_EQ_tableId=" + nodeId + 
            	("-C" === nodeId ? "" : "&Q_EQ_columnType=0") + ((!columnType) ? "&Q_EQ_columnType=0" : "") + "&P_orders=showOrder";
            var h = document.body.clientHeight,
            height = 600;
            if (height > h) {
            	height = h;
			}
			detailForm = openNewWindow(getFormJson(), initToolbar, 820, height);
			detailForm.setItemValue("tableId",nodeId);
			var cp_combo = detailForm.getCombo("columnLabel");
			cp_combo.addOption(columns_data);
			cp_combo.selectOption(0);
        	dataExist = 0;
			formChange();
		} else if (id == "modify") {
			var selectId = dataGrid.getSelectedRowId();
			if (selectId == undefined) {
				dhtmlx.message(getMessage("select_record"));
				return;
			}
			if (selectId.indexOf(",") != -1) {
				dhtmlx.message(getMessage("select_only_one_record"));
				return;
			}
            var columnType = ST_form.isItemChecked("columnType"),
            QUERY_URL = AppActionURI.columnDefine + "!search.json?Q_EQ_tableId=" + nodeId + 
            	("-C" === nodeId ? "" : "&Q_EQ_columnType=0") + ((!columnType) ? "&Q_EQ_columnType=0" : "") + "&P_orders=showOrder";
            if (!appDhxWins) {
                var appDhxWins = new dhtmlXWindows();
            }
            var h = document.body.clientHeight,
            height = 600;
            if (height > h) {
            	height = h;
			}
            dataWin = appDhxWins.createWindow(WIN_ID, 0, 0, 820, height);
            dataWin.setModal(true);
            dataWin.setText("修改");
            dataWin.center();
            var formFormat = initFormFormat(getFormJson());
            detailForm = dataWin.attachForm(formFormat);
            var statusBar = dataWin.attachStatusBar();
            var toolBar = new dhtmlXToolbarObject(statusBar);
            initToolbar.call(this, toolBar);
            var loadUrl = MODEL_URL + "/" + selectId + ".json";
            loadForm(detailForm, loadUrl);
			detailForm.disableItem("columnName");
            toolBar.hideItem("submitAndAdd");
            var columnType = dataGrid.cells(selectId,4).getValue();
			if (columnType == 1) {
				detailForm.removeItem("columnType");
				detailForm.addItem("columnInfo", {type: "hidden", name: "columnType",value:"1"});
			}
//			if (classification == Classification.VIEW) {
//				detailForm.disableItem("dataType");
//				detailForm.disableItem("length");
//			}
            if (classification != "V" && classification != "C") {
				dhtmlxAjax.get(AppActionURI.columnDefine + "!dataExist.json?P_columnName=" + detailForm.getItemValue("columnName") + "&P_tableId=" + nodeId, function(loader) {
					var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
	                if (jsonObj.success != true) {
	                	dataExist = 0;
	                } else {
	                	//字符型和日期型转换不受数据限制(数据库中均为varchar)，只校验与数字型的相互转换
	                	dataExist = 1;
	                	var dataType = detailForm.getItemValue("dataType");
	                	var opts;
	                	var combo = detailForm.getCombo("dataType");
	                	combo.clearAll();
	                	if (dataType == "c") {
	                		opts = [{value: "c", text: "字符型", selected: true},
	    							{value: "d", text: "日期型"}];
						} else if (dataType == "n") {
							opts = [{value: "n", text: "数字型", selected: true}];
						} else if (dataType == "d") {
							opts = [{value: "c", text: "字符型"},
	    							{value: "d", text: "日期型", selected: true}];
						}
                		combo.addOption(opts);
	                }
				});
            }
			oldLength = detailForm.getItemValue("length");
			formChange();
			var cp_combo = detailForm.getCombo("columnLabel");
			var cp = detailForm.getItemValue("columnLabel");
			//剔除本表已经绑定的字段标签
			var loadopts = loadJson(AppActionURI.columnLabel + "!getUnBindedLabel.json?tableId=" + nodeId);
			cp_combo.addOption([["","请选择"]]);
			if(cp != ""){
				var f = true;
				for (var i = 0; i < datas.length; i++) {
					for (var j = 0; j < loadopts.length; j++) {
						if(cp == datas[i].code && f){
							cp_combo.addOption([[datas[i].code,datas[i].name]]);
							f = false;
						}
						else if(datas[i].code == loadopts[j][0]){
							cp_combo.addOption([[datas[i].code,datas[i].name]]);
						}
					}
				}
				detailForm.setItemValue("columnLabel",cp);
			}
			else{
				for (var j = 0; j < loadopts.length; j++) {
					cp_combo.addOption([[loadopts[j][0],loadopts[j][1]]]);
				}
				cp_combo.selectOption(0);
			}
		}else if(id == "delete"){
			var rowIds = dataGrid.getSelectedRowId();
			if(rowIds == undefined){
				dhtmlx.message(getMessage("select_record"));
				return;
			}
			dhtmlx.confirm({
				text: getMessage("delete_warning"),
				ok: "确定",
				cancel: "取消",
				callback: function(flag) {
					if (flag) {
						if (classification == "LT") {
							var CK_relation = AppActionURI.logicTableRelation + "!checkRelation.json?P_rowsValue=" + rowIds;
							dhtmlxAjax.get(CK_relation, function(loader) {
								var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
				                if (jsonObj.success != true) {
									dhtmlx.message(jsonObj.message);
				                    return;
				                } else {
									deleteColumnById(rowIds);
				                }
							});
							return;
						}
						dhtmlx.confirm({
							text: "<font color='red'>如果确定，则会删除该字段及相关的所有配置信息！</font>再次确定要删除所选字段记录？",
							ok: "确定",
							cancel: "取消",
							callback: function(flag) {
								if (flag) { deleteColumnById(rowIds);}
							}
						});
					}
				}
			});
		} else if (id == "sous") {
			pageable = false;
			var value = ST_form.getItemValue("columnValue"),
			    name  = ST_form.getItemValue("columnName"),
			    param = ""
			    url,
			    columnType = ST_form.isItemChecked("columnType");
			value = encodeURIComponent(value);
			if (name == "") {
				dhtmlx.message("过滤字段不能为空，请选择！");
				return;
			}
			if (value !== "") {
				param = "&Q_LIKE_" + name + "=" + value;
			}
			url = AppActionURI.columnDefine + "!search.json?Q_EQ_tableId=" + nodeId + 
				((!columnType) ? "&Q_EQ_columnType=0" : "") + param + "&P_orders=showOrder";
   			search(null, null, url, null, null);
			pageable = true;
		} else if (id == "copy") {
			if (classification == 'LT') {//逻辑表字段复制到其他表
				var rowIdsTo = dataGrid.getSelectedRowId();
				if (null == rowIdsTo || "" == rowIdsTo) {
					dhtmlx.message(getMessage("select_record"));
					return;
				} 
				var _this = this;
				if(!win){
					var h = 500;
		        	if (h > document.body.clientHeight) {
		        		h = document.body.clientHeight;
		        	}
					win = createDhxWindow({id:"win$tableCopyTo", title:"字段复制到", width:760, height:h});
					win.attachEvent("onClose", function() {
						classification=columnClassification;
						loadColumn(tabbar,flag);
			   			_this.columnManager = true;
			   			return true;
					});
		       	 	initTableCopyWinTo(win,rowIdsTo,nodeId);
				}
			} else {//物理表字段复制到其他表
				var rowIdsTo = dataGrid.getSelectedRowId();
				if (null == rowIdsTo || "" == rowIdsTo) {
					dhtmlx.message(getMessage("select_record"));
					return;
				} 
				var _this = this;
				if(!win){
					var h = 500;
		        	if (h > document.body.clientHeight) {
		        		h = document.body.clientHeight;
		        	}
					win = createDhxWindow({id:"win$tableCopyTo", title:"字段复制到", width:760, height:h});
					win.attachEvent("onClose", function() {
						classification=columnClassification;
						loadColumn(tabbar,flag);
			   			_this.columnManager = true;
			   			return true;
					});
		       	 	initTableCopyWin(win,rowIdsTo);
				}
			}
		}
	});
	pageable = true;
	
	/**
	 * 字段删除
	 * @param rowIds
	 */
	function deleteColumnById(rowIds) {
		var rowIdArr = rowIds.split(",");
		for (var i = 0; i < rowIdArr.length; i++) {
			var rowId = rowIdArr[i];
			var columnType = dataGrid.cells(rowId,4).getValue();
			if (columnType == 1) {
				dhtmlx.message("您选择记录中有\"基础字段\",基础字段不可删除,请重新选择!");
				return;
			}
		}
	    DELETE_URL = COLUMN_MODEL_URL + "/" + rowIds + ".json?_method=delete";
	    dhtmlxAjax.get(DELETE_URL, function(loader) {
	    	var rlt = eval("(" + loader.xmlDoc.responseText + ")");
			if (rlt.success) {
				dhtmlx.message(getMessage("delete_success"));
				QUERY_URL = AppActionURI.columnDefine + "!search.json?Q_EQ_tableId=" + nodeId + 
            	("-C" === nodeId ? "" : "&Q_EQ_columnType=0") + "&P_orders=showOrder";
	    		search();
			} else {
				dhtmlx.alert(getMessage("delete_failure") + rlt.message);
			}
	    });
	}
	var url = contextPath +"/code/code-type!getCodeTypeSelect.json";
	var opts = loadJson(url);
	opts.unshift({value: "", text: "请选择编码",selected:true});
	/*增,查,改按钮对应form*/
	//columnFormData = 
	
	function getFormJson() {
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
//										{type:"newcolumn"},
//										{type: "radio", label: "文本框(按钮)", name: "inputType", value: "textboxbutton", position:"label-right", labelAlign:"left",labelWidth: 100},
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
		if ("-C" === nodeId) {
			formJson.format.splice(3, 1);
			formJson.format.push(
					{type:"fieldset", label:"其他", width:750, offsetLeft:"10", list:[
						{type: "block", width:750, list:[
								{type: "itemlabel", label: "其他项:&nbsp;&nbsp;&nbsp;", width: "230"},
								{type:"newcolumn"},
								{type: "checkbox", label: "录入项", name: "inputable", checked: true, position:"label-right", labelAlign:"left",labelWidth: 75},
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
     					{type: "block", width:750, list:[
               					{type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "remark",rows:"3",inputWidth:"610",maxLength:125 }
               			]}
					]});	
		} else if ("LT" == classification) {
			formJson.format.splice(3, 1);
			formJson.format.push(
					{type:"fieldset", label:"其他", width:750, offsetLeft:"10", list:[
						{type: "block", width:750, list:[
								{type: "itemlabel", label: "其他项:&nbsp;&nbsp;&nbsp;", width: "230"},
								{type:"newcolumn"},
								{type: "checkbox", label: "录入项", name: "inputable", checked: true, position:"label-right", labelAlign:"left",labelWidth: 75},
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
						{type: "block", width:750, list:[
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
		} else {
			formJson.format.splice(3, 1);
			formJson.format.push(
					{type:"fieldset", label:"其他", width:750, offsetLeft:"10", list:[
						{type: "block", width:750, list:[
								{type: "itemlabel", label: "其他项:&nbsp;&nbsp;&nbsp;", width: "230"},
								{type:"newcolumn"},
								{type: "checkbox", label: "录入项", name: "inputable", checked: true, position:"label-right", labelAlign:"left",labelWidth: 75},
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
		}
		return formJson;
	}

	function formChange() {
		var dataType = detailForm.getItemValue("dataType");
		var defaultValue = detailForm.getItemValue("defaultValue");
		var dataTypeExtend = detailForm.getItemValue("dataTypeExtend");
		var inputType = detailForm.getCheckedValue("inputType");
		var inputOption = detailForm.getItemValue("inputOption");
		var comboItem = {type: "combo", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "defaultValue",width: "230",maxLength:25,offsetLeft:"50"};
		var labelItem = {type: "input", label: "标签值:&nbsp;&nbsp;&nbsp;", name: "dataTypeExtend", maxLength:10, width: "230",offsetLeft:"50"};
		var numberItem = {type: "input", label: "精度:&nbsp;&nbsp;&nbsp;", validate:"ValidNumeric", name: "dataTypeExtend", maxLength:1, width: "230",offsetLeft:"50"};
		var dateItem = {type: "combo", label: " 存入格式:&nbsp;&nbsp;&nbsp;", name: "dataTypeExtend", maxLength:25, readonly: true, width: "230", offsetLeft:"50",options:[]};
		var codeTypeCode = detailForm.getItemValue("codeTypeCode");
		detailForm.hideItem("dataTypeExtend");
		detailForm.hideItem("label");
		detailForm.hideItem("precision");
		//if(dataType != "e"){
			//detailForm.disableItem("codeTypeCode");
			if ("d" === dataType) {
				var dataTypeExtend = detailForm.getItemValue("dataTypeExtend");
				detailForm.removeItem("dataTypeExtend");
				detailForm.addItem("columnTypeExpend", dateItem);
				initDateFormatCombo(detailForm, "dataTypeExtend");
				if (dataTypeExtend) detailForm.setItemValue("dataTypeExtend",dataTypeExtend);
			} else if ("n" === dataType && "floating" === inputOption) {
				detailForm.removeItem("dataTypeExtend");
				detailForm.addItem("columnTypeExpend", numberItem);
				detailForm.setItemValue("dataTypeExtend", dataTypeExtend);
			} else if ("c" === dataType && "textboxlabel" === inputOption) {
				detailForm.removeItem("dataTypeExtend");
				detailForm.addItem("columnTypeExpend", labelItem);
				detailForm.setItemValue("dataTypeExtend", dataTypeExtend);
			}
		//} else {
		if (isNotEmpty(codeTypeCode)) {
			detailForm.removeItem("defaultValue");
			detailForm.addItem("columnInfo", comboItem);
			var combo = detailForm.getCombo("defaultValue");
			var opts  = loadJson(contextPath + "/code/code!combobox.json?codeTypeCode=" + codeTypeCode);
			opts.unshift({value: "", text: "请选择", selected: true});
			combo.addOption(opts);
		}
		detailForm.setItemValue("defaultValue", defaultValue);
		// 
		changeInputType(detailForm, dataType);
		detailForm.checkItem("inputType", inputType);
		// 
		changeInterface(detailForm, inputType, dataType);
		
		detailForm.attachEvent("onChange", function (id, value){
			if ("dataType" == id) {
				/*if ("e" == value || "u" == value || "p" == value) {
					detailForm.setItemValue("length", 32);
				} else */
				var comboItem = {type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "defaultValue",width: "230",maxLength:25,offsetLeft:"50"};
				var combo = detailForm.getCombo("codeTypeCode");
				if ("d" == value) {
					detailForm.checkItem("inputType", "datepicker");
					detailForm.setItemValue("inputOption", null);
					changeInterface(detailForm, "datepicker", value);
					var dataTypeExtend = detailForm.getItemValue("dataTypeExtend");
					detailForm.removeItem("dataTypeExtend");
					detailForm.addItem("columnTypeExpend", dateItem);
					initDateFormatCombo(detailForm, "dataTypeExtend");
					if (dataTypeExtend) detailForm.setItemValue("dataTypeExtend",dataTypeExtend);
				}else if ("n" == value) {
					var length = detailForm.getItemValue("length");
					var dataTypeExtend = detailForm.getItemValue("dataTypeExtend");
					if ("" == length || length > 38) {
						detailForm.setItemValue("length", 18);
					}
					detailForm.checkItem("inputType", "textbox");
					changeInterface(detailForm, "textbox", value);
				} else {
					detailForm.checkItem("inputType", "textbox");
					changeInterface(detailForm, "textbox", value);
					detailForm.hideItem("dataTypeExtend");
				}
				detailForm.removeItem("defaultValue");
				detailForm.addItem("columnInfo", comboItem);
				combo.setComboValue("");
				//detailForm.disableItem("codeTypeCode");
				changeInputType(detailForm, value);
			} else if ("length" == id) {
				var dt = detailForm.getItemValue("dataType");
				if ("n" == dt && value > 38) {
					dhtmlx.message("数字类型的长度不能大于38！");
					detailForm.setItemValue("length", 18);
				} else if (value > 4000) {
					dhtmlx.message("数据类型的长度不能大于4000！");
					detailForm.setItemValue("length", 4000);
				}
				if (dataExist == 1) {
					if (value < oldLength) {
						dhtmlx.message("该字段已保存数据，长度不能小于原字段长度！");
						detailForm.setItemValue("length", oldLength);
					}
				}
			} else if ("codeTypeCode" == id) {
				var combo = null;
				if (detailForm.getItemType("defaultValue") === "combo") {
					combo = detailForm.getCombo("defaultValue");
					combo.clearAll();
				} else {
					detailForm.setItemValue("defaultValue", "");
				}
				//console.log("codeTypeCode: " + value);
				if (isEmpty(value)) {
//					controlInputType(detailForm, ["text", "textarea"], "enable", "text");
//					return;
				} else if (detailForm.getItemType("defaultValue") !== "combo") {
					var comboItem = {type: "combo", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "defaultValue",width: "230",maxLength:25,offsetLeft:"50"};
					detailForm.removeItem("defaultValue");
					detailForm.addItem("columnInfo", comboItem);
				}
				//controlInputType(detailForm, ["datepicker"], "hide", "textbox");
				
				var opts  = loadJson(contextPath + "/code/code!combobox.json?codeTypeCode=" + value);
				opts.unshift({value: "", text: "请选择", selected: true});
				combo = detailForm.getCombo("defaultValue");
				combo.addOption(opts);
				combo.selectOption(0);
			} else if ("dataTypeExtend" == id && detailForm.getItemValue("dataType") == "n") {
				if (value > 10) {
					dhtmlx.message("精度不能大于10！");
					detailForm.setItemValue("dataTypeExtend", 10);
				}
			} else if ("inputType" == id) {
				detailForm.setItemValue("inputOption", null);
				detailForm.setItemValue("dataTypeExtend", null);
				detailForm.setItemValue("label", null);
				detailForm.setItemValue("precision", null);
				detailForm.hideItem("label");
				detailForm.hideItem("precision");
				detailForm.getCombo("codeTypeCode").selectOption(0);
				var defaultItem = {type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "defaultValue",width: "230", maxLength:20,maxLength:25,offsetLeft:"50"};
				detailForm.removeItem("defaultValue");
				detailForm.addItem("columnInfo", defaultItem);
				changeInterface(detailForm, value, detailForm.getItemValue("dataType"));
			} else if ("columnLabel" == id) {
				var url_ = AppActionURI.columnLabel + "!search.json?F_in=codeTypeCode&Q_EQ_code=" + value;
				var datas = loadJson(url_).data[0];
				if (datas.codeTypeCode != null) {
					var combo = detailForm.getCombo("codeTypeCode");
					combo.selectOption(combo.getIndexByValue(datas.codeTypeCode));
				} else {
					combo.selectOption(0);
				}
			} else if ("inputOption" == id) {
				if (value == "textboxlabel") {
					detailForm.removeItem("dataTypeExtend");
					detailForm.addItem("columnTypeExpend", labelItem);
					detailForm.showItem("dataTypeExtend");
					detailForm.hideItem("label");
					detailForm.hideItem("precision");
				} else if (value == "floating") {
					detailForm.removeItem("dataTypeExtend");
					detailForm.addItem("columnTypeExpend", numberItem);
					detailForm.showItem("dataTypeExtend");
					detailForm.hideItem("label");
					detailForm.hideItem("precision");
				} else if (value == "integerlabel") {
					detailForm.hideItem("dataTypeExtend");
					detailForm.showItem("label");
					detailForm.hideItem("precision");
				} else if (value == "floatinglabel") {
					detailForm.hideItem("dataTypeExtend");
					detailForm.showItem("label");
					detailForm.showItem("precision");
				} else if (detailForm.getItemValue("dataType") == "d") {
					detailForm.showItem("dataTypeExtend");
					detailForm.hideItem("label");
					detailForm.hideItem("precision");
				} else {
					detailForm.hideItem("dataTypeExtend");
					detailForm.hideItem("label");
					detailForm.hideItem("precision");
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
	
	function changeInterface(detailForm, inputType, dataType) {
		var comboItem = {type: "input", label: "默认值:&nbsp;&nbsp;&nbsp;", name: "defaultValue",width: "230",maxLength:25,offsetLeft:"50"};
		var labelItem = {type: "input", label: "标签值:&nbsp;&nbsp;&nbsp;", name: "dataTypeExtend", maxLength:10, width: "230",offsetLeft:"50"};
		var numberItem = {type: "input", label: "精度:&nbsp;&nbsp;&nbsp;", validate:"ValidNumeric", name: "dataTypeExtend", maxLength:1, width: "230",offsetLeft:"50"};
		var rowHeight = {type: "input", label: "高度:&nbsp;&nbsp;&nbsp;", validate:"ValidNumeric", name: "dataTypeExtend", maxLength:5, width: "230"};
		var dateItem = {type: "combo", label: " 存入格式:&nbsp;&nbsp;&nbsp;", name: "dataTypeExtend", maxLength:25, readonly: true, width: "230", offsetLeft:"50",options:[]};
		if ("textbox" === inputType) {
			if ("c" === dataType) {
				var combo = detailForm.getCombo("inputOption");
				var value = detailForm.getItemValue("inputOption");
				combo.clearAll();
				var opts = [{value: "textbox", text: "文本框"},
							{value: "textboxbutton", text: "文本框+按钮"},
							{value: "textboxlabel", text: "文本框+标签"}];
				combo.addOption(opts);
				if (value == null || (value != "textbox" && value != "textboxbutton" && value != "textboxlabel")) {
					combo.selectOption(0);
				} 
				if ("textboxlabel" == combo.getSelectedValue()) {
					detailForm.showItem("dataTypeExtend");
				} else {
					detailForm.hideItem("dataTypeExtend");
				}
				detailForm.showItem("inputOption");
				detailForm.hideItem("codeTypeCode");
			} else if ("n" === dataType) {
				var dataTypeExtend = detailForm.getItemValue("dataTypeExtend");
				var combo = detailForm.getCombo("inputOption");
				var value = detailForm.getItemValue("inputOption");
				var opts = [{value: "integer", text: "整型"},
							{value: "floating", text: "浮点型"},
							{value: "integerlabel", text: "整型+标签"},
							{value: "floatinglabel", text: "浮点型+标签"}];
				combo.clearAll();
				detailForm.removeItem("dataTypeExtend");
				detailForm.addItem("columnTypeExpend", numberItem);
				combo.addOption(opts);
				detailForm.showItem("inputOption");
				detailForm.hideItem("codeTypeCode");
				if ("floating" == value) {
					combo.selectOption(1);
					detailForm.removeItem("dataTypeExtend");
					detailForm.addItem("columnTypeExpend", numberItem);
					detailForm.setItemValue("dataTypeExtend", dataTypeExtend);
					detailForm.showItem("dataTypeExtend");
					detailForm.hideItem("label");
					detailForm.hideItem("precision");
				} else if ("integerlabel" == value) {
					combo.selectOption(2);
					detailForm.removeItem("dataTypeExtend");
					detailForm.addItem("columnTypeExpend", labelItem);
					detailForm.setItemValue("dataTypeExtend", dataTypeExtend);
					detailForm.showItem("dataTypeExtend");
					detailForm.hideItem("label");
					detailForm.hideItem("precision");
				} else if ("floatinglabel" == value) {
					combo.selectOption(3);
					detailForm.hideItem("dataTypeExtend");
					detailForm.showItem("label");
					detailForm.showItem("precision");
				} else {
					combo.selectOption(0);
					detailForm.hideItem("dataTypeExtend");
					detailForm.hideItem("label");
					detailForm.hideItem("precision");
				}
			}
		} else if ("textarea" === inputType) {
			var dataTypeExtend = detailForm.getItemValue("dataTypeExtend");
			detailForm.removeItem("dataTypeExtend");
			detailForm.addItem("columnTypeExpend", rowHeight);
			detailForm.setItemValue("dataTypeExtend", dataTypeExtend);
			detailForm.showItem("dataTypeExtend");
			detailForm.hideItem("inputOption");
			detailForm.hideItem("codeTypeCode");
		} else if ("combobox" === inputType) {
			detailForm.hideItem("inputOption");
			detailForm.showItem("codeTypeCode");
			detailForm.hideItem("dataTypeExtend");
		} else if ("combotree" === inputType) {
			var combo = detailForm.getCombo("inputOption");
			var value = detailForm.getItemValue("inputOption");
			combo.clearAll();
			var opts = [{value: "popup", text: "弹出树"},
						{value: "pulldown", text: "下拉树"}];
			combo.addOption(opts);
			if (value == null || (value != "popup" && value != "pulldown")) {
				combo.selectOption(0);
			} else {
				detailForm.setItemValue("inputOption", value);
			}
			detailForm.showItem("inputOption");
			detailForm.showItem("codeTypeCode");
			detailForm.hideItem("dataTypeExtend");
		} else if ("combogrid" === inputType) {
			var combo = detailForm.getCombo("inputOption");
			var value = detailForm.getItemValue("inputOption");
			combo.clearAll();
			var opts = [{value: "combogrid", text: "下拉列表"},
						{value: "combogridbutton", text: "下拉列表+按钮"}];
			combo.addOption(opts);
			if (value == null || (value != "combogrid" && value != "combogridbutton")) {
				combo.selectOption(0);
			}  else {
				detailForm.setItemValue("inputOption", value);
			}
			detailForm.showItem("inputOption");
			detailForm.showItem("codeTypeCode");
			detailForm.hideItem("dataTypeExtend");
		} else if("checkbox" === inputType || "radio" === inputType) {
			detailForm.hideItem("inputOption");
			detailForm.showItem("codeTypeCode");
			detailForm.hideItem("dataTypeExtend");
		} else if ("datepicker" === inputType) {
			var combo = detailForm.getCombo("inputOption");
			var value = detailForm.getItemValue("inputOption");
			initDateFormatCombo(detailForm, "inputOption");
			if (value) detailForm.setItemValue("inputOption", value);
			detailForm.setItemValue("codeTypeCode", "");
			detailForm.showItem("inputOption");
			detailForm.hideItem("codeTypeCode");
			detailForm.showItem("dataTypeExtend");
		}
	}
	/**
	 * 字段检索
	 * @returns {dhtmlXForm}
	 */
	function initSearchColumn() {
		var sformJson = [
			{type: "itemlabel", label: "",labelWidth: 15},
	        {type: "newcolumn"},
	        {type: "checkbox", label: "显示基础字段：", name: "columnType", checked: ("-C" === nodeId ? true: false), position:"label-left", labelAlign:"right",labelWidth: 90},
			{type: "newcolumn"},
			{type: "combo", name: "columnName", className: "dhx_toolbar_form", label: "过滤字段：", labelWidth: 80, labelAlign:"right", style:"font-size:11px;", width: 120, readonly:"true",
	        	options:[{value:'showName',text:'显示名称',selected:false},
				  		 {value:'columnName',text:'字段名称',selected:true},
				  		 {value:'codeTypeCode',text:'编码值',selected:false}]
	         },
	        {type: "newcolumn"},
			{type: "input",label: "&nbsp;&nbsp;值：", name: "columnValue", className: "dhx_toolbar_form", width:120, inputHeight:17}
		];
		var form = new dhtmlXForm("top$searchTextdiv",sformJson);
		var scInp = form.getInput("columnValue");
		scInp.onfocus = function() {
			form.setItemValue("columnValue","");
		};
		scInp.onblur = function() {
			//form.setItemValue("columnValue","");
		};
		scInp.onkeydown = function(e) {
			e = e || window.event;
			var keyCode = e.keyCode || e.which;
			if (13 == keyCode) {
				pageable = false;
				var value = form.getItemValue("columnValue"),
				    name  = form.getItemValue("columnName"),
				    param = "",
				    columnType = form.isItemChecked("columnType");
				value = encodeURIComponent(value);
				if (name == "") {
					dhtmlx.message("过滤字段不能为空，请选择！");
					return;
				}
				if (value !== "") {
					param = "&Q_LIKE_" + name + "=" + value;
				}
				QUERY_URL = AppActionURI.columnDefine + "!search.json?Q_EQ_tableId=" + nodeId + 
					((!columnType) ? "&Q_EQ_columnType=0" : "") + param + "&P_orders=showOrder";
	   			search();
				pageable = true;
			}
		};
		form.attachEvent("onChange", function(id, value, status) {
			if (id === "columnType") {
				pageable = false;
				var name = form.getItemValue("columnName"),
				    value= form.getItemValue("columnValue"),
				    param= "";
				if (name != "" && value != "") {
					param = "&Q_LIKE_" + name + "=" + value;
				}
				QUERY_URL = AppActionURI.columnDefine + "!search.json?Q_EQ_tableId=" + nodeId +
					(status ? "" : "&Q_EQ_columnType=0") + param + "&P_orders=showOrder";
	   			search();
				pageable = true;
			}
		});
		return form;
	}
	
}
/**
 * 初始化日期格式下拉框
 */
function initDateFormatCombo(detailform, item) {
    var dateFormatCombo = detailform.getCombo(item);
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
function initToolbar(toolBar) {
	toolBar.setIconsPath(IMAGE_PATH);
    toolBar.addButton("submit", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    toolBar.addButton("submitAndAdd", 2, "&nbsp;&nbsp;保存并新增&nbsp;&nbsp;");
    toolBar.addButton("close", 3, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    toolBar.setAlign("right");
    toolBar.attachEvent("onClick", function(id) {
        if (id == "close") {
            dataWin.close();
        } else if (id == "submit") {
            var columnName = detailForm.getItemValue("columnName").toUpperCase();
            var showName = detailForm.getItemValue("showName").toUpperCase();
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
            var CHE_showName_Url = AppActionURI.columnDefine + "!checkUnique.json?Q_EQ_showName=" + showName + "&Q_EQ_tableId=" + nodeId;
            detailForm.send(CHE_showName_Url, "post", function(loader, response) {
                var cheJsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (cheJsonObj.success != true) {
                	dhtmlx.message(getMessage("form_field_exist", "显示名称"));
                    return;
                } else {
		            var CHE_Name_Url = AppActionURI.columnDefine + "!checkUnique.json?Q_EQ_columnName=" + columnName + "&Q_EQ_tableId=" + nodeId;
		            detailForm.send(CHE_Name_Url, "post", function(loader, response) {
		                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
		                if (jsonObj.success != true) {
		                	dhtmlx.message(getMessage("form_field_exist", "字段名称"));
		                } else {
		                	toolBar.disableItem("submit");
		                    detailForm.send(COLUMN_MODEL_URL + "!save.json", "post", function(loader, response) {
		                    	var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
		                    	if (jsonObj.success != true) {
		                    		dhtmlx.message(jsonObj.message);
								} else {
									pageable = false;
			                        search();
			                        dataWin.close();
			                        pageable = true;
			                        dhtmlx.message(getMessage("operate_success"));
				                	toolBar.enableItem("submit");
								}
		                    });
		                }
		            });
                }
            });
        } else if (id == "submitAndAdd") {
            var columnName = detailForm.getItemValue("columnName").toUpperCase();
            var showName = detailForm.getItemValue("showName").toUpperCase();
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
            var CHE_showName_Url = AppActionURI.columnDefine + "!checkUnique.json?Q_EQ_showName=" + showName + "&Q_EQ_tableId=" + nodeId;
            detailForm.send(CHE_showName_Url, "post", function(loader, response) {
                var cheJsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (cheJsonObj.success != true) {
                	dhtmlx.message(getMessage("form_field_exist", "显示名称"));
                    return;
                } else {
		            var CHE_Name_Url = AppActionURI.columnDefine + "!checkUnique.json?Q_EQ_columnName=" + columnName + "&Q_EQ_tableId=" + nodeId;
		            detailForm.send(CHE_Name_Url, "post", function(loader, response) {
		                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
		                if (jsonObj.success != true) {
		                	dhtmlx.message(getMessage("form_field_exist", "字段名称"));
		                } else {
		                	toolBar.disableItem("submit");
		                    detailForm.send(COLUMN_MODEL_URL + "!save.json", "post", function(loader, response) {
		                    	var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
		                    	if (jsonObj.success != true) {
		                    		dhtmlx.message(jsonObj.message);
								} else {
									pageable = false;
			                    	detailForm.setItemValue("columnName", "");
			                    	detailForm.setItemValue("showName", "");
			                        search();
			                        pageable = true;
			                        dhtmlx.message(getMessage("operate_success"));
				                	toolBar.enableItem("submit");
								}
		                    });
		                }
		            });
                }
            });
        }
    });
}