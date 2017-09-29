 package com.jx.blackface.mycenter.frame;

import com.jx.argo.ArgoTool;
import com.jx.blackface.gaea.sell.contract.ILvzPackageSellService;
import com.jx.blackface.gaea.sell.contract.ILvzPackageService;
import com.jx.blackface.gaea.sell.contract.ILvzProductService;
import com.jx.blackface.gaea.sell.contract.ILvzSellProductService;
import com.jx.blackface.gaea.usercenter.contract.IAreasService;
import com.jx.blackface.gaea.usercenter.contract.IEmployersService;
import com.jx.blackface.gaea.usercenter.contract.ILoginService;
import com.jx.blackface.gaea.usercenter.contract.IUserCounponsService;
import com.jx.blackface.gaea.vendor.contract.IVendorServeService;
import com.jx.blackface.messagecenter.core.contract.ICallService;
import com.jx.blackface.messagecenter.core.contract.IMailBFGService;
import com.jx.blackface.servicecoreclient.contract.IOrderBFGService;
import com.jx.blackface.servicecoreclient.contract.IOrderVendorService;
import com.jx.blackface.servicecoreclient.contract.IPayOrderBFGService;
import com.jx.service.dic.contract.ILvDicService;
import com.jx.service.enterprise.contract.ILvEnterpriseAddressService;
import com.jx.service.enterprise.contract.ILvEnterpriseBusinessDataService;
import com.jx.service.enterprise.contract.ILvEnterpriseBusinessService;
import com.jx.service.enterprise.contract.ILvEnterpriseDicDataService;
import com.jx.service.enterprise.contract.ILvEnterpriseMainBusinessService;
import com.jx.service.enterprise.contract.ILvEnterpriseOperatingRangeService;
import com.jx.service.enterprise.contract.ILvEnterprisePartnerPayService;
import com.jx.service.enterprise.contract.ILvEnterprisePersonService;
import com.jx.service.enterprise.contract.ILvEnterpriseRoleRelationService;
import com.jx.service.enterprise.contract.ILvEnterpriseService;
import com.jx.service.enterprise.contract.ILvGovService;
import com.jx.service.messagecenter.contract.IMoblieSmsService;
import com.jx.service.newcore.contract.ICouponsService;
import com.jx.service.newcore.contract.ISorderService;
import com.jx.service.newcore.contract.IUserCouponsService;
import com.jx.service.workflow.contract.ILvCompanyService;
import com.jx.service.workflow.contract.ILvHistoryService;
import com.jx.service.workflow.contract.ILvNoticeService;
import com.jx.service.workflow.contract.ILvProcessService;
import com.jx.service.workflow.contract.ILvReceiveTaskService;
import com.jx.service.workflow.contract.ILvTaskQueryService;
import com.jx.service.workflow.contract.ILvTaskService;
import com.jx.spat.gaea.client.GaeaInit;
import com.jx.spat.gaea.client.proxy.builder.ProxyFactory;

public class RSBLL {
	
	private String GAEA_WF = "workflow";
	private String GAEA_UC = "usercenter";
	private String GAEA_EP = "enterprise";
	private String GAEA_UN = "union";
	private String GAEA_MSG ="jxmessage";
	private String GAEA_VENDOR = "vendorcore";
	private String GAEA_SERVICE ="servicecore";
	private String GAEA_SELL ="sellcore";
	
	private String GAEA_DIC = "dicservice";
	
	
	
	static{
		String url = ArgoTool.getConfigFolder()+ArgoTool.getNamespace()+"/gaea.config";
		GaeaInit.init(url);
	}
	
	private static RSBLL newstance = null;
	private static Object lock = new Object();
	public static RSBLL getstance(){
		if(newstance == null){
			synchronized (lock) {
				if(newstance == null){
					newstance = new RSBLL();
				}
			}
		}
		return newstance;
	}
	public IOrderBFGService getOrderBFGService(){
		return ProxyFactory.create(IOrderBFGService.class, "tcp://servicecore/OrderBFGService");
	}
	
	public IPayOrderBFGService getPayOrderBFGService(){
		return ProxyFactory.create(IPayOrderBFGService.class, "tcp://servicecore/PayOrderBFGService");
	}
	
	//返回客户表的service
	public ILoginService getLoginService(){
		String url = "tcp://" + GAEA_UC + "/LoginService";
		ILoginService ics = ProxyFactory.create(ILoginService.class, url);
		return ics;
	}
	
