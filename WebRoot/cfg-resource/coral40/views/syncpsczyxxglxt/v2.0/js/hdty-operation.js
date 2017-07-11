// JavaScript Document
var blur1=0;//判断是否已遮罩的变量
var bgObj;
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
	if(headHeight>=64){
		$(".head").css("height",headHeight);
	}else{
		$(".head").css("height",64);
	}
	
	//.tab-info是选项卡内容部分，以下是调整放图片容器的高度
	var imgWidth=$(".tab-info .img").width();
	$(".tab-info .img").css("height",imgWidth*1.21);
	
	
	/*var tipWidth=$(".tab-info .tips").width();
	$(".tab-info .tips").css("height",tipWidth*1);
	$(".tab-info .tips").css("line-height",tipWidth+'px');//不加后面的px字符串就不行！！！*/
	
	
	//调整选项卡内容部分的垂直位置，使其垂直居中！
	for(var i=0; i<$(".tab-info").length; i++){
		var tabInfo_height=$(".tab-info").eq(i).height();
		var a_height=$(".tab-info").eq(i).parents('a').height();
		$(".tab-info").eq(i).css('margin-top',(a_height-tabInfo_height)/2);
	}
	
	//使整个内容在水平居中！
	var active_li=$(".coral-tabs>.coral-tabs-nav>.coral-state-active").width();
	var fragment_li=$("#tabs1 #fragment-2").width();
	$('#tabs1').css("margin-left",(winWidth-active_li-fragment_li)/2);
	
	
	//调整表格头部按钮大小
	var buttonWidth=$('.button').width();
	$('.button').css({'height':buttonWidth*0.45,'line-height':buttonWidth*0.45+'px','margin-top':buttonWidth*0.08,'margin-bottom':buttonWidth*0.08});
	$('.buttons').css({'height':buttonWidth*0.60});
	
	//全选按钮大小
	var buttonWidth1=$('.all-select').width();
	$('.all-select').css({'height':buttonWidth1*0.5898,'line-height':buttonWidth1*0.5898+'px'});



	/*var marginTop=($("#fragment-1").height()-$("#tabs1 ul").height())*0.4;
	console.log($("#fragment-1").outerHeight());
	$("#tabs1 ul").css("margin-top",marginTop);




	var marginTop1=($(window).height()-$(".head").height()-$("#tabs1").height())*0.5;
	console.log($(window).height());
	console.log($(".head").height());
	console.log($("#tabs1").height());*/
	/*if(marginTop1>10){
		$("#tabs1").css("margin-top",marginTop1);
	}else{
		$("#tabs1").css("margin-top",10);
	}*/

	

	
	
	
	
	var width=$(".alertBox").width();
	var height=width*266/622;
	$(".alertBox").css("height",height);
	$(".alertBox").css("margin-left",-width*0.5);
	$(".alertBox").css("margin-top",-height*0.5);
	
	//判断是否在遮罩状态，是的话调整遮罩区域的大小！
	if(blur1==1){
		document.body.removeChild(bgObj);
		bgBlur();
	}
	
	var widthAdd=$(".addBox").width();
	var heightAdd=widthAdd*574/844;
	$(".addBox").css("height",heightAdd);
	$(".addBox").css("margin-left",-widthAdd*0.5);
	$(".addBox").css("margin-top",-heightAdd*0.5);
	
	
	var cWidth=$(".cancelAdd").width();
	$(".cancelAdd, .confirmAdd").css("height",cWidth*0.5);
	$(".cancelAdd, .confirmAdd").css("line-height",cWidth*0.5+"px");
	$(".cancelAdd, .confirmAdd").css("margin-top",cWidth*0.1+"px");
	$(".cancelAdd, .confirmAdd").css("margin-bottom",cWidth*0.1+"px");
	
	
	$(".operation").css("width",$(".button").eq(0).width());
	$('.operation').css({'height':$('.operation').width()*0.45,'line-height':$('.operation').width()*0.45+'px'});
	$(".state").css("width",$(".button").eq(0).width());
	$('.state').css({'height':$('.state').width()*0.45,'line-height':$('.state').width()*0.45+'px'});
	
	
	
	
	
}
$(function(){
	sizeAdjust();
	//size();
	/*var td_height=$(".right-td1").height();
	$(".deleteItem").css("height",td_height);
	var deleteItem=$(".deleteItem").height();
	var img_height=$(".deleteItem img").height();
	$(".deleteItem img").css("margin-top",(deleteItem-img_height)/2);*/
})
$(window).resize(function(){
	resetSize();
	sizeAdjust();
	//size();
});


