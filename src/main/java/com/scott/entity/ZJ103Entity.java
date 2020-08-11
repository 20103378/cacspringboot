package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class ZJ103Entity extends BaseEntity {

	private String DeviceID;  //i2id
	private String DeviceName;  //i1类型
	private Integer CommAddress;  //i1id
	private Integer DeviceType;//名称
	private String IEC61850LD_LN;//备注
    private Integer DevPhase;//备注

}
