package com.jx.blackface.mycenter.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.jx.argo.ActionResult;
import com.jx.argo.annotations.Path;
import com.jx.blackface.gaea.sell.entity.LvzPackageSellEntity;
import com.jx.blackface.gaea.sell.entity.LvzSellProductEntity;
import com.jx.blackface.gaea.usercenter.entity.BFAreasEntity;
import com.jx.blackface.mycenter.annotaion.CheckLogin;
import com.jx.blackface.mycenter.common.CommonUtils;
import com.jx.blackface.mycenter.common.LocationPage;
import com.jx.blackface.mycenter.common.PagerTool;
import com.jx.blackface.mycenter.frame.RSBLL;
import com.jx.blackface.mycenter.utils.DoubleTools;
import com.jx.blackface.mycenter.utils.Timers;
import com.jx.blackface.orderplug.buzs.OrderBuz;
import com.jx.blackface.servicecoreclient.entity.OrderBFGEntity;
import com.jx.blackface.servicecoreclient.entity.PayOrderBFGEntity;

/**
 * 此类的所有方法应该放到MyOrderController中
 */
@Path("/order")
@CheckLogin
public class UserPageController extends BaseController {

	@Path("/index.html")
	public ActionResult index(){
		long uid = getLoginuserid();
		
		return view("index");
	}
	/***
	 * 获取未支付订单
	 * @return
	 */
	@Path("/unpayorder.html")
	public ActionResult unpayorder(){
		long uid = getLoginuserid();
		beat().getModel().add("listtype", "unpaylist");
		PagerTool pt = new PagerTool();
		pt.setUrl("/order/unpayorder.html");
		PayOrderListByCondition("userid="+uid+" and paystate=0",pt,uid);
		return menuview("index");
	}
	/***
	 * 查询已支付订单
	 * @return
	 */
	@Path("/payedorder.html")
	public ActionResult payedorder(){
		long uid = getLoginuserid();
		PagerTool pt = new PagerTool();
		beat().getModel().add("listtype", "orderlist");
		pt.setUrl("/order/payedorder.html");
		PayOrderListByCondition("userid="+uid+" and paystate=1",pt,uid);
		return menuview("index");
	}
	
	/***
	 * 查询取消的订单
	 * @return
	 */
	@Path("/cancelorder.html")
	public ActionResult canceledorder(){
		long uid = getLoginuserid();
		beat().getModel().add("listtype", "cancellist");
		PagerTool pt = new PagerTool();
		pt.setUrl("/order/cancelorder.html");
		PayOrderListByCondition("userid="+uid+" and paystate = 9",pt,uid);
		return menuview("index");
	}
	
