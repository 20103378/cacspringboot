package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class Sf6AlarmEntity extends BaseEntity {
	private  String DeviceID;// 设备ID
	private  Double PressureThreshold;//压力告警值
	private  String PressureThresholdName;//压力告警值
}
