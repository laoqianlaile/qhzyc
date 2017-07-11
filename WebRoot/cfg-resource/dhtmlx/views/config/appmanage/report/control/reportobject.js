/**
 * 报表控件 object 标签
 */
function writeCellWeb() {
	var clsid = "3F166327-8030-4881-8BD2-EA25350E574A";
	var version = "65536", extentx="9710", extenty="4842", stockProps="0", wmode="opaque";
	if (_isIE || _isChrome) {
		// IE
		document.write("<OBJECT ID=\"CellWeb\" " +
			"CODEBASE=\"cellweb5.cab\" " +
			"STYLE=\"height: 100%;width: 100%;\"  " +
			"CLASSID=\"clsid:" + clsid + "\">" +
				"<PARAM NAME=\"_Version\" VALUE=\"" + version + "\">" +
				"<PARAM NAME=\"_ExtentX\" VALUE=\"" + extentx + "\">" +
				"<PARAM NAME=\"_ExtentY\" VALUE=\"" + extenty + "\">" +
				"<PARAM NAME=\"_StockProps\" VALUE=\"" + stockProps + "\">" +
				"<PARAM NAME=\"Wmode\" VALUE=\"" + wmode + "\">" +
			"</OBJECT>");
	} else {
		// OTHER
		document.write("<OBJECT TYPE=\"application/x-itst-activex\" " +
				"ID=\"CellWeb\" " +
				"CODEBASE=\"cellweb5.cab\" " +
				"STYLE=\"height: 100%;width: 100%;\" " +
				"CLSID=\"{" + clsid + "}\" " +
				"PARAM__Version=\"" + version + "\" " +
				"PARAM__ExtentX=\"" + extentx + "\" " +
				"PARAM__ExtentY=\"" + extenty + "\" " +
				"PARAM__StockProps=\"" + stockProps + "\" " +
				"PARAM_wmode=\"" + wmode + "\"></OBJECT>");
	}
}
/**
 * 菜单控件 object 标签
 */
function writeCellMenu() {
	var clsid = "F82DB98D-842D-4DAB-9312-E478D8255720";
	var version = "65536", extentx="24527", extenty="503", stockProps="0";
	
	if (_isIE || _isChrome) {
		document.write("<OBJECT ID=\"Menu1\" " +
				"CODEBASE=\"Menu.ocx\" " + 
				"STYLE=\"HEIGHT: 20px;width: 100%;\" " + 
				"CLASSID=\"clsid:" + clsid + "\">" +
					"<PARAM NAME=\"_Version\" VALUE=\"" + version + "\">" +
					"<PARAM NAME=\"_ExtentX\" VALUE=\"" + extentx + "\">" + 
					"<PARAM NAME=\"_ExtentY\" VALUE=\"" + extenty + "\">" +
					"<PARAM NAME=\"_StockProps\" VALUE=\"" + stockProps + "\">" +
				"</OBJECT>");
	} else {
		document.write("<OBJECT ID=\"Menu1\" " +
					"TYPE=\"application/x-itst-activex\" " +
					"CODEBASE=\"Menu.ocx\" " + 
					"STYLE=\"HEIGHT: 20px;width: 100%;\" " + 
					"CLASSID=\"clsid:" + clsid + "\" " +
					"PARAM__Version=\"" + version + "\" " +
					"PARAM__ExtentX=\"" + extentx + "\" " + 
					"PARAM__ExtentY=\"" + extenty + "\" " +
					"PARAM__StockProps=\"" + stockProps + "\">" +
				"</OBJECT>");
	}
}
/**
 * 初始化字体下拉框
 */
function initFontname(){
	strFontnames = CellWeb.GetDisplayFontnames();
	var arrFontname = strFontnames.split('|');
	arrFontname.sort();
	var i;
	var sysFont;
	if( CellWeb.GetSysLangID () == 2052) {
		sysFont = "宋体";
	} else {
		sysFont = "Arial";
	}
	var options = [];

	for( i =0; i < arrFontname.length;i++ ) {
		var option = [arrFontname[i],arrFontname[i]];
		options.push(option);
	}
	fontNameCombo.addOption(options);
	
	if (null != fontNameCombo.getOption(sysFont)) {
		fontNameCombo.setComboValue(sysFont);
	}
	
	fontNameCombo.attachEvent("onChange", function() {
		changeFontName( fontNameCombo.getActualValue() );
	});  
}

/**
 * 报表工具条
 */
