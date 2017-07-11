<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @author qiucs 
 * @date   2014-7-15
 * 系统配置平台应用自定义二次开发JS模板 
 ***************************************************/
 
 
(function(subffix) {

/**
 * 二次开发定位自己controller
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return $.contextPath + "/zzsyxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	var ncq;
	
	//回调函数处理栽培信息
	ui.addCallback("setComboGridValue_zpxx", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		//根据所选栽培信息得到栽培负责人
		var fzr_obj=$.loadJson($.contextPath + "/zzzpxx!getZpxxFzr.json?zzpch="+obj.ZZPCH);//getCtdaFzr
		ui.setFormData({ZZPCH:obj.ZZPCH,FZR:fzr_obj[0].FZR,FZRBH:fzr_obj[0].FZRBH});
 	}); 
 	//回调函数处理栽培信息
	ui.addCallback("setComboGridValue_nyda", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setFormData({SYNYBH:obj.NYBH,SYNY:obj.NYTYM});
 		ncq = obj.NCQ;
 	});
 	//回调函数处理使负责人信息
	ui.addCallback("setComboGridValue_fzr", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setFormData({FZR:obj.XM,FZRBH:obj.GZRYBH});
 	});
 	
 	ui.bindEvent = function () {
		//ZHBH：账号编号
		var zhbh=ui.getItemJQ("ZHBH");//ZHBH
		
 		//JSRQ：结束日期
 		var jsrq = ui.getItemJQ("JSRQ");
 		//ZZPCH：种植批次号
 		var zzpch = ui.getItemJQ("ZZPCH");
 		zzpch.combogrid("option","onChange",function(e,data){
			var newText = data.newText;
			//根据所选栽培信息得到栽培负责人
			var fzr_obj=$.loadJson($.contextPath + "/zzzpxx!getZpxxFzr.json?zzpch="+newText);//getCtdaFzr
			ui.setFormData({ZZPCH:newText,FZR:fzr_obj[0].FZR,FZRBH:fzr_obj[0].FZRBH});
		});
		//SYNY:使用农药
 		var synybh = ui.getItemJQ("SYNYBH");
 		synybh.combogrid("option","onChange",function(e,data){
			var newText = data.newText;
			var newValue = data.newValue;
			ui.setFormData({SYNY:newText,SYNYBH:newValue});
			var bh=zhbh.textbox("getValue");
			//获得不同农药对应的农残期
			ncq = $.loadJson($.contextPath + "/zznyda!getNcqData.json?zhbh="+bh+"&nybh="+newValue);
			//施药结束时间不变，药物残留期限随之改变
			var endDate=jsrq.datepicker("getDate");
			getNcqDate(endDate,ncq);
		});
 		//负责人名称和编号处理
 		var fzrbh = ui.getItemJQ("FZRBH");
 		fzrbh.combogrid("option","onChange",function(e,data){
			var newText = data.newText;
			var newValue = data.newValue;
			ui.setFormData({FZR:newText,FZRBH:newValue});
		});
		//处理改变施药结束的时间，药物残留期限随之改变
		jsrq.datepicker("option","onChange",function(e,data){
			var endDate=jsrq.datepicker("getDate");
			getNcqDate(endDate,ncq);
		});
 	}
 	if(ui.options.number==1){
 		/**
		 * 设置菜田列表过滤条件
		 */
		ui.addOutputValue("setZpxxColumns",function(o){
			var o = {
				status : true,
				P_columns : "EQ_C_QYZT≡1;EQ_C_ZZZT≡2"
			}
			//alert("o : =" + o.P_columns);
			return o;
		});
		/**
		 * 设置菜种列表过滤条件：菜种档案
		 */
		ui.addOutputValue("setNydaColumns",function(o){
			var o = {
				status : true,
				P_columns : "EQ_C_QYZT≡1"
			}
			return o;
		});
		/**
		 * 设置菜种列表过滤条件：工作人员
		 */
		ui.addOutputValue("setGzrydaColumns",function(o){
			var o = {
				status : true,
				P_columns : "EQ_C_QYZT≡1"
			}
			return o;
		});
 	
 	
 	}
 		//初始化参数
 	ui._init = function () {
		if (isEmpty(ui.options.dataId)) {//自动绑定账户编号
			var qybm=$.loadJson($.contextPath + '/zzcdda!getQybm.json');
			ui.setFormData({QYBM:qybm});
		}
 	} 
 	
 	//药物残留期限计算=日期+天数
 	function getNcqDate(startEnd,ncq){
 		//判断结束时间是否为空，为空不做农残期计算
 		if(startEnd==null)
 			return;
 		//判断是否获得农药残留的 天数，为空在不做药物残留期限计算
 		if(ncq==null)
 			return;
 		var date =new Date(startEnd);
 		//在施药结束有的基础上加上农药残留的天数，即为药物残留期限
 		date.setDate(date.getDate() + ncq); 
 		//药物残留期限赋值
 		var ywclqx=  ui.getItemJQ("YWCLQX");
 		ywclqx.datepicker("setDate",date );
 	}
};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义树
 */
function _override_tree (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override tree!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override tbar!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义布局
 */
function _override_layout (ui) {
	//console.log("override layout!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};








/**
 * 在此可以复写所有自定义JS类
 * @param selector
 * @returns {JQ_override}
 */
window[CFG_overrideName(subffix)] = function () {
	
	//var startTime = new Date().getTime();
	
	if (this instanceof $.config.cform) {
		_override_form(this);
	} else if (this instanceof $.config.cgrid) {
		_override_grid(this);
	} else if (this instanceof $.config.ctree) {
		_override_tree(this);
	} else if (this instanceof $.config.ctbar) {
		_override_tbar(this);
	} else if (this instanceof $.config.clayout) {
		_override_layout(this);
	}
	
	//console.log("over ride cost time: " + (new Date().getTime() - startTime));
};

	
	
	
	
})("${timestamp}");
</script>
