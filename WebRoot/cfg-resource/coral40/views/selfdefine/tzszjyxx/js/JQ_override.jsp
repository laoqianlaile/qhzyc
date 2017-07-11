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
	return $.contextPath + "/tzszjyxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
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
		//alert(ui.getItemJQ("TZCMC").textbox("getValue") + ui.getItemJQ("TZCBM").textbox("getValue"));
		
		//加载货主名称下拉列表
		var jqHzmc = ui.getItemJQ("HZMC");
		var jsonData = $.loadJson($.contextPath+'/jyzxx!getJyzxx.json?xtlx=TZ&zt=1');
//		jqHzmc.combogrid("reload",jsonData.data);
		
		//加载生猪产地检疫证号下拉列表
		var jqJyzh = ui.getItemJQ("SZCDJYZH");
		var jyzhData = $.loadJson($.contextPath + "/tzszjyxx!getSzjyByHzbm.json?hzbm=" + ui.getItemJQ("HZBM").combogrid("getValue"));
		jqJyzh.combogrid("reload",jyzhData);
		
		//加载检疫员下拉列表
		var jqJyy = ui.getItemJQ("JYY");
		var jyyData = $.loadJson($.contextPath + "/tzszjyxx!getJyyGrid.json");
		jqJyy.combogrid("reload",jyyData);
	}
	/**
	*绑定事件
	*
	*
	*/
	ui.bindEvent = function(){
		var jqJyzh = ui.getItemJQ("SZCDJYZH");//生猪产地检疫证号
		var jqJyy = ui.getItemJQ("JYY");//检疫员
		var jqHzmc = ui.getItemJQ("HZBM");//货主名称
		var jqYxts = ui.getItemJQ("YXTS");//阳性头数
		var jqCyts = ui.getItemJQ("CYTS");//采样头数
		jqHzmc.combogrid("option","onChange",function(e,data){
			var newText = data.newText;
			var newValue = data.newValue;
			var jsonData = $.loadJson($.contextPath + "/tzszjyxx!getSzjyByHzbm.json?hzbm="+newValue);
			ui.setFormData({SZCDJYZH:"",JYZJCSL:""});
			jqJyzh.combogrid("reload",jsonData);
			ui.setFormData({HZMC:newText,HZBM:newValue});
		});
		jqJyzh.combogrid("option","onChange",function(e,data){//带入检疫证进场数量
			var newText = data.newText;
			var newValue = data.newValue;
			var jsonData = $.loadJson($.contextPath + "/tzszjyxx!getSzjcxx.json?szcdjyzh="+newText);
			ui.setFormData({
				SZCDJYZH:newText,
				JYZJCSL:jsonData[2],
				HZMC:jsonData[0],
				HZBM:jsonData[1],
				JCPCH:jsonData[3]
			});
		});
		jqJyy.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			ui.setFormData({JYY:newText,JYYBH:newValue});
		});
		jqYxts.textbox({
			onKeyUp: function (e, data) {
				var cyts = jqCyts.textbox("getValue");
				if (data.value > cyts) {
					jqYxts.textbox("setValue", "");
					$.alert("阳性头数不可大于采样头数");
				}
				else if (cyts == null || cyts=="") {
					jqYxts.textbox("setValue", "");
				}else if(cyts == 0){
					jqYxts.textbox("setValue",0);
				}
			}


		});
		jqCyts.textbox({
			onKeyUp: function(e,data){
				var yxts=jqYxts.textbox("getValue");
                if(data.value<yxts){
					$.alert("采样头数不可少于阳性头数");
					jqYxts.textbox("setValue", "");
				}
			}
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
		var jsonData = $.loadJson($.contextPath + "/tzszjyxx!getSzjyByHzbm.json?hzbm="+rowData.B_JYZBM);
		ui.setFormData({SZCDJYZH:"",JYZJCSL:""});
		jqJyzh.combogrid("reload",jsonData);
		ui.setFormData({
			HZMC:rowData.A_JYZMC,
			HZBM:rowData.A_JYZBM
		});
	});
	
	ui.addCallback("setComboGridValue_Szcdjyzh",function(o) {//货主名称赋值
		if(null == o) return;
		var rowData = o.result;
		if(null == rowData) return;
		ui.setFormData({
			SZCDJYZH:rowData.SZCDJYZH,
			JYZJCSL:rowData.JYZJCSL,
			HZMC:rowData.HZMC,
			HZBM:rowData.HZBM,
			JCPCH:rowData.JCPCH
		});
	});
	
	ui.addCallback("setComboGridValue_Jyy",function(o) {//货主名称赋值
		if(null == o) return;
		var rowData = o.result;
		if(null == rowData) return;
		ui.setFormData({
			JYY:rowData.XM,
			JYYBH:rowData.GZRYBH
		});
	});
	
	/**
	*   出参方法
	*
	*/
	ui.addOutputValue("setTcsJyy",function(o){//弹出式进场理货编码
		var tzcbm = ui.getItemJQ("TZCBM").textbox("getValue");
		var o = {
			status : true,
			P_columns : "EQ_C_TZCBM≡"+tzcbm
		}
		return o;
	});
	ui.addOutputValue("setTcsHzmc",function(o){//弹出式进场理货编码
		var tzcbm = ui.getItemJQ("TZCBM").textbox("getValue");
		var o = {
			status : true,
			P_columns :  "(EQ_C_BALTJDBM≡"+tzcbm+") AND (EQ_C_ZT≡1)"
		}
		return o;
	});
	ui.addOutputValue("setTcsJyz",function(o){//弹出式进场理货编码
		var tzcbm = ui.getItemJQ("TZCBM").textbox("getValue");
		var hzbm = ui.getItemJQ("HZBM").combogrid("getValue");
	
		var o = {
			status : true,
			P_columns : "(EQ_C_TZCBM≡"+tzcbm+")AND(LIKE_C_HZBM≡"+hzbm+")AND(EQ_C_JYZT≡1)"
		}
		return o;
	});
	/**
	*	重写保存方法
	*
	*/
	ui.clickSave = function(op){
		var _this = this, formUI = $("form", this.uiForm), 
		    url = this.getAction() + "!save.json?P_tableId=" + this.options.tableId + "&P_op=" + op + "&P_menuCode=" + CFG_getInputParamValue(this.assembleData, "menuCode"),
		    formData;

		if (!formUI.form("valid")) return;
		// 保存前回调方法
		if (_this.processBeforeSave(formUI, op) === false) return;
		// 获取表单数据
	    formData = formUI.form("formData", false);
	    
		$.ajax({
			type : "post",
			url : url,
			data : {E_entityJson: $.config.toString(formData)},
			dataType : 'json',
			success : function(entity) {
				if (false === entity.success || !("ID" in entity)) {
					if (isEmpty(entity.message)) CFG_message("操作失败！", "warning");
					else CFG_message(entity.message, "warning");
					return;
				}
				//更改生猪进场信息检疫状态为已检疫
				$.ajax({
					type : "post",
					data : {Jyzh:formData.SZCDJYZH},
					url : $.contextPath + '/tzszjyxx!setJyzt.json',
					success : function(data){
					},
					error : function() {
						return;
					}
				});
				/*
				 * if (_this.hasGrid) { $("#" +
				 * _this.options.gDivId).grid("reload"); } else if
				 * (_this.options.model) { }
				 */
				// _this.reloadSelfGrid();
				if ("save" === op) {
					formUI.form("loadData", entity);
				} else if ("close" === op) {
					if (!_this.isNested) _this.uiForm.dialog("close");
					else _this.clickBackToGrid();
				} else if ("create" === op) {
					_this.clickCreate();
				}
				// 保存后回调方法
				_this.processAfterSave(entity, op);
				CFG_message("操作成功！", "success");
			},
			error : function() {
				CFG_message("操作失败！", "error");
			}
		});
	}
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