	/**
	 * wf - task query
	 * @return
	 */
	public ILvTaskQueryService getWfTaskQueryService(){
		return ProxyFactory.create(ILvTaskQueryService.class, "tcp://" + GAEA_WF + "/LvTaskQueryService");
	}
	
	/**
	 * wf - task
	 * @return
	 */
	public ILvTaskService getWfTaskService(){
		return ProxyFactory.create(ILvTaskService.class, "tcp://" + GAEA_WF + "/LvTaskService");
	}
	/**
	 * wf - process
	 * @return
	 */
	public ILvProcessService getWfProcessService(){
		return ProxyFactory.create(ILvProcessService.class, "tcp://" + GAEA_WF + "/LvProcessService");
	}
	/**
	 * wf - history
	 * @return
	 */
	public ILvHistoryService getWfHistoryService(){
		return ProxyFactory.create(ILvHistoryService.class, "tcp://" + GAEA_WF + "/LvHistoryService");
	}
	/**
	 * wf - receive task
	 * @return
	 */
	public ILvReceiveTaskService getWfReceiveTaskService(){
		return ProxyFactory.create(ILvReceiveTaskService.class, "tcp://" + GAEA_WF + "/LvReceiveTaskService");
	}
	
	/**
	 * wf - dic data
	 * @return
	 */
	public ILvDicService getDicService(){
		return ProxyFactory.create(ILvDicService.class, "tcp://" + GAEA_DIC + "/LvDicService");
	}
	
	
	/**
	 * ep - enterprise
	 * @return
	 */
	public ILvEnterpriseService getEpEnterpriseService(){
		return ProxyFactory.create(ILvEnterpriseService.class, "tcp://" + GAEA_EP + "/LvEnterpriseService");
	}
	
	/**
	 * ep - enterprise business data
	 * @return
	 */
	public ILvEnterpriseBusinessDataService getEpEnterpriseBusinessDataService(){
		return ProxyFactory.create(ILvEnterpriseBusinessDataService.class, "tcp://" + GAEA_EP + "/LvEnterpriseBusinessDataService");
	}
	
	/**
	 * ep - enterprise business
	 * @return
	 */
	public ILvEnterpriseBusinessService getEpEnterpriseBusinessService(){
		return ProxyFactory.create(ILvEnterpriseBusinessService.class, "tcp://" + GAEA_EP + "/LvEnterpriseBusinessService");
	}
	
	/**
	 * ep - enterprise operating range
	 * @return
	 */
	public ILvEnterpriseOperatingRangeService getEpEnterpriseOperatingRangeService(){
		return ProxyFactory.create(ILvEnterpriseOperatingRangeService.class, "tcp://" + GAEA_EP + "/LvEnterpriseOperatingRangeService");
	}
	
	/**
	 * ep - enterprise operating range
	 * @return
	 */
	public ILvEnterpriseMainBusinessService getEpEnterpriseMainBusinessService(){
		return ProxyFactory.create(ILvEnterpriseMainBusinessService.class, "tcp://" + GAEA_EP + "/LvEnterpriseMainBusinessService");
	}
	/**
	 * ep - dic data
	 * @return
	 */
	public ILvEnterpriseDicDataService getEpEnterpriseDicDataService(){
		return ProxyFactory.create(ILvEnterpriseDicDataService.class, "tcp://"+GAEA_EP+"/LvEnterpriseDicDataService");
	}
	/**
	 * ep - enterprise person
	 * @return
	 */
	public ILvEnterprisePersonService getEpEnterprisePersonService(){
		return ProxyFactory.create(ILvEnterprisePersonService.class , "tcp://"+GAEA_EP+"/LvEnterprisePersonService");
	}
	/**
	 * ep - enterprise person
	 * @return
	 */
	public ILvEnterpriseRoleRelationService getEpEnterpriseRoleRelationService(){
		return ProxyFactory.create(ILvEnterpriseRoleRelationService.class , "tcp://"+GAEA_EP+"/LvEnterpriseRoleRelationService");
	}
	/**
	 * ep - enterprise pay
	 * @return
	 */
	public ILvEnterprisePartnerPayService getEpEnterprisePartnerPayService(){
		return ProxyFactory.create(ILvEnterprisePartnerPayService.class , "tcp://"+GAEA_EP+"/LvEnterprisePartnerPayService");
	}
	/**
	 * ep - enterprise person
	 * @return
	 */
	public ILvEnterpriseAddressService getEpEnterpriseAddressService(){
		return ProxyFactory.create(ILvEnterpriseAddressService.class , "tcp://"+GAEA_EP+"/LvEnterpriseAddressService");
	}
	/**
	 * ep - enterprise gov
	 * @return
	 */
	public ILvGovService getEpGovService(){
		return ProxyFactory.create(ILvGovService.class , "tcp://"+GAEA_EP+"/LvGovService");
	}
	
	
	/**
	 * un - userservice
	 * @return
	 */
	public ILoginService getUserService() {
		return ProxyFactory.create(ILoginService.class, "tcp://" + GAEA_UN + "/LoginService");
	}
	public IUserCounponsService getUserCounonsService(){
		return ProxyFactory.create(IUserCounponsService.class, "tcp://" + GAEA_UC + "/UserCounponsService");
	}
	public IAreasService getAreaService(){
		return ProxyFactory.create(IAreasService.class, "tcp://" + GAEA_UC + "/AreasService");
	}
	
