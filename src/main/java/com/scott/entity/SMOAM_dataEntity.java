package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class SMOAM_dataEntity extends BaseEntity {
	private String DeviceName;
	private  String DeviceID;// 设备ID
	private  String   SampleTime;// 采集时间
	private  Double TotA;	  // 全电流
	private  Double RisA;	  // 阻性电流
	private  Double RisCaRte;// 阻容比
	private  Double LastLigTm;// 最近动作时间
	private  Integer LigCnt;// 动作次数
	private  Integer Remark;// 备注


}
