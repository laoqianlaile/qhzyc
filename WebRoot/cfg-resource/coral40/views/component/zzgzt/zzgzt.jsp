<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
	String path = request.getContextPath();
    String systemId = request.getParameter("systemId");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
	SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
	Date date = sdf.parse(sdf.format(new Date()));
	String dqr = sdf.format(new Date(date.getTime() + Long.parseLong("5") * 24 * 60 * 60 * 1000));
	request.setAttribute("dqr",dqr);
    request.setAttribute("ctx",path);
%>
<style>
.contentwp{
	padding-top:30px;
	padding-left: 0px;
}
.cont-center{
	margin: 0px;
}
.cont-left{
	margin: 0 28px;
}
.con-title span a{
	color:#6e6e6e;
}
a:hover{cursor:pointer}
.environmenticon1,
.environmenticon2,
.environmenticon3,
.environmenticon4{
	display: inline-block;
	background: url(http://www.zhuisuyun.net/spzsypt/html/resource/css/images_common/environmentIcon.png) no-repeat;
	width: 72px !important;
	height: 58px !important;
	background-position: 8px -92px;
}
.environmenticon2{
	background-position: -80px -92px;
}
.environmenticon3{
	background-position: -183px -92px;
}
.environmenticon4{
	background-position: -290px -92px;
}
.lineleft{
	float:left !important;
	width:72px !important;

}
.env-title{
	margin-left:20px;
    font-size:16px;
    font-weight: bold;
}
.env-info{
	margin: 5px 20px 5px 20px;
    font-size:16px;
}

.cont-left {
	width: 52%;
	float: left;
	margin: 0 2.5%;
}
.con-title {
	font-size: 12px;
	font-weight: bold;
	color: #6e6e6e;
	margin-bottom: 13px;
}
.con-title:before, .con-title:after {
	display: table;
	content: "";
	line-height: 0
}

.con-title:after {
	clear: both
}

.con-title span.farming {
	float: right;
	font-weight: normal;
	padding-left: 10px;
	background: url("images/icos_other.png") no-repeat 0 -308px
}

.conleft-list {
	padding-top: 20px;
	padding-left: 20px;
	padding-bottom: 7px;
	*zoom: 1;
	border: #d8d8d8 solid 1px;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px
}

.conleft-list:before, .conleft-list:after {
	display: table;
	content: "";
	line-height: 0
}

.conleft-list:after {
	clear: both
}

.conleft-list li {
	width: 30%;
	float: left;
	padding-left: 77px;
	margin-right: 18px;
	height: 65px;
	margin-bottom: 12px
}

.conleft-item1 {
	background: url("images/in_ico.png") no-repeat left 0
}

.conleft-item1 div:hover,.conleft-item2 div:hover,.conleft-item3 div:hover,.conleft-item4 div:hover,.conleft-item5 div:hover,.conleft-item6 div:hover,.conleft-item7 div:hover{
    text-decoration: underline;
    cursor: pointer;
}

.conleft-item2 {
	background: url("images/in_ico.png") no-repeat left -288px
}

.conleft-item3 {
	background: url("images/in_ico.png") no-repeat left -479px
}

.conleft-item4 {
	background: url("images/in_ico.png") no-repeat left -96px
}

.conleft-item5 {
	background: url("images/in_ico.png") no-repeat left -383px
}

.conleft-item6 {
	background: url("images/in_ico.png") no-repeat left -575px
}

.conleft-item7 {
	background: url("images/in_ico.png") no-repeat left -192px
}

.conleft-item-tt {
	margin-bottom: 10px;
	font-size: 14px;
	font-weight: bold
}

.color-red {
    color: #ff5a60;
    font-weight: bold;
    font-size: 12px;
}
</style>
<div id="max${idSuffix}" class="fill">
	<!--head end-->
	<div class="contentwp">
		<div class="con-top">
			<div class="cont-right">
				<div class="con-title">
					快速入口
					<span></span>
				</div>
				<ul class="qkdor">
					<%--<li class="qk-ico1"><a href="javaScript:menuClick('投入品采购管理','/cfg-resource/coral40/views/selfdefine/zztrpcglb/MT_component.jsp?P_moduleId=838384964f3f4080014f3f4acfda0000&constructId=838384964f3f4080014f3f560ba6001d&bindingType=menu&menuId=838384964f3f4080014f3f5d9306002e&componentVersionId=838384964f3f4080014f3f560ba1001c&menuCode=ZZTRPCGGL&topComVersionId=838384964f3f4080014f3f560ba1001c&useNavigation=1')">投入品采购</a></li>--%>
					<%--<li class="qk-ico2"><a href="javaScript:menuClick('投入品领用管理','/cfg-resource/coral40/views/selfdefine/zztrplylb/MT_component.jsp?P_moduleId=8383845e4f490249014f49d38e56002e&constructId=8383845e4f490249014f49d60b380042&bindingType=menu&menuId=8383845e4f490249014f49d9bdd20053&componentVersionId=8383845e4f490249014f49d60b350041&menuCode=TRPLYGL&topComVersionId=8383845e4f490249014f49d60b350041&useNavigation=1')">投入品领用</a></li>--%>
					<%--<li class="qk-ico3"><a href="javaScript:menuClick('分拣管理','/cfg-resource/coral40/views/selfdefine/zzcsgl/MT_component.jsp?P_moduleId=402881f34f3ea42a014f3eaf331c0001&constructId=402881f34f3ea42a014f3eaf89b20009&bindingType=menu&menuId=402881f34f394c9f014f3aee80840045&componentVersionId=402881f34f3ea42a014f3eaf89a70008&menuCode=CSGL&topComVersionId=402881f34f3ea42a014f3eaf89a70008&useNavigation=1')">采收管理</a></li>--%>
					<%--<li class="qk-ico4"><a href="javaScript:menuClick('包装管理','/cfg-resource/coral40/views/selfdefine/zzbzgl/MT_component.jsp?P_moduleId=838384384f43967b014f44198415002a&constructId=838384384f43967b014f4419ef310032&bindingType=menu&menuId=838384384f43967b014f44643de7004a&componentVersionId=838384384f43967b014f4419ef2c0031&menuCode=BZGL&topComVersionId=838384384f43967b014f4419ef2c0031&useNavigation=1')">包装管理</a></li>--%>
					<%--<li class="qk-ico5"><a href="javaScript:menuClick('出场管理','/cfg-resource/coral40/views/selfdefine/zzccgl/MT_component.jsp?P_moduleId=838385d54f63be47014f63f4f4510008&constructId=838385d54f63be47014f63fcf12d0010&bindingType=menu&menuId=838385d54f63be47014f64006502002b&componentVersionId=838385d54f63be47014f63fcf115000f&menuCode=CCGL&topComVersionId=838385d54f63be47014f63fcf115000f&useNavigation=1')">出场管理</a></li>--%>
				</ul>
			</div>
			<div class="cont-left">
				<div class="con-title">
					农事项任务统计
					<span class="farming"><a href="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1')">更多</a></span>
				</div>
				<ul class="conleft-list">
					<li class="conleft-item1">
						<div class="conleft-item-tt" onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=1,1')">播种</div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=2,1')">已完成：<span class="color44" id="bz0${idSuffix}"></span></div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=3,1')">未完成：<span class="color-red" id="bz1${idSuffix}"></span></div>
					</li>
					<li class="conleft-item2">
						<div class="conleft-item-tt" onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=1,2')">灌溉</div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=2,2')">已完成：<span class="color44" id="gg0${idSuffix}"></span></div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=3,2')">未完成：<span class="color-red" id="gg1${idSuffix}"></span></div>
					</li>
					<li class="conleft-item3">
                        <div class="conleft-item-tt" onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=1,3')">施肥</div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=2,3')">已完成：<span class="color44" id="sf0${idSuffix}"></span></div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=3,3')">未完成：<span class="color-red" id="sf1${idSuffix}"></span></div>
					</li>
					<li class="conleft-item4">
						<div class="conleft-item-tt" onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=1,4')">用药</div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=2,4')">已完成：<span class="color44" id="yy0${idSuffix}"></span></div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=3,4')">未完成：<span class="color-red" id="yy1${idSuffix}"></span></div>
					</li>
					<li class="conleft-item5">
                        <div class="conleft-item-tt" onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=1,5')">除草</div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=2,5')">已完成：<span class="color44" id="cc0${idSuffix}"></span></div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=3,5')">未完成：<span class="color-red" id="cc1${idSuffix}"></span></div>
					</li>
					<li class="conleft-item6">
						<div class="conleft-item-tt" onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=1,6')">采收</div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=2,6')">已完成：<span class="color44" id="cs0${idSuffix}"></span></div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=3,6')">未完成：<span class="color-red" id="cs1${idSuffix}"></span></div>
					</li>
					<li class="conleft-item7">
						<div class="conleft-item-tt" onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=1,7')">其他</div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=2,7')">已完成：<span class="color44" id="qt0${idSuffix}"></span></div>
                        <div onclick="javaScript:menuClick('农事项任务跟踪管理','/cfg-resource/coral40/views/component/zznsxgz/zznsxgz.jsp?constructId=408a962b51a3564d0151a45dd6260004&bindingType=menu&menuId=408a962b51a3564d0151a46571aa0005&componentVersionId=408a962b51a3564d0151a45dd6230003&menuCode=NSXRWGZGL&topComVersionId=408a962b51a3564d0151a45dd6230003&useNavigation=1&P_columns=3,7')">未完成：<span class="color-red" id="qt1${idSuffix}"></span></div>
					</li>
				</ul>


			</div>
			<div class="cont-center czgfwp">
			  <!-- <div>采收批次：<a id="cspc_" href="javaScript:menuClick('分拣管理','/cfg-resource/coral40/views/selfdefine/zzcsgl/MT_component.jsp?P_moduleId=402881f34f3ea42a014f3eaf331c0001&constructId=402881f34f3ea42a014f3eaf89b20009&bindingType=menu&menuId=402881f34f394c9f014f3aee80840045&componentVersionId=402881f34f3ea42a014f3eaf89a70008&menuCode=CSGL&topComVersionId=402881f34f3ea42a014f3eaf89a70008&useNavigation=1')"></a></div>
				 <div>待包装：<a id="dbz_" href="javaScript:menuClick('包装管理','/cfg-resource/coral40/views/selfdefine/zzbzgl/MT_component.jsp?P_moduleId=838384384f43967b014f44198415002a&constructId=838384384f43967b014f4419ef310032&bindingType=menu&menuId=838384384f43967b014f44643de7004a&componentVersionId=838384384f43967b014f4419ef2c0031&menuCode=BZGL&topComVersionId=838384384f43967b014f4419ef2c0031&useNavigation=1')"></a></div> -->
				<div class="con-title">
					操作规范
					<span class="farming"><a href="javaScript:menuClick('操作规范管理','/cfg-resource/coral40/views/selfdefine/zzczgfgl/MT_component.jsp?P_moduleId=838385984f5d826a014f5db4a0ef000d&constructId=838385984f5d826a014f5dc8b1a00015&bindingType=menu&menuId=838385984f5d826a014f5ddf3f910035&componentVersionId=838385984f5d826a014f5dc8b1920014&menuCode=CZGFGL&topComVersionId=838385984f5d826a014f5dc8b1920014&useNavigation=1')">更多</a></span>
				</div>
				<div id="czgf">
					<ul>
						<!-- <li><a href="#czgf-1">种植</a></li> -->
						<%--<li><a href="#czgf-2">施肥</a></li>--%>
						<%--<li><a href="#czgf-3">采摘</a></li>--%>
					</ul>
					<div id="czgf-1" style="height:170px;">
						<ul>

						</ul>
					</div>
					<div id="czgf-2" >
						<ul>

						</ul>
					</div>
					<div id="czgf-3">
						<ul>

						</ul>
					</div>
				</div>

			</div>

		</div>
		<div class="con-bottom">

            <div class="conb-left">
                <div class="con-title">
                    销售订单提醒
                    <span class="farming"><a href="javaScript:menuClick('订单管理','/cfg-resource/coral40/views/selfdefine/zzddxxlb/MT_component.jsp?P_moduleId=402881fb4f660e33014f66156898000b&constructId=402881e64f63d537014f63e5df260008&bindingType=menu&menuId=402881f54f66aff8014f66be1f190002&componentVersionId=402881e64f63d537014f63e5df170007&topComVersionId=402881e64f63d537014f63e5df170007&useNavigation=1&P_columns=EQ_C_DDZT≡1')">更多</a></span>
                </div>

                <!--grid组件begin-->
                <div id="xsddDiv${idSuffix}" >
                </div>
                <!--grid组件end-->
