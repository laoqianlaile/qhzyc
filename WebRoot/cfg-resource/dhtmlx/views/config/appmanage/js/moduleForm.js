/**
 * 模块表单
 * @param that
 */
function initModuleForm(that, moduleId) {
	var _this = this;
	var form;
	var saving = false;
	/**
	 * 初始化表单工具条
	 */
	this.initFormToolbar = function(toolBar) {
	    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
	    toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
	    toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
	    toolBar.setAlign("right");
	    toolBar.attachEvent("onClick", function(id) {
	        if (id == "close") {
	            dhxWins.window(WIN_ID).close();
	        } else if (id == "save") {
	            _this.save();
	        }
	    });
	};
	/**
	 * 保存表单方法
	 */
	this.save = function() {
		if (saving) {
			dhtmlx.message("“保存”正在进行中……，请稍后再操作！");
			return;
		}
    	if (!checkAreaSize()) {
    		dhtmlx.message("区域高度设置请统一使用百分比或像素！");
    		return;
    	}
    	if (!checkPercent()) {
    		dhtmlx.message("区域高度百分比分配有问题，请检查！");
    		return;
    	}
    	if (!form.validate())  return;
        var id = form.getItemValue("id");
		var type = form.getItemValue("type");
		if (type == "3") {
			form.setItemValue("table1Id", "");
			form.setItemValue("table2Id", "");
			form.setItemValue("table3Id", "");
			form.uncheckItem("area1Id","0");
			form.uncheckItem("area1Id","1");
			form.uncheckItem("area1Id","2");
			form.uncheckItem("area2Id","0");
			form.uncheckItem("area2Id","1");
			form.uncheckItem("area2Id","2");
			form.uncheckItem("area3Id","0");
			form.uncheckItem("area3Id","1");
			form.uncheckItem("area3Id","2");
		} else if (type == "4" || type == "5") {
			form.setItemValue("treeId", "");
			var table1Id = form.getItemValue("table1Id");
			var table2Id = form.getItemValue("table2Id");
			var table3Id = form.getItemValue("table3Id");
			var area1Name, area2Name, area3Name;
			var area1Id = form.getItemValue("area1Id");
			var area2Id = form.getItemValue("area2Id");
			var area3Id = form.getItemValue("area3Id");
			if (area1Id == "0") {
			    area1Name = "FORM";
			} else if (area1Id == "0") {
			    area1Name = "GRID";
			}
			if (area2Id == "0") {
			    area2Name = "FORM";
			} else if (area2Id == "0") {
			    area2Name = "GRID";
			}
			if (area3Id == "0") {
			    area3Name = "FORM";
			} else if (area3Id == "0") {
			    area3Name = "GRID";
			}
			if (isNotEmpty(table3Id)) {
				if (table3Id==table1Id) {
				    dhtmlx.message("区域一和区域三不能选择同一张表！");
				    return;
				}
				if ((table3Id+area3Name)==(table2Id+area2Name) || (table3Id+area3Name)==(table1Id+area1Name) || (table1Id+area1Name)==(table2Id+area2Name)) {
				    dhtmlx.message("同一张表不能在不同的区域中都选择列表或表单！");
				    return;
				}
			}
			if (isNotEmpty(table2Id)) {
				var templateType = form.getItemValue("templateType");
				if ("2E_S" != templateType) {
					if ((table1Id+area1Name)==(table2Id+area2Name)) {
						dhtmlx.message("同一张表不能在不同的区域中都选择列表或表单！");
						return;
					}
				}
			}
		} else if (type == "7") {
		    form.setItemValue("treeId", "");
            form.setItemValue("table1Id", "");
            form.setItemValue("table2Id", "");
            form.setItemValue("table3Id", "");
            form.uncheckItem("area1Id","0");
            form.uncheckItem("area1Id","1");
            form.uncheckItem("area1Id","2");
            form.uncheckItem("area2Id","0");
            form.uncheckItem("area2Id","1");
            form.uncheckItem("area2Id","2");
            form.uncheckItem("area3Id","0");
            form.uncheckItem("area3Id","1");
            form.uncheckItem("area3Id","2");
        }
		var ccn = form.getItemValue("componentClassName");
		var cvId= form.getItemValue("componentVersionId");
		var result = eval("(" + loadJson(contextPath + "/component/component!validateFields.json?name=" + encodeURIComponent(ccn) + "&componentVersionId=" + cvId) + ")");
    	if (result.nameExist) {
    		dhtmlx.message(getMessage("form_field_exist", "构件类名"));
    		return;
    	}
    	var result1 = loadJson(that.MODEL_URL + "!checkUnique.json?id=" + id + "&Q_EQ_name=" + form.getItemValue("name"));
		if (result1.success == false) {
			if ("OK" == result1.message) {
				dhtmlx.message(getMessage("form_field_exist", "名称"));
			} else {
				dhtmlx.message("唯一检查出错，请联系管理员！");
			}
			return;
		}
		saving = true;
		if (type == "4" || type == "5" || type == "6") {
            var obj = loadJson(that.MODEL_URL + "!checkModuleBinding.json?id=" + id);
            if (true == obj.success) {
                dhtmlx.confirm({
                    type : "confirm",
                    text : "该模块在组装平台中已经被组装过了，如果修改，则可能需要重新配置相关配置！确定修改吗？",
                    ok : "确定",
                    cancel : "取消",
                    callback : function(flag) {
                        if (flag) {
                            dhtmlx.confirm({
                                type : "confirm",
                                text : "是否删除原先的相关配置，重新设置？",
                                ok : "是",
                                cancel : "否",
                                callback : function(flag) {
                                    if (flag) {
                                        form.setItemValue("updateComponentConfig", "1");
                                    } else {
                                        form.setItemValue("updateComponentConfig", "0");
                                    }
                                    saveModule(form, id);
                                }
                            });
                        } else {
                        	saving = false;
                        }
                    }
                });
            } else {
                form.setItemValue("updateComponentConfig", "1");
                saveModule(form, id);
            }
        } else {
            form.setItemValue("updateComponentConfig", "0");
            saveModule(form, id);
        }
		/** 
		 * 保存
		 */
		function saveModule(form, id) {
			var url = that.MODEL_URL  + ".json";
			if ("" == id) {
				form.setItemValue("_method", "post");
			} else {
				form.setItemValue("_method", "put");
				url = that.MODEL_URL + "/" + id + ".json";
			}
			form.setItemValue("componentAreaId", that.nodeId);
			form.send(url, "post", function(loader, response) {
				var obj = eval("(" + loader.xmlDoc.responseText + ")");
				if (obj.id == null || obj.id == "") {
					dhtmlx.message(getMessage("save_failure"));
					return;
				}
				dhtmlx.message(getMessage("save_success"));
				form.setItemValue("id", obj.id);
				form.setItemValue("componentUrl", obj.componentUrl);
				form.setItemValue("componentVersionId", obj.componentVersionId);
				that.reloadModuleGrid();
				saving = false;
			});
		}
		
		/**
		 * 
		 */
		function checkAreaSize() {
			var templateType = form.getItemValue("templateType"),
			    s1 = form.getItemValue("area1Size"),
			    s2 = form.getItemValue("area2Size"),
			    s3 = form.getItemValue("area3Size"),
			    pReg = /^(\d|[1-9]\d|100)%$/;
			
			if ("2E" == templateType) {
				if (isNotEmpty(s1) && isNotEmpty(s2)) {
					return pReg.test(s1) === pReg.test(s2);
				}
			} else if ("3E" === templateType) {
				if (isNotEmpty(s1) && isNotEmpty(s2) && isNotEmpty(s3)) {
					return (pReg.test(s1) === pReg.test(s2)) && (pReg.test(s1) === pReg.test(s3));
				} else if (isNotEmpty(s1) && isNotEmpty(s2)) {
					return (pReg.test(s1) === pReg.test(s2));
				} else if (isNotEmpty(s1) && isNotEmpty(s3)) {
					return (pReg.test(s1) === pReg.test(s3));
				} else if (isNotEmpty(s2) && isNotEmpty(s3)) {
					return (pReg.test(s2) === pReg.test(s3));
				}
			} else if ("3L" == templateType) {
				if (isNotEmpty(s2) && isNotEmpty(s3)) {
					return pReg.test(s2) === pReg.test(s3);
				}
			}
			return true;
		}
		
		function checkPercent() {
			var templateType = form.getItemValue("templateType"),
			    s1 = form.getItemValue("area1Size"),
			    s2 = form.getItemValue("area2Size"),
			    s3 = form.getItemValue("area3Size"),
			    pReg = /^(\d|[1-9]\d|100)%$/;
			
			if ("2E" == templateType || "2E_S" == templateType) {
				if (isNotEmpty(s1) && isNotEmpty(s2) && pReg.test(s1)) {
					return calc(s1, s2) === 100;
				}
			} else if ("3E" === templateType) {
				if (isNotEmpty(s1) && isNotEmpty(s2) && isNotEmpty(s3) && pReg.test(s1)) {
					return calc(s1, s2, s3) === 100;
				} else if (isNotEmpty(s1) && isNotEmpty(s2) && pReg.test(s1)) {
					return calc(s1, s2) < 100;
				} else if (isNotEmpty(s1) && isNotEmpty(s3) && pReg.test(s1)) {
					return calc(s1, s3) < 100;
				} else if (isNotEmpty(s2) && isNotEmpty(s3) && pReg.test(s2)) {
					return calc(s3, s2) < 100;
				}
			} else if ("3L" == templateType) {
				if (isNotEmpty(s2) && isNotEmpty(s3) && pReg.test(s2)) {
					return calc(s3, s2) === 100;
				}
			}
			return true;
		}
		
		function calc() {
			var ttl = 0, i = 0, len = arguments.length ;
			for (; i < len; i ++) {
				ttl += parseInt(arguments[i].toString().replace("%", ""), 10);
			}
			return ttl;
		}
	};
	this.formJson = {
		format: [
	   		{type: "hidden", name: "id"},
	   		{type: "hidden", name: "updateComponentConfig", value:"1"},
	   		{type: "hidden", name: "componentVersionId"},
	   		{type: "hidden", name: "componentAreaId", value:that.nodeId},
	   		{type: "hidden", name: "showOrder"},
	   		{type: "hidden", name: "_method"},
	   		{type: "fieldset",  name: "FS_base", label: "构件类型", width:750, list:[
	   		    {type: "block", name:"block_base", width: 700, list:[
                    {type: "combo", label: "类型:", name: "type", labelWidth: 80, width: 200, labelAlign:"right", required: true, options:[
						{value: "3", text: "树构件", selected:true},
						{value: "4", text: "物理表构件"},
						{value: "5", text: "逻辑表构件"},
						{value: "6", text: "通用表构件"},
						{value: "7", text: "标签页构件"}
					]},
                    {type:"newcolumn"},
                    {type: "combo", label: "逻辑表组:", name: "logicTableGroupCode", labelWidth: 80, width: 200, labelAlign:"right", required: true}
	    		]}
	   		]},
	   		{type: "fieldset",  name: "FS_slt", label: "构件布局", width:750, list:[
    		    {type: "block", name:"template_block", width: 700, offsetLeft:20, list:[
    		    	{type: "block", name:"template1", width: 200, offsetLeft:20, list:[
	    		    	{type: "img", src: DHX_RES_PATH + "/common/images/2U_TREE.jpg", alt: '树形模板'},
	    				{type: "radio", label: "布局一", labelWidth: 120, name: "templateType", value:"2U", width: 100, offsetLeft:20, labelAlign:"left",position:"label-right"}
	    		    ]}
    		    ]}
	    	]},
    		{type: "fieldset",  name: "FS_cfg", label: "构件信息", width:750, list:[
	    		{type: "block", name:"block_comp", width: 700, list:[
                    {type: "input", label: "构件名称:", name: "name", labelWidth: 80, width: 200, labelAlign:"right", required: true},
                    {type:"newcolumn"},
                    {type: "input", label: "构件类名:", name: "componentClassName", labelWidth: 80, width: 200, labelAlign:"right", validate: "ValidateClassName", tooltip:"构件类名为字母、数字和下划线组成，首字母大写，如DemoTest", maxLength:50, required: true}
	    		]},
	    		{type: "block", name:"block_tree", width: 700, list:[
					{type: "combo", label: "树:", labelWidth: 80, labelAlign:"right", name: "treeId", tooltip:"请从下拉框中选择树，如果下拉框中没有数据，请到树定义模块中配置！", width: 200, required: true}
	    		]},
    			{type: "block", name:"block_area1", width: 700, list:[
	    			{type: "combo", label: "表1:", labelWidth: 80, labelAlign:"right", name: "table1Id", maxLength:50, width: 200, required: true},
					{type:"newcolumn"},
 	    			{type: "input", label: "高度:&nbsp;&nbsp;&nbsp;", tooltip:"高度可以用百分比或像素", labelWidth: 80, labelAlign:"right", name: "area1Size", validate:"ValidateAreaSize", maxLength:50, width: 200}
    			]},
    			{type: "block", name:"block_area1_1", width: 700, list:[
 	    			{type: "itemlabel", label: "区域1：&nbsp;", labelWidth: 80, labelAlign:"right"},
					{type:"newcolumn"},
					{type: "radio", label: "表单界面", name: "area1Id", value:"0", labelWidth: 60, labelAlign:"left", position:"label-right"},
					{type:"newcolumn"},
					{type: "radio", label: "列表界面", name: "area1Id", value:"1", labelWidth: 60, labelAlign:"left", position:"label-right", checked: true},
   					{type:"newcolumn"},
   					{type: "radio", label: "列表界面(含缩略图)", name: "area1Id",value:"2", labelWidth: 120, labelAlign:"left", position:"label-right"},
   					{type:"newcolumn"},
   					{type: "radio", label: "查询区", name: "area1Id",value:"3", labelWidth: 60, labelAlign:"left", position:"label-right"},
   					{type:"newcolumn"},
    			    {type: "checkbox", label: "默认折叠", name: "collapse1", labelWidth: 80, labelAlign:"left", position:"label-right"},
    			    {type:"newcolumn"},
    			    {type: "checkbox", label: "默认缩略图", name: "thumbnail1", labelWidth: 80, labelAlign:"left", position:"label-right"},
    			    {type:"newcolumn"},
    			    {type: "checkbox", label: "拖动排序", name: "sort1", labelWidth: 80, labelAlign:"left", position:"label-right"}
    			]},
    			{type: "block", name:"block_area2", width: 700, list:[
   	    			{type: "combo", label: "表2:", labelWidth: 80, labelAlign:"right", name: "table2Id", maxLength:50, width: 200, readonly: true, required: true},
					{type:"newcolumn"},
					{type: "input", label: "高度:&nbsp;&nbsp;&nbsp;", labelWidth: 80, labelAlign:"right", name:"area2Size", validate:"ValidateAreaSize", maxLength:50, width: 200}
	    		]},
	    		{type: "block", name:"block_area2_1", width: 700, list:[
 	    			{type: "itemlabel", label: "区域2：&nbsp;",labelWidth: 80,labelAlign:"right"},
					{type:"newcolumn"},
					{type: "radio", label: "表单界面", name: "area2Id", value:"0", labelWidth: 60, labelAlign:"left", position:"label-right"},
					{type:"newcolumn"},
					{type: "radio", label: "列表界面", name: "area2Id", value:"1", labelWidth: 60, labelAlign:"left", position:"label-right", checked: true},
   					{type:"newcolumn"},
   					{type: "radio", label: "列表界面(含缩略图)", name: "area2Id",value:"2", labelWidth: 120, labelAlign:"left", position:"label-right"},
					{type:"newcolumn"},
    			    {type: "checkbox", label: "默认缩略图", name: "thumbnail2", labelWidth: 80, labelAlign:"left", position:"label-right"},
    			    {type:"newcolumn"},
    			    {type: "checkbox", label: "拖动排序", name: "sort2", labelWidth: 80, labelAlign:"left", position:"label-right"}
    			]},
	    		{type: "block", name:"block_area3", width: 700, list:[
   	    			{type: "combo", label: "表3:", labelWidth: 80, labelAlign:"right", name: "table3Id", maxLength:50, width: 200, readonly: true, required: true},
					{type:"newcolumn"},
					{type: "input", label: "高度:&nbsp;&nbsp;&nbsp;", labelWidth: 80, labelAlign:"right", name: "area3Size", validate:"ValidateAreaSize", maxLength:50, width: 200}
	    		]},
	    		{type: "block", name:"block_area3_1", width: 700, list:[
 	    			{type: "itemlabel", label: "区域3：&nbsp;",labelWidth: 80,labelAlign:"right"},
					{type:"newcolumn"},
					{type: "radio", label: "表单界面", name: "area3Id", value:"0", labelWidth: 60, labelAlign:"left", position:"label-right"},
					{type:"newcolumn"},
					{type: "radio", label: "列表界面", name: "area3Id", value:"1", labelWidth: 60, labelAlign:"left", position:"label-right", checked: true},
   					{type:"newcolumn"},
   					{type: "radio", label: "列表界面(含缩略图)", name: "area3Id",value:"2", labelWidth: 120, labelAlign:"left", position:"label-right"},
					{type:"newcolumn"},
    			    {type: "checkbox", label: "默认缩略图", name: "thumbnail3", labelWidth: 80, labelAlign:"left", position:"label-right"},
    			    {type:"newcolumn"},
    			    {type: "checkbox", label: "拖动排序", name: "sort3", labelWidth: 80, labelAlign:"left", position:"label-right"}
    			]},
    			{type: "block", name:"block_remark", width: 700, list:[
    			    {type: "input", label: "构件说明:&nbsp;&nbsp;&nbsp;", labelWidth: 80, labelAlign:"right", name: "remark", rows:4, width: 480}
    			]},
    			{type: "block", name:"block_url", width: 700, list:[
    			    {type: "input", label: "URL地址:&nbsp;&nbsp;&nbsp;", labelWidth: 80, labelAlign:"right", name: "componentUrl", rows:4, width: 480, readonly: true}
    			]}
	    	]}
        ],
		settings: {labelWidth: 80, inputWidth: 160}
	};
	if (moduleId) {
		form = openEditWindow(this.formJson, moduleId, this.initFormToolbar, 800, 450);
	} else {
	    form = openNewWindow(this.formJson, this.initFormToolbar, 800, 450);
	}
	form.attachEvent("onBeforeChange", function(id, old_value, new_value) {
	    if (id == "type") {
	    	if (isNotEmpty(moduleId) && old_value!=new_value) {
	    		dhtmlx.message("不能修改构件的类型！");
	    		return false;
	    	}
	    }
	    return true;
	});
	form.attachEvent("onChange", function(id, value) {
	    if (id == "type") {
	        _this.changeType(value);
	    } else if (id == "templateType") {
			var type = form.getItemValue("type");
			_this.changeTemplateType(value, type, false);
		} else if (id == "logicTableGroupCode") {
			_this.changeLogicTableGroupCode(value);
		} else if (id == "table1Id") {
			_this.relateTable(value);
		} else if (id == "table2Id") {
			var table1Id = form.getItemValue("table1Id");
			_this.relateTable(table1Id, value);
		} else if (id == "area1Id" || id == "area2Id" || id == "area3Id") {
		    changeArea(id, value);
		}
	});
	/**
	 * 更改Type时触发
	 */
	this.changeType = function(type, templateType, isInit) {
	    var tree = {type: "block", name:"template_block", width: 700, offsetLeft:20, list:[
		    	{type: "block", name:"template1", width: 200, offsetLeft:20, list:[
    		    	{type: "img", src: DHX_RES_PATH + "/common/images/2U_TREE.jpg", alt: '树形模板'},
    				{type: "radio", label: "布局一", labelWidth: 120, name: "templateType", value:"2U", width: 100, offsetLeft:20, labelAlign:"left",position:"label-right"}
    		    ]}
		    ]};
		var physical = {type: "block", name:"template_block", width: 700, offsetLeft:20, list:[
				{type: "block", name:"template1", width: 150, offsetLeft:20, list:[
    				{type: "img", src: DHX_RES_PATH + "/common/images/1C_PHYSICAL.jpg", alt: '整张页面'},
    				{type: "radio", label: "布局一",  name: "templateType", value:"1C", width: 100, offsetLeft:20, labelAlign:"left",position:"label-right"}
				]},
				{type:"newcolumn"},
				{type: "block", name:"template2", width: 150, offsetLeft:20, list:[
    				{type: "img", src: DHX_RES_PATH + "/common/images/2E_PHYSICAL.jpg", alt: '上下结构'},
	    			{type: "radio", label: "布局二",  name: "templateType", value:"2E", width: 100, offsetLeft:20, labelAlign:"left",position:"label-right"}
				]},
				{type:"newcolumn"},
				{type: "block", name:"template3", width: 150, offsetLeft:20, list:[
    				{type: "img", src: DHX_RES_PATH + "/common/images/3E_PHYSICAL.jpg", alt: '上下结构'},
	    			{type: "radio", label: "布局三",  name: "templateType", value:"3E", width: 100, offsetLeft:20, labelAlign:"left",position:"label-right"}
				]},
				{type:"newcolumn"},
				{type: "block", name:"template4", width: 150, offsetLeft:20, list:[
    				{type: "img", src: DHX_RES_PATH + "/common/images/2E_S_PHYSICAL.jpg", alt: '上下结构'},
	    			{type: "radio", label: "布局四",  name: "templateType", value:"2E_S", width: 100, offsetLeft:20, labelAlign:"left",position:"label-right"}
				]}
			]};
		var logic = {type: "block", name:"template_block", width: 700, offsetLeft:20, list:[
		    	{type: "block", name:"template1", width: 200, offsetLeft:20, list:[
    		    	{type: "img", src: DHX_RES_PATH + "/common/images/1C_LOGIC.jpg", alt: '整张页面'},
    				{type: "radio", label: "布局一", labelWidth: 120, name: "templateType", value:"1C", width: 100, offsetLeft:20, labelAlign:"left",position:"label-right"}
    		    ]},
				{type:"newcolumn"},
				{type: "block", name:"template2", width: 200, offsetLeft:20, list:[
    				{type: "img", src: DHX_RES_PATH + "/common/images/2E_LOGIC.jpg", alt: '上下结构'},
    				{type: "radio", label: "布局二",  name: "templateType", value:"2E", width: 100, offsetLeft:20, labelAlign:"left",position:"label-right"}
				]},
				{type:"newcolumn"},
				{type: "block", name:"template3", width: 200, offsetLeft:20, list:[
    				{type: "img", src: DHX_RES_PATH + "/common/images/3L_LOGIC.jpg", alt: '左上下结构'},
	    			{type: "radio", label: "布局三",  name: "templateType", value:"3L", width: 100, offsetLeft:20, labelAlign:"left",position:"label-right"}
				]}
	    	]};
	    var noTable = {type: "block", name:"template_block", width: 700, offsetLeft:20, list:[
				{type: "block", name:"template1", width: 200, offsetLeft:20, list:[
    				{type: "img", src: DHX_RES_PATH + "/common/images/1C_PHYSICAL.jpg", alt: '整张页面'},
    				{type: "radio", label: "布局一",  name: "templateType", value:"1C", checked:true, width: 100, offsetLeft:20, labelAlign:"left",position:"label-right"}
				]}
			]};
	    form.removeItem("template_block");
	    if (type == "3") {
	        form.showItem("FS_slt");
	        form.addItem("FS_slt", tree, 0);
	        form.hideItem("logicTableGroupCode");
	        form.setItemValue("logicTableGroupCode", "");
	        form.setRequired("logicTableGroupCode", false);
		    form.checkItem("templateType", "2U");
	    } else if (type == "4") {
	        form.showItem("FS_slt");
	        form.addItem("FS_slt", physical, 0);
	        form.hideItem("logicTableGroupCode");
	        form.setItemValue("logicTableGroupCode", "");
	        form.setRequired("logicTableGroupCode", false);
	        form.setItemLabel("table1Id", "表1:");
	        form.setItemLabel("table2Id", "表2:");
	        form.setItemLabel("table3Id", "表3:");
	        form.checkItem("templateType", "1C");
	    } else if (type == "5") {
	        form.showItem("FS_slt");
	        form.addItem("FS_slt", logic, 0);
	        form.showItem("logicTableGroupCode");
	        form.setRequired("logicTableGroupCode", true);
	        form.setItemLabel("table1Id", "逻辑表1:");
	        form.setItemLabel("table2Id", "逻辑表2:");
	        form.setItemLabel("table3Id", "逻辑表3:");
	        form.checkItem("templateType", "1C");
	    } else if (type == "6") {
	        form.showItem("FS_slt");
	    	form.addItem("FS_slt", noTable, 0);
	        form.hideItem("logicTableGroupCode");
	        form.setItemValue("logicTableGroupCode", "");
	        form.setRequired("logicTableGroupCode", false);
	    } else if (type == "7") {
	        form.hideItem("FS_slt");
	        form.hideItem("logicTableGroupCode");
            form.setItemValue("logicTableGroupCode", "");
            form.setRequired("logicTableGroupCode", false);
	    }
	    if (isInit) {
	        if (type == "7") {
                _this.changeTemplateType("", type);
            } else {
                if (templateType) {
                    _this.changeTemplateType(templateType, type, true);
                }
                if (type == "4") {
                    _this.initTable1Combo();
                } else if (type == "5") {
                    var logicTableGroupCode = form.getItemValue("logicTableGroupCode");
                    if (isNotEmpty(logicTableGroupCode)) {
                        _this.initTable1Combo(logicTableGroupCode);
                    }
                }
                var table1Id = form.getItemValue("table1Id");
                var table2Id = form.getItemValue("table2Id");
                var table3Id = form.getItemValue("table3Id");
                if (templateType == "2E" || templateType == "2E_S" || templateType == "3E" || templateType == "3L") {
                    if (isNotEmpty(table1Id)) {
                        this.relateTable(table1Id);
                    }
                    if (isNotEmpty(table2Id)) {
                        form.setItemValue("table2Id", table2Id);
                    }
                }
                if (templateType == "3E" || templateType == "3L") {
                    if (isNotEmpty(table2Id)) {
                    	this.relateTable(table1Id, table2Id);
                    }
                    if (isNotEmpty(table3Id)) {
                        form.setItemValue("table3Id", table3Id);
                    }
                }
            }
	    } else {
		    form.getCombo("table1Id").clearAll();
		    form.setItemValue("table1Id", "");
		    if (type == "3") {
		        _this.changeTemplateType("2U", type);
		    } else if (type == "7") {
		        _this.changeTemplateType("", type);
		    } else {
	    	    _this.changeTemplateType("1C", type);
		    }
	    }
	};
	/**
	 * 更改TemplateType时触发
	 */
	this.changeTemplateType = function(templateType, type, isInit) {
		if (templateType == "2U") {
			form.showItem("block_tree");
			form.hideItem("block_area1");
			form.hideItem("block_area1_1");
			form.hideItem("block_area2");
			form.hideItem("block_area2_1");
			form.hideItem("block_area3");
			form.hideItem("block_area3_1");
			form.setRequired("treeId", true);
			form.setRequired("table1Id", false);
			form.setRequired("table2Id", false);
			form.setRequired("table3Id", false);
			form.setItemValue("table1Id","");
			form.setItemValue("table2Id","");
			form.setItemValue("table3Id","");
		} else if (templateType == "1C") {
		    form.hideItem("block_tree");
			form.showItem("block_area1");
			form.showItem("block_area1_1");
			form.disableItem("area1Size");
			form.setItemLabel("area1Size", "高度:&nbsp;&nbsp;&nbsp;");
			form.setItemValue("area1Size", "100%");
			show2E_S(templateType, isInit);
			changeArea("area1Id", form.getItemValue("area1Id"));
			form.hideItem("block_area2");
			form.hideItem("block_area2_1");
			form.hideItem("block_area3");
			form.hideItem("block_area3_1");
			form.setRequired("treeId", false);
			if (type == "6") {
			    form.setRequired("table1Id", false);
			    form.setItemValue("table1Id", "");
			    form.disableItem("table1Id");
			} else {
				form.enableItem("table1Id");
			    form.setRequired("table1Id", true);
			}
			form.setRequired("table2Id", false);
			form.setRequired("table3Id", false);
			form.setItemValue("treeId","");
			form.setItemValue("table2Id","");
			form.setItemValue("table3Id","");
			if (!isInit) {
				if (type == "4") {
					_this.initTable1Combo();
				} else if (type == "5") {
					var logicTableGroupCode = form.getItemValue("logicTableGroupCode");
					if (isNotEmpty(logicTableGroupCode)) {
						_this.initTable1Combo(logicTableGroupCode);
					}
				}
			}
		} else if (templateType == "2E") {
			form.hideItem("block_tree");
			form.showItem("block_area1");
			form.showItem("block_area1_1");
			form.enableItem("area1Size");
			form.setItemLabel("area1Size", "高度:&nbsp;&nbsp;&nbsp;");
			form.setItemValue("area1Size", "");
			show2E_S(templateType, isInit);
			changeArea("area1Id", form.getItemValue("area1Id"));
			form.showItem("block_area2");
			form.showItem("block_area2_1");
			changeArea("area2Id", form.getItemValue("area2Id"));
			form.hideItem("block_area3");
			form.hideItem("block_area3_1");
			form.setRequired("treeId", false);
			form.setRequired("table1Id", true);
			form.enableItem("table2Id");
			form.setItemLabel("table2Id", "表2:");
			form.setRequired("table2Id", true);
			form.setRequired("table3Id", false);
			form.setItemValue("treeId","");
			form.setItemValue("table3Id","");
			if (!isInit) {
				var table1Id = form.getItemValue("table1Id");
				var table2Id = form.getItemValue("table2Id");
				if (isNotEmpty(table1Id)) {
					this.relateTable(table1Id);
				}
				if (isNotEmpty(table2Id)) {
				    form.setItemValue("table2Id", table2Id);
				}
			}
		} else if (templateType == "2E_S") {
			form.hideItem("block_tree");
			form.showItem("block_area1");
			form.showItem("block_area1_1");
			form.enableItem("area1Size");
			form.setItemLabel("area1Size", "高度:&nbsp;&nbsp;&nbsp;");
			form.setItemValue("area1Size", "");
			changeArea("area1Id", form.getItemValue("area1Id"));
			form.showItem("block_area2");
			form.showItem("block_area2_1");
			show2E_S(templateType, isInit);
			changeArea("area2Id", form.getItemValue("area2Id"));
			form.hideItem("block_area3");
			form.hideItem("block_area3_1");
			form.setRequired("treeId", false);
			form.setRequired("table1Id", true);
			form.setRequired("table2Id", false);
			form.disableItem("table2Id");
			form.setItemLabel("table2Id", "表2:&nbsp;&nbsp;&nbsp;");
			form.setRequired("table3Id", false);
			form.setItemValue("treeId","");
			form.setItemValue("table3Id","");
			if (!isInit) {
				var table1Id = form.getItemValue("table1Id");
				var table2Id = form.getItemValue("table2Id");
				if (isNotEmpty(table1Id)) {
					this.relateTable(table1Id);
				}
				if (isNotEmpty(table2Id)) {
				    form.setItemValue("table2Id", table2Id);
				}
			}
		} else if (templateType == "3L" || templateType == "3E") {
			form.hideItem("block_tree");
			form.showItem("block_area1");
			form.showItem("block_area1_1");
			form.enableItem("area1Size");
			form.setItemValue("area1Size", "");
			show2E_S(templateType, isInit);
			if (templateType == "3L") {
				form.setItemLabel("area1Size", "宽度:&nbsp;&nbsp;&nbsp;");
			} else {
				form.setItemLabel("area1Size", "高度:&nbsp;&nbsp;&nbsp;");
			}
			changeArea("area1Id", form.getItemValue("area1Id"));
			form.showItem("block_area2");
			form.showItem("block_area2_1");
			changeArea("area2Id", form.getItemValue("area2Id"));
			form.showItem("block_area3");
			form.showItem("block_area3_1");
			changeArea("area3Id", form.getItemValue("area3Id"));
			form.setRequired("treeId", false);
			form.setRequired("table1Id", true);
			form.enableItem("table2Id");
			form.setItemLabel("table2Id", "表2:");
			form.setRequired("table2Id", true);
			form.setRequired("table3Id", true);
			form.setItemValue("treeId","");
			if (!isInit) {
				var table2Id = form.getItemValue("table2Id");
				if (isNotEmpty(table2Id)) {
					var table2Id = form.getItemValue("table1Id");
					this.relateTable(table1Id, table2Id);
				}
			}
		} else {
		    form.hideItem("block_tree");
            form.hideItem("block_area1");
            form.hideItem("block_area1_1");
            form.hideItem("block_area2");
            form.hideItem("block_area2_1");
            form.hideItem("block_area3");
            form.hideItem("block_area3_1");
            form.setRequired("treeId", false);
            form.setRequired("table1Id", false);
            form.setRequired("table2Id", false);
            form.setRequired("table3Id", false);
            form.setItemValue("table1Id","");
            form.setItemValue("table2Id","");
            form.setItemValue("table3Id","");
		}
	};
	
	/**
	 * 
	 */
	function show2E_S(templateType, isInit) {
		if ("2E_S" == templateType) {
			form.hideItem("area1Id","0");
			form.hideItem("area1Id","1");
			form.hideItem("area1Id","2");
			form.showItem("area1Id","3");
			form.checkItem("area1Id","3");
			form.showItem("collapse1");
			form.checkItem("collapse1");
			form.hideItem("thumbnail1");
			form.hideItem("sort1");
			form.hideItem("area2Id","0");
			if (!isInit) {
				form.checkItem("area2Id","1");
				changeArea("area2Id", form.getItemValue("area2Id"));
			}
		} else {
			form.showItem("area1Id","0");
			form.showItem("area1Id","1");
			form.showItem("area1Id","2");
			form.hideItem("area1Id","3");
			form.hideItem("collapse1");
			form.showItem("thumbnail1");
			form.showItem("sort1");
			form.showItem("area2Id","0");
			if (!isInit) {
				form.checkItem("area1Id","0");
				changeArea("area1Id", form.getItemValue("area1Id"));
			}
			
		}
	}
	/**
	 * 改变布局
	 */
	function changeArea(id, value) {
	    if (id == "area1Id") {
		    if (value == "0") {
		        //form.hideItem("block_area1_1");
		        form.hideItem("sort1");
		        form.hideItem("thumbnail1");
		    } else if (value == "1") {
		        //form.showItem("block_area1_1");
		        form.showItem("sort1");
		        form.hideItem("thumbnail1");
		    } else if (value == "2") {
		        //form.showItem("block_area1_1");
		        form.showItem("sort1");
		        form.showItem("thumbnail1");
		    }
		} else if (id == "area2Id") {
		    if (value == "0") {
		        //form.hideItem("block_area2_1");
		        form.hideItem("sort2");
		        form.hideItem("thumbnail2");
		    } else if (value == "1") {
		        //form.showItem("block_area2_1");
		        form.showItem("sort2");
		        form.hideItem("thumbnail2");
		    } else if (value == "2") {
		        //form.showItem("block_area2_1");
		        form.showItem("sort2");
		        form.showItem("thumbnail2");
		    }
		} else if (id == "area3Id") {
		    if (value == "0") {
		        //form.hideItem("block_area3_1");
		        form.hideItem("sort3");
		        form.hideItem("thumbnail3");
		    } else if (value == "1") {
		        //form.showItem("block_area3_1");
		        form.showItem("sort3");
		        form.hideItem("thumbnail3");
		    } else if (value == "2") {
		        //form.showItem("block_area3_1");
		        form.showItem("sort3");
		        form.showItem("thumbnail3");
		    }
		}
	}
	/**
	 * 更改逻辑表组ID时改变
	 */
	this.changeLogicTableGroupCode = function(logicTableGroupCode) {
		form.setItemValue("table1Id", "");
		_this.initTable1Combo(logicTableGroupCode);
		form.getCombo("table2Id").clearAll();
		form.getCombo("table3Id").clearAll();
	};
	/**
	 * 初始化逻辑表组下拉框
	 */
	this.initLogicTableGroupCombo = function() {
		var url = AppActionURI.logicGroupDefine + "!comboOfLogicTableGroups.json";
		var opts = loadJson(url);
		var combo = form.getCombo("logicTableGroupCode");
		combo.clearAll();
		combo.addOption(opts);
	};
	/**
	 * 初始化树下拉框
	 */
	this.initTreeCombo = function() {
		var url = AppActionURI.treeDefine + "!comboOfTrees.json";
		var opts = loadJson(url);
		var combo = form.getCombo("treeId");
		combo.clearAll();
		combo.addOption(opts);
	};
	/**
	 * 初始化表1或逻辑表1下拉框
	 */
	this.initTable1Combo = function(logicTableGroupCode) {
		var type = form.getItemValue("type");
		var url = "";
		if (type == "4") {
			url = AppActionURI.physicalTableDefine + "!comboOfTables.json?P_includeView=1";
		} else if (type == "5") {
			url = AppActionURI.logicTableDefine + "!comboOfLogicTables.json?logicTableGroupCode=" + logicTableGroupCode;
		}
		opts = loadJson(url);
		combo = form.getCombo("table1Id");
		combo.clearAll();
		combo.addOption(opts);
	};
	/**
	 * 初始化关联表下拉框，如果table2Id不为空，初始化table3Id下拉框；如果table2Id为空，初始化table2Id下拉框
	 */
	this.relateTable = function(table1Id, table2Id) {
		if (isEmpty(table1Id)) {
			return;
		}
		var relateUrl, relateOpts, relateCombo;
		var type = form.getItemValue("type");
		var templateType = form.getItemValue("templateType");
		if (type == "4") {
			if (isEmpty(table2Id)) {
				form.setItemValue("table2Id", "");
				relateUrl = AppActionURI.tableRelation + "!comboOfRelateTables.json?P_includeMe=1&P_tableId=" + table1Id;
				relateOpts = loadJson(relateUrl);
				relateCombo = form.getCombo("table2Id");
				relateCombo.clearAll();
				relateCombo.addOption(relateOpts);
				if (templateType == "2E_S") {
//					var tmp = form.getCombo("table1Id");
//					relateCombo.setComboValue(tmp.getSelectedValue());
					form.setItemValue("table2Id", form.getItemValue("table1Id"));
				}
			} else {
				form.setItemValue("table3Id", "");
				relateUrl = AppActionURI.tableRelation + "!comboOfRelateTables.json?P_includeMe=1&P_tableId=" + table2Id;
				relateOpts = loadJson(relateUrl);
				relateCombo = form.getCombo("table3Id");
				relateCombo.clearAll();
				relateCombo.addOption(relateOpts);
			}
			
		} else if (type == "5") {
			var logicTableGroupCode = form.getItemValue("logicTableGroupCode");
			if (isEmpty(table2Id)) {
				form.setItemValue("table2Id", "");
				relateUrl = AppActionURI.logicTableDefine + "!comboOfLogicTables.json?logicTableGroupCode=" + logicTableGroupCode + "&parentTableCode=" + table1Id;
				relateOpts = loadJson(relateUrl);
				relateCombo = form.getCombo("table2Id");
				relateCombo.clearAll();
				relateCombo.addOption(relateOpts);
			} else {
				form.setItemValue("table3Id", "");
				relateUrl = AppActionURI.logicTableDefine + "!comboOfLogicTables.json?logicTableGroupCode=" + logicTableGroupCode + "&parentTableCode=" + table2Id;
				relateOpts = loadJson(relateUrl);
				relateCombo = form.getCombo("table3Id");
				relateCombo.clearAll();
				relateCombo.addOption(relateOpts);
			}
		}
	};
	this.initLogicTableGroupCombo();
	this.initTreeCombo();
	this.setConfigFormData = function () {
		if (moduleId) {
			var fUrl = that.MODEL_URL + "/" + moduleId + ".json?_method=get";
			var fData = loadJson(fUrl);
			_this.changeType(fData.type, fData.templateType, true);
			form.setFormData(fData);
		} else {
			_this.changeType("3", "2U", true);
		}
	};
	this.setConfigFormData();
}
function ValidateClassName(value) {
	return value.match(/^[A-Z]\w+$/);
}

function ValidateAreaSize(value) {
	if (isEmpty(value)) return true;
	var re = /^(\d|[1-9]\d|100)%$|^\d+$/;
	return re.test(value);
}