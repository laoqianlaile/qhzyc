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
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appdefinebase.js"></script>
  </head>
  <body scroll="no" onload="init()">
    <script type="text/javascript">
        var app = {tableId: "-1", modified: false, classification: "", menuId: "-1", menuType: false};
        //var nodeId = -1;
        function init(){
        	var that = this;
            var dhxLayout = new dhtmlXLayoutObject("content", "2U");
            initLayout(dhxLayout, true);
            var layoutA = dhxLayout.cells("a");
            var layoutB = dhxLayout.cells("b");
            that.aLayout = layoutA;
            that.bLayout = layoutB;
            app.contentLayout = layoutB;
            that.currentTreeStruct = "physical"; // 默认为物理表树
            that.currentConfigType = "module";   // 默认为按构件
            layoutA.setWidth(240);
            layoutA.hideHeader();
            initAppDefineTree(that);
            //initHelpContent(layoutB);
            initMenuContent(that);
        }
            
        function initModuleContent(that) {
        	app.contentLayout = that.bLayout;
        	if (isEmpty(app.tableId) || "-1" == app.tableId) {
        		initHelpContent(that.bLayout);
        	} else {
        		that.bLayout.hideHeader();
                loadAppDefine(that.bLayout);
        	}
        }
            
        function initMenuContent(that) {
        	that.bLayout.hideHeader();
        	that.bLayout.detachToolbar();
        	var subLayout = that.bLayout.attachLayout("2U");
        	var layoutA = subLayout.cells("a");
        	var layoutB = subLayout.cells("b");
        	app.contentLayout = layoutB;
        	layoutA.setText("按菜单配置");
        	layoutA.setWidth(240);
        	initAppMenuTree(that, layoutA);
        	initHelpContent(layoutB);
        }
            
        function initHelpContent(layout) {
            layout.detachToolbar();
            layout.setText("操作说明");
            layout.showHeader();
            layout.attachObject(createTableHelpDiv());
        }
            
        function createTableHelpDiv() {
            var obj = document.getElementById("DIV-help");
            if (null == obj) {
                obj = document.createElement("DIV");
                obj.setAttribute("id", "DIV-help");
                obj.setAttribute("style", "font-family: Tahoma; font-size: 11px;display: none;");
                obj.innerHTML = "<ul> \n"
                    + "<li type=\"square\">"
                    + "<p><b>应用定义操作说明：</b><br></p> \n"
                    + "<p>1. 选择应用定义树中的表节点，右侧出现应用定义主界面<br></p> \n"
                    + "<p>2. 在应用定义主界面中可以进行【检索定义】、【列表定义】、【排序定义】、【列表按钮定义】和【界面定义】<br></p> \n"
                    + "</li> \n"
                    + "</ul> \n";
            }
            return obj;
        }
    </script>        
  </body>
</html>
