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
	return $.contextPath + "/prjcxx2";
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
	ui.processDefaultValue=function(){
		var pfscbm = ui.getItemJQ("PFSCBM");
		var pfscmc = ui.getItemJQ("PFSCMC");
		if(ui.options.dataId ==null||ui.options.dataId==""){
			$.ajax({
				type:"post",
				url:$.contextPath+"/prjcxx2!getInitformData.json",
				dataType:"json",
				success:function(data){
					pfscbm.textbox("setValue",data.PFSCBM);
					pfscmc.textbox("setValue",data.PFSCMC);
				}
			})
		}
		var jsonData=$.loadJson($.contextPath +"/jyzxx!getJyzxx.json?xtlx=PR&zt=1");
		var pfsmc = ui.getItemJQ("PFSMC");
//		pfsmc.combogrid("reload",jsonData.data);
	}
	var zspzhValue="";//追溯凭证号值
	ui.bindEvent = function(){
		var pfsmc = ui.getItemJQ("PFSMC");	//批发商名称
		var pfsbm = ui.getItemJQ("PFSBM");	//批发商编码
		var zspzh = ui.getItemJQ("ZSPZH");	//追溯凭证号
		var jcy = ui.getItemJQ("JCY");		//检测员
		var jcybh = ui.getItemJQ("JCYBH");	//检测员编码
		var spmc = ui.getItemJQ("SPMC");	//商品名称
		var spbm = ui.getItemJQ("SPBM");	//商品编码
		var jhpch = ui.getItemJQ("JHPCH");	//商品编码
		pfsbm.combogrid({
			onChange:function(e,data){
				pfsmc.textbox("setValue",data.text);
//			pfsbm.combogrid("setValue",pfsmc.combogrid("getText"));
				ui.setFormData({ZSPZH:""});
				//加载追溯凭证号数据
				zspzh.combogrid("reload",$.contextPath + '/prjcxx2!getZspzhGrid.json?pfsbm='+ pfsbm.combogrid("getValue"));
			}
		});
//		pfsmc.combogrid({onChange:function(){
//			pfsmc.textbox("setValue",pfsmc.combogrid("getValue"));
////			pfsbm.combogrid("setValue",pfsmc.combogrid("getText"));
//			ui.setFormData({ZSPZH:""});
//			//加载追溯凭证号数据
//			zspzh.combogrid("reload",$.contextPath + '/prjcxx2!getZspzhGrid.json?pfsbm='+ pfsbm.combogrid("getValue"));
//		}});
		jcybh.combogrid({
			onChange:function(e,data){
				jcy.textbox("setValue",data.text);
//				jcybh.combogrid("setValue",jcy.combogrid("getText"));
		}});
		spmc.combogrid({onChange:function(){
			spbm.textbox("setValue",spmc.combogrid("getValue"));
			spmc.combogrid("setValue",spmc.combogrid("getText"));
		}});
		zspzh.combogrid({onChange:function(){
			spmc.combogrid("setValue","");
			zspzhValue = zspzh.combogrid("getValue");
			spmc.combogrid("reload",$.contextPath+"/tcsprjcspxx!getJcspxxGrid.json?zspzh="+zspzhValue)
			var jhpchValue = $.loadJson($.contextPath+"/tcsprjcspxx!getJhpch.json?zspzh="+zspzhValue);
			jhpch.textbox("setValue",jhpchValue);
			var jyzxx = $.loadJson($.contextPath + '/prjcxx2!getJyzxxByZspzh.json?zspzh=' +zspzhValue);
			ui.setFormData({PFSMC:jyzxx.PFSMC,PFSBM:jyzxx.PFSBM});
		}})
	}
	/*回调函数为下拉列表赋值*/
	 ui.addCallback("setCombogridValue_Jyzmc",function(o){
			var pfsbm = ui.getItemJQ("PFSBM");
			var pfsmc = ui.getItemJQ("PFSMC");
			var zspzh = ui.getItemJQ("ZSPZH");
			if (null == o) return;
			var rowData = o.result;
			if (null == rowData) return;
			pfsmc.textbox("setValue",rowData.B_JYZBM);
			pfsbm.combogrid("setValue",rowData.A_JYZMC);
			ui.setFormData({ZSPZH:""});
			//加载追溯凭证号数据
			zspzh.combogrid("reload",$.contextPath + '/prjcxx2!getZspzhGrid.json?pfsbm='+ pfsbm.combogrid("getValue"));
		});
		//追溯凭证号回调
	 ui.addCallback("setCombogridValue_Zspzh",function(o){
			if (null == o) return;
			var rowData = o.result;
			if (null == rowData) return;
			var zspzh = ui.getItemJQ("ZSPZH");
			zspzh.combogrid("setValue",rowData.ZSPZH);
			//批发商信息
			var jyzxx = $.loadJson($.contextPath + '/prjcxx2!getJyzxxByZspzh.json?zspzh=' +rowData.ZSPZH);
			ui.setFormData({PFSMC:jyzxx.PFSMC,PFSBM:jyzxx.PFSBM});
		});
		//检测员回调
	 ui.addCallback("setCombogridValue_Jcy",function(o){
			if (null == o) return;
			var rowData = o.result;
			if (null == rowData) return;
			var jcybh = ui.getItemJQ("JCYBH");
		 	jcybh.combogrid("setValue",rowData.GZRYBH);
		 	var jcy = ui.getItemJQ("JCY");
		 	jcy.textbox("setValue",rowData.MC);
		});
		//商品信息回调
	 ui.addCallback("setCombogridValue_Jcspxx",function(o){
			if (null == o) return;
			var rowData = o.result;
			if (null == rowData) return;
			var spmc = ui.getItemJQ("SPMC");
			spmc.combogrid("setValue",rowData.SPMC);
		});
		
		/*出参 列表过滤条件*/
		ui.addOutputValue("setJyzzt",function(o){//弹出式经营者状态过滤
			var pfscbm = ui.getItemJQ("PFSCBM");
			var baltjdbm = pfscbm.textbox("getValue");
			var o = {
				status : true,
				P_columns : "(EQ_C_TZCBM≡"+baltjdbm+")"
			}
			return o;
		}); 
		ui.addOutputValue("setPfscbm",function(o){//弹出式追溯凭证号列表过滤
			var pfscbm = ui.getItemJQ("PFSCBM").textbox("getValue");
			var pfsbm = ui.getItemJQ("PFSBM").combogrid("getValue");
			var o = {
				status : true,
				P_columns : "(EQ_C_ZT≡1) AND (EQ_C_PFSCBM≡"+pfscbm+")AND (LIKE_C_PFSBM≡"+pfsbm+")"
			}
			return o;
		}); 
		ui.addOutputValue("setTzcbm",function(o){//弹出式检测员列表过滤
			var pfscbm = ui.getItemJQ("PFSCBM").textbox("getValue");
			var o = {
				status : true,
				P_columns : "(EQ_C_TZCBM≡"+pfscbm+")"
			}
			return o;
		}); 
		ui.addOutputValue("setJcspxx",function(o){//弹出式商品信息列表过滤
			var pfscbm = ui.getItemJQ("PFSCBM").textbox("getValue");
			var P_columns = "(EQ_C_ZT≡1) AND (EQ_C_PFSCBM≡"+pfscbm+")";
			if(zspzhValue!=""&&zspzhValue!=null){
				P_columns +=" AND (EQ_C_ZSPZH≡"+zspzhValue+")";
			}
			var o = {
				status : true,
				P_columns : "("+P_columns+")"
			}
			return o;
		}); 
};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	ui.addOutputValue("setTptableIdColumns", function(o){
		var ids = ui.selectedRowId;
		var tableId = $.loadJson($.contextPath +'/tyttgj!getTpTableId.json?pTableId='+ui.options.tableId);
		o={
			status:true,
			P_tableId : tableId,
			P_columns : "EQ_C_ZBID≡"+ids
		};
		return o;

	})
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
