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
    
    <title>基本服务</title>
    
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
    <div id="menu1" style="margin-top:10px;">
       		<div id="btns1" style="display: inline;margin-left:10px;">
	       		<ces:button id="changeBtn" label="变更服务"></ces:button>
	       		<ces:button id="conBtn" label="续订服务"></ces:button>
       		</div>
       		<div id="aUrl1" style="display: inline;float: right;cursor: pointer;">
       			<a url="#" style="color:blue;margin-right:10px;">基本服务介绍></a>
       		</div>
       	</div>
       	<div id="con" style="width: 100%;margin-top:100px;">
       		<div id="fw" style="width:30%; margin:0 auto;">
       			<ul>
       				<li class="fwLi" id="fwmc"></li>
       				<li class="fwLi" id="fwnr"></li>
       				<li class="fwLi" id="qysj"></li>
       				<li class="fwLi" id="dqsj"></li>
       			</ul>
       		</div>
       	</div>
       	<div id="dialog1"></div>
  		<div id="dialog2"></div>
  </body>
</html>
<script type="text/javascript">
<!--

//-->
$(function() {
	//获取企业基本服务信息
   $.ajax({
   		type:"post",
   		url:$.contextPath+"/qyptqtfw!getBasicService.json",
   		dataType:"json",
   		success:function(data){
   			$("#fwmc").html("<a style='font-weight:bold'>服务名称：</a>"+data.FWMC);
   			$("#fwnr").html("<a style='font-weight:bold'>服务内容：</a>"+data.FWNR);
   			$("#qysj").html("<a style='font-weight:bold'>启用时间：</a>"+data.QYSJ);
   			$("#dqsj").html("<a style='font-weight:bold'>到期时间：</a>"+data.DQSJ);
   		}
   });
   $('#dialog1').dialog({
	    width:'60%',
	    height:400,
	    modal:true,
	    title:'变更服务',
	    autoOpen:false,
	    reloadOnOpen:true,
	    url:'<%=path%>/cfg-resource/coral40/views/component/qyptqtfw/bgfw.jsp',
	    onOpen:function(){
	    	$("#bgxq").val("");
	    }
	});
	$('#dialog2').dialog({
	    width:'60%',
	    height:500,
	    modal:true,
	    title:'续订服务',
	    autoOpen:false,
	    reloadOnOpen:true,
	    url:'<%=path%>/cfg-resource/coral40/views/component/qyptqtfw/xdfw.jsp'
		});
	});
 $("#changeBtn").click(function(){
   		$('#dialog1').dialog("open");
   });
 $("#conBtn").click(function(){
 		$('#dialog2').dialog("open");
 });
</script>
