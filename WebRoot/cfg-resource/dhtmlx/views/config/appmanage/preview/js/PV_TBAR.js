/**
 * PV => PREVIEW
 *       模块模板
 * B  => tool bar
 *       工具条
 * @param that
 * @param bLayout
 * @param tableId
 *        表ID
 * @param type
 *        1-列表按钮，0-表单按钮
 * @param clickEvent
 *        工具条单击事件
 * @param areaIndex
 *        区域索引位置：如区域一为1， 区域二为2 (标记预留区用的)
 */
function PV_TBAR_init(that, bLayout, tableId, type, clickEvent, areaIndex) {
	var _this = this;
	
	/** 当前布局的所有按钮数据.*/
	function PV_TBAR_buttons() {
		var url = PV_app_uri + "/app-button!dhtmlxToolbar.json?P_tableId=" + tableId 
			+ "&P_moduleId=" + that.moduleId
			+ "&P_type=" + type;
		return loadJson(url);
	};
	/** 模块的所有打印报表.*/
	function PV_TBAR_reports() {
		var url = PV_app_uri + "/report-binding!bindedReports.json?P_tableId=" + tableId + "&P_moduleId=" + that.moduleId;
		return loadJson(url);
	};
	
	var btns = PV_TBAR_buttons();
	/*if (null == btns || btns.length == 0) {
		return null;
	}//*/
	
	bLayout.detachToolbar();
	var tbar = bLayout.attachToolbar();	
	tbar.setIconPath(TOOLBAR_IMAGE_PATH);
	var pos = 0;
	for(var i = 0; i < btns.length; i++) {
		var btn = btns[i];
		if (PV_common.B_BUTTON == btn.type) {
			if (PV_common.P_REPORT == btn.id) {
				tbar.addButtonSelect(btn.id, btn.pos, btn.name, [],btn.image);
				var reports = PV_TBAR_reports();
				for (var j = 0; j < reports.length; j++) {
					var report = reports[j];
					tbar.addListOption(btn.id, (PV_common.P_REPORT_PRE + reports[j][0]), j, "button",reports[j][1], "print.gif");
				}
			} else {
				tbar.addButton(btn.id, btn.pos, btn.name, btn.image);
			}
		} else if (PV_common.B_SEPARATOR == btn.type) {
			tbar.addSeparator(btn.id, btn.pos);
		}
		//
		if (pos < btn.pos) pos = btn.pos;
	}
	var btnCnt = 0;
	tbar.forEachItem(function(itemId) {
		btnCnt ++;
	});
	if (btnCnt == 0) {
		bLayout.detachToolbar(); tbar = null;
	} else {
		tbar.attachEvent("onClick", function(id) {
			if (typeof clickEvent == "function") {
				buttonType=0;
				clickEvent(id);
			}
		});
	}
	
	return tbar;
}

