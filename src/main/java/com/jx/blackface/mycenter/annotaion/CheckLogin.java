package com.jx.blackface.mycenter.annotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.jx.argo.interceptor.PreInterceptorAnnotation;
import com.jx.blackface.mycenter.annotaion.impl.CheckLoginImpl;



/***
 * 检查登录的cookie是否还存在
 * @author duxiaofei
 * @date   2016年1月11日
 */
@PreInterceptorAnnotation(CheckLoginImpl.class)
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CheckLogin {

}
