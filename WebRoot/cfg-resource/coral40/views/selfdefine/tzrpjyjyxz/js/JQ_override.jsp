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
	return $.contextPath + "/tzrpjyjyxz";
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
	var inputid;
	if(1 === ui.options.number){
		/**
		*	加载数据后执行
		*
		*/
		ui._init = function(){
			inputid=ui.options.dataId;
			var jsonData = $.loadJson($.contextPath + '/tzqyda!getQyda.json?prefix=TZ');
			ui.setFormData({
				TZCMC:jsonData.tzcmc,
				TZCBM:jsonData.tzcbm
			});//屠宰场名称，屠宰场编码隐藏字段赋值
			
			//货主名称下拉列表数据加载
			var jqHzmc = ui.getItemJQ("HZMC");
			var jsonData = $.loadJson($.contextPath+'/jyzxx!getJyzxx.json?xtlx=TZ&zt=1');
			jqHzmc.combogrid("reload",jsonData.data);
			//加载生猪产地检疫证号下拉列表
			var jqJyzh = ui.getItemJQ("SZCDJYZH");
			var hzbm = ui.getItemJQ("HZBM").textbox("getValue");
			var jyzhData = $.loadJson($.contextPath + "/tzrpjyjyxz!getJyzhGridByHzbm.json?hzbm=" + hzbm);
			jqJyzh.combogrid("reload",jyzhData);

			if(inputid!=null){
				var hzmc=ui.getItemJQ('HZMC'); 
				var jjh=ui.getItemJQ('dwcd'); 
				var Xgdata = $.loadJson($.contextPath+'/jyzxx!getXgxx.json?id='+inputid); 

				var jsonData = $.loadJson($.contextPath + "/tzrpjyjyxz!getJyzhGridByHzbm.json?hzbm=" + Xgdata.HZBM);
				ui.setFormData({SZCDJYZH:Xgdata.SZCDJYZH});
				jqJyzh.combogrid("reload",jsonData);
				jqJyzh.combogrid("setValue",Xgdata.SZCDJYZH);
				hzmc.combogrid('setValue',Xgdata.HZBM); 
				jjh.combogrid('setValue',Xgdata.ID);
			}
		}
		
		/**
		*	绑定事件
		*
		*/
		ui.bindEvent = function(){
			var jqHzmc = ui.getItemJQ("HZMC");
			var jqJyzh = ui.getItemJQ("SZCDJYZH");
			
			//货主名称下拉列表onChange事件
			jqHzmc.combogrid("option","onChange",function(e,data){
				var newtext = data.newText;
				var newValue = data.newValue;
				var jsonData = $.loadJson($.contextPath + "/tzrpjyjyxz!getJyzhGridByHzbm.json?hzbm=" + newValue);
				ui.setFormData({SZCDJYZH:""});
				jqJyzh.combogrid("reload",jsonData);
				ui.setFormData({HZMC:newtext,HZBM:newValue});
			});
			
			//检疫证号下拉列表onChange事件
			jqJyzh.combogrid("option","onChange",function(e,data){
				var newText = data.newText;
				var jsonData = $.loadJson($.contextPath + "/tzrpjyjyxz!getSzjyxx.json?szcdjyzh="+newText);
				ui.setFormData({HZBM:jsonData[1]});
				ui.setFormData({
					SZCDJYZH:newText,
					HZMC:jsonData[0],
					HZBM:jsonData[1],
					SJJCSL:jsonData[2],
					JCPCH:jsonData[3]
				});
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
			var jsonData = $.loadJson($.contextPath + "/tzrpjyjyxz!getJyzhGridByHzbm.json?hzbm="+rowData.B_JYZBM);
			ui.setFormData({SZCDJYZH:""});
			jqJyzh.combogrid("reload",jsonData);
			ui.setFormData({
				HZMC:rowData.A_JYZMC,
				HZBM:rowData.B_JYZBM
			});
		});
		ui.addCallback("setComboGridValue_Jyzh",function(o) {//货主名称赋值
			if(null == o) return;
			var rowData = o.result;
			if(null == rowData) return;
			var jsonData = $.loadJson($.contextPath + "/tzrpjyjyxz!getSzjyxx.json?szcdjyzh="+rowData.SZCDJYZH);
			ui.setFormData({
				SZCDJYZH:rowData.SZCDJYZH,
				HZMC:rowData.HZMC,
				HZBM:rowData.HZBM,
				SJJCSL:jsonData[2],
				JCPCH:jsonData[3]
			});
		});
		
		/**
		*   出参方法
		*
		*/
		ui.addOutputValue("setTcsHzmc",function(o){//弹出式进场理货编码
			var tzcbm = ui.getItemJQ("TZCBM").textbox("getValue");
			var o = {
				status : true,
				P_columns : "(EQ_C_BALTJDBM≡"+tzcbm+") AND (EQ_C_ZT≡1)"
			}
			return o;
		});
		ui.addOutputValue("setTcsJyzh",function(o){//弹出式进场理货编码
			var tzcbm = ui.getItemJQ("TZCBM").textbox("getValue");
			var hzbm = ui.getItemJQ("HZBM").textbox("getValue");
			var o = {
				status : true,
				P_columns : "(EQ_C_TZCBM≡"+tzcbm+")AND(LIKE_C_HZBM≡"+hzbm+")AND(EQ_C_JYZT≡1)"
			}
			return o;
		});
		
		/**
		*	重写保存所有 方法
		*
		*/
		ui.clickSaveAll = function(){
			var dGrid = this.getDetailGrid();
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
			// 获取表单数据
		    formData = jqForm.form("formData", false);
			//校验
			if (rowData.length>formData.SJJCSL) {
				CFG_message("检验检疫证张数不得大于生猪实际进场数(" + formData.SJJCSL + ")", "warning");
				return false;
			}
		    // 向列表添加数据
		    // 重置表单数据
		    url = this.getAction() + "!saveAll.json?P_tableId=" + this.options.tableId + "&P_D_tableIds=" + dGrid.options.tableId;
		    
		    $.ajax({
		    	url : url,
				type : "post",
				data : {E_entityJson: $.config.toString(formData),
					    E_dEntitiesJson: $.config.toString(rowData)},
				dataType : "json",
				success : function(rlt) {
					if (rlt.success) {
						//修改生猪检疫 检疫状态
						$.ajax({
							type : "post",
							data : {Jyzh:formData.SZCDJYZH},
							url : $.contextPath + '/tzrpjyjyxz!setJyzt.json',
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
					} else {
						CFG_message(rlt.message, "warning");
					}
				},
				error : function() {
					CFG_message("保存主从表数据失败！", "error");
				}
		    });
		}
		
	}else if(2 === ui.options.number){
		
		/**
		*	自定义删除按钮
		*
		*/
		ui.clickSecondDev = function(id){
			masterForm = ui.getMasterForm();
			
			if(id==$.config.preButtonId+"del"){
				var cGrid = ui.getSelfGrid();
				var rowIdArr = cGrid.getSelectedRowId();
				if (rowIdArr === null||0 === rowIdArr.length) {
					$.message({message: "请选择记录!", cls: "warning"});
					return;
				}
				for(var i = rowIdArr.length - 1; i > -1; i-- ){
					cGrid.uiGrid.grid("delRowData",rowIdArr[i]);
				}
				var length = cGrid.getRowData().length;
				masterForm.getItemJQ("JYJYZZS").textbox("setValue",length);
			}
		};
		
		/**
		*	重写添加方法
		*
		*/
		ui.clickAdd = function(){
			var cGrid = ui.getSelfGrid(), // 对应列表
		    jqForm = $("form", ui.uiForm),
		    formData,
		    masterForm = ui.getMasterForm();
			// 表单检验
			if (!jqForm.form("valid")) return false;
			// 获取表单数据
		    formData = jqForm.form("formData", false);
			//校验现有行数是否等于实际进场数量
			var length = cGrid.getRowData().length;
			var sjjcsl = masterForm.getItemJQ("SJJCSL").textbox("getValue");
			if(sjjcsl!=null&&sjjcsl!=""){
				if(length >= sjjcsl){
					$.message({message: "检验检疫证张数不得大于生猪实际进场数量(" + sjjcsl + ")!", cls: "warning"});
					return;
				}
			}
		    // 向列表添加数据
			cGrid.addRowData(formData);
		    //计算列表行数并校验是否不超过实际进场，赋值检疫证张数
		    masterForm.setFormData({JYJYZZS:++length});
		    // 重置表单数据
			this.clickCreate();
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
	/**
	 * 添加自定义按钮
	 * data为原来的按钮
	 */
	if(1 === ui.options.number){
		
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
