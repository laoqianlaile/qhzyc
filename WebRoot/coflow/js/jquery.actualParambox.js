(function($){
	function createBox(target){
		var state = $.data(target, 'actualParam');
		var opts = state.options;
		$(target).combo($.extend({}, opts, {
			onShowPanel:function(){
				$("#srcSelectTitle").html("可选参数列表:");
				$("#objSelectTitle").html("已选参数列表:");
				var srcOptions = new Array();
				var objOptions = new Array();
				var formalParams = new Array();
				var application = opts.application;
				var workflowProcess = opts.workflowProcess;
				var subflow = opts.subflow;
				var xmlDoc= opts.xmlDoc;
				var flag = opts.flag;
				if(workflowProcess != null){
					var ps = getDataFields(workflowProcess);
					if(ps != null){
						/*for(var i=0;i<ps.length;i++){
							srcOptions.push(ps[i].getAttribute("id"));
						}*/
						for(var i=0;i<ps.length;i++){
							srcOptions.push(ps[i].getAttribute("id"));
						}
						var value = getValue(target);
						if(value != ""){
							var valArray = value.split(",");
							for(var i=0;i<valArray.length;i++){
								var key = valArray[i];
								if(ps!=null){
									for(var j=0;j<ps.length;j++){
										if(valArray[i]==ps[j].getAttribute("name")){
											key = ps[j].getAttribute("id");
											break;
										}
									}
								}
								objOptions.push(key);
							}
						}
						var pJsonArray = new Array();
						
						if(flag == "tool" && application != null){
							var params = selectNodes(xmlDoc,application,".//FormalParameter");
							if(params != null){
								for(var i=0;i<params.length;i++){
									var basicType = selectNode(xmlDoc,params[i],".//BasicType");
									//formalParams.push(params[i].getAttribute("id")+":"+transDataType2Chinese(basicType.getAttribute("type")));
									var pJson = {"index":"","p":""};
									pJson.index = params[i].getAttribute("index");
									pJson.p = params[i].getAttribute("id")+":"+transDataType2Chinese(basicType.getAttribute("type"));
									pJsonArray.push(pJson);
								}
							}
						}else if(flag == "subflow" && subflow != null){
							var params = selectNodes(xmlDoc,subflow,".//FormalParameter");
							if(params != null){
								for(var i=0;i<params.length;i++){
									var basicType = selectNode(xmlDoc,params[i],".//BasicType");
									//formalParams.push(params[i].getAttribute("id")+":"+transDataType2Chinese(basicType.getAttribute("type")));
									var pJson = {"index":"","p":""};
									pJson.index = params[i].getAttribute("index");
									pJson.p = params[i].getAttribute("id")+":"+transDataType2Chinese(basicType.getAttribute("type"));
									pJsonArray.push(pJson);
								}
							}
						}
						pJsonArray.sort(sortFormalParamByIndexAsc);
						$(pJsonArray).each(function(index,value){
							formalParams.push(value.p);
						});
						resetOptions(xmlDoc,workflowProcess,srcOptions,objOptions,formalParams,flag);
					}
					/*var value = getValue(target);
					if(value != ""){
						objOptions = value.split(",");
					}
					resetOptions(srcOptions,objOptions);*/
				}
				opts.onShowPanel.call(target);
			}
		}));
		createSelect();
		function createSelect(){
			var panel = $(target).combo('panel');
			$("#dlg_select").appendTo(panel);
			$("#dlg_select").css("display","block");
			var button = $('<div class="datebox-button"><table cellspacing="0" cellpadding="0" style="width:100%"><tr></tr></table></div>').appendTo(panel);
			var tr = button.find('tr');
			var td1 = $('<td></td>').appendTo(tr);
			var td2 = $('<td></td>').appendTo(tr);
			for(var i=0; i<opts.buttons.length; i++){
				var span = $('<span style="padding-left:20px"></span>').appendTo(td2);
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
	$.fn.actualParam = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.actualParam.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.combo(options, param);
			}
		}
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'actualParam');
			if (state){
				$.extend(state.options, options);
			} else {
				$.data(this, 'actualParam', {
					options: $.extend({}, $.fn.actualParam.defaults, options)
				});
			}
			createBox(this);
		});
	};
	
	$.fn.actualParam.methods = {
			setValue: function(jq, value){
				return jq.each(function(){
					setValue(this, value);
				});
			},
			reset: function(jq){
				return jq.each(function(){
					var opts = $(this).actualParam('options');
					$(this).actualParam('setValue', opts.originalValue);
				});
			}
		};
	$.fn.actualParam.defaults = $.extend({}, $.fn.combo.defaults, {
		panelWidth:600,
		panelHeight:200,
		workflowProcess:null,
		xmlDoc:null,
		application:null,
		subflow:null,
		flag:null,
		buttons:[{
			text: "确定",
			handler: function(target){
				setValue(target,checkSelectedOption("actualParam"));
				$(this).closest('div.combo-panel').panel('close');
			}
		},{
			text: "取消",
			handler: function(target){
				$(this).closest('div.combo-panel').panel('close');
			}
		}]
	});
})(jQuery);
