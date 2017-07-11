package com.ces.component.qyptxxsz.service;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.MailUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class QyptxxszService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	//HTML换行符
	public final static String HTML_ENTER = "<br>";
	//HTML空格
	public final static String HTML_SPACE = "&nbsp;";

	/**
	 * 模板变量
	 */
	//企业名称
	public static final String USER = "user";
	//时间
	public static final String DATE = "date";

	/**
	 * 获取所有的消息列表（下拉框）
	 *
	 * @return
	 */
	public List<Map<String, Object>> getAllMsgs() {
		String sql = "select t.id,t.zt from t_qypt_xxsz t";
		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		for (Map<String, Object> map : list) {
			map.put("value", map.get("ID"));
			map.put("text", map.get("ZT"));
		}
		return list;
	}

	/**
	 * 发送消息
	 *
	 * @return
	 */
	@Transactional
	public void sendMsg(String msgId, String uuids) throws Exception {
		String querySql = "select * from t_qypt_xxsz t where t.id = '" + msgId + "'";
		Map<String, Object> msg = DatabaseHandlerDao.getInstance().queryForMap(querySql);
		//查看重复发送以及需要发送的标志
		String cffs = String.valueOf(msg.get("CFFS"));
		String qysjdx = String.valueOf(msg.get("QYSJDX"));
		String qydzyj = String.valueOf(msg.get("QYDZYJ"));
		String qyxtxx = String.valueOf(msg.get("QYXTXX"));

		StringBuffer sql = new StringBuffer();
		sql.append("select t.zhbh,t.qymc,t.yx,t.sj from t_qypt_zhgl t where t.id in (");
		for (String uuid : uuids.split(",")) {
			sql.append("'").append(uuid).append("',");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
		for (Map<String, Object> map : list) {
			String zhbh = String.valueOf(map.get("ZHBH"));
			String qymc = String.valueOf(map.get("QYMC"));
			String sj = String.valueOf(map.get("SJ"));
			String yx = String.valueOf(map.get("YX"));
			String fssj = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			//模板数据
			Map<String, String> tempdateData = new HashMap<String, String>();
			tempdateData.put(USER, qymc);
			tempdateData.put(DATE, fssj);
			//替换邮件标题
			msg.put("YJBT", parseTemplate(String.valueOf(msg.get("YJBT")), tempdateData, "yj"));
			//替换邮件正文
			msg.put("YJZW", parseTemplate(String.valueOf(msg.get("YJZW")), tempdateData, "yj"));
			//替换短信正文
			msg.put("DXZW", parseTemplate(String.valueOf(msg.get("DXZW")), tempdateData, "dx"));
			//喜欢消息正文
			msg.put("XXZW", parseTemplate(String.valueOf(msg.get("XXZW")), tempdateData, "xx"));
			if (!"".equals(yx)) {//有邮箱才发送
				sendEmail(zhbh, yx, msg, cffs, fssj);
			}
			if (!"".equals(sj)) {//有手机才发送
				sendSMS(zhbh, sj, msg, cffs, fssj);
			}
			sendSysMsg(zhbh, msg, cffs, fssj);
		}
	}

	/**
	 * 发送Email
	 *
	 * @param zhbh
	 * @param yx
	 * @param msg
	 * @param cffs
	 * @throws Exception
	 */
	@Transactional
	private void sendEmail(String zhbh, String yx, Map<String, Object> msg, String cffs, String fssj) throws Exception {
		if (!"1".equals(cffs)) {//不允许重复发送
			String sql = "select count(1) from t_qypt_yjxxlb t where t.zhbh = '" + zhbh + "' and t.xxbh = '" + msg.get("ID") + "'";
			Object count = DatabaseHandlerDao.getInstance().queryForObject(sql);
			if (Integer.parseInt(String.valueOf(count)) > 0) {
				return;
			}
		}
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("xxbh", String.valueOf(msg.get("ID")));
		dataMap.put("zhbh", zhbh);
		dataMap.put("fssj", fssj);
		save("T_QYPT_YJXXLB", dataMap, null);
		MailUtil.newInstance().sendEmail(yx, String.valueOf(msg.get("YJBT")), String.valueOf(msg.get("YJZW")), null);
	}

	/**
	 * 发送短信
	 *
	 * @param zhbh
	 * @param sj
	 * @param msg
	 * @param cffs
	 */
	@Transactional
	private void sendSMS(String zhbh, String sj, Map<String, Object> msg, String cffs, String fssj) {
		if (!"1".equals(cffs)) {//不允许重复发送
			String sql = "select count(1) from T_QYPT_DXXXLB t where t.zhbh = '" + zhbh + "' and t.xxbh = '" + msg.get("ID") + "'";
			Object count = DatabaseHandlerDao.getInstance().queryForObject(sql);
			if (Integer.parseInt(String.valueOf(count)) > 0) {
				return;
			}
		}
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("xxbh", String.valueOf(msg.get("ID")));
		dataMap.put("zhbh", zhbh);
		dataMap.put("fssj", fssj);
		save("T_QYPT_DXXXLB", dataMap, null);
		//TODO 发送短信
	}

	/**
	 * 发送系统消息
	 *
	 * @param zhbh
	 * @param msg
	 * @param cffs
	 */
	@Transactional
	private void sendSysMsg(String zhbh, Map<String, Object> msg, String cffs, String fssj) {
		if (!"1".equals(cffs)) {//不允许重复发送
			String sql = "select count(1) from t_qypt_xxlb t where t.zhbh = '" + zhbh + "' and t.xxbh = '" + msg.get("ID") + "'";
			Object count = DatabaseHandlerDao.getInstance().queryForObject(sql);
			if (Integer.parseInt(String.valueOf(count)) > 0) {
				return;
			}
		}
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("xxbh", String.valueOf(msg.get("ID")));
		dataMap.put("zhbh", zhbh);
		dataMap.put("fssj", fssj);
		dataMap.put("xxzw", String.valueOf(msg.get("XXZW")));
		save("T_QYPT_XXLB", dataMap, null);
	}


	/**
	 * 简单模板数据转换
	 *
	 * @param template
	 * @param data
	 * @return
	 */
	private String parseTemplate(String template, Map<String, String> data, String msgType) {
		if ("yj".equals(msgType)) {
			template = template.replaceAll("\\r\\n", HTML_ENTER).replaceAll(" ", HTML_SPACE);
		}
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
		Matcher matcher = pattern.matcher(template);
		while (matcher.find()) {
			String name = matcher.group(1);// 键名
			String value = data.get(name);// 键值
			if (value == null) {
				value = "";
			} else {
				value = value.replaceAll("\\$", "\\\\\\$");
			}
			matcher.appendReplacement(sb, value);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public Object checkYxId(String ids){
		String[] idarr=ids.split(",");

		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append("select * from t_qypt_zhgl t where t.id in (");
		for(int i=0;i<idarr.length;i++){
			if(i<(idarr.length-1)){
				stringBuffer.append("'"+idarr[i]+"'"+",");
			}else{
				stringBuffer.append("'"+idarr[i]+"'"+")");
			}
		}

		return DatabaseHandlerDao.getInstance().queryForMaps(stringBuffer.toString());
	}

}