package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

/**
 *
 * <br>
 * <b>功能：</b>油色谱遥信实体类<br>
 * <b>作者：</b>xjiawei<br>
 * <b>日期：</b> 9, 7, 2015 <br>
 */
@Data
public class WeatherEntity extends BaseEntity {
	//
	private String DeviceID;
	private String DeviceName;
	private String SampleTime;

	private Object Temperature;
	private Object Humidity;
	private Object WindDirection;
	private Object WindSpeed;
	private String Remark;

}

