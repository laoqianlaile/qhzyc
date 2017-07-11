<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
	String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String folderPath = request.getRequestURI();
    folderPath = folderPath.substring(0, folderPath.lastIndexOf("/"));
    String moduleId = request.getParameter("P_moduleId");
    String tableId = request.getParameter("P_tableId");
    String id = request.getParameter("P_id");
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
    <script type="text/javascript" src="<%=dhxResPath %>/common/js/CFG_component.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_COMMON.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_FORM_VIEW.js"></script>
    <script type="text/javascript" src="<%=folderPath %>/js/SECONDARY_DEVELOPMENT.js"></script>
    <script type="text/javascript">
        function init() {
            var _this = this;
            this.moduleId = "<%=moduleId %>"; // 页面模块构件ID
            this.masterId = "<%=id %>";       // 主表ID的值(从其他构件传入过来)
            this.masterTableId = "<%=tableId %>"; // 表ID(从其他构件传入过来)
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
            document.title = "";
            this.componentVersionId = "";
            this.actionName = mObj.componentClassName;
            MT_FORM_VIEW_init(_this, aLayout, "<%=tableId %>", "<%=id %>");
        }
    </script>
  </head>
  <body onload="init()">
  </body>
</html>
