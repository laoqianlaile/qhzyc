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
	return $.contextPath + "/appmanage/show-module";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
    //修改操作加载表单数据
    var dataId = CFG_getInputParamValue(ui.assembleData.parentConfigInfo, "CCGLID");
    var isView = CFG_getSelfParamValue(ui.assembleData.parentConfigInfo, "ISVIEW");
    var bzid= CFG_getInputParamValue(ui.assembleData.parentConfigInfo, "BZID");
    ui.options.dataId = dataId;
    ui._init = function () {
        //主表数据id 属性，提供列表区域调用
        ui.assembleData.dataId = dataId;
        //form id
        ui.assembleData.cFormDivId = ui.element.attr("id");
        //新增
        if(isEmpty(ui.options.dataId)){

        }
        //详细查看
        if(isView=="YES"){
           $("form", ui.uiForm).form("setReadOnly", true);

        }
    };

    //绑定事件
    ui.bindEvent = function() {
        var khbh = ui.getItemJQ("KHBH");
        var xsddh = ui.getItemJQ("XSDDH");
        //客户编号下拉列表onchange事件
        khbh.combogrid("option","onChange",function(e,data){
            xsddh.combogrid("reload",'tcszzddxx!getDdxxByKhbh.json?khbh=' + data.value);
            ui.setFormData({
                KHBH:data.value,
                KHMC:data.text,
                XSDDH:""
            });
        });
    };

    //客户信息弹出框回调
    ui.addCallback("setComboGridValue_Khxx",function(o){
        if (null == o) return;
        var rowData = o.result;
        if (null == rowData) return;
        var xsddh = ui.getItemJQ("XSDDH");

        xsddh.combogrid("reload",'tcszzddxx!getDdxxByKhbh.json?khbh=' + o.result.KHBH);
        ui.setFormData({
            KHBH:rowData.KHBH,
            KHMC:rowData.KHMC,
            XSDDH:""
        });
    });

    //订单信息弹出框回调
    ui.addCallback("setComboGridValue_Ddxx",function(o){
        if (null == o) return;
        var rowData = o.result;
        if (null == rowData) return;
        ui.setFormData({
            XSDDH:rowData.DDBH
        });
    });

    //订单信息弹出框过滤
    ui.addOutputValue("setTcsDdxx",function(o){
        var khbh = ui.getItemValue("KHBH");
        var o = {
            status : true,
            P_columns : "EQ_C_KHBH≡"+khbh
        };
        return o;
    });

    //复写保存按钮，设置属性:主表记录id
    ui.clickSave = function(op) {

        var allData = {};
        //主表
        var jqForm = $("form", ui.uiForm),formData;
        if (!jqForm.form("valid")) return;
        formData = jqForm.form("formData", false);
        allData.ccgl = formData;
        //获取标签页configInfo
        var tabConfigInfo = ui.assembleData.parentConfigInfo.parentConfigInfo.bottomChildConfigInfo;
        var tabId = tabConfigInfo.tabId;        //标签页id
        var childrenConfigInfo = tabConfigInfo.childConfigInfos;		//tab页下的grid们
        //校验
        var errorTab = "";
        var tabName = "";
        for(var i in childrenConfigInfo){
            switch (parseInt(i)){
                case 1 :
                    errorTab = "散货出场";
                    tabName = "shxx";
                    break;
                case 0 :
                    errorTab = "产品出场";
                    tabName = "cpxx";
                    break;
                default :
                    break;
            }
            var jq = $("#" + childrenConfigInfo[i].cGridDivId);
            var bool = true;
            if (i == 0){
                $.each(jq.cgrid("getRowData"),function(e, data){
                    if(parseFloat(data.CCJS) > parseFloat(data.KCJS)){
                        bool = false;
                    }
                });
            }else if (i == 1){
                $.each(jq.cgrid("getRowData"),function(e, data){
                    if(parseFloat(data.CCZL) > parseFloat(data.KCZL)){
                        bool = false;
                    }
                });
            }
            if(!jq.cgrid("validGridData") || !bool){
                $('#'+tabId).tabs("option", "active", parseInt(i));
                CFG_message("请检查"+errorTab+"信息", "warning");
                return;
            }
            allData[tabName] = jq.cgrid("getRowData");
////           var cpxxdata= allData[tabName].CPXX;
//            for(var l=0;l<allData[tabName].length;l++){
//                allData[tabName][l].splice(8,1);
//            }
            if(bzid!=""){
                for(var i=0;i<allData[tabName].length;i++){allData[tabName][i].ID='';}

            }

        }
        $.ajax({
            url : $.contextPath + "/zzccglbd!saveCcxx.json",
            type : "POST",
            dataType: "json",
            async : false,
            data : {ccxx: JSON.stringify(allData)},
            success : function() {
                CFG_message("保存成功", "success");
                CFG_clickCloseButton(ui.assembleData.parentConfigInfo.parentConfigInfo);
                //CFG_clickReturnButton(ui.assembleData.parentConfigInfo.parentConfigInfo.parentConfigInfo, false);
            },
            error : function() {
                CFG_message("保存失败", "error");
            }
        });
    }

    ui.clickSecondDev = function(id){
        if(id ==$.config.preButtonId + "back"){
            CFG_clickCloseButton(ui.assembleData.parentConfigInfo.parentConfigInfo);
        }
    };



};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {

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
    ui.processItems = function (data,pos) {
        var isView = CFG_getSelfParamValue(ui.assembleData.parentConfigInfo, "ISVIEW");
        var btns = [];
        var poss = "";
        var op = ui.options.op.toString();
        if ($.config.contentType.isForm(ui.options.type)) {
            if (op == '0') {
                poss =" - 新增";
            } else if (op == '1') {
                poss =" - 修改";
            } else if (op == '2') {
                poss =" - 详情";
            }
            if(data.length == 0){
                poss =" - 详请";
            }
        }
        if(pos == 'top') {// 在顶部的时候添加当前位置
            btns.push({
                "type": "html",
                "content": "<div class='homeSpan'><div><div style='margin-left:20px'> - 出场管理" + poss + "</div>",
                frozen: true
            });
        }

        if(isView=="NO"){
            for (var i = 0; i < data.length; i++) {
                btns.push(data[i]);
            }
        }
        if(btns.length == 1){
            btns = [];
            btns.push({
                "type": "html",
                "content": "<div class='homeSpan'><div><div style='margin-left:20px'> - 出场管理 - 详情</div>",
                frozen: true
            });
            btns.push("->");
        }

        btns.push({
            id:$.config.preButtonId + "back",
            cls:"return_tb",
            label: "返回",
            type : "button"
        });
        return btns;
    };

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
