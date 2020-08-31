package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class SmoamAlarmEntity extends BaseEntity {
	private  String deviceID;// 设备ID
	private Double totAThresHold;//泄漏电流上限(mA)
}
