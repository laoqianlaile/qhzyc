/*!
 * 组件库4.0： 消息提示窗
 *
 * 依赖JS文件:
 *	jquery.coral.core.js
 *	jquery.coral.component.js
 *	jquery.coral.mouse.js
 *  jquery.coral.button.js
 *	jquery.coral.draggable.js
 *	jquery.coral.position.js
 *	jquery.coral.resizable.js
 *	jquery.coral.dailog.js
 */
(function( $, undefined ) {

$.messager = {
	/**
	 *  遮罩功能
	 */
	loading : {
		show: function($el) {
			if (! $el instanceof jQuery) {
				$el = $($el);
			}
			var loadingId = $el.attr("data-loading-id");
			if (!loadingId) {
				loadingId = new Date().getTime();
				$el.attr("data-loading-id", "loading_" + loadingId);
			}
			
			var $uiLoading = $("<div id='loading_"+ loadingId +"' class='coral-loading'><div class='coral-loading-zone'></div><span class='coral-loading-text'>加载中，请稍后...</span></div>");
			var $uiLoadingZone = $uiLoading.find(".coral-loading-zone");
			var $uiLoadingText = $uiLoading.find(".coral-loading-text");	
			
			$uiLoading.outerWidth($el.outerWidth());
			$uiLoading.outerHeight($el.outerHeight());
			$uiLoadingZone.outerWidth($el.outerWidth());
			$uiLoadingZone.outerHeight($el.outerHeight());
			
			$uiLoading.position({
				my: "left top",
				at: "left top",
				of: $el
			});	
			$(document.body).append($uiLoading);
			
			var loadingTextLeft = ( $uiLoading.outerWidth() - $uiLoadingText.outerWidth() ) / 2;
			$uiLoadingText.css({
				left: loadingTextLeft
			});
		},
		hide: function($el) {
			if (! $el instanceof jQuery) {
				$el = $($el);
			}
			var loadingId = $el.attr("data-loading-id");
			if (!loadingId) return;
			
			$("#" + loadingId).remove();
			$el.removeAttr("data-loading-id");
		}
	},
	_init: function( dialog, options ){
		if( options.queue == true ){
			if ( !$("#coral-msgBox").length ) {
				$("<div id='coral-msgBox' class='queueMessage'></div>").appendTo("body");
			}
			options.appendTo = "#coral-msgBox";
		} 
		var icons = "";
		if ( options.wtype == "messageBox" ) {
			dialog = $( "<div class='coral-messagerBox'>" +
				(options.icons || "") +
				"<span class='coral-messagerBox-content'>" +
				options.message + "</span></div>" ).appendTo( "body" );
		}else if ( options.wtype == "message" ) {
			if ( options.cls.indexOf("warning") > -1 ) {
				options.icons = "icon-warning2";
				icons = "<span class='icon icon-control coral-control-warning "+options.icons+"'></span>";
			} else if ( options.cls.indexOf("success") > -1 ) {
				options.icons = "icon-checkmark-circle2";
				icons = "<span class='icon icon-control coral-control-success "+options.icons+"'></span>";
			} else if ( options.cls.indexOf("danger") > -1 ) {
				options.icons = "icon-close3";
				icons = "<span class='icon icon-control coral-control-danger "+options.icons+"'></span>";
			} else if ( options.cls.indexOf("info") > -1 ) {
				options.icons = "icon-notification2";
				icons = "<span class='icon icon-control coral-control-info "+options.icons+"'></span>";
			}
			dialog = $("<div class='hasIcon "+ options.cls +"' role='alert'>" +
					icons +
					"<span class='coral-alert-content'>" + options.message + "</span></div>").appendTo( "body" );
		}else if ( typeof options.message !== "undefined" && options.wtype != "message" ) {
			if ( options.type = "question" ) {
				options.icons = "icon-info";
				icons = "<span class='icon icon-control coral-control-info "+options.icons+"'></span>"
			} else if ( options.type = "alert" ) {
				options.icons = "icon-info";
				icons = "<span class='icon icon-control coral-control-info "+options.icons+"'></span>";
			}
			dialog = $( "<div class='coral-messager-body'><span class='coral-messager-box'><span class='hasIcon coral-messager-box-content'>" +
				icons +
				"<span class='coral-messager-content'>" +
				options.message + "</span></span></span></div>" ).appendTo( "body" );
		} 
		return dialog;
	},
	/**
	 * 非模式窗口消息提示
	 * 
	 * options: 设置参数
	 * type: 消息类型
	 * fn: 消息回调
	 */
	message : function ( options, msgType, fn ) {
		if (typeof options === "string" || typeof options === "boolean" || typeof options === "number") {
			options = {message: options.toString(), wtype: "message"};
		} 
		if ( !fn && typeof msgType === "function") {
			fn = msgType;
			msgType = "info";
		}
		if ( !msgType || $.inArray(msgType, ["info", "success", "warning", "error", "question", "danger", "alert"]) < 0) {
			msgType = "info";
		}
		options = options || {};
		options.type = msgType;
		if (!options.cls) options.cls = msgType;
		options = $.extend( true, {
			autoOpen: true,
			title: "消息提示",
			isMessage: true,//区别消息框和对话框
			show: "slideDown",
			hide: "slideUp",
			cls: "info",
			modal: false,
			queue: false,
			onClose: fn,
			draggable: false,
			resizable: false,
			width: "auto",
			maxWidth: 600,
			wtype: "message",
			timeOut: 200
		}, $.message.defaults , $.messageQueue.defaults , options);
		
		var dialog = $.messager._init( dialog, options );
		dialog.css("max-width", options.maxWidth + "px");
		dialog.dialog(options);
		if (options.wtype === "message") {
			$.messager.messageInstances++;
			$.messager.messageHeights.push(dialog.outerHeight());
		}
	},
	messageQueue : function ( options ) {
		options.queue = true;
		$.messager.message( options );
	},
	messageBox: function( options, fn ) {
		/*if (typeof options === "string" && options == "close" ) {
			$(fn).dialog("close");
		}*/
		if (typeof options === "string" || typeof options === "boolean" || typeof options === "number") {
			options = {message: options.toString(), wtype: "messageBox"};
		} 
		options = options || {};
		options = $.extend( true, {
			autoOpen: true,
			title: "消息提示",
			show: "slideDown",
			hide: "slideUp",
			isMessage: true,//区别消息框和对话框
			modal: false,
			draggable: false,
			resizable: false,
			width: 250,
			wtype: "messageBox",
			timeOut: 2999
		}, $.messageBox.defaults , options);
		
		var dialog = $.messager._init( dialog, options );
		dialog.dialog(options);
		return dialog;
	},
	// 模式窗口消息提示
	alert : function(options, fn) {
		if (typeof options === "string" || typeof options === "boolean" || typeof options === "number") {
			options = {message: options.toString()};
		}
		options = $.extend( true, {
			modal: true,
			timeOut: 0,
			title: "信息提示",
			isMessage: true,//区别消息框和对话框
			width: 300,
			icons: "icon-notification2",
			wtype: "alert"
		}, $.alert.defaults ,
		options);
		if ((options.timeOut <= 0 || isNaN(options.timeOut)) && !options.buttons) {
			options.buttons = {
				"确定": function() {						
					$( this ).dialog( "close" );
					if (typeof fn === "function") {
						fn(true);
					}
					return true;
				}
			};
		}
		options.wtype = "alert";
		options.type = "alert";
		var dialog = $.messager._init( dialog, options );
		dialog.dialog(options);
		
		return dialog;
	},
	
	// 确认框提示窗口
	confirm : function (options, fn) {
		var okText = "确定",
			cancelText = "取消",
			buttons = {};
		
		if (typeof options === "string" || typeof options === "boolean" || typeof options === "number") {
			options = {message: options};
		}
		okText = options.okText ? options.okText : okText;
		cancelText = options.cancelText ? options.cancelText : cancelText;
		buttons[okText] = function() {
			$( this ).dialog( "close" );
			if (typeof fn === "function") {
				fn(true);
			}
			return false;
		};
		buttons[cancelText] = function() {
			$( this ).dialog( "close" );
			if (typeof fn === "function") {
				fn(false);
			}
			return false;
		};
		options.type = "question";
		options.wtype= "confirm";
		options = $.extend( true, {
			autoOpen: true,
			title: "确认提示",
			isMessage: true,//区别消息框和对话框
			width: 300,
			show: "slideDown",
			modal: true,
			icons: "icon-question",
			buttons: buttons
		}, $.confirm.defaults, options);
		
		var dialog = $.messager._init( dialog, options );
		dialog.dialog(options);
		
		return dialog;
	},
	progress : function (options, fn) {
		var okText = "确定",
			cancelText = "取消",
			buttons = {};
		
		if (typeof options === "string" || typeof options === "boolean" || typeof options === "number") {
			options = {message: options};
		}
		okText = options.okText ? options.okText : okText;
		cancelText = options.cancelText ? options.cancelText : cancelText;
		buttons[okText] = function() {
			$( this ).dialog( "close" );
			if (typeof fn === "function") {
				fn(true);
			}
			return false;
		};
		buttons[cancelText] = function() {
			$( this ).dialog( "close" );
			if (typeof fn === "function") {
				fn(false);
			}
			return false;
		};
		options.type = "question";
		options.wtype= "confirm";
		options = $.extend( true, {
			autoOpen: true,
			title: "确认提示",
			isMessage: true,//区别消息框和对话框
			width: 300,
			show: "slideDown",
			modal: true,
			icons: "icon-question",
			buttons: buttons
		}, $.confirm.defaults, options);
		
		var dialog = $.messager._init( dialog, options );
		dialog.dialog(options);
		
		return dialog;
	}
};
/*$.fn["messageBox"] = function ( options ) {
    return this.each(function () {
        if ( options && "hide" !== options ) {
    		$.messageBox( options );
        } else {
            if( "hide" === options ) { 
            	$(this).dialog("close"); 
            } else { 
            	$(this).dialog("open"); 
            }
        }
    });
};*/
$.messager.messageInstances = 0;
$.messager.messageHeights = [];
// 简化使用方式
$.message = $.messager.message;
$.messageQueue = $.messager.messageQueue;
$.messageBox = $.messager.messageBox;
$.alert = $.messager.alert;
$.confirm = $.messager.confirm;
$.message.defaults = {
	position: {
		my: "top",
		at: "top top+50",
		of: window
	}
}
$.messageQueue.defaults = {
	position: {
		my: "top",
		at: "top top+50",
		of: window
	}
}
$.messageBox.defaults = {
	position: {
		my: "right bottom",
		at: "right bottom",
		of: window
	}
};
$.alert.defaults = {
	position: {
		my: "center",
		at: "center top+200",
		of: window
	}
};
$.confirm.defaults = {
	position: {
		my: "center",
		at: "center top+200",
		of: window
	}
};
}( jQuery ) );
