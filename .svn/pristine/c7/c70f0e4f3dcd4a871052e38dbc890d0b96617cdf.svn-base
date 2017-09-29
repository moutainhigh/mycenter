package com.jx.blackface.mycenter.controllers;

import org.apache.commons.lang.StringUtils;

import com.jx.argo.ActionResult;
import com.jx.argo.Model;
import com.jx.argo.annotations.Path;
import com.jx.blackface.gaea.usercenter.entity.BFLoginEntity;
import com.jx.blackface.mycenter.annotaion.CheckLogin;
import com.jx.blackface.mycenter.common.CommonUtils;
import com.jx.blackface.mycenter.common.LocationPage;
import com.jx.blackface.mycenter.frame.RSBLL;
import com.jx.blackface.mycenter.utils.ActionResultUtils;
import com.jx.blackface.mycenter.vo.LoginUserVO;
import com.jx.tools.waq.WAQ;

/****
 * 我的账户controller
 * @author duxiaofei
 * @date   2015年12月19日
 */
@Path("/myaccount")
@CheckLogin
public class MyAccountController extends BaseController{
	
	@Path("/index.html")
	public ActionResult gotoMyIndex(){
		Model model = model();
		LoginUserVO loginVo = CommonUtils.getLoginEntity(beat());
		
		model.add("loginVo", loginVo);
		//转到的页面路径
		model.add("LocationPage", new LocationPage("myaccount", "myaccount","myaccount"));
		return menuview("index");
	}
	/**
	 * 更换手机号
	 * @return
	 */
	@Path("/updatePhone/{userid:\\S+}")
	public ActionResult updatePhone(String userid){
		String phoneNum = beat().getRequest().getParameter("phoneNum")==null?"":WAQ.forXSS().HTMLEncode(beat().getRequest().getParameter("phoneNum"));
		if(StringUtils.isNotBlank(phoneNum)){
			try {
				BFLoginEntity loginE = RSBLL.getstance().getLoginService().getLoginEntityById(Long.valueOf(userid));
				if(null != loginE){
					loginE.setUserphone(phoneNum);
					RSBLL.getstance().getLoginService().updateLoginEntity(loginE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ActionResultUtils.renderJson("{\"error\":}");
			}
		}
		return ActionResultUtils.renderJson("{\"success\":\"true\"}");
	}
	
	/**
	 * 绑定手机号
	 * @return
	 */
	@Path("/setPhone/{userid:\\S+}")
	public ActionResult setPhone(String userid){
		String phoneNum = beat().getRequest().getParameter("phoneNum")==null?"":WAQ.forXSS().HTMLEncode(beat().getRequest().getParameter("phoneNum"));
		if(StringUtils.isNotBlank(phoneNum)){
			try {
				BFLoginEntity loginE = RSBLL.getstance().getLoginService().getLoginEntityById(Long.valueOf(userid));
				if(null != loginE){
					loginE.setUserphone(phoneNum);
					loginE.setAuthenflag(1); //设置未认证用户
					RSBLL.getstance().getLoginService().updateLoginEntity(loginE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ActionResultUtils.renderJson("{\"error\":}");
			}
		}
		return ActionResultUtils.renderJson("{\"success\":\"true\"}");
	}
	
	@Path("/setNewPass/{userid:\\S+}")
	public ActionResult setNewPass(String userid){
		String pass  =beat().getRequest().getParameter("pass")==null?"":beat().getRequest().getParameter("pass");
		if(StringUtils.isNotBlank(pass)){
			try {
				BFLoginEntity loginE = RSBLL.getstance().getLoginService().getLoginEntityById(Long.valueOf(userid));
				if(null != loginE){
					loginE.setPassword(pass);
					RSBLL.getstance().getLoginService().updateLoginEntity(loginE);
					return ActionResultUtils.renderJson("{\"success\":\"true\"}");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("设置密码失败,userid:"+userid);
				return ActionResultUtils.renderJson("{\"error\":}");
			}
		}
		return ActionResultUtils.renderJson("{\"success\":\"false\"}");
	}
	
	@Path("/updatePass/{userid:\\S+}")
	public ActionResult updatePass(String userid){
//		String currentPass  =beat().getRequest().getParameter("currentPass")==null?"":beat().getRequest().getParameter("currentPass");
		String pass  =beat().getRequest().getParameter("pass")==null?"":beat().getRequest().getParameter("pass");
		if(StringUtils.isNotBlank(pass)){
			try {
				BFLoginEntity loginE = RSBLL.getstance().getLoginService().getLoginEntityById(Long.valueOf(userid));
				if(null != loginE){
//					if(!StringUtils.equals(loginE.getPassword(), currentPass)){
//						return ActionResultUtils.renderJson("{\"success\":\"false\"}");
//					}else if(StringUtils.isNotBlank(pass)){
						loginE.setPassword(pass);
						RSBLL.getstance().getLoginService().updateLoginEntity(loginE);
						return ActionResultUtils.renderJson("{\"success\":\"true\"}");
//					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ActionResultUtils.renderJson("{\"error\":}");
			}
		}
		return ActionResultUtils.renderJson("{\"error\":}");
	}
	
	@Path("/saveUserInfo/{userid:\\S+}")
	public ActionResult saveUserInfo(String userid){
		String email  =beat().getRequest().getParameter("email")==null?"":beat().getRequest().getParameter("email");
		String username  =beat().getRequest().getParameter("username")==null?"":beat().getRequest().getParameter("username");
		String address  =beat().getRequest().getParameter("address")==null?"":beat().getRequest().getParameter("address");
		try {
			BFLoginEntity loginE =  RSBLL.getstance().getLoginService().getLoginEntityById(Long.valueOf(userid));
			if(null != loginE){
				loginE.setEmail(email);
				loginE.setUsername(username);
				loginE.setAddress(address);
				RSBLL.getstance().getLoginService().updateLoginEntity(loginE);
				return ActionResultUtils.renderJson("{\"success\":\"true\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("更新用户信息失败userid:"+userid);
		}
		return ActionResultUtils.renderJson("{\"error\":}");
	}
}
