package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class SmoamAlarmEntity extends BaseEntity {
	private  String DeviceID;// 设备ID
	private Double TotalCurrentThresHold;//泄漏电流上限(mA)
}
