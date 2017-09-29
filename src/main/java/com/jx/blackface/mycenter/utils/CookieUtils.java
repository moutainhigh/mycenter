package com.jx.blackface.mycenter.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jx.blackface.gaea.usercenter.entity.BFLoginEntity;
import com.jx.blackface.mycenter.frame.RSBLL;

public class CookieUtils {
	
	/***
	 * 检查此cookie名称是否存在且值不为空 
	 * @param cookicName
	 * @param request
	 * @return true 不为空 false 为空
	 * @throws Exception
	 */
    public static boolean checkCookieName(String cookicName,HttpServletRequest request) throws Exception{ 
	     //根据cookieName取cookieValue 
		Cookie cookies[] = request.getCookies();
       String cookieValue = null; 
       boolean userFlag = false; 
       if(cookies!=null){ 
          for(int i=0;i<cookies.length; i++){
             if (cookicName.equals(cookies[i].getName())) { 
                cookieValue = cookies[i].getValue(); 
                break; 
             } 
          }
          if(StringUtils.isNotBlank(cookieValue)){
          	long userid =0l;
  			if(StringUtils.contains(cookieValue, ":")){
  				String[] splitCookieValues = StringUtils.split(cookieValue, ":");
  				if(splitCookieValues.length > 0 && StringUtils.isNotBlank(splitCookieValues[0])){
  					userid =Long.valueOf(splitCookieValues[0]) ;
  				}
  			}else{
  				userid = Long.valueOf(cookieValue);
  			}
  			if(userid > 0){
  				BFLoginEntity loginEntity = RSBLL.getstance().getLoginService().getLoginEntityById(userid);
  				if(null != loginEntity){
  					userFlag = true;
  				}
  			}
          }
          
       } 
       //如果cookieValue为空,返回, 
       if(cookieValue==null){ 
      	 return false;
       }
       if(!userFlag){
      	 return false;
       }
       return true;
     } 
    
    /***
     * 根据cookie名称取cookie的值  (注:使用值时需要看下值是否需要处理)
     * @param cookieName
     * @param request
     * @return 
     */
    public static String getCookieValueByName(String cookieName,HttpServletRequest request) throws Exception{
    	//根据cookieName取cookieValue 
		Cookie cookies[] = request.getCookies(); 
        String cookieValue = ""; 
        if(cookies!=null){ 
           for(int i=0;i<cookies.length; i++){
              if (cookieName.equals(cookies[i].getName())) {
                 cookieValue = cookies[i].getValue(); 
                 break; 
              } 
           }
        }
        return cookieValue;
    }
    
    /**
     * 删除指定名称的cookie
     * @param request
     * @param response
     */
    public static void deleteCookie(String cookieName,HttpServletRequest request, HttpServletResponse response) throws Exception{
    	 Cookie cookies[] = request.getCookies(); 
    	 if(cookies!=null){ 
             for(int i=0;i<cookies.length; i++){
                    if (cookieName.equals(cookies[i].getName())) { 
                    	Cookie cookie = new Cookie(cookieName,null);
                        cookie.setMaxAge(0);
//                        cookie.setPath("/");
                        cookie.setDomain(".lvzheng.com");
                        response.addCookie(cookie);
                        break;
                    } 
             }
      } 
    }
    
    
    
    
}
