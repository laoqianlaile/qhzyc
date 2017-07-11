package com.ces.xarch.plugins.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * 导出资源路径注解.
 * <p>描述:导出资源路径注解</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author 方俊新(fang.junxin@cesgroup.com.cn)
 * @date 2015-5-13 10:54:44
 * @version 1.0.2015.0513
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportSourcePath {

}
