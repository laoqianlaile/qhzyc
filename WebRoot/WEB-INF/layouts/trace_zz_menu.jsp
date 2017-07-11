<%@page import="com.ces.config.utils.CommonUtil" %>
<%@ page import="com.ces.config.utils.StringUtil" %>
<%@ page import="com.ces.config.utils.SystemParameterUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
	String systemName = SystemParameterUtil.getInstance().getSystemParamValue("系统名称");
	if (StringUtil.isEmpty(systemName)) {
		systemName = "食用农产品生产作业信息管理系统 V2.0";
	}
	//设置企业编码的session
	/*request.getSession().setAttribute("_systemId_", request.getParameter("systemId"));
	request.getSession().setAttribute("_companyCode_", request.getParameter("companyCode"));*/
	request.getSession().setAttribute("_multi_", request.getParameter("multi"));
	String username = CommonUtil.getUser().getUsername();
	String name = CommonUtil.getUser().getName();
	String multi = request.getParameter("multi");
%>
<!DOCTYPE html>
<html>
<head>
	<%--<title><%=systemName%></title>--%>
	<title>食用农产品生产作业信息管理系统 V2.0</title>
	<script type="text/javascript">
		var contextPath = "${ctx}";
		var systemId = '<%=request.getParameter("systemId")%>';
	</script>
	<%@include file="/cfg-resource/coral40/common/jsp/_cui_library.jsp" %>
	<link href="<%=contextPath %>/cfg-resource/coral40/common/themes/zz/css/zzmain.css" rel="stylesheet"/>
	<script src="<%=contextPath %>/cfg-resource/coral40/common/js/trace/hdty.config.js"></script>
</head>
<body>
<div class="wrapper">
	<div class="headTop">
		<span class="logo">食用农产品生产作业信息管理系统 V2.0</span>
		<ul class="usermm">
			<li><a href="#" class="mm-uname"><%=name%></a></li>
			<li onclick="changePassword()"><a href="#" class="mm-user">账号管理</a></li>
		<% if ("true".equals(multi)) { %>
			<li><a href="javascript:window.open('${ctx}/trace.jsp', '_self');" class="ch-sys">切换系统</a></li>
		<% } %>
			<li onclick="logout()"><a href="#" class="esc-sys">注销</a></li>
		</ul>
	</div>
	<div class="headwrap">
		<div class="header">
			<ul class="headul">
			</ul>
			<a href="javascript:download('${ctx}/download/食安追溯云平台用户帮助手册.docx');"><div class="head-help"></div></a>
		</div>
		<!--second menu start-->
		<div class="menu_b menufix">
			<p class="lockside unlock"></p>

		</div>
		<!--second menu end-->
	</div>
	<!--head end-->
	<div class="tablewp" id="mainContent">

	</div>
	<div class="footerwp">Copyright © 2015 上海中信信息发展股份有限公司 版权所有</div>
</div>


