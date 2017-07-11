/**
 * 报表定义树初始化
 * @param layoutA
 * @param layoutB
 */

function initCellReportTree(layoutA, layoutB){
	
	var _this = this;
	var treeMenuId;
	
	var moduleUrl = AppActionURI.report;	
	layoutA.setText("报表定义树");
	layoutA.setWidth(240);
	tree = layoutA.attachTree();
	//tree.setStdImages("child.gif","parent.gif","parent.gif");
	//tree.enableHighlighting(1);
	tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	
	//初始右键菜单
	var treeMenu = new dhtmlXMenuObject();
	treeMenu.renderAsContextMenu();
	treeMenu.loadXMLString(getMenuXml());
	treeMenu.attachEvent("onClick", function(id) {
		treeMenuId = id;
		if (id == "add_t") {
			_this.openCellReportWin("新增分类", id);
		} else if (id == "upd_t") {
			_this.openCellReportWin("修改分类", id);
		} else if (id == "del_t") {
			dhtmlx.confirm({
				type:"confirm",
				text: "确定要删除分类节点吗？",
				ok: "确定",
				cancel: "取消",
				callback: function(flag) {
					if (flag) {
						MODEL_URL = moduleUrl;
						var url = MODEL_URL + "!checkUnique.json?Q_EQ_parentId=" + nodeId;
						// check if have children node
						dhtmlxAjax.get(url,function(loader) {
							var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
							if (jsonObj.success == false) {
								tree.openItem(nodeId);
								dhtmlx.alert("请删除该节点下的所有子节点，再删除该节点！");
							} else {
								deleteById(nodeId, _this.delCallback);
							}
						});
					}else{
						return;
					}
				}
			});
		} else if (id == "add_e") {
			_this.openCellReportWin("新增报表", id);
		} else if (id == "upd_e") {
			_this.openCellReportWin("修改报表", id);
		} else if (id == "del_e") {
			dhtmlx.confirm({
				type:"confirm",
				text: "确定要删除报表节点吗？",
				ok: "确定",
				cancel: "取消",
				callback: function(flag) {
					if (flag) {
						MODEL_URL = moduleUrl;
						deleteById(nodeId, _this.delCallback);
					}else{
						return;
					}
				}
			});
		}
	});
	
	// 这几个分类是需求中固定的
	var treeJson = {id:0, item:[
                 {id:'-1',text:"报表定义树", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child:1, userdata:[{name:"type",content:"0"}]}
             ]};
	tree.enableContextMenu(treeMenu);
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	tree.setXMLAutoLoading(moduleUrl + "!tree.json?E_model_name=tree&P_orders=showOrder&P_filterId=parentId&F_in=name,child&P_UD=type");
	tree.loadJSONObject(treeJson);
	tree.refreshItem("-1");
	tree.attachEvent("onBeforeContextMenu", function(nId) {
		nodeId = nId;
		type = tree.getUserData(nId,"type");
		if (nId == "-1") {
			treeMenu.showItem("add_t");
			treeMenu.showItem("add_e");
			treeMenu.hideItem("upd_t");
			treeMenu.hideItem("upd_e");
			treeMenu.hideItem("del_t");
			treeMenu.hideItem("del_e");
		} else if(type == "1"){
			treeMenu.hideItem("add_t");
			treeMenu.hideItem("upd_t");
			treeMenu.hideItem("del_t");
			treeMenu.hideItem("add_e");
			treeMenu.showItem("upd_e");
			treeMenu.showItem("del_e");
		} else {
			treeMenu.showItem("add_t");
			treeMenu.showItem("upd_t");
			treeMenu.showItem("del_t");
			treeMenu.showItem("add_e");
			treeMenu.hideItem("upd_e");
			treeMenu.hideItem("del_e");
		}
		return true;
	});
	//点击节点刷新右边列表
	tree.attachEvent("onClick", function(nId) {
		// 树节点ID
		nodeId = nId;
		// 节点类型
		type = tree.getUserData(nId,"type");
		if (type=="1"){
			initLayoutBContent(layoutB);
		}else{
			initLayoutBHelp(layoutB);
		}
	});
		
	this.openCellReportWin = function(title, menuId) {
		var win = createDhxWindow({id:"win$cellreprot",title:title,width:360,height:180});
		var form = win.attachForm(getFormJson(menuId));
		var winsbar = win.attachStatusBar();
		var winbtbar = new dhtmlXToolbarObject(winsbar.id);
		winbtbar.setIconPath(TOOLBAR_IMAGE_PATH);
		winbtbar.addButton("save", 1, "保存", "save.gif");
		winbtbar.addSeparator("separator$01", 2);
		winbtbar.addButton("saveAndCreate", 3, "保存并新增", "save.gif");
		winbtbar.addSeparator("separator$01", 4);
		winbtbar.addButton("close", 5, "关闭", "close.gif");
		winbtbar.setAlign("right");
		winbtbar.attachEvent("onClick", function(itemId) {
			if ("close" == itemId) {
				win.close();
			} else if ("save" == itemId) {
				save("u");
			} else if ("saveAndCreate" == itemId) {
				save("c");
			}
		});
		// load form data
		if ("upd_t" == menuId || "upd_e" == menuId) {
			var updateUrl = moduleUrl + "/" + nodeId + ".json";
			var formData = loadJson(updateUrl);
			form.setFormData(formData);
		}
		/**
		 * @param op 
		 *        c -- 保存并新增
		 *        u -- 保存
		 */
		function save(op) {
			var id = form.getItemValue("id")
			var url = moduleUrl + "!checkUnique.json";
			var params = "id=" + id + "&Q_EQ_name=" + form.getItemValue("name") + "&Q_EQ_parentId=" + nodeId
			dhtmlxAjax.post(url, params, function(loader) {
				var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
				if (false == jsonObj.success) {
					if ("OK" == jsonObj.message) {
						dhtmlx.alert(getMessage("form_field_exist", "报表名称"));
					} else {
						dhtmlx.alert("唯一检查出错，请联系管理员！");
					}
				} else {
					form.send(moduleUrl+"!update.json", "post", function(loader, response) {
						var formData = eval("(" + loader.xmlDoc.responseText + ")");
						if ("c" == op) {
							form.setItemValue("id", "");
							form.setItemValue("name", "");
							form.setItemValue("showOrder", "");
						}
						var refreshId = nodeId;
						if (treeMenuId.indexOf("upd") > -1) {
							refreshId = tree.getParentId(nodeId);
						}
						tree.refreshItem(refreshId);
						//tree.selectItem(formData.id, true);
					});
				}
			});
		}
	};
	
	// delete tree node callback function
	this.delCallback = function(loader) {
		var refreshId = tree.getParentId(nodeId);
		tree.refreshItem(refreshId);
	};
}

