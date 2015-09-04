package com.cherry.application.servlet.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cherry.application.webcommon.ResponseCommon;

@WebServlet(asyncSupported=true,name="user",urlPatterns="/user")
public class UserServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5903222675539744517L;

	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
			ResponseCommon.printJSON(response, "serlvet 访问成功");
	}

}
