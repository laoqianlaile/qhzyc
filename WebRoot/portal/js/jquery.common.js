/**
 * @CHARSET "UTF-8"
 */
document.write('<script language="javascript" src="'+basePath+'/portal/js/json2.js"></script>');

/**
 * 修复IE6下图排缓存问题
 */
(function() {
	try {
		var userAgent = navigator.userAgent.toLowerCase();
		var ver = userAgent.match(/msie ([\d.]+)/gi);
		if (ver == "msie 6.0") {
			document.execCommand("BackgroundImageCache", false, true);
		}
	} catch (e) {
		alert(e);
	}
})();

/**
 * 
 * 功能列表：基于jQuery.1.8.3
 * 1、动态显示系统时钟
 * --例：$([显示时钟的元素]).showCalendar();
 * 
 * 2、操作cookie
 * --例：$.Cookie("writer",{name:'名称',value:'值',times:'有效时间'});
 * --例：$.Cookie("reader",'Cookie名称');
 */
(function($) {    
	jQuery.fn.extend({
		/**
		 * 显示系统时间
		 * 格式：2014年01月08日 星期一 16:18:45
		 */
		showCalendar:function(){
			var weeks = ["日","一","二","三","四","五","六"];
			
			var $calendar = $(this);
			if($calendar){
				window.setInterval(function(){
					$calendar.html(createTimes());
				},1000);
			}
			
			function createTimes(){
				var time = new Date();
				var year = time.getYear();
				var month = time.getMonth();
				var today = time.getDate();
				var week = time.getDay();
				var hour = time.getHours();
				var minuts = time.getMinutes();
				var second = time.getSeconds();
				var timeString = year+"年"+fixed(month+1)+"月"+fixed(today)+"日 "+" 星期"+weeks[week]+" "+fixed(hour)+":"+fixed(minuts)+":"+fixed(second); 
				
				function fixed(src){
					return (src * 1 < 10)?"0"+src:src;
				}
				return timeString;
			}
		},
		
		/**
		 * 获取form表单定义的参数列表
		 * 表单中元素的定义方式参见grid.tag
		 */
		getParams:function(){
			var paramarr = [];
			this.each(function(i,param){
				paramarr.push({
					operator: param.getAttribute("operator"),
					connector: param.getAttribute("connector"),
					fieldType: param.getAttribute("fieldType"),
					fieldName: param.getAttribute("name"),
					fieldValue: param.getAttribute("value")
				});
			});
			return $.json.toString(paramarr);
		}
	});
	
	jQuery.extend({
		/**
		 * ajax请求
		 * @param opts
		 */
		ajaxsend:function(opts){
			var defaults = jQuery.extend({
				url : '',
				type : 'POST',
				data : {},
				async: false,
				cache: false,
				dataType: "json",
				error: function(xhr, status, error){
					
				},
				success: function(data, status, xhr){
					$.messager.alert('提示信息','信息发送成功!','info');
				},
				statusCode: {
					404:function() {
						$.messager.alert('错误信息','请求的页面不存在：','error');
					},
					500:function(){
						$.messager.alert('错误信息','服务器内部错误：','error');
					}
				}
			}, opts);
			
			jQuery.ajax(defaults);
		},
		/**
		 * 操作cookie
		 * @param method
		 * @param opts
		 * @returns
		 */
		Cookie:function(method,opts){
			if (typeof method == 'string') {
				return jQuery.Cookie.methods[method](this,opts);
			}
		},
		/**
		 * json对象转字符串
		 * @param jsonObj
		 * @returns
		 */
		json:{
			toString:function(jsonObj){
				return JSON.stringify(jsonObj);
			},
			toObject:function(jsonStr){
				return eval("("+jsonStr+")");
			}
		},
		string:{
			replace:function(src, regex){
				
			}
		},
		browser:{
			version:function(ver){
				var userAgent = navigator.userAgent.toLowerCase();
				return ver == userAgent.match(/ie ([\d.]+)/gi);
			}
		},
		/**
		 * 操作iframe
		 * @param method
		 * @param opts
		 * @returns
		 */
		iframe:function(method,opts){
			if (typeof method == 'string') {
				return jQuery.iframe.methods[method](this, opts);
			}
		},
		/**
		 * 操作浏览器窗口的方法
		 */
		Window:{
			open:function(options,callback){
				var defualt = {
					url:'',
					title:'',
					modal:false,
					width:800,
					height:600,
					help: 'no',
					status: 'no',
					toolbar: 'no',
					menubar: 'no',
					location: 'no',
					resizable: 'no',
					scrollbars: 'no',
					fullscreen: 'no'
				};
				if(options){
					jQuery.extend(defualt,options);
				}
				
				var top = defualt.fullscreen=="no"?(window.screen.availHeight-defualt.height)/2:0;
				var left = defualt.fullscreen=="no"?(window.screen.availWidth-defualt.width)/2:0;
				
				if(defualt.modal){
					if(defualt.fullscreen=="yes"){
						top = 0;
						left = 0;
						defualt.width = window.screen.availWidth;
						defualt.height = window.screen.availHeight;
					}
					window.showModalDialog(defualt.url,defualt.title,
					"help:"+defualt.help+
					";status:"+defualt.status+
					";dialogTop:"+top+"px"+
					";dialogLeft:"+left+"px"+
					";dialogWidth:"+defualt.width+"px"+
					";dialogHeight:"+defualt.height+"px");
				}else{
					window.open(defualt.url,"newwindow",
					"top="+top+
					",left="+left+
					",width="+defualt.width+
					",height="+defualt.height+
					",status="+defualt.status+
					",toolbar="+defualt.toolbar+
					",menubar="+defualt.menubar+
					",location="+defualt.location+
					",resizable="+defualt.resizable+
					",scrollbars="+defualt.scrollbars+
					",fullscreen="+defualt.fullscreen);
				}
				if(callback && typeof callback == "function") callback();
			}
		}
	});
	// 操作cookie的方法
	jQuery.Cookie.methods = {
		writer:function(obj,opts){
			var defaults = {
				name:"cookies",
				value:"",
				times:24
			};
			if(opts){
				jQuery.extend(defaults,opts);
			}
			
			var expire = "";
			if (hours != null) {
				expire = new Date((new Date()).getTime() + defaults.times * 3600000);
				expire = "; expires=" + expire.toGMTString();
			}
			document.cookie = defaults.name + "=" + escape(defaults.value) + expire;
		},
		reader:function(obj,name){
			var cookieValue = "";
			var search = name + "=";
			if (document.cookie.length > 0) {
				offset = document.cookie.indexOf(search);
				if (offset != -1) {
					offset += search.length;
					end = document.cookie.indexOf(";", offset);
					if (end == -1)
						end = document.cookie.length;
					cookieValue = unescape(document.cookie.substring(offset, end));
				}
			}
			return cookieValue;
		}
	};
	
	// 操作iframe的方法
	jQuery.iframe.methods = {
		/**
		 * 在JS中创建iframe元素
		 * @param options 创建iframe元素所需的相关属性（参见defaults）
		 * @returns {String}
		 */
		create : function(obj,opts){
			var defaults = {
				id : 'tab-frame',
				src : '',
				width : '100%',
				height : '100%',
				border : '0',
				scrolling : 'no'
			};
			if(opts){
				jQuery.extend(defaults,opts);
			}
			var iframe = "<iframe id='"+defaults.id+"'" +
								" name='"+defaults.id+"'" +
								" width='"+defaults.width+"'" +
								" height='"+defaults.height+"'" +
								" scrolling='"+defaults.scrolling+"'" +
								" frameborder='"+defaults.border+"'" +
								" src='"+defaults.src+"'>" +
						 "</iframe>";
			return iframe;
		},
		// 根据iframe id获取iframe的DOM对象（用于通过js方法访问iframe页面中的元素）
		getDocument : function(frameid, hasParent){
			var frame = getFrame(frameid, getDocument(hasParent));
			if(frame){
				return frame.contentDocument || frame.document;
			}else{
				return null;
			}
		},
		// 根据iframe id获取iframe的window对象（用于访问iframe页面中的js函数）
		getWindow : function(frameid, hasParent){
			var frame = getFrame(frameid, getDocument(hasParent));
			if(frame){
				return frame.contentWindow || frame.window;
			}else{
				return null;
			}
		},
		// 根据iframe id获取iframe的jquery对象（用于通过jquery方法访问iframe页面中的元素）
		getObject : function(frameid){
			return $('#'+frameid).contents();
		}
	};
	
	// 内部方法 返回document中的iframe对象
	function getFrame(frameid, doc){
		return doc.getElementById(frameid) || doc.frames[frameid];
	}
	// 内部方法 返回document对象
	function getDocument(hasParent){
		if(hasParent){
			return parent.document;
		}else{
			return document;
		}
	}
})(jQuery);

