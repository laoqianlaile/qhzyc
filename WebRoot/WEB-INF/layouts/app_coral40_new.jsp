<%@page import="com.ces.config.utils.StringUtil"%>
<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page import="com.ces.config.utils.SystemParameterUtil" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String menuTabSize = SystemParameterUtil.getInstance().getSystemParamValue("菜单标签页个数");
    if (StringUtil.isEmpty(menuTabSize)) {
        menuTabSize = "-1";
    }
    String systemName = SystemParameterUtil.getInstance().getSystemParamValue("系统名称");
    String systemPicture = SystemParameterUtil.getInstance().getSystemParamValue("系统图标");
    if (StringUtil.isEmpty(systemName)) {
        systemName = "系统配置平台V1.0";
    }
    String sessionId = request.getSession().getId();
    String currentUserId = CommonUtil.getCurrentUserId();
    //设置企业编码的session
    /*request.getSession().setAttribute("_systemId_", request.getParameter("systemId"));
    request.getSession().setAttribute("_companyCode_", request.getParameter("companyCode"));*/
    request.getSession().setAttribute("_multi_", request.getParameter("multi"));
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<% if (StringUtil.isNotEmpty(systemPicture)) { %>
<link rel="shortcut icon" type="image/ico" href="<%=systemPicture%>"></link>
<% } %>
<%--<title><%=systemName%>test1</title>--%>
    <title>企业管理子系统</title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<script type="text/javascript">
    var contextPath = "${ctx}";
    var systemId = '<%=request.getParameter("systemId")%>';
    var tabMaxSize = <%=menuTabSize%>;
    var jSessionId = '<%=sessionId%>';
    var currentUserId = '<%=currentUserId%>';
</script>
<%@include file="/cfg-resource/coral40/common/jsp/_cui_library.jsp" %>
<!-- 配置平台添加的个性化的css和js -->
<link href="<%=cuiFolder %>/common/css/styles.css" rel="stylesheet"/>
<link href="<%=contextPath %>/cfg-resource/coral40/common/themes/base/css/common.css" rel="stylesheet"/>
<link href="<%=contextPath %>/cfg-resource/coral40/common/themes/base/css/theme.css" rel="stylesheet"/>
<link href="<%=contextPath %>/cfg-resource/coral40/common/themes/base/css/globalSearch.css" rel="stylesheet"/>
    <style>
        #tabbarDiv.coral-tabs{
            background: transparent;
        }
        .globalbg{
            background:url('${ctx}/cfg-resource/coral40/common/themes/base/images/1920.jpg') no-repeat center -35px;
            background-size:100% 100%;
            filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${ctx}/cfg-resource/coral40/common/themes/base/images/1920.jpg',sizingMethod='scale');
        }
    </style>
</head>
<body onresize="" >
    <ces:layout id="mainLayout" fit="true">
        <ces:layoutRegion region="north" maxHeight="300" minWidth="1024" style="height:38px;">
            <jsp:include page="app_header_new.jsp">
                <jsp:param name="multi" value="${_multi_}"></jsp:param>
            </jsp:include>
        </ces:layoutRegion>
        <ces:layoutRegion region="center" minWidth="1024" cls="globalbg">
            <ces:tabs id="tabbarDiv" cls="coral-tabs-bottom" heightStyle="fill" onTabClose="tabClose" onActivate="activateTab"><ul></ul></ces:tabs>
        </ces:layoutRegion>
        <%@ include file="/WEB-INF/layouts/app_footer_new.jsp" %>
    </ces:layout>
