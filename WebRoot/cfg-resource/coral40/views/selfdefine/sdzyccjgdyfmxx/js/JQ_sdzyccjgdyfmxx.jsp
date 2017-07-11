<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-04-26 10:01:52
 * 系统配置平台自动生成的自定义构件二次开发JS模板
 * 详细的二次开发操作，请查看文档（二次开发手册/自定义构件.docx）
 * 注：请勿修改模板结构（若需要调整结构请联系系统配置平台）
 ***************************************************/
 
 
(function(subffix) {
	var _djcpsl = "";
	var _zhcpsl = "";
    var  resourceFolder = $.contextPath + "/cfg-resource/coral40/common";
    var hUI;

/**
 * 二次开发定位自己controller
 * 系统默认的controller: jQuery.contextPath + "/appmanage/show-module"
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return jQuery.contextPath + "/sdzyccjgdyfmxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
    ui._init= function (){
        //初始化图片删除区域
        hUI= ui;
        var jywjDiv = ui.getItemBorderJQ("bztp");
        jywjDiv.empty();
        jywjDiv.html(initTPdiv());
        if(!isEmpty(ui.options.dataId)){
            var url = ui.getAction() + "!show.json?P_tableId=" + ui.options.tableId +
                    "&P_componentVersionId=" + ui.options.componentVersionId +
                    "&P_menuId=" + ui.options.menuId +
                    "&P_workflowId=" + null2empty(ui.options.workflowId) +
                    "&P_processInstanceId=" + ui._getProccessInstanceId() +
                    "&id=" + ui.options.dataId + "&P_menuCode=" + ui.getParamValue("menuCode");
            var fromData = $.loadJson(url);
            if(ui.options.isView){
                $("#chooseImage").removeAttr("onClick");
            }
            if(!isEmpty(fromData.BZTP)){
                $.ns("ns"+subffix+"").uploadImage($.contextPath+"/spzstpfj/"+fromData.BZTP);
            }
        }
      //  var hgJsondata = $.loadJson( $.contextPath +"/sdzyccjgycjgxx!searchhgGridData.json");
      //  ui.getItemJQ("SCPCH").combogrid("reload",hgJsondata.data);
    }
	ui.bindEvent = function() {
		//var jgpch = ui.getItemJQ("QYPCH");
        var scpch = ui.getItemJQ("SCPCH");
        scpch.combogrid("option","onChange",function(e,data) {
			var rowData =scpch.combogrid("grid").grid("getRowData",data.value);
			ui.setFormData({SCPCH:rowData.JGPCH,YCMC:rowData.YLMC,BZZL:rowData.JGZZL,YCDM:rowData.YCDM,QYPCH:rowData.QYJGPCH});
		});
		var bzge = ui.getItemJQ("BZGG");
		var bzzl = ui.getItemJQ("BZZL");
		bzge.textbox("option","onChange",function(e,data){
			var bzgg =bzge.val();
			var bzzls =bzzl.val();
			/*if(bzgg < bzzls){
				debugger
				CFG_message("请保证包装规格小于包装数量","error");}
			else if(bzgg > bzzls){*/
			if(bzgg!=null&&bzzls!=null){
				var bzsl=parseInt(bzzls/bzgg);
				ui.setFormData({BZSL:bzsl})
			};

		});
		bzzl.textbox("option","onChange",function(e,data){
			var bzgg =bzge.val();
			var bzzls =bzzl.val();
			/*if(bzzls < bzgg){
				debugger
				CFG_message("请保证包装规格小于包装数量","error");}
			else if(bzzls > bzgg){*/
			if(bzgg!=null&&bzzls!=null){
				var bzsl=parseInt(bzzls/bzgg);
				ui.setFormData({BZSL:bzsl})
			}

		});

	}
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
		});
        if( parseFloat(formData.BZGG) > parseFloat(formData.BZZL)){
            CFG_message("包装重量不能小于包装规格","warning");
            return;
        }
		var dfile = document.forms[jqForm.attr('id')]["imageUpload"].files[0];
		if ( undefined != dfile && 'undefined' != dfile) {
            postData.append("imageUpload", dfile);
        }else{
            CFG_message("请上传包装图片","warning");
            return;
        }
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
var jqUC;
var total=0;
var next = 0;
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	ui.clickDelete = function (isLogicalDelete) {
		var idArr = ui.uiGrid.grid("option", "selarrrow");
		if (0 == idArr.length) {
			CFG_message( "请选择记录!", "warning");
			return;
		}
		for(var i in idArr) {
			var rowData = ui.uiGrid.grid("getRowData",idArr[i]);
			var url = $.contextPath + "/sdzyccjgycjgxx!queryPch.json?pch="+rowData.SCPCH;
			var ssqy = $.loadJson(url);
			if((ssqy.data[0].SFRK=='0')){
				ui.databaseDelete(idArr, isLogicalDelete);
			}else{
				$.alert("不能删除!该批次赋码已入库");
				return;
			}
		}
	}
	ui.beforeInitGrid = function(setting) {
		setting.fitStyle = "width";
		return setting;
	};

	ui.beforeInitGrid = function(setting) {
		setting.fitStyle = "width";
		return setting;
	};

	ui._init = function(){
		console.log();
		var onChangeFunction = "if (window.FileReader) {var file = this.files; var f = file[file.length-1]; var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);if(ImgSuffix != 'xls'){CFG_message('导入文件格式不正确', 'error');return false;} var formData = new FormData(document.forms.namedItem('fileinfo'));$.ajax({type: 'POST',url: '" + $.contextPath + "/zzdkzztj!ImportXls.json',data: formData,contentType: false,processData: false,timestamp: false,async: false,success: function (data) {$('#imageUpload').val('');if(data.RESULT == 'SUCCESS'){CFG_message(data.MSG, 'success');}else if(data.RESULT == 'ERROR'){CFG_message(data.MSG, 'error');}$('#" + ui.uiGrid.attr("id") + "').grid('reload');},error: function () {$('#imageUpload').val('');CFG_message('操作失败！', 'error');}});}";
		var importDiv = "<div id=\"importDiv\" style=\"display:none\"><form id=\"importDiv\" name=\"fileinfo\" action=\"" + $.contextPath + "/zzdkzztj" + "\" enctype=\"multipart/form-data\" method=\"post\"><input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"imageUpload\" lable=\"预览\" accept=\"application/vnd.ms-excel\" name=\"imageUpload\" onchange=\"" + onChangeFunction + "\"/></form></div>";
		$(ui.options.global).append(importDiv);
	}
	ui.beforeInitGrid = function(setting) {
		setting.fitStyle = "width";
		return setting;
	};

	ui.beforeInitGrid = function(setting) {
		setting.fitStyle = "width";
		return setting;
	};

	ui.clickSecondDev = function(id){
		if (id == $.config.preButtonId + "print") {
			if(true){
				if(ui.getSelectedRowId().length != 1){
					CFG_message("请选择一条记录", "warning");
					return;
				}
			}else{
				$.alert("请在程序环境中写卡");
				return;
			}
			var rowData = ui.uiGrid.grid("getRowData",ui.getSelectedRowId());
			var zsm = $.loadJson($.contextPath +"/sdzyccjgdyfmxx!demandBzpch.json?bzpch="+rowData.BZPCH);
			var bxq = "";
			for(var i in rowData.BZSJ.split("-")){
				if(i==0){
					bxq = parseInt(rowData.BZSJ.split("-")[i]) + 1 ;
				}else{
					bxq += "-" + rowData.BZSJ.split("-")[i];
				}
			}
			var printSc = true ;
			//showPrintDialog();

			$.alert("<span style='font-size:20px;color:red'> 正在打印请稍后...</span>");
			if(zsm.length%2==0) {
				for (var i = 0; i < zsm.length / 2; i++) {
					try {
						var result = _print.print("bzglPrint", {
							"CPMC": "产品名称 ：" + rowData.YCMC,
							"CPDJ": "产品单价 ：1000",
							"BZRQ": "包装日期 ：" + rowData.BZSJ,
							"ZL": "重量 ：",
							"CPZSM": "产品追溯码：" + zsm[i].ZSM,
							//"URL":"http://www.sdzyczs.com/zsm?"+zsm[i].ZSM,
							"URLL": "http://www.sdzyczs.com/zsm?" + zsm[i*2].ZSM,
							"URLR": "http://www.sdzyczs.com/zsm?" + zsm[i*2 + 1].ZSM
						});
						var resultData = JSON.parse(result);
						if (resultData.MSG != "SUCCESS") {
							$.alert("打印失败");
							jqUC.dialog("close");
							return;
						}
					} catch (e) {
					}
				}
			}else{
				if(zsm.length<2)
				{
					try {
						var result = _print.print("bzglPrint", {
							"CPMC": "产品名称 ：" + rowData.YCMC,
							"CPDJ": "产品单价 ：1000",
							"BZRQ": "包装日期 ：" + rowData.BZSJ,
							"ZL": "重量 ：",
							"CPZSM": "产品追溯码：" + zsm[0].ZSM,
							//"URL":"http://www.sdzyczs.com/zsm?"+zsm[i].ZSM,
							"URLL": "http://www.sdzyczs.com/zsm?" + zsm[0].ZSM
						});
						var resultData = JSON.parse(result);
						if (resultData.MSG != "SUCCESS") {
							$.alert("打印失败");
							jqUC.dialog("close");
							return;
						}
					} catch (e) {
					}
				}else{
					for (var i = 0; i < (zsm.length / 2)-1; i++) {
						try {
							var result = _print.print("bzglPrint", {
								"CPMC": "产品名称 ：" + rowData.YCMC,
								"CPDJ": "产品单价 ：1000",
								"BZRQ": "包装日期 ：" + rowData.BZSJ,
								"ZL": "重量 ：",
								"CPZSM": "产品追溯码：" + zsm[i].ZSM,
								//"URL":"http://www.sdzyczs.com/zsm?"+zsm[i].ZSM,
								"URLL": "http://www.sdzyczs.com/zsm?" + zsm[i*2].ZSM,
								"URLR": "http://www.sdzyczs.com/zsm?" + zsm[i*2 + 1].ZSM
							});
							var resultData = JSON.parse(result);
							if (resultData.MSG != "SUCCESS") {
								$.alert("打印失败");
								jqUC.dialog("close");
								return;
							}
						} catch (e) {
						}
					}
					try {
						var result = _print.print("bzglPrint", {
							"CPMC": "产品名称 ：" + rowData.YCMC,
							"CPDJ": "产品单价 ：1000",
							"BZRQ": "包装日期 ：" + rowData.BZSJ,
							"ZL": "重量 ：",
							"CPZSM": "产品追溯码：" + zsm[zsm.length-1].ZSM,
							//"URL":"http://www.sdzyczs.com/zsm?"+zsm[i].ZSM,
							"URLL": "http://www.sdzyczs.com/zsm?" + zsm[zsm.length-1].ZSM
						});
						var resultData = JSON.parse(result);
						if (resultData.MSG != "SUCCESS") {
							$.alert("打印失败");
							jqUC.dialog("close");
							return;
						}
					} catch (e) {
					}
				}
			}
		/**
				for(var i=0;i<zsm.length;i++){
				try{
					var result = _print.print("bzglPrint",{
						"CPMC":"产品名称 ："+rowData.YCMC,
						"CPDJ":"产品单价 ：1000",
						"BZRQ":"包装日期 ：" +rowData.BZSJ,
						"ZL"  :"重量 ：",
						"CPZSM":"产品追溯码："+zsm[i].ZSM,
						"URL":"http://zyzs.sdcom.gov.cn:8080/zsm?"+zsm[i].ZSM
					});
					var resultData = JSON.parse(result);
					if(resultData.MSG != "SUCCESS"){
						$.alert("打印失败");
						jqUC.dialog("close");
						return;
					}
				}catch(e){

				}

			}
			*/
		}else if( id == $.config.preButtonId +"download") {
			var rowData = ui.uiGrid.grid("getRowData",ui.getSelectedRowId());
			window.open($.contextPath +"/sdzyccjgdyfmxx!downLoad?bzpch="+rowData.BZPCH,"_self");
		}

	}


	function printZsm(){
		var rowData = ui.uiGrid.grid("getRowData",ui.getSelectedRowId());
		var isTrue = true;
		for(var i = 0;i< rowData.zsm;i++){
			var cpzsm = rowData.SCPCH;
			var data = {
				qymc: rowData.YCMC,
				cpmc: rowData.YCMC,
				cpdj: rowData.YCMC,
				bzrq: rowData.JYSJ,
				bxq: bxq,
				cpzsm: zsm,
				bzgg: rowData.YCMC
			};
			if(isNewSwt){
				var result=_print.print("bzglPrint",{
					"CPMC":"产品名称 ：" + rowData.YCMC,
					"CPDJ":"产品等级 ：" + rowData.YCMC,
					"BZRQ":"包装日期 ：" + rowData.JYSJ,
					"BXQ":"保鲜期 ：" + bxq,
					"ZL":"重量 ：" + rowData.YCMC,
					"CPZSM": cpzsm,
					"URL":"http://zyzs.sdcom.gov.cn:8080/zsm?" + cpzsm
				});

				if (result.MSG == "SUCCESS") {
					var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.CSPCH+"&bzxs=xbz&cpzsm="+cpzsm+"&id="+rowData.ID);
					if(savePrint != true ){
						$.alert("打印失败");
						return false;
					}
				} else {
					$.alert("打印失败");
					return false;
				}
			}
		}
	}


	/************************打印dialog bigen************************/
	function showPrintDialog(){
		var jqGlobal = $(ui.options.global);
		jqUC = $("<div id=\"jqUC\"></div>").appendTo("body");
		jqUC.dialog({
			appendTo: jqGlobal,
			modal: true,
			title: "打印提示",
			width: 400,
			height: 260,
			resizable: false,
			position: {
				at: "center top+300"
			},
			onClose: function () {
				jqUC.dialog("close");
				jqUC.remove();
				_djcpsl = "";
				_zhcpsl = "";
			},
			onCreate: function () {
				//jqDiv为新增内容，如果恢复之前功能 请删除
				var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\">" +
						"      <div class=\"app-inputdiv12\" style=\"float: center;padding-top: 8px\"  >" +
						"         " +
						"       </div>" +
						"   </div>"
				).appendTo(this);
			}
			});

	}

};

