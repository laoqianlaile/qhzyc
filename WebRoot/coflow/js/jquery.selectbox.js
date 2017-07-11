(function($){
	function createBox(target){
		var state = $.data(target, 'selectbox');
		var opts = state.options;
		$(target).combo($.extend({}, opts, {
			onShowPanel:function(){
				$("#srcSelectTitle").html("可选参与者列表:");
				$("#objSelectTitle").html("已选参与者列表:");
				var srcOptions = new Array();
				var objOptions = new Array();
				var xmlDoc = opts.xmlDoc;
				if(xmlDoc != null){
					var ps = getParticipants(xmlDoc);
					if(ps != null){
						for(var i=0;i<ps.length;i++){
							srcOptions.push(ps[i].getAttribute("id"));
						}
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
					resetOptions(xmlDoc,null,srcOptions,objOptions,null,"performer");
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
	$.fn.selectbox = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.selectbox.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.combo(options, param);
			}
		}
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'selectbox');
			if (state){
				$.extend(state.options, options);
			} else {
				$.data(this, 'selectbox', {
					options: $.extend({}, $.fn.selectbox.defaults, options)
				});
			}
			createBox(this);
		});
	};
	
	$.fn.selectbox.methods = {
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
	$.fn.selectbox.defaults = $.extend({}, $.fn.combo.defaults, {
		panelWidth:400,
		panelHeight:200,
		xmlDoc:null,
		buttons:[{
			text: "确定",
			handler: function(target){
				setValue(target,checkSelectedOption("performer"));
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
