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
	return $.contextPath + "/prjyxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	/**
	*	按钮二次开发
	*
	*
	*/
	ui.clickSecondDev = function (id){
		/******读取零售商信息*******/
		if (id == $.config.preButtonId + "readCard") {//读卡按钮
			if (isSwt) {
				var result = _window("readCard", "userInfo");
				var resultData = JSON.parse(result);
				var value = resultData.value;
				var jqLssmcCombogrid = ui.getItemJQ("LSSMC");
				if (resultData.status == "true" || resultData.status == true) {
					ui.setFormData({
						LSSBM : value.userId,
						LSSMC : value.userName
					});
					var jsonData = $.loadJson($.contextPath + "/prjyzda!getJyzSpddd.json?jyzbm=" +value.userId);
					ui.getItemJQ("DDD").textbox("setValue",jsonData.SPDDD);
					$.message({message:"读卡成功", cls:"info"});
				} else {
					$.message({message:resultData.msg, cls:"warning"});
				}
			}
			else {
				$.alert("请在程序中操作");
			}
		}
		/*****写码保存*****/
		if (id == $.config.preButtonId + "writeBarCodeSave") {
			if (isSwt || isNewSwt) {
				var op = "close";
				var _this = this, formUI = $("form", this.uiForm), 
			    url = this.getAction() + "!save.json?P_tableId=" + this.options.tableId + "&P_op=" + op + "&P_menuCode=" + CFG_getInputParamValue(this.assembleData, "menuCode"),
			    formData;

				if (!formUI.form("valid")) return;
				// 保存前回调方法
				if (_this.processBeforeSave(formUI, op) === false) return;
				// 获取表单数据
			    formData = formUI.form("formData", false);

				//校验重量
				if(formData.ZSPZH!=""&&formData.ZL!=""){
					var validate = $.loadJson($.contextPath + '/prjyxx!validateWeight.json?weight=' + formData.ZL + "&zspzh=" + formData.ZSPZH);
					if("TRUE"!=validate){
						$.message({message:"交易重量不得大于剩余数量("+validate+")", cls:"warning"});
						return;
					}
				}
				formData.PFSMC = ui.getItemJQ("PFSMC").combogrid("getText");
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
						//打印条形码
						var detailArray = [];
						var totalPrice = formData.DJ * formData.ZL;
			    		var data = {
		    				tracecode : formData.ZSM,//追溯码
						    productName : formData.SPMC,
						    price : formData.DJ,
						    weight : formData.ZL,
							totalPrice:totalPrice
	   					};
			    		detailArray.push(data);
				    	var date = formData.JYRQ;
				    	var detail = {
						    code : formData.JYTMH,
						    marketName : formData.PFSCMC,
						    sellerName : formData.PFSMC,
						    buyer : formData.LSSMC,
						    details : detailArray
				    	}
						if(isSwt){
				    		var result = _window("printDetailWithBarCode",JSON.stringify(detail));
						}else if(isNewSwt){
							var d = new Date();
							var buyDate = d.getFullYear() + "年" + (d.getMonth() + 1) + "月" + d.getDate() + "日";
							var _result = _print.print("prccsfd",{
								"code" : formData.JYTMH,
								"marketName":formData.PFSCMC,
								"sellerName": formData.PFSMC,
								"buyer": formData.LSSMC,
								"buyDate":buyDate,
								"detailArray":detailArray,
								"urltracecode" :"http://www.zhuisuyun.net/spzsypt/html/tracingcodeSearch1.html?zsm=" +  formData.ZSM,//追溯码
								"tracecode" :formData.ZSM//追溯码
							});
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
			}else {
				$.alert("请在程序中操作");
			}
		}
	}
	/**
	*	写卡保存并打印
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
	    
	    //追溯码list
		var traceCodeList = new Array();
    	traceCodeList.push(formData.ZSM);

		//校验重量
		if(formData.ZSPZH!=""&&formData.ZL!=""){
			var validate = $.loadJson($.contextPath + '/prjyxx!validateWeight.json?weight=' + formData.ZL + "&zspzh=" + formData.ZSPZH);
			if("TRUE"!=validate){
				$.message({message:"交易重量不得大于剩余数量("+validate+")", cls:"warning"});
				return;
			}
		}

	  	//写卡
	    if(isSwt){
	    	var detailArray = new Array();
	    	var food = {};
	    	//交易明细
    		food[formData.SPBM] = formData.SPMC;
    		var data = {
   				tracecode : formData.ZSM,//追溯码
   				batchCode : formData.JHPCH,//批次号
   				sellerCode : formData.PFSBM,
   				sellerName : formData.PFSMC,
   				produceLocName : "??*",//生产基地名称
   				businessMarketCode : formData.PFSCBM,
   				businessMarketName : formData.PFSCMC,
   				productCode : formData.SPBM,
   				productPlaceCode : "??*",//产地编码
   				dealCount : 0,
   				dealWeight : formData.ZL,
   				dealPrice : formData.DJ,
   				carNo : "??*"
			};
    		detailArray.push(data);
    		//交易信息
	    	var date = formData.JYRQ;
	    	var bussinessInfo = {
	    			bDate:date.replace(/-/g,""),
	    			bCount:1,
	    			bType:"?"
	    	}
	    	var result = _window("writeBusinessInfoAndDetailAndPrint","1",JSON.stringify(bussinessInfo),JSON.stringify(detailArray),JSON.stringify(food),"PR");
	    	var resultData = JSON.parse(result);
	    	if (resultData.status == "true" || resultData.status == true) {
	    		//保存数据库
	    		$.ajax({
	    			type : "post",
	    			url : url,
	    			data : {E_entityJson: $.config.toString(formData)},
	    			dataType : 'json',
	    			success : function(entity) {
	    				if (false === entity.success || !("ID" in entity)) {
	    					if (isEmpty(entity.message)) CFG_message("操作失败！", "warning");
	    					else CFG_message(entity.message, "warning");
	    					_window("clearBusinessInfoAndDetail",traceCodeList);
	    					return;
	    				}
	    				// 保存后回调方法
	    				_this.processAfterSave(entity, op);
						//
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
	    				//清卡
	    				_window("clearBusinessInfoAndDetail",traceCodeList);
	    			}
	    		});
	    	} else {
				$.alert("写卡失败：" + resultData.msg);
			}
	    }else{
	    	$.alert("请在程序环境中写卡");
	    }
	},
	
	ui.processDefaultValue=function(){
		var pfscbm = ui.getItemJQ("PFSCBM");
		var pfscmc = ui.getItemJQ("PFSCMC");
		var zsm = ui.getItemJQ("ZSM");
		var jytmh = ui.getItemJQ("JYTMH");
		if(ui.options.dataId ==null||ui.options.dataId==""){
			$.ajax({
				type:"post",
				url:$.contextPath+"/prjyxx!getInitformData.json",
				dataType:"json",
				success:function(data){
					pfscbm.textbox("setValue",data.PFSCBM);
					pfscmc.textbox("setValue",data.PFSCMC);
					zsm.textbox("setValue",data.ZSM);
					jytmh.textbox("setValue","PR" + data.JYTMH);
				}
			})
		}
		var jsonData=$.loadJson($.contextPath +"/jyzxx!getJyzxx.json?xtlx=PR&zt=1");
		var pfsmc = ui.getItemJQ("PFSMC");
		var lssmc = ui.getItemJQ("LSSMC");
		pfsmc.combogrid("reload",jsonData.data);
		lssmc.combogrid("reload",jsonData.data);
	}

	ui.bindEvent=function(){
		var dddJQ = ui.getItemJQ("DDD");  //到达地
		var pfsmc = ui.getItemJQ("PFSMC");	//批发商名称
		var pfsbm = ui.getItemJQ("PFSBM");	//批发商编码
		var lssmc = ui.getItemJQ("LSSMC");	//批发商名称
		var lssbm = ui.getItemJQ("LSSBM");	//批发商编码
		var zspzh = ui.getItemJQ("ZSPZH");	//追溯凭证号
		var spmc = ui.getItemJQ("SPMC");	//商品名称
		var spbm = ui.getItemJQ("SPBM");	//商品编码
		var ddd = ui.getItemJQ("DDD");	//到达地
		var zl = ui.getItemJQ("ZL");	//重量
		var dj = ui.getItemJQ("DJ");	//单价
		var je = ui.getItemJQ("JE");	//金额
		var jhpch = ui.getItemJQ("JHPCH");	//金额
		//到达地下拉列表onChange事件
		dddJQ.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			ui.setFormData({DDD:newText});
		});

		pfsmc.combogrid({onChange:function(){
			pfsbm.textbox("setValue",pfsmc.combogrid("getValue"));
			pfsmc.combogrid("setValue",pfsmc.combogrid("getText"));
			//清空追溯凭证号
			ui.setFormData({ZSPZH:""});
			//过滤零售商名称
			lssmc.combogrid("reload",$.contextPath +"/jyzxx!getJyzxx.json?xtlx=PR&zt=1&jyzbm=" + pfsbm.textbox("getValue"));
			//过滤追溯凭证号
			zspzh.combogrid("reload",$.contextPath + '/prjyxx!getZspzhGrid.json?pfsbm=' +  pfsbm.textbox("getValue"));
		}});
		lssmc.combogrid({onChange:function(){
			lssbm.textbox("setValue",lssmc.combogrid("getValue"));
			lssmc.combogrid("setValue",lssmc.combogrid("getText"));
			var jsonData = $.loadJson($.contextPath + "/prjyzda!getJyzSpddd.json?jyzbm=" +lssbm.textbox("getValue"));
			ddd.combogrid("setValue",jsonData.SPDDD);
			//过滤批发商名称
			//pfsmc.combogrid("reload",$.contextPath +"/jyzxx!getJyzxx.json?xtlx=PR&zt=1&jyzbm=" + lssbm.textbox("getValue"));
		}});
		zspzh.combogrid({onChange:function(){
			zspzhValue = zspzh.combogrid("getValue");
			var jsonData = $.loadJson($.contextPath + "/prjcxx2!getSpxx.json?zspzh=" +zspzhValue);
			spmc.textbox("setValue",jsonData.SPMC);
			spbm.textbox("setValue",jsonData.SPBM);
			var jhpchValue = $.loadJson($.contextPath+"/tcsprjcspxx!getJhpch.json?zspzh="+zspzhValue);
			jhpch.textbox("setValue",jhpchValue);
			
			//关联批发商名称
			var pfsxx = $.loadJson($.contextPath + '/prjyxx!getPfsxxByZspzh.json?zspzh=' + zspzhValue);
			ui.setFormData({PFSMC:pfsxx.PFSMC,PFSBM:pfsxx.PFSBM});
			//过滤零售商名称
//			var lssmc = ui.getItemJQ("LSSMC");
//			lssmc.combogrid("reload",$.contextPath +"/jyzxx!getJyzxx.json?xtlx=PR&zt=1&jyzbm=" + ui.getItemJQ("PFSBM").textbox("getValue"));
//			if(zspzh.combogrid("getValue")!=""&&zl.val()!=""){
//				var validate = $.loadJson($.contextPath + '/prjyxx!validateWeight.json?weight=' + zl.val() + "&zspzh=" + zspzh.combogrid("getValue"));
//				if("TRUE"!=validate){
//					//$.message({message:"交易重量不得大于剩余数量("+validate+")", cls:"warning"});
//				}
//			}
		}})
		zl.textbox("option","onKeyUp",function(){
		    var zlNum = zl.val()*1000;
			var djNum = dj.val()*1000;
			if(zspzh.combogrid("getValue")!=""&&zl.val()!=""){
				var validate = $.loadJson($.contextPath + '/prjyxx!validateWeight.json?weight=' + zl.val() + "&zspzh=" + zspzh.combogrid("getValue"));
				if("TRUE"!=validate){
					//$.message({message:"交易重量不得大于剩余数量("+validate+")", cls:"warning"});
					//return;
				}
			}
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
	//批发商名称回调
	 ui.addCallback("setCombogridValue_Jyzmc",function(o){
			if (null == o) return;
			var rowData = o.result;
			if (null == rowData) return;
			var pfsmc = ui.getItemJQ("PFSMC");
			var pfsbm = ui.getItemJQ("PFSBM");
			var lssmc = ui.getItemJQ("LSSMC");
			var zspzh = ui.getItemJQ("ZSPZH");
			var ddd = ui.getItemJQ("DDD");
			pfsbm.textbox("setValue",rowData.A_JYZBM);
			//清空追溯凭证号数据
			ui.setFormData({ZSPZH:""});
			pfsmc.combogrid("setValue",rowData.A_JYZMC);
			//过滤零售商名称
			lssmc.combogrid("reload",$.contextPath +"/jyzxx!getJyzxx.json?xtlx=PR&zt=1&jyzbm=" + pfsbm.textbox("getValue"));
			//过滤追溯凭证号
			zspzh.combogrid("reload",$.contextPath + '/prjyxx!getZspzhGrid.json?pfsbm=' +  pfsbm.textbox("getValue"));
			
		});
	 ui.addCallback("setCombogridValue_Lssmc",function(o){
			if (null == o) return;
			var rowData = o.result;
			if (null == rowData) return;
			var lssmc = ui.getItemJQ("LSSMC");
			var lssbm = ui.getItemJQ("LSSBM");
			var pfsmc = ui.getItemJQ("PFSMC");
			var ddd = ui.getItemJQ("DDD");
			lssbm.textbox("setValue",rowData.A_JYZBM);
			lssmc.combogrid("setValue",rowData.A_JYZMC);
			ddd.combogrid("setValue",rowData.SPDDD);
			//过滤批发商名称
			pfsmc.combogrid("reload",$.contextPath +"/jyzxx!getJyzxx.json?xtlx=PR&zt=1&jyzbm=" + lssbm.textbox("getValue"));
		});
		//追溯凭证号回调
	 ui.addCallback("setCombogridValue_Zspzh",function(o){
			if (null == o) return;
			var rowData = o.result;
			if (null == rowData) return;
			var zspzh = ui.getItemJQ("ZSPZH");
			var spmc = ui.getItemJQ("SPMC");
			var spbm = ui.getItemJQ("SPBM");
			zspzh.combogrid("setValue",rowData.ZSPZH);
            var jsonData = $.loadJson($.contextPath + "/prjcxx2!getSpxx.json?zspzh=" +rowData.ZSPZH);
            spmc.textbox("setValue",jsonData.SPMC);
            spbm.textbox("setValue",jsonData.SPBM);
			
			//关联批发商名称
			var pfsxx = $.loadJson($.contextPath + '/prjyxx!getPfsxxByZspzh.json?zspzh=' + zspzh.combogrid("getValue"));
			ui.setFormData({PFSMC:pfsxx.PFSMC,PFSBM:pfsxx.PFSBM});
			//过滤零售商名称
			var lssmc = ui.getItemJQ("LSSMC");
			lssmc.combogrid("reload",$.contextPath +"/jyzxx!getJyzxx.json?xtlx=PR&zt=1&jyzbm=" + ui.getItemJQ("PFSBM").textbox("getValue"));
	});
	//到达地弹出框回调函数
    ui.addCallback("setComboGridValue_ddd",function(o){
        if(null == o) return;
        var obj = o.rowData;
        if(null == obj) return;
        ui.setFormData({DDD:obj.CDMC});
    });
	/*出参 列表过滤条件*/
		ui.addOutputValue("setPfs",function(o){//弹出式批发商过滤
			var lssbm = ui.getItemJQ("LSSBM").textbox("getValue");
			if(lssbm ==""){
				var o = {
					status : true,
					P_columns :null
				}
				return o;
			}
			var o = {
				status : true,
				P_columns : "EQ_C_B_XTLX≡PC;EQ_C_B_ZT≡1;NOT_C_A_JYZBM≡"+lssbm
			}
			return o;
		});
		ui.addOutputValue("setLss",function(o){//弹出式零售商过滤
			var pfsbm = ui.getItemJQ("PFSBM").textbox("getValue");
			if(pfsbm ==""){
				var o = {
					status : true,
					P_columns :null
				}
				return o;
			}
			var o = {
				status : true,
				P_columns : "EQ_C_B_XTLX≡PR;EQ_C_B_ZT≡1;NOT_C_A_JYZBM≡"+pfsbm
			}
			return o;
		}); 
		ui.addOutputValue("setPfscbm",function(o){//弹出式追溯凭证号列表过滤
			var pfscbm = ui.getItemJQ("PFSCBM").textbox("getValue");
			var pfsbm = ui.getItemJQ("PFSBM").textbox("getValue");
			var o = {
				status : true,
				P_columns : "(EQ_C_JCJG≡1) AND(EQ_C_ZT≡1) AND (EQ_C_PFSCBM≡"+pfscbm+") AND (LIKE_C_PFSBM≡"+pfsbm+")"
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
	if (isEmpty(ui.options.model)) {
		ui.processItems = function (data) {
			var btns = [];
//			btns.push({
//				id:$.config.preButtonId + "readCard",
//				label: "读卡",
//				type : "button"
//			});
			btns.push({
				id:$.config.preButtonId + "writeBarCodeSave",
				label: "写码保存",
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
