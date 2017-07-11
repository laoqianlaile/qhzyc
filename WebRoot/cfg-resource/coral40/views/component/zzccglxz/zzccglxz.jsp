<%--
  Created by IntelliJ IDEA.
  User: Synge
  Date: 15/8/27
  Time: 上午10:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
	String path = request.getContextPath();
    String id=request.getParameter("id");
    request.setAttribute("ID",id);
    System.out.println(id);
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="max${idSuffix}" class="fill">
    <ces:layout id="layoutId${idSuffix}" name="" style="width:800px;height:700px;" fit="true">
        <ces:layoutRegion region="north" split="true" minHeight="200" maxHeight="400" style="height:237px;">
        </ces:layoutRegion>
        <ces:layoutRegion region="center">
        </ces:layoutRegion>
    </ces:layout>
</div>
<%--<div></div>--%>
<script>
    var id='${ID}';





    $.extend($.ns("#namespaceId${idSuffix}"),{
        getBzid:function(){
            return '${ID}';
        }

    });

    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page' : 'zzccglxz.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage' : $('#max${idSuffix}'),
            /** 页面初始化的方法 */
            'bodyOnLoad' : function(configInfo) {

                var topChildConfigInfo = CFG_initConfigInfo({
                    /** 页面名称 */
                    'page' : 'zzccglxz.jsp',
                    /** 页面中的最大元素 */
                    'maxEleInPage' : $('#max${idSuffix}'),
                    /** 获取构件嵌入的区域 */
                    'getEmbeddedZone' : function() {
                        return $('#layoutId${idSuffix}').layout('panel', 'north');
                    }
                });
                var bottomChildConfigInfo = CFG_initConfigInfo({
                    /** 页面名称 */
                    'page' : 'zzccglxz.jsp',
                    /** 页面中的最大元素 */
                    'maxEleInPage' : $('#max${idSuffix}'),
                    /** 获取构件嵌入的区域 */
                    'getEmbeddedZone' : function() {
                        return $('#layoutId${idSuffix}').layout('panel', 'center');
                    }
                });
                var topConstructDetails = CFG_getReserveZoneInfo(configInfo, "Ccgl", '2');
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

//                    bottomChildConfigInfo.parentConfigInfo.data("BZID",id);
                }
                //var as=document.getElementsByName('a');
                var _fjglid=configInfo.CFG_urlParams.FJGLID;
                if(_fjglid!=undefined){
                    var lis=$('.coral-tabs-top  ul li ')[1].getElementsByTagName('a')[0];
                    lis.click();
                }



                //document.getElementById('coral-id-53').click();
                //debugger
//                alert('加载完毕');
                //
            }
        });
        if (configInfo) {

            configInfo.parentConfigInfo.jqChild = $("#maxDiv${idSuffix}");
            //alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue(configInfo, 'systemParam1')
            //		+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue(configInfo, 'selfParam1')
            //		+ "\n构件入参：\t" + "inputParamName_1=" + CFG_getInputParamValue(configInfo, 'inputParamName_1'));
        }


    });
</script>