<p>&nbsp</p>
			<div class="con-title">
					投入品到期提醒
					<span class="farming"><a href="javaScript:menuClick('投入品采购管理','/cfg-resource/coral40/views/selfdefine/zztrpcglb/MT_component.jsp?P_moduleId=838384964f3f4080014f3f4acfda0000&constructId=838384964f3f4080014f3f560ba6001d&bindingType=menu&menuId=838384964f3f4080014f3f5d9306002e&componentVersionId=838384964f3f4080014f3f560ba1001c&menuCode=ZZTRPCGGL&topComVersionId=838384964f3f4080014f3f560ba1001c&useNavigation=1&P_columns=LTE_C_DQR≡${dqr}')">更多</a></span>
				</div>

				<!-- grid组件begin -->
				<div id="gridDemo1" >
				</div>
				<!--grid组件end -->
            </div>
			<div class="conb-right">
				<div class="con-title span-combo">
					物联网信息
					<input id="DK${idSuffix}" name="DK">
					<input id="QY${idSuffix}" name="QY">
				</div>
				<div class="ww-wp">
					<%--<div class="env-title">地块/大棚：<span id="dkmc${idSuffix}"></span></div>--%>
					<div class="env-info">
						<span style="float:left;">传感器组编号：<span id="sbsbh${idSuffix}"></span></span><br>
						<span style="float:left;">监测时间：<span id="jcsj${idSuffix}"></span></span>
                    </div>
					<div style="clear:both">
					<ul class="ww-right" style="margin:10px 0 10px 10px;">
						<span class="lineleft">
                            <span class="environmenticon1"></span>
                        </span>
						<li>大气湿度：<span id="dqsd${idSuffix}"></span>%RH</li>
						<li>大气温度：<span id="dqwd${idSuffix}"></span>℃</li>
					</ul>
					<ul class="ww-right" style="margin:10px 0 10px 10px;">
						<span class="lineleft">
                            <span class="environmenticon2"></span>
                        </span>
						<li>土壤湿度：<span id="trsd${idSuffix}"></span>%RH</li>
						<li>土壤温度：<span id="trwd${idSuffix}"></span>℃</li>
					</ul>
                    </div>
					<div>
					<ul class="ww-right" style="margin:10px 0 10px 10px;">
						<span class="lineleft">
                            <span class="environmenticon3"></span>
                        </span>
						<li>光照强度：<span id="gzqd${idSuffix}"></span>lux</li>
					</ul>
					<ul class="ww-right" style="margin:10px 0 10px 10px;">
						<span class="lineleft">
                            <span class="environmenticon4"></span>
                        </span>
						<li>CO2浓度：<span id="eyht${idSuffix}"></span>ppm</li>
					</ul>
                    </div>
				</div>
			</div>
		</div>
	</div>

