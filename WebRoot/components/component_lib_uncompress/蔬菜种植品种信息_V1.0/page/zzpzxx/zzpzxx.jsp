<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String resourceFolder = path + "/cfg-resource/coral40/common";
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    request.setAttribute("turl", path + "/sczzpzxx!getPlxx.json");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>种植品种信息</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
        <link rel="stylesheet" type="text/css" href="styles.css">
        -->
</head>
<div id="max${idSuffix}" class="fill">
    <ces:layout id="layoutId${idSuffix}" name="" style="width:800px;height:600px;" fit="true">
        <ces:layoutRegion region="west" split="true" minWidth="300" maxWidth="400" style="width:150px;padding:10px;overflow:visible;overflow-x:hidden;overflow-y:scroll;overflow-base-color:white">
            <ces:tree id="treeId${idSuffix}" asyncEnable="true" asyncType="post"
                      data="[{ name:\"品种\",id:\"-1\",isParent:true,plym:\"plym\"}]" asyncUrl="${turl}" asyncAutoParam="ID,NAME"
                      onClick="jQuery.ns('namespaceId${idSuffix}').asyncOnclick" onLoad="asyncExpandnode">
            </ces:tree>
        </ces:layoutRegion>
        <ces:layoutRegion region="center">
        </ces:layoutRegion>
    </ces:layout>
</div>
<script type="text/javascript">
    $.extend($.ns("namespaceId${idSuffix}"), {
        currentTreeNodeId: '',
        currentTreeNodeName: '',
        asyncOnclick: function (e, treeId, treeNode) {
            $.ns("namespaceId${idSuffix}").currentTreeNodeId = treeNode.id;
            $.ns("namespaceId${idSuffix}").currentTreeNodeName = treeNode.name;
            if (treeNode.plym == "plym") {
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "plym", "树节点预留区", "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
            if (treeNode.pzjd == "pzjd") {
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "pzjd", "树节点预留区2", "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
            CFG_clearComponentZone($('#max${idSuffix}').data('configInfo'));
            //alert($.ns("namespaceId${idSuffix}").currentTreeNodeId);
            //自定义url 获得指定的列表数据
            //$('#gridId${idSuffix}').grid('option', 'url', "${gurl}?id=" + treeNode.id);
        },
        getTreeNodeId:function(o){
            return {
                status: true,
                plbh: $.ns("namespaceId${idSuffix}").currentTreeNodeId,
                pl:$.ns("namespaceId${idSuffix}").currentTreeNodeName
            };
        }
    })
    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zzpzxx.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                return $('#layoutId${idSuffix}').layout('panel', 'center');
            },
            /** 初始化预留区 */
            <%--'initReserveZones' : function(configInfo) {--%>
            <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'cdmc', $('#toolbarId${idSuffix}').toolbar("getLength")-1);--%>
            <%--},--%>
            /** 获取返回按钮添加的位置 */
            <%--'setReturnButton' : function(configInfo) {--%>
            <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            <%--},--%>
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                //alert("bodyOnLoad");
                // 按钮权限控制
                //alert(configInfo.notAuthorityComponentButtons);
            }
        });
    });
</script>