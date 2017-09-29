package com.jx.blackface.mycenter.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.jx.argo.ActionResult;
import com.jx.argo.Model;
import com.jx.argo.annotations.Path;
import com.jx.blackface.gaea.usercenter.entity.BFEmployersEntity;
import com.jx.blackface.gaea.vendor.entity.VendorServeEntity;
import com.jx.blackface.messagecenter.core.entity.MailBFGEntity;
import com.jx.blackface.mycenter.annotaion.CheckLogin;
import com.jx.blackface.mycenter.common.CommonUtils;
import com.jx.blackface.mycenter.common.EmpSatisfactionAndOrdersum;
import com.jx.blackface.mycenter.common.LocationPage;
import com.jx.blackface.mycenter.frame.RSBLL;
import com.jx.blackface.mycenter.service.MyCenterService;
import com.jx.blackface.mycenter.utils.ActionResultUtils;
import com.jx.blackface.mycenter.utils.EntityUtils;
import com.jx.blackface.mycenter.utils.Timers;
import com.jx.blackface.mycenter.vo.AgencyTaskMappingVo;
import com.jx.blackface.mycenter.vo.LoginUserVO;
import com.jx.blackface.orderplug.buzs.OrderBuz;
import com.jx.blackface.orderplug.vo.PayOrderBFVo;
import com.jx.blackface.tools.webblack.utils.DateUtils;
import com.jx.service.enterprise.contract.ILvEnterpriseRoleRelationService;
import com.jx.service.enterprise.entity.LvEnterpriseEntity;
import com.jx.service.newcore.entity.SorderExtEntity;
import com.jx.service.workflow.entity.LvProcInstEntitiy;
import com.jx.service.workflow.entity.LvTaskEntity;

/**
 * 我的主页controller
 * @author duxiaofei
 * @date   2015年12月19日
 */
@Path("/mycenter")
@CheckLogin
public class MyCenterController extends BaseController{
	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private String PROC_DEF_KEY_BJ_COMPANY_REG  = "bj-all-company_reg";
	private String PROC_DEF_KEY_BJ_LOCAL_TAX  = "bj-all-local_tax_reg";
	private String PROC_DEF_KEY_BJ_NATIONAL_TAX  = "bj-all-national_tax_reg";
	private String VAR_BUS_ID = "busid";

