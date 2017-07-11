/*!
 * @author qiucs
 * @date   2014.7.17
 * 系统配置平台应用自定义: 生成配置树JS
 * 
 * 依赖JS:
 *    
 */
(function( $, undefined ) {

	"use strict";
	
	$.component("config.ctree", {
		version: $.config.version,
		options: {
			menuId : null, // 菜单ID
			componentVersionId : null, // 组装构件ID
			moduleId : null, // 自定义构件ID
			info   : null,   // 模块配置信息
			rightPanel : null, // 
			global : null    // 
		},
		/**
		 * 创建构件
		 */
	    _create: function () {
	    	// 
	    	this.globalTimestamp = $.config.getTimestampData(this.options.global);
	    	// override current tree
	    	var JQ_override = window[CFG_overrideName(this.globalTimestamp)];
	    	// 构件组装数据
	    	this.assembleData = $.config.getAssembleData(this.options.global);
	    	// override current tree
	    	if ($.isFunction(JQ_override)) {
	    		JQ_override.apply(this);
	    	}
	    	//
	    	if (!this.options.info.treeId) {
	    		CFG_message("该模块未绑定树，请联系管理员！", "error");
	    		return;
	    	}
	    	this.zNodes = this._getTreeRootNode();
	    	if (null === this.zNodes) {
	    		CFG_message("该模块绑定的树已被删除，请联系管理员！", "error");
	    		return;
	    	}
	    	this.preNode = null;
	    	// 布局区域
	    	// this.wPanel = this.options.uiLayout.layout("panel", "west");
	    	// this.cPanel = this.options.uiLayout.layout("panel", "center");
	    	// 树初始化
	    	var rNode = this._initCTree();
	    	//var relt = CFG_clickTreeNode(this.assembleData, rNode.type, "", rNode.name, "", this);
			var relt = this.treeClick(null, this._getTreeId(), rNode);
			if (false === relt) {
				// 帮助信息
				this._initHelp();
			}
            // 存储全局数据
            $.config.setGlobalData(this.options.global, {zNodes: this.zNodes});
		},
		/**
		 * 树根节点信息
		 * @returns
		 */
		_getTreeRootNode : function () {
			var url = $.config.appActionPrefix + "/tree-define!rootNodes.json"
					+ "?E_frame_name=coral"
					+ "&E_model_name=tree" 
					+ "&F_in=name,id,type,dbId,value,tableId,showNodeCount,columnFilter" 
					+ "&P_ISPARENT=child" 
					+ "&id=" + this.options.info.treeId
					+ "&P_componentVersionId=" + this.options.componentVersionId + "&P_menuId=" + this.options.menuId;
			return $.loadJson(url);
		},
		/**
		 * 初始化layout
		 **/
		_initCTree : function () {
			var _this   = this,
			    pBody  = this.element /*this.wPanel.panel("body")*/,
			    rNode  = null, 
			    outerDiv = $("<div class=\"left_tree_bg fill\"></div>").appendTo(pBody),
			    treeDiv  = $("<div class=\"coral-adjusted\"></div>").appendTo(outerDiv);

	    	this.uiTree = $("<ul id=\"" + this._getTreeId() + "\" class=\"outlook-tree\"></ul>").appendTo(treeDiv);
	    	
			var setting = {
					asyncAutoParam : "id",
					asyncUrl  : this._getTreeUrl(),
					asyncEnable : true,
					asyncType : "post", 
					onClick : function (e, treeId, treeNode) {
						_this.treeClick(e, treeId, treeNode);
					},
					/*onLoad : function (e, treeId, treeNode) {
						//if ("1" != _this.options.info.showNodeCount) return;
						var i = 0, children = treeNode.children, child;
						for (; i < children.length; i++) {
							child = children[i];
							//_this.uiTree.tree("updateNode", child);
							_this._updateNodeCountNumber(child);
						}
					},*/
					cls : "outlook-tree"
				};
			setting = this.beforeInitTree(setting);
			this.uiTree.tree(setting, this.zNodes);
			// 获取根节点
			rNode = this.uiTree.tree("getNodeByParam", "id", this.zNodes[0].id);
			// 展开第一层节点
			//if ("1" != this.options.info.isFiltrate) {
			this.uiTree.tree("expandNode", rNode, true);
			//}
			//
            /*$(pBody).find(".coral-adjusted").each(function() {
    			if ($(this).is(":visible")){
    				$.coral.adjusted(this);
    			}
    		});*/
			return rNode;
		},
		// 重构组件库树setting二次开发接口
		beforeInitTree : function (setting) {
			return setting;
		},
		
		_getAuthorityTreeIds : function () {
			var url  = $.contextPath + "/authority/authority-tree!getTreeNodeIds.json?P_componentVersionId=" + this.options.componentVersionId + "&P_menuId=" + this.options.menuId,
			    data = $.loadJson(url);
			return (null == data ? "" : data.toString());
		},
		// 更新节点数量信息
		_updateNodeCountNumber : function (treeNode) {
			if (!treeNode.id || treeNode.id.indexOf("NR2_") == 0) {
    			return;
    		}
			var url = $.config.appActionPrefix + "/tree-define!getTreeNodeRecordCount.json?P_treeNodeId=" + treeNode.id + "" +
					"&P_moduleId=" + this.options.info.id + 
					"&P_menuId=" + this.options.menuId,
				total = 0;
			if (isNotEmpty(treeNode["tableId"]) && "1" == treeNode["showNodeCount"]) {
				total = $.loadJson(url);
				treeNode.name = treeNode.name + " (" + total + ")";
				this.uiTree.tree("updateNode", treeNode);
			}
		},
		
		_getTreeUrl : function () {
			return (this.getAction() + "!tree.json"
					+ "?E_frame_name=coral"
					+ "&E_model_name=tree" 
					+ "&F_in=name,id,type,dbId,value,tableId,showNodeCount,columnFilter" 
					+ "&P_ISPARENT=child" 
					//+ "&P_OPEN=" + (("1" == this.options.info.isFiltrate ? true : false))
					+ "&P_filterId=parentId" 
		            + "&Q_EQ_nodeRule=0" 
		            + "&P_orders=showOrder"
		            + "&P_treeIds=" + this._getAuthorityTreeIds() 
		            + "&P_moduleId=" + this.options.info.id 
		            + "&P_componentVersionId=" + this.options.info.componentVersionId
		            + "&P_menuId=" + this.options.menuId
		            + "&P_showNodeCount=" + this.options.info.showNodeCount);
		},
		// ID 生成规则
		_generateId : function (prefix) {
			return prefix + "_" + this.options.info.id + "_" + this.uuid;
		},
		// 生成树UL的ID
		_getTreeId : function() {
			return this._generateId("tree");
		},
		// 点击树节点事件
		treeClick : function (e, treeId, treeNode) {
			var start = new Date().getTime();
			//console.log("remove start : " + (new Date().getTime() - start));
			// 导航条信息清空
	        if (typeof CFG_clearComponentZone === "function") {
	        	CFG_clearComponentZone(this.assembleData);
	        }
			//console.log("remove end: " + (new Date().getTime() - start));
	        // 
			var params, nodeArea;
			if ("6" == treeNode.type || "9" == treeNode.type) {
				this.toDefaultComponent(e, treeId, treeNode);
			} else {
				params   = this.getNodeParams(treeNode);
				nodeArea = this.getNodeArea(treeNode);
				//console.log("param:" + params);
				return CFG_clickTreeNode(this.assembleData, nodeArea.type, nodeArea.prop, treeNode.name, params, this);
			}
			//console.log("page end: " + (new Date().getTime() - start));
		},
		// 渲染节点布局或重新加载数据
		toNodeLayout : function (treeNode) {
			if ("9" == treeNode.type || this.preNode == null || this.preNode.tableId != treeNode.tableId) {
				this.initNodeLayout(treeNode);
			} else {
				this.resetNodeLayout(treeNode);
			}
		},
		// 初始化节点布局
		initNodeLayout : function (treeNode) {
			var cBody   = this.options.rightPanel, 
			    columns = this.treeNodeFilter(treeNode),
			    workflowId= ("9" == treeNode.type ? treeNode.dbId : ""),
			    box     = ("9" == treeNode.type ? treeNode.value : "");
			// 1. 删除当前panel中的内容
			cBody.find("div,input").unbind("remove");
			cBody.children().remove();
			// 2. 初始化树节点布局配置
			var options = {
					menuId : this.options.menuId,
					componentVersionId : this.options.componentVersionId,
					moduleId : this.options.moduleId,
					workflowId: workflowId,
					box     : box,
					columns : columns,
					info : this.cast2layout(treeNode),
					type : 1,
					global : this.options.global
			};
			this.uiLayout = new $.config.clayout(options, cBody);
		}, 
		// 重新加载节点数据
		resetNodeLayout : function (treeNode) {
			var tableId = treeNode["tableId"],
			    data    = $.config.getGlobalData(this.options.global),
			    storeId = $.config.storeId(tableId, $.config.contentType.grid),
			    columns = this.treeNodeFilter(treeNode),
			    box     = ("9" == treeNode.type ? treeNode.value : null),
			    relation= null, jqUI = null;
			if (storeId in data) relation = data[storeId];
			if (relation) jqUI = relation.jqUI;
			if (jqUI) {
				jqUI.options.box = box;
				jqUI.options.columns = columns;
				jqUI.searchFilter = "";
				jqUI.processSearchColumn();
				jqUI.reload();
			}
		},
	    // 节点过滤条件（从该节点到根节点所有字段节点添加到过滤条件中）
	    treeNodeFilter : function (treeNode, filterArr) {
	        if (undefined == filterArr || null == filterArr) {
	            filterArr = new Array();
	        }
	        var columnId = treeNode["dbId"],
	            value    = treeNode["value"],
	            type     = treeNode["type"],
	            tableId  = treeNode["tableId"],
	            parentNode = treeNode.getParentNode();
	        if ("3" == type || "4" == type) {
	            var isId = ("3" == type);
	            var filter = this.treeNodeColumn(columnId, tableId, value, isId);
	            if (null != filter) {
	            	filterArr.push(filter);
	            }
	        } else if ("2" == type) {
	        	// 物理表节点
                if (isNotEmpty(treeNode.columnFilter)) {
                	filterArr.push(treeNode.columnFilter);
                }
                // 如果物理表与物理表组节点同时存在，只取物理表过滤条件
	        	if (null != parentNode && "5" == parentNode.type) {
	        		parentNode._ht_ = false; // 物理表节点标记
	        	}
	        } else if ("5" == type && isNotEmpty(treeNode.columnFilter)) {
	        	// 物理表组节点
	        	if (false !== treeNode._ht_) {
	        		filterArr.push(treeNode.columnFilter);
	        	}
	        	// 清物理表节点添加了标记
                if ("_ht_" in treeNode) {
                	delete treeNode._ht_;
                }
	        }
	        
	        if (parentNode == null) {
	        	return filterArr.join(";");
	        }/* else {
	        	parentNode["tableId"] = tableId;
	        }*/
	        
	        return this.treeNodeFilter(parentNode, filterArr);
	    },
	    //
	    getNodeParams : function (treeNode) {
	    	var params = "", columns;
	    	// 
	    	params = this.addParam(params, "P_treeNodeId", treeNode.id);
	    	
	    	if ("1" == treeNode.type || "4" == treeNode.type) {
	    		// 空节点或字段标签
	    		params = this.addParam(params, "P_value", treeNode.value);
	    	} else {
	    		// 字段节点过滤条件
	    		columns = this.treeNodeFilter(treeNode);
	    		if ("2" == treeNode.type) {
	    			// 表节点
	    			params = this.addParam(params, "P_tableId", treeNode.dbId);
	    		} else if ("5" == treeNode.type) {
	    			// 物理表组节点
	    			params = this.addParam(params, "P_groupId", treeNode.dbId);
	    			params = this.addParam(params, "P_tableId", treeNode.tableId);
	    		} else if ("3" == treeNode.type) {
	    			// 字段节点
	    			params = this.addGroupOrTableParam(params, treeNode);
	    		}
	    		// 添加字段过滤参数
	    		if (isNotEmpty(columns)) params = this.addParam(params, "P_columns", columns);
	    	}
	    	return this.processParams(treeNode, params);
	    },
	    //
	    getNodeArea : function (treeNode) {
	    	var prop = "";
	    	if ("1" == treeNode.type) {
	    		// 空节点
	    		prop = treeNode.value;
	    	} else {
	    		if ("2" == treeNode.type) {
		    		// 表节点
	    			// 组装时 （节点类型 + 树节点ID）
	    			prop = treeNode.id;  
	    		} else if ("4" == treeNode.type) {
		    		// 字段标签节点
	    			// 组装时 （节点类型 + 字段标签）
	    			prop = treeNode.dbId;
	    		} else if ("5" == treeNode.type) {
		    		// 物理表组节点
	    			// 组装时 （节点类型 + 逻辑表组CODE）
	    			prop = this.getLogicGroupCode(treeNode.dbId);
	    		} else if ("3" == treeNode.type) {
		    	    // 字段节点
	    			return this.getGroupOrTableArea(treeNode);
	    		}
	    	}
	    	return {type: treeNode.type, prop: prop};
	    },
	    // 获取物理表组对应的逻辑表组代码
	    getLogicGroupCode : function (groupId) {
	    	var url = $.config.appActionPrefix + "/physical-group-define!getLogicGroupCode.json?id=" + groupId;
	    	return $.loadJson(url);
	    },
	    // 向URL添加参数
	    addParam : function (param, paramName, value) {
	    	if (param.length > 0) param += "&";
	    	param += (paramName + "=" + (value));
	    	return param;
	    },
	    // 查找字段节点对应的表节点或物理表组节点
	    lookupGroupOrTableNode : function (treeNode) {
	    	var parentNode = treeNode.getParentNode();
	    	if (null == parentNode) return null;
	    	if ("2" == parentNode.type) return parentNode;
	    	if ("5" == parentNode.type) return parentNode;
	    	return this.lookupGroupOrTableNode(parentNode);
	    },
	    // 给字段节点添加对应的表节点或物理表组节点参数
	    addGroupOrTableParam : function (params, treeNode) {
	    	var distNode = this.lookupGroupOrTableNode(treeNode);
	    	if (null == distNode) return params;
	    	if ("2" == distNode.type) return this.addParam(params, "P_tableId", distNode.dbId);
	    	if ("5" == distNode.type) return this.addParam(params, "P_groupId", distNode.dbId);
	    	return params;
	    },
	    // 给字段节点添加对应的表节点或物理表组节点属性值（表节点：表ID；物理表组节点：逻辑表组编码）
	    getGroupOrTableArea : function (treeNode) {
	    	var distNode = this.lookupGroupOrTableNode(treeNode);
	    	if (null == distNode) return {};
	    	if ("2" == distNode.type) return {type: "2", prop: distNode.id};
	    	if ("5" == distNode.type) return {type: "5", prop: this.getLogicGroupCode(distNode.dbId)};
	    	return {};
	    },
	    // 打开树节点绑定的构件
	    toBindedComponent : function (url) {
	    	var cBody = this.options.rightPanel;
	    	$.ajax({
				url : $.contextPath + url,
				dataType : "html",
				context : document.body
			}).done(function(html) {
				var CFG_configInfo = {};
				cBody.data("selfUrl", url);
				cBody.data("parentConfigInfo", CFG_configInfo);
				cBody.empty();
				cBody.append(html);
				$.parser.parse(cBody);
				if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_bodyOnLoad) {
					CFG_configInfo.childConfigInfo.CFG_bodyOnLoad();
				}
				if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_initReserveZones) {
					CFG_configInfo.childConfigInfo.CFG_initReserveZones();
				}
			});
	    },
	    // 如果树节点没有绑定构件时，默认打开页面
	    toDefaultComponent : function (e, treeId, treeNode) {
	    	var type = treeNode.type,
	    	    columns, box;
	    	if ("0" == type || "1" == type || "4" == type || "6" == type) {
        		this._initHelp(e, treeId, treeNode);
	            this.preNode = null;
	        } else {
	            // 渲染页面或重新加载数据
	            this.toNodeLayout(treeNode);
	            // 更新前一次节点信息
	            this.preNode = treeNode;
	        }
	    },
	    
	    /**
	     * 组装字段过渡条件（与列表检索条件形式一致）.
	     * @param columnId
	     * @param value
	     * @param isId --columnId是否为字段ID：true-是，false-否(字段英文名)
	     */
	    treeNodeColumn: function (columnId, tableId, value, isId) {
	    	var url = $.config.appActionPrefix + "/column-define!toColumnName.json";
	        if (isId) {
	            url += "?id=" + columnId + "&P_isLabel=0";
	        } else {
	        	url += "?P_columnLabel=" + columnId + "&P_tableId=" + tableId + "&P_isLabel=1";
	        }
	        return this._2filter(url, value, isId);
	    },
	    // 把字段节点转成过滤条件
	    _2filter : function (url, value, isId) {
	    	var col = $.loadJson(url);
            if (isEmpty(col)) {
            	if (isId) CFG_message("字段节点配置有问题，请联系管理员！", "error");
            	else CFG_message("字段标签节点配置有问题，请联系管理员！", "error");
            	return null;
            }
            if (isEmpty(value)) return ("NLL_C_" + col + CFG_cv_split + "0");
            return ("EQ_C_" + col + CFG_cv_split + value);
	    },
		// 生成帮助信息
		_initHelp : function (e, treeId, treeNdoe) {
			var cBody = this.options.rightPanel /*this.cPanel.panel("body")*/;
			// 1. 先销毁panel中的内容
			cBody.empty();
			// 2. 添加帮助信息
			cBody.append("<div style=\"margin-top:10px;margin-left:10px\"><ul>" +
					"<li type=\"square\">" + "<p><b>操作说明：</b><br></p>" + 
					"<p>1. 选择树中的节点，右侧出现该节点的操作界面<br></p> \n" + 
					"</li>" + 
					"</ul></div>");
		},
		//
		cast2layout : function (treeNode) {
			return {
				table1Id: treeNode.tableId,
				area1Id : $.config.contentType.grid,
				templateType: $.config.pattern._1C
			};
		},
		// 
		getAction : function () {
			var fn = window[CFG_actionName(this.globalTimestamp)];
			if ($.isFunction(fn)) return fn(this);
			
			return $.config.appActionPrefix + "/show-module";
		},
		// 点击节点时，给对应构件传入参数二次开发接口
		processParams : function(treeNode, params) {
			return params;
		},
		
		/**
		 * 获取URL中的参数值(如果参数名以“P_”打头的，如P_tableId，则key为tableId)
		 * @return Object
		 */
		getParamValue : function (key) {
			var data = $.config.getGlobalData(this.options.global),
			    val  = null;
			if (key in data) {
				val = data[key];
			} else {
				val = CFG_getInputParamValue(this.assembleData, key);
			}
			return val || "";
		},
		
		// 添加回调方法 callback.name
		addCallback : function (name, fn) {
			if (!this.callback) this.callback = {};
			this.callback[name] = fn;
		},
		// 添加传递参数函数（传递给组装构件） value.name
		addOutputValue : function (name, fn) {
			if (!this.value) this.value = {};
			this.value[name] = fn;
		},
		/********************************** 预留区默认回调函数、输出参数函数(注：请勿复写)--开始 **********************************/
		_defaultOutput : function () {
			// 回调函数
			if (!this.callback) this.callback = {};
			

			// 输出参数函数
			if (!this.value) this.value = {};
		}
	});

})( jQuery );
