// JavaScript Document

$(function(){
	init();
	
	//屏幕缩放
	$(window).resize(function(){
		init();
		})
	
	
	//定义公共变量
	var w,h,contentH,tableboxH,menuleftulH,noticeallH,shortulH,footerH;
	
	//初始化操作
	function init(){

		w = parseInt($(window).width());
		h = parseInt($(window).height());
		
		footerH = parseInt($('.footer').height());
		shortulH = parseInt($('.shortul').height());
		
		//快捷方式
		if($('.moreiocn').hasClass('up')){
			$('.shortcut').css('height', '55px');
			}else{
				$('.shortcut').css('height', shortulH + 'px');
				}
		
		contentH = parseInt( h - $('.headwrap').height() - $('.footer').height() );
		$('.content').css('height', contentH + 'px');
		
		tableboxH = parseInt( contentH - $('.slidebox').height() - 55 );//55为$('.shortcut').height()
		$('.tablebox').css('height', tableboxH + 'px');
		
		noticeallH = parseInt( tableboxH - $('.noticeboard').height());
		$('.noticeall').css('height', noticeallH + 'px');
		
		
		//taizhang	
		var videoLen = $('.slideborder').find('div.slidediv').length;
		var width = 10; //初始值是10，保证所有元素不被溢出
		for(var i=0;i<videoLen;i++){
			width += $('.slidediv').width();
			}		
		$('.slidecontent').css({'width':width + 'px'});
		
		
		//menu lock/unlock
		if($('.menu_b').hasClass('menufix')){
			$('.menu_b').mouseleave(function(){
				$(this).hide();
				})						
			}else{
				$('.menu_b').mouseleave(function(){
					$(this).show();
					})
				}
				
		//快捷方式数量判断
		var shortcutLen = $('.shortul').find('li').length;
		//快捷方式溢出判断
		if( w <= 1024 && shortcutLen <=6 ){
			$('.short_more').hide();
			}
		if( w >= 1250 && shortcutLen <=7 ){
			$('.short_more').hide();
			}
		if( w >= 1366 && shortcutLen <=8 ){
			$('.short_more').hide();
			}	
		
		
		//ie7
		var userAgent = navigator.userAgent.toLowerCase(); 
		jQuery.browser = { 
			version: (userAgent.match( /.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/ ) || [])[1], 
			msie: /msie/.test( userAgent ) && !/opera/.test( userAgent )
		};		
		if($.browser.msie&&($.browser.version == "7.0")){
			if( w <= 1024){
				tableboxH = parseInt( contentH - 140 - 55 );//55为$('.shortcut').height()
				$('.tablebox').css('height', tableboxH + 'px');
				}else{
					tableboxH = parseInt( contentH - 200 - 55 );//55为$('.shortcut').height()
					$('.tablebox').css('height', tableboxH + 'px');
					}
			}
				
				
		}
		

	
	   
	   
	   //head addClass('hit')
	   $('.headul li').click(function(){
				$(this).addClass('hit').siblings().removeClass('hit');
				$('.menu_b').show();
				$('.lockside').show();
				$('.menu_b>div:eq('+$(this).index()+')').show().siblings().hide();
			   
				init();	
			}).mouseover(function(){
			    $(this).addClass('hit').siblings().removeClass('hit');
				$('.menu_b>div:eq('+$(this).index()+')').show().siblings().hide();
				$('.menu_b').show();						
				$('.lockside').show();
				init();	
			   }).mouseleave(function(){					   
				   init();
				   });
			   
		  
		//second menu none/block
		/*$('.menu_area>div.menu_second').click(function(){
			
			if($(this).parent().hasClass('hit')){				
				$(this).parent().removeClass('hit');
				$(this).parent().children('div.menu_third').animate({width:"hide"},300);
				}else{
					$(this).parent().addClass('hit').siblings().removeClass('hit');
					$(this).parent().children('div.menu_third').animate({width:"show"},300);
					$(this).parent().siblings().children('div.menu_third').animate({width:"hide"},300);	
					}
					
			})*/
		
		//third menu click
		$('.menu_third ul li').click(function(){
			$(this).addClass('current').siblings().removeClass('current');
			$(this).parent().siblings().children().removeClass('current');			
			})
		
		
		//second menu lock/unlock
		$('.lockside').click(function(){
			if($(this).hasClass('unlock')){
				$(this).removeClass('unlock').addClass('lock');
				$('.menu_b').removeClass('menufix');
				}else{
					$(this).removeClass('lock').addClass('unlock');
					$('.menu_b').addClass('menufix');					
					}
			init();
			})
			
			
		 //快捷方式
		$('.moreiocn').click(function(){
			if($(this).hasClass('up')){				
				$(this).removeClass('up').addClass('down');
				}else{
					$(this).addClass('up').removeClass('down');
					}
				init();							
			})
				
		
		
	   
	   
	})