function initReportToolbar() {
	var _this = this;
	var reportImagePath = DHX_RES_PATH + "/views/config/appmanage/report/images/";
	// first tool bar 
	var ttbar_1 = new dhtmlXToolbarObject("reporttoolbar$1$div", Skin); 
	ttbar_1.setIconPath(reportImagePath);
	ttbar_1._addItem({id:"cmdFileNew", type:"button", title:"新建", img:"new.gif"}, 0);
	ttbar_1._addItem({id:"cmdFileOpen", type:"button", title:"打开", img:"open.gif"}, 1);
	ttbar_1._addItem({id:"cmdFileSave", type:"button", title:"保存", img:"save.gif"}, 2);
	ttbar_1._addItem({id:"cmdFilePageSetup", type:"button", title:"打印设置", img:"filePageSetup.gif"}, 3);
	ttbar_1._addItem({id:"cmdFilePrint", type:"button", title:"打印", img:"print.gif"}, 4);
	ttbar_1._addItem({id:"cmdFilePrintPreview", type:"button", title:"打印预览", img:"printpreview.gif"}, 5);
	ttbar_1._addItem({id:"ttbar$separator$01", type:"separator"}, 6);
	ttbar_1._addItem({id:"cmdEditCut", type:"button", title:"剪切", img:"cut.gif"}, 7);
	ttbar_1._addItem({id:"cmdEditCopy", type:"button", title:"复制", img:"copy.gif"}, 8);
	ttbar_1._addItem({id:"cmdEditPaste", type:"button", title:"粘贴", img:"paste.gif"}, 9);
	ttbar_1._addItem({id:"cmdEditFind", type:"button", title:"查找", img:"find.gif"}, 10);
	ttbar_1._addItem({id:"ttbar$separator$02", type:"separator"}, 11);
	ttbar_1._addItem({id:"cmdEditUndo", type:"button", title:"撤消", img:"undo.gif"}, 12);
	ttbar_1._addItem({id:"cmdEditRedo", type:"button", title:"重做", img:"redo.gif"}, 13);
	ttbar_1._addItem({id:"ttbar$separator$03", type:"separator"}, 14);
	ttbar_1._addItem({id:"cmdViewFreeze", type:"button", title:"不滚动区域", img:"freeze.gif"}, 15);
	ttbar_1._addItem({id:"cmdFormatPainter", type:"button", title:"格式刷", img:"painter.gif"}, 16);
	ttbar_1._addItem({id:"cmdSortAscending", type:"button", title:"升序排列", img:"sorta.gif"}, 17);
	ttbar_1._addItem({id:"cmdSortDescending", type:"button", title:"降序排列", img:"sortd.gif"}, 18);
	ttbar_1._addItem({id:"ttbar$separator$04", type:"separator"}, 19);
	ttbar_1._addItem({id:"cmdFormulaInput", type:"button", title:"输入公式", img:"formula.gif"}, 20);
	ttbar_1._addItem({id:"cmdFormulaSerial", type:"button", title:"填充单元公式序列", img:"formulaS.gif"}, 21);
	ttbar_1._addItem({id:"ttbar$separator$05", type:"separator"}, 22);
	ttbar_1._addItem({id:"cmdFormulaSumH", type:"button", title:"水平求和", img:"sumh.gif"}, 23);
	ttbar_1._addItem({id:"cmdFormulaSumV", type:"button", title:"垂直求和", img:"sumv.gif"}, 24);
	ttbar_1._addItem({id:"cmdFormulaSumHV", type:"button", title:"双向求和", img:"sum.gif"}, 25);
	ttbar_1._addItem({id:"ttbar$separator$06", type:"separator"}, 27);
	ttbar_1._addItem({id:"cmdChartWzd", type:"button", title:"图表向导", img:"chartw.gif"}, 28);
	ttbar_1._addItem({id:"cmdInsertPic", type:"button", title:"插入图片", img:"insertpic.gif"}, 29);
	ttbar_1._addItem({id:"cmdHyperlink", type:"button", title:"超级链接", img:"hyperlink.gif"}, 30);
	ttbar_1._addItem({id:"cmdWzdBarcode", type:"button", title:"条形码向导", img:"barcode.gif"}, 31);
	ttbar_1._addItem({id:"ttbar$separator$07", type:"separator"}, 32);
	// view scale combo select
	ttbar_1._addItem({id:"cmdViewScale", type:"divElement", title:"显示比例"}, 33);
	var scaleCombo = new dhtmlXCombo("cmdViewScale","viewScaleSelect",80);
	scaleCombo.addOption([["200","200%"],["150","150%"],["120","120%"],["110","110%"],["100","100%"],
		["90","90%"],["80","80%"],["70","70%"],["60","60%"],["50","50%"],["40","40%"],["30","30%"],["20","20%"],["10","10%"],
		["5","5%"],["3","3%"],["1","1%"]]);
	scaleCombo.setComboValue("100");
	scaleCombo.attachEvent("onChange", function() {
		changeViewScale(scaleCombo.getSelectedValue());
	});
	ttbar_1._addItem({id:"ttbar$separator$08", type:"separator"}, 34);
	ttbar_1._addItem({id:"cmdShowGridline", type:"button", title:"显示/隐藏背景网格线", img:"gridline.gif"}, 35);
	ttbar_1._addItem({id:"cmdVPagebreak", type:"button", title:"垂直分隔线", img:"vpagebreak.gif"}, 36);
	ttbar_1._addItem({id:"cmdHPagebreak", type:"button", title:"水平分隔线", img:"hpagebreak.gif"}, 37);
	ttbar_1._addItem({id:"cmdShowPagebreak", type:"button", title:"显示/隐藏分隔线", img:"pagebreak.gif"}, 38);
	ttbar_1._addItem({id:"ttbar$separator$09", type:"separator"}, 39);
	ttbar_1._addItem({id:"cmdAbout", type:"button", title:"关于华表插件", img:"about.gif"}, 40);
	ttbar_1._addItem({id:"ttbar$separator$10", type:"separator"}, 41);

	// second tool bar 
	var ttbar_2 = new dhtmlXToolbarObject("reporttoolbar$2$div", Skin); 
	ttbar_2.setIconPath(reportImagePath);
	// font name combo select(global variable, define in reportdefine.jsp)
	ttbar_2._addItem({id:"cmdFontName", type:"divElement"}, 0);
	fontNameCombo = new dhtmlXCombo("cmdFontName","FontNameSelect", 100);
	ttbar_2._addItem({id:"cmdFontSize", type:"divElement", title:"字号"}, 1);
	var fontSizeCombo = new dhtmlXCombo("cmdFontSize","FontSizeSelect", 70);
	fontSizeCombo.addOption([["5", "5"],["6", "6"],["7", "7"],["8", "8"],["9", "9"],["10", "10"],
			["11", "11"],["12", "12"],["14", "14"],["16", "16"],["18", "18"],["20", "20"],
			["22", "22"],["24", "24"],["26", "26"],["28", "28"],["30", "30"],
			["36", "36"],["42", "42"],["48", "48"],["72", "72"],["100", "100"],
			["150", "150"],["300", "300"],["500", "500"],["800", "800"],["1200", "1200"],["2000", "2000"]]);
	fontSizeCombo.setComboValue("10");
	fontSizeCombo.attachEvent("onChange", function() {
		changeFontSize(fontSizeCombo.getSelectedValue());
	});
	
	ttbar_2._addItem({id:"ttbar$separator$01", type:"separator"}, 2);
	ttbar_2._addItem({id:"cmdBold", type:"button", title:"粗体", img:"bold.gif"}, 3);
	ttbar_2._addItem({id:"cmdItalic", type:"button", title:"斜体", img:"italic.gif"}, 4);
	ttbar_2._addItem({id:"cmdUnderline", type:"button", title:"下划线", img:"underline.gif"}, 5);
	ttbar_2._addItem({id:"cmdBackColor", type:"button", title:"背景色", img:"backcolor.gif"}, 6);
	ttbar_2._addItem({id:"cmdForeColor", type:"button", title:"前景色", img:"forecolor.gif"}, 7);
	ttbar_2._addItem({id:"ttbar$separator$02", type:"separator"}, 8);
	ttbar_2._addItem({id:"cmdWordWrap", type:"button", title:"自动折行", img:"wordwrap.gif"}, 9);
	ttbar_2._addItem({id:"cmdAlignLeft", type:"button", title:"居左对齐", img:"alignleft.gif"}, 10);
	ttbar_2._addItem({id:"cmdAlignCenter", type:"button", title:"居中对齐", img:"aligncenter.gif"}, 11);
	ttbar_2._addItem({id:"cmdAlignRight", type:"button", title:"居右对齐", img:"alignright.gif"}, 12);
	ttbar_2._addItem({id:"cmdAlignTop", type:"button", title:"居上对齐", img:"aligntop.gif"}, 13);
	ttbar_2._addItem({id:"cmdAlignMiddle", type:"button", title:"垂直居中", img:"alignmiddle.gif"}, 14);
	ttbar_2._addItem({id:"cmdAlignBottom", type:"button", title:"居下对齐", img:"alignbottom.gif"}, 15);
	ttbar_2._addItem({id:"ttbar$separator$03", type:"separator"}, 16);
	ttbar_2._addItem({id:"cmdBoderType", type:"divElement", title:"边框类型"}, 17);
	var boderTypeCombo = new dhtmlXCombo("cmdBoderType","BorderTypeSelect", 85);
	boderTypeCombo.addOption([
        ["2", "细线"],
        ["3", "中线"],
        ["4", "粗线"],
        ["5", "划线"],
        ["6", "点线"],
        ["7", "点划线"],
		["8", "点点划线"],
		["9", "粗划线"],
		["10", "粗点线"],
		["11", "粗点划线"],
		["12", "粗点点划线"]]);
	boderTypeCombo.setComboValue("2");
	boderTypeCombo.attachEvent("onChange", function() {
		//changeFontSize(boderTypeCombo.getSelectedValue());
	});
	
	ttbar_2._addItem({id:"cmdDrawBorder", type:"button", title:"画边框线", img:"border.gif"}, 18);
	ttbar_2._addItem({id:"cmdEraseBorder", type:"button", title:"抹边框线", img:"erase.gif"}, 19);
	ttbar_2._addItem({id:"cmdCurrency", type:"button", title:"货币符号", img:"currency.gif"}, 20);
	ttbar_2._addItem({id:"cmdPercent", type:"button", title:"百分号", img:"percent.gif"}, 21);
	ttbar_2._addItem({id:"cmdThousand", type:"button", title:"千分位", img:"thousand.gif"}, 22);
	ttbar_2._addItem({id:"ttbar$separator$04", type:"separator"}, 23);
	
	

	var ttbar_3 = new dhtmlXToolbarObject("reporttoolbar$3$div", Skin); 
	ttbar_3.setIconPath(reportImagePath);
	ttbar_3._addItem({id:"cmdInsertCol", type:"button", title:"插入列", img:"insertcol.gif"}, 19);
	ttbar_3._addItem({id:"cmdInsertRow", type:"button", title:"插入行", img:"insertrow.gif"}, 20);
	ttbar_3._addItem({id:"cmdAppendCol", type:"button", title:"追加列", img:"appendcol.gif"}, 21);
	ttbar_3._addItem({id:"cmdAppendRow", type:"button", title:"追加行", img:"appendrow.gif"}, 22);
	ttbar_3._addItem({id:"ttbar$separator$04", type:"separator"}, 23);
	ttbar_3._addItem({id:"cmdDeleteCol", type:"button", title:"删除列", img:"deletecol.gif"}, 20);
	ttbar_3._addItem({id:"cmdDeleteRow", type:"button", title:"删除行", img:"deleterow.gif"}, 21);
	ttbar_3._addItem({id:"cmdSheetSize", type:"button", title:"表页尺寸", img:"sheetsize.gif"}, 22);
	ttbar_3._addItem({id:"ttbar$separator$04", type:"separator"}, 23);
	ttbar_3._addItem({id:"cmdMergeCell", type:"button", title:"组合单元格", img:"mergecell.gif"}, 19);
	ttbar_3._addItem({id:"cmdUnMergeCell", type:"button", title:"取消单元格组合", img:"unmergecell.gif"}, 20);
	ttbar_3._addItem({id:"cmdMergeRow", type:"button", title:"行组合", img:"mergerows.gif"}, 21);
	ttbar_3._addItem({id:"cmdMergeCol", type:"button", title:"列组合", img:"mergecols.gif"}, 22);
	ttbar_3._addItem({id:"ttbar$separator$04", type:"separator"}, 23);
	ttbar_3._addItem({id:"cmdReCalcAll", type:"button", title:"重算全表", img:"calculateall.gif"}, 20);
	ttbar_3._addItem({id:"cmdFormulaSum3D", type:"button", title:"设置汇总公式", img:"sum3d.gif"}, 21);
	ttbar_3._addItem({id:"cmdReadOnly", type:"button", title:"单元格只读", img:"readonly.gif"}, 22);
	ttbar_3._addItem({id:"ttbar$separator$04", type:"separator"}, 23);
	ttbar_3._addItem({id:"cmdFillType", type:"divElement", title:"填充方式"}, 24);
	var fillTypeCombo = new dhtmlXCombo("cmdFillType","FillTypeSelect", 80);
	fillTypeCombo.addOption([
         ["1", "向下填充"],
         ["2", "向右填充"],
         ["4", "向上填充"],
         ["8", "向左填充"],
         ["16", "重复填充"],
         ["32", "等差填充"],
         ["64", "等比填充"]]);
	fillTypeCombo.setComboValue("1");
	fillTypeCombo.attachEvent("onChange", function() {
		changeFillType(fillTypeCombo.getSelectedValue());
	});
	
	ttbar_3._addItem({id:"cmdFillSerial", type:"button", title:"序列填充", img:"fillserial.gif"}, 25);
	ttbar_3._addItem({id:"ttbar$separator$05", type:"separator"}, 26);
	ttbar_3._addItem({id:"cmdDateType", type:"divElement", title:"日期类型"}, 27);
	var dataTypeCombo = new dhtmlXCombo("cmdDateType","DateTypeSelect", 160);
	dataTypeCombo.addOption([
	    ["1", "1997-3-4"],
	    ["2", "1997-03-04 13:30:12"],
	    ["3", "1997-3-4 1:30 PM"],
	    ["4", "1997-3-4 13:30"],
	    ["5", "97-3-4"],
	    ["6", "3-4-97"],
	    ["7", "03-04-97"],
	    ["8", "3-4"],
	    ["9", "一九九七年三月四日"],
	    ["10", "一九九七年三月"],
	    ["11", "三月四日"],
	    ["12", "1997年3月4日"],
	    ["13", "1997年3月"],
	    ["14", "3月4日"],
	    ["15", "星期二"],
	    ["16", "二"],
	    ["17", "4-Mar"],
	    ["18", "4-Mar-97"],
	    ["19", "04-Mar-97"],
	    ["20", "Mar-97"],
	    ["21", "March-97"],
	    ["22", "1997-03-04"]]);
	dataTypeCombo.setComboValue("1");
	dataTypeCombo.attachEvent("onChange", function() {
		changeDateType(dataTypeCombo.getSelectedValue());
	});
	
	ttbar_3._addItem({id:"cmdTimeType", type:"divElement", title:"时间类型"}, 28);
	var timeTypeCombo = new dhtmlXCombo("cmdTimeType","TimeTypeSelect", 120);
	timeTypeCombo.addOption([
 	    ["1", "1:30"],
	    ["2", "1:30 PM"],
	    ["3", "13:30:00"],
	    ["4", "1:30:00 PM"],
	    ["5", "13时30分"],
	    ["6", "13时30分00秒"],
	    ["7", "下午1时30分"],
	    ["8", "下午1时30分00秒"],
	    ["9", "十三时三十分"],
	    ["10", "下午一时三十分"]]);
	timeTypeCombo.setComboValue("1");
	timeTypeCombo.attachEvent("onChange", function() {
		changeTimeType(timeTypeCombo.getSelectedValue());
	});
	
	ttbar_3._addItem({id:"ttbar$separator$06", type:"separator"}, 29);
	
	/********************************************************************/
	ttbar_1.attachEvent("onClick", function(itemId){
		_this.toolbarClick(ttbar_1, itemId);
	});
	ttbar_2.attachEvent("onClick", function(itemId){
		_this.toolbarClick(ttbar_2, itemId);
	});
	ttbar_3.attachEvent("onClick", function(itemId){
		_this.toolbarClick(ttbar_3, itemId);
	});
	
	this.toolbarClick = function(ttbar, itemId) {
		var type = ttbar.getType(itemId);
		if ("button" == type) {
			if(window.event != null) {
				window.event.cancelBubble = true;
			}
			// Regular push button
			onCbClick(itemId, true);
			return(false);
		}
	};
	/********************************************************************/
}
/**
 * 
 */
