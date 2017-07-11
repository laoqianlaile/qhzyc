<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String jdbh = request.getParameter("jdbh");
    String gurl = path + "/appmanage/column-define!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=columnName,showName";
    String turl = path + "/appmanage/tree-define!tree.json?E_frame_name=coral&E_model_name=tree&P_ISPARENT=child&F_in=name,id&P_OPEN=true&P_filterId=parentId&P_CHECKED=838386ae45acc0c60145acc412bb0003";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>业务平台-选择区域</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
    <script type="application/javascript"
            src="<%=path%>/cfg-resource/coral40/views/nsjlcmpzxt/common/js/common.js"></script>
    <%@include file="../../common/jsp/_cui_library.jsp" %>
</head>

<body>
<div>
    <div style="text-align: center;"><span style="font-size: 40px;">业务平台</span></div>
    <div style="width: 80%;height:80%;border:1px solid;margin:0 auto;">
        <div style="text-align: center;border-bottom: 1px solid;">
            <span style="font-size: 18px">＊请选择地块/大棚所在区域＊</span>
        </div>
        <div id="qyxx" style="width: 100%;text-align: center;">
            <div id="qy_0" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="qy_1" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="qy_2" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="qy_3" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="qy_4" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="qy_5" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="qy_6" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="qy_7" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="qy_8" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="qy_9" style="width: 10%;height:10%;border-bottom:1px solid;float: left;"></div>
        </div>
    </div>
    <div style="text-align: center;margin-top: 10px;">
        <button style="width: 100px;" onclick="javascript:history.go(-1)">返回</button>
    </div>
</div>
</body>
<script>
    $(function () {
        $.ajax({
            url: $.contextPath + baseUrl + "/queryAreaInfo.json",
            type: "get",
            data: {"qybm": qybm,"jdbh":"<%=jdbh%>"},
            success: function (data) {
                setQyxx(data);
            }
        })
    })
    function setQyxx(qyData){
        var qy = $("div[id^='qy_']");
        $.each(qyData,function(index){
            $(qy[index]).append("<a href='<%=path%>/cfg-resource/coral40/views/nsjlcmpzxt/dkxzym.jsp?jdbh="+this.JDBH+"&qybh="+this.QYBH+"' style='line-height: 50px;cursor: hand'>"+this.QYMC+"</a>")
        })
    }
</script>
</html>