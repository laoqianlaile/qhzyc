package com.ces.trace.test;

import com.ces.component.trace.service.TraceService;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
//import org.junit.FixMethodOrder;
import org.junit.Test;
//import org.junit.runners.MethodSorters;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by 黄翔宇 on 15/6/27.
 */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TraceServiceTest extends BaseTest {

	@Resource(name="traceService")
	private TraceService traceService;

	static String CY_QYDA = "T_CY_QYDA";
	static String id;

	@Test
	public void test_2_Insert() throws Exception {
		Map<String, String> data = new HashMap<String, String>();
		data.put("QYBM", "aaaa");
		data.put("QYMC", "bbbb");
		data.put("CREATE_USER", "1");
		id = traceService.save(CY_QYDA, data, null);
	}

	@Test
	public void test_3_Query() throws Exception {
		String sql = "select * from " + CY_QYDA + " where id = ?";
		Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{id});
		assertEquals("bbbb", data.get("QYMC").toString());
	}
}