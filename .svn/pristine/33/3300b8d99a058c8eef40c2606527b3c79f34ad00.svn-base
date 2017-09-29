package com.jx.blackface.mycenter.controllers;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.jx.argo.ActionResult;
import com.jx.argo.Model;
import com.jx.argo.annotations.Path;
import com.jx.blackface.mycenter.common.CommonUtils;
import com.jx.blackface.mycenter.common.LocationPage;
import com.jx.blackface.mycenter.common.PagerTool;
import com.jx.service.preferential.plug.buz.PreferentialAccountBuz;
import com.jx.service.preferential.plug.vo.AccountVO;

public class PreferentialController extends BaseController{
	
	@Path("/unusepreferential.html")
	public ActionResult gotoMyIndex(){
		beat().getModel().add("cptype", "work");
		Model model = model();
		int pageindex = 1;
		int pagesize = 12;
		PagerTool pt = new PagerTool();
		int pagenumbers = 0;
		String pn = beat().getRequest().getParameter("pageno");
		pt.setUrl("/unusepreferential.html");
		if(pn != null && !"".equals(pn)){
			pageindex = Integer.parseInt(pn);
		}else{
			pageindex = 1;
		}
		pt.setPageindex(pageindex);
		long count = 0;//未使用的优惠券数量
		long uid = CommonUtils.getLoginuserid(beat());
		List<AccountVO> cvolist = null;
		if(uid > 0){
			try {
				count = PreferentialAccountBuz.getInstance().getUserCouponsCountByStatus(uid,0);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(count > 0){
				beat().getModel().add("count", count);
				pagenumbers = (int) (count%pagesize == 0?count/pagesize:count/pagesize+1);
				pt.setPagecount(pagenumbers);
				try {
					cvolist = PreferentialAccountBuz.getInstance().getUserCouponsByStatus(uid, 0,pageindex,pagesize,"quota desc");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(!CollectionUtils.isEmpty(cvolist)){
			pt.setContentlist(cvolist);
		}
		beat().getModel().add("pagetool", pt);
		beat().getModel().add("pageEntity", pt);
		
		beat().getModel().add("simpleDateFormat", new SimpleDateFormat("yyyy-MM-dd"));
		//转到的页面路径
		model.add("LocationPage", new LocationPage("preferential", "new-coupon", "coupon"));
		return menuview("index");
	}
	@Path("/usedpreferential.html")
	public ActionResult usedcouponlist(){
		beat().getModel().add("cptype", "used");
		Model model = model();
		int pageindex = 1;
		int pagesize = 10;
		PagerTool pt = new PagerTool();
		pt.setUrl("/usedpreferential.html");
		int pagenumbers = 0;
		String pn = beat().getRequest().getParameter("pageno");
		if(pn != null && !"".equals(pn)){
			pageindex = Integer.parseInt(pn);
			
		}else{
			pageindex = 1;
		}
		pt.setPageindex(pageindex);
		long count = 0;//已使用的优惠券数量
		long unUseCount = 0;
		long uid = CommonUtils.getLoginuserid(beat());
		List<AccountVO> cvolist = null;
		if(uid > 0){
			try {
				count = PreferentialAccountBuz.getInstance().getUserCouponsCountByStatus(uid,2);
				unUseCount = PreferentialAccountBuz.getInstance().getUserCouponsCountByStatus(uid,0);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(count > 0){
				pagenumbers = (int) (count%pagesize == 0?count/pagesize:count/pagesize+1);
				pt.setPagecount(pagenumbers);
				try {
					cvolist = PreferentialAccountBuz.getInstance().getUserCouponsByStatus(uid, 2,pageindex,pagesize,"quota desc");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		if (!CollectionUtils.isEmpty(cvolist)) {
			pt.setContentlist(cvolist);
		}
		model.add("count", unUseCount);
		model.add("pagetool", pt);
		model.add("pageEntity", pt);
		model.add("simpleDateFormat", new SimpleDateFormat("yyyy-MM-dd"));
		//转到的页面路径
		model.add("LocationPage", new LocationPage("preferential", "new-coupon","coupon"));
		return menuview("index");
	}
	@Path("/overpreferential.html")
	public ActionResult overduecouponlist(){
		beat().getModel().add("cptype", "over");
		Model model = model();
		int pageindex = 1;
		int pagesize = 10;
		PagerTool pt = new PagerTool();
		pt.setUrl("/overpreferential.html");
		int pagenumbers = 0;
		String pn = beat().getRequest().getParameter("pageno");
		if(pn != null && !"".equals(pn)){
			pageindex = Integer.parseInt(pn);
		}else{
			pageindex = 1;
		}
		pt.setPageindex(pageindex);
		long count = 0;//已使用的优惠券数量
		long unUseCount = 0;
		long uid = CommonUtils.getLoginuserid(beat());
		List<AccountVO> cvolist = null;
		if(uid > 0){
			try {
				count = PreferentialAccountBuz.getInstance().getUserCouponsCountByStatus(uid,-1);
				unUseCount = PreferentialAccountBuz.getInstance().getUserCouponsCountByStatus(uid,0);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(count > 0){
				pagenumbers = (int) (count%pagesize == 0?count/pagesize:count/pagesize+1);
				pt.setPagecount(pagenumbers);
				try {
					cvolist = PreferentialAccountBuz.getInstance().getUserCouponsByStatus(uid, -1,pageindex,pagesize,"quota desc");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		if (!CollectionUtils.isEmpty(cvolist)) {
			pt.setContentlist(cvolist);
		}
		model.add("count", unUseCount);
		model.add("pagetool", pt);
		model.add("pageEntity", pt);
		model.add("simpleDateFormat", new SimpleDateFormat("yyyy-MM-dd"));
		//转到的页面路径
		model.add("LocationPage", new LocationPage("preferential", "new-coupon","coupon"));
		return menuview("index");
	}
	
}
