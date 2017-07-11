<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String gurl = path + "/appmanage/column-define!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=columnName,showName";
    String turl = path + "/appmanage/tree-define!tree.json?E_frame_name=coral&E_model_name=tree&P_ISPARENT=child&F_in=name,id&P_OPEN=true&P_filterId=parentId&P_CHECKED=838386ae45acc0c60145acc412bb0003";
    Date date = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String yearDate = df.format(date);
    df = new SimpleDateFormat("hh:mm:ss");
    String time = df.format(date);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>农事记录触摸品子系统</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
    <%@include file="../../common/jsp/_cui_library.jsp" %>
    <script type="application/javascript"
            src="<%=path%>/cfg-resource/coral40/views/nsjlcmpzxt/common/js/common.js"></script>

</head>

<body>
    <div style="text-align: center;width:100%;height: 20%;">
        <span style="font-size: 40px;">食用农产品生产作业信息管理系统</span>
    </div>
    <div id="content" style="margin-top: 5%;height: 80%;">
        <div id="leftContent" style="width: 50%;height:80%;border:1px solid;">

        </div>
        <div id="rightContent" style="width: 50%;height: 80%;">
            <div id="time" style="height: 10%;">
                <div style="width: 48%;vertical-align: middle;display: inline-block;text-align: center;font-size: 20px;">
                    <%=yearDate%>
                </div>
                <div style="width: 48%;vertical-align: middle;display: inline-block;text-align: center;font-size: 20px;">
                    <%=time%>
                </div>
            </div>
            <div style="text-align: center;">
                <a style="font-size: 40px;cursor:hand;" onclick="turnTo()">进入业务平台</a>
            </div>
        </div>
        <div id="dialog${idSuffix}"></div>
    </div>
</body>
<script>
    function turnTo(){
        //TODO 读卡获取IC卡卡号
        var icNum = "";
        if($.isEmptyObject(icNum)){
            $('#dialog${idSuffix}').dialog({
                width:'70%',
                height:400,
                modal:true,
                title:'确认操作',
                autoOpen:true,
                reLoadOnOpen:true,
                url:'<%=path%>/cfg-resource/coral40/views/nsjlcmpzxt/skym.jsp'
            });
        }else{
            window.location.href = "<%=path%>/cfg-resource/coral40/views/nsjlcmpzxt/czym.jsp?icNum="+icNum;
        }
    }
</script>
</html>
