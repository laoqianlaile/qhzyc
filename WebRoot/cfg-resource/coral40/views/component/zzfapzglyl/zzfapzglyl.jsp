<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("gurl", path + "/zzrzxx!getRzxx.json");
    request.setAttribute("turl", path + "/zzrzxx!getSjzdmc.json");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<%--<link rel="stylesheet" href="cfg-resource/coral40/views/component/zzfapzglyl/jquery-cui/css/jquery.coral.min.css">--%>
<%--<link rel="stylesheet" href="cfg-resource/coral40/views/component/zzfapzglyl/css/reset.css">--%>
<link rel="stylesheet" href="cfg-resource/coral40/views/component/zzfapzglyl/css/style.css">
<div id="max${idSuffix}" class="fill">
    <div id="ft${idSuffix}" style="height:40px">
        <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                     data="['->',{'label': '返回', 'id':'CFG_closeComponentZone', 'cls':'return_tb','disabled': 'false','type': 'button'},'','','','']">
        </ces:toolbar>
        <div class='homeSpan' style="margin-top: -23px;">
            <div>
                <div style='margin-left:20px;width: 200px;' id="nva${idSuffix}"> - 种植方案配置管理 - 方案预览</div>
            </div>
        </div>
    </div>
    <div>
        <div class="wrapper" style="width:90%">
            <!--时间轴部分-->
            <div class="TL">
                <!--按钮部分，这部分是为了方便测试看效果的-->
                <div class="TL-title clearfix">
                    <%--<div class="text">方案预览</div>--%>
                    <div class="selectBox" style="display: inline-block;float:left;">
                        <span>类别筛选:</span>
                        <input id="kind${idSuffix}" class="coralui-combobox" style="width:100px" data-options="valueField:'value',textField:'text',data: [{'value': 'all', 'text': '全部'}, {'value': 'bz', 'text': '播种'}, {'value': 'gg', 'text': '灌溉'}, {'value': 'sf', 'text': '施肥'}, {'value': 'yy', 'text': '用药'}, {'value': 'cc', 'text': '除草'}, {'value': 'cs', 'text': '采收'}, {'value': 'qt', 'text': '其他'}]">
                    </div>
                    <div class="selectBox" style="margin-left: 5px;float: left;">
                        <span>起始农事项时间:</span>
                        <input id="farming${idSuffix}" name="farming" class="coralui-datepicker">
                    </div>
                </div>
                <!--主体内容部分-->
                <div class="TL-content">
                    <div class="TL-cnt-title">
                        <div class="TL-title-text">种植方案名称：太空小番茄种植方案</div>
                    </div>
                    <div class="TL-cnt-body clearfix">
                        <!--左边的轴-->
                        <div class="TL-body-left">
                        </div>
                        <!--右边的块-->
                        <div class="TL-body-right clearfix">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%--<script src="jquery-cui/js/jquery.coral.min.js"></script>--%>
