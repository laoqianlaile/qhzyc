$.extend($.fn.datagrid.defaults.editors, {
	participantSelect: {
		init: function(container, options){
			var width = ($(container).width()-30)+"px";
			var input = $("<input type='text' style='width:"+width+"'/>").appendTo(container);
			input.selectbox(options);
			return input;
		},
		destroy: function(target){
			$(target).remove();
		},
		getValue: function(target){
			return $(target).combo('getValue');
		},
		setValue: function(target, value){
			$(target).selectbox("setValue",value);
		},
		resize: function(target, width){
			$(target)._outerWidth(width);
		}
    }
});
$.extend($.fn.datagrid.defaults.editors, {
	actualParambox: {
		init: function(container, options){
			var width = ($(container).width()-30)+"px";
			var input = $("<input type='text'/>").appendTo(container);
			input.actualParam(options);
			return input;
		},
		destroy: function(target){
			$(target).remove();
		},
		getValue: function(target){
			return $(target).combo('getValue');
		},
		setValue: function(target, value){
			$(target).actualParam("setValue",value);
		},
		resize: function(target, width){
			$(target)._outerWidth(width);
		}
	}
});
$.extend($.fn.datagrid.defaults.editors, {
	participantbox: {
		init: function(container, options){
			var width = ($(container).width()-30)+"px";
			var input = $("<input type='text' style='width:"+width+"'/>").appendTo(container);
			input.participant(options);
			return input;
		},
		destroy: function(target){
			$(target).remove();
		},
		getValue: function(target){
			return $(target).combo('getValue');
		},
		setValue: function(target, value){
			$(target).participant("setValue",value);
		},
		resize: function(target, width){
			$(target)._outerWidth(width);
		}
	}
});


$.extend($.fn.datagrid.methods, {
	editCell: function(jq,param){
		return jq.each(function(){
			var opts = $(this).datagrid('options');
			var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
			for(var i=0; i<fields.length; i++){
				var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor1 = col.editor;
				if (fields[i] != param.field){
					col.editor = null;
				}
			}
			$(this).datagrid('beginEdit', param.index);
			$(this).datagrid('getEditors', param.index)[0].target.focus();
			for(var i=0; i<fields.length; i++){
				var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor = col.editor1;
			}
		});
	}
});