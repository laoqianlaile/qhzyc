<%@ page import="com.ces.config.utils.SystemParameterUtil"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.ces.xarch.core.web.listener.XarchListener" %>
<%@ page import="com.ces.component.trace.service.TraceService" %>
<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%

    Map<String, String> data = XarchListener.getBean(TraceService.class).getAllSystemByUser();
    String systemId = data.get("sysCode");
    String companyCode = data.get("companyCode");
    request.getSession().setAttribute("_companyCode_", companyCode);
    request.getSession().setAttribute("_sysCode_", systemId);
    System.out.println("request = companyCode: "+companyCode);
%>

<%
    if (data.containsKey("forward")) {//监管子系统
//亿林-->海王,源鑫堂-->美东，康普农业-->金泰
%>
<jsp:forward page="trace.jsp"></jsp:forward>
<%
} else if ("838381e14e948937014e957e93400000".equals(systemId)){//监管子系统

%>
<jsp:forward page="WEB-INF/layouts/trace_csgl_menu.jsp"></jsp:forward>
<%
}else if("000007824".equals(companyCode))
{
%>
<jsp:forward page="WEB-INF/layouts/trace_md_menu.jsp"></jsp:forward>
<%
}else if("000007804".equals(companyCode))
{
%>
<jsp:forward page="WEB-INF/layouts/trace_hwyy_menu.jsp"></jsp:forward>
<%
}else if("000007784".equals(companyCode))
{
%>
<jsp:forward page="WEB-INF/layouts/trace_jt_menu.jsp"></jsp:forward>
<%
} else if ("000007844".equals(companyCode)){
%>
<jsp:forward page="WEB-INF/layouts/trace_kpsw_menu.jsp"></jsp:forward>
<%--<jsp:forward page="WEB-INF/layouts/trace_zyc_menu.jsp"></jsp:forward>--%>
<%
}else if("000007864".equals(companyCode))
{
%>
<jsp:forward page="WEB-INF/layouts/trace_dpsw_menu.jsp"></jsp:forward>
<%
}else if("000007884".equals(companyCode))
{
%>
<jsp:forward page="WEB-INF/layouts/trace_dmr_menu.jsp"></jsp:forward>
<%
}else if("000007904".equals(companyCode))
{
%>
<jsp:forward page="WEB-INF/layouts/trace_jk_menu.jsp"></jsp:forward>
<%
}else if("000000824".equals(companyCode))
{
%>
<jsp:forward page="WEB-INF/layouts/trace_zyc_menu.jsp"></jsp:forward>
<%
}else if("8a8a4aa05c1e9b2f015c1eafb78d0000".equals("systemId"))
{
%>
<jsp:forward page="WEB-INF/layouts/trace_zyy_menu.jsp"></jsp:forward>
<%
}else if("000007764".equals(companyCode))
{
%>
<jsp:forward page="WEB-INF/layouts/trace_zyc_menu.jsp"></jsp:forward>
<%
}else if("000007924".equals(companyCode))
{
%>
<jsp:forward page="WEB-INF/layouts/trace_xlz_menu.jsp"></jsp:forward>
<%
}else if("000000849".equals(companyCode))
{
%>
<jsp:forward page="WEB-INF/layouts/trace_zyy_menu.jsp"></jsp:forward>
<%
}else if("000007944".equals(companyCode))
{
%>
<jsp:forward page="WEB-INF/layouts/trace_zz_menu.jsp"></jsp:forward>


<%--<%
} else if ("402880f1552361370155239abe880000".equals(systemId)){
%>


&lt;%&ndash;<jsp:forward page="WEB-INF/layouts/trace_zyy_menu.jsp"></jsp:forward>&ndash;%&gt;
<jsp:forward page="WEB-INF/layouts/trace_zyy_menu.jsp"></jsp:forward>--%>
<%--<%
}else if ("402881024cf8b745014cf8d0e5d70026".equals("systemId")) {//种植子系统
%>--%>
<%--<jsp:forward page="WEB-INF/layouts/trace_zz_menu.jsp"></jsp:forward>
<%
} else if ("402881f44fb51171014fb517fe100000".equals("systemId")) {//质量追踪子系统
%>--%>
<%--<jsp:forward page="WEB-INF/layouts/trace_qygl_menu.jsp"></jsp:forward>--%>
<%
} else if ("1".equals((companyCode))) {
%>
<jsp:forward page="WEB-INF/layouts/app_dhtmlx.jsp"></jsp:forward>
<%--<%
} else if ("coral40".equals(SystemParameterUtil.getInstance().getReleaseSystemUI())) {
    if ("0".equals(SystemParameterUtil.getInstance().getSystemParamValue("菜单形式"))) {
%>
<jsp:forward page="WEB-INF/layouts/trace_zz_menu.jsp"></jsp:forward>
<%
} else if ("1".equals(SystemParameterUtil.getInstance().getSystemParamValue("菜单形式"))) {
%>
<jsp:forward page="WEB-INF/layouts/app_coral40_vertical_menu.jsp"></jsp:forward>--%>
<%

    }
%>
