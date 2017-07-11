<%@page language="java" pageEncoding="UTF-8" %>
<script type="text/javascript">
    /***************************************************!
     * @author qiucs
     * @date   2014-7-15
     * 系统配置平台应用自定义二次开发JS模板
     ***************************************************/


    (function (subffix) {

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
        function _override_form(ui) {
            var _zhid = "";
            ui._init = function () {
                var ssqy = ui.getItemJQ("QYBM");
                var ssdw = ui.getItemJQ("SSDW");
                //初始化所属企业下拉列表数据
                ssqy.combogrid("reload", 'tcszhxx!getZhxx.json?id=-1');
                //新增时初始化所属单位下拉列表为disabed
                if (isEmpty(ui.options.dataId)) {
                    ssdw.combogrid("disable");//新增disable单位下拉列表
                    //获取设备编号流水号

                } else {
                    var qyzhbh = ui.getItemJQ("QYBM").combogrid("getValue");
                    _zhid = $.loadJson($.contextPath + '/tcszhxx!getIdByZhbh.json?zhbh=' + qyzhbh).AUTH_ID;
                    var oldSsdwValue = ssdw.combogrid("getValue");
                    ssdw.combogrid("reload", 'tcszhxx!getZhxx.json?id=' + _zhid);
                    ssdw.combogrid("setValue", oldSsdwValue);
                }
                var jqLb = ui.getItemJQ("LB");
                jqLb.combobox("reload", $.contextPath + '/trace!getDataDictionary.json?lxbm=SBLB');

            }
 
            ui.bindEvent = function () {
                var ssqy = ui.getItemJQ("QYBM");
                var ssdw = ui.getItemJQ("SSDW");
                ssqy.combogrid("option", "onChange", function (e, data) {
                    _zhid = $.loadJson($.contextPath + '/tcszhxx!getIdByZhbh.json?zhbh=' + data.value).AUTH_ID;
                    ui.setFormData({
                        SSQY: data.text,
                        QYBM: data.value,
                        SSDW: ""
                    });
                    ssdw.combogrid("enable");
                    ssdw.combogrid("reload", 'tcszhxx!getZhxx.json?id=' + _zhid);
                    var sbbh = $.loadJson($.contextPath + '/tcszhxx!getSbbh.json?zhen_companyCode=' + data.value);
                    ui.setFormData({
                        BH: sbbh
                    });
                });
                ssdw.combogrid("option", "onChange", function (e, data) {
                    ui.setFormData({
                        SSDW: data.text,
                        DWBM: data.value
                    });
                });

            }

            //回调
            ui.addCallback("setComboGrid_Dw", function (o) {//单位
                if (null == o) return;
                var rowData = o.result;
                if (null == rowData) return;
                ui.setFormData({
                    SSDW: rowData.QYMC,
                    DWBM: rowData.ZHBH
                });
            });

            ui.addCallback("setComboGrid_Qy", function (o) {//企业
                if (null == o) return;
                var rowData = o.result;
                if (null == rowData) return;
                ui.setFormData({
                    SSQY: rowData.QYMC,
                    QYBM: rowData.ZHBH,
                    SSDW: ""
                });
                var sbbh = $.loadJson($.contextPath + '/tcszhxx!getSbbh.json?zhen_companyCode=' + rowData.ZHBH);
                ui.setFormData({
                    BH: sbbh
                });
                _zhid = $.loadJson($.contextPath + '/tcszhxx!getIdByZhbh.json?zhbh=' + rowData.ZHBH).AUTH_ID;
                var ssdw = ui.getItemJQ("SSDW");
                ssdw.combogrid("enable");
                ssdw.combogrid("reload", 'tcszhxx!getZhxx.json?id=' + _zhid);
            });

            //传参方法
            ui.addOutputValue("setTcsZhxx_Dw", function (o) {//单位
                var o = {
                    status: true,
                    P_columns: "EQ_C_AUTH_PARENT_ID≡" + _zhid
                }
                return o;
            });

            ui.addOutputValue("setTcsZhxx_Qy", function (o) {//企业
                var o = {
                    status: true,
                    P_columns: "EQ_C_AUTH_PARENT_ID≡-1"
                }
                return o;
            });

        };
        /**
         *  二次开发：复写自定义列表
         */
function _override_grid(ui) {
//导入
        ui._init = function(){
		console.log();
		var onChangeFunction = "if (window.FileReader) {var file = this.files; var f = file[file.length-1]; var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);if(ImgSuffix != 'xls'){CFG_message('导入文件格式不正确', 'error');return false;} var formData = new FormData(document.forms.namedItem('fileinfo'));$.ajax({type: 'POST',url: '" + $.contextPath + "/qyptsbgl!ImportXls.json',data: formData,contentType: false,processData: false,timestamp: false,async: false,success: function (data) {$('#imageUpload').val('');if(data.RESULT == 'SUCCESS'){CFG_message(data.MSG, 'success');}else if(data.RESULT == 'ERROR'){CFG_message(data.MSG, 'error');}$('#" + ui.uiGrid.attr("id") + "').grid('reload');},error: function () {$('#imageUpload').val('');CFG_message('操作失败！', 'error');}});}";
        var importDiv = "<div id=\"importDiv\" style=\"display:none\"><form id=\"importDiv\" name=\"fileinfo\" action=\"" + $.contextPath + "/qyptsbgl" + "\" enctype=\"multipart/form-data\" method=\"post\"><input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"imageUpload\" lable=\"预览\" accept=\"application/vnd.ms-excel\" name=\"imageUpload\" onchange=\"" + onChangeFunction + "\"/></form></div>";
        $(ui.options.global).append(importDiv);
        }
        ui.clickSecondDev = function(id){
        if (id == $.config.preButtonId + "import") {
            $("#imageUpload").click();
        }else if(id == $.config.preButtonId + "export"){
           window.location.href=$.contextPath + "/qyptsbgl!downLoad";
       }
      }
    }; 
        /**
         *  二次开发：复写自定义树
         */
        function _override_tree(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override tree!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
        };
        /**
         *  二次开发：复写自定义工具条
         */
        function _override_tbar(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override tbar!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
            
        if (ui.options.type == $.config.contentType.grid) {
		if(isEmpty(ui.options.dataId)){
            	ui.processItems = function (data) {
		var btns = [];
		for (var i =0; i <  data.length-3; i++) {
			btns.push(data[i]);
		}
		btns.push({
			id: $.config.preButtonId + "import",
			icon: "icon-print",
			label: "导入设备信息",
			type: "button"
		});
		btns.push({
			id: $.config.preButtonId + "export",
			icon: "icon-print",
			label: "导入模板下载",
			type: "button"
		}); 
		for (var i =3; i < data.length; i++) {
			btns.push(data[i]);
		}
		return btns;
		}
		}
	}; 
/* 	    
        if (ui.options.type == $.config.contentType.grid) {
		if(isEmpty(ui.options.dataId)){
			ui.processItems = function (data) {
				btns.push({
			id: $.config.preButtonId + "import",
			icon: "icon-print",
			label: "导入设备信息",
			type: "button"
		});
		btns.push({
			id: $.config.preButtonId + "export",
			icon: "icon-print",
			label: "导入模板下载",
			type: "button"
		}); 
				for (var i = 0; i < data.length; i++) {
					btns.push(data[i]);
				}
				return btns;
			};
		}
	} */
 };
        
        

        /**
         *  二次开发：复写自定义布局
         */
        function _override_layout(ui) {
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