function initObj() {
	// init menu
	//var href = basePath + "cfg-resource/views/config/appmanage/report/emenu.clm";
	//Menu1.ReadMenuFromRemoteFile(href);
	// set widht and height
	var tbarObj = document.getElementById("reporttoolbar$3$div");
	var cellWebObj = document.getElementById("cellweb$div");
	var accordionObj = document.getElementById("accordion$div");
	var cw = document.body.clientWidth - 260 - 24 + 13;
	var ch = document.body.offsetHeight - 106 + 19;
	CellWeb.Login("中信报表", "", 13040409, "5160-0447-0112-5004");
	CellWeb.style.width = cw;
	cellWebObj.style.width = cw + "px";
	CellWeb.style.height = ch;
	cellWebObj.style.height = ch + "px";
	accordionObj.style.height = ch + "px";
	accordionObj.style.display = "";
	// bind event
	if (window.attachEvent) {
		/*Menu1.attachEvent('ClickMenuItem', function(name) {
			if (name == "FileSave") {
				CellUtil.save();
			}
		});*/
		/*CellWeb.attachEvent("SelChanged", function(oc, or, nc, nr) {
			if (curSelect) {
				curSelect.collapse();
				curSelect.fireEvent("blur");
				curSelect = null;
			}
		});//*/
		CellWeb.attachEvent("KeyDown", function(keyCode, shift) {
			//alert(keyCode+"=============================");
			if (keyCode == 46) {
				CellUtil.cancelField();
			}
		});
	} else {
		/*Menu1.addEventListener("ClickMenuItem", function(name) {
			//alert(name);
			if (name == "FileSave") {
				CellUtil.save();
			}
		}, false);*/
		//alert(CellWeb.KeyDown);
		CellWeb.addEventListener("KeyDown", function(keyCode, shift) {
			//alert(keyCode+"=============================");
			if (keyCode == 46) {
				CellUtil.cancelField();
			}
		}, false);
	}
	
	// font name
	initFontname();
	// 
	var rlt = CellWeb.OpenFile(cllPath, "");
	//alert(rlt);
	
}


