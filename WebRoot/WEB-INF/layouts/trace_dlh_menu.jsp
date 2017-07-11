<%@page import="com.ces.config.utils.CommonUtil" %>
<%@ page import="com.ces.config.utils.StringUtil" %>
<%@ page import="com.ces.config.utils.SystemParameterUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
	//设置企业编码的session
/*	request.getSession().setAttribute("_systemId_", request.getParameter("systemId"));
	request.getSession().setAttribute("_companyCode_", request.getParameter("companyCode"));*/
	request.getSession().setAttribute("_multi_", request.getParameter("multi"));
	String username = CommonUtil.getUser().getUsername();
	String name = CommonUtil.getUser().getName();
	String multi = request.getParameter("multi");
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<title>青海省中药材信息管理系统 V1.0</title>
	<script type="text/javascript">
		var contextPath = "${ctx}";
		var systemId = '<%=request.getParameter("systemId")%>';
	</script>
	    <%@include file="/cfg-resource/coral40/common/jsp/_cui_library.jsp" %>

		<link rel="stylesheet" href="<%=contextPath %>/cfg-resource/coral40/common/themes/qhzyc/css/style.css">
		<link rel="stylesheet" href="<%=contextPath %>/cfg-resource/coral40/common/themes/qhzyc/css/swiper.min.css">
	    <script src="<%=contextPath %>/cfg-resource/coral40/common/js/trace/hdty.config.js"></script>
<style>
	img {
		max-width: 100%;
		max-height: 100%;
		margin-left: 0px;
		margin-top: 0px;
	}
	.ycb_nav li {
		font-size: 16px;
	}
	.ycb_nav li div dl{
		font-size: 14px;
	}
	.sdyc_content{
		width:100%;
		height: 922px;
	}
</style>
</head>
<body>
<div class="ycb">
	<!--header-->
	<div class="ycb_header">
		<!--logo-->
		<div class="ycb_login">
			<svg class="icon" aria-hidden="true">
				<use xlink:href="#icon-weibiaoti-"></use>
			</svg>
			<h4>德令哈中药材种植加工流通追溯系统</h4>
		</div>
		<!--nav-->
		<div class="ycb_nav">

		</div>
		<!--loginbar-->
		<div class="ycb_loginbar">
			<dl>
				<dt><a href="javascript:void(0)">
					<svg class="icon" aria-hidden="true">
						<use xlink:href="#icon-weibiaoti-3"></use>
					</svg>
					<span>系统中心</span></a>
				</dt>

				<dd><a href="javascript:void(0)" onclick="changePassword()">
					<svg class="icon" aria-hidden="true">
						<use xlink:href="#icon-weibiaoti-5"></use>
					</svg>
					<span>账户管理</span></a>
				</dd>
				<dd><a href="javascript:void(0)" onclick="logout()">
					<svg class="icon" aria-hidden="true">
						<use xlink:href="#icon-zhuxiao"></use>
					</svg>
					<span>注销</span></a>
				</dd>

			</dl>
		</div>
	</div>
	<!--banner-->
	<div id="mainContent" class="sdyc_content" style="z-index:-9999">
	<div class="ycb_banner">
		<img src="<%=contextPath %>/cfg-resource/coral40/common/themes/qhzyc/images/ycb_banner.png" alt="">
		<span><img src="<%=contextPath %>/cfg-resource/coral40/common/themes/qhzyc/images/bt.png" alt=""></span>
	</div>
	<!--content-->
	<div class="swiper-container">
		<div class="swiper-wrapper">
			<div class="swiper-slide"><img src="<%=contextPath %>/cfg-resource/coral40/common/themes/qhzyc/images/zy1.png" alt=""></div>
			<div class="swiper-slide"><img src="<%=contextPath %>/cfg-resource/coral40/common/themes/qhzyc/images/zy1.png" alt=""></div>
			<div class="swiper-slide"><img src="<%=contextPath %>/cfg-resource/coral40/common/themes/qhzyc/images/zy1.png" alt=""></div>
		</div>
		<!-- 如果需要分页器 -->
		<div class="swiper-pagination"></div>
	</div>
</div>
	<!--foot-->
	<div class="ycb_foot">
		COPYRIGHT 2016 <span>上海中信信息发展股份有限公司</span> ALL RIGHTS RESERVED
	</div>
</div>


