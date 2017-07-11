<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>系统配置平台1.0-树定义</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appcommon.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/AppActionURI.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/treeNodeCopy.js"></script>
  </head>
  
 	<body scroll="no" onload="init()">
<script type="text/javascript">
	var _initObj = null;
	var oldTableId;
	function init(){
		var _this = this;
		_initObj = this;
		// 存放字段节点数据类型
		_initObj.cdtArr = [];
		this.moduleUrl = AppActionURI.treeDefine;
		this.nodeId = "-1";
		this.type = "1";
		this.tableId = "";
		this.rootId = null;
		this.tree = null;
		this.grid = this.gtbar = this.gurl = this.gcfg = null;
		this.form = this.ftabr = null;
		var dhxLayout = new dhtmlXLayoutObject("content", "3W");
		initLayout(dhxLayout, true);
		
		this.aLayout = dhxLayout.cells("a");
		this.bLayout = dhxLayout.cells("b");
		this.cLayout = dhxLayout.cells("c");
	 	// reload tree item
		this.reloadTreeItem = function() {
	 		var rule = _this.tree.getUserData(_this.nodeId, "nodeRule");
	 		if ("1" == rule) {
		 		var parentId = _this.tree.getParentId(_this.nodeId);
	 			_this.tree.refreshItem(parentId);
	 			//_this.tree.selectItem(_this.nodeId, false, "0");
	 		} else {
	 			_this.tree.refreshItem(_this.nodeId);
	 		}
		};
		// reload grid data
		this.reloadGrid = function() {
			loadGridData(_this.grid, _this.gcfg, getGridUrl(_this));
		};
		// reset form
		this.resetForm = function() {
			var form = _this.form;
			initFormData(form, true, true);
			// 
			form.setItemValue("child", "0");
			form.setItemValue("dynamic", "0");
			form.setItemValue("parentId", _this.nodeId);
			form.setItemValue("rootId", _this.rootId);
			form.setItemValue("tableId", _this.tableId);
		    form.showItem("name");
			form.hideItem("dbId");
			form.hideItem("value");
			form.checkItem("nodeRule","0");
			form.hideItem("sourceBlock");
			form.hideItem("sortBlock");
			form.uncheckItem("dataSource","0");
			form.uncheckItem("dataSource","1");
			form.uncheckItem("sortType","asc");
			form.uncheckItem("sortType","desc");
			form.setRequired("dbId", false);
			form.setRequired("value", false);
			//form.setRequired("name", true);
			form.setItemLabel("value", "节点编码：");
			form.hideItem("FS_greatProperty");
			// 
			_this.setNodeType();
			//隐藏高级配置
			//hideGreatPoperty();
		};
		// disable or enable node type
		this.setNodeType = function() {
			var form = _this.form;
			var type = _this.type;
			if (_this.nodeId == "-1") {
				form.checkItem("showRoot");
				form.showItem("showRootBlock");
				form.setItemValue("value", "");
				settingNodeType(_this, "0");
				form.uncheckItem("showNodeCount");
				form.disableItem("showNodeCount");
			} else {
				form.uncheckItem("showRoot");
				form.hideItem("showRootBlock");
				//emptyNode();
				if ("0" == type || "1" == type) {
					// 根节点或空节点：只能新增“空节点(1)”、“空字段节点(4)”、、“逻辑表组节点(7)”“物理表组节点(5)”、“表节点(2)”
					settingNodeType(_this, "1,2,4,5,6,7");
				} else if ("2" == type || "3" == type) {
					// 表节点或物理表组节点或字段节点（本表）：只能新增“字段节点（本表）”
					settingNodeType(_this, "3");
				} else if ("5" == type) {
					// 表节点或物理表组节点或字段节点（本表）：新增“表节点(2)”、“字段节点（本表）”
					settingNodeType(_this, "2,3");
				} else if ("4" == type) {
					// 字段节点（跨表）：只能新增“物理表组节点(5)”、“表节点(2)”
					settingNodeType(_this, "5,2,4");
				} else if ("6" == type || "7" == type) {
					// 工作流节点
					
				} else if ("9" == type) {
					// 工作箱节点
					settingNodeType(_this, "9");
				}
			}
		};
		// 节点类型改变时，触发事件
		this.changeTypeNode = function(type, tableId) {
			_initObj.cdtArr.length = 0;
			if ("1" == type) {
				emptyNode();
			} else if ("2" == type) {
				tableNode(tableId);
			} else if ("3" == type) {
				tableColumnNode();
			} else if ("4" == type){ // 跨表字段节点
				emptyColumnNode();
			} else if ("5" == type) { // table group
				tableGroupNode();
			} else if ("6" == type) { // coflow node
				coflowNode();
			} else if ("7" == type) { // logic node
				logicGroupNode();
			} else if ("9" == type) {
				boxNode(tableId);
			} 
		};
		// 初始化左边树
		initArchiveTree(_this);
		// 初始化在右边列表与表单
		initRightLayout(_this);
	}
	
	/**
	 * 表定义树初始化
	 */
	function initArchiveTree(that){
		var layout = that.aLayout;
		//layout.setText("档案定义树");
		layout.hideHeader();
		layout.setWidth(240);
		tree = layout.attachTree();
		// 树样式
		tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
		tree.attachEvent("onMouseIn", function(id) {
			tree.setItemStyle(id, "background-color:#D5E8FF;");
		});
		tree.attachEvent("onMouseOut", function(id) {
			tree.setItemStyle(id, "background-color:#FFFFFF;");
		});		
		var treeJson = {id:0, item:[{id:"-1",text:"树定义", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif",child:1}]};		
		tree.setDataMode("json");
		tree.enableSmartXMLParsing(true);

		tree.setXMLAutoLoading(that.moduleUrl + "!tree.json?E_model_name=tree&P_filterId=parentId&F_in=name,child&P_UD=type,dbId,tableId,nodeRule,rootId&P_orders=showOrder&Q_EQ_dynamic=0");
		tree.loadJSONObject(treeJson);
		tree.refreshItem("-1");
		tree.selectItem("-1", false, "0");
		//点击节点刷新右边列表
		tree.attachEvent("onClick", function(nId) {
			that.type = tree.getUserData(nId, "type");
			if (nId == -1) {
				that.rootId = null;
			} else {
				if (that.type == 0) {
					that.rootId = nId;
				} else {
					that.rootId = tree.getUserData(nId, "rootId");
				}
			}
			var ftbar = _initObj.ftbar;
			var form = _initObj.form;
			var gtbar = _initObj.gtbar;
			if(nId != "-1"){
				gtbar.showItem("septr$01");
				gtbar.showItem("copy");
			}else{
				gtbar.hideItem("septr$01");
				gtbar.hideItem("copy");
			}
			if("6" == that.type || "7" == that.type || "9" == that.type){
				ftbar.disableItem("new");
				ftbar.disableItem("save");
				form.lock();
			}else{
				form.unlock();
				ftbar.enableItem("new");
				ftbar.enableItem("save");
			}
			that.tableId = tree.getUserData(nId, "tableId");
			if (isEmpty(that.tableId)) {
				that.tableId = "";
			}
			form.setItemValue("tableId", null2empty(that.tableId));
			that.nodeId = nId;
			// reload grid data
			that.reloadGrid();
			// reset form 
			that.resetForm();
		});
	}
	/**
	 * 初始化右侧列表与表单
	 */
	function initRightLayout(that) {
		initArchiveGrid(that);
		initArchiveForm(that);
		that.setNodeType();
	}
	
	/**
	 * 初始化右侧列表
	 */
	function initArchiveGrid(that) {
		var layout = that.bLayout;
		layout.hideHeader();
		that.gtbar = layout.attachToolbar();
		that.grid  = layout.attachGrid();
		that.gcfg  = {
				format: {
					headers: ["<center>节点名称</center>","<center>节点类型</center>","<center>备注</center>"],
					   cols: ["name","type","remark"],
					userdata: ["nodeRule"],
					colWidths: ["180","120","*"],
					colTypes: ["ro","co","ro"],
					colAligns: ["left","left","left"]
				}
			};
		this.gurl = getGridUrl(that);
		
		var _this = this;
		var ttbar = that.gtbar;
		var grid  = that.grid;
		var cfg   = that.gcfg;
		var url   = that.gurl;
		ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
		ttbar.addButton("copy", 1, "复制", "copy.gif");
		ttbar.addSeparator("septr$01", 2);
		ttbar.addButton("del", 3, "删除", "delete.gif");
		if(that.nodeId == "-1"){
			ttbar.hideItem("septr$01");
			ttbar.hideItem("copy");
		}
		ttbar.attachEvent("onClick", function(itemId) {
			if ("copy" == itemId) {
				_this.copy();
			} else if ("del" == itemId) {
				_this.del();
			}
		});
		
		// 
		function setReserveArea (that) {
			var rowId = grid.getSelectedRowId();
			if (isEmpty(rowId)) {
				dhtmlx.alert("请选择一条记录！");
				return;
			}
			if (rowId.indexOf(",") > 1) {
				dhtmlx.alert("一次只能选择一条记录进行预留区配置！");
				return;
			}
			var h = 600;
        	if (h > document.body.clientHeight) {
        		h = document.body.clientHeight;
        	}
			var win = createDhxWindow({id:"win$setting", title:"树预留区设置", width:600, height:h});
			var layout = win.attachLayout("1C");
			initTreeReserveArea(layout.cells("a"), that, rootId);
		};
		// 复制事件
		this.copy = function() {
			var _this = this;
			if(!win){
				var h = 500;
	        	if (h > document.body.clientHeight) {
	        		h = document.body.clientHeight;
	        	}
	        	var win = createDhxWindow({id:"win$treeCopy", title:"树节点复制", width:500, height:h});
	       	 	initTreeCopyWin(win);
	        }
			
		};
		// 删除事件
		this.del = function() {
			var rowIds = grid.getSelectedRowId();
			if (null == rowIds || "" == rowIds) {
				dhtmlx.message(getMessage("select_record"));
				return;
			}
			dhtmlx.confirm({
				type:"confirm",
				text: getMessage("delete_warning"),
				ok: "确定",
				cancel: "取消",
				callback: function(flag) {
					if (flag) {
						var url = that.moduleUrl + "/" + rowIds + ".json?_method=delete";
						dhtmlxAjax.get(url, function(loader) {
							var obj = eval("(" + loader.xmlDoc.responseText + ")");
							if (obj.status == 1) {
								var id = obj.message;
								var name = grid.cells(id, 0).getValue();
								dhtmlx.alert("【" + name + "】有子节点，请先删除子节点！");
							} else if (obj.status == 2) {
								dhtmlx.alert(obj.message);
							} else {
								dhtmlx.message(getMessage("delete_success"));
								that.reloadGrid();
								that.resetForm();
								that.reloadTreeItem();
							}
						});
					}
				}
			}); 
		};
		grid.enableDragAndDrop(true);
		var typeCombo = grid.getCombo(1);
		typeCombo.put("0","树根节点");
		typeCombo.put("1","空节点");
		typeCombo.put("2","表节点");
		typeCombo.put("3","字段节点（本表）");
		typeCombo.put("4","字段节点（跨表）");
		typeCombo.put("5","物理表组节点");
		typeCombo.put("6","工作流节点");
		typeCombo.put("7","逻辑表组节点");
		typeCombo.put("9","工作箱");
		initGridWithoutPageable(grid, cfg, url);
		//
		grid.attachEvent("onRowSelect", function(id,ind) {
			_this.view(id);
		});
		grid.attachEvent("onDrag",function(sIds,tId) {
			return _this.drag(sIds,tId);
		});
		grid.attachEvent("onDrop", function(sIds,tId,dId,sObj,tObj,sCol,tCol) {
			_this.drop(sIds,tId);
		});//*/
		// 单击查看
		this.view = function(id) {
			if (id.indexOf(",") > -1) {
				return;
			};
			// init form data
			var updateUrl = that.moduleUrl + "/" + id + ".json";
			var formData = loadJson(updateUrl);			
			var nt = formData.type;			
			// 节点类型不可修改
			settingNodeType(that, nt);
			// 
			that.changeTypeNode(nt, formData.tableId);
			var form = that.form;
			if ("3" == nt) {
				columnChange(form, formData.dbId, formData.nodeRule);
			} else if ("4" == nt && "0" != formData.nodeRule) {
				// TODO
				var opts = loadJson(contextPath +"/code/code-type!getCodeTypeSelect.json");
				var valueCombo = {type: "combo", label: "编码类型", name: "value", options: opts, labelWidth: 100,width: 200,labelAlign:"right", required: true};
				form.removeItem("value");
				form.addItem("nodeBlock", valueCombo);
				form.checkItem("dataSource", "1");
				form.disableItem("dataSource", "0");
			}
			form.setFormData(formData);
		};
		// 拖拽时
		this.drag = function(sIds, tId) {
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
						dhtmlx.alert("请选择连续的记录进行调整顺序！");
						return false;
					}
					preIdx = curIdx;
				}
			}
			return true;
		};
		// 拖拽释放时
		this.drop = function(sIds,tId) {
			if(undefined != tId){
				var adjustUrl = that.moduleUrl + "!adjustShowOrder?P_parentId=" + that.nodeId + "&P_sourceIds=" + sIds + "&P_targetId=" + tId;
				dhtmlxAjax.get(adjustUrl, function(loader) {
					that.reloadGrid();
					that.grid.selectRowById(sIds);
					that.reloadTreeItem();
				});//*/	
			}			
		};
		
	}
	/**
	 * 初始化右侧表单
	 */
	function initArchiveForm(that) {
		// 而在设置
		var layout = that.cLayout;
		layout.hideHeader();
		layout.setWidth(460);
		// 初始化变量
		that.ftbar = layout.attachToolbar();
		that.form  = layout.attachForm(getFormJson(that));
	
		var _this = this;
		var ftbar = that.ftbar;
		var form  = that.form;
	
		ftbar.setIconsPath(TOOLBAR_IMAGE_PATH);
		ftbar.addButton("new", 1, "新增", "new.gif");
		ftbar.addSeparator("top$septr$02", 2);
		ftbar.addButton("addColumnFilter", 3, "配置条件", "new.gif");
		ftbar.addSeparator("top$septr$01", 4);
		ftbar.addButton("save", 5, "保存", "save.gif");
		ftbar.attachEvent("onClick", function(itemId) {
			if ("save" == itemId) {
				_this.save();
			} else if ("new" == itemId) {
				that.resetForm();
			} else if ("addColumnFilter" == itemId) {
				addColumnFilter();
			}
		});
		this.save = function () {
			var id   = form.getItemValue("id");
			var name = form.getItemValue("name");
    		var nt = form.getCheckedValue("type");
			if (isEmpty(name)) {
				if ("6" == nt) {
					dhtmlx.alert("请选择工作流！");
				} else {
					dhtmlx.alert("请输入节点名称！");
				}
				return;
			}
			var sUrl = that.moduleUrl;
			form.setItemValue("rootId", that.rootId);
			form.setItemValue("parentId", that.nodeId);
	    	if (id == "") {
	    		sUrl += ".json"; form.setItemValue("_method", "post");
	    	} else {
	    		sUrl += "/" + id + ".json";
	    		form.setItemValue("_method", "put");
	    	}
    		var CHE_Name_Url = that.moduleUrl + "!checkUnique.json?id=" + id + "&Q_EQ_parentId=" + _initObj.nodeId + "&Q_EQ_name=" + name;
    		dhtmlxAjax.get(encodeURI(CHE_Name_Url),function(loader){
	            var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
	            if (false == jsonObj.success) {
	            	dhtmlx.alert("该节点已存在，请重新输入节点名称！");
	            } else {
	            	if ("2" == nt || "5" == nt) { // 表节点 和物理表组节点
						 if("" != id && oldTableId != form.getItemValue("tableId")){
							dhtmlx.confirm({
								text: "<font color='red'>如果更换表，则该节点下的所有字段节点将被清空，请慎重！</font>",
								ok: "确定",
								cancel: "取消",
								callback: function(flag) {
									if (flag) {
										checkAndSubmit(that,sUrl);									
									}
								}
							});
						} else { 					
	    					checkAndSubmit(that,sUrl);
					  	}
	    			} else if ("4" == nt && isNotEmpty(id)) { // 字段标签节点修改校验子节点中为表节点和表组是否存在该字段标签
	   					var checkUrl = _initObj.moduleUrl + "!checkTableNode.json?id=" + id + "&P_columnLabel=" + form.getItemValue("dbId");
	   					dhtmlxAjax.get(encodeURI(checkUrl), function(loader) {
	       					var obj = eval("(" + loader.xmlDoc.responseText + ")");
	       					if(false == obj.success){
	       						dhtmlx.alert(obj.message);
	       			    	}else{
					    		form.send(sUrl, "post", function(loader, response) {
									var formData = eval("(" + loader.xmlDoc.responseText + ")");
									if (formData.id == null || formData.id == "") {
										dhtmlx.alert(getMessage("save_failure"));
										return;
									}
									form.setFormData(formData);
									that.reloadGrid();
									that.resetForm();
									that.reloadTreeItem();
									dhtmlx.message(getMessage("save_success"));
								});
					    	}
	       				});
    				} else {
    					form.send(sUrl, "post", function(aloader, response) {
    					var formData = eval("(" + aloader.xmlDoc.responseText + ")");
    					if (formData.id == null || formData.id == "") {
    						dhtmlx.alert(getMessage("save_failure"));
    						return;
    					}
    					form.setFormData(formData);
    					that.reloadGrid();
    					that.resetForm();
    					that.reloadTreeItem();
    					dhtmlx.message(getMessage("save_success"));
    				});
    			}	
            }});			    			
		};
		
		//
		function addColumnFilter() {
    		var win, nt = form.getCheckedValue("type"),
    		    tableId = form.getItemValue("tableId");
			if ("2" == nt || "5" == nt) {
				if (isEmpty(tableId)) {
					dhtmlx.message("请先选“物理表组”或“物理表”，再添加！");
					return;
				}
			    win = createDhxWindow({
			        id : "win$addcolumnfilter",
			        title : "添加条件",
			        width : 480,
			        height : 260
			    });
			    win.button("close").attachEvent("onClick", function() {
			        win.close();
			    });
			    var mbsbar = win.attachStatusBar();
			    var mbbtbar = new dhtmlXToolbarObject(mbsbar.id);
			    mbbtbar.setIconPath(IMAGE_PATH);
			    mbbtbar.addButton("bottom$save", 0, "确定", "save.gif");
			    mbbtbar.addSeparator("bottom$septr$01", 1);
			    mbbtbar.addButton("bottom$clear", 2, "清空", "delete.gif");
			    mbbtbar.addSeparator("bottom$septr$01", 3);
			    mbbtbar.addButton("bottom$close", 4, "取消", "default/close.png");
			    mbbtbar.setAlign("right");
			    mbbtbar.attachEvent("onClick", function(id) {
			    	if ("bottom$save" == id) {
			    		var multisel = cfilterForm.getSelect("columnFilter");
			    		var opts = multisel.options;
			    		var columns = "";
			    		/*if (opts.length === 0) {
			    			dhtmlx.message("请添加过滤条件");
			    		} else {*/
			    			for (var i = 0; i < opts.length; i++) {
			    				columns += (";" + opts[i].value); 
			    			}
			    			if (columns.length > 0) columns = columns.substring(1);
			    			form.setItemValue("columnFilter", columns);
				    		closeFilterWin ();
			    		//}
			    	} else if ("bottom$clear" == id) {
			    		var multisel = cfilterForm.getSelect("columnFilter");
			    		multisel.options.length = 0;
			    	} else {
			    		closeFilterWin ();
			    	}
			    });
			    var cfilterForm = initColumnFilterWin(win, form);
			} else {
				dhtmlx.message("只有“物理表组”和“物理表”节点才能添加条件！");
			}
			
			function closeFilterWin () {
				win.close();
			}
		}
		
		form.attachEvent("onChange", function (id, value){
			if ("type" == id) {
				that.changeTypeNode(value);
			} else if ("layoutType" == id) {
				_this.setLayoutArea(value);
			} else if ("dbId" == id) {
				var nt = form.getCheckedValue("type");
				// 表节点
				if ("2" == nt) {
					form.setItemValue("tableId", value);
					if (isNotEmpty(value)) {
						// 检查父节点中是否有空节点，如果有的话，是否在该表中
					}
				} else if ("3" == nt) { // 表字段节点
					if ("1" == form.getCheckedValue("nodeRule") || "2" == form.getCheckedValue("nodeRule")) {
						if (isEmpty(_initObj.cdtArr[value])) {
							form.disableItem("dataSource", 1);
			 			} else {
			 				form.enableItem("dataSource", 1);
			 			}
					}
					columnChange(form, value);
				} else if ("6" == nt) {
					// 工作流节点
					if (isNotEmpty(value)) {
						var text = form.getCombo("dbId").getComboText();
						// 获取工作流绑定业务表ID
						var url  = AppActionURI.workflowDefine + "!edit.json?id=" + value;
						var entity = loadJson(url);
						if (value === entity.id) {
							form.setItemValue("name", entity.workflowName);
							form.setItemValue("tableId", entity.businessTableId);
						} else {
							dhtmlx.alert("工作流【" + text + "】不存在，请刷新后再配置！");
							form.setItemValue("dbId", "");
							form.setItemValue("tableId", "");
						}//*/
					} else {
						form.setItemValue("name", "");
						form.setItemValue("tableId", "");
					}
				} else if ("5" == nt) {
					// 物理表组节点
					if (isNotEmpty(value)) {
						var text = form.getCombo("dbId").getComboText();
						// 校验物理表组中是否已经添加的相应物理表
						var url = _initObj.moduleUrl + "!checkTableGroup.json?P_groupId=" + value;
						var json = loadJson(url);
						if (false === json.success) {
							form.setItemValue("dbId", "");
							form.setItemValue("tableId", "");
							dhtmlx.alert(json.message);
						} else {
							form.setItemValue("tableId", json.message);
						}
					}
				}
			} else if ("nodeRule" == id) {
				var type = form.getCheckedValue("type");
				if ("3" != type && "4" != type) {
					dhtmlx.alert("该节点类型不是字段节点，不能作为动态节点！");
					form.checkItem("nodeRule", "0");
					return;
				}
				//var rule = form.getCheckedValue("nodeRule");
				if (value == "1" || value == "2") {
					if ("3" == type) { // 字段节点（本表）
						form.hideItem("value");
						form.setRequired("value", false);
		 				form.enableItem("dataSource", "0");
						form.checkItem("dataSource", "0");
						var dbId = form.getItemValue("dbId");
						if (isEmpty(dbId) || isEmpty(_initObj.cdtArr[dbId])) {
							form.disableItem("dataSource", "1");
			 			} else {
			 				form.enableItem("dataSource", "1");
			 			}
					} else { // 字段节点（跨表）
						var opts = loadJson(contextPath +"/code/code-type!getCodeTypeSelect.json");
						var valueCombo = {type: "combo", label: "编码类型", name: "value", options: opts, labelWidth: 100,width: 200,labelAlign:"right", required: true};
						form.removeItem("value");
						form.addItem("nodeBlock", valueCombo);
						form.checkItem("dataSource", "1");
						form.disableItem("dataSource", "0");
					}
					form.showItem("sourceBlock");
					form.showItem("sortBlock");
					form.checkItem("sortType", "asc");
				} else {
					if ("4" == type) {
						var valueInput = {type: "input", label: "字符值", name: "value", labelWidth: 100,width: 200,labelAlign:"right", required: true};
						form.removeItem("value");
						form.addItem("nodeBlock", valueInput);
					} else {
						form.setRequired("value", true);
						form.showItem("value");
					}
					form.hideItem("sourceBlock");
					form.hideItem("sortBlock");
					form.uncheckItem("dataSource","0");
					form.uncheckItem("dataSource","1");
					form.uncheckItem("sortType","asc");
					form.uncheckItem("sortType","desc");
				}
			}
			if (("dataSource" == id && "1" == value) || 
					("nodeRule" == id && ("1" == form.getCheckedValue("nodeRule") || "2" == form.getCheckedValue("nodeRule")) && 
					"1" == form.getItemValue("dataSource"))) {
	 			var type = form.getItemValue("type");
	 			if ("4" == type) {
	 				return;
	 			}
	 			var combo = form.getCombo("dbId");
	 			var idx = combo.getSelectedValue();
	 			if (isEmpty(_initObj.cdtArr[idx])) {
	 				dhtmlx.alert("该字段不是编码类型，请选择编码类型字段！");
					form.checkItem("dataSource", 0);
	 			}	 			
			}
		});
		
		// reset form
		that.resetForm();
	}
	
	function initColumnFilterWin(win, form) {
		var tableId = form.getItemValue("tableId");
		var cfilterForm = win.attachForm(getColumnFilterFormJson(tableId));
	    var columnFilter = form.getItemValue("columnFilter");
	    
	    if (isNotEmpty(columnFilter)) {
	    	var opts = columnFilter.split(";");

			var multisel = cfilterForm.getSelect("columnFilter");
			
			for (var i = 0; i < opts.length; i++) {
				multisel.options.add(new Option(opts[i], opts[i]));
			}
	    }
	    
		cfilterForm.attachEvent("onButtonClick", function (id) {
			if ("add" == id) {
				var columnName  = cfilterForm.getItemValue("columnName");
				var filterType  = cfilterForm.getItemValue("filterType");
				var columnValue = cfilterForm.getItemValue("columnValue");
				if (isEmpty(columnName)) {
					dhtmlx.message("请选择字段！");
					return;
				}
				if (isEmpty(filterType)) {
					dhtmlx.message("请选择条件！");
					return;
				}
				if (isEmpty(columnValue)) {
					dhtmlx.message("请填定字段过滤值！");
					return;
				} else if (columnValue.match(/\"|\'|;/)) {
					dhtmlx.message("值不能包含特殊字符(\" ' ;)！");
					return;
				}
				var col = filterType + "_C_" + columnName + "≡" + columnValue;
				var multisel = cfilterForm.getSelect("columnFilter");
				for (var i = 0; i < multisel.options.length; i ++) {
					if (multisel.options[i].value == col) {
						return;
					} 
				}
				multisel.options.add(new Option(col, col))
			} else if ("del" == id) {
				var multisel = cfilterForm.getSelect("columnFilter");
				for (var i = multisel.options.length - 1; i >= 0 ; i--) {
					var opt = multisel.options[i];
					if (opt.selected) {
						multisel.options.remove(i);
					}
				}
			}
		});
		
		return cfilterForm;
	}
	
	function getColumnFilterFormJson(tableId) {
		var fopts = [{value: "EQ", text: "等于"},{value: "LIKE", text: "包含"}],
		    copts = loadJson(AppActionURI.columnDefine + "!comboOfTableColumns.json?P_tableId=" + tableId + "&P_optionValue=1");
		return [
	    	{type: "block", width: 420, name:"labelBlock", position: "absolute", offsetLeft: 10, offsetTop: 10, list:[
  				{type: "itemlabel", label: "字段", labelWidth: 100, labelAlign:"left"},
  				{type:"newcolumn"},
  				{type: "itemlabel", label: "条件", labelWidth: 60, labelAlign:"left"},
  				{type:"newcolumn"},
  				{type: "itemlabel", label: "值", labelWidth: 200, labelAlign:"left"}
   			]},
    		{type: "block", width: 420, name:"editBlock", position: "absolute", offsetLeft: 10, list:[
				{type: "combo", label: "", name: "columnName", width: 100, options: copts},
				{type:"newcolumn"},
				{type: "combo", label: "", name: "filterType", width: 60, options: fopts},
				{type:"newcolumn"},
				{type: "input", label: "", name: "columnValue", width: 200, maxLength:50},
				{type:"newcolumn"},
				{type: "button", name: "add", value: "添加", width:40}
 			]},
    		{type: "block", width: 420, name:"totalBlock", position: "absolute", offsetLeft: 10, list:[
    		    {type: "multiselect", name: "columnFilter", inputHeight:120, inputWidth:363},
				{type:"newcolumn"},
				{type: "button", name: "del", value: "删除", width:40}
    		]}
       ];
	}
	
	function columnChange(form, columnId, nodeRule) { 
		var idx = columnId;
		if (undefined == _initObj.cdtArr[idx]) {
			dhtmlx.message("请在下拉列表中选择");
			form.setItemValue("dbId","");
			return;
		} else {
			var valueItem  = {type: "input", label: "字段值", name: "value", labelWidth: 100,width: 200,labelAlign:"right", required: true};
			var valueCombo = {type: "combo", label: "字段值", name: "value", labelWidth: 100,width: 200,labelAlign:"right", required: true};
			var val  = form.getItemValue("value");
			var rule = nodeRule || form.getItemValue("nodeRule");
			if ("1" != rule && "2" != rule) {
				if (isNotEmpty(_initObj.cdtArr[idx])) {
					form.removeItem("value");
					form.addItem("nodeBlock", valueCombo);
					form.setRequired("value",true);
					//cancelAuto(form);
					var vCombo = form.getCombo("value");
					vCombo.clearAll();			
					var vUrl = AppActionURI.columnDefine + "!valComboOfTableColumns.json?P_id=" + columnId;
					var vOpts = loadJson(vUrl);
					vCombo.addOption(vOpts);
					if("" != val){
						vCombo.setComboValue(val);
					}					
				} else {
					form.removeItem("value");
					form.addItem("nodeBlock", valueItem);
					if("" != val){
						form.setItemValue("value", val);
					}
				}
			} else {
				form.setItemValue("value","");
				form.hideItem("value");
				form.setRequired("value", false);
				form.showItem("sourceBlock");
				form.showItem("sortBlock");
				if (isEmpty(_initObj.cdtArr[idx])) form.checkItem("dataSource", 0);
			}	
		}										
	}
	function checkAndSubmit(that,sUrl) {
		var columnCheck = true;
   		var checkUrl = _initObj.moduleUrl + "!checkColumnLabel.json?P_parentId=" + _initObj.nodeId + "&P_tableId=" + form.getItemValue("tableId");
    	var columnCheck = true;
    	dhtmlxAjax.get(checkUrl, function(loader) {
			var obj = eval("(" + loader.xmlDoc.responseText + ")");
			if(false == obj.success){
				dhtmlx.alert(obj.message);
	    	}else{
	    		form.send(sUrl, "post", function(loader, response) {
					var formData = eval("(" + loader.xmlDoc.responseText + ")");
					if (formData.id == null || formData.id == "") {
						dhtmlx.alert(getMessage("save_failure"));
						return;
					}
					form.setFormData(formData);
					that.reloadGrid();
					that.resetForm();
					that.reloadTreeItem();
					dhtmlx.message(getMessage("save_success"));
				});
	    	}
    	});
	}
	/**
	 * 列表检索URL
	 **/
	function getGridUrl(that) {
//		return that.moduleUrl + "!search.json?Q_EQ_parentId=" + that.nodeId + "&Q_EQ_dynamic=0&P_orders=showOrder";
		return that.moduleUrl + "!search.json?Q_EQ_parentId=" + that.nodeId + "&P_orders=showOrder";
	}
	/**
	 * 空节点
	 **/
	function emptyNode() {	
		var form = _initObj.form;
		var valueItem = {type: "input", label: "节点编码：", name: "value", labelWidth: 100, width: 200,labelAlign:"right"};
		form.showItem("name");
		form.hideItem("filterBlock");
		form.removeItem("value");
		form.addItem("nodeBlock", valueItem);
		form.hideItem("sourceBlock");
		form.hideItem("sortBlock");
		form.hideItem("dbId");
		form.setItemValue("tableId", "");
		form.setItemValue("name", "");
		form.checkItem("nodeRule","0");
		form.disableItem("nodeRule","1");
		form.disableItem("nodeRule","2");
		form.uncheckItem("dataSource", "0");
		form.uncheckItem("dataSource", "1");
		form.uncheckItem("sortType", "asc");
		form.uncheckItem("sortType", "desc");
		form.setItemValue("dbId", "");
		form.setRequired("dbId", false);
		form.hideItem("FS_greatProperty");
		form.setItemValue("tableId", _initObj.tableId);
		form.uncheckItem("showNodeCount");
		form.disableItem("showNodeCount");
	}
	/**
	 * 表节点
	 **/
	function tableNode(tableId) {
		oldTableId = tableId;
		var pType   = _initObj.tree.getUserData(_initObj.nodeId, "type");
		var groupId = "";
		if (pType ==  "5") {
			groupId = _initObj.tree.getUserData(_initObj.nodeId, "dbId");
		}
		var tOpts = loadJson(AppActionURI.physicalTableDefine + "!comboOfTables.json?P_groupId=" + groupId);
		var dbIdCombo = {type: "combo", label: "选择表", name: "dbId", labelWidth: 100, width: 200, labelAlign:"right", required: true, options: tOpts};
		var valueItem = {type: "input", label: "字段值：", name: "value", labelWidth: 100, width: 200,labelAlign:"right"};
		var form = _initObj.form;
		form.showItem("name");
		form.showItem("filterBlock");
		form.setItemValue("tableId", "");
		form.hideItem("sourceBlock");
		form.hideItem("sortBlock");
		form.setItemValue("name", "");
		form.removeItem("dbId");
		form.removeItem("value");
		form.addItem("nodeBlock", dbIdCombo);
		form.addItem("nodeBlock", valueItem);
		form.hideItem("value");
		form.checkItem("nodeRule","0");
		form.disableItem("nodeRule","1");
		form.disableItem("nodeRule","2");
		form.uncheckItem("dataSource","0");
		form.uncheckItem("dataSource","1");
		form.uncheckItem("sortType","asc");
		form.uncheckItem("sortType","desc");
		form.showItem("FS_greatProperty");
		form.enableItem("showNodeCount");
	}
	/**
	 * 空字段节点
	 **/
	function emptyColumnNode() {
		var cOpts = loadJson(AppActionURI.columnLabel + "!combobox.json?E_frame_name=coral&E_model_name=combobox&F_in=code,name&P_orders=showOrder");
		cOpts.unshift({value:"", text:"请选择字段标签"});
		var dbIdItem = {type: "combo", label: "字段标签", name: "dbId", labelWidth: 100, width: 200,labelAlign:"right", required: true, options: cOpts};
		var valueItem = {type: "input", label: "字段值", name: "value", labelWidth: 100, width: 200,labelAlign:"right", required: true};
		var form = _initObj.form;
		form.showItem("name");
		form.hideItem("filterBlock");
		form.setItemValue("name", "");
		form.setItemValue("tableId", "");
		form.removeItem("dbId");
		form.removeItem("value");
		form.hideItem("sourceBlock");
		form.hideItem("sortBlock");
		form.checkItem("nodeRule","0");
		form.disableItem("nodeRule","1");
		form.enableItem("nodeRule","0");
		form.enableItem("nodeRule","2");
		form.uncheckItem("dataSource","0");
		form.uncheckItem("dataSource","1");
		form.uncheckItem("sortType","asc");
		form.uncheckItem("sortType","desc");
		form.addItem("nodeBlock",dbIdItem);
		form.addItem("nodeBlock", valueItem);
		form.uncheckItem("showNodeCount");
		form.disableItem("showNodeCount");
		form.showItem("FS_greatProperty");
	}
	/**
	 * 物理表组节点
	 **/
	function tableGroupNode() {
		var pGroup = loadJson(AppActionURI.physicalGroupDefine + "!combobox.json?E_frame_name=coral&E_model_name=combobox&F_in=id,groupName&P_orders=showOrder");
		if (null == pGroup) pGroup = [];
		pGroup.unshift({value:"", text:"请选择物理表组", selected:true});
		var dbIdItem = {type: "combo", label: "物理表组", name: "dbId", labelWidth: 100, width: 200,labelAlign:"right", required: true, options: pGroup};
		var form = _initObj.form;
		form.showItem("name");
		form.showItem("filterBlock");
		form.setItemValue("name", "");
		form.removeItem("dbId");
		form.removeItem("value");
		form.hideItem("FS_greatProperty"); // 隐藏高级配置
		form.hideItem("sourceBlock");
		form.hideItem("sortBlock");
		form.checkItem("nodeRule","0");
		form.disableItem("nodeRule","1");
		form.disableItem("nodeRule","2");
		form.uncheckItem("dataSource","0");
		form.uncheckItem("dataSource","1");
		form.uncheckItem("sortType","asc");
		form.uncheckItem("sortType","desc");
		form.addItem("nodeBlock",dbIdItem);
		form.uncheckItem("showNodeCount");
		form.disableItem("showNodeCount");
	}
	/**
	 * 物理表组节点
	 **/
	function logicGroupNode() {
		var pGroup = loadJson(AppActionURI.logicGroupDefine + "!combobox.json?E_frame_name=coral&E_model_name=combobox&F_in=code,groupName&P_orders=showOrder");
		if (null == pGroup) pGroup = [];
		pGroup.unshift({value:"", text:"请选择逻辑表组", selected:true});
		var dbIdItem = {type: "combo", label: "逻辑表组", name: "dbId", labelWidth: 100, width: 200,labelAlign:"right", required: true, options: pGroup};
		var form = _initObj.form;
		form.showItem("name");
		form.hideItem("filterBlock");
		form.setItemValue("name", "");
		form.removeItem("dbId");
		form.removeItem("value");
		form.showItem("FS_greatProperty"); // 显示高级配置
		form.hideItem("sourceBlock");
		form.hideItem("sortBlock");
		form.checkItem("nodeRule","2");
		form.disableItem("nodeRule","0");
		form.disableItem("nodeRule","1");
		form.enableItem("nodeRule","2");
		form.uncheckItem("dataSource","0");
		form.uncheckItem("dataSource","1");
		form.uncheckItem("sortType","asc");
		form.uncheckItem("sortType","desc");
		form.addItem("nodeBlock",dbIdItem);
		form.uncheckItem("showNodeCount");
		form.disableItem("showNodeCount");
	}
	/**
	 * 工作流节点
	 **/
	function coflowNode() {
		var cfOpts = loadJson(AppActionURI.workflowDefine + "!combobox.json?E_frame_name=coral&E_model_name=combobox&F_in=id,workflowName&P_orders=showOrder");
		if (null == cfOpts) cfOpts = [];
		cfOpts.unshift({value:"", text:"请选择工作流程", selected:true});
		//cfOpts.push({value:"1", text:"借阅流程"});
		var dbIdItem = {type: "combo", label: "工作流程", name: "dbId", labelWidth: 100, width: 200,labelAlign:"right", required: true, options: cfOpts};
		var form = _initObj.form;
		form.hideItem("name");
		form.hideItem("filterBlock");
		//form.setRequired("name", false);
		form.setItemValue("name", "");
		form.removeItem("dbId");
		form.removeItem("value");
		form.hideItem("FS_greatProperty"); // 隐藏高级配置
		form.hideItem("sourceBlock");
		form.hideItem("sortBlock");
		form.checkItem("nodeRule","0");
		form.disableItem("nodeRule","1");
		form.disableItem("nodeRule","2");
		form.uncheckItem("dataSource","0");
		form.uncheckItem("dataSource","1");
		form.uncheckItem("sortType","asc");
		form.uncheckItem("sortType","desc");
		form.addItem("nodeBlock",dbIdItem);
		form.uncheckItem("showNodeCount");
		form.disableItem("showNodeCount");
	}
	/**
	 * 表字段节点
	 **/
	function tableColumnNode() {
		var dbIdCombo  = {type: "combo", label: "字段名称", name: "dbId", labelWidth: 100, width: 200,labelAlign:"right", required: true};
		var valueItem  = {type: "input", label: "字段值", name: "value", labelWidth: 100, width: 200,labelAlign:"right", required: true};
		var valueCombo = {type: "combo", label: "字段值", name: "value", labelWidth: 100, width: 200,labelAlign:"right", required: true};
		var form = _initObj.form;
		form.showItem("name");
		form.hideItem("filterBlock");
		form.setItemValue("name", "");
		form.setItemValue("tableId", _initObj.tableId);
		form.removeItem("dbId");
		form.removeItem("value");
		form.addItem("nodeBlock", dbIdCombo);
		form.addItem("nodeBlock", valueItem);
		var cCombo = form.getCombo("dbId");
		cCombo.clearAll();
		form.showItem("FS_greatProperty");
		form.checkItem("nodeRule","0");
		form.enableItem("nodeRule","1");
		form.enableItem("nodeRule","2");
		var cUrl = AppActionURI.columnDefine + "!comboOfTableColumns.json?P_tableId=" + _initObj.tableId;
		var cOpts = loadJson(cUrl);
		if (isEmpty(cOpts)) cOpts = [];
		//cOpts.unshift({value:"", text:"请选择字段", selected:true});
		cCombo.addOption(cOpts);
		//form.getCombo("nameRuleId").addOption(cOpts);
		_initObj.cdtArr.length = 0;
		for(var i=0;i<cOpts.length;i++){				
			_initObj.cdtArr[cOpts[i].value] = cOpts[i].prop;
		}		
		form.hideItem("sourceBlock");
		form.hideItem("sortBlock");
		form.uncheckItem("dataSource","0");
		form.uncheckItem("dataSource","1");
		form.uncheckItem("sortType","asc");
		form.uncheckItem("sortType","desc");
		combo_option_filter(_initObj.nodeId,cCombo);
		form.enableItem("showNodeCount");
	}
	function combo_option_filter(nId, combo) {
		if (undefined == combo || null == combo) {
			return combo;
		} 
		var columnId = tree.getUserData(nId, "dbId");
		var type     = tree.getUserData(nId, "type");
		var parentId = tree.getParentId(nId);
		if ("3" == type) {
			combo.deleteOption(columnId);
		}
		if (parentId == -1) {
			return combo;
		}
		return combo_option_filter(parentId, combo);
	}
	/** 工作箱点.**/
	function boxNode(tableId) {
		var ndbIdComboItem={type: "combo", label: "选择表", name: "dbId", labelWidth: 100, width: 200, labelAlign:"right", required: true};
		var valueComboItem={type: "combo", label: "字段值", name: "value", labelWidth: 100, width: 200, labelAlign:"right", required: true};
		var form = _initObj.form;
		form.showItem("name");
		form.hideItem("sourceBlock");
		form.hideItem("sortBlock");
		form.hideItem("filterBlock");
		form.checkItem("nodeRule","0");
		form.disableItem("nodeRule","1");
		form.disableItem("nodeRule","2");
		//form.setItemValue("nameRuleId","");
		form.uncheckItem("dataSource","0");
		form.uncheckItem("dataSource","1");
		form.uncheckItem("sortType","asc");
		form.uncheckItem("sortType","desc");
		form.setItemValue("name", "");
		form.removeItem("dbId");
		form.removeItem("value");
		form.addItem("nodeBlock",ndbIdComboItem);
		form.addItem("nodeBlock", valueComboItem);
		var vCombo = form.getCombo("value");
		vCombo.clearAll();
		vCombo.addOption([{value:"applyfor", text:"申请箱"},
				{value:"todo", text:"待办箱"},
				{value:"hasdone", text:"已办箱"},
				{value:"complete", text:"办结箱"},
				{value:"toread", text:"待阅箱"}]);	
		form.setItemLabel("value", "工作箱");
		form.setRequired("value", true);
		var tUrl = AppActionURI.physicalTableDefine + "!comboOfTables.json";
		var tOpts = loadJson(tUrl);
		var tCombo = form.getCombo("dbId");
		tCombo.clearAll();
		tCombo.addOption(tOpts);
		form.enableItem("showNodeCount");
		//hideGreatPoperty();
	}
	
	/**
	 * FORM JSON格式
	 **/
	function getFormJson(that) {
		var formJson = [
		    {type: "setting", labelWidth: 100, inputWidth: 260},
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "rootId", value: that.rootId},
			{type: "hidden", name: "parentId", value: that.nodeId},
			{type: "hidden", name: "tableId", value: that.tableId},
			{type: "hidden", name: "showOrder"},
			{type: "hidden", name: "child"},
			{type: "hidden", name: "dynamic"},
			{type: "hidden", name: "columnNames"},
			{type: "hidden", name: "columnValues"},
			{type: "hidden", name: "remark"},
			{type: "hidden", name: "dynamicFromId"},
			{type: "fieldset",  name: "FS_node", label: "节点配置", width:420, list:[
				{type: "block", width: 420, name: "typeBlock", position: "absolute", offsetLeft: 0, list:[ 
					    {type: "itemlabel", label: "节点类型：", labelWidth: 100, labelAlign:"right"},
						{type:"newcolumn"},
						{type: "block", width: "320", position: "absolute", offsetLeft: 0, list:[
							{type: "radio", label: "树根节点", name: "type", value:"0", labelWidth: 120,position:"label-right", labelAlign:"left"},
							{type: "radio", label: "逻辑表组节点",name: "type", value:"7", labelWidth: 120, position:"label-right", labelAlign:"left"},
							{type: "radio", label: "表节点", name: "type", value:"2", labelWidth: 120, position:"label-right", labelAlign:"left"},
							{type: "radio", label: "字段节点（跨表）",name: "type", value:"4", labelWidth: 120, position:"label-right", labelAlign:"left"},
							{type:"newcolumn"},
							{type: "radio", label: "空节点", name: "type", value:"1", labelWidth: 120,position:"label-right", labelAlign:"left"},
							{type: "radio", label: "物理表组节点", name: "type", value:"5", labelWidth: 120, position:"label-right", labelAlign:"left"},
							{type: "radio", label: "工作流程节点", name: "type", value:"6", labelWidth: 120, position:"label-right", labelAlign:"left"},
							{type: "radio", label: "字段节点（本表）",name: "type", value:"3", labelWidth: 120, position:"label-right", labelAlign:"left"}
							//{type: "radio", label: "工作箱",name: "type", value:"9", labelWidth: 120, position:"label-right", labelAlign:"left"}
						]}
				]},
			    {type: "block", width: 420, name:"nodeBlock", position: "absolute", offsetLeft: 0, list:[
					{type: "input", label: "节点名称", name: "name", labelWidth: 100, width: 200, labelAlign:"right", maxLength:50,required: true},
					{type: "block", width: 420, name: "showRootBlock", position: "absolute", offsetLeft: 0, list:[ 
						    {type: "itemlabel", label: "　", labelWidth: 100, labelAlign:"right"},
							{type:"newcolumn"},
							{type: "block", width: "280", position: "absolute", offsetLeft: 0, list:[
								{type: "checkbox", label: "显示根节点", name: "showRoot", position:"label-right", labelAlign:"left", checked: true}
							]}
					]},
  					{type: "combo", label: "选择表", name: "dbId", labelWidth: 100, width: 200, labelAlign:"right", required: true},
  					{type: "combo", label: "字段值", name: "value", labelWidth: 100, width: 200, labelAlign:"right", required: true}
  				]},
			    {type: "block", width: 420, name:"filterBlock", position: "absolute", offsetLeft: 0, list:[
 					{type: "input", label: "字段条件： ", name: "columnFilter", labelWidth: 100, readonly: true, width: 200, labelAlign:"right", maxLength:250, rows: 3},
   				]}
			]},
			{type: "fieldset",  name: "FS_greatProperty", label: "高级配置", width:420, list:[
				{type: "block", width: 420, name: "gpBlock", position: "absolute", offsetLeft: 0, list:[ 
					{type: "block", width: 420, name: "greatBlock", position: "absolute", offsetLeft: 0, list:[ 
	   					    {type: "itemlabel", label: "　", labelWidth: 100, labelAlign:"right"},
	   					    //{type: "itemlabel", label: "　", labelWidth: 100, labelAlign:"right"},
	   						{type:"newcolumn"},
							//{type: "checkbox", label:"自动生成工作箱", name: "P_auto", position:"label-right", labelAlign:"left"},
							{type: "checkbox", label:"节点数量显示", name: "showNodeCount", position:"label-right", labelAlign:"left"}
	   				]},
					{type: "block", width: 420, name: "ruleBlock", position: "absolute", inputLeft:0, offsetLeft:0, list:[
					    {type: "itemlabel", label: "生成规则：", labelWidth: 100, labelAlign:"right"},
						{type:"newcolumn"},
						{type: "radio", label: "静态节点", name: "nodeRule", value:"0", labelWidth: 100,position:"label-right", labelAlign:"left"},
						{type: "radio", label: "实时动态节点", name: "nodeRule", value:"2", labelWidth: 100,position:"label-right", labelAlign:"left"},
						{type:"newcolumn"},
						{type: "radio", label: "触发器动态节点", name: "nodeRule", value:"1", labelWidth: 100,position:"label-right", labelAlign:"left"}
	       			]},
					{type: "block", width: 420, name: "sortBlock", position: "absolute", inputLeft:0, offsetLeft:0, list:[
   					    {type: "itemlabel", label: "排序方式：", labelWidth: 100, labelAlign:"right"},
   						{type:"newcolumn"},
   						{type: "radio", label: "升序", name: "sortType", value:"asc", labelWidth: 100,position:"label-right", labelAlign:"left"},
   						{type:"newcolumn"},
   						{type: "radio", label: "降序", name: "sortType", value:"desc", labelWidth: 100,position:"label-right", labelAlign:"left"},
   	       			]},
					{type: "block", width: 420, name: "sourceBlock", position: "absolute", inputLeft:0, offsetLeft:0, list:[
	   					{type: "itemlabel", label: "数据来源：", labelWidth: 100, labelAlign:"right"},
						{type:"newcolumn"},
	   					{type: "radio", label: "业务表", name: "dataSource", value:"0", labelWidth: 100,position:"label-right", labelAlign:"left"},
						{type:"newcolumn"},
	   					{type: "radio", label: "编码表", name: "dataSource", value:"1", labelWidth: 100,position:"label-right", labelAlign:"left"}
	       			]}
       			]}
			]}
        ];
		return formJson;
	}
	
	function showGreatPoperty() {
		var form   = _initObj.form;
		form.setItemLabel("FS_greatProperty", "<div style=\"cursor: pointer;\" onclick=\"hideGreatPoperty()\">隐藏高级配置</div>");
		form.showItem("gpBlock");
	}
	
	function hideGreatPoperty() {
		var form   = _initObj.form;
		form.setItemLabel("FS_greatProperty", "<div style=\"cursor: pointer;\" onclick=\"showGreatPoperty()\">显示高级配置</div>");
		form.hideItem("gpBlock");
	}
	
	// 设置节点类型，那些禁用、那些可用
	function settingNodeType(that, enabledType) {
		var eArr = enabledType.split(","),
		    dArr = ["0","1","2","3","4","5","6","7","9"],
		    i;
		// 禁用
		for (i = 0; i < dArr.length; i++) {
			if (enabledType.indexOf(dArr[i]) < 0) {
				that.form.disableItem("type", dArr[i]);
			}
		}
		// 启用
		for (i = 0; i < eArr.length; i++) {
			that.form.enableItem("type", eArr[i]);
		}
		// 选中第一个启用类型
		if (eArr.length > 0) {
			that.form.checkItem("type", eArr[0]);
			that.changeTypeNode(eArr[0]);
		}
	}
</script>		
	</body>
</html>
