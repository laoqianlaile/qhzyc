/**
 *
 * @authors Qi Ying
 */
var baseUrl = $.contextPath;
$(function(){

	newsrightheight();
	$(window).resize(function(){
		newsrightheight();
	})

	function newsrightheight(){
		if(parseInt($('.food-news-list-left').css('margin-right')) < 0 ){
			$('.food-news-list-right').height($('.food-news-list-left').height());
		}else{
			$('.food-news-list-right').css('height','auto');
		}
	}


	//浮层
	var floatLayer = '';
	floatLayer += '<div class="floatLayer small">';
		floatLayer += '<div class="floatlayerClose"></div>';
		floatLayer += '<ul class="floatLayer-ul">';
			floatLayer += '<li>';
				floatLayer += '<div class="appshow">';
					floatLayer += '<i class="icon-app icon-android-app"></i>';
				floatLayer += '</div>';
				floatLayer += '<div class="QRcode">';
					floatLayer += '<img src="css/images_common/QRcode.png">';
				floatLayer += '</div>';
				floatLayer += '<p>追溯Android客户端下载</p>';
			floatLayer += '</li>';
			floatLayer += '<li>';
				floatLayer += '<div class="appshow">';
					floatLayer += '<i class="icon-app icon-ios-app"></i>';
				floatLayer += '</div>';
				floatLayer += '<div class="QRcode">';
					floatLayer += '<img src="css/images_common/QRcode.png">';
				floatLayer += '</div>';
				floatLayer += '<p>追溯IOS客户端下载</p>';
			floatLayer += '</li>';
			floatLayer += '<li>';
				floatLayer += '<div class="appshow">';
					floatLayer += '<i class="icon-app icon-webchat-app"></i>';
				floatLayer += '</div>';
				floatLayer += '<div class="QRcode">';
					floatLayer += '<img src="css/images_common/QRcode.png">';
				floatLayer += '</div>';
				floatLayer += '<p>微信查询</p>';
			floatLayer += '</li>';
		floatLayer += '</ul>';
	floatLayer += '</div>';
	//$('body').append(floatLayer);

	//浮层展开收缩
	$('body').on('click','.floatLayer',function(){
		if($(this).hasClass('small')){
			$(this).removeClass('small').addClass('floatLayerslide');
		}else{
			$(this).addClass('small').removeClass('floatLayerslide');
		}
	})
	//浮层关闭
	$('body').on('click','.floatlayerClose',function(){
		$(this).parent('.floatLayer').hide();
	})


	//追溯码查询按钮事件
	/*$('body').on('click','.tracingcode-search-btn',function(){
		if($('.tracingcode-input').val() == ''){
			$('.message-error').show();
			$('.message-error-text').html('追溯码不能为空');
		}else{
			var zsm = $(".tracingcode-input").val();
			window.location.href="tracingcodeSearch.html?zsm="+zsm;
		}
	})*/
	//4s后提示框消失
	function flash_title(){
		if($('.message-error').show()){
			$('.message-error').hide();
		}
	}
	setInterval(flash_title,4000);


	//资讯事件切换
	$('body').on('click','.news-top-ul li',function(){
		$(this).addClass('current').siblings().removeClass('current');
	})

	//分页
	$('body').on('click','.page>span',function(){
		$(this).addClass('current').siblings().removeClass('current');
	})

	if($('.paging').hasClass('disabled')){
		return;
	}


	//追溯码查询页面 缩略图列表页drop 事件
	$('body').on('click','.legenddrop',function(){
		if($(this).hasClass('down')){
			$(this).addClass('up').removeClass('down');
			$(this).parent('fieldset').children('.tracingcodecontentbox').slideUp();
		}else{
			$(this).addClass('down').removeClass('up');
			$(this).parent('fieldset').children('.tracingcodecontentbox').slideDown();
		}
	})

	//示例关闭、
	$('body').on('click','.exampledialogclose',function(){
		$('.exampledialog').hide();
	})

	//forexample 示例弹出
	$('body').on('click','.forexample',function(){
		$('.exampledialog').show();
	})

})
/*
 * 获取参数
 * name:属性名
 */
function GetQueryString(name)
{
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  decodeURI(r[2]); return null;
}