function initAccordion() {
	var _this = this;
	this.bInitDs = this.bInitPage = this.bInitSort = this.bInitCond = this.bInitGroup = false;
	
	var dhxAccord = new dhtmlXAccordion("accordion$div", Skin);
	
	var ds = dhxAccord.addItem("acc$ds", "数据源");
	var page = dhxAccord.addItem("acc$page", "页面设置");
	var cond = dhxAccord.addItem("acc$cond", "条件设置");
	var group= dhxAccord.addItem("acc$group", "分组设置");
	var sort = dhxAccord.addItem("acc$sort", "排序设置");
	dhxAccord.openItem("acc$ds");
	initDs(ds, _this);
	
	dhxAccord.setSizes();
	
	this.doInitActive = function(itemId) {
		if ("acc$ds" == itemId && !_this.bInitDs) {
			initDs(ds, _this); 
		} else if ("acc$page" == itemId && !_this.bInitPage) {
			initPage(page, _this); 
		} else if ("acc$sort" == itemId && !_this.bInitSort) {
			initSetting(sort, 3, _this);
		} else if ("acc$cond" == itemId && !_this.bInitCond) {
			initSetting(cond, 1, _this);
		} else if ("acc$group" == itemId && !_this.bInitGroup) {
			initSetting(group, 2, _this);
		} 
		return true;
	};
	dhxAccord.attachEvent("onBeforeActive", _this.doInitActive);

}
/**
 * CELL报表数据源树
 * @param ds
 */
