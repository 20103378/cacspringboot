package com.base.entity.dto;

import lombok.Data;

@Data
public class Sf6AlarmRequestDTO {
    private  String deviceID;// 设备ID
    private  String pressureThreshold;//压力告警值
    private  String pressChgRateThreshold;//
    private  String weekChgRateThreshold;
}