$(document).ready(function(){
	setTimeout(function(){
		$('#scrollbar1').tinyscrollbar();
	},100);//设置延时，作用是等左边数据加载完再进行滚动条的初始化，以免出现滚不到底的现象
	
});
//icheck插件中的jq部分
$(document).ready(function(){
  $('.skin-minimal input').iCheck({
	checkboxClass: 'icheckbox_minimal',
	increaseArea: '20%'
  });
});







//选项卡组件初始化代码
$('#tabs1').tabs({
	//heightStyle: 'fill',
	cls:'coral-tabs-right'
});


//联动
var department="区域（全部）";
var name="地块（全部）";
var works="作业类型（全部）";
function getData1(e, ui){
	//if ( ui.item.text == "A区" ) {
	//	$name.combobox("reload", "json/combobox_data1.json");
	//	$name.combobox("setValue", "1");
	//}
	//else if( ui.item.text == "B区" ) {
	//	$name.combobox("reload", "json/combobox_data2.json");
	//	$name.combobox("setValue", "1");//选择默认值的序号
	//}
	//else{
	//	$name.combobox("reload", "json/combobox_data.json");
	//	$name.combobox("setValue", "1");
	//}
	department=(ui.item.text);
	name="地块（全部）";
	selectFun();
}
function getData2(e, ui){
	name=(ui.item.text);
	selectFun();
}
function getData3(e, ui){
	works=(ui.item.text);
	selectFun();
}

function selectFun(){
	var $dpm=$(".department");
	var $n=$(".name");
	var $work=$(".work");
	if(department=="区域（全部）"){
		if(name=="地块（全部）"){
			if(works=="作业类型（全部）"){
				for(var i=0; i<$dpm.length; i++){
					$work.eq(i).parents("tr").show().next().show().next().show();
				}
			}
			else{ 
				for(var i=0; i<$dpm.length; i++){
					if($work.eq(i).children("span").text()!=works){
						$work.eq(i).parents("tr").hide().next().hide().next().hide();
					}else{
						$work.eq(i).parents("tr").show().next().show().next().show();
					}
				}
			}
		}else{
			if(works=="作业类型（全部）"){
				for(var i=0; i<$dpm.length; i++){
					if($n.eq(i).text()!=name){
						$n.eq(i).parents("tr").hide().next().hide().next().hide();
					}else{
						$n.eq(i).parents("tr").show().next().show().next().show();
					}
				}
			}
			else{ 
				for(var i=0; i<$dpm.length; i++){
					if($work.eq(i).children("span").text()!=works||$n.eq(i).text()!=name){
						$work.eq(i).parents("tr").hide().next().hide().next().hide();
					}else{
						$work.eq(i).parents("tr").show().next().show().next().show();
					}
				}
			}
		}
	}else{
		if(name=="地块（全部）"){
			if(works=="作业类型（全部）"){
				for(var i=0; i<$dpm.length; i++){
					if($dpm.eq(i).text()!=department){
						$dpm.eq(i).parents("tr").hide().next().hide().next().hide();
					}else{
						$dpm.eq(i).parents("tr").show().next().show().next().show();
					}
				}
			}
			else{ 
				for(var i=0; i<$dpm.length; i++){
					if($dpm.eq(i).text()!=department||$work.eq(i).children("span").text()!=works){
						$work.eq(i).parents("tr").hide().next().hide().next().hide();
					}else{
						$work.eq(i).parents("tr").show().next().show().next().show();
					}
				}
			}
		}else{
			if(works=="作业类型（全部）"){
				for(var i=0; i<$dpm.length; i++){
					if($n.eq(i).text()!=name||$dpm.eq(i).text()!=department){
						$n.eq(i).parents("tr").hide().next().hide().next().hide();
					}else{
						$n.eq(i).parents("tr").show().next().show().next().show();
					}
				}
			}
			else{ 
				for(var i=0; i<$dpm.length; i++){
					if($work.eq(i).children("span").text()!=works||$n.eq(i).text()!=name||$dpm.eq(i).text()!=department){
						$work.eq(i).parents("tr").hide().next().hide().next().hide();
					}else{
						$work.eq(i).parents("tr").show().next().show().next().show();
					}
				}
			}
		}
	}
}
/*function onChange(e, ui){
	if ( ui.value == "sh" ) {
		$name.combobox("reload", "json/combobox_data1.json");
		$name.combobox("setValue", "1");
	} 
	else if( ui.value == "bj" ) {
		$name.combobox("reload", "json/combobox_data2.json");
		$name.combobox("setValue", "1");//选择默认值的序号
	}
	else if( ui.value == "all" ) {
		$name.combobox("reload", "json/combobox_data.json");
		$name.combobox("setValue", "1");//选择默认值的序号
	}
}*/
function styleChange(e,ui){
	var $component = $(this).combobox("component");//将整个下拉框封装成jQ对象并返回，这样就可以用jQ的常规方法来操作它了！
	$component.siblings(".bg").show();
	$(".areaBlock .coral-combo-content .coral-combobox-item").eq(0).text("全部");
	$(".areaBlockAdd .coral-combo-content .coral-combobox-item").eq(0).text("全部");	
	$(".workBlock .coral-combo-content .coral-combobox-item").eq(0).html("<div class='itemIcon opeicon1'></div>全部");
	$(".workBlockAdd .coral-combo-content .coral-combobox-item").eq(0).html("<div class='itemIcon opeicon1'></div>全部");	
}
function styleChange1(e,ui){
	var $component = $(this).combobox("component");//将整个下拉框封装成jQ对象并返回，这样就可以用jQ的常规方法来操作它了！
	$component.siblings(".bg").hide();	
}