/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	ui.processItems = function (data) {
		var btns = [];
		for (var i = 0; i < data.length; i++) {
			btns.push(data[i]);
			if(i == 4){
				btns.push({
					id:$.config.preButtonId + "print",
					icon:"icon-print",
					label: "打印",
					type : "button"
				});
				btns.push({
					id:$.config.preButtonId + "download",
					//icon:"icon-print",
					label: "下载追溯码",
					type : "button"
				});
			}
//            if(i==5){
//                btns.push({
//                    id:$.config.preButtonId + "chuchang",
////                    icon:"chuchang",
//                    label: "出场",
//                    type : "button"
//                });
//            }
		}

		return btns;
	};
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
//jQuery.extend(jQuery.ns("ns" + subffix), {
//	name : "",
//	click: function() {}
//});
    jQuery.extend(jQuery.ns("ns" + subffix), {
        sumJgzzl : function(ui){
            dGrid = ui.getDetailGrid();
            var rowdData = dGrid.toFormData();
        },
        viewImage : function (fileInput) {
            var view = $("#view${idSuffix}");
//        view.empty();
            if (window.FileReader) {
                var p = $("#preview${idSuffix}");
                var file = fileInput.files;
                var f = file[file.length-1];

                var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);

                //图片不能超过2M，
                if(!(f.size<2097152 && (ImgSuffix=='jpg'||ImgSuffix=='jpeg'||ImgSuffix=='bmp'||ImgSuffix=='png'))){
                    CFG_message("图片必须小于2M，且格式必须为.png, .jpg 或 .bmp！", "error");
                    $("#imageUpload${idSuffix}").val("");
                    return false;
                }
                var fileReader = new FileReader();
                fileReader.onload = (function (file) {
                    return function (e) {
                        $.ns('ns'+subffix).uploadImage(this.result);
                        return;
                    }
                })(f);
                fileReader.readAsDataURL(f);
            }
        },
        uploadImage : function (tplj){
            var view = $("#view${idSuffix}");
            view.empty();
            var image = new Image();
            image.src = tplj;
            image.className = "thumb";
            image.onload = function (e) {

                var div = document.createElement('div');
                $(div).append(this);
//                $(div).append('<span style="display: none;position: absolute; margin-top: 0px; margin-left: 6px; width: 20px; height: 20px;"><img onclick = "$.ns(\'ns'+subffix+'\').deleteImage(\'' + this.src + '\')" style = "height:20px" src = "'+resourceFolder+'/css/images/trace-image/trash_bin.png"></span>');
                var originWidth = this.width;
                var originHeight = this.height;
                div.style.float = "left";
//            div.style.marginTop = "10px";
                div.style.marginRight = "5px";
                div.style.height = "100px";
                if(((originWidth * 100) / originHeight) > 750){
                    div.style.width = "750px";
                }else{
                    div.style.width = (originWidth * 100) / originHeight + "px";
                }
                image.style.height = div.style.height;
                image.style.width = div.style.width;
                this.style.position = "absolute";
                this.style.border = "1px solid #2AC79F",
                        $(div).hover(function () {
                            $(div).find("span").toggle();
                        });
                view.append(div);
                if(view.css("width") != "0px"){
                    $("#chooseImage${idSuffix}").css("margin-top","75px");
                }
            }
        },
        deleteImage : function (src) {
            var strArray = src.split("/");
            var tplj = strArray[strArray.length - 1];
            $.ajax({
                type: "POST",
                url: $.contextPath + '/sdzyccjgdyfmxx!deleteTp.json',
                data: {'id': hUI.options.dataId},
                dataType: "json",
                success: function (data) {
                    var view = $("#view${idSuffix}");
                    hUI.setFormData({"BZTP":""});
                    view.empty();
                    $("#imageUpload${idSuffix}").val("");
                    $("#chooseImage${idSuffix}").css("margin-top","0px");
                }
            })
        }
    });
    function  initTPdiv() {//
        var html = "<div style=\"margin-bottom: 50px;margin-left:55px\">"+
                "<div class=\"fillwidth colspan2 clearfix\">"+
                "<div class=\"app-inputdiv6\">"+
                "<label class=\"app-input-label\" style=\"width:20%;\">包装图片：</label>"+
                "<div id=\"file${idSuffix}\">"+
                "<input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"imageUpload${idSuffix}\" multiple=\"multiple\" lable=\"预览\"	accept=\"image/*\" name=\"imageUpload\" onchange=\"$.ns('ns"+subffix+"').viewImage(this)\"/>"+
                "</div>"+
                "<div style=\"margin-left: 10%\" id=\"view${idSuffix}\"></div>"+
                "<button style=\"margin-left: 0px\" id=\"chooseImage${idSuffix}\" class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component' type='button' onclick=\"$('#imageUpload${idSuffix}').click()\">"+
                "<span class=\"coral-button-text \">上传图片</span>"+
                "</button>"+
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
