package com.ces.xarch.plugins.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 导出数据时需要忽略属性的标记.
 * <p>描述:导出数据时需要忽略属性的标记</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author 方俊新(fang.junxin@cesgroup.com.cn)
 * @date 2015-05-12 09:20:17
 * @version 1.0.2015.0512
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportIgnore {

	
}
