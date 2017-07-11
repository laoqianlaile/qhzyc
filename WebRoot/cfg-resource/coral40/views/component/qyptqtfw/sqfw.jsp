<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>申请服务</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <div id="apply" style="width:90%;margin:0 auto;margin-top:5px;">
    	<div id="fwmcDiv">服务名称：&nbsp;<input id="zzfwmc" name="zzfwmc" value="1"></div>
    	<div id="xqsmDiv" style="text-align: left;margin-top:5px;">需求说明：<ces:textarea id="xqsm" width="500" height="250" required="true"></ces:textarea></div>
    	<div style="color: gray;">• 变更需求提交后我们会主动致电与您详细沟通</div>
    </div>
    <div style="text-align: center;margin-top: 10px;">
    	<ces:button id="sub3" label="提交"></ces:button>
    	<ces:button id="cancel3" label="取消"></ces:button>
    </div>
  </body>
  <script type="text/javascript">
  	$(function(){
	  	//获取增值服务
		$.ajax({
	   		type:"post",
	   		url:$.contextPath+"/qyptqtfw!getZzfw.json",
	   		dataType:"json",
	   		success:function(data){
	   			$('#zzfwmc').combobox({
				    valueField:'value',
				    textField:'text',
				    data:data
				});
	   		}
	  	 });
  	})
  	$("#sub3").click(function(){
		if($.isEmptyObject($("#zzfwmc").combobox("getValue"))){
			$.message("请选择服务名称！");
			return;
		}
		if($.isEmptyObject($("#xqsm").val())){
			$.message("请输入申请说明！");
			return;
		}
  		$.ajax({
	   		type:"post",
	   		url:$.contextPath+"/qyptqtfw!sqZzfw.json",
	   		data:{sqsm:$("#xqsm").val(),fwmc:$("#zzfwmc").combobox("getValue")},
	   		dataType:"json",
	   		success:function(data){
	   			if(data=="success"){
	   				$('#dialog3').dialog("close");
	   				$.message("服务申请成功！");
	   			}else{
	   				$.message("服务申请失败！","error")
	   			}
	   		}
	  	 });
  	})
  	$("#cancel3").click(function(){
  		$('#dialog3').dialog("close");
  	})
  </script>
</html>
