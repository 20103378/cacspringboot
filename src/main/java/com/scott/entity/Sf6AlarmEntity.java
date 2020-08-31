package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class Sf6AlarmEntity extends BaseEntity {
	private  String deviceID;// 设备ID
	private  Double pressureThreshold;//压力告警值
	private  Double pressChgRateThreshold;//
	private  Double weekChgRateThreshold;
}
