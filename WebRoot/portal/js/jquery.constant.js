/**
 * @CHARSET "UTF-8"
 * 初始化页面常量
 */

(function($) {
	jQuery.constant = {
		sysdate:function(type){
			var date = new Date();
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			var hh = date.getHours();
			var mm = date.getMinutes();
			var ss = date.getSeconds();
			
			if(type && "date"==type){
				return y+"-"+fill0(m)+"-"+fill0(d);				
			}else if(type && "datetime"==type){
				return y+"-"+fill0(m)+"-"+fill0(d)+" "+fill0(hh)+":"+fill0(mm)+":"+fill0(ss);
			}
			
			function fill0(v){
				return v<10?"0"+v:v;
			}
		},
		wjlx:{
			"WJ":"文件",
			"JB":"简报",
			"TZ":"会议通知",
			"QT":"其他"
		},
		wjly:{
			"01":"市委",
			"03":"市院",
			"04":"高检",
			"05":"其他"
		},
		wjzt:{
			"dygw":"待阅",
			"yygw":"已阅",
			"gwzs":"全部",
			"wdsc":"收藏夹"
		},
		ydzt:{
			"dygw":0,
			"yygw":1,
			"gwzs":2,
			"wdsc":3
		},
		zt:{
			"0":"待阅",
			"1":"已阅",
			"5":"系统"
		},
		code:{
			"WMJ":"无",
			"MM":"秘密",
			"JM":"机密",
			"NBCL":"内部材料",
			"F":"否",
			"T":"是",
			"WJJCD":"无",
			"PJ":"平急",
			"JJ":"加急",
			"TJ":"特急",
			"WJ":"文件",
			"TZ":"会议通知",
			"JB":"简报",
			"QT":"其他",
			"01":"市委",
			"02":"市府",
			"03":"市院",
			"04":"高检",
			"05":"其他"
		}
	};
})(jQuery);