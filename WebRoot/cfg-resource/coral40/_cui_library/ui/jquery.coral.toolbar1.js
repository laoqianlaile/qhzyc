/**
 *	Coral 4.0: 工具条（toolbar）
 *
 * 依赖JS文件:
 *		jquery.coral.core.js
 *		jquery.coral.component.js
 *		jquery.coral.button.js
 *		jquery.coral.textbox.js
 *		jquery.coral.combobox.js
 */

( function ( $, undefined ) {
	"use strict";	

	$.component ( "coral.toolbar1", {
		version: "4.0.2",
		castProperties: ["data"],
		options: {
			/* default options */
			id: null,
			name: null,
			disabled: false,
			cls: null,
			url: null, 
			method: "get",
			data: null,
			width: null,
			height: null,
			/* default events */
			onCreate: null,
			onClick: null
		},
		/**
		 * get coral component
		 */
		component: function () {
			return this.uiBox;
		},
		/**
		 * get coral border
		 */
		_uiBorder: function() {
			return uiBorder;
		},
		/**
		 * create
		 */
		_create: function () {
			var that = this;
			
			this._initElements();
			this._bindEvents();
		},
		_bindEvents: function () {
			/* var that = this; */
		},
		/**
		 * initialize elements
		 */
		_initElements: function () {
			var that = this,
				options = this.options;
			
			this.uiBox = $( "<span class=\"coral-toolbar\"></span>" );
			this.uiBorder =  $( "<span class=\"coral-toolbar-border\"></span>" );
			this.uiBox.append( this.uiBorder );
			this.uiBox.insertAfter( this.element );
			this.element.appendTo( this.uiBorder );
			this.element.addClass( "ctrl-init ctrl-init-toolbar" );
			
			if(typeof this.element.attr("id") != "undefined"){
	    		this.options.id = this.element.attr("id");
	    	} else if (this.options.id){
	    		this.element.attr("id", this.options.id);
	    	}
	    	if(typeof this.element.attr("name") != "undefined"){
	    		this.options.name = this.element.attr("name");
	    	} else if (this.options.name){
	    		this.element.attr("name", this.options.name);
	    	}
			
			this._loadData();
		},
		/**
		 * load json data from options.url or options.data
		 */
		_loadData: function () {
			var that = this,
				options = this.options;
	
			if (options.url) {
				$.ajax({
					type: options.method,
					url: options.url,
					data: {},
					dataType: "json",
					success: function (json) {
						that._initData(json);
					},
					error: function () {
				        $.alert( "Json Format Error!" );
					}
				});
			} else if (options.data) {
				this._initData(options.data);
			}
		},
		/**
		 * initialize json data
		 */
		_initData: function (json) {
			var that = this;
			
			if (typeof json === "object" && typeof json.data === "object") {
				var data = json.data;
				this._addItems(null, data); // index = null 代表尾部直接添加
			}
		},
		/**
		 * 增加子项内部处理方法
		 * @param index{number}：0 ~ length,字符型数字,null代表尾部直接添加
		 * @param data {json object{} or array[]} : 子项数据对象
		 * @return : ;
		 */
		_addItems: function (index, data) {
			if (typeof data !== "object") return ;
			
			var that = this,
				items = [];
			
			$.each( data, function( i, itemData ) {
				items.push( that._createItem(itemData) );
			});
			
			this._appendItems(index, items);
			this._initItems(items);
		},
		/**
		 * 初始化添加的项内部组件
		 * @param items {json object{}} : 子项数据对象
		 * @return ;
		 */
		_initItems: function ( items ) {
			for (var i in items) {
				var $el = items[i].$el,
					coralType = items[i].coralType,
					opts = items[i].options;
				
				$el[coralType](opts);
			}
		},
		/**
		 * 根据索引增加子项到页面
		 * @param index{number}：0 ~ length,字符型数字,null代表尾部直接添加
		 * @param items {json object{}} : 子项数据对象
		 * @return ;
		 */
		_appendItems: function (index, items) {
			if ( null == index || index == this.getLength()) {
				for (var i in items) {
					items[i].$item.appendTo(this.element);
				}
			} else if ( index == 0 ){
				for (var j in items) {
					items[j].$item.prependTo(this.element);
				}
			} else {
				for (var k in items) {
					this.element.find(".coral-toolbar-item:eq(" + index + ")").before(items[k].$item);
				}
			}
		},
		/**
		 * create a toolbar item
		 * @param itemData{json object}:子项数据对象
		 * @return {jquery object{}};
		 */
		_createItem: function (itemData) {
			var that = this,
				data = itemData,
				type = itemData.type,
				opts = itemData.options,
				$el = this._createEl(type),
				$item = $("<span class=\"coral-toolbar-item\" data-type=\"" + type + "\"></span>");
			
			$el.appendTo($item);
			//$item.appendTo(this.element);
			//$el[type](opts);
			
			return {
				$item: $item, // item jquery dom object
				$el: $el, // item jquery element of coral
				coralType: type, // coral type
				options: opts // coral init options
			}
		},
		/**
		 * 根据组件类型，创建一个对应的jQuery element
		 * @param coralType(string):组件类型
		 * @return jQuery object
		 */
		_createEl: function (coralType) {
			var $el;
			
			switch(coralType) {
				case "button":
					$el = $("<button></button>");
					break;
				case "textbox":
				case "combobox":
					$el = $("<input type=\"text\"/>");
					break;
				case "splitbutton":					
				case "menubutton":
					$el = $("<span></span>");
					break;
				default:
					$el = $("<div></div>");
					break;
			}
			// 标识是toolbar下的element
			return $el.addClass("ctrl-toolbar-element");
		},
		/**
		 * 获取子项长度
		 * @return {number};
		 */
		getLength: function () {
			return this.element.children( ".coral-toolbar-item" ).length;
		},
		/**
		 * 根据id判断是否存在子项
		 * @return {boolean} : true - 存在; false - 不存在
		 */
		isExist: function ( id ) {
			var nodes = this.element.find( ".ctrl-toolbar-element" ).filter( "[id$='" + id + "']" );
			
			return nodes.length ? true : false;
		},
		/**
		 * 增加子项
		 * @param index{number}：0 ~ length,字符型数字,null代表尾部直接添加
		 * @param data {json object{} or array[]} : 子项数据对象
		 * @return : false or ;
		 */
		add: function ( index, data ) {
			if (typeof data !== "object") return;
			
			var that = this,
				idx = parseInt( index );
			
			if ( ((null != index) && isNaN(idx)) || idx < 0 || idx > this.getLength() ) {
				return false;
			}
			if ( !$.isArray(data) ) {
				data = [data];
			}
			// 如果长度为0，则直接添加在尾部
			if ( 0 == this.getLength() ) {
				index = null;
			}
			this._addItems(index, data);
		},
		/**
		 * remove item by index or id of item
		 * @param key {index(number)/id(string)}: 索引或id
		 */
		remove: function ( key ) {
			var that = this;
			
			if ( typeof key === "string" ) {
				return that._removeById( key );
			} else {
				return that._removeByIndex( key );
			}
			
		}, 
		/**
		 * remove all items
		 */
		removeAll: function() {
			this.element.children(".coral-toolbar-item").remove();
		},
		_removeById: function(id) {
			$( "#"+id ).closest(".coral-toolbar-item").remove();
		},
		_removeByIndex: function(index) {
			var idx = parseInt(index);
			
			if (isNaN(idx) || idx < 0 || idx > (this.getLength() - 1) ) return false;
			
			this.element.find(".coral-toolbar-item:eq(" + idx + ")").remove();
		},
		/**
		 * disable all items
		 */
		disable: function () {
			this._setDisabled(true);
		},
		/**
		 * enable all items
		 */
		enable: function () {
			this._setDisabled(false);
		},

		/**
		 * disabled handler code
		 * @param disabled{boolean}: true - disable; false - enable
		 */
		_setDisabled: function(disabled) {
			var that = this;
			
			var $els = $.coral.findComponent( this.element , ".ctrl-toolbar-element" );	
			
			for ( var i in $els ) {
				if (disabled) {
					$els[i].disable();
				} else {
					$els[i].enable();
				}
			}
			
			this.options.disabled = !!disabled;			
		},		
		/**
		 * disable item by index or id
		 * @param key {index(number)/id(string)}: 索引或id
		 */
		disableItem: function ( key ) {
			var that = this;
			
			if ( typeof key === "string" ) {
				return that._disableItemById( key );
			} else {
				return that._disableItemByIndex( key );
			}
		},
		/**
		 * disable item by inner coral's id
		 * @param id{string} : coral's id attribute
		 * @return {boolean} : true - 禁用成功; false - 禁用失败
		 */
		_disableItemById: function(id) {
			var that = this;
			
			var $itemEl = $.coral.findComponent( $( "#"+id ).closest(".coral-toolbar-item") , ".ctrl-toolbar-element");
			
			if (!$itemEl.length) {
				return false;
			}
			
			$itemEl[0].disable();
			return true;
		},
		/**
		 * disable item by item's index
		 * @param index{number}:item's index
		 * @return {boolean} : true - 禁用成功; false - 禁用失败
		 */
		_disableItemByIndex: function(index) {
			var that = this,
				idx = parseInt(index);
			
			if (isNaN(idx) || idx < 0 || idx > (this.getLength() - 1) ) return false;
			
			var $itemEl = $.coral.findComponent( this.element.find(".coral-toolbar-item:eq(" + idx + ")") , ".ctrl-toolbar-element");
				
			if (!$itemEl.length) {
				return false;
			}
			
			$itemEl[0].disable();
			return true;
		},
		/**
		 * enable item by index or id
		 * @param key {index(number)/id(string)}: 索引或id
		 */
		enableItem: function ( key ) {
			var that = this;
			
			if (typeof key === "string" ) {
				return that._enableItemById( key );
			} else {
				return that._enableItemByIndex( key );
			}
		},
		/**
		 * enable item by inner coral's id
		 * @param id{string}:coral's id attribute
		 */
		_enableItemById: function(id) {
			var that = this;
			
			var $itemEl = $.coral.findComponent( $( "#"+id ).closest(".coral-toolbar-item") , ".ctrl-toolbar-element");
			
			if (!$itemEl.length) {
				return false;
			}
			
			$itemEl[0].enable();
			return true;
		},
		/**
		 * enable item by item's index
		 * @param index{number}:item's index
		 */
		_enableItemByIndex: function(index) {
			var that = this,
				idx = parseInt(index);
			
			if (isNaN(idx) || idx < 0 || idx > (this.getLength() - 1) ) return false;
			
			var $itemEl = $.coral.findComponent( this.element.find(".coral-toolbar-item:eq(" + idx + ")") , ".ctrl-toolbar-element");
			
			if (!$itemEl.length) {
				return false;
			}
			
			$itemEl[0].enable();
			return true;
		},
		/**
		 * hide item by index or id
		 * @param key {index(number)/id(string)}: 索引或id
		 */
		hide: function ( key ) {
			var that = this;
			
			if ( typeof key === "string" ) {
				return that._hideById( key );
			} else {
				return that._hideByIndex( key );
			}
		},
		/**
		 * hide all items
		 */
		hideAll: function() {
			var that = this;
			
			this.element.children(".coral-toolbar-item").hide();
		},
		/**
		 * hide item by inner coral's id
		 * @param id{string}:coral's id attribute
		 */
		_hideById: function(id) {
			var that = this;
			
			var $item = $( "#"+id ).closest(".coral-toolbar-item");
			if (!$item.length) return false;
			
			$item.hide();
			return true;
		},
		/**
		 * hide item by item's index
		 * @param index{number}:item's index
		 */
		_hideByIndex: function(index) {
			var that = this,
				idx = parseInt(index);
			
			if (isNaN(idx) || idx < 0 || idx > (this.getLength() - 1) ) return false;

			var $item = this.element.find(".coral-toolbar-item:eq(" + idx + ")");
			if (!$item.length) return false;
			
			$item.hide();
			return true;
		},
		/**
		 * show item by index or id
		 * @param key {index(number)/id(string)}: 索引或id
		 */
		show: function ( key ) {
			var that = this;
			
			if (typeof key === "string" ) {
				return that._showById(key);
			} else {
				return that._showByIndex(key);
			}
		},
		/**
		 * show all items
		 */
		showAll: function() {
			var that = this;
			
			this.element.children(".coral-toolbar-item").show();
		},
		/**
		 * show item by inner coral's id
		 * @param id{string}:coral's id attribute
		 */
		_showById: function(id) {
			var that = this;
			
			var $item = $( "#"+id ).closest(".coral-toolbar-item");
			if (!$item.length) return false;
			
			$item.show();
			return true;
		},
		/**
		 * show item by item's index
		 * @param index{number}:item's index
		 */
		_showByIndex: function(index) {
			var that = this,
				idx = parseInt(index);
			
			if (isNaN(idx) || idx < 0 || idx > (this.getLength() - 1) ) return false;
			
			var $item = this.element.find(".coral-toolbar-item:eq(" + idx + ")");
			if (!$item.length) return false;
			
			$item.show();
			return true;
		},
		/**
		 * set option
		 * @param key {string}:键
		 * @param value {string}:值
		 */
		_setOption: function ( key, value ) {
			var that = this;
			//default option can't be modified
			if (key === "id" || key === "name" ) {
				return;
			}			 		
			if ( key === "disabled" ) {
				this._setDisabled(value);
			}
			
			this._super( key, value );
		}
	});
	
	$.fn['toolbar'].defaults = {
		width: 'auto'
	};

})(jQuery);