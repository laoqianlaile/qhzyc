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
    <title>模块定义</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">    
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/AppActionURI.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appcommon.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/moduleTree.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/moduleGrid.js"></script>
    <script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/moduleForm.js"></script>
  </head>
  <body onload="init()">
    <script type="text/javascript">
        function init() {
            var _this = this;
            this.nodeId = "-1";
            this.MODEL_URL = AppActionURI.module;
            this.createModuleGrid = false;
            var curLayout = new dhtmlXLayoutObject("content", "2U");
            initLayout(curLayout, true);
            this.aLayout = curLayout.cells("a");
            this.bLayout = curLayout.cells("b");
            initModuleTree(_this);
        }
    </script>
  </body>
</html>
