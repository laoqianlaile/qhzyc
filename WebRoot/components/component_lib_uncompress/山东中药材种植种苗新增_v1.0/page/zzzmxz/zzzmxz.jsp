<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("gurl",path + "/module-category!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=name,id");
    request.setAttribute("turl",path + "/module-category!tree.json?E_frame_name=coral&E_model_name=tree&F_in=name,id&P_filterId=parentId");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="max${idSuffix}" class="fill">
    <div id="ft${idSuffix}" class="toolbarsnav clearfix top0" style="margin-top: 20px;margin-right: 10px">
        <ces:toolbar id="toolbarId${idSuffix}"
                     data="['->',{\"type\": \"button\",\"id\": \"saveBtm\",'cls':'save_tb',\"label\": \"保存\",\"name\":\"保存\",\"onClick\":\"submitForm()\"}]">
        </ces:toolbar>
        <div class='homeSpan' style="margin-top: -23px;margin-left:10px;"><div><div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> -企业信息 </div></div></div>
    </div>
    <form id="enterForm${idSuffix}" action="${basePath}zxtqyda!saveQyda" style="margin-top: -10px"
          enctype="multipart/form-data" method="post" class="coralui-form">

        <div class="fillwidth colspan3 clearfix">

            <div class="app-inputdiv4">
                <label class="app-input-label" style="width:36%;">种子种苗编号：</label>
                <input id="qymc${idSuffix}" name="qymc" data-options="required:true,readonly:true"maxlength="20"  class="coralui-textbox"
                       disabled="true"/>
            </div>
            <div class="app-inputdiv4">
                <label class="app-input-label" style="width:36%;">种子种苗名称：</label>
                <input id="gszcdjzh${idSuffix}" name="gszcdjzh" data-options="required:true"maxlength="10"
                       class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv4">

            </div>
            <div class="fillwidth colspan3 clearfix">

                <div class="app-inputdiv12">
                    <label class="app-input-label" style="width:36%;">介绍：</label>
                    <input id="lxdh${idSuffix}" name="lxdh" maxlength="20"   data-options="required:true, pattern:'//^((0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?)|((13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8})$//',errMsg:'请输入手机号码或者固话'" class="coralui-textbox"/>
                </div>
            </div>


        </div>
</div>
<script type="text/javascript">
    $.extend($.ns("namespaceId${idSuffix}"), {


    });
    $(function() {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page' : 'zzzmxz.jsp',
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
                CFG_addTabbar(configInfo, "TabReserve", $('#tabbarDiv${idSuffix}'), '4', $.ns("namespaceId${idSuffix}"));
            }
        });
        var systemParam1 = CFG_getSystemParamValue(configInfo, 'systemParam1'); // 获取构件系统参数
        var selfParam1 = CFG_getSelfParamValue(configInfo, 'selfParam1'); // 获取构件自身参数
        var inputParam1 = CFG_getInputParamValue(configInfo, 'inputParamName_1'); // 获取构件输入参数
        configInfo.CFG_outputParams.xxx = ''; // 设置输出参数
    });
</script>