	@Path("/index.html")
	public ActionResult gotoMyIndex(){
		Model model = model();
		
		try {
			/*****账户信息start********/
			//获取用户信息
			LoginUserVO loginVo = CommonUtils.getLoginEntity(beat());
//			loginVo = new LoginUserVO();
//			loginVo.setUserid(38546352173313L);
			//获取优惠券总张数
			int couponCount = MyCenterService.getInstan().getUnusedcoupounscount(loginVo.getUserid());
			if(couponCount > 0){
				model.add("couponCount", couponCount);
			}
			
			if(StringUtils.isNotBlank(loginVo.getOpenid())){
				model.add("wxClass", "class=\"wx-on\"");
			}else{
				model.add("wxClass", "class=\"wx\"");
			}
//			if(StringUtils.isNotBlank(loginVo.getEmail())){
//				model.add("emailClass", "class=\"email-on\"");
//			}else{
//				model.add("emailClass", "class=\"email\"");
//			}
			
			//判读是否手机认证
			if(StringUtils.isNotBlank(loginVo.getUserphone()) && loginVo.getAuthenflag() == 1){ 
				model.add("phoneClass", "class=\"phone-on\"");
			}else{
				model.add("phoneClass", "class=\"phone\"");
			}
			model.add("loginVo", loginVo);
			
			/********进行中的服务信息start*********/
			Map<String, Object> variable = new HashMap<String, Object>();
			// 查询当前登录人
			variable.put("webUserId", loginVo.getUserid());
			List<String> varKeyList = new ArrayList<String>();
			varKeyList.add("busid");
			//查询自助的流程实例
			List<LvProcInstEntitiy> doingServerList = RSBLL.getstance().getWfHistoryService().getPageRunProcListWithVarByVariableWithOutSubProc(variable, 0, 5, varKeyList); 
			if(null != doingServerList && !doingServerList.isEmpty()){
				for(LvProcInstEntitiy doingServer : doingServerList){
					Map<String, Object> processVariables = doingServer.getProcessVariables();
					if(processVariables == null){
						continue;
					}
					Object busidObj = processVariables.get("busid");
					if(busidObj == null){
						continue;
					}
					String enterpriseName = null;
					try {
						enterpriseName = RSBLL.getstance().getEpEnterpriseService().getMainValueByEnterpriseIdAndKey(busidObj.toString(), "name");
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(StringUtils.isNotBlank(enterpriseName)){
						processVariables.put("enterpriseName", enterpriseName);
					}
				}
				/*********服务顾问信息start**************/
				//根据此用户获取企业信息
				List<LvEnterpriseEntity> enterpriseEntityList = RSBLL.getstance().getEpEnterpriseService().getEnterpriseListByRoleTypeAndRoleIdWork(ILvEnterpriseRoleRelationService.ROLETYPE_ORDERPERSON, String.valueOf(loginVo.getUserid()));
				if(null != enterpriseEntityList && !enterpriseEntityList.isEmpty()){
					
				}
				//根据企业id获取当前企业负责人id
				String empid = RSBLL.getstance().getWFCompanyService().getBusValueByCompanyIdAndBusKey(String.valueOf(0l), "AE");
				if(StringUtils.isNotBlank(empid)){
					BFEmployersEntity employerEntity = RSBLL.getstance().getEmployerService().getEmployersEntityById(Long.valueOf(empid));
					if(null != employerEntity){
						model.add("employerE", employerEntity);
						model.addAll(EmpSatisfactionAndOrdersum.getStatisfactionAndOrderSum(empid));  
					}
				}
				
				model.add("simpleDateFormat", simpleDateFormat); 
			}
			
			//查询代办的流程实例
			List<LvProcInstEntitiy> agencyProcList = getAgencyServiceList(loginVo.getUserid());
			if(agencyProcList != null && agencyProcList.size() > 0){
				if(doingServerList == null){
					doingServerList = new ArrayList<LvProcInstEntitiy>();
				}
				
				doingServerList.addAll(agencyProcList);
				//通过开始时间倒序排序
				EntityUtils.sortList(doingServerList, "startTime", false);
			}
			
			
			if(doingServerList != null && doingServerList.size() > 0){
				if(doingServerList.size() > 5){
					//只取前五个
					List<LvProcInstEntitiy> showList = new ArrayList<LvProcInstEntitiy>();
					for(int i=0;i<5;i++){
						showList.add(doingServerList.get(i));
					}
					setCurHandleNodeKey(showList);
					model.add("doingServerList", showList);
				}else{
					setCurHandleNodeKey(doingServerList);
					model.add("doingServerList", doingServerList);
				}
			}
			
			//带支付订单start
			List<PayOrderBFVo> userUnpaylist = OrderBuz.ob.getUserUnpaylist(loginVo.getUserid(), 1, 5, "addtime desc");
			if(null != userUnpaylist && !userUnpaylist.isEmpty()){
				model.add("userUnpaylist", userUnpaylist);
			}
			
			
			
			//String aa="32284470234625";
			/****最新消息strt****/
			List<Map<String,String>> mailListMap = new ArrayList<Map<String,String>>(); 
			List<MailBFGEntity> mailEntityList = RSBLL.getstance().getMailBFGService().getMailListbypage("reciveid='"+loginVo.getUserid()+"'", 1, 5, "posttime desc");
			if(null != mailEntityList && !mailEntityList.isEmpty()){
				for(MailBFGEntity mailE : mailEntityList){
					String posttime = Timers.formatLongDate("yyyy-MM-dd hh:mm", mailE.getAddtime());
					 Map<String,String> tempMailMap = BeanUtils.describe(mailE);
					 tempMailMap.put("addtime", posttime);
					 String content=mailE.getContent();
					 int length=content.length();
					 if( length > 30 ){
						 tempMailMap.put("content", content.substring(0, 30));
						 tempMailMap.put("contentmore", content.substring(30, length-1));
					 }else{
						 tempMailMap.put("content", mailE.getContent());
					 }
					 mailListMap.add(tempMailMap);
				}
				model.add("messageList", mailListMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//转到的页面路径
		model.add("LocationPage", new LocationPage("mycenter", "mycenter","mycenter"));
		return menuview("index");
	}
	
	//设置已读
	@Path("/readMessage/{messageId:\\S+}")
	public ActionResult readMessage(String messageId){
		try {
		    MailBFGEntity mailEntity = RSBLL.getstance().getMailBFGService().loadMailEntity(Long.valueOf(messageId));
		    if(null != mailEntity){
		    	mailEntity.setReadstate(1); //设置未已读
		    	RSBLL.getstance().getMailBFGService().updateMail(mailEntity);
		    }
		} catch (Exception e) {
			System.out.println("设置已读消息失败!;messageId:"+messageId);
			e.printStackTrace();
		}
		return ActionResultUtils.renderJson("{\"success\":\"true\"}");
	}
	
	/**查询代办的流程实例*/
	public List<LvProcInstEntitiy> getAgencyServiceList(long userId){
		List<VendorServeEntity> pageVendorServList = null;
		List<LvProcInstEntitiy> lvProcInstList = null;
		List<Map<String,String>> vendorServMap = new ArrayList<Map<String,String>>();
		String condition = " userid = "+ userId ;
		try {
			pageVendorServList = RSBLL.getstance().getVendorServeService().getVendorServeListBycondition(condition, 1, 100, "");
			List<String> zyOrderidList = new ArrayList<String>();
			for(VendorServeEntity entity : pageVendorServList){
				//通过平台orderid查询自营的orderid
				List<String> ZYorderids = getZYorderidListByPTorderid(entity.getOrderid()+"");
				if(ZYorderids != null && ZYorderids.size() > 0){
					zyOrderidList.addAll(ZYorderids);
				}
			}
			
			Map<String, Object> variable = new HashMap<String, Object>();
			variable.put("taskOrderId", zyOrderidList);
			List<String> varKeyList = new ArrayList<String>();
			varKeyList.add(VAR_BUS_ID);
			lvProcInstList = RSBLL.getstance().getWfHistoryService().getNativePageHisProcListWithVarByVariable(variable, 0, 99, varKeyList);
			if(lvProcInstList == null || lvProcInstList.size() == 0){
				return null;
			}
			
			for (LvProcInstEntitiy proInstEntity : lvProcInstList) {
				
				//公司名称
				Map<String, Object> processVariables = proInstEntity.getProcessVariables();
				if(processVariables != null){
					processVariables.put("isAgencyProc", "true");//标记是代办流程
					Object busidObj = processVariables.get(VAR_BUS_ID);
					if(busidObj != null){
						String enterpriseName = null;
						try {
							enterpriseName = RSBLL.getstance().getEpEnterpriseService().getMainValueByEnterpriseIdAndKey(busidObj.toString(), "name");
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(StringUtils.isNotBlank(enterpriseName)){
							processVariables.put("enterpriseName", enterpriseName);
						}
					}
				}
				
				String endTime = DateUtils.getFormatDateStr(proInstEntity.getEndTime(), new SimpleDateFormat("yyyy-MM-dd"));
				processVariables.put("handleEndTime", endTime);
				String startTime = DateUtils.getFormatDateStr(proInstEntity.getStartTime(), new SimpleDateFormat("yyyy-MM-dd"));
				processVariables.put("handleStartTime", startTime);
			
				//服务名称
				String proc_def_id = proInstEntity.getProcessDefinitionId();
				//判断流程类型（公司注册或地税报道或国税报道....）
				String[] strArr = proc_def_id.split(":");
				if(PROC_DEF_KEY_BJ_COMPANY_REG.equals(strArr[0])){//公司注册
					processVariables.put("serviceName", "公司注册");
				}else if(PROC_DEF_KEY_BJ_LOCAL_TAX.equals(strArr[0])){//地税
					processVariables.put("serviceName", "地税报到");
				}else if(PROC_DEF_KEY_BJ_NATIONAL_TAX.equals(strArr[0])){//国税
					processVariables.put("serviceName", "国税报到");
				}else{
					System.out.println("代办服务未知流程："+proc_def_id);
					processVariables.put("serviceName", "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lvProcInstList;
	}
	
	
	/***通过平台orderid查询自营的orderid*/;
	public List<String> getZYorderidListByPTorderid(String ptOrderid){
		List<String> list = new ArrayList<String>();
		//1.查询t_sorder_ext表，通过dataKey=terraceOrderId and dataValue='{ptOrderid}'
		List<SorderExtEntity> sorderExtList = null;
		try {
			sorderExtList = RSBLL.getstance().getSorderService().getSorderExtByDataKeyAndDataValue("terraceOrderId",ptOrderid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(sorderExtList == null || sorderExtList.size() == 0){
			return null;
		}
		
		SorderExtEntity sorderExt = null;
		for(int i=0;i<sorderExtList.size();i++){
			list.add(sorderExtList.get(i).getOrderId()+"");//自营的订单id
		}
		
		return list;
	};

	
	/**获取流程实例当前进行的环节名称  并保存*/
	public void setCurHandleNodeKey(List<LvProcInstEntitiy> list){
		if(list == null || list.size() == 0){
			return;
		}
		
		for (LvProcInstEntitiy entity : list) {
			if(!("true".equals(entity.getProcessVariables().get("isAgencyProc")))){//如果不是代办服务，则继续下一个
				continue;
			}
			List<LvTaskEntity> hisTaskList = null;
			try {
				hisTaskList = RSBLL.getstance().getWfHistoryService().getPageHisTaskListByProcessInstanceId(entity.getId(), 0, 100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(hisTaskList == null || hisTaskList.size() == 0){
				continue;
			}
			
			Map<String,Object> map = getShowTaskContent(hisTaskList);//前台需要展示的当前环节数据
			
			if(map != null){
				entity.getProcessVariables().putAll(map);
			}
		}
		
	}
	
	
	public Map<String,Object> getShowTaskContent(List<LvTaskEntity> hisTaskList){
		int index = -1;//下标
		String endTime = "";
		Map<String,Object> map = null;
		
		// 历史任务
		List<String> showTaskList = null;
		Map<String,String> handleNoteMap = null;
		Map<String,String> handleStateMap = null;
		Map<String,String> handleNodeMap = null;
		
		for(LvTaskEntity taskEntity : hisTaskList){
			String proc_def_id = taskEntity.getProcessDefinitionId();
			//判断流程类型（公司注册或地税报道或国税报道....）
			String[] strArr = proc_def_id.split(":");
			if(PROC_DEF_KEY_BJ_COMPANY_REG.equals(strArr[0])){
				showTaskList = AgencyTaskMappingVo.getShowCompanyRegList();
				handleNoteMap = AgencyTaskMappingVo.getCompanyRegHandleNoteMap();
				handleStateMap = AgencyTaskMappingVo.getCompanyRegHandleStateMap();
				handleNodeMap = AgencyTaskMappingVo.getCompanyRegHandleNodeMap();
			}else if(PROC_DEF_KEY_BJ_LOCAL_TAX.equals(strArr[0])){
				showTaskList = AgencyTaskMappingVo.getShowlocalTaxList();
				handleNoteMap = AgencyTaskMappingVo.getLocalTaxHandleNoteMap();
				handleStateMap = AgencyTaskMappingVo.getLocalTaxHandleStateMap();
				handleNodeMap = AgencyTaskMappingVo.getLocalTaxHandleNodeMap();
			}else if(PROC_DEF_KEY_BJ_NATIONAL_TAX.equals(strArr[0])){
				showTaskList = AgencyTaskMappingVo.getShownationalTaxList();
				handleNoteMap = AgencyTaskMappingVo.getNationalTaxHandleNoteMap();
				handleStateMap = AgencyTaskMappingVo.getNationalTaxHandleStateMap();
				handleNodeMap = AgencyTaskMappingVo.getNationalTaxHandleNodeMap();
			}else{
				return null;
			}
			
			//与前台要展示的节点对比，并记录下标
			for(int i=0;i<showTaskList.size();i++){
				if((taskMapping(taskEntity.getTaskDefinitionKey()) == null 
						? taskEntity.getTaskDefinitionKey() : taskMapping(taskEntity.getTaskDefinitionKey()))
						.equals(showTaskList.get(i))){
					if(index < i){
						index = i;
						endTime = DateUtils.getFormatDateStr(taskEntity.getEndTime(), new SimpleDateFormat("yyyy-MM-dd"));
					}
					break;
				}
			}
		}
		
		if(index == -1){
			return null;
		}else{
			map = new HashMap<String, Object>();
			map.put("curHandleNodeKey", showTaskList.get(index)) ;
			map.put("curHandleNodeText", handleNodeMap.get(showTaskList.get(index)));
			map.put("handleStateText", handleStateMap.get(showTaskList.get(index)));
			map.put("handleNoteText", handleNoteMap.get(showTaskList.get(index)));
			map.put("handleEndTime", endTime);
		}
		return map;
	}
	
	/**判断是否存在任务映射*/
	public String taskMapping(String curTaskDefKey){
		String key = AgencyTaskMappingVo.getExclusiveTaskMapping().get(curTaskDefKey);
		return key;
	}
	//	//退出登录
//	@Path("/outlogin")
//	public ActionResult outLogin(){
//		try {
//			CookieUtils.deleteCookie("lvuser", beat().getRequest(), beat().getResponse());
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("退出登录失败!");
//		}
//		return redirect("http://www.lvzheng.com/index.html");
//	}
}
