<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div id="max${idSuffix}" style="height: 250px;width: 300px">

</div>
<script type="text/javascript">
  var configInfo1;
  var id

    $(function() {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page' : 'zlgjzzxq.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage' : $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone' : function() {
                <%--return $('#layoutId${idSuffix}').layout('panel', 'center');--%>
            },
            /** 初始化预留区 */
            'initReserveZones' : function(configInfo) {
                <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength")-1);--%>
            },
            /** 获取返回按钮添加的位置 */
            'setReturnButton' : function(configInfo) {
                <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>

            },
            /** 页面初始化的方法 */
            'bodyOnLoad' : function(configInfo) {
                id = CFG_getInputParamValue(configInfo, 'InputRowid');
                 configInfo1 = $("#max${idSuffix}").data('configInfo');


                var scdaxxData = $.loadJson($.contextPath + "/zlgjzzxq!getScdaxx.json?id=" + id);
                for(var i=0;i<scdaxxData.length;i++){
                    $('#max${idSuffix}').append("<a onclick='openXq()'>"+scdaxxData[i].CSSJ+'采收的'+scdaxxData[i].PZ+"</a>")
                }


            }
        });
        //var tableId = CFG_getSystemParamValue(configInfo, 'dataId'); // 获取构件系统参数
        <%--var tableId = CFG_getInputParamValue(configInfo, 'tableId'); // 获取构件输入参数--%>
        <%--var dataId = CFG_getInputParamValue(configInfo, 'dataId'); // 获取构件输入参数--%>
        <%--var tzid = dataId.split("_")[0];--%>
        <%--var imagesJson = $.loadJson($.contextPath + "/sctzcx!getImages.json?tzid="+tzid);--%>
        <%--$.each(imagesJson,function(e,data){--%>
        <%--$("#max${idSuffix}").append("<img src='aquatic/"+data.TPLJ+"' style='margin:20px'>");--%>
        <%--if((e+1)%2 == 0){--%>
        <%--$("#max${idSuffix}").append("<br>");--%>
        <%--}--%>
        <%--});--%>
    });
    function openXq(){
        var url1='http://localhost:8092/config1.0/cfg-resource/coral40/views/component/sczzscdaxx/sczzscdaxx.jsp?componentVersionId=402894815065addb0150668d50f7004e&constructDetailId=8a8a4aa050cbbd2e0150cc6c7df20000&assembleType=1&menuId=402881fc4f3ffb80014f4031d280013e&menuCode=ZZSCDA&topComVersionId=402881fc4f3ffb80014f4037464f0141&status=true&paramIn1=408a962b50d086250150d64b8690019b&___t=1448355807376'

        var scdaid=$.loadJson($.contextPath + "/zlgjzzxq!getScdaid.json?id="+id)
        CFG_clickCloseButton(configInfo1);
//      alert("生产档案id:"+scdaid.ID);
        var url1='/cfg-resource/coral40/views/component/sczzscdaxx/sczzscdaxx.jsp?ID='+scdaid.ID+"&LX=GJZZ";
        <%--var configInfo = $('#mtc_${timestamp}').data("configInfo");--%>
        menuClick('生产档案',url1);
    }

</script>