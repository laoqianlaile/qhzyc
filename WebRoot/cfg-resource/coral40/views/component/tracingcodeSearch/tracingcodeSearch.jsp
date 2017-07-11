<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
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
<!DOCTYPE html>
<html>
<head>
    <%--<@tag type="single" name="食品追溯云平台追溯码查询"></@tag>--%>
<meta charset="utf-8">
<meta http-equiv="imagetoolbar" content="no" />
<meta http-equiv="pragma" content="no-cach" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="format-detection" content="telephone=no" />
<meta name="HandheldFriendly" content="true" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="shortcut icon" href="<%=resourceFolder%>/css/images/trace-image/favicon.ico" type="image/x-icon" />
<title>食品追溯云平台</title>
</head>
<body>
<div id="max${idSuffix}"> <%--class="wrapper"--%>
    <div><%--class="wrapperbox"--%>
        <!--topbox-->
        <%--<div class="topbox">--%>
            <%--<#include "header.html"/>--%>
        <%--</div>--%>
        <!--topbox end-->

        <!--主体结构 包含footer-->
        <div class="main">
            <%--<div class="localtion clearfix">--%>
                <%--<div class="localtion-left">--%>
                    <%--<a href="javascript:history.back(-1)"><i class="icon-backto"></i></a>--%>
                <%--</div>--%>
                <%--<div class="localtion-right">--%>
                    <%--<div class="localtion-right-mid">--%>
                        <%--<h2 class="localtionTitle">食品追溯云平台追溯码查询</h2>--%>
                        <%--<p class="CrumbsP"><i class="icon-c-home"></i><@navigation splitChar='/'></@navigation></p>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>

            <div class="tracingcoderesult">
                <div class="tracingresult">
                    <table class="tracingcodeTable">
                        <tr>
                            <td width="90">
                                <span class="tracingfont">搜索结果</span>
                            </td>
                            <td>
                                <div class="tracingcode-input-box">
                                    <input id="zsmInput" type="text" class="tracingcode-input" placeholder="请输入20位追溯码">
                                    <span class="tracingcode-search-btn" id="traceBtn" onclick="traceBtnSearch()"><i class="icon-web-search"></i></span>
                                    <div class="message-error">
                                        <div class="message-error-angle"></div>
                                        <div class="message-error-text">您输入的追溯码格式不正确</div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

            <div class="tracingcodesearchresultbox" style="display:none">


                <fieldset>
                    <legend>产品基本信息</legend>
                    <legend class="legenddrop down"></legend>
                    <div class="tracingcodecontentbox">
                        <div class="tracodebasicmessage">
                            <div class="foodslinkband clearfix">
                                <ul id="qyzs" style="display: none">
                                    <!--
                                    <li><img src="<@URL src='resource/css/images_common/tb2.jpg'/>"></li>
                                    <li><img src="<@URL src='resource/css/images_common/tb3.jpg'/>"></li>
                                    <li><img src="<@URL src='resource/css/images_common/tb4.jpg'/>"></li>-->
                                    <!--<li><img src="<@URL src='resource/css/images_common/l1.jpg'/>"></li>
                                    <li><img src="<@URL src='resource/css/images_common/l1.jpg'/>"></li>-->
                                </ul>
                            </div>
                            <ul>
                                <li id="pmLi">
                                    <span class="basicname">品名：</span>
									<span class="lifetext">
										<span class="lifetextmid" id="spmc"></span>
										<!--<span class="lifetextmid">冷鲜肉</span>-->
									</span>
                                </li>
                                <li style="display:none" id="pzSpan">
                                    <span class="basicname">品种：</span>
									<span class="lifetext">
										<span class="lifetextmid" id="pz"></span>
										<!--<span class="lifetextmid">养殖的仔畜全称</span>-->
									</span>
                                </li>
                                <!--<li>
                                    <span class="basicname">配料：</span>
                                    <span class="lifetext">
                                        <span class="lifetextmid">饲料</span>
                                    </span>
                                </li>-->
                                <li>
                                    <span class="basicname">产地：</span>
									<span class="lifetext">
										<span id="cd" class="lifetextmid"></span>
										<!--<span class="lifetextmid">上海市松江区</span>-->
									</span>
                                </li>
                                <li>
                                    <span class="basicname">生产企业：</span>
									<span class="lifetext">
										<span id="scqy" class="lifetextmid"></span>
										<!--<span class="lifetextmid">松江肉制品公司</span>-->
									</span>
                                </li>
                                <li>
                                    <span class="basicname">生产基地：</span>
									<span class="lifetext">
										<span id="scjd" class="lifetextmid"></span>
										<!--<span class="lifetextmid">松江养殖厂</span>-->
									</span>
                                </li>
                                <li id="pchList" style="display:none">
                                    <span class="basicname">批次号：</span>
									<span class="lifetext">
										<span id="pch" class="lifetextmid"></span>
										<!--<span class="lifetextmid">松江养殖厂</span>-->
									</span>
                                </li>
                                <li>
                                    <span class="basicname" style="display:none" id="plSpan">配料：</span>
									<span class="lifetext">
										<span class="lifetextmid" id="plDiv">
											<!--<a class="mixturebtn"><span>猪肉</span><span class="mixturesearch"></span></a>
											<a class="mixturebtn"><span>生菜</span><span class="mixturesearch"></span></a>
											<a class="mixturebtn"><span>胡萝卜</span><span class="mixturesearch"></span></a>-->
										</span>
									</span>
                                </li>
                            </ul>
                            <div class="food-Carousel-box clearfix" style="display: none">
                                <!--youce-->
                                <div class="foodcright">
                                    <div class="foodcdrop foodcdropupbox">
                                        <div class="foodcdropup"></div>
                                    </div>
                                    <div class="droppicbox">
                                        <ul class="droppicboxul" id="rightUl">
                                            <!--<li class="current"><img src="<@URL src='resource/css/images_common/news/default.jpg'/>"></li>
                                            <li><img src="<@URL src='resource/css/images_common/news/default.jpg'/>"></li>
                                            <li><img src="<@URL src='resource/css/images_common/news/default.jpg'/>"></li>
                                            <li><img src="<@URL src='resource/css/images_common/news/default.jpg'/>"></li>
                                            <li><img src="<@URL src='resource/css/images_common/news/default.jpg'/>"></li>
                                            <li><img src="<@URL src='resource/css/images_common/news/default.jpg'/>"></li>
                                            <li><img src="<@URL src='resource/css/images_common/news/default.jpg'/>"></li>
                                            <li><img src="<@URL src='resource/css/images_common/news/default.jpg'/>"></li>-->
                                        </ul>
                                    </div>
                                    <div class="foodcdrop foodcdropdownbox">
                                        <div class="foodcdropdown"></div>
                                    </div>
                                </div>
                                <!--youce end-->
                                <!--left-->
                                <div class="foodcleft">
                                    <div class="foodcleftmid">
                                        <div class="foodcarpic">
                                            <ul id="foodcleft">
                                                <li><img id="pmImg" src="<%=resourceFolder%>/css/images/trace-image/default.jpg"></li>
                                                <li><img id="sczImg" src="<%=resourceFolder%>/css/images/trace-image/default2.jpg"></li>
                                                <li><img id="scqyImg" src="<%=resourceFolder%>/css/images/trace-image/default.jpg"></li>
                                                <li><img id="scjdImg" src="<%=resourceFolder%>/css/images/trace-image/default2.jpg"></li>
                                            </ul>
                                        </div>
                                        <div class="foodcarpicul clearfix">
                                            <ul>
                                                <li class="current">品名</li>
                                                <li>生产者</li>
                                                <li>生产企业</li>
                                                <li>生产基地</li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                <!--left end-->

                            </div>
                        </div>
                    </div>
                </fieldset>

                <fieldset>
                    <legend>产品历程信息</legend>
                    <legend class="legenddrop down"></legend>
                    <div class="tracingcodecontentbox">
                        <div class="tracodelifeline">
                            <ul id="revTrace">
                            </ul>
                        </div>
                    </div>
                </fieldset>

                <fieldset id="scjgxx">
                    <legend>生产加工信息</legend>
                    <legend class="legenddrop down"></legend>
                    <div class="tracingcodecontentbox">
                        <h2 class="title2" id="title1"></h2>
                        <table class="tracodeTablemesssge" id="table1">
                        </table>
                        <h2 class="title2" id="title2"></h2>
                        <table class="tracodeTablemesssge" id="table2">
                        </table>
                    </div>
                </fieldset>


                <fieldset id="jyjcxx">
                    <legend>检验检测信息</legend>
                    <legend class="legenddrop down"></legend>
                    <div class="tracingcodecontentbox">
                        <table class="tracodeTablemesssge" id="table3">
                        </table>
                    </div>
                </fieldset>

            </div>
            <div class="resultshow" style="display: none">
                <div class="resulttext"><p class="warn">抱歉！</p><p>您输入的追溯码不存在</p></div>
            </div>
        </div>
        <!--主体结构 包含footer end-->
    </div>
    <input type="button" name="sdzyc"/>
    <!--footer share-->
    <%--<div class="footerbox">--%>
        <%--<#include "footer.html" />--%>
    <%--</div>--%>
    <!--footer share end-->


