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
		<title>系统配置平台1.0-CELL报表定义</title>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/reportbase.js"></script>

	</head>
	<body scroll="no" onload="init()">
		<script type="text/javascript">
			var nodeId = -1;
			var type = 0;
			function init() {
				var dhxLayout = new dhtmlXLayoutObject("content", "2U");
				initLayout(dhxLayout, true);
				
				var layoutA = dhxLayout.cells("a");
				var layoutB = dhxLayout.cells("b");
				
				layoutA.setWidth(240);
				layoutA.setText("报表定义树");
				
				layoutB.hideHeader();
				initCellReportTree(layoutA, layoutB);
			    initLayoutBHelp(layoutB);
			    var toolBar = layoutB.attachToolbar();
				initSelfToolBar(toolBar);
			    
			    //initLayoutBContent(layoutB);
			    
			}
			
			function initLayoutBContent(layoutB) {
				var _this = this;
				this.dsLoaded = this.relationLoaded = this.defineLoaded = this.bindedLoaded = false;
				layoutB.hideHeader();
				layoutB.hideToolbar();
				var tabbar = layoutB.attachTabbar();
			    tabbar.setImagePath(IMAGE_PATH);

			    tabbar.addTab("tab$report$01", "第一步：数据源字段设置", "160px");
			    tabbar.addTab("tab$report$02", "第二步：数据源表关系设置", "180px");
			    tabbar.addTab("tab$report$03", "第三步：报表制作", "130px");
			    //tabbar.addTab("tab$report$04", "第四步：报表绑定", "130px");
			    
			    tabbar.setTabActive("tab$report$01");
			    loadCellReportDS(tabbar, _this);//*/
			    
			    /*tabbar.setTabActive("tab$report$03");
			    loadCellReportDefine(tabbar, _this);//*/
			    
				tabbar.attachEvent("onSelect", function(id,last_id){
			   		if (!_this.dsLoaded && id == "tab$report$01") {
			   			loadCellReportDS(tabbar, _this);//数据源字段设置
				    } else if (!_this.relationLoaded && id == "tab$report$02") {
				    	loadCellReportTR(tabbar, _this);//表关系设置
				    } else if (!_this.defineLoaded && id == "tab$report$03") {
				    	loadCellReportDefine(tabbar, _this);
					} else if (!_this.bindedLoaded && id == "tab$report$04") {
						loadCellReportBinding(tabbar, _this);
					}
		   			return true;
			   	});
			}
			
			function initLayoutBHelp(layoutB) {
				layoutB.showHeader();
				layoutB.setText("操作说明");
				layoutB.showToolbar();
				layoutB.attachObject(createTableHelpDiv());
			}
			
			/**
			 * 初始化列表工具条
			 */
			function initSelfToolBar(toolBar) {
			    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
			    toolBar.addButton("download", 0, "下载报表插件", "download.gif");
			    toolBar.attachEvent('onClick', function(id) {
			        if (id == "download") {
			        	download(AppActionURI.report + "!downloadReportPlugin.json");
			        }
			    });
			}
			
			function createTableHelpDiv() {
				var obj = document.getElementById("DIV-help");
				if (null == obj) {
					obj = document.createElement("DIV");
					obj.setAttribute("id", "DIV-help");
					obj.setAttribute("style", "font-family: Tahoma; font-size: 11px;display: none;overflow-y:auto; overflow-x:auto;height:"+(document.body.clientHeight-30)+"px;");
					obj.innerHTML = "<ul> \n"
						+ "<li type=\"square\">"
						+ "<p><b>报表定义操作说明：</b><br></p> \n"
						+ "<p>1. 在根节点下<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;1.1 右键菜单可以新增分类或报表节点<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;1.2 不可以修改或删除根节点<br></p> \n"
						+ "<p>2. 在分类节点下<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.1 右键菜单可以新增分类或报表节点<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.2 右键菜单可以修改或删除此分类节点<br></p> \n"
						+ "<p>3. 在报表节点下<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;3.1 右键菜单可以修改或删除此报表节点<br></p> \n"
						+ "<p>4. 制作报表需要安装报表插件<br></p> \n"
						+ "</li> \n"
						+ "</ul> \n"
						+ "<ul> \n"
						+ "<li type=\"square\">"
						+ "<p><b>报表定义树操作步骤：</b><br></p> \n"
						+ "<p>1. 【新增】选择一个节点，右键->弹出右键菜单->选择“新增分类”或“新增表”，则在该节点下新增一个分类或报表<br></p> \n"
						+ "<p>2. 【修改】选择一个节点，右键->弹出右键菜单->选择（如果选中节点是分类，则是修改分类；<br></p> \n"
						+ "<p>如果选中节点是表，则修改报表）“修改分类”或“修改报表”<br></p> \n"
						+ "<p>3. 【删除】选择一个节点，右键->弹出右键菜单->选择（如果选中节点是分类，则是删除分类；<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;如果选中节点是表，则删除报表）“删除分类”或“删除报表”<br></p> \n"
						+ "<p>4. 点击分类节点时，右侧页面是操作说明<br></p> \n"
						+ "<p>5. 点击报表节点时，右侧页面是报表定义操作页面<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;5.1  数据源字段设置<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;5.2  数据源表关系设置<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;5.3  报表制作<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;5.4  报表绑定<br></p> \n"
						+ "</li> \n"
						+ "</ul> \n";
				}
				
				return obj;
			}
		</script>		
	</body>
</html>
