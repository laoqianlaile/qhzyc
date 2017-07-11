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
	return $.contextPath + "/appmanage/show-module";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {

	/**
	 * 二次开发第一区域表单功能
	 */
	if (ui.options.number === 1) {
		/**
		*	数据加载后执行
		*	
		*/
		ui._init = function() {
			ui.setFormData({
				A_LTKLX:"2"
			});
		}
		ui.bindEvent = function(){
			var jqJyzmc = ui.getItemJQ("B_JYZMC");
			jqJyzmc.combogrid("option","onChange",function(e,data){
				var newValue = data.newValue;
                var jsonData = $.loadJson($.contextPath + "/jyzxx!getJyzInfo.json?jyzbm=" + newValue);
                ui.setFormData({
                    B_JYZMC:jsonData.JYZMC,
                    B_GSZCDJZHHSFZH:jsonData.GSZCDJZHHSFZH,
                    B_JYZXZ:jsonData.JYZXZ,
                    B_FRDB:jsonData.FRDB,
                    B_SJHM:jsonData.SJHM,
                    A_JYZBM:jsonData.JYZBM
                });
			});
		}; 
		/**
		 * 添加方法：获取表单值，传值给虚拟逻辑构件
		 * isSwt：判断是否在SWT程序环境中打开
		 * _window：写卡，成功后才能调用逻辑构件
		 */
		ui.addOutputValue("cardRepair", function (o) {//补卡
			var o = {
				status: false,
				operate: "cardRepair",
				table:"PC"
			}
			if (isSwt) {
				var formData = ui.getFormData();
				if (formData.A_LTKH == "" || formData.A_JYZBM == "" || formData.B_JYZMC == "") {
					$.message({message:"请填写必填字段", cls:"warning"});
				} else {
					var data = {
						cardType: formData.A_LTKLX,
						cardNo: formData.A_LTKH,
						userId: formData.A_JYZBM,
						userName: formData.B_JYZMC,
						userCertificate: formData.B_GSZCDJZHHSFZH,
						userPhone: formData.B_SJHM,
						userType: formData.B_JYZXZ,
						other: formData.B_FRDB
					}
					var result = _window("writeUserInfo", JSON.stringify(data));
					var resultData = JSON.parse(result);
					if (resultData.status == "true" || resultData.status == true) {
						o.value = $.config.toString(formData);
						o.status = true;
					} else {
						$.alert("写卡失败：" + resultData.msg);
					}
				}
			} else {
				$.alert("请在程序环境中写卡");
			}
			return o;
		});
		/**
		 * 逻辑构件回调函数：获取后台保存信息的返回值
		 * 后台保存成功则弹出提示，否则清空卡内数据后再弹出提示信息
		 */
		ui.addCallback("getResultRepair", function (res) {
			var operate = res.operate;
			var result = res.result;
			var value = res.value;
			if (result == true || result == "true") {//后台保存失败
				$.message( {message:"写卡成功", cls:"info"});
			} else {
				_window("clearUserInfo");
				$.message( {message:"写卡失败", cls:"warning"});
			}
		});
		
		ui.addCallback("setComboGridValue_Jyzmc",function(o) {//获取经营者下拉列表
			if(null == o) return;
			var rowData = o.result;
			if(null == rowData) return;
            var jsonData = $.loadJson($.contextPath + "/jyzxx!getJyzInfo.json?jyzbm=" + rowData.A_JYZBM);
            ui.setFormData({
                B_JYZMC:jsonData.JYZMC,
                B_GSZCDJZHHSFZH:jsonData.GSZCDJZHHSFZH,
                B_JYZXZ:jsonData.JYZXZ,
                B_FRDB:jsonData.FRDB,
                B_SJHM:jsonData.SJHM,
                A_JYZBM:jsonData.JYZBM
            });
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
