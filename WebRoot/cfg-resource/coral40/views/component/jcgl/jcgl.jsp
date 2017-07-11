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
<div id="zxtDiv">

</div>
<div id="max${idSuffix}" class="fill" style="display:none">
    <ces:layout id="layoutId${idSuffix}" name="" style="width:800px;height:600px;" fit="true">
        <ces:layoutRegion region="center">
            <div class="fill">
                <div class="toolbarsnav clearfix">
                    <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '新增', 'id':'add', 'disabled': 'false','type': 'button'},{'label': '修改', 'id':'update', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delete', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                </div>
                <ces:grid id="gridId${idSuffix}" shrinkToFit="true" forceFit="true" fitStyle="fill" model="grid" url="${gurl}">
                    <ces:gridCols>
                        <ces:gridCol name="name" width="180">名称</ces:gridCol>
                    </ces:gridCols>
                    <ces:gridPager gridId="gridId${idSuffix}"/>
                </ces:grid>
                <div class="toolbarsnav clearfix">
                    <ces:toolbar id="toolbarId1${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick1"
                                 data="[{'label': '保存', 'id':'save', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                </div>
            </div>
        </ces:layoutRegion>
    </ces:layout>
</div>
<script type="text/javascript">

    $(function() {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page' : 'page.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage' : $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone' : function() {
                return $('#layoutId${idSuffix}').layout('panel', 'center');
            },
            /** 初始化预留区 */
            'initReserveZones' : function(configInfo) {
                CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength"));
            },
            /** 获取返回按钮添加的位置 */
            'setReturnButton' : function(configInfo) {
                //CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
                CFG_setCloseButton(configInfo, $('#toolbarId${idSuffix}'));
            },
            /** 获取关闭按钮添加的位置 */
            'setCloseButton' : function(configInfo) {
                CFG_setCloseButton(configInfo, $('#toolbarId1${idSuffix}'));
            },
            /** 页面初始化的方法 */
            'bodyOnLoad' : function(configInfo) {
                //alert(1);
            }
        });
        configInfo.CFG_outputParams = {'success':'otp'};
    });

</script>