</body>
</html>
<script type="text/javascript">
	var passworddom="<div id=\"gyzd\" ><div class=\"app-inputdiv10\" > <label  class=\"app-input-label\" >原密码:</label> <input  id=\"YMM${idSuffix}\" name=\"YMM\" data-options=\"required:true\" class=\"coralui-textbox\" /> </div>  <div class=\"app-inputdiv10\" > <label class=\"app-input-label\">新密码:</label> <input id=\"XMM${idSuffix}\" type=\"password\" name=\"XMM\" data-options=\"required:true\" class=\"coralui-textbox\"/> </div>  <div class=\"app-inputdiv10\" > <label  class=\"app-input-label\">确认密码:</label> <input id=\"QRMM${idSuffix}\" type=\"password\" name=\"QRMM\" data-options=\"required:true,/* validType:'mobile' */\" class=\"coralui-textbox\"/></div>"

	//注销
	function logout() {
		$.confirm("确定注销", function (r) {
			if (r) {
				window.location = '${ctx}/logout';
			}
		});
	}
	//修改密码
	function changePassword(){

		var jqGlobal = document.body;
		var dialogDivJQ = $("<div></div>").appendTo(jqGlobal);
		dialogDivJQ.dialog({
			title:'修改密码',
			width:300,
			height:160,
			resizable:false,
			draggable:false,
			modal:true,
			autoDestroy:true,
			onCreate : function (e,ui){
				$(passworddom).appendTo(dialogDivJQ);
				$.parser.parse(e.target);
			},
            onOpen: function(e,ui){
                var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
                        jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
                jqDiv.toolbar({
                    data: ["->", {id:"sure", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
                    onClick: function(e, ui) {
                        if ("sure" === ui.id) {
                            confire();
                        } else {
                            dialogDivJQ.remove();
                        }
                    }
                });
            },
			onClose:function(){
				dialogDivJQ.remove();
			}

		}
		);
		$("#button1").button();
	}
   //密码修改验证
	function confire(){
		//var val=document.getElementById("YMM${idSuffix}").value;
		var ymm=$("#YMM${idSuffix}").textbox('getValue');
		var xmm=$("#XMM${idSuffix}").textbox('getValue');
		var qrmm=$("#QRMM${idSuffix}").textbox('getValue');
		if((ymm=='')||(xmm=='')||(qrmm=='')){CFG_message("请将信息填写完整！", "error");return;}
		if(xmm!=qrmm){CFG_message("两次密码输入不一样！", "error");return;}
		if(ymm==xmm){CFG_message("新密码不能和原密码一样！", "error");return;}
		
		if(xmm.length < 8){CFG_message("密码最少为8位！", "error");return;}//判断密码是否大于6位数字
		if(qrmm.length < 8){CFG_message("密码最少为8位！", "error");return;}
		//var jsonData = $.loadJson($.contextPath + "/zzxgmm!changePassword.json?DKBH="+dkbh);
		$.ajax({
			type: 'POST',
			url:$.contextPath+"/trace!changePassword.json",
			data: {
				YMM:ymm,
				XMM:xmm,
				QRMM:qrmm,
			},
			dataType: "json",
			//contentType: false,//必须false才会自动加上正确的Content-Type
			//processData: false,//必须false才会避开jQuery对 formdata 的默认处理XMLHttpRequest会对 formdata 进行正确的处理
			//timestamp: false,

			success: function (data) {
				if(data==0){CFG_message("操作成功！", "success");}else{CFG_message("密码错误！", "error");}

			},
			error: function () {
				CFG_message("操作失败！", "error");
			}
		})
		//alert(ymm+"   "+xmm+"    "+qrmm);
	}

	function menuClick(name, url, id) {
		if (isEmpty(url)) {
			return;
		}
		if (url.charAt(0) != '/') {
			url = "/" + url;
		}
		$("#mainContent").empty();
		var tabId = "mainContent";
		CFG_openTab(tabId, name, url);
	}
	function CFG_openTab(tabId, name, url) {
		$.ajax({
			url: contextPath + url,
			dataType: "html",
			context: document.body
		}).done(function (html) {
			var CFG_configInfo = {'tabId': tabId, 'name': name, 'url': url};
			$("#" + tabId).data("selfUrl", url);
			$("#" + tabId).data("parentConfigInfo", CFG_configInfo);
			$("#" + tabId).append(html);
			$.parser.parse($("#" + tabId));
			if (url.indexOf("useNavigation") != -1) {
				var content = $("#" + tabId).children("div")[1];
				$(content).height($("#" + tabId).height() - 31).addClass("coral-adjusted");
				CFG_getEmbeddedInfo(CFG_configInfo);
			}
			if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_bodyOnLoad) {
				CFG_configInfo.childConfigInfo.CFG_bodyOnLoad();
			}
			if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_initReserveZones) {
				CFG_configInfo.childConfigInfo.CFG_initReserveZones();
			}
		});
	}
	//初始化企业档案数据
	function initQyda(sysId, companyCode) {
		$.loadJson($.contextPath + "/trace!initQyda.json?sysId=" + sysId + "&companyCode=" + companyCode);
	}
	// head part
	$(function () {
		$.getJSON(contextPath + "/menu/menu!loadAppMenuForCoral40.json?systemId=" + systemId + "&P_timestamp=" + getTimestamp(), function (data) {
			if (typeof data == 'string') {
				data = eval("(" + data + ")");
			}
			$.each(data, function (i, menuA) {
				if (!menuA) return;
				var className = "n" + menuA.code.toLowerCase();
				if(!isEmpty(menuA.url) && menuA.items.length==0){
					$('.headul').append('<li><a id="gzt" class="'+className+'" onclick="menuClick(\'' + menuA.name + '\',\'' + menuA.url + '\',\'' + menuA.id + '\')">' + menuA.name + '</a></li>');
				}else{
					$('.headul').append('<li><a class="'+className+'">' + menuA.name + '</a></li>');
				}
				var menuContent = "<div class='headmenu clearfix'><div class='menu_area' style='margin-left: "+computeMargin(className)+"px'>";
				//判断菜单下面是否有子选项

				if (checkHasThree(menuA)) {//存在三级菜单
					$.each(menuA.items, function (j, menuB) {
						console.log(menuB);
						var className = menuB.code.toLowerCase();
						menuContent += "<div class='" + className + "wp'><div class='" + className + "-ico'></div><ul class='" + className + "'>";
						$.each(menuB.items, function (k, menuC) {
							menuContent += '<li onclick="menuClick(\'' + menuC.name + '\',\'' + menuC.url + '\',\'' + menuC.id + '\')"><a>' + menuC.name + '</a></li>';
						});
						menuContent += "</ul></div>";
					});
				} else {//只有二级菜单
					menuContent += "<ul class='menu2other'>";
					$.each(menuA.items, function (j, menuB) {
						menuContent += '<li onclick="menuClick(\'' + menuB.name + '\',\'' + menuB.url + '\',\'' + menuB.id + '\')"><a>' + menuB.name + '</a></li>';
					});
					menuContent += "</ul>";
				}
				menuContent += "</div></div>";
				$(".menu_b").append(menuContent);
			});
			menuInit();
		});

		//计算二级菜单的偏移位置
		function computeMargin(className) {
			var margin = $("." + className).parents("li").offset().left - 44;
			return margin > 800 ? 800 : margin;
		}
		function checkHasThree(menu) {//检查是否存在三级菜单
			var flag = false;
			$.each(menu.items, function (j, menuB) {
				if (menuB.items.length > 0) {
					flag = true;
					return false;
				}
			});
			return flag;
		}

		function menuInit() {
			init();
            if (systemId=="402881024cf8b745014cf8d0e5d70026") {//作业系统打开工作台
                menuClick('工作台','/cfg-resource/coral40/views/component/zzgzt/zzgzt.jsp');
            } else if (systemId == "402881f14cdfafda014ce04901ee007b") {
                menuClick('消息设置','/cfg-resource/coral40/views/selfdefine/qyptxxsz/MT_component.jsp?P_moduleId=8a8ad0e74ce50198014ce5847b390007&constructId=8a8ad0e74ce50198014ce584bc1c000f&bindingType=menu&menuId=8a8ad0e74ce50198014ce5857b2e0015&componentVersionId=8a8ad0e74ce50198014ce584bc10000e&menuCode=QYPTXXSZ&topComVersionId=8a8ad0e74ce50198014ce584bc10000e&useNavigation=1','8a8ad0e74ce50198014ce5857b2e0015');
            }
			//屏幕缩放
			$(window).resize(function () {
				init();
			})


			//定义公共变量
			var w, h, contentH, tableboxH, menuleftulH, noticeallH, shortulH, footerH;

			//初始化操作
			function init() {

				w = parseInt($(window).width());
				h = parseInt($(window).height());

				footerH = parseInt($('.footer').height());
				shortulH = parseInt($('.shortul').height());

				//快捷方式
				if ($('.moreiocn').hasClass('up')) {
					$('.shortcut').css('height', '55px');
				} else {
					$('.shortcut').css('height', shortulH + 'px');
				}

				contentH = parseInt(h - $('.headwrap').height() - $('.footer').height());
				$('.content').css('height', contentH + 'px');

				tableboxH = parseInt(contentH - $('.slidebox').height() - 55);//55为$('.shortcut').height()
				$('.tablebox').css('height', tableboxH + 'px');

				noticeallH = parseInt(tableboxH - $('.noticeboard').height());
				$('.noticeall').css('height', noticeallH + 'px');


				//taizhang
				var videoLen = $('.slideborder').find('div.slidediv').length;
				var width = 10; //初始值是10，保证所有元素不被溢出
				for (var i = 0; i < videoLen; i++) {
					width += $('.slidediv').width();
				}
				$('.slidecontent').css({'width': width + 'px'});


				//menu lock/unlock
				if ($('.menu_b').hasClass('menufix')) {
					$('.menu_b').mouseleave(function () {
						$(this).hide();
					})
				} else {
					$('.menu_b').mouseleave(function () {
						$(this).show();
					})
				}

				//快捷方式数量判断
				var shortcutLen = $('.shortul').find('li').length;
				//快捷方式溢出判断
				if (w <= 1024 && shortcutLen <= 6) {
					$('.short_more').hide();
				}
				if (w >= 1250 && shortcutLen <= 7) {
					$('.short_more').hide();
				}
				if (w >= 1366 && shortcutLen <= 8) {
					$('.short_more').hide();
				}


				//ie7
				var userAgent = navigator.userAgent.toLowerCase();
				jQuery.browser = {
					version: (userAgent.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [])[1],
					msie: /msie/.test(userAgent) && !/opera/.test(userAgent)
				};
				if ($.browser.msie && ($.browser.version == "7.0")) {
					if (w <= 1024) {
						tableboxH = parseInt(contentH - 140 - 55);//55为$('.shortcut').height()
						$('.tablebox').css('height', tableboxH + 'px');
					} else {
						tableboxH = parseInt(contentH - 200 - 55);//55为$('.shortcut').height()
						$('.tablebox').css('height', tableboxH + 'px');
					}
				}


			}

			//head addClass('hit')
			$('.headul li').click(function () {
				$(this).addClass('hit').siblings().removeClass('hit');
				$('.menu_b').show();
				$('.lockside').show();
				$('.menu_b>div:eq(' + $(this).index() + ')').show().siblings().hide();
				init();
			}).mouseover(function () {

				$(this).addClass('hit').siblings().removeClass('hit');
				$('.menu_b>div:eq(' + $(this).index() + ')').show().siblings().hide();
				if (this.textContent == $('#gzt').text()) {
					$('#gzt').removeClass('hit');

				} else {
					$('.menu_b').show();
					$('.lockside').show();
				}
				init();
			}).mouseleave(function () {
				init();
			});

			//third menu click
			$('.menu_third ul li').click(function () {
				$(this).addClass('current').siblings().removeClass('current');
				$(this).parent().siblings().children().removeClass('current');
			})


			//second menu lock/unlock
			$('.lockside').click(function () {
				if ($(this).hasClass('unlock')) {
					$(this).removeClass('unlock').addClass('lock');
					$('.menu_b').removeClass('menufix');
				} else {
					$(this).removeClass('lock').addClass('unlock');
					$('.menu_b').addClass('menufix');
				}
				init();
			})


			//快捷方式
			$('.moreiocn').click(function () {
				if ($(this).hasClass('up')) {
					$(this).removeClass('up').addClass('down');
				} else {
					$(this).addClass('up').removeClass('down');
				}
				init();
			})
		}

		var companyCode = "<%=request.getParameter("companyCode")%>";
		if (systemId != null && systemId != "") {
			initQyda(systemId, companyCode);
		}
	});
</script>