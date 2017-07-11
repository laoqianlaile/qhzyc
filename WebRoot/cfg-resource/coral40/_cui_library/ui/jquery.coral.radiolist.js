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
	
$.component("coral.radiolist", {
	version: "4.0.1",
	castProperties : ["data", "triggers"],
	options: {
		id: null,
		name: null,
		valueField: "value",
		textField : "text",
		//width : "auto",
		//height : 22,
		required : false,
		labelField: null, // 表单元素前面的文本
		starBefore: false, // 必输项 * 是否前面
		showStar: true,
		column : null, // 每行放几个单选框
		disabled : false,
		readonly:false,
		isCheck: false,
		allowCancel:false,
		value : null,
		valueIndex: null, // number
		data  : [], // 数组形式: [{value:,text:},...]或者 字符串形式: "cn:中国;us:美国;..."
		url   : null,
		termSplit : ";", // 如果data是字符串，则termSplit作为复选框组的分隔符
		itemSplit : ":", // 如果data是字符串，则每个复选框隐藏值与显示值的分隔符
		repeatLayout: "table", // "table", "flow" // flow 时自适应
		itemWidth: "auto", // repeatLayout 为 flow 时，radio item 控制宽度，用于对齐

		errMsg: null,
		errMsgPosition: "leftBottom",
		onLoad: null, // url
		onValidError: null,
		onKeyDown: null,
		onValidSuccess: null,
		triggers: null, // 覆盖 validate 里的 triggers
		excluded: false, // true 则不单独校验
		onChange : $.noop /*参数(event, {value: string, checked: boolean})*/
	},
    _create: function () {
    	var that = this,
    	    textbox = null, 
    	    valuebox = null,
    	    uiArrow = null,
    	    options = this.options;
    	
    	if ( !this.element.jquery ) {
    			this.element = $(this.element);
		}

    	this.element.addClass("coral-form-element-radiolist coral-validation-radiolist ctrl-init ctrl-form-element ctrl-init-radiolist");

    	typeof that.element.attr("id") == "undefined" && !!that.options.id&&that.element.attr( "id", that.options.id );
    	that.options.id = that.element.uniqueId().attr("id");
    	
    	var name = that.element.attr("name");
    	typeof name != "undefined" ? (that.options.name = name) : (that.element.attr("name", that.options.name));

    	this.uiBoxlist = $("<span class=\"coral-radiolist\"></span>");
		this.uiInput   = $("<input type=\"hidden\">");
		if (this.options.name) {
			this.uiInput.attr("name", this.options.name);
			this.element.removeAttr("name").attr("orgname", this.options.name);
		}
		// 数据处理
		this._proccessData();
		// 创建复选框组
		if (this.options.repeatLayout == "table") {
			if (this.options.column == null ) {
				this.options.column = 3;
			} 
			this._createTable();
			//this.uiBorder = $("<span class=\"coral-radiolist-border\"></span>");
			
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
			this.uiLabel= $("<label class=\"coral-label\">"+ options.labelField +"</label>");
			this.uiBoxlist.prepend(this.uiLabel);
			this.uiBoxlist.addClass("coral-hasLabel");
		}
		// add label and required star before function @lhb @2015-04-27

		this.uiBoxlist.insertAfter(this.element);
		// 设置默认值
		if (this.options.value) {
			this.setValue( this.options.value );
		}
		
		this.element.hide();
		
		this._bindEvent();
		
		if ( this.options.url ) {
			this._trigger("onLoad", null, [{data: this.getData()}]);
		}
	},
	/**
	 * 获取生成筛选框的数据
	 * @returns
	 */
	_proccessData: function() {
		var that = this,
			options = this.options;

		if (options.url) {
			$.ajax({
				type: "get",
				url: options.url,
				data: {},
				async: false,
				dataType: "json",
				success: function (data) {
					that._initData(data);
				},
				error: function () {
			        $.alert( "Json Format Error!" );
				}
			});
		} else if (options.data) {
			this._initData(options.data);
		}
	},
	_initData : function (data) {
		var tmpArr = null,
	        i = 0, option = null, 
	        tmpRow = null, rowArr = null;
		
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
	/**
	 * 返回data ( url / data )
	 */
	getData: function () {
		return this.data;
	},
	_createBorder: function () {
		var that = this,
			opts = this.options,
			column = this.options.column,
			data = this.data;
		
		that.uiBorder = $("<span class=\"coral-radiolist-border\"></span>");
		
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
		var uiRadio = $("<span class='coral-radio "+isHidden+"'></span>"),
		    uiLabel    = $("<label class='coral-radio-label'></label>"),
		    uiIcon = $("<span class='coral-radio-icon'></span>"),
		    value      = cellData[this.options.valueField],
		    text       = cellData[this.options.textField];
		
		uiRadio.attr("value", value);		
		uiLabel.append(uiIcon).append(text);
		uiLabel.appendTo(uiRadio);
		uiIcon.addClass("icon icon-radio-unchecked");
		
		return uiRadio;
	}, 
	_bindEvent: function() {
		var that = this;
		
		if ( this.options.disabled ) {
			this._setDisabled ( this.options.disabled );
		}
		this._on( {
			"mouseenter .coral-radio": function( e ){
				if ( that.options.readonly ) {
					return false;
				}
				var radio = $( e.target ).closest( ".coral-radio" );
				if ( $( radio ).hasClass( "coral-state-disabled" ) ) {
					return;
				}
				$( radio ).addClass( "coral-radio-hover" );
			},
			"mouseleave .coral-radio": function( e ){
				if ( that.options.readonly ) {
					return false;
				}
				var radio = $( e.target ).closest( ".coral-radio" );
				if ( $( radio ).hasClass( "coral-state-disabled" ) ) {
					return;
				}
				$( radio ).removeClass( "coral-radio-hover" );
			},
			"keydown .coral-radio": function( e ){
				that._trigger( "onKeyDown", e, {} );
			},
			"click .coral-radio": function( e ){
				var radio = $( e.target ).closest( ".coral-radio" );
				if ( that.options.readonly ) {
					return false;
				}				
				var uiRadio = radio,
					uiIcon = uiRadio.find( ".coral-radio-icon" ),
				    value   = uiRadio.attr( "value" ),
				    oldValue = that.getValue();
				if ( value == oldValue ){
					if ( !this.options.allowCancel ) return false;
					uiIcon.removeClass( "icon-radio-checked" ).addClass( "icon-radio-unchecked" );
					that.uiInput.val( "" );
				} else {
				    that.setValue( value );
				    that._trigger( "onChange", null, {value: value, checked: uiIcon.hasClass( "icon-radio-checked" )} );
				}				
				e.stopPropagation();
			}
		} );
	},
	_setDisabled: function(disabled) {
		disabled = !!disabled;
		
		this.uiBoxlist.find(".coral-radio").each(function() {
			$(this).toggleClass( "coral-state-disabled", disabled );
		});
		
		this.options.disabled = disabled;
	},	
	_setReadonly: function(readonly) {
		readonly = !!readonly;
		
		this.uiBoxlist.find(".coral-radio").each(function() {
			$(this).toggleClass( "coral-readonly", readonly );
		});
		
		this.options.readonly = readonly;
	},
	_setOption: function(key, value) {
		if (key === "id" || key === "name") {
			return ;
		}

		if (key === "disabled") {
			this._setDisabled(value);
		}
		if (key === "readonly") {
			this._setReadonly(value);
		}
		
		this._super(key, value );
	},
	_destroy : function() {
		// ??
		this.component().remove();
		
		if (this.options.name) {
			this.element.removeAttr("orgname").attr("name", this.options.name);
		}
		this.element.removeClass("coral-form-element-radiolist");
		this.element.removeClass("coral-validation-radiolist");
		this.element.show();
	},
	focus: function(){
		//TODO: focus 
	},
	component : function() {
		return this.uiBoxlist;
	},
	disable : function() {
		this._setDisabled(true);
	},
	readonly : function(){
		this._setReadonly(true);
	},
	enable : function() {
		this._setDisabled(false);
	},
	disableItem : function (value) {
		this.uiBoxlist.find( ".coral-radio[value=\"" + value + "\"]" ).toggleClass( "coral-state-disabled", true );
	},
	enableItem : function (value) {
		this.options.disabled = false;
		this.uiBoxlist.find( ".coral-radio[value=\"" + value + "\"]" ).toggleClass( "coral-state-disabled", false );
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
	setValue : function( value ) {
		this.uiBoxlist.find(".icon-radio-checked").each(function() {
			$(this).removeClass("icon-radio-checked").addClass("icon-radio-unchecked");
		});
		
		var item = this.uiBoxlist.find(".coral-radio[value=\"" + value + "\"]").find(".coral-radio-icon")
			.removeClass("icon-radio-unchecked").addClass("icon-radio-checked");
		this.uiBoxlist.find(".coral-radio").removeClass("coral-state-highlight");
		this.uiBoxlist.find(".coral-radio[value=\"" + value + "\"]").addClass("coral-state-highlight");
		
		this.uiInput.val( value );	
	},
	// 获取指定项的显示名称
	getText : function (value) {
		var i    = 0, 
		    data = this.data,
		    val  = null,
		    txtArr = [];
		if (!value) {
			value = this.getValue();
		}
		for (; i < data.length; i++) {
			val = data[i][this.options.valueField];
			if (val == value) return (data[i][this.options.textField]);
		}
		return "";
	}
});
})(jQuery);