	//流程 -接口
	public ILvCompanyService getWFCompanyService(){
		return ProxyFactory.create(ILvCompanyService.class, "tcp://" + GAEA_WF + "/LvCompanyService");
	}
	
	//增加站内信接口
	public IMailBFGService getMailBFGService(){
		return ProxyFactory.create(IMailBFGService.class, "tcp://"+ GAEA_MSG +"/MailBFGService");
	}
		
	//手机短信服务
	public IMoblieSmsService getMoblieSmsService(){
		return ProxyFactory.create(IMoblieSmsService.class, "tcp://"+GAEA_MSG+"/MoblieSmsService");
	}
	/*雇员服务**/
	public IEmployersService getEmployerService(){
		return ProxyFactory.create(IEmployersService.class, "tcp://" + GAEA_UC + "/EmployersService");
	} 
	/**呼叫系统服务*/
	public ICallService getCallService(){
		return ProxyFactory.create(ICallService.class, "tcp://"+GAEA_MSG+"/CallService");
	}
	
	//优惠券接口（需要改动的
	public ICouponsService getCouponsService(){
		return ProxyFactory.create(ICouponsService.class, "tcp://jxcore/CouponsService");
	}
	
	//优惠券接口(需要改动的
	public IUserCouponsService getUserCouponsServiceOld(){
		return ProxyFactory.create(IUserCouponsService.class, "tcp://jxcore/UserCouponsService");
	}
	
	//优惠券发送短信 临时
	public ILvNoticeService getNoticeService(){
		return ProxyFactory.create(ILvNoticeService.class, "tcp://"+GAEA_WF+"/LvNoticeService");
	}
	
	/**
	 * 服务商服务
	 * @return 
	 */
	public IVendorServeService getVendorServeService(){
		return ProxyFactory.create(IVendorServeService.class, "tcp://" + GAEA_VENDOR + "/VendorServeService");
	}
	
	
	// 自营订单 - 子单主表
	public ISorderService getSorderService(){
		return ProxyFactory.create(ISorderService.class, "tcp://jxcore/SorderService");
	}
	
	/***
	 * 服务商订单服务
	 * @return
	 */
	public IOrderVendorService getOrderVendorService(){
		IOrderVendorService orderVendorService = ProxyFactory.create(IOrderVendorService.class, "tcp://"+GAEA_SERVICE+"/OrderVendorService");
		return orderVendorService;
	}
	
	/**
	 * 商品实体
	 * @return
	 */
	public ILvzProductService getLvzProductService(){
		ILvzProductService lvzProductService=ProxyFactory.create(ILvzProductService.class, "tcp://"+GAEA_SELL+"/LvzProductService");
		return lvzProductService;
	}
	/**
	 * 定价条目实体
	 * @return
	 */
	public ILvzSellProductService getSellProductService(){
		ILvzSellProductService sellProductService=ProxyFactory.create(ILvzSellProductService.class, "tcp://"+GAEA_SELL+"/LvzSellProductService");
		return sellProductService;
	}
	
	/*** 商品包服务 start */
	public ILvzPackageService getPackageService(){
		return ProxyFactory.create(ILvzPackageService.class, "tcp://"+GAEA_SELL+"/LvzPackageService");
	}
	
	public ILvzPackageSellService getPackageSellService(){
		return ProxyFactory.create(ILvzPackageSellService.class, "tcp://"+GAEA_SELL+"/LvzPackageSellService");
	}
	/*** 商品包服务 end */
}