function initDs(ds, that) {
	var _this = this;

	/*var sbar = ds.attachStatusBar();
	var tbar = new dhtmlXToolbarObject(sbar.id, Skin);
	tbar.addDiv("op$div",0);
	var combo = new dhtmlXCombo("op$div","operator",80);
	var opts = [["+", "加"],
	            ["-", "减"],
	            ["*", "乘"],
	            ["/", "除"],
	            ["+|", "前连接"],
	            ["|+", "后连接"],
	            ["**", "补位"]];
	combo.addOption(opts);
	combo.attachEvent("onSelectionChange", function() {
		
	});
	tbar.addInput("value",1,"",80);//*/
	
	var ttbar = ds.attachToolbar();
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addButton("ds$save", 0, "保存", "save.gif");
	ttbar.addSeparator("ds$septr$01", 1);
	ttbar.addButton("ds$cancel", 2, "取消绑定", "cancel.gif");
	ttbar.addSeparator("ds$septr$02", 3);
	ttbar.addButton("ds$view", 4, "查看绑定", "view.gif");
	ttbar.attachEvent("onClick", function(id){
		if (id == "ds$save") {
			CellUtil.save();
		} else if (id == "ds$cancel") {
			CellUtil.cancelField();
		} else if (id == "ds$view") {
			CellUtil.viewBinded();
		}
	});
	// 这几个分类是需求中固定的
	var treeJson = {id:0, item:[
        {id:'-1',text:"报表数据源", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child:1, type:"ROOT"}
    ]};
	var tree = ds.attachTree();
	tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	var treeUrl = AppActionURI.reportDataSource + "!tree.json?E_model_name=tree&F_in=text,child&P_UD=type&P_reportId=" + reportId;
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	tree.setXMLAutoLoading(treeUrl);
	tree.loadJSONObject(treeJson);
	tree.refreshItem("-1");
	//tree.loadJSON(treeUrl + "&id=0");
	
	tree.attachEvent("onClick", function(nId) {
		var type = tree.getAttribute(nId,"type");
		if ("C" != type) return;
		var text = tree.getItemText(nId);
		CellUtil.bindedField(nId, text);
	});
	//把数据源设置初始化标识置为TRUE
	that.bInitDs = true;
}
/**
 * 页面设置
 */