/**
 * 判断对象是否为空
 * @param obj
 * @returns {Boolean}
 */
function isNull(obj){
	if(obj == null || obj == undefined || obj == ""){
		return true;
	}else{
		return false;
	}
}

/**
 * 屏蔽退格键等导航键
 */
document.onkeydown = function() {
	if (event.keyCode == 8 && ((event.srcElement.type != "text" && event.srcElement.type != "textarea" && event.srcElement.type != "password") || (event.srcElement.readOnly)) //退格键
		) {
		event.keyCode = 0;
		event.returnValue = false;
	}
};

// TODO 获取一个DIV的绝对坐标的功能函数,即使是非绝对定位,一样能获取到
function getElCoordinate(dom) {
	var t = dom.offsetTop;
	var l = dom.offsetLeft;
	dom = dom.offsetParent;
	while (dom) {
		t += dom.offsetTop;
		l += dom.offsetLeft;
		dom = dom.offsetParent;
	}
	return {
		top : t,
		left : l
	};
}

// TODO 兼容各种浏览器的,获取鼠标真实位置
function mousePosition(ev){
	if(!ev) ev=window.event;
    if(ev.pageX || ev.pageY){
        return {x:ev.pageX, y:ev.pageY};
    }
    return {
        x:ev.clientX + document.documentElement.scrollLeft - document.body.clientLeft,
        y:ev.clientY + document.documentElement.scrollTop  - document.body.clientTop
    };
}
 
