package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

/**
 * 
 * <br>
 * <b>功能：</b>设备实体类<br>
 * <b>作者：</b>xjiawei<br>
 * <b>日期：</b> 9, 7, 2015 <br>
 */
@Data
public class MessageEntity extends BaseEntity {
	private String DeviceID;//告警类型
	private String Remark;//告警类型
	private String DeviceType;//设备类型
	private String Space;//设备区域
	private String DeviceName;//设备区域
}