/**
 * 右键菜单XML
 * @returns {String}
 */
function getMenuXml() {
	var xml = "<menu>" +
			"<item id='add_t' text='新增分类' />	" +
			"<item id='upd_t' text='修改分类' />" +
			"<item id='del_t' text='删除分类' />" +
			"<item id='add_e' text='新增报表' />" +
			"<item id='upd_e' text='修改报表' />" +
			"<item id='del_e' text='删除报表' /></menu>";
	return xml;
}
/**
 * 表单JSON
 * @param type
 * @returns {Array}
 */
function getFormJson(menuId) {
	var nameLabel = "分类", typeValue = "0";
	if ("add_e" == menuId || "upd_e" == menuId) {
		nameLabel = "报表";
		typeValue = "1";
	}
	var formJson = [
		{type: "setting", labelWidth: 80, inputWidth: 160},
		{type: "hidden", name: "_method"},
		{type: "hidden", name: "id"},
		{type: "hidden", name: "parentId", value: nodeId},
		{type: "hidden", name: "type", value: typeValue},
		{type: "input", label: nameLabel+"名称", name: "name", offsetLeft:20, offsetTop:20, required:true, width:160, maxLength:24, tooltip:"请输入显示名称!"},
		{type: "input", label: "显示顺序：", name: "showOrder", offsetLeft:20, offsetTop:10, validate:"ValidNumeric", width:160, maxLength:4, tooltip:"请输入整数!"}
	];
	return formJson;
}