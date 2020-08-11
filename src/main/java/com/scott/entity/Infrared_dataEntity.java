package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class Infrared_dataEntity extends BaseEntity {

	private String DeviceID;// 设备ID
	private java.util.Date SampleTime;// 采集时间
	private String DeviceName;
	private Double Tmp;// 温度
	private Integer Remark; // 状态
}
