package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class YXHistoryEntity extends BaseEntity {

	private  String DeviceID;// 设备ID
	private  String ReportDate;// 采集时间
	private  String   ConstMember;// 参数名(遥信表)
	private  String   Value;// 参数值(遥信表)
}
