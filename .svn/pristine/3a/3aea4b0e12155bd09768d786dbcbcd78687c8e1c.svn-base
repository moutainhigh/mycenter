/**
 * 
 */

package com.jx.blackface.mycenter.controllers;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jx.argo.ActionResult;
import com.jx.argo.annotations.Path;
import com.jx.argo.controller.AbstractController;
import com.jx.blackface.fileplug.buzs.FileDownloadBuz;
import com.jx.blackface.mycenter.utils.ActionResultUtils;
import com.jx.service.dic.entity.DicFileEntity;



/**
 * simple introduction
 *
 * <p>detailed comment</p>
 * @author chuxuebao 2015年8月18日
 * @see
 * @since 1.0
 */
@Path("file")
public class FileController extends AbstractController {
	
	
	@Path("download/{fileIds:[\\w-;]+}")
	public ActionResult downloadFile(String fileIds){
		if(StringUtils.isBlank(fileIds)){
			return ActionResultUtils.renderJson("failed");
		}
		String maxLength = request().getParameter("maxLength");
		DicFileEntity fileEntity = FileDownloadBuz.getInstance().getLvFileEntity(fileIds, maxLength);
		if (fileEntity != null) {
			return ActionResultUtils.renderFile(fileEntity.getFileData(), fileEntity.getFileName());
		}
		
		return ActionResultUtils.renderJson("");
	}
	
	
	@Path("get/info/{fileId:\\d+}")
	public ActionResult getInfoFile(long fileId){
		DicFileEntity lvFileEntity = null;
		try {
			lvFileEntity = FileDownloadBuz.iLvFileService.loadByIdNoData(fileId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 上传成功；
		JSONObject reObj = new JSONObject();
		if(lvFileEntity != null){
			reObj.put("fileId", lvFileEntity.getId());
			reObj.put("fileName", lvFileEntity.getFileName());
		}
		return ActionResultUtils.renderJson(reObj);
	}
}
