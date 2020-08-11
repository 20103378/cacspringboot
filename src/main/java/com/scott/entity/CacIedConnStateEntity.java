package com.scott.entity;

import lombok.Data;

/**
 * 
 * @author zhou_wang
 *	2018-7-20
 *  Description:cac与各装置通讯日志
 */
@Data
public class CacIedConnStateEntity {

	private String state_time;
	
	private int conn_state;
	
	private String ied_name;
	
	private String ied_ip;
	
	
}
