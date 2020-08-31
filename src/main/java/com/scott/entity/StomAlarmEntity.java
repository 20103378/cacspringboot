package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class StomAlarmEntity extends BaseEntity {
	private String deviceID;// 设备ID
	//氢气上限值(ppm)
	private Double h2ThresHold;
	//乙炔上限值(ppm)
	private Double c2h2ThresHold;
	//总烃上限值(ppm)
	private Double thThresHold;
//	private Double h2Change;//氢气变化率(ppm/day)
//	private Double c2h2Change;//乙炔变化率(ppm/5day)
//	private Double thChange;//总烃变化率(ppm/day)

	//甲烷上限值
	private Double ch4ThresHold;
	//一氧化碳上限值
	private Double coThresHold;
	//二氧化碳上限值
	private Double co2ThresHold;
	//氧气上限值
	private Double o2ThresHold;
	//乙烯上限值
	private Double c2h4ThresHold;
	//乙烷上限值
	private Double c2h6ThresHold;

	//微水上限值(mg/m3)
	private Double mstThresHold;
}
