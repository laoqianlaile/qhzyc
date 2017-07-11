/**
 * 组件库4.0：布局
 * 
 * 依赖JS文件:
 *  jquery.coral.core.js
 *  jquery.coral.component.js
 *  jquery.coral.parser.js
 *  jquery.coral.panel.js
 *	jquery.coral.resizable.js
 */
(function ($) {
"use strict";
	
$.component("coral.layout", {
	version: "4.0.1",
	
	options:{
		onCreate: null,
		fit : false
	},
	layoutPanelDefault : {
		region : null,
		split : false,
		collapsedSize : 28,
		minWidth : 10,
		minHeight : 10,
		maxWidth : 10000,
		maxHeight : 10000
	},	
	_flag : false,//当分割条在被拖动时,值为true
	
	 _refresh : function(){
		var that = this;
		var el = this.element;
		
		var layoutOptions = that.options;
		var layoutPanels = that.panels;
		var cc = $(el);
		if (el.tagName == "BODY") {
			this._fit();
		} else {
			layoutOptions.fit ? cc.css(this._fit()) : this._fit(false);
		}
		//设置中央panel的默认尺寸大小
		var centerPanelSize = {
			top : 0,
			left : 0,
			width : cc.width(),
			height : cc.height()
		};
		_setNSPanelSize(this._isPanelVisible(layoutPanels.expandNorth) ? layoutPanels.expandNorth : layoutPanels.north, "n");
		_setNSPanelSize(this._isPanelVisible(layoutPanels.expandSouth) ? layoutPanels.expandSouth : layoutPanels.south, "s");
		_setEWPanelSize(this._isPanelVisible(layoutPanels.expandEast) ? layoutPanels.expandEast : layoutPanels.east, "e");
		_setEWPanelSize(this._isPanelVisible(layoutPanels.expandWest) ? layoutPanels.expandWest : layoutPanels.west, "w");
		layoutPanels.center.panel("resize", centerPanelSize);
		function _getHeight(pp) {
			var _c = pp.panel("getOptions");
			return Math.min(Math.max(_c.height, _c.minHeight), _c.maxHeight);
		};
		function _getWidth(pp) {
			var _e = pp.panel("getOptions");
			return Math.min(Math.max(_e.width, _e.minWidth), _e.maxWidth);
		};
		//设置南、北panel尺寸大小
		function _setNSPanelSize(pp, dir) {
			if (!pp.length || !that._isPanelVisible(pp)) {
				return;
			}
			var panelOptions = pp.panel("getOptions");
			var pHeight = _getHeight(pp);
			pp.panel("resize", {
				width : cc.width(),
				height : pHeight,
				left : 0,
				top : (dir == "n" ? 0 : cc.height() - pHeight)
			});
			centerPanelSize.height -= pHeight;
			if (dir == "n") {
				centerPanelSize.top += pHeight;
				if (!panelOptions.split && panelOptions.border) {
					centerPanelSize.top--;
				}
			}
			if (!panelOptions.split && panelOptions.border) {
				centerPanelSize.height++;
			}
		};
		//设置东、西panel尺寸大小
		function _setEWPanelSize(pp, dir) {
			if (!pp.length || !that._isPanelVisible(pp)) {
				return;
			}
			var panelOptions = pp.panel("getOptions");
			var pWidth = _getWidth(pp);
			pp.panel("resize", {
				width : pWidth,
				height : centerPanelSize.height,
				left : (dir == "e" ? cc.width() - pWidth : 0),
				top : centerPanelSize.top
			});
			centerPanelSize.width -= pWidth;
			if (dir == "w") {
				centerPanelSize.left += pWidth;
				if (!panelOptions.split && panelOptions.border) {
					centerPanelSize.left--;
				}
			}
			if (!panelOptions.split && panelOptions.border) {
				centerPanelSize.width++;
			}
		};
	},

	_create : function () {
		//layout对象初始化五个方向的panel对象
		this.panels = {
						center : $(),
						north : $(),
						south : $(),
						east : $(),
						west : $()
					};
		var that = this;
		var el = this.element;
		this.element.addClass( "ctrl-init ctrl-init-layout" );
		var cc = $(el);
		cc.addClass("coral-layout");
		//为layout中各个方向的div初始化panel
		function _addLayoutPanel(cc) {
			cc.children("div").each(function() {
				var options = $.parser.parseOptions(this,[]);
				if ("north,south,east,west,center".indexOf(options.region) >= 0) {
					that._add(el, options, this);
				}
			});
		};
		cc.children("form").length ? _addLayoutPanel(cc.children("form")) : _addLayoutPanel(cc);
		//layout中添加竖向和横向分割条div
		cc.append("<div class=\"coral-layout-split-proxy-h\"></div><div class=\"coral-layout-split-proxy-v\"></div>");
		//刷新layout中各个panel的尺寸大小
		this._refresh();
		//初始化panel的折叠
		this._initCollapse();
	},
	//为layout中的各个div(region)创建panel
	_add : function (el, options, ele) {
		var that = this;
		options.region = options.region || "center";
		//layout的所有panel对象集合
		var panels = this.panels;
		//layout元素对象
		var cc = $(el);
		var dir = options.region;
		if (panels[dir].length) {
			return;
		}
		//panel元素对象
		var pp = $(ele);
		if (!pp.length) {
			pp = $("<div></div>").appendTo(cc);
		}
		//创建panel的options属性
		var setting = $.extend( {}, this.layoutPanelDefault, {
			width : (pp.length ? parseInt(pp[0].style.width) || pp.outerWidth()
					: "auto"),
			height : (pp.length ? parseInt(pp[0].style.height)
					|| pp.outerHeight() : "auto"),
			doSize : false,
			collapsible : true,
			componentCls : ("coral-layout-panel coral-layout-panel-" + dir),
			bodyCls : "coral-layout-body",
			//初始化时展开的panel，onOpen回调中绑定_collapse方法
			onOpen : function() {
				var panelToolDiv = $(this).panel("header").children("div.coral-panel-tool");
				panelToolDiv.children("a.coral-panel-tool-collapse").hide();
				var region = {
					north : "up",
					south : "down",
					east : "right2",
					west : "left"
				};
				if (!region[dir]) {
					return;
				}
				//panel上工具条中的展开折叠箭头css
				var arrows = "icon-arrow-" + region[dir];
				var t = panelToolDiv.children("a." + arrows);
				if (!t.length) {
					t = $("<a href=\"javascript:void(0)\"></a>").addClass("icon " + arrows)
							.appendTo(panelToolDiv);
				}
				t.unbind("click").bind("click", {
					dir : dir
				}, function(e) {
					that._collapse(e.data.dir);
					return false;
				});
				/*
				//增加分割条上的箭头
				arrows = "coral-layout-arrow-" + region[dir];
				var panelDiv = $(this).panel("component");
				var panelBodyDiv = $(this).panel("body");
				var tt = panelBodyDiv.next("div.bar");
				if(!tt.length){
					tt = $("<div class=\"bar\"></div>").appendTo(panelDiv);
					$("<a href=\"javascript:void(0)\"></a></div>").addClass(arrows).appendTo(tt);
					tt.bind("click", {
						dir : dir
					}, function(e) {
						that._collapse(e.data.dir);
						return false;
					});
				}
				*/
				
				$(this).panel("getOptions").collapsible ? t.show() : t.hide();
			}
		}, options);
		//创建panel
		pp.panel(setting);
		panels[dir] = pp;
		//panel之间是否有分割条
		if (pp.panel("getOptions").split) {
			var panelObj = pp.panel("component");
			panelObj.addClass("coral-layout-split-" + dir);
			var _region = "";
			if (dir == "north") {
				_region = "s";
			}
			if (dir == "south") {
				_region = "n";
			}
			if (dir == "east") {
				_region = "w";
			}
			if (dir == "west") {
				_region = "e";
			}
			/*
			 * 分割条拖动事件：
			 * 使用resizable组件使得panel组件可拖放大小
			 * layout : true:告知resizable组件中_mouseDrag方法在拖动时不改变panel的大小，_mouseStop时才改变panel的大小
			*/
			panelObj.resizable($.extend({
				handles : _region,
				start : function(e) {
					that._flag = true;
					if (dir == "north" || dir == "south") {
						var lSplit = $( ">div.coral-layout-split-proxy-v", el );
					} else {
						var lSplit = $( ">div.coral-layout-split-proxy-h", el );
					}
					var top = 0;
					var pos = {
						display : "block"
					};
					if (dir == "north") {
						pos.top = parseInt( panelObj.css("top") ) + panelObj.outerHeight() - lSplit.height();
						pos.left = parseInt( panelObj.css("left"));
						pos.width = panelObj.outerWidth();
						pos.height = lSplit.height();
					} else {
						if (dir == "south") {
							pos.top = parseInt( panelObj.css("top"));
							pos.left = parseInt( panelObj.css("left"));
							pos.width = panelObj.outerWidth();
							pos.height = lSplit.height();
						} else {
							if (dir == "east") {
								pos.top = parseInt(panelObj.css("top")) || 0;
								pos.left = parseInt(panelObj.css("left")) || 0;
								pos.width = lSplit.width();
								pos.height = panelObj.outerHeight();
							} else {
								if (dir == "west") {
									pos.top = parseInt(panelObj.css("top")) || 0;
									pos.left = panelObj.outerWidth()- lSplit.width();
									pos.width = lSplit.width();
									pos.height = panelObj.outerHeight();
								}
							}
						}
					}
					lSplit.css(pos);
					$( "<div class=\"coral-layout-mask\"></div>").css( {
						left : 0,
						top : 0,
						width : cc.width(),
						height : cc.height()
					}).appendTo(cc);
				},
				resize : function(e) {
					if ( dir == "north" || dir == "south" ) {
						var lSplit = $( ">div.coral-layout-split-proxy-v", el );
						lSplit.css( "top", e.pageY - $(el).offset().top - lSplit.height() / 2 );
					} else {
						var lSplit = $( ">div.coral-layout-split-proxy-h", el );
						lSplit.css( "left", e.pageX - $(el).offset().left - lSplit.width() / 2 );
					}
					return false;
				},
				helper: "coral-resizable-helper",
				stop : function(e,ui) {
					cc.children( "div.coral-layout-split-proxy-v,div.coral-layout-split-proxy-h" ).hide();
					pp.panel( "resize", ui.size );
					that._refresh();
					that._flag = false;
					cc.find( ">div.coral-layout-mask" ).remove();
				}
			}, options));
							
		};
	},
	
	//删除region对应的panel
	_remove : function (region) {
		var panels = this.panels;
		//删除panel
		if (panels[region].length) {
			panels[region].panel("destroy");
			// added at 20141106
			$(panels[region]).remove();
			//
			panels[region] = $();
			var expandRegion = "expand" + region.substring(0, 1).toUpperCase()
					+ region.substring(1);
			//如果有折叠panel也一起删除
			if (panels[expandRegion]) {
				panels[expandRegion].panel("destroy");
				$(panels[region]).remove();
				panels[expandRegion] = undefined;
			}
		}
	},
	//折叠panel(折叠的panel与展开的panel是两个div)
	_collapse : function (region, animateSpeed) {
		var that = this;
		var el = this.element;
		
		if (animateSpeed == undefined) {
			animateSpeed = "normal";
		}
		var panels = this.panels;
		var p = panels[region];
		var panelOptions = p.panel("getOptions");
		if (panelOptions.beforeCollapse.call(p) == false) {
			return;
		}
		
		var expandRegion = "expand" + region.substring(0, 1).toUpperCase()
				+ region.substring(1);
		if (!panels[expandRegion]) {
			panels[expandRegion] = _createCollapsedPanel(region);
			//为折叠的panel绑定点击展开事件
			panels[expandRegion].panel("component").bind(
					"click",
					function() {
						var styleC = _getStyleC();
						p.panel("expand", false).panel("open").panel("resize",
								styleC.collapse);
						p.panel("component").animate(
								styleC.expand,
								function() {
									$(this).unbind(".layout").bind(
											"mouseleave.layout", {
												region : region
											}, function(e) {
												//当分割条在被拖动时，鼠标离开panel不触发折叠事件
												if (that._flag == true) {
													return;
												}
												that._collapse(e.data.region);
											});
								});
						return false;
					});
		}
		var styleC = _getStyleC();
		if (!this._isPanelVisible(panels[expandRegion])) {
			panels.center.panel("resize", styleC.resizeC);
		}
		//折叠当前panel的动画效果
		p.panel("component").animate(styleC.collapse, animateSpeed, function() {
			p.panel("collapse", false).panel("close");
			panels[expandRegion].panel("open").panel("resize", styleC.expandP);
			$(this).unbind(".layout");
		});
		//创建折叠后的panel
		function _createCollapsedPanel(dir) {
			var iconClass;
			if (dir == "east") {
				iconClass = "icon icon-arrow-left";
			} else {
				if (dir == "west") {
					iconClass = "icon icon-arrow-right2";
				} else {
					if (dir == "north") {
						iconClass = "icon icon-arrow-down";
					} else {
						if (dir == "south") {
							iconClass = "icon icon-arrow-up";
						}
					}
				}
			}
			var p = $("<div></div>").appendTo(el);
			p.panel($.extend( {}, that.layoutPanelDefault, {
				componentCls : ("coral-layout-expand coral-layout-expand-" + dir),
				title : "&nbsp;",
				closed : true,
				minWidth : 0,
				minHeight : 0,
				doSize : false,
				tools : [ {
					iconCls : iconClass,
					handler : function() {
						//折叠后的panel，需要绑定_expand方法
						that._expand(region);
						return false;
					}
				} ]
			}));
			p.panel("component").hover(function() {
				$(this).addClass("coral-layout-expand-over");
			}, function() {
				$(this).removeClass("coral-layout-expand-over");
			});
			return p;
		};
		//获得尺寸大小及位置
		function _getStyleC() {
			var cc = $(el);
			var centerOpt = panels.center.panel("getOptions");
			var collapsedSize = panelOptions.collapsedSize;
			if (region == "east") {
				var ww = centerOpt.width + panelOptions.width - collapsedSize;
				if (panelOptions.split || !panelOptions.border) {
					ww++;
				}
				return {
					resizeC : {
						width : ww
					},
					expand : {
						left : cc.width() - panelOptions.width
					},
					expandP : {
						top : centerOpt.top,
						left : cc.width() - collapsedSize,
						width : collapsedSize,
						height : centerOpt.height
					},
					collapse : {
						left : cc.width(),
						top : centerOpt.top,
						height : centerOpt.height
					}
				};
			} else {
				if (region == "west") {
					var ww = centerOpt.width + panelOptions.width - collapsedSize;
					if (panelOptions.split || !panelOptions.border) {
						ww++;
					}
					return {
						resizeC : {
							width : ww,
							left : collapsedSize - 1
						},
						expand : {
							left : 0
						},
						expandP : {
							left : 0,
							top : centerOpt.top,
							width : collapsedSize,
							height : centerOpt.height
						},
						collapse : {
							left : -panelOptions.width,
							top : centerOpt.top,
							height : centerOpt.height
						}
					};
				} else {
					if (region == "north") {
						var hh = centerOpt.height;
						if (!that._isPanelVisible(panels.expandNorth)) {
							hh += panelOptions.height - collapsedSize
									+ ((panelOptions.split || !panelOptions.border) ? 1 : 0);
						}
						panels.east.add(panels.west).add(panels.expandEast).add(
								panels.expandWest).panel("resize", {
							top : collapsedSize - 1,
							height : hh
						});
						return {
							resizeC : {
								top : collapsedSize - 1,
								height : hh
							},
							expand : {
								top : 0
							},
							expandP : {
								top : 0,
								left : 0,
								width : cc.width(),
								height : collapsedSize
							},
							collapse : {
								top : -panelOptions.height,
								width : cc.width()
							}
						};
					} else {
						if (region == "south") {
							var hh = centerOpt.height;
							if (!that._isPanelVisible(panels.expandSouth)) {
								hh += panelOptions.height - collapsedSize
										+ ((panelOptions.split || !panelOptions.border) ? 1 : 0);
							}
							panels.east.add(panels.west).add(panels.expandEast).add(
									panels.expandWest).panel("resize", {
								height : hh
							});
							return {
								resizeC : {
									height : hh
								},
								expand : {
									top : cc.height() - panelOptions.height
								},
								expandP : {
									top : cc.height() - collapsedSize,
									left : 0,
									width : cc.width(),
									height : collapsedSize
								},
								collapse : {
									top : cc.height(),
									width : cc.width()
								}
							};
						}
					}
				}
			}
		};
	},
	//展开panel
	_expand : function (region) {
		var that = this;
		var el = this.element;
		
		var panels = this.panels;
		var p = panels[region];
		var panelOptions = p.panel("getOptions");
		if (panelOptions.beforeExpand.call(p) == false) {
			return;
		}
		
		var styleE = _getStyleE();
		var expandRegion = "expand" + region.substring(0, 1).toUpperCase()
				+ region.substring(1);
		if (panels[expandRegion]) {
			panels[expandRegion].panel("close");
			p.panel("component").stop(true, true);
			p.panel("expand", false).panel("open")
					.panel("resize", styleE.collapse);
			p.panel("component").animate(styleE.expand, function() {
				that._refresh();
			});
		}
		//获得尺寸大小及位置
		function _getStyleE() {
			var cc = $(el);
			var centerOpt = panels.center.panel("getOptions");
			if (region == "east" && panels.expandEast) {
				return {
					collapse : {
						left : cc.width(),
						top : centerOpt.top,
						height : centerOpt.height
					},
					expand : {
						left : cc.width() - panels["east"].panel("getOptions").width
					}
				};
			} else {
				if (region == "west" && panels.expandWest) {
					return {
						collapse : {
							left : -panels["west"].panel("getOptions").width,
							top : centerOpt.top,
							height : centerOpt.height
						},
						expand : {
							left : 0
						}
					};
				} else {
					if (region == "north" && panels.expandNorth) {
						return {
							collapse : {
								top : -panels["north"].panel("getOptions").height,
								width : cc.width()
							},
							expand : {
								top : 0
							}
						};
					} else {
						if (region == "south" && panels.expandSouth) {
							return {
								collapse : {
									top : cc.height(),
									width : cc.width()
								},
								expand : {
									top : cc.height()
											- panels["south"].panel("getOptions").height
								}
							};
						}
					}
				}
			}
		};
	},
	_isPanelVisible : function (pp) {
		if (!pp) {
			return false;
		}
		if (pp.length) {
			return pp.panel("component").is(":visible");
		} else {
			return false;
		}
	},
	//layout初始化时，折叠collapse=true的panel
	_initCollapse : function () {
		var panels = this.panels;
		if (panels.east.length && panels.east.panel("getOptions").collapsed) {
			this._collapse("east", 0);
		}
		if (panels.west.length && panels.west.panel("getOptions").collapsed) {
			this._collapse("west", 0);
		}
		if (panels.north.length && panels.north.panel("getOptions").collapsed) {
			this._collapse("north", 0);
		}
		if (panels.south.length && panels.south.panel("getOptions").collapsed) {
			this._collapse("south", 0);
		}
	},
	//layout大小自适应外层html元素
	_fit : function (fit) {
		return $.coral.panel.fit(this.element, fit);
	},
	// 销毁layout
	_destroy : function () {
		//this.panels.north.panel("destroy");
		//this.panels.south.panel("destroy");
		//this.panels.west.panel("destroy");
		//this.panels.east.panel("destroy");
		//this.panels.center.panel("destroy");
		this.element.children().remove();
		this.element.detach();
	},
	//刷新layout
	refresh : function() {
		this._refresh();
	},
	//获得layout中对应方位region的panel
	panel : function(region) {
		return this.panels[region];
	},
	//折叠layout中对应方位region的panel
	collapse : function(region) {
		this._collapse(region);
	},
	//展开layout中对应方位region的panel
	expand : function(region) {
		this._expand(region);
	},
	//添加layout中对应方位region的panel
	add : function(options) {
		this._add(this.element, options);
		this._refresh();
		if (this.panels[options.region]
				.panel("getOptions").collapsed) {
			this._collapse(options.region, 0);
		}
	},
	//删除layout中对应方位region的panel
	remove : function(region) {
		this._remove(region);
		this._refresh();
	},
	//改变layout中对应方位region的大小
	resize : function(pos,region){
		var props = pos;
		var el = this.element;
		if(region !== undefined){
			var pp = this.panels[region];
			var oldHeight = pp.panel('component').outerHeight();
			var oldWidth = pp.panel('component').outerWidth();
			pp.panel('resize', pos);
			var newHeight = pp.panel('component').outerHeight();
			var newWidth = pp.panel('component').outerWidth();
			
			props.height = $(el).height() + newHeight - oldHeight;
			props.width = $(el).width() + newWidth - oldWidth;
		}
		$(el).css(props);
		
		this._refresh();
	}
});

})(jQuery);
