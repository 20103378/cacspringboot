package com.scott.entity;

import lombok.Data;

/**
 * 
 * @author li_xujun
 *	2018-7-11
 *  Description:程序状态类
 */
@Data
public class CacExeStateEntity {

	private String exeName;
	
	private int exeState;
	
	private String exeStateTimestamp;
	
	private String updateStateTimestamp;
	
	private int runNum;
}