function formatter(item){
	return "<div class='itemIcon "+item.icon+"'></div>"+item.text;//itemIcon 里面定义一张背景图,item.icon定义图标的positon 

}




//新增框里面的二级联动
function getDataAdd(e, ui){
	if ( ui.item.text == "A区" ) {
		$name2.combobox("reload", "json/combobox_data1.json");
		$name2.combobox("setValue", "1");
	}
	else if( ui.item.text == "B区" ) {
		$name2.combobox("reload", "json/combobox_data2.json");
		$name2.combobox("setValue", "1");//选择默认值的序号
	}
	else{
		$name2.combobox("reload", "json/combobox_data.json");
		$name2.combobox("setValue", "1");
	}
}


$('#combobox3').combobox({
	valueField:'value',
	textField:'text',
	data:[{
			"value":"all",
			"text":"作业类型（全部）",
			"selected":true,
			icon:"opeicon0"
		},{
			"value":"bz",
			"text":"播种",
			icon:"opeicon1"
		},{
			"value":"sf",
			"text":"施肥",
			icon:"opeicon2"
		},{
			"value":"gg",
			"text":"灌溉",
			icon:"opeicon3"
		},{
			"value":"cc",
			"text":"锄草",
			icon:"opeicon4"
		},{
			"value":"yy",
			"text":"用药",
			icon:"opeicon5"
		},{
			"value":"qt",
			"text":"其它",
			icon:"opeicon6"
		},{
			"value":"cs",
			"text":"采收",
			icon:"opeicon7"
		}],
	onShowPanel:"styleChange",
	onHidePanel:"styleChange1",
	formatter: "formatter",
	panelWidth:"364px",
	panelComponentCls:"workBlockAdd"
});


