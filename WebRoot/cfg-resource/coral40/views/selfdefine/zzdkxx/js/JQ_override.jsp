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
			return $.contextPath + "/zzdkxx";
		};


		/**
		 * 二次开发：复写自定义表单
		 */
		function _override_form (ui) {

			ui._init = function(){
				var jdbh = ui.getItemJQ("JDBH");//基地编号
				/* jdbh.combogrid("reload",'zzdyxx!getJdxx.json'); */
				var qyxx = $.loadJson($.contextPath+'/trace!getQybmAndQymc.json?sysName=ZZ');
               //var dkbh = $.loadJson($.contextPath + "/zzdkzztj!getZzdkbh.json?qybh="+ui.getItemValue("QYBH"));
				ui.setFormData({QYBM:qyxx.qybm});
			    if(isEmpty(ui.options.dataId)){
                ui.setFormData({SYZT:"XG"});
                }
				var data1 = [{"BH":"11","MC":"22","PP":"33"},
					{"BH":"11","MC":"22","PP":"33"},
					{"BH":"11","MC":"22","PP":"33"}]
				var cgqzJQ = ui.getItemJQ("CGQZ");//传感器组
				var cgqzValue = cgqzJQ.combogrid("getValue");
//				return;
				cgqzJQ.combogrid("destroy");
				var _colNames = ['编号','设备识别号','品牌'];
				var _colModel = [{name:'BH'},{name:'MC',key:true},{name:'PP'}];
				cgqzJQ.combogrid({
					colNames : _colNames,
					colModel : _colModel,
					multiple : true,
					panelWidth: 300,
					sortable: true,
		//			data:data1,
					//调code里面的   url
					url : $.contextPath + "/zzdkxx!getCGQZ.json"
				});
//				cgqzJQ.combogrid("reload");
				setTimeout(function(){
					ui.setFormData({
						CGQZ:cgqzValue,
					})
				},100);
		        var getQyxx = $.loadJson($.contextPath +'/qyxxjdxx!getJDXX.json');
		        ui.setFormData({JDBH:getQyxx.JDBH,JDMC:getQyxx.JDMC});
			}

			ui.bindEvent = function (){
				var qybh = ui.getItemJQ("QYBH");
				var jdbh = ui.getItemJQ("JDBH");
				var dkfzr = ui.getItemJQ("DKFZRBH");
				var cgqz=ui.getItemJQ("CGQZ");
				qybh.combogrid("option","onChange",function ( e, data){
					var dkbh = $.loadJson($.contextPath + "/zzdkzztj!getZzdkbh.json?jdbh="+ui.getItemValue("JDBH"));
					ui.setFormData({
						QYBH:data.newValue,
						QYMC:data.newText,
						DKBH:dkbh
					});
				})
				dkfzr.combogrid("option","onChange",function(e,data){
					ui.setFormData({
						DKFZR:data.newText,
						DKFZRBH:data.newValue
					});
				});
//		chuangna
				cgqz.combogrid("option","onChange",function(e,data){//data就是在code里面定义的隐藏值value和显示值text.
					//data.value
					ui.setFormData({
						CGQZ:data.newText,
						CGQZ:data.newValue
					});
				});

			}

			ui.beforeSave = function(jq,options){
				var cgqzJQ = ui.getItemJQ("CGQZ");
				var idJQ = ui.getItemJQ("ID");
				var validCgqz = $.loadJson($.contextPath + "/zzdkxx!validCgqz.json?id=" + idJQ.textbox("getValue") + "&cgqz=" + cgqzJQ.combogrid("getValue"));
//				if(validCgqz > 0){
//					CFG_message("传感器组选择与其他地块重复，请重新选择！", "error");
//					return false;
//				}
				var dkmjJQ = ui.getItemJQ("MJ");//地块面积
				var qybh = ui.getItemJQ("QYBH").combogrid("getValue");
				var dkbh = ui.getItemJQ("DKBH").textbox("getValue");
				if(qybh.length == 0 || dkbh.length == 0){
					return false;
				}
				var mj = dkmjJQ.textbox("getValue");
				if(mj == 0){
					CFG_message("剩余面积不够！", "error");
					return false;
				}
				var jsonData = $.loadJson($.contextPath + "/trace!checkMj.json?mk=dk&bh="+qybh+"&mj="+mj+"&sbh="+dkbh);
				if(jsonData.result == "ERROR"){
					dkmjJQ.textbox("setValue","");
					CFG_message(jsonData.msg, "error");
					return false;
				}

				var id=ui.getItemValue("ID");

			}


			//弹出式基地信息过滤
			ui.addOutputValue("setTcsSsqy",function(o){
				var jdbh = ui.getItemValue("JDBH");
				var o = {
					status : true,
					P_columns : "EQ_C_JDBH≡"+jdbh
				}
				return o;
			});

			ui.addCallback("setComboGridValue_fzr",function(o){
				if (null == o) return;
				var rowData = o.rowData;
				if (null == rowData) return;
				ui.setFormData({
					DKFZR:rowData.XM,
					DKFZRBH:rowData.GZRYBH,
				});
			});
			ui.addCallback("getCombogridValue_cgqz",function(o){
				if (null == o) return;
				var rowData = o.rowData;
				if (null == rowData) return;
				var str = "";
				$.each(rowData,function(i){
					str += rowData[i].SBSBH + ",";
				});
				str = str.substring(0,str.length-1);
				ui.setFormData({
					CGQZ:str,
				});
			});
			ui.addCallback("getCombogridValue_qyxx",function ( o ) {
				if( null == o ) return;
				var obj = o.rowData;
				if( null == obj) return ;
				var dkbh = $.loadJson($.contextPath + "/zzdkzztj!getZzdkbh.json?qybh="+ui.getItemValue("QYBH"));
				ui.setFormData({
					QYBH:obj.QYBH,
					QYMC:obj.QYMC,
					DKBH:dkbh
				});
			})
			//弹出式基地信息回调
			ui.addCallback("setComboGridValue_jdxx",function(o){
				if (null == o) return;
				var rowData = o.result;
				if (null == rowData) return;
				ui.setFormData({
					JDBH:rowData.JDBH,
					JDMC:rowData.JDMC
				});
				var qybh = ui.getItemJQ("QYBH");
				qybh.combogrid("reload",'zzdyxx!getQyxx.json?jdbh='+rowData.JDBH);
				ui.setFormData({
					QYMC:"",
					QYBH:"",
					DkBH:""
				});
//        alert(ui.getItemValue("SSJD")+ui.getItemValue("JDBH"));
			})
		};
		/**
		 *  二次开发：复写自定义列表
		 */
		function _override_grid (ui) {
            //弹出式传感器组过滤
            ui.addOutputValue("setTcsCgqz",function(o){
                var qybm=ui.getItemValue("QYBM");
                var o = {
                    status:true,
                    P_columns : "EQ_C_QYBM="+qybm
                }
                return o;
            });

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
