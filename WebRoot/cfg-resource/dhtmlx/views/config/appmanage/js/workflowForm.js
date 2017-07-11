/**
 * 表单初始化
 */
function initWfForm(layout, that) {
	// 而在设置
	layout.hideHeader();
	layout.setHeight(260);
	// 初始化变量
	that.ftbar = layout.attachToolbar();
	that.form  = layout.attachForm(getFormJson(that));
	
	var _this = this;
	var ftbar = that.ftbar;
	var form  = that.form;
	// 
	ftbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	//ftbar.addButton("add", 1, "保存", "new.gif");
	//ftbar.addSeparator("top$septr$01", 2);
	ftbar.addButton("save", 3, "保存", "save.gif");
	//ftbar.addSeparator("top$septr$01", 2);
	ftbar.attachEvent("onClick", function(itemId) {
		if ("save" == itemId) {
			_this.save();
		}
	});
	// save
	this.save = function () {
		var id  = form.getItemValue("id");
		var name= form.getItemValue("name");
		if (null == name || "" == name) {
			dhtmlx.alert("名称不可为空！");
			return;
		}
		var type = form.getItemValue("type");
		var typelabel = "分类";
		if("1" == type){
			typelabel = "工作流";
		}
		var checkUrl = that.moduleUrl + "!checkUnique.json?id=" + id + "&Q_EQ_name=" + name + "&Q_EQ_parentId=" + that.nodeId +"&Q_EQ_type=" + type;
		dhtmlxAjax.get(encodeURI(checkUrl),function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (!jsonObj.success) {
				if ("OK" == jsonObj.message) {
					dhtmlx.alert("您填写的" + typelabel + "名称已存在，请修改！");
				} else {
					dhtmlx.alert("唯一检查出错，请联系管理员！");
				}				
			} else {
				if ("1" === type) {
					checkUrl = that.moduleUrl + "!checkCoflow.json?packageId=" + form.getItemValue("packageId") + "&processId=" + form.getItemValue("processId");
					var rlt = loadJson(checkUrl);
					if (!rlt.success) {
						dhtmlx.alert(rlt.message); 
						return;
					}
				}
				
				form.send(that.moduleUrl + ".json", "post", function(loader, response) {
					var formData = eval("(" + loader.xmlDoc.responseText + ")");
					if (formData.id == null || formData.id == "") {
						dhtmlx.message(getMessage("save_failure"));
						return;
					}
					form.setFormData(formData);
					that.reloadGrid();
					that.reloadTreeItem();
					dhtmlx.message(getMessage("save_success"));
				});
			}
		});
	};

	form.attachEvent("onChange", function (id, value){
	     if ("type" == id) {
	    	 that.hidePPId(value);
	    	 if("0" == value){
	    		 form.setRequired("tableId",false);
	    		 form.setRequired("packageId",false);
	    		 form.setRequired("processId",false);
	    	 }else{
	    		 form.setRequired("tableId",true);
	    		 form.setRequired("packageId",true);
	    		 form.setRequired("processId",true);
	    	 }
	     }
	});
	that.hidePPId(form.getCheckedValue("type"));
	
	var combo = form.getCombo("tableId");
	var url   = AppActionURI.tableDefine + "!comboOfTables.json";
	combo.addOption(loadJson(url));
}

/**
 * 列表数据加载URL
 */
function getFormJson(that) {
	var formJson = [
   		{type: "setting", labelWidth: 80, inputWidth: 200},
   		{type: "hidden", name: "_method"},
   		{type: "hidden", name: "id"},
   		{type: "hidden", name: "parentId", value: that.nodeId},
   		{type: "hidden", name: "showOrder"},
   		{type: "hidden", name: "started"},
   		{type: "block", name:"block_name", width: 600, list:[
				{type: "input", label: "名称", labelAlign:"right", labelWidth: 80, name: "name", offsetTop:20, required:true, width:200, maxLength:25, tooltip:"请输入名称!"},
				{type: "newcolumn"},
				{type: "itemlabel", label: "类　型：", labelAlign:"right", labelWidth: 80, offsetTop:20, width:80},
				{type: "newcolumn"},
        		{type: "radio", label: "分类", offsetTop:20,  width:80, name: "type", value:"0", position:"label-right", labelAlign:"left", checked: true},
				{type: "newcolumn"},
        		{type: "radio", label: "工作流", offsetTop:20, width:80, name: "type", value:"1", position:"label-right", labelAlign:"left"}
      		]},
       	{type: "block", name: "block_table", width: 600, list:[
				{type: "combo", label: "表", labelAlign:"right", labelWidth: 80, name: "tableId", readonly:false, width:200}
   			]},
   		{type: "block", name: "block_ppid", width: 600, list:[
				{type: "input", label: "包ID", labelAlign:"right", labelWidth: 80, name: "packageId", required:false, width:200, maxLength:30},
				{type: "newcolumn"},
				{type: "input", label: "流程ID", labelAlign:"right", labelWidth: 80, name: "processId", required:false, width:200, maxLength:30},
   			]},
   		{type: "block", name: "block_remark", width: 600, list:[
				{type: "input", label: "备注：", labelAlign:"right", labelWidth: 80, name: "remark", rows:3, width:480, maxLength:100}
      		]}
   	];
	return formJson;
}