</div>

<!--<script type="text/javascript" src="<@URL src='resource/js/jquery.min.js'/>"></script>-->
<script type="text/javascript" src="<%=resourceFolder%>/js/trace/common.js"></script>
<script type="text/javascript" src="<%=resourceFolder%>/js/trace/respond.min.js"></script>
<!--<script type="text/javascript" src="<@URL src='resource/js/header.js'/>"></script>
<script type="text/javascript" src="<@URL src='resource/js/footer.js'/>"></script>-->
<!--<script type="text/javascript" src="<@URL src='resource/js/login.js'/>"></script>-->
<%--<script type="text/javascript" src="<@URL src='resource/js/traceCommon.js'/>"></script>--%>
<script type="text/javascript" src="<%=resourceFolder%>/js/trace/fxTrace.js"></script>
<script type="text/javascript" src="<%=resourceFolder%>/js/trace/tracingcodeSearch.js"></script>
<script type="text/javascript">

    $.extend($.ns("namespaceId${idSuffix}"), {
        pchClick: function () {
            CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "pch", "追溯树", 2, $.ns("namespaceId${idSuffix}"));
        },
        setPch: function(o){
            var o = {
                status : true,
                value_pch : pch
            }
            return o;
        }
        <%--setComboGridValue_Cdmc: function (o) {--%>
            <%--if (null == o) return;--%>
            <%--var rowData = o.rowData;--%>
            <%--if (null == rowData) return;--%>
            <%--var zzcdbm = $("#zzcdbm${idSuffix}");--%>
            <%--var yzcdbm = $("#yzcdbm${idSuffix}");--%>

            <%--$("#cdmc${idSuffix}").textbox("setValue",rowData.CDMC);--%>
            <%--$("#cdbm${idSuffix}").combogrid("setValue",rowData.CDBM);--%>
<%--//            $("input[name='cdmc']").combogrid("setText",rowData.CDMC);--%>
        <%--}--%>
    });



    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'tracingcodeSearch.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                return $('#max${idSuffix}');
            },
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                //alert("bodyOnLoad");
                // 按钮权限控制
                //alert(configInfo.notAuthorityComponentButtons);
                if (configInfo.notAuthorityComponentButtons) {
                    $.each(configInfo.notAuthorityComponentButtons, function (i, v) {
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
    });

    var pcOrMb;
    try{
        if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
            pcOrMb = "PH";
        }else{
            pcOrMb = "PC";
        }
    }catch(e){}
    //品名图片
    var pmTp ;
    var sczTp;	//生产者图片
    var scqyTp;	//生产企业图片
    var scjdTp;	//生产基地图片
    var pch;
    //追溯码查询按钮事件
