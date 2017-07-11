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
<link rel="stylesheet" href="cfg-resource/coral40/views/component/zzscdayl/css/style.css">
<div id="max${idSuffix}" style="overflow:scroll;height: 100%"  >
    <div id="ft${idSuffix}"  style="height:40px">
        <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                     data="['->',{'label': '返回', 'id':'CFG_closeComponentZone', 'cls':'return_tb','disabled': 'false','type': 'button'},'','','','']">
        </ces:toolbar>
        <div class='homeSpan' style="margin-top: -23px;">
            <div>
                <div style='margin-left:20px;width: 200px;' id="nva${idSuffix}"> -生产档案-预览</div>
            </div>
        </div>
    </div>
    <div>
        <div class="wrapper">
            <!--时间轴部分-->
            <div class="TL">
                <!--按钮部分，这部分是为了方便测试看效果的-->
                <div class="TL-title clearfix">
                    <%--<div class="text">方案预览</div>--%>
                    <div class="selectBox" style="display: inline-block;float:left;">
                        <span>类别筛选:</span>
                        <input id="kind${idSuffix}" class="coralui-combobox" style="width:100px"
                               data-options="valueField:'value',textField:'text',data: [{'value': 'all', 'text': '全部'}, {'value': 'bz', 'text': '播种'}, {'value': 'gg', 'text': '灌溉'}, {'value': 'sf', 'text': '施肥'}, {'value': 'yy', 'text': '用药'}, {'value': 'cc', 'text': '锄草'}, {'value': 'cs', 'text': '采收'}, {'value': 'qt', 'text': '其他'}]">
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
        id:null,
        toolbarClick: function (event, button) {
            if (button.id == "CFG_closeComponentZone") {
                CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
            }else if(button.id == "updateBtm"){
                var id=this.id;
                var configInfo = $('#max${idSuffix}').data("configInfo");
                var jq = $('#max${idSuffix}');
                jq.data("parentConfigInfo", configInfo);
                var url= $.contextPath+'/cfg-resource/coral40/views/component/sczzscdaxg/sczzscdaxg.jsp';
                $.ajax({
                    type : "POST",
                    url : url,
                    data : {ID:id
                    },
                    dataType : "html",
                    context : document.body,
                    async : false
                }).done(function(html) {
                    jq.empty();
                    jq.append(html);
                    $.parser.parse(jq);
                    configInfo.childConfigInfo.CFG_bodyOnLoad(configInfo.childConfigInfo);
                });
            }
        },
        queryNsx: function ($nsxData) {
            $(".TL-body-right").empty();
            $(".TL-body-left").empty();

            $.each($nsxData, function (e, data) {
                var bool = true;
                if (data.JSSJ == "" || data.JSSJ == "null" || data.JSSJ == null) {
                    bool = false;
                } else {
                    bool = true;
                }
                $(".TL-title-text").html("生产档案编号：" + data.SCDABH);
                /**************************************加载左边图标 begin****************/
                var kind = "";
                var image = "";
                var lineTop = "";
                var lineMiddle = "";
                var lineBotton = "";
                var kindTextColor = "";
                var span1Color = "";
                var span2Color = "";
                var span3Color = "";
                if (bool) {
                    span1Color = "border-right: 7px solid #898989;";
                    span2Color = "background-color: #898989;"
                    span3Color = "background-color: #898989;"
                    kindTextColor = "color:#898989;"
                    lineTop = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/gray-top.png) no-repeat 67px center;";
                    lineMiddle = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/grayBar.png) repeat-y 67px center;";
                    lineBotton = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/gray-bottom.png) no-repeat 67px center;";
                } else {
                    lineTop = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/green-top.png) no-repeat 67px center;";
                    lineMiddle = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/greenBar.png) repeat-y 67px center;";
                    lineBotton = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/green-bottom.png) no-repeat 67px center;";
                }

                if (data.LX == "bz") {
                    kind = "播种";
                    if (bool) {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-plant.png) no-repeat center bottom;";
                    } else {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-plant.png) no-repeat center top;";
                    }
                } else if (data.LX == "sf") {
                    kind = "施肥";
                    if (bool) {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-fertilize.png) no-repeat center bottom;";
                    } else {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-fertilize.png) no-repeat center top;";
                    }
                } else if (data.LX == "gg") {
                    kind = "灌溉";
                    if (bool) {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/Tl-watering.png) no-repeat center bottom;";
                    } else {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/Tl-watering.png) no-repeat center top;";
                    }
                } else if (data.LX == "yy") {
                    kind = "用药";
                    if (bool) {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-drug.png) no-repeat center bottom;";
                    } else {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-drug.png) no-repeat center top;";
                    }
                } else if (data.LX == "cc") {
                    kind = "锄草";
                    if (bool) {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-weeding.png) no-repeat center bottom;";
                    } else {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-weeding.png) no-repeat center top;";
                    }
                } else if (data.LX == "cs") {
                    kind = "采收";
                    if (bool) {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-recovery.png) no-repeat center bottom;";
                    } else {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-recovery.png) no-repeat center top;";
                    }
                } else if (data.LX == "qt") {
                    kind = "其他";
                    if (bool) {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-detection.png) no-repeat center bottom;";
                    } else {
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-detection.png) no-repeat center top;";
                    }
                }
                var is_end = "";
                if (data.IS_END == "1") {
                    is_end = "<div class=\"TL-arrow\" style=\"display: none;\"> <div class=\"TL-arrow-top\" style=\"" + lineTop + "\"></div><div class=\"TL-arrow-middle\" style=\"" + lineMiddle + "\"></div><div class=\"TL-arrow-bottom\" style=\"" + lineBotton + "\"></div></div>";
                } else {
                    is_end = "<div class=\"TL-arrow\"> <div class=\"TL-arrow-top\" style=\"" + lineTop + "\"></div><div class=\"TL-arrow-middle\" style=\"" + lineMiddle + "\"></div><div class=\"TL-arrow-bottom\" style=\"" + lineBotton + "\"></div></div>";
                }
                var html = "<div class=\"TL-operation clearfix\"><span class=\"TL-operation-name TL-operation-name1\" style=\"" + kindTextColor + "\">" + kind + "</span><span class=\"TL-icon1 TL-icon1-complete\" style=\"" + image + "\"></span>" + is_end + "</div>";
                $(".TL-body-left").append(html);
                /**************************************加载左边图标 end******************************************/
                /**************************************加载右边内容 begin******************************************/
                var operationTime = "";
                if (bool) {
                    operationTime = data.KSSJ + "&nbsp;&nbsp;-&nbsp;&nbsp;" + data.JSSJ;
                } else {
                    operationTime = data.OPERATIONTIME;
                }
                html = "<div class=\"TL-right-item clearfix\"><div class=\"TL-triangle\" style=\"" + span1Color + "\"></div><div class=\"TL-item-info\"><div class=\"TL-info-input\" style=\"" + span2Color + "\"><label for=\"TL-operater\">农事项名称</label><spanstyle=\"display:inline-block; width:7px;\"></span>: <span name=\"farmingItem\">" + data.NSZYXMC + "</span> <label for=\"datepicker\">操作人</label><spanstyle=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.CZR + "</span><br><label for=\"datepicker\">作业时间</label><span style=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + operationTime + "</span> </div> <div class=\"TL-item-comment\" style=\"" + span3Color + "\"> </div> </div> </div>";
                $(".TL-body-right").append(html);
                /**************************************加载右边内容 end******************************************/
            });
        },
    });


    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zzscdayl.jsp',
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
                var rowDataId = CFG_getInputParamValue(configInfo, 'rowDataId'); // 获取构件输入参数
                $.ns("namespaceId${idSuffix}").id = rowDataId;
                var $nsxData = $.loadJson($.contextPath + "/sczzscda!preView.json?id=" + rowDataId + "&kind=" + $("#kind${idSuffix}").combobox("getValue"));
                $.ns("namespaceId${idSuffix}").queryNsx($nsxData);
                $("#kind${idSuffix}").combobox("option", "onChange", function (e, data) {
                    var $nsxData = $.loadJson($.contextPath + "/sczzscda!preView.json?id=" + rowDataId + "&kind=" + $("#kind${idSuffix}").combobox("getValue"));
                    $.ns("namespaceId${idSuffix}").queryNsx($nsxData);
                });

            }

        });
    });



</script>
