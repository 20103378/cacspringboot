package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

/**
 *
 * <br>
 * <b>功能：</b>JeecgPersonEntity<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Data
public class EquipmentEntity extends BaseEntity {


	private String equipmentID;//设备编码
	private String equipmentName;//设备名称
	private String phase;//设备相别
	private Integer deviceType;//   设备类型
	private String objectVoltage;// 电压等级
	private String spaceId;// 区域ID
	private String manufactoryName;
	private String remark;
	private String iec61850LD; //主设备编码
}

