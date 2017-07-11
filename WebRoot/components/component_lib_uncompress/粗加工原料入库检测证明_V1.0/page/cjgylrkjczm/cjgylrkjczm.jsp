<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String gurl = path + "/appmanage/column-define!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=columnName,showName";
    String turl = path + "/appmanage/tree-define!tree.json?E_frame_name=coral&E_model_name=tree&P_ISPARENT=child&F_in=name,id&P_OPEN=true&P_filterId=parentId&P_CHECKED=838386ae45acc0c60145acc412bb0003";
%>
<!doctype html>
<html>
    <head>
        <title>检测证明</title>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <div style="text-align:center;padding-top: 100px">
            <img src="<%=path%>/cfg-resource\coral40\views\component\jdxxspjk\demo.jpg"/>
        </div>
    </body>
</html>