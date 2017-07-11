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
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>播种操作页面</title>

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
            <div id="toolbar${idSuffix}">
            </div>
            <div id="gridDemo${idSuffix}">
                <div class="gridDemo${idSuffix}"></div>
            </div>
        </div>
        <div id="dialog${idSuffix}"></div>
    </div>
</div>
</body>
<script>
    var qybh = "<%=qybh%>";
    var dkbh = "<%=dkbh%>";
    var dybh = "<%=dybh%>";
    var pId;
    $(function () {
        $('#toolbar${idSuffix}').toolbar({
            onClick:toolbarClick1,
            data:[{'label': '批量确认操作', 'id':"batchVerify", 'readCard': 'false','type': 'button'}]
        });
        var $grid = $("#gridDemo${idSuffix}"),
                _colModel = [
                    {name: "ID", sortable: true, width: 100, hidden: true},
                    {name: "ZZDYMC", sortable: true, width: 300, fixed: true},
                    {name: "PZ", sortable: true, width: 200, fixed: true},
                    {name: "CKPCH", sortable: true, width: 300, fixed: true},
                    {
                        name: "options",
                        width: 200,
                        formatter:"toolbar",
                        align:'center',
                        formatoptions: {
                            onClick: gridClick,
                            data: [{'label': '确认操作', 'id': 'verifyCz', 'disabled': 'false', 'type': 'button'},
                                   {'label': '评价', 'id': 'evaluate', 'disabled': 'false', 'type': 'button'},
                                   {'label': '再做一次', 'id': 'again', 'disabled': 'false', 'type': 'button'}]
                        }
                    }
                ],
                _colNames = ["id", "单元名称","种子名称", "出库批次号","操作"],
                _setting = {
                    width: "auto",
                    height: "400",
                    colModel: _colModel,
                    colNames: _colNames,
                    rowNum:10,
                    url: $.contextPath + baseUrl + "/queryBzPage?qybm=" + qybm + "&qybh=<%=qybh%>&dkbh=<%=dkbh%>&dybh=<%=dybh%>"
                };;
        $grid.grid(_setting);
    })
    function toolbarClick1(e,ui){
        var $grid = $("#gridDemo${idSuffix}");
        var gridIds = $grid.grid("getDataIDs");
        var idList;
        $.ajax({
            url:$.contextPath + baseUrl + "/queryBzIds?qybm=" + qybm + "&qybh=<%=qybh%>&dkbh=<%=dkbh%>&dybh=<%=dybh%>",
            type:"post",
            async:false,
            success:function(data){
                idList = data.ids;
            }
        })
        var ids="";
        $.each(idList,function(index){
            ids+=this.ID;
            if(index<idList.length){
                ids+=",";
            }
        })
        var rowData = $grid.grid("getRowData", e.data.rowId);
        if(ui.id=="batchVerify"){//判断点击读卡
            $('#dialog${idSuffix}').dialog({
                width:'70%',
                height:400,
                modal:true,
                title:'确认操作',
                autoOpen:true,
                reLoadOnOpen:true,
                url:'<%=path%>/cfg-resource/coral40/views/nsjlcmpzxt/verifyCz.jsp?flag=batch&ids='+ids
            });
        }
    }
    /**
     *列表点击事件
     * @param e
     * @param ui
     */
    gridClick = function (e, ui) {
        var $grid = $("#" + e.data.gridId);
        var rowData = $grid.grid("getRowData", e.data.rowId);
        pId=rowData.ID;
        if (ui.id == "verifyCz") {//确认操作
            $('#dialog${idSuffix}').dialog({
                width:'70%',
                height:400,
                modal:true,
                title:'确认操作',
                autoOpen:true,
                reLoadOnOpen:true,
                url:'<%=path%>/cfg-resource/coral40/views/nsjlcmpzxt/verifyCz.jsp'
            });
        }
        if(ui.id=="again"){//再做一次
            $.ajax({
                type:"post",
                url:$.contextPath + baseUrl + "/doAgainBz",
                dataType:"json",
                contentType:"application/x-www-form-urlencoded",
                data:{"id":pId},
                success:function(data) {
                    if (data.result == "success") {
                        $.message("操作成功！");
                    } else {
                        $.message("操作失败！");
                    }
                },
                error:function(){
                    $.message("ajax调用失败！");
                }
            })
            $("#gridDemo${idSuffix}").grid("reload");
        }
        if(ui.id=="evaluate"){
            $('#dialog${idSuffix}').dialog({
                width:'70%',
                height:400,
                modal:true,
                title:'确认操作',
                autoOpen:true,
                reLoadOnOpen:true,
                url:'<%=path%>/cfg-resource/coral40/views/nsjlcmpzxt/czpj.jsp'
            });
        }
    }
</script>
</html>