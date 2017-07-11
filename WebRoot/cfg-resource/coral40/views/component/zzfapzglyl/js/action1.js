// JavaScript Document
//头部下拉框初始化
function getData(e, ui){
	var flag=0;
	var category=(ui.item.text);//返回text项的值
	if(category!="全部"){
		$(".TL-operation, .TL-right-item, .TL-arrow").hide();
		for(var i=0; i<$(".TL-operation-name").length; i++){
			if ($(".TL-operation-name").eq(i).text()==category){
				$(".TL-operation").eq(i).show();
				$(".TL-arrow").eq(i-1).show();
				$(".TL-right-item").eq(i).show();
				if(flag==0){
					$(".TL-operation").eq(i).css("margin-top","44px");
					flag=1;
				}
			}
		}
	}else{
		$(".TL-operation, .TL-right-item, .TL-arrow").show();
		$(".TL-operation").css("margin-top","0").eq(0).css("margin-top","44px");
		$(".TL-arrow").eq($(".TL-arrow").length-1).hide();
	}	
}
$('#combobox1').combobox({
    valueField:'value',
    textField:'text',
    data:[{
            "value":1,
            "text":"全部",
			"selected":true
        },{
            "value":2,
            "text":"栽种"
        },{
            "value":3,
            "text":"施肥"
        },{
            "value":4,
            "text":"灌溉"
        },{
            "value":5,
            "text":"锄草"
        },{
            "value":6,
            "text":"用药"
        },{
            "value":7,
            "text":"检测"
        },{
            "value":8,
            "text":"采收"
        }],
	onSelect : "getData"
});

//日历初始化
$('#datepicker1,#datepicker2,#datepicker3,#datepicker4,#datepicker5,#datepicker6,#datepicker7').datepicker({
    readonly:true
});


//点击顶部几个按钮的效果,测试用的，可以删除
$("#edit").click(function(){
	$(this).hide();
	$("#save, #cancel").show();
	$("input[name='TL-operater']").css({"background-color":"#efefef", "color":"#3e3a39"});
	$("input[name='TL-operater']").attr("disabled",false);
	$("span#datepicker1_span2").css("background-color","#efefef");
	$("span#datepicker1_span2 .coral-textbox-default").css({"font-size":"14px", "color":"#3e3a39", "line-height":"28px"});
	$(".coral-readonly .coral-icon-calendar").show();
});
$("#save, #cancel").click(function(){
	$("#save, #cancel").hide();
	$("#edit").show();
	$("input[name='TL-operater']").css({"background-color":"transparent", "color":"#fff"});
	$("input[name='TL-operater']").attr("disabled",true);
	$("span#datepicker1_span2").css("background-color","transparent");
	$("span#datepicker1_span2 .coral-textbox-default").css({"font-size":"18px", "color":"#fff", "line-height":"24px"});
	$(".coral-readonly .coral-icon-calendar").hide();
});

//遍历左边轴每一项
/*
for(var i=0; i<$(".TL-operation-name").length; i++){
	alert($(".TL-operation-name").eq(i).text());
}*/

//点击评论按钮
/*$(".TL-comment-button").toggle(
	function(){
		$(this).siblings(".stars, .TL-comment-form").show();
		$(this).parent(".TL-item-comment1").css("background-color","#dbdbdb");
		$(this).parents(".TL-right-item").css("margin-bottom","0");
		var i=$(this).parents(".TL-right-item").index();
		$(".TL-arrow1").eq(i).css({"height":"187px","background-position":"67px 0"});
	},
	function(){
		$(this).siblings(".stars, .TL-comment-form").hide();
		$(this).parent(".TL-item-comment1").css("background-color","#898989");
		$(this).parents(".TL-right-item").css("margin-bottom","22px");
		var i=$(this).parents(".TL-right-item").index();
		$(".TL-arrow1").eq(i).css({"height":"83px","background-position":"67px -187px"});
	}
);*/
//toggle方法在iquery1.9以后版本中被弃用了！！！！

$(function(){
	var arr=[];
	for(var i=0; i<$(".TL-item-comment1").length; i++){
		arr[i]=$(".TL-item-comment1").eq(i).height();
		$(".TL-arrow").eq(i).children(".TL-arrow-middle1").css({"height":arr[i]+28});
	}
});


/*
var num=0;
$(".TL-comment-button").click(function(){
		if(num==1){
			$(this).siblings(".stars, .TL-comment-form").show();
			$(this).parent(".TL-item-comment1").css("background-color","#dbdbdb");
			var height=$(this).parent(".TL-item-comment1").height()+28;
			$(this).parents(".TL-right-item").css("margin-bottom","0");
			var i=$(this).parents(".TL-right-item").index();
			$(".TL-arrow").eq(i).children(".TL-arrow-middle1").css({"height":height});
			num=0;
		}else{
			$(this).siblings(".stars, .TL-comment-form").hide();
			$(this).parent(".TL-item-comment1").css("background-color","#898989");
			$(this).parents(".TL-right-item").css("margin-bottom","22px");
			var i=$(this).parents(".TL-right-item").index();
			$(".TL-arrow").eq(i).children(".TL-arrow-middle1").css({"height":"75px"});
			num=1;
		}
	}
);*/

var comments=$('[data-commentid]');
for(var n=0;n<comments.length;n++){
	var button=$('[data-buttonid]',comments[n]);
	button.attr('cardbelong',$(comments[n]).data('commentid'));//jQuery的.data()方法来访问这些"data-*" 属性
}
var arr=[];
for(var n=0;n<comments.length;n++){
	arr[n]=0;
}
$('[data-buttonid]').click(function(){
	var commentid=$(this).attr('cardbelong');
	var comments=$('[data-commentid="'+commentid+'"]');
	if(arr[commentid-1]==1){
		$('[data-buttonid]',comments).siblings(".stars, .TL-comment-form").show();
		$('[data-buttonid]',comments).parent(".TL-item-comment1").css("background-color","#dbdbdb");
		var height=$('[data-buttonid]',comments).parent(".TL-item-comment1").height()+28;
		$('[data-buttonid]',comments).parents(".TL-right-item").css("margin-bottom","0");
		var i=$('[data-buttonid]',comments).parents(".TL-right-item").index();
		$(".TL-arrow").eq(i).children(".TL-arrow-middle1").css({"height":height});
		arr[commentid-1]=0;
	}else{
		$('[data-buttonid]',comments).siblings(".stars, .TL-comment-form").hide();
		$('[data-buttonid]',comments).parent(".TL-item-comment1").css("background-color","#898989");
		$('[data-buttonid]',comments).parents(".TL-right-item").css("margin-bottom","22px");
		var i=$('[data-buttonid]',comments).parents(".TL-right-item").index();
		$(".TL-arrow").eq(i).children(".TL-arrow-middle1").css({"height":"75px"});
		arr[commentid-1]=1;
	}
});


