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
	var por=winWidth/1440;
	var headHeight=90*por;
	if(headHeight>=84){
		$(".head").css("height",headHeight);
	}else{
		$(".head").css("height",84);
	}
	
	
	var imgWidth=$(".tab-info .img").width();
	$(".tab-info .img").css("height",imgWidth*1.21);
	/*var tipWidth=$(".tab-info .tips").width();
	$(".tab-info .tips").css("height",tipWidth*1);
	$(".tab-info .tips").css("line-height",tipWidth+'px');//不加后面的px字符串就不行！！！*/
	for(var i=0; i<$(".tab-info").length; i++){
		var tabInfo_height=$(".tab-info").eq(i).height();
		var a_height=$(".tab-info").eq(i).parents('a').height();
		$(".tab-info").eq(i).css('margin-top',(a_height-tabInfo_height)/2);
	}
	
	var active_li=$(".coral-tabs>.coral-tabs-nav>.coral-state-active").width();
	var fragment_li=$("#tabs1 #fragment-1").width();
	$('#tabs1').css("margin-left",(winWidth-active_li-fragment_li)/2);
	
	
	//调整按钮大小
	var buttonWidth=$('.button').width();
	$('.button').css({'height':buttonWidth*0.45,'line-height':buttonWidth*0.45+'px','margin-top':buttonWidth*0.08,'margin-bottom':buttonWidth*0.08});
	$('.buttons').css({'height':buttonWidth*0.60});
	
	//全选按钮大小
	var buttonWidth1=$('.all-select').width();
	$('.all-select').css({'height':buttonWidth1*0.5898,'line-height':buttonWidth1*0.5898+'px'});
	
	
	//确定内容表格的高度
	var heightTd=$(".left-td1").height();
	$(".unfold").css("height",heightTd);
	
	
	
	//让主题内容居中显示
	$("#fragment-1").css('height',$("#fragment-2").height());
	var marginTop=($("#fragment-2").height()-$("#tabs1 ul").height())*0.4;
	$("#tabs1 ul").css("margin-top",marginTop);	
	
	var marginTop1=($(window).height()-$(".head").height()-$("#tabs1").height())*0.5;
	if(marginTop1>10){
		$("#tabs1").css("margin-top",marginTop1);
	}else{
		$("#tabs1").css("margin-top",10);
	}
	
	
	
	$(".backOpr").css("width",$(".button").eq(0).width());
	$('.backOpr').css({'height':$('.backOpr').width()*0.45,'line-height':$('.backOpr').width()*0.45+'px'});
}
$(function(){
	sizeAdjust();
})
$(window).resize(function(){
	resetSize();
	sizeAdjust();
});

var flag=0;
var flag1=0;
//两个选项卡切换使用的代码
var flag=0;
var flag1=0;
$('#second-tab').click(function(){
	var first=$('#first-tab');
	var second=$('#second-tab');
	
	$(this).find('.img').css('background-image','url(images/complete-icon1.png)');
	$(this).find('.tab-name').removeClass('tab-name1').css("color","#114100");
	$(this).find('.tips').removeClass('tips1');
	flag=1;
	
	first.find('.tips').addClass('tips1');
	first.find('.img').css('background-image','url(images/task-icon2.png)');
	first.find('.tab-name').addClass('tab-name1');
	first.find('.tab-name').css('color','#67bb80');
	
	var winWidth=$(window).width();
	var winheight=$(window).height();
	if(winWidth<1280){
		if(winheight<768){
			$(this).children('.tab-info').css('margin-top',(292-$(this).children('.tab-info').height())/2);
			first.children('.tab-info').css('margin-top',(216-second.children('.tab-info').height())/2);
		}
		else if(winheight>=768&&winheight<=880){
			$(this).children('.tab-info').css('margin-top',(349-$(this).children('.tab-info').height())/2);
			first.children('.tab-info').css('margin-top',(256-second.children('.tab-info').height())/2);
		}
		else if(winheight>=880){
			$(this).children('.tab-info').css('margin-top',(372-$(this).children('.tab-info').height())/2);
			first.children('.tab-info').css('margin-top',(274-second.children('.tab-info').height())/2);
		}
		
	}else if(winWidth>=1280&&winWidth<1440){
		if(winheight<768){
			$(this).children('.tab-info').css('margin-top',(316-$(this).children('.tab-info').height())/2);
			first.children('.tab-info').css('margin-top',(230-second.children('.tab-info').height())/2);
		}
		else if(winheight>=768&&winheight<=880){
			$(this).children('.tab-info').css('margin-top',(339-$(this).children('.tab-info').height())/2);
			first.children('.tab-info').css('margin-top',(250-second.children('.tab-info').height())/2);
		}
		else if(winheight>=880){
			$(this).children('.tab-info').css('margin-top',(394-$(this).children('.tab-info').height())/2);
			first.children('.tab-info').css('margin-top',(277-second.children('.tab-info').height())/2);
		}
	}
	else if(winWidth>=1440&&winWidth<1920){
		if(winheight<768){
			$(this).children('.tab-info').css('margin-top',(305-$(this).children('.tab-info').height())/2);
			first.children('.tab-info').css('margin-top',(222-first.children('.tab-info').height())/2);
		}else if(winheight>=768&&winheight<=880){
			$(this).children('.tab-info').css('margin-top',(332-$(this).children('.tab-info').height())/2);
			first.children('.tab-info').css('margin-top',(243-first.children('.tab-info').height())/2);
		}
		else if(winheight>=860&&winheight<880){
			$(this).children('.tab-info').css('margin-top',(360-$(this).children('.tab-info').height())/2);
			first.children('.tab-info').css('margin-top',(264-first.children('.tab-info').height())/2);
		}
		else if(winheight>=880&&winheight<920){
			$(this).children('.tab-info').css('margin-top',(379-$(this).children('.tab-info').height())/2);
			first.children('.tab-info').css('margin-top',(277-first.children('.tab-info').height())/2);
		}
		else if(winheight>=920){
			$(this).children('.tab-info').css('margin-top',(394-$(this).children('.tab-info').height())/2);
			first.children('.tab-info').css('margin-top',(289-first.children('.tab-info').height())/2);
		}
	}else if(winWidth>=1920){
		$(this).children('.tab-info').css('margin-top',(394-$(this).children('.tab-info').height())/2);
		first.children('.tab-info').css('margin-top',(289-first.children('.tab-info').height())/2);
	}
	flag1=0;
	
	
});


