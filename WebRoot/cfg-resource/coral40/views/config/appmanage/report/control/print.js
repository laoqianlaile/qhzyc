/**
 * 报表控件 object 标签
 */
function writeCellWeb() {
	var clsid = "3F166327-8030-4881-8BD2-EA25350E574A",
	    version = "65536", extentx="9710", extenty="4842", stockProps="0", wmode="opaque",
	    html = null;
	if ($.browser.msie || $.browser.chrome) {
		// IE
		html = ("<OBJECT ID=\"CellWeb\" " +
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
		html = ("<OBJECT TYPE=\"application/x-itst-activex\" " +
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
	
	$("#_cell_body_").append(html);
}
/**
 * 报表工具条
 */
function initReportToolbar() {
	$("#_rpt_tbar_").toolbar( {
		onClick: function (e, ui) {
			e.stopPropagation();    
			onCbClick(ui.id, true);
			return false;
		},
		data : [{
			id : "cmdFilePrint",
			type : "button",
			label: "打印"
		}, {
			id : "cmdFilePrintPreview",
			type : "button",
			label: "预览"
		}]
	} );
}
/**
 * 初始化报表控件
 */
function initObj() {
	var tbh = $("#_tbar_border_").outerHeight(true),
	    dch = document.body.clientHeight;
	CellWeb.Login("中信报表", "", 13040409, "5160-0447-0112-5004");
	CellWeb.style.height = (dch - tbh) + "px";
	CellWeb.OpenFile(params.cllPath, "");
	CellWeb.ExtendPaste = 1; // 要粘贴的区域超过表格边界,是否扩大寸
}

/**
 * 获取报表数据
 * @returns
 */
function getReportData() {
	var data ,
	    url = $.contextPath + "/appmanage/report-print!getReportData.json?P_reportId=" + params.reportId + 
	    		"&P_tableId=" + params.tableId + 
	    		"&P_rowIds=" + params.rowIds + 
	    		"&P_timestamp=" + params.timestamp;
	data = $.loadJson(url);
	return data;
}
/**
 * 统计打印报表总页数
 * @param reportData
 * @param pageSize
 * @param cycleSize
 * @param rowSize
 * @returns {Number}
 */
function getTotalPages(reportData, pageSize, cycleSize, rowSize) {
	var totalPages=0, i, data, len;
	if (!reportData) return 0;
	if (0 == cycleSize) {
		cycleSize = 1;
	}
	for(i = 0; i < reportData.length; i++) {
		data = reportData[i].data;
		len = data.length;
		totalPages = parseInt(((cycleSize * len) / rowSize), 10);
		if((cycleSize * len) % rowSize != 0) {
			totalPages++;
		}	    		
	}
	return totalPages;
}	

/**
 * 向报表中写数据
 * @param reportData
 * @param pageSize
 * @param cycleSize
 * @param rowSize
 * @returns
 */
function writData(reportData, pageSize, cycleSize, rowSize, rnCell){
	var pageNo = 0, rowIndex, i, j, k, m,
	    group, data, val, row, col, cell, idx = 1;
	if (reportData && reportData.length) {
		for(i = 0; i < reportData.length; i++) {
			group = reportData[i].group;
			data  = reportData[i].data;
			if( null != group) {
		    	for(j = 0; j < group.length; j++) {
		    		val = group[j].val;
		    		row = group[j].row + pageNo * pageSize;
		    		col = group[j].col; 
		    		cellUtil.cell.S(col, row, 0, val);
		    	}
			}
	    	for(k = 0; k < data.length; k++) {
	    		cell = data[k];
	    		if((cycleSize * k) % rowSize == 0) {
	    			pageNo++;
	    			if(null != group) {
		    			for(j = 0; j < group.length; j++) {
				    		val = group[j].val;
				    		row = group[j].row + pageNo * pageSize;
				    		col = group[j].col; 
				    		cellUtil.cell.S(col, row, 0, val);
				    	}
	    			}
	    		}
	    		for(m = 0; m < cell.length; m++) {
	    			val = cell[m].val;
		    		row = cell[m].row;
		    		col = cell[m].col; 
		    		rowIndex = (pageNo - 1) * pageSize + (cycleSize * k) % rowSize + row;
		    		cellUtil.cell.S(col, rowIndex, 0, val);
	    		}	
	    		if (rnCell) { 
	    			// 序号列
	    			cellUtil.cell.S(rnCell.col, ((pageNo - 1) * pageSize + (cycleSize * k) % rowSize + rnCell.row), 0, ("" + idx));
	    			idx++;
	    		}
	    	}	
		}
	}
	return rowIndex;
}

function initReportBody() {
	var url = $.contextPath + "/appmanage/report-print!getReportDefine.json?P_reportId=" + params.reportId,
	    rdData = $.loadJson(url),
	    rpData = getReportData(),
	    rowSize= 0,
	    inrows = cellUtil.cell.GetRows(0)-1,
	    incols = cellUtil.cell.GetCols(0)-1,
	    pageSize = inrows,
		cycleSize= 0,
		lastRows = 0,
		totalPages, i, endRow, rnCell = false;
	if (rdData.rnRowIndex && rdData.rnColIndex) {
		rnCell = {row: rdData.rnRowIndex, col: rdData.rnColIndex};
	}
	// 如果表头没设置，则默认为0
	if (!rdData.headerStart) {
		rdData.headerStart = 0;
	}
	if (!rdData.headerEnd) {
		if (rdData.headerStart == 0) {
			rdData.headerEnd = 0;
		} else {
			rdData.headerEnd = rdData.headerStart + 1;
		}
	}
	// 如果循环开始行号没设，取表头结束行号下一行
	if (!rdData.cycleStart) {
		if (rdData.headerEnd == 0) {
			rdData.cycleStart = 0;
		} else {
			rdData.cycleStart = rdData.headerEnd + 1;
		}
	}
	// 如果循环结束行号没设，取循环开始行号下一行
	if (!rdData.cycleEnd) {
		if (rdData.cycleStart == 0) {
			rdData.cycleEnd = 0;
		} else {
			rdData.cycleEnd = rdData.cycleStart + 1;
		}
	}
	
	// 每页循环打印的行数
	if (rdData.cycleStart) {
		if (rdData.tailStart) {
			rowSize = rdData.tailStart - rdData.cycleStart;
		} else if (rdData.lastStart) {
			rowSize = rdData.lastStart - rdData.cycleStart;
		} else {
			rowSize = cellUtil.cell.GetRows(0) - rdData.cycleStart;
		}	
	} else {
		rowSize = inrows;
	}
	// 未页所占行数
	if (rdData.lastEnd && rdData.lastStart) {
		lastRows  = rdData.lastEnd  - rdData.lastStart  + 1;
	}
	// 一条记录占打印报表的行数
	if (!rdData.cycleStart && !rdData.cycleEnd) {
		cycleSize = inrows;
	} else {
		cycleSize = rdData.cycleEnd - rdData.cycleStart + 1;  
	}
	
	totalPages= getTotalPages(rpData, pageSize, cycleSize, rowSize);
    cellUtil.cell.CopyRange(1, 1, incols, inrows);	    
	for (i = 0; i < totalPages; i++) {
    	cellUtil.cell.Paste(1, inrows * i + 1, 0, 1, 0);
    	if(i < totalPages - 1) {
	    	if(null != rdData.lastEnd || null != rdData.lastStart) {
	    		cellUtil.cell.SetRowHidden(inrows * (i + 1) - lastRows + 1, inrows * (i + 1));
	    	} 	    	
    	}
    }
    endRow = writData(rpData, pageSize, cycleSize, rowSize, rnCell);
    if (lastRows > 1) {
 	 	cellUtil.cell.CopyRange(1, inrows * totalPages - lastRows + 1, incols, inrows * totalPages);
		cellUtil.cell.SetRowHidden(inrows * totalPages - lastRows + 1, inrows * totalPages);
		cellUtil.cell.Paste(1, endRow + 1, 0, 1, 0);  
    }
}

$(function() {
	initReportToolbar();
	writeCellWeb();
	initObj();
	cellUtil = CellUtil();
	
	initReportBody();
});
