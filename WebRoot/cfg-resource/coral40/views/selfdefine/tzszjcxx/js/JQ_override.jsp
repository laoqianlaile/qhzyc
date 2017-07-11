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
	return $.contextPath + "/tzszjcxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	var barCode = "";
	//数据加载后执行
	ui._init = function(){
		var jsonData = $.loadJson($.contextPath + '/tzqyda!getQyda.json?prefix=TZ');
		ui.setFormData({
			TZCMC:jsonData.tzcmc,
			TZCBM:jsonData.tzcbm
		});//屠宰场名称，屠宰场编码隐藏字段赋值
		
		var jqHzmc = ui.getItemJQ("HZBH");
		var jsonData = $.loadJson($.contextPath +"/jyzxx!getJyzxx.json?xtlx=TZ&zt=1");
		jqHzmc.combogrid("reload",jsonData.data);
	}
	
	ui.clickSave = function(op){
		//校验检疫证进场数量大于实际进场数量与途亡数量之和
		var jsonData = $.loadJson($.contextPath +'/tzszjcxx!getszjcxx.json?szcdjyzh=' + ui.getItemJQ("SZCDJYZH").textbox("getValue"));
		var jyzjcsl = ui.getItemJQ("JYZJCSL").textbox("getValue");
		var sjjcsl = ui.getItemJQ("SJJCSL").textbox("getValue");
		var twsl = ui.getItemJQ("TWSL").textbox("getValue");
		var jqJyzh = ui.getItemJQ("SZCDJYZH").textbox("getValue");
		if (!$.isNumeric(jyzjcsl)) jyzjcsl = 0;
		if (!$.isNumeric(sjjcsl)) sjjcsl = 0;
		if (!$.isNumeric(twsl)) twsl = 0;
		if(parseFloat(jyzjcsl, 10)<(parseFloat(sjjcsl, 10)+parseFloat(twsl, 10))){
			$.message({message: "检疫证进场数量小于实际进场数量和途亡数量，请核查后重新填写!", cls: "warning"});
				return false;
		}
		if(null!=jsonData && !$.isEmptyObject(jsonData) && jqJyzh == jsonData[0].SZCDJYZH){//判断检疫证号不能为同一个
				$.message({message:"检疫证号不能为同一个"});
				return false;
        }
		
		//保存

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
			data : {
				E_entityJson: $.config.toString(formData),
				barCode : barCode == null ? "" : barCode
		    },
			dataType : 'json',
			success : function(entity) {
				if (false === entity.success || !("ID" in entity)) {
					if (isEmpty(entity.message)) CFG_message("操作失败！", "warning");
					else CFG_message(entity.message, "warning");
					return;
				}
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
	
	ui.bindEvent = function(){
		var jqHzmc = ui.getItemJQ("HZMC");
		var jqCdmc = ui.getItemJQ("CDMC");
		jqHzmc.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			ui.setFormData({HZMC:newText,HZBM:newValue});
		});
		jqCdmc.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			ui.setFormData({CDMC:newText,CDBM:newValue});
		});
	}
	
	//回调函数
	ui.addCallback("setComboGridValue_Hzmc",function(o) {//货主名称赋值
		if(null == o) return;
		var rowData = o.result;
		if(null == rowData) return;
		ui.setFormData({
			HZMC:rowData.A_JYZMC,
			HZBM:rowData.A_JYZBM
		});
	});
	
	ui.addCallback("setComboGridValue_Cdmc",function(o){//产地信息赋值
		if(null == o) return;
		var rowData = o.result;
		if(null == rowData) return;
		ui.setFormData({CDMC:rowData.CDMC,CDBM:rowData.CDBM});
	});
	
	/**
	*	二次开发按钮
	*
	*
	*/
	ui.clickSecondDev = function(id){
		if(id==$.config.preButtonId+"inputBarCode"){
			showDialog();
		}	
	};
	
	/**
	*	弹出式货主名称过滤
	*
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
	function showDialog(){
		var _this = ui;
		var jqGlobal = $(ui.options.global);
		var jqUC = $("<div id=\"jqUC\"></div>").appendTo(jqGlobal);
		jqUC.dialog({
			appendTo : jqGlobal,
			modal : true,
			title : "请输入条码",
			width : 300,
			height : 80,
			resizable : false,
			position : {
				at: "center top+200"
			},
			onClose : function() {
				jqUC.remove();
			},
			onCreate : function() {
				var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\"></div>").appendTo(this);
				var jq = $("<input id=\"UNTREAD_OPINION_" + _this.uuid + "\" name=\"opinion\"></textarea>").appendTo(jqDiv);
				jq.textbox({width: 200});
				jq.textbox("option","onKeyDown",function( e,data){
					if(e.keyCode =='13'){//添加回车事件
						$('#sure').trigger("click");
					}
				});
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
						} else {
							_this.close(jqUC);
						}
                        $("#UNTREAD_OPINION_" + _this.uuid).remove();
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
		if(prefix != "YZ"){
			CFG_message("请输入有效的条形码!", "warning");
			return;
		}
		var jsonData = $.loadJson($.contextPath + '/tzszjcxx!getJcpcxx.json?barCode=' + barCode);
		if ("ERROR" === jsonData) {
			CFG_message("请输入有效的条形码!", "warning");
		}else {
			ui.setFormData({
				SZCDJYZH:jsonData.SZCDJYZH,
				JYZJCSL:jsonData.SL,
				SJJCSL:jsonData.SL,
				CDMC:jsonData.CDMC,
				YZCMC:jsonData.YZCMC,
				YSCPH:jsonData.YSCPH,
				JCPCH:jsonData.PCH,
				JYPZH:jsonData.ZSM,
				CDBM:jsonData.CDBM,
				SJJCZL:jsonData.ZZL

			})
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
	// ui.assembleData 就是 configInfo
	//console.log("override tbar!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
	if (isEmpty(ui.options.model)) {
		ui.processItems = function (data) {
			var btns = [];
			btns.push({
				id:$.config.preButtonId + "inputBarCode",
				label: "输入条码",
				type : "button"
			});
			for (var i = 0; i < data.length; i++) {
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
