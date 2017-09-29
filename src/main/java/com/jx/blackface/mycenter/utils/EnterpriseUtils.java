/**
 * 
 */

package com.jx.blackface.mycenter.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jx.blackface.mycenter.frame.RSBLL;
import com.jx.service.enterprise.contract.ILvEnterpriseAddressService;
import com.jx.service.enterprise.contract.ILvEnterpriseRoleRelationService;
import com.jx.service.enterprise.contract.ILvEnterpriseService;
import com.jx.service.enterprise.entity.LvEnterpriseAddressEntity;
import com.jx.service.enterprise.entity.LvEnterprisePersonEntity;
import com.jx.service.enterprise.entity.LvEnterpriseRoleDataEntity;
import com.jx.service.enterprise.entity.LvEnterpriseRoleRelationEntity;
import com.jx.service.enterprise.entity.LvGovAppointInfoEntity;
import com.jx.service.enterprise.entity.LvGovOrgInfoEntity;



/**
 * simple introduction
 *
 * <p>detailed comment</p>
 * @author chuxuebao 2015年12月28日
 * @see
 * @since 1.0
 */

public class EnterpriseUtils {
	
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getRoleListByEnterpriseAndRoleType(String enterpriseId, String roleType){
		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		List<LvEnterpriseRoleRelationEntity> roleRelationList = new ArrayList<LvEnterpriseRoleRelationEntity>();
		try {
			roleRelationList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleRelationListByEnterpriseIdAndRoleType(Long.parseLong(enterpriseId), roleType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(roleRelationList == null || roleRelationList.isEmpty()){
			return reList;
		}
		for(LvEnterpriseRoleRelationEntity enterpriseRoleRelationEntity:roleRelationList){
			Map<String, String> roleDataMap = new HashMap<String, String>();
			if(StringUtils.equalsIgnoreCase(enterpriseRoleRelationEntity.getRoleType(), ILvEnterpriseRoleRelationService.ROLETYPE_LEGALPARTNER)){
				// 获取企业信息
				try {
					roleDataMap = RSBLL.getstance().getEpEnterpriseService().getAllValueByEnterpriseId(enterpriseRoleRelationEntity.getRoleId() + "");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				// 获取人员信息
				LvEnterprisePersonEntity enterprisePersonEntity = null;
				try {
					enterprisePersonEntity = RSBLL.getstance().getEpEnterprisePersonService().getEnterprisePersonById(enterpriseRoleRelationEntity.getRoleId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(enterprisePersonEntity != null){
					roleDataMap = JSONObject.parseObject(JSONObject.toJSONString(enterprisePersonEntity), Map.class);
				}
			}
			List<LvEnterpriseRoleDataEntity> roleDataList = null;
			try {
				roleDataList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleDataListByRoleRelationId(enterpriseRoleRelationEntity.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(roleDataList != null && !roleDataList.isEmpty()){
				for(LvEnterpriseRoleDataEntity enterpriseRoleDataEntity:roleDataList){
					roleDataMap.put(enterpriseRoleDataEntity.getDataKey(), enterpriseRoleDataEntity.getDataValue());
				}
			}
			roleDataMap.put("isComplete", EnterpriseUtils.naturalRule(LvMapUtils.combine(roleDataMap))==null?"true":"false");
			roleDataMap.put("roleRelationId", enterpriseRoleRelationEntity.getId() + "");
			roleDataMap.put("roleType", enterpriseRoleRelationEntity.getRoleType());
			roleDataMap.put("isLegalPerson", EnterpriseUtils.isLegalPerson(enterpriseId, enterpriseRoleRelationEntity.getRoleId() + "", enterpriseRoleRelationEntity.getRoleType()) + "");
			
			reList.add(roleDataMap);
		}
		return reList;
	}
	
	
	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, Object>> getRoleNameStrByByEnterprise(String enterpriseId){
		Map<String, Map<String, Object>> reMap = new HashMap<String, Map<String, Object>>();
		List<LvEnterpriseRoleRelationEntity> roleRelationList = new ArrayList<LvEnterpriseRoleRelationEntity>();
		try {
			roleRelationList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleRelationListByEnterpriseId(Long.parseLong(enterpriseId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(roleRelationList == null || roleRelationList.isEmpty()){
			return reMap;
		}
		
		for(LvEnterpriseRoleRelationEntity enterpriseRoleRelationEntity:roleRelationList){
			String roleType = enterpriseRoleRelationEntity.getRoleType();
			if(!StringUtils.equalsIgnoreCase(roleType, ILvEnterpriseRoleRelationService.ROLETYPE_LEGALPARTNER)){
				Map<String, Object> roleTypeData = reMap.get(roleType);
				if(roleTypeData == null || roleTypeData.isEmpty()){
					roleTypeData = new HashMap<String, Object>();
					Set<String> roleNameSet = new HashSet<String>();
					Set<Long> roleIdSet = new HashSet<Long>();
					roleTypeData.put("roleNameSet", roleNameSet);
					roleTypeData.put("roleIdSet", roleIdSet);
					reMap.put(roleType, roleTypeData);
				}
				// 获取人员信息
				LvEnterprisePersonEntity enterprisePersonEntity = null;
				try {
					enterprisePersonEntity = RSBLL.getstance().getEpEnterprisePersonService().getEnterprisePersonById(enterpriseRoleRelationEntity.getRoleId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(enterprisePersonEntity != null){
					// 名称
					((Set<String>)roleTypeData.get("roleNameSet")).add(enterprisePersonEntity.getName());
					// ID
					((Set<Long>)roleTypeData.get("roleIdSet")).add(enterprisePersonEntity.getId());
					try {
						roleTypeData.putAll(BeanUtils.describe(enterprisePersonEntity));
					} catch (Exception e) {

						e.printStackTrace();
					} 
				}
			}
		}
		
		if(reMap != null && !reMap.isEmpty()){
			Set<String> keySet = reMap.keySet();
			for(String roleType:keySet){
				Map<String, Object> map = reMap.get(roleType);
				if(map == null || map.isEmpty()){
					continue;
				}
				Object roleNameSet = map.get("roleNameSet");
				if(roleNameSet != null){
					map.put("roleNameList", StringUtils.join((Set<String>)roleNameSet, "；"));
				}
				Object roleIdSet = map.get("roleIdSet");
				if(roleIdSet != null){
					map.put("roleIdList", StringUtils.join((Set<String>)roleIdSet, ";"));
				}
			}
			
			
		}
		return reMap;
	}
	/**
	 * 针对一种角色只有一个人
	 * @param enterpriseId
	 * @param roleType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getRoleDataByEnterpriseIdAndRoleType(String enterpriseId, String roleType){
		Map<String, String> reMap = new HashMap<String, String>();
		if(StringUtils.isBlank(roleType) || StringUtils.isBlank(enterpriseId)){
			return reMap;
		}
		List<LvEnterpriseRoleRelationEntity> roleRelationList = null;
		try {
			roleRelationList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleRelationListByEnterpriseIdAndRoleType(Long.parseLong(enterpriseId), roleType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(roleRelationList == null || roleRelationList.isEmpty()){
			return null;
		}
		
		LvEnterpriseRoleRelationEntity enterpriseRoleRelationEntity = roleRelationList.get(0);
		// 关联信息
		reMap.putAll(JSONObject.parseObject(JSONObject.toJSONString(enterpriseRoleRelationEntity), Map.class));
		reMap.put("roleRelationId", enterpriseRoleRelationEntity.getId() + "");
		reMap.put("roleType", enterpriseRoleRelationEntity.getRoleType());
		
		if(StringUtils.equalsIgnoreCase(enterpriseRoleRelationEntity.getRoleType(), ILvEnterpriseRoleRelationService.ROLETYPE_LEGALPARTNER)){
			// 获取企业信息
			try {
				reMap.putAll(RSBLL.getstance().getEpEnterpriseService().getAllValueByEnterpriseId(enterpriseRoleRelationEntity.getRoleId() + ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			// 获取人员信息
			LvEnterprisePersonEntity enterprisePersonEntity = null;
			try {
				enterprisePersonEntity = RSBLL.getstance().getEpEnterprisePersonService().getEnterprisePersonById(enterpriseRoleRelationEntity.getRoleId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(enterprisePersonEntity != null){
				reMap.putAll(JSONObject.parseObject(JSONObject.toJSONString(enterprisePersonEntity), Map.class));;
			}
		}
		// 角色扩展信息
		List<LvEnterpriseRoleDataEntity> roleDataList = null;
		try {
			roleDataList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleDataListByRoleRelationId(enterpriseRoleRelationEntity.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(roleDataList != null && !roleDataList.isEmpty()){
			for(LvEnterpriseRoleDataEntity enterpriseRoleDataEntity:roleDataList){
				reMap.put(enterpriseRoleDataEntity.getDataKey(), enterpriseRoleDataEntity.getDataValue());
			}
		}
		return reMap;
	}
	
	/**
	 * 针对一种角色有多个人
	 * @param enterpriseId
	 * @param roleTypes 多个角色，使用分号分开
	 * @return
	 */
	public static List<Map<String, String>> getRoleListByEnterpriseAndRoleTypes(String enterpriseId, String roleTypes){
		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		if(StringUtils.isBlank(roleTypes) || StringUtils.isBlank(enterpriseId)){
			return reList;
		}
		String[] roleTypeArray = StringUtils.split(roleTypes, ";");
		if(roleTypeArray == null || roleTypeArray.length <= 0){
			return reList;
		}
		for(String roleType:roleTypeArray){
			List<Map<String, String>> roleList = EnterpriseUtils.getRoleListByEnterpriseAndRoleType(enterpriseId, roleType);
			if(roleList == null || roleList.isEmpty()){
				continue;
			}
			reList.addAll(roleList);
		}
		return reList;
	}
	
	
	
	/**
	 * 针对一种角色有多个人
	 * @param enterpriseId
	 * @param roleType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getRoleInfoListByEnterpriseIdAndRoleType(String enterpriseId, String roleType){
		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		if(StringUtils.isBlank(roleType) || StringUtils.isBlank(enterpriseId)){
			return reList;
		}
		// 角色主要信息
		List<LvEnterpriseRoleRelationEntity> roleRelationList = new ArrayList<LvEnterpriseRoleRelationEntity>();
		try {
			roleRelationList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleRelationListByEnterpriseIdAndRoleType(Long.parseLong(enterpriseId), roleType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(roleRelationList == null || roleRelationList.isEmpty()){
			return reList;
		}
		for(LvEnterpriseRoleRelationEntity enterpriseRoleRelationEntity:roleRelationList){
			Map<String, String> roleDataMap = new HashMap<String, String>();
			
			// 关联信息
			roleDataMap.putAll(JSONObject.parseObject(JSONObject.toJSONString(enterpriseRoleRelationEntity), Map.class));
			roleDataMap.put("roleRelationId", enterpriseRoleRelationEntity.getId() + "");
			roleDataMap.put("roleType", enterpriseRoleRelationEntity.getRoleType());
			
			if(StringUtils.equalsIgnoreCase(enterpriseRoleRelationEntity.getRoleType(), ILvEnterpriseRoleRelationService.ROLETYPE_LEGALPARTNER)){
				// 获取企业信息
				try {
					roleDataMap.putAll(RSBLL.getstance().getEpEnterpriseService().getAllValueByEnterpriseId(enterpriseRoleRelationEntity.getRoleId() + ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				// 获取人员信息
				LvEnterprisePersonEntity enterprisePersonEntity = null;
				try {
					enterprisePersonEntity = RSBLL.getstance().getEpEnterprisePersonService().getEnterprisePersonById(enterpriseRoleRelationEntity.getRoleId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(enterprisePersonEntity != null){
					roleDataMap.putAll(JSONObject.parseObject(JSONObject.toJSONString(enterprisePersonEntity), Map.class));
				}
			}
			// 角色扩展信息
			List<LvEnterpriseRoleDataEntity> roleDataList = null;
			try {
				roleDataList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleDataListByRoleRelationId(enterpriseRoleRelationEntity.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(roleDataList != null && !roleDataList.isEmpty()){
				for(LvEnterpriseRoleDataEntity enterpriseRoleDataEntity:roleDataList){
					roleDataMap.put(enterpriseRoleDataEntity.getDataKey(), enterpriseRoleDataEntity.getDataValue());
				}
			}
			reList.add(roleDataMap);
		}
		return reList;
	}
	
	/**
	 * 判断是否为企业法人
	 * @param enterpriseId 企业ID
	 * @param roleId 人员ID
	 * @return
	 */
	public static boolean isLegalPerson(String enterpriseId, String roleId, String roleType){
		if(StringUtils.isBlank(enterpriseId) || StringUtils.isBlank(roleId) || StringUtils.isBlank(roleType)){
			return false;
		}
		LvEnterpriseRoleRelationEntity enterpriseRoleRelationEntity = null;
		try {
			enterpriseRoleRelationEntity = RSBLL.getstance().getEpEnterpriseRoleRelationService().loadEnterpriseRoleRelationEntityByEnterpriseIdAndRoleIdAndRoleType(Long.parseLong(enterpriseId), Long.parseLong(roleId), ILvEnterpriseRoleRelationService.ROLETYPE_LEGALPERSON);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if(enterpriseRoleRelationEntity != null){
			String roleDataValue = null;
			try {
				roleDataValue = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleDataValueByRoleRelationIdAndDataKey(enterpriseRoleRelationEntity.getId(), "legalPersonMappingRoleType");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(StringUtils.equals(roleDataValue, roleType)){
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getMainMemberList(String enterpriseId){
		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		if(StringUtils.isBlank(enterpriseId)){
			return reList;
		}
		List<LvEnterprisePersonEntity> personList = null;
		try {
			personList = RSBLL.getstance().getEpEnterprisePersonService().getPersonListByEnterpriseId(enterpriseId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if(personList != null && !personList.isEmpty()){
			for(LvEnterprisePersonEntity personEntity:personList){
				List<LvEnterpriseRoleRelationEntity> roleRelationList = null;
				try {
					roleRelationList = RSBLL.getstance().getEpEnterpriseRoleRelationService().getRoleRelationListByEnterpriseIdAndRoleId(Long.parseLong(enterpriseId), personEntity.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				Set<String> roleTypeSet = new HashSet<String>();
				if(roleRelationList != null && !roleRelationList.isEmpty()){
					for(LvEnterpriseRoleRelationEntity roleRelationEntity:roleRelationList){
						String roleType = roleRelationEntity.getRoleType();
						if(!StringUtils.contains(ILvEnterpriseRoleRelationService.MAIN_MEMBER_ROLE_TYPES, roleType)){
							continue;
						}
						String roleTypeName = DicUtils.getEnterpriseDicValue("roleType", roleType);
						roleTypeSet.add(roleTypeName);
					}
				}
				if(roleTypeSet != null && !roleTypeSet.isEmpty()){
					Map<String, String> roleDataMap = new HashMap<String, String>();
					roleDataMap.putAll(JSONObject.parseObject(JSONObject.toJSONString(personEntity), Map.class));
					roleDataMap.put("roleTypeNames", StringUtils.join(roleTypeSet, "，"));
					reList.add(roleDataMap);
				}
			}
		}
		return reList;
	}
	
	
	
	/**
	 * 获取企业地址信息
	 * @param enterpriseId
	 * @return
	 */
	public static Map<String, Object> getAddressDataByEnterpriseId(String enterpriseId){
		Map<String, Object> reMap = new HashMap<String, Object>();
		List<LvEnterpriseAddressEntity> enterpriseAddressList = null;
		try {
			enterpriseAddressList = RSBLL.getstance().getEpEnterpriseAddressService().getEnterpriseAddressListByEnterpriseId(enterpriseId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(enterpriseAddressList == null || enterpriseAddressList.isEmpty()){
			return reMap;
		}
		String addressType = null;
		try {
			addressType = RSBLL.getstance().getEpEnterpriseService().getValueByEnterpriseIdAndKey(enterpriseId, ILvEnterpriseService.EXT_KEY_ADDRESSTYPE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtils.equals(addressType, ILvEnterpriseService.MAIN_KEY_ADDRESSTYPE_ONE)){
			// 孵化器地址
			String addressArea = null;
			try {
				addressArea = RSBLL.getstance().getEpEnterpriseAddressService().getEnterpriseAddressDataMapByEnterpriseIdAndDataKey(Long.parseLong(enterpriseId), ILvEnterpriseAddressService.COLUMN_ADDRESS_TEMPLATE_ID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(StringUtils.isNotBlank(addressArea)){
/*				LvEnterpriseAddressTemplateEntity enterpriseAddressTemplate = null;
				try {
					enterpriseAddressTemplate = RSBLL.getstance().getEpEnterpriseAddressTemplateService().getEnterpriseAddressTemplateById(Long.parseLong(addressArea));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(enterpriseAddressTemplate != null){
					try {
						reMap.putAll(BeanUtils.describe(enterpriseAddressTemplate));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}*/
			}
		}else{
			// 自有地址
			for(LvEnterpriseAddressEntity enterpriseAddressEntity:enterpriseAddressList){
				reMap.put(enterpriseAddressEntity.getDataKey(), enterpriseAddressEntity.getDataValue());
			}
		}
		
		return DicUtils.transferDicData(reMap);
	}
	
	/**
	 * 获取登录信息 TODO
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getLoginInfo(HttpServletRequest request) throws Exception{
		String ipAddr = "";
		String empId = "";
		Map<String, String> loginInfo = new HashMap<String, String>();
		loginInfo.put("empId", empId);
		loginInfo.put("IP", ipAddr);
		return loginInfo;
	}
	
	/**
	 * 获取工商局信息
	 * @param orgCode
	 * @return
	 */
	public static LvGovOrgInfoEntity getGovOrgConfig(String orgCode){
		LvGovOrgInfoEntity govOrgEntity = null;
		try {
			govOrgEntity = RSBLL.getstance().getEpGovService().loadGovOrgEntityByOrgCode(orgCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return govOrgEntity;
	}
	
	/**
	 * 工商预约配置信息
	 * @param orgCode
	 * @return
	 */
	public static List<LvGovAppointInfoEntity> getGovAppointList(String orgCode){
		List<LvGovAppointInfoEntity> govAppointEntityList = null;
		 try {
			 govAppointEntityList = RSBLL.getstance().getEpGovService().getGovAppointEntityListByOrgCode(orgCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return govAppointEntityList;
	}
	
	/**
	 * 自然人股东规则
	 */
	private static Map<String, String> naturalRule = new HashMap<String, String>();
	static{
		naturalRule.put("idNum", "证件号码");
		naturalRule.put("sex", "性别");
		naturalRule.put("folk", "民族");
		naturalRule.put("residenceProv", "户籍登记地址");
		naturalRule.put("residenceCity", "户籍登记地址");
		naturalRule.put("residenceAddress", "户籍登记地址");
		naturalRule.put("capitalSize", "出资额");
	}
	public static String naturalRule(Map<String, String> naturalData){
		Set<String> keySet = naturalRule.keySet();
		for(String key:keySet){
			if(naturalData == null || naturalData.isEmpty()){
				return naturalRule.get(key);
			}
			String data = naturalData.get(key);
			if(StringUtils.isBlank(data)){
				return naturalRule.get(key) + "不能为空";
			}
		}
		return null;
	}
	/**
	 * 法人股东规则
	 */
	private static Map<String, String> logicalRule = new HashMap<String, String>();
	static{
		logicalRule.put("enterpriseType", "单位类型");
		logicalRule.put("businessLicenseType", "证件类型");
		logicalRule.put("businessLicenseNum", "证件号码");
		logicalRule.put("legalPerson", "法人代表");
		logicalRule.put("residenceProv", "住所");
		logicalRule.put("residenceCity", "住所");
		logicalRule.put("residenceAddress", "住所");
		logicalRule.put("capitalSize", "出资额");
	}
	public static String logicalRule(Map<String, String> logicalData){
		Set<String> keySet = logicalRule.keySet();
		for(String key:keySet){
			if(logicalData == null || logicalData.isEmpty()){
				return logicalRule.get(key);
			}
			String data = logicalData.get(key);
			if(StringUtils.isBlank(data)){
				return logicalRule.get(key) + "不能为空";
			}
		}
		return null;
	}
}
