<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cui" uri="http://www.springframework.org/tags/form" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("gurl", path + "/zzrzxx!getRzxx.json");
    request.setAttribute("turl", path + "/zzrzxx!getSjzdmc.json");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<link rel="stylesheet" href="cfg-resource/coral40/views/component/zzscdayl/css/style.css">
<div id="max${idSuffix}" style="overflow-y:scroll;height: 100%;">
    <div id="ft${idSuffix}"  style="height:40px">
        <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                     data="['->',{'label': '返回', 'id':'CFG_closeComponentZone', 'cls':'return_tb','disabled': 'false','type': 'button'},'','','','']">
        </ces:toolbar>
        <div class='homeSpan' style="margin-top: -23px;">
            <div>
                <div style='margin-left:20px;width: 200px;' id="nva${idSuffix}"> - 内部追溯</div>
            </div>
        </div>
    </div>
    <div>
        <div class="wrapper">
            <!--时间轴部分-->
            <div class="TL">
                <!--按钮部分，这部分是为了方便测试看效果的-->
                <div class="TL-title clearfix">

                </div>
                <!--主体内容部分-->
                <div class="TL-content">
                    <div class="TL-cnt-title">
                        <div class="TL-title-text"></div>
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
        queryNsx: function (scxxData) {
            $(".TL-body-right").empty();
            $(".TL-body-left").empty();
            var jcxx = scxxData.jcxx;
            var jyxx = scxxData.jyxx;
            var scxx = scxxData.scxx;

            //渲染饮片生产数据
            var  kindTextColor = "color:#2ac79f;"
            span1Color = "border-right: 7px solid #2ac79f;";
            span2Color = "background-color: #2ac79f;"
            span3Color = "background-color: #2ac79f;"
            lineTop = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/green-top.png) no-repeat 67px center;";
            lineMiddle = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/greenBar.png) repeat-y 67px center;";
            lineBotton = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/green-bottom.png) no-repeat 67px center;";
            if(isNotEmpty(jcxx)&&jcxx.JHPCH){
                $(".TL-title-text").html("进场编号：" + jcxx.JHPCH+" 饮片名称："+jcxx.SPMC);
                kind = "医院进场";
                image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-plant.png) no-repeat center bottom;";
                var is_end = is_end = "<div class=\"TL-arrow\"> <div class=\"TL-arrow-top\" style=\"" + lineTop + "\"></div><div class=\"TL-arrow-middle\" style=\"" + lineMiddle + "\"></div><div class=\"TL-arrow-bottom\" style=\"" + lineBotton + "\"></div></div>";
                var html = "<div class=\"TL-operation clearfix\"><span class=\"TL-operation-name TL-operation-name1\" style=\"" + kindTextColor + " margin-left:-40px\">" + kind + "</span><span class=\"TL-icon1 TL-icon1-complete\" style=\"" + image + "\"></span>" + is_end + "</div>";
                $(".TL-body-left").append(html);
                html = "<div class=\"TL-right-item clearfix\"><div class=\"TL-triangle\" style=\"" + span1Color + "\"></div><div class=\"TL-item-info\"><div class=\"TL-info-input\" style=\"" + span2Color + "\"><label for=\"TL-operater\">饮片名称</label><spanstyle=\"display:inline-block; width:7px;\"></span>: <span name=\"farmingItem\">" + jcxx.SPMC + "</span> <label for=\"datepicker\">进场编号</label><spanstyle=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + jcxx.JHPCH + "</span><br><label for=\"datepicker\">进场日期</label><span style=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + jcxx.JCRQ + "</span> </div> <div align='center' class=\"TL-item-comment\" style=\"" + span3Color + " background-color: #898989;color: white;font-size: 16px;\"><span id=\"datepicker\" name=\"datepicker\">" + jcxx.QYMC+"</span></div> </div> </div>";
                $(".TL-body-right").append(html);
            }
            if(isNotEmpty(jyxx)&&jyxx.QYXSDDH){
                if(isNotEmpty(jcxx)){}else {
                $(".TL-title-text").html("销售订单号：" + jyxx.QYXSDDH+" 饮片名称："+jyxx.YPMC);}
                kind = "饮片交易";
                image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-plant.png) no-repeat center bottom;";
                var is_end = is_end = "<div class=\"TL-arrow\"> <div class=\"TL-arrow-top\" style=\"" + lineTop + "\"></div><div class=\"TL-arrow-middle\" style=\"" + lineMiddle + "\"></div><div class=\"TL-arrow-bottom\" style=\"" + lineBotton + "\"></div></div>";
                var html = "<div class=\"TL-operation clearfix\"><span class=\"TL-operation-name TL-operation-name1\" style=\"" + kindTextColor + " margin-left:-40px\">" + kind + "</span><span class=\"TL-icon1 TL-icon1-complete\" style=\"" + image + "\"></span>" + is_end + "</div>";
                $(".TL-body-left").append(html);
                html = "<div class=\"TL-right-item clearfix\"><div class=\"TL-triangle\" style=\"" + span1Color + "\"></div><div class=\"TL-item-info\"><div class=\"TL-info-input\" style=\"" + span2Color + "\"><label for=\"TL-operater\">饮片名称</label><spanstyle=\"display:inline-block; width:7px;\"></span>: <span name=\"farmingItem\">" + jyxx.YPMC + "</span> <label for=\"datepicker\">销售订单号</label><spanstyle=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + jyxx.QYXSDDH + "</span><br><label for=\"datepicker\">交易日期</label><span style=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + jyxx.JYSJ + "</span> </div> <div align='center' class=\"TL-item-comment\" style=\"" + span3Color + " background-color: #898989;color: white;font-size: 16px;\"><span id=\"datepicker\" name=\"datepicker\">" + jyxx.QYMC+"</span></div> </div> </div>";
                $(".TL-body-right").append(html);
            }
            if(isNotEmpty(scxx)){
                if(isNotEmpty(jcxx)){}else {
                    $(".TL-title-text").html("生产批次号：" + scxx.QYSCPCH + " 饮片名称:" + scxx.YPMC);
                }
                kind = "饮片生产";
                image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-plant.png) no-repeat center bottom;";
                var is_end = is_end = "<div class=\"TL-arrow\"> <div class=\"TL-arrow-top\" style=\"" + lineTop + "\"></div><div class=\"TL-arrow-middle\" style=\"" + lineMiddle + "\"></div><div class=\"TL-arrow-bottom\" style=\"" + lineBotton + "\"></div></div>";
                var html = "<div class=\"TL-operation clearfix\"><span class=\"TL-operation-name TL-operation-name1\" style=\"" + kindTextColor + " margin-left:-40px\">" + kind + "</span><span class=\"TL-icon1 TL-icon1-complete\" style=\"" + image + "\"></span>" + is_end + "</div>";
                $(".TL-body-left").append(html);
                html = "<div class=\"TL-right-item clearfix\"><div class=\"TL-triangle\" style=\"" + span1Color + "\"></div><div class=\"TL-item-info\"><div class=\"TL-info-input\" style=\"" + span2Color + "\"><label for=\"TL-operater\">饮片名称</label><spanstyle=\"display:inline-block; width:7px;\"></span>: <span name=\"farmingItem\">" + scxx.YPMC + "</span> <label for=\"datepicker\">生产批次号</label><spanstyle=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + scxx.QYSCPCH + "</span><br><label for=\"datepicker\">生产时间</label><span style=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + scxx.SCRQ + "</span> </div> <div align='center' class=\"TL-item-comment\" style=\"" + span3Color + " background-color: #898989;color: white;font-size: 16px;\"><span id=\"datepicker\" name=\"datepicker\">" + scxx.QYMC+"</span></div> </div> </div>";
                $(".TL-body-right").append(html);
                var ylxx = scxxData.ylxx;
                //生产混批数据处理 ylxx
                var kind;
                $.each(ylxx , function( e, data){
                    kind = "药材采购";
                    image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-plant.png) no-repeat center bottom;";
                    var is_end = "<div class=\"TL-arrow\"> <div class=\"TL-arrow-top\" style=\"" + lineTop + "\"></div><div class=\"TL-arrow-middle\" style=\"" + lineMiddle + "\"></div><div class=\"TL-arrow-bottom\" style=\"" + lineBotton + "\"></div></div>";
                    var html = "<div class=\"TL-operation clearfix\"><span class=\"TL-operation-name TL-operation-name1\" style=\"" + kindTextColor + " margin-left:-40px\">" + kind + "</span><span class=\"TL-icon1 TL-icon1-complete\" style=\"" + image + "\"></span>" + is_end + "</div>";
                    $(".TL-body-left").append(html);
                    html = "<div class=\"TL-right-item clearfix\"><div class=\"TL-triangle\" style=\"" + span1Color + "\"></div><div class=\"TL-item-info\"><div class=\"TL-info-input\" style=\"" + span2Color + "\"><label for=\"TL-operater\">原料名称</label><spanstyle=\"display:inline-block; width:7px;\"></span>: <span name=\"farmingItem\">" + data.YCMC + "</span> <label for=\"datepicker\">原料批次号</label><spanstyle=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.QYPCH + "</span><br><label for=\"datepicker\">入库时间</label><span style=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.RKSJ + "</span></div> <div align='center' class=\"TL-item-comment\" style=\"" + span3Color + " background-color: #898989;color: white;font-size: 16px;\"><span id=\"datepicker\" name=\"datepicker\">" + scxx.QYMC+"</span></div> </div> </div>";
                    $(".TL-body-right").append(html);
                })
                var cjg = scxxData.cjg;

                if(isNotEmpty(cjg)){

                    $.each(cjg , function( e, data){
                        if(isEmpty(data.QYPCH)){
                            return;
                        }
                        kind = "药材交易";
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-plant.png) no-repeat center bottom;";
                        var is_end = "<div class=\"TL-arrow\"> <div class=\"TL-arrow-top\" style=\"" + lineTop + "\"></div><div class=\"TL-arrow-middle\" style=\"" + lineMiddle + "\"></div><div class=\"TL-arrow-bottom\" style=\"" + lineBotton + "\"></div></div>";
                        var html = "<div class=\"TL-operation clearfix\"><span class=\"TL-operation-name TL-operation-name1\" style=\"" + kindTextColor + " margin-left:-40px\">" + kind + "</span><span class=\"TL-icon1 TL-icon1-complete\" style=\"" + image + "\"></span>" + is_end + "</div>";
                        $(".TL-body-left").append(html);
                        html = "<div class=\"TL-right-item clearfix\"><div class=\"TL-triangle\" style=\"" + span1Color + "\"></div><div class=\"TL-item-info\"><div class=\"TL-info-input\" style=\"" + span2Color + "\"><label for=\"TL-operater\">药材名称</label><spanstyle=\"display:inline-block; width:7px;\"></span>: <span name=\"farmingItem\">" + data.YCMC + "</span> <label for=\"datepicker\">交易批次号</label><spanstyle=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.QYPCH + "</span><br><label for=\"datepicker\">交易时间</label><span style=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.JYSJ+"</span></div> <div align='center' class=\"TL-item-comment\" style=\"" + span3Color + " background-color: #898989;color: white;font-size: 16px;\"><span id=\"datepicker\" name=\"datepicker\">" + data.QYMC+"</span></div> </div> </div>";
                        $(".TL-body-right").append(html);

                        kind = "原料采购";
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-plant.png) no-repeat center bottom;";
                        var is_end = "<div class=\"TL-arrow\"> <div class=\"TL-arrow-top\" style=\"" + lineTop + "\"></div><div class=\"TL-arrow-middle\" style=\"" + lineMiddle + "\"></div><div class=\"TL-arrow-bottom\" style=\"" + lineBotton + "\"></div></div>";
                        var html = "<div class=\"TL-operation clearfix\"><span class=\"TL-operation-name TL-operation-name1\" style=\"" + kindTextColor + " margin-left:-40px\">" + kind + "</span><span class=\"TL-icon1 TL-icon1-complete\" style=\"" + image + "\"></span>" + is_end + "</div>";
                        $(".TL-body-left").append(html);
                        html = "<div class=\"TL-right-item clearfix\"><div class=\"TL-triangle\" style=\"" + span1Color + "\"></div><div class=\"TL-item-info\"><div class=\"TL-info-input\" style=\"" + span2Color + "\"><label for=\"TL-operater\">药材名称</label><spanstyle=\"display:inline-block; width:7px;\"></span>: <span name=\"farmingItem\">" + data.YCMC + "</span> <label for=\"datepicker\">采购批次号</label><spanstyle=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.QYPCH + "</span><br><label for=\"datepicker\">入库时间</label><span style=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.RKSJ+"</span></div> <div align='center' class=\"TL-item-comment\" style=\"" + span3Color + " background-color: #898989;color: white;font-size: 16px;\"><span id=\"datepicker\" name=\"datepicker\">" + data.QYMC+"</span></div> </div> </div>";
                        $(".TL-body-right").append(html);
                    })
                }
                var zz = scxxData.zz;
                if(isNotEmpty(zz)){
                    $.each(zz , function( e, data){
                        if(isEmpty(data.QYCSPCH)){
                            return;
                        }
                        kind = "药材采收";
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-plant.png) no-repeat center bottom;";
                        var is_end = "<div class=\"TL-arrow\"> <div class=\"TL-arrow-top\" style=\"" + lineTop + "\"></div><div class=\"TL-arrow-middle\" style=\"" + lineMiddle + "\"></div><div class=\"TL-arrow-bottom\" style=\"" + lineBotton + "\"></div></div>";
                        var html = "<div class=\"TL-operation clearfix\"><span class=\"TL-operation-name TL-operation-name1\" style=\"" + kindTextColor + " margin-left:-40px\">" + kind + "</span><span class=\"TL-icon1 TL-icon1-complete\" style=\"" + image + "\"></span>" + is_end + "</div>";
                        $(".TL-body-left").append(html);
                        html = "<div class=\"TL-right-item clearfix\"><div class=\"TL-triangle\" style=\"" + span1Color + "\"></div><div class=\"TL-item-info\"><div class=\"TL-info-input\" style=\"" + span2Color + "\"><label for=\"TL-operater\">药材名称</label><spanstyle=\"display:inline-block; width:7px;\"></span>: <span name=\"farmingItem\">" + data.YCMC + "</span> <label for=\"datepicker\">采收批次号</label><spanstyle=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.QYCSPCH + "</span><br><label for=\"datepicker\">采收时间</label><span style=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.JSSJ+"</span><br></div> <div align='center' class=\"TL-item-comment\" style=\"" + span3Color + " background-color: #898989;color: white;font-size: 16px;\"><span id=\"datepicker\" name=\"datepicker\">" + data.JDMC+"</span></div> </div> </div>";
                        $(".TL-body-right").append(html);

                        kind = "药材种植";
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-plant.png) no-repeat center bottom;";
                        var is_end = "<div class=\"TL-arrow\"> <div class=\"TL-arrow-top\" style=\"" + lineTop + "\"></div><div class=\"TL-arrow-middle\" style=\"" + lineMiddle + "\"></div><div class=\"TL-arrow-bottom\" style=\"" + lineBotton + "\"></div></div>";
                        var html = "<div class=\"TL-operation clearfix\"><span class=\"TL-operation-name TL-operation-name1\" style=\"" + kindTextColor + " margin-left:-40px\">" + kind + "</span><span class=\"TL-icon1 TL-icon1-complete\" style=\"" + image + "\"></span>" + is_end + "</div>";
                        $(".TL-body-left").append(html);
                        html = "<div class=\"TL-right-item clearfix\"><div class=\"TL-triangle\" style=\"" + span1Color + "\"></div><div class=\"TL-item-info\"><div class=\"TL-info-input\" style=\"" + span2Color + "\"><label for=\"TL-operater\">药材名称</label><spanstyle=\"display:inline-block; width:7px;\"></span>: <span name=\"farmingItem\">" + data.YCMC + "</span> <label for=\"datepicker\">种植批次号</label><spanstyle=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.QYPCH + "</span><br><label for=\"datepicker\">种植时间</label><span style=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.ZZSJ+"</span><br></div> <div align='center' class=\"TL-item-comment\" style=\"" + span3Color + " background-color: #898989;color: white;font-size: 16px;\"><span id=\"datepicker\" name=\"datepicker\">" + data.JDMC+"</span></div> </div> </div>";
                        $(".TL-body-right").append(html);

                    })
                }
            }else{
                var cjg = scxxData.cjg;

                if(isNotEmpty(cjg)){
                    $.each(cjg , function( e, data){
                        $(".TL-title-text").html(data.YCMC+" ( 生产批次号：" + data.QYJGPCH+" )");
                        kind = "原料采购";
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-plant.png) no-repeat center bottom;";
                        var is_end = "<div class=\"TL-arrow\"> <div class=\"TL-arrow-top\" style=\"" + lineTop + "\"></div><div class=\"TL-arrow-middle\" style=\"" + lineMiddle + "\"></div><div class=\"TL-arrow-bottom\" style=\"" + lineBotton + "\"></div></div>";
                        var html = "<div class=\"TL-operation clearfix\"><span class=\"TL-operation-name TL-operation-name1\" style=\"" + kindTextColor + " margin-left:-40px\">" + kind + "</span><span class=\"TL-icon1 TL-icon1-complete\" style=\"" + image + "\"></span>" + is_end + "</div>";
                        $(".TL-body-left").append(html);
                        html = "<div class=\"TL-right-item clearfix\"><div class=\"TL-triangle\" style=\"" + span1Color + "\"></div><div class=\"TL-item-info\"><div class=\"TL-info-input\" style=\"" + span2Color + "\"><label for=\"TL-operater\">药材名称</label><spanstyle=\"display:inline-block; width:7px;\"></span>: <span name=\"farmingItem\">" + data.YCMC + "</span> <label for=\"datepicker\">采购批次号</label><spanstyle=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.QYPCH + "</span><br><label for=\"datepicker\">入库时间</label><span style=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.RKSJ+"</span></div> <div align='center' class=\"TL-item-comment\" style=\"" + span3Color + " background-color: #898989;color: white;font-size: 16px;\"><span id=\"datepicker\" name=\"datepicker\">" + data.QYMC+"</span></div> </div> </div>";
                        $(".TL-body-right").append(html);
                    })
                }
                var zz = scxxData.zz;
                if(isNotEmpty(zz)){
                    $.each(zz , function( e, data){
                        if(isEmpty(data.QYCSPCH)){
                            return;
                        }
                        kind = "药材采收";
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-plant.png) no-repeat center bottom;";
                        var is_end = "<div class=\"TL-arrow\"> <div class=\"TL-arrow-top\" style=\"" + lineTop + "\"></div><div class=\"TL-arrow-middle\" style=\"" + lineMiddle + "\"></div><div class=\"TL-arrow-bottom\" style=\"" + lineBotton + "\"></div></div>";
                        var html = "<div class=\"TL-operation clearfix\"><span class=\"TL-operation-name TL-operation-name1\" style=\"" + kindTextColor + " margin-left:-40px\">" + kind + "</span><span class=\"TL-icon1 TL-icon1-complete\" style=\"" + image + "\"></span>" + is_end + "</div>";
                        $(".TL-body-left").append(html);
                        html = "<div class=\"TL-right-item clearfix\"><div class=\"TL-triangle\" style=\"" + span1Color + "\"></div><div class=\"TL-item-info\"><div class=\"TL-info-input\" style=\"" + span2Color + "\"><label for=\"TL-operater\">药材名称</label><spanstyle=\"display:inline-block; width:7px;\"></span>: <span name=\"farmingItem\">" + data.YCMC + "</span> <label for=\"datepicker\">采收批次号</label><spanstyle=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.QYCSPCH + "</span><br><label for=\"datepicker\">采收时间</label><span style=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.JSSJ + "</span><br></div> <div align='center' class=\"TL-item-comment\" style=\"" + span3Color + " background-color: #898989;color: white;font-size: 16px;\"><span id=\"datepicker\" name=\"datepicker\">" + data.JDMC + "</span></div> </div> </div>";
                        $(".TL-body-right").append(html);

                        kind = "药材种植";
                        image = "background: url(cfg-resource/coral40/views/component/zzscdayl/images/TL-plant.png) no-repeat center bottom;";
                        var is_end = "<div class=\"TL-arrow\"> <div class=\"TL-arrow-top\" style=\"" + lineTop + "\"></div><div class=\"TL-arrow-middle\" style=\"" + lineMiddle + "\"></div><div class=\"TL-arrow-bottom\" style=\"" + lineBotton + "\"></div></div>";
                        var html = "<div class=\"TL-operation clearfix\"><span class=\"TL-operation-name TL-operation-name1\" style=\"" + kindTextColor + " margin-left:-40px\">" + kind + "</span><span class=\"TL-icon1 TL-icon1-complete\" style=\"" + image + "\"></span>" + is_end + "</div>";
                        $(".TL-body-left").append(html);
                        html = "<div class=\"TL-right-item clearfix\"><div class=\"TL-triangle\" style=\"" + span1Color + "\"></div><div class=\"TL-item-info\"><div class=\"TL-info-input\" style=\"" + span2Color + "\"><label for=\"TL-operater\">药材名称</label><spanstyle=\"display:inline-block; width:7px;\"></span>: <span name=\"farmingItem\">" + data.YCMC + "</span> <label for=\"datepicker\">种植批次号</label><spanstyle=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.QYPCH + "</span><br><label for=\"datepicker\">种植时间</label><span style=\"display:inline-block; width:5px;\"></span>: <span id=\"datepicker\" name=\"datepicker\">" + data.ZZSJ + "</span><br></div> <div align='center' class=\"TL-item-comment\" style=\"" + span3Color + " background-color: #898989;color: white;font-size: 16px;\"><span id=\"datepicker\" name=\"datepicker\">" + data.JDMC + "</span></div> </div> </div>";
                        $(".TL-body-right").append(html);
                    })
                }
            }


        },
    });


    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'jjgscpczs.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),

            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                var rowDataId = CFG_getInputParamValue(configInfo, 'inParam'); // 获取构件输入参数
                $.ns("namespaceId${idSuffix}").id = rowDataId;
                //根据id获取生产详细信息。进行生产批次及饮片名称加载
                var sczsData = $.loadJson($.contextPath+"/qyjnbzs!searchYpsczsxx.json?id="+rowDataId);
                <%--var $nsxData = $.loadJson($.contextPath + "/sczzscda!preView.json?id=" + rowDataId + "&kind=" + $("#kind${idSuffix}").combobox("getValue"));--%>
                $.ns("namespaceId${idSuffix}").queryNsx(sczsData);
                <%--$("#kind${idSuffix}").combobox("option", "onChange", function (e, data) {--%>
                <%--var $nsxData = $.loadJson($.contextPath + "/sczzscda!preView.json?id=" + rowDataId + "&kind=" + $("#kind${idSuffix}").combobox("getValue"));--%>
                <%--$.ns("namespaceId${idSuffix}").queryNsx($nsxData);--%>
                <%--});--%>

            }

        });


    });



</script>
