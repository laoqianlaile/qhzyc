<%@page import="com.ces.config.utils.StringUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
	String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String folderPath = request.getRequestURI();
    folderPath = folderPath.substring(0, folderPath.lastIndexOf("/"));
    String moduleId = request.getParameter("P_moduleId");
    String tableId = request.getParameter("P_tableId");
    String id = request.getParameter("P_id");
    String moduleName = request.getParameter("P_moduleName");
    String tableName = request.getParameter("P_tableName");
    if (StringUtil.isNotEmpty(tableName)) {
        tableName = tableName.toUpperCase();
    }
    String showToolbar = request.getParameter("P_showToolbar");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title></title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">    
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <script type="text/javascript">
        var page="MT_page_";
    </script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/AppActionURI.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/common/js/CFG_component.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appcommon.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appsearch.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appcolumn.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appsort.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_COMMON.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_BASE_SEARCH.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_BASE_TBAR.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_BASE_GRID.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_BASE_FORM.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_BASE_USER.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/show/js/MT_TBAR.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/show/js/MT_GRID.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/show/js/MT_FORM.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/show/js/MT_USER.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/show/js/SECONDARY_DEVELOPMENT.js"></script>
  </head>
  <body onload="init()">
    <script type="text/javascript">
        function init() {
            var _this = this;
            this.moduleId = "<%=moduleId %>"; // 调用该页面的构件ID
            this.masterId = "<%=id %>";       // 主表ID的值(从其他构件传入过来)
            this.masterTableId = "<%=tableId %>"; // 调用该页面的构件的表ID(从其他构件传入过来)
            this.moduleName = "<%=moduleName %>"; // 本页面所在的调用的模块名称
            this.tableName = "<%=tableName %>"; // 本页面所在的调用的模块名称
            this.showToolbar = "<%=showToolbar %>"; // 本页面是否显示工具条
            if (isNotEmpty(this.moduleName)) {
                this.moduleId = loadJson(contextPath + "/appmanage/module!getModuleIdByName.json?P_moduleName=" + encodeURIComponent(this.moduleName));
            }
            if (isNotEmpty(this.tableName)) {
                this.tableId = loadJson(contextPath + "/appmanage/table-define!getIdByName.json?realTableName=" + encodeURIComponent(this.tableName));
            } else {
                this.tableId = this.masterTableId;
            }
            var curLayout = new dhtmlXLayoutObject("content", "1C");
            initLayout(curLayout, true);
            var mObj = MT_GetModule(_this.moduleId);
            var aLayout = curLayout.cells("a");
            aLayout.hideHeader();
            if (null == mObj) {
                aLayout.setText("模块不存在");
                aLayout.showHeader();
                return;
            }
            document.title = mObj.name;
            this.componentVersionId = mObj.componentVersionId;
            this.actionName = mObj.componentClassName;
            MT_SubLayoutInit(_this, aLayout, this.tableId, MT_common.L_GRID, 1);
            //if (!eval(this.showToolbar)) {
            //    aLayout.detachToolbar();
            //}
        }
    </script>
  </body>
</html>
