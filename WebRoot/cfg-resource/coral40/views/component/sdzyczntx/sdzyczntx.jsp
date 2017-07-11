
<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String resourceFolder = path + "/cfg-resource/coral40/common";
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("gurl", path + "/qyptsjlx!getSjzdByLxbm.json");
    request.setAttribute("turl", path + "/qyptsjlx!getSjzdmc.json");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="max${idSuffix}" class="fill">
    <div id="ft${idSuffix}" class="toolbarsnav clearfix top0">

        <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> - 职能提醒  </div></div></div>
    </div>
    <form id="enterForm${idSuffix}" action="${basePath}"
          enctype="multipart/form-data" method="post" class="coralui-form">
        <div class="fillwidth colspan2 clearfix" align="center">
            <div class="app-inputdiv3">

            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label">时间:</label>
                <input id="jgsj${idSuffix}" name="jgsj" />
            </div>
            <div class="app-inputdiv3">

            </div>

        </div>
        <div class="fillwidth colspan2 clearfix">
            <div class="app-inputdiv12" style="margin-top:20px;" align="center">
                <ces:button componentCls="btn-primary " label="开启" text="false" id="startBtn" ></ces:button>
                <ces:button componentCls="btn-primary" label="停止" text="false"  id="stopBtn" style="margin-left:20px" ></ces:button>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $.extend($.ns("namespaceId${idSuffix}"), {
        toolbarClick : function(event, button) {
            if (button.id == "CFG_closeComponentZone") {
                CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
            }else if(button.id == "save"){

            }
        }
    });





    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'sdzyczntx.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                return $('#layoutId${idSuffix}').layout('panel', 'center');
            },

            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                var tiemss=null;
                $("#startBtn").button({
                    onClick : function(){
                        var times = $("#jgsj${idSuffix}").combobox("getValue");
                        tiemss=parseInt(times*1000);
                        document.cookie='timess='+tiemss+";";
                        $("#startBtn").hide();
                        $("#stopBtn").show();
                        setTimeout("window.parent.timeControl()",tiemss);
                    }
                }).addClass("save_tb");
                $("#stopBtn").button({
                    onClick : function(){
                        document.cookie='timess=0;';
                        $("#startBtn").show();
                        $("#stopBtn").hide();
                    }
                }).addClass("return_tb");
                $("#jgsj${idSuffix}").combobox({
                    data:[{text:'10秒',value:'10'},{text:'1分钟',value:'60'},{text:'15分钟',value:'300',selected:"selected"},{text:'30分钟',value:'1800'},{text:'45分钟',value:'2700'},{text:'60分钟',value:'3600'},{text:'90分钟',value:'5400'},{text:'120分钟',value:'7200'}]
                })
                var timess = parseInt(getCookie("timess"));
                if(timess != 0 && !isEmpty(timess) && !isNaN(timess)){
                    $("#startBtn").hide();
                }else{
                    $("#stopBtn").hide();
                }
            }
        });
    });

</script>