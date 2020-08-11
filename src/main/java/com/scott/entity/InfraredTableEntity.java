package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class InfraredTableEntity extends BaseEntity {

	private String DeviceID;  //红外测温设备id
	private String DeviceName;  //红外测温设备名称

	public InfraredTableEntity(String deviceID, String deviceName) {
		DeviceID = deviceID;
		DeviceName = deviceName;
	}
}
