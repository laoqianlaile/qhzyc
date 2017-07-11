<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.*" %>
<% String qybm =SerialNumberUtil.getInstance().getCompanyCode();
%>
<script type="text/javascript">
	/***************************************************!
	 * @date   2016-04-14 17:22:04
	 * 系统配置平台自动生成的自定义构件二次开发JS模板
	 * 详细的二次开发操作，请查看文档（二次开发手册/自定义构件.docx）
	 * 注：请勿修改模板结构（若需要调整结构请联系系统配置平台）
	 ***************************************************/


	(function(subffix) {
		var ImgSuffix;
		var resourceFolder = $.contextPath + "/cfg-resource/coral40/common";
		var hUI;
		/**
		 * 二次开发定位自己controller
		 * 系统默认的controller: jQuery.contextPath + "/appmanage/show-module"
		 * @returns {String}
		 **/
		window[CFG_actionName(subffix)] = function (ui) {
			// ui.assembleData 就是 configInfo
			return jQuery.contextPath + "/sdzyccjgylrkxx";
		};


		/**
		 * 二次开发：复写自定义表单
		 */
		function _override_form (ui) {
			ui._init = function(){
				hUI = ui;
				//初始化图片删除区域
				var cdzmtpDiv = ui.getItemBorderJQ("cdzmtp");
				cdzmtpDiv.empty();

				//检测信息链接图片div
				var jcxxDiv = ui.getItemBorderJQ("jcxxlj");
				jcxxDiv.empty();
				jcxxDiv.html(initJCdiv());
                var url = ui.getAction() + "!show.json?P_tableId=" + ui.options.tableId +
                        "&P_componentVersionId=" + ui.options.componentVersionId +
                        "&P_menuId=" + ui.options.menuId +
                        "&P_workflowId=" + null2empty(ui.options.workflowId) +
                        "&P_processInstanceId=" + ui._getProccessInstanceId() +
                        "&id=" + ui.options.dataId + "&P_menuCode=" + ui.getParamValue("menuCode");
                var formData = $.loadJson(url);
				if(ui.options.isView){
					$("#chooseImage").removeAttr("onClick");
				}
				if(!isEmpty(formData.JCXXLJ))
					$.ns("ns"+subffix+"").uploadImage($.contextPath+"/spzstpfj/"+formData.JCXXLJ);

				if(isEmpty(ui.options.dataId)){
					var qybm = ui.getItemJQ("QYBM");
					var qybm ="<%=qybm%>"//这个在上面定义了，是获取企业代码的
					//获取企业名称
					var jsondata = $.loadJson($.contextPath +"/sdzyccjgylrkxx!searchQybm.json?qybm="+qybm+"&dwlx=ZZQY");
					var qymc=jsondata.data[0].QYMC;
					ui.setFormData({QYBM:qybm,GYS:qymc});
				}
				if(!isEmpty(ui.options.dataId)) {//修改或详细出事pch下拉框为文本框
					ui.getItemJQ("YLMC").combogrid("destroy");
					ui.getItemJQ("YLMC").textbox();
					ui.getItemJQ("YLMC").textbox("setValue",formData.YLMC);
                  //  $("#PCH_"+ui.timestamp+"_DIV span").hide();
                   // $("#PCH_"+ui.timestamp+"_DIV").append("<input name='QYPCH' id='QYPCH'/>");
                   /* $("#QYPCH").textbox();
                    $("#QYPCH").textbox("setValue",formData.QYPCH);
                    $("#QYPCH_"+ui.timestamp).val("");

					if(ui.options.isView){
						ui.getItemBorderJQ("CDZM").remove();
                        $("#QYPCH").attr("readonly");
					}*/
					ui.getItemJQ("QYPCH").combogrid("destroy");
					ui.getItemJQ("QYPCH").textbox();
					ui.getItemJQ("QYPCH").textbox("option","readonly",true);
					ui.setFormData({QYPCH:formData.QYPCH});
					var jsondata = $.loadJson($.contextPath +"/sdzyccjgcjxx!searchcdzmxxData.json?sctpmc="+formData.CDZM);
					if(!isEmpty(jsondata.data[0]))
					    cdzmtpDiv.html(initTPdiv(jsondata.data[0].TPBCMC));
                }
				ui.getItemJQ("RKDJFZR").combogrid("reload",$.contextPath+"/zzgzryda!getGzrydaGrid.json?dwlx=CJGQY");
			}
			ui.bindEvent = function(){
				var pch = ui.getItemJQ("QYPCH");
				//var pch = ui.getItemJQ("PCH");
				pch.combogrid("option","onChange",function(e,data) {
					var rowData =pch.combogrid("grid").grid("getRowData",data.value);
					ui.setFormData({PCH:rowData.CSPCH,YLMC:rowData.YCMC,YLCD:rowData.SSDQ,RKZL:rowData.CSZL,YCDM:rowData.YCDM,QYPCH:rowData.QYCSPCH});
				});

				//入库重量数值检验
				ui.getItemJQ("RKZL").textbox("option","onChange",function(e,data){
					var pch = ui.getItemValue("PCH");
					var jsonData =  $.loadJson($.contextPath +"/sdzyccjgylrkxx!searchKcByPch.json?pch="+pch);
					var rkzl = data.value;
					if(rkzl>jsonData.data[0].CSZL){
						CFG_message("所填重量超出库存"+jsonData.data[0].CSZL+"，请重新输入", "warning");
						ui.getItemJQ("RKZL").val("");//入库重量数据清空
					}
				});
				ui.getItemJQ("GYS").combogrid("option", "onChange", function (e, data){

                    var gys_value =  ui.getItemJQ("GYS").combogrid("grid").grid("getRowData", data.value);
					var gys_data = $.loadJson($.contextPath +"/sdzycgysxx!searchYlgysxxByGysbh.json?gysbh="+gys_value.GYSBH);
					ui.getItemJQ("CDZM").combogrid("reload", gys_data);
					var cdzm = ui.getItemJQ("CDZM");
					cdzm.combogrid("option", "onChange", function (e, data) {
						var rowData = cdzm.combogrid("grid").grid("getRowData", data.value);
						var tpmc = rowData.TPBCMC;
						ui.getItemBorderJQ("cdzmtp").html(initTPdiv(tpmc));
					});
				});


			}


			ui.addCallback("setComboGridValue_rkbh",function(o){
				if(null == o) return;
				var obj = o.rowData;
				if(null == obj) return;
				ui.setFormData({RKBH:obj.CKMC,SLCK:obj.CKMC});
			});
			ui.addCallback("setComboGridValue_gys",function(o){
				if( null == o) return;
				var rowData = o.rowData;
				if( null == rowData) return ;
				ui.setFormData({GYS:rowData.GYSMC});
			});

			ui.addCallback("setComboGridValue_rkdjfzr",function(o){
				if( null == o) return;
				var rowData = o.rowData;
				if( null == rowData) return ;
				ui.setFormData({RKDJFZR:rowData.XM,DH:rowData.LXFS});
			})

			ui.addOutputValue("setCombogridValue_fzr", function (o){

				return {
					status : true ,
					P_columns: 'EQ_C_DWLX≡CJGQY'
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
//				var base64 = $(".thumb").attr("src");
//				base64 = base64.replace("data:image/jpeg;base64,",'');
				var newsrc = $(".thumb").attr("src");
//				var newsrc = $("#data_list").attr("src");
                if(!isEmpty(newsrc) ){
                    var subsrc = newsrc.substring(0,newsrc.indexOf(",")+1);
                    debugger
                    var base64 = newsrc.replace(subsrc,'');
                    formData.JCXXLJ=base64;
                    formData.IMGSUFFIX=ImgSuffix;
                }

				$.each(formData,function ( i, data) {
					postData.append(i,data);
				})

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



			// ui.assembleData 就是 configInfo
			//ui.bindEvent = function () {
			//	  ui.setItemOption("NAME", "onChange", function(e, data) {});// 添加onChange事件
			//    ui.callItemMethod("USER_ID", "disable");// 禁用USER_ID
			//};
		};

		/**
		 *  二次开发：复写自定义列表
		 */
		function _override_grid (ui) {
			ui._init = function(){
				console.log();
				var onChangeFunction = "if (window.FileReader) {var file = this.files; var f = file[file.length-1]; var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);if(ImgSuffix != 'xls'){CFG_message('导入文件格式不正确', 'error');return false;} var formData = new FormData(document.forms.namedItem('fileinfo'));$.ajax({type: 'POST',url: '" + $.contextPath + "/sdzyccjgylrkxx!ImportXls.json',data: formData,contentType: false,processData: false,timestamp: false,async: false,success: function (data) {$('#imageUpload').val('');if(data.RESULT == 'SUCCESS'){CFG_message(data.MSG, 'success');}else if(data.RESULT == 'ERROR'){CFG_message(data.MSG, 'error');}$('#" + ui.uiGrid.attr("id") + "').grid('reload');},error: function () {$('#imageUpload').val('');CFG_message('操作失败！', 'error');}});}";
				var importDiv = "<div id=\"importDiv\" style=\"display:none\"><form id=\"importDiv\" name=\"fileinfo\" action=\"" + $.contextPath + "/sdzyccjgylrkxx" + "\" enctype=\"multipart	/form-data\" method=\"post\"><input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"imageUpload\" lable=\"预览\" accept=\"application/vnd.ms-excel\" name=\"imageUpload\" onchange=\"" + onChangeFunction + "\"/></form></div>";
				$(ui.options.global).append(importDiv);
			}
			ui.beforeInitGrid = function(setting) {
				setting.fitStyle = "width";
				return setting;
			};

			ui.clickSecondDev = function(id){
				if (id == $.config.preButtonId + "import") {
					$("#imageUpload").click();
				}else if(id == $.config.preButtonId + "export"){
					window.location.href=$.contextPath + "/sdzyccjgylrkxx!downLoad";
					/*}else if(id == $.config.preButtonId + "print"){*/
					/*if(isSwt || isNewSwt){
					 if(ui.getSelectedRowId().length != 1){
					 CFG_message("请选择一条记录", "warning");
					 return;
					 }
					 /!*}else{
					 $.alert("请在程序环境中写卡");
					 return;
					 }*!/

					 var rowData = ui.uiGrid.grid("getRowData",ui.getSelectedRowId());
					 if(isSwt){
					 var data = {
					 QYBH:rowData.QYBH,
					 DKBH:rowData.DKBH
					 }
					 var result = _window("printSczzDkbq", JSON.stringify(data));
					 }else if(isNewSwt){
					 var result = _print.print("dkbqPrint",{"QYBHDKBH":rowData.QYBH + "/" + rowData.DKBH,"DKBH":rowData.DKBH});
					 if(result.MSG = "SUCCESS")
					 $.alert("打印成功");
					 //				_print.print("template",{"variable":{"QYBH":rowData.QYBH,"DKBH":rowData.DKBH},"url":"http://www.zhuisuyun.net/01234567890123456789","barcode":"12345678900123456789"})
					 }
					 }*/
				}
			};
			ui.clickDelete = function (isLogicalDelete) {
				var idArr = ui.uiGrid.grid("option", "selarrrow");
				if (0 == idArr.length) {
					CFG_message( "请选择记录!", "warning");
					return;
				}
				for(var i in idArr) {
					var rowData = ui.uiGrid.grid("getRowData",idArr[i]);
					var url = $.contextPath + "/sdzyccjgylllxx!queryPch.json?pch="+rowData.PCH;
					var ssqy = $.loadJson(url);
					if(isEmpty(ssqy.data[0])){
						ui.databaseDelete(idArr, isLogicalDelete);
					}else{
						$.alert("不能删除!已生产领料");
						return;
					}
				}
			}
			// ui.assembleData 就是 configInfo
			//ui.beforeInitGrid = function (setting) {
			//	return setting;
			//};
		};

		/**
		 *  二次开发：复写自定义工具条
		 */
		function _override_tbar (ui) {
			ui.processItems = function (data) {
				var btns = [];
				for (var i = 0; i < data.length; i++) {
					btns.push(data[i]);
				}
				if(ui.options.type  == 1){  //grid(type =1 ) form(type =2 )
					btns.push({
						id: $.config.preButtonId + "import",
						icon: "icon-print",
						label: "导入原料信息",
						type: "button"
					});
					btns.push({
						id: $.config.preButtonId + "export",
						icon: "icon-print",
						label: "模板下载",
						type: "button"
					});
				}
				return btns;
			};
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

		function initTPdiv(tpmc) {
			//var rowData = ui.uiGrid.grid("getRowData",ui.getSelectedRowId());
			//var path = "spzstpfj/thumb/1476780291968.jpeg";//+tpbcmc;
			var path = "spzstpfj/thumb/"+tpmc;
			var html = "<div style=\"margin-bottom: 50px;margin-left:55px;display:none\">" +
					"<div class=\"fillwidth colspan2 clearfix\">" +
					"<div class=\"app-inputdiv6\">" +
					"<label class=\"app-input-label\" style=\"width:55%;\">产地证明：</label>" +
					"<div id=\"file${idSuffix}\">" +
					"<input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"imageUpload${idSuffix}\" multiple=\"multiple\" accept=\"image/*\" name=\"imageUpload\" onchange=\"$.ns('ns" + subffix + "').viewImage(this)\"/>" +
					"</div>" +
					"<div style=\"margin-left: 10%\" id=\"view${idSuffix}\"></div>" +
					"<img id=\"data_list\" src='"+path+"'>" +
					"</div>" +
					"</div>" +
					"</div>";
			return html;
		}


		//检测信息链接
		function initJCdiv(){
			var html = "<div style=\"margin-bottom: 50px;margin-left:55px\">"+
					"<div class=\"fillwidth colspan2 clearfix\">"+
					"<div class=\"app-inputdiv6\">"+
					"<label class=\"app-input-label\" style=\"width:19.4%;\">检测信息：</label>"+
					"<div id=\"jctp${idSuffix}\">"+
					"<input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"jctpUpload${idSuffix}\"  lable=\"预览\"	accept=\"image/*\" name=\"imageUpload\" onchange=\"$.ns('ns"+subffix+"').viewJcImage(this)\"/>"+
					"</div>"+
					"<div style=\"margin-left: 10%\" id=\"viewJc${idSuffix}\"></div>"+
					"<button style=\"margin-left: 0px\" id=\"chooseImage${idSuffix}\" class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component' type='button' onclick=\"$('#jctpUpload${idSuffix}').click()\">"+
					"<span class=\"coral-button-text \">上传图片</span>"+
					"</button>"+
					"</div>"+
					"</div>"+
					"</div>";
			return html;
		}


		jQuery.extend(jQuery.ns("ns" + subffix), {
			viewJcImage : function (fileInput) {
				var view = $("#viewJc${idSuffix}");
				if (window.FileReader) {
					var p = $("#preview${idSuffix}");
					var file1 = fileInput.files;
					var f1 = file1[file1.length-1];

					ImgSuffix = f1.name.substring(f1.name.lastIndexOf('.')+1);

					//图片不能超过2M，
					if(!(f1.size<2097152 && (ImgSuffix=='jpg'||ImgSuffix=='bmp'||ImgSuffix=='png'))){
						CFG_message("图片必须小于2M，且格式必须为.png, .jpg 或 .bmp！", "error");
						$("#jctpUpload${idSuffix}").val("");
						return false;
					}
					var fileReader = new FileReader();
					fileReader.onload = (function (file1) {
						return function (e) {
							$.ns('ns'+subffix).uploadImage(this.result);
							return;
						}
					})(f1);
					fileReader.readAsDataURL(f1);
				}
			},
			uploadImage : function (tplj){
				var view = $("#viewJc${idSuffix}");
				view.empty();
				var image = new Image();
				image.src = tplj;
				image.className = "thumb";
				image.onload = function (e) {

					var div = document.createElement('div');
					$(div).append(this);
//					$(div).append('<span style="display: none;position: absolute; margin-top: 0px; margin-left: 6px; width: 20px; height: 20px;"><img onclick = "$.ns(\'ns'+subffix+'\').deleteImage(\'' + this.src + '\')" style = "height:20px" src = "'+resourceFolder+'/css/images/trace-image/trash_bin.png"></span>');
					var originWidth = this.width;
					var originHeight = this.height;
					div.style.float = "left";
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
				if(hUI.options.isView){
					return
				}
				$.ajax({
					type: "POST",
					url: $.contextPath + '/sdzyccjgylrkxx!deleteTp.json',
					data: {'id': hUI.options.dataId},
					dataType: "json",
					success: function (data) {
						var view = $("#viewJc${idSuffix}");
						hUI.setFormData({"JCXXLJ":""});
						view.empty();
						$("#jctpUpload${idSuffix}").val("");
						$("#chooseImage${idSuffix}").css("margin-top","0px");
					}
				})
			}
		});



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
     