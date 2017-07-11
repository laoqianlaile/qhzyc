package com.ces.component.aquatic.webservice.service;

import ces.sdk.util.MD5;
import com.ces.component.aquatic.dto.JyshDto;
import com.ces.component.aquatic.entity.TzEntity;
import com.ces.component.aquatic.entity.TzxxEntity;
import com.ces.component.aquatic.service.ShxxService;
import com.ces.component.aquatic.service.TzService;
import com.ces.component.aquatic.service.TzxxService;
import com.ces.component.aquatic.utils.AquaticCommonUtil;
import com.ces.component.aquatic.utils.AquaticUserUtil;
import com.ces.component.aquatic.utils.ImageUtil;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.StringUtil;
import com.ces.utils.TokenUtils;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/7/1.
 * 水产系统手机端接口
 */
@Component()
@Path("/aquatic")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class AquaticJaxRsService {

	@Autowired
	private TzService tzService;

	@Autowired
	private TzxxService tzxxService;

    @Autowired
	private ShxxService shxxService;

	@Path("/login")
	@POST
	public Response login(@FormParam("username") String username, @FormParam("password") String password, @FormParam("userType") String userType) {
		JyshDto jyshDto = AquaticUserUtil.loadUser(username, userType);
		if (jyshDto == null) {
			return AquaticResponseUtil.generateResponse(AquaticResponseUtil.RES_ERROR, "用户名或密码错误");
		}
		String md5Pass = new MD5().getMD5ofStr(password);
		if (!jyshDto.getPassword().equalsIgnoreCase(md5Pass)) {
			return AquaticResponseUtil.generateResponse(AquaticResponseUtil.RES_ERROR, "用户名或密码错误");
		}
		String token = TokenUtils.getToken(username, password,3000);
		jyshDto.setPassword("");
		jyshDto.setToken(token);
		return AquaticResponseUtil.generateResponse(AquaticResponseUtil.RES_OK, jyshDto);
	}

	@Path("/modifyPass")
	@POST
	public Response modifyPass(@FormParam("username") String username, @FormParam("newpass") String newpass, @FormParam("userType") String userType) {
		newpass = new MD5().getMD5ofStr(newpass);
		AquaticUserUtil.updateUserPass(username, newpass, userType);
		return AquaticResponseUtil.generateResponse(AquaticResponseUtil.RES_OK, "OK");
	}

	@Path("/uploadLedger")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Transactional
	public Response uploadLedger(@Multipart("uploadedFile") Attachment image,
	                             @Multipart("qybm") String qybm,
	                             @Multipart("qymc") String qymc,
	                             @Multipart("shid") String shid,
	                             @Multipart("shmc") String shmc,
	                             @Multipart("tzlx") String tzlx,
	                             @Multipart("spbh") String spbh) {
		if (StringUtil.isEmpty(tzlx) || StringUtil.isEmpty(qybm) || StringUtil.isEmpty(qymc) || StringUtil.isEmpty(shid) || StringUtil.isEmpty(tzlx) || StringUtil.isEmpty(spbh)) {
			return AquaticResponseUtil.generateResponse("1", "提交信息不全，请重试");
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DataHandler dh = image.getDataHandler();
		MultivaluedMap<String, String> map = image.getHeaders();
		String filename = getFileName(map);
		String storename = getStoreName(filename);
		InputStream ins = null;
		OutputStream out = null;
		File target = null;
		try {
			ins = dh.getInputStream();
			target = new File(ComponentFileUtil.getProjectPath() + "/aquatic/" + storename);
			out = new FileOutputStream(target);
			writeToFile(ins, out);
			ImageUtil.compressPic(target, new File(ComponentFileUtil.getProjectPath() + "/aquatic/thumb/" + storename), 180.0, 0);
			TzEntity tz = null;
			String dayTzId = tzService.getDayTzId(shid, tzlx);
			if (StringUtil.isEmpty(dayTzId)) {//当天不台帐存在
				tz = new TzEntity();
				tz.setQybm(qybm);
				tz.setQymc(qymc);
				tz.setShid(shid);
				tz.setShmc(shmc);
				tz.setSbsj(simpleDateFormat.format(new Date()));
				tz.setGxsj(simpleDateFormat.format(new Date()));
				tz.setTzlx(tzlx);
				tz.setSpbh(spbh);
			} else {//当天台帐存在
				tz = tzService.getByID(dayTzId);
				tz.setGxsj(simpleDateFormat.format(new Date()));
			}
			final TzEntity savedTzEntity = tzService.save(tz);
			TzxxEntity tzxx = new TzxxEntity();
			tzxx.setQybm(qybm);
			tzxx.setTpmc(filename);
			tzxx.setTplj(storename);
			tzxx.setScrq(simpleDateFormat.format(new Date()));
			tzxx.setTzid(savedTzEntity.getId());
			tzxxService.save(tzxx);
			return AquaticResponseUtil.generateResponse("0", savedTzEntity.getId());
		} catch (IOException e) {
//				e.printStackTrace();
			return AquaticResponseUtil.generateResponse("1", "上传失败，内部服务器错误");
		} finally {
			IOUtils.closeQuietly(ins);
			IOUtils.closeQuietly(out);
		}
	}

	@Path("/getTzxx")
	@GET
	public Response getTzxx(@QueryParam("tzid") String tzid,@QueryParam("tzlx") String tzlx,@QueryParam("date") String date) {
		List<Map<String, String>> tzxx = tzService.getTzxx(tzid, tzlx, date);
		return AquaticResponseUtil.generateResponse(AquaticResponseUtil.RES_OK, tzxx);
	}

	@Path("/getTzxxAll")
	@GET
	public Response getTzxx(@QueryParam("tzlx") String tzlx,@QueryParam("date") String date) {
		List<Map<String, String>> tzxx = tzService.getTzxxAll(tzlx, date);
		return AquaticResponseUtil.generateResponse(AquaticResponseUtil.RES_OK, tzxx);
	}

	private String getFileName(MultivaluedMap<String, String> header) {
		String[] contentDisposition = header.getFirst("Content-Disposition")
				.split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String exactFileName = name[1].trim().replaceAll("\"", "");
				try {
					return new String(exactFileName.getBytes("ISO-8859-1"), "utf8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return "unknown";
	}

	private void writeToFile(InputStream in, OutputStream out) throws IOException {
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = in.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		in.close();
		out.flush();
		out.close();
	}

	private String getStoreName(String filename) {
		long now = System.currentTimeMillis();
		String ext = filename.substring(filename.lastIndexOf("."));
		return now + ext;
	}

	/**
	 * 商户日期分类台账查询
	 * @param shid
	 * @param tzlx
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@Path("/queryLedger")
	@POST
	public Response queryLedger(@FormParam("shid")String shid,@FormParam("tzlx")String tzlx,@FormParam("P_pageNumber")String pageNumber,@FormParam("P_pageSize")String pageSize){
		PageRequest pageRequest = new PageRequest(Integer.parseInt(pageNumber),Integer.parseInt(pageSize));
		Page page = tzService.queryTzList(shid,tzlx,pageRequest);
		Map map = AquaticCommonUtil.getInstance().putPageToMap(page, pageSize, pageNumber);
		return AquaticResponseUtil.generateResponse(AquaticResponseUtil.RES_OK, map);
	}
	/**
	 * 商户日期分类台账查询
	 * @param tzlx
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@Path("/queryLedgerAll")
	@POST
	public Response queryLedgerAll(@FormParam("tzlx")String tzlx,@FormParam("P_pageNumber")String pageNumber,@FormParam("P_pageSize")String pageSize){
		PageRequest pageRequest = new PageRequest(Integer.parseInt(pageNumber),Integer.parseInt(pageSize));
		Page page = tzService.queryTzListAll(tzlx,pageRequest);
		Map map = AquaticCommonUtil.getInstance().putPageToMap(page, pageSize, pageNumber);
		return AquaticResponseUtil.generateResponse(AquaticResponseUtil.RES_OK, map);
	}


    /**
     * 修改商户信息
     * @param shid 商户id
     * @param sjh 手机号码
     * @return
     */
    @Path("/modifyShjbxx")
    @POST
    @Transactional
    public Response modifyShjbxx(@FormParam("shid")String shid,@FormParam("sjh")String sjh){
        //是否需要登录校验
        shxxService.updateShxx(shid,sjh);
        return AquaticResponseUtil.generateResponse(AquaticResponseUtil.RES_OK,"OK");
    }

    /**
     * 修改商户经营品类信息
     * @param shid
     * @param jyplbm
     * @return
     */
    @Path("/modifyJypl")
    @POST

    public Response modifyJypl(@FormParam("shid")String shid,@FormParam("jyplbm")String jyplbm){
        shxxService.updateJypl(shid, jyplbm);
        return AquaticResponseUtil.generateResponse(AquaticResponseUtil.RES_OK,"OK");
    }

    /**
     * 分页查询经营品类信息列表
     * @param P_pageNumber
     * @param P_pageSize
     * @return
     */
    @Path("/searchJyplxx")
    @POST
    public Response searchJyplxx(@FormParam("P_pageNumber")int P_pageNumber,@FormParam("P_pageSize")int P_pageSize) {
        Page page = shxxService.searchJypl(P_pageNumber, P_pageSize);
        Map map = AquaticCommonUtil.getInstance().putPageToMap(page, P_pageSize + "", P_pageNumber + "");
        return AquaticResponseUtil.generateResponse("0", map);
    }
	/**
	 * 监控台账列表
	 *
	 * @param spbh         商铺编号
	 * @param shmc         商户名称
	 * @param sbSjqs       上报区间开始时间
	 * @param sbSjjs       上报区间结束时间
	 * @param P_pageSize   pageSize
	 * @param P_pageNumber pageNumber
	 * @return
	 */
	@POST
	@Path("/queryLedgerList")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@SuppressWarnings("rawtypes")
	public Response queryLedgerList(@Context HttpServletRequest request,
									@FormParam("spbh") String spbh,
	                                @FormParam("shmc") String shmc,
	                                @FormParam("tzlx") String tzlx,
	                                @FormParam("jypl") String jypl,
									@FormParam("sbSjqs") String sbSjqs,
									@FormParam("sbSjjs") String sbSjjs,
									@FormParam("P_pageSize") String P_pageSize,
									@FormParam("P_pageNumber") String P_pageNumber) {
		PageRequest pageRequest = new PageRequest(Integer.parseInt(P_pageNumber), Integer.parseInt(P_pageSize));
		String token = request.getParameter("Aquatic-Token");
		String userType = request.getParameter("User-Type");
		String qybm = null;
		try {
			qybm = AquaticUserUtil.loadQybmByToken(token, userType);
		} catch (IOException e) {
		}
		Map map = AquaticCommonUtil.getInstance().putPageToMap(tzService.queryTzList(spbh, shmc,qybm,tzlx,jypl, sbSjqs, sbSjjs, pageRequest), P_pageSize, P_pageNumber);
		return AquaticResponseUtil.generateResponse("0", map);
	}

    @Path("/searchSptzxx")
    @POST
    public Response searchSptzxx(@FormParam("shid")String shid,@FormParam("P_pageNumber")int P_pageNumber,@FormParam("P_pageSize")int P_pageSize){
        Page page = tzService.searchSptzxx(shid,P_pageNumber, P_pageSize);
        Map map = AquaticCommonUtil.getInstance().putPageToMap(page, P_pageSize + "", P_pageNumber + "");
        return AquaticResponseUtil.generateResponse("0", map);
    }

	@Path("/getTzDetail")
	@GET
	public Response getTzDetail(@QueryParam("tzid")String tzid){
		Map<String,Object> map = tzService.getTzDetail(tzid);
		return AquaticResponseUtil.generateResponse(AquaticResponseUtil.RES_OK, map);
	}
}
