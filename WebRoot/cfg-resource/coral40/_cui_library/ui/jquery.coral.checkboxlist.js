/*!
 * 组件库4.0：复选框组
 * 
 * 依赖JS文件：
 *    jquery.coral.core.js
 *    jquery.coral.component.js
 *    jquery.validatehelper.js
 */

(function ($) {
"use strict";
	
$.component("coral.checkboxlist", {
	version: "4.0.1",
	castProperties : ["data", "triggers"],
	options: {
		id:null,
		name:null,
		valueField:"value",
		textField :"text",
		//width : "auto",
		//height : 22,
		required: false,
		showStar: true,
		maxLabelWidth : "auto",
		labelField: null, // 表单元素前面的文本
		starBefore: false, // 必输项 * 是否前面
		column: null, // 每行放几个复选框
		disabled: false,
		readonly:false,
		value:null,
		data:null, // 数组形式: [{value:,text:},...]或者 字符串形式: "cn:中国;us:美国;..."
		url: null,
		termSplit : ";", // 如果data是字符串，则termSplit作为复选框组的分隔符
		itemSplit : ":", // 如果data是字符串，则每个复选框隐藏值与显示值的分隔符
		errMsg: null,
		errMsgPosition: "leftBottom",
		repeatLayout: "table", // "table", "flow" // flow 时自适应
		itemWidth: "auto", // repeatLayout 为 flow 时，radio item 控制宽度，用于对齐
		
		onValidError: null,
		onKeyDown: null,
		onValidSuccess: null,
		triggers: null, // 覆盖 validate 里的 triggers
		excluded: false, // true 则不单独校验
		onChange: $.noop /*参数(event, {value:string,checked:boolean})*/
	},
    _create: function () {
    	var that = this,
    	    textbox = null, 
    	    valuebox = null,
    	    uiArrow = null,
    	    options = this.options;
    	
    	if (!this.element.jquery) {
    		this.element = $(this.element);
    	}
    	
    	this.element.addClass("coral-form-element-checkboxlist  ctrl-init ctrl-form-element ctrl-init-checkboxlist coral-validation-checkboxlist");

    	typeof that.element.attr("id") == "undefined" && !!that.options.id&&that.element.attr( "id", that.options.id );
    	that.options.id = that.element.uniqueId().attr("id");    	
    	var name = that.element.attr("name");
    	typeof name != "undefined" ? (that.options.name = name) : (that.element.attr("name", that.options.name));
    	
		this.uiBoxlist = $("<span class=\"coral-checkboxlist\"></span>");
		this.uiInput   = $("<input type=\"hidden\">");
		if (this.options.name) {
			this.uiInput.attr("name", this.options.name);
		}
		if (this.options.value) {
			this.uiInput.val(this.options.value);
		}
		
		this._proccessData();		
		
		if (this.options.repeatLayout == "table") {
			if (this.options.column == null ) {
				this.options.column = 3;
			} 
			this._createTable();			
			this.uiTable.appendTo(this.uiBoxlist);
		} else if (this.options.repeatLayout == "flow") {
			if (this.options.column == null ) {
				this.options.column = this.data.length;
			} 
			this._createBorder();
			this.uiBorder.appendTo(this.uiBoxlist);
		}		
		this.uiInput.appendTo(this.uiBoxlist);
		// add label and required star before function @lhb @2015-04-27 add labelField attribute
		if (options.labelField) {
			this.uiLabel = $("<label class=\"coral-label\">"+ options.labelField +"</label>");
			this.uiBoxlist.prepend(this.uiLabel);
			this.uiBoxlist.addClass("coral-hasLabel");
		}
		// add label and required star before function @lhb @2015-04-27
		if (this.options.value) {
			this.setValue(this.options.value, true);
		}		
		this.uiBoxlist.insertAfter(this.element);		
		this.element.hide();		
		this._bindEvent();
	},
	reload: function( url ){
		
	},
	/**
	 * 获取生成筛选框的数据
	 * @returns
	 */
	_proccessData : function () {
		var data = null, tmpArr = null,
		    i = 0, option = null, 
		    tmpRow = null, rowArr = null;
		if (this.options.url) {
			data = $.loadJson(this.options.url);
		}
		data = data || this.options.data;
		if (typeof data === "string") {
			tmpArr = data.split(this.options.termSplit);
			data = [];
			for (; i < tmpArr.length; i++) {
				tmpRow = tmpArr[i];
				rowArr = tmpRow.split(this.options.itemSplit);
				option = {};
				option[this.options.valueField] = rowArr[0];
				option[this.options.textField]  = rowArr[1];
				data.push(option);
			}
		}
		this.data = data;
	},
	_createBorder: function () {
		var that = this,
			opts = this.options,
			column = this.options.column,
			data = this.data;
		
		that.uiBorder = $("<span class=\"coral-checkboxlist-border\"></span>");
		
		for (var i in data) {
			if ( i > (column-1) && i%(column) == 0) {
				$("<br/>").appendTo(that.uiBorder);
			}
			that._createItem(data[i]).css("width", opts.itemWidth).appendTo(that.uiBorder);
		}
	},
	_createTable: function () {
		this.uiTable = $("<table></table>");
		
		var i = 0, j = 0, 
		    data = this.data, 
		    len  = data.length || 0, 
		    column = this.options.column,
		    rows = 0, uiTr = null, uiTd = null;
		
		if (!data || data.length < 1) return;
		
		rows = Math.ceil(len/column);
		
		for (; i < rows; i++) {
			uiTr = $("<tr></tr>");
			for (j = 0; j < column ; j++) {
				uiTd = $("<td></td>");
				if ((i*column + j) < len) {
					this._createItem(data[(i*column + j)]).appendTo(uiTd);
				}
				uiTd.appendTo(uiTr);
			}
			uiTr.appendTo(this.uiTable);
		}
	},
	_createItem : function (cellData) {
		var isHidden = cellData.hidden == true?"hidden":"";
		var uiCheckbox = $("<span class='coral-checkbox "+isHidden+"'></span>"),
		    uiLabel    = $("<span class='coral-checkbox-label'></span>"),
		    uiIcon = $("<span class='coral-checkbox-icon'></span>"),
		    maxLabelWidth = this.options.maxLabelWidth,
		    uiText = $(),
		    value      = cellData[this.options.valueField],
		    text       = cellData[this.options.textField];
		
		//uiCheckbox.val( value );	
		if ( maxLabelWidth == "auto" ){
			uiText = $("<span class=\"coral-checkbox-text\"></span>");
		}else{
			uiText = $("<span class=\'coral-checkbox-text\'  title=\'"+text+"\' style=\'max-width:"+maxLabelWidth+"px;\'></span>");
		}
		uiCheckbox.attr( "data-value", value );	
		
		uiLabel.append(uiIcon).append(uiText);
		uiLabel.appendTo(uiCheckbox);
		uiIcon.addClass("icon icon-checkbox-unchecked");
		uiText.append(text);
		
		return uiCheckbox;
	}, 
	_bindEvent: function() {
		var that = this;
		
		if ( this.options.disabled ) {
			this._setDisabled(this.options.disabled);
		}		
		this.uiBoxlist.find(".coral-checkbox").each(function() {
			$(this).bind("mouseenter" + that.eventNamespace, function() {
				if ($(this).hasClass("coral-state-disabled")) {
					return;
				}
				$(this).addClass("coral-checkbox-hover");
			}).bind("mouseleave" + that.eventNamespace, function() {
				if ($(this).hasClass("coral-state-disabled")) {
					return;
				}
				$(this).removeClass("coral-checkbox-hover");
			}).bind("click" + that.eventNamespace, function( event ) {
				if (that.options.disabled) {
					return;
				}
				
				var uiCheckbox = $(this),
					  uiIcon = uiCheckbox.find(".coral-checkbox-icon");
				
				if (uiCheckbox.hasClass("coral-state-disabled")) {
					event.stopPropagation();
					return;
				}
				if (uiIcon.hasClass("icon-checkbox-checked")) {
					uiIcon.removeClass("icon-checkbox-checked").addClass("icon-checkbox-unchecked");
				} else {
					uiIcon.removeClass("icon-checkbox-unchecked").addClass("icon-checkbox-checked");
				}
				var oldValue = that.getValue();
				that._changeValue();
				that._trigger("onChange", null, {value: that.uiInput.val(),oldValue:oldValue, checked: uiIcon.hasClass("icon-checkbox-checked")});
				event.stopPropagation();
			}).bind( "keydown" + this.eventNamespace, function(e) {
				that._trigger("onKeyDown", e, {});
			});
		});
		this.uiBoxlist.find(".coral-checkbox-label").each(function() {
			$(this).bind("click" + that.eventNamespace, function( event ) {
				if (that.options.readonly) {
					return false;
				}				
			})
		});	
	},
	_changeValue: function() {
		var that = this, valArr = [];

		this.uiBoxlist.find(".coral-checkbox").each(function() {
			if ($(this).find(".coral-checkbox-icon").hasClass("icon-checkbox-checked")) {				
				valArr.push( $(this).attr("data-value") );
			}			
		});
		
		this.uiInput.val(valArr.toString());
	},
	_setDisabled: function(disabled) {
		disabled = !!disabled;

		this.uiBoxlist.find(".coral-checkbox").each(function() {
			$(this).toggleClass( "coral-state-disabled", disabled );
		});
		
		this.options.disabled = disabled;
	},
	_setReadonly: function(readonly) {
		readonly = !!readonly;

		this.uiBoxlist.find(".coral-checkbox").each(function() {
			$(this).toggleClass( "coral-readonly", readonly );
		});
		
		this.options.readonly = readonly;
	},	
	//设置属性处理
	_setOption: function(key, value) {
		//默认属性不允许更改
		
		if (key === "id" || key === "name") {
			return;
		}
		if (key === "readonly") {
			this._setReadonly(value);
		} 
		if (key === "disabled") {
			this._setDisabled(value);
			return;
		}
		if (key ==="maxLabelWidth"){
			var maxLabelWidth = value;
			if ( value != "auto" ){
				maxLabelWidth = maxLabelWidth+"px";
                var array = this.component().find(".coral-checkbox-text");
                for( var i=0;i<array.length;i++){
                	var text = $(array[i]).html();
                	$(array[i]).attr("title",text);
                }
			} else {
				maxLabelWidth = "";
				this.component().find(".coral-checkbox-text").attr("title","");
			}
			this.component().find(".coral-checkbox-text").css("max-width",maxLabelWidth);
		}
		this._super(key, value );
	},
	_destroy : function() {
		this.component().remove();
		if (this.options.name) {
			this.element.removeAttr("orgname").attr("name", this.options.name);
		}
		this.element.removeClass("coral-form-element-checkboxlist");
		this.element.removeClass("coral-validation-checkboxlist");
		this.element.show();
	},
	focus: function() {
		//TODO:focus
	},
	component : function() {
		return this.uiBoxlist;
	},
	disable : function() {
		this._setOption("disabled", true);
		this._setDisabled(true);
	},
	readonly : function(){
		this._setReadonly("readonly",true);
	},
	enable : function() {
		this._setOption("disabled", false);
		this._setDisabled(false);
	},
	disableItem : function (value) {
		this.uiBoxlist.find(".coral-checkbox[data-value=\"" + value + "\"]").toggleClass( "coral-state-disabled", true );
	},
	enableItem : function (value) {
		this.options.disabled = false;
		
		var item = this.uiBoxlist.find(".coral-checkbox[data-value=\"" + value + "\"]").toggleClass( "coral-state-disabled", false );		
	},
	show : function() {
		this.component().show();
	},
	hide : function() {
		this.component().hide();
	},
	getValue : function() {
		return this.uiInput.val();
	},
	setValue : function(value, force) {
		var oldValue = this.getValue() || [];
		this.uiBoxlist.find(".icon-checkbox-checked").each(function() {
			$(this).removeClass("icon-checkbox-checked").addClass("icon-checkbox-unchecked");
		});
		
		var i = 0, valArr = $.isArray(value) ? value 
				: ((!value || typeof value !== "string" || "" === $.trim(value)) ? [] : value.split(","));
		for (; i < valArr.length; i++) {
			this.uiBoxlist.find(".coral-checkbox[data-value=\"" + valArr[i] + "\"]").find(".coral-checkbox-icon")
							   .removeClass("icon-checkbox-unchecked").addClass("icon-checkbox-checked");
		}
		
		this.uiInput.val(valArr.toString());
		
		/*if (force !== true) {
			this.valid();
		}*/
	},
	// 反选 force 的意义同 setValue 中的 force
	invertCheck : function (force) {
		var valArr = [];
		
		this.uiBoxlist.find(".icon-checkbox-checked").each(function() {
			$(this).removeClass("icon-checkbox-checked").addClass("coral-checkbox-temp");
		});
		this.uiBoxlist.find(".icon-checkbox-unchecked").each(function() {
			$(this).removeClass("icon-checkbox-unchecked").addClass("icon-checkbox-checked");
		});
		this.uiBoxlist.find(".coral-checkbox-temp").each(function() {
			$(this).removeClass("coral-checkbox-temp").addClass("icon-checkbox-unchecked");
		});
		
		this.uiBoxlist.find(".coral-checkbox").each(function() {
			if ($(this).find(".coral-checkbox-icon").hasClass("icon-checkbox-checked")) {
				valArr.push( $(this).attr("data-value") );
			}			
		});
		
		this.setValue(valArr, force);		
	},
	// 全选；若要反选请使用 setValue(null)
	checkAll : function () {
		var valArr = [], i = 0, row = null, data = this.data;
		for (; i < data.length; i++ ) {
			row = data[i];
			valArr.push(row[this.options.valueField]);
		}
		this.setValue(valArr);
	},
	// 获取指定项的显示名称
	getText : function (values/*String:"CN,US,EN" or Array:["CN","US","EN"]*/) {
		var i    = 0, 
		    data = this.data,
		    val  = null,
		    txtArr = [];
		if (!values) {
			values = this.getValue().split(",");
		} else if (typeof values) {
			values = values.split(",");
		}
		for (; i < data.length; i++) {
			val = data[i][this.options.valueField];
			if ($.inArray(val, values) > -1) txtArr.push(data[i][this.options.textField]);
		}
		return txtArr.toString();
	}
});
})(jQuery);