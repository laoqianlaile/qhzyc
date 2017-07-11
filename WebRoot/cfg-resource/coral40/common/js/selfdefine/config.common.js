/***************************************
 * @author qiucs
 * @date   2014.7.17
 * 系统配置平台应用自定义公用函数
****************************************/

var CFG_cv_split = "≡",  /* 字段与字段值的分隔符.*/
    CFG_oc_split = "_C_",/* 操作符关键字与字段的分隔符.*/
    CFG_common = {
    	    /* 预置按钮.*/
    	    P_CREATE : "create", // 新增
    	    P_SAVE : "save",     // 保存
    	    P_ADD : "add",     // 保存
    	    P_UPDATE : "update", // 修改
    	    P_BATCH_UPDATE : "batchUpdate", // 批量修改
    	    P_MODIFY : "modify", // 前台修改
    	    P_VIEW : "view", // 查看
    	    P_DELETE : "delete", // 删除(物理)
    	    P_LOGICAL_DELETE : "logicalDelete", // 删除(逻辑)
    	    P_REMOVE : "remove", // 前台删除（只删除选中的列表数据，不走后台）
    	    P_SEARCH : "search", // 检索
    	    P_REFRESH : "refresh", // 刷新
    	    //P_NESTED_SEARCH : "nestedSearch", // 查询（嵌入式）
    	    P_BASE_SEARCH : "baseSearch", // 检索
    	    P_GREAT_SEARCH : "greatSearch", // 检索
    	    P_REPORT : "report", // 打印
    	    P_UPLOAD : "upload", // 上传电子全文
    	    P_VIEW_DOCUMENT : "viewDocument", // 查看附件
    	    P_EXPORT : "export", //导出
    	    P_EXPORT_SETTING : "exportSetting", // 导出设置
    	    P_EXPORT_DONE : "exportDone", // 执行导出
    	    P_SAVE_ALL : "saveAll", // 保存并新增
    	    P_SAVE_AND_CREATE : "saveAndCreate", // 保存并新增
    	    P_SAVE_AND_CLOSE : "saveAndClose", // 保存并关闭
    	    P_ADD_AND_CLOSE : "addAndClose", // 保存并关闭
    	    P_RESET : "reset", // 重置
    	    P_REPORT_PRE : "_subreport_", // 报表前缀字符
    	    P_CUSTOM_SEARCH : "customSearch", // 检索自定义
    	    P_CUSTOM_COLUMN : "customColumn", // 列表自定义
    	    P_CUSTOM_SORT : "customSort", // 排序自定义
    	    P_FIRST_RECORD : "firstRecord", // 首条
    	    P_PREVIOUS_RECORD : "previousRecord", // 上一条
    	    P_NEXT_RECORD : "nextRecord", // 下一条
    	    P_LAST_RECORD : "lastRecord", // 末条
    	    /* 列表超链接按钮. */
    	    P_LINK_DELETE_GD : "linkDeleteGD", // 删除（只删除列表数据）
    	    P_LINK_DELETE_DB : "linkDeleteDB", // 删除（物理）
    	    P_LINK_DELETE_LG : "linkDeleteLG", // 删除（逻辑）
    	    P_LINK_UPDATE : "linkUpdate", // 修改
    	    P_LINK_VIEW_DFORM : "linkViewDForm", // 详细
    	    P_LINK_VIEW_DGRID : "linkViewDGrid", // 明细
    	    P_LINK_VIEW_DOCUMENT : "linkViewDocument", // 查看附件
    	    
    	    /* 表单与列表的预留区前缀. */
    	    RZ_PRE_FORM : "RZ_F",
    	    RZ_PRE_GRID : "RZ_G",
    	    /* 工作流按钮. */
    	    P_TRANSACT : "transact", // 办理
    	    P_TRANSACT_AND_CHECKOUT : "transactAndCheckout", // 办理并签收
    	    P_START : "start", // 启动
    	    P_START_AND_COMPLETE : "startAndComplete", // 启动并提交
    	    P_COMPLETE : "complete",// 提交
    	    P_CHECKOUT : "checkout",// 签收
    	    P_UNTREAD : "untread", // 退回
    	    P_RECALL : "recall", // 撤回
    	    P_REASSIGN : "reassign",// 转办
    	    P_DELIVER : "deliver", // 传阅
    	    P_TRACK : "track", // 跟踪
    	    P_HASREAD : "hasread", // 阅毕
    	    P_SUSPEND : "suspend", // 中止（删除）
    	    P_TERMINATION : "termination", // 终止
    	    /* 预留区名称前缀. */
    	    ZONE_NAME_PRE : "MT_zone_",
    	    /* 工作箱. */
    	    BOX_APPLYFOR : "applyfor", // 申请箱
    	    BOX_TODO : "todo", // 待办箱
    	    BOX_HASDONE : "hasdone", // 已办箱
    	    BOX_COMPLETE : "complete", // 办结箱
    	    BOX_TOREAD : "toread", // 待阅箱
    	    
    	    P_ID : "PROCESS_INSTANCE_ID_",// 流程实例ID属性
    	    W_ID : "WI_ID_", // 工作项ID属性
    	    W_STATUS : "WI_STATUS_KEY_", // 工作项状态属性
    	    ACTIVITY_ID : "WI_ACTIVITY_ID_", // 流程节点ID属性
    	    START_ACTIVITY_ID : "__0__"   // 流程开始节点ID
    	};


