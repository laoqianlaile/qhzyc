package com.ces.component.trace.webservice.service;

import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.utils.ResponseUtil;
import com.ces.xarch.core.security.entity.SysUser;
import com.ces.xarch.core.web.listener.XarchListener;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by 黄翔宇 on 15/5/26.
 */
@Component
@Path("/register")
@Produces("*/*")
@Consumes("*/*")
public class RegisterJaxRsService {

	/**
	 * 验证码缓存验证
	 * key:请求时间戳
	 * vakue:验证码字符串
	 */
	private Map<String, String> captchaMap = new HashMap<String, String>();

	@Autowired
	@Qualifier("XarchUserService")
	private UserDetailsService userDetailsService;

	/*
	@OPTIONS
	@Path("/register")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(Register register) {
		register.setSqbh(String.valueOf(System.currentTimeMillis()));
		register.setSqsj(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		register.setShzt("1");
		Map<String, String> map = JSON.fromJSON(JSON.toJSON(register), new TypeReference<Map<String, String>>(){});
		XarchListener.getBean(TraceShowModuleDefineDaoService.class).save("t_qypt_zhsqgl", map, null);
		return Response.ok()
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
				.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept,X-Requested-With").build();
	}
	*/

	/**
	 * 企业申请服务
	 * @param registerStr
	 * @return
	 */
	@POST
	@Path("/register")
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(@FormParam("regist") String registerStr) {
		Map<String, String> map = JSON.fromJSON(registerStr, new TypeReference<Map<String, String>>() {});
		map.put("sqbh", String.valueOf(System.currentTimeMillis()));
		map.put("sqsj", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		map.put("shzt", "1");
		map.put("CREATE_USER","1");
		XarchListener.getBean(TraceShowModuleDefineDaoService.class).save("t_qypt_zhsqgl", map, null);
		return ResponseUtil.generateResponse(map);
	}

	/**
	 * 生成验证码
	 * @param resp
	 * @param timestmp
	 * @return
	 */
	@GET
	@Path("/captcha")
	public Response captcha(@Context HttpServletResponse resp, @QueryParam("t")String timestmp) {
		if (timestmp == null) {
			return ResponseUtil.generateResponse();
		}
		resp.setContentType("image/jpeg");
		OutputStream o = null;
		int width = 200;
		int height = 60;
		int number = 4;
		String code = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random r = new Random();
		String str = "";
		try {
			o = resp.getOutputStream();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < number; i++) {
				String charcode = String.valueOf(code.charAt(r.nextInt(code
						.length())));
				sb.append(charcode);
			}
			str = sb.toString();
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic = image.createGraphics();

			// 填充图像
			graphic.setColor(Color.WHITE);
			graphic.fillRect(0, 0, width, height);

			// 画边框
			graphic.setColor(Color.BLACK);
			graphic.drawRect(0, 0, width - 1, height - 1);

			// 画字符串
			int x = width / number;
			int y = height - 10;
			Font font = new Font("宋体", Font.BOLD, 60);
			for (int i = 0; i < str.length(); i++) {
				graphic.setFont(font);
				graphic.setColor(new Color(r.nextInt(255), r.nextInt(255), r
						.nextInt(255)));
				graphic.drawString(String.valueOf(str.charAt(i)), (x * i) + 15,
						y);
			}
			// 随机生成60点
			for (int i = 0; i < 60; i++) {
				graphic.setColor(new Color(r.nextInt(255), r.nextInt(255), r
						.nextInt(255)));
				graphic.drawOval(r.nextInt(width), r.nextInt(height), 2, 2);
			}
			captchaMap.put(timestmp, str);
			ImageIO.write(image, "jpg", o);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseUtil.generateResponse(str);
	}

	/**
	 * 验证验证码
	 * @param timestmp
	 * @param captcha
	 * @return
	 */
	@GET
	@Path("/checkCaptcha")
	public Response checkCaptcha(@QueryParam("t")String timestmp,@QueryParam("captcha")String captcha) {
		String storeCaptcha = captchaMap.get(timestmp);
		if (storeCaptcha == null) {
			return ResponseUtil.generateResponse(false);
		}
		if (!storeCaptcha.equalsIgnoreCase(captcha)) {
			return ResponseUtil.generateResponse(false);
		}
		return ResponseUtil.generateResponse(true);
	}

	/**
	 * 检查用户是否存在
	 * @param base64Code
	 * @return
	 */
	@POST
	@Path("/checkUserAndCaptcha")
	public Response checkUserAndCaptcha(@QueryParam("p")String base64Code,@QueryParam("t")String timestmp,@QueryParam("captcha")String captcha,@QueryParam("type")String type) {
		try {
			String storeCaptcha = captchaMap.get(timestmp);
			if (storeCaptcha == null) {
				return ResponseUtil.generateResponse("验证码错误");
			}
			if (!storeCaptcha.equalsIgnoreCase(captcha)) {
				return ResponseUtil.generateResponse("验证码错误");
			}
			byte[] decode = Base64.decodeBase64(base64Code);
			String[] p = new String(decode,"UTF-8").split("__");
			SysUser sysUser = (SysUser)userDetailsService.loadUserByUsername(p[0]);
			if (sysUser == null) {
				return ResponseUtil.generateResponse("用户名或密码错误");
			}
            if ((sysUser.getBelongOrgId().equals("30903d80537047fca18b406cb069f44a")&&type.equalsIgnoreCase("qy"))||(!sysUser.getBelongOrgId().equals("30903d80537047fca18b406cb069f44a")&&type.equalsIgnoreCase("jg"))) {
                return ResponseUtil.generateResponse("用户名或密码错误");
            }
			if (!p[1].equalsIgnoreCase(sysUser.getPassword())) {
				return ResponseUtil.generateResponse("用户名或密码错误");
			}
			return ResponseUtil.generateResponse(true);
		} catch(Exception e) {
			return ResponseUtil.generateResponse("用户名或密码错误");
		}
	}

}
