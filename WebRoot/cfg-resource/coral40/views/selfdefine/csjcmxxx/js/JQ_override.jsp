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
	return $.contextPath + "/csjcmxxx";
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

	ui._init = function(){
		if(ui.options.number==1){
			var jhpch = $("input[name='JHPCH']", this.uiForm);
			var scjcbh = $("input[name='SCJCBH']", this.uiForm);
			var csbm = $("#CSBM_"+ui.timestamp);
			var csmc = $("#CSMC_"+ui.timestamp);
			//加载蔬菜进场编号
			if(scjcbh.textbox("getValue")==null||scjcbh.textbox("getValue")==''){
				jQuery.ajax({
					type:'post',
					url:$.contextPath +'/csscjcxx!getScjcbh.json',
					dataType:'json',
					success:function(data){
						scjcbh.textbox("setValue",data.scjcbh);
					},
					error:function(data){
					}
				});
			}
			//加载表单数据
			if(jhpch.textbox("getValue")==null||jhpch.textbox("getValue")==''){
				jQuery.ajax({
					type:'post',
					url:$.contextPath +'/csscjcxx!initFormData.json',
					dataType:'json',
					success:function(data){
						jhpch.textbox("setValue",data.jhpch);
						csbm.textbox("setValue",data.csbm);
						csmc.textbox("setValue",data.csmc);
					},
					error:function(data){
					}
				});
			}
			//加载下拉列表供应商名称
			var jqGys = ui.getItemJQ("GYSMC");
			var gysData =$.loadJson($.contextPath +"/jyzxx!getJyzxx.json?xtlx=CS&zt=1");
			jqGys.combogrid("reload",gysData.data);
			//初始化供应商名称
			if(!isEmpty(ui.options.dataId)){
				var gysxxData = $.loadJson($.contextPath + "/jyzxx!getGysxxData.json?id="+ui.options.dataId);
				jqGys.combogrid("setValue",gysxxData.GYSMC);
			}
			//加载菜品名称
			var spmcData = $.loadJson($.contextPath + "/lsjcxxxz!getAllSpmc.json");
			for(var i=0;i<spmcData.length;i++){
				spmcMap.put(spmcData[i][0],spmcData[i][1]);
			}

		}else{
			var jqSpmc = ui.getItemJQ("SPMC");
			var jsonData = $.loadJson($.contextPath + "/cpfl!getCpflGrid.json");
			jqSpmc.combogrid("reload",jsonData.data);
		}
	}
	function Map() {
			var struct = function(key, value) { 
						this.key = key; 
						this.value = value;
			}  
	   
	 		var put = function(key, value){  
	  				for (var i = 0; i < this.arr.length; i++) {
	  					if ( this.arr[i].key === key ) {
	  						this.arr[i].value = value; return;
	  					}
	  				}  
	   				this.arr[this.arr.length] = new struct(key, value);
	   		}  
	   
	 		var get = function(key) {  
	  				for (var i = 0; i < this.arr.length; i++) {  
	   					if ( this.arr[i].key === key ) { 
	   						return this.arr[i].value; 
	   					}  
	  				}  
	  				return null;  
	 		}
	 		this.arr = new Array();  
			this.get = get;  
			this.put = put;  
	 }
	var spmcMap = new Map();

	var barCode = "";
	if(ui.options.number==1){

		/**
		 * 获取弹出框选择的供应商信息
		 */
		ui.addCallback("getJyzInfo",function(data) {
			if(data==null) return;
			var jyzInfo = data.jyzInfo;
			if(jyzInfo==null) return;
			//填充form表单
			$(ui.uiForm).form();
			var jyzData = {"GYSMC":jyzInfo.A_JYZMC,"GYSBM":jyzInfo.A_JYZBM};
			ui.uiForm.form("loadData",jyzData);
		});
		/**
		 * 获取弹出框选择的产地信息
		 */
		ui.addCallback("getCdxx",function(data) {
			if(data==null) return;
			var cdxx = data.cdmc;
			if(cdxx==null) return;
			//填充form表单
			$(ui.uiForm).form();
			var cdxxData = {"CDMC":cdxx.CDMC,"CDBM":cdxx.CDBM};
			ui.uiForm.form("loadData",cdxxData);
		});

		//设置经营者下拉列表
		ui.addCallback("setComboGridValue_Gysmc",function(o){
			if( null == o ) return ;
			var rowData = o.result;
			if( null == rowData )  return ;
			ui.setFormData({GYSMC:rowData.A_JYZMC,GYSBM:rowData.A_JYZBM});
		});

		ui.bindEvent = function () {
			//供应商名称下拉列表
			var jqGysmc = $("#GYSMC_" + ui.timestamp);
			var jqCdmc = $("#CDBM_" + ui.timestamp);
			jqGysmc.combogrid("option","onChange",function(e,data){
				var text = data.text;
				var value = data.value;
				ui.setFormData({GYSMC:text,GYSBM:value});
			});
			//产地名称下拉列表
			jqCdmc.combogrid("option","onChange",function(e,data){
				var text = data.text;
				var value = data.value;
				ui.setFormData({CDMC:text,CDBM:value});
			});
		}
		//保存主从表
		ui.clickSaveAll = function (){
			var _this = this, dGrid = this.getDetailGrid(), op = "saveAll";
			if (!dGrid) return;
			var jqForm  = $("form", this.uiForm),
					rowData = dGrid.toFormData(),
					formData, url;
			// 表单检验
			if (!jqForm.form("valid")) return false;
			// 检验
			if (!rowData.length) {
				CFG_message("请先添加明细列表数据再保存！", "warning");
				return false;
			}
			// 保存前回调方法
			if (_this.processBeforeSave(jqForm, op) === false) return;
			// 获取表单数据
			formData = jqForm.form("formData", false);
			// 向列表添加数据
			// 重置表单数据
			url = this.getAction() + "!saveAll.json?P_tableId=" + this.options.tableId + "&P_D_tableIds=" + dGrid.options.tableId
					+ "&P_componentVersionId=" + this.options.componentVersionId
					+ "&P_menuId=" + this.options.menuId
					+ "&P_menuCode=" + CFG_getInputParamValue(this.assembleData, "menuCode");
			// console.log("master: " + $.config.toString(formData));
			// console.log("detail: " + $.config.toString(rowData));

			$.ajax({
				url : url,
				type : "post",
				data : {E_entityJson: $.config.toString(formData),
					E_dEntitiesJson: $.config.toString(rowData),
					barCode:barCode},
				dataType : "json",
				success : function(rlt) {
					if (rlt.success) {
						jqForm.form("loadData", rlt.data.master);
						dGrid.clearGridData();
						dGrid.addRowData(rlt.data.detail);
						CFG_message("保存成功！", "success");
						// 保存后回调方法
						_this.processAfterSave(rlt.data, op);
					} else {
						CFG_message(rlt.message, "warning");
					}
				},
				error : function() {
					CFG_message("保存主从表数据失败！", "error");
				}
			});// */
		}
		//添加读码功能
		ui.clickSecondDev = function(id){
			if (id==$.config.preButtonId+"inputBarCode") {
				showDialog();
			}
			if(id==$.config.preButtonId+"dk"){
				/**
				 * 从卡内读取理货卡信息在表单上显示
				 */
			 	if (isSwt) {
					var result = _window("readUserInfoAndBusinessDetail");

					var res = JSON.parse(result);
					if(res.status == false || res.status == "false"){
						$.message( {message:res.msg, cls:"warning"});
						return;
					}
					var user = res.user;
					var records = res.records;
					if(res.status == "true" || res.status == true){
						//判断卡类型是否为菜卡
						if(user.cardType != 2 || user.cardType != '2'){
							$.message( {message:"本卡不是菜卡，请选择菜卡进行操作！", cls:"info"});
							return;
						}
						//读出经营者
						ui.setFormData({GYSMC:user.userName,GYSBM:user.userId});
						//读出交易明细
						var cGrid = ui.getDetailGrid();
						var cForm = ui.getDetailForm();
						for(var i = 0; i<records.length;i++){
							var detailData = records[i];
							cForm.SPMC = spmcMap.get(detailData.productCode);
							cForm.ZL = detailData.dealWeight;
							cForm.DJ = detailData.dealPrice;
							cForm.JHPCH = detailData.batchCode;
							cForm.SPBM = detailData.productCode;
							cForm.LSPZH = detailData.tracecode;
							//计算批次金额
							if (!$.isNumeric(detailData.dealWeight)) cForm.ZL = 0;
							if (!$.isNumeric(detailData.dealPrice)) cForm.DJ = 0;
							cForm.JE = parseFloat(cForm.ZL, 10) * parseFloat(cForm.DJ, 10);
							//向表单中添加数据
							cGrid.addRowData(cForm);
						}
						//更新主表单理货总重量和总金额
						var rowData = cGrid.getRowData(), zl,je,lhzzl=0, lhzje=0;
					    for (var i = 0, len = rowData.length; i < len; i++) {
					    	zl = rowData[i].ZL;
					    	je = rowData[i].JE;
					    	if (!$.isNumeric(zl)) zl = 0;
					    	if (!$.isNumeric(je)) je = 0;
					    	lhzzl += parseInt(zl, 10);
					    	lhzje += parseInt(je, 10);
					    }
					    $(this.uiForm).form();
						this.uiForm.form("loadData",{ZZL: lhzzl,ZJE:lhzje});
						$.message( {message:"读卡成功", cls:"info"});
					}
				} else {
					$.alert("请在程序中操作");
				}
			 }
		}

		function setPfsxx(jyzbm,jyzmc,barCode){//根据读码信息获取本市场内该经营者信息
			var csbm = ui.getItemJQ("CSBM").textbox("getValue");
			var getJyzId = $.loadJson($.contextPath+'/jyzxx!getJyzxxByBmAndQybm.json?jyzbm='+jyzbm+"&qybm="+csbm);
			if("FATAL" == getJyzId){
				CFG_message("批发商信息错误，请录入！", "warning");
			}else if("SUCCESS" == getJyzId){
				ui.setFormData({
					GYSMC:jyzmc,
					GYSBM:jyzbm
				});
			} else {
				var id = getJyzId;
				var createJyz = $.loadJson($.contextPath + '/jyzxx!createJyz.json?id='+id+"&qybm="+csbm+"&xltx=CS&barCode="+barCode);
				ui.setFormData({
					GYSMC:jyzmc,
					GYSBM:jyzbm
				});
				CFG_message("已添加经营者:"+jyzmc+"！", "success");
			}
		}

	}else if(ui.options.number==2){
		var masterForm = ui.getMasterForm();
		var masterFormData =  $('form',masterForm.uiForm).form('formData',false);

		/**
		 * 获取弹出框商品信息
		 */
		ui.addCallback("getSpxx",function(data) {
			if(data==null) return;
			var spxx = data.spxx;
			if(spxx==null) return;
			//填充form表单
			$(ui.uiForm).form();
			var spxxData = {"SPMC":spxx.SPMC,"SPBM":spxx.SPBM};
			ui.uiForm.form("loadData",spxxData);
		});
		ui.bindEvent = function () {
			//商品名称的下拉列表
			var jqSpbm = $("#SPMC_" + ui.timestamp);
			var je = $("input[name='JE']", this.uiForm),
			zl = $("input[name='ZL']", this.uiForm),
			js = $("input[name='JS']", this.uiForm),
			dj = $("input[name='DJ']", this.uiForm);
			jqSpbm.combogrid("option","onChange",function(e,data){
				var text = data.text;
				var value = data.value;
				ui.setFormData({SPMC:text,SPBM:value});
			});
			function countTotal(){
			    var zlValue = zl.textbox("getValue"),
                    jsValue = js.textbox("getValue"),
                    djValue = dj.textbox("getValue"),
                    jeValue;
                if (!$.isNumeric(djValue)) djValue = 0;
                if (!$.isNumeric(zlValue)) zlValue = 0;
                if (!$.isNumeric(jsValue)) jsValue = 0;
                jeValue = parseFloat(djValue, 10) * parseFloat(zlValue, 10) + parseFloat(djValue, 10) * parseFloat(jsValue, 10);
                je.textbox("setValue", Math.round(jeValue*100)/100);
			}
			zl.textbox("option", "onChange", function(e, data) {
                countTotal();
			});
			js.textbox("option", "onChange", function(e, data) {
                countTotal();
            });
			zl.textbox("option", "onKeyUp", function(e, data) {
			    if (data.text.length > 0){
                    js.textbox({readonly:true});
                }
                else{
                    js.textbox({readonly:false});
                }
            });
            js.textbox("option", "onKeyUp", function(e, data) {
                if (data.text.length > 0){
                    zl.textbox({readonly:true});
                }
                else{
                    zl.textbox({readonly:false});
                }
            });
			dj.textbox("option", "onChange", function(e, data) {
				countTotal();
			});
		}
		ui.clickAdd = function(){
			var masterForm = ui.getMasterForm();
			var masterFormData =  $('form',masterForm.uiForm).form('formData',false);
			var cGrid = this.getSelfGrid(), // 对应列表
			    jqForm = $("form", this.uiForm),
			    formData;
			// 表单检验
			if (!jqForm.form("valid")) return false;
			// 获取表单数据
		    formData = jqForm.form("formData", false);
		    //写入进货批次号
		    formData.JHPCH = masterFormData.JHPCH;
		    //添加零售凭证号
		    jQuery.ajax({
					type:'post',
					url:$.contextPath +'/csjcmxxx!getLspzh.json',
					dataType:'json',
					async:false,
					success:function(data){
						formData.LSPZH = data.lspzh;
					},
					error:function(data){
					}
				});
		    // 向列表添加数据
			cGrid.addRowData(formData);
		    // 重置表单数据
			ui.clickCreate();
			//更新主表单理货总重量和总金额
			var rowData = cGrid.getRowData(), zl,je,lhzzl=0, lhzjs=0, lhzje=0;
		    for (var i = 0, len = rowData.length; i < len; i++) {
		    	zl = rowData[i].ZL;
		    	js = rowData[i].JS;
		    	je = rowData[i].JE;
		    	if (!$.isNumeric(zl)) zl = 0;
		    	if (!$.isNumeric(js)) js = 0;
		    	if (!$.isNumeric(je)) je = 0;
		    	lhzzl += parseFloat(zl, 10);
		    	lhzjs += parseFloat(js, 10);
		    	lhzje += parseFloat(je, 10);
		    }
		    zl = $("input[name='ZL']", this.uiForm).textbox({readonly:false});
            js = $("input[name='JS']", this.uiForm).textbox({readonly:false});
			masterForm.setFormData({ZZL: lhzzl,ZJE:lhzje, ZJS:lhzjs});

		};
		//删除功能
		ui.clickSecondDev = function(id){
			if(id==$.config.preButtonId+"del"){
				var cGrid = this.getSelfGrid();
				var rowIdArr = cGrid.getSelectedRowId();
				if (rowIdArr === null||0 === rowIdArr.length) {
					$.message({message: "请选择记录!", cls: "warning"});
					return;
				}
				for(var i = rowIdArr.length - 1; i > -1; i-- ){
					cGrid.uiGrid.grid("delRowData",rowIdArr[i]);
				}
				//更新主表单理货总重量和总金额
				var rowData = cGrid.getRowData(), zl,je,lhzzl=0, lhzje=0;
			    for (var i = 0, len = rowData.length; i < len; i++) {
			    	zl = rowData[i].ZL;
			    	je = rowData[i].JE;
			    	if (!$.isNumeric(zl)) zl = 0;
			    	if (!$.isNumeric(je)) je = 0;
			    	lhzzl += parseInt(zl, 10);
			    	lhzje += parseInt(je, 10);
			    }
				masterForm.setFormData({ZZL: lhzzl,ZJE:lhzje});
			}
		};

		ui.addCallback("setComboGridValue_Spmc",function(o){
			if( null == o ) return ;
			var rowData = o.result;
			if( null == rowData )  return ;
			ui.setFormData({SPMC:rowData.SPMC,SPBM:rowData.SPBM});
		});
	}

	function checkSpxx(spxxArray){//读码添加本门店没有的商品
		var spbmArray = new Array();
		var addedSpmcArray = new Array();
		for(var i = 0;i<spxxArray.length;i++){
			spbmArray.push(spxxArray[i].SPBM);
		}
		var exsitSp = $.loadJson($.contextPath + '/qyptmdspgl!checkSpid.json?spbm=' + JSON.stringify(spbmArray));
		if(exsitSp!=null&&exsitSp.length!=0){
			for(var i = 0;i<spxxArray.length;i++){
				for(var j = 0;j<exsitSp.length;j++){
					if(spxxArray[i].SPBM!=exsitSp[j].SPBM){
						if(j == exsitSp.length-1){
							addedSpmcArray.push(spxxArray[i].SPMC);
						}
						continue;
					}
					break;
				}
			}
		} else {
			for(var i = 0;i<spxxArray.length;i++){
				addedSpmcArray.push(spxxArray[i].SPMC);
			}
		}

		if(addedSpmcArray.length>0){
			CFG_message("已添加商品"+JSON.stringify(addedSpmcArray)+"至系统","success");
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
		prefix = barCode.substring(0,2);
		var detailForm = ui.getDetailForm();
		var detailGrid = ui.getDetailGrid();
		if(barCode.length != 22){
			CFG_message("请输入有效的条形码!", "warning");
			return;
		}
		if("ZZ" === prefix){
			var jsonData = $.loadJson($.contextPath + '/csjcmxxx!getZzccxx.json?zztmh='+barCode);
			if ("ERROR" === jsonData) {
				CFG_message("该条码已被使用!", "warning");
			}else{
				//添加系统内不存在的商品
				if(jsonData.SPMC!=null&&jsonData.SPMC!=""&&jsonData.SPBM!=null&&jsonData.SPBM!=""){
					var spxx = {};
					var spxxArray = new Array();
					spxx.SPMC = jsonData.SPMC;
					spxx.SPBM = jsonData.SPBM;
					spxxArray.push(spxx);
					checkSpxx(spxxArray);
				}
				//加载主表数据
				ui.setFormData({
					CDPZH:jsonData.CDZMH,
					CDMC:jsonData.CDMC
				});
				detailGrid.clearGridData();
				//加载从表列表数据
				detailForm.SPMC=jsonData.SPMC;
				detailForm.SPBM=jsonData.SPBM;
				detailForm.ZL=jsonData.ZL;
				detailForm.JHPCH=jsonData.CCPCH;
				detailForm.LSPZH=jsonData.ZSM;
				detailGrid.addRowData(detailForm);
				//更新主表理货重量 金额
				var rowData = detailGrid.getRowData(), zl,je,zzl=0,zje=0;
				for (var i = 0, len = rowData.length; i < len; i++) {
					zl = rowData[i].ZL;
					je = rowData[i].JE;
					if (!$.isNumeric(zl)) zl = 0;
					if (!$.isNumeric(je)) je = 0;
					zzl += parseInt(zl, 10);
					zje += parseInt(je, 10);
				}
				ui.setFormData({
					ZZL : zzl,
					ZJE : zje,
					JHPCH : ""
				});
			}
		}else if("PC" === prefix){
			var jsonData = $.loadJson($.contextPath + '/csjcmxxx!getPcjyxx.json?pctmh='+barCode);
			if("ERROR" === jsonData){
				CFG_message("该条码已被使用!", "warning");
			}else{
				var lhxx = jsonData.lhxx;
				var jymxxx = jsonData.jymxxx;
				var gysmc = lhxx.LSSMC;
				var gysbm = lhxx.LSSBM;
				if(jymxxx!=null&&jymxxx.length>0){
					var spxxArray = new Array();
					for(var i = 0 ; i<jymxxx.length ; i++){
						var spxx = {};
						spxx.SPMC = jymxxx[i].SPMC;
						spxx.SPBM = jymxxx[i].SPBM;
						spxxArray.push(spxx);
					}
					checkSpxx(spxxArray);
				}
				setPfsxx(gysbm,gysmc,barCode);
				//加载主表数据
				ui.setFormData({
					CDPZH:lhxx.CDZMH,
					CDMC:lhxx.CDMC
				});
				detailGrid.clearGridData();
				//加载从表列表数据
				for(var i = 0;i<jymxxx.length;i++){
					detailForm.SPMC=jymxxx[i].SPMC;
					detailForm.SPBM=jymxxx[i].SPBM;
					detailForm.ZL=jymxxx[i].ZL;
					detailForm.DJ=jymxxx[i].DJ;
					detailForm.JE=jymxxx[i].JE;
					detailForm.JHPCH=jymxxx[i].JHPCH;
					detailForm.LSPZH=jymxxx[i].ZSM;
					detailGrid.addRowData(detailForm);
				}
				//更新主表理货重量 金额
				var rowData = detailGrid.getRowData(), zl,je,zzl=0, zje=0;
				for (var i = 0, len = rowData.length; i < len; i++) {
					zl = rowData[i].ZL;
					je = rowData[i].JE;
					if (!$.isNumeric(zl)) zl = 0;
					if (!$.isNumeric(je)) je = 0;
					zzl += parseInt(zl, 10);
					zje += parseInt(je, 10);
				}
				ui.setFormData({
					ZZL : zzl,
					ZJE : zje,
					JHPCH : ""
				});
			}
		}else {
			CFG_message("请输入以ZZ或PC开头的条形码!", "warning");
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
	ui.eSelectRow = function(e, data) {return null;};
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
	if(ui.options.number == 1){
		ui.processItems = function (data) {
			var btns = [];
			/* Modified at 2016.1.5 by Guoshun, 将条码输入移出至'蔬菜快捷进场(原蔬菜扫码进场)'*/
			/*
			btns.push({
				id:$.config.preButtonId + "inputBarCode",
				label: "输入条码",
				type : "button"
			});

            // 未实现, 暂时屏蔽
			btns.push({
				id:$.config.preButtonId + "dk",
				label: "读卡",
				type : "button"
			});
            */
			for (var i = 0; i < data.length; i++) {
				btns.push(data[i]);
			}
			return btns;
		};
	}else if(ui.options.number ==2 ){
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
