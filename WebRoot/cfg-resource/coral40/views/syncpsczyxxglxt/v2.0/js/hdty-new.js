// JavaScript Document

function resetSize(){
	$(".warpper").css("height",0);
}
function sizeAdjust(){
	var height=$(window).height();
	$(".wrapper").css("height",height);
	//调整头部宽度
	var winWidth=$(window).width();//浏览器时下窗口区域的宽度
	var winheight=$(window).height();
	var footheight=$(".footer").height();
	var por=winWidth/1920;
	var headHeight=156*por;
	if(headHeight>=84){
		$(".head").css("height",headHeight);
	}else{
		$(".head").css("height",84);
	}
	
	//确定整个登录框的大小和位置
	$(".content").css("height",winheight-headHeight-footheight);
	var loginWidth=$(".content .login").width();
	$(".content .login").css("height",loginWidth*0.532);
	$(".content .login").css("margin-left",-loginWidth*0.5);
	$(".content .login").css("margin-top",-loginWidth*0.532*0.5);
	
	
	var inputWidth=$("input#number").width();
	$("input#number").css("height",inputWidth*0.1457);
	var inputWidth1=$("input#login-button").width();
	$("input#login-button").css("height",inputWidth1*0.2677);
	
	var buttonsHeight=$(".num-buttons").height();
	$(".num-buttons").css("margin-top",height*0.5-buttonsHeight*0.48);
	
	
}
$(function(){
	sizeAdjust();
})
$(window).resize(function(){
	resetSize();
	sizeAdjust();
});


//icheck插件中的jq部分
$(document).ready(function(){
  $('.skin-minimal input').iCheck({
	checkboxClass: 'icheckbox_minimal',
	increaseArea: '20%'
  });
});


//获取日期时间
function extra(x){
	if(x < 10)  
	{  
		return "0" + x;  
	}  
	else  
	{  
		return x;  
	}  
}
var time="";
function timeShow(){
	var date=new Date();
	var hour=date.getHours();
	var minute=date.getMinutes();
	var second=date.getSeconds();
	hour=extra(hour);
	minute=extra(minute);
	second=extra(second);
	var time=hour+':'+minute+':'+second;
	document.getElementById("time").innerHTML=time;
}
timeShow();
setInterval("timeShow()",1000);

var date=new Date();
/*
var week;
if(date.getDay()==0){week="周日";}
else if(date.getDay()==1){week="周一";}
else if(date.getDay()==2){week="周二";}
else if(date.getDay()==3){week="周三";}
else if(date.getDay()==4){week="周四";}
else if(date.getDay()==5){week="周五";}
else{week="周六";}
*/
var year=date.getFullYear();
var month=date.getMonth()+1;
var dt=date.getDate();
month=extra(month);
dt=extra(dt);
document.getElementById("date").innerHTML=year+"年"+month+"月"+dt+"日";



//键盘显示隐藏
var flag=0;
$(".keyboard").mouseenter(function(){
	flag=1;
});

var value=$("#number").val();
$("#number").focus(function(){
	$(".keyboard").animate({marginRight:'0'},200,function(){
		$(".arrow").show();
	});
});
$("#number").blur(function(){
	if(flag==0){
		$(".arrow").hide();
		$(".keyboard").animate({marginRight:'-23.06%'},200);
	}	
});


//键盘按下
$(".num-buttons .num-button").mousedown(function(){
	$(this).css({"background-color":"#f08300","border-color":"#b86400","color":"#c06800"});
	if($(this).index()!=($(".num-button").length-1)){
		var num=$(this).text();
		value=value+num;
		$("#number").val(value);
	}else{
		value=value.substring(0,value.length-1);
		$("#number").val(value); 
	}	
});
$(".num-buttons .num-button").mouseup(function(){
	$(this).css({"background-color":"#104400","border-color":"#206000","color":"#ffffff"});
	
});

$(".content, .head, .footer").click(function(){
	if(flag==1){
		$(".arrow").hide();
		$(".keyboard").animate({marginRight:'-23.06%'},200);
	}
	flag=0;	
});

