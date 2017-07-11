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
	return $.contextPath + "/tzrpjcxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () { 
	//	return $.contextPath + "/appmanage/show-module";
	//};
	/**
	*	数据加载后执行
	*
	*/
	ui._init = function(){
		var jsonData = $.loadJson($.contextPath + '/tzqyda!getQyda.json?prefix=TZ');
		ui.setFormData({
			TZCMC:jsonData.tzcmc,
			TZCBM:jsonData.tzcbm
		});//屠宰场名称，屠宰场编码隐藏字段赋值
		
		//加载货主名称下拉列表
//		var jqHzmc = ui.getItemJQ("HZMC");
//		jqHzmc.combogrid("reload",$.contextPath +"/jyzxx!getJyzxx.json?xtlx=TZ&zt=1");
		var hzbmJQ=ui.getItemJQ("HZBM");
		var hzmcJQ=ui.getItemJQ("HZMC");
		var jqJyzh = ui.getItemJQ("SZCDJYZH");
        //动物产地检疫证号
        var szcdj = ui.getFormData().SZCDJYZH;
		hzbmJQ.combogrid('option','onChange',function(e,data){

			hzmcJQ.textbox('setValue',data.text);
			var jsonData = $.loadJson($.contextPath + "/tzrpjcxx!getJyzhGridByHzbm.json?hzbm="+data.newValue);
			//ui.setFormData({SZCDJYZH:""});
			jqJyzh.combogrid("reload",jsonData);
		})
		//加载生猪产地检疫证号下拉列表
		var hzbm = ui.getItemJQ("HZBM").combogrid("getValue");
		var jyzhData = $.loadJson($.contextPath + "/tzrpjcxx!getJyzhGridByHzbm.json?hzbm=" + hzbm);
		jqJyzh.combogrid("reload",jyzhData);
        ui.setFormData({SZCDJYZH:szcdj });
	}
	/**
	*绑定事件
	*
	*
	*/
	ui.bindEvent = function(){
		var jqJyzh = ui.getItemJQ("SZCDJYZH");//生猪产地检疫证号
		var jqHzmc = ui.getItemJQ("HZBM");//货主名称

		jqHzmc.combogrid("option","onChange",function(e,data){
			//debugger
			var newText = data.newText;
			var newValue = data.newValue;
			var jsonData = $.loadJson($.contextPath + "/tzrpjcxx!getJyzhGridByHzbm.json?hzbm="+newValue);
			ui.setFormData({SZCDJYZH:""});
			jqJyzh.combogrid("reload",jsonData);
			ui.setFormData({HZMC:newText,HZBM:newValue});
		});
		jqJyzh.combogrid("option","onChange",function(e,data){//带入检疫证进场数量
			var newText = data.newText;
			var newValue = data.newValue;
			var jsonData = $.loadJson($.contextPath + "/tzrpjcxx!getSzjyxx.json?szcdjyzh="+newText);
			ui.setFormData({SZCDJYZH:newText,HZMC:jsonData[0],HZBM:jsonData[1]});
		});
	}
	/**
	*   回调函数
	*
	*
	*/
	ui.addCallback("setComboGridValue_Hzmc",function(o) {//货主名称赋值
		if(null == o) return;
		var rowData = o.result;

		if(null == rowData) return;
		var jqJyzh = ui.getItemJQ("SZCDJYZH");
		var jsonData = $.loadJson($.contextPath + "/tzrpjcxx!getJyzhGridByHzbm.json?hzbm="+rowData.B_JYZBM);
		ui.setFormData({SZCDJYZH:""});
		jqJyzh.combogrid("reload",jsonData);
		ui.setFormData({
			HZMC:rowData.A_JYZMC,
			HZBM:rowData.A_JYZBM
		});
	});
	ui.addCallback("setComboGridValue_Jyzh",function(o) {//货主名称赋值
		if(null == o) return;
		var rowData = o.result;
		if(null == rowData) return;
		ui.setFormData({SZCDJYZH:rowData.SZCDJYZH,HZMC:rowData.HZMC,HZBM:rowData.HZBM});
	});
	/**
	*   出参方法
	*
	*/
	ui.addOutputValue("setTcsHzmc",function(o){//弹出式进场理货编码
		var tzcbm = ui.getItemJQ("TZCBM").textbox("getValue");
		var o = {
			status : true,
			P_columns : "EQ_C_BALTJDBM≡"+tzcbm
		}
		return o;
	});
	ui.addOutputValue("setTcsJyzh",function(o){//弹出式进场理货编码
		var tzcbm = ui.getItemJQ("TZCBM").textbox("getValue");
		var hzbm = ui.getItemJQ("HZBM").combogrid("getValue");
		var o = {
			status : true,
			P_columns : "(EQ_C_TZCBM≡"+tzcbm+")AND(LIKE_C_HZBM≡"+hzbm+")"
		}
		return o;
	});
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