</div>
<div id="dialog${idSuffix}" style="display:none"></div>
</div>
<%--<script type="text/javascript" src="ces_coral/js/jquery.coral.min.js"></script>--%>
<script type="text/javascript" src="cfg-resource/coral40/views/component/zzgzt/js/header.js"></script>
<script type="text/javascript" src="cfg-resource/coral40/views/component/zzgzt/js/echarts/echarts.js"></script>
<script type="text/javascript">
	$.extend($.ns("namespaceId${idSuffix}"), {
		toolbarClick : function(event, button) {

		},
		toolbarClick1 : function(event, button) {
			if (button.id == "CFG_closeComponentDialog") {//关闭按钮事件
				if (window.CFG_clickCloseButton) {
					CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
				}
			}
		},
		/* 构件方法和回调函数 */
		getCategoryId : function() {
			alert("调用方法getCategoryId！");
			var o = {
		        status : true
		    };
		    o.categoryId = '1';
		    return o;
		},
		refreshGrid : function(cbParam1) {
			alert("调用回到函数refreshGrid(" + cbParam1 + ")");
		},
        setWlwxx:function(dataJson){//物联网信息添加
            if(dataJson != null && dataJson.cgqxx != null){
                $("#sbsbh${idSuffix}").html(dataJson.cgqxx.SBSBH);
                $("#jcsj${idSuffix}").html(dataJson.cgqxx.JQSJ);
                <%--$("#dkmc${idSuffix},#dkmc1${idSuffix}").html($("#DK${idSuffix}").combobox("getText"));--%>
                $("#dqsd${idSuffix}").html(dataJson.cgqxx.DQSD);
                $("#dqwd${idSuffix}").html(dataJson.cgqxx.DQWD);
                $("#trsd${idSuffix}").html(dataJson.cgqxx.TRSD);
                $("#trwd${idSuffix}").html(dataJson.cgqxx.TRWD);
                $("#gzqd${idSuffix}").html(dataJson.cgqxx.GZQD);
				$("#eyht${idSuffix}").html(dataJson.cgqxx.EYHTND);
            }else{
                $("#sbsbh${idSuffix}").html("");
                $("#jcsj${idSuffix}").html("");
                <%--$("#dkmc${idSuffix},#dkmc1${idSuffix}").html("");--%>
                $("#dqsd${idSuffix}").html("");
                $("#dqwd${idSuffix}").html("");
                $("#trsd${idSuffix}").html("");
                $("#trwd${idSuffix}").html("");
                $("#gzqd${idSuffix}").html("");
				$("#eyht${idSuffix}").html("");
            }
        },
		initFarmingInfo : function(farmingInfo){
			$.each(farmingInfo, function(e, data){
				if(data.ZT == "2"){
                    //已完成
                    if(data.LX == "1"){
                        $("#bz0${idSuffix}").html(data.CT);
                    }else if(data.LX == "2"){
                        $("#gg0${idSuffix}").html(data.CT);
                    }else if(data.LX == "3"){
                        $("#sf0${idSuffix}").html(data.CT);
                    }else if(data.LX == "4"){
                        $("#yy0${idSuffix}").html(data.CT);
                    }else if(data.LX == "5"){
                        $("#cc0${idSuffix}").html(data.CT);
                    }else if(data.LX == "6"){
                        $("#cs0${idSuffix}").html(data.CT);
                    }else if(data.LX == "7"){
                        $("#qt0${idSuffix}").html(data.CT);
                    }
                }else if(data.ZT == "3"){
                    //未完成
                    if(data.LX == "1"){
                        $("#bz1${idSuffix}").html(data.CT);
                    }else if(data.LX == "2"){
                        $("#gg1${idSuffix}").html(data.CT);
                    }else if(data.LX == "3"){
                        $("#sf1${idSuffix}").html(data.CT);
                    }else if(data.LX == "4"){
                        $("#yy1${idSuffix}").html(data.CT);
                    }else if(data.LX == "5"){
                        $("#cc1${idSuffix}").html(data.CT);
                    }else if(data.LX == "6"){
                        $("#cs1${idSuffix}").html(data.CT);
                    }else if(data.LX == "7"){
                        $("#qt1${idSuffix}").html(data.CT);
                    }
                }
			});
		}
	});
	$(function() {
		var configInfo = CFG_initConfigInfo({
			/** 页面名称 */
			'page' : 'page.jsp',
			/** 页面中的最大元素 */
			'maxEleInPage' : $('#max${idSuffix}'),
			/** 获取构件嵌入的区域 */
			'getEmbeddedZone' : function() {
				<%--return $('#layoutId${idSuffix}').layout('panel', 'center');--%>
			},
			/** 初始化预留区 */
			'initReserveZones' : function(configInfo) {
				<%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength"));--%>
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
                var dataJson = $.loadJson($.contextPath + "/zzgzt!searchQyxx.json");
				$("#QY${idSuffix}").combobox({//区域下拉框初始化
					value:"请选择区域",
					valueField:'QYBH',
					textField:'QYMC',
					data:dataJson.qyxx
				});
                if(dataJson.qyxx && dataJson.qyxx.length > 0){
				    $("#QY${idSuffix}").combobox("setValue",dataJson.qyxx[0].QYBH);
                }
				$("#DK${idSuffix}").combobox({//初始化地块信息下拉框
					value:"请选择地块",
					valueField:'DKBH',
					textField:'DKMC',
					data:dataJson.dkxx
				});
                if(dataJson.dkxx && dataJson.dkxx.length > 0){
				    $("#DK${idSuffix}").combobox("setValue",dataJson.dkxx[0].DKBH);
                    $.ns("namespaceId${idSuffix}").setWlwxx(dataJson);
                }
                $("#QY${idSuffix}").combobox("option","onChange",function(e,data){//区域信息添加onChange事件
                    var dkxxData = $.loadJson($.contextPath + "/zzgzt!searchDkxx.json?qybh="+data.value);
                    if(dkxxData.dkxx == null){
                        dkxxData.dkxx = [];
                    }
					//联动地块信息
                    $("#DK${idSuffix}").combobox("reload",dkxxData.dkxx);
                    $.ns("namespaceId${idSuffix}").setWlwxx(null);

                });
                $("#DK${idSuffix}").combobox("option","onChange",function(e,data){//地块信息添加onChange事件
                    var wlwxxData = $.loadJson($.contextPath + "/zzgzt!searchWlwxx.json?dkbh="+data.value);
                    if(wlwxxData.cgqxx == null){
                        $.ns("namespaceId${idSuffix}").setWlwxx(null);
                    }else{
                        $.ns("namespaceId${idSuffix}").setWlwxx(wlwxxData);
                    }
                });


				//tab操作规范
				/* $("#czgf").tabs(); */
				//grid表格begin
				var $grid = $("#xsddDiv${idSuffix}"),
						_colModel=[
							{name:"DDBH",sortable:true,width:146,align:"center",formatter:function(value, options, rowObj){
								return "<a  onclick='tiaozhuanXq(this)' myAttr1='"+rowObj.ID+"'>"+value+"</a>";
							}},
							{name:"KHMC",sortable:true,width:146,align:"center"},
							{name:"XDRQ",sortable:true,width:146,align:"center"},
							{name:"SHSJ",sortable:true,width:146,align:"center"}

						],

				_colNames=["订单编号","客户名称","下单时间","送货时间"],
						_setting={
							width:"auto",
							height:"auto",
							colModel:_colModel,
							colNames:_colNames,
							url:"zzgzt!searchDdxx.json"
						};
				//初始化订单信息列表
				$grid.grid(_setting);
				var $gridD = $("#gridDemo1"),
						_colModelD=[
							{name:"TRPMC",align:'left',sortable:true,width:146,align:"center",formatter:function(value, options, rowObj){
								return "<a href='javascript:showDetials(\""+rowObj.ID+"\")'>"+value+"</a>";
							}},
							{name:"RKLSH",align:'left',sortable:true,width:146,align:"center",formatter:function(value ,option,rowObj){
								return "<a href=\"javaScript:menuClick('投入品采购管理','/cfg-resource/coral40/views/selfdefine/zztrpcglb/MT_component.jsp?P_moduleId=838384964f3f4080014f3f4acfda0000&constructId=838384964f3f4080014f3f560ba6001d&bindingType=menu&menuId=838384964f3f4080014f3f5d9306002e&componentVersionId=838384964f3f4080014f3f560ba1001c&menuCode=ZZTRPCGGL&topComVersionId=838384964f3f4080014f3f560ba1001c&useNavigation=1&P_columns=LTE_C_DQR≡${dqr}')\">"+value+"</a>";
							}},
							{name:"SCRQ",align:'right',sortable:true,width:146,align:"center"},
							{name:"DQR",align:'right',sortable:true,width:146,align:"center"}
						],
						_colNamesD=["投入品名称","入库编号","生产日期","到期日"],
						_settingD={
							width:"auto",
							height:"auto",
							colModel:_colModelD,
							colNames:_colNamesD,
							url: $.contextPath+ "/zzgzt!searchTrp.json"
						};
				//初始化投入品信息列表
				$gridD.grid(_settingD);
				//grid表格end
				//遍历操作规范
				var czgfJson= $.loadJson($.contextPath + "/zzgzt!searchCzgf.json");
				var czgf=czgfJson.data;
				//初始化操作规范
				initCzgf(czgf);
				//初始化快捷菜单
				initQickMenu();
				//初始化采收批次、待包装
				var countData=  $.loadJson($.contextPath + "/trace!getCount.json?");
				$("#cspc_").html(countData.FJTJ==null?0:countData.FJTJ);
				$("#dbz_").html(countData.BZTJ==null?0:countData.BZTJ);
				//加载播种信息
				loadTabsData('scbz');

				//加载农事项任务统计
				var farmingInfo = $.loadJson($.contextPath + "/zzgzt!serchFarmingInfo.json");
				$.ns("namespaceId${idSuffix}").initFarmingInfo(farmingInfo);
				
			}
		});
		if (configInfo) {
			//alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue(configInfo, 'sysParam1')
			//		+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue(configInfo, 'selfParam1')
			//		+ "\n构件入参：\t" + "inputParamName_1=" + CFG_getInputParamValue(configInfo, 'inputParamId'));
			//alert(CFG_getInputParamValue(configInfo, 'inputParamId'));
			//alert(CFG_getInputParamValue(configInfo, 'inputParamId1'));
		}
	});

	//初始化操作规范
	function initCzgf(czgf){
		var $ul = $("#czgf-1 ul");
		for(var i = 0 ; i < czgf.length ; i++){//根据穿过来的数据
			var obj = czgf[i];
			//限制数据的长度
			if(obj.SCTPMC.length>20){
			var obj_data = obj.SCTPMC.split(".");
			obj.SCTPMC = obj_data[0].substring(0,4)+"..."+obj_data[0].substring(obj_data[0].length-7)+"."+obj_data[1];
			}
			$ul.append("<li><a href='javascript:download(\""+ $.contextPath+"/zzczgfgl!downLoad?id="+obj.ID+"\")'>"+obj.SCTPMC+"</a></li>");
		}
	}

	function initQickMenu(){//快捷菜单
		var data = $.loadJson($.contextPath + "/trace!getAllSystemByUser.json");
		var $ul = $(".qkdor");
        var systemId;
        var companyCode;
        $.each(data,function(index,sdata){
            if (sdata.sysName == "蔬菜种植"){
                systemId = sdata.sysCode;
                companyCode = sdata.companyCode;
            }
        });
		var menuJson= $.loadJson($.contextPath +"/menu/menu!loadAppQuickMenuForCoral40.json?systemId=" + systemId + "&companyCode=" + companyCode);
		var newJson = menuJson.replace("\"[","[").replace("]\"","]");
		var jsonO = eval("("+newJson+")"); 
		var len = jsonO.length;
		if(len > 5) len = 5;
		for (var i =0 ; i < len ; i++){
			var $starli ="<li class=\"qk-"+jsonO[i].code+"\">";
			var $conStr = "<a href=\"javaScript:menuClick('"+jsonO[i].name+"','"+jsonO[i].url+"')\">"+jsonO[i].name+"</a>";
			var $endli = "</li>";
			var apphtml= $starli+$conStr+$endli;
			$ul.append(apphtml);
		}
	}	
	function showDetials(id){//显示订单详细信息
//		var jqGlobal = document.body;
//		var dialogDivJQ = $("<div></div>").appendTo(jqGlobal);
//		new $.config.cform({
//			menuId : "-1",
//			componentVersionId : '-1',
//			moduleId : null,
//			tableId : '838384964f3e7a44014f3e8779f80000',
//			number  : 1,   // 当前表单在构件中的区域次序
//			isNested : false, // 是否为嵌入式表单(如果为false，则根据表单自身设置为显示)
//			dataId : id, // 数据ID(修改/查看记录)
//			isView : true,// 是否查看
//            global : jqGlobal,
//            sequence: 1, // 当前表单在构件中所有表单中的次序
//            title  : "投入品详情",
//            toolbar: false
//		}, dialogDivJQ);


		var configInfo = $('#max${idSuffix}').data("configInfo");
		var jq=$('#dialog${idSuffix}');
		jq.data("parentConfigInfo", configInfo);
		var selfUrl = jq.data("selfUrl");
		var url= $.contextPath+'/cfg-resource/coral40/views/component/zztrpcgglxq/zztrpcgglxq.jsp';
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
			jq.dialog({
				width:'80%',
				height:'80%',
				modal:true
			});
			$.parser.parse(jq);
			configInfo.childConfigInfo.CFG_bodyOnLoad(configInfo.childConfigInfo);
		});

	}
	function loadTabsData(typeId){
		var type=typeId;
		<%--$("#"+typeId+"Grid${idSuffix}").grid("option",{"url":$.contextPath + "/trace!getYgNsx.json?lx="+typeId});--%>
		var jsonData=$.loadJson($.contextPath + "/trace!getYgNsx.json?lx="+typeId);

		for(var i=0;i<jsonData.data.length;i++){

			var id=jsonData.data[i].ID;
			var oldNSZYXMC=jsonData.data[i].NSZYXMC;
			jsonData.data[i].NSZYXMC="<a  onclick='tiaozhuan(this)' myAttr1='"+id+"' myAttr2='"+type+"'>"+oldNSZYXMC+"</a>";
			var griddata=$("#"+typeId+"Grid${idSuffix}").grid('getRowData');
			var flag=true;
			for(var k=0;k<griddata.length;k++){
				if(griddata[k].ID==id){flag=false;break;}
			}
//			if(){}
			if(flag){$("#"+typeId+"Grid${idSuffix}").grid('addRowData',id,jsonData.data[i]);}


		}



	}


	function tiaozhuan(e){
		var id=$(e).attr('myAttr1');
		var type= $(e).attr('myAttr2');
		var ParentId=($.loadJson($.contextPath + "/trace!getParentId.json?lx="+type+"&id="+id)).PID;
		var configInfo = $('#max${idSuffix}').data("configInfo");
		var jq=$('#dialog${idSuffix}');
		jq.data("parentConfigInfo", configInfo);
        var selfUrl = jq.data("selfUrl");
		var url= $.contextPath+'/cfg-resource/coral40/views/component/sczzscdaxx/sczzscdaxx.jsp';
		$.ajax({
			type : "POST",
			url : url,
			data : {ID:ParentId,
				LX:type
			},
			dataType : "html",
			context : document.body,
			async : false
		}).done(function(html) {
			jq.empty();
			jq.append(html);
			jq.dialog({
				width:'80%',
				height:'80%',
				modal:true
			});
			$.parser.parse(jq);
			configInfo.childConfigInfo.CFG_bodyOnLoad(configInfo.childConfigInfo);
		});

	}

	function tiaozhuanXq(e){

		var id=$(e).attr('myAttr1');
		var configInfo = $('#max${idSuffix}').data("configInfo");
		//var jq=$('#max${idSuffix}');
		var jq = $('#dialog${idSuffix}');
		jq.data("parentConfigInfo", configInfo);
		var url= $.contextPath+'/cfg-resource/coral40/views/component/zzddxxxzxq/zzddxxxzxq.jsp';
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
			jq.dialog({

				width:'80%',
				height:'80%',
				modal:true
			})
			$.parser.parse(jq);
			configInfo.childConfigInfo.CFG_bodyOnLoad(configInfo.childConfigInfo);
		});

	}
	javascript:download();
</script>
