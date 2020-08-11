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
public class YXhistroyData extends BaseEntity {
	
	private  String deviceID;//设备ID
	private  String sampleTime;//
	private  Integer phyHealth;//
	private  Integer supDevRun;//运行异常告警
	private  Integer moDevConf;//通讯异常告警
	private  Integer health;//
	private  Integer gasUnPresAlm;//载气欠压告警
	private  Integer gasLowPresAlm;//载气低压告警
	private  Integer actCyGasSta;//实际气瓶供气状态
	private  Integer gasBot;//气瓶状态
	private  Integer gasLowPresAlm2;//载气2低压告警
	private  Integer gasUnPresAlm2;//载气2欠压告警
}