function Base64() {   
    // private property  
    _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";  
   
    // public method for encoding  
    this.encode = function (input) {  
        var output = "";  
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;  
        var i = 0;  
        input = _utf8_encode(input);  
        while (i < input.length) {  
            chr1 = input.charCodeAt(i++);  
            chr2 = input.charCodeAt(i++);  
            chr3 = input.charCodeAt(i++);  
            enc1 = chr1 >> 2;  
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);  
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);  
            enc4 = chr3 & 63;  
            if (isNaN(chr2)) {  
                enc3 = enc4 = 64;  
            } else if (isNaN(chr3)) {  
                enc4 = 64;  
            }  
            output = output +  
            _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +  
            _keyStr.charAt(enc3) + _keyStr.charAt(enc4);  
        }  
        return output;  
    };
   
    // public method for decoding  
    this.decode = function (input) {  
        var output = "";  
        var chr1, chr2, chr3;  
        var enc1, enc2, enc3, enc4;  
        var i = 0;  
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");  
        while (i < input.length) {  
            enc1 = _keyStr.indexOf(input.charAt(i++));  
            enc2 = _keyStr.indexOf(input.charAt(i++));  
            enc3 = _keyStr.indexOf(input.charAt(i++));  
            enc4 = _keyStr.indexOf(input.charAt(i++));  
            chr1 = (enc1 << 2) | (enc2 >> 4);  
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);  
            chr3 = ((enc3 & 3) << 6) | enc4;  
            output = output + String.fromCharCode(chr1);  
            if (enc3 != 64) {  
                output = output + String.fromCharCode(chr2);  
            }  
            if (enc4 != 64) {  
                output = output + String.fromCharCode(chr3);  
            }  
        }  
        output = _utf8_decode(output);  
        return output;  
    };
   
    // private method for UTF-8 encoding  
    _utf8_encode = function (string) {  
        string = string.replace(/\r\n/g,"\n");  
        var utftext = "";  
        for (var n = 0; n < string.length; n++) {  
            var c = string.charCodeAt(n);  
            if (c < 128) {  
                utftext += String.fromCharCode(c);  
            } else if((c > 127) && (c < 2048)) {  
                utftext += String.fromCharCode((c >> 6) | 192);  
                utftext += String.fromCharCode((c & 63) | 128);  
            } else {  
                utftext += String.fromCharCode((c >> 12) | 224);  
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);  
                utftext += String.fromCharCode((c & 63) | 128);  
            }  
   
        }  
        return utftext;  
    };
   
    // private method for UTF-8 decoding  
    _utf8_decode = function (utftext) {  
        var string = "";  
        var i = 0;  
        var c = c1 = c2 = 0;  
        while ( i < utftext.length ) {  
            c = utftext.charCodeAt(i);  
            if (c < 128) {  
                string += String.fromCharCode(c);  
                i++;  
            } else if((c > 191) && (c < 224)) {  
                c2 = utftext.charCodeAt(i+1);  
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));  
                i += 2;  
            } else {  
                c2 = utftext.charCodeAt(i+1);  
                c3 = utftext.charCodeAt(i+2);  
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));  
                i += 3;  
            }  
        }  
        return string;  
    };
}