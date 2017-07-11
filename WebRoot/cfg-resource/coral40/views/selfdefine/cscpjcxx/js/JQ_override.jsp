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
	return $.contextPath + "/cscpjcxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
var barCode;
	ui._init = function (){
		if(isEmpty(ui.options.dataId)){
			//初始化零售市场名称和编码
			var cs= $.loadJson($.contextPath +"/csscjcxx!initFormData.json");
			ui.setFormData({CSBM:cs.csbm,CSMC:cs.csmc});
		}
		//初始化经营者信息
		var jyzmc = ui.getItemJQ("JYZMC");
		var jyzData = $.loadJson($.contextPath +"/jyzxx!getJyzxx.json?xtlx=CS&zt=1");
		jyzmc.combogrid("reload",jyzData.data);
		//修改时设定经营者名称
		if(!isEmpty(ui.options.dataId)){		
		var jyzData = $.loadJson($.contextPath +"/cscpjcxx!getJyzById.json?id="+ui.options.dataId);
		jyzmc.combogrid("setValue",jyzData.JYZMC);
		}

		var zzlJQ = ui.getItemJQ("ZZL");
		zzlJQ.textbox("destroy");
		zzlJQ.textbox({
			maxlength:13,
			buttons:[{'innerRight':[{label:'公斤'}]}],
			pattern:'//^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$//'
		});

	}
	/**
	 * 设置经营者列表过滤条件
	 */
	ui.addOutputValue("setJyzColumns",function(o){
		var o = {
			status : true,
			P_columns : ""
		}
		return o;
	});
	//设置经营者下拉列表
	ui.addCallback("setComboGridValue_Jyzmc",function(o){
		if( null == o ) return ;
		var obj = o.rowData;
		if( null == obj ) return ;
		ui.setFormData({JYZMC:obj.A_JYZMC,JYZBM:obj.A_JYZBM});
	});
	ui.bindEvent = function (){
		//批发商名称下拉列表
		var jyzmc = ui.getItemJQ("JYZMC");
		jyzmc.combogrid("option","onChange",function(e,data){
			var text = data.text;
			var value = data.value;
			ui.setFormData({JYZMC:text,JYZBM:value});
		});
	}

	ui.clickSecondDev = function(id){
		if (id==$.config.preButtonId+"inputBarCode") {
			showDialog();
		}

	}

	function showDialog(){
		var _this = ui;
		var jqGlobal = $(ui.options.global);
		var jqUC = $("<div id=\"jqUC\"></div>").appendTo(jqGlobal);
		jqUC.dialog({
			appendTo : jqGlobal,
			modal : true,
			title : "请输入条码",
			width : 240,
			height : 80,
			resizable : false,
			position : {
				at: "center top+200"
			},
			onClose : function() {
				jqUC.remove();
				jqUC.remove();
			},
			onCreate : function() {
				var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\"></div>").appendTo(this);
				var jq = $("<input id=\"UNTREAD_OPINION_" + _this.uuid + "\" name=\"opinion\"></textarea>").appendTo(jqDiv);
				jq.textbox({width: 200});
			},
			onOpen : function() {
				_this.close(false);
				var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
						jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
				jqDiv.toolbar({
					data: ["->", {id:"sure", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
					onClick: function(e, ui) {
						if ("sure" === ui.id) {
							barCode=$("#UNTREAD_OPINION_" + _this.uuid).val();
							setFormData(barCode);
							_this.close(jqUC);
							$("#UNTREAD_OPINION_" + _this.uuid).remove();
						} else {
							_this.close(jqUC);
							$("#UNTREAD_OPINION_" + _this.uuid).remove();
						}
					}
				});
			}
		});
	}
	function setFormData(barCode){
		if(barCode.length != 22){
			CFG_message("请输入有效的条形码!", "warning");
			return;
		}
		var prefix = barCode.substring(0,2);
		if("JG" === prefix){
			var jsonData = $.loadJson($.contextPath + '/cscpjcxx!getJgcpchxx.json?jgtmh='+barCode);
			if ("ERROR" === jsonData) {
				CFG_message("请输入有效的条形码!", "warning");
			}else{
				//加载主表数据
				ui.setFormData({
					ZSPZH: jsonData.ZSM,
					CPMC: jsonData.CPMC,
					CPBH: jsonData.CPBH,
					BZGG: jsonData.CCBZGG,
					BZSL: jsonData.CCBZSL,
					ZZL: jsonData.CPZZL,
					JGCMC: jsonData.JGCMC,
					JGCBM: jsonData.QYBM,
					JHPCH: jsonData.PCH
				});
			}
		}else {
			CFG_message("请输入有效的条形码!", "warning");
			return;
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
	if(ui.options.type == $.config.contentType.form){
		ui.processItems = function (data) {
			var btns = [];
			btns.push('->',{
				id:$.config.preButtonId + "inputBarCode",
				label: "输入条码",
				type : "button"
			});
			for (var i = 2; i < data.length; i++) {//忽略'','->'
				btns.push(data[i]);
			}
			return btns;
		};
	}


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
