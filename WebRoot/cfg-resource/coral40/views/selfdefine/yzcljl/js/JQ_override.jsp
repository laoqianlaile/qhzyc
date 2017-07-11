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
	return $.contextPath + "/yzcljl";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	var sl;
	var oldasl;
	var oldawghclsl;
	var xgzjsl ;//修改之后相比修改之前增加的数量
	var issave = false;//判断是否第二次保存
	//根据养殖批次号，处理相关的数据：猪舍编号、品种通用名、进栏日期、负责人、负责人编号、养殖数量
	ui.addCallback("setComboGridValue_jlxx", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
		//根据所选养殖批次号信息得到负责人及相关的数据
		var fzr_obj=$.loadJson($.contextPath + "/yzjlxx!getJlxxFzr.json?yzpch="+obj.YZPCH);//getCtdaFzr
 		ui.setFormData({
	 		A_YZPCH:obj.YZPCH,//养殖批次号
	 		B_SYZS:fzr_obj.SYZS,//使用猪舍
	 		B_JLRQ:fzr_obj.JLRQ,//进栏日期
	 		B_PZTYM:fzr_obj.PZTYM,//品种通用名
	 		B_PZQC:fzr_obj.PZQC,//品种全称
	 		A_FZR:fzr_obj.FZR,//负责人
	 		A_FZRBH:fzr_obj.FZRBH,//负责人编号
	 		B_SL:fzr_obj.SL//进栏数量
 		});
 		sl=fzr_obj.SL;
 		var als=ui.getItemJQ("A_SL");
 		if(isEmpty(ui.options.dataId)){
 			//checkSl(sl,als.val());
 		}else{
 			//checkSl(sl,als.val()-oldasl);
 		}
 		
 	}); 
 	//回调函数处理使负责人信息
	ui.addCallback("setComboGridValue_fzr", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setFormData({A_FZR:obj.XM,A_FZRBH:obj.GZRYBH});
 	});
 	//回调函数处理使到达地信息
	ui.addCallback("setComboGridValue_ddd", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setFormData({A_DDD:obj.CDMC,A_DDDBM:obj.CDBM});
 	});
 	
 	//绑定事件
 	ui.bindEvent = function (){
 		var yzpch=ui.getItemJQ("A_YZPCH");
 		yzpch.combogrid("option","onChange",function(e,data){
			var newValue = data.value;
			//根据所选养殖批次号信息得到负责人及相关的数据
			var fzr_obj=$.loadJson($.contextPath + "/yzjlxx!getJlxxFzr.json?yzpch="+newValue);//getCtdaFzr
	 		ui.setFormData({
		 		A_YZPCH:newValue,//养殖批次号
		 		B_SYZS:fzr_obj.SYZS,//使用猪舍
		 		B_JLRQ:fzr_obj.JLRQ,//进栏日期
		 		B_PZTYM:fzr_obj.PZTYM,//品种通用名
		 		B_PZQC:fzr_obj.PZQC,//品种全称
		 		A_FZR:fzr_obj.FZR,//负责人
		 		A_FZRBH:fzr_obj.FZRBH,//负责人编号
		 		B_SL:fzr_obj.SL//进栏数量
	 		});
	 		sl=fzr_obj.SL;
 			var als=ui.getItemJQ("A_SL");
 			if(isEmpty(ui.options.dataId)){
	 			//checkSl(sl,als.val());
 			}else{
 				//checkSl(sl,als.val()-oldasl);
 			}
		});
 		//负责人名称和编号处理
 		var fzrbh = ui.getItemJQ("A_FZRBH");
 		fzrbh.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			ui.setFormData({A_FZR:newText,A_FZRBH:newValue});
		});
 		var asl = ui.getItemJQ("A_SL");
 		asl.textbox("option","onChange",function(e,data){
			var newValue = data.value;
//			if(newValue <0){
//				ui.setFormData({A_SL:""});
//				return;
//			}
			var yzpchValue = yzpch.combogrid("getValue");
			if(yzpchValue != null || yzpchValue != ""){
				//根据所选养殖批次号信息得到负责人及相关的数据
				var fzr_obj=$.loadJson($.contextPath + "/yzjlxx!getJlxxFzr.json?yzpch="+yzpch.combogrid("getValue"));//getCtdaFzr
		 		ui.setFormData({
			 		//A_YZPCH:newValue,//养殖批次号
			 		B_SYZS:fzr_obj.SYZS,//使用猪舍
			 		B_JLRQ:fzr_obj.JLRQ,//进栏日期
			 		B_PZTYM:fzr_obj.PZTYM,//品种通用名
			 		B_PZQC:fzr_obj.PZQC,//品种全称
			 		A_FZR:fzr_obj.FZR,//负责人
			 		A_FZRBH:fzr_obj.FZRBH,//负责人编号
			 		B_SL:fzr_obj.SL//进栏数量
		 		});
		 		sl=fzr_obj.SL;
			}
			if(isEmpty(ui.options.dataId)){
				//checkSl(sl,data.newValue);
			}else{
				//checkSl(sl,data.newValue-oldasl);
			}
		});
		//到达地onChange事件
		var dddJQ = ui.getItemJQ("A_DDD");
		dddJQ.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			ui.setFormData({A_DDD:newText,A_DDDBM:newValue});
		});
		var sl = ui.getItemJQ("A_SL");
        var wghclsl = ui.getItemJQ("A_WGHCLSL");
        sl.textbox("option","onChange",function(e,data){
            if (!$.isNumeric(data.value)) {
                sl.textbox("setValue",0);
            } else {
                sl.textbox("setValue",parseInt(data.value));
            }
        });
        wghclsl.textbox("option","onChange",function(e,data){
            if (!$.isNumeric(data.value)) {
                wghclsl.textbox("setValue",0);
            } else {
                wghclsl.textbox("setValue",parseInt(data.value));
            }
        });
 	};
 	//初始化参数
 	ui._init = function () {
		if (isEmpty(ui.options.dataId)) {//自动种植批次号生成
			//默认添加所属账户编号
			var obj=$.loadJson($.contextPath + "/yzcdda!getCddaByZhbh.json");
			var cctmh=$.loadJson($.contextPath + "/yzcljl!getCctmh.json");
			var zsm=$.loadJson($.contextPath + "/yzcljl!getYzzsm.json");
			var pch=$.loadJson($.contextPath + "/yzcljl!getYzpch.json");
			//初始化加载账号编码和种植批次号
			ui.setFormData({A_QYBM:obj.qybm,A_QYMC:obj.qymc,A_CCTMH:"YZ"+cctmh,A_ZSM:zsm,A_PCH:pch});
		}else{
			oldasl = ui.getItemJQ("A_SL").val();
			oldawghclsl = ui.getItemJQ("A_WGHCLSL").val();
            /********修改时加载使用畜舍，下拉列表数据必须包含本身********/
            var $syzs = ui.getItemJQ("B_SYZS");
            var syzs = $syzs.combogrid("getValue");
            $syzs.combogrid("option","url","yzzsda!getZsdaGrid.json?self=" + syzs);
            $syzs.combogrid("reload");
            $syzs.combogrid("setValue",syzs);

            var $yzpch = ui.getItemJQ("A_YZPCH");
            var yzpch = $yzpch.combogrid("getValue");
            $yzpch.combogrid("option","url","yzjlxx!getJlxxGrid.json?self=" + yzpch);
            $yzpch.combogrid("reload");
            $yzpch.combogrid("setValue",yzpch);

		}
 	}
 	function checkSl(totalSl,inputSl){
 		if(totalSl == "" || totalSl==null) return ;
 		if(inputSl == "" || inputSl == null) return;
 		if(totalSl<inputSl){
 			if(isEmpty(ui.options.dataId)){
	 			$.alert({message:"出栏数量，不能大于进栏数量<br>进栏总数为："+totalSl,modal:true});
 			}else{
 				$.alert({message:"出栏数量，不能大于进栏数量<br>进栏总数为："+(parseInt(totalSl)+parseInt(oldasl)),modal:true});
 			}
 			ui.setFormData({A_SL:""});
 		}
 	}

	ui.clickSecondDev = function (id) {
		if (id == $.config.preButtonId + "smbc") {//写码保存
			if (isSwt || isNewSwt) {
				var jqForm = $("form", this.uiForm), formData;
				// 表单检验
				if (!jqForm.form("valid")) return false;
				// 获取表单数据
				formData = jqForm.form("formData", false);
				if(""!=formData.A_SZCDJYZH){
					var jyzhExists = $.loadJson($.contextPath + "/yzcljl!checkJyzh.json?jyzh=" + formData.A_SZCDJYZH);
					if(jyzhExists){
						$.message({message: "动物产地检疫证号已存在！请重新输入", cls: "warning"});
						return
					}
				}
				
				if(isEmpty(ui.options.dataId)){
			    	if(issave){
				    	xgzjsl = (parseInt(formData.A_SL)+parseInt(formData.A_WGHCLSL)-parseInt(oldasl)-parseInt(oldawghclsl));
				    	 if((parseInt(formData.A_SL)+parseInt(formData.A_WGHCLSL)-parseInt(oldasl)-parseInt(oldawghclsl)) > parseInt(formData.B_SL)){
							$.message({message: "出栏数量与无公害处理数量修改之和必需小于进栏剩余数量(" + formData.B_SL + ")!", cls: "warning"});
							ui.setFormData({A_SL:oldasl,A_WGHCLSL:oldawghclsl});
							//$.message({message: "出栏数量与无公害处理数量之和必须小于等于进栏数量(" + (parseInt(formData.B_SL)+parseInt(oldasl)+parseInt(oldawghclsl)) + ")!", cls: "warning"});
							return;
						}
						if(parseInt(formData.B_SL) == 0 && xgzjsl < 0){
							var yzpchData =  formData.A_YZPCH;
							var issyzsData = $.loadJson($.contextPath+"/yzcljl!getIssyzs.json?yzpch="+yzpchData);
							if(!issyzsData){
								$.message({message: "该畜舍已被使用,不能修改出栏数量与无公害处理数量!", cls: "warning"});
								ui.setFormData({A_SL:oldasl,A_WGHCLSL:oldawghclsl});
								return;
							}
						}
			    	}else{
					    if((parseInt(formData.A_SL)+parseInt(formData.A_WGHCLSL)) > parseInt(formData.B_SL)){
							$.message({message: "出栏数量与无公害处理数量之和必须小于等于进栏数量(" + formData.B_SL + ")!", cls: "warning"});
							return;
						}
			    	}
			    }else{
			    	xgzjsl = (parseInt(formData.A_SL)+parseInt(formData.A_WGHCLSL)-parseInt(oldasl)-parseInt(oldawghclsl));
			    	 if((parseInt(formData.A_SL)+parseInt(formData.A_WGHCLSL)-parseInt(oldasl)-parseInt(oldawghclsl)) > parseInt(formData.B_SL)){
						$.message({message: "出栏数量与无公害处理数量修改之和必需小于进栏剩余数量(" + formData.B_SL + ")!", cls: "warning"});
						ui.setFormData({A_SL:oldasl,A_WGHCLSL:oldawghclsl});
						//$.message({message: "出栏数量与无公害处理数量之和必须小于等于进栏数量(" + (parseInt(formData.B_SL)+parseInt(oldasl)+parseInt(oldawghclsl)) + ")!", cls: "warning"});
						return;
					}
					if(parseInt(formData.B_SL) == 0 && xgzjsl < 0){
						var yzpchData =  formData.A_YZPCH;
						var issyzsData = $.loadJson($.contextPath+"/yzcljl!getIssyzs.json?yzpch="+yzpchData);
						if(!issyzsData){
							$.message({message: "该畜舍已被使用,不能修改出栏数量与无公害处理数量!", cls: "warning"});
							ui.setFormData({A_SL:oldasl,A_WGHCLSL:oldawghclsl});
							return;
						}
					}
			    }
				
				url = this.getAction() + "!save.json?P_tableId=" + this.options.tableId;
				$.ajax({//保存数据库成功后写码
					url: url,
					type: "post",
					data: {E_entityJson: $.config.toString(formData),
							xgzjsl:xgzjsl},
					dataType: "json",
					success: function (rlt) {
						if (rlt != null && rlt != "") {
							var cdda=$.loadJson($.contextPath + "/yzcdda!getCddaByZhbh.json");
							var deal = {
								marketName : cdda.yzcmc,
								code: formData.A_CCTMH,
								details : [{
									productName : formData.B_PZQC,
									quarantineNumber : formData.A_SZCDJYZH,
									outDate : formData.A_CLRQ,
									corralNo : formData.B_SYZS,
									number : formData.A_SL,
									weight : formData.A_ZZL,
									tracecode : formData.A_ZSM
								}]
							}
							if(isSwt){
								var result = _window("printDetailWithBarCode", JSON.stringify(deal));
							}else if(isNewSwt){
								var _result = _print.print("yzccsfdPrint",{
									"code": formData.A_CCTMH,
									"marketName":cdda.yzcmc,
									"productName" : formData.B_PZQC,
									"quarantineNumber" : formData.A_SZCDJYZH,
									"outDate" : formData.A_CLRQ,
									"corralNo" : formData.B_SYZS,
									"number" : formData.A_SL,
									"weight" : formData.A_ZZL,
									"tracecode" : formData.A_ZSM,
									"urltracecode" :"http://www.zhuisuyun.net/spzsypt/html/tracingcodeSearch1.html?zsm=" + formData.A_ZSM
								});
							}
							//方法已写save方法中进行事物管理
							CFG_message("保存成功！", "success");
							
							oldasl = ui.getItemJQ("A_SL").val();
							oldawghclsl = ui.getItemJQ("A_WGHCLSL").val();
							issave = true;
							//修改保存之后重新赋值数据
							//if(!isEmpty(ui.options.dataId)){
								xgzjsl = 0;
								var fzr_obj=$.loadJson($.contextPath + "/yzjlxx!getJlxxFzr.json?yzpch="+formData.A_YZPCH);//getCtdaFzr
					 			ui.setFormData({
							 		//A_YZPCH:newValue,//养殖批次号
							 		//B_SYZS:fzr_obj.SYZS,//使用猪舍
							 		//B_JLRQ:fzr_obj.JLRQ,//进栏日期
							 		//B_PZTYM:fzr_obj.PZTYM,//品种通用名
							 		//B_PZQC:fzr_obj.PZQC,//品种全称
							 		//A_FZR:fzr_obj.FZR,//负责人
							 		//A_FZRBH:fzr_obj.FZRBH,//负责人编号
							 		B_SL:fzr_obj.SL//进栏数量
					 			});
					 			sl=fzr_obj.SL;
							//}
							
							
							
						} else {
							CFG_message("保存失败", "warning");
							return;
						}
					},
					error: function () {
						CFG_message("保存主表数据失败！", "error");
						return;
					}
				});// */
			} else {
				$.alert("请在程序中操作");
			}
		}
	}
	
	ui.clickSave = function(op) {
		var _this = this, jqForm = $("form", this.uiForm), 
		    url = this.getAction() + "!save.json?P_tableId=" + this.options.tableId + "&P_op=" + op 
                                   + "&P_componentVersionId=" + this.options.componentVersionId
                                   + "&P_menuId=" + this.options.menuId
		                           + "&P_menuCode=" + CFG_getInputParamValue(this.assembleData, "menuCode"),
		    formData;

		if (!jqForm.form("valid")) return;
		// 保存前回调方法
		if (_this.processBeforeSave(jqForm, op) === false) return;
		// 获取表单数据
	    formData = jqForm.form("formData", false);
	    
	    if(isEmpty(ui.options.dataId)){
	    	if(issave){
		    	xgzjsl = (parseInt(formData.A_SL)+parseInt(formData.A_WGHCLSL)-parseInt(oldasl)-parseInt(oldawghclsl));
		    	 if((parseInt(formData.A_SL)+parseInt(formData.A_WGHCLSL)-parseInt(oldasl)-parseInt(oldawghclsl)) > parseInt(formData.B_SL)){
					$.message({message: "出栏数量与无公害处理数量修改之和必需小于进栏剩余数量(" + formData.B_SL + ")!", cls: "warning"});
					ui.setFormData({A_SL:oldasl,A_WGHCLSL:oldawghclsl});
					//$.message({message: "出栏数量与无公害处理数量之和必须小于等于进栏数量(" + (parseInt(formData.B_SL)+parseInt(oldasl)+parseInt(oldawghclsl)) + ")!", cls: "warning"});
					return;
				}
				if(parseInt(formData.B_SL) == 0 && xgzjsl < 0){
					var yzpchData =  formData.A_YZPCH;
					var issyzsData = $.loadJson($.contextPath+"/yzcljl!getIssyzs.json?yzpch="+yzpchData);
					if(!issyzsData){
						$.message({message: "该畜舍已被使用,不能修改出栏数量与无公害处理数量!", cls: "warning"});
						ui.setFormData({A_SL:oldasl,A_WGHCLSL:oldawghclsl});
						return;
					}
				}
	    	}else{
			    if((parseInt(formData.A_SL)+parseInt(formData.A_WGHCLSL)) > parseInt(formData.B_SL)){
					$.message({message: "出栏数量与无公害处理数量之和必须小于等于进栏数量(" + formData.B_SL + ")!", cls: "warning"});
					return;
				}
	    	}
	    }else{
	    	xgzjsl = (parseInt(formData.A_SL)+parseInt(formData.A_WGHCLSL)-parseInt(oldasl)-parseInt(oldawghclsl));
	    	 if((parseInt(formData.A_SL)+parseInt(formData.A_WGHCLSL)-parseInt(oldasl)-parseInt(oldawghclsl)) > parseInt(formData.B_SL)){
				$.message({message: "出栏数量与无公害处理数量修改之和必需小于进栏剩余数量(" + formData.B_SL + ")!", cls: "warning"});
				ui.setFormData({A_SL:oldasl,A_WGHCLSL:oldawghclsl});
				//$.message({message: "出栏数量与无公害处理数量之和必须小于等于进栏数量(" + (parseInt(formData.B_SL)+parseInt(oldasl)+parseInt(oldawghclsl)) + ")!", cls: "warning"});
				return;
			}
			if(parseInt(formData.B_SL) == 0 && xgzjsl < 0){
				var yzpchData =  formData.A_YZPCH;
				var issyzsData = $.loadJson($.contextPath+"/yzcljl!getIssyzs.json?yzpch="+yzpchData);
				if(!issyzsData){
					$.message({message: "该畜舍已被使用,不能修改出栏数量与无公害处理数量!", cls: "warning"});
					ui.setFormData({A_SL:oldasl,A_WGHCLSL:oldawghclsl});
					return;
				}
			}
	    }
		$.ajax({
			type : "post",
			url : url,
			data : {E_entityJson: $.config.toString(formData),
					xgzjsl:xgzjsl},
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
					oldasl = ui.getItemJQ("A_SL").val();
					oldawghclsl = ui.getItemJQ("A_WGHCLSL").val();
					issave = true;
				//修改保存之后重新赋值数据
				//if(!isEmpty(ui.options.dataId)){
					xgzjsl = 0;
					var fzr_obj=$.loadJson($.contextPath + "/yzjlxx!getJlxxFzr.json?yzpch="+formData.A_YZPCH);//getCtdaFzr
		 			ui.setFormData({
				 		//A_YZPCH:newValue,//养殖批次号
				 		//B_SYZS:fzr_obj.SYZS,//使用猪舍
				 		//B_JLRQ:fzr_obj.JLRQ,//进栏日期
				 		//B_PZTYM:fzr_obj.PZTYM,//品种通用名
				 		//B_PZQC:fzr_obj.PZQC,//品种全称
				 		//A_FZR:fzr_obj.FZR,//负责人
				 		//A_FZRBH:fzr_obj.FZRBH,//负责人编号
				 		B_SL:fzr_obj.SL//进栏数量
		 			});
		 			sl=fzr_obj.SL;
				//}
	 			
				if ("save" === op) {
					jqForm.form("loadData", entity);
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
