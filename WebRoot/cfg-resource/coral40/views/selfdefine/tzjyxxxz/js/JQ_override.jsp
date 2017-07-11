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
	return $.contextPath + "/tzjyxxxz";
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
	if(1 === ui.options.number){
		
		function setDdd(mzbm){//设置到达地方法
			var jqDdd = ui.getItemJQ("DDD");
			var jsonData = $.loadJson($.contextPath + "/tzjyzda!getDddByJyzbh.json?jyzbh=" +mzbm);
			jqDdd.combogrid("setValue",jsonData);
		}
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
			
			//加载生猪屠宰检疫证号 	
			var jqJyzh = ui.getItemJQ("SZCDJYZH");
			var jsonData = $.loadJson($.contextPath + '/tzjyxxxz!getJyzhByHzbm.json?hzbm=' + ui.getItemJQ("HZBM").combogrid("getValue"));
			jqJyzh.combogrid("reload",jsonData);
			
			//生成追溯码及交易条码号
			var zsmData = $.loadJson($.contextPath + '/tzjyxxxz!getZsm.json');
			ui.setFormData({ZSM:zsmData,JYTMH:"TZ" + zsmData});
			
			//加载货主名称和买家名称下拉列表
			var jqHzmc = ui.getItemJQ("HZBM");
			var jqMzmc = ui.getItemJQ("MZBM");
			var jsonData = $.loadJson($.contextPath+'/jyzxx!getJyzxx.json?xtlx=TZ&zt=1');
			jqHzmc.combogrid("reload",jsonData.data);
			jqMzmc.combogrid("reload",jsonData.data);
			//jqHzmc.combogrid("reload",$.contextPath+'/tcstzjyz!getJyzdaGrid.json?opponent='+ui.getItemJQ("MZBM").textbox("getValue"));
			//jqMzmc.combogrid("reload",$.contextPath+'/tcstzjyz!getJyzdaGrid.json?opponent='+ui.getItemJQ("HZBM").textbox("getValue"));
		}

		
		/**
		*	绑定事件
		*
		*/
		ui.bindEvent = function(){
			var jqHzmc = ui.getItemJQ("HZBM");//货主名称
			var jqMzmc = ui.getItemJQ("MZBM");//买主名称
			var jqJyzh = ui.getItemJQ("SZCDJYZH");//生猪产地检疫证号
			var jqSpmc = ui.getItemJQ("SPBM");
			var jqZl = ui.getItemJQ("ZL");//重量
			var jqDj = ui.getItemJQ("DJ");//单价
			var dddJQ = ui.getItemJQ("DDD");//到达地
			//货主名称下拉列表onChange事件
			jqHzmc.combogrid("option","onChange",function(e,data){
				var newText = data.newText;
				var newValue = data.newValue;
				var jsonData = $.loadJson($.contextPath + '/tzjyxxxz!getJyzhByHzbm.json?hzbm=' + newValue);
				jqJyzh.combogrid("reload",jsonData);
				ui.setFormData({HZMC:newText,HZBM:newValue});
				jsonData=$.loadJson($.contextPath+'/jyzxx!getJyzxx.json?xtlx=TZ&zt=1&jyzbm='+ui.getItemJQ("HZBM").combogrid("getValue"));
				jqMzmc.combogrid("reload",jsonData.data);
			});
			
			//生猪产地检疫证号下拉列表onChange事件
			jqJyzh.combogrid("option","onChange",function(e,data){
				var newText = data.newText;
				var newValue = data.newValue;
				var detailGrid = ui.getDetailGrid();
				var list = new Array();
				//根据生猪产地检疫证号获取货主信息
				var hzxxData = $.loadJson($.contextPath + '/tzjyxxxz!getRpjyxx.json?szcdjyzh=' + newText);
				ui.setFormData({
					//HZMC:hzxxData[0],
					HZBM:hzxxData[1],
					JCPCH:hzxxData[2]
				});
				//过滤买家
				//var jqMzmc = ui.getItemJQ("MZMC");
//				var jsonData=$.loadJson($.contextPath+'/jyzxx!getJyzxx.json?xtlx=TZ&zt=1&jyzbm='+ui.getItemJQ("HZBM").textbox("getValue"));
//				jqMzmc.combogrid("reload",jsonData.data);



				//根据生猪产地检疫证号获得两证数据
				var list = $.loadJson($.contextPath + '/tzjyxxxz!getLzxxByJyzh.json?szcdjyzh=' + newText);
				detailGrid.clearGridData();
				for(var i = 0;i < list.length;i++){
					var data = list[i];
					detailGrid.addRowData(data);
				}
				//计算交易数量及检疫检验证张数
				ui.setFormData({JYSL:list.length,JYJYZZS:list.length,SZCDJYZH:newText});
			});
			
			//买主名称下拉列表onChange事件 带入到达地
			jqMzmc.combogrid("option","onChange",function(e,data){
				var newText = data.newText;
				var newValue = data.newValue;
				ui.setFormData({MZMC:newText,MZBM:newValue});
				setDdd(newValue);
//				var jsonData=$.loadJson($.contextPath+'/jyzxx!getJyzxx.json?xtlx=TZ&zt=1&jyzbm='+ui.getItemJQ("MZBM").textbox("getValue"));
//				jqHzmc.combogrid("reload",jsonData.data);
			});
			
			//商品名称onChange事件
			jqSpmc.combogrid("option","onChange",function(e,data){
				var newText = data.newText;
				var newValue = data.newValue;
				ui.setFormData({SPMC:newText,SPBM:newValue});
			});
			
			//计算金额
			jqZl.textbox("option","onKeyUp",function(e,data){
				var zl = data.text;
				var dj = jqDj.textbox("getValue");
				if(!$.isNumeric(zl)) zl = 0;
				if(!$.isNumeric(dj)) dj = 0;
				var je = parseFloat(zl,10)*parseFloat(dj,10);
				ui.setFormData({JE:je});
			});
			jqDj.textbox("option","onKeyUp",function(e,data){
				var dj = data.text;
				var zl = jqZl.textbox("getValue");
				if(!$.isNumeric(zl)) zl = 0;
				if(!$.isNumeric(dj)) dj = 0;
				var je = parseInt(zl,10)*parseInt(dj,10);
				ui.setFormData({JE:je});
			});
			//到达地下拉列表onChange事件
			dddJQ.combogrid("option","onChange",function(e,data){
				var newText = data.newText;
				ui.setFormData({DDD:newText});
			});
		}
		
		/**
		*	回调函数
		*
		*
		*/
		ui.addCallback("setComboGridValue_Hzmc",function(o) {//货主名称弹出框回调
			
			if(null == o) return;
			var rowData = o.result;
			if(null == rowData) return;
			var jqMzmc = ui.getItemJQ("MZMC");
			var jqJyzh = ui.getItemJQ("SZCDJYZH");
			var jsonData = $.loadJson($.contextPath + '/tzjyxxxz!getJyzhByHzbm.json?hzbm=' + rowData.B_JYZBM);
			ui.setFormData({SZCDJYZH:""});
			jqJyzh.combogrid("reload",jsonData);
			ui.setFormData({
				HZMC:rowData.A_JYZMC,
				HZBM:rowData.B_JYZBM
			});
			jsonData=$.loadJson($.contextPath+'/jyzxx!getJyzxx.json?xtlx=TZ&zt=1&jyzbm='+ui.getItemJQ("HZBM").combogrid("getValue"));
			jqMzmc.combogrid("reload",jsonData.data);
		});
		ui.addCallback("setComboGridValue_Mzmc",function(o) {//买主名称弹出框回调
			if(null == o) return;
			var rowData = o.result;
			if(null == rowData) return;
			var jqHzmc = ui.getItemJQ("HZMC");
			ui.setFormData({
				MZMC:rowData.A_JYZMC,
				MZBM:rowData.B_JYZBM
			});
			setDdd(rowData.B_JYZBM);
			var jsonData=$.loadJson($.contextPath+'/jyzxx!getJyzxx.json?xtlx=TZ&zt=1&jyzbm='+ui.getItemJQ("MZBM").textbox("getValue"));
			jqHzmc.combogrid("reload",jsonData.data);
		});
		ui.addCallback("setComboGridValue_Jyzh",function(o) {//检疫证号弹出框回调
			if(null == o) return;
			var rowData = o.result;
			if(null == rowData) return;
			var detailGrid = ui.getDetailGrid();
			var list = new Array();
			//根据生猪产地检疫证号获取货主信息
			var hzxxData = $.loadJson($.contextPath + '/tzjyxxxz!getRpjyxx.json?szcdjyzh=' + rowData.SZCDJYZH);
			ui.setFormData({HZMC:hzxxData[0],HZBM:hzxxData[1],JCPCH:hzxxData[2]});
			//过滤卖家
			var jqMzmc = ui.getItemJQ("MZMC");
			var jsonData=$.loadJson($.contextPath+'/jyzxx!getJyzxx.json?xtlx=TZ&zt=1&jyzbm='+ui.getItemJQ("HZBM").combogrid("getValue"));
			jqMzmc.combogrid("reload",jsonData.data);
			//根据生猪产地检疫证号获得两证数据
			var list = $.loadJson($.contextPath + '/tzjyxxxz!getLzxxByJyzh.json?szcdjyzh=' + rowData.SZCDJYZH);
			detailGrid.clearGridData();
			for(var i = 0;i < list.length;i++){
				detailGrid.addRowData(list[i]);
			}
			//计算交易数量及检疫检验证张数
			ui.setFormData({JYSL:list.length,JYJYZZS:list.length,SZCDJYZH:rowData.SZCDJYZH});
		});
		ui.addCallback("setComboGridValue_Spmc",function(o) {//商品名称回调
			if(null == o) return;
			var rowData = o.result;
			if(null == rowData) return;
			ui.setFormData({
				SPMC:rowData.SPMC,
				SPBM:rowData.SPBM
			});
		});
		//到达地弹出框回调方法
		ui.addCallback("setComboGridValue_ddd",function(o){
			if(null == o) return;
			var obj = o.rowData;
			if(null == obj) return;
			ui.setFormData({DDD:obj.CDMC});
		});
		
		/**
		*	出参方法
		*
		*
		*/
		ui.addOutputValue("setTcsHzmc",function(o){//弹出式货主名称
			var mzbm = ui.getItemJQ("MZBM").textbox("getValue");
			if(mzbm == "" || mzbm == null){
				return {
					status : true,
					P_columns :null
				};
			}
			var o = {
				status : true,
				P_columns :  "EQ_C_B_ZT≡1;EQ_C_B_XTLX≡TZ;NOT_C_B_JYZBM≡"+mzbm
			}
			return o;
		});
		ui.addOutputValue("setTcsMzmc",function(o){//弹出式买主名称
			var hzbm = ui.getItemJQ("HZBM").combogrid("getValue");
			if(hzbm == "" || hzbm == null){
				return {
					status : true,
					P_columns :null
				};
			}
			var o = {
				status : true,
				P_columns :  "EQ_C_B_ZT≡1;EQ_C_B_XTLX≡TZ;NOT_C_B_JYZBM≡"+hzbm
			}
			return o;
		});
		ui.addOutputValue("setTcsJyzh",function(o){//弹出式检疫证号
			var tzcbm = ui.getItemJQ("TZCBM").textbox("getValue");
			var hzbm = ui.getItemJQ("HZBM").combogrid("getValue");
			var o = {
				status : true,
				P_columns : "(EQ_C_TZCBM≡" + tzcbm + ")AND(LIKE_C_HZBM≡" + hzbm + ")"
			}
			return o;
		});
		
		/**
		*	提取clickSaveAll方法
		*
		*/
		function secondDevSaveAll (){
			var dGrid = ui.getDetailGrid();
			if (!dGrid) return;
			var jqForm  = $("form", ui.uiForm),
			    rowData = dGrid.toFormData(), 
			    formData, url;
			// 表单检验
			if (!jqForm.form("valid")) return false;
			// 检验
			if (!rowData.length) {
				CFG_message("请先添加明细列表数据再保存！", "warning");
				return false;
			}
			// 获取表单数据
		    formData = jqForm.form("formData", false);
		    // 向列表添加数据
		    // 重置表单数据
		    url = ui.getAction() + "!saveAll.json?P_tableId=" + ui.options.tableId + "&P_D_tableIds=" + dGrid.options.tableId;
			// console.log("master: " + $.config.toString(formData));
			// console.log("detail: " + $.config.toString(rowData));
		    var flag;
		    $.ajax({
		    	url : url,
				type : "post",
				data : {E_entityJson: $.config.toString(formData),
					    E_dEntitiesJson: $.config.toString(rowData)},
				dataType : "json",
				async : false,
				success : function(rlt) {
					if (rlt.success) {
						//修改肉品检疫检验明细信息 交易状态
						$.ajax({
							type : "post",
							data : {Jyzh:formData.SZCDJYZH,RowData:JSON.stringify(rowData)},
							url : $.contextPath + '/tzjyxxxz!setJyzt.json',
							success : function(data){
							},
							error : function() {
								return;
							}
						});
						jqForm.form("loadData", rlt.data.master);
						dGrid.clearGridData();
						dGrid.addRowData(rlt.data.detail);
						CFG_message("保存成功！", "success");
						flag = "SUCCESS";
					} else {
						CFG_message(rlt.message, "warning");
						flag = "ERROR";
					}
				},
				error : function() {
					CFG_message("保存主从表数据失败！", "error");
					flag = "ERROR";
				}
		    });// */
		    return flag;
		}
		
		/**
		*	二次开发按钮
		*
		*
		*/
		ui.clickSecondDev = function(id){
			if(id==$.config.preButtonId+"readCard"){//读卡按钮
				//读卡按钮
				if (isSwt) {
					var result = _window("readCard", "userInfo");
					var resultData = JSON.parse(result);
					var value = resultData.value;
					if (resultData.status == "true" || resultData.status == true) {
						ui.setFormData({
							MZBM : value.userId,
							MZMC : value.userName
						});
						setDdd(value.userId);
						$.message({message:"读卡成功", cls:"info"});
					} else {
						$.message({message:resultData.msg, cls:"warning"});
					}
				}
				else {
					$.alert("请在程序中操作");
				}
			}
			
			if(id==$.config.preButtonId+"writeCardAndSaveAndPrint"){//写卡保存按钮
				if(isSwt){
			    	var detailArray = new Array();
			    	var jqForm  = $("form", ui.uiForm);
					var formData = jqForm.form("formData", false);
					var meatCode = {};
					meatCode[formData.SPBM] = formData.SPMC; 
		    		var data = {
	    				tracecode : formData.ZSM,//追溯码
	    				batchCode : formData.SZCDJYZH,//批次号
	    				sellerCode : formData.HZBM,
	    				sellerName : formData.HZMC,
	    				produceLocName : "??*",//生产基地名称
	    				businessMarketCode : formData.TZCBM,
	    				businessMarketName : formData.TZCMC,
	    				productCode : formData.SPBM,
	    				productPlaceCode : "??*",//产地编码
	    				dealCount : "1",
	    				dealWeight : formData.ZL,
	    				dealPrice : formData.DJ,
	    				carNo : "??*"
   					};
		    		detailArray.push(data);
			    	var date = formData.JYRQ;
			    	var bussinessInfo = {
			    			bDate:date.replace(/-/g,""),
			    			bCount:"1",
			    			bType:"?"
			    	}
			    	var result = _window("writeBusinessInfoAndDetailAndPrint","1",JSON.stringify(bussinessInfo),JSON.stringify(detailArray),JSON.stringify(meatCode),"TZ");
			    	var resultData = JSON.parse(result);
					if (resultData.status == "true" || resultData.status == true) {
						var saveStatus = secondDevSaveAll();//保存到数据库
						if("SUCCESS" === saveStatus){
							$.alert("写卡成功！：" + resultData.msg);
						}else if("ERROR" === saveStatus){
							_window("clearBusinessInfoAndDetail");
						}
					} else {
						$.alert("写卡失败：" + resultData.msg);
					}
			    }else{
			    	$.alert("请在程序环境中写卡");

			    }
			}
			
			if(id==$.config.preButtonId+"writeBarcodeAndSave"){//写码保存按钮
				if (isSwt || isNewSwt) {
					var saveStatus = secondDevSaveAll();
					if("SUCCESS" === saveStatus){
						var jqForm  = $("form", ui.uiForm);
						var formData = jqForm.form("formData", false);
						var detailArray = [];
				    	for(var i = 0;i < 1;i++){
							var totalPrice = formData.DJ * formData.ZL;
				    		var data = {
			    				tracecode : formData.ZSM,//追溯码
							    productName : formData.SPMC,
							    price : formData.DJ,
							    weight : formData.ZL,
								totalPrice:totalPrice
		   					};
				    		detailArray.push(data);
				    	}
				    	
				    	var detail = {
						    code : formData.JYTMH,
						    marketName : formData.TZCMC,
						    sellerName : formData.HZMC,
						    buyer : formData.MZMC,
						    details : detailArray
				    	}
						if(isSwt){
				    		var result = _window("printDetailWithBarCode",JSON.stringify(detail));
						}else if(isNewSwt){
							var d = new Date();
							var buyDate = d.getFullYear() + "年" + (d.getMonth() + 1) + "月" + d.getDate() + "日";
							var _result = _print.print("tzccsfd",{
								"code" : formData.JYTMH,
								"marketName":formData.TZCMC,
								"sellerName": formData.HZMC,
								"buyer": formData.MZMC,
								"buyDate":buyDate,
								"detailArray":detailArray,
								"urltracecode" :"http://www.zhuisuyun.net/spzsypt/html/tracingcodeSearch1.html?zsm=" +  formData.ZSM,//追溯码
								"tracecode" :formData.ZSM//追溯码
							});
						}
				    	var resultData = JSON.parse(result);
				    	if (resultData.status == "true" || resultData.status == true) {
				    		$.alert("写码成功！：" + resultData.msg);
				    	}else {
							$.alert("写码失败：" + resultData.msg);
						}
					}
				}
				else {
					$.alert("请在程序中操作");
				}
			}
		};
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
	if(1 === ui.options.number){
		
	}
	if(2 === ui.options.number){
		ui.clickSecondDev = function(id){
			masterForm = ui.getMasterForm();
			if(id==$.config.preButtonId+"del"){
				var rowIdArr = ui.getSelectedRowId();
				if (rowIdArr === null||0 === rowIdArr.length) {
					$.message({message: "请选择记录!", cls: "warning"});
					return;
				}
				//删除行
				for(var i = rowIdArr.length - 1; i > -1; i-- ){
					ui.uiGrid.grid("delRowData",rowIdArr[i]);
				}
				//更新主表交易数量和检疫检验证数量
				var rowData = ui.getRowData();
			    var length = rowData.length;
			   	masterForm.setFormData({JYSL:length,JYJYZZS:length});
			}
		};
		
		ui.toFormData = function(){
		 	var rData = ui.getRowData();
			return rData;
		}
	}
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
	if (1 === ui.options.number) {
		ui.processItems = function (data) {
			var btns = [];
//			btns.push({
//				id:$.config.preButtonId + "readCard",
//				label: "读卡",
//				type : "button"
//			});
//			btns.push({
//				id:$.config.preButtonId + "writeCardAndSaveAndPrint",
//				label: "保存",
//				type : "button"
//			});
			btns.push({
				id:$.config.preButtonId + "writeBarcodeAndSave",
				label: "写码保存",
				type : "button"
			});
			for (var i = 0; i < data.length; i++) {
				btns.push(data[i]);
			}
			return btns;
		};
	}else if(2 === ui.options.number){
		ui.processItems = function (data) {
			var btns = [];
			for (var i = 0; i < data.length; i++) {
				btns.push(data[i]);
			}
			btns.push({
				id:$.config.preButtonId + "del",
				label: "删除",
				type : "button"
			});
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
