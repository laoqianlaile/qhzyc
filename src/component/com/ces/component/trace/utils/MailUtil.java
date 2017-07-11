package com.ces.component.trace.utils;

import com.ces.config.utils.ComponentFileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class MailUtil {

	private static Log log = LogFactory.getLog(MailUtil.class);

	/**
	 * 邮件服务器端口
	 */
	private static int port;
	/**
	 * 邮件服务器
	 */
	private static String server;
	/**
	 * 发送者
	 */
	private static String from;
	/**
	 * 发件人的用户名
	 */
	private static String username;
	/**
	 * 发件人的密码
	 */
	private static String password;
	/**
	 * 用来实现附件添加的组件
	 */
	private Multipart multipart = new MimeMultipart();

	static {
		try {
			Properties mailProps = new Properties();
			mailProps.load(new FileInputStream(ComponentFileUtil.getConfigPath() + "trace/mail.properties"));
			port = Integer.parseInt(mailProps.getProperty("port"));
			server = mailProps.getProperty("server");
			from = mailProps.getProperty("from");
			username = mailProps.getProperty("username");
			password = mailProps.getProperty("password");
		} catch (IOException e) {
			log.error("加载邮件服务器配置失败");
		}
	}

	public static MailUtil newInstance() {
		return new MailUtil();
	}

	/**
	 * 发送邮件
	 * @param email 收件人
	 * @param subject 主题
	 * @param mailBody 邮件正文
	 * @param files 附件
	 * @throws Exception
	 */
	public void sendEmail(String email, String subject, String mailBody, String[] files) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", server);
		props.put("mail.smtp.port", String.valueOf(port));
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage msg = new MimeMessage(session);
		msg.setSubject(subject, "UTF-8");
		setBody(mailBody);
		msg.setSentDate(new Date());
		msg.setFrom(new InternetAddress(from));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
		if (files != null && files.length > 0) {
			for (String file : files) {
				addFileAffix(file);
			}
		}
		msg.setContent(multipart);
		msg.saveChanges();
		Transport transport = session.getTransport("smtp");
		transport.connect(server, username, password);
		transport.sendMessage(msg, msg.getAllRecipients());
		transport.close();
	}

	/**
	 * 设置邮件内容,并设置其为文本格式或HTML文件格式，编码方式为UTF-8
	 *
	 * @param mailBody
	 * @return
	 */
	private void setBody(String mailBody) throws Exception {
		BodyPart bp = new MimeBodyPart();
		bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + mailBody,
				"text/html;charset=UTF-8");
		// 在组件上添加邮件文本
		multipart.addBodyPart(bp);
	}

	/**
	 * 增加发送附件
	 *
	 * @param filename 邮件附件的地址，只能是本机地址而不能是网络地址，否则抛出异常
	 * @return
	 */
	private void addFileAffix(String filename) throws Exception {
		BodyPart bp = new MimeBodyPart();
		FileDataSource fileds = new FileDataSource(filename);
		bp.setDataHandler(new DataHandler(fileds));
		// 发送的附件前加上一个用户名的前缀
		bp.setFileName(fileds.getName());
		// 添加附件
		multipart.addBodyPart(bp);
	}

	public static void main(String[] args) throws Exception {
		MailUtil.newInstance().sendEmail("599257009@qq.com", "BT123", "ZW!@#", null);
	}

}