$('#first-tab').click(function(){
	var first=$('#first-tab');
	var second=$('#second-tab');
	
	
	$(this).find('.img').css('background-image','url(images/task-icon1.png)');
	$(this).find('.tab-name').removeClass('tab-name1').css("color","#114100");
	$(this).find('.tips').removeClass('tips1');
	flag1=1;
	
	
	second.find('.img').css('background-image','url(images/complete-icon2.png)');
	second.find('.tab-name').css('color','#67bb80');
	second.find('.tab-name').addClass('tab-name1');
	second.find('.tips').addClass('tips1');
	
	var winWidth=$(window).width();
	var winheight=$(window).height();
	if(winWidth<1280){
		if(winheight<768){
			$(this).children('.tab-info').css('margin-top',(292-$(this).children('.tab-info').height())/2);
			second.children('.tab-info').css('margin-top',(216-second.children('.tab-info').height())/2);
		}
		else if(winheight>=768&&winheight<=880){
			$(this).children('.tab-info').css('margin-top',(349-$(this).children('.tab-info').height())/2);
			second.children('.tab-info').css('margin-top',(256-second.children('.tab-info').height())/2);
		}
		else if(winheight>=880){
			$(this).children('.tab-info').css('margin-top',(372-$(this).children('.tab-info').height())/2);
			second.children('.tab-info').css('margin-top',(274-second.children('.tab-info').height())/2);
		}
	}else if(winWidth>=1280&&winWidth<1440){
		if(winheight<768){
			$(this).children('.tab-info').css('margin-top',(316-$(this).children('.tab-info').height())/2);
			second.children('.tab-info').css('margin-top',(230-second.children('.tab-info').height())/2);
		}
		else if(winheight>=768&&winheight<=880){
			$(this).children('.tab-info').css('margin-top',(339-$(this).children('.tab-info').height())/2);
			second.children('.tab-info').css('margin-top',(250-second.children('.tab-info').height())/2);
		}
		else if(winheight>=880){
			$(this).children('.tab-info').css('margin-top',(394-$(this).children('.tab-info').height())/2);
			second.children('.tab-info').css('margin-top',(277-second.children('.tab-info').height())/2);
		}
	}
	else if(winWidth>=1440&&winWidth<1920){
		if(winheight<768){
			$(this).children('.tab-info').css('margin-top',(305-$(this).children('.tab-info').height())/2);
			second.children('.tab-info').css('margin-top',(222-first.children('.tab-info').height())/2);
		}else if(winheight>=768&&winheight<860){
			$(this).children('.tab-info').css('margin-top',(332-$(this).children('.tab-info').height())/2);
			second.children('.tab-info').css('margin-top',(243-first.children('.tab-info').height())/2);
		}
		else if(winheight>=860&&winheight<880){
			$(this).children('.tab-info').css('margin-top',(360-$(this).children('.tab-info').height())/2);
			second.children('.tab-info').css('margin-top',(264-first.children('.tab-info').height())/2);
		}
		else if(winheight>=880&&winheight<920){
			$(this).children('.tab-info').css('margin-top',(379-$(this).children('.tab-info').height())/2);
			second.children('.tab-info').css('margin-top',(277-first.children('.tab-info').height())/2);
		}
		else if(winheight>=920){
			$(this).children('.tab-info').css('margin-top',(394-$(this).children('.tab-info').height())/2);
			second.children('.tab-info').css('margin-top',(289-first.children('.tab-info').height())/2);
		}
	}else if(winWidth>=1920){
		$(this).children('.tab-info').css('margin-top',(394-$(this).children('.tab-info').height())/2);
		second.children('.tab-info').css('margin-top',(289-first.children('.tab-info').height())/2);
	}
	flag=0;
});

var selFlag=0;
$('.all-select').click(function(){
	if(selFlag==0){
		$(this).css({'background-color':'#114100','color':'#fff'});
		$("[name='checkbox']").iCheck("check");
		selFlag=1;
	}else{
		$(this).css({'background-color':'transparent','color':'#114100'});
		$("[name='checkbox']").iCheck("uncheck");
		selFlag=0;
	}
});

$(".backOpr").click(function(){
	$(this).parents("tr").hide().next("tr").hide().next("tr").hide();
});


$("table-body .fold1").children("span").addClass("hidden");
$(".showTr1").click(function(){
	$(".showTr1").children("td").removeClass("activeTd1");
	$(".showTr1").next("tr").children(".fold1").css("background-color","#114100");
	$(".showTr1").next("tr").find(".record1, .start-time1, .end-time1, .people1").css("display","none");
	$(this).children("td").addClass("activeTd1");
	$(this).find("[name='checkbox']").iCheck("check");
	$(this).next("tr").children(".fold1").css("background-color","#fff");
	$(this).next("tr").find(".record1, .start-time1, .end-time1, .people1").css("display","inline-block");
});