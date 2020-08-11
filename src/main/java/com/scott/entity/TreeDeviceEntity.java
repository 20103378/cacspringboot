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
public class TreeDeviceEntity extends BaseEntity {


	private  String id;
	private  String text;
	private  String parentid;
	private  String nodetype;
	private  String value;
	private  String remark;
	private  String imgPath;
}

