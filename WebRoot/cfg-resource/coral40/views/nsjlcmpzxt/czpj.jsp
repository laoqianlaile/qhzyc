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
        <div id="gridDemo${idSuffix}">
            <div class="gridDemo${idSuffix}"></div>
        </div>
    </div>
</div>
</body>
<script>
    $(function () {
        var $grid = $("#gridDemo${idSuffix}"),
                _colModel = [
                    {name: "ID", sortable: true, width: 100, hidden: true},
                    {name: "KSSJ", sortable: true, width: 250, fixed: true},
                    {name: "JSSJ", sortable: true, width: 250, fixed: true},
                    {name: "CZR", sortable: true, width: 250, fixed: true},
                    {
                        name: "options",
                        width: 100,
                        formatter:"toolbar",
                        align:'center',
                        formatoptions: {
                            onClick: gridClick1,
                            data: [{'label': '评价', 'id': 'evaluate', 'disabled': 'false', 'type': 'button'}]
                        }
                    }
                ],
                _colNames = ["id", "开始时间", "结束时间", "操作人","操作"],
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
     *列表点击事件
     * @param e
     * @param ui
     */
     function gridClick1(e, ui) {
        var $grid = $("#" + e.data.gridId);
        var rowData = $grid.grid("getRowData", e.data.rowId);
        if (ui.id == "evaluate") {//确认操作
            $.ajax({
                type:"post",
                url:$.contextPath + baseUrl + "/updatePj",
                dataType:"json",
                contentType:"application/x-www-form-urlencoded",
                data:{"id":rowData.ID,"pj":3},
                success:function(data) {
                    if (data.result == "success") {
                        $.message("评价成功！");
                    } else {
                        $.message("评价失败！");
                    }
                },
                error:function(){
                    $.message("ajax调用失败！");
                }
            })
        }
    }
</script>
</html>