package com.jx.blackface.mycenter.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.jx.argo.BeatContext;
import com.jx.blackface.gaea.usercenter.entity.BFLoginEntity;
import com.jx.blackface.mycenter.frame.RSBLL;
import com.jx.blackface.mycenter.utils.CookieUtils;
import com.jx.blackface.mycenter.utils.EntityUtils;
import com.jx.blackface.mycenter.vo.LoginUserVO;

public class CommonUtils {
	public static final String cookieName = "lvuser";
	
	/***
	 * 通过cookie判断用户是否还存在
	 * @param beat
	 * @return
	 */
	public static LoginUserVO getLoginEntity(BeatContext beat){
		LoginUserVO loginVo = null;
		if(CommonUtils.checkCookieName(cookieName, beat.getRequest())){
			String userid = CommonUtils.getUserIdFormCookie(cookieName, beat.getRequest());
			if(StringUtils.isNotBlank(userid)){
				BFLoginEntity loginEntity = null;
				try {
					loginEntity = RSBLL.getstance().getLoginService().getLoginEntityById(Long.valueOf(userid));
				} catch (Exception e) {
					System.out.println("获取用户失败userid:"+userid);
					e.printStackTrace();
				}
				if(null != loginEntity){
					loginVo = EntityUtils.transferEntity(loginEntity, LoginUserVO.class);
				}
			}
		}
		return loginVo;
	}

	public static long getLoginuserid(BeatContext beat){
		long uid = 0l;
		/*****调用通用调取cookie包 Start*******/
		if(CommonUtils.checkCookieName(cookieName, beat.getRequest())){
			String userid = CommonUtils.getUserIdFormCookie(cookieName, beat.getRequest());
			if(StringUtils.isNotBlank(userid)){
				BFLoginEntity loginEntity = null;
				try {
					loginEntity = RSBLL.getstance().getLoginService().getLoginEntityById(Long.valueOf(userid));
				} catch (Exception e) {
					System.out.println("获取用户失败userid:"+userid);
					e.printStackTrace();
				}
				if(null != loginEntity){
					uid = loginEntity.getUserid();
				}
			}
		}
		/*****End*******/
		return uid;
	}
	
	/***
	 * 从cookie中获取用户id
	 * @param request
	 */
	public static String getUserIdFormCookie(String cookieName,HttpServletRequest request){
		String userid="";
		try {
			String cookieValues = CookieUtils.getCookieValueByName(cookieName, request);
			if(StringUtils.isNotBlank(cookieValues)){
				if(StringUtils.contains(cookieValues, ":")){
					String[] splitCookieValues = StringUtils.split(cookieValues, ":");
					if(splitCookieValues.length > 0 && StringUtils.isNotBlank(splitCookieValues[0])){
						userid = splitCookieValues[0];
					}
				}else{
					userid = cookieValues;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userid;
	}
	
	/***
	 * 检查此cookie是否存在
	 * @param cookieName
	 * @param request
	 * @return true 存在 false 不存在
	 */
	public static boolean checkCookieName(String cookieName,HttpServletRequest request){
		boolean checkFlag = false;
		try {
			checkFlag = CookieUtils.checkCookieName(cookieName, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return checkFlag;
	}
	
	public static boolean isChinese(String text){
		if(StringUtils.isBlank(text)){
			return false;
		}
		Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
		Matcher m = p_str.matcher(text);
		if (!m.find() || !m.group(0).equals(text)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断字符串是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){  
	    Pattern pattern = Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?");  
	    return pattern.matcher(str).matches();     
	}
}