	/***
	 * 优惠券里用到的方法[具体页面为coupon.html和preferential文件夹下]
	 * @return
	 */
	@Path("/orderlist.html")
	public ActionResult orderlist(){
		long uid = CommonUtils.getLoginuserid(beat());
		String pn = beat().getRequest().getParameter("pageno");
		int pageindex = 1;
		if(null != pn && !"".equals(pn)){
			pageindex = Integer.parseInt(pn);
		}
		int ps = 10;
		int ordercount = 0;
		try {
			ordercount = OrderBuz.ob.getUserorderCount(uid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int pagecount = 0;
		if(ordercount > 0){
			pagecount = ordercount%ps > 0?ordercount/ps+1:ordercount/ps;
		}
		try {
			OrderBuz.ob.getUserOrderlist(uid, pageindex, ps, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PagerTool pt = new PagerTool();
		pt.setPagecount(pagecount);
		pt.setPageindex(Integer.parseInt(pn));
		beat().getModel().add("pagetool", pt);
		return menuview("index");
	}
	@Path("/maillist.html")
	public ActionResult maillist(){
		
		return menuview("mailist");
	}
	
	/***
	 * 查询所有订单
	 * @return
	 */
	@Path("/getAllOrderList.html")
	public ActionResult getAllOrderList(){
		long uid = getLoginuserid();
		beat().getModel().add("listtype", "alllist");
		PagerTool pt = new PagerTool();
		pt.setUrl("/order/getAllOrderList.html");
		PayOrderListByCondition("userid='"+uid+"'", pt, uid);
		return menuview("index");
	}
	
	
	/***
	 * 获取payorder集合公共方法 通过传入查询条件进行获取
	 * @param condition
	 * @param pt
	 * @param uid
	 */
	public void PayOrderListByCondition(String condition,PagerTool pt,Long uid){
		String pn = beat().getRequest().getParameter("pageno");
		int pageindex = 1;
		int ps = 10;
		int ordercount = 0;
		int pagecount = 0;
		List<PayOrderBFGEntity> PayOrderList = new ArrayList<PayOrderBFGEntity>();
		if(null != pn && !"".equals(pn)){
			pageindex = Integer.parseInt(pn);
		}
		pt.setPageindex(pageindex);
		if(uid > 0){
			try {
				ordercount = RSBLL.getstance().getPayOrderBFGService().getPOEcountBycondition(condition);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(ordercount > 0){
				pagecount = ordercount%ps > 0?ordercount/ps+1:ordercount/ps;
				if(pageindex > pagecount){
					pageindex = pagecount;
					pt.setPageindex(pagecount);
				}
				pt.setPagecount(pagecount);
			}
			try {
				PayOrderList = RSBLL.getstance().getPayOrderBFGService().getPoelistBypage(condition, pageindex, ps, "addtime desc");
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<Map<String,Object>> allPayOrderList = new ArrayList<Map<String,Object>>();
			if(!PayOrderList.isEmpty()){
				for(PayOrderBFGEntity t_payOrder : PayOrderList){
					Map<String, Object> payOrderMap = new HashMap<String, Object>();
					try {
						payOrderMap = BeanUtils.describe(t_payOrder);  //把支付订单实体转换为map对象
					} catch (Exception e) {
						e.printStackTrace();
					}
					payOrderMap.put("addtime",Timers.longToString(t_payOrder.getAddtime())); //转换时间单位
					
					List<OrderBFGEntity> orderListBycondition = null;
					try {
						orderListBycondition = RSBLL.getstance().getOrderBFGService().getOrderListBycondition("payid='"+t_payOrder.getPayid()+"'", 1, 30, "packagesellid desc");
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(null != orderListBycondition && !orderListBycondition.isEmpty()){
						Set<Long> temp_Set = new HashSet<Long>();
						List<Map<String,Object>> tempOrderList = new ArrayList<Map<String,Object>>();
						for(OrderBFGEntity orderE : orderListBycondition){
							Map<String,Object> tempOrderMap = new HashMap<String, Object>();
							try {
								LvzSellProductEntity sellProductEntityById = RSBLL.getstance().getSellProductService().getSellProductEntityById(orderE.getSellerid());
								if(null != sellProductEntityById){
									//判断此订单是否为商品包订单，如果是则需要把商品包中包含的商品名称拼接起来
									if(orderE.getPackagesellid() == 0){
										tempOrderMap.put("packageFlag", "false"); //是否为商品包标识
										tempOrderMap.put("sell_product_name", sellProductEntityById.getSell_product_name()); //定价条目名称
										tempOrderMap.put("sell_overprice", sellProductEntityById.getSell_overprice());   //单价取自定价条目成交价格
										tempOrderMap.put("sell_amount", sellProductEntityById.getSell_amount());   //定价条目数量
										tempOrderMap.put("sell_payprice", orderE.getPaycount());
										tempOrderMap.put("discount", DoubleTools.roundUp((sellProductEntityById.getSell_overprice()-orderE.getPaycount()), 2));

										Map<String,Object> map = new HashMap<String, Object>();
										BFAreasEntity aeasEntity = RSBLL.getstance().getAreaService().getAeasEntityById(sellProductEntityById.getArea_id());
										if(aeasEntity != null){
											map.put("aeasname", aeasEntity.getName());
											map.put("aeasid", aeasEntity.getAreaid());
											BFAreasEntity cityEntity = RSBLL.getstance().getAreaService().getAeasEntityById(Long.valueOf(aeasEntity.getParentid()));
											map.put("cityname", cityEntity.getName());
											map.put("cityid", cityEntity.getAreaid());
										}
										tempOrderMap.putAll(map);
									}else{
										if(temp_Set.contains(orderE.getPackagesellid())){
											continue;
										}
										tempOrderMap.put("packageFlag", "true"); //是否为商品包标识
										LvzPackageSellEntity lvzPackageSellEntity = RSBLL.getstance().getPackageSellService().getLvzPackageSellEntity(orderE.getPackagesellid());
										if(null != lvzPackageSellEntity){
											temp_Set.add(lvzPackageSellEntity.getPackagesell_id());
											tempOrderMap.put("sell_overprice", lvzPackageSellEntity.getSell_overprice());  //成交价格
											
											StringBuffer sb = new StringBuffer();
											String sellids = lvzPackageSellEntity.getSellids();
											if(StringUtils.isNotBlank(sellids)){
												String[] idArr = sellids.split(",");
												for(int i=0;i<idArr.length;i++){
													if(StringUtils.isBlank(idArr[i])){
														continue;
													}
													LvzSellProductEntity sellProductEntity = null;
													try {
														sellProductEntity = RSBLL.getstance().getSellProductService().getSellProductEntityById(Long.parseLong(idArr[i]));
														if(null != sellProductEntity){
															sb.append(sellProductEntity.getSell_product_name()).append("<br/>");
														}
													} catch (NumberFormatException e) {
														e.printStackTrace();
													} catch (Exception e) {
														e.printStackTrace();
													}
												}
											}
											tempOrderMap.put("sell_product_name", sb.toString()); //商品包所有的定价条目
											
											Map<String,Object> map = new HashMap<String, Object>();
											BFAreasEntity aeasEntity = RSBLL.getstance().getAreaService().getAeasEntityById(lvzPackageSellEntity.getAreaid());
											if(aeasEntity != null){
												map.put("aeasname", aeasEntity.getName());
												map.put("aeasid", aeasEntity.getAreaid());
												BFAreasEntity cityEntity = RSBLL.getstance().getAreaService().getAeasEntityById(Long.valueOf(aeasEntity.getParentid()));
												map.put("cityname", cityEntity.getName());
												map.put("cityid", cityEntity.getAreaid());
											}
											tempOrderMap.putAll(map);
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							tempOrderList.add(tempOrderMap);
						}
						payOrderMap.put(String.valueOf(t_payOrder.getPayid()), tempOrderList); //以支付主键为KEY存放包含的订单信息
					}
					allPayOrderList.add(payOrderMap);  //存放到集合中
				}
				if(!allPayOrderList.isEmpty()){
					beat().getModel().add("allPayOrderList", allPayOrderList);   
				}
			}
		}
		beat().getModel().add("pageEntity", pt);
		beat().getModel().add("LocationPage", new LocationPage("myorder", "order","myorder"));
	}
	
	
}
