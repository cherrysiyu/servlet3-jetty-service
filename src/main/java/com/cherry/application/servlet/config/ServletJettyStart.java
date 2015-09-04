package com.cherry.application.servlet.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.application.utils.ClassUtils;
import com.cherry.application.webcommon.SetCharacterEncodingFilter;

public class ServletJettyStart {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private int port ;
	private Server server;
	private String contextPath;
	private Map<String,Class<? extends HttpServlet>> servlets;
	private List<Class<? extends HttpServlet>> annotationServlets;
	private List<String> annotationServletPackage;
	public ServletJettyStart(Map<String,Class<? extends HttpServlet>> servlets) {
		this(80, "/",servlets);
	}
	public ServletJettyStart(List<String> annotationServletPackage) {
		this(annotationServletPackage,80, "/");
	}
	
	public ServletJettyStart(int port, String contextPath,Map<String,Class<? extends HttpServlet>> servlets) {
		this(port, contextPath, null, servlets);	
	}
	public ServletJettyStart(List<String> annotationServletPackage,int port, String contextPath) {
		this(port, contextPath, null, null,annotationServletPackage);	
	}
	public ServletJettyStart(int port, String contextPath,List<Class<? extends HttpServlet>> annotationServlets) {
		this(port, contextPath, annotationServlets, null);	
	}
	public ServletJettyStart(int port, String contextPath,List<Class<? extends HttpServlet>> annotationServlets,Map<String,Class<? extends HttpServlet>> servlets) {
		this(port, contextPath, annotationServlets, servlets, null);
	}
	
	public ServletJettyStart(int port, String contextPath,List<Class<? extends HttpServlet>> annotationServlets,Map<String,Class<? extends HttpServlet>> servlets,List<String> annotationServletPackage) {
		super();
		this.port = port;
		this.contextPath = contextPath;
		this.servlets = servlets;
		this.annotationServletPackage = annotationServletPackage;
		this.annotationServlets = annotationServlets;
	}
	
	
	@SuppressWarnings("unchecked")
	private void init() {
		server = new Server(port);
		// 设置在JVM退出时关闭Jetty的钩子。
        server.setStopAtShutdown(true);
        
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		
		context.setContextPath(getContextPath());
		server.setHandler(context);
		//设置编码
		context.addFilter(SetCharacterEncodingFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
		
		if(annotationServletPackage != null && !annotationServletPackage.isEmpty()){
			Set<Class<? extends HttpServlet>> collection = new LinkedHashSet<Class<? extends HttpServlet>>();
			for (String servletPackage : annotationServletPackage) {
				Set<Class<?>> classes = ClassUtils.getClasses(servletPackage);
				for (Class<?> clazz : classes) {
					 if (clazz.isAnnotationPresent(WebServlet.class)) {
						 collection.add(((Class<? extends HttpServlet>)clazz));
					 }
				}
			}
			if(annotationServlets == null)
				annotationServlets = new ArrayList<Class<? extends HttpServlet>>();
			annotationServlets.addAll(collection);
		}
		
		
		
		if(annotationServlets != null){
			for (Class<? extends HttpServlet> clazz : annotationServlets) {
				 if (clazz.isAnnotationPresent(WebServlet.class)) {
					//获取WebServlet这个Annotation的实例
					 WebServlet annotationInstance = clazz.getAnnotation(WebServlet.class);
					 List<String> urlpatterns = new ArrayList<String>();
					 //获取Annotation的实例的urlPatterns属性的值
					 String[] urlPatterns = annotationInstance.urlPatterns();
					 if(urlPatterns != null && urlPatterns.length >0){
						 urlpatterns.addAll(Arrays.asList(urlPatterns));
					 }else{
						//获取Annotation的实例的value属性的值
						 String[] annotationAttrValues = annotationInstance.value();
						 if(annotationAttrValues != null && annotationAttrValues.length>0){
							 urlpatterns.addAll(Arrays.asList(annotationAttrValues));
						 }
					 }
					if(!urlpatterns.isEmpty()){
						for (String urlPattern : urlPatterns) {
							context.addServlet(clazz, urlPattern);
						}
					}
				 }
			}
			
		}
		if(servlets != null){
			for (Map.Entry<String,Class<? extends HttpServlet>> entry : servlets.entrySet()) {
				Class<? extends HttpServlet> class1 = entry.getValue();
				context.addServlet(class1,entry.getKey());
			}
		}
		
	}
	public void start() throws Exception{
		init();
		if (server!= null) {
			if (server.isStarting() || server.isStarted() || server.isRunning() ) {
				return;
			}
		}
		TimeUnit.SECONDS.sleep(1);
		server.start();
		logger.info("启动servletJetty成功，端口:"+port+" contentPath:"+getContextPath());
	}
	public void stop() throws Exception{
		if (server != null) {
			if (server.isRunning()) {
				server.stop();
			}
		}
	}
	public void join() throws InterruptedException{
		if (server!=null) {
			server.join();
		}
	}


	public String getContextPath() {
		return (contextPath==null||"".equals(contextPath))?"/":contextPath;
	}
	
	
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	public List<Class<? extends HttpServlet>> getAnnotationServlets() {
		return annotationServlets;
	}
	public void setAnnotationServlets(
			List<Class<? extends HttpServlet>> annotationServlets) {
		this.annotationServlets = annotationServlets;
	}
	public List<String> getAnnotationServletPackage() {
		return annotationServletPackage;
	}
	public void setAnnotationServletPackage(List<String> annotationServletPackage) {
		this.annotationServletPackage = annotationServletPackage;
	}
	
	
	
}
