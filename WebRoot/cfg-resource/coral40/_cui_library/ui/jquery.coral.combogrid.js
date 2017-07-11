/**
 * 组件库4.0：下拉框
 * 
 * 依赖JS文件:
 *   jquery.coral.core.js
 *   jquery.coral.component.js
 *   jquery.coral.panel.js
 *   jquery.coral.combo.js
 * 	 jquery.coral.tree.js
 *   jquery.coral.grid.js
 */
(function($){	
$.component( "coral.combogrid", $.coral.combo, {
	version: "4.0.3",
	castProperties : ["colNames", "colModel", "data", "buttonOptions", "gridOptions","buttons"],
	options: {
		colNames: [],
		colModel : [],
		valueField: "id",
		panelRenderOnShow: false,
		textField: "name",
		multiple : false,
		selarrrow: [],
		buttons:[],
		url       : null,
		sortable : false,
		data      : [],
		onSelectRow : null,
		onSortableColums : null,
		onLoad     : $.noop, /*数据加载成功*/
		onComplete : null,
		pager: false,
		// add sub button options
		buttonOptions: null,
		gridOptions: {} // grid 额外的参数添加
	}, 
	grid: function () {
		return $("#combo_grid_"+$(this.element).attr('id'));
	},
	/**
	 * 获取combogrid的button组件
	 */
	button: function () {
		if (null !== this.options.buttonOptions) {
			return this.$button;
		}
	},
	_destroy : function () {
		this.element.removeClass("coral-validation-combogrid");
		this.element.removeClass("coral-form-element-combogrid");
		this.grid().remove();
		this._super();
	},
	/**
	 * input search filter
	 */
	_searchFilter: function(keyword) {
		this._removeGridHighlights();
		var that = this,
			opts = this.options,
			textField = this.options.textField,
			sdata = [],
			$grid = this.grid(),
			multiselect = $grid.grid("option", "multiselect");

		this.endSearch = false;
		var fields = $grid.grid("option", "colModel"),
			fieldsRules = "";
		
		for (var i in fields) {
			var item = fields[i];
			if ("cb" == item.name || "rn" == item.name) continue;
			
			if ("" != fieldsRules) {
				fieldsRules += ',';
			}
			fieldsRules += '{"field":"'+ item.name +'","op":"cn","data":"'+ keyword +'"}';
		}
		//sdata['filters'] = '{"groupOp":"AND","rules":[{"field":"'+ textField +'","op":"cn","data":"'+ keyword +'"}]}';
		sdata['filters'] = '{"groupOp":"OR","rules":['+ fieldsRules +']}';
		$grid.grid("option", "localonce", true);
		$.extend($grid.grid("option", "postData"),sdata);
		$grid.grid("reload", {page:1});
		return false;
	},
	/**
	 * reload
	 */
	reload: function(urlOrData) {
		var $grid = this.grid(),
			// TODO: 临时取属性 orgdatatype
			datatypeTemp = $grid.grid("option", "orgdatatype");
		
		if (typeof urlOrData === "string") {
			$grid.grid("option", "url", urlOrData);
			if (datatypeTemp != "json") {
				$grid.grid("option", "datatype", "json");
				$grid.grid("reload");
				$grid.grid("option", "datatype", datatypeTemp);
				return ;
			}
		} else if (typeof urlOrData === "object") {
			if (datatypeTemp != "local") {
				$grid.grid("option", "orgdatatype", "local");
				$grid.grid("clearGridData");
				$grid.grid("addRowData", this.options.valueField, urlOrData);
				$grid.grid("option", "orgdatatype", datatypeTemp);
				return ;
			}
			$grid.grid("option", "data", urlOrData);
		}
		
		$grid.grid("reload");
	},
	/**
	 * 加载后执行缓存的方法
	 */
	_loadedHandler: function() {
		var that = this;
		/** setValues **/
		var item_setValues = this._getCacheItem("setValues");
		if (item_setValues) {
			this.setValues(item_setValues.values, item_setValues.text, item_setValues.triggerOnChange);
			this._removeCacheItem("setValues");
		}
		/** _addHighlight **/
		this._addGridHighlights();
		/** focus **/
		var item_focus = this._getCacheItem("focus");
		if (item_focus) {
			this.focus();
			this._removeCacheItem("focus");
		}	
	},
	_removeGridHighlights: function() {		
		var	that = this,
			gridId = "combo_grid_" + $(that.element).attr('id'),
			$items_Highlight = this.grid().find(".coral-grid-btable tr td > span.coral-keyword-highlight");
		
		this._removeHighlight($items_Highlight);
	},
	/**
	 * 获取有效的td
	 */
	_getGridTds: function() {
		var that = this,
			$grid = this.grid(),
			multiselect = $grid.grid("option", "multiselect");
		
		if (multiselect) {
			return $grid.find(".coral-grid-btable tr.jqgrow > td");
		} else {
			return $grid.find(".coral-grid-btable").find("tr.jqgrow").children("td");
		}
	},
	/**
	 * 给搜索到的grid结果集上加高亮显示
	 */
	_addGridHighlights: function() {
		if (!this.options.enableSearch || this.endSearch ) return;

		var	that = this,
			gridId = "combo_grid_" + $(that.element).attr('id'),
			textField = that.options.textField,
			$items_Highlight = this._getGridTds()/*grid().find(".coral-grid-btable tr td")*//*.filter( "[aria-describedby$='" + gridId + "_" + textField + "']" )*/,
			keyword = that.uiCombo.textbox.val();
		
		this._addHighlight($items_Highlight, keyword);
	},
	/**
	** 每次选择一行时，更新缓存的value，text，rowId
	**/
	_updateGridData: function (ui) {
		var that = this;
		var rowData = this.grid().grid("getRowData", ui.rowId);
		var value = this._getTextFromHTML( rowData[this.options.valueField] );
		var text = this._getTextFromHTML( rowData[this.options.textField] );
		var index = $.inArray(value, this.gridValueArr);
		//单选时，不缓存值。
		if ( !this.options.multiple ) {
			this.gridValueArr = [value];
			this.gridTextArr = [text];
			this.gridRowIdArr = [ui.rowId];
			return ;
		}
		//多选时，缓存值。
		if ( !ui.status ) {
			if ( index != -1 ) {
				this.gridValueArr.splice(index, 1);
				this.gridTextArr.splice(index, 1);
				this.gridRowIdArr.splice(index, 1);
			}
		} else {
			if ( index == -1 ) {
				this.gridValueArr.push(value);
				this.gridTextArr.push(text);
				this.gridRowIdArr.push(ui.rowId);
			}
		}
	},
	_create : function() {
		var that = this, 
		    showPanelEvent = null, 
		    goptions;

    	this.element.addClass("coral-form-element-combogrid coral-validation-combogrid ctrl-init ctrl-form-element ctrl-init-combogrid");
		this._super();	
		var $grid = $();
		
		if ( this.options.pager ) {
			grid = $('<div id="combo_grid_'+$(this.element).attr('id')+'"><div class="combo_grid_'+$(this.element).attr('id')+'"></div></div>').appendTo(this.uiCombo.pContent);	
		} else {
			grid = $('<div id="combo_grid_'+$(this.element).attr('id')+'"></div>').appendTo(this.uiCombo.pContent);	
		}
		
		// 缓存value，text，rowId数组，用以设置值
		this.gridValueArr = [];
		this.gridTextArr = [];
		this.gridRowIdArr = []

		goptions = {
			onSelectRow : function ( e, ui ) {
				/*var vf = that.options.valueField, valueArr = [],
					tf = that.options.textField, textArr = [];*/

				/*if (that.options.multiple) {
					var selarrrow = that.grid().grid("option", "selarrrow");
					for (var i=0, l=selarrrow.length; i<l; i++) {
						var rowData = $(this).grid("getRowData", selarrrow[i]);
						valueArr.push(that._getTextFromHTML(rowData[vf]));
						textArr.push(that._getTextFromHTML(rowData[tf]));
					}
				} else {
					var rowData = $(this).grid("getRowData", ui.rowId);
					valueArr.push(that._getTextFromHTML(rowData[vf]));
					textArr.push(that._getTextFromHTML(rowData[tf]));
				}*/
				//that.setValues(valueArr, textArr, true);
				that._updateGridData(ui);
				that.setValues(that.gridValueArr, that.gridTextArr, true);
				if (!that.options.multiple && e.originalEvent && e.originalEvent.type == "click" ) {
					that.hidePanel();
				}
			},
			onLoad: function (e, ui) {
				that.isLoaded = true;
				that._loadedHandler();
				// 点击分页码时，reload，设置缓存的值
				$.each( that.gridRowIdArr, function(i, v) {
					that.grid().grid("setSelection", v, false, null);					
				});
			},
			fitStyle: "fill",
			sortable: this.options.sortable,
			colModel : this.options.colModel,
		    colNames : this.options.colNames,
			multiselect: this.options.multiple,
			width : "auto"
		};
		goptions = $.extend({}, goptions, this.options.gridOptions);
		
		if (null != this.options.url) {
			goptions.url = this.options.url;
			goptions.datatype = "json";
		} else {
			goptions.data = this.options.data;
			goptions.datatype = "local";
		}
			
		grid.grid(goptions);
		grid.grid("refresh");
		this.panelRendered = true;
		// add button code
		if (null !== this.options.buttonOptions) {
			this.$button = this._getButtonEl();
			this.component().append(this.$button).addClass("coral-combogrid-hasButton");
			this.$button.button(this.options.buttonOptions);
		}
		this.uiCombo.panel.unbind().bind('mousedown', function(e) {
			e.stopPropagation();
			return false;
		});
	}, 
	_getButtonEl: function () {
		return $("<button type='button'></button>").addClass("coral-combogrid-button");
	},
	/**
	 * 根据colName获取grid所有行集合数据
	 * @param colNameArr {array,string} : 列名name数组
	 * @return rowData {array} : 行集合数据
	 */
	_getRowDataByColName: function(colNameArr) {
		var that = this,
			opts = this.options,
			$grid = this.grid(),
			gridData = $grid.grid("option", "data"),
			rowData = [];
		
		if ( typeof colNameArr === "string" ) {
			colNameArr = [colNameArr];
		}
		
		$.each(gridData, function(index, item) {
			var rowObj = {};
			for (var i in colNameArr) {
				var colName = colNameArr[i];
				rowObj[colName] = item[colName];
			}
			rowData.push(rowObj);
		});

		return rowData;
	},
	/**
	 * grid根据value数组，获取对应的text数组
	 * @param valueArr {array} : value 集合数组
	 * @returns textArr {array} : text 集合数组
	 */
	_getTextArrByValueArr: function (valueArr) {
		var that = this,
			opts = this.options,
			valueField = this.options.valueField,
			textField = this.options.textField,
			dataObj = this._getRowDataByColName( [valueField, textField] ),
			textArr = [];
		
		for (var i in valueArr) {
			var valueItem = valueArr[i],
				hasText = false;
			
			$.each(dataObj, function(index, item) {
				if ( valueItem == item[valueField] ) {
					textArr.push(item[textField]);
					hasText = true;
				}
			});
			// 如果没找到对应的text，则将value作为一个textItem
			if (!hasText) {
				textArr.push(valueArr[i]);
			}
		}
		
		return textArr;
	},	
	/**
	 * 设置默认值
	 */
	_setDefaultValue: function() {
		if ( !this.options.value ) return ;
		
		var that = this,
			opts = this.options,
			value = this.options.value;
		

		if (typeof value === "string") {
			value = value.split(opts.separator);
		}
		
		this.setValues(value);		
	},
	//给文本框赋值
	setValues: function (values, text, triggerOnChange) {
		// 如果没加载完，则先缓存，onLoad之后统一执行
		if (!this.isLoaded) {
			var cacheItem = {
				"setValues": {
					values: values,
					text: text,
					triggerOnChange: triggerOnChange
				}
			};
			this._addCacheItem(cacheItem);
		}
		var opts  = this.options;
		var textArr = [];

		textArr = this._getTextArrByValueArr(values);

		if (text) {
			this._setText(text.join(opts.separator));
		} else {
			this._setText(textArr.join(opts.separator));
		}

		this._super(values, false, triggerOnChange);

		if ( this.options.enableSearch && !this.endSearch ) {
			this.endSearch = true;
		}

	},

	setValue: function(value){ 
		this.setValues([value],false,false);
	},
	getData: function() {
		return this.data || [];
	},
	_selectPrev : function(){
		 var that = this,
		 	 selected = null,
		 	 index = 0,
		 	 rows = that.grid().grid("getDataIDs");
			 selarrrow = that.grid().grid("option","selarrrow");
		 if(that.options.multiple){
			 if ( that.selectedRow ) {
					// that.selectedRow 在rows中的第几个 得到下一个
					for(var i=rows.length;i>=0;i--){
						if ( that.selectedRow == rows[i] ) {
							index = (i==0? (rows.length-1) : ( i - 1 ));
							that.selectedRow = rows[ index ];
							break;
						}//下一个
					}
					if ( $.inArray( that.selectedRow, selarrrow ) == -1){//没选中的情况
						that.grid().grid("setSelection", that.selectedRow);
					} 
					// 判断当前项目(selectedRow)是否选中
					// that.selectedRow 在selarrrow中是否存在	
				} else {
					that.selectedRow = rows.length;
					that.grid().grid("setSelection",rows.length);
				}
				that._scrollTo(that.selectedRow);
		 }else{
		  //取得选中行	
			selected = that.grid().grid("option","selrow");
			if (selected) {
				//取得选中行的rowIndex
				index = that.grid().grid("getInd",selected);   
				//向上移动到第一行为止
				if (index >= 0) {
					that.grid().grid("setSelection",rows[index-2]);
				}
			} else {	
				that.grid().grid("setSelection", rows.length);	
			}
		 }
			that._scrollTo(selected);
	},
	_selectNext : function(){
		var that = this, 
			selected = null,
			index = 0,
			rows = that.grid().grid("getDataIDs");
			selarrrow = that.grid().grid("option","selarrrow");
		if(that.options.multiple){
			if ( that.selectedRow && this.endSearch != false) {
				// that.selectedRow 在rows中的第几个 得到下一个
				for(var i=0;i<rows.length;i++){
					if ( that.selectedRow == rows[i] ) {
						index = (i==rows.length-1? 0 : ( i + 1 ));
						that.selectedRow = rows[ index ];
						break;
					}//下一个
				}
				if ( $.inArray( that.selectedRow, selarrrow ) == -1){//没选中的情况
					that.grid().grid("setSelection", that.selectedRow);
				} 
				// 判断当前项目(selectedRow)是否选中
				// that.selectedRow 在selarrrow中是否存在	
			} else {
				that.selectedRow = rows[0];
				that.grid().grid("setSelection",rows[0]);
			}
			that._scrollTo(that.selectedRow);
		} else {		
			selected = that.grid().grid("option","selrow");
			//$.inArray
			if (selected) {
				//取得选中行的rowIndex
				var index = that.grid().grid("getInd",selected);
				//向下移动到当页最后一行为止	
				if (index < rows.length) {	
					that.grid().grid("setSelection",rows[index]);
				}
			} else {
				that.grid().grid("setSelection",rows[0]);
			}
		}
		that._scrollTo(selected);
	},
	_doEnter: function() {
		if (!this.uiCombo.panel.is(":visible")) return;

		this.grid().grid("setSelection", this.selectedRow);
	},
	_scrollTo : function(value) {
		var panel = this.panel();
		var item = panel.find(".coral-row-ltr[id=\"" + value + "\"]");
		if (item.length){
			if (item.position().top <= 0){
				var h = panel.find(".coral-grid-rows-view").scrollTop() + item.position().top - item.outerHeight();
				panel.find(".coral-grid-rows-view").scrollTop(h);
			} else if (item.position().top + item.outerHeight() > panel.find(".coral-grid-rows-view").height()){
				var h = panel.find(".coral-grid-rows-view").scrollTop() + item.position().top + item.outerHeight() - panel.find(".coral-grid-rows-view").height();
				panel.find(".coral-grid-rows-view").scrollTop(h);
			}
		}
	}
});
})(jQuery);