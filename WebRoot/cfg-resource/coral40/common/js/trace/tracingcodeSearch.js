/**
 *追溯码查询
 * @authors Qi Ying
 */

$(function(){
	//轮播图事件
	$('body').on('click','.foodcarpicul li',function(){
		$(this).addClass('current').siblings().removeClass('current');
		$('.foodcarpic ul > li:eq('+$(this).index()+')').show().siblings().hide();
		if($(this).index()=="0"){
			$("#rightUl").html("");
			if(!$.isEmptyObject(pmTp))
			$.each(pmTp,function(){
				$("#rightUl").append("<li><img src='"+baseUrl+"/spzstpfj/"+this.TPBCMC+"'/></li>");
			})
			$("#rightUl").prop("name","pmUl");
		}else if($(this).index()=="1"){
			$("#rightUl").html("");
			if(!$.isEmptyObject(sczTp))
			$.each(sczTp,function(){
				$("#rightUl").append("<li><img src='"+baseUrl+"/spzstpfj/"+this.TPBCMC+"'/></li>");
			})
			$("#rightUl").prop("name","sczUl");
		}else if($(this).index()=="2"){
			$("#rightUl").html("");
			if(!$.isEmptyObject(scqyTp))
			$.each(scqyTp,function(){
				$("#rightUl").append("<li><img src='"+baseUrl+"/spzstpfj/"+this.TPBCMC+"'/></li>");
			})
			$("#rightUl").prop("name","scqyUl");
		}else if($(this).index()=="3"){
			$("#rightUl").html("");
			if (!$.isEmptyObject(scjdTp)) {
				$.each(scjdTp, function () {
					$("#rightUl").append("<li><img src='" + baseUrl + "/spzstpfj/" + this.TPBCMC + "'/></li>");
				})
			}
			$("#rightUl").prop("name","scjdUl");
		}
	})
	//上下
	$('body').on('click','.droppicboxul li',function(){
		$(this).addClass('current').siblings().removeClass('current');
	})
	//点击右边ul切换图片
	$('body').on('click','#rightUl li',function(){
		var name = $(this).parent().prop("name");
		if(name=="pmUl"){
			$("#pmImg").prop("src",$(this).find("img").prop("src"));
			$("#foodcleft li").removeClass();
			$("#pmImg").parent().addClass("current");
		}else if(name=="sczUl"){
			$("#sczImg").prop("src",$(this).find("img").prop("src"));
			$("#foodcleft li").removeClass();
			$("#sczImg").parent().addClass("current");
		}else if(name=="scqyUl"){
			$("#scqyImg").prop("src",$(this).find("img").prop("src"));
			$("#foodcleft li").removeClass();
			$("#scqyImg").parent().addClass("current");
		}else if(name=="scjdUl"){
			$("#scjdImg").prop("src",$(this).find("img").prop("src"));
			$("#foodcleft li").removeClass();
			$("#scjdImg").parent().addClass("current");
		}
	})

	//轮播上下移动按钮事件
	var speed = 400;
	var page = 1;
	var per = 4;										//每页4条
	var videoLen = $('.droppicboxul').find('li').length;		//总数
	if(videoLen <= 4){
		videoLen =4;
		$('.foodcdropupbox,.foodcdropdownbox').addClass('opacity0');
	}
	if(page == 1){
		$('.foodcdropupbox').addClass('opacity0');
	}
	var page_count = videoLen - per + 1;				//页数
	var height = parseInt($('.droppicboxul li').outerHeight(true));				//单位个数宽
	var heightTotal = height * videoLen;				//总宽度

	$('.droppicboxul').css({'height':heightTotal + 'px'});		//根据总数重设#prolistboxarea ul的宽度



	$('.foodcdropdownbox').click(function(){
		if(page == page_count){
			$(this).addClass('opacity0');
			return ;
		}else{
			$(this).removeClass('opacity0');
			$('.droppicboxul').animate({marginTop:'-=' + height + 'px'},speed);
			page ++;
		}

		if(page >= page_count){
			$(this).addClass('opacity0');
			}

		if(page_count > 1){
			$('.foodcdropupbox').removeClass('opacity0');
			}

	})

	$('.foodcdropupbox').click(function(){
		if(page == 1){
			$(this).addClass('opacity0');
			return;
		}else{
			$(this).removeClass('opacity0');
			$('.droppicboxul').animate({marginTop:'+=' + height + 'px'},speed);
			page --;
		}

		if(page <= 1){
			$(this).addClass('opacity0');
			}
		if(page_count > 1){
			$('.foodcdropdownbox').removeClass('opacity0');
			}
	})
	//轮播上下移动按钮事件end
})
//初始化滚播
function initTp(pmTp,sczTp,scqyTp,scjdTp,type){
	if(!$.isEmptyObject(pmTp)){
		$("#pmImg").prop("src",baseUrl+"/spzstpfj/"+pmTp[0].TPBCMC);
		if(type=="1"){
			$("#pmImg").parent().addClass("current");
			$("#rightUl").prop("name","pmUl");
			$.each(pmTp,function(){
				$("#rightUl").append("<li><img src='"+baseUrl+"/spzstpfj/"+this.TPBCMC+"'/></li>");
			})
		}
	}
	if(!$.isEmptyObject(sczTp)){
		$("#sczImg").prop("src",baseUrl+"/spzstpfj/"+sczTp[0].TPBCMC);
		if(type=="2"){
			$("#sczImg").parent().addClass("current");
			$("#rightUl").prop("name","sczUl");
			$.each(sczTp,function(){
				$("#rightUl").append("<li><img src='"+baseUrl+"/spzstpfj/"+this.TPBCMC+"'/></li>");
			})
		}
	}
	if(!$.isEmptyObject(scqyTp)){
		$("#scqyImg").prop("src",baseUrl+"/spzstpfj/"+scqyTp[0].TPBCMC);
		if(type=="3"){
			$("#scqyImg").parent().addClass("current");
			$("#rightUl").prop("name","scqyUl");
			$.each(scqyTp,function(){
				$("#rightUl").append("<li><img src='"+baseUrl+"/spzstpfj/"+this.TPBCMC+"'/></li>");
			})
		}
	}
	if(!$.isEmptyObject(scjdTp)){
		$("#scjdImg").prop("src",baseUrl+"/spzstpfj/"+scjdTp[0].TPBCMC);
		if(type=="4") {
			$("#scjdImg").parent().addClass("current");
			$("#rightUl").prop("name","scjdUl");
			$.each(scjdTp,function(){
				$("#rightUl").append("<li><img src='"+baseUrl+"/spzstpfj/"+this.TPBCMC+"'/></li>");
			})
		}
	}
}