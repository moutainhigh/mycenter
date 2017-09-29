package com.jx.blackface.mycenter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.jx.blackface.gaea.usercenter.entity.BFLoginEntity;
import com.jx.blackface.gaea.usercenter.entity.UserCounponsBFGEntity;
import com.jx.blackface.mycenter.dto.UserCounponsDto;
import com.jx.blackface.mycenter.frame.RSBLL;
import com.jx.blackface.mycenter.utils.Timers;
import com.jx.blackface.orderplug.common.CommonTools;
import com.jx.service.preferential.plug.buz.PreferentialAccountBuz;

/***
 * 用户中心服务类
 * @author duxiaofei
 * @date   2015年12月21日
 */
public class MyCenterService {
	
	private static MyCenterService myCenterService = null;
	public static MyCenterService getInstan(){
		if(myCenterService == null){
			myCenterService = new MyCenterService();
		}
		return myCenterService;
	}
	
	
	public BFLoginEntity getUserEntity(long userid) throws Exception{
		BFLoginEntity userEntity = RSBLL.getstance().getLoginService().getLoginEntityById(userid);
		if(userEntity == null){
			return new BFLoginEntity();
		}
		return userEntity;
	}
	/**
	 * 获取历史优惠券数量
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public int getUsedcoupounscount(long userid)throws Exception{
		int i = 0;
		String condition = "userid = "+userid;
		i = RSBLL.getstance().getUserCounonsService().getUcounponsCountbycondition(condition);
		return i;
	}
	/**
	 * 获取没使用优惠券数量
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public int getUnusedcoupounscount(long userid)throws Exception{
		int i = 0;
		
		i = (int) PreferentialAccountBuz.getInstance().getUserCouponsCountByStatus(userid,0);
		return i;
	}
	/**
	 * 获取用户未使用的优惠券
	 * @param userid
	 * @param pageindex
	 * @param pagesize
	 * @param sortby
	 * @return
	 * @throws Exception
	 */
	public List<UserCounponsDto> getUserUnusecounpons(long userid,int pageindex,int pagesize,String sortby)throws Exception{
		String condition = "userid="+userid+" and isuse=0 and dqtime > "+new Date().getTime();
		return getCounponsBycondition(condition,pageindex,pagesize,sortby);
	}
	/**
	 * 获取用户使用的优惠券纪录
	 * @param userid
	 * @param pageindex
	 * @param pagesize
	 * @param sortby
	 * @return
	 * @throws Exception
	 */
	public List<UserCounponsDto> getUseedCounpoons(long userid,int pageindex,int pagesize,String sortby)throws Exception{
		String condition = "userid = "+userid+" and isuse = 1";
		return getCounponsBycondition(condition,pageindex,pagesize,sortby);
	}
	private List<UserCounponsDto> getCounponsBycondition(String condition,int pageindex,int pagesize,String sortby)throws Exception{
		List<UserCounponsDto> uclist = new ArrayList<UserCounponsDto>();
		List<UserCounponsBFGEntity> ucelist = 
				RSBLL.getstance().getUserCounonsService().getUcounponsBypage(condition, pageindex, pagesize, sortby);
		if(ucelist != null && ucelist.size() > 0){
			for(UserCounponsBFGEntity uce : ucelist){
				UserCounponsDto ud = tranceUCounponsToVo(uce);
				uclist.add(ud);
			}
		}
		return uclist;
	}


	private UserCounponsDto tranceUCounponsToVo(UserCounponsBFGEntity uce) {
		// TODO Auto-generated method stub
		UserCounponsDto udt = new UserCounponsDto();
		if(uce != null){
			udt.setMoney(uce.getCmoney());
			udt.setCounponsname(uce.getCname());
			if(uce.getIsuse() == 1){
				udt.setStatestr("已使用");
				udt.setChangedatestr(CommonTools.tranceDateToString("yyyy-MM-dd", uce.getUsertime()));
			}else{
				long currentTime = new Date().getTime();
				long dqTime = uce.getDqtime();
				if(currentTime > dqTime){
					udt.setStatestr("已过期");
				}
				udt.setChangedatestr(CommonTools.tranceDateToString("yyyy-MM-dd",uce.getDqtime()));

				String date1 = Timers.formatLongDate("yyyy-MM-dd", new Date().getTime());
				String date2 = Timers.formatLongDate("yyyy-MM-dd", uce.getDqtime());
				//获得两个时间差(单位:天)
				Long timeCha = Timers.getBetweenDay(date2,date1);
				if(timeCha > 0){
					udt.setDatestr("仅剩"+String.valueOf(timeCha)+"日过期");
				}
			}
			udt.setUcid(uce.getTucid());
			udt.setUserid(uce.getUserid());
			udt.setUserstate(uce.getIsuse());
		}
		return udt;
	}

}
