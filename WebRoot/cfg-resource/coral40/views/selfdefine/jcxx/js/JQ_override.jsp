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
window[CFG_actionName(subffix)] = function () {
	// this.assembleData 就是 configInfo
	return $.contextPath + "/jcxx";
};

var spbm_;
var pfsbm_;
/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {

    ui.beforeSave = function(){
		if(!isEmpty(ui.options.dataId)){
			ui.setFormData({SPBM:spbm_,PFSBM:pfsbm_});
		}
        return true;
    }

	ui._init = function(){
		var jqJclhbh = ui.getItemJQ("JCLHBH");
		var jqSpmc = ui.getItemJQ("SPBM");
		jqSpmc.combogrid('disable');
		var jsonData = null;
		var jclhbhData = $.loadJson($.contextPath + "/jcxx!getJclhbhByPfsbm.json?pfsbm=");
		var spmcData = $.loadJson($.contextPath + "/jcxx!getSpmcByPid.json?jclhid=");
		var jsonData=$.loadJson($.contextPath +'/qyda!getQyda.json?prefix=PC');
		ui.setFormData({
				PFSCBM:jsonData.pfscbm,
				PFSCMC:jsonData.pfscmc
		});
		jqJclhbh.combogrid("reload",jclhbhData);
		jqSpmc.combogrid("reload",spmcData.data);
		var pfsbm = ui.getItemJQ("PFSBM");
		var jsonData=$.loadJson($.contextPath +"/jyzxx!getJyzxx.json?zt=1&xtlx=PC");
		pfsbm.combogrid("reload",jsonData.data);
        //如果是修改操作则需要加载
        if(!isEmpty(ui.options.dataId)){
            //ajax请求获得对应理货编号的记录ID
//            var lhbh=jqJclhbh.combogrid("getValue");
            var lhbh= $.loadJson($.contextPath + '/jcxx!getLhbhById.json?id=' + ui.options.dataId);
            var spmcJQ = ui.getItemJQ("SPMC");
            var pfsmcJQ = ui.getItemJQ("PFSMC");
            var pfsbmJQ = ui.getItemJQ("PFSBM");
            var spmc = spmcJQ.val();
            var pfsmc = pfsmcJQ.val();
            spbm_ = jqSpmc.combogrid("getValue");
            pfsbm_ = pfsbmJQ.combogrid("getValue");
            ui.setFormData({SPBM:spmc,PFSBM:pfsmc,JCLHBH:lhbh});
            var rowData = $.loadJson($.contextPath + "/jcxx!getJclhxxpid.json?jclhbh=" +lhbh);
            jsonData =  $.loadJson($.contextPath + "/jcxx!getSpmcByPid.json?jclhid=" +rowData.PID);
            jqSpmc.combogrid("reload",jsonData.data);
            jqJclhbh.combogrid("disable");
            pfsbmJQ.combogrid("disable");
        }
	}
	
	ui.bindEvent = function(){
		var jqPfsmc = ui.getItemJQ("PFSBM");
		var jqJclhbh = ui.getItemJQ("JCLHBH");
		var jqSpmc = ui.getItemJQ("SPBM");
		var jqJcy = ui.getItemJQ("JCYBH");
		/**
		*批发商名称，进场理货编号，商品名称onChange时间
		*/
		jqPfsmc.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			var jsonData = $.loadJson($.contextPath + "/jcxx!getJclhbhByPfsbm.json?pfsbm="+newValue);
			ui.setFormData({JCLHBH:"",SPMC:""});
			//jqJclhbh.combogrid("clear");
			jqJclhbh.combogrid("reload",jsonData);
            pfsbm_ = newValue;
			jqSpmc.combogrid('setValue','');
			jqSpmc.combogrid('disable');
			ui.setFormData({PFSMC:newText,PFSBM:newValue});
		});
		jqJclhbh.combogrid("option","onChange",function(e,data){
			var newText = data.text;//进场理货编码
			var newValue = data.value;//ID
			var spmcData = $.loadJson($.contextPath + "/jcxx!getSpmcByPid.json?jclhid=" +newValue);
			var pfsmcData = $.loadJson($.contextPath + "/jcxx!getPfsmcByJclhbh.json?jclhbh=" + newText);
			var pfsmc = pfsmcData[0];
			ui.setFormData({SPMC:""});
			jqSpmc.combogrid('enable');
			jqSpmc.combogrid("reload",spmcData.data);
			ui.setFormData({JCLHBH:newText});//表单赋值 进场理货编号
			ui.setFormData({PFSMC:pfsmc.PFSMC,PFSBM:pfsmc.PFSBM});
		});
		jqSpmc.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
            spbm_ = newValue;
			ui.setFormData({SPMC:newText,SPBM:newValue});
		});
		jqJcy.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			ui.setFormData({JCY:newText,JCYBH:newValue});
		});
	}
	
	ui.addCallback("setComboGridValue_Pfsmc", function(o){
		var jqJclhbh = ui.getItemJQ("JCLHBH");
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
		var jsonData = $.loadJson($.contextPath + "/jcxx!getJclhbhByPfsbm.json?pfsbm="+obj.A_JYZBM);
		ui.setFormData({JCLHBH:"",SPMC:""});
		jqJclhbh.combogrid("reload",jsonData);
 		ui.setFormData({PFSBM:obj.A_JYZBM,PFSMC:obj.A_JYZMC});
        pfsbm_ = obj.A_JYZBM;
 	}); 
	ui.addCallback("setComboGridValue_spmc", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setFormData({SPMC:obj.SPMC,SPBM:obj.SPBM});
        spbm_ = obj.SPBM;
 	});
	ui.addCallback("setComboGridValue_Jclhbh", function(o){
		if (null == o) return;
		var rowData = o.rowData;
		if (null == rowData) return;
		var jqSpmc = ui.getItemJQ("SPBM");
		//加载批发商信息
		var pfsmcData = $.loadJson($.contextPath + "/jcxx!getPfsmcByJclhbh.json?jclhbh=" + rowData.JCLHBH);
		var pfsmc = pfsmcData[0];
		ui.setFormData({PFSMC:pfsmc.PFSMC,PFSBM:pfsmc.PFSBM});
		ui.setFormData({JCLHBH:rowData.JCLHBH});
		//加载商品名称下拉框数据
		var jsonData = $.loadJson($.contextPath + "/jcxx!getSpmcByPid.json?jclhid=" +rowData.ID);
		ui.setFormData({SPMC:""});
		jqSpmc.combogrid("reload",jsonData.data);
 	});
	ui.addCallback("setComboGridValue_Jcry", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setItemValue("JCYBH",obj.GZRYBH);
 		ui.setItemValue("JCY",obj.XM);
 	});
	
	ui.addOutputValue("setTcsJclhbh",function(o){
		var pfsbm = ui.getItemJQ("PFSBM").combogrid("getValue");
		var o = {
			status : true,
			P_columns : "LIKE_C_PFSBM≡"+pfsbm
		}
		return o;
	});
	ui.addOutputValue("setTcsSpmc",function(o){
		var jclhbh = ui.getItemJQ("JCLHBH").combogrid("getValue");
		var lhbhId;
		var jsonData = $.loadJson($.contextPath + "/jcxx!getIdByJclhbh.json?jclhbh=" +jclhbh);
		var o = {
			status : true,
			P_columns : "LIKE_C_PID≡"+jsonData
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
 * 在此可以复写所有自定义JS类
 * @param selector
 * @returns {JQ_override}
 */
window[CFG_overrideName(subffix)] = function () {
	if (this instanceof $.config.cform) {
		_override_form(this);
	} else if (this instanceof $.config.cgrid) {
		_override_grid(this);
	} else if (this instanceof $.config.ctree) {
		_override_tree(this);
	} else if (this instanceof $.config.ctbar) {
		_override_tbar(this);
	}
};

	
	
	
	
})("${timestamp}");
</script>
