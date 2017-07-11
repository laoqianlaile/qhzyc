/**
 *	Coral 4.0: toolbar
 *
 *	Depends:
 *		jquery.coral.core.js
 *		jquery.coral.component.js
 *
 */

( function ( $, undefined ) {
	"use strict";	

	$.component ( "coral.toolbar", {
		version: "4.0.1",
		castProperties: ["data","dataCustom"],
		options: {
			// 配置信息
			id: null,
			name: null,
			dataCustom: {},
			url: null, 
			data: null,
			width: null,
			height: null,
			// 事件
			onClick: null
		},
		/**
		 * 获取组件外围元素
		 * @return {object} :  组件外层 Jquery Dom对象
		 */
		component: function () {
			return this.uiToolbar;
		},
		/**
		 * 组件初始化入口
		 * @return ;
		 */
		_create: function () {
			//创建组件外围元素
			this.uiToolbar = $( "<span class=\"coral-toolbar\"></span>" );	
			this._initElement();
			this._bindEvents();
			
			return ;
		},	
		//组件初始化
		_initElement: function () {
			var options = this.options;
			
			if ( options.width !== "" ) {
				this.element.width( options.width );
			}
			if ( options.height !== "" ) {
				this.element.height( options.height );
			}
			//添加组件外围元素
			this.elementBorder =  $( "<span class=\"coral-toolbar-border\"></span>" );
			this.childrenBorder =  $( "<span class=\"coral-toolbar-children-border\"></span>" ).appendTo( document.body );
			this.uiToolbar.append( this.elementBorder );
			this.uiToolbar.insertAfter( this.element );
			this.element.appendTo( this.elementBorder );
			this.element.addClass( "coral-toolbar-default ctrl-init ctrl-init-toolbar" );
			
			if ( options.id !== "" ) {
				this.element.attr( "id", options.id );
			}
			if ( options.name !== "" ) {
				this.element.attr( "name", options.name );
			}
			
			this._loadData();	
			
			return ;
		},
		//加载setting data
		_loadData: function () {
			var that = this,
				options = this.options;
	
			if ( "" !== options.url && null !== options.url ) {
				$.ajax({
					url: options.url,
					data: {},
					async: "false",
					dataType: "json",
					success: function ( data ) {
						that._initInnerElements(data);
					},
					error: function () {  
				        $.error( "Json Format Error!" );
					}
				});
			} else if ( "" !== options.data && null !== options.data ) {
				that._initInnerElements( options.data );
			}
			
			return ;
		},
		//初始化一级元素
		_initInnerElements: function( data ) {
			var that = this;
		
	        if( typeof data == "object" ) {
	            $.each( data, function( i, d ) { 
	            	that._addItem( null, d );
	            });
	        }
		},	
		_addItem: function ( index, dataItem ) {
			var that = this,
				options = this.options,
				d = dataItem,
				idx = null;
			
			if ( index != null ) {
				idx = parseInt( index );
			}
			
			var $btn = $( "<span class=\"coral-toolbar-btn\"></span>" ),
				$btnBorder = $( "<span class=\"coral-toolbar-btn-border coral-corner-all\"></span>" ),
				$btnDropdown = $( "<span class=\"coral-toolbar-btn-dropdown coral-corner-all\"></span>" ),
				$btnText = $( "<span class=\"coral-toolbar-btn-text\"></span>" ),
				$btnIco1 = $( "<span class=\"coral-toolbar-btn-ico icon\"></span>" ),
				$btnIco2 = $( "<span class=\"coral-toolbar-btn-ico icon\"></span>" ),
				$btnIco3 = $( "<span class=\"coral-toolbar-btn-ico icon\"></span>" ),
				$child = $( "<span class=\"coral-toolbar-child\"></span>" ),
	    		$textbox = $( "<input type='text' />" );
	    	//handle level
	    	$child.css({ "z-index": 99 });
	    	//获取icon class
	    	var icon = that._getIcon( d.icon );
	    	
	    	if ( index == null ) {
	    		that.element.append( $btn );
	    	} else if ( 0 == index ) {
	    		that.element.prepend( $btn );
	    	} else {
	    		$btn.insertAfter( that.element.children( ".coral-toolbar-btn" ).eq( idx - 1 ) );
	    	}
	    	
	    	if ( d.id ) {
				$btn.attr( "data-id", d.id );
			} else {
				$btn.attr( "data-id", $btn.uniqueId().attr("id") ).removeAttr("id"); // 如果没有设置data-id，则设置一个data-id
			}
	    	
	    	if ( d.cls ) {
	    		$btn.addClass(d.cls);
	    	}
	    	if ( d.type == "button" ) {
	    		$btn.append( $btnBorder );            		
	    			
	    		if ( icon.ico1 != null ) {
	    			$btnBorder.append( $btnIco1.addClass( icon.ico1 ) );
	    		}
	    		if ( d.label != null ) {
	    			$btnBorder.append( $btnText.html( d.label ) );
	    		}
	    		if ( icon.ico2 != null ) {
	    			$btnBorder.append( $btnIco2.addClass( icon.ico2 ) );
	    		}
	    		if ( d.disabled == "true" ) {
	    			$btn.addClass( "coral-toolbar-disabled" );
	    		}
	    		//bind click
	    		$btnBorder.click( function ( e ) {
						$( this ).parents( ".coral-toolbar-btn:first" ).siblings( ".coral-toolbar-btn" ).find( ".coral-toolbar-child-1,.coral-toolbar-child-2" ).hide();
						
						if ( $( this ).parents( ".coral-toolbar-btn:first" ).hasClass( "coral-toolbar-disabled" ) || $( that.element).hasClass( "coral-state-disabled" ) ) {
							return ;
						}
						// 按钮中自己的单击事件
						if ( d.click != null ) {
							that._apply( d.click );	
							return ;
		         		}
							
						if ( d.id != null && options.onClick != "" ) {
							that._trigger( "onClick", null, [d] );	
							return ;
						}
	 			});
	    		//bind hover
	    		$btn.bind( "mouseenter" + that.eventNamespace, function ( e ) {
	    			if ( $( this ).hasClass( "coral-toolbar-disabled" ) || $( that.element ).hasClass( "coral-state-disabled" ) ) {
						return ;
					}
	    			$btn.addClass( "coral-toolbar-hover" );
	        	});
				$btn.bind( "mouseleave" + that.eventNamespace, function ( e ) {
					$btn.removeClass( "coral-toolbar-hover" );            		
				});
	    	} else if ( d.type == "textbox" ) {
	    		$btn.append( $btnBorder );     
	    		$btnBorder.append( $textbox );
	    		
	    		$btnBorder.css({
	    			"padding": "0px",
	    			"border-width": "0px"
	    		});
	    		if ( !d["type"] ) {
	    			delete d["type"];
	    		}
	    		$textbox.textbox( d );
	    	} else if ( d.type == "split" || d.type == "dropdown" ) {
	    		$btn.append( $btnBorder );
	    		if (d.type == "split") {
	    			$btnBorder.css({"border-top-right-radius":"0","border-bottom-right-radius": "0px"});
	    			$btnDropdown.css({"border-top-left-radius":"0","border-bottom-left-radius": "0px"});
	    			$btn.append( $btnDropdown );
	    			$btnDropdown.append( $btnIco3.addClass( "icon icon-arrow-down2" ) );
	    		}	
	    		
	    		if ( icon.ico1 != null ) {
	    			$btnBorder.append( $btnIco1.addClass( icon.ico1 ) );
	    		}
	    		
	    		if ( d.label != null ) {
	    			$btnBorder.append( $btnText.html( d.label ) );
	    		}
	    		if ( d.type == "dropdown" && icon.ico2 != null ) {
	    			$btnBorder.append( $btnIco2.addClass( icon.ico2 ) );
	    		}
	    		if ( d.disabled == "true" ) {
	    			$btn.addClass( "coral-toolbar-disabled" );
	    		}
	    		//bind click
	    		$btnBorder.click( function ( e ) {
					$( this ).parents( ".coral-toolbar-btn:first" ).siblings( ".coral-toolbar-btn" ).find( ".coral-toolbar-child-1,.coral-toolbar-child-2" ).hide();
					
					if ( $( this ).parents( ".coral-toolbar-btn:first" ).hasClass( "coral-toolbar-disabled" ) || $(that.element).hasClass( "coral-state-disabled" ) ) {
						return ;
					}
					
					if ( d.type == "dropdown") {
						// !!$( this ).parents( ".coral-toolbar-btn:first" ).find( ".coral-toolbar-child-1:first" ).show();
						that._showChildren($btn);
						e.preventDefault();
					    return false;
					}	
					
					if ( d.click != null ) {
						that._apply( d.click );	
						return ;
	         		}
	
					if ( d.id != null && options.onClick != "" ) {
						that._trigger( "onClick", null, [d] );	
						return ;
					}    
				});
	    		if ( d.type == "split" ) {
	    			$btnDropdown.click( function ( e ) {
						$( this ).parents( ".coral-toolbar-btn:first" ).siblings( ".coral-toolbar-btn" ).find( ".coral-toolbar-child-1,.coral-toolbar-child-2" ).hide();
						
						if ( $( this ).parents( ".coral-toolbar-btn:first" ).hasClass( "coral-toolbar-disabled" ) || $(that.element).hasClass( "coral-state-disabled" ) ) {
							return ;
						}
						
						// !!$( this ).parents( ".coral-toolbar-btn:first" ).find( ".coral-toolbar-child-1:first" ).show();	
						that._showChildren($btn);
						e.preventDefault();
					    return false;         			
					});
	    			//bind hover	
	    			$btnBorder.bind( "mouseenter" + that.eventNamespace, function ( e ) {        				
						if ( $( this ).parents( ".coral-toolbar-btn:first" ).hasClass( "coral-toolbar-disabled" ) || $(that.element).hasClass( "coral-state-disabled" ) ) {
							return ;
						}
						$btn.addClass( "coral-toolbar-hover" );
		        	});
					$btnBorder.bind( "mouseleave" + that.eventNamespace, function ( e ) {
						$btn.removeClass( "coral-toolbar-hover" );
					});
					$btnDropdown.bind( "mouseenter" + that.eventNamespace, function ( e ) {
						if ( $( this ).parents( ".coral-toolbar-btn:first" ).hasClass( "coral-toolbar-disabled" ) || $( that.element ).hasClass( "coral-state-disabled" ) ) {
							return ;
						}
						$btn.addClass( "coral-toolbar-hover" );
		        	});
					$btnDropdown.bind( "mouseleave" + that.eventNamespace, function ( e ) {
						$btn.removeClass( "coral-toolbar-hover" );
					});
	    		} else if (d.type="dropdown") {
	    			//bind hover	
	    			$btnBorder.bind( "mouseenter" + that.eventNamespace, function ( e ) {        				
						if ( $( this ).parents( ".coral-toolbar-btn:first" ).hasClass( "coral-toolbar-disabled" ) || $(that.element).hasClass( "coral-state-disabled" ) ) {
							return ;
						}
						$btn.addClass( "coral-toolbar-hover" );
		        	});
					$btnBorder.bind( "mouseleave" + that.eventNamespace, function ( e ) {
						$btn.removeClass( "coral-toolbar-hover" );
					});
	    		}
	    		
	    		$btn.bind( "mouseenter" + that.eventNamespace, function ( e ) {
	    			if ( $( this ).hasClass( "coral-toolbar-disabled" ) || $( that.element ).hasClass( "coral-state-disabled" ) ) {
						return ;
					}
	    			$btn.addClass( "coral-toolbar-hover" );
	    			e.preventDefault();
				    return false;        
	        	});
				$btn.bind( "mouseleave" + that.eventNamespace, function ( e ) {
					$btn.removeClass( "coral-toolbar-hover" ); 
					e.preventDefault();
				    return false;
				});
				
	    		/*$( that.element ).find( ".coral-toolbar-btn" ).css({
	    			"margin-top":  (parseInt(that.options.height) - 30) / 2
	    		});*/
	    		
	    		if ( typeof d.item == "object" ) {
	    			that._initChildren1Elements( d.item, $btn );
	    		}
			}	  
		},
		/**
		 * 显示一级按钮节点的子按钮，弹出
		 * @param $btn {jquery dom object} : 一级按钮节点jquery对象
		 * @return ;
		 */
		_showChildren: function ($btn) {
			this._hideChildren();
			var dataId = $btn.attr("data-id");
			
			this.childrenBorder.find( "[data-p-id='" + dataId + "']" ).css({
					"z-index": ++$.coral.zindex,
					"left": $btn.offset().left,
					"top": $btn.offset().top + $btn.outerHeight()
			}).show();
		},
		/**
		 * 隐藏一级按钮节点的子按钮
		 * @return ;
		 */
		_hideChildren: function () {
			this.childrenBorder.find( "[data-p-id]" ).hide();
		},
		//初始化一级子元素
		_initChildren1Elements: function ( item, $btn ) {
			var that = this,
				$child = $( "<span class=\"coral-toolbar-children coral-toolbar-child-1\"></span>" );
			$child.css({ "z-index": 999 });
			
			//!!$btn.append( $child );
			$child.attr( "data-p-id", $btn.attr("data-id") ).appendTo( this.childrenBorder );
			
	    	$child.bind( "mouseenter" + that.eventNamespace, function ( e ) {
	    		if ( $( that.element ).hasClass( "coral-state-disabled" ) ) {
					return ;
				}
	    		
				$btn.find( ".coral-toolbar-btn-border,.coral-toolbar-btn-dropdown" ).removeClass( "coral-toolbar-hover" );
				return false;
	    	});
			
			$.each( item, function ( i, d ) {
				that._addChild1Item( $child, $btn, d);    		
			});
		},
		_addChild1Item: function ( $child, $btn, dataItem ) {
			var that = this,
				options = this.options,
				d = dataItem;
			
			var $btnBorder = $( "<span class=\"coral-toolbar-btn-border\"></span>" ),
					$btnText = $( "<span class=\"coral-toolbar-btn-text\"></span>" ),
					$btnIco1 = $( "<span class=\"coral-toolbar-btn-ico icon\"></span>" ),
					$btnIco2 = $( "<span class=\"coral-toolbar-btn-ico icon\"></span>" ),				
					$childBtn = $( "<span class=\"coral-toolbar-child-btn\"></span>" );
			//获取icon class
			var icon = that._getIcon( d.icon );
			
			$child.append( $childBtn );
			$childBtn.append( $btnBorder );
			
			if ( icon.ico1 != null ) {
				$btnBorder.append( $btnIco1.addClass( icon.ico1 ) );
			}
			if ( d.label != null ) {
				$btnBorder.append( $btnText.html( d.label ) );
			}
			if ( d.disabled == "true" ) {
				$childBtn.addClass( "coral-toolbar-disabled" );
			}
			$childBtn.bind( "mouseenter" + that.eventNamespace, function ( e ) {
				$btn.find( ".coral-toolbar-btn-border,.coral-toolbar-btn-dropdown" ).removeClass( "coral-toolbar-hover" );
				$( this ).siblings( ".coral-toolbar-child-btn" ).find( ".coral-toolbar-child-2" ).hide();
				
				if ( $( this ).hasClass( "coral-toolbar-disabled" ) || $( that.element ).hasClass( "coral-state-disabled" ) ) {
					return ;
				}    				
				$( this ).find( ".coral-toolbar-child-2:first" ).show();
				$( this ).addClass( "coral-toolbar-hover" );
			});
			$childBtn.bind( "mouseleave" + that.eventNamespace, function ( e ) {
				$( ".coral-toolbar-child-btn" ).find( ".coral-toolbar-child-2" ).hide();
				$( this ).removeClass( "coral-toolbar-hover" );
			});
			
			if ( d.id != "undefined" ) {
				$childBtn.attr( "data-id", d.id );
			}
	    	if ( d.cls != "undefined" ) {
	    		$childBtn.addClass(d.cls);
	    	}
			if ( typeof d.item == "object" ) {
				$btnBorder.append( $btnIco2.addClass( "icon-arrow-right3" ) );
		    	
				that._initChildren2Elements( d.item, $childBtn );
			} else if ( d.type == "button" ) {
				if ( icon.ico2 != null ) {
					$btnBorder.append( $btnIco2.addClass( icon.ico2 ) );
				}
				
				$btnBorder.click( function ( e ) {
					if ( $( this ).parents( ".coral-toolbar-child-btn:first" ).hasClass( "coral-toolbar-disabled" ) || $(that.element).hasClass( "coral-state-disabled" ) ) {
						return ;
					}
					
					if ( d.click != null ) {
						that._apply( d.click );	
						return ;
					}
		
					if ( d.id != null && options.onClick != "" ) {
						that._trigger( "onClick", null, [d] );	
						return ;
					}
				});    			
			}
		},
		//初始化二级/三级/四级...子元素
		_initChildren2Elements: function ( item, $child ) {
			var that = this,
				$child2 = $( "<span class=\"coral-toolbar-children coral-toolbar-child-2\"></span>" );
				$child.append( $child2 );
			$child2.css({ "z-index": 9999 });
			
			$child2.bind( "mouseenter" + that.eventNamespace, function (e) {
	    		if ( $( that.element ).hasClass( "coral-state-disabled" ) ) {
					return ;
				}
	    		
	    		$child.removeClass( "coral-toolbar-hover" );
				return false;
	    	});
			
			$.each( item, function( i, d ) {
				that._addChild2Item( $child2, $child, d );	
			});
		},
		_addChild2Item: function ( $child2, $child, dataItem ) {
			var that = this,
				options = this.options,
				d = dataItem;
				
			var $btnBorder = $( "<span class=\"coral-toolbar-btn-border\"></span>" ),
				$btnText = $( "<span class=\"coral-toolbar-btn-text\"></span>" ),
				$btnIco1 = $( "<span class=\"coral-toolbar-btn-ico icon\"></span>" ),
				$btnIco2 = $( "<span class=\"coral-toolbar-btn-ico icon\"></span>" ),				
				$childBtn = $( "<span class=\"coral-toolbar-child-btn\"></span>" );
			//获取icon class
			var icon = that._getIcon( d.icon );
			
			$child2.append( $childBtn );
			$childBtn.append( $btnBorder );			
			
			if ( icon.ico1 != null ) {
				$btnBorder.append( $btnIco1.addClass( icon.ico1 ) );
			}
			if ( d.label != null ) {
				$btnBorder.append( $btnText.html( d.label ) );
			}
			if ( d.disabled == "true" ) {
				$childBtn.addClass( "coral-toolbar-disabled" );
			}
			
			$childBtn.bind( "mouseenter" + that.eventNamespace, function ( e ) {
				$child.removeClass( "coral-toolbar-hover" );
				$( this ).siblings( ".coral-toolbar-child-btn" ).find( ".coral-toolbar-child-2" ).hide();
				
				if ( $( this ).hasClass( "coral-toolbar-disabled" ) || $(that.element).hasClass( "coral-state-disabled" ) ) {
					return ;
				}    	
				
				$( this ).find( ".coral-toolbar-child-2:first" ).show();
				$( this ).addClass( "coral-toolbar-hover" );
			});
			$childBtn.bind( "mouseleave" + that.eventNamespace, function ( e ) {
				$( this ).removeClass( "coral-toolbar-hover" );
				$child.addClass( "coral-toolbar-hover" );
			}); 
			
			if ( d.id != "undefined" ) {
				$childBtn.attr( "data-id", d.id );
			}
			if ( d.cls != "undefined" ) {
				$childBtn.addClass(d.cls);
	    	}
			if ( typeof d.item == "object" ) {
				$btnBorder.append( $btnIco2.addClass( "icon-arrow-right3" ) );
		    	$btnBorder.click( function ( e ) {
		   			 $( this ).parents( ".coral-toolbar-child-btn:first" ).siblings( ".coral-toolbar-child-btn" ).find( ".coral-toolbar-child-2" ).hide();
					 
		   			 if ( $( this ).parents( ".coral-toolbar-child-btn:first" ).hasClass( "coral-toolbar-disabled" ) || $(that.element).hasClass( "coral-state-disabled" ) ) {
		   				 return ;
		   			 }
		   			
		   			 $( this ).parents( ".coral-toolbar-child-btn:first" ).find( ".coral-toolbar-child-2:first" ).show();
					 e.preventDefault();
					 return false;
		    	});
				
				that._initChildren2Elements( d.item, $childBtn );
			} else if ( d.type == "button" ) {
				if ( icon.ico2 != null ) {
					$btnBorder.append( $btnIco2.addClass( icon.ico2 ) );
				}
				
				$btnBorder.click( function ( e ) {
					if ( $( this ).parents( ".coral-toolbar-child-btn:first" ).hasClass( "coral-toolbar-disabled" ) || $(that.element).hasClass( "coral-state-disabled" ) ) {
						return ;
					}
					
					if ( d.click != null ) {
						that._apply( d.click );	
						return ;
		     		}
		
					if ( d.id != null && options.onClick != "" ) {
						that._trigger( "onClick", null, [d] );	
						return ;
					}
				});
			}		
		},
		//获取ico的两个class
		_getIcon: function ( icoStr ) {
			var ico = { ico1: null, ico2: null },
				icoArray = [],
				icoTrim;
			
			if ( icoStr == null ) {
				return ico;
			}
			
			icoTrim = $.trim( icoStr );
			if ( icoTrim.indexOf( "," ) >= 0 ) {
				icoArray = icoTrim.split( "," );
				
				ico.ico1 = icoArray[0] == "" ? null : icoArray[0];
				ico.ico2 = icoArray[1] == "" ? null : icoArray[1];
			} else {
				ico.ico1 = icoTrim;
			}
			
			return ico;
		},
		//callback click eventHandler
		_apply: function ( click ) {
			var that = this,
				_fn = $.coral.toFunction(click);
			
			if ( _fn != null ) {
				return _fn.apply( that.element[0], []);	
			}						
		},	
		_bindEvents: function () {
			var that = this;
			//hide children
			$( document ).bind( "click" + this.eventNamespace, function ( e ) {
				/*!! var $el = $( e.target );
		        if ( !$el.hasClass( "coral-toolbar-btn" ) && !$el.hasClass( "coral-toolbar-child-btn" ) && !$el.hasClass( "coral-toolbar-btn-ico" ) && !$el.hasClass( "coral-toolbar-btn-text" ) ) {
		        	$( that.element ).find( ".coral-toolbar-child-1,.coral-toolbar-child-2" ).hide();
		        }*/
		        that._hideChildren();
		    });  
		},
		//设置属性处理
		_setOption: function ( key, value ) {
			//默认属性不允许更改
			if (key === "id" || key === "name" ) {
				return;
			}
			this._super( key, value );
			 		
			if ( key === "width" ) {
				if ( value ) {
					this.element.width( value );
				}			
			}
			if ( key === "height" ) {
				if ( value ) {
					this.element.height( value );
				}
			}			
		},
		//获取toolbar 一级按钮数
		getLength: function () {
			return this.element.children( ".coral-toolbar-btn" ).length;
		},
		//根据id判断是否存在
		isExist: function ( id ) {
			var nodes = this.element.find( ".coral-toolbar-btn,.coral-toolbar-child-btn" ).filter( "[data-id$='" + id + "']" ),
					childrenNodes = this.childrenBorder.find( ".coral-toolbar-child-btn" ).filter( "[data-id$='" + id + "']" );
			
			if ( nodes.length || childrenNodes.length) {
				return true;
			}
					
			return false;
		},
		_addByIndex: function ( index, dataItem ) {
			var that = this,
				idx = parseInt( index );
			
			if ( (that.getLength() != 0) && (this.element.hasClass( "coral-state-disabled" ) || idx < 0 || idx > that.getLength() ) ) {				
				return false;
			}
			
			this._addItem( index, dataItem );		
			return true;
		},
		_addByParentId: function ( pid, dataItem ) {
			var nodes = this.element.find( ".coral-toolbar-btn" ).filter( "[data-id$='" + pid + "']" ),
					childrenNodes = this.childrenBorder.find( ".coral-toolbar-child-btn" ).filter( "[data-id$='" + pid + "']" ),
					node = null;
			
			if ( this.element.hasClass( "coral-state-disabled" ) ) {
				return false;
			}
			if ( nodes.length ) {
				node = nodes;
			} else if ( childrenNodes.length ) {
				node = childrenNodes;
			} 
			
			/*if ( parentNode.hasClass( "coral-toolbar-children" ) ) {
				var childBtn = nodes.children( ".coral-toolbar-children" );
				if ( childBtn.length <= 0 ) {
					var $btnIco2 = $( "<span class=\"coral-toolbar-btn-ico icon icon-arrow-right3\"></span>" );
					if ( nodes.find( ".coral-toolbar-btn-text:first" ).next( ".coral-toolbar-btn-ico" ).length <= 0 ) {
						$btnIco2.insertAfter( nodes.find( ".coral-toolbar-btn-text" ) );
					} else {
						nodes.find( ".coral-toolbar-btn-text" ).next( ".coral-toolbar-btn-ico" ).replaceWith( $btnIco2 );
					}
					this._initChildren2Elements( [dataItem], nodes );
				} else {
					this._addChild2Item( childBtn, nodes, dataItem );
				}	
			} else {
				var childBtn = nodes.children( ".coral-toolbar-children" );
				if ( childBtn.length <= 0 ) {
					this._initChildren1Elements( [dataItem], nodes );
				} else {
					this._addChild1Item( childBtn, nodes, dataItem );
				}			
			}*/
			node = $( node[0] );
			if ( node.hasClass("coral-toolbar-child-btn") ) {
				var childBtn = node.children( ".coral-toolbar-children" );
				if ( childBtn.length <= 0 ) {
					var $btnIco2 = $( "<span class=\"coral-toolbar-btn-ico icon icon-arrow-right3\"></span>" );
					if ( node.find( ".coral-toolbar-btn-text:first" ).next( ".coral-toolbar-btn-ico" ).length <= 0 ) {
						$btnIco2.insertAfter( node.find( ".coral-toolbar-btn-text" ) );
					} else {
						node.find( ".coral-toolbar-btn-text" ).next( ".coral-toolbar-btn-ico" ).replaceWith( $btnIco2 );
					}
					this._initChildren2Elements( [dataItem], node );
				} else {
					this._addChild2Item( childBtn, node, dataItem );
				}	
			} else if ( node.hasClass("coral-toolbar-btn") ) {
				var childBtn = this.childrenBorder.find( "[data-p-id='" + pid + "']" );
				if ( childBtn.length <= 0 ) {
					this._initChildren1Elements( [dataItem], node );
				} else {
					this._addChild1Item( childBtn, node, dataItem );
				}		
			}
			
			return true;
		},
		//添加toolbar item: key - index (0 ~ length) / parentId
		add: function ( key, dataItem ) {
			var that = this;
			
			if ( typeof key === "string" ) {
				return that._addByParentId( key, dataItem );
			} else {
				return that._addByIndex( key, dataItem );
			}
		},
		/**
		 * 根据id获取node
		 * @param id {string} : 对应node的data-id属性
		 * @return 找到，则返回$node，否则，返回null；
		 */
		_getNodeById: function ( id ) {
			var node = null,
					nodes = this.element.find( ".coral-toolbar-btn" ).filter( "[data-id$='" + id + "']" ),
					childrenNodes = this.childrenBorder.find( ".coral-toolbar-child-btn" ).filter( "[data-id$='" + id + "']" );
			
			if ( nodes.length ) {
				node = nodes;
			} else if ( childrenNodes.length ) {
				node = childrenNodes;
			} else {
				return null;
			}
			
			return $( node[0] );
			
		},
		//删除指定id的按钮
		_removeById: function ( id ) {
			if ( this.element.hasClass( "coral-state-disabled" ) ) {
				return false;
			}
			
			var nodeRemove = this._getNodeById( id );
			
			if ( nodeRemove ) {
				nodeRemove.remove();
				if ( nodeRemove.hasClass("coral-toolbar-btn") ) {
					this._removeChildren( id );
				} 
			}
			
			return true;
		},
		/**
		 *  根据parent id（仅限于第一层的node date-id），删除其子元素
		 * @param pid {string} : 要删除的子元素的父id
		 * @return ;
		 */
		_removeChildren: function ( pid ) {
			this.childrenBorder.children().filter( "[data-p-id$='" + pid + "']" ).remove();
		},
		/**
		 * 根据index（仅限于第一层），删除相应的节点
		 * @param index {number} : 第一层节点按钮的索引
		 * @return boolean : 如果传入的index不合适，或者toolbar禁用状态，则返回false，否则返回true
		 */
		_removeByIndex: function ( index ) {
			var children = this.element.children( ".coral-toolbar-btn" ),
					idx = parseInt( index ),
					nodeRemove = null;
			
			if ( idx < 0 || idx > ( children.length - 1 ) || this.element.hasClass( "coral-state-disabled" ) ) {
				return false;
			}
			
			nodeRemove = children.eq( idx );			
			nodeRemove.remove();
			this._removeChildren( nodeRemove.attr("data-id") );
			return true;
		},	
		//删除toolbar item: key - index / id
		remove: function ( key ) {
			var that = this;
			
			if ( typeof key === "string" ) {
				return that._removeById( key );
			} else {
				return that._removeByIndex( key );
			}
			
		},   
		_updateById: function ( id, label ) {
			var nodesUpdate = this._getNodeById( id );
			
			if ( !nodesUpdate || this.element.hasClass( "coral-state-disabled" ) ) {
				return false;
			}
			
			nodesUpdate.find( ".coral-toolbar-btn-text:first" ).html( label );	
			return true;
		},
		_updateByIndex: function ( index, label ) {
			var children = this.element.children( ".coral-toolbar-btn" ),
					idx = parseInt( index );
			
			if ( idx < 0 || idx > ( children.length - 1 ) || this.element.hasClass( "coral-state-disabled" ) ) {
				return false;
			}
			
			children.eq( idx ).find( ".coral-toolbar-btn-text:first" ).html( label );
			return true;
		},
		//更新toolbar item: key - index / id
		update: function ( key, label ) {
			var that = this;
			
			if ( typeof key === "string" ) {
				return that._updateById( key, label );
			} else {
				return that._updateByIndex( key, label );
			}
		},
		_disableItemById: function ( id ) {
			var nodesDisable = this._getNodeById( id );
			
			if ( !nodesDisable || this.element.hasClass( "coral-state-disabled" ) ) {
				return false;
			}
			
			nodesDisable.removeClass( "coral-toolbar-disabled" ).addClass( "coral-toolbar-disabled" );	
			return true;
		},
		_disableItemByIndex: function ( index ) {
			var children = this.element.children( ".coral-toolbar-btn" ),
					idx = parseInt( index );
			
			if ( idx < 0 || idx > ( children.length - 1 ) || this.element.hasClass( "coral-state-disabled" ) ) {
				return false;
			}
			
			children.eq( idx ).removeClass( "coral-toolbar-disabled" ).addClass( "coral-toolbar-disabled" );
			
			return true;
		},
		//禁用所有的toolbar button
		disable: function () {
			//this.element.addClass( "coral-state-disabled" );
			this.element.find( ".coral-toolbar-btn" ).addClass("coral-toolbar-disabled");
			this.childrenBorder.find( ".coral-toolbar-child-btn" ).addClass("coral-toolbar-disabled");
			this.element.find(".coral-textbox-default").textbox("disable");
		},
		//启用所有的toolbar button
		enable: function () {
			//this.element.removeClass( "coral-state-disabled" );
			this.element.find(".coral-textbox-default").textbox("enable");
			// 同时启用所有被禁用的按钮
			this.element.find(".coral-toolbar-disabled").removeClass("coral-toolbar-disabled");
			this.childrenBorder.find(".coral-toolbar-disabled").removeClass("coral-toolbar-disabled");
		},
		//禁用toolbar item: key - index / id
		disableItem: function ( key ) {
			var that = this;
			
			if ( typeof key === "string" ) {
				return that._disableItemById( key );
			} else {
				return that._disableItemByIndex( key );
			}
		},
		//启用toolbar item: key - index / id
		enableItem: function ( key ) {
			var that = this;
			
			if (typeof key === "string" ) {
				return that._enableItemById( key );
			} else {
				return that._enableItemByIndex( key );
			}
		},
		_enableItemById: function ( id ) {
			var nodesEnable = this._getNodeById( id );
			
			if ( !nodesEnable || this.element.hasClass( "coral-state-disabled" ) ) {
				return false;
			}
			 
			nodesEnable.removeClass( "coral-toolbar-disabled" );
			return true;
		},
		_enableItemByIndex: function ( index ) {
			var children = this.element.children( ".coral-toolbar-btn" ),
					idx = parseInt( index );
			
			if ( idx < 0 || idx > (children.length - 1) || this.element.hasClass( "coral-state-disabled" ) ) {
				return false;
			}
			
			children.eq( idx ).removeClass( "coral-toolbar-disabled" );
			
			return true;
		},
		_hideById: function ( id ) {
			var nodesHidden = this._getNodeById( id );
			
			if ( !nodesHidden || this.element.hasClass( "coral-state-disabled" ) ) {
				return false;
			}
			
			nodesHidden.removeClass( "coral-toolbar-hidden" ).addClass( "coral-toolbar-hidden" );
			return true;
		},
		_hideByIndex: function ( index ) {
			var children = this.element.children( ".coral-toolbar-btn" ),
					idx = parseInt( index );
			
			if ( idx < 0 || idx > ( children.length - 1 ) || this.element.hasClass( "coral-state-disabled" ) ) {
				return false;
			}
			
			children.eq( idx ).removeClass( "coral-toolbar-hidden" ).addClass( "coral-toolbar-hidden" );
			
			return true;
		},
		//隐藏toolbar item: key - index / id
		hide: function ( key ) {
			var that = this;
			
			if ( typeof key === "string" ) {
				return that._hideById( key );
			} else {
				return that._hideByIndex( key );
			}
		},
		_showById: function ( id ) {
			var nodesShow = this._getNodeById( id );
			
			if ( !nodesShow || this.element.hasClass( "coral-state-disabled" ) ) {
				return false;
			}
			
			nodesShow.removeClass( "coral-toolbar-hidden" );		
			return true;		
		},
		_showByIndex: function ( index ) {
			var children = this.element.children( ".coral-toolbar-btn" ),
					idx = parseInt( index );
			
			if ( idx < 0 || idx > ( children.length - 1 ) || this.element.hasClass( "coral-state-disabled" ) ) {
				return false;
			}
			
			children.eq(idx).removeClass( "coral-toolbar-hidden" );
			
			return true;
		},
		//显示toolbar item: key - index / id
		show: function ( key ) {
			var that = this;
			
			if (typeof key === "string" ) {
				return that._showById(key);
			} else {
				return that._showByIndex(key);
			}
		}
	});
	
	$.fn['toolbar'].defaults = {
		width: 'auto'
	};

})(jQuery);