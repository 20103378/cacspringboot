package com.base.entity.dto;

import lombok.Data;

@Data
public class StomAlarmRequestDTO {
    private String deviceID;// 设备ID
    //氢气上限值(ppm)
    private String h2ThresHold;
    //乙炔上限值(ppm)
    private String c2h2ThresHold;
    //总烃上限值(ppm)
    private String thThresHold;
//	private Double h2Change;//氢气变化率(ppm/day)
//	private Double c2h2Change;//乙炔变化率(ppm/5day)
//	private Double thChange;//总烃变化率(ppm/day)

    //甲烷上限值
    private String ch4ThresHold;
    //一氧化碳上限值
    private String coThresHold;
    //二氧化碳上限值
    private String co2ThresHold;
    //氧气上限值
    private String o2ThresHold;
    //乙烯上限值
    private String c2h4ThresHold;
    //乙烷上限值
    private String c2h6ThresHold;

    //微水上限值(mg/m3)
    private String mstThresHold;
}
