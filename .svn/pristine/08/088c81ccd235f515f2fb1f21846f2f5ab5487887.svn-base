package com.jx.blackface.mycenter.controllers;

import java.util.List;

import com.jx.argo.ActionResult;
import com.jx.argo.Model;
import com.jx.argo.annotations.Path;
import com.jx.blackface.mycenter.annotaion.CheckLogin;
import com.jx.blackface.mycenter.common.CommonUtils;
import com.jx.blackface.mycenter.common.LocationPage;
import com.jx.blackface.mycenter.common.PagerTool;
import com.jx.blackface.mycenter.dto.UserCounponsDto;
import com.jx.blackface.mycenter.service.MyCenterService;

/***
 * 优惠券controller
 * @author duxiaofei
 * @date   2015年12月19日
 */
@CheckLogin
public class CouponController extends BaseController{
	@Path("/unusecoupon.html")
	public ActionResult gotoMyIndex(){
		beat().getModel().add("cptype", "unuseconpouselist");
		Model model = model();
		int pageindex = 1;
		int pagesize = 12;
		PagerTool pt = new PagerTool();
		int pagenumbers = 0;
		String pn = beat().getRequest().getParameter("pageno");
		pt.setUrl("/unusecoupon.html");
		if(pn != null && !"".equals(pn)){
			pageindex = Integer.parseInt(pn);
		}else{
			pageindex = 1;
		}
		pt.setPageindex(pageindex);
		int count = 0;//未使用的优惠券数量
		long uid = CommonUtils.getLoginuserid(beat());
		List<UserCounponsDto> cvolist = null;
		if(uid > 0){
			try {
				count = MyCenterService.getInstan().getUnusedcoupounscount(uid);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(count > 0){
				beat().getModel().add("count", count);
				pagenumbers = count%pagesize == 0?count/pagesize:count/pagesize+1;
				pt.setPagecount(pagenumbers);
				try {
					cvolist = MyCenterService.getInstan().getUserUnusecounpons(uid, pageindex, pagesize, "gettime desc");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		if(cvolist != null && cvolist.size() > 0){
			pt.setContentlist(cvolist);
		}
		beat().getModel().add("pagetool", pt);
		beat().getModel().add("pageEntity", pt);
		//转到的页面路径
		model.add("LocationPage", new LocationPage("coupon", "coupon","coupon"));
		return menuview("index");
	}
	@Path("/usedcouponlist.html")
	public ActionResult usedcouponlist(){
		beat().getModel().add("cptype", "usedconpouselist");
		Model model = model();
		int pageindex = 1;
		int pagesize = 10;
		PagerTool pt = new PagerTool();
		pt.setUrl("/usedcouponlist.html");
		int pagenumbers = 0;
		String pn = beat().getRequest().getParameter("pageno");
		if(pn != null && !"".equals(pn)){
			pageindex = Integer.parseInt(pn);
			
		}else{
			pageindex = 1;
		}
		pt.setPageindex(pageindex);
		int count = 0;//未使用的优惠券数量
		long uid = CommonUtils.getLoginuserid(beat());
		List<UserCounponsDto> cvolist = null;
		if(uid > 0){
			try {
				count = MyCenterService.getInstan().getUsedcoupounscount(uid);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(count > 0){
				pagenumbers = count%pagesize == 0?count/pagesize:count/pagesize+1;
				pt.setPagecount(pagenumbers);
				try {
					cvolist = MyCenterService.getInstan().getUseedCounpoons(uid, pageindex, pagesize, "gettime desc");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		if(cvolist != null && cvolist.size() > 0){
			pt.setContentlist(cvolist);
		}
		model.add("pcontent", pt);
		model.add("pageEntity", pt);
		//转到的页面路径
		model.add("LocationPage", new LocationPage("coupon", "coupon","coupon"));
		return menuview("index");
	}
}
