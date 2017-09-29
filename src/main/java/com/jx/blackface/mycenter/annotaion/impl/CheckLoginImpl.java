package com.jx.blackface.mycenter.annotaion.impl;

import org.apache.commons.lang.StringUtils;

import com.jx.argo.ActionResult;
import com.jx.argo.BeatContext;
import com.jx.argo.interceptor.PreInterceptor;
import com.jx.blackface.mycenter.common.CommonUtils;
import com.jx.blackface.mycenter.utils.ActionResultUtils;

public class CheckLoginImpl implements PreInterceptor{
	@Override
	public ActionResult preExecute(BeatContext beat) {
//	System.out.println("*********进入menter的地址:"+beat.getRequest().getRequestURL().toString());
		//如果存在此cookie则不跳转
		if(CommonUtils.checkCookieName("lvuser", beat.getRequest())){
			return null;
		}
//	System.out.println("*********不存在lvuser的cookie");
		String path = beat.getRequest().getRequestURL().toString();
		String queryString = beat.getRequest().getQueryString();
		if(StringUtils.isNotBlank(queryString)){
			path += "?" + queryString;
		}
//		System.out.println("***********请求的路径：" + path); 
		try {
			beat.getResponse().sendRedirect("http://www.lvzheng.com/login.html?test=1&path="+path);
			return ActionResultUtils.renderJson("");
		} catch (Exception e) {
			return ActionResultUtils.renderJson("");
		}
	}

}
