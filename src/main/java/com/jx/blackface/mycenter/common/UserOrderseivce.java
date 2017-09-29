package com.jx.blackface.mycenter.common;

import java.util.List;

import com.jx.blackface.mycenter.frame.RSBLL;
import com.jx.blackface.orderplug.vo.PayOrderBFVo;
import com.jx.blackface.servicecoreclient.contract.IOrderBFGService;

public class UserOrderseivce {

	public static UserOrderseivce uod = new UserOrderseivce();
	public static IOrderBFGService ios = RSBLL.getstance().getOrderBFGService();
	public int getOrderCount(String condition)throws Exception{
		return ios.getOrderCountBycondition(condition);
	}
	public List<PayOrderBFVo> getOrdervolistBycondition(String condition,int pageindex,int pagesize,String sortby)throws Exception{
		 List<PayOrderBFVo> volist = null;
		 
		 return volist;
	}
}
