package com.cherry.application.jetty;

public class InitBean {
	/**
	 * filter或者servlet的名称，例如com.xxx.xxxFilter 或者com.xxx.xxxServlet
	 */
	private String className;
	/**
	 * 匹配路径
	 */
	private String pathSpec;


	public String getClassName() {
		return className;
	}

	public String getPathSpec() {
		return pathSpec;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setPathSpec(String pathSpec) {
		this.pathSpec = pathSpec;
	}
	
	

}
