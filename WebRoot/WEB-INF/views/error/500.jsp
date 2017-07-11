<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true"  %>
{status:"error",message:"<%=exception.getMessage()%>",code:<%=response.getStatus()%>}