//    $('body').on('click','#traceBtn',function(){
//        if($('.tracingcode-input').val() == ''){
//            $('.message-error').show();
//            $('.message-error-text').html('追溯码不能为空');
//        }else{
//            //window.location.href="tracingcodeSearch.html";
//            var zsm = $(".tracingcode-input").val();
//            toSearch(zsm)
//        }
//    })
    function traceBtnSearch(){
        if($('.tracingcode-input').val() == ''){
            $('.message-error').show();
            $('.message-error-text').html('追溯码不能为空');
        }else{
            //window.location.href="tracingcodeSearch.html";
            var zsm = $(".tracingcode-input").val();
            toSearch(zsm)
        }
    }
    function toSearch(zsm) {
        $(".tracingcode-input").innerHTML = zsm;
        $("#revTrace").html("");
        $("#table1").html("");
        $("#table2").html("");
        $("#table3").html("");
        $(".lifetextmid").html("");
        $.ajax({
            type: "get",
            url: zxUrl + "/getRevTraceChain",
            data: {"zsm": zsm},
            async: false,
            success: function (data) {
                if ($.isEmptyObject(data)) {
                    $(".tracingcodesearchresultbox").hide();
                    $('.resultshow').show();
                    return;
                }
                $('.resultshow').hide();
                if(pcOrMb=="PH"){
                    $(".tracingcoderesult").hide();
                    $("#pmLi").before("<li>"+
                            "<span class='basicname'>追溯码：</span>"+
                            "<span class='lifetext'>"+
                            "<span class='lifetextmid' id='zsm'>"+zsm+"</span>"+
                            "</span>"+
                            "</li>");
                }
                $(".tracingcodesearchresultbox").show();
                $("#spmc").html(data.SPMC);
                $("#cd").html(data.CDMC);
                $("#scqy").html(data.QYMC);
                $("#scjd").html(data.JDMC);
                $("#cpzs").html(data.CDZSH);
                var cplc = data.CPLC;	//产品历程
                var xtlx = data.XTLX;	//系统类型
                if (!$.isEmptyObject(data.PZTYM)) {
                    $("#pzSpan").show();
                    $("#pz").html(data.PZTYM);
                } else {
                    $("#pzSpan").hide();
                }
                if ("1" == xtlx || "2" == xtlx) {
                    $(".food-Carousel-box").show();
                    pmTp = data.PMTP;			//品名图片
                    scjdTp = data.SCJDTP;		//生产基地图片
                    scqyTp = data.SCQYTP;		//生产企业图片
                    sczTp = data.SCZTP;			//生产者图片
                    var qyzs = data.QYZS;		//企业证书
                    $("#qyzs").show();
                    $("#qyzs").html("");
                    if(!$.isEmptyObject(qyzs))
                        $.each(qyzs,function(){
                            $("#qyzs").append("<li><img src='<%=path%>/spzstpfj/"+this.TPBCMC+"'></li>");
                        })
                }else{
                    $(".food-Carousel-box").hide();
                }
                initTp(pmTp,sczTp,scqyTp,scjdTp,"1");
                /* $("<li class='current'><img src='"+url+"/companyImages/"+data.QYTP2+"'></li>").prependTo($("#foodcleft"));
                 $("<li class='current'><img src='"+url+"/companyImages/"+data.QYTP2+"'></li>").prependTo($(".droppicboxul"));*/
                if (!$.isEmptyObject(data.YLXX)) {
                    //加载配料信息
                    loadPlxx(data.YLXX);
                    $("#plSpan").show();
                } else {
                    $("#plSpan").hide();
                }
                var title1;
                var table1List;
                var title1List;
                var title1BmList;
                var title2;
                var table2List;
                var title2List;
                var title2BmList;
                //检测检验信息
                titleList = ["检测时间", "检测地点","检测结果", "检测人员"];
                titleBmList = ["JCSJ", "JCDD", "JCJG", "JCRY"];
                setTableList("table3", titleList, titleBmList, data.JCJYXX);
                if (xtlx == "1") {
                    var zzpch = data.ZZPCH
                    pch = data.QYBM+zzpch;
                    createLi(data.ZPRQ, "栽培");
                    createLi(data.CSRQ, "采收");
                    //施肥信息table
                    title1 = "施肥";
                    table1List = data.SFXX;
                    title1List = ["施肥日期", "使用肥料", "负责人"];
                    title1BmList = ["SFSJ", "SYFL", "FZR"];
                    //施药信息table
                    title2="施药";
                    table2List = data.SYXX;
                    title2List = ["施药日期", "使用农药", "负责人"];
                    title2BmList = ["SYSJ", "SYNY", "FZR"];
                } else if (xtlx == "2") {
                    var yzpch = data.YZPCH
                    pch = data.QYBM+yzpch;
                    createLi(data.JLRQ, "进栏");
                    createLi(data.CLRQ, "出栏");
                    //饲料信息table
                    title1="饲料";
                    table1List = data.SLXX;
                    title1List = ["饲喂日期", "使用饲料", "负责人"];
                    title1BmList = ["WSSJ", "SYSL", "FZR"];
                    //用药信息table
                    title2 = "用药";
                    table2List = data.YYXX;
                    title2List = ["用药日期", "使用兽药", "负责人"];
                    title2BmList = ["YYSJ", "SYSY", "FZR"];
                } else if (xtlx == "10") {
                    var lcxx = data.LCXX;
                    var ylxx = lcxx.YLXX;
                    var jgxx = lcxx.JGXX;
                    var bzxx = lcxx.BZXX;
                    var ccxx = lcxx.CCXX;
                    $.each(ylxx, function (index) {
                        createLi(this.JCSJ, "入库");
                    })
                    $.each(jgxx, function (index) {
                        createLi(this.HJKSSJ, "开始“" + this.JGHJMC + "”");
                    })
                    $.each(bzxx, function (index) {
                        createLi(this.BZKSSJ, "开始“" + this.BZGY + "”");
                    })
                    if (!$.isEmptyObject(ccxx.CCSJ))
                        createLi(ccxx.CCSJ, "出场");
                    //加工信息table
                    $("#title1").html("加工");
                    table1List = data.JGXX;
                    title1List = ["添加剂使用时间", "添加剂", "负责人"];
                    title1BmList = ["HJKSSJ", "SYTJJMC", "FZR"];
                    //包装信息table
                    $("#title2").html("包装");
                    table2List = data.BZXX;
                    title2List = ["包装材料使用时间", "包装材料", "负责人"];
                    title2BmList = ["BZKSSJ", "BZCLZL", "FZR"];
                }
                if (!$.isEmptyObject(table1List) && !$.isEmptyObject(table2List)) {
                    if (!$.isEmptyObject(table1List)) {
                        $("#title1").html(title1)
                        //加载table1
                        setTableList("table1", title1List, title1BmList, table1List);
                    }
                    if (!$.isEmptyObject(table2List)) {
                        $("#title2").html(title2)
                        //加载table2
                        setTableList("table2", title2List, title2BmList, table2List);
                    }
                }else{
                    $("#scjgxx").hide();
                }
				alert(pch);
                if(xtlx!="10"){
                    if(!$.isEmptyObject(pch)){
                        $("#pchList").show();
//                    var a = $("<a href='trace.html?pch="+pch+"'>"+pch+"</a>");
                        var a = $("<a onclick = '$.ns(\"namespaceId${idSuffix}\").pchClick()'>"+pch+"</a>");
                        $("#pch").append(a);
                    }
                } else {
                    $("#pchList").hide();
                }

                if (xtlx != "10") {
                    //产品历程信息
                    getCplc(cplc);
                }
            },
            error:function(){
                $(".tracingcodesearchresultbox").hide();
                $('.resultshow').show();
            }
        });
    }
</script>
</body>
</html>