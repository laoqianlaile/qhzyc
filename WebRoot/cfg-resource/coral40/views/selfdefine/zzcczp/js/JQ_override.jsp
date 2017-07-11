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
	return $.contextPath + "/zzcczp";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	//回调函数处理栽培信息
	ui.addCallback("setComboGridValue_zpxx", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
		ui.setFormData({A_ZZPCH:obj.ZZPCH});
		defaultDataBy_zpxx(obj.ZZPCH);
 	}); 
 	//回调函数处理使负责人信息
	ui.addCallback("setComboGridValue_fzr", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setFormData({A_FZR:obj.XM,A_FZRBH:obj.GZRYBH});
 	});
 	//回调函数处理到达地信息
	ui.addCallback("setComboGridValue_ddd", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setFormData({A_DDD:obj.CDMC,A_DDDBM:obj.CDBM});
 	});
 	if(ui.options.number==1){
 		/**
		 * 设置栽培列表过滤条件
		 */
		ui.addOutputValue("setZpxxColumns",function(o){
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
 	
 	ui.bindEvent = function () {
 		//根据种植批次选择默认的负责人
 		var zzpch = ui.getItemJQ("A_ZZPCH");
 		zzpch.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			//根据所选栽培信息得到栽培负责人
			var fzr_obj=$.loadJson($.contextPath + "/zzzpxx!getZpxxFzr.json?zzpch="+newText);//getCtdaFzr
			ui.setFormData({A_ZZPCH:newText});
			defaultDataBy_zpxx(newText);
		});
 		//负责人名称和编号处理
 		var fzrbh = ui.getItemJQ("A_FZRBH");
 		fzrbh.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			ui.setFormData({A_FZR:newText,A_FZRBH:newValue});
		});
		var dddJQ = ui.getItemJQ("A_DDD");
		dddJQ.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			ui.setFormData({A_DDD:newText,A_DDDBM:newValue});
		});
 	}
 	//初始化参数
 	ui._init = function () {
		if (isEmpty(ui.options.dataId)) {//自动绑定账户编号
			var cdda=$.loadJson($.contextPath + '/zzcdda!getByQybm.json');
			var ccpch=$.loadJson($.contextPath + "/zzccxx!getCcpch.json");
			var ccbh=$.loadJson($.contextPath + "/zzccxx!getCcbm.json");
			var tm=$.loadJson($.contextPath + "/zzccxx!getTm.json");
			var zsm=$.loadJson($.contextPath + "/zzccxx!getZsm.json");
			ui.setFormData({A_CCBH:ccbh,A_QYBM:cdda.qybm,A_QYMC:cdda.qymc,A_PCH:ccpch,A_CCTMH:"ZZ"+tm,A_ZSM:zsm});
		}
 	} 
 	//根据种植批次号进行默认数据的加载
 	function defaultDataBy_zpxx(zzpch){
 		var obj=$.loadJson($.contextPath + "/zzzpxx!getZpxxFzr.json?zzpch="+zzpch);
		ui.setFormData({A_FZR:obj[0].FZR,A_FZRBH:obj[0].FZRBH,B_SYCT:obj[0].SYCT,B_SYCZ:obj[0].SYCZ,B_ZPRQ:obj[0].ZPRQ,B_SPMC:obj[0].SPMC,B_SYCTBH:obj[0].SYCTBH});
 	}
 	
  	ui.clickSecondDev = function (id){
 		if (id == $.config.preButtonId + "smbc") {//写码保存
				if(isSwt){
					var	jqForm = $("form", this.uiForm),
					formData;
					// 表单检验
					if (!jqForm.form("valid")) return false;
					// 获取表单数据
				    formData = jqForm.form("formData", false);
				    url = this.getAction() + "!save.json?P_tableId=" + this.options.tableId;
				    $.ajax({//保存数据库成功后写码
					    	url : url,
							type : "post",
							data : {E_entityJson: $.config.toString(formData)},
							dataType : "json",
							success : function(rlt) {
								 if (rlt!=null && rlt!="") {
							    	jqForm.form("loadData", rlt);
							    	var cdda=$.loadJson($.contextPath + '/zzcdda!getCddaByQybm.json');
							    	var detail = {
									    code : formData.A_CCTMH,
									    marketName : cdda.zzjdmc,
									    details : [{
									    	productName    : formData.B_SPMC, //商品名称
									    	placeProve     : formData.A_CDZMH, //产地证明号
									    	checkQualified : formData.A_JCHGZH,//检测合格证号
									    	outDate        : formData.A_CCRQ, //出场日期
									    	plotNo 		   : formData.B_SYCTBH,  //菜田编号
									    	weight 		   : formData.A_ZL,    //重量
									    	tracecode      : formData.A_ZSM  //追溯码 
									    }]
							    	}
							    	var result = _window("printDetailWithBarCode",JSON.stringify(detail));
									CFG_message("保存成功！", "success");
								} else {
									CFG_message("保存失败", "warning");
									return;
								}
							},
							error : function() {
								CFG_message("保存主从表数据失败！", "error");
								return;
							}
					    });// 
				 }else {
					$.alert("请在程序中操作");
				}  
		}
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
	if (ui.options.type == $.config.contentType.form) {
		if(isEmpty(ui.options.dataId)){
			ui.processItems = function (data) {
				var btns = [];
				btns.push({
					id:$.config.preButtonId + "smbc",
					label: "写码保存",
					type : "button"
				});
				for (var i = 0; i < data.length; i++) {
					btns.push(data[i]);
				}
				return btns;
			};
		}
	}
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
