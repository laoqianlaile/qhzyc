(function( $, undefined ) {

$.component( "coral.textboxtree", $.coral.autocomplete, {

	requestIndex: 0,
	pending: 0,
	options: {
		allowPushParent: false
	},
	_create: function() {
		this.options.render = "tree";
		this._super();
		$( this.element ).easyText({
			minWidth:20,
			maxWidth:200,
			maxChars:60
		});
		// Clicks outside of a treePanel move the input element to the end
		this._on( this.document, {
			click: function( event ) {
				this._moveInput( "last", event );
			}
		});
	},
	_filter: function( array, request, response  ){
		$( this.menu.element ).tree("reload", null, array);
		$( this.menu.element ).tree("filterNodesByParam", {"name": request.term} );
		response( array );
	},
	_initData: function(){
		var that = this;
		this.menu = {};
		var u = $( "<ul>" )[0];
		$( u )
		.uniqueId()
		.addClass( "coral-autocomplete-panel coral-front" )
		.appendTo( this._appendTo() )
		.tree({
			showLine: false,
			showIcon: false,
			componentCls: "coral-autocomplete-tree",
			addDiyDom : function (treeId, treeNode) {
				var spaceWidth = 15;
				var switchObj = $("#" + treeNode.tId + "_switch"), icoObj = $("#" + treeNode.tId + "_ico");
				switchObj.remove();
				icoObj.before(switchObj);

				if (treeNode.level > 0) {
					var spaceStr = "<span style='display: inline-block;width:"
							+ (spaceWidth * treeNode.level) + "px'></span>";
					switchObj.before(spaceStr);
				}
				switchObj.remove();
			},
			beforeClick : function ( treeId, treeNode ) {
				if ( !that.options.allowPushParent && treeNode.isParent ) {
					return false;
				} 
			},
			onClick: function( event, treeId, treeNode ){
				var item = treeNode,
					previous = that.previous;
				// only trigger when focus was lost (click on menu)
				if ( that.element[ 0 ] !== that.document[ 0 ].activeElement ) {
					that.element.focus();
					that.previous = previous;
					// #6109 - IE triggers two focus events and the second
					// is asynchronous, so we need to reset the previous
					// term synchronously and asynchronously :-(
					that._delay(function() {
						that.previous = previous;
						that.selectedItem = item;
					});
				}

				if ( false !== that._trigger( "onNodeClick", event, { item: item } ) ) {
					// ??
					that._value( item[that.options.valueField] );
					//that._text( item[that.options.textField] );
					that.selectedItems.push( item );
					that._change(event);
				}
				// reset the term after the select event
				// this allows custom select handling to work properly
				//that.term = that._text();
				that.term = that._text();
				that.close( event );
				that.selectedItem = item;
			}
		})
		.hide();
		this.menu.element = $( u );
	},
	_initAutocomplete: function(){
		var nodeName = this.element[ 0 ].nodeName.toLowerCase(),
			isTextarea = nodeName === "textarea",
			isInput = nodeName === "input";
		this.isMultiLine =
			// Textareas are always multi-line
			isTextarea ? true :
			// Inputs are always single-line, even if inside a contentEditable element
			// IE also treats inputs as contentEditable
			isInput ? false :
			// All other element types are determined by whether or not they're contentEditable
			this.element.prop( "isContentEditable" );
		
		this.autocompleteWrapper = $("<span class=\"coral-autocomplete coral-textboxlist coral-textbox\" style=\"border-width: 0;\"></span>").insertAfter(this.element);
		this.autocompleteBorder = $("<span class='coral-textbox-border coral-corner-all clearfix'></span>").appendTo(this.autocompleteWrapper);
		// 下拉框显示值文本框
		this.valuebox = $("<input type='hidden'>").appendTo(this.autocompleteBorder);
		
		this.valueMethod = this.valuebox[ isTextarea || isInput ? "val" : "text" ];
		this.textMethod = this.element[ isTextarea || isInput ? "val" : "text" ];
		
		this.element
			.addClass( "ctrl-form-element coral-validation-autocomplete coral-autocomplete-input coral-autocomplete-text coral-textbox-default coral-form-element-autocomplete" )
			.attr( "autocomplete", "off" );
		this.textboxlistUl = $( "<ul class='coral-textboxlist-ul'><li class='coral-textboxlist-inputLi'></li></ul>" );
		this.textboxlistUl.appendTo( this.autocompleteBorder );
		this.element.appendTo( this.textboxlistUl.find( ".coral-textboxlist-inputLi" ) );
		if ( this.options.buttons.length > 0 ) {
			this._createButtonPanel();
		}
		// 初始化id，name，value
		if ( typeof this.element.attr("id") != "undefined" ) {
    		this.options.id = this.element.attr("id");
    	} else if ( this.options.id ){
    		this.element.attr("id", this.options.id);
    	}
		if ( typeof this.element.attr( "name" ) != "undefined" ) {
    		this.options.name = this.element.attr( "name" ); // name属性加到hidden元素上
    		this.valuebox.attr( "name", this.options.name );
    		this.element.removeAttr( "name" );
    	} else if ( this.options.name ) {
    		this.valuebox.attr( "name", this.options.name );
    	}

		if ( this.options.text ) {
			this._text( this.options.text );
		}
		if ( $.trim( this.valuebox.val() ) != "" ) {
    		this.options.value = this.valuebox.val();
    	}
	},
	_bindEvent: function(){
		this._super();
		this._on( {
			"click .coral-label-close" : function( e ){
				this._removeLabel( e );
			},
			"click .coral-textboxlist-item" : function( e ){
				e.stopPropagation();
			},
			"click .coral-textbox-border" : function( e ){
				this.element.focus();
			},
			"mouseenter .coral-label-close" : function( e ){
				$( e.target ).addClass( "coral-label-close-hover" );
			},
			"mouseleave .coral-label-close" : function( e ){
				$( e.target ).removeClass( "coral-label-close-hover" );
			},
			"focus .coral-textboxlist-item" : function( e ) {
				$( e.target ).addClass( "coral-state-active" );
			},
			"blur .coral-textboxlist-item" : function( e ) {
				$( e.target ).removeClass( "coral-state-active" );
			},
			"keydown .coral-state-active" : function( e ) {
				var keyCode = $.coral.keyCode;
				switch ( e.keyCode ) {
				case keyCode.LEFT:
					//suppressKeyPress = true;
					this._moveInput( "left", e );
					break;
				case keyCode.RIGHT:
					//suppressKeyPress = true;
					this._moveInput( "right", e );
					break;
				case keyCode.BACKSPACE:
					this._removeLabel( e );
					break;
				case keyCode.DELETE:
					this._removeLabel( e );
					break;
				default:
					break;
				}
			}
		});
	},
	_term: function(){
		return this.element.val();
	},
	_removeLabel: function( e ){
		var inputLi = this.element.closest( "li.coral-textboxlist-inputLi" ),
			curItem = $(e.target).closest( "li.coral-textboxlist-item" ),
			lastItem = $(e.target).closest( "li.coral-textboxlist-item:last" );
		inputLi.insertAfter( lastItem );
		var indexOfInputLi = inputLi.index();
		var indexOfItem = curItem.index();
		var index = indexOfItem;
		if ( indexOfItem > indexOfInputLi ) {
			index = indexOfItem - 1;
		}
		curItem.remove();
		var values = this.valuebox.val()==""?
				[]:this.valuebox.val().split( "," );
		values.splice( index, 1 );
		this.selectedItems.splice( index, 1 );
	/*	
		for( var i = 0; i < values.length; i++ ) {
			for( var j = 0; j < this.selectedItems.length; j++ ) {
				values[ i ] == this.selectedItems[ j ];
			}
		}*/
		//console.log(values, this.selectedItems);
		this.valuebox.val( values.join(",") );
		//this.selectedItems.p();
		this._moveInput( "last", e );
		this.element.val("");
		this.element.focus();
	},
	_moveInput: function( direction, e ){
		var inputLi = this.element.closest( "li.coral-textboxlist-inputLi" );
		var curItem = $(e.target).closest( "li.coral-textboxlist-item" );
		var lastItem = $(inputLi).closest( "li.coral-textboxlist-inputLi" ).nextAll("li.coral-textboxlist-item").last();

		switch ( direction ) {
		case 'left':
			inputLi.insertBefore( curItem );
			this.element.val("");
			this.element.focus();
			break;
		case 'right':
			inputLi.insertAfter( curItem );
			this.element.val("");
			this.element.focus();
			break;
		case 'last':
			inputLi.insertAfter( lastItem );
			this.element.val("");
			break;
		}
	},
	_moveItem: function( direction, e ){
		var inputLi = this.element.closest( "li.coral-textboxlist-inputLi" );
		if ( direction == "left" && inputLi.prev().length && $( inputLi.find( "input" ) ).caret() == 0 ) {
			this.element.val("");
			inputLi.prev().focus();
		}
		if ( direction == "right" && inputLi.next().length && $( inputLi.find( "input" ) ).caret() == inputLi.find( "input" ).val().length ) {
			this.element.val("");
			inputLi.next().focus();
		}
	},
	_setValues: function( value ) {
		this._value(value);
	},
	_value: function( value ) {
		var that = this;
		if ( !value ) {
			return this.valuebox.val();
		}
		var inputLi = this.element.closest( "li.coral-textboxlist-inputLi" ),
			indexOfInputLi = inputLi.index(),
			index = indexOfInputLi,
			values = this.valuebox.val()==""?[]:this.valuebox.val().split( "," );
		
		values.splice(index,0,value);
		
		if ( $.trim( value ) !== "" && $.isArray( this.options.source ) ) {
			var array = $( this.menu.element ).tree("transformToArray", this.options.source),
				texts = [],
				grepArr = $.grep( value.split( "," ), function( _value ) {
					var ret = false;
					$.each(array, function(i){
						if ( _value == array[i][that.options.valueField] ) {
							texts.push(array[i][that.options.textField]);
							return true;
						}
					});
					return ret;
				});
			if ( texts.length ){
				this.setText( texts );
			}
		} else if ( $.trim( value ) == "" ) {
			this.setText(value);
		}
		return this.valuebox.val( values.join( "," ) );
	},
	_text: function( text ) {
		if ( !text ) {
			var texts = [];
			$.each( this.textboxlistUl.find( ".coral-textboxlist-item" ), function(){
				texts.push( $( this ).text() );
			} );
			return texts.join( "," );
		}
		var item = "";
		if ( text instanceof Array ) {
			$.each(text, function(i){
				text.splice(i,1,"<li tabindex='-1' class='coral-textboxlist-item'>"+this+"<span class='coral-label-close icon icon-close2'></span></li>");
			});
			item = text.join("");
		} else {
			item = "<li tabindex='-1' class='coral-textboxlist-item'>"+text+"<span class='coral-label-close icon icon-close2'></span></li>";
		}
		this.textboxlistUl.find( ".coral-textboxlist-inputLi" ).before( item );
		//return this.textMethod.apply( this.element, arguments );
	}
});

var autocomplete = $.coral.autocomplete;
})( jQuery );