</body>
<script src="<%=contextPath %>/cfg-resource/coral40/common/themes/qhzyc/foot/iconfont.js"></script>
<script src="<%=contextPath %>/cfg-resource/coral40/common/themes/qhzyc/js/swiper.js"></script>
<script>
	var mySwiper = new Swiper('.swiper-container', {
		autoplay: 5000,//可选选项，自动滑动
		// 如果需要分页器
		pagination: '.swiper-pagination',
		paginationClickable: true,
	})
    var passworddom="<div id=\"gyzd\" ><div class=\"app-inputdiv10\" > <label  class=\"app-input-label\" >原密码:</label> <input  id=\"YMM${idSuffix}\" name=\"YMM\" data-options=\"required:true\" class=\"coralui-textbox\" /> </div>  <div class=\"app-inputdiv10\" > <label class=\"app-input-label\">新密码:</label> <input id=\"XMM${idSuffix}\" type=\"password\" name=\"XMM\" data-options=\"required:true\" class=\"coralui-textbox\"/> </div>  <div class=\"app-inputdiv10\" > <label  class=\"app-input-label\">确认密码:</label> <input id=\"QRMM${idSuffix}\" type=\"password\" name=\"QRMM\" data-options=\"required:true,/* validType:'mobile' */\" class=\"coralui-textbox\"/></div>"
    function timeControl (){
        var starttiemstamp;
        var endtiemstamp;
        var timess = parseInt(getCookie("timess"));
        if(timess == 0 || isEmpty(timess)){
            return;
        }
        $("#warningWav").dialog({
            title:"智能提醒",
            modal : true,
            onOpen : function(){
                starttiemstamp = new Date().getTime();
                $(this).append(" <div style=\"display: none\"><audio  src=\"spzstpfj/video/4031.wav\" autoplay=\"autoplay\" loop=\"loop\"  controls=\"controls\"></audio></div>");
                //$(this).append("<div align=\"center\" style=\"margin-top: 10px;font-size: 30px;color: red;font-weight: bold\"> <span color=\"red\">提醒！提醒！！</span> <div>");
                $(this).append("<div align=\"center\"><img src=\"spzstpfj/video/labb.gif\" /> </div>");
            },onClose : function(){
                $(this).empty();
                endtiemstamp = new Date().getTime();
                var readTime =timess- (endtiemstamp - starttiemstamp);
                if(readTime< 1){
                    readTime= timess -readTime;
                }
                setTimeout("timeControl()",readTime);
            }
        })
    }
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
                        jqDiv   = $("<div width=\"400px \" class=\"dialog-toolbar\">").appendTo(jqPanel);
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
                QRMM:qrmm
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
    }
    function menuClick(name, url, id) {
        debugger
        if (isEmpty(url)) {
            return;
        }
        if(!isEmpty($('.menudrop'))){
            $('.menuList').removeClass('active');
            $('.menuList').children('.menudrop').hide();
        }
        if (url.charAt(0) != '/') {
            url = "/" + url;
        }
        $("#mainContent").empty();
        var tabId = "mainContent";
        CFG_openTab(tabId, name, url);
    }
    function CFG_openTab(tabId, name, url) {

       if(name=='工作台'){
           window.location.reload();
	   }
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
	$(function () {

		$.getJSON(contextPath + "/menu/menu!loadAppMenuForCoral40.json?systemId=" + systemId + "&P_timestamp=" + getTimestamp(), function (data) {console.info(data);
			if (typeof data == 'string') {console.info(data);
				data = eval("(" + data + ")");
			}
           /* menuClick('工作台','/cfg-resource/coral40/views/component/sdzycbwttz/sdzycbwttz.jsp');*/
			var onemenu ='<ul>';
			var onemenus='';
			var onesy='';
			$.each(data, function (i, menuA) {
				if (!menuA) return;
				var ermenu = "";
				if(menuA.items.length==0){//菜单下面没有子菜单的走这里
					if(menuA.code == 'sy'){//加载首页菜单样式
						 onesy += '<li><a href="javascript:void(0)" onclick="menuClick(\'工作台\',\'/cfg-resource/coral40/views/component/sdzycbwttz/sdzycbwttz.jsp\',this)"><svg class="icon" aria-hidden="true"><use xlink:href="#icon-weibiaoti-9"></use>	</svg>'
								+menuA.name
						        +'<span class="nav_l_line"></span></a>';
						/*$('.ycb_nav').append(sy);*/
					}
				}else{
				    if(menuA.code == 'zz'){
                        onemenus += ' <li><a href="javascript:void(0)"><svg class="icon" aria-hidden="true"><use xlink:href="#icon-weibiaoti-8"></use></svg>'
                            +menuA.name
                            +'<span class="nav_l_line"></span></a><div class="submenu">' ;
                        onemenus+=initMenu(menuA);
                        onemenus +='</div></li>';
                        //$('.ycb_nav').append(ermenu);
					}else if(menuA.code == 'cjg'){
                        onemenus += ' <li><a href="javascript:void(0)"><svg class="icon" aria-hidden="true"><use xlink:href="#icon-weibiaoti-6"></use></svg>'
                            +menuA.name
                            +'<span class="nav_l_line"></span></a><div class="submenu">' ;
                        onemenus+=initMenu(menuA);
                        onemenus +='</div></li>';
                    }else if(menuA.code == 'jjg'){
                        onemenus += ' <li><a href="javascript:void(0)"><svg class="icon" aria-hidden="true"><use xlink:href="#icon-weibiaoti-10"></use></svg>'
                            +menuA.name
                            +'<span class="nav_l_line"></span></a><div class="submenu">' ;
                        onemenus+=initMenu(menuA);
                        onemenus +='</div></li>';
                    }
				   /* // 含有子菜单的菜单
						onemenus += ' <li><a href="javascript:void(0)"><svg class="icon" aria-hidden="true"><use xlink:href="#icon-weibiaoti-8"></use></svg>'
						    	+menuA.name
								+'<span class="nav_l_line"></span></a><div class="submenu">' ;
					    onemenus+=initMenu(menuA);
					    onemenus +='</div></li>';
						//$('.ycb_nav').append(ermenu);*/
				}
			});
			onemenu = onemenu+onesy+onemenus;
		   $('.ycb_nav').append(onemenu);
            $('.sdyc_sidebar').append(onemenu);
            $(".ycb_nav li").mousemove(function () {
                $(this).find(".submenu").show()
                $(this).find("a").eq(0).addClass("header_bg")
            })
            $(".ycb_nav li").mouseleave(function () {
                $(this).find(".submenu").hide()
                $(this).find("a").eq(0).removeClass("header_bg")
            })
            $(".submenu").mousemove(function () {
                $(this).parent("li").find("a").eq(0).addClass("header_bg")
            })
            $(".submenu").mouseleave(function () {
                $(this).parent("li").find("a").eq(0).removeClass("header_bg")
            })
            $(".ycb_loginbar").mousemove(function () {
                $(this).css({
                    "background": "url(<%=contextPath %>/cfg-resource/coral40/common/themes/qhzyc/images/r_bg.png)",
                    "backgroundSize": "100% 100%"
                })
                $(".ycb_loginbar dd").show()
                $(".ycb_loginbar dt a").addClass("dt_zh")
            })
            $(".ycb_loginbar").mouseleave(function () {
                $(this).css({
                    "background": "none"
                })
                $(".ycb_loginbar dd").hide()
                $(".ycb_loginbar dt a").removeClass("dt_zh")
            })
            /*$(".ycb_nav li").click(function () {
                $(this).find(".submenu").toggle();
                $(this).siblings("li").children("div").hide();
                $(this).children("a").addClass("ycb_nav_hover");
                $(this).siblings("li").children("a").removeClass("ycb_nav_hover");
            });
            $(".submenu dl dd a").click(function () {
                $(".submenu").css("display","none!important")
            });
            $(".ycb_loginbar").click(function () {
                $(this).css({
                    "background": "url(<%=contextPath %>/cfg-resource/coral40/common/themes/qhzyc/images/r_bg.png)",
                    "backgroundSize": "100% 100%"
                })
                $(".ycb_loginbar dd").show();
                $(".ycb_loginbar dt a").addClass("dt_zh")
            })
            $(".ycb_loginbar dl dd a").click(function () {
                $(".ycb_loginbar").css({
                    "background": "none"
                });
                $(".ycb_loginbar dd").hide();
                $(".ycb_loginbar dt a").removeClass("dt_zh");
            })*/

			/*$('.menuList').click(function(event) {
				$(this).addClass('active').siblings().removeClass('active');
				$(this).siblings().children('.menudrop').hide();
				$(this).children('.menudrop').show();
				dropwidth();
			}).mouseleave(function(event) {
				$(this).removeClass('active');
				$(this).children('.menudrop').hide();
			});*/
			//二级菜单
			function dropwidth(){
				var droplength = $('.menudrop:visible').find('.mdropArea').length;
				var dropwidth = $('.mdropArea').width();
				$('.menudrop:visible').width(dropwidth * droplength+80);
			}

		});
		function initMenu(menuA) {
			if (checkHasThree(menuA)) {//存子菜单
				var menuContent ='';
				var ermuen ='';

				$.each(menuA.items, function (j, menuB) {
					var menuContentC = '<dl><dt>'+menuB.name+' <img src="<%=contextPath %>/cfg-resource/coral40/common/themes/qhzyc/images/al.png" alt=""></dt>';
					$.each(menuB.items, function (k, menuC) {
						menuContentC += '<dd><a href="javascript:void(0)" onclick="menuClick(\'' + menuC.name + '\',\'' + menuC.url + '\',\'' + menuC.id + '\',this)" >' + menuC.name + '</a></dd>';
					});
					menuContentC += "</dl>";
					menuContent+=ermuen.replace("menuName", menuB.name)+menuContentC;

				});
				return menuContent;
			}
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
		var companyCode = "<%=request.getParameter("companyCode")%>";
		if (systemId != null && systemId != "") {
			initQyda(systemId, companyCode);
		}
		var timess = parseInt(getCookie("timess"));
		if(timess != 0 && !isEmpty(timess) && !isNaN(timess)){
			setTimeout("timeControl()",timess);
		}

	});

	function getCookie(name)
	{
		var arr=document.cookie.split('; ');
		for(var i=0;i<arr.length;i++)
		{
			//arr2->['username', 'abc']
			var arr2=arr[i].split('=');
			if(arr2[0]==name)
			{
				var getC = arr2[1];
				return getC;
			}
		}

		return '';
	}
</script>
</html>
