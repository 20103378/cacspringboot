package com.scott.page;

import com.base.page.BasePage;
import lombok.Data;

/**
 * 
 * <br>
 * <b>功能：</b>JeecgPersonPage<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Data
public class AlarmRecordPage extends BasePage {
	private String Start;//设备ID
	private String End;//设备ID
	private Integer DeviceType;//设备类型
	private Double RecordType;//告警类型
	
}
