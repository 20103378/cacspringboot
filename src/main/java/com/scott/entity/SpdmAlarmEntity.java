package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class SpdmAlarmEntity extends BaseEntity {
	private String DeviceID;// 设备ID
	private Double PaDschThresHold;//压力告警值
}
