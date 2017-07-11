package com.ces.component.virtural.action;

import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.utils.TableNameUtil;
import com.ces.component.virtural.service.VirturalLogicService;
import com.ces.config.action.base.LogicComponentController;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.struts2.rest.DefaultHttpHeaders;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WILL on 2015/3/12.
 * 虚拟逻辑构件，只是为了扩充方法（写卡，打印等）
 */
public class VirturalLogicController extends LogicComponentController {

	private static final long serialVersionUID = 1L;

	// 输入参数
	/**
	 * 操作类型
	 */
	private static final String paramIn1 = "paramIn1";
	/**
	 * 操作传入的值
	 */
	private static final String paramIn2 = "paramIn2";
	/**
	 * 操作表名
	 */
	private static final String paramIn3 = "paramIn3";

	//输出参数
	/**
	 * 操作类型
	 */
	private static final String paramOut1 = "paramOut1";
	/**
	 * 操作结果
	 */
	private static final String paramOut2 = "paramOut2";
	/**
	 * 操作结果值
	 */
	private static final String paramOut3 = "paramOut3";

	/**
	 * 流通卡管理通用虚拟逻辑构件
	 * 输入参数：
	 * operate：操作类型
	 * value：传递的值
	 * 输出参数：
	 * paramOut1：操作类型
	 * paramOut2：操作结果
	 * paramOut3：操作结果值
	 * @return
	 */
	public Object virtural() {
		String operate = getInputParamValue(paramIn1);
		String value = getInputParamValue(paramIn2);
		String table = TableNameUtil.getLtkglTableName(getInputParamValue(paramIn3));
		try {
			if ("cardInsert".equals(operate) || "cardRepair".equals(operate)) {//发卡操作
				Map<String, String> map = JSON.fromJSON(value, new TypeReference<Map<String, String>>() {
				});
				Map<String, String> dataMap = new HashMap<String, String>();
				dataMap.put("LTKH", map.get("A_LTKH"));
				dataMap.put("LTKLX", map.get("A_LTKLX"));
				dataMap.put("JYZBM", map.get("A_JYZBM"));
				getService(VirturalLogicService.class).saveUserInfo(table, dataMap);

				setReturnData(getResultData(true, null, operate, true, null));
			}
			/*
			else if ("cardView".equals(operate) || "cardDelView".equals(operate)) {//查看卡内的信息
				getService(VirturalLogicService.class).getSerialNumber("dsasad", "PCH");
				Map<String,String> data = getService(VirturalLogicService.class).readUserCardInfo(value);
				setReturnData(getResultData(true, null, operate, true, data));
			} else if ("cardDel".equals(operate)) {//回收卡
				setReturnData(getResultData(true, null, operate, true, null));
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
			setReturnData(getResultData(true, null, operate, false, null));
		}
		return new DefaultHttpHeaders(SUCCESS).disableCaching();
	}

	/**
	 * 构造返回数据
	 * @param success 构件标志成功、失败
	 * @param msg 父构件显示信息
	 * @param operate 操作类型（回调函数内判断用）
	 * @param result 操作结果
	 * @param value 操作结果值
	 * @return
	 */
	public String getResultData(boolean success, String msg, String operate, boolean result, Object value) {
		Map<String ,Object> map = new HashMap<String, Object>();
		map.put("success",success);
		if (StringUtil.isNotEmpty(msg)) {
			map.put("message", msg);
		}
		map.put(paramOut1, operate);
		map.put(paramOut2, result);
		map.put(paramOut3, value);
		return JSON.toJSON(map);
	}
}
