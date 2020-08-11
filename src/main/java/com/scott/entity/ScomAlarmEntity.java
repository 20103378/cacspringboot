package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class ScomAlarmEntity extends BaseEntity {
	private  String DeviceID;// 设备ID
	private  Double CGAmpThresHold;//泄漏电流上限(mA)
}