//两个选项卡切换使用的代码
var flag=0;
var flag1=0;
//$('#second-tab').click(function(){
//	var first=$('#first-tab');
//	var second=$('#second-tab');
//
//	$(this).find('.img').css('background-image','url(images/complete-icon1.png)');
//	$(this).find('.tab-name').removeClass('tab-name1').css("color","#114100");
//	$(this).find('.tips').removeClass('tips1');
//	flag=1;
//
//	first.find('.tips').addClass('tips1');
//	first.find('.img').css('background-image','url(images/task-icon2.png)');
//	first.find('.tab-name').addClass('tab-name1');
//	first.find('.tab-name').css('color','#67bb80');
//
//	var winWidth=$(window).width();
//	var winheight=$(window).height();
//	if(winWidth<1280){
//		if(winheight<768){
//			$(this).children('.tab-info').css('margin-top',(292-$(this).children('.tab-info').height())/2);
//			first.children('.tab-info').css('margin-top',(216-second.children('.tab-info').height())/2);
//		}
//		else if(winheight>=768&&winheight<=880){
//			$(this).children('.tab-info').css('margin-top',(349-$(this).children('.tab-info').height())/2);
//			first.children('.tab-info').css('margin-top',(256-second.children('.tab-info').height())/2);
//		}
//		else if(winheight>=880){
//			$(this).children('.tab-info').css('margin-top',(372-$(this).children('.tab-info').height())/2);
//			first.children('.tab-info').css('margin-top',(274-second.children('.tab-info').height())/2);
//		}
//
//	}else if(winWidth>=1280&&winWidth<1440){
//		if(winheight<768){
//			$(this).children('.tab-info').css('margin-top',(292-$(this).children('.tab-info').height())/2);
//			first.children('.tab-info').css('margin-top',(216-second.children('.tab-info').height())/2);
//		}
//		else if(winheight>=768&&winheight<=880){
//			$(this).children('.tab-info').css('margin-top',(339-$(this).children('.tab-info').height())/2);
//			first.children('.tab-info').css('margin-top',(250-second.children('.tab-info').height())/2);
//		}
//		else if(winheight>=880){
//			$(this).children('.tab-info').css('margin-top',(394-$(this).children('.tab-info').height())/2);
//			first.children('.tab-info').css('margin-top',(277-second.children('.tab-info').height())/2);
//		}
//	}
//	else if(winWidth>=1440&&winWidth<1920){
//		if(winheight<768){
//			$(this).children('.tab-info').css('margin-top',(305-$(this).children('.tab-info').height())/2);
//			first.children('.tab-info').css('margin-top',(222-first.children('.tab-info').height())/2);
//		}else if(winheight>=768&&winheight<=880){
//			$(this).children('.tab-info').css('margin-top',(332-$(this).children('.tab-info').height())/2);
//			first.children('.tab-info').css('margin-top',(243-first.children('.tab-info').height())/2);
//		}
//		else if(winheight>=860&&winheight<880){
//			$(this).children('.tab-info').css('margin-top',(360-$(this).children('.tab-info').height())/2);
//			first.children('.tab-info').css('margin-top',(264-first.children('.tab-info').height())/2);
//		}
//		else if(winheight>=880&&winheight<920){
//			$(this).children('.tab-info').css('margin-top',(379-$(this).children('.tab-info').height())/2);
//			first.children('.tab-info').css('margin-top',(277-first.children('.tab-info').height())/2);
//		}
//		else if(winheight>=920){
//			$(this).children('.tab-info').css('margin-top',(394-$(this).children('.tab-info').height())/2);
//			first.children('.tab-info').css('margin-top',(289-first.children('.tab-info').height())/2);
//		}
//	}else if(winWidth>=1920){
//		$(this).children('.tab-info').css('margin-top',(394-$(this).children('.tab-info').height())/2);
//		first.children('.tab-info').css('margin-top',(289-first.children('.tab-info').height())/2);
//	}
//	flag1=0;
//
//
//});

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
			$(this).children('.tab-info').css('margin-top',(292-$(this).children('.tab-info').height())/2);
			second.children('.tab-info').css('margin-top',(216-second.children('.tab-info').height())/2);
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

//全选按钮
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


//实现遮罩
//function bgBlur(){
//	var sWidth,sHeight;
//	sWidth=document.body.scrollWidth;//获得的body正文全文宽
//	sHeight=document.body.scrollHeight ;//获得的body正文全文高
//	bgObj=document.createElement("div");
//	bgObj.setAttribute('id','bgDiv');
//	bgObj.style.position="absolute";
//	bgObj.style.top="0";
//	bgObj.style.background="#000";
//	bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
//	bgObj.style.opacity="0.3";
//	bgObj.style.left="0";
//	bgObj.style.width=sWidth + "px";
//	bgObj.style.height=sHeight + "px";
//	bgObj.style.zIndex = "10000";
//	document.body.appendChild(bgObj);
//	blur1=1;
//}

