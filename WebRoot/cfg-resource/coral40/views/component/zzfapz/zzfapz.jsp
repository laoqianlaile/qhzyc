<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
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

<div id="max${idSuffix}" class="fill">
    <ces:layout id="layoutId${idSuffix}" name="" style="width:800px;height:700px;" fit="true">
        <ces:layoutRegion region="north" split="true" style="height:220px;" title = "种植方案配置">
        </ces:layoutRegion>
        <ces:layoutRegion region="center" split="true" cls="small-grid">
        </ces:layoutRegion>
    </ces:layout>
</div>
<script type="text/javascript">
    $.extend($.ns("namespaceId${idSuffix}"), {

    });

    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page' : 'zzfapz.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage' : $('#max${idSuffix}'),
            /** 页面初始化的方法 */
            'bodyOnLoad' : function(configInfo) {
                var topChildConfigInfo = CFG_initConfigInfo({
                    /** 页面名称 */
                    'page' : 'zzfapz.jsp',
                    /** 页面中的最大元素 */
                    'maxEleInPage' : $('#max${idSuffix}'),
                    /** 获取构件嵌入的区域 */
                    'getEmbeddedZone' : function() {
                        return $('#layoutId${idSuffix}').layout('panel', 'north');
                    }
                });
                var bottomChildConfigInfo = CFG_initConfigInfo({
                    /** 页面名称 */
                    'page' : 'zzfapz.jsp',
                    /** 页面中的最大元素 */
                    'maxEleInPage' : $('#max${idSuffix}'),
                    /** 获取构件嵌入的区域 */
                    'getEmbeddedZone' : function() {
                        return $('#layoutId${idSuffix}').layout('panel', 'center');
                    }
                });
                var topConstructDetails = CFG_getReserveZoneInfo(configInfo, "Zzfa", '2');
                if (topConstructDetails && topConstructDetails.length > 0) {
                    var topConstructDetail = topConstructDetails[0];
                    CFG_openComponent(topChildConfigInfo, topConstructDetail.bindingComponentUrl, "", false);
                    configInfo.topChildConfigInfo = topChildConfigInfo.childConfigInfo;
                    topChildConfigInfo.parentConfigInfo = configInfo;
                }
                var bottomConstructDetails = CFG_getReserveZoneInfo(configInfo, "Bqy", '2');
                if (bottomConstructDetails && bottomConstructDetails.length > 0) {
                    var bottomConstructDetail = bottomConstructDetails[0];
                    configInfo.embeddedInfo = null;
                    CFG_openComponent(bottomChildConfigInfo, bottomConstructDetail.bindingComponentUrl, "", true);
                    configInfo.bottomChildConfigInfo = bottomChildConfigInfo.childConfigInfo;
                    bottomChildConfigInfo.parentConfigInfo = configInfo;
                }
                //
            }
        });
    });
</script>