</body>
</html>
<script type="text/javascript">
    var menuTabbarSize = 0;
    //add by myr at 20141120 JRIA GSLHKF-564 系统打开时，菜单栏固定锁住（即显示二层菜单），并显示第一个一层菜单内容
    function loadIndex(){
        $(".headMid .headMid_c .headul li:first").click();
        $('.menu_b').show();
        $('.lockside').show();
        if ($('.lockside').hasClass('unlock')) {
            $('.lockside').click();
        }
        // menuClick('台账','/cfg-resource/coral40/views/taizhang.jsp');
    }
    /** 刷新所有的组件 */
    function activateTab() {
        //$.coral.refreshAllComponent();
        $(".ctrl-init-layout:visible").layout("refresh");
    }
    $("#tabbarDiv").css({"width":"100%", "height":"100%"});
    function menuClick(name, url, id) {
        if (isEmpty(url)) {
            return;
        }
        if (url.charAt(0) != '/') {
            url = "/" + url;
        }
        if ($("#mainLayout").data(url)) {
            var tabId = $("#mainLayout").data(url);
            var currentTabId = $("#tabbarDiv").tabs("getIdByIndex", $("#tabbarDiv").tabs("option", "active"));
            if (currentTabId == tabId) {
                return;
            }
            $("#tabbarDiv").tabs("option", "active", "#" + tabId);
            $.confirm('是否刷新？', function(r) {
                if (r) {
                    CFG_refreshTab(tabId, name, url);
                }
            });
        } else {
            if (tabMaxSize > 0 && menuTabbarSize >= tabMaxSize) {
                CFG_message("最多能够打开" + tabMaxSize + "个菜单标签页！", "warning");
                return;
            }
            var tabId = generateId("tab");
            //$("#tabbarDiv").tabs("add", name, "<iframe src='" + contextPath + "/" + url + "' width='100%' height='100%' scrolling='Auto' frameborder='0' marginwidth='0' marginheight='0'></iframe>");
            //$("#tabbarDiv").tabs("add", name, "", {"href":tabId, "closeable":true});
            $("#tabbarDiv").tabs("add", {
                label : name,
                content : "",
                href : "#" + tabId,
                closeable : name =='台账'?false:true
            }).tabs("option", "active", "#" + tabId);
            CFG_openTab(tabId, name, url);
            $("#tabbarDiv").tabs("option", "active", "#" + tabId);
            $("#mainLayout").data(url, tabId);
            menuTabbarSize++;
        }
    }
    function CFG_openTab(tabId, name, url) {
        var navigationId = generateId("nav");
        $.ajax({
            url : contextPath + url,
            dataType : "html",
            context : document.body
        }).done(function(html) {
            var CFG_configInfo = {'tabId':tabId, 'name':name, 'url':url};
            $("#" + tabId).data("selfUrl", url);
            $("#" + tabId).data("parentConfigInfo", CFG_configInfo);
            $("#" + tabId).append(html);
            $.parser.parse($("#" + tabId));
            if (url.indexOf("useNavigation") != -1) {
                $("#" + tabId).prepend("<div id='" + navigationId + "'></div>");
                $('#' + navigationId).navigationbar();
                var content = $("#" + tabId).children("div")[1];
                $(content).height($("#" + tabId).height() - 31).addClass("coral-adjusted");
                activateTab();
                CFG_configInfo.CFG_navigationBar = $('#' + navigationId);
                CFG_getEmbeddedInfo(CFG_configInfo);
                $('#' + navigationId).addNavigation({'name':name, 'index':0, 'configInfo':CFG_configInfo.childConfigInfo});
            }
            if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_bodyOnLoad) {
                CFG_configInfo.childConfigInfo.CFG_bodyOnLoad();
            }
            if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_initReserveZones) {
                CFG_configInfo.childConfigInfo.CFG_initReserveZones();
            }
            if (CFG_configInfo.childConfigInfo && CFG_configInfo.CFG_navigationBar) {
                CFG_configInfo.childConfigInfo.CFG_navigationBar = $('#' + navigationId);
            }
        });
    }
    function CFG_refreshTab(tabId, name, url) {
        CFG_emptyJQ($("#" + tabId));
        CFG_openTab(tabId, name, url);
    }
    function tabClose(e, eventData) {
        $.each($("#mainLayout").data(), function(i, v) {
            if (v == eventData.panelId) {
                $("#mainLayout").removeData(i);
            }
        });
        menuTabbarSize--;
    }
    function CFG_addTab(CFG_configInfo, text, url) {
        var tabId = generateId("tab");
        /*$("#tabbarDiv").tabs("add", text, "", {
            "href" : tabId,
            "closeable" : true
        });*/
        $("#tabbarDiv").tabs("add", {
            label : text,
            content : "",
            href : "#" + tabId,
            closeable : true
        }).tabs("option", "active", "#" + tabId);
        $.ajax({
            url : url,
            dataType : "html",
            context : document.body
        }).done(function(html) {
            $("#" + tabId).data("selfUrl", url);
            $("#" + tabId).data("parentConfigInfo", CFG_configInfo);
            $("#" + tabId).append(html);
            $.parser.parse($("#" + tabId));
            if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_bodyOnLoad) {
                CFG_configInfo.childConfigInfo.CFG_bodyOnLoad();
            }
            if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_initReserveZones) {
                CFG_configInfo.childConfigInfo.CFG_initReserveZones();
            }
        });
        $("#tabbarDiv").tabs("option", "active", "#" + tabId);
    }
    //初始化企业档案数据
    function initQyda(sysId, companyCode) {
        $.loadJson($.contextPath + "/trace!initQyda.json?sysId=" + sysId + "&companyCode=" + companyCode);
    }
    // head part
    $(function() {
        $.getJSON(contextPath + "/menu/menu!loadAppMenuForCoral40.json?systemId=" + systemId + "&P_timestamp=" + getTimestamp(), function(data) {
            if (typeof data == 'string') {
                data = eval("(" + data + ")");
            }
            $.each(data, function(i, menuA) {
                if (!menuA) return;
                if (menuA.icon1 && isNotEmpty(menuA.icon1)) {
                	if (menuA.url) {
						$('.headMid .headMid_c .headul').append('<li onclick="menuClick(\''+menuA.name+'\',\''+menuA.url+'\',\''+menuA.id+'\')" class="three" style="background-image: url(${ctx}/cfg-resource/coral40/common/themes/base/images/menuicon/onelevel/'+menuA.icon1+')">'+menuA.name+'</li>');
					} else {
						$('.headMid .headMid_c .headul').append('<li class="three" style="background-image: url(${ctx}/cfg-resource/coral40/common/themes/base/images/menuicon/onelevel/'+menuA.icon1+')">'+menuA.name+'</li>');
					}
                } else {
                	if (menuA.url) {
                		$('.headMid .headMid_c .headul').append('<li onclick="menuClick(\''+menuA.name+'\',\''+menuA.url+'\',\''+menuA.id+'\')" class="three">'+menuA.name+'</li>');
                	} else {
                		$('.headMid .headMid_c .headul').append('<li class="three">'+menuA.name+'</li>');
                	}
                }
                var content = '<div class="headmenu clearfix">';
                if (menuA.items) {
                    var menuBRoot;
                    var menuBRootContent = new Array();
                    var menuBContent = new Array();
                    $.each(menuA.items, function(j, menuB) {
                        if (!menuB) return;
                        if (menuB.items.length != 0) {
                            var menuBElement;
                            if (menuB.icon2 && isNotEmpty(menuB.icon2)) {
                                menuBElement = '<div class="menu_area"><div class="menu_second"><img src="${ctx}/cfg-resource/coral40/common/themes/base/images/menuicon/twolevel/'+menuB.icon2+'"><p>'+menuB.name+'</p></div><div class="menu_third">';
                            } else {
                                menuBElement = '<div class="menu_area"><div class="menu_second"><img src="${ctx}/cfg-resource/coral40/common/themes/base/images/headmenu/icon2_1.png"><p>'+menuB.name+'</p></div><div class="menu_third">';
                            }
                            var menuBElementContent = new Array();
                            $.each(menuB.items, function(k, menuC) {
                                if (!menuC) return;
                                if (menuC.url) {
                                    menuBElementContent[menuBElementContent.length] = '<li onclick="menuClick(\''+menuC.name+'\',\''+menuC.url+'\',\''+menuC.id+'\')">'+menuC.name+'</li>';
                                    if (isNotEmpty(menuC.code)) {
                                        $('.headMid .headMid_c .headul').data(menuC.code, {'id':menuC.id, 'name':menuC.name, 'url':menuC.url});
                                    }
                                }
                            });
                            $.each(menuBElementContent, function(l, v) {
                                if (l % 2 == 0) {
                                    menuBElement += '<ul>';
                                }
                                menuBElement += v;
                                if (l % 2 == 1 || l == menuBElementContent.length-1) {
                                    menuBElement += '</ul>';
                                }
                            });
                            menuBElement += '</div></div>';
                            menuBContent[menuBContent.length] = menuBElement;
                        } else {
                            if (!menuBRoot) {
                                if (menuA.icon1 && isNotEmpty(menuA.icon2)) {
                                    menuBRoot = '<div class="menu_area"><div class="menu_second"><img src="${ctx}/cfg-resource/coral40/common/themes/base/images/menuicon/twolevel/'+menuA.icon2+'"><p>'+menuA.name+'</p></div><div class="menu_third">';
                                } else {
                                    menuBRoot = '<div class="menu_area"><div class="menu_second"><img src="${ctx}/cfg-resource/coral40/common/themes/base/images/headmenu/icon2_1.png"><p>'+menuA.name+'</p></div><div class="menu_third">';
                                }
                            }
                            menuBRootContent[menuBRootContent.length] = '<li onclick="menuClick(\''+menuB.name+'\',\''+menuB.url+'\',\''+menuB.id+'\')">'+menuB.name+'</li>';
                            if (isNotEmpty(menuB.code)) {
                                $('.headMid .headMid_c .headul').data(menuB.code, {'id':menuB.id, 'name':menuB.name, 'url':menuB.url});
                            }
                        }
                    });
                    if (menuBRoot) {
                        $.each(menuBRootContent, function(l, v) {
                            if (l % 2 == 0) {
                                menuBRoot += '<ul>';
                            }
                            menuBRoot += v;
                            if (l % 2 == 1 || l == menuBRootContent.length-1) {
                                menuBRoot += '</ul>';
                            }
                        });
                        menuBRoot += '</div></div>';
                        content += menuBRoot;
                    }
                    $.each(menuBContent, function(l, v) {
                        content += v;
                    });
                }
                content += '</div>';
                //alert(content);
                $('.menu_b').append(content);
            });
            menuInit();
            //loadIndex();
            openMenuAfterPageOpen();
        });
        function menuInit() {
            init();
            //初始化操作
            function init() {
                //menu lock/unlock
                if ($('.menu_b').hasClass('menufix')) {
                    $('.menu_b').mouseleave(function() {
                        $(this).hide();
                    });
                } else {
                    $('.menu_b').mouseleave(function() {
                        $(this).show();
                    });
                }
            };
            //head addClass('hit')
            $('.headul li').click(function() {
                $(this).addClass('hit').siblings().removeClass('hit');
                $('.menu_b>div:eq(' + $(this).index() + ')').show().siblings().hide();
                $('.menu_b,.lockside').show();
            }).mouseover(function() {
                /*$(this).addClass('hit').siblings().removeClass('hit');
                $('.menu_b>div:eq(' + $(this).index() + ')').show().siblings().hide();
                $('.menu_b').show();
                $('.lockside').show();*/
            }).mouseleave(function() {
                // init();
            });
            /**
             * second menu lock/unlock
             */
            $('.lockside').click(function() {
                if ($(this).hasClass('unlock')) {
                    $(this).removeClass('unlock').addClass('lock');
                    $('.menu_b').removeClass('menufix');
                    $('#mainLayout').layout('resize', {
                        height : '115'
                    }, "north");
                } else {
                    $(this).removeClass('lock').addClass('unlock');
                    $('.menu_b').addClass('menufix');
                    $('#mainLayout').layout('resize', {
                        height : '38'
                    }, "north");
                }
                $(":coral-layout").each(function() {
                    if ($(this).is(":visible")) {
                        $(this).layout("refresh");
                    }
                });
                init();
            });
            //third menu click
            $('.menu_third ul li').click(function() {
                $(this).addClass('current').siblings().removeClass('current');
                $(this).parent().siblings().children().removeClass('current');
            });
        }
        function openMenuAfterPageOpen() {
            // 是否要打开某个菜单
            var localUrl = window.location.href;
            var paramStr = localUrl.substring(localUrl.indexOf('?') + 1);
            var params = paramStr.split('&');
            var menuCode;
            for (var i in params) {
                var keyvalue = params[i].split("=");
                if (keyvalue[0] == 'menuCode') {
                    menuCode = keyvalue[1];
                }
            }
            if (menuCode) {
                var menuInfo = $('.headMid .headMid_c .headul').data(menuCode);
                if (menuInfo) {
                    var url = menuInfo.url;
                    if (url.indexOf('?') != -1) {
                        url += '&' + paramStr;
                    } else {
                        url += '?' + paramStr;
                    }
                    menuClick(menuInfo.name, url, menuInfo.id);
                }
            }
            //superadmin登陆打开第一个菜单
            if (systemId == "402881f14cdfafda014ce04901ee007b") {
                menuClick('消息设置','/cfg-resource/coral40/views/selfdefine/qyptxxsz/MT_component.jsp?P_moduleId=8a8ad0e74ce50198014ce5847b390007&constructId=8a8ad0e74ce50198014ce584bc1c000f&bindingType=menu&menuId=8a8ad0e74ce50198014ce5857b2e0015&componentVersionId=8a8ad0e74ce50198014ce584bc10000e&menuCode=QYPTXXSZ&topComVersionId=8a8ad0e74ce50198014ce584bc10000e&useNavigation=1','8a8ad0e74ce50198014ce5857b2e0015');
            }
        }
        var companyCode = "<%=request.getParameter("companyCode")%>";
        if (systemId != null && systemId != "") {
            initQyda(systemId, companyCode);
        }
    });
</script>