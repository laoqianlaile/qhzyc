package com.ces.component.trace.utils;

import com.ces.component.sdzycdzjgmdr.service.SdzycdzjgmdrService;
import com.ces.xarch.core.web.listener.XarchListener;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Created by hpsgt on 2016-10-12.
 */
@Component
public class JdbcDaoUtil {
    public static JdbcDaoUtil getInstance() {
        return XarchListener.getBean(JdbcDaoUtil.class);
    }
    public Connection getConnection() {
        try {
            String path =  SdzycdzjgmdrService.class.getResource("/").getPath();
            Properties prop = new Properties();
            String sourcePath = path.replace("/classes/","/conf/db/db.properties").replaceFirst("\\/","");
            InputStream in =new FileInputStream(sourcePath);
            prop.load(in);
            String driver = prop.getProperty("jdbc.driver");

            String url = prop.getProperty("jdbc.url");
            String username = prop.getProperty("jdbc.username");
            String password = prop.getProperty("jdbc.password");
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
