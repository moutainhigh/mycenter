/**
 * 
 */

package com.jx.blackface.mycenter.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.jx.blackface.mycenter.frame.RSBLL;
import com.jx.blackface.tools.webblack.Env;
import com.jx.blackface.tools.webblack.auth.AuthHelper;
import com.jx.blackface.tools.webblack.page.entity.QueryPageEntity;
import com.jx.blackface.tools.webblack.query.data.DataQuery;
import com.jx.blackface.tools.webblack.query.industry.IndustryHelper;
import com.jx.blackface.tools.webblack.query.industry.entity.IndustryCategoryQueryEntity;
import com.jx.blackface.tools.webblack.query.industry.entity.SubIndustryQueryEntity;
import com.jx.service.enterprise.contract.ILvEnterpriseDicDataService;
import com.jx.service.enterprise.contract.ILvEnterpriseMainBusinessService;
import com.jx.service.enterprise.entity.LvEnterpriseDicDataEntity;
import com.jx.service.enterprise.entity.LvEnterpriseMainBusinessEntity;



/**
 * simple introduction
 *
 * <p>detailed comment</p>
 * @author chuxuebao 2016年1月7日
 * @see
 * @since 1.0
 */

public class SynGovDataUtils {

	public static void main(String[] args) throws Exception {
//		synScope();
//		synProvinces("residenceProv", "100008");
		
//		String sessionId = AuthHelper.postLogin();
		
		// 民族
/*		synData("folk", "100009", DataQuery.queryData(sessionId, DataQuery.DICID_CB32));
		
		// 出资方式
		synData("capitalMethod", "100010", DataQuery.queryData(sessionId, DataQuery.DICID_CA22));*/
		// 单位股东 - 证照类型
//		synData("businessLicenseType", "100011", DataQuery.queryData(sessionId, DataQuery.DICID_CA50));
		
		// 住所提供方式
//		synData("getWay", "100012", DataQuery.queryData(sessionId, DataQuery.DICID_CA44));
		
/*		Map<String, String> domOwnType = DataQuery.getDomOwnType(sessionId);
		System.out.println(JSON.toJSONString(domOwnType));*/
		
		Env.initVerifyPath("D:/opt/blackface/webblack/javaOcrTrain");
		synOrgInfo();
	}
	
	/**
	 * 同步省份，市
	 * @throws Exception
	 */
	public static void synProvinces(String dicTypeKey, String dicTypeId) throws Exception{
		String sessionId = AuthHelper.postLogin();
		Map<String, String> map = DataQuery.queryDomProvinces(sessionId);
		synData(dicTypeKey, dicTypeId, map);
		Set<String> keySet = map.keySet();
		for(String key:keySet){
			ILvEnterpriseDicDataService epEnterpriseDicDataService = RSBLL.getstance().getEpEnterpriseDicDataService();
			LvEnterpriseDicDataEntity dicDataEntity = epEnterpriseDicDataService.getDicDataEntityByTypeKeyAndDataKey(dicTypeKey, key);
			Map<String, String> queryDomCities = DataQuery.queryDomCities(sessionId, key);
			synData(key, dicDataEntity.getDataId() + "", queryDomCities);
		}
	}
	
	/**
	 * @param dicTypeKey
	 * @param dicTypeId
	 * @param map
	 * @throws Exception
	 */
	private static void synData(String dicTypeKey, String dicTypeId, Map<String, String> map) throws Exception {
		if(map == null || map.isEmpty()){
			return;
		}
		ILvEnterpriseDicDataService epEnterpriseDicDataService = RSBLL.getstance().getEpEnterpriseDicDataService();
		Set<String> keySet = map.keySet();
		for(String key:keySet){
			LvEnterpriseDicDataEntity dicDataEntity = epEnterpriseDicDataService.getDicDataEntityByTypeKeyAndDataKey(dicTypeKey, key);
			if(dicDataEntity == null){
				LvEnterpriseDicDataEntity newDicDataEntity = new LvEnterpriseDicDataEntity();
				long dicDataCount = epEnterpriseDicDataService.getDicDataCountByTypeKey(dicTypeKey) + 1;
				newDicDataEntity.setDicTypeKey(dicTypeKey);
				newDicDataEntity.setDataId(Long.parseLong(dicTypeId + pullNum(3, dicDataCount)));
				newDicDataEntity.setDataKey(key);
				newDicDataEntity.setDataValue(map.get(key));
				newDicDataEntity.setDataOrder(dicDataCount);
				epEnterpriseDicDataService.insertDicData(newDicDataEntity);
			}else{
				dicDataEntity.setDataValue(map.get(key));
				epEnterpriseDicDataService.updateDicData(dicDataEntity);
			}
		}
	}
	
