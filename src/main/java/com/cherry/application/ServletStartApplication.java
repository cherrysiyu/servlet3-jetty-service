package com.cherry.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import com.cherry.application.logback.LogbackConfigurer;
import com.cherry.application.servlet.action.UserServlet;
import com.cherry.application.servlet.config.ServletJettyStart;

public class ServletStartApplication {

	public static void main(String[] args) {
		try{
			LogbackConfigurer.initLogging("config/logback.xml");
			Map<String,Class<? extends HttpServlet>> servlets = new HashMap<String,Class<? extends HttpServlet>>();
			servlets.put("/user",UserServlet.class);
			new ServletJettyStart(servlets).start();
			
			List<Class<? extends HttpServlet>> servletList = new ArrayList<Class<? extends HttpServlet>>();
			servletList.add(UserServlet.class);
			new ServletJettyStart(8080,"/",servlets).start();
			
			
			new ServletJettyStart(Arrays.asList(new String[]{"com.cherry.application.servlet.action"}),9090,"/").start();
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		
		
		
	}
}
