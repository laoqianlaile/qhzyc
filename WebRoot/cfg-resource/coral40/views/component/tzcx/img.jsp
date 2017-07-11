<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div id="max${idSuffix}" class="fill" style="text-align: center;height: auto">

</div>
<script type="text/javascript">
    $(function() {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page' : 'module.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage' : $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone' : function() {
                return $('#layoutId${idSuffix}').layout('panel', 'center');
            },
            /** 初始化预留区 */
            'initReserveZones' : function(configInfo) {
                CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength")-1);
            },
            /** 获取返回按钮添加的位置 */
            'setReturnButton' : function(configInfo) {
                CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
            },
            /** 页面初始化的方法 */
            'bodyOnLoad' : function(configInfo) {
                //alert("bodyOnLoad");
                // 按钮权限控制
                //alert(configInfo.notAuthorityComponentButtons);
                if (configInfo.notAuthorityComponentButtons) {
                    $.each(configInfo.notAuthorityComponentButtons, function(i, v){
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
            }
        });
        //var tableId = CFG_getSystemParamValue(configInfo, 'dataId'); // 获取构件系统参数
        var tableId = CFG_getInputParamValue(configInfo, 'tableId'); // 获取构件输入参数
        var dataId = CFG_getInputParamValue(configInfo, 'dataId'); // 获取构件输入参数
        var tzid = dataId.split("_")[0];
        var imagesJson = $.loadJson($.contextPath + "/sctzcx!getImages.json?tzid="+tzid);
        $.each(imagesJson,function(e,data){
            $("#max${idSuffix}").append("<img src='aquatic/"+data.TPLJ+"' style='margin:20px'>");
            if((e+1)%2 == 0){
                $("#max${idSuffix}").append("<br>");
            }
        });
    });

</script>