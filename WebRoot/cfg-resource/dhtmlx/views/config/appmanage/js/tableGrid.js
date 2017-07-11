/**
 * 
 * @param that
 * @param layout
 */
function initTableGrid(that, layout) {
    layout.hideHeader();
    
    var _this = this;
    var moduleUrl = AppActionURI.tableDefine;
    var ttbar = layout.attachToolbar();
    var grid = layout.attachGrid();
    var ttbarIndex = 1;
    ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    ttbar.addButton("add", ttbarIndex++, "新增", "new.gif");
    ttbar.addSeparator("septr$01", ttbarIndex++);
    ttbar.addButton("delete", ttbarIndex++, "删除", "delete.gif");
    ttbar.addSeparator("septr$02", ttbarIndex++);
    ttbar.addButton("template", ttbarIndex++, "生成模板表", "copy.gif");
    ttbar.addSeparator("septr$03", ttbarIndex++);
    ttbar.addButton("download", ttbarIndex++, "下载模版", "download.gif");
    ttbar.addSeparator("septr$04", ttbarIndex++);
    ttbar.addButton("import", ttbarIndex++, "导入", "upload.gif");
    ttbar.addSeparator("septr$05", ttbarIndex++);
    ttbar.addButton("export", ttbarIndex++, "导出", "download.gif");
    ttbar.addSeparator("septr$06", ttbarIndex++);
    ttbar.addButton("syn$view", ttbarIndex++, "同步视图", "sync.gif");

    if (classification == 'V') { // 视图不可以新增
    	layout.showToolbar();
		ttbar.hideItem("add");
		ttbar.hideItem("septr$01");
		ttbar.hideItem("delete");
		ttbar.hideItem("septr$02");
		ttbar.hideItem("template");
		ttbar.hideItem("septr$03");
		ttbar.hideItem("import");
		ttbar.hideItem("septr$04");
		ttbar.hideItem("export");
		ttbar.hideItem("septr$05");
		ttbar.hideItem("download");
		ttbar.hideItem("septr$06");
		ttbar.showItem("syn$view");
	} else if(classification == 'T') {
		layout.showToolbar();
		ttbar.showItem("add");
		ttbar.showItem("septr$01");
		ttbar.showItem("delete");
		ttbar.showItem("septr$02");
		ttbar.hideItem("template");
		ttbar.hideItem("septr$03");
		ttbar.showItem("download");
		ttbar.showItem("septr$04");
		ttbar.showItem("import");
		ttbar.showItem("septr$05");
		ttbar.showItem("export");
		ttbar.hideItem("septr$06");
		ttbar.hideItem("syn$view");
	} else if(classification == 'W') {//工作流引擎
		layout.hideToolbar();
	} else {
		layout.showToolbar();
		ttbar.showItem("add");
		ttbar.showItem("septr$01");
		ttbar.showItem("delete");
		ttbar.showItem("septr$02");
		ttbar.showItem("template");
		ttbar.showItem("septr$03");
		ttbar.showItem("download");
		ttbar.showItem("septr$04");
		ttbar.showItem("import");
		ttbar.showItem("septr$05");
		ttbar.showItem("export");
		ttbar.hideItem("septr$06");
		ttbar.hideItem("syn$view");
	}
	var copyTemplateForm;
	var copyTemplateFormData = {
			format: [
				{type: "block", width: "350", list:[
					{type: "hidden", name: "_method"},
					{type: "hidden", name: "copyTableId"},
	 				{type: "input", label: "显示名称：", labelWidth:80, name: "name", required:true, width:200, maxLength:24,  tooltip:"请输入显示名称!"},
	 				{type: "combo", label: "　表标签：&nbsp;&nbsp;&nbsp;", name: "tableLabel",labelWidth:80,width:200,readonly:true}
				]}
			],
			settings: {labelWidth: 80, inputWidth: 160}
		};
	/**
	 * 初始化构件分类表单工具条的方法
	 * @param {dhtmlxToolbar} toolBar
	 */
	function initCopyTemplateFormToolbar(toolBar) {
		toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
		toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
		toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
		toolBar.setAlign("right");
		toolBar.attachEvent("onClick", function(id) {
			if (id == "close") {
				dhxWins.window(WIN_ID).close();
			} else if (id == "save") {
				if (!copyTemplateForm.validate())
					return;
				var url = AppActionURI.tableDefine + "!comboOfTables2TableCopy.json";
				var templates = loadJson(url);
	        	var name = copyTemplateForm.getItemValue("name");
	        	for (var i in templates) {
	        		if (name == templates[i][1]) {
	        			dhtmlx.message(getMessage("form_field_exist", "显示名称"));
	        			return;
	        		}
	        	}
        		copyTemplateForm.setItemValue("_method", "post");
	    		copyTemplateForm.send(AppActionURI.tableDefine + "!copyToTemplate.json", "post", function(loader, response){
					dhxWins.window(WIN_ID).close();
					dhtmlx.message(getMessage("save_success"));
					that.reloadTreeItem("-T");
	    		});
			}
		});
	}
	ttbar.attachEvent("onClick", function(id) {
		if ("add" == id) {
			if (grid.getRowId(0) == '') {
            	return;
            }
            grid.addRow('','',0);
            grid.cells('',0).open();
		} else if ("delete" == id) {
			deleteById();
		} else if ("template" == id) {
			var rowIds = grid.getSelectedRowId();
			if (null == rowIds || "" == rowIds) {
				dhtmlx.message(getMessage("select_record"));
				return;
			} 
			if (rowIds.split(",").length > 1) {
				dhtmlx.message(getMessage("select_only_one_record"));
				return;
			}
			copyTemplateForm = openNewWindow(copyTemplateFormData, initCopyTemplateFormToolbar, 400, 200);
			copyTemplateForm.setItemValue("copyTableId", rowIds);
			var dataJson = loadJson(AppActionURI.tableLabel + "!search.json?F_in=code,name");
			var t_combo = copyTemplateForm.getCombo("tableLabel");
			for (var i = 0; i < dataJson.data.length; i++) {
				t_combo.addOption(dataJson.data[i].code,dataJson.data[i].name);
			}
		} else if ("syn$view" == id) {
			var url = moduleUrl + "!synViews.json";
			dhtmlxAjax.get(url, function(loader) {
				var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
				if (jsonObj.success) {
					that.reloadTreeItem();
					that.reloadGrid();
					dhtmlx.message(jsonObj.message);
			    } else {
			    	dhtmlx.message(getMessage("operate_failure"));
			    }
			});
		}else if("download" == id){
			download(AppActionURI.tableImpExport + "!downloadExcelTemplate.json");
			
		}else if("import" == id){
			var impExpURL = AppActionURI.tableImpExport;
			
			if (!dhxWins) {
		    		dhxWins = new dhtmlXWindows();
		    	}
			    dataWin = dhxWins.createWindow(WIN_ID, 0, 0, 450, 280);
			    dataWin.setModal(true);
			    dataWin.setText("导入表");
			    dataWin.center();
			    dataWin.button('park').hide();
			    dataWin.button('minmax1').hide();
			    var vaultDiv = document.createElement("div");
			    vaultDiv.setAttribute("id", "vaultDiv");
			    document.body.appendChild(vaultDiv);
			    
			    var upload_url = impExpURL + "!uploadHandler";// -处理文件上传
			    var getInfo_url = impExpURL + "!getInfoHandler"; //处理进度提示
			    var getId_url = impExpURL + "!getIdHandler"; //-处理会话初始化
	        	var vault = new dhtmlXVaultObject();
	            vault.setImagePath(DHX_RES_PATH + "/common/css/imgs/");
	            vault.setServerHandlers(upload_url, getInfo_url, getId_url);  //向服务器端发送处理
	            vault.setFilesLimit(1);//选择上传文件个数
				vault.strings = {remove: "移除", done: "完成", error: "上传失败!", btnAdd : "选择文件",btnUpload : "上传",btnClean : "清空"};
	            vault.onAddFile = function(fileName) { 
					var ext = this.getFileExtension(fileName).toLocaleString(); 
						if (ext != "xls") {
							dhtmlx.message("只能上传Excel文件！");
							return false;
						} else {
							return true;
						}
				};
				//获取文件对象属性
				vault.onFileUploaded = function(file) { 
					var impUrl = impExpURL + "!impTableExcel.json?classification=" + classification+"&parentId="+nodeId;
					dhtmlxAjax.get(encodeURI(impUrl), function(loader) {
						var result = eval("(" + loader.xmlDoc.responseText + ")");
						//0.成功  1.异常  2.重复
							if (result.status == 0) {
								that.reloadGrid();
								that.reloadTreeItem();
								dhtmlx.message(result.message);
							}else if (result.status == 1) {
								that.reloadGrid();
								that.reloadTreeItem();
					    		dhtmlx.alert("<font color='red'>{"+result.message+"}</font>表导入失败，表中文名或英文名已存在，请注意表的中文名和英文名的<font color='red'>唯一性</font>!");
							}else if (result.status == 2 || result.status == 3) {
					    		dhtmlx.message(result.message);
							}
							dataWin.close();
					});
				};
	            vault.create("vaultDiv");
			    dataWin.attachObject(vaultDiv);
			    
		}else if("export" == id){
			var rowIds = grid.getSelectedRowId();
			var typeNo = 1;
			var types;
			if (null == rowIds || "" == rowIds) {
				dhtmlx.message(getMessage("select_record"));
				return;
			}
			if(rowIds.indexOf(",") > 0){
				var rIdArray = rowIds.split(",");
		    	for (var i = 0; i < rIdArray.length; i++) {
		    		var type = grid.cells(rIdArray[i],3).getValue();
		    		types += type+";";
		    		if(type=="0"){
		    			typeNo += typeNo;
		    		}
		    		if(types.indexOf("0")>0 && types.indexOf("1")>0){
		    			dhtmlx.message("只能选择一条分类记录或多张表记录!");
		    			return;
		    		}
		    	}
    		}else{
    			var type = grid.cells(rowIds,3).getValue();
    		}
			if(typeNo >1){
            	dhtmlx.message("只能选择一条分类记录!");
                return;
			}
			download(AppActionURI.tableImpExport + "!exportTableExcel.json?rowIds="+rowIds+"&type="+type);
		}
	});
	
	var gcfg  = {
			format: {
				headers: ["&nbsp;","<center>显示名称</center>","<center>表(视图)名称</center>","<center>类型</center>","<center>类型标签</center>","<center>表标签</center>",""],
				   cols: ["id","name","realTableName","type","typeLabel","tableLabel",""],
				colWidths: ["30","200","200","150","150","150","*"],
				colTypes: ["sub_row_form","ro","ro","co","co","co","ro"],
				colAligns: ["right","left","left","left","left","left","left"]
			}
	};
	
	var tCombo = grid.getCombo(3);
	tCombo.put("0","分类");
	tCombo.put("1","表");
	
	/**
	 * 刷新列表中表标签下拉框
	 */
	function refreshGridTableLabel() {
		var dataJson = loadJson(AppActionURI.tableLabel + "!search.json?F_in=code,name");
		var dataJson1 = loadJson(AppActionURI.typeLabel + "!search.json?F_in=code,name");
		var ttCombo = grid.getCombo(5);
		for (var i = 0; i < dataJson.data.length; i++) {
			ttCombo.put(dataJson.data[i].code, dataJson.data[i].name);
		}
		var ttCombo1 = grid.getCombo(4);
		for (var i = 0; i < dataJson1.data.length; i++) {
			ttCombo1.put(dataJson1.data[i].code, dataJson1.data[i].name);
		}
	}
	refreshGridTableLabel();
	
	var gurl = getGridUrl();
	grid.enableDragAndDrop(true);
	grid.enableTooltips("false,false,false,false,false,false");
	initGridWithoutPageable(grid, gcfg, gurl);
	/** 列表拖拽调整顺序*/
	grid.attachEvent("onDrag",function(sIds,tId) {
		// 判断选中的记录是否连续，如果不连续，则不能调整顺序
		if (sIds.indexOf(",") != -1) {
			var idArr = sIds.split(",");
			var preIdx = null, curIdx = null;
			for (var i = 0; i < idArr.length; i++) {
				curIdx = grid.getRowIndex(idArr[i]);
				if (null == preIdx) {
					preIdx = curIdx;
					continue;
				}
				if (Math.abs(curIdx - preIdx) != 1) {
					dhtmlx.message("请选择连续的记录进行调整顺序！");
					return false;
				}
				preIdx = curIdx;
			}
		}
		return true;
	});
	
	/** 获取默认表前缀*/
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
		} else if (Classification.WORKFLOW == classification) {
			return TablePrefix.WORKFLOW;
		}
	};
	
	function getFormJson() {
		return [
 				{type: "setting", labelWidth: 75, inputWidth: 200, labelAlign:"right"},
				{type: "hidden", name: "_method"},
				{type: "hidden", name: "id"},
				{type: "hidden", name: "classification", value: classification},
				{type: "hidden", name: "created",value:"0"},
				{type: "hidden", name: "parentId", value: nodeId},
				{type: "hidden", name: "showOrder"},
				{type:"block",width:800,list:[
					{type: "block", width:320, list:[
						{type: "itemlabel", label: "　　类型：", labelWidth:80},
						{type: "newcolumn"},
		 				{type: "radio", name: "type", label: "分类", value:"0", labelWidth:30, offsetLeft:"20",labelAlign:"left",position:"label-right"},
						{type: "newcolumn"},
		 				{type: "radio", name: "type", label: "　表", value:"1", labelWidth:35,offsetLeft:"30",labelAlign:"left",position:"label-right",checked: true}
					]},
					{type: "block", width:1800, list:[
						{type: "block", width:290, list:[
			 				{type: "input", label: "显示名称：", labelWidth:80, name: "name", required:true, width:200, maxLength:24,  tooltip:"请输入显示名称!"},
			 				{type: "combo", label: "分类标签：", name: "typeLabel",labelWidth:80,width:200,readonly:true},
			 				{type: "combo", label: "　表标签：", name: "tableLabel",labelWidth:80,width:200,readonly:true}
			 			]},
			 			{type: "newcolumn"},
						{type: "block", name: "block_table", width:290, list:[
		  					{type: "itemlabel", label: "　表名称：", labelWidth:80},
							{type: "newcolumn"},
			  				{type: "input", labelWidth:80, name: "tablePrefix", value: getTablePrefix(classification), width:40},
							{type: "newcolumn"},
			 				{type: "input", name: "tableName", width:158, maxLength:20}
			 			]},
			 			{type: "block", width:290, list:[
							{type: "combo", label: "　表复制：", name: "template",labelWidth:80,width:200}
						]},
						{type: "newcolumn"},
			 			{type: "block", width:290, offsetTop:"22", list:[
							{type: "button", name: "save", value: "保存", width:80}
						]}
					]}
				]}
			];
	}
	
    grid.attachEvent("onRowSelect", function(rId, cInd) {
    	if(classification != 'V'){
    		grid.cells(rId,0).open();
    	}
	});
	
	grid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
		subform.c = getFormJson();
	});
	
	grid.attachEvent("onSubFormLoaded", function(subform,id,index) {
		subform.setItemValue("tablePrefix", getTablePrefix(classification));
		//初始化combo
		var url = AppActionURI.tableDefine + "!comboOfTables2TableCopy.json";
		var opts = loadJson(url);
		var combo = subform.getCombo("template");
		combo.addOption(opts);
		
		var dataJson = loadJson(AppActionURI.tableLabel + "!search.json?F_in=code,name");
		var t_combo = subform.getCombo("tableLabel");
		for (var i = 0; i < dataJson.data.length; i++) {
			t_combo.addOption(dataJson.data[i].code,dataJson.data[i].name);
		}
		dataJson = loadJson(AppActionURI.typeLabel + "!search.json?F_in=code,name");
		t_combo = subform.getCombo("typeLabel");
		for (var i = 0; i < dataJson.data.length; i++) {
			t_combo.addOption(dataJson.data[i].code,dataJson.data[i].name);
		}

		subform.hideItem("typeLabel");
		var MODEL_URL = AppActionURI.tableDefine;
		 if (id != "") {
			var url = MODEL_URL + "/" + id + ".json?_method=get";
	    	loadForm(subform, url);
			var data = loadJson(url);
			if (null == data) {
				return;
			}
			if ("0" == data.type) {
				subform.enableItem("type","0");
				subform.disableItem("type","1");
				subform.hideItem("block_table");
				subform.hideItem("template");
				subform.hideItem("tableLabel");
				subform.showItem("typeLabel");
				//subform.setRequired("tablePrefix", false);
			} else {
				subform.hideItem("typeLabel");
				subform.enableItem("type","1");
				subform.disableItem("type","0");
				if (Classification.TEMPLATE == data.classification) {
					subform.hideItem("block_table");
					subform.hideItem("template");
					//subform.setRequired("tablePrefix", false);
				} else {
					subform.showItem("block_table");
					if (Classification.VIEW == data.classification) {
						//subform.setRequired("tablePrefix", false);
						subform.hideItem("template");
						subform.hideItem("tableLabel");
						subform.hideItem("tablePrefix");
						subform.setItemWidth("tableName", 200);
					} else {
						subform.showItem("tablePrefix");
						subform.setItemWidth("tableName", 158);
					}
					subform.setReadonly("tablePrefix", true);
					subform.setReadonly("tableName", true);
				}
			}
			subform.setFormData(data);
    	}
		
		if (classification=="T" || classification=="W") {
			subform.hideItem("block_table");
			subform.hideItem("template");
		}
	
		subform.attachEvent("onChange", function(id, val) {
			if ("type" == id || Classification.TEMPLATE == classification) {
				if ("0" == val) {
					subform.hideItem("block_table");
					subform.hideItem("template");
					subform.hideItem("tableLabel");
					subform.showItem("typeLabel");
					subform.setItemValue("tableName", "");
				} else {
					subform.hideItem("typeLabel");
					subform.showItem("tableLabel");
					if (Classification.TEMPLATE == classification) {
						subform.hideItem("block_table");
						subform.hideItem("template");
						subform.setItemValue("tableName", "");
					} else {
						subform.showItem("block_table");
						if (Classification.VIEW == classification) {
							subform.hideItem("template");
							subform.hideItem("tablePrefix");
							subform.setItemWidth("tableName", 200);
						} else {
							subform.showItem("template");
							subform.showItem("tablePrefix");
							subform.setItemWidth("tableName", 158);
						}
					}
				}
			} else if ("tablePrefix" == id) {
				subform.setItemValue("tablePrefix", val.toUpperCase());
			} else if ("tableName" == id) {
				subform.setItemValue("tableName", val.toUpperCase());
			}
		});
		
	    subform.attachEvent("onButtonClick", function(buttonName) {
		    if (buttonName == "save") {
				var _ck = subform.getCombo("template").getSelectedValue();
				var id  = subform.getItemValue("id");
				var type = subform.getCheckedValue("type");
				if ("0" == type || Classification.TEMPLATE == classification || Classification.VIEW == classification) {
					submit(id);
					return;
				}
				var prefix    = subform.getItemValue("tablePrefix");
				var tableName = subform.getItemValue("tableName");
				if (null == prefix || "" == prefix) {
					dhtmlx.alert("表前缀不可为空！");
					return;
				}
				if (null == tableName || "" == tableName) {
					dhtmlx.alert("表名称不可为空！");
					return;
				}
				prefix = prefix.toUpperCase();
				tableName = tableName.toUpperCase();
				if ("T_XTPZ_" == prefix || "T_WF_" == prefix) {
					dhtmlx.alert("此表前缀已被系统使用，请修改！");
					return;
				}
				if (!prefix.match(/^[t_|T_]([\w\_])+\_$/)) {
					dhtmlx.alert("表前缀格式只能以T_开头并要以_结束，中间可由字母、数字、下划线组成！");
					return;
				}
				if (!tableName.match(/([\w\_])+/)) {
					dhtmlx.alert("表名称只能是字母、数字组成！");
					return;
				}
				var checkUrl = moduleUrl + "!checkUnique.json?id=" + id + "&Q_EQ_tableName=" + tableName.toUpperCase() + "&Q_EQ_tablePrefix=" + prefix;
				dhtmlxAjax.get(checkUrl,function(loader) {
					var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
					if (false == jsonObj.success) {
						if ("OK" == jsonObj.message) {
							dhtmlx.message(getMessage("form_field_exist", "表名称"));
						} else {
							dhtmlx.message("唯一检查出错，请联系管理员！");
						}
					} else {//*/
						submit(id);
					}
				});//*/
	        }
		});
		    
		    /** 表单提交*/
		function submit(id) {
			var url = moduleUrl;
			var checkUrl = url;
			var parentId = subform.getItemValue("parentId");
			var name = subform.getItemValue("name");
			var	type = subform.getItemValue("type");
			if (id) { // modify
				subform.setItemValue("_method", "put");
				url += "/" + id + ".json";
				checkUrl += "!checkUnique.json?id=" + id + "&Q_EQ_parentId=" + parentId + "&Q_EQ_name=" + name + "&Q_EQ_type=" + type;
			} else { // create
				subform.setItemValue("_method", "post");
				url += ".json";
				checkUrl += "!checkUnique.json?Q_EQ_parentId=" + parentId + "&Q_EQ_name=" + name + "&Q_EQ_type=" + type;
			}
			dhtmlxAjax.get(encodeURI(addTimestamp(checkUrl)), function(loader) {
				var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
				if (false == jsonObj.success) {
					if ("OK" == jsonObj.message) {
						dhtmlx.message(getMessage("form_field_exist", "同级目录下该显示名称"));
					} else {
						dhtmlx.message("唯一检查出错，请联系管理员！");
					}
				} else {
					subform.send(url, "post", function(loader, response) {
						var formData = eval("(" + loader.xmlDoc.responseText + ")");
						if (formData.id == null || formData.id == "") {
							dhtmlx.message(getMessage("save_failure"));
							return;
						}
						subform.setFormData(formData);
						that.reloadGrid();
						that.reloadTreeItem();
						if ("0" == formData.type) {
							subform.disableItem("type", "1");
						} else {
							subform.disableItem("type", "0");
						}
						var _ck = subform.getCombo("template").getSelectedValue();
						if (isNotEmpty(_ck) && type == 1) {
							//复制表的操作
							var url = AppActionURI.columnDefine + "!copyFileds2newTableByTemplatTable.json?templateTable_id="+_ck+"&newTable_id="+formData.id+"";
							dhtmlxAjax.get(url,function(loader){
								var rlt = eval("(" + loader.xmlDoc.responseText + ")");
								if ("" != rlt.message) {
									dhtmlx.message(rlt.message);
									return;
								}
								dhtmlx.message(getMessage("save_success"));
							});
						} else {
							dhtmlx.message(getMessage("save_success"));
						}
					});
				}
			});			
		}
	});
	
	grid.attachEvent("onSubRowOpen", function(id,expanded){
		if (expanded) {
			grid.forEachRow(function(rId){
				if (id != rId) {
					grid.cells(rId,0).close();
				}
			});
		}
	});
				
	grid.attachEvent("onDrop", function(sId,tId,dId,sObj,tObj,sCol,tCol){
		if(undefined != tId){
			var adjustUrl = moduleUrl + "!adjustShowOrder?P_parentId=" + that.nodeId + "&P_sourceIds=" + sId + "&P_targetId=" + tId;
			dhtmlxAjax.get(adjustUrl, function(loader) {
				that.reloadGrid();
				grid.selectRowById(sId);
				that.reloadTreeItem();
			});
		}		
	});
	
	/** 列表刷新*/
	that.reloadGrid = function() {
		loadGridData(grid, gcfg, getGridUrl());
	};
	/** 列表工具条按钮*/
	that.buttonGrid = function () {
		 if (classification == 'V') { // 视图不可以新增
			layout.showToolbar();
			ttbar.hideItem("add");
			ttbar.hideItem("septr$01");
			ttbar.hideItem("delete");
			ttbar.hideItem("septr$02");
			ttbar.hideItem("template");
			ttbar.hideItem("septr$03");
			ttbar.hideItem("export");
			ttbar.hideItem("septr$04");
			ttbar.hideItem("download");
			ttbar.hideItem("septr$05");
			ttbar.hideItem("import");
			ttbar.hideItem("septr$06");
			ttbar.showItem("syn$view");
		} else if (classification == 'T') {
			layout.showToolbar();
			ttbar.showItem("add");
			ttbar.showItem("septr$01");
			ttbar.showItem("delete");
			ttbar.showItem("septr$02");
			ttbar.hideItem("template");
			ttbar.hideItem("septr$03");
			ttbar.showItem("download");
			ttbar.showItem("septr$04");
			ttbar.showItem("import");
			ttbar.showItem("septr$05");
			ttbar.showItem("export");
			ttbar.hideItem("septr$06");
			ttbar.hideItem("syn$view");
		} else if(classification == 'W') {//工作流引擎
			layout.hideToolbar();
		} else {
			layout.showToolbar();
			ttbar.showItem("add");
			ttbar.showItem("septr$01");
			ttbar.showItem("delete");
			ttbar.showItem("septr$02");
			ttbar.showItem("template");
			ttbar.showItem("septr$03");
			ttbar.showItem("download");
			ttbar.showItem("septr$04");
			ttbar.showItem("import");
			ttbar.showItem("septr$05");
			ttbar.showItem("export");
			ttbar.hideItem("septr$06");
			ttbar.hideItem("syn$view");
		}
	};
	/** 列表查询地址*/
	function getGridUrl() {
		return moduleUrl + "!search.json?Q_EQ_parentId=" + nodeId + "&P_orders=showOrder";
	};
	/** 单条删除记录*/
	function deleteById() {
		var rowIds = grid.getSelectedRowId();
		if (null == rowIds || "" == rowIds) {
			dhtmlx.message(getMessage("select_record"));
			return;
		} 
		if (rowIds.split(",").length > 1) {
			dhtmlx.message(getMessage("select_only_one_record"));
			return;
		}
	    dhtmlx.confirm({
			type:"confirm",
			text: getMessage("delete_warning"),
			ok: "确定",
			cancel: "取消",
			callback: function(flag) {
				if (flag) {
					var type = grid.cells(rowIds, 3).getValue();
					if ("0" == type) {
						deleteTableById(rowIds);
					} else {
						dhtmlx.confirm({
							text: "<font color='red'>如果确定，则会删除该表及相关的所有配置信息！</font>再次确定要删除所选表记录？",
							ok: "确定",
							cancel: "取消",
							callback: function(flag) {
								if (flag) { deleteTableById(rowIds);}
							}
						});
					}
				}
			}
		});
	};
	
	function deleteTableById(id) {
		dhtmlxAjax.get(moduleUrl + "/" + id + ".json?_method=delete", function(loader) {
			var obj = eval("(" + loader.xmlDoc.responseText + ")");
			if (obj.success == false) {
				var id = obj.message;
				var name = grid.cells(id, 1).getValue();
				dhtmlx.alert("【" + name + "】有子节点，请先删除子节点！");
				return;
			} else {
				that.reloadGrid();
				that.reloadTreeItem();
				dhtmlx.message(getMessage("delete_success"));
			}			
        });
	}
}
