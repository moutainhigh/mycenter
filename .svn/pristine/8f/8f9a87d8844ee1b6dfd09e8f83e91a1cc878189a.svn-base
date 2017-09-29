package com.jx.blackface.mycenter.controllers;

import org.apache.commons.lang.StringUtils;

import com.jx.argo.ActionResult;
import com.jx.argo.annotations.Path;
import com.jx.argo.controller.AbstractController;
import com.jx.blackface.gaea.usercenter.utils.CookieUtils;
import com.jx.blackface.mycenter.annotaion.TracePoint;
import com.jx.blackface.mycenter.common.CommonUtils;
import com.jx.blackface.mycenter.frame.RSBLL;
import com.jx.blackface.mycenter.service.MyCenterService;
import com.jx.blackface.mycenter.utils.ActionResultUtils;
import com.jx.service.messagecenter.entity.MobileSmsResultExt;
import com.jx.tools.waq.WAQ;
@TracePoint
public class BaseController extends AbstractController{

	
	@Override
	protected ActionResult redirect(String redirectUrl) {
		// TODO Auto-generated method stub
		return super.redirect(redirectUrl);
	}

	public ActionResult menuview(String view){
		
		long usid  = CommonUtils.getLoginuserid(beat());
		
//		ILoginService us = RSBLL.getstance().getLoginService();
//		BFLoginEntity loginEntity = null;
//		try {
//			loginEntity = us.getLoginEntityById(usid);
//		} catch (Exception e1) {
//			System.out.println("**********查询用户失败,用户id："+usid);
//			e1.printStackTrace();
//		}
//		
//		loginEntity.getUsername();
//		beat().getModel().add("username", loginEntity.getUsername());
		
		int unusercounponscout = 0;
		try {
			unusercounponscout = MyCenterService.getInstan().getUnusedcoupounscount(usid);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		beat().getModel().add("unusedcounpons", unusercounponscout);
		
		//查询消息中心
		int unreadmessagecount = 0;
		
		//埋点入口
		
//		try {
//			unreadmessagecount = MailBuz.mb.getUnreadcount(usid);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		beat().getModel().add("unreadmailcount", unreadmessagecount);
		return super.view(view);
	}
	long getLoginuserid(){
		return CommonUtils.getLoginuserid(beat());
	}
	
	/***
	 * 发送手机验证码
	 * @return 1发送成功 2发送的语音验证码 -1发送失败 error 获取手机号失败
	 */
	@Path("/common/sendPhoneCode")
	public ActionResult sendPhoneCode(){
		String phoneNum = beat().getRequest().getParameter("phoneNum")==null?"":WAQ.forXSS().HTMLEncode(beat().getRequest().getParameter("phoneNum"));
		if(StringUtils.isBlank(phoneNum)){
			return ActionResultUtils.renderJson("{\"error\":}");
		}
		try {
			MobileSmsResultExt  result = RSBLL.getstance().getMoblieSmsService().sendVerifyCode(phoneNum);
			System.out.println("********************"+result.getCode()+"==="+result.getMsg()+"==="+result.isResult());
			if(result.isResult()){
				if(result.getCode() == 2){
					return ActionResultUtils.renderJson("{\"flag\":\"2\"}");
				}
				return ActionResultUtils.renderJson("{\"flag\":\"1\"}");
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return ActionResultUtils.renderJson("{\"flag\":\"-1\"}");
		}
		return ActionResultUtils.renderJson("{\"flag\":\"-1\"}");
	}
	
	/***
	 * 校验手机发送的验证码是否正确
	 * @return true正确 false 错误 error 手机或验证码为空
	 */
	@Path("/common/checkPhoneAndCode")
	public ActionResult checkPhoneAndCode(){
		String phoneNum = beat().getRequest().getParameter("phoneNum")==null?"":WAQ.forXSS().HTMLEncode(beat().getRequest().getParameter("phoneNum"));
		String code = beat().getRequest().getParameter("code")==null?"":WAQ.forXSS().HTMLEncode(beat().getRequest().getParameter("code"));
		
		if(StringUtils.isBlank(phoneNum) || StringUtils.isBlank(code)){
			return ActionResultUtils.renderJson("{\"error\":}");
		}
		try {
			Boolean  result = RSBLL.getstance().getMoblieSmsService().checkVerifyCode(phoneNum, code);
			if(result){
				return ActionResultUtils.renderJson("{\"success\":\"true\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("调用校验手机和验证码方法：checkVerifyCode失败手机号:" + phoneNum +"验证码:"+code);
		}
		return ActionResultUtils.renderJson("{\"success\":\"false\"}");
	}
	protected long getLoginUserid(){
		return CookieUtils.getUseridFromCookie(beat().getRequest());
	}
	
	
}
