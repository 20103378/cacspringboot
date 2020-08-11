package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

/**
 *
 * <br>
 * <b>功能：</b>JeecgPersonEntity<br>
 * <b>作者：</b>www.jeecg.org<br>F
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Data
public class DataEntity extends BaseEntity {
	private String id;
	private String refname;
	private String time;
	private String value;
	private String ln_inst_name;
	private String ld_inst_name;
	private String ied_type_id;
	private String quality;
}

