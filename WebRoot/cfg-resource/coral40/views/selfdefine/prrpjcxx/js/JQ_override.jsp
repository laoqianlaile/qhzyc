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
	return $.contextPath + "/prrpjcxx";
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
	var barCode = "";
	
	/**
	*	二次开发按钮
	*
	*/
	ui.clickSecondDev = function(id){
		if (id==$.config.preButtonId+"inputBarCode") {
			showDialog();
		}
		
		if (id==$.config.preButtonId+"readCard") {
			/**
			 * 从卡内读取理货卡信息在表单上显示
			 *
			 *a
			 */
		 	if (isSwt) {
				//var result = _window("readAllRecords", "businessDetail");
				//var userInfo = _window("readCard","userInfo");
				var result = _window("readUserInfoAndBusinessDetail");
				var userInfoAndBusinessDetail = JSON.parse(result);
				
				var userValue = userInfoAndBusinessDetail.user;
				var value = userInfoAndBusinessDetail.records;
				
				if (userInfoAndBusinessDetail.status == "true" || userInfoAndBusinessDetail.status == true) {
					if (userValue.cardType === "1") {//判断卡类型
						//填入卡内用户信息
						ui.setFormData({
							PFSMC:userValue.userName,
							PFSBM:userValue.userId
						});
						//var cGrid = ui.getDetailGrid();
						//var cForm = ui.getDetailForm();
						for(var i = 0; i<1;i++){
							var detailData = value[i],zl,dj;
							var spmc = $.loadJson($.contextPath+"/rpspxx!getSpmc.json?spbm=" + detailData.productCode);
							ui.setFormData({
								SPMC:spmc,
								SPBM:detailData.productCode,
								ZL:detailData.dealWeight,
								DJ:detailData.dealPrice,
								ZSPZH:detailData.tracecode,
								JHPCH:detailData.batchCode,//批次号
								SPBM:detailData.productCode,
								GHPCSCBM:detailData.sellerCode,
								GHPCSCMC:detailData.sellerName,
								JYPZH:detailData.tracecode,
								GHPFSCMC:detailData.businessMarketName,
								GHPFSCBM:detailData.businessMarketCode,
								JE:detailData.dealWeight*detailData.dealPrice
							});
						}
						$.message( {message:"读卡成功", cls:"info"});
					} else {
						$.message( {message:"请使用肉类流通卡！", cls:"warning"});
					}
				} else {
					$.message( {message:userInfoAndBusinessDetail.msg, cls:"warning"});
				}
			} else {
				$.alert("请在程序中操作");
			}
		}
	};
	
	ui.processDefaultValue=function(){
		var pfscbm = ui.getItemJQ("PFSCBM");
		var pfscmc = ui.getItemJQ("PFSCMC");
		if(ui.options.dataId ==null||ui.options.dataId==""){
			$.ajax({
				type:"post",
				url:$.contextPath+"/prrpjcxx!getInitformData.json",
				dataType:"json",
				success:function(data){
					pfscbm.textbox("setValue",data.PFSCBM);
					pfscmc.textbox("setValue",data.PFSCMC);
				}
			})
		}
		var jsonData=$.loadJson($.contextPath +"/jyzxx!getJyzxx.json?xtlx=PR&zt=1");
		var pfsmc = ui.getItemJQ("PFSMC");
		pfsmc.combogrid("reload",jsonData.data);
	}
	ui.bindEvent = function(){
		var pfsmc = ui.getItemJQ("PFSMC");
		var pfsbm = ui.getItemJQ("PFSBM");
		var spmc = ui.getItemJQ("SPMC");
		var spbm = ui.getItemJQ("SPBM");
		var zl = ui.getItemJQ("ZL");
		var dj = ui.getItemJQ("DJ");
		var je = ui.getItemJQ("JE");
		var cdmc = ui.getItemJQ("CDMC");
		var cdbm = ui.getItemJQ("CDBM");
		pfsmc.combogrid({onChange:function(){
			pfsbm.textbox("setValue",pfsmc.combogrid("getValue"));
			pfsmc.combogrid("setValue",pfsmc.combogrid("getText"));
		}});
		spmc.combogrid({onChange:function(){
			spbm.textbox("setValue",spmc.combogrid("getValue"));
			spmc.combogrid("setValue",spmc.combogrid("getText"));
		}});
		cdmc.combogrid({onChange:function(){
			cdbm.textbox("setValue",cdmc.combogrid("getValue"));
			cdmc.combogrid("setValue",cdmc.combogrid("getText"));
		}});
		zl.textbox("option","onKeyUp",function(){
				var zlNum = zl.val()*1000;
				var djNum = dj.val()*1000;
			   if(zl.val()!=""&&dj.val()!=""){
			   		je.textbox("setValue",(zlNum*djNum)/1000000);
			   }else{
			   		je.textbox("setValue",0);
			   }
			});
		dj.textbox("option","onKeyUp",function(){  
			  	var zlNum = zl.val()*1000;
				var djNum = dj.val()*1000;
			   if(zl.val()!=""&&dj.val()!=""){
			   		je.textbox("setValue",(zlNum*djNum)/1000000);
			   }else{
			   		je.textbox("setValue",0);
			   }
			});
	}
	/*回调函数为下拉列表赋值*/
	 ui.addCallback("setCombogridValue_Jyzmc",function(o){
			if (null == o) return;
			var rowData = o.result;
			if (null == rowData) return;
			var pfsmc = ui.getItemJQ("PFSMC");
            ui.setFormData({
				PFSBM:rowData.A_JYZBM,
				PFSMC:rowData.A_JYZMC
            });
		});
	/*回调函数为下拉列表赋值*/
	//商品名称弹出框回调
	 ui.addCallback("setCombogridValue_Spmc",function(o){
			if (null == o) return;
			var rowData = o.result;
			if (null == rowData) return;
            ui.setFormData({
                SPMC:rowData.SPMC,
                SPBM:rowData.SPBM
            });
		});
		//产地名称弹出框回调
		ui.addCallback("setComboGridValue_Cdmc",function(o){
			if(null == o) return;
			var rowData = o.result;
			if(null == rowData) return;
			ui.setFormData({CDMC:rowData.CDMC,CDBM:rowData.CDBM});
		});
		/*出参 列表过滤条件*/
		ui.addOutputValue("setJyzzt",function(o){//弹出式经营者状态过滤
			var pfscbm = ui.getItemJQ("PFSCBM");
			var baltjdbm = pfscbm.textbox("getValue");
			var o = {
				status : true,
				P_columns : "((EQ_C_ZT≡1) AND (EQ_C_BALTJDBM≡"+baltjdbm+"))"
			}
			return o;
		}); 
		
		/**
		*重写保存方法 传出barCode
		*
		*
		*/
		ui.clickSave = function(op) {
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
					barCode : barCode == null? "" : barCode
				},
				dataType : 'json',
				success : function(entity) {
					if (false === entity.success || !("ID" in entity)) {
						if (isEmpty(entity.message)) CFG_message("操作失败！", "warning");
						else CFG_message(entity.message, "warning");
						return;
					}
					// 保存后回调方法
					_this.processAfterSave(entity, op);
					CFG_message("操作成功！", "success");
					if ("save" === op) {
						formUI.form("loadData", entity);
					} else if ("close" === op) {
						if (!_this.isNested) _this.uiForm.dialog("close");
						else _this.clickBackToGrid();
					} else if ("create" === op) {
						_this.clickCreate();
					}
				},
				error : function() {
					CFG_message("操作失败！", "error");
				}
			});
		}

	function setPfsxx(jyzbm,jyzmc,barCode){//根据读码信息获取本市场内该经营者信息
		var pfscbm = ui.getItemJQ("PFSCBM").textbox("getValue");
		var getJyzId = $.loadJson($.contextPath+'/jyzxx!getJyzxxByBmAndQybm.json?jyzbm='+jyzbm+"&qybm="+pfscbm+"&barCode="+barCode);
		if("FATAL" == getJyzId.result){
			CFG_message("批发商信息错误，请录入！", "warning");
		}else if("SUCCESS" == getJyzId.result){
			ui.setFormData({
				PFSMC:jyzmc,
				PFSBM:jyzbm
			});
		} else {
			var id = getJyzId.ID;
			var createJyz = $.loadJson($.contextPath + '/jyzxx!createJyz.json?id='+id+"&qybm="+pfscbm+"&xltx=PR&barCode="+barCode);
			ui.setFormData({
				PFSMC:createJyz.jyzmc,
				PFSBM:createJyz.jyzbm
			});
			var jsonData=$.loadJson($.contextPath +"/jyzxx!getJyzxx.json?xtlx=PR&zt=1");
			var pfsmc = ui.getItemJQ("PFSMC");
			pfsmc.combogrid("reload",jsonData.data);
			CFG_message("已添加经营者:"+jyzmc+"！", "success");
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
		var spmc = ui.getItemJQ("SPMC");
		if("TZ" === prefix){
			var jsonData = $.loadJson($.contextPath + '/prrpjcxx!getTzjyxx.json?tztmh='+barCode);
			if ("ERROR" === jsonData) {
				CFG_message("请输入有效的条形码!", "warning");
			}else{
				var pfsmc = jsonData.MZMC;
				var pfsbm = jsonData.MZBM;
				setPfsxx(pfsbm,pfsmc,barCode);
				//加载主表数据
				ui.setFormData({
					ZSPZH:jsonData.ZSM,
					SPMC:jsonData.SPMC,
					SPBM:jsonData.SPBM,//
					ZL:jsonData.ZL,
					DJ:jsonData.DJ,
					JE:jsonData.JE,
					CDMC:jsonData.CDMC,
					CDBM:jsonData.CDBM,
					GHSCMC:jsonData.TZCMC,
					JHPCH:jsonData.JCPCH
				});
				//设置商品只读
				//spmc.combogrid("option", "disabled", true);
			}
		}else if("PR" === prefix){
			var jsonData = $.loadJson($.contextPath + '/prrpjcxx!getPrjyxx.json?prtmh='+barCode);
			if("ERROR" === jsonData){
				CFG_message("请输入有效的条形码!", "warning");
			}else{
				var pfsmc = jsonData.LSSMC;
				var pfsbm = jsonData.LSSBM;
				setPfsxx(pfsbm,pfsmc,barCode);
				//加载主表数据
				ui.setFormData({
					ZSPZH:jsonData.ZSM,
					SPMC:jsonData.SPMC,
					SPBM:jsonData.SPBM,//
					ZL:jsonData.ZL,
					DJ:jsonData.DJ,
					JE:jsonData.JE,
					CDMC:jsonData.CDMC,
					CDBM:jsonData.CDBM,
					GHSCMC:jsonData.PFSCMC,
					JHPCH:jsonData.JHPCH
				});
				//设置商品只读
				//spmc.combogrid("option", "disabled", true)

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
//			btns.push({
//				id:$.config.preButtonId + "readCard",
//				label: "读卡",
//				type : "button"
//			});
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
