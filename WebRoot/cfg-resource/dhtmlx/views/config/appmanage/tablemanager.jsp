<%@page import="com.ces.config.utils.TableUtil"%>
<%@page import="com.ces.config.utils.ConstantVar"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>系统配置平台1.0-档案表定义</title>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/tablemanagerbase.js"></script>
		<script type="text/javascript">
			Classification = function(){
				return {
					ARCHIVE: "<%=ConstantVar.TableClassification.ARCHIVE%>",
					DEFINE : "<%=ConstantVar.TableClassification.DEFINE%>",
					PRESET : "<%=ConstantVar.TableClassification.PRESET%>",
					TEMPLATE : "<%=ConstantVar.TableClassification.TEMPLATE%>",
					VIEW     : "<%=ConstantVar.TableClassification.VIEW%>"
				};
			}();
			//默认表前缀
			TablePrefix = function() {
				return {
					ARCHIVE: "<%=TableUtil.getTablePrefix(ConstantVar.TableClassification.ARCHIVE)%>",
					DEFINE : "<%=TableUtil.getTablePrefix(ConstantVar.TableClassification.DEFINE)%>",
					PRESET : "<%=TableUtil.getTablePrefix(ConstantVar.TableClassification.PRESET)%>",
					TEMPLATE : "<%=TableUtil.getTablePrefix(ConstantVar.TableClassification.TEMPLATE)%>",
					VIEW     : "<%=TableUtil.getTablePrefix(ConstantVar.TableClassification.VIEW)%>"
				};
			}();
		</script>
	</head>
	<%

	 %>
	<body scroll="no" onload="init()">
		<script type="text/javascript">
			var nodeId = "-U";
			var type = 0;
			var classification = null;
			function init(){
				var that = this;
				var dhxLayout = new dhtmlXLayoutObject("content", "2U");
				initLayout(dhxLayout, true);
				that.aLayout = dhxLayout.cells("a");
				that.bLayout = dhxLayout.cells("b");				
				that.createTableInited = false;				
			    initTableTree(that);
			    initLayoutBHelp(that);			    
			}
			function initLayoutBContent(that, treeId, groupId, treeName) {
				var layoutB = that.bLayout;
				var _this = this;
				this.columnManager = this.tableRelation = this.columnRelation = false;
				this.isChange = false; this._count =0;
				layoutB.hideHeader();
				layoutB.detachToolbar();
				
				var tabbar = layoutB.attachTabbar();
			    tabbar.setImagePath(IMAGE_PATH);

				if (classification == "LT") {
					tabbar.addTab("tab$table$01", "逻辑表定义", "130px");
				} else {
					tabbar.addTab("tab$table$01", "物理表定义", "130px");
				    if (classification != "V" && classification != "C") {
					    tabbar.addTab("tab$table$02", "表关系定义", "130px");
					    tabbar.addTab("tab$table$03", "字段关联定义", "130px");
				    }
				}
			    //tabbar.addTab("tab$table$04", "应用定义", "100px");
			    tabbar.setTabActive("tab$table$01");
			    /*if(w==0){
			    	loadColumnRelation(tabbar,treeName);
			    }//*/
			
				loadColumn(tabbar,treeId);
				_this.columnManager = true;
				tabbar.attachEvent("onSelect", function(id,last_id){
			   		if (id == "tab$table$01" && !_this.columnManager) {
			   			loadColumn(tabbar);//字段定义
			   			_this.columnManager = true;
				    } else if (id == "tab$table$02" && !_this.tableRelation) {
				    	loadRelation(tabbar,treeName,groupId,_this);//表关系设置
				    	_this.tableRelation = true;
				    } else if (id == "tab$table$03" ) {
				    	if(!_this.columnRelation){
					    	loadColumnRelation(tabbar,treeName); //字段关系定义
				    	}else{
					    	initCombo4F();
				    		if(_this.isChange&&_this._count!=0){
						    	initTab4TabChange();
				    		}
				    	}
				    	++_this._count;
				    	_this.columnRelation = true;
				    	_this.isChange = false;
					}
		   			return true;
			   	});
				// 标记
				that.createTableInited = false;
				
			}
			/** 初始化物理表新增页面*/
			function initPhysicalTableCreate(that, treeType) {
				var subLayout = that.bLayout.attachLayout("1C");
				initLayout(subLayout);
				
				var aLayout = subLayout.cells("a");
				initPhysicalTableGrid(that, aLayout, treeType);
				// 标记已初始化完成
				that.createTableInited = true;
			}	
			/** 初始化逻辑表新增页面*/
			function initLogicTableCreate(that, nodeId) {
				var subLayout = that.bLayout.attachLayout("1C");
				initLayout(subLayout);
				
				var aLayout = subLayout.cells("a");
				initLogicTableGrid(that, aLayout, nodeId);
				// 标记已初始化完成
				that.createTableInited = true;
			}	
			/** 初始化逻辑组表新增页面*/
			function initLogicGroupCreate(that) {
				var subLayout = that.bLayout.attachLayout("1C");
				initLayout(subLayout);
				
				var aLayout = subLayout.cells("a");
				initLogicGroupGrid(that, aLayout);
				// 标记已初始化完成
				that.createTableInited = true;
			}	
			/** 初始化物理组表新增页面*/
			function initPhysicalGroupCreate(that) {
				var subLayout = that.bLayout.attachLayout("1C");
				initLayout(subLayout);
				
				var aLayout = subLayout.cells("a");
				initPhysicalGroupGrid(that, aLayout);
				// 标记已初始化完成
				that.createTableInited = true;
			}
			/** 初始化逻辑组表详细页面*/
			function initLogicGroupDetailCreate(that, nodeId) {
				var subLayout = that.bLayout.attachLayout("1C");
				initLayout(subLayout);
				
				var aLayout = subLayout.cells("a");
				initLogicGroupDetailGrid(that, aLayout, nodeId);
				// 标记已初始化完成
				that.createTableInited = true;
			}	
			/** 初始化物理组表详细页面*/
			function initPhysicalGroupDetailCreate(that, nodeId) {
				var subLayout = that.bLayout.attachLayout("1C");
				initLayout(subLayout);
				
				var aLayout = subLayout.cells("a");
				initPhysicalGroupDetailGrid(that, aLayout, nodeId);
				// 标记已初始化完成
				that.createTableInited = true;
			}	
			/** 初始化逻辑表组中逻辑表关系详细页面*/
			function initLogicTableRelationDetailCreate(that, nodeId, treeName) {
				var layoutB = that.bLayout;
				layoutB.hideHeader();
				layoutB.detachToolbar();
				
				var tabbar = layoutB.attachTabbar();
			    tabbar.setImagePath(IMAGE_PATH);
				tabbar.addTab("tab$table$01", "逻辑表关系定义", "130px");
				tabbar.setTabActive("tab$table$01");
				loadLogicRelation(tabbar, treeName, this);
			}	
			/** 重新加载列表新增页面*/
			function reloadTableCreate(that) {
				that.reloadGrid();
				that.buttonGrid();
				//that.initForm();
				//that.buttonForm();
			}
			/** 帮助页面*/
			function initLayoutBHelp(that) {
				var layoutB = that.bLayout;
				layoutB.showHeader();
				layoutB.setText("操作说明");
				layoutB.attachObject(createTableHelpDiv());
				// 
				that.createTableInited = false;
			}
			
			function createTableHelpDiv() {
				var obj = document.getElementById("DIV-help");
				if (null == obj) {
					obj = document.createElement("DIV");
					obj.setAttribute("id", "DIV-help");
					obj.setAttribute("style", "font-family: Tahoma; font-size: 11px;display: none;overflow-y:auto; overflow-x:auto;height:"+(document.body.clientHeight-30)+"px;");
					obj.innerHTML = "<ul> \n"
						+ "<li type=\"square\">"
						+ "<p><b>表定义树操作说明：</b><br></p> \n"
						+ "<p>1. 根节点下<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;1.1 可以新增分类或表节点<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;1.2 不可以修改或删除根节点<br></p> \n"
						+ "<p>2. 在分类节点下<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.1 可以新增分类或表节点<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.2 可以修改或删除此分类节点<br></p> \n"
						+ "<p>3. 在表节点下，<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;3.1 可以修改或删除此表节点<br></p> \n"
						+ "</li> \n"
						+ "</ul> \n"
						+ "<ul> \n"
						+ "<li type=\"square\">"
						+ "<p><b>表定义树操作步骤：</b><br></p> \n"
						+ "<p>1. 【新增】选择一个节点，右键->弹出右键菜单->选择“新增分类”或“新增表”，则在该节点下新增一个分类或表<br></p> \n"
						+ "<p>2. 【修改】选择一个节点，右键->弹出右键菜单->选择（如果选中节点是分类，则是修改分类；<br></p> "
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;如果选中节点是表，则修改表）“修改分类”或“修改表”<br></p> \n"
						+ "<p>3. 【删除】选择一个节点，右键->弹出右键菜单->选择（如果选中节点是分类，则是删除分类；<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;如果选中节点是表，则删除表）“删除分类”或“删除表”<br></p> \n"
						+ "<p>4. 点击分类节点时，右侧页面是操作说明<br></p> \n"
						+ "<p>5. 点击表节点时，右侧页面是表定义操作页面<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;5.1 可以表定义、表关系定义、字段关联定义、应用定义<br></p> \n"
						+ "</li> \n"
						+ "</ul> \n";
				}
				
				return obj;
			}
		</script>		
	</body>
</html>
