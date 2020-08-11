package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class StomAlarmEntity extends BaseEntity {
	private String DeviceID;// 设备ID
	private Double H2ThresHold;//氢气上限值(ppm)
	private Double C2H2ThresHold;//乙炔上限值(ppm)
	private Double THThresHold;//总烃上限值(ppm)
	private Double H2Change;//氢气变化率(ppm/day)
	private Double C2H2Change;//乙炔变化率(ppm/5day)
	private Double THChange;//总烃变化率(ppm/day)
	private Double MstThresHold;//微水上限值(mg/m3)
}
