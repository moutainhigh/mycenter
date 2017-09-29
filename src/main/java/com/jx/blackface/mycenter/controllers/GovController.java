/**
 * 
 */

package com.jx.blackface.mycenter.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jx.argo.ActionResult;
import com.jx.argo.annotations.Path;
import com.jx.blackface.mycenter.annotaion.CheckLogin;
import com.jx.blackface.mycenter.common.CommonUtils;
import com.jx.blackface.mycenter.frame.RSBLL;
import com.jx.blackface.mycenter.utils.ActionResultUtils;
import com.jx.blackface.mycenter.utils.EnterpriseUtils;
import com.jx.blackface.mycenter.utils.WAQUtils;
import com.jx.blackface.mycenter.vo.LoginUserVO;
import com.jx.blackface.tools.webblack.Constant;
import com.jx.blackface.tools.webblack.Env;
import com.jx.blackface.tools.webblack.appoint.AppointHelper;
import com.jx.blackface.tools.webblack.auth.AuthHelper;
import com.jx.blackface.tools.webblack.checkname.CheckNameHelper;
import com.jx.blackface.tools.webblack.checkname.entity.FoundCheckEntity;
import com.jx.blackface.tools.webblack.exception.WebblackException;
import com.jx.blackface.tools.webblack.query.qichacha.QichachaHelper;
import com.jx.blackface.tools.webblack.query.qichacha.entity.QichachaEntity;
import com.jx.blackface.tools.webblack.user.RegistHelper;
import com.jx.blackface.tools.webblack.user.entity.UserFtEntity;
import com.jx.blackface.tools.webblack.user.entity.UserGovEntity;
import com.jx.blackface.tools.webblack.utils.RandomValueUtils;
import com.jx.service.enterprise.contract.ILvEnterpriseRoleRelationService;
import com.jx.service.enterprise.entity.LvEnterpriseBusinessEntity;



/**
 * 对接工商
 *
 * <p>detailed comment</p>
 * @author chuxuebao 2015年12月28日
 * @see
 * @since 1.0
 */
@Path("gov")
@CheckLogin
public class GovController extends BaseController {

	private static String city_BJ = "北京";
	static{
//		Env.initVerifyPath("D:/opt/blackface/webblack/javaOcrTrain");
	}
	
