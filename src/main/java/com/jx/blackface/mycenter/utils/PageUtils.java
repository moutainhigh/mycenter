/**
 * 
 */

package com.jx.blackface.mycenter.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang.StringUtils;

import com.jx.argo.Model;
import com.jx.blackface.mycenter.vo.PageVO;



/**
 * simple introduction
 *
 * <p>detailed comment</p>
 * @author chuxuebao 2016年1月14日
 * @see
 * @since 1.0
 */

public class PageUtils {

	public static void buildPageModel(Model model, long pageIndex, long count, long pageSize, String currenturl){
		PageUtils.buildPageModel(model, pageIndex, count, pageSize, currenturl, "", "");
	}
	
	public static void buildPageModel(Model model, long pageIndex, long count, long pageSize, String currenturl, String paramValue){
		PageUtils.buildPageModel(model, pageIndex, count, pageSize, currenturl, paramValue, "");
	}
	
	/**
	 * 
	 * @param model
	 * @param pageIndex 当前页 从1开始
	 * @param count 总条数，注意 不是总页数
	 * @param pageSize 一页个数
	 * @param currenturl 链接
	 * @param paramValue 链接参数
	 * @param urlSuffix 链接后缀
	 */
	public static void buildPageModel(Model model, long pageIndex, long count, long pageSize, String currenturl, String paramValue, String urlSuffix){
		PageVO pageVO = new PageVO();
		pageVO.setPageIndex(pageIndex);
		BigDecimal countM = new BigDecimal(count);
		BigDecimal pageSizeM = new BigDecimal(pageSize);
		BigDecimal divide = countM.divide(pageSizeM, RoundingMode.UP);
		pageVO.setPageCount(divide.longValue());
		pageVO.setCurrenturl(currenturl);
		pageVO.setUrlSuffix(urlSuffix);
		if(!StringUtils.startsWith(paramValue, "?") && StringUtils.isNotBlank(paramValue)){
			paramValue = "?" + paramValue;
		}
		pageVO.setParamValue(paramValue);
		model.add("pageEntity", pageVO);
	}
	
	public static void main(String[] args) {
		BigDecimal countM = new BigDecimal(13);
		BigDecimal pageSizeM = new BigDecimal(4);
		BigDecimal divide = countM.divide(pageSizeM, RoundingMode.UP);
		System.out.println(divide.longValue());
	}
}