/*jslint nomen: true */

/**
 * @fileOverview EasyEdit - A jQuery plugin for the input dom element of type text
 * @author Andres Jorquera
 * @version 0.2
 * @requires jQuery
 * @namespace easyInput.easyText
 * TODO Must set the new values if the storage option changes
 */

;(function ($, window, document, undefined) {
    'use strict';
    
    var NAMESPACE = 'easyInput',
        PLUG_NAME = 'easyText',
        // constant use for events handlers to get the plugin instance using
        //$(this).data(NAME_SPAC_PLUG)
        NAME_SPAC_PLUG = NAMESPACE + PLUG_NAME.charAt(0).toUpperCase() + 
                         PLUG_NAME.slice(1);

    /**
     * Description of the plugin
     * 
     */

    $.component(NAMESPACE + '.' + PLUG_NAME, {

        options: {
            /**
             * The current value of the input element
             * @type String
             * @default ''
             */
            currentValue: '',

            /**
             * A custom array containing values for the input element
             * @type Array
             * @default []
             */
            values: [],

            /**
             * Max width in pixels for the input element.
             * @type Number
             * @default 200
             */
            maxWidth: 200,
            /**
             * Min width for the input element in pixels fo the input element.
             * @type number
             * @default 80
             */
            minWidth: 80,
            /**
             * Maximum number of characters permitted in the input.
             * @type number
             * @default 200
             */
            maxChars: 200,
            /**
             * Type of storage use for the input element.
             * @type string
             * @default 'array'
             */
            storage: 'array',
            /**
             * Configuration options for the ajax requests
             * @type object
             */
            ajaxConfig: {
                url: '/',
                id: '',
                UPDATETemplate: '',
                UPDATEData: {},
                GETCallback: null
            }
        },
        //---------------------------------------------------------------------
        //                          PRIVATE PROPERTIES
        //---------------------------------------------------------------------

        /**
         * Holder for the name of the plugin.
         * @type string
         * @private
         */
        _name: PLUG_NAME,
        /**
         * Holder for the input Jquery object.
         * @type Jquery object
         * @private
         */
        _input:null,
        /**
         * Holder for the id of the input.
         * @type string
         * @private
         */
        _id:null,
        /**
         * Holder for the current position of the current value in the
         * values attribute. It's use by the getNextBackValue public function
         * @type number
         * @private
         */
        _valuesIndex:null,
        /**
         * Holder for the class-name for the 'unChange'. Its the look of the
         * input when it hasn't been modified
         * @type String
         * @private
         */
        _classUnChanged:null,
        /**
         * Holder for the class-name for the 'empty'. The look of the input
         * when it's empty
         * @type String
         * @private
         */
        _classEmpty:null,
        /**
         * Holder for the class-name for the 'onEdit'.The look of the input
         * when it's been edited
         * @type String
         * @private
         */
        _classOnEdit: null,
        /**
         * Holder for the class-name for the "changed". The look of the input
         * when it's has been changed
         * @type String
         * @private
         */
        _classChanged:null,
        /**
         * Holder for the class-name of the input. It will be a class for the
         * input that has the plugin.
         * @type String
         * @private
         */
        _classEasyText:PLUG_NAME.toLowerCase(),

//-----------------------------------------------------------------------------
//                          PRIVATE FUNCTIONS
//-----------------------------------------------------------------------------
        /**
         * Add listeners to the input
         * @private
         */
        _setListeners:function() {
            this._input.bind({
                focus: this._focusBehaviour,
                input: this._autoGrownBehaviour,
                blur: this._blurBehaviour,
                storageChange: this._storageChange
            });
        },

        /**
         * Function that it's inspired in the getClassName function from the YUI
         * widget library.
         * reference: http://yuilibrary.com/yui/docs/api/files/widget_js_Widget.js.html#l309
         * FIXME re-think this function
         * @private
         */
        _getClassName: function(str) {
            return this._name.toLowerCase() + '-' + str;
        },
        /**
         * Sets the name for all the classes
         * @private
         */
        _setClassNames: function() {
            // set classes names
            this._classOnHover = this._getClassName('onHover');
            this._classUnChanged = this._getClassName('unChanged');
            this._classOnEdit = this._getClassName('onEdit');
            this._classChanged = this._getClassName('changed');
            this._classEmpty = this._getClassName('empty');

        },
        /**
         * Sets initial values for private variables
         * @private
         */
        _setVars: function() {
            var input = this.element;

            //save the host node to the private variable
            this._input = input;

            //creates an unique id in the element
            input.uniqueId();

            //saves the current position for the values attribute
            this._valuesIndex = 0;

            //sets the id
            this._id = this._name + '-' + input.attr('id');

            //set the maximum characters allow in the input
            input.attr('maxLength',this.options.maxChars);
        },
        /**
         * Creates a span element with the purpose for measuring the length in
         * pixels.
         * @private
         */
        _setRuler: function() {
            var rulerID    = '#' + this._name + '-ruler',
                ruler      = $(rulerID),
                input      = this._input;

            // check if a ruler already exist
            if (!ruler.length) {
                //NOTE maybe we shouldn't augment the string object
                String.prototype.visualLength = function() {
                    var ruler = $(rulerID);
                    //we need to escaped spaces
                    ruler.html(this.replace(/&/g, '&amp;').replace(/\s/g,'&nbsp;')
                          .replace(/</g, '&lt;').replace(/>/g, '&gt;'));

                    return ruler.width();
                };
                /* It will copy the same font family and size. Must be
                 * careful because if the font doesn't match it will not grown in the
                 * same way as the ruler
                 */
                ruler = $('<span id="' + rulerID.substr(1) + '"></span>');

                ruler.css({
                    fontSize:input.css('fontSize'),
                    fontFamily:input.css('font-family'),
                    fontWeight:input.css('fontWeight'),
                    position:'absolute',
                    visibility:'hidden',
                    //hides the ruler and breaks the text to avoid overflow
                    whiteSpace: 'nowrap'
                });
                $('body').after(ruler);
            }
        },
        /**
         * Prepares the configuration object for the ajax request. 
         * @private
         */
        _initializeAjax: function() {
            var ajaxConfig = this.options.ajaxConfig;
            
            //checks if there's a callback in the configuration object.
            //Do not send request if not
            if (!$.isFunction(ajaxConfig.GETCallback)) return;
            
            if (ajaxConfig.url.charAt(ajaxConfig.url.length - 1) !== '/') {
                ajaxConfig.url += '/'; 
            }
            
            if (!ajaxConfig.id) {
                ajaxConfig.id = this._id;
            }
            
            $.ajax({
                url:ajaxConfig.url + ajaxConfig.id,
                succes:ajaxConfig.GETCallback
            });
        },
        /**
         * Checks and sets the state for the input element 
         * @private
         */
        _setState:function() {
            var input         = this._input,
                text          = input.val(),
                visualLenText = text.visualLength(),
                minWidth      = this.options.minWidth,
                maxWidth      = this.options.maxWidth,
                width;

            if (visualLenText < minWidth) {
                width = minWidth;
            } else if (maxWidth < visualLenText) {
                width = maxWidth;
            } else {
                width = visualLenText;
            }

            input.width(width);

            //set the appropriate classes
            if (input.val().length === 0){
                //TODO do it in one call
                input.removeClass(this._classChanged);
                input.addClass(this._classEmpty);
            } else {
                input.addClass(this._classChanged);
            }
        },

        /**
         * Set the behaviour when the input gets clicked
         * @private
         */
        _focusBehaviour: function() {
            var that = $(this).data(NAME_SPAC_PLUG),
                input = that._input;

            input.removeClass(that._classChanged + ' ' +
                              that._classUnChanged + ' ' +
                              that._classEmpty)
                 .addClass(that._classOnEdit);
        },
        
        /**
         * Function that controls the auto-grown behaviour of the input 
         * @private
         */
        _autoGrownBehaviour: function() {
            var that     = $(this).data(NAME_SPAC_PLUG),
                input    = that._input,
                text     = input.val(),
                minWidth = that.options.minWidth,
                maxWidth = that.options.maxWidth,
                width    = text.visualLength();
                
            if (width < minWidth) {
                width = minWidth;
            } else if (width > maxWidth) {
                width = maxWidth;
            } 

            input.width(width);   
        },
        /**
         * Function that is trigger when the input loses focus. 
         * @private 
         */
        _blurBehaviour: function(){
            var that = $(this).data(NAME_SPAC_PLUG),
                input = that._input,
                value = input.val(),
                ajaxConfig = that.options.ajaxConfig,
                UPDATETemplate;

            input.removeClass(that._classOnEdit);

            //check if the field if empty. Don't store values if it is
            if (value.length === 0) {
                input.addClass(that._classEmpty);

            } else {
                
                input.addClass(that._classChanged);
                that.option('values',value);

                //TODO check this, not sure if it is the best way to do it
                that._valuesIndex = that.option('values').length - 1;

                //ajax feature
                if (that.options.storage === 'ajax') {
                    ajaxConfig.UPDATEData.value = input.val();

                    $.ajax({
                        url: ajaxConfig.url + ajaxConfig.id,
                        data: that._tmpl(ajaxConfig.UPDATETemplate,
                                         ajaxConfig.UPDATEData),
                        type: 'UPDATE'
                    });
                }
            }
            that._setState();
        },

        /**
         * Simple JavaScript Templating
         * John Resig - http://ejohn.org/ - MIT Licensed
         * ref:http://ejohn.org/blog/javascript-micro-templating/
         * @private
         */
        _tmpl : function(str, data) {
            // Figure out if we're getting a template, or if we need to
            // load the template - and be sure to cache the result.
            var fn = typeof(str) !== 'string' ?
               (function(){throw {
                   name: 'Invalid Parameter',
                   message: 'You must insert a string into the function'
               }}()):
             
              // Generate a reusable function that will serve as a template
              // generator (and which will be cached).
              new Function("obj",
                "var p=[],print=function(){p.push.apply(p,arguments);};" +
               
                // Introduce the data as local variables using with(){}
                "with(obj){p.push('" +
               
                // Convert the template into pure JavaScript
                str
                  .replace(/[\r\t\n]/g, " ")
                  .split("<%").join("\t")
                  .replace(/((^|%>)[^\t]*)'/g, "$1\r")
                  .replace(/\t=(.*?)%>/g, "',$1,'")
                  .split("\t").join("');")
                  .split("%>").join("p.push('")
                  .split("\r").join("\\'")
              + "');}return p.join('');");
           
            // Provide some basic currying to the user
            return data ? fn( data ) : fn;
        },
        /**
         * Setter function for the options of the plugin
         * @param {string} key - key string for the option value
         * @param {string} value - value for the option 
         * @private
         */
        _setOption: function(key, value) {
            var options = this.options;

            switch (key) {

            case 'values':
                //adds a new value to the array
                options.values.push(value);

                //saves the value on the local storage if it applies.
                //Add instead of replace. 
                if (options.storage === 'localStorage') {
                    localStorage.setItem(this._id,JSON.stringify(options.values));
                }

                return;
            case 'currentValue':
                this.option('values',value);
                this._input.val(value);
                break;
            }
            this._super("_setOption", key, value);

        },
        /**
         * Function that automatically runs the first time the plugin is 
         * instantiated. 
         * @private
         */
        _create: function () {
            var values,
                input,
                storage = this.options.storage;

            this._setClassNames();
            this._setVars();
            this._setListeners();


            input = this._input;
            //TODO re-think this
            input.addClass(this._classEasyText);

            //check the type of storage
            if (storage === 'localStorage') {

                //Check if there's some initial value in the localStorage
                if (localStorage[this._id]) {
                    values = JSON.parse(localStorage[this._id]);
                    this.options.values = values;

                    input.val(values[values.length - 1]);
                    this._valuesIndex = values.length - 1;
                }

            //ajax feature. Sends a GET request to the the web service.
            //TODO Not sure of this. With server side scripting we can write the
            //     values on the input.
            } else if (storage === 'ajax') {
                this._initializeAjax();
            }

            //create a ruler to measure the text in pixels
            this._setRuler();
            this._setState();
        },
        /**
         * Destroy an instantiated plugin and clean up
         * @private
         */
        _destroy: function () {
            //removes any classes
            this._input.attr('class','');
            //$.Widget.prototype.destroy.call(this);
        },
        
        /**
         * Deletes the storage for all the values. 
         * @public
         */
        deleteStorage: function() {
            var ajaxConfig = this.options.ajaxConfig,
                typeStorage = this.options.storage;

            if (typeStorage === 'localStorage') {
                localStorage.removeItem(this._id);

            } else if (typeStorage === 'ajax') {
                $.ajax({
                    url: ajaxConfig.url + ajaxConfig.id,
                    type: 'DELETE'
                });
            }

            this.options.values = [];
            this._input.val('');
            this._setState();
        },
        /**
         * Get the value store before or after the current value.
         * @param {boolean} direction - Flag use to set the direction of the
         *                              searched value. True is next value
         *                              and false is the previous value.
         * @public
         */
        getNextBackValue:function(direction) {
            var values = this.option('values'),
                index = this._valuesIndex;

            if (direction === undefined) {
                direction = true;
            }

            //check valid direction
            if (typeof(direction) !== 'boolean') {
                throw new Error('Invalid parameter');
            }

            //check if next or back
            index += direction ? 1 : (-1);

            //check if we are in one extreme
            if (values[index] === undefined) return -1;

            this._valuesIndex = index;
            this._input.val(values[index]);
            
            this._setState();
            
            return values[index];    
        }
    });
})(jQuery, window, document );