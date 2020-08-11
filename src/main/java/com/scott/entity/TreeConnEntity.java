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
public class TreeConnEntity extends BaseEntity {
	
	private  String id;
	private  String text;//逻辑设备名
	private  String iedstate;//61850连通状态
	private  String datastate;//数据通信状态
	private  String systime;//数据通信状态
	private  String deviceip;//设备ip
	private  String updatetime;
	private  Boolean checked; //是否被选中
}

