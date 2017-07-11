<%@taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<%
String contextPath = request.getContextPath();
String cuiFolder   = contextPath + "/cfg-resource/coral40";
String coral4Path  = cuiFolder   + "/_cui_library";
    Object companyCode = request.getSession().getAttribute("_companyCode_");
    System.out.println("cui_lib:++_companyCode_"+companyCode);
%>


<!-- 德令哈-->

<%
    if("000007764".equals(companyCode)){
%>
<link href="<%=cuiFolder %>/cui/dlh_cui.css" rel="stylesheet"/>
<!-- 帝玛尔-->
<% }else if("000007884".equals(companyCode)){
%>
<link href="<%=cuiFolder %>/cui/dmr_cui.css" rel="stylesheet"/>
<!-- 康普农业-->
<% }else if("000007784".equals(companyCode)){
%>
<link href="<%=cuiFolder %>/cui/kpny_cui.css" rel="stylesheet"/>
<!-- 亿林-->
<% }else if("000007804".equals(companyCode)){
%>
<link href="<%=cuiFolder %>/cui/yl_cui.css" rel="stylesheet"/>
<!-- 源鑫堂-->
<% }else if("000007824".equals(companyCode)){
%>

<link href="<%=cuiFolder %>/cui/yxt_cui.css" rel="stylesheet"/>
<!-- 帝普生物-->
<% }else if("000007864".equals(companyCode)){
%>
<link href="<%=cuiFolder %>/cui/dpsw_cui.css" rel="stylesheet"/>
<!-- 新绿州-->
<% }else if("000007924".equals(companyCode)){
%>
<link href="<%=cuiFolder %>/cui/xlz_cui.css" rel="stylesheet"/>
<!-- 金诃-->
<% }else if("000007904".equals(companyCode)){
%>
<link href="<%=cuiFolder %>/cui/jk_cui.css" rel="stylesheet"/>
<!-- 康普生物-->
<% }else if("000007844".equals(companyCode)){
%>
<link href="<%=cuiFolder %>/cui/kpsw_cui.css" rel="stylesheet"/>
<!-- 其他-->
<%
    }else {
%>
<link href="<%=cuiFolder %>/cui/cui.css" rel="stylesheet"/>
<!-- 其他-->
<%
    }
%>
<script type="text/javascript" src="<%=cuiFolder %>/cui/cui-dev.js"></script>
<script type="text/javascript" src="<%=cuiFolder  %>/common/js/jquery.browser.js"></script>
<link  rel="stylesheet" href="<%=cuiFolder  %>/icon-font/style.css"/>

<!-- config resources -->
<script type="text/javascript" src="<%=coral4Path %>/ui/jquery.coral.config.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/highcharts.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/highcharts-3d.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/jquery.Huploadify.js"></script>

<!-- 配置平台自定义展现CSS -->
<link rel="stylesheet" type="text/css" href="<%=cuiFolder %>/common/css/show.module.css"/>
<!-- html5上传插件样式 -->
<link href="<%=cuiFolder %>/common/css/Huploadify.css" rel="stylesheet"/>

<script type="text/javascript">
<!--
(function() {
	// context path 
	$.contextPath = $.contextPath || "<%=contextPath %>";
	// coral4.0 main folder path
	$.cuiFolder   = $.cuiPath || "<%=cuiFolder %>";
	// name space
	$.ns = $.namespace = function() {
		var o, d;
        $.each(arguments, function(i, v) {
            d = v.split(".");
            o = window[d[0]] = window[d[0]] || {};
            $.each(d.slice(1), function(j, v2){
                o = o[v2] = o[v2] || {};
            });
        });
        return o;
	};
})();
//屏蔽SWT没有console对象的错误
if (typeof console == "undefined") {
    console = {
        log : function() {

        }
    }
}
(function($) {
	//backup jquery.ajax 
	var _ajax=$.ajax;
	// 
	function _direct(jqXHR, callback, scope, args) {
		var lt = null, ss = jqXHR.getResponseHeader("session-status");
		if ("timeout" === ss) {
			lt = jqXHR.getResponseHeader("location");
			window.location.replace(lt);
		} else if (callback) {
			callback.apply(scope, args);
		}
	}
	
	//override jquery.ajax function
	$.ajax = function(opt) {
		var error = opt.error, success = opt.success;
		// extend options
		var _opt = $.extend(opt, {
			success : function (data, status, jqXHR) {
				_direct(jqXHR, success, this, [data, status, jqXHR]);
			},
			error : function (jqXHR, status, e) {
				_direct(jqXHR, error, this, [jqXHR, status, e]);
			}
		});
		// add timestamp
		if (!isEmpty(_opt.url) && false !== _opt.timestamp) {
			if (_opt.url.indexOf("?") > 0) _opt.url = (_opt.url + "&___t=" + new Date().getTime());
			else _opt.url = (_opt.url + "?___t=" + new Date().getTime());
		}
		return _ajax(_opt);
	};
})(jQuery);

if (typeof contextPath === "undefined") window.contextPath = $.contextPath;
isSwt = typeof _window == "undefined" ? false : true;
isNewSwt = typeof _print == "undefined" ? false : true;
//-->
</script>
<!-- 配置平台公用JS -->
<script type="text/javascript" src="<%=cuiFolder %>/common/js/common.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/CFG_defaults.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/CFG_component.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/PinYin4Js.js"></script>
<!-- 配置平台自定义展现JS -->
<script type="text/javascript" src="<%=cuiFolder %>/common/js/selfdefine/config.common.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/selfdefine/jquery.config.core.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/selfdefine/jquery.config.component.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/selfdefine/jquery.config.clayout.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/selfdefine/jquery.config.ctbar.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/selfdefine/jquery.config.cform.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/selfdefine/jquery.config.cgrid.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/selfdefine/jquery.config.csearch.js"></script>
<script type="text/javascript" src="<%=cuiFolder %>/common/js/selfdefine/jquery.config.ctree.js"></script>
<!-- 组件库默认设置JS -->
<script type="text/javascript" src="<%=cuiFolder %>/common/js/jquery.config.js"></script>
<!-- 食品追溯saas 默认设置js -->
<script type="text/javascript" src="<%=cuiFolder %>/common/js/trace/trace.config.js"></script>

<!-- 馆室联合开发定义的 CSS和JS -->
<script type="text/javascript" src="<%=cuiFolder %>/common/js/msg.js"></script>
<!-- 图片预览功能 -->
<script type="text/javascript" src="<%=cuiFolder %>/common/themes/base/fancybox/jquery.fancybox.js"></script>
<link  rel="stylesheet" href="<%=cuiFolder  %>/common/themes/base/fancybox/jquery.fancybox.css"/>