<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="com.ces.config.utils.CommonUtil" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String gurl = path + "/appmanage/column-define!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=columnName,showName";
    String turl = path + "/appmanage/tree-define!tree.json?E_frame_name=coral&E_model_name=tree&P_ISPARENT=child&F_in=name,id&P_OPEN=true&P_filterId=parentId&P_CHECKED=838386ae45acc0c60145acc412bb0003";
    String qybh = request.getParameter("qybh");
    String dkbh = request.getParameter("dkbh");
    String dybh = request.getParameter("dybh");
    String flag = request.getParameter("flag");
    String ids = request.getParameter("ids");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>确认操作页面</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->

</head>

<body>
<div>
    <div style="font-size: 18px;color: red;text-align: center">请刷卡确认操作</div>
    <div>
        <div id="toolbar${idSuffix}">
        </div>
        <div id="gridDemo${idSuffix}">
            <div class="gridDemo${idSuffix}"></div>
        </div>
    </div>
</div>
</body>
<script>
    $(function () {
        $('#toolbar${idSuffix}').toolbar({
           onClick:toolbarClick,
            data:[{'label': '读卡', 'id':'readCard', 'readCard': 'false','type': 'button'}]
        });
        var $grid = $("#gridDemo${idSuffix}"),
                _colModel = [
                    {name: "ID", sortable: true, width: 100, hidden: true},
                    {name: "KSSJ", sortable: true, width: 300, fixed: true},
                    {name: "JSSJ", sortable: true, width: 300, fixed: true},
                    {name: "CZR", sortable: true, width: 300, fixed: true}
                ],
                _colNames = ["id", "开始时间", "结束时间", "操作人"],
                _setting = {
                    width: "auto",
                    height: "100%",
                    colModel: _colModel,
                    colNames: _colNames,
                    url: $.contextPath + baseUrl + "/queryCzjlPage.json?pId=" + pId
                };
        $grid.grid(_setting);
        $("#gridDemo${idSuffix}").grid("reload");
    })
    /**
     *工具条点击事件
     * @param e
     * @param ui
     */
   function toolbarClick(e, ui) {
        if(ui.id=="readCard"){//判断点击读卡
            //TODO 读取卡的信息
            var czr = "张五";
            var czrbh = "003";
            var flag = "<%=flag%>";
            if(flag!="batch") {
                commitData(czr, pId, czrbh);
            }else{
                var ids = "<%=ids%>";
                $.each(ids.split(","),function(){
                    commitData(czr,this,czrbh);
                })
            }
        }
    }
    function commitData(czr,pId,czrbh){
        $.ajax({
            type:"post",
            url:$.contextPath + baseUrl + "/saveOrUpdateCzjl",
            dataType:"json",
            contentType:"application/x-www-form-urlencoded",
            data:{"czr":czr,"pId":pId,"czrbh":czrbh},
            async:false,
            success:function(data) {
                if (data.result == "success") {
                    $.message("操作成功！");
                } else {
                    $.message("读卡失败！");
                }
            },
            error:function(){
                $.message("读卡失败！");
            }
        })
        $("#gridDemo${idSuffix}").grid("reload");
    }
</script>
</html>