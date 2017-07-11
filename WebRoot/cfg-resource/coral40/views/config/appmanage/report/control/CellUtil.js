function CellUtil() {

	var cell = document.getElementById("CellWeb");
	
	var moduleUrl = $.contextPath + "/appmanage/report-define";
	
	return {
		/** 模块访问URL*/
		moduleUrl: moduleUrl,
		/** 报表id*/
		reportId: params.reportId,
		/** CELL报表控件对象*/
		cell: cell,
		/** CELL报表中绑定的字段*/
		fields: {},
		/** CELL报表中绑定的字段*/
		BINDED_MSG: "绑定字段",
		
		setCell : function(cell) {
			CellUtil.cell = cell;
		},
		/**
		 * 跳转到下一个单元格
		 * @returns
		 */
		pre : function() {
			with (CellUtil.cell) {
				// alert("col = " + GetCurrentCol() + ", row = " + GetCurrentRow());
				// cell row and col index start with 1
				if (GetCurrentCol() == 1 && GetCurrentRow() > 1) {
					MoveToCell(GetCols(0) - 1, 1);
				} else {
					MoveToCell(GetCurrentCol() - 1, GetCurrentRow());
				}
			}
		},
		/**
		 * 跳转到下一个单元格
		 * @returns
		 */
		next : function() {
			with (CellUtil.cell) {
				if (GetCurrentCol() == GetCols(0) - 1) {
					MoveToCell(1, GetCurrentRow() + 1);
				} else {
					MoveToCell(GetCurrentCol() + 1, GetCurrentRow());
				}
			}
		},
		/**
		 * 向指定位置字段显示值
		 * @param text
		 * @param col
		 * @param row
		 * @returns
		 */
		setPositionText : function(text, col, row) {
			if (null !=text && "" != text) {
				text = "[" + text + "]";
			} else {
				text = "";
			}
			CellUtil.cell.S(col, row, 0, text);
		},
		/**
		 * 设置绑定字段显示值
		 * @param text
		 * @param isNext
		 *        true  向后一个单元格移动
		 *        false 向前一个单元格移动
		 * @returns
		 */
		setText : function(text, isNext) {
			var col = CellUtil.cell.GetCurrentCol();
			var row = CellUtil.cell.GetCurrentRow();
			CellUtil.setPositionText(text, col, row);
			if (isNext == true) {
				CellUtil.next();
			} else {
				CellUtil.pre();
			}
			
		},
		/**
		 * cll文件上传
		 * @returns
		 */
		cllUpload : function() {
			with (CellUtil.cell) {
				var uploadUrl = this.moduleUrl + "!cllUpload?P_reportId=" + reportId;
				var status = UploadFile(uploadUrl);
				if (status == 1) {
					alert("定制报表成功！");
				}
			}
		},
		/**
		 * 绑定一个字段
		 * @param id
		 * @returns
		 */
		addField : function(id) {
			var col = CellUtil.cell.GetCurrentCol();
			var row = CellUtil.cell.GetCurrentRow();
			var prop = CellUtil.fieldProp(row, col);
			CellUtil.fields[prop] = {id: id, row: row, col: col};
			//CellUtil.viewFieldsProp();
		},
		/**
		 * 取消字段绑定
		 * @returns
		 */
		delField : function() {
			var col  = CellUtil.cell.GetCurrentCol();
			var row  = CellUtil.cell.GetCurrentRow();
			var prop = CellUtil.fieldProp(row, col);
			delete CellUtil.fields[prop];
			//CellUtil.viewFieldsProp();
		},
		/**
		 * 绑定字段
		 * @param text
		 * @returns
		 */
		bindedField : function(id, text) {
			CellUtil.addField(id);
			CellUtil.setText(text, true);
		},
		/**
		 * 取消字段
		 * @returns
		 */
		cancelField : function() {
			CellUtil.delField();
			CellUtil.setText("", false);
		},
		/**
		 * 保存CELL报表配置
		 * @returns
		 */
		save : function() {
			var fieldsInfo = "";
			for (var p in CellUtil.fields) {
				var field = CellUtil.fields[p];
				fieldsInfo += ";" + field.id + "," + field.row + "," + field.col;
			}
			if (fieldsInfo.length > 1) {
				fieldsInfo = fieldsInfo.substring(1);
			}
			var url = this.moduleUrl + "!save?P_reportId=" + CellUtil.reportId + "&P_fields=" + fieldsInfo;
			dhtmlxAjax.get(url, function(loader) {
				var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
				if (jsonObj.status) {
					CellUtil.cllUpload();
				} else {
					alert("定制报表失败！");
				}
			});
		},
		/**
		 * 重置报表
		 * @returns
		 */
		reset : function() {
			CellUtil.fields = {};
		},
		/**
		 * 初始化字段绑定信息
		 * @returns
		 */
		initBinded : function() {
			var url = this.moduleUrl + "!getBindedColumns.json?P_reportId=" + CellUtil.reportId;
			dhtmlxAjax.get(url, function(loader) {
				var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
				//alert("len: " + jsonObj.length);
				if (null != jsonObj) {
					for(var i = 0; i < jsonObj.length; i++) {
						var one = jsonObj[i];
						var prop = CellUtil.fieldProp(one.rowIndex, one.colIndex);
						CellUtil.fields[prop] = {id: one.reportId, row: one.rowIndex, col: one.colIndex};
						//alert(one.columnComment + ": " + one.colIndex + "," + one.rowIndex);
						CellUtil.setPositionText(one.columnComment, one.colIndex, one.rowIndex);
					}
					//CellUtil.test();
				}
			});
		},
		
		/**
		 * 字段属性名称
		 * @param rowIndex
		 *        行索引
		 * @param colIndex
		 *        列索引
		 * @returns
		 */
		fieldProp : function(rowIndex, colIndex) {
			return "P_" + rowIndex + "_" + colIndex;
		},
		
		/** 测试字段绑定信息 */
		test : function() {
			for (var p in CellUtil.fields) {
				alert("id=" + CellUtil.fields[p].id + 
						", col=" + CellUtil.fields[p].col + 
						", row=" + CellUtil.fields[p].row);
			}
		}
		
	};
};