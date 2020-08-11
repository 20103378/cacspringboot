package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

/**
 * 
 * <br>
 * <b>功能：</b>设备实体类<br>
 * <b>作者：</b>xjiawei<br>
 * <b>日期：</b> 9, 7, 2015 <br>
 */
@Data
public class ProcstateEntity extends BaseEntity {
	
	private String pid;//程序ID
	private String pname;//程序名称
	private String systime;//采集时间
	private String cpu;//CPU使用率
	private String mem;//内存使用率
}