	private static String pullNum(int length, long num){
		String f = "%0" + length + "d";
        return String.format(f, num);
	}
	
	/**
	 * 同步经营范围
	 * @throws Exception
	 */
	public static void synScope() throws Exception{
		String sessionId = AuthHelper.postLogin();
		QueryPageEntity<IndustryCategoryQueryEntity> industries = IndustryHelper.getIndustries(sessionId);
		List<IndustryCategoryQueryEntity> industryCategoryList = industries.getRows();
		int rows = 6;
		for(IndustryCategoryQueryEntity industryCategoryQuery:industryCategoryList){
			int page = 0;
			int totalrows = 0;
			System.out.println(industryCategoryQuery.getWb());
			do{
				QueryPageEntity<SubIndustryQueryEntity> subIndustries = IndustryHelper.getSubIndustries(sessionId, industryCategoryQuery.getDm(), rows, page, totalrows);
				if(subIndustries != null && subIndustries.getRows() != null){
					List<SubIndustryQueryEntity> industryEntityList = subIndustries.getRows();
					for(SubIndustryQueryEntity industry:industryEntityList){
						ILvEnterpriseMainBusinessService epEnterpriseMainBusinessService = RSBLL.getstance().getEpEnterpriseMainBusinessService();
						LvEnterpriseMainBusinessEntity businessEntity = epEnterpriseMainBusinessService.loadByCode(industry.getCode());
						if(businessEntity == null){
							System.out.println(industry.getCode());
						}else{
							businessEntity.setScope(industry.getScope());
							epEnterpriseMainBusinessService.updateMainBusiness(businessEntity);
						}
					}
				}
				page++;
				totalrows = subIndustries.getTotalrows();
				Thread.sleep(200L);
			}while(!(totalrows/rows < page));
		}
	}
	
	public static void synOrgInfo() throws Exception{
		String sessionId = AuthHelper.postLogin();
		
		Map parseObject = JSON.parseObject(JSON.toJSONString(DataQuery.queryOrgConfig(sessionId, "110105000")), Map.class);
		parseObject.put("orgAddress", parseObject.get("orgAdd"));
		RSBLL.getstance().getEpGovService().saveGovOrgInfo(parseObject);
		
		
		Map parseObject2 = JSON.parseObject(JSON.toJSONString(DataQuery.queryOrgConfig(sessionId, "110108000")), Map.class);
		parseObject2.put("orgAddress", parseObject2.get("orgAdd"));
		RSBLL.getstance().getEpGovService().saveGovOrgInfo(parseObject2);
		
		Map parseObject3 = JSON.parseObject(JSON.toJSONString(DataQuery.queryOrgConfig(sessionId, "110101000")), Map.class);
		parseObject3.put("orgAddress", parseObject3.get("orgAdd"));
		RSBLL.getstance().getEpGovService().saveGovOrgInfo(parseObject3);
		
		Map parseObject4 = JSON.parseObject(JSON.toJSONString(DataQuery.queryOrgConfig(sessionId, "110102000")), Map.class);
		parseObject4.put("orgAddress", parseObject4.get("orgAdd"));
		RSBLL.getstance().getEpGovService().saveGovOrgInfo(parseObject4);
		
		
		Map parseObject5 = JSON.parseObject(JSON.toJSONString(DataQuery.queryOrgConfig(sessionId, "110106000")), Map.class);
		parseObject5.put("orgAddress", parseObject5.get("orgAdd"));
		RSBLL.getstance().getEpGovService().saveGovOrgInfo(parseObject5);
		
		
		Map parseObject6 = JSON.parseObject(JSON.toJSONString(DataQuery.queryOrgConfig(sessionId, "110107000")), Map.class);
		parseObject6.put("orgAddress", parseObject6.get("orgAdd"));
		RSBLL.getstance().getEpGovService().saveGovOrgInfo(parseObject6);
		
/*		System.out.println(JSON.toJSONString(DataQuery.queryOrgConfig(sessionId, "110105000")));
		System.out.println(JSON.toJSONString(DataQuery.queryOrgConfig(sessionId, "110108000")));
		System.out.println(JSON.toJSONString(DataQuery.queryOrgConfig(sessionId, "110101000")));
		System.out.println(JSON.toJSONString(DataQuery.queryOrgConfig(sessionId, "110102000")));
		System.out.println(JSON.toJSONString(DataQuery.queryOrgConfig(sessionId, "110106000")));
		System.out.println(JSON.toJSONString(DataQuery.queryOrgConfig(sessionId, "110107000")));*/
		
	}
	
}
