package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class SconditionAlarmEntity extends BaseEntity {
	private  String DeviceID;// 设备ID
	private double OilTempThresHold;//顶层油温上限(℃)
	private  String   ConstMember;// 参数名(遥信表)
	private  String   Value;// 参数值(遥信表)
}
