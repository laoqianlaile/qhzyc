/**
 * 逻辑表字段复制配置
 * @param win
 * @returns {initAppButtonWin}
 */
function initTableCopyWinTo(win,rowIdsTo,tableId){
	var _this = this;
	var tabbar = win.attachTabbar();
    tabbar.setImagePath(IMAGE_PATH);
    //物理表树
	tabbar.addTab("tab$table$01", "物理表树", "130px");
	physicalTree = tabbar.cells("tab$table$01").attachTree();
	var subWinTtbar = tabbar.cells("tab$table$01").attachToolbar();
	subWinTtbar.setIconPath(TOOLBAR_IMAGE_PATH);
	subWinTtbar.addButton("bottom$checkout", 0, "选择关联物理表", "checkout.gif");
	subWinTtbar.attachEvent("onClick", function(itemId, value) {
		if ("bottom$checkout" == itemId) {
			selectPhysicalTable(physicalTree, tableId);
		}
	});
	loadPhysicalTableTree();
	//逻辑表树
	tabbar.addTab("tab$table$02", "逻辑表树", "130px");
	logicTree = tabbar.cells("tab$table$02").attachTree();
	loadLogicTableTree();
	tabbar.setTabActive("tab$table$01");
	var mbsbar = win.attachStatusBar();
	var mbbtbar = new dhtmlXToolbarObject(mbsbar.id);
	mbbtbar.setIconPath(IMAGE_PATH);
	mbbtbar.addButton("bottom$save", 0, "保存", "save.gif");
	mbbtbar.addSeparator("bottom$septr$01", 1);
	mbbtbar.addButton("bottom$close", 4, "关闭", "default/close.png");
	mbbtbar.setAlign("right");
	mbbtbar.attachEvent("onClick", function(itemId) {
		if ("bottom$close" == itemId) {
			win.close();
		} else if ("bottom$save" == itemId) {
			var rowIds = null;
			var physicalTableIds = physicalTree.getAllChecked();
			if (physicalTableIds != null && physicalTableIds != "") {
				var physicalTableIdArr = physicalTableIds.split(",");
				for ( var i = 0; i < physicalTableIdArr.length; i++) {
					if (physicalTree.getUserData(physicalTableIdArr[i], "type") != "1") {
						dhtmlx.alert(physicalTree.getItemText(physicalTableIdArr[i]) + "不是表节点，请选择表节点复制字段!");
						return;
					}
				}
				rowIds += "," + physicalTableIds;
			}
			var logicTableData = logicTree.getAllChecked();
			var logicTableIds = null;
			if (logicTableData != null && logicTableData != "") {
				var logicTableIdArr = logicTableData.split(",");
				for ( var i = 0; i < logicTableIdArr.length; i++) {
					if (logicTree.getUserData(logicTableIdArr[i], "type") != "1") {
						dhtmlx.alert(logicTree.getItemText(logicTableIdArr[i]) + "不是表节点，请选择表节点复制字段!");
						return;
					}
					logicTableIds += "," + logicTableIdArr[i].substring(3);
				}
				rowIds += "," + logicTableIds;
			}
			if (rowIds == null || rowIds == "") {
				dhtmlx.alert("请选择任意一张或多张表来添加字段!");
				return;
			}
			dhtmlx.confirm({
				type:"confirm",
				text: "确定要复制到所选记录吗？",
				ok: "确定",
				cancel: "取消",
				callback: function(flag) {
					if (flag) {
						var CHE_Name_Url = AppActionURI.columnDefine + "!checkColumn2.json?Q_EQ_Ids=" + rowIdsTo + "&Q_EQ_tableId=" + rowIds;
						dhtmlxAjax.get(CHE_Name_Url, function(loader) {
					    	var obj = eval("(" + loader.xmlDoc.responseText + ")");
							if (obj.success == false) {
								var names="";
								var repeatIds = obj.message;
								var repeatIdArr=repeatIds.split(",");
								for(var i=0;i<repeatIdArr.length;i++){							
									var repeatId=repeatIdArr[i];
									if(names!=""){
										names += ",";
									}
									names += _this.dataGrid.cells(repeatId, 1).getValue();
								}
								dhtmlx.alert("【" + names + "】字段名称已存在!");
								return;
							}else {
								var Copy_Url= AppActionURI.columnDefine + "!columnCopy2.json?Q_EQ_Ids=" + rowIdsTo + "&Q_EQ_tableId=" + rowIds;
								dhtmlxAjax.get(Copy_Url, function(loader) {
									dhtmlx.message(getMessage("save_success"));
									win.close();							
								});	
							}
						});
					}
				}
			});
		}
	});
	function loadPhysicalTableTree(){
		var moduleUrl = AppActionURI.tableTree;
		physicalTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
		physicalTree.attachEvent("onMouseIn", function(id) {
			physicalTree.setItemStyle(id, "background-color:#D5E8FF;");
		});
		physicalTree.attachEvent("onMouseOut", function(id) {
			physicalTree.setItemStyle(id, "background-color:#FFFFFF;");
		});
		physicalTree.enableCheckBoxes(1);
		// 物理表分类节点
		var ptItem = loadJson(AppActionURI.tableClassification + "!openedTreeNode.json");
		_this.prefixObj = {};
		for (var i = 0; i < ptItem.length; i++){
			var item = ptItem[i];
			_this.prefixObj[item.userdata[1].content] = item.prefix;
		}
		var treeJson = {id:0, item:[
			{id:"-PT", text: "物理表", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item: ptItem}
		]};
		physicalTree.setDataMode("json");
		physicalTree.enableSmartXMLParsing(true);
		physicalTree.setXMLAutoLoading(moduleUrl+"!getCopyToPhysicalTableTree.json?E_model_name=tree&P_orders=showOrder&P_filterId=parentId&F_in=name,child&P_UD=type,classification,tableLabel&P_columnIds=" + rowIdsTo);
		physicalTree.loadJSONObject(treeJson);
	}
	function loadLogicTableTree(){
		var moduleUrl = AppActionURI.tableTree;
		logicTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
		logicTree.attachEvent("onMouseIn", function(id) {
			logicTree.setItemStyle(id, "background-color:#D5E8FF;");
		});
		logicTree.attachEvent("onMouseOut", function(id) {
			logicTree.setItemStyle(id, "background-color:#FFFFFF;");
		});
		logicTree.enableCheckBoxes(1);
		var treeJson = {id:0, item:[
			{id:"-LT", text: "逻辑表", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, child:1, userdata:[{name:"type",content:"0"},{name:"classification",content:"LT"}]}
		]};
		logicTree.setDataMode("json");
		logicTree.enableSmartXMLParsing(true);
		logicTree.setXMLAutoLoading(moduleUrl+"!getCopyToLogicTableTree.json?P_columnIds=" + rowIdsTo);
		logicTree.loadJSONObject(treeJson);
	}
	function selectPhysicalTable(physicalTree, tableId){
		var tables = loadJson(AppActionURI.physicalTableDefine+"!search.json?Q_EQ_logicTableCode=" + tableId + "&F_in=id").data;
		for ( var i = 0; i < tables.length; i++) {
			physicalTree.setCheck(tables[i].id, true);
		}
	}
}