<%--<script src="js/action.js"></script>--%>
<script type="text/javascript">
    $.extend($.ns("namespaceId${idSuffix}"), {
        toolbarClick: function (event, button) {
            if (button.id == "CFG_closeComponentZone") {
                CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
            }        },
        queryNsx: function ($nsxData) {
            $(".TL-body-right").empty();
            $(".TL-body-left").empty();

            $.each($nsxData,function(e, data){
                $(".TL-title-text").html("种植方案名称：" + data.ZZFAMC);
                /**************************************加载左边图标 begin****************/
                var kind = "";
                var image = "";
                if(data.LX == "bz"){
                    kind = "播种";
                    image = "TL-icon1";
                }else if(data.LX == "sf"){
                    kind = "施肥";
                    image = "TL-icon2";
                }else if(data.LX == "gg"){
                    kind = "灌溉";
                    image = "TL-icon3";
                }else if(data.LX == "yy"){
                    kind = "用药";
                    image = "TL-icon5";
                }else if(data.LX == "cc"){
                    kind = "除草";
                    image = "TL-icon4";
                }else if(data.LX == "cs"){
                    kind = "采收";
                    image = "TL-icon7";
                }else if(data.LX == "qt"){
                    kind = "其他";
                    image = "TL-icon6";
                }
                var is_end = "";
                if(data.IS_END == "1"){
                    is_end = "<div class=\"TL-arrow\" style=\"display: none;\"> <div class=\"TL-arrow-top\"></div><div class=\"TL-arrow-middle\"></div><div class=\"TL-arrow-bottom\"></div></div>";
                }else{
                    is_end = "<div class=\"TL-arrow\"> <div class=\"TL-arrow-top\"></div><div class=\"TL-arrow-middle\"></div><div class=\"TL-arrow-bottom\"></div></div>";
                }
                var html = "<div class=\"TL-operation clearfix\"><span class=\"TL-operation-name\">" + kind + "</span><span class=\"" + image + "\"></span>" + is_end + "</div>";
                $(".TL-body-left").append(html);
                /**************************************加载左边图标 end******************************************/
                /**************************************加载右边内容 begin******************************************/
                var date = "";
                if($("#farming${idSuffix}").datepicker("getValue") == ""){
                    date = "第" + parseInt(data.NSXJGSJ + 1) + "天";
                }else{
                    date = data.NSXJGSJ;
                }
                html = "<div class=\"TL-right-item clearfix\"><div class=\"TL-triangle\"></div><div class=\"TL-item-info\"><div class=\"TL-info-input\"><label for=\"TL-operater\">农事项名称</label><spanstyle=\"display:inline-block; width:7px;\"></span>: <span name=\"farmingItem\">" + data.NSZYXMC +"</span><br><label for=\"datepicker\">操作时间</label><spanstyle=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + date + "</span> </div> <div class=\"TL-item-comment\"> </div> </div> </div>";
                $(".TL-body-right").append(html);
                /**************************************加载右边内容 end******************************************/
            });
        }
    });


    $(function () {

        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zzfapzglyl.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            <%--'getEmbeddedZone': function () {--%>
            <%--return $('#layoutId${idSuffix}').layout('panel', 'center');--%>
            <%--},--%>
            <%--/** 初始化预留区 */--%>
            <%--'initReserveZones': function (configInfo) {--%>
            <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength") - 1);--%>
            <%--},--%>
            <%--/** 获取返回按钮添加的位置 */--%>
            <%--'setReturnButton': function (configInfo) {--%>
            <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            <%--},--%>
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                var rowDataId = CFG_getInputParamValue(configInfo, 'rowDadaId'); // 获取构件输入参数
                var $nsxData = $.loadJson($.contextPath + "/zzfalb!preView.json?id=" + rowDataId + "&kind=" + $("#kind${idSuffix}").combobox("getValue") + "&datepicker=" + $("#farming${idSuffix}").datepicker("getValue"));
                $.ns("namespaceId${idSuffix}").queryNsx($nsxData);
                $("#kind${idSuffix}").combobox("option", "onChange", function(e,data){
                    var $nsxData = $.loadJson($.contextPath + "/zzfalb!preView.json?id=" + rowDataId + "&kind=" + $("#kind${idSuffix}").combobox("getValue") + "&datepicker=" + $("#farming${idSuffix}").datepicker("getValue"));
                    $.ns("namespaceId${idSuffix}").queryNsx($nsxData);
                });
                $("#farming${idSuffix}").datepicker("option", "onChange", function(e,data){
                    var $nsxData = $.loadJson($.contextPath + "/zzfalb!preView.json?id=" + rowDataId + "&kind=" + $("#kind${idSuffix}").combobox("getValue") + "&datepicker=" + $("#farming${idSuffix}").datepicker("getValue"));
                    $.ns("namespaceId${idSuffix}").queryNsx($nsxData);
                });

            }

        });
    });

</script>
