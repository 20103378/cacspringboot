package com.base.entity.dto;

import lombok.Data;

@Data
public class DeviceRequestDTO {
    private  String deviceID;//设备ID
    private  String equipmentID;//主设备ID
    private  Integer deviceType;//设备类型
}
