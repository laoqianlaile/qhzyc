package com.ces.component.trace.webservice.service;

import com.ces.config.utils.ComponentFileUtil;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;

/**
 * Created by 黄翔宇 on 15/4/14.
 */
@Component
@Path("/business")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BusinessDetailJaxRsService {

	static final String RES_OK = "{'result':'1'}";
	static final String RES_ERR = "{'result':'0'}";

	/*
	@Path("/businessInfo")
	@POST
	public String businessInfo(BusinessInfo info) {
		try {
			XarchListener.getBean(ShowModuleDefineDaoService.class).save("T_CSPT_SCJYXX", JSON.toJSON(info), null);
		} catch (Exception e) {
			return RES_ERR;
		}
		return RES_OK;
	}

	@Path("/businessInfos")
	@POST
	public String businessInfo(BusinessInfos infos) {
		long start = System.currentTimeMillis();
		ShowModuleDefineDaoService service = XarchListener.getBean(ShowModuleDefineDaoService.class);
		try {
			for (BusinessInfo info : infos.getBusinessInfos()) {
				Map<String, String> map = JSON.fromJSON(JSON.toJSON(info), new TypeReference<Map<String, String>>() {
				});
				map.put("CREATE_USER", "DBA");
				service.save("T_CSPT_SCJYXX", map, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RES_ERR;
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		return RES_OK;
	}
*/
	@POST
	@Path("/uploadFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadFile(
			@Multipart("csv") Attachment file1) {
		DataHandler dh = file1.getDataHandler();
		MultivaluedMap<String, String> map = file1.getHeaders();
		String filename = getFileName(map);

		InputStream ins = null;
		OutputStream out = null;
		File target = null;
		try {
			ins = dh.getInputStream();
			target = new File(ComponentFileUtil.getProjectPath() + "/csv/" + getStoreName());
			out = new FileOutputStream(target);
			writeToFile(ins, out);
			return Response.ok(RES_OK).build();
		} catch (IOException e) {
//				e.printStackTrace();
			return Response.status(500).entity(RES_ERR).build();
		} finally {
			IOUtils.closeQuietly(ins);
			IOUtils.closeQuietly(out);
		}

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

	private String getStoreName() {
		long now = System.currentTimeMillis();
		return "csv_" + now + ".business";
	}

	private String getStoreName(String filename) {
		long now = System.currentTimeMillis();
		String ext = filename.substring(filename.lastIndexOf("."));
		return now + ext;
	}

}
