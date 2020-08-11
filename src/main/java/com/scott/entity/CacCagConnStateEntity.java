package com.scott.entity;

import lombok.Data;

/**
 * 
 * @author zhou_wang
 *	2018-7-20
 *  Description:cac与cag通讯日志
 */
@Data
public class CacCagConnStateEntity {

	private String state_time;
	
	private int conn_state;
	
	private String client_name;
	
	private String client_ip;
	
	
}
