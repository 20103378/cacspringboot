package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

/**
 *
 * <br>
 * <b>功能：</b>JeecgPersonEntity<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Data
public class stom_dataEntity extends BaseEntity {
	private String DeviceName;
	private String DeviceID;
	private String SampleTime;
	private Double H2ppm;
	private Double COppm;
	private Double CO2ppm;
	private Double CH4ppm;
	private Double C2H2ppm;
	private Double C2H4ppm;
	private Double C2H6ppm;
	private Double TotHyd;//总烃
	private Double N2;
	private Double OilTmp;
	private Double GasPres;//载气压力
	private Double Mst;//微水
	private Integer Remark; // 状态

}