/**
 * 获取预留区名称
 * @param tableId 表ID
 * @param areaIndex 预留区位置
 * @returns {string}
 */
function CFG_getZoneName(tableId, type, areaIndex, pos) {
	var name = CFG_common.ZONE_NAME_PRE + tableId + "_" + type + "_" + areaIndex;
	if (isNotEmpty(pos)) {
		name += "_" + pos;
	}
    return name;
}

/**
 * 用户下拉树控制，如果是部门节点则不能选择
 */
function CFG_combotreeUser(treeId, treeNode) {
	if (treeNode && "USER" === treeNode["treeType"] && "U" !== treeNode["nodeType"]) {
		return false;
	}
	
	return true;
}

/**
 * JSON特殊字符处理
 */
function CFG_processSpecialChar(str) {
	if (typeof str !== "string") return str;
	
	if (str.match(/\"/)) str = str.replace(/\"/g, "\\\"");
	if (str.match(/\'/)) str = str.replace(/\'/g, "\\\'");
	if (str.match(/\//)) str = str.replace(/\//g, "\\\/");
	if (str.match("\b")) str = str.replace(/\b/g, "\\\b");
	if (str.match(/\b/)) str = str.replace(/\r/g, "\\\r");
	if (str.match(/\n/)) str = str.replace(/\n/g, "\\\n");
	if (str.match(/\t/)) str = str.replace(/\t/g, "\\\t");
	
	return str;
}

/**
 * 用户下拉树控制，如果是部门节点则不能选择
 */
function CFG_combotreeControl(treeId, treeNode) {
	if (treeNode && "USER" === treeNode["treeType"] && "U" !== treeNode["nodeType"]) {
		return false;
	}
	if (typeof customCombotreeControl === "function") {
		return !!customCombotreeControl(treeId, treeNode);
	}
	return true;
}

/**
 * 
 * @param subffix
 * @returns {String}
 */
function CFG_actionName(subffix) {
	return "_action_" + subffix;
}
/**
 * 
 * @param subffix
 * @returns {String}
 */
function CFG_overrideName(subffix) {
	return "_override_" + subffix;
}

/**
 * 防止组件库带来性能影响
 * @param jq
 */
function CFG_removeJQ (jq) {
	// 移除组件库绑定了remove方法
	jq.unbind("remove");
	//jq.find("div").unbind("remove");
	// 移除
	jq.remove();
}

/**
 * 防止组件库带来性能影响
 * @param jq
 */
function CFG_emptyJQ (jq) {
	// 移除组件库绑定了remove方法
	//jq.find("div").unbind("remove");
	// 清空
	jq.empty();
}

/**
 * 列表链接事件处理
 * @param url
 * @param componentId
 */
function MT_GRID_link(id, dataId, cgridDivId) {
    var jq = null, cgrid = null;
    if (isEmpty(id)) {
        CFG_message("未设置该列的链接事件，请检查！", "warning");
    } else {
    	jq = $("#" + cgridDivId);
    	cgrid = $.data(jq.get(0), "config-cgrid");
    	cgrid.selectedRowId = dataId;
    	cgrid.clickLink(id, dataId);
    }
	return false;
}

/**
 * 列表超链接预留区事件处理
 * @param cgridDivId
 * @param reserveZoneName
 * @param id
 */
function MT_GRID_LINK_ZONE_click(cgridDivId, reserveZoneName, id, currentRowId) {
    var jq = $("#" + cgridDivId);
    var cgrid = $.data(jq.get(0), "config-cgrid");
    cgrid.selectedRowId = currentRowId;
    if (id.match(/^link/)) {
    	cgrid.clickLink(id, currentRowId);
    	return false;
    }
    CFG_clickToolbar(cgrid.assembleData, reserveZoneName, id, "1", cgrid, currentRowId);
}

/**
 * 表单预留区事件处理
 * @param cFromDivId
 * @param reserveZoneName
 * @param title
 * @param type
 */
function MT_FORM_click(cFromDivId, reserveZoneName, title, type) {
    var cf = $("#" + cFromDivId);
    var cform = $.data(cf.get(0), "config-cform");
    CFG_clickButtonOrTreeNode(cform.assembleData, reserveZoneName, title, type, cform)
}

/**
 * 基本检索预留区事件处理
 * @param cFromDivId
 * @param reserveZoneName
 * @param title
 * @param type
 */
function MT_SEARCH_click(csearchDivId, reserveZoneName, title, type) {
    var cf = $("#" + csearchDivId);
    var csearch = $.data(cf.get(0), "config-cbsearch");
    CFG_clickButtonOrTreeNode(csearch.assembleData, reserveZoneName, title, type, csearch)
}

/**
 * @param controller
 *        --指定controller(不包括方法，同时这个controller必须是继承ShowModuleDefineServiceDaoController): 如果 $.contextPath + "/appmanage/show-module"
 * @param workflowId  --流程定义中的流程ID
 * @param workitemId  --工作项ID
 * @param dataId      --要提交的表单数据ID
 * @param jqGlobal    --表单区或是当前区域所在DIV jquery对象
 * @param opt
 *        --可选项
 *        格式： {
 *        	opinion:"0xxx/1xxx", // 审批结果（1表示同意/0表示不同意）和审批意见（xxx），如："1已阅"（表示：审批结果为同意，审批意见为已阅）
 *        	formData: {},        // 要保存的表单数据，格式为键值对
 *          success : function(workflowId, workitemId, dataId) {} // 提交成功时，回调方法
 *          cancel  : function(workflowId, workitemId, dataId) {} // 取消按钮回调方法
 *        }
 */
function CFG_coflowComplete(controller, workflowId, workitemId, dataId, jqGlobal, opt) {
    var coflowPerformerTarget = null,
        activityMap = null,
        url = controller + "!isUserControl.json?P_workitemId=" + workitemId,
        isUserControl = $.loadJson(url);

	if (true == isUserControl) {
		_coflowUserControl();// 弹出选择下一步节点页面
	} else {
		_coflowComplete(); // 自动提交
	}
	// 提交
	function _coflowComplete(performers, close) {
		var url, formData, postData = {};
		url  = _getCoflowUrl();
		//
		if (performers) postData["P_performers"] = null2empty(performers);
		//
		if (opt && "opinion" in opt) postData["P_opinion"] = null2empty(opt.opinion);
		// 获取表单数据
		if (opt && "formData" in opt) postData["E_entityJson"] = $.config.toString(opt.formData);
		$.ajax({
			type : "post",
			url : url,
			data : postData,
			dataType : "json",
			success : function(rlt) {
				if (false === rlt.success) {
					CFG_message(rlt.message, "warning");
				} else if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
					CFG_message("操作成功！", "success");
					// 成功时，回调方法
					if (opt && typeof opt.success === "function") {
						opt.success(workflowId, rlt.workitemId, dataId);
					}
					// 关闭当前窗口
					if (close && typeof close === "function") {
						close();
					}
				} else {
					CFG_message("提交失败！", "warning");
				}
			},
			error : function() {
				CFG_message("操作失败！", "error");
			}
		});
	};
	
	function _coflowUserControl() {
		var rNode = null, setting = null, 
		    jqUC  = $("<div>").appendTo(jqGlobal), 
		    h     = jqGlobal.height() > 600 ? 600 : jqGlobal.height(), 
		    jqLy  = $("<div class=\"coralui-layout\" data-options=\"fit:true\" style=\"height:100%;width:620px;" + h + "\"></div>"), 
		    jqWt  = $("<DIV data-options=\"region:'west',split:true\" style=\"width:260px;height:100%;padding-left:10px;padding-right:10px;\"></DIV>"), 
		    jqCt  = $("<DIV data-options=\"region:'center',split:true\" style=\"width:60px;height:100%;\"></DIV>"),  
		    jqAt  = $("<DIV data-options=\"region:'east',split:true\" style=\"width:300px;height:100%;padding-left:10px;padding-right:10px;\"></DIV>"), 
		    jqTs  = $("<input type=\"text\">").appendTo(jqWt), // 搜索框（树）
		    jqTr  = $("<UL class=\"coral-adjusted\" style=\"height:100%;overflow:auto;\"></UL>").appendTo(jqWt), 
			url = controller + "!coflowNextStep.json?P_workitemId=" + workitemId, 
			nsInfo = $.loadJson(url), 
			title = "当前节点：" + nsInfo.currentActivity.name,
			boxStyle = nsInfo.currentActivity.boxStyle,
			treeData = null;

		if (opt && isNotEmpty(opt.opinion)) {
			if (opt.opinion.length > 500) {
				CFG_message("审批意见过长(最多500个字符)，请修改！", "warning");
				jqUC.remove();
				return ;
			} else if ("0" === opt.opinion.substring(0,1)) {
				// 如果不同意，直接提交终止流程
				$.confirm("不同意，该流程将被终止，确定要提交吗？", function (sure) {
					if (sure) _this._coflowComplete(null, opinion);
				} );	
				jqUC.remove();
				return ;
			}
		}
		// 检查是否只有一个结束节点
		if (nsInfo.nextActivities.length === 1 && nsInfo.nextActivities[0].ended) {
			// 结束节点，直接提交
			jqUC.remove();
			jqUC = null;
			_complete(nsInfo.nextActivities[0].id + "^_^" + nsInfo.nextActivities[0].defaultPerformers[0].id);
			return;
		}
	
		jqUC.dialog({
			autoOpen : false,
			appendTo : jqGlobal,
			title : title,
			modal : true,
			width : 660,
			height : (h - 60),
			resizable : false,
			position : {
				of : jqGlobal
			},
			onClose : function() {
				jqUC.remove();
			}, 
			onOpen : function () {
				var jqPanel = jqUC.dialog("buttonPanel"),
				    jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
				jqPanel.addClass("app-bottom-toolbar");
				jqDiv.toolbar({
					data: ["", "->", 
					       {id:"sure", label:"提交", type:"button", cls: "save"},
					       {id:"cancel", label:"取消", type:"button"}],
					onClick: function(e, ui) {
						if ("sure" === ui.id) {
							var success = true;
							jqDiv.toolbar("disableItem", "sure");
							success = _complete();
							if (false === success) {
								jqDiv.toolbar("enableItem", "sure");
							}
						} else {
							// 取消时，回调方法
							if (opt && typeof opt.cancel === "function") {
								opt.cancel(workflowId, workitemId, dataId);
							}
							jqUC.dialog("close");
						}
					}
				});
			}
		});
	
		jqWt.appendTo(jqLy);
		jqCt.appendTo(jqLy);
		jqAt.appendTo(jqLy);
		jqLy.appendTo(jqUC);
		// 按钮区
		jqCt.append("<div style=\"text-align:center;width:100%;padding-top:120px;\">" 
				+ "<p style=\"margin-top:5px;\"><button id=\"toRight_" + this.uuid + "\" class=\"coralui-button\" dataOptions=\"\">　&gt;　</button></p>"
				+ "<p style=\"margin-top:5px;\"><button id=\"toLeft_" + this.uuid + "\" class=\"coralui-button\" dataOptions=\"\">　&lt;　</button></p>"
				+ "</div>");
		// 解析布局
		$.parser.parse(jqUC);
		// 按钮事件绑定
		$("#toRight_" + this.uuid, jqUC).click(function () {
			var tns = jqTr.tree("getCheckedNodes", true);
			if (!tns || tns.length == 0) {
				CFG_message("请选择审批人！", "warning");
				return false;
			}
			var activityId = coflowPerformerTarget.attr("name"), 
		        value = null;
			for (var i = 0, len = tns.length; i < len; i++) {
				value = (activityId + "^_^" + tns[i].id);
				if (coflowPerformerTarget.find("option[value='" + value + "\']").length > 0) {
					continue;
				}
				$("<option value=\"" + value + "\">" + tns[i].name + "</option>").appendTo(coflowPerformerTarget);
			}
		});
		$("#toLeft_" + this.uuid, jqUC).click(function () {
			if (!coflowPerformerTarget ||
					coflowPerformerTarget.find("option:selected").length < 1) {
				CFG_message("请选择要移除了审批人！", "warning");
				return false;
			}
			coflowPerformerTarget.find("option:selected").remove();
		});
		// 
		jqTs.textbox({
			name: "treeSearch",
			componentCls : "tree-search-box",
			placeholder : "请输入关键字",
			icons: [{icon: "icon-search3", click: function(e, data) {
				jqTr.tree("filterNodesByParam", {name: data.value});
			}}],
			onKeyUp : function (e, data) {
				jqTr.tree("filterNodesByParam", {name: data.value});
			}
		});
		// 用户组织树数据
		treeData = $.loadJson(controller + "!page.json?E_frame_name=coral&E_model_name=user&P_ASYNC=0&P_OPEN=0&P_NOCHECK=1");
		// 初始化机构人员树
		setting = {	checkable : true };
		jqTr.tree(setting, [ {id : "-1", name : "组织机构树", nocheck : true, children: treeData, open: true} ]);
		// 初始化下一步审批节点布局
		_coflowCreateNextStepUI(jqTr, jqAt, nsInfo, treeData);
		// 打开对话框
		jqUC.dialog("open");
		jqLy.layout("refresh");
		// 置顶
		jqUC.dialog("moveToTop");
		$(".coflow-performer-border", jqAt).each(
				function() {
					var jq = $(this)
					var h = jq.height();
					$("div", jq).height(h);
				}
		);

		/** 获取提交下一节点和参与者. */
		function getPerformers() {
			var i = 0, idArr = [], jqSel = null, jqOp = null, j = 0, performers = "";
			$(".coflow-next-activity", jqAt).each(
				function() {
					var jq = $(this);
					if (jq.checkbox("isChecked")) idArr.push(jq.checkbox("getValue"));
				}
			);
			if (nsInfo.currentActivity.minLin > idArr.length) {
				CFG_message("本节点定义至少选中" + nsInfo.currentActivity.minLin + "个节点！", "warning");
				return false;
			}
			if (nsInfo.currentActivity.maxLin < idArr.length) {
				CFG_message("本节点定义最多选中" + nsInfo.currentActivity.maxLin + "个节点！", "warning");
				return false;
			}
			for (; i < idArr.length; i++) {
				jqSel = $("select[name='" + idArr[i] + "']");
				jqOp = jqSel.find("option");
				if (0 === jqOp.length) {
					CFG_message("选中的节点必须要有参与者！", "warning");
					return false;
				}
				jqOp.each(function() {
					performers += "," + $(this).val();
				});
			}
			return performers.substring(1);
		};
		// 提交
		function _complete(performers) {
			var flag = !!performers; //是否直接提交，如果直接提交，则没有选择审批人员窗口
			performers = flag ? performers : getPerformers();
			if (false === performers) {
				return false;
			}
			_coflowComplete(performers, (flag ? null : function () {if (jqUC) jqUC.dialog("close");}));
			return true;
		};
	};
	
	/**
	 * 下一步审批页面
	 */
	function _coflowCreateNextStepUI ($tree, jqAt, nsInfo, treeData) {
		var i = 0, jqTr = null, jqTd = null, jqDiv = null, jqTp = null, html = null, 
		    j = 0, defaultPerformers, optionPerformers, activity, label, 
		    boxStyle = nsInfo.currentActivity.boxStyle, 
		    nextActivities = nsInfo.nextActivities, 
		    jqTb = $("<table style=\"width:100%;height:100%;\"></table>").appendTo(jqAt),
		    suffixCkId = ("_" + getTimestamp()),
		    zNodes= [];
		
		activityMap = {asyncTree: true};

		for (; i < nextActivities.length; i++) {
			activity = nextActivities[i];
			defaultPerformers = activity.defaultPerformers;
			optionPerformers  = activity.optionPerformers;
		    zNodes= [];
			// 审核节点
			jqTr = $("<tr></tr>").appendTo(jqTb);
			jqTd = $("<td style=\"height:32px;\"></td>").appendTo(jqTr);
			label = activity.name;
			if (activity.isRollback) {
				label = "<font color=\"red\">" + label + "(回退流转节点)</font>";
			}
			activityMap[activity.id] = {
					isPerformerControl: activity.isPerformerControl,
					zNodes : optionPerformers,
					isRollback : activity.isRollback
			};
			jqTp = $("<input type=\"checkbox\" id=\"" + (activity.id + suffixCkId) + "\" name=\"" + (activity.id + suffixCkId) + "\" class=\"coflow-next-activity\">").appendTo(jqTd);
			// 节点
			jqTp.checkbox({
				name : "activity",
				value : activity.id,
				label : label,
				checked : activity.checked,
				disabled : activity.disabled,
				onChange : function(e, ui) {
					var jqChk = $(this),
					    activityId = jqChk.checkbox("option", "value"),
					    jqSel = $("select[name='" + activityId + "']", jqAt);
					changePerformerTree(jqSel, !ui.checked);
				}
			});
			// 审批人员
			jqTr = $("<tr></tr>").appendTo(jqTb);
			jqTd = $("<td valign=\"top\" class=\"coflow-performer-border\"></td>").appendTo(jqTr);
			jqDiv= $("<div class=\"coral-adjusted\"></div>").appendTo(jqTd);
			jqTp = $("<SELECT mutiple=\"multiple\" size=5 class=\"selectNormal\"></SELECT>").appendTo(jqDiv);
			jqTp.attr("name", activity.id);
			jqTp.prop("disabled", activity.ended);
			// 默认审批人员
			for (j = 0; j < defaultPerformers.length; j++) {
				$("<option>").val(activity.id + "^_^" + defaultPerformers[j].id).text(defaultPerformers[j].name).appendTo(jqTp);
			}
			
			if (activity.ended) jqTp.hide();
			
			if (activity.checked) coflowPerformerTarget = jqTp;
		}
		
		$("select", jqTb).focus(function() {
			changePerformerTree($(this));
		}).dblclick(
			function() {
				var jq = $(this);
				if (!jq.prop("disabled")) {
					jq.find("option[value='" + jq.val() + "']").remove();
				}
		});
		//
		if (coflowPerformerTarget) {
			changePerformerTree(coflowPerformerTarget);
		}// */
		// 改变组织机构树
		function changePerformerTree(jqSel, onblur) {
			var activityId, zNodes, jqChk, isControl, isOption, asyncTree;
			if (typeof onblur === "boolean") jqSel.prop("disabled", onblur);
			if (onblur || jqSel.prop("disabled")) {
				return _resetSelected(jqSel);
			}
			
			
			activityId = jqSel.attr("name");
			jqChk = $("#" + (activityId + suffixCkId));
			if (!jqChk.checkbox("isChecked") || jqChk.checkbox("option", "disabled")) {
				return _resetSelected(jqSel);
			}
			$("select.selectActive", jqTb).removeClass("selectActive");
			jqSel.addClass("selectActive");
			coflowPerformerTarget = jqSel;
			zNodes = activityMap[activityId].zNodes,
		    isControl = activityMap[activityId].isPerformerControl,
			asyncTree = !isControl;
			if (asyncTree && activityMap.asyncTree) return;
			$tree.tree("destroy");
			var setting = {	checkable: true	};
			if (asyncTree) {
				$tree.tree(setting, [{id:"-1", name:"组织机构树", nocheck: true, children: treeData, open: true}]);
				activityMap.asyncTree = true;
			} else {
				$tree.tree(setting, [{id:"-1", name:"组织机构树", nocheck: true, children: zNodes, open:true}]);
				activityMap.asyncTree = false;
			}
		};
		// 重置节点选择
		function _resetSelected(jqSel) {
			var rtNode = null;
			if (jqSel.hasClass("selectActive")) {
				jqSel.removeClass("selectActive")
				rtNode = $tree.tree("getNodesByParam", "id", "-1");
				$tree.tree("removeChildNodes", rtNode[0]);
				activityMap.asyncTree = false;
			}
			coflowPerformerTarget = null;
		}
	};
		
	function _getCoflowUrl() {
		var url = controller + "!coflow.json?P_workflowId=" + workflowId + 
        		"&P_workitemId=" + workitemId + 
        		"&P_op=" + CFG_common.P_COMPLETE + "&id=" + dataId;
		return url;
	};
}

/**
 * @author qiucs
 * 拼接查询条件
 * @param col 字段名称
 * @param op  操作符：EQ/LIKE/GT/LT/NOT/IN/NLL/GTE/LTE
 * @param val 值
 */
function spliceFilter(col, op, val) {
	return op + CFG_oc_split + col + CFG_cv_split + val;
}

/**
 * @author qiucs
 * 分离单个查询条件
 * @param item
 */
function spliteFilterItem(item) {
	var cvArr, ocArr, op ;
	cvArr = item.split(CFG_cv_split);
	if (!cvArr || cvArr.length != 2) {
		return null;
	}
	ocArr = cvArr[0].split(CFG_oc_split);
	return {col: ocArr[1], op : ocArr[0], val: cvArr[1]};
}

/**
 * 获取自定构件前台UI对象
 * @param id -- DOM元素ID
 * @param name -- 自定义构件名称：cgrid/cform
 * @returns
 */
function CFG_getConfigUI(id, configInfo) {
	var jq, role, assembleType, _JQUERY;
	assembleType = CFG_getInputParamValue(configInfo, "assembleType");
	if (assembleType === "3") {
		_JQUERY = window.opener.$;
	} else {
		_JQUERY = window.$;
	}
	jq = _JQUERY("#" + id);
	role = jq.attr("component-role");
	return _JQUERY.data(elem, "config-" + role);
}
