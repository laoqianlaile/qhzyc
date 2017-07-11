package com.ces.coflow.web.update;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;

import ces.workflow.wapi.WFException;
import ces.workflow.wapi.define.DefineXmlFile;
import ces.workflow.wapi.service.IPacket;
import ces.workflow.wapi.service.util.DBTable;

public class CoflowClient {

	/**
	 * 同步相关数据
	 * 
	 * @param table
	 *            相关数据表对象
	 * @throws Exception
	 */
	public static void synchronizeDataFieldTable(String host, DBTable table)
			throws Exception {
		Socket s = null;
		DataInputStream dataIn = null;
		DataOutputStream dataOut = null;
		ObjectOutputStream objectOut = null;
		try {
			s = new Socket(host, 801);
			dataIn = new DataInputStream(s.getInputStream());
			dataOut = new DataOutputStream(s.getOutputStream());
			dataOut.write(IPacket.REQ_UPDATEDATAFIELD);
			objectOut = new ObjectOutputStream(s.getOutputStream());
			objectOut.writeObject(table);
			objectOut.flush();
			if (s.getInputStream().read() != IPacket.REP_UPDATEDATAFIELD) {
				throw new Exception("服务器异常");
			}
			String errormessage = dataIn.readUTF();
			if (errormessage != null && errormessage.length() > 0) {
				throw new Exception(errormessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null, objectOut);
			close(dataIn, dataOut);
			close(s);
		}
	}

	
	
	
	/**
	 * 上传文件
	 * 
	 * @param file
	 *            文件
	 * @param version
	 *            版本
	 * @param author
	 *            作者
	 * @throws Exception
	 */
	public static void checkInFile(String host, File file, String version,
			String author) throws Exception {
		Socket s = null;
		DataInputStream in = null;
		DataOutputStream out = null;
		InputStream in3 = null;
		int result = 0;
		try {
			s = new Socket(host, 801);
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
			out.write(IPacket.REQ_UPDATEFILE);
			out.write(file.getName().getBytes().length);
			out.write(file.getName().getBytes());

			out.write(0);
			out.write(version.getBytes().length);
			out.write(version.getBytes());

			out.write(0);
			out.write(author.getBytes().length);
			out.write(author.getBytes());

			out.write(0);
			out.writeLong(file.lastModified());
			out.writeLong(file.length());
			in3 = new FileInputStream(file);
			while (in3.available() > 0) {
				out.write(in3.read());
			}
			result = in.read();
			if (result == IPacket.REQ_UPDATEFILE) {
				// long lastm = in.readLong();
				// read file length
				// in.readLong();
				
				// file1.setContents(in,true,true, null);
				// file1.getRawLocation().toFile().setLastModified(lastm);
			} else {
				throw new Exception("上传失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(in3, null);
			close(in, out);
			close(s);
		}
		

	}

	/**
	 * 更新文件
	 * 
	 * @param file1
	 * @throws Exception
	 */
	public static void updateFile(String host, File file) throws Exception {
		Socket s = null;
		InputStream is = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		DataInputStream dataInput = null;
		OutputStream out = null;
		
		try {
			s = new Socket(host, 801);
			dataInput = new DataInputStream(s.getInputStream());
			out = s.getOutputStream();
			out.write(IPacket.REQ_GETFILE);
			out.write(file.getName().getBytes().length);
			out.write(file.getName().getBytes());

			String resTime = 0 + "";
			out.write(resTime.getBytes().length);
			out.write(resTime.getBytes());

			if (dataInput.read() == IPacket.REP_GETFILE) {
				long lastm = dataInput.readLong();
				// read status
				// status = in.read();
				// read file size
				if (lastm != file.lastModified()) {
					out.write(1);
					dataInput.readLong();
				
					is = s.getInputStream();
					br = new BufferedReader(new InputStreamReader(is, "utf-8"));
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
					String line = null;
					while ((line = br.readLine()) != null) {
						bw.write(line + "\r\n");
					}
					bw.flush();
					file.setLastModified(lastm);
				} else if (lastm != file.lastModified()) {
					out.write(0);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(bw, br);
			close(dataInput, null);
			close(is, out);
			close(s);
		}

	}

	/***
	 * 检查服务器文件
	 * 
	 * @param file1
	 * @param time2
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws CoreException
	 */
	public static int checkServerFile(File file, long time2) throws Exception {
		// Socket s = new Socket(host, 801);
		Socket s = null;
		try {
			s = new Socket();
			s.connect(new InetSocketAddress("192.10.33.114", 801), 10);
		} catch (UnknownHostException e1) {
			return 0;
		}

		DataInputStream in = new DataInputStream(s.getInputStream());
		OutputStream out = s.getOutputStream();
		// out
		// .write(("getmtimeoffile" + file1.getName() + "\n")
		// .getBytes());
		out.write(IPacket.REQ_GETFILEMTIME);
		out.write(file.getName().getBytes().length);
		out.write(file.getName().getBytes());
		// read the status of file
		int result = in.read();
		if (result == IPacket.REQ_GETFILEMTIME) {
			long result0 = in.readLong();
			int status = in.read();
			try {
				if (!s.isClosed()) {
					s.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			int r1 = 0;
			if (result0 == time2) {
				r1 = 1;
			} else {
				r1 = 2;
			}

			return r1 * 10 + status;
		} else {
			try {
				if (!s.isClosed()) {
					s.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
	}

	/**
	 * 获取服务器文件更新时间
	 * 
	 * @param file1
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static long getServerFileMtime(File file)
			throws UnknownHostException, IOException {

		Socket s = new Socket("192.10.33.114", 801);
		DataInputStream in = new DataInputStream(s.getInputStream());
		OutputStream out = s.getOutputStream();
		// out
		// .write(("getmtimeoffile" + file1.getName() + "\n")
		// .getBytes());
		out.write(IPacket.REQ_GETFILEMTIME);
		out.write(file.getName().getBytes().length);
		out.write(file.getName().getBytes());
		// read the status of file

		int result = in.read();
		if (result == IPacket.REQ_GETFILEMTIME) {
			long result0 = in.readLong();
			int status = in.read();
			try {
				if (!s.isClosed()) {
					s.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result0;
		} else {
			try {
				if (!s.isClosed()) {
					s.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
	}

	/**
	 * 更新组织结构
	 * 
	 * @param project
	 * @param monitor
	 * @param shell
	 * @throws Exception
	 */
	public static void updateOrganization(String host, File projectDir)
			throws Exception {
		Socket s = null;
//		DataInputStream dataInput = null;
		OutputStream out = null;
		InputStream is = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		try {
			s = new Socket(host, 801);
//			dataInput = new DataInputStream(s.getInputStream());
			out = s.getOutputStream();
			out.write(IPacket.REQ_GETORG);
			int respons = s.getInputStream().read();
			if (respons == IPacket.REP_GETORG) {
				new DataInputStream(s.getInputStream()).readLong();
				File file = new File(projectDir.getCanonicalPath() + "\\orgUser.xml");
				is = s.getInputStream();
				br = new BufferedReader(new InputStreamReader(is, "utf-8"));
				bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(file), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					bw.write(line + "\r\n");
				}
				bw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(bw, br);
			close(is, out);
			close(s);
		}
	}

	/**
	 * 更新工程
	 * 
	 * @param project
	 * @param monitor
	 * @param shell
	 * @throws Exception
	 */
	public static List updateProject(File projectDir, String host)
			throws Exception {
		Socket s = null;
		DataInputStream in = null;
		OutputStream out = null;
		File orgfile = null;
		InputStream is = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		File res = null;
		
		try {
			s = new Socket(host, 801);
			in = new DataInputStream(s.getInputStream());
			out = s.getOutputStream();
			out.write(IPacket.REQ_GETORG);
			int response = s.getInputStream().read();
			if (response == IPacket.REP_GETORG) {
				new DataInputStream(s.getInputStream()).readLong();
				orgfile = new File(projectDir.getCanonicalPath() + "\\orgUser.xml");
				is = s.getInputStream();
				br = new BufferedReader(new InputStreamReader(is, "utf-8"));
				bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(orgfile), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					bw.write(line + "\r\n");
				}
				bw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(bw, br);
			close(in, null);
			close(is, out);
			close(s);
		}
		try {
			s = new Socket(host, 801);
			in = new DataInputStream(s.getInputStream());
			out = s.getOutputStream();
			out.write(IPacket.REQ_GETSTR);
			int response = s.getInputStream().read();
			if (response == IPacket.REP_GETSTR) {
				new DataInputStream(s.getInputStream()).readLong();
				res = new File(projectDir.getCanonicalPath() + "\\repository.xml");
				if (!res.exists()) {
					res.createNewFile();
				}
			
				is = s.getInputStream();
				br = new BufferedReader(new InputStreamReader(is, "utf-8"));
				bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(res), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					bw.write(line + "\r\n");
				}
				bw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(bw, br);
			close(in, null);
			close(is, out);
			close(s);
		}

		List defineXmlFiles = new ArrayList();
		Digester digester = new Digester();
		digester.push(defineXmlFiles);
		digester.setValidating(false);
		// digester.register(
		// "-//CES Information Co, Ltd.//DTD FlowDefine 1.0//EN",
		// "file:///" + Globals.getCesHome() + "/Repository.dtd");

		String pattern = "Repository/DefineXmlFiles/DefineXmlFile";
		digester.addObjectCreate(pattern, DefineXmlFile.class);
		digester.addSetProperties(pattern);
		digester.addBeanPropertySetter(pattern + "/Name", "packageName");
		digester.addBeanPropertySetter(pattern + "/Author", "author");
		digester.addBeanPropertySetter(pattern + "/Status", "status");
		digester.addBeanPropertySetter(pattern + "/CreatedDate", "createdDate");
		digester.addBeanPropertySetter(pattern + "/FileName", "fileName");
		digester.addSetNext(pattern, "add");
		digester.parse(new InputStreamReader(new FileInputStream(res), "utf-8"));
		return defineXmlFiles;
	}

	/**
	 * 删除流程
	 * 
	 * @param file1
	 * @throws Exception
	 */
	public static void deleteProcess(File file, String host) throws Exception {
		Socket s = null;
		DataInputStream in = null;
		OutputStream out = null;
		int result = 0;
		
		try {
			s = new Socket(host, 801);
			in = new DataInputStream(s.getInputStream());
			out = s.getOutputStream();
			out.write(IPacket.REP_STATUSCONTROL);
			out.write(4);
			out.write(file.getName().getBytes().length);
			out.write(file.getName().getBytes());

			result = in.read();
			if (result == IPacket.REP_STATUSCONTROL) {
				if (in.read() == 0) {
					
				} else {
					throw new WFException("删除失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(in, out);
			close(s);
		}
		
	}

	/**
	 * 注册流程
	 * 
	 * @param file1
	 * @throws Exception
	 */
	public static void registerProcess(File file, String host) throws Exception {
		Socket s = null;
		DataInputStream in = null;
		OutputStream out = null;
		int result = 0;
		
		try {
			s = new Socket(host, 801);
			in = new DataInputStream(s.getInputStream());
			out = s.getOutputStream();
			out.write(IPacket.REP_STATUSCONTROL);
			out.write(1);
			out.write(file.getName().getBytes().length);
			out.write(file.getName().getBytes());
			out.flush();
			result = in.read();
			if (result == IPacket.REP_STATUSCONTROL) {
				if (in.read() == 0) {
					
				} else {
					throw new WFException("注册失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(in, out);
			close(s);
		}
	}

	/**
	 * 停止流程
	 * 
	 * @param file1
	 * @throws Exception
	 */
	public static void stopProcess(File file, String host) throws Exception {
		Socket s = null;
		DataInputStream in = null;
		OutputStream out = null;
		int result = 0;
		
		try {
			s = new Socket(host, 801);
			in = new DataInputStream(s.getInputStream());
			out = s.getOutputStream();
			out.write(IPacket.REP_STATUSCONTROL);
			out.write(2);
			out.write(file.getName().getBytes().length);
			out.write(file.getName().getBytes());
			result = in.read();
			if (result == IPacket.REP_STATUSCONTROL) {
				result = in.read();
				if (result == 0) {
					
				} else if (result == 2) {
					throw new WFException("该流程尚有实例在运行，不能执行停止！");
				} else if (result == 3) {
					throw new WFException("流程当前状态不允许执行停止！");
				} else {
					throw new WFException("服务器异常！" + result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(in, out);
			close(s);
		}

	}

	/**
	 * 启动流程
	 * 
	 * @param file1
	 * @throws Exception
	 */
	public static void startProcess(File file, String host) throws Exception {
		Socket s = null;
		DataInputStream in = null;
		OutputStream out = null;
		int result = 0;
		
		try {
			s = new Socket(host, 801);
			in = new DataInputStream(s.getInputStream());
			out = s.getOutputStream();
			out.write(IPacket.REP_STATUSCONTROL);
			out.write(3);
			out.write(file.getName().getBytes().length);
			out.write(file.getName().getBytes());
			result = in.read();
			if (result == IPacket.REP_STATUSCONTROL) {
				if (in.read() == 0) {
					
				} else {
					throw new WFException("启动失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(in, out);
			close(s);
		}
	}
	
	/**
	 * 关闭输入流，输出流
	 * @param inputStream
	 * @param outputStream
	 */
	private static void close(InputStream inputStream, OutputStream outputStream) throws IOException{
		StringBuffer sb = new StringBuffer();
		if (null != outputStream) {
			try {
				outputStream.close();
			} catch (IOException e) {
				sb.append("字节输出流关闭失败："+e.getMessage());
				sb.append("\n");
			}
			outputStream = null;
		}
		if (null != inputStream) {
			try {
				inputStream.close();
			} catch (IOException e) {
				sb.append("字节输入流关闭失败："+e.getMessage());
			}
			inputStream = null;
		}
		if (sb.length() > 0) {
			throw new IOException(sb.toString());
		}
	}
	
	/**
	 * 关闭缓冲输入流，缓冲输出流
	 * @param writer
	 * @param reader
	 */
	private static void close(Writer writer, Reader reader) throws IOException{
		StringBuffer sb = new StringBuffer();
		if (null != writer) {
			try {
				writer.close();
			} catch (IOException e) {
				sb.append("writer关闭失败："+e.getMessage());
				sb.append("\n");
			}
			writer = null;
		}
		if (null != reader) {
			try {
				reader.close();
			} catch (IOException e) {
				sb.append("reader关闭失败："+e.getMessage());
			}
			reader = null;
		}
		if (sb.length() > 0) {
			throw new IOException(sb.toString());
		}
	}
	
	/**
	 * 关闭socket
	 * @param socket
	 */
	private static void close(Socket socket) throws IOException{
		StringBuffer sb = new StringBuffer();
		if (null != socket) {
			try {
				socket.close();
			} catch (IOException e) {
				sb.append("socket关闭失败："+e.getMessage());
			}
			socket = null;
		}
		if (sb.length() > 0) {
			throw new IOException(sb.toString());
		}
	}
}
