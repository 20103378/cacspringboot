package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class Sf6_dataEntity extends BaseEntity {
	private String DeviceName;
	private  String DeviceID;// 设备ID
	private  String SampleTime;// 采集时间
	private  Double Tmp;// 温度
	private  Double Pres;// 压力
	private  Double Hum;// 湿度
	private  Double Den;// 密度
	private  Double MicrWat;// 微水
	private  Integer Remark; // 状态


}
