'*********************************
'	菜单点击事件处理
'*********************************
Sub Menu1_ClickMenuItem( byval name)
	name = ucase(name)
	select case name
	case "FILENEW"
	   mnuFileNew_click
	case "FILEOPEN"
	   mnuFileOpen_click
	case "FILEWEBOPEN"
	   mnuFileWebOpen_click
	case "FILESAVE"
	   mnuFileSave_click
	case "FILESHEETSAVEAS"
	   mnuFileSheetSaveAs_click
	case "FILEIMPORTTEXT"
	   mnuFileImportText_click
	case "FILEIMPORTCSV"
	   mnuFileImportCSV_click      
	case "FILEIMPORTEXCEL"
	   mnuFileImportExcel_click
	case "FILEEXPORTTEXT"
	   mnuFileExportText_click
	case "FILEEXPORTCSV"
	   mnuFileExportCSV_click   
	case "FILEEXPORTEXCEL"
	   mnuFileExportExcel_click
	case "FILEEXPORTPDF"
	   mnuFileExportPDF_click   
	case "FILEPAGESETUP"
	   mnuFilePageSetup_click
	case "FILEPRINTPREVIEW"
	   mnuFilePrintPreview_click
	case "FILEPRINT"
	   mnuFilePrint_click
	case "FILEEXIT"
	   mnuFileExit_click
	case "EDITUNDO"
	   mnuEditUndo_click
	case "EDITREDO"
	   mnuEditRedo_click
	case "EDITCUT"
	   mnuEditCut_click
	case "EDITCOPY"
	   mnuEditCopy_click
	case "EDITPASTE"
	   mnuEditPaste_click
	case "EDITPASTESPECIAL"
	   mnuEditPasteSpecial_Click 
	case "EDITFIND"
	   mnuEditFind_click
	case "EDITREPLACE"
	   mnuEditReplace_click
	case "EDITGOTO"
	   mnuEditGoto_click
	case "EDITSELECTALL"
	   mnuEditSelectAll_click
	   flag = Menu1.GetMenuItemCheck( "EditSelectAll" )
	   menu1.SetMenuItemCheck "EditSelectAll" ,flag
	case "EDITFILLV"
	   mnuEditFillV_click
	case "EDITINSERTSPECHAR"
	   mnuEditInsertSpeChar_click
	case "EDITHYPERLINK"
	   mnuEditHyperlink_click
	case "VIEWFREEZED"
	   mnuViewFreezed_click
	case "VIEWSHEETLABEL"
	   mnuViewSheetLabel_click
	   flag = Menu1.GetMenuItemCheck( "ViewSheetLabel" )
	   menu1.SetMenuItemCheck "ViewSheetLabel" ,flag
	case "VIEWROWLABEL"
	   mnuViewRowLabel_click
	   flag = Menu1.GetMenuItemCheck( "ViewRowLabel" )
	   menu1.SetMenuItemCheck "ViewRowLabel" ,flag
	case "VIEWCOLLABEL"
	   mnuViewColLabel_click
	   flag = Menu1.GetMenuItemCheck( "ViewColLabel" )
	   menu1.SetMenuItemCheck "ViewColLabel" ,flag
	case "VIEWHSCROLL"
	   mnuViewHScroll_click
	   flag = Menu1.GetMenuItemCheck( "ViewHScroll" )
	   menu1.SetMenuItemCheck "ViewHScroll" ,flag
	case "VIEWVSCROLL"
	   mnuViewVScroll_click
	   flag = Menu1.GetMenuItemCheck( "ViewVScroll" )
	   menu1.SetMenuItemCheck "ViewVScroll" ,flag
	case "FORMATCELLPROPERTY"
	   mnuFormatCellProperty_click
	case "FORMATDRAWBORDER"
	   mnuFormatDrawborder_click
	case "FORMATINSERTPIC"
	   mnuFormatInsertPic_click
	case "FORMATREMOVEPIC"
	   mnuFormatRemovePic_click
	case "FORMATMERGECELL"
	   mnuFormatMergeCell_click
	case "FORMATUNMERGECELL"
	   mnuFormatUnMergeCell_click
	case "ROWINSERT"
	   mnuRowInsert_click
	case "ROWDELETE"
	   mnuRowDelete_click
	case "ROWAPPEND"
	   mnuRowAppend_click
    case "ROWHEIGHT"
       mnuRowHeight_click
    case "ROWHIDE"
       mnuRowHide_click
    case "ROWUNHIDE"
       mnuRowUnhide_click
    case "ROWBESTHEIGHT"
       mnuRowBestHeight_click
    case "COLINSERT"
       mnuColInsert_click
    case "COLDELETE"
       mnuColDelete_click
    case "COLAPPEND"
       mnuColAppend_click
    case "COLWIDTH"
       mnuColWidth_click
    case "COLHIDE"
       mnuColHide_click
    case "COLUNHIDE"
       mnuColUnhide_click
    case "COLBESTWIDTH"
       mnuColBestWidth_click
    case "SHEETRENAME"
       mnuSheetRename_click
    case "SHEETSIZE"
       mnuSheetSize_click
    case "SHEETPROPTECT"
       mnuSheetProptect_click
    case "SHEETINSERT"
       mnuSheetInsert_click
    case "SHEETDELETE"
       mnuSheetDelete_click
    case "SHEETAPPEND"
       mnuSheetAppend_click
    case "SHEETSORTSTYLE"
       mnuSheetSortStyle_click
    case "SHEETSORTVALUE"
       mnuSheetSortValue_click
    case "FORMULAINPUT"
       mnuFormulaInput_click
    case "FORMULABATCHINPUT"
       mnuFormulaBatchInput_click
    case "FORMULASERIAL"
       mnuFormulaSerial_click
    case "FORMULACELLSHOW"
       mnuFormulaCellShow_click
    case "FORMULACELLCOLOR"
       mnuFormulaCellColor_click
    case "FORMULALIST"
       mnuFormulaList_click
    case "FORMULARECALC"
       mnuFormulaReCalc_click
    case "USERFUNCDEFINE"
       mnuUserFuncDefine_click 
    case "USERFUNCADD"
       mnuUserFuncAdd_click
    case "USERFUNCDELETE"
       mnuUserFuncDelete   
    case "USERFUNCMODIFY"
       mnuUserFuncModify_click
    case "DATARANGEROTATE"
       mnuDataRangeRotate_click
    case "DATARANGEBLANCE"
       mnuDataRangeBlance_click
    case "DATARANGESORT"
       mnuDataRangeSort_click
    case "DATARANGECLASSSUM"
       mnuDataRangeClassSum_click
    case "DATARANGEQUERY"
       mnuDataRangeQuery_click
    case "DATARANGE3DEASYSUM"
       mnuDataRange3DEasySum_click
    case "DATARANGE3DSUM"
       mnuDataRange3DSum_click
    case "DATARANGE3DVIEW"
       mnuDataRange3DView_click
    case "DATARANGE3DQUERY"
       mnuDataRange3DQuery_click
    case "DATAWZDBARCODE"
       mnuDataWzdBarcode_click
    case "DATAWZDCHART"
       mnuDataWzdChart_click
    case "MENUADDROWCOMPAGE"
       menuAddRowCompage_click
    case "MENUDELROWCOMPAGE"
       menuDelRowCompage_click
    case "MENUADDCOLCOMPAGE"
       menuAddColCompage_click
    case "MENUDELCOLCOMPAGE"
       menuDelColCompage_click
    case "MENUDELALLCOMPAGE"
       menuDelAllCompage_click
    case "DEFINEVARDLG"
       menuDefineVarDlg_click
    end select
		
	
End Sub