//警告框
var width=$(".alertBox").width();
var height=width*266/622;
$(".alertBox").css("height",height);
$(".alertBox").css("margin-left",-width*0.5);
$(".alertBox").css("margin-top",-height*0.5);


$(".head img").click(function(){
	bgBlur();
	$(".text").html("是否退出系统");
	$(".alertBox").show();
});

$(".selection .cancel,.selection .confirm").click(function(){
	document.body.removeChild(bgObj);
	$(".alertBox").hide();
	blur1=0;
});

////点击新增按钮
//$("#add-button").click(function(){
//	bgBlur();
//	$(".addBox").show();
//	setTimeout(function(){
//		$('#scrollbar3').tinyscrollbar();
//		var cWidth=$(".cancelAdd").width();
//		$(".cancelAdd, .confirmAdd").css("height",cWidth*0.5);
//		$(".cancelAdd, .confirmAdd").css("line-height",cWidth*0.5+"px");
//		$(".cancelAdd, .confirmAdd").css("margin-top",cWidth*0.1+"px");
//		$(".cancelAdd, .confirmAdd").css("margin-bottom",cWidth*0.1+"px");
//	},100);
//
//});

//$(".addBox-head img").click(function(){
//	document.body.removeChild(bgObj);
//	$(".wrapper").css("webkitFilter","none");
//	$(".wrapper").css("mozFilter","none");
//	$(".wrapper").css("msFilter","none");
//	$(".wrapper").css("oFilter","none");
//	$(".wrapper").css("filter","none");
//	$(".addBox").css("display","none");
//	blur1=0;
//});

//点击条目中按钮
$(".delete-able").click(function(){
	event.stopPropagation();
	$(this).parents("tr").addClass("hidden").next("tr").addClass("hidden").next("tr").addClass("hidden");
});
$(".state").click(function(){
	event.stopPropagation();
	$(this).addClass("hidden").next(".operations").removeClass("hidden");
});
$(".back").click(function(){
	event.stopPropagation();
	$(this).parent(".operations").addClass("hidden").prev(".state").removeClass("hidden");
});

$(".complete").click(function(){
	event.stopPropagation();
	$(this).parents("tr").addClass("hidden").next("tr").addClass("hidden").next("tr").addClass("hidden");
});

$("table-body .fold").children("span").addClass("hidden");
$(".showTr").click(function(){
	$(".showTr").children("td").removeClass("activeTd");
	$(".showTr").find(".proBar").removeClass("activeBar");
	$(".showTr").find(".state").removeClass("activeButton");
	$(".showTr").find(".operation").css("background-color","#f08300");
	$(".showTr").next("tr").children(".fold").css("background-color","#24710c");
	$(".showTr").next("tr").find(".record, .start-time, .end-time, .people").css("display","none");
	$(this).children("td").addClass("activeTd");
	$(this).find("[name='checkbox']").iCheck("check");
	$(this).find(".proBar").addClass("activeBar");
	$(this).find(".state").addClass("activeButton");
	$(this).find(".operation").css("background-color","#104100");
	$(this).next("tr").children(".fold").css("background-color","#f8f8f8");
	$(this).next("tr").find(".record, .start-time, .end-time, .people").css("display","inline-block");
});


$(".showTr1").click(function(){
	$(".showTr1").children("td").css("background-color","#114100");
	$(".showTr1").next("tr").children(".fold2").css("background-color","#24710c").children(".cancelAdd, .confirmAdd").css("display","none");
	$(this).children("td").css("background-color","#f08300");
	$(this).next("tr").children(".fold2").css("background-color","#fff").children(".cancelAdd, .confirmAdd").css("display","inline-block");
});

$(".cancelAdd, .confirmAdd").click(function(){
	$index=$(this).parents("tr").prev(".showTr1");
	$index.children("td").css("background-color","#114100");
	$index.next("tr").children(".fold2").css("background-color","#24710c").children(".cancelAdd, .confirmAdd").css("display","none");
});

