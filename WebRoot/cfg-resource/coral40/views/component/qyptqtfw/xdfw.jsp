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
    
    <title>变更基本服务</title>
    
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
    <div id="con" style="width:90%;margin:0 auto; margin-top:5px;">
    	<div id="dqjbfw2" style="text-align: left;"></div>
    	<div id="xdsc">选择续订时长：&nbsp;<input id="xdsj" name="xdsj" value="1"></div>
    	<div id="xdxqsm" style="text-align: left;">续订需求说明：<ces:textarea id="xdxq" width="500" height="250" required="true"></ces:textarea></div>
    	<div style="color: gray;">• 变更需求提交后我们会主动致电与您详细沟通</div>
    </div>
    <div style="text-align: center;margin-top: 10px;">
    	<ces:button id="sub2" label="提交"></ces:button>
    	<ces:button id="cancel2" label="取消"></ces:button>
    </div>
  </body>
  <script type="text/javascript">
  	$(function(){
	  	//获取基本服务名称
		$.ajax({
	   		type:"post",
	   		url:$.contextPath+"/qyptqtfw!getJbfwmc.json",
	   		dataType:"json",
	   		success:function(data){
	   			$("#dqjbfw2").html("当前基本服务："+data);
	   		}
	  	 });
	  	//获取续订时长
		$.ajax({
	   		type:"post",
	   		url:$.contextPath+"/qyptqtfw!getJbfwscCode.json",
	   		dataType:"json",
	   		success:function(data){
	   			$('#xdsj').combobox({
				    valueField:'value',
				    textField:'text',
				    data:data
				});
	   		}
	  	 });
  	})
  	$("#sub2").click(function(){
		if($.isEmptyObject($("#xdxq").val())){
			$.message("请输入续订需求说明！");
			return;
		}
		if($.isEmptyObject($("#xdsj").combobox("getValue"))){
			$.message("请选择续订时长！");
			return;
		}
  		$.ajax({
	   		type:"post",
	   		url:$.contextPath+"/qyptqtfw!conJbfw.json",
	   		data:{sqsm:$("#xdxq").val(),xdsj:$("#xdsj").combobox("getValue")},
	   		dataType:"json",
	   		success:function(data){
	   			if(data=="success"){
	   				$('#dialog2').dialog("close");
	   				$.message("服务续订申请成功！");
	   			}else{
	   				$.message("服务续订申请失败！","error")
	   			}
	   		}
	  	 });
  	})
  	$("#cancel2").click(function(){
  		$('#dialog2').dialog("close");
  	})
  </script>
</html>
