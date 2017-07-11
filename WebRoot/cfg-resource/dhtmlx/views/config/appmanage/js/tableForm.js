/**
 * 
 * @param that
 * @param layout
 */
function initTableForm(that, layout) {
	layout.hideHeader();
	layout.setHeight(200);
	
	var moduleUrl = AppActionURI.tableDefine;
	
	var ttbar = layout.attachToolbar();
	
	ttbar.setIconsPath(IMAGE_PATH);
	ttbar.addButton("new", 1, "新增", "new.gif");
    ttbar.addSeparator("septr$01", 2);
	ttbar.addButton("submit", 3, "保存", "save.gif");
	if (classification == 'V') { // 视图不可以新增
		ttbar.disableItem("new");
	} else {
		ttbar.enableItem("new");
	}
	ttbar.attachEvent("onClick", function(id) {
		if ("new" == id) {
			that.initForm();
		} else if ("submit" == id) {
			save();
		}
	});
	
	var curForm = layout.attachForm(getFormJson());
	curForm.hideItem("block_table");
	curForm.hideItem("template");
	curForm.hideItem("tableLable");
	
	//初始化combo
	var url = AppActionURI.tableDefine + "!comboOfTables2TableCopy.json";
	var opts = loadJson(url);
	var combo = curForm.getCombo("template");
	combo.addOption(opts);
	
	
	var dataJson = loadJson(AppActionURI.tableLabel + "!search.json?F_in=code,name");
	var t_combo = curForm.getCombo("tableLable");
	for (var i = 0; i < dataJson.data.length; i++) {
		t_combo.addOption(dataJson.data[i].code,dataJson.data[i].name);
	}
	
	curForm.attachEvent("onChange", function(id, val) {
		if ("type" == id || Classification.TEMPLATE == classification) {
			if ("0" == val) {
				//curForm.setRequired("tablePrefix", false);
				curForm.hideItem("block_table");
				curForm.hideItem("template");
				curForm.hideItem("tableLable");

				curForm.setItemValue("tableName", "");
			} else {
				curForm.showItem("tableLable");
				if (Classification.TEMPLATE == classification) {
					curForm.hideItem("block_table");
					curForm.hideItem("template");
					curForm.setItemValue("tableName", "");
				} else {
					curForm.showItem("block_table");
					if (Classification.VIEW == classification) {
						curForm.hideItem("template");
						curForm.hideItem("tablePrefix");
						curForm.setItemWidth("tableName", 200);
					} else {
						curForm.showItem("template");
						curForm.showItem("tablePrefix");
						curForm.setItemWidth("tableName", 158);
					}
				}
			}
		} else if ("tableName" == id) {
			curForm.setItemValue("tableName", val.toUpperCase());
		}
	});
	// 查看或修改
	that.setFormData = function(id) {
		ttbar.enableItem("submit");
		var url = moduleUrl + "/" + id + ".json?_method=get";
		var data = loadJson(url);
		if (null == data) {
			return;
		}
		if ("0" == data.type) {
			curForm.enableItem("type","0");
			curForm.disableItem("type","1");
			curForm.hideItem("block_table");
			curForm.hideItem("template");
			//curForm.setRequired("tablePrefix", false);
		} else {
			curForm.enableItem("type","1");
			curForm.disableItem("type","0");
			if (Classification.TEMPLATE == data.classification) {
				curForm.hideItem("block_table");
				curForm.hideItem("template");
				//curForm.setRequired("tablePrefix", false);
			} else {
				curForm.showItem("block_table");
				if (Classification.VIEW == data.classification) {
					//curForm.setRequired("tablePrefix", false);
					curForm.hideItem("tablePrefix");
					curForm.setItemWidth("tableName", 200);
				} else {
					curForm.showItem("tablePrefix");
					curForm.setItemWidth("tableName", 158);
				}
				curForm.setReadonly("tableName", true);
			}
		}
		
		curForm.setFormData(data);
	};
	/** 初始化表单*/
	that.initForm = function() {
		curForm.enableItem("type","0");
		curForm.enableItem("type","1");
		curForm.checkItem("type","0");
		
		curForm.hideItem("block_table");
		curForm.hideItem("template");
		curForm.hideItem("tableLable");
		
		curForm.setItemValue("id", "");
		curForm.setItemValue("parentId", nodeId);
		curForm.setItemValue("created", "0");
		curForm.setItemValue("classification", classification);
		curForm.setItemValue("text", "");
		curForm.setItemValue("tablePrefix", getTablePrefix(classification));
		curForm.setItemValue("tableName", "");
		curForm.setReadonly("tableName", false);
	};
	/** 表单工具条按钮*/
	that.buttonForm = function() {
		if (classification == 'V') { // 视图不可以新增
			ttbar.disableItem("new");
			ttbar.disableItem("submit");
		} else {
			ttbar.enableItem("new");
			ttbar.enableItem("submit");
		}
	};
	/** 表单保存*/
	function save() {
		var _ck = curForm.getCombo("template").getSelectedValue();
		var id  = curForm.getItemValue("id");
		var type = curForm.getCheckedValue("type");
		if ("0" == type || "" != id || Classification.TEMPLATE == classification) {
			submit(id);
			return;
		}
		var prefix    = curForm.getItemValue("tablePrefix");
		var tableName = curForm.getItemValue("tableName");
		if (null == tableName || "" == tableName) {
			dhtmlx.alert("表名称不可为空！");
			return;
		}
		var checkUrl = moduleUrl + "!checkUnique.json?id=" + id + "&Q_EQ_tableName=" + tableName.toUpperCase() + "&Q_EQ_tablePrefix=" + prefix;
		dhtmlxAjax.get(checkUrl,function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (false == jsonObj.success) {
				if ("OK" == jsonObj.message) {
					dhtmlx.message(getMessage("form_field_exist", "表名称"));
				} else {
					dhtmlx.alert("唯一检查出错，请联系管理员！");
				}
			} else {//*/
				submit();
			}
		});//*/
	};
	/** 表单提交*/
	function submit(id) {
		var url = moduleUrl;
		var checkUrl = url;
		var parentId = curForm.getItemValue("parentId");
		var text = curForm.getItemValue("text");
		var	type = curForm.getItemValue("type");
		if (id) { // modify
			curForm.setItemValue("_method", "put");
			url += "/" + id + ".json";
			checkUrl += "!checkUnique.json?id=" + id + "&Q_EQ_parentId=" + parentId + "&Q_EQ_text=" + text + "&Q_EQ_type=" +type;
		} else {       // create
			curForm.setItemValue("_method", "post");
			url += ".json";
			checkUrl += "!checkUnique.json?Q_EQ_parentId=" + parentId + "&Q_EQ_text=" + text + "&Q_EQ_type=" +type;
		}
		curForm.send(checkUrl,"post", function(loader, response){
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (false == jsonObj.success) {
				if ("OK" == jsonObj.message) {
					dhtmlx.message(getMessage("form_field_exist", "同级目录下该显示名称"));
				} else {
					dhtmlx.message("唯一检查出错，请联系管理员！");
				}
			} else{
				curForm.send(url, "post", function(loader, response) {
					var formData = eval("(" + loader.xmlDoc.responseText + ")");
					if (formData.id == null || formData.id == "") {
						dhtmlx.message(getMessage("save_failure"));
						return;
					}
					curForm.setFormData(formData);
					that.reloadGrid();
					that.reloadTreeItem();
					if ("0" == formData.type) {
						curForm.disableItem("type", "1");
					} else {
						curForm.disableItem("type", "0");
					}
					var _ck = curForm.getCombo("template").getSelectedValue();
					if(_ck && type==1){
						//复制表的操作
						var url = AppActionURI.columnDefine + "!copyFileds2newTableByTemplatTable?templateTable_id="+_ck+"&newTable_id="+formData.id+"";
						dhtmlxAjax.get(url,function(loader){
							var formData = eval("(" + loader.xmlDoc.responseText + ")");
							if (!formData.status) {
								dhtmlx.message("复制表失败！");
								return;
							}
							dhtmlx.message(getMessage("save_success"));
						});
					}
				});
			}
		});		
	}
	/** 获取表前缀*/
	function getTablePrefix(classification) {
		if (Classification.ARCHIVE == classification) {
			return TablePrefix.ARCHIVE;
		} else if (Classification.DEFINE == classification) {
			return TablePrefix.DEFINE;
		} else if (Classification.PRESET == classification) {
			return TablePrefix.PRESET;
		} else if (Classification.TEMPLATE == classification) {
			return TablePrefix.TEMPLATE;
		} else if (Classification.VIEW == classification) {
			return TablePrefix.VIEW;
		}
	};
	/** 表单格式*/
	function getFormJson() {
		return [
		   		{type: "setting", labelWidth: 80, inputWidth: 200, labelAlign:"right"},
				{type: "hidden", name: "_method"},
				{type: "hidden", name: "id"},
				{type: "hidden", name: "classification", value: classification},
				{type: "hidden", name: "created",value:"0"},
				{type: "hidden", name: "parentId", value: nodeId},
				{type: "hidden", name: "showOrder"},
				{type:"block",list:[
					{type: "block", width:320, list:[
						{type: "itemlabel", label: "　　类型", labelWidth:80},
						{type: "newcolumn"},
		 				{type: "radio", name: "type", label: "分类", value:"0", labelWidth:80, position:"label-right", labelAlign:"left", checked: true},
						{type: "newcolumn"},
		 				{type: "radio", name: "type", label: "表", value:"1", labelWidth:80, position:"label-right", labelAlign:"left"}
					]},
					{type: "block", width:320, list:[
		 				{type: "input", label: "显示名称", labelWidth:80, name: "text", required:true, width:200, maxLength:24}
		 			]},
					{type: "block", name: "block_table", width:320, list:[
	  					{type: "itemlabel", label: "　表名称", labelWidth:80},
						{type: "newcolumn"},
		  				{type: "input", labelWidth:80, name: "tablePrefix", value: getTablePrefix(classification), readonly:true, width:40},
						{type: "newcolumn"},
		 				{type: "input", name: "tableName", width:158, maxLength:20},
		 			]},
		 			
					{type: "combo", label: "　表复制", name: "template",labelWidth:80,width:200},
		 		
					{type: "combo", label: "　表类型", name: "tableLable",labelWidth:80,width:200}
		 			
				]},
				
			];
	}
}
