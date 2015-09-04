package com.cherry.application.jetty;

import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
  	用户自定义ServletContextHandler，继承自ServletContextHandler，为了可以添加过滤器
 	在jetty.xml文件中不好配置{@code EnumSet<DispatcherType>},故继承已省略此参数
 * 
 * <p>
 * Copyright: Copyright (c) 2015年9月2日 上午9:02:50
 * <p>
 * Company: 
 * <p>
 * @version 1.0.0
 */
public class CustomeServletContextHandler extends ServletContextHandler {
	
	public CustomeServletContextHandler(List<InitBean> filterList){
		this(filterList, null);
	}
	
	public CustomeServletContextHandler(List<InitBean> filterList,List<InitBean> servletList){
		if(filterList == null && servletList == null){
			throw new IllegalArgumentException("no filters or servlets to init，please check");
		}
		if(filterList != null && !filterList.isEmpty()){
			for (InitBean initBean : filterList) {
				addFilter(initBean.getClassName(), initBean.getPathSpec());
			}
		}
		if(servletList != null && !servletList.isEmpty()){
			for (InitBean initBean : servletList) {
				addServlet(initBean.getClassName(), initBean.getPathSpec());
			}
		}
	}
	
	
	/**
	 * convenience method to add a filter
	 * @param filterClass
	 * @param pathSpec
	 * @return
	 */
	public FilterHolder addFilter(String filterClass, String pathSpec) {
		return getServletHandler().addFilterWithMapping(filterClass, pathSpec,
				EnumSet.allOf(DispatcherType.class));
	}
	/**
	 * convenience method to add a filter
	 * @param holder
	 * @param pathSpec
	 */
	public void addFilter(FilterHolder holder,String pathSpec){
		getServletHandler().addFilterWithMapping(holder,pathSpec,EnumSet.allOf(DispatcherType.class));
	}
	/**
	 * convenience method to add a filter
	 * @param filterClass
	 * @param pathSpec
	 * @return
	 */
	public FilterHolder addFilter(Class<? extends Filter> filterClass,String pathSpec)
    {
        return getServletHandler().addFilterWithMapping(filterClass,pathSpec,EnumSet.allOf(DispatcherType.class));
    }

}
