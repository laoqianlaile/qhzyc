/**
 * 
 * @param that
 * @param layout
 */
function initPhysicalTableGrid(that, layout, treeType) {
    layout.hideHeader();
    
    var _this = this;
    var moduleUrl = AppActionURI.physicalTableDefine;
    var ttbar = layout.attachToolbar();
    var grid = layout.attachGrid();
    var ttbarIndex = 1;
    ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    ttbar.addButton("add", ttbarIndex++, "新增", "new.gif");
    ttbar.addSeparator("septr$01", ttbarIndex++);
    ttbar.addButton("delete", ttbarIndex++, "删除", "delete.gif");
    ttbar.addSeparator("septr$02", ttbarIndex++);
    ttbar.addButton("logic", ttbarIndex++, "生成逻辑表", "copy.gif");
    ttbar.addSeparator("septr$03", ttbarIndex++);
    ttbar.addButton("addGroup", ttbarIndex++, "批量建表", "copy.gif");
    ttbar.addSeparator("septr$04", ttbarIndex++);
    ttbar.addButton("download", ttbarIndex++, "下载模版", "download.gif");
    ttbar.addSeparator("septr$05", ttbarIndex++);
    ttbar.addButton("import", ttbarIndex++, "导入", "upload.gif");
    ttbar.addSeparator("septr$06", ttbarIndex++);
    ttbar.addButton("export", ttbarIndex++, "导出", "download.gif");
    ttbar.addSeparator("septr$07", ttbarIndex++);
    ttbar.addButton("syn$view", ttbarIndex++, "同步视图", "sync.gif");

    if (classification == 'V') { // 视图不可以新增
    	layout.showToolbar();
		ttbar.hideItem("add");
		ttbar.hideItem("septr$01");
		ttbar.hideItem("delete");
		ttbar.hideItem("septr$02");
		ttbar.hideItem("logic");
		ttbar.hideItem("septr$03");
		ttbar.hideItem("addGroup");
		ttbar.hideItem("septr$04");
		ttbar.hideItem("import");
		ttbar.hideItem("septr$05");
		ttbar.hideItem("export");
		ttbar.hideItem("septr$06");
		ttbar.hideItem("download");
		ttbar.hideItem("septr$07");
		ttbar.showItem("syn$view");
	} else {
		layout.showToolbar();
		ttbar.showItem("add");
		ttbar.showItem("septr$01");
		ttbar.showItem("delete");
		ttbar.showItem("septr$02");
		ttbar.showItem("logic");
		ttbar.showItem("septr$03");
		ttbar.showItem("addGroup");
		ttbar.showItem("septr$04");
		ttbar.showItem("download");
		ttbar.showItem("septr$05");
		ttbar.showItem("import");
		ttbar.showItem("septr$06");
		ttbar.showItem("export");
		ttbar.hideItem("septr$07");
		ttbar.hideItem("syn$view");
	}
	var copylogicForm;
	var copylogicFormData = {
			format: [
				{type: "block", width: "350", list:[
					{type: "hidden", name: "_method"},
					{type: "hidden", name: "copyTableId"},
	 				{type: "input", label: "逻辑表名称：", labelWidth:120, name: "showName", required:true, width:200, maxLength:24,  tooltip:"请输入显示名称!"},
	 				{type: "input", label: "逻辑表代码：", labelWidth:120, name: "code", required:true, width:200, maxLength:24,  tooltip:"请输入逻辑表代码!"}
				]}
			],
			settings: {labelWidth: 80, inputWidth: 160}
		};
	/**
	 * 初始化构件分类表单工具条的方法
	 * @param {dhtmlxToolbar} toolBar
	 */
	function initCopylogicFormToolbar(toolBar) {
		toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
		toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
		toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
		toolBar.setAlign("right");
		toolBar.attachEvent("onClick", function(id) {
			if (id == "close") {
				dhxWins.window(WIN_ID).close();
			} else if (id == "save") {
				if (!copylogicForm.validate())
					return;
				var url = AppActionURI.logicTableDefine + "!comboOfTables2TableCopy.json";
				var logics = loadJson(url);
	        	var showName = copylogicForm.getItemValue("showName");
	        	var code = copylogicForm.getItemValue("code");
	        	for (var i in logics) {
	        		if (showName == logics[i][1]) {
	        			dhtmlx.message(getMessage("form_field_exist", "显示名称"));
	        			return;
	        		}
	        	}
        		copylogicForm.setItemValue("_method", "post");
	    		copylogicForm.send(AppActionURI.logicTableDefine + "!copyToLogic.json", "post", function(loader, response){
					dhxWins.window(WIN_ID).close();
					dhtmlx.message(getMessage("save_success"));
					that.reloadTreeItem("-LT");
	    		});
			}
		});
	}
	var addGroupForm;
	var addGroupFormData = {
			format: [
						{type: "block", width: 650, list:[
	                        {type: "hidden", name: "_method"},
							{type: "hidden", name: "id"},
							{type: "hidden", name: "showOrder"},
							{type: "hidden", name: "tableTreeId", value: nodeId},
							{type: "hidden", name: "defaultTablePrefix", value: that.prefixObj[classification]},
							{type: "hidden", name: "classification", value: classification},
							{type: "block", name: "block_table", width:650, list:[
								{type: "input", label: "物理表组名称：", labelWidth:120, name: "groupName", required:true, width:170, maxLength:24},
								{type: "newcolumn"},
								{type: "input", label: "物理表组编码：", labelWidth:120, name: "code", required:true, width:170, maxLength:24, offsetLeft:20}                 			 				
							    ]},
						    {type: "block", width:650, list:[
						        {type: "combo", label: "逻辑表组名称：", labelWidth:120, name: "logicGroupCode", required:true, width:170, maxLength:24},
						        {type: "newcolumn"},
						        {type: "input", label: "分类表前缀：", labelWidth:120, name: "tablePrefix", width:170, maxLength:5, offsetLeft:20}
						    ]},
				 			{type: "block", width:650, list:[
    							{type: "input", label: "备&nbsp;&nbsp;注：&nbsp;&nbsp;&nbsp;", name: "remark",labelWidth:120,width:480, maxLength:24, rows:3},
    						]}
						]}
					],
					settings: {labelWidth: 80, inputWidth: 160}
				};
	/**
	 * 初始化构件分类表单工具条的方法
	 * @param {dhtmlxToolbar} toolBar
	 */
	function initAddGroupFormToolbar(toolBar) {
		toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
		toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
		toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
		toolBar.setAlign("right");
		toolBar.attachEvent("onClick", function(id) {
			if (id == "close") {
				dhxWins.window(WIN_ID).close();
			} else if (id == "save") {
				if (!addGroupForm.validate()) return;
				var code = addGroupForm.getItemValue("code");
				var checkUrl = AppActionURI.physicalGroupDefine + "!checkUnique.json?Q_EQ_code=" + code;
				dhtmlxAjax.get(checkUrl, function(loader) {
					var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
					if (false == jsonObj.success) {
						if ("OK" == jsonObj.message) {
							dhtmlx.message(getMessage("form_field_exist","表组编码"));
						} else {
							dhtmlx.message("唯一检查出错，请联系管理员！");
						}
					} else {
						addGroupForm.setItemValue("_method", "post");
						addGroupForm.send(AppActionURI.physicalGroupDefine + "!saveAll.json", "post", function(loader, response){
							dhxWins.window(WIN_ID).close();
							dhtmlx.message(getMessage("save_success"));
							that.reloadTreeItem();
			    		});
					}
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
		} else if ("logic" == id) {
			var rowIds = grid.getSelectedRowId();
			if (null == rowIds || "" == rowIds) {
				dhtmlx.message(getMessage("select_record"));
				return;
			} 
			if (rowIds.split(",").length > 1) {
				dhtmlx.message(getMessage("select_only_one_record"));
				return;
			}
			copylogicForm = openNewWindow(copylogicFormData, initCopylogicFormToolbar, 400, 200);
			copylogicForm.setItemValue("copyTableId", rowIds);
		} else if ("addGroup" == id) {
			addGroupForm = openNewWindow(addGroupFormData, initAddGroupFormToolbar, 670, 300);
			var t_combo = addGroupForm.getCombo("logicGroupCode");
			var dataJson = loadJson(AppActionURI.logicGroupDefine + "!search.json?F_in=code,groupName");
			for ( var i = 0; i < dataJson.data.length; i++) {
				t_combo.addOption(dataJson.data[i].code,dataJson.data[i].groupName);
			}
		} else if ("syn$view" == id) {
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var msgWin = dhxWins.createWindow("msgWin", 0, 0, 200, 50);
            msgWin.setModal(true);
            msgWin.hideHeader();
            msgWin.center();
            msgWin.button("park").hide();
            msgWin.button("minmax1").hide();
            msgWin.button("minmax2").hide();
            msgWin.button("close").hide();
            var obj = document.getElementById("DIV-msg");
            if (obj == null) {
                obj = document.createElement("DIV");
                obj.setAttribute("id", "DIV-msg");
                obj.innerHTML = "正在处理，请稍后...";
                obj.setAttribute("style", "position:relative;top:35%;left:20%;font-size:11px;");
                document.body.appendChild(obj);
            }
            msgWin.attachObject(obj);
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
		    	msgWin.close();
		    });
		} else if("download" == id){
			download(AppActionURI.tableImpExport + "!downloadExcelLogic.json");
			
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
					var impUrl = impExpURL + "!impTableExcel.json?classification=" + classification+"&tableTreeId="+nodeId;
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
				headers: ["&nbsp;","<center>显示名称</center>","<center>表(视图)名称</center>",""],
				   cols: ["id","showName","tableName",""],
				colWidths: ["30","200","200","*"],
				colTypes: ["sub_row_form","ro","ro","ro"],
				colAligns: ["right","left","left","left"]
			}
	};
	
	var gurl = getGridUrl();
	grid.enableDragAndDrop(true);
	grid.enableTooltips("false,false,false");
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
	
	function getFormJson() {
		if (treeType == "logic") {
			return [
	 				{type: "setting", labelWidth: 75, inputWidth: 200,labelAlign:"left",position:"label-right"},
					{type: "hidden", name: "_method"},
					{type: "hidden", name: "id"},
					{type: "hidden", name: "tableType", valueL: "0"},
					{type: "hidden", name: "classification", valueL: classification},
					{type: "hidden", name: "created",value:"0"},
					{type: "hidden", name: "logicTableCode", value: nodeId.substring(3)},
					{type: "hidden", name: "showOrder"},
					{type:"block",width:800,list:[
						{type: "block", width:800, list:[
							{type: "block", name: "block_table", width:700, list:[
	 			 				{type: "input", label: "显示名称：", labelAlign:"right", labelWidth:80, name: "showName", required:true, width:200, maxLength:24,  tooltip:"请输入显示名称!"},
			  					{type: "newcolumn"},
			  					{type: "itemlabel", label: "表名称：", labelAlign:"right", labelWidth:80, offsetLeft:20},
								{type: "newcolumn"},
				  				{type: "input", labelWidth:80, name: "tablePrefix", width:40, readonly: true},
								{type: "newcolumn"},
				 				{type: "input", name: "tableCode", width:158, maxLength:20}
				 			]},
				 			{type: "block", width:700, list:[
								{type: "combo", label: "表分类 ：　", labelAlign:"right", name: "tableTreeId",labelWidth:80,width:200},
								{type: "newcolumn"},
								{type: "itemlabel", label: "其他：", labelAlign:"right", labelWidth:80, offsetLeft:20},
								{type: "newcolumn"},
				 				{type: "checkbox", name: "releaseWithData", label: "发布时生成数据脚本", labelWidth:120, labelAlign:"left",position:"label-right"},
								{type: "newcolumn"},
				 				{type: "checkbox", name: "createIndex", label: "创建索引库", labelWidth:80, offsetLeft:10, labelAlign:"left",position:"label-right"}
							]},
							{type: "block", width:700, list:[
								{type: "input", label: "备　　注：　", labelAlign:"right", name: "remark", labelWidth:80, width:500, maxLength:20, rows:3},
								{type: "newcolumn"},
								{type: "button", name: "save", value: "保存", width:80, offsetTop:30, offsetLeft:10}
							]}
						]}
					]}
				];
		} else if (classification == "V") {
			return [
	 				{type: "setting", labelWidth: 75, inputWidth: 200,labelAlign:"left",position:"label-right"},
					{type: "hidden", name: "_method"},
					{type: "hidden", name: "id"},
					{type: "hidden", name: "tableType", valueL: "0"},
					{type: "hidden", name: "classification", valueL: classification},
					{type: "hidden", name: "created",value:"0"},
					{type: "hidden", name: "tableTreeId", value: nodeId},
					{type: "hidden", name: "logicTableCode"},
					{type: "hidden", name: "releaseWithData", value: "0"},
					{type: "hidden", name: "showOrder"},
					{type:"block",width:800,list:[
						{type: "block", width:800, list:[
							{type: "block", name: "block_table", width:700, list:[
	 			 				{type: "input", label: "显示名称：", labelAlign:"right", labelWidth:80, name: "showName", required:true, width:200, maxLength:24,  tooltip:"请输入显示名称!"},
			  					{type: "newcolumn"},
			  					{type: "itemlabel", label: "表名称：", labelAlign:"right", labelWidth:80, offsetLeft:20},
								{type: "newcolumn"},
				  				{type: "input", labelWidth:80, name: "tablePrefix", value: that.prefixObj[classification], width:40, readonly: true},
								{type: "newcolumn"},
				 				{type: "input", name: "tableCode", width:158, maxLength:20, readonly: true}
				 			]},
				 			{type: "block", width:700, list:[
								{type: "itemlabel", label: "其　　他：　", labelAlign:"right", labelWidth:80},
								{type: "newcolumn"},
				 				{type: "checkbox", name: "createIndex", label: "创建索引库", labelWidth:80, offsetLeft:10, labelAlign:"left", position:"label-right"}
							]},
							{type: "block", width:700, list:[
								{type: "input", label: "备　　注：　", name: "remark", labelAlign:"right", labelWidth:80, width:500, maxLength:20, rows:3},
								{type: "newcolumn"},
								{type: "button", name: "save", value: "保存", width:80, offsetTop:30, offsetLeft:10}
							]}
						]}
					]}
				];
		} else {
			return [
	 				{type: "setting", labelWidth: 75, inputWidth: 200,labelAlign:"left",position:"label-right"},
					{type: "hidden", name: "_method"},
					{type: "hidden", name: "id"},
					{type: "hidden", name: "tableType", valueL: "0"},
					{type: "hidden", name: "classification", valueL: classification},
					{type: "hidden", name: "created",value:"0"},
					{type: "hidden", name: "tableTreeId", value: nodeId},
					{type: "hidden", name: "showOrder"},
					{type:"block",width:800,list:[
						{type: "block", width:800, list:[
							{type: "block", name: "block_table", width:700, list:[
	 			 				{type: "input", label: "显示名称：", labelAlign:"right", labelWidth:80, name: "showName", required:true, width:200, maxLength:24,  tooltip:"请输入显示名称!"},
			  					{type: "newcolumn"},
			  					{type: "itemlabel", label: "表名称：", labelAlign:"right", labelWidth:80, offsetLeft:20},
								{type: "newcolumn"},
				  				{type: "input", labelWidth:80, name: "tablePrefix", value: that.prefixObj[classification], width:40},
								{type: "newcolumn"},
				 				{type: "input", name: "tableCode", width:158, maxLength:20}
				 			]},
				 			{type: "block", width:700, list:[
								{type: "combo", label: "逻辑表：　", labelAlign:"right", name: "logicTableCode",labelWidth:80,width:200},
								{type: "newcolumn"},
								{type: "itemlabel", label: "其他：", labelAlign:"right", labelWidth:80, offsetLeft:20},
								{type: "newcolumn"},
				 				{type: "checkbox", name: "releaseWithData", label: "发布时生成数据脚本", labelWidth:120, labelAlign:"left", position:"label-right"},
								{type: "newcolumn"},
				 				{type: "checkbox", name: "createIndex", label: "创建索引库", labelWidth:80, offsetLeft:10, labelAlign:"left", position:"label-right"}
							]},
							{type: "block", width:700, list:[
								{type: "input", label: "备　　注：　", labelAlign:"right", name: "remark", labelWidth:80, width:500, maxLength:20, rows:3},
								{type: "newcolumn"},
								{type: "button", name: "save", value: "保存", width:80, offsetTop:30, offsetLeft:10}
							]}
						]}
					]}
				];
		}
	}
	
    grid.attachEvent("onRowSelect", function(rId, cInd) {
    	grid.cells(rId,0).open();
	});
	
	grid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
		subform.c = getFormJson();
	});
	
	grid.attachEvent("onSubFormLoaded", function(subform,id,index) {
		if (treeType == "logic") {
			subform.setItemValue("tableType", "0");
			subform.setItemValue("classification", classification);
			//初始化combo
			var url = AppActionURI.tableTree + "!comboOfTableTree.json";
			var opts = loadJson(url);
			var combo = subform.getCombo("tableTreeId");
			combo.addOption(opts);
			combo.attachEvent("onChange", function(){
				var tableTreeId = subform.getCombo("tableTreeId").getSelectedValue();
				var url = AppActionURI.tableTree + "!getClassification.json?P_id=" + tableTreeId;
				var tableClassification = loadJson(url);
				subform.setItemValue("tablePrefix", that.prefixObj[tableClassification]);
				subform.setItemValue("classification", tableClassification);
			});
		} else if (classification == "V") {
			subform.setItemValue("tablePrefix", that.prefixObj[classification]);
			subform.setItemValue("tableType", "1");
			subform.setItemValue("classification", classification);
		} else {
			var tablePrefix = loadJson(AppActionURI.tableTree + "!getTablePrefix.json?P_tableTreeId=" + nodeId);
			if (tablePrefix == 0) {
				tablePrefix = that.prefixObj[classification];
			}
			subform.setItemValue("tablePrefix", tablePrefix);
			subform.setItemValue("tableType", "0");
			subform.setItemValue("classification", classification);
			//初始化combo
			var url = AppActionURI.logicTableDefine + "!comboOfTables2TableCopy.json";
			var opts = loadJson(url);
			var combo = subform.getCombo("logicTableCode");
			combo.addOption(opts);
		}

		var MODEL_URL = moduleUrl;
		 if (id != "") {
			var url = MODEL_URL + "/" + id + ".json?_method=get";
	    	loadForm(subform, url);
			var data = loadJson(url);
			if (null == data) {
				return;
			}
			subform.setFormData(data);
    	}
		
	    subform.attachEvent("onButtonClick", function(buttonName) {
		    if (buttonName == "save") {
				var id  = subform.getItemValue("id");
				var type = subform.getCheckedValue("type");
				var prefix    = subform.getItemValue("tablePrefix");
				var tableCode = subform.getItemValue("tableCode");
				if (null == prefix || "" == prefix) {
					dhtmlx.alert("表前缀不可为空！");
					return;
				}
				if (null == tableCode || "" == tableCode) {
					dhtmlx.alert("表名称不可为空！");
					return;
				}
				prefix = prefix.trim().toUpperCase();
				subform.setItemValue("tablePrefix", prefix);
				tableCode = tableCode.trim().toUpperCase();
				subform.setItemValue("tableCode", tableCode);
				if ("T_XTPZ_" == prefix || "T_WF_" == prefix) {
					dhtmlx.alert("此表前缀已被系统使用，请修改！");
					return;
				}
				if (!tableCode.match(/([\w\_])+/)) {
					dhtmlx.alert("表名称只能是字母、数字组成！");
					return;
				}
				if (isNotEmpty(id)) {
					submit(id);
				} else {
					var checkUrl = moduleUrl + "!checkUnique.json?Q_EQ_tableCode=" + tableCode + "&Q_EQ_tablePrefix=" + prefix;
					dhtmlxAjax.get(checkUrl,function(loader) {
						var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
						if (false == jsonObj.success) {
							if ("OK" == jsonObj.message) {
								dhtmlx.message(getMessage("form_field_exist", "表名称"));
							} else {
								dhtmlx.message("唯一检查出错，请联系管理员！");
							}
						} else {//*/
							submit();
						}
					});//*/
				}
	        }
		});
		    
		    /** 表单提交*/
		function submit(id) {
			var url = moduleUrl;
			var checkUrl = url;
			var tableTreeId = subform.getItemValue("tableTreeId");
			var tableCode = subform.getItemValue("tableCode");
			if (id) { // modify
				subform.setItemValue("_method", "put");
				url += "/" + id + ".json";
				checkUrl += "!checkUnique.json?id=" + id + "&Q_EQ_tableTreeId=" + tableTreeId + "&Q_EQ_tableCode=" + tableCode;
			} else { // create
				subform.setItemValue("_method", "post");
				url += ".json";
				checkUrl += "!checkUnique.json?Q_EQ_tableTreeId=" + tableTreeId + "&Q_EQ_tableCode=" + tableCode;
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
						var _ck;
						if (treeType == "logic") {
							_ck = subform.getItemValue("logicTableCode");
						} else if (classification == "V") {
							_ck = null;
						} else {
							_ck = subform.getCombo("logicTableCode").getSelectedValue();
						}
						if (isNotEmpty(_ck)&&!id) {
							//复制表的操作
							var url = AppActionURI.columnDefine + "!copyFileds2newTableByLogicTable.json?logicTable_code="+_ck+"&newTable_id="+formData.id+"";
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
			var adjustUrl = moduleUrl + "!adjustShowOrder?P_tableTreeId=" + that.nodeId + "&P_sourceIds=" + sId + "&P_targetId=" + tId;
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
			ttbar.hideItem("logic");
			ttbar.hideItem("septr$03");
			ttbar.hideItem("export");
			ttbar.hideItem("septr$04");
			ttbar.hideItem("download");
			ttbar.hideItem("septr$05");
			ttbar.hideItem("import");
			ttbar.hideItem("septr$06");
			ttbar.showItem("syn$view");
		} else {
			layout.showToolbar();
			ttbar.showItem("add");
			ttbar.showItem("septr$01");
			ttbar.showItem("delete");
			ttbar.showItem("septr$02");
			ttbar.showItem("logic");
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
		if (treeType == "logic") {
			return moduleUrl + "!search.json?Q_EQ_logicTableCode=" + nodeId.substring(3) + "&P_orders=showOrder";
		} else{
			return moduleUrl + "!search.json?Q_EQ_tableTreeId=" + nodeId + "&P_orders=showOrder";
		}
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
		});
	};
	
	function deleteTableById(id) {
		dhtmlxAjax.get(moduleUrl + "/" + id + ".json?_method=delete", function(loader) {
			var obj = eval("(" + loader.xmlDoc.responseText + ")");
			that.reloadGrid();
			that.reloadTreeItem();
			dhtmlx.message(getMessage("delete_success"));
        });
	}
}
