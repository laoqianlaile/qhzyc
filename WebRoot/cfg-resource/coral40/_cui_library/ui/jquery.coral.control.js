/*!
 * 组件库4.0：下拉框
 * 
 * 依赖JS文件：
 *    jquery.coral.core.js
 *    jquery.coral.component.js
 *    jquery.coral.panel.js
 *    jquery.validatehelper.js
 */

(function ($) {
"use strict";
$.component("coral.control", {
	version: "4.0.2",
	options: {
		inputButtonGutter : 5,//textbox和button之间的间距
	},
	_create: function(){
	},
	_outerButtons: function(){
		this.uiDialogOuterButtonPanel = $("<span class=\"coral-outerbuttonset coral-corner-all \"></span>");
		this.component().append( this.uiDialogOuterButtonPanel );
		this._createButtons( this.options.buttonOptions,null, this.uiDialogOuterButtonPanel );
		this.component().css( "padding-right", this.uiDialogOuterButtonPanel.outerWidth() + 8 );
	},
	_createButtonPanel: function() {
		var is = false;
		for(var i=0;i< this.options.buttons.length;i++){
			var direction = "right";
			var pos = "inner";
			var key = "innerRight";
			var opermethod = "prepend",
				buttons = this.options.buttons;
			if(buttons[i]["innerLeft"]){
				key = "innerLeft";
				direction = "left";
				this._addButtonPanel(pos,key,direction,opermethod,i);
				is = true;
			}
			if(buttons[i]["innerRight"]){
				key = "innerRight";
				direction = "right";
				opermethod = "append";
				this._addButtonPanel(pos,key,direction,opermethod,i);
				is = true;
			}
			if(buttons[i]["outerRight"]){
				key = "outerRight";
				direction = "right";
				pos = "outer";
				opermethod = "append";
				this._addButtonPanel(pos,key,direction,opermethod,i);
				is = true;
			}
			if(buttons[i]["outerLeft"]){
				key = "outerLeft";
				direction = "left";
				pos = "outer";
				this._addButtonPanel(pos,key,direction,opermethod,i);
				is = true;
			}
			if(buttons[i]["floatRight"]){
				key = "floatRight";
				pos = "float";
				opermethod = "append";
				this._addButtonPanel(pos,key,direction,opermethod,i);
				is = true;
			}
			if(buttons[i]["floatLeft"]){
				key = "floatLeft";
				direction = "left";
				pos = "float";
				this._addButtonPanel(pos,key,direction,opermethod,i);
				is = true;
			}
		}
		if( !is ){
			this.uiDialogButtonPanel = $("<span class='"+ key +" coral-textbox-btn-icons coral-buttonset coral-corner-"+direction+" '></span>");
			this._createButtons( this.options.buttons, direction , this.uiDialogButtonPanel );
			this.elementBorder.append( this.uiDialogButtonPanel );
			this.elementBorder.css( "padding-right", this.uiDialogButtonPanel.outerWidth());
			this.uiDialogButtonPanel.css( "right", 0 );
		}
	},
	_addButtonPanel: function(pos,key,direction,opermethod,i){
		this.uiDialogButtonPanel = $("<span class='"+ key +" coral-textbox-btn-icons coral-buttonset coral-corner-"+direction+" '></span>");
		this.uiDialogOuterButtonPanel = $("<span class=\"coral-outerbuttonset coral-corner-all \"></span>");
		if ( pos == "inner" ) {
			this._createButtons( this.options.buttons[i][key], direction , this.uiDialogButtonPanel );
			this.elementBorder[opermethod]( this.uiDialogButtonPanel );
			this.elementBorder.css( "padding-" + direction, this.uiDialogButtonPanel.outerWidth() );
			this.uiDialogButtonPanel.css( direction, 0 );
		} else if(pos == "float"){
			this.component()[opermethod]( this.uiDialogOuterButtonPanel );
			this._createButtons( this.options.buttons[i][key], null , this.uiDialogOuterButtonPanel );
			this.uiDialogOuterButtonPanel.css( direction, -(this.uiDialogOuterButtonPanel.outerWidth()+this.options.inputButtonGutter ));
		}else {
			this.component()[opermethod]( this.uiDialogOuterButtonPanel );
			this._createButtons( this.options.buttons[i][key], null, this.uiDialogOuterButtonPanel );
			var width = this.uiDialogOuterButtonPanel.outerWidth() ;
			this.component().css( "padding-" + direction, this.uiDialogOuterButtonPanel.outerWidth() + this.options.inputButtonGutter );
			this.uiDialogOuterButtonPanel.css( direction, 0 ).css("width",width);
		}
	},
	_createButtons: function( buttons,direction, appendTo ) {
		var that = this;
		if ( $.isEmptyObject( buttons ) ) buttons = {};
		if ( buttons instanceof Array == false ) {
			buttons = [buttons];
		}
		$.each( buttons, function(i) {
			var buttonOptions,
				addCls = "",
				removeCls = "",
				props = $.extend( { type: "button" }, {click: this.click} );
			this.click = this.onClick || this.click;
			delete this.onClick;
			var click = this.click || $.noop;
			props.click = function() {
				click.apply( that.element[0], arguments );
			};
			delete this.click;
			removeCls = "coral-corner-all";
			if(direction == "left"){
				if(i == 0){
					addCls = "coral-corner-left";
				}
				$( "<button></button>", props ).button( this )
				.addClass(addCls).removeClass(removeCls).appendTo( appendTo );
			}else if(direction == "right"){
					if(i==(buttons.length-1)){
						addCls = "coral-corner-right";
					}
					$( "<button></button>", props ).button( this )
					.addClass(addCls).removeClass(removeCls).appendTo( appendTo );
				}else{
					$( "<button></button>", props ).button( this )
					.addClass(addCls).appendTo( appendTo );
				}
			this.click = click;
		});
	}
});
})(jQuery);