function initPage(page, that) {
	var _this = this;
	var moduleUrl = AppActionURI.reportDefine;
	var caption = "<p>表头、表尾、循环、末页行可以不止一行，但必须是连续行号</p>" +
			"<p>设置格式为：起始行号 到 终止行号</p>" + 
			"<p>如要设置第1行和第2行为表头，则在表头行号：前面文本框输入1，后面文本框输入2</p>";
	var formJson = [{type : "settings", position : "label-left", 
		labelWidth : 60, inputWidth : 50, labelAlign : "left", offsetTop:"5"
    }, {type: "block", list:[
         	{type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "reportId", value: reportId},
            {type: "hidden", name: "rnRowIndex"},
            {type: "hidden", name: "rnColIndex"}
        ]
    }, {type: "fieldset", name: "page", label: "页面设置", width: 230, list:[
            {type: "input", label: "表头行号", name: "headerStart", validate:"ValidNumeric", maxLength:3, offsetLeft: 5},
            {type: "input", label: "循环行号", name: "cycleStart", validate:"ValidNumeric", maxLength:3, offsetLeft: 5},
            {type: "input", label: "表尾行号", name: "tailStart", validate:"ValidNumeric", maxLength:3, offsetLeft: 5},
            {type: "input", label: "末页行号", name: "lastStart", validate:"ValidNumeric", maxLength:3, offsetLeft: 5},
            {type: "newcolumn"},
            {type: "input", label: "到", labelWidth : 15, name: "headerEnd", validate:"ValidNumeric", maxLength:3},
            {type: "input", label: "到", labelWidth : 15, name: "cycleEnd", validate:"ValidNumeric", maxLength:3},
            {type: "input", label: "到", labelWidth : 15, name: "tailEnd", validate:"ValidNumeric", maxLength:3},
            {type: "input", label: "到", labelWidth : 15, name: "lastEnd", validate:"ValidNumeric", maxLength:3},
        ]
    }, {type: "fieldset", name: "caption", label: "说明", width: 230, list:[
            {type: "itemlabel", label: caption, labelWidth: 200}
        ]
    }];
	var form = page.attachForm(formJson);
	that["pageSettingForm"] = form;
	
	var ttbar = page.attachToolbar();
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addButton("page$save", 0, "保存", "save.gif");
	ttbar.attachEvent("onClick", function(id){
		if (id == "page$save") {
			save();
		}
	});
	loadFormData();
	//把页面设置初始化标识置为TRUE
	that.bInitPage = true;
	
	function save () {
		var url = moduleUrl;
		var method = "post";
		var id = form.getItemValue("id");
		if (id != "") {
			url += "/" + id;
			method = "put";
		}
		form.setItemValue("_method", method);
		form.send(url + ".json", "post", function(loader, response) {
			var formData = eval("(" + loader.xmlDoc.responseText + ")");
			if (formData.id) {
				form.setItemValue("id", formData.id);
				alert(getMessage("save_success"));
			} else {
				alert(getMessage("save_failure"));
			}
			
		});
	};
	//
	function loadFormData () {
		var url = moduleUrl + "!edit.json?P_reportId=" + reportId;
		var formData = loadJson(url);
		form.setFormData(formData);
	};
}

/**
 * 条件、分组、排序设置
 */
function initSetting(item, type, that) {
	//TO-DO
	var _this = this;
	var moduleUrl = AppActionURI.reportPrintSetting;
	// tool bar init
	//var layout = item.attachLayout("1C");
	var ttbar = item.attachToolbar();
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addButton("group$save", 1, "保存", "save.gif");
	ttbar.addSeparator("group$septr$01", 2);
	ttbar.addButton("group$new", 3, "添加", "new.gif");
	ttbar.addSeparator("group$septr$02", 4);
	ttbar.addButton("group$delete", 5, "移除", "delete.gif");
	ttbar.attachEvent("onClick", function(id){
		if (id == "group$save") {
			save();
		} else if (id == "group$new") {
			add();
		} else if (id == "group$delete") {
			remove();
		}
	});//*/
	// grid init
	var grid = item.attachGrid();
	var header_1 = "默认值";
	var type_1 = "co";
	if (1 == type) {
		type_1 = "ed";
	}
	if (2 == type) {
		header_1 = "分组方式";
	} else if (3 == type) {
		header_1 = "排序方式";
	}
	var gcfg = {
			format: {
				headers: ["<center>字段名称</center>","<center>" + header_1 + "</center>"],
				cols: ["columnId","value"],
				//userdata: [2, 3],
				colWidths: ["*", "60"],
				colTypes: ["co",type_1],
				colAligns: ["left","left"]
			}
		};
	var gurl = moduleUrl + "!search.json?P_orders=showOrder&Q_EQ_type=" + type + "&Q_EQ_reportId=" + reportId;
	
	var cCombo = grid.getCombo(0);
	cCombo.put("-1", "请选择字段");
	initColumns();
	if (1 != type) {
		var sCombo = grid.getCombo(1);
		sCombo.put("asc", "升序");
		sCombo.put("desc", "降序");
	}
	
	// init grid
	initGridWithoutPageable(grid, gcfg, gurl);
	//grid.enableDragAndDrop(true);
	//把条件设置初始化标识置为TRUE
	if (1 == type) {
		that.bInitCond = true;
	} else if (2 == type) {
		that.bInitGroup = true;
	} else if (3 == type) {
		that.bInitSort = true;
	};
	//
	that["printSettingGrid_" + type] = grid;
	//that["printSettingGcfg_" + type] = gcfg;
	//that["printSettingGurl_" + type] = gurl;

	// 保存
	function save () {
		// 1. 
		grid.forEachRow(function(rId) {
			var columnId = grid.cells(rId, 0).getValue();
			if (columnId == "-1") {
				grid.deleteRow(rId);
			}
		});
		// 2. 
		var rowsNum = grid.getRowsNum();
		if (null == rowsNum || 0 == rowsNum) {
			alert("请先设置，再保存！");
			return;
		}
		// 3. 
		var rowsValue = "";
		for (var i = 0; i < rowsNum; i++) {
			var rId = grid.getRowId(i);
			rowsValue += ";" + grid.cells(rId,0).getValue() + "," + grid.cells(rId,1).getValue();
		}
		rowsValue = rowsValue.substring(1);
		
		var url = moduleUrl + "!save.json?P_type=" + type + "&P_reportId=" + reportId + "&P_rowsValue=" + rowsValue;
		dhtmlxAjax.get(url,function(loader){
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.success) {
		    	alert(getMessage("save_success"));
		    	loadGridData(grid, gcfg, gurl);
		    } else {
		    	alert(getMessage("save_failure"));
		    }
		});
	};
	// 添加
	function add () {
		var rowId = new Date().getTime();
		if (1 == type) {
			grid.addRow(rowId, ["-1", ""]);
		} else {
			grid.addRow(rowId, ["-1", "asc"]);
		}
	};
	// 移除
	function remove () {
		var rowIds = grid.getSelectedRowId();
    	if (null == rowIds) {
    		alert(getMessage("select_record"));
    		return;
    	}
		// grid.deleteSelectedRows();
    	var rowIdArr = rowIds.split(",");
    	var idArr = [];
    	for (var i = 0; i < rowIdArr.length; i++) {
    		if (rowIdArr[i].length == 32) {
    			idArr.push(rowIdArr[i]);
    		}
    	}
    	if (idArr.length > 0) {
    		var url = moduleUrl + "!destroy.json?id=" + idArr.toString();
    		dhtmlxAjax.get(url, function(loader) {});
    	}
		grid.deleteSelectedRows();
	};
	// 初始化下拉列表
	function initColumns () {
		var url = AppActionURI.reportDataSource + "!columnsOfReport.json?Q_reportId=" + reportId;
		var data= loadJson(url);
		if (data) {
			//cCombo.addOption(data);
			for (var i = 0; i < data.length; i++) {
				var col = data[i];
				cCombo.put(col.value, col.text);
			}//*/
		}
	};
};
