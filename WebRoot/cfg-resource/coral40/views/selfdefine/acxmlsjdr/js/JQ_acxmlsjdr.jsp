<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-10-12 10:01:54
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
	return jQuery.contextPath + "/acxmlsjdr";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
    ui._init = function (){
        //初始化图片删除区域
        hUI= ui;
        var jywjDiv = ui.getItemBorderJQ("wjsc");
        jywjDiv.empty();
        jywjDiv.html(initTPdiv());
    }
    ui.bindEvent = function (){
        ui.setItemOption("SPMC","onChange",function( event ,data){
            var rowData = ui.getItemJQ("SPMC").combogrid("grid").grid("getRowData",data.value);
            ui.setFormData({SPDM:rowData.ZSSPM,SPMC:rowData.YCMNAME});

        })
    }
    /*药材名称的回调函数*/
    ui.addCallback("setComboGridValue_ycmc",function(o){
        if(null == o){
            return;
        }
        var obj = o.rowData;
        if(null == obj) return;
        ui.setFormData({SPMC:obj.YCMNAME,SPDM:obj.ZSSPM});
    });


    ui.addOutputValue("setCombogridValue_ycmc", function (o){
        return {
            status : true ,
            P_columns: 'EQ_C_QYLX≡JJG'
        }
    });




    ui.clickSave = function( op ){

        var _this = ui, jqForm = $("form", this.uiForm),
                url = this.getAction() + "!save.json?",
                formData, selfGrid, postData;

        if (!jqForm.form("valid")) return;
        // 保存前回调方法
        if (_this.processBeforeSave(jqForm, op) === false) return;
        // 获取表单数据
        postData = new FormData(jqForm);
        formData = jqForm.form("formData", false);
        $.each(formData,function ( i, data) {
            postData.append(i,data);
        })
        var dfile = document.forms[jqForm.attr('id')]["imageUpload"].files[0];
        if ( undefined != dfile && 'undefined' != dfile)
            postData.append("uploadFile",dfile);
        $.ajax({
            type : "POST",
            url : url,
            data : postData,
            /**
             *必须false才会自动加上正确的Content-Type
             */
            contentType: false,
            /**
             * 必须false才会避开jQuery对 formdata 的默认处理
             * XMLHttpRequest会对 formdata 进行正确的处理
             */
            processData: false,
            timestamp: false,
            async: false,
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
                    jqForm.form("loadData", entity);
                    if (_this.options.model === $.config.contentType.form) {
                        selfGrid = _this.getSelfGrid();
                        if (selfGrid) {
                            selfGrid.reload();
                        }
                    }
                } else if ("close" === op) {
                    _this.clickBackToGrid();
                } else if ("create" === op) {
                    _this.clickCreate();
                } else {
                    _this.doViewRecord(op);
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
	//ui.beforeInitGrid = function (setting) {
	//	return setting;
	//};
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
jQuery.extend(jQuery.ns("ns" + subffix), {
    viewImage : function (fileInput) {
        var file = fileInput.files;
        var f = file[file.length-1];
        var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1).toLowerCase();
        //图片不能超过2M，20971520Bytes
        if(!(f.size<20971520 && (ImgSuffix=='xml'))){
            CFG_message("文件必须小于20M，且格式必须为.xml！", "error");
            $("#imageUpload${idSuffix}").val("");
            return false;
        }
    }
});
    function  initTPdiv() {
        var html = "<div style=\"margin-bottom: 50px;margin-left:55px\">"+
                "<div class=\"fillwidth colspan2 clearfix\">"+
                "<div class=\"app-inputdiv6\">"+
                "<label class=\"app-input-label\" style=\"width:20%;\">电子监管码：</label>"+
                "<div id=\"file${idSuffix}\">"+
                "<input class=\"inputfile\" type=\"file\" style=\"width:300px;\" id=\"imageUpload${idSuffix}\" multiple=\"multiple\" lable=\"预览\"	onchange=\"jQuery.ns('ns" + subffix+"').viewImage(this)\" name=\"uploadFile\" />"+
                "</div>"+
                "</div>"+
                "</div>"+
                "</div>";
        return html;
    }









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
