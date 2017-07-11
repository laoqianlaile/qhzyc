<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-04-21 17:14:46
 * 系统配置平台自动生成的自定义构件二次开发JS模板
 * 详细的二次开发操作，请查看文档（二次开发手册/自定义构件.docx）
 * 注：请勿修改模板结构（若需要调整结构请联系系统配置平台）
 ***************************************************/
 
 
(function(subffix) {

/**
 * 二次开发定位自己controller
 * 系统默认的controller: jQuery.contextPath + "/appmanage/show-module"
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return jQuery.contextPath + "/sdzyczzdkxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	// ui.assembleData 就是 configInfo
	//ui.bindEvent = function () {
	//	  ui.setItemOption("NAME", "onChange", function(e, data) {});// 添加onChange事件
	//    ui.callItemMethod("USER_ID", "disable");// 禁用USER_ID
	//};
	ui._init = function(){
		var qybm = ui.getItemJQ("QYBM");
		var dataid = ui.options.dataId;
		if(isEmpty(dataid)){
			var qyxx = $.loadJson($.contextPath+'/trace!getQybm.json');
			qybm.textbox("setValue",qyxx);
		}
		ui.getItemJQ("DKFZR").combogrid("reload",$.contextPath+"/zzgzryda!getGzrydaGrid.json?dwlx=ZZQY");
	}

	ui.bindEvent = function () {
		ui.setItemOption("DKFZR", "onChange", function (e, data) {
			var rowData = ui.getItemJQ("DKFZR").combogrid("grid").grid("getRowData", data.value);
			ui.setFormData({
				DKFZRBH: rowData.GZRYBH,
				DKFZR:rowData.XM,
				LXDH:rowData.LXFS
			});
		})
		ui.setItemOption("JDMC", "onChange", function (e, data) {
			var rowData = ui.getItemJQ("JDMC").combogrid("grid").grid("getRowData", data.value);
			ui.setFormData({
				JDMC: rowData.JDMC,
				JDBH:rowData.JDBH,
				MJ:rowData.MJ
			});
		})
        //判断地块面积小于基地面积
        ui.setItemOption("MJ","onChange",function(e,data){
            var mj = ui.getItemValue("MJ");
            var jdbh  = ui.getItemValue("JDBH");
            var jsonData=  $.loadJson($.contextPath+'/sdzyczzdkxx!getJdxx.json?jdbh='+jdbh);
            if(mj > jsonData.data[0].MJ){
                CFG_message("请输入小于"+jsonData.data[0].MJ+"的数字","warning");
                ui.setFormData({MJ:jsonData.data[0].MJ});
            }
        });
	}

	ui.addOutputValue("setCombogridValue_fzr", function (o){

		return {
			status : true ,
			P_columns: 'EQ_C_DWLX≡ZZQY'
		}
	});

	ui.addCallback("setComboGridValue_dkfzr",function(o){
		if (null == o) return;
		var rowData = o.rowData;
		if (null == rowData) return;
		ui.setFormData({
			DKFZR:rowData.XM,
			DKFZRBH: rowData.GZRYBH,
			LXDH:rowData.LXFS

		});
	});

	ui.addCallback("setComboGridValue_jdmc",function(o){
		if (null == o) return;
		var rowData = o.rowData;
		if (null == rowData) return;
		ui.setFormData({
			JDMC:rowData.JDMC
		});
	});
};

/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
    //弹出式传感器组过滤
    ui.addOutputValue("setTcsCgqz",function(o){
        //var qybm=ui.getItemValue("QYBM");
        rowId = this.uiGrid.grid("option", "selrow");
        var o = {
            status:true,
            P_columns : "EQ_C_QYBM="+rowId
        }
        return o;
    });

};

/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	// ui.assembleData 就是 configInfo
};

/**
 *  二次开发：复写基本查询区
 */
function _override_bsearch (ui) {
	// ui.assembleData 就是 configInfo
	//ui.bindEvent = function () {
	    // 添加onChange事件
	//	  ui.setItemOption("NAME", "onChange", function(e, data) {})
	//};
	ui._init = function(){
		ui.getItemJQ("DKFZR").combogrid("reload",$.contextPath+"/zzgzryda!getGzrydaGrid.json?dwlx=ZZQY");
		//add by zhangyipeng
		var dkfzr = document.getElementById("DKFZR_button");
		dkfzr.style.marginRight = "-30px";
		//add by zhangyipeng
		var jdmc = document.getElementById("JDMC_button");
		jdmc.style.marginRight = "-30px";
	};


    ui.addOutputValue("setCombogridValue_fzr", function (o){

        return {
            status : true ,
            P_columns: 'EQ_C_DWLX≡ZZQY'
        }
    });

    ui.addCallback("setComboGridValue_dkfzr",function(o){
        if (null == o) return;
        var rowData = o.rowData;
        if (null == rowData) return;
        ui.setItemValue("DKFZR",rowData.XM);
    });

    ui.addCallback("setComboGridValue_jdmc",function(o){
        if (null == o) return;
        var rowData = o.rowData;
        if (null == rowData) return;
        ui.setItemValue("JDMC",rowData.JDMC);

    });
};

/**
 *  二次开发：复写高级查询区
 */
function _override_gsearch (ui) {
	// ui.assembleData 就是 configInfo
};

/**
 *  二次开发：复写自定义布局
 */
function _override_layout (ui) {
	// ui.assembleData 就是 configInfo
};

/**
 *  二次开发：复写自定义树
 */
function _override_tree (ui) {
	// ui.assembleData 就是 configInfo
};

/***
 * 当前构件全局函数实现位置
 * 如果有需要的，可打开以下实现体
 * 使用方法： 与开发构件一致
 **/
//jQuery.extend(jQuery.ns("ns" + subffix), {
//	name : "",
//	click: function() {}
//});









/**
 * 在此可以复写所有自定义JS类
 */
window[CFG_overrideName(subffix)] = function () {
	if (this instanceof jQuery.config.cform) {
		_override_form(this);
	} else if (this instanceof jQuery.config.cgrid) {
		_override_grid(this);
	} else if (this instanceof jQuery.config.cbsearch) {
		_override_bsearch(this);
	} else if (this instanceof jQuery.config.cgsearch) {
		_override_gsearch(this);
	} else if (this instanceof jQuery.config.ctree) {
		_override_tree(this);
	} else if (this instanceof jQuery.config.ctbar) {
		_override_tbar(this);
	} else if (this instanceof jQuery.config.clayout) {
		_override_layout(this);
	}
};

	
	
	
	
})("${timestamp}");
</script>
