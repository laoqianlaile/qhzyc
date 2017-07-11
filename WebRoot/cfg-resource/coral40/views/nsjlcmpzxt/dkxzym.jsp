<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String jdbh = request.getParameter("jdbh");
    String qybh = request.getParameter("qybh");
    String gurl = path + "/appmanage/column-define!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=columnName,showName";
    String turl = path + "/appmanage/tree-define!tree.json?E_frame_name=coral&E_model_name=tree&P_ISPARENT=child&F_in=name,id&P_OPEN=true&P_filterId=parentId&P_CHECKED=838386ae45acc0c60145acc412bb0003";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>地块选择</title>

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

<body style="overflow: auto;">
<div>
    <div style="text-align: center;"><span style="font-size: 40px;">业务平台</span></div>
    <div style="width: 80%;height:80%;border:1px solid;margin:0 auto;">
        <div style="text-align: center;border-bottom: 1px solid;">
            <span style="font-size: 18px">＊请选择地块/大棚＊</span>
        </div>
        <div id="dkxx" style="width: 100%;text-align: center;">
            <input type="text" hidden="hidden" id="dkbh" value=""/>
            <div id="dk_0" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="dk_1" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="dk_2" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="dk_3" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="dk_4" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="dk_5" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="dk_6" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="dk_7" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="dk_8" style="width: 10%;height:10%;border-right:1px solid;border-bottom:1px solid;float: left;"></div>
            <div id="dk_9" style="width: 10%;height:10%;border-bottom:1px solid;float: left;"></div>
        </div>
    </div>
    <div style="text-align: center;margin-top: 10px;">
        <button style="width: 100px;" onclick="javascript:history.go(-1)">返回</button>
        <button id="nextStep" style="width: 100px;">下一步</button>
    </div>
    <div id="dyDiv" style="display:none;border:1px solid; width:200px;">
        <ul id="dyList">

        </ul>
    </div>
</div>
</body>
<script>
    $(function () {
        $.ajax({
            url: $.contextPath + baseUrl + "/queryPlotInfo.json",
            type: "get",
            data: {"qybm": qybm,"jdbh":"<%=jdbh%>","qybh":"<%=qybh%>"},
            success: function (data) {
                setQyxx(data);
            }
        })
    })

    function setQyxx(qyData){
        var dk = $("div[id^='dk_']");
        $.each(qyData,function(index){
            $(dk[index]).append("<a onclick='showDyList(\""+this.JDBH+"\",\""+this.QYBH+"\",\""+this.DKBH+"\")' style='line-height: 50px;cursor: hand'>"+this.DKMC+"</a>")
        })
    }
   /**
    * 显示单元预留区
    * @param jdbh
    * @param qybh
    * @param dkbh
    */
    function showDyList(jdbh,qybh,dkbh){
       $("#dkbh").val(dkbh);
        var dyList;
        $.ajax({
            url:$.contextPath + baseUrl + "/queryUnitInfo.json",
            type:"get",
            data:{"qybm":qybm,"jdbh":jdbh,"qybh":qybh,"dkbh":dkbh},
            async:false,
            success:function(data){
                dyList = data;
            }
        });
        if (dyList.length > 0) {
            $("#dyDiv").show();
            $("#dyList").html("");
            $.each(dyList, function (index) {
                $("#dyList").append("<li><input type='checkbox' name='dy' value='" + this.ZZDYBH + "'/>" + this.ZZDYMC + "</li>")
            })
        }else{
            $("#dyDiv").hide();
        }
    }
    $("#nextStep").click(function(){
        var dybh="";
        $.each($("input[name='dy']:checked"),function(index){
            dybh+=$(this).val()+","
        })
        if(!$.isEmptyObject(dybh)){
        window.location.href= $.contextPath+"/cfg-resource/coral40/views/nsjlcmpzxt/czym.jsp?" +
                "qybh=<%=qybh%>&dkbh="+$("#dkbh").val()+"&dybh="+dybh;
        }
        else{
            $.message("请选择地块单元！");
        }

    })
</script>
</html>