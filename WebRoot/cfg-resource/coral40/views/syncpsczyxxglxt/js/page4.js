// JavaScript Document




var bgObj;
function bgBlur(){
	var sWidth,sHeight;
	sWidth=document.body.scrollWidth;//获得的body正文全文宽
	sHeight=document.body.scrollHeight ;//获得的body正文全文高
	bgObj=document.createElement("div");
	bgObj.setAttribute('id','bgDiv');
	bgObj.style.position="absolute";
	bgObj.style.top="0";
	bgObj.style.background="#000";
	bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	bgObj.style.opacity="0.6";
	bgObj.style.left="0";
	bgObj.style.width=sWidth + "px";
	bgObj.style.height=sHeight + "px";
	bgObj.style.zIndex = "10000";
	document.body.appendChild(bgObj);
	var wrapper=document.getElementById("wrapper");
	wrapper.style.webkitFilter="blur(10px)";
	wrapper.style.mozFilter="blur(10px)";
	wrapper.style.msFilter="blur(10px)";
	wrapper.style.oFilter="blur(10px)";
	wrapper.style.filter="blur(10px)";
}
function infoShow(id){
		_data.operationId = id;
		_data.czlx = "1";
		bgBlur();
		$("#tanchuang").css("display","block");
        showCzjl();//显示操作记录
		$("#closeBtn").css("cursor","pointer");
		_data.readCard = "true";
		//alert(1720);
		window.setTimeout(touchReadCard,3000);
		$("#closeBtn").click(
			function(){
				$("#operationInput").val("");
				$("#pjInput").val("");
                showNsx(_data.lx);
				_data.readCard = "false";
				document.body.removeChild(bgObj);
				$("#tanchuang").css("display","none");
				$("#wrapper").css("webkitFilter","none");
				$("#wrapper").css("mozFilter","none");
				$("#wrapper").css("msFilter","none");
				$("#wrapper").css("oFilter","none");
				$("#wrapper").css("filter","none");
			}
		)
}
function infoShow1(id){
		var index;
		bgBlur();
		$("#tanchuang1").css("display","block");
		$("#closeBtn1").css("cursor","pointer");
	_data.operationId = id;
	showPjCzjl();//显示评价操作记

		//$("#closeBtn1").click(
		//	function(){
		//		document.body.removeChild(bgObj);
		//		$("#tanchuang1").css("display","none");
		//		$("#wrapper").css("webkitFilter","none");
		//		$("#wrapper").css("mozFilter","none");
		//		$("#wrapper").css("msFilter","none");
		//		$("#wrapper").css("oFilter","none");
		//		$("#wrapper").css("filter","none");
		//	}
		//);
		//var x=0;
		//$(".starts ul li").click(
		//	function(){
		//		alert($(this).parent().attr("czjlId"));
		//		index=$(this).index();
		//		for(var i=0;i<=index;i++){
		//			$(".starts ul li").eq(i).css("background-position","center -240px").css({width:"27px"});
		//		}
		//		for(var i=index+1;i<=2;i++){
		//			$(".starts ul li").eq(i).css("background-position","center -160px").css({width:"27px"});
		//		}
		//		if(x==0){
		//			$(".handle-info1").animate({width:"760px"},80,function(){
		//				//calback
		//				$("#scrollbar1").css({width:"750px"});
		//				$("#tab-cnt1").css({width:"720px"});
		//				$(".cnt1 td").css({fontSize:"20px"});
		//				$("#person").css({width:"28%"});
		//				$("#forthTd").css({width:"17%"});
		//				$(".starts").css({width:"45%"});
		//			});
		//			/*$(".cnt1 td").animate({fontSize:"20px"},10);
		//			$(".cnt1 .right-rad1").animate({width:"16%"},80);
		//			$("#forthTd").animate({width:"24%"},80);*/
		//			x=0;
		//		}
		//	}
		//);
		
		 $('#scrollbar1').tinyscrollbar();
}

function isCheck(){
	var obj=document.getElementsByName("checkbox");//根据自己的多选框名称修改下
	var b =false;
	for(var i=0;i< obj.length;i++){
		if(obj[i].checked){
			b = true;
		}
	}
	if(b==false){
		alert("请先选择项目，再进行操作");//提示信息自己修改
	}
	return b;
}
function infoShow2(){
    var checkboxs = $("input[name='checkbox']");
    _data.operationId = "";
    $.each(checkboxs,function(e,data){
        if($(this).parent().prop("class").indexOf("checked") != -1){
            _data.operationId += $(this).attr("bid")+",";
        }
    });
	_data.czlx = "1";
	if(isCheck()){
		bgBlur();
		$("#tanchuang").css("display","block");
        showCzjl();//显示操作记录
		$("#closeBtn").css("cursor","pointer");
		_data.readCard = "true";
		window.setTimeout(touchReadCard,3000);
		$("#closeBtn").click(
			function(){
				$("#operationInput").val("");
				$("#pjInput").val("");
				_data.readCard = "false";
				document.body.removeChild(bgObj);
				$("#tanchuang").css("display","none");
				$("#wrapper").css("webkitFilter","none");
				$("#wrapper").css("mozFilter","none");
				$("#wrapper").css("msFilter","none");
				$("#wrapper").css("oFilter","none");
				$("#wrapper").css("filter","none");
			}
		)
	}	
}

//star
//$(function(){
//	var s = document.getElementById("pingStar"),
//		n = s.getElementsByTagName("li"),
//		input = document.getElementById('startP');  //保存所选值
//	clearAll = function () {
//		for (var i = 0; i < n.length; i++) {
//			n[i].className = '';
//		}
//	}
//	for (var i = 0; i < n.length; i++) {
//		n[i].onclick = function () {
//			var q = this.getAttribute("rel");
//			clearAll();
//			input.value = q;
//			for (var i = 0; i < q; i++) {
//				n[i].className = 'on';
//			}
//		}
//		n[i].onmouseover = function () {
//			var q = this.getAttribute("rel");
//			clearAll();
//			for (var i = 0; i < q; i++) {
//				n[i].className = 'on';
//			}
//		}
//		n[i].onmouseout = function () {
//			clearAll();
//			for (var i = 0; i < input.value; i++) {
//				n[i].className = 'on';
//			}
//		}
//	}
//});


//输入框
$("#card-number input").focus(function(){
	$(this).attr("value","");
}).blur(function(){
	if($(this).attr("value")==""){
		$(this).attr("value","请输入卡号");
	}
});