<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String path = request.getContextPath();
    String resourceFolder = path + "/cfg-resource/coral40/common";
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
    request.setAttribute("menuId", request.getParameter("menuId"));
    request.setAttribute("basePath", basePath);
    request.setAttribute("path", path);
%>
<!--<@tag type="single" name="食品追溯云平台批次号查询"></@tag>-->
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="imagetoolbar" content="no" />
    <meta http-equiv="pragma" content="no-cach" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta name="format-detection" content="telephone=no" />
    <meta name="HandheldFriendly" content="true" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <link rel="shortcut icon" href="<%=resourceFolder%>/css/images/trace-image/favicon.ico" type="image/x-icon" />
    <title>食品追溯云平台</title>
    <link type="text/css" media="all" rel="stylesheet" href="<%=resourceFolder%>/css/trace/tipsy.css" />
    <style type="text/css">

        .node {
            cursor: pointer;
        }

        .overlay{
            background-color:#EEE;
        }

        .node circle {
            fill: #fff;
            stroke: steelblue;
            stroke-width: 1.5px;
        }

        .node text {
            font-size:8px;
            font-family:sans-serif;
        }

        .link {
            fill: none;
            stroke: #ccc;
            stroke-width: 1.5px;
        }

        .templink {
            fill: none;
            stroke: red;
            stroke-width: 3px;
        }

        .ghostCircle.show{
            display:block;
        }

        .ghostCircle, .activeDrag .ghostCircle{
            display: none;
        }

    </style>

</head>
<body>
<div id="max${idSuffix}">
    <%--<div class="wrapperbox">--%>
        <%--<!--topbox-->--%>
        <%--<div class="topbox"></div>--%>
        <%--<!--topbox end-->--%>
        <%--&lt;%&ndash;<#include "header.html"/>&ndash;%&gt;--%>
        <%--<!--主体结构 包含footer-->--%>
        <%--<div class="main">--%>
            <%--<div class="localtion clearfix">--%>
                <%--<div class="localtion-left">--%>
                    <%--<i class="icon-backto"></i>--%>
                <%--</div>--%>
                <%--<div class="localtion-right">--%>
                    <%--<div class="localtion-right-mid">--%>
                        <%--<h2 class="localtionTitle">食品追溯云平台追溯码查询</h2>--%>
                        <%--<p class="CrumbsP"><i class="icon-c-home"></i><a>首页</a><span class="textadd">/</span><a>批次号查询</a></p>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>

            <div class="toolbarsnav clearfix">
                <ces:toolbar id="toolbarId${idSuffix}" onClick = '$.ns("namespaceId${idSuffix}").toolbarClick'>
                </ces:toolbar>
            </div>
            <div class="tracingcoderesult">
                <div class="tracingresult">
                    <table class="tracingcodeTable">
                        <tr>
                            <td width="90">
                                <span class="tracingfont">搜索结果</span>
                            </td>
                            <td>
                                <div class="tracingcode-input-box">
                                    <input id="pchInput" type="text" class="tracingcode-input" placeholder="请输入批次号">
                                    <span class="tracingcode-search-btn" id="searchBtn" onclick="btnSearch()"><i class="icon-web-search"></i></span>
                                    <div class="message-error">
                                        <div class="message-error-angle"></div>
                                        <div class="message-error-text">您输入的批次号格式不正确</div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="tree-container-div">

                <div id="tree-container"></div>
                <text></text>
            </div>

        <%--</div>--%>
        <!--主体结构 包含footer end-->
    <%--</div>--%>

    <!--footer share-->
    <%--<div class="footerbox">--%>
        <%--<#include "footer.html" />--%>
    <%--</div>--%>
    <!--footer share end-->


</div>

<%--<script type="text/javascript" src="<@URL src='resource/js/jquery.min.js'/>"></script>--%>
<script type="text/javascript" src="<%=resourceFolder%>/js/trace/common.js"></script>
<script type="text/javascript" src="<%=resourceFolder%>/js/trace/fxTrace.js"></script>
<script type="text/javascript" src="<%=resourceFolder%>/js/trace/respond.min.js"></script>
<!--<script type="text/javascript" src="<@URL src='resource/js/header.js'/>"></script>
<script type="text/javascript" src="<@URL src='resource/js/footer.js'/>"></script>-->
<!--<script type="text/javascript" src="<@URL src='resource/js/login.js'/>"></script>-->
<script type="text/javascript" src="<%=resourceFolder%>/js/trace/tracingcodeSearch.js"></script>
<script type="text/javascript" src="<%=resourceFolder%>/js/trace/jquery.tipsy.js"></script>
<script src="<%=resourceFolder%>/js/trace/d3.v3.min.js"></script>
<script src="<%=resourceFolder%>/js/trace/dTree.js"></script>
</body>
<script type="application/javascript">
    $.extend($.ns("namespaceId${idSuffix}"), {
        toolbarClick : function(event, button) {
            if (button.id == "CFG_closeComponentZone") {
                if (window.CFG_clickReturnButton) {
                    CFG_clickReturnButton($('#max${idSuffix}').parent().data('parentConfigInfo'));
                }
            }
        }
    });

    $(function(){
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'tracetree.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                return $('#max${idSuffix}');
            },
            /** 获取返回按钮添加的位置 */
            'setReturnButton' : function(configInfo) {
                <%--var flag = "<%=%>";--%>

                CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
            },
            <%--/** 初始化预留区 */--%>
            <%--'initReserveZones' : function(configInfo) {--%>
                <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength")-1);--%>
            <%--},--%>
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                //alert("bodyOnLoad");
                // 按钮权限控制
                //alert(configInfo.notAuthorityComponentButtons);
               // $.parser.parse("#max${idSuffix}");
                //alert(1);
                if (configInfo.notAuthorityComponentButtons) {
                    $.each(configInfo.notAuthorityComponentButtons, function (i, v) {
                        if (v == 'add') {
                            //$('#toolbarId${idSuffix}').toolbar('disableItem', 'add');
                            $('#toolbarId${idSuffix}').toolbar('hide', 'add');
                        } else if (v == 'update') {
                            //$('#toolbarId${idSuffix}').toolbar('disableItem', 'update');
                            $('#toolbarId${idSuffix}').toolbar('hide', 'update');
                        } else if (v == 'delete') {
                            //$('#toolbarId${idSuffix}').toolbar('disableItem', 'delete');
                            $('#toolbarId${idSuffix}').toolbar('hide', 'delete');
                        }
                    });
                }
                <%--$('#toolbarId${idSuffix}').toolbar('refresh');--%>
            }
        });
//        var pch = GetQueryString("pch");
        var pch = CFG_getInputParamValue(configInfo, 'pch');
        if(!$.isEmptyObject(pch)){
            $("#pchInput").val(pch);
            search(pch)
        }
    })
//    $('body').on('click','#searchBtn',function(){
//        var pch=$("#pchInput").val();
//        search(pch)
//    })
    function btnSearch(){
        var pch=$("#pchInput").val();
        if(pch.length==16){
            search(pch);
        }else{
//            $(".tracingcodesearchresultbox").hide();
            $('.message-error').show();
//            $('.message-error-text').html('批次号无效');
            return;
        }
    }

    function search(pch){
        $("#tree-container").html("");
        var treeUrl = baseUrl+"/services/jaxrs/traceThain/getFwordTraceChain?zzyzpch="+pch;
        renderTree(treeUrl);
    }
</script>
</html>