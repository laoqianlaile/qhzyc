<%@page import="com.ces.config.utils.CfgCommonUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%if (CfgCommonUtil.isReleasedSystem()) { %>
<jsp:forward page="trace.jsp"></jsp:forward>
<%} else { %>
<jsp:forward page="app.jsp"></jsp:forward>
<%} %>
