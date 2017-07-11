package com.ces.trace.test;

import com.ces.config.utils.ComponentFileUtil;
import com.ces.xarch.core.web.listener.XarchListener;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Log4jConfigurer;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by 黄翔宇 on 15/7/1.
 */
@ContextConfiguration(
		locations={
				"classpath*:spring-test.xml"
		}
)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration
public class BaseTest extends AbstractJUnit4SpringContextTests {

	static boolean hasInject = false;

	@BeforeClass
	public static void beforeClass() throws FileNotFoundException {
		Log4jConfigurer.initLogging("classpath:spring-test-log4j.xml");
	}

	@Before
	public void init() {
		if (!hasInject) {
			ReflectionTestUtils.setField(new XarchListener(), "ctx", applicationContext, ApplicationContext.class);
			ComponentFileUtil.setProjectPath(new File("WebRoot").getAbsolutePath());
			hasInject = true;
		}
	}

}
