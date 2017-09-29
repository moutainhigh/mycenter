package com.jx.blackface.mycenter.controllers;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jx.argo.ActionResult;
import com.jx.argo.BeatContext;
import com.jx.argo.Model;
import com.jx.argo.annotations.Path;
import com.jx.blackface.gaea.sell.entity.LvzSellProductEntity;
import com.jx.blackface.gaea.usercenter.entity.BFEmployersEntity;
import com.jx.blackface.gaea.usercenter.utils.CookieUtils;
import com.jx.blackface.gaea.vendor.entity.VendorServeEntity;
import com.jx.blackface.messagecenter.core.contract.ICallService;
import com.jx.blackface.messagecenter.core.entity.CallEntity;
import com.jx.blackface.mycenter.annotaion.CheckLogin;
import com.jx.blackface.mycenter.common.CommonUtils;
import com.jx.blackface.mycenter.common.LocationPage;
import com.jx.blackface.mycenter.frame.RSBLL;
import com.jx.blackface.mycenter.utils.ActionResultUtils;
import com.jx.blackface.mycenter.utils.CouponUtils;
import com.jx.blackface.mycenter.utils.DicUtils;
import com.jx.blackface.mycenter.utils.EnterpriseUtils;
import com.jx.blackface.mycenter.utils.EntityUtils;
import com.jx.blackface.mycenter.utils.LvMapUtils;
import com.jx.blackface.mycenter.utils.PageUtils;
import com.jx.blackface.mycenter.utils.WAQUtils;
import com.jx.blackface.mycenter.vo.AgencyTaskMappingVo;
import com.jx.blackface.mycenter.vo.AgencyTaskVo;
import com.jx.blackface.mycenter.vo.LoginUserVO;
import com.jx.blackface.orderplug.buzs.OrderBuz;
import com.jx.blackface.tools.blackTrack.TrackLogUtils;
import com.jx.blackface.tools.blackTrack.entity.WebLogs;
import com.jx.blackface.tools.webblack.auth.AuthHelper;
import com.jx.blackface.tools.webblack.exception.WebblackException;
import com.jx.blackface.tools.webblack.localtax.LocalTaxHelper;
import com.jx.blackface.tools.webblack.query.checkname.CheckNameQuery;
import com.jx.blackface.tools.webblack.query.checkname.entity.NameIndCoEntity;
import com.jx.blackface.tools.webblack.utils.DateUtils;
import com.jx.service.enterprise.contract.ILvEnterpriseBusinessService;
import com.jx.service.enterprise.contract.ILvEnterpriseRoleRelationService;
import com.jx.service.enterprise.entity.LvEnterpriseBusinessEntity;
import com.jx.service.enterprise.entity.LvEnterpriseDicDataEntity;
import com.jx.service.enterprise.entity.LvEnterpriseEntity;
import com.jx.service.enterprise.entity.LvEnterpriseIndustryRelationEntity;
import com.jx.service.enterprise.entity.LvEnterpriseMainBusinessEntity;
import com.jx.service.enterprise.entity.LvEnterpriseOperatingRangeEntity;
import com.jx.service.enterprise.entity.LvEnterprisePartnerPayEntity;
import com.jx.service.enterprise.entity.LvEnterprisePersonEntity;
import com.jx.service.enterprise.entity.LvEnterpriseRoleDataEntity;
import com.jx.service.enterprise.entity.LvEnterpriseRoleRelationEntity;
import com.jx.service.newcore.entity.SorderEntity;
import com.jx.service.newcore.entity.SorderExtEntity;
import com.jx.service.workflow.entity.LvHiActEntity;
import com.jx.service.workflow.entity.LvProcInstEntitiy;
import com.jx.service.workflow.entity.LvTaskEntity;

/***
 * 服务流程controller
 * @author duxiaofei
 * @date   2015年12月19日
 */
@Path("/mywf")
@CheckLogin
public class MyWfController extends BaseController{

//	private static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private String VAR_LOCK_BUS_KEY = "lockBusinessKey";
	
	private String VAR_WEB_USER_ID = "webUserId";
	
	private String VAR_BUS_ID = "busid";
	
	private String VAR_CITY = "city";
	
	private String PROC_DEF_KEY_BJ_SELF_REG = "bj-all-self-help-company_reg";
	private String PROC_DEF_KEY_BJ_SELF_CHANGE = "bj-all-self-help-company_change";
	
	private String WEB_HOST = "http://www.lvzheng.com";
	
	private Integer VAR_CITY_BJ = 1; // 默认使用北京城市
	
	private static long SELL_ID= 36646075134465l;//用来获取咨询服务价格  回来后需要改为页面获取id
	
	private String PROC_DEF_KEY_BJ_COMPANY_REG  = "bj-all-company_reg";
	private String PROC_DEF_KEY_BJ_LOCAL_TAX  = "bj-all-local_tax_reg";
	private String PROC_DEF_KEY_BJ_NATIONAL_TAX  = "bj-all-national_tax_reg";
	private String ISPROCESS = "1";//会开启流程
	

	
	@Path("/index.html")
	public ActionResult gotoMyIndex(){
		return gotoMyIndex(1);
	}
	
