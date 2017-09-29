package com.jx.blackface.mycenter.actionresult;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.jx.argo.ActionResult;
import com.jx.argo.BeatContext;

public class JSONActionResult implements ActionResult {

	private static String jsonstr = null;
	public JSONActionResult(String jsonstr){
		this.jsonstr = jsonstr;
	}
	@Override
	public void render(BeatContext beat) {
		// TODO Auto-generated method stub
		HttpServletResponse response = beat.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.print(jsonstr.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}

	}

}
