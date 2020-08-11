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


	private String EquipmentID;//设备编码
	private String EquipmentName;//设备名称
	private String Phase;//设备相别
	private Integer DeviceType;//   设备类型
	private String ObjectVoltage;// 电压等级
	private String SpaceId;// 区域ID
	private String ManufactoryName;
	private String Remark;
}

