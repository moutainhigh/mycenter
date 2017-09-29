package com.jx.blackface.mycenter.controllers;

import com.jx.argo.ActionResult;
import com.jx.argo.annotations.Path;
import com.jx.blackface.mycenter.annotaion.CheckLogin;

public class FirstPageController extends BaseController {
	
	@CheckLogin
	@Path("/")
	public ActionResult index(){
		return redirect("/mycenter/index.html");
	}
}
