package com.base.entity.dto;

import lombok.Data;

@Data
public class SmoamAlarmRequestDTO {
    private  String deviceID;// 设备ID
    private String totAThresHold;//泄漏电流上限(mA)
}
