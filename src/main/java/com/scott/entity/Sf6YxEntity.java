package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * <br>
 * <b>功能：</b>油色谱遥信实体类<br>
 * <b>作者：</b>xjiawei<br>
 * <b>日期：</b> 9, 7, 2015 <br>
 */
@Data
@NoArgsConstructor
public class Sf6YxEntity extends BaseEntity {

	//
	private String DeviceName;
	private Object Tmp;// 温度
	private Object Pres;// 压力
	private Object Hum;// 湿度
	//
	private String SupDevRun;  //监测设备运行异常
	private String MoDevConf;
	private String DenAlm;
	private String Health;
	private String DeviceID;
	private String SampleTime;
	private String Type;
	private String Remark;

	public Sf6YxEntity(int l) {
        this.Tmp="无数据";
        this.Pres="无数据";
        this.Hum="无数据";
    }
}

