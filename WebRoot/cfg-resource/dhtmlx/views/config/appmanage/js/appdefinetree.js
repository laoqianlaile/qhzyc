/**
 * 应用定义树初始化
 * @param layoutA
 * @param layoutB
 */
function initAppDefineTree(that){
    var mbbtbar = that.aLayout.attachToolbar();
    mbbtbar.setIconPath(IMAGE_PATH);
    mbbtbar.addDiv("form$pos", 0);
    //mbbtbar.setAlign("right");
    // 
    initConfigType(that);
    // 
    initPhysicalTableTree(that);
}

/**
 * 物理表树
 * @param that
 */
function initPhysicalTableTree(that) {
	var layout = that.aLayout;
	var tree = layout.attachTree();
	tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	// 物理表分类节点
	var ptItem = loadJson(AppActionURI.tableClassification + "!treeNode.json");
	// 视图分类节点
	var vItem = loadJson(AppActionURI.tableClassification + "!treeNodeOfView.json");
	// 这几个分类是需求中固定的
	var treeJson = {id:0, item:[
		{id:"-PT", text: "物理表", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item: ptItem},
		{id:"-V", text: "视图", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child:1, userdata:[{name:"type",content:"0"},{name:"classification",content:"V"}], item: vItem}
	]};
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	tree.setXMLAutoLoading(AppActionURI.tableTree + "!tree.json?E_model_name=tree&P_orders=showOrder&P_filterId=parentId&F_in=name,child&P_UD=type,classification");
	tree.loadJSONObject(treeJson);
	//tree.refreshItem("-1");
	//点击节点刷新右边列表
	tree.attachEvent("onClick", function(nId) {
		app.tableComment  = tree.getItemText(nId);
		app.classification= tree.getUserData(nId, "classification");
		var type = tree.getUserData(nId, "type");
		if (type=="1") {
			// 树节点ID
			app.tableId       = nId;
			if (app.currentConfigType == "menu" && "1" != app.menuType) {
				initHelpContent(app.contentLayout);
			} else {
				//loadAppDefine(app.contentLayout);
				initMenuContent(that);
			}
		} else {
			// 树节点ID
			app.tableId       = "-1";
			initHelpContent(app.contentLayout);
		}
	});
}

/**
 * 逻辑表树
 * @param that
 */
function initLogicTableTree(that) {
	var layout = that.aLayout;
	var tree = layout.attachTree();
	tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	// 这几个分类是需求中固定的
	var treeJson = {id:0, item:[
		{id:"-1", text: "逻辑表组", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child: true}
	]};
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	tree.setXMLAutoLoading(AppActionURI.tableTree + "!logicTableTree.json");
	tree.loadJSONObject(treeJson);
	//tree.refreshItem("-1");
	//点击节点刷新右边列表
	tree.attachEvent("onClick", function(nId) {
		app.tableComment  = tree.getItemText(nId);
		app.classification= tree.getUserData(nId, "classification");
		var type = tree.getUserData(nId, "type");
		if (type=="1") {
			// 树节点ID
			app.tableId       = nId;
			if (app.currentConfigType == "menu" && "1" != app.menuType) {
				initHelpContent(app.contentLayout);
			} else {
				loadAppDefine(app.contentLayout);
			}
		} else {
			// 树节点ID
			app.tableId       = "-1";
			initHelpContent(app.contentLayout);
		}
	});
}
/**
 * 配置方式
 * @param that
 */
function initConfigType(that) {
	var formJson = [
        {type: "combo", name: "treeStruct", className: "dhx_toolbar_form", label: "树结构：", style:"font-size:11px;", width: 80, 
        	options:[{value: "physical", text:"物理表"},
                     {value: "logic", text:"逻辑表"}]}/*,
        {type: "newcolumn"},
	    {type: "combo", name: "configType", className: "dhx_toolbar_form", label: "　配置：", style:"font-size:11px;", width: 60, 
	        options:[{value: "module", text:"按构件"},
	                  {value: "menu", text:"按菜单"}]}*/
    ];
 	var form = new dhtmlXForm("form$pos",formJson);
 	form.attachEvent("onChange", function(itemId, value) {
 		if ("treeStruct" == itemId) {
 			app.currentTreeStruct = value;
 			if ("logic" == value) {
 				initLogicTableTree(that);
 			} else {
 				initPhysicalTableTree(that);
 			}
 			app.tableId = "-1";
			initHelpContent(app.contentLayout);
 		} else {
 			app.currentConfigType = value;
 			if ("menu" == value) {
 				initMenuContent(that);
 			} else {
 				app.menuType = false; 
 				app.menuId   = "-1"; 
 				initModuleContent(that);
 			}
 		}
 	} );
}

/**
 * 应用定义树初始化
 * @param layoutA
 * @param layoutB
 */
function initAppMenuTree(that, layout){
	var tree = layout.attachTree();
	tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	// 这几个分类是需求中固定的
	var treeJson = {id:0, item:[
		{id:"-M", text: "构件全局设置", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open : true, item: []},
		{id:"-1", text: "系统菜单树", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", item: []}
	]};
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	var mTreeUrl = contextPath + "/menu/menu!getAppMenuTree.json?E_model_name=tree&F_in=name,hasChild&P_UD=bindingType&P_filterId=parentId&P_orders=showOrder&P_tableId=";
	if (isNotEmpty(app.tableId) && "-1" !== app.tableId) {
		mTreeUrl += app.tableId;
	}
	tree.setXMLAutoLoading(mTreeUrl);
	tree.loadJSONObject(treeJson);
	tree.refreshItem("-1");
	// tree.selectItem("-M");
	//点击节点刷新右边列表
	tree.attachEvent("onClick", function(nId) {
		// 树节点ID
		var type = tree.getUserData(nId, "bindingType");
		app.menuType = null2empty(type);
		if ("-M" == nId || type == "1") {
			app.menuId = ("-M" == nId ? "-1" : nId);
			if (isEmpty(app.tableId) || "-1" == app.tableId) {
				initHelpContent(app.contentLayout);
			} else {
				loadAppDefine(app.contentLayout);
			}
		} else {
			app.menuId = "-1";
			initHelpContent(app.contentLayout);
		}
	});
}

/**
 * 应用定义树初始化
 * @param layoutA
 * @param layoutB
 */
function initCopyMenuTree(tree) {
	tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	// 这几个分类是需求中固定的
	var treeJson = {id:0, item:[
		{id:"-1", text: "系统菜单树", nocheckbox: "1", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", item: []}
	]};
	tree.setDataMode("json");
	tree.enableCheckBoxes(1);
	tree.enableSmartXMLParsing(true);
	var mTreeUrl = contextPath + "/menu/menu!getApplyMenuTree.json?P_tableId=";
	if (isNotEmpty(app.tableId) && "-1" !== app.tableId) {
		mTreeUrl += app.tableId;
	}
	tree.setXMLAutoLoading(mTreeUrl);
	tree.loadJSONObject(treeJson);
	tree.refreshItem("-1");
	// tree.selectItem("-M");
	//点击节点刷新右边列表
	/*tree.attachEvent("onClick", function(nId) {
		// 树节点ID
		var type = tree.getUserData(nId, "bindingType");
		if (type == "1") {
			loadGridData(grid, gcfg, (gurl + "&P_menuId=" + nId));
		} else {
			grid.clearAll();
		}
	});*/
}
