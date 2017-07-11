(function($){
	function createBox(target){
		var state = $.data(target, 'participant');
		var opts = state.options;
		$(target).combo($.extend({}, opts, {
			editable:false,
			onShowPanel:function(){
				var dValue = getValue((target));
				//alert(dValue);
				var xmlDoc= opts.xmlDoc;
				var participantType = opts.participantType;
				if(xmlDoc != null){
					var as = getActivities(xmlDoc);
					//var datas = new Array();
					//for(var i=0;i<as.length;i++){
						//datas.push(as[i].getAttribute("id"));
					//}
					//initCreaterNode(datas);
					var dataMap = {};
					for(var i=0;i<as.length;i++){
						if(as[i].getAttribute("type").toUpperCase() == "START" || as[i].getAttribute("type").toUpperCase()=="FINISH"){
							continue;
						}
						dataMap[as[i].getAttribute("id")]=as[i].getAttribute("name");
					}
					initCreaterNodeMap(dataMap,"creator_node",dValue,participantType);
					initCreaterNodeMap(dataMap,"creator_node2",dValue,participantType);
				}
				var tab = $('#process_tabs').tabs('getSelected');
				if(tab){
					var id = $(tab).data("tabId");
					//var zTree = $.fn.zTree.getZTreeObj("projectView");
					//var node = zTree.getNodeByParam("id", id);
					//if(node != null){
						/*$.ajax({
							type: "POST",
							url: basePath+"/action/work-flow-manager!parseOrganization",
							data:"id="+id,
							success: function(data){
								try{
									var rdata = $.parseJSON(data);
									if(rdata != null){
										$.fn.zTree.init($("#orgView"), opts.ztreeSetting, rdata);
									}
								}catch(e){
									
								}
							}
						});*/
						/*$.ajax({
							type: "POST",
							url: basePath+"/action/work-flow-manager!parseUser",
							data:"id="+id,
							success: function(data){
								try{
									var rdata = $.parseJSON(data);
									if(rdata != null){
										$.fn.zTree.init($("#userView"), opts.ztreeSetting, rdata);
									}
								}catch(e){
									
								}
							}
						});*/
				//}
			 	}
				$.ajax({
					type: "POST",
					url: pwin.AppActionURI.workflowVersion + "!customFormula.json",
					success: function(data){
						try{
							$("#custom_gs_table").html("");
							var rdata = data.data;
							if(rdata != null){
								for(var i=0;i<rdata.length;i++){
									var uuid = Math.uuid();
									$("#custom_gs_table").append("<tr><td><input type='radio' name='gs_operator' id='"+uuid+"' value='"+rdata[i].value+"'/><label for='"+uuid+"'>"+rdata[i].name+"</label></td></tr>")
								}
							}
						}catch(e){}
						initParticipantDefaultSelect(dValue,participantType);
					}
				});
				opts.onShowPanel.call(target);
				initParticipantDefaultSelect(dValue,participantType);
			}
		}));
		createSelect();
		function createSelect(){
			var panel = $(target).combo('panel');
			$("#dailog_participant").appendTo(panel);
			$("#dailog_participant").css("display","block");
			var button = $('<div class="datebox-button"><table cellspacing="0" cellpadding="0" style="width:100%;padding-bottom:15px;"><tr></tr></table></div>').appendTo(panel);
			var tr = button.find('tr');
			var td1 = $('<td></td>').appendTo(tr);
			var td2 = $('<td></td>').appendTo(tr);
			for(var i=0; i<opts.buttons.length; i++){
				var span = $('<span style="padding-left:20px;"></span>').appendTo(td2);
				var btn = opts.buttons[i];
				var t = $('<a href="javascript:void(0)"></a>').html($.isFunction(btn.text) ? btn.text(target) : btn.text).appendTo(span);
				t.bind('click', {target: target, handler: btn.handler}, function(e){
					e.data.handler.call(this, e.data.target);
				});
			}
			tr.find('td').css('width', (100/opts.buttons.length)+'%');
		}
		
	}
	
	function setValue(target, value){
		$(target).combo('setValue', value).combo('setText', value);
	}
	
	function getValue(target){
		return $(target).combo('getValue');
	};
	$.fn.participant = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.participant.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.combo(options, param);
			}
		}
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'participant');
			if (state){
				$.extend(state.options, options);
			} else {
				$.data(this, 'participant', {
					options: $.extend({}, $.fn.participant.defaults, options)
				});
			}
			createBox(this);
		});
	};
	
	$.fn.participant.methods = {
			setValue: function(jq, value){
				return jq.each(function(){
					setValue(this, value);
				});
			},
			reset: function(jq){
				return jq.each(function(){
					var opts = $(this).selectbox('options');
					$(this).datebox('setValue', opts.originalValue);
				});
			}
		};
	var orgViewonClick = function(event, treeId, treeNode, clickFlag){
	};
	var ztreeSetting = {
			view:{
				showIcon:false,
				selectedMulti: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: orgViewonClick
			}
	};
	
	$.fn.participant.defaults = $.extend({}, $.fn.combo.defaults, {
		panelWidth:510,
		panelHeight:360,
		xmlDoc:null,
		participantType:null,
		ztreeSetting:ztreeSetting,
		buttons:[{
			text: "确定",
			handler: function(target){
				var partType,participant;
				var isError = false;
				$("input[name=gs_operator]").each(function(){
					if(this.checked){
						//开始
							if(this.id == "gs_department"){
								//指定部门
								var treeObj = $.fn.zTree.getZTreeObj("orgView");
								if(treeObj == null){
									isError = true;
									alert("没有选择数据");
								}else{
									var nodes = treeObj.getSelectedNodes();
									if(nodes && nodes.length>0){
										var node = nodes[0];
										partType = node.type;
										if(partType == "DEPARTMENT"){//部门
											var id = node.id;
											id = id.substring(0,id.lastIndexOf("_department"));
											currentParticipant = participant = id+":"+node.name;
										}else{
											isError = true;
											alert("请选择部门");
										}
									}else{
										isError = true;
										alert("没有选择数据");
									}
								}
							
							}else if(this.id == "gs_human"){
								//指定用户
								var treeObj = $.fn.zTree.getZTreeObj("userView");
								if(treeObj == null){
									isError = true;
									alert("没有选择数据");
								}else{
									var nodes = treeObj.getSelectedNodes();
									if(nodes && nodes.length>0){
										var node = nodes[0];
										partType = node.type;
										if(partType == "HUMAN"){//用户
											var id = node.id;
											currentParticipant = participant = id+":"+node.name;
										}else{
											isError = true;
											alert("请选择用户");
										}
									}else{
										isError = true;
										alert("没有选择数据");
									}
								}
							}else if(this.id == "gs_creater"){
								//创建者
								partType = "FORMULA";
								participant = "创建者";
								currentParticipant = "@creator";
							}else if(this.id == "gs_pre_operator"){
								//上一节点操作者
								partType = "FORMULA";
								participant =  "上一节点操作者";
								currentParticipant = "@_";
							}else if(this.id == "gs_same_node_operator"){
								//与节点相同的操作者
								partType = "FORMULA";
								participant = "@_"+$('#creator_node').find("option:selected").text();
								currentParticipant = "@_"+$('#creator_node').val();
							}else if(this.id == "gs_system"){
								partType = "SYSTEM";
								currentParticipant=participant = "";
							}else{
								//与节点相同的操作者
								partType = "FORMULA";
								participant = $(this).next("label").text();
								currentParticipant = $(this).val();
							}
						//结束
					}
				});
				if(!isError){
					setValue(target,participant);
					$('#basicProps').datagrid('updateRow',{
						index: 4,
						row: {
							value: transParticipantType2Chinese(partType)
						}
					});
					$('#basicProps').trigger("typeUpdate",partType);
					$(this).closest('div.combo-panel').panel('close');
				}
			}
		},{
			text: "取消",
			handler: function(target){
				$(this).closest('div.combo-panel').panel('close');
			}
		}]
	});
})(jQuery);