	@Path("business/checkUserAndPwd")
	public ActionResult checkUserAndPwd(){
		String userName = WAQUtils.HTMLEncode(request().getParameter("userName"));
		String passWord = WAQUtils.HTMLEncode(request().getParameter("passWord"));
		String enterpriseId = WAQUtils.HTMLEncode(request().getParameter("enterpriseId"));
		String sessionId = null;
		try {
			sessionId = AuthHelper.postLogin(userName, passWord);
		} catch (WebblackException e) {
			e.printStackTrace();
		}
		boolean checkLoginWork = AuthHelper.checkLoginWork(sessionId);
		if(checkLoginWork){
			// 保存注册用户信息
			saveCheckNamePerson(enterpriseId, sessionId, userName, passWord);
		}
		return ActionResultUtils.renderText(checkLoginWork + "");
	}
	@Path("business/postRegistUser")
	public ActionResult postRegistUser(){
		JSONObject reJson = new JSONObject();
		String businessKey = WAQUtils.HTMLEncode(request().getParameter("businessKey"));
		String enterpriseId = WAQUtils.HTMLEncode(request().getParameter("enterpriseId"));
		String cerNo = WAQUtils.HTMLEncode(request().getParameter("cerNo"));
		String userName = WAQUtils.HTMLEncode(request().getParameter("userName"));
		
		String loginName = null;
		String userPwd = null;
		
		if(StringUtils.isNotBlank(cerNo)){
			Map<String, String> personValues = null;
			try {
				personValues = RSBLL.getstance().getEpEnterprisePersonService().getAllValueByIdNum(cerNo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(personValues != null && !personValues.isEmpty()){
				loginName = personValues.get("checkNameAccount");
				userPwd = personValues.get("checkNamePassword");
				if(StringUtils.isNotBlank(userPwd) && StringUtils.isNotBlank(loginName)){
					reJson = checkLogin(loginName, userPwd, businessKey, enterpriseId, cerNo, userName);
					return ActionResultUtils.renderJson(reJson);
				}
			}
		}
		
		LoginUserVO loginVo = CommonUtils.getLoginEntity(beat());
		if(loginVo == null){
			reJson.put("result", "false");
			reJson.put("msg", "请重新登陆");
		}
		
		// 工商局注册新用户
		loginName = RandomValueUtils.getRandomUserName();
		userPwd = RandomValueUtils.getRandomPassword();
		UserGovEntity userGovEntity = new UserGovEntity(cerNo, userName, loginName, userPwd);
		if(loginVo == null){
			userGovEntity.setMobile(RandomValueUtils.getTel()); // 用户手机
			userGovEntity.setEmail(RandomValueUtils.getEmail(6, 13)); // 用户邮箱
		}else{
			userGovEntity.setMobile(loginVo.getUserphone()); // 用户手机
			userGovEntity.setEmail(loginVo.getEmail()); // 用户邮箱
		}
		
		String postRegist = null;
		try {
			postRegist = RegistHelper.postRegist(userGovEntity);
		} catch (Exception e) {
			e.printStackTrace();
			reJson.put("msg", e.getMessage());
			reJson.put("result", "false");
			return ActionResultUtils.renderJson(reJson);
		}
		
		JSONObject parseObject = JSON.parseObject(postRegist);
		if(StringUtils.isNotBlank(postRegist)
				&& !StringUtils.contains(postRegist, "exception") 
				&& parseObject != null && parseObject.containsKey("data")){
			reJson = checkLogin(loginName, userPwd, businessKey, enterpriseId, cerNo, userName);
		}else{
			reJson.put("result", "false");
		}
		return ActionResultUtils.renderJson(reJson);
	}

	/**
	 * @param reJson
	 * @param businessKey
	 * @param enterpriseId
	 * @param loginName
	 * @param userPwd
	 */
	private JSONObject checkLogin(String loginName, String userPwd, String businessKey, String enterpriseId, String cerNo, String userName) {
		JSONObject reJson = new JSONObject();
		reJson.put("result", "false");
		// 检测用户登录是否有效
		String sessionId = null;
		try {
			sessionId = AuthHelper.postLogin(loginName, userPwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(sessionId)){
			boolean checkLoginWork = false;
			try {
				checkLoginWork = AuthHelper.checkLoginWork(sessionId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(checkLoginWork){
				LvEnterpriseBusinessEntity businessEntity = null;
				try {
					businessEntity = RSBLL.getstance().getEpEnterpriseBusinessService().getBusinessEntityByBusinessKey(businessKey);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(businessEntity != null){
					Map<String, String> map = new HashMap<String, String>();
					map.put("checkNameAccount", loginName);
					map.put("checkNamePassword", userPwd);
					try {
						RSBLL.getstance().getEpEnterpriseBusinessDataService().saveEnterpriseBusinessData(enterpriseId, businessEntity.getBusinessId() + "", map, EnterpriseUtils.getLoginInfo(request()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 保存注册用户信息
				saveCheckNamePerson(enterpriseId, sessionId, loginName, userPwd);
				reJson.put("result", "true");
			}else{
				reJson.put("msg", "注册失败，请联系工作人员");
			}
		}else{
			reJson.put("msg", "当前身份证号已有用户，请重新输入身份证号。");
		}
		return reJson;
	}

	private void saveCheckNamePerson(String enterpriseId, String sessionId, String loginName, String userPwd){
		UserGovEntity userInfo = null;
		int index = 0;
		do{
			try {
				userInfo = RegistHelper.getUserInfo(sessionId);
			} catch (WebblackException e1) {
				e1.printStackTrace();
			}
		}while(userInfo == null && index++ < 10);

		if(userInfo != null){
			// 保存企业相关用户
			Map<String, String> personMap = new HashMap<String, String>();
			personMap.put("idNum", userInfo.getCerNo());
			personMap.put("name", userInfo.getUserName());
			personMap.put("sex", userInfo.getSex());
			personMap.put("email", userInfo.getEmail());
			personMap.put("phoneNum", userInfo.getMobile());
			personMap.put("checkNameAccount", loginName);
			personMap.put("checkNamePassword", userPwd);
			long personId = 0L;
			try {
				personId = RSBLL.getstance().getEpEnterprisePersonService().saveEnterprisePersonEntity(enterpriseId, personMap, EnterpriseUtils.getLoginInfo(request()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(personId > 0L){
				try {
					RSBLL.getstance().getEpEnterpriseRoleRelationService().saveRoleRelationEntity(Long.parseLong(enterpriseId), ILvEnterpriseRoleRelationService.ROLETYPE_CHECKNAME_PERSON, personId, null, EnterpriseUtils.getLoginInfo(request()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Path("/business/postGovLogin")
	public ActionResult postGovLogin(){
		String userName = WAQUtils.HTMLEncode(request().getParameter("userName"));
		String passWord = WAQUtils.HTMLEncode(request().getParameter("passWord"));
		String sessionId = null;
		if(StringUtils.isBlank(passWord) || StringUtils.isBlank(userName)){
			try {
				sessionId = AuthHelper.postLogin();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
				sessionId = AuthHelper.postLogin(userName, passWord);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", Env.JSESSIONID);
		jsonObject.put("value", sessionId);
		return ActionResultUtils.renderJson(jsonObject.toJSONString());
	}
	@Path("/business/postGovFoundcheck")
	public ActionResult postGovFoundcheck(){
		String hangye = WAQUtils.HTMLEncode(request().getParameter("hangye"));
		String zihao = WAQUtils.HTMLEncode(request().getParameter("zihao"));
		String sessionId = null;
		try {
			sessionId = AuthHelper.postLogin();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String foundCheck = null;
		if(StringUtils.isNotBlank(sessionId)){
			FoundCheckEntity foundCheckEntity = new FoundCheckEntity();
			foundCheckEntity.setHangye(hangye);
			foundCheckEntity.setHbdm(WAQUtils.HTMLEncode(request().getParameter("hbdm")));
			foundCheckEntity.setHydm(WAQUtils.HTMLEncode(request().getParameter("hydm")));
			foundCheckEntity.setQuanming(WAQUtils.HTMLEncode(request().getParameter("quanming")));
			foundCheckEntity.setShijian(WAQUtils.HTMLEncode(request().getParameter("shijian")));
			foundCheckEntity.setZihao(zihao);
			foundCheckEntity.setZihaohangye(WAQUtils.HTMLEncode(request().getParameter("zihaohangye")));
			foundCheckEntity.setSuperNameId("");
			try {
				foundCheck = CheckNameHelper.foundCheck(sessionId, foundCheckEntity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<QichachaEntity> searchEnterprise = null;
		if(StringUtils.isBlank(foundCheck)){
			// 工商局又不能用了
			try {
				searchEnterprise = QichachaHelper.searchEnterprise(zihao, city_BJ);
			} catch (Exception e) {
				e.printStackTrace();
				foundCheck = "{\"errorNum\":1,\"jyz\":[],\"cc\":[{\"entName\":\"系统繁忙\",\"rule\":\"请稍后再试\"}]}";
			}
		}
		if(StringUtils.isBlank(foundCheck)){
			if(searchEnterprise != null && !searchEnterprise.isEmpty()){
				for(QichachaEntity qichachaEntity:searchEnterprise){
					if(!StringUtils.equals(qichachaEntity.getEntStatus(), "吊销")){
						foundCheck = "{\"errorNum\":1,\"jyz\":[],\"cc\":[{\"entName\":\"" + qichachaEntity.getName() + "\",\"rule\":\"全行业字号_查重列表(查重)\"}]}";
						break;
					}
				}
			}else{
				foundCheck = "{\"errorNum\":0,\"jyz\":[],\"cc\":[]}";
			}
		}
		if(StringUtils.isBlank(foundCheck)){
			foundCheck = "{\"errorNum\":1,\"jyz\":[],\"cc\":[{\"entName\":\"系统繁忙\",\"rule\":\"请稍后再试\"}]}";
		}
		return ActionResultUtils.renderJson(foundCheck);
	}

	@Path("business/appointCancel")
	public ActionResult appointCancel(){
		String enterpriseId = WAQUtils.HTMLEncode(request().getParameter("enterpriseId"));
		Map<String, String> params = null;
		try {
			params = RSBLL.getstance().getEpEnterpriseBusinessDataService().getBusinessDataMapByEnterpriseId(enterpriseId);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		String sessionId = null;
		try {
			sessionId = AuthHelper.postLogin();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(params != null && !params.isEmpty()){
			try {
				AppointHelper.appointCancel(sessionId, params.get("gov-regOrgCode"), params.get("gov-reqId"));
				AppointHelper.gwssiResetAppointment(sessionId, params.get("gov-regOrgCode"), params.get("gov-appointmentId"), params.get("gov-appointmentNo"));
			} catch (WebblackException e) {
				e.printStackTrace();
			}
		}
		return ActionResultUtils.renderText("");
	}
	
	@Path("business/appoint/registUser")
	public ActionResult registUser(){
		String enterpriseId = WAQUtils.HTMLEncode(request().getParameter("enterpriseId"));
		Map<String, String> allValue = null;
		try {
			allValue = RSBLL.getstance().getEpEnterpriseService().getAllValueByEnterpriseId(enterpriseId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String orgCode = allValue.get("gov-regOrgCode");
		if(StringUtils.equals(orgCode, "110106000")){
			// 石景山预约用户注册
			String registUserCerNo = allValue.get("registUserCerNo");
			String registUserName = allValue.get("registUserName");
			String userPhone = allValue.get("mobilePhoneNo");
			String userName = allValue.get("checkNameAccount");
			String userPwd = allValue.get("checkNamePassword");
			String ftAppointAccount = StringUtils.trim(allValue.get("ftAppointAccount"));
			String ftAppointPassword = StringUtils.trim(allValue.get("ftAppointPassword"));
			
			if(StringUtils.isBlank(ftAppointAccount) || StringUtils.isBlank(ftAppointPassword)){
				if(StringUtils.isBlank(registUserCerNo) || StringUtils.isBlank(registUserName)){
					String sessionId = AuthHelper.postTryLogin(userName, userPwd);
					if(StringUtils.isBlank(sessionId)){
						// 登陆失败
						String msg = "getSessionId is null. enterpriseId = " + enterpriseId + ", userName = " + registUserName + ", userPwd = " + userPwd;
						System.out.println(msg);
						return ActionResultUtils.renderText("error");
					}
					UserGovEntity userInfo = null;
					try {
						userInfo = RegistHelper.getUserInfo(sessionId);
					} catch (Exception e) {
						e.printStackTrace();
					}
					registUserName = userInfo.getUserName();
					registUserCerNo = userInfo.getCerNo();
					Map<String, String> enterpriseMap = new HashMap<String, String>();
					enterpriseMap.put("registUserCerNo", registUserCerNo);
					enterpriseMap.put("registUserName", registUserName);
					enterpriseMap.put("enterpriseId", enterpriseId);
					try {
						RSBLL.getstance().getEpEnterpriseService().saveEnterpriseAllEntity(enterpriseMap, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Map<String, String> personValues = null;
				try {
					personValues = RSBLL.getstance().getEpEnterprisePersonService().getAllValueByIdNum(registUserCerNo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(personValues != null && !personValues.isEmpty()){
					ftAppointAccount = personValues.get("ftAppointAccount");
					ftAppointPassword = personValues.get("ftAppointPassword");
				}
			}
			
			if(StringUtils.isBlank(ftAppointAccount) || StringUtils.isBlank(ftAppointPassword)){
				ftAppointPassword = RandomValueUtils.getRandomPassword();
				UserFtEntity userFtEntity = new UserFtEntity();
				userFtEntity.setUser_idcard(registUserCerNo);
				userFtEntity.setUser_name(registUserName);
				userFtEntity.setUser_phone(userPhone);
				userFtEntity.setUser_pwd(ftAppointPassword);
				boolean registerFlag = false;
				try {
					registerFlag = RegistHelper.registerFtUser(orgCode, userFtEntity);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(!registerFlag){
					// 注册失败
					return ActionResultUtils.renderText("error");
				}else{
					Map<String, String> personMap = new HashMap<String, String>();
					personMap.put("ftAppointAccount", registUserCerNo);
					personMap.put("ftAppointPassword", ftAppointPassword);
					personMap.put("idNum", registUserCerNo);
					try {
						RSBLL.getstance().getEpEnterprisePersonService().saveEnterprisePersonEntity(enterpriseId, personMap, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					Map<String, String> enterpriseData = new HashMap<String, String>();
					enterpriseData.put("ftAppointAccount", ftAppointAccount);
					enterpriseData.put("ftAppointPassword", ftAppointPassword);
					try {
						RSBLL.getstance().getEpEnterpriseBusinessDataService().saveEnterpriseBusinessData(enterpriseId, "2020", enterpriseData, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else{
				try {
					String ftSessionId = Constant.ftMycenterSessionIdArray[RandomValueUtils.getNum(0, Constant.ftMycenterSessionIdArray.length - 1)];
					AuthHelper.loginFtUser(orgCode, ftSessionId, ftAppointAccount, ftAppointPassword);
				} catch (Exception e) {
					e.printStackTrace();
					return ActionResultUtils.renderText("error");
				}
				
			}
		}
		return ActionResultUtils.renderText("");
	}
}