	public ActionResult index(long pageIndex){
		Model model = model();
		int pageSize = 10;
		Map<String, Object> variable = new HashMap<String, Object>();
		// 查询当前登录人
//		variable.put("prodcateid", "3000");
		variable.put(VAR_WEB_USER_ID, CommonUtils.getLoginuserid(beat()));
		
		List<String> varKeyList = new ArrayList<String>();
		varKeyList.add(VAR_BUS_ID);
		List<LvProcInstEntitiy> pageHisProcList = null;
		try {
			pageHisProcList = RSBLL.getstance().getWfHistoryService().getPageHisProcListWithVarByVariableWithOutSubProc(variable, (int)(pageIndex - 1), (int)pageSize, varKeyList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(pageHisProcList != null && !pageHisProcList.isEmpty()){
			for(LvProcInstEntitiy pageHisProc:pageHisProcList){
				Map<String, Object> processVariables = pageHisProc.getProcessVariables();
				if(processVariables == null){
					continue;
				}
				Object busidObj = processVariables.get(VAR_BUS_ID);
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
			
			long count = 0L;
			try {
				count = RSBLL.getstance().getWfHistoryService().getPageHisProcListWithVarByVariableWithOutSubProcCount(variable);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 构建分页
			PageUtils.buildPageModel(model, pageIndex, count, pageSize, "/mywf/index","", ".html");
			
			model.add("pageHisProcList", pageHisProcList);
		}
		long pageHisProcCount = 0L;
		try {
			pageHisProcCount = RSBLL.getstance().getWfHistoryService().getHisProcCountByVariable(variable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.add("pageHisProcCount", pageHisProcCount);
		model.add("simpleDateFormat", simpleDateFormat);
		//转到的页面路径
		model.add("LocationPage", new LocationPage("mywf", "allserver","mywf"));
		return menuview("index");
	}
	
	@Path("/payServIndex/{pageIndex:\\d+}")
	public ActionResult getPayServByPageIndex(int pageIndex){
		return payServIndex(pageIndex);
	}
	
	public ActionResult payServIndex(int pageIndex){
		Model model = model();
		int pageSize = 10;
		long count = 0;
		List<VendorServeEntity> pageVendorServList = null;
		List<Map<String,String>> vendorServMap = new ArrayList<Map<String,String>>();
		long userId = CommonUtils.getLoginuserid(beat());
//		String condition = " userid = "+ 38546352173313L ;
		String condition = " userid = "+userId;
		try {
			
			//count = RSBLL.getstance().getVendorServeService().getVendorServesCountBycondition(condition);
			PageUtils.buildPageModel(model, pageIndex, count, pageSize, "/mywf/index","", ".html");
			pageVendorServList = RSBLL.getstance().getVendorServeService().getVendorServeListBycondition(condition, 1, 100, "");
			List<String> zyOrderidList = new ArrayList<String>();
			for(VendorServeEntity entity : pageVendorServList){
				//通过平台orderid查询自营的orderid
				List<String> ZYorderids = getZYorderidListByPTorderid(entity.getOrderid()+"");
				//List<String> ZYorderids = getZYorderidListByPTorderid("41442261603841");
				if(ZYorderids != null && ZYorderids.size() > 0){
					zyOrderidList.addAll(ZYorderids);
				}
			}
			
			List<LvProcInstEntitiy> lvProcInstList = null;
			
			Map<String, Object> variable = new HashMap<String, Object>();
			variable.put("taskOrderId", zyOrderidList);
			List<String> varKeyList = new ArrayList<String>();
			varKeyList.add(VAR_BUS_ID);
			lvProcInstList = RSBLL.getstance().getWfHistoryService().getNativePageHisProcListWithVarByVariable(variable, (int)pageIndex-1, pageSize, varKeyList);
			count = RSBLL.getstance().getWfHistoryService().getNativePageHisProcListWithVarByVariableCount(variable);
			if(lvProcInstList == null || lvProcInstList.size() == 0){
				model.add("LocationPage", new LocationPage("mywf", "allserver","mywf"));
				model.add("tabIndex", "1");
				return menuview("index");
			}
			
			for (LvProcInstEntitiy proInstEntity : lvProcInstList) {
				//公司名称
				Map<String, Object> processVariables = proInstEntity.getProcessVariables();
				if(processVariables != null){
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
			
			model().add("pageVendorServList", lvProcInstList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 构建分页
		PageUtils.buildPageModel(model, pageIndex, count, pageSize, "/mywf/payServIndex","", "");
		model.add("LocationPage", new LocationPage("mywf", "allserver","mywf"));
		model.add("tabIndex", "1");
		return menuview("index");
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
	
	
	public Map<String,String> getShowTaskContent(List<LvTaskEntity> hisTaskList){
		int index = -1;//下标
		String endTime = "";
		Map<String,String> map = null;
		
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
			map = new HashMap<String, String>();
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
	
	//代办业务详情2
	@Path("/getTaskDetail.html")
	public ActionResult getTaskDetail(){
		String procInstId = request().getParameter("procInstId");
		String serviceName = request().getParameter("serviceName");
		String pro_def_id = request().getParameter("proDefId");
		String deleteReasonFlag = request().getParameter("deleteReasonFlag");
		if(deleteReasonFlag != null){
			model().add("deleteReasonFlag", deleteReasonFlag);
		}
		if(StringUtils.isNotBlank(serviceName)){
			model().add("serviceName", serviceName);
		}
		
		//procInstId = "10812236651880448";
		
		List<LvTaskEntity> hisTaskList = null;
		try {
			hisTaskList = RSBLL.getstance().getWfHistoryService().getPageHisTaskListByProcessInstanceId(procInstId, 0, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(hisTaskList == null || hisTaskList.size() == 0){
			//转到的页面路径
			model().add("LocationPage", new LocationPage("", "taskDetail","mywf"));
			return menuview("index");
		}
		
		Map<String,String> map = getShowTaskContent(hisTaskList);//前台需要展示的当前环节数据
		if(map == null){
			List<String> showTaskList = null;
			Map<String,String> handleNodeMap = null;
			//判断流程类型（公司注册或地税报道或国税报道....）
			String[] strArr = pro_def_id.split(":");
			if(PROC_DEF_KEY_BJ_COMPANY_REG.equals(strArr[0])){
				showTaskList = AgencyTaskMappingVo.getShowCompanyRegList();
				handleNodeMap = AgencyTaskMappingVo.getCompanyRegHandleNodeMap();
			}else if(PROC_DEF_KEY_BJ_LOCAL_TAX.equals(strArr[0])){
				showTaskList = AgencyTaskMappingVo.getShowlocalTaxList();
				handleNodeMap = AgencyTaskMappingVo.getLocalTaxHandleNodeMap();
			}else if(PROC_DEF_KEY_BJ_NATIONAL_TAX.equals(strArr[0])){
				showTaskList = AgencyTaskMappingVo.getShownationalTaxList();
				handleNodeMap = AgencyTaskMappingVo.getNationalTaxHandleNodeMap();
			}
			
			if(showTaskList != null && handleNodeMap != null){
				model().add("handleNodeKeyList", showTaskList);
				model().add("handleNodeMap", handleNodeMap);
			}
			
			//转到的页面路径
			model().add("LocationPage", new LocationPage("", "taskDetail","mywf"));
			return menuview("index");
		}
		
		model().addAll(map);
		//前台需要展示的历史环节数据
		List<AgencyTaskVo> list = getHisHandleNodeData(hisTaskList,pro_def_id);
		list = EntityUtils.sortList(list, "datatime", false);
		
		if(list != null){
			model().add("hisTaskNodes", list);
		}
		
		//转到的页面路径
		model().add("LocationPage", new LocationPage("", "taskDetail","mywf"));
		return menuview("index");
	}
	
	
	public List<AgencyTaskVo> getHisHandleNodeData(List<LvTaskEntity> taskList,String proc_def_id){
		List<AgencyTaskVo> list = new ArrayList<AgencyTaskVo>();
		List<String> showTaskList = null;
		Map<String,String> handleNodeMap = null;
		Map<String,String> handleNoteMap = null;
		Map<String,String> handleStateMap = null;
		
		//String proc_def_id = taskEntity.getProcessDefinitionId();
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
		
		model().add("handleNodeKeyList", showTaskList);
		model().add("handleNodeMap", handleNodeMap);
		
		for(LvTaskEntity taskEntity : taskList){
			for(int i=0;i<showTaskList.size();i++){
				if((taskMapping(taskEntity.getTaskDefinitionKey()) == null 
						? taskEntity.getTaskDefinitionKey() : taskMapping(taskEntity.getTaskDefinitionKey()))
						.equals(showTaskList.get(i))){
					AgencyTaskVo taskVo = new AgencyTaskVo();
					taskVo.setDatatime(DateUtils.getFormatDateStr(taskEntity.getEndTime(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
					taskVo.setHandleStateText(handleStateMap.get(showTaskList.get(i)));
					taskVo.setHandleNoteText(handleNoteMap.get(showTaskList.get(i)));
					list.add(taskVo);
					break;
				}
			}
		}
		return list;
	}
	
	@Path("/index/{pageIndex:\\d+}.html")
	public ActionResult gotoMyIndex(long pageIndex){
		//增加条件判断
		String type = "";
		try {
			type = CookieUtils.getCookieValueByName("type", request());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(type != "" && type.equals("1")){//加载付费代理服务列表
			return payServIndex((int)pageIndex);
		}else{//加载默认免费服务列表
			return index(pageIndex);
		}
	}
	
	@Path("/page/{enterpriseId:[\\w-]+}/{businessKey:[\\w-]+}")
	public ActionResult pageRegTask(String enterpriseId, String businessKey){
		// 页面转跳
		long uid = CommonUtils.getLoginuserid(beat());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(VAR_CITY, VAR_CITY_BJ); // 默认是北京
		map.put(VAR_WEB_USER_ID, uid);
		map.put(VAR_BUS_ID, Long.parseLong(enterpriseId));
		Map<String, String> startTask = null;
		try {
			startTask = RSBLL.getstance().getWfTaskService().startTask(PROC_DEF_KEY_BJ_SELF_REG, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String procInstId = startTask.get(PROC_DEF_KEY_BJ_SELF_REG);
		LvHiActEntity currentAct = null;
		try {
			currentAct = RSBLL.getstance().getWfReceiveTaskService().loadCurrentActByProcInstId(procInstId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//埋点
		TrackLogUtils.addMyCenterRedirectTrackInfo(request(), response(), enterpriseId, procInstId,uid);
		
		return redirect("/mywf/company/detail/" + procInstId + "/" + currentAct.getId() + "/" + businessKey);
	}
	
	@SuppressWarnings("static-access")
	@Path("/page/{type:[\\w-]+}")
	public ActionResult pageReg(String type){
		if(StringUtils.equals("reg", type)){
			// 自助公司注册
			Map<String, Object> variable = new HashMap<String, Object>();
			// 查询当前登录人
//			variable.put("prodcateid", "3000");
			variable.put(VAR_WEB_USER_ID, CommonUtils.getLoginuserid(beat()));
			List<String> varKeyList = new ArrayList<String>();
			varKeyList.add(VAR_BUS_ID);
			List<LvProcInstEntitiy> pageHisProcList = null;
			try {
				pageHisProcList = RSBLL.getstance().getWfHistoryService().getPageHisProcListWithVarByVariableWithOutSubProc(variable, 0, 10, varKeyList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(pageHisProcList == null  || pageHisProcList.isEmpty() ){
				//发放优惠券duxf
				try {
					CouponUtils.getInstance().sendCoupons(CommonUtils.getLoginuserid(beat()));
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("优惠券发送失败!");
				}
				// 自动开通
				return redirect("/mywf/wf/openWorkflow/" + PROC_DEF_KEY_BJ_SELF_REG);
			}else if(pageHisProcList.size() == 1){
				// 转到流程首页
				return redirect("/mywf/company/reg/" + pageHisProcList.get(0).getId());
			}else{
				// 转到列表页
				return redirect("/mywf/index.html");
			}
		}else if(StringUtils.equals("change", type)){
			// 自助公司变更
			Map<String, Object> variable = new HashMap<String, Object>();
			// 查询当前登录人
//			variable.put("prodcateid", "3000");
			variable.put(VAR_WEB_USER_ID, CommonUtils.getLoginuserid(beat()));
			List<String> varKeyList = new ArrayList<String>();
			varKeyList.add(VAR_BUS_ID);
			List<LvProcInstEntitiy> pageHisProcList = null;
			try {
				pageHisProcList = RSBLL.getstance().getWfHistoryService().getPageHisProcListWithVarByVariableWithOutSubProc(variable, 0, 10, varKeyList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(pageHisProcList == null  || pageHisProcList.isEmpty() ){
				//发放优惠券duxf
				try {
					CouponUtils.getInstance().sendCoupons(CommonUtils.getLoginuserid(beat()));
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("优惠券发送失败!");
				}
				// 自动开通
				return redirect("/mywf/wf/openWorkflow/" + PROC_DEF_KEY_BJ_SELF_CHANGE);
			}else if(pageHisProcList.size() == 1){
				// 转到流程首页
				return redirect("/mywf/company/reg/" + pageHisProcList.get(0).getId());
			}else{
				// 转到列表页
				return redirect("/mywf/index.html");
			}
		}else{
			StringBuffer requestURL = request().getRequestURL();
			return redirect(requestURL.toString());
		}
		
		
	}
	
	
	@Path("/wf/openWorkflow/{procDefKey:[\\w-]+}")
	public ActionResult openWorkflow(String procDefKey){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(VAR_CITY, VAR_CITY_BJ); // 默认是北京
		map.put(VAR_WEB_USER_ID, CommonUtils.getLoginuserid(beat()));
		// 企业ID为空，创建企业
		LvEnterpriseEntity enterpriseEntity = new LvEnterpriseEntity();
		enterpriseEntity.setCityId(VAR_CITY_BJ);
		Long enterpriseId = null;
		try {
			enterpriseId = RSBLL.getstance().getEpEnterpriseService().addEnterpriseEntity(enterpriseEntity, null) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put(VAR_BUS_ID, enterpriseId);
		Map<String, String> startTask = null;
		try {
			startTask = RSBLL.getstance().getWfTaskService().startTask(procDefKey, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(startTask == null || startTask.isEmpty()){
			// 出错了 ( ▼-▼ )
			return redirect(WEB_HOST);
		}
		return redirect("/mywf/company/reg/" + startTask.get(procDefKey));
	}
	
	
	@Path("/company/reg/{procInstId:[\\w-]+}")
	public ActionResult companyRegIndex(String procInstId){
		WebLogs log = WebLogs.getIntanse(MyWfController.class, "companyRegIndex");
		String webUserId = null;
		try {
			webUserId = RSBLL.getstance().getWfHistoryService().getVariableByProcessInstanceIdAndName(procInstId, VAR_WEB_USER_ID);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		if(!StringUtils.equals(webUserId, CommonUtils.getLoginuserid(beat()) + "")){
			return redirect(WEB_HOST);
		}
		
		Model model = model();
		List<LvHiActEntity> fullReceiveTaskList = null;
		try {
			fullReceiveTaskList = RSBLL.getstance().getWfReceiveTaskService().getFullReceiveTaskListByProcessInstanceId(procInstId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(fullReceiveTaskList != null){
			int i = 0;
			for(LvHiActEntity actEntity:fullReceiveTaskList){
				String actInstId = actEntity.getId();
				if(StringUtils.isNotBlank(actInstId)){
					long unPayCount = 0;
					try {
						// * @return 大于0表示未支付 等于0表示支付成功 －1表示没购买
						unPayCount = OrderBuz.ob.getPayidByactityid(Long.parseLong(actInstId));
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(actEntity.getTaskVariables() == null){
						actEntity.setTaskVariables(new HashMap<String, Object>());
					}
					actEntity.getTaskVariables().put("order-unPayCount", unPayCount);
				}
				// 待优化
				String calledProcessInstanceId = actEntity.getCalledProcessInstanceId();
				if(StringUtils.isNotBlank(calledProcessInstanceId)){
					List<LvTaskEntity> pageHisTaskList = null;
					try {
						pageHisTaskList = RSBLL.getstance().getWfHistoryService().getAllTaskListByProcessInstanceId(calledProcessInstanceId);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					if(pageHisTaskList != null && !pageHisTaskList.isEmpty()){
						String assignee = pageHisTaskList.get(0).getAssignee();
						if(StringUtils.isNotBlank(assignee)){
							BFEmployersEntity employer = null;
							try {
								employer = RSBLL.getstance().getEmployerService().getEmployersEntityById(Long.parseLong(assignee));
							} catch (Exception e) {
								e.printStackTrace();
							}
							if(employer != null){
								actEntity.getTaskVariables().put("employer-name", employer.getRealname()); // 姓名
								actEntity.getTaskVariables().put("employer-photo", employer.getFileid()); // 头像
								actEntity.getTaskVariables().put("employer-phoneNum", employer.getPhonenumber()); // 手机号码
							}
						}
					}
				}
				log.putParam(i+"", JSONObject.toJSONString(actEntity));
			}
			model.add("fullReceiveTaskList", fullReceiveTaskList);
		}
		
		log.printInfoLog();
		//咨询详情
		LoginUserVO loginVo = CommonUtils.getLoginEntity(beat());
		long userid = 0L;
		if(loginVo != null){
			userid=loginVo.getUserid();
		}
		ICallService callservice=RSBLL.getstance().getCallService();
		String condition="userid="+userid+" and callstate=3 and receivestate>0 ";//只查询正常通话结束的通话记录
		getCallRecords(beat(),condition);//通话记录
		CallEntity callEntity=null;
		try {
			callEntity=callservice.getFirstCallByUserid(userid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(callEntity==null){//未购买服务
			model.add("callid", -1);
		}else{
			model.add("callid", callEntity.getCallid());
		}
		//当前有几条可用服务
		try {
			int availableCount=callservice.getCallCountByCondition("userid="+userid+" and callstate=0");
			if(availableCount>0){
				model.add("availableCount", availableCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//获取咨询价格
		LvzSellProductEntity lvp = null;
		try {
			lvp = OrderBuz.ob.loadSellerProductByid(SELL_ID);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(lvp != null){
			model.add("zixunPrice", lvp.getSell_overprice());//咨询价格
		}
		//获取url路径
		model.add("procInstId", procInstId);
		//转到的页面路径
		model.add("LocationPage", new LocationPage("mywf", "self-bj-comany","mywf"));
		return menuview("index");
	}
	/**
	 * 获取通话记录
	 * @param beat
	 * @param condition
	 */
	private static void getCallRecords(BeatContext beat,String condition){
		ICallService callservice=RSBLL.getstance().getCallService();
		int count=0;
		try {
			count = callservice.getCallCountByCondition(condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<CallEntity> calllist=null;
		try {
			calllist = callservice.getCallListbyPage(condition, 1, count, "starttime");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(calllist!=null){
			List<Map<String, Object>> resultlist=new ArrayList<Map<String, Object>>();
			for(CallEntity call : calllist){
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("starttime",simpleDateFormat.format(call.getStarttime()));//咨询时间
				map.put("usetime", call.getUsetime());//咨询时长
				map.put("servicestep", call.getServicestep());//咨询服务
				map.put("comments", call.getComments());//咨询反馈
				Date overtime=call.getVoiceovertime();
				if(overtime!=null){
					long voice=call.getVoiceovertime().getTime();
					long nowtime=new Date().getTime();
					if(voice > nowtime){ //录音未过期
						map.put("voiceUrl", call.getVoicerecord());
					}
				}else{//过期时间为空也有记录
					map.put("voiceUrl", call.getVoicerecord());
				}
				
				resultlist.add(map);
			}
			beat.getModel().add("calllist", resultlist);
		}
	}
	
	/**
	 * 咨询小微
	 * @return
	 */
	@Path("/getConsult")
	public ActionResult getConsult(){
		LoginUserVO loginVo = CommonUtils.getLoginEntity(beat());
		long userid=loginVo.getUserid();
		ICallService callservice=RSBLL.getstance().getCallService();
		CallEntity callEntity=null;
		try {
			callEntity=callservice.getFirstCallByUserid(userid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> map = new HashMap<String, String>();
		if( null != callEntity ){//有咨询服务
			map.put("callid", String.valueOf(callEntity.getCallid()));
			return ActionResultUtils.renderJson(JSON.toJSONString(map));
		}else{//没有咨询服务  去购买
			map.put("callid", "-1");
			return ActionResultUtils.renderJson(JSON.toJSONString(map));
		}
	}

	
	@Path("/company/detail/{procInstId:[\\w-]+}/{taskId:[\\w-]+}")
	public ActionResult companyRegAct(String procInstId, String taskId){
		StringBuilder returnurl=new StringBuilder("http://mycenter.lvzheng.com/mywf/company/detail/").append(procInstId).append("/").append(taskId);
		beat().getModel().add("returnurl", returnurl);
		
		return companyRegAct(procInstId, taskId, "");
	}

	@Path("/company/detail/{procInstId:[\\w-]+}/{taskId:[\\w-]+}/{lockBusinessKey:[\\w-]+}")
	public ActionResult companyRegAct(String procInstId, String taskId, String lockBusinessKey){
		String webUserId = null;
		try {
			webUserId = RSBLL.getstance().getWfHistoryService().getVariableByProcessInstanceIdAndName(procInstId, VAR_WEB_USER_ID);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		if(!StringUtils.equals(webUserId, CommonUtils.getLoginuserid(beat()) + "")){
			return redirect(WEB_HOST);
		}
		
		String activityId =WAQUtils.HTMLEncode(request().getParameter("activityId"));
		int size = 99;
		// 判断 taskId是否已经完成，如果完成，或者开启了小微服务，转跳到结果页
		LvHiActEntity hiActEntity = null;
		try {
			hiActEntity = RSBLL.getstance().getWfReceiveTaskService().loadHiActByActId(taskId);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		long businessId = 0L;
		if(hiActEntity != null){
			if(StringUtils.isBlank(lockBusinessKey)){
				if(hiActEntity.getEndTime() != null || StringUtils.isNotBlank(hiActEntity.getCalledProcessInstanceId())){
					lockBusinessKey = hiActEntity.getAttributes().get("showKey");
				}else{
					try {
						lockBusinessKey = RSBLL.getstance().getWfHistoryService().getTaskVariableByTaskIdAndName(taskId, VAR_LOCK_BUS_KEY);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(StringUtils.isBlank(lockBusinessKey)){
					lockBusinessKey = hiActEntity.getActivityId();
				}
			}
		}

		/**
		 * 默认行业特点
		 */
		List<LvEnterpriseMainBusinessEntity> mainBusinessList = null;
		try {
			mainBusinessList = RSBLL.getstance().getEpEnterpriseMainBusinessService().getDefaultMainBusinessList(size);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mainBusinessList != null){
			model().add("mainBusinessList", mainBusinessList);
		}
		
		LvEnterpriseBusinessEntity businessEntity = null;
		try {
			businessEntity = RSBLL.getstance().getEpEnterpriseBusinessService().getBusinessEntityByBusinessKey(lockBusinessKey);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(businessEntity != null){
			businessId = businessEntity.getBusinessId();
		}
		List<LvEnterpriseBusinessEntity> businessKeyList = null;
		try {
			businessKeyList = RSBLL.getstance().getEpEnterpriseBusinessService().getBusinessKeyListByType(ILvEnterpriseBusinessService.BUSINESS_TYPE_TWO);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String nextBusinessKey = null;
		String lastBusinessKey = null;
		if(businessKeyList != null){
			for(int i = 0; i < businessKeyList.size(); i++){
				LvEnterpriseBusinessEntity enterpriseBusinessEntity = businessKeyList.get(i);
				if(StringUtils.equalsIgnoreCase(lockBusinessKey, enterpriseBusinessEntity.getBusinessKey())
						&& i < businessKeyList.size() - 1 
						&& businessKeyList.get( i + 1) != null){
					nextBusinessKey = businessKeyList.get( i + 1).getBusinessKey();
					lastBusinessKey = businessKeyList.get( ((i > 0 )?i:1) - 1).getBusinessKey();
					break;
				}
			}
		}
		
		// 业务配置信息
		Map<String, String> businessConfig = null;
		try {
			businessConfig = RSBLL.getstance().getEpEnterpriseBusinessService().getBusinessConfigByBusinessKey(lockBusinessKey);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(businessConfig != null && !businessConfig.isEmpty()){
			model().add("businessConfig", businessConfig);
		}

		/**
		 * 企业信息
		 */
		String enterpriseId = null;
		try {
			enterpriseId = RSBLL.getstance().getWfHistoryService().getVariableByProcessInstanceIdAndName(procInstId, "busid");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtils.isBlank(enterpriseId)){
			// 企业ID为空，创建企业
			LvEnterpriseEntity enterpriseEntity = new LvEnterpriseEntity();
			String cityId = null;
			try {
				cityId = RSBLL.getstance().getWfHistoryService().getVariableByProcessInstanceIdAndName(procInstId, "cityid");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(StringUtils.isNotBlank(cityId)){
				enterpriseEntity.setCityId(Long.parseLong(cityId));
			}
			String areaId = null;
			try {
				areaId = RSBLL.getstance().getWfHistoryService().getVariableByProcessInstanceIdAndName(procInstId, "areaId");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(StringUtils.isNotBlank(areaId)){
				enterpriseEntity.setAreaId(Long.parseLong(areaId));
			}
			try {
				enterpriseId = RSBLL.getstance().getEpEnterpriseService().addEnterpriseEntity(enterpriseEntity, null) + "";
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 保存到流程
			try {
				RSBLL.getstance().getWfProcessService().setProcessVariable(procInstId, "busid", Long.parseLong(enterpriseId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			// 业务信息详情
			Map<String, String> businessDataMap = null;
			try {
				businessDataMap = RSBLL.getstance().getEpEnterpriseBusinessDataService().getBusinessDataMapByEnterpriseId(enterpriseId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(businessDataMap != null && !businessDataMap.isEmpty()){
				model().addAll(businessDataMap);
			}
			// 企业主信息
			Map<String, String> enterpriseMap = null;
			try {
				enterpriseMap = RSBLL.getstance().getEpEnterpriseService().getAllValueByEnterpriseId(enterpriseId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			model().addAll(DicUtils.transferDicData(enterpriseMap));
		}
		
		// 支付订单
		if(StringUtils.isNotBlank(taskId) && !StringUtils.equals(taskId, "showflow")){
			long payid = 0;
			try {
				// * @return 大于0表示未支付 等于0表示支付成功 －1表示没购买
				payid = OrderBuz.ob.getPayidByactityid(Long.parseLong(taskId));
			} catch (Exception e) {
				e.printStackTrace();
			}
			model().add("payid", payid);
		}
		
		LoginUserVO loginVo = CommonUtils.getLoginEntity(beat());
		if(loginVo != null){
			model().add("loginVo", loginVo);
			
			//------页面显示付费小微判断
			CallEntity callEntity=null;
			try {
				callEntity=RSBLL.getstance().getCallService().getFirstCallByUserid(loginVo.getUserid());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if( null != callEntity ){//有咨询服务
				model().add("callid", callEntity.getCallid());
			}
			//----------------
		}
		
		LvProcInstEntitiy procInstEntitiy = null;
		try {
			procInstEntitiy = RSBLL.getstance().getWfHistoryService().loadByProcessInstanceId(procInstId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		model().add("DicUtils", DicUtils.class);
		model().add("EnterpriseUtils", EnterpriseUtils.class);
		model().add("enterpriseId", enterpriseId);
		model().add("businessId", businessId);
		if(StringUtils.isNotBlank(nextBusinessKey)){
			model().add("nextBusinessKey", nextBusinessKey);
		}
		if(StringUtils.isNotBlank(lastBusinessKey)){
			model().add("lastBusinessKey", lastBusinessKey);
		}
		model().add("businessKey", lockBusinessKey);
		model().add("procInstId", procInstId);
		model().add("taskId", taskId);
		if(hiActEntity != null){
			model().add("activityId", hiActEntity.getActivityId());
			model().add("hiActEntity", hiActEntity);
		}else{
			model().add("activityId", activityId);
		}
		return view("/mywf/" + procInstEntitiy.getProcessDefinitionKey() + "/act-detail");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/business/enterpriseSave")
	public ActionResult enterpriseSave(){
		String data = WAQUtils.HTMLEncode(request().getParameter("data"));
		String enterpriseId = WAQUtils.HTMLEncode(request().getParameter("enterpriseId"));
		
		if(StringUtils.isBlank(data)){
			return ActionResultUtils.renderText("NULL");
		}
		JSONObject dataJson = JSONObject.parseObject(data);
		
		// 业务信息保存
		List<LvEnterpriseBusinessEntity> businessKeyList = null;
		try {
			businessKeyList = RSBLL.getstance().getEpEnterpriseBusinessService().getBusinessKeyListByType(ILvEnterpriseBusinessService.BUSINESS_TYPE_TWO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(businessKeyList != null && !businessKeyList.isEmpty()){
			for(LvEnterpriseBusinessEntity lvEnterpriseBusinessEntity:businessKeyList){
				String businessKey = lvEnterpriseBusinessEntity.getBusinessKey();
				long businessId = lvEnterpriseBusinessEntity.getBusinessId();
				if(!dataJson.containsKey(businessKey)){
					continue;
				}
				Map businessMap = dataJson.getObject(businessKey, Map.class);
				try {
					RSBLL.getstance().getEpEnterpriseBusinessDataService().saveEnterpriseBusinessData(enterpriseId, businessId + "", businessMap, EnterpriseUtils.getLoginInfo(request()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		// 保存企业库地址信息
		if(dataJson.containsKey("addressData")){
			Map addressDataMap = dataJson.getObject("addressData", Map.class);
			try {
				RSBLL.getstance().getEpEnterpriseAddressService().saveEnterpriseAddressData(enterpriseId, addressDataMap, EnterpriseUtils.getLoginInfo(request()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return ActionResultUtils.renderJson(dataJson);
	}
	
	@Path("/business/enterpriseSubmit")
	public ActionResult enterpriseSubmit(){
		String procInstId = WAQUtils.HTMLEncode(request().getParameter("procInstId"));
		String taskId = WAQUtils.HTMLEncode(request().getParameter("taskId"));
		String nextBusinessKey = WAQUtils.HTMLEncode(request().getParameter("nextBusinessKey"));
		try {
			RSBLL.getstance().getWfProcessService().setProcessVariable(procInstId, VAR_LOCK_BUS_KEY, nextBusinessKey);
			RSBLL.getstance().getWfReceiveTaskService().setReceiveTaskVariable(taskId, VAR_LOCK_BUS_KEY, nextBusinessKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionResultUtils.renderText("");
	}
	
	/**
	 * 企业人员信息保存
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/business/enterpriseRoleSave")
	public ActionResult enterpriseRoleSave(){
		String data = WAQUtils.HTMLEncode(request().getParameter("data"));
		String enterpriseId = WAQUtils.HTMLEncode(request().getParameter("enterpriseId"));
		if(StringUtils.isBlank(data)){
			return ActionResultUtils.renderText("NULL");
		}
		JSONObject dataJson = JSONObject.parseObject(data);
		// 相关角色信息
		JSONArray partnerInfoArray = dataJson.getJSONArray("partnerInfoArray");
		if(partnerInfoArray != null && !partnerInfoArray.isEmpty()){
			for(int i = 0; i < partnerInfoArray.size(); i++){
				JSONObject comPartnerJson = partnerInfoArray.getJSONObject(i);
				// 保存主体信息
				Map partnerInfoMain = comPartnerJson.getObject("partnerInfoMain", Map.class);
				if(partnerInfoMain == null || partnerInfoMain.isEmpty()){
					continue;
				}
				String dataRoleType = comPartnerJson.getString("dataRoleType");
				long roleId = 0;
				if(StringUtils.equalsIgnoreCase(dataRoleType, ILvEnterpriseRoleRelationService.ROLETYPE_LEGALPARTNER)){
					// 保存单位
					try {
						roleId = RSBLL.getstance().getEpEnterpriseService().saveEnterpriseAllEntity(partnerInfoMain, EnterpriseUtils.getLoginInfo(request()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					// 保存人员
					try {
						roleId = RSBLL.getstance().getEpEnterprisePersonService().saveEnterprisePersonEntity(enterpriseId, partnerInfoMain, EnterpriseUtils.getLoginInfo(request()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				partnerInfoMain.put("roleId", roleId);
				// 保存关系与扩展数据
				Map comPartnerExt = comPartnerJson.getObject("partnerInfoExt", Map.class);
				try {
					RSBLL.getstance().getEpEnterpriseRoleRelationService().saveRoleRelationEntity(Long.parseLong(enterpriseId), dataRoleType, roleId, comPartnerExt, EnterpriseUtils.getLoginInfo(request()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 保存股东的出资信息
				if(StringUtils.equalsIgnoreCase(dataRoleType, ILvEnterpriseRoleRelationService.ROLETYPE_LEGALPARTNER) 
						|| StringUtils.equalsIgnoreCase(dataRoleType, ILvEnterpriseRoleRelationService.ROLETYPE_NATURALPARTNER)){
					List<Map> payInfoList = comPartnerJson.getObject("payInfoArray", List.class);
					if(payInfoList == null || payInfoList.isEmpty()){
						continue;
					}
					try {
						RSBLL.getstance().getEpEnterprisePartnerPayService().savePartnerPayList(Long.parseLong(enterpriseId), roleId, dataRoleType, payInfoList);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return ActionResultUtils.renderText("");
	}
	
	/**
	 * 股东出资信息删除
	 * @return
	 */
	@Path("/business/enterprisePartnerPayDel")
	public ActionResult enterprisePartnerPayDel(){
		String payId = WAQUtils.HTMLEncode(request().getParameter("payId"));
		if(StringUtils.isNotBlank(payId)){
			try {
				RSBLL.getstance().getEpEnterprisePartnerPayService().delPartnerPayEntity(Long.parseLong(payId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ActionResultUtils.renderText("");
	}
	
	
	@Path("/business/wfSignlReceiveTask")
	public ActionResult wfSignlReceiveTask(){
		String procInstId = WAQUtils.HTMLEncode(request().getParameter("procInstId"));
		String taskId = WAQUtils.HTMLEncode(request().getParameter("taskId"));
		String variableName = WAQUtils.HTMLEncode(request().getParameter("variableName"));
		String value = WAQUtils.HTMLEncode(request().getParameter("value"));
		
		if(StringUtils.isNotBlank(variableName) && StringUtils.isNotBlank(value)){
			try {
				RSBLL.getstance().getWfReceiveTaskService().setReceiveTaskVariable(taskId, variableName, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LvHiActEntity currentAct = null;
		try {
			currentAct = RSBLL.getstance().getWfReceiveTaskService().loadCurrentActByProcInstId(procInstId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * 当前环节才触发下一任务
		 */
		if(StringUtils.equals(currentAct.getId(), taskId) && StringUtils.isNotBlank(taskId)){
			try {
				RSBLL.getstance().getWfReceiveTaskService().signlReceiveTask(procInstId, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ActionResultUtils.renderText("");
	}
	
	@Path("/business/roleRelationDel")
	public ActionResult roleRelationDel(){
		String roleRelationId = WAQUtils.HTMLEncode(request().getParameter("relationId"));
		if(StringUtils.isBlank(roleRelationId)){
			return ActionResultUtils.renderText("OK");
		}
		try {
			RSBLL.getstance().getEpEnterpriseRoleRelationService().delEnterpriseRoleRelationEntityById(Long.parseLong(roleRelationId), EnterpriseUtils.getLoginInfo(request()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionResultUtils.renderText("OK");
	}
	
	@Path("/business/getOperatingRange")
	public ActionResult getOperatingRange(){
		String addressType = WAQUtils.HTMLEncode(request().getParameter("addressType"));
		String searchKey = WAQUtils.HTMLEncode(request().getParameter("searchKey"));
		String parentId = WAQUtils.HTMLEncode(request().getParameter("parentId"));
		List<LvEnterpriseOperatingRangeEntity> operatingRangeList = null;
		if(StringUtils.isBlank(searchKey)){
			try {
				operatingRangeList = RSBLL.getstance().getEpEnterpriseOperatingRangeService()
						.getOperatingRangeListByAddressTypeAndParentId(addressType, parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
				operatingRangeList = RSBLL.getstance().getEpEnterpriseOperatingRangeService()
						.getOperatingRangeListByAddressTypeAndSearchKey(addressType, searchKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(operatingRangeList != null && !operatingRangeList.isEmpty()){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("operatingRangeList", operatingRangeList);
			return ActionResultUtils.renderJson(jsonObject);
		}
		return ActionResultUtils.renderText("");
	}
	
	@Path("/business/getMainbusiness")
	public ActionResult getMainBusiness(){
		String codeName = WAQUtils.HTMLEncode(request().getParameter("searchKey"));
		String pUniteCode = WAQUtils.HTMLEncode(request().getParameter("pUniteCode"));
		String scopeKey = WAQUtils.HTMLEncode(request().getParameter("scopeKey"));
		
		List<LvEnterpriseMainBusinessEntity> mainBusinessList = null;
		if(StringUtils.isBlank(codeName) && StringUtils.isBlank(scopeKey)){
			try {
				mainBusinessList = RSBLL.getstance().getEpEnterpriseMainBusinessService().getMainBusinessListByPUniteCode(pUniteCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
				mainBusinessList = RSBLL.getstance().getEpEnterpriseMainBusinessService().getMainBusinessListByPUniteCodeAndLikeCodeNameAndLikeScope(pUniteCode, codeName, scopeKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(mainBusinessList != null && !mainBusinessList.isEmpty()){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("mainBusinessList", mainBusinessList);
			return ActionResultUtils.renderJson(jsonObject);
		}
		return ActionResultUtils.renderText("");
	}
	
	@Path("/business/getL1Mainbusiness")
	public ActionResult getL1MainBusiness(){
		List<LvEnterpriseMainBusinessEntity> mainBusinessList = null;
		try {
			mainBusinessList = RSBLL.getstance().getEpEnterpriseMainBusinessService().getMainBusinessListByPUniteCode("00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mainBusinessList != null && !mainBusinessList.isEmpty()){
			JSONArray parseArray = JSON.parseArray(JSON.toJSONString(mainBusinessList));
			for(int i=0; i< parseArray.size(); i++){
				JSONObject jsonObject = parseArray.getJSONObject(i);
				String code = jsonObject.getString("code");
				List<LvEnterpriseMainBusinessEntity> businessList = null;
				try {
					businessList = RSBLL.getstance().getEpEnterpriseMainBusinessService().getMainBusinessListByPUniteCode(code);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(businessList != null && !businessList.isEmpty()){
					jsonObject.put("businessList", businessList);
				}
			}
			return ActionResultUtils.renderJson(parseArray.toJSONString());
		}
		return ActionResultUtils.renderText("");
	}
	@Path("/business/getMainbusinessByText")
	public ActionResult getMainbusinessByText(){
		String relationText = WAQUtils.HTMLEncode(request().getParameter("relationText"));
		int size = 5;
		List<LvEnterpriseMainBusinessEntity> mainBusinessList = null;
		if(StringUtils.isNotBlank(relationText)){
			try {
				mainBusinessList = RSBLL.getstance().getEpEnterpriseMainBusinessService().getMainBusinessListByRelationText(relationText, size);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(mainBusinessList == null || mainBusinessList.isEmpty()){
				if(StringUtils.isNotBlank(relationText) && CommonUtils.isChinese(relationText)){
					new Thread(new SynIndustryRelation(relationText)).start();
				}
			}
		}else{
			try {
				mainBusinessList = RSBLL.getstance().getEpEnterpriseMainBusinessService().getDefaultMainBusinessList(size);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(mainBusinessList != null && !mainBusinessList.isEmpty()){
			for(LvEnterpriseMainBusinessEntity mainBus:mainBusinessList){
				if(StringUtils.isNotBlank(mainBus.getIndustryText())){
					continue;
				}
				mainBus.setIndustryText(relationText);
			}
		}
		return ActionResultUtils.renderJson(JSON.toJSONString(mainBusinessList));
	}
	
	/**
	 * 同步
	 * simple introduction
	 *
	 * <p>detailed comment</p>
	 * @author chuxuebao 2016年1月21日
	 * @see
	 * @since 1.0
	 */
	public static class SynIndustryRelation implements Runnable{
		private String relationText;
		public SynIndustryRelation(String relationText){
			this.relationText = relationText;
		}
		@Override
		public void run() {
			try{
				// 调用工商局接口
				String sessionId = AuthHelper.postLogin();
				List<NameIndCoEntity> queryNameIndCo = CheckNameQuery.queryNameIndCo(sessionId, relationText);
				if(queryNameIndCo != null && !queryNameIndCo.isEmpty()){
					for(NameIndCoEntity nameIndCoEntity:queryNameIndCo){
						double position = nameIndCoEntity.getPosition();
						String value = nameIndCoEntity.getValue();
						if(position <= 0 || !StringUtils.contains(value, ",")){
							continue;
						}
						String text = nameIndCoEntity.getText(); // 行业特点(主营业务:其他谷物种植)
						String industryText = StringUtils.substringBefore(text, "(");
						String mainBusCode = StringUtils.substringBefore(value, ","); // 0119,01
						LvEnterpriseIndustryRelationEntity enterpriseIndustryRelationEntity = new LvEnterpriseIndustryRelationEntity();
						enterpriseIndustryRelationEntity.setIndustryText(WAQUtils.HTMLEncode(industryText));
						enterpriseIndustryRelationEntity.setMainBusinessCode(WAQUtils.HTMLEncode(mainBusCode));
						enterpriseIndustryRelationEntity.setPosition(position + "");
						enterpriseIndustryRelationEntity.setOrderCode((int)nameIndCoEntity.getSort());
						enterpriseIndustryRelationEntity.setRelationText(relationText);
						RSBLL.getstance().getEpEnterpriseMainBusinessService().saveIndustryRelationEntity(enterpriseIndustryRelationEntity);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@Path("/business/getRoleInfo")
	public ActionResult getRoleInfo(){
		String enterpriseId = WAQUtils.HTMLEncode(request().getParameter("enterpriseId"));
		String roleId = WAQUtils.HTMLEncode(request().getParameter("roleId"));
		String roleType = WAQUtils.HTMLEncode(request().getParameter("roleType"));
		if(StringUtils.isBlank(enterpriseId) || StringUtils.isBlank(roleId) || StringUtils.isBlank(roleType)){
			return ActionResultUtils.renderText("none");
		}
		JSONObject jsonObj = new JSONObject();
		if(StringUtils.equals(roleType, ILvEnterpriseRoleRelationService.ROLETYPE_LEGALPARTNER)){
			Map<String, String> allValue = null;
			try {
				allValue = RSBLL.getstance().getEpEnterpriseService().getAllValueByEnterpriseId(roleId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(allValue != null){
				jsonObj.putAll(allValue);
			}
		}else{
			LvEnterprisePersonEntity enterprisePerson = null;
			try {
				enterprisePerson = RSBLL.getstance().getEpEnterprisePersonService().getEnterprisePersonById(Long.parseLong(roleId));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(enterprisePerson != null){
				String jsonString = JSON.toJSONString(enterprisePerson, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
				jsonObj.putAll(JSON.parseObject(jsonString));
				jsonObj.put("id", enterprisePerson.getId() + ""); // TODO 待改善
			}
		}
		
		// 获取人员担任的角色
		LvEnterpriseRoleRelationEntity roleRelationEntity = null;
		try {
			roleRelationEntity = RSBLL.getstance().getEpEnterpriseRoleRelationService().loadEnterpriseRoleRelationEntityByEnterpriseIdAndRoleIdAndRoleType(Long.parseLong(enterpriseId), Long.parseLong(roleId), roleType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(roleRelationEntity != null){
			jsonObj.put("roleRelationId", roleRelationEntity.getId() + "");
			jsonObj.put("roleType", roleRelationEntity.getRoleType());
			List<LvEnterpriseRoleDataEntity> roleDataList = null;
			try {
				roleDataList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleDataListByRoleRelationId(roleRelationEntity.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(roleDataList != null){
				for(LvEnterpriseRoleDataEntity  roleData:roleDataList){
					jsonObj.put(roleData.getDataKey(), roleData.getDataValue());
				}
			}
			
		}

		if(StringUtils.contains(roleType, ILvEnterpriseRoleRelationService.ROLETYPE_NATURALPARTNER)
				||StringUtils.contains(roleType, ILvEnterpriseRoleRelationService.ROLETYPE_LEGALPARTNER)){
			// 股东出资信息
			List<LvEnterprisePartnerPayEntity> partnerPayEntityList = null;
			try {
				partnerPayEntityList = RSBLL.getstance().getEpEnterprisePartnerPayService().getPartnerPayEntityListByRoleId(Long.parseLong(enterpriseId), Long.parseLong(roleId));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(partnerPayEntityList != null && !partnerPayEntityList.isEmpty()){
				JSONArray jsonArray = new JSONArray();
				for(LvEnterprisePartnerPayEntity partner:partnerPayEntityList){
					String jsonString = JSON.toJSONString(partner);
					JSONObject parseObject = JSON.parseObject(jsonString);
					String capitalDate = DateUtils.getFormatDateStr(partner.getCapitalDate(), DateUtils.DATA_FORMAT_YYYY_MM_DD);
					if(capitalDate != null){
						parseObject.put("capitalDate", capitalDate);
					}
					jsonArray.add(parseObject);
				}
				jsonObj.put("partnerPayArray", jsonArray.toJSONString());
			}
		}
		return ActionResultUtils.renderJson(jsonObj);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/business/addPersonRole")
	public ActionResult addPersonRole(){
		String roleId = WAQUtils.HTMLEncode(request().getParameter("roleId"));
		String roleType = WAQUtils.HTMLEncode(request().getParameter("roleType"));
		String enterpriseId = WAQUtils.HTMLEncode(request().getParameter("enterpriseId"));
		String data = WAQUtils.HTMLEncode(request().getParameter("data"));
		Map dataJson = JSON.parseObject(data, Map.class);
		try {
			RSBLL.getstance().getEpEnterpriseRoleRelationService().saveRoleRelationEntity(Long.parseLong(enterpriseId), roleType, Long.parseLong(roleId), dataJson, EnterpriseUtils.getLoginInfo(request()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionResultUtils.renderText("");
	}
	
	@Path("/common/getdicdatalist")
	public ActionResult getDicDataList(){
		String type = WAQUtils.HTMLEncode(request().getParameter("type"));
		String parentId = WAQUtils.HTMLEncode(request().getParameter("parentId"));
		
		JSONObject json = new JSONObject();
		if(StringUtils.equals(type, "enterpriseDicData")){
			List<LvEnterpriseDicDataEntity> enterpriseDicDataList = DicUtils.getEnterpriseDicDataList(parentId);
			if(enterpriseDicDataList != null && !enterpriseDicDataList.isEmpty()){
				for(LvEnterpriseDicDataEntity enterpriseDicDataEntity:enterpriseDicDataList){
					json.put(enterpriseDicDataEntity.getDataKey(), enterpriseDicDataEntity.getDataValue());
				}
			}
		}
		return ActionResultUtils.renderJson(json);
	}
	
	private static Map<String, String> mainMemberRule = new HashMap<String, String>();
	static{
		mainMemberRule.put("directorManager", "secondDirectorManager;director;supervisorChairman;supervisor;supervisor-S;directorManager-S"); // 董事长
		mainMemberRule.put("secondDirectorManager", "directorManager;director;supervisorChairman;supervisor;supervisor-S;directorManager-S"); // 副董事长
		mainMemberRule.put("director", "directorManager;secondDirectorManager;supervisorChairman;supervisor;supervisor-S;directorManager-S"); // 董事
		mainMemberRule.put("manager", "supervisorChairman;supervisor"); // 经理
		mainMemberRule.put("supervisorChairman", "directorManager;secondDirectorManager;director;manager;supervisor;directorManager-S;legalPerson;supervisor-S"); // 监事会主席
		mainMemberRule.put("supervisor", "directorManager;secondDirectorManager;director;manager;supervisorChairman;directorManager-S;legalPerson;supervisor-S"); // 监事
		mainMemberRule.put("legalPerson", "finance;supervisor-S;supervisor;supervisorChairman"); // 法人
		mainMemberRule.put("directorManager-S", "directorManager;secondDirectorManager;director;supervisorChairman;supervisor;supervisor-S"); // 执行董事
		mainMemberRule.put("supervisor-S", "directorManager;secondDirectorManager;director;manager;supervisorChairman;directorManager-S;legalPerson"); // 独立监事
	}
	
	@Path("rule/entMainMemberRule")
	public ActionResult entMainMemberRule(){
		String enterpriseId = WAQUtils.HTMLEncode(request().getParameter("enterpriseId"));
		String roleType = WAQUtils.HTMLEncode(request().getParameter("roleType"));
		String idNum = WAQUtils.HTMLEncode(request().getParameter("idNum"));
		
		LvEnterprisePersonEntity enterprisePerson = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", true);
		try {
			enterprisePerson = RSBLL.getstance().getEpEnterprisePersonService().getEnterprisePersonByIdNum(idNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(enterprisePerson != null){
			List<LvEnterpriseRoleRelationEntity> roleRelationList = null;
			try {
				roleRelationList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleRelationListByEnterpriseIdAndRoleId(Long.parseLong(enterpriseId), enterprisePerson.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(roleRelationList != null){
				String mainMemRuleStr = mainMemberRule.get(roleType);
				for(LvEnterpriseRoleRelationEntity roleRelationEntity:roleRelationList){
					if(StringUtils.contains(";" + mainMemRuleStr + ";", ";" + roleRelationEntity.getRoleType() + ";")){
						jsonObject.put("result", false);
					}
				}
			}
		}
		return ActionResultUtils.renderJson(jsonObject);
	}
	

	@SuppressWarnings("unchecked")
	@Path("rule/entAllRoleRule")
	public ActionResult entAllRoleRule(){
		String enterpriseIdStr = WAQUtils.HTMLEncode(request().getParameter("enterpriseId"));
		ILvEnterpriseRoleRelationService epEnterpriseRoleRelationService = RSBLL.getstance().getEpEnterpriseRoleRelationService();
		// 自然人股东
		List<LvEnterpriseRoleRelationEntity> naturalRelationList = null;
		long enterpriseId = Long.parseLong(enterpriseIdStr);
		try {
			naturalRelationList = epEnterpriseRoleRelationService.getRoleRelationListByEnterpriseIdAndRoleType(enterpriseId, ILvEnterpriseRoleRelationService.ROLETYPE_NATURALPARTNER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(naturalRelationList != null && !naturalRelationList.isEmpty()){
			for(LvEnterpriseRoleRelationEntity naturalRelation:naturalRelationList){
				Map<String, String> personMainValue = null;
				try {
					personMainValue = RSBLL.getstance().getEpEnterprisePersonService().getMainValueById(naturalRelation.getRoleId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				Map<String, String> entRoleData = null;
				try {
					entRoleData = epEnterpriseRoleRelationService.getRoleDataByEnterpriseIdAndRoleTypeAndRoleId(enterpriseId, ILvEnterpriseRoleRelationService.ROLETYPE_NATURALPARTNER, naturalRelation.getRoleId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				String naturalRule = EnterpriseUtils.naturalRule(LvMapUtils.combine(personMainValue, entRoleData));
				if(StringUtils.isNotBlank(naturalRule)){
					return ActionResultUtils.renderText(naturalRule);
				}
			}
		}
		// 法人股东
		List<LvEnterpriseRoleRelationEntity> logicalRelationList = null;
		try {
			logicalRelationList = epEnterpriseRoleRelationService.getRoleRelationListByEnterpriseIdAndRoleType(enterpriseId, ILvEnterpriseRoleRelationService.ROLETYPE_LEGALPARTNER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(logicalRelationList != null && !logicalRelationList.isEmpty()){
			for(LvEnterpriseRoleRelationEntity logicalRelation:logicalRelationList){
				Map<String, String> enterpriseMainValue = null;
				try {
					enterpriseMainValue = RSBLL.getstance().getEpEnterpriseService().getAllValueByEnterpriseId(logicalRelation.getRoleId() + "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Map<String, String> entRoleData = null;
				try {
					entRoleData = epEnterpriseRoleRelationService.getRoleDataByEnterpriseIdAndRoleTypeAndRoleId(enterpriseId, ILvEnterpriseRoleRelationService.ROLETYPE_LEGALPARTNER, logicalRelation.getRoleId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				String naturalRule = EnterpriseUtils.logicalRule(LvMapUtils.combine(enterpriseMainValue, entRoleData));
				if(StringUtils.isNotBlank(naturalRule)){
					return ActionResultUtils.renderText(naturalRule);
				}
			}
		}
		
		Map<String, String> entAllValue = null;
		try {
			entAllValue = RSBLL.getstance().getEpEnterpriseService().getAllValueByEnterpriseId(enterpriseIdStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * 董事会
		 */
		String isDongShiMeeting = entAllValue.get("isDongShiMeeting");
		if(StringUtils.equals(isDongShiMeeting, "1")){
			// 设立董事会
			// 董事长
			long directorManagerCount = 0L;
			try {
				directorManagerCount = epEnterpriseRoleRelationService.getRoleRelationCountByEnterpriseIdAndRoleType(enterpriseId, ILvEnterpriseRoleRelationService.ROLETYPE_DIRECTOR_MANAGER);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(directorManagerCount != 1L){
				return ActionResultUtils.renderText("请设置董事长职位");
			}
			// 董事 + 副董事长
			long directorCount = 0L;
			try {
				directorCount = epEnterpriseRoleRelationService.getRoleRelationCountByEnterpriseIdAndRoleTypes(enterpriseId, ILvEnterpriseRoleRelationService.ROLETYPE_DIRECTOR, ILvEnterpriseRoleRelationService.ROLETYPE_SECOND_DIRECTOR_MANAGER);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(directorCount < 2L){
				return ActionResultUtils.renderText("董事会成员至少2人");
			}
			if(directorCount > 12L){
				return ActionResultUtils.renderText("董事会成员不能多余13人");
			}
			
		}else if(StringUtils.equals(isDongShiMeeting, "2")){
			// 没有设立董事会
			// 执行董事
			long directorManagerCount = 0L;
			try {
				directorManagerCount = epEnterpriseRoleRelationService.getRoleRelationCountByEnterpriseIdAndRoleType(enterpriseId, ILvEnterpriseRoleRelationService.ROLETYPE_DIRECTORMANAGER_S);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(directorManagerCount != 1L){
				return ActionResultUtils.renderText("请设置执行董事职位");
			}
		}else{
			return ActionResultUtils.renderText("请选择是否设立董事会");
		}
		
		/**
		 * 监事会
		 */
		String isJianShiMeeting = entAllValue.get("isJianShiMeeting");
		if(StringUtils.equals(isJianShiMeeting, "1")){
			// 设立监事会
			// 监事会主席
			long supervisorChairmanCount = 0L;
			try {
				supervisorChairmanCount = epEnterpriseRoleRelationService.getRoleRelationCountByEnterpriseIdAndRoleType(enterpriseId, ILvEnterpriseRoleRelationService.ROLETYPE_SUPERVISOR_CHAIRMAN);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(supervisorChairmanCount != 1L){
				return ActionResultUtils.renderText("请设置监事会主席职位");
			}
			// 监事
			long supervisorCount = 0L;
			try {
				supervisorCount = epEnterpriseRoleRelationService.getRoleRelationCountByEnterpriseIdAndRoleTypes(enterpriseId, ILvEnterpriseRoleRelationService.ROLETYPE_SUPERVISOR);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(supervisorCount < 2L){
				return ActionResultUtils.renderText("您的监事会成员输入有误，监事会成员不得少于3人");
			}
			if(supervisorCount > 12L){
				return ActionResultUtils.renderText("您的监事会成员输入有误，监事会成员不得多于13人");
			}
		}else if(StringUtils.equals(isJianShiMeeting, "2")){
			// 没有设立监事会
			// 监事
			long supervisorCount = 0L;
			try {
				supervisorCount = epEnterpriseRoleRelationService.getRoleRelationCountByEnterpriseIdAndRoleType(enterpriseId, ILvEnterpriseRoleRelationService.ROLETYPE_SUPERVISOR_S);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(supervisorCount < 1L || supervisorCount > 2L){
				return ActionResultUtils.renderText("您的监事成员输入有误，监事成员1-2人");
			}
		}else{
			return ActionResultUtils.renderText("请选择是否设立监事会");
		}
		
		// 经理
		long managerCount = 0L;
		try {
			managerCount = epEnterpriseRoleRelationService.getRoleRelationCountByEnterpriseIdAndRoleType(enterpriseId, ILvEnterpriseRoleRelationService.ROLETYPE_MANAGER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(managerCount != 1L){
			return ActionResultUtils.renderText("您还未设立经理，请先设立经理");
		}
		
		// 法人
		long legalPersonCount = 0L;
		try {
			legalPersonCount = epEnterpriseRoleRelationService.getRoleRelationCountByEnterpriseIdAndRoleType(enterpriseId, ILvEnterpriseRoleRelationService.ROLETYPE_LEGALPERSON);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(legalPersonCount != 1L){
			return ActionResultUtils.renderText("您还未设立法定代表人，请先设立法定代表人");
		}
		return ActionResultUtils.renderText("");
	}
	
	private static Map<String, String> valideNaturalWithRoleTypes = new HashMap<String, String>();
	static{
		valideNaturalWithRoleTypes.put("dongshi", "directorManager;secondDirectorManager;director;supervisorChairman;supervisor;supervisor-S;directorManager-S");
		valideNaturalWithRoleTypes.put("jianshi", "directorManager;secondDirectorManager;director;supervisorChairman;supervisor;supervisor-S;directorManager-S;manager");
		valideNaturalWithRoleTypes.put("jingli", "supervisorChairman;supervisor;supervisor-S;manager");
	}
	
	@Path("business/getValideNaturalParts")
	public ActionResult getValideNaturalParts(){
		String enterpriseId = WAQUtils.HTMLEncode(request().getParameter("enterpriseId"));
		String type = WAQUtils.HTMLEncode(request().getParameter("type"));
		if(StringUtils.isBlank(type) || StringUtils.isBlank(enterpriseId)){
			return ActionResultUtils.renderText("");
		}
		List<LvEnterpriseRoleRelationEntity> roleRelationList = null;
		try {
			roleRelationList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleRelationListByEnterpriseIdAndRoleType(Long.parseLong(enterpriseId), ILvEnterpriseRoleRelationService.ROLETYPE_NATURALPARTNER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(roleRelationList != null && !roleRelationList.isEmpty()){
			JSONArray jsonArray = new JSONArray();
			
		loop:for(LvEnterpriseRoleRelationEntity roleRelationEntity:roleRelationList){
				List<LvEnterpriseRoleRelationEntity> roleTypeRelationList = null;
				try {
					roleTypeRelationList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleRelationListByEnterpriseIdAndRoleId(Long.parseLong(enterpriseId), roleRelationEntity.getRoleId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(roleTypeRelationList != null && !roleTypeRelationList.isEmpty()){
					for(LvEnterpriseRoleRelationEntity roleTypeRelationEntity:roleTypeRelationList){
						if(StringUtils.contains(";" + valideNaturalWithRoleTypes.get(type) + ";" , ";" + roleTypeRelationEntity.getRoleType() + ";" )){
							continue loop;
						}
					}
				}
				LvEnterprisePersonEntity enterprisePerson = null;
				try {
					enterprisePerson = RSBLL.getstance().getEpEnterprisePersonService().getEnterprisePersonById(roleRelationEntity.getRoleId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				JSONObject personInfo = JSON.parseObject(JSON.toJSONString(enterprisePerson));
				personInfo.put("roleType", roleRelationEntity.getRoleType());
				jsonArray.add(personInfo);
			}
			return ActionResultUtils.renderJson(jsonArray.toJSONString());
		}
		return ActionResultUtils.renderText("");
	}
	
	@Path("business/updateRegCapital")
	public ActionResult updateRegCapital(){
		String enterpriseId = WAQUtils.HTMLEncode(request().getParameter("enterpriseId"));
		List<LvEnterpriseRoleRelationEntity> enterpriseRelationList = null;
		try {
			enterpriseRelationList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleRelationListByEnterpriseIdAndRoleType(Long.parseLong(enterpriseId), ILvEnterpriseRoleRelationService.ROLETYPE_LEGALPARTNER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String regCapital = "0";
		regCapital = sumCapitalSize(enterpriseRelationList, regCapital);
		List<LvEnterpriseRoleRelationEntity> naturalRelationList = null;
		try {
			naturalRelationList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleRelationListByEnterpriseIdAndRoleType(Long.parseLong(enterpriseId), ILvEnterpriseRoleRelationService.ROLETYPE_NATURALPARTNER );
		} catch (Exception e) {
			e.printStackTrace();
		}
		regCapital = sumCapitalSize(naturalRelationList, regCapital);
		Map<String, String> map = new HashMap<String,String>();
		map.put("regCapital", regCapital);
		map.put("enterpriseId", enterpriseId);
		try {
			RSBLL.getstance().getEpEnterpriseService().saveEnterpriseAllEntity(map, EnterpriseUtils.getLoginInfo(request()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionResultUtils.renderText("");
	}
	/**
	 * @param enterpriseRelationList
	 * @param capitalSizeKey
	 * @param capitalSize
	 */
	private String sumCapitalSize(List<LvEnterpriseRoleRelationEntity> enterpriseRelationList, String regCapital) {
		BigDecimal capitalSize = new BigDecimal(regCapital);
		if(enterpriseRelationList != null && !enterpriseRelationList.isEmpty()){
			for(LvEnterpriseRoleRelationEntity enterpriseRoleRelationEntity:enterpriseRelationList){
				List<LvEnterprisePartnerPayEntity> partnerPayEntityList = null;
				try {
					partnerPayEntityList = RSBLL.getstance().getEpEnterprisePartnerPayService().getPartnerPayEntityListByRoleId(enterpriseRoleRelationEntity.getEnterpriseId(), enterpriseRoleRelationEntity.getRoleId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(partnerPayEntityList == null || partnerPayEntityList.isEmpty()){
					continue;
				}
				for(LvEnterprisePartnerPayEntity partnerPayEntity:partnerPayEntityList){
					String paySize = partnerPayEntity.getPaySize();
					System.out.println(paySize);
					if(StringUtils.isNotBlank(paySize) && CommonUtils.isNumeric(paySize)){
						BigDecimal bigDecimal = new BigDecimal(paySize);
						capitalSize = bigDecimal.add(capitalSize);
					}
				}
			}
		}
		return capitalSize.toString();
	}
	
	@Path("localTaxReg/getComanyInfo")
	public ActionResult getCompanyInfoByCreditCode(){
		String code = request().getParameter("code");
		String htmlStr = "";
		String resultStr = "";
		try {
			htmlStr = LocalTaxHelper.getLocalTaxInfo(code);
		} catch (WebblackException e) {
			e.printStackTrace();
		}
		if(StringUtils.isBlank(htmlStr)){
			return ActionResultUtils.renderText(resultStr);
		}
		Document doc = Jsoup.parse(htmlStr);
		if(doc == null){
			return ActionResultUtils.renderText(resultStr);
		}
		Elements els = doc.select("td div p");
		List<String> list = new ArrayList<String>();
		if(els != null){
			for (Element element : els) {
				String content=element.text();
				content = StringUtils.replace(content, "&nbsp;", "");
				list.add(content);
			}
		}
		resultStr = JSONArray.toJSONString(list);
		return ActionResultUtils.renderText(resultStr);
	}
	
	public static void main(String[] args) {
		List<LvProcInstEntitiy> lvProcInstList = null;
		List<String> orderList = new ArrayList<String>();
		/*for (SorderEntity sorderEntity : sorderList) {
			orderList.add(sorderEntity.getOrderid());
		}*/
		orderList.add("35738705506305");
		orderList.add("35739363489793");
		orderList.add("35469072935937");
		
		Map<String, Object> variable = new HashMap<String, Object>();
		variable.put("taskOrderId", orderList);
		List<String> varKeyList = new ArrayList<String>();
		varKeyList.add("busid");
		try {
			lvProcInstList = RSBLL.getstance().getWfHistoryService().getNativePageHisProcListWithVarByVariable(variable, 0, 100, varKeyList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(lvProcInstList == null || lvProcInstList.size() == 0){
			System.out.println("无数据");
		}else{
			System.out.println(lvProcInstList.size());
		}
	}
	
}
