package com.base.page;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class BasePage {

	private Integer page = 1;
	
	private Integer rows =10;
	
	private String sort;
	
	private String order;

	
	/**
	 * 分页导航
	 */
	private Pager pager = new Pager();
	
	public Pager getPager() {
		pager.setPageId(getPage());
		pager.setPageSize(getRows());
		String orderField="";
		if(StringUtils.isNotBlank(sort)){
			orderField = sort;
		}
		if(StringUtils.isNotBlank(orderField) && StringUtils.isNotBlank(order)){
			orderField +=" "+ order;
		}
		pager.setOrderField(orderField);
		return pager;
	}
	
}
