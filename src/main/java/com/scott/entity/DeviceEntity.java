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
public class DeviceEntity extends BaseEntity {
	
	private  String DeviceID;//设备ID
	private  String DeviceName;//设备名称
	private  String DeviceType;//设备类型
	private  String ManufactoryName;//制造厂家
	private  String DeviceProductID;//出厂ID
	private  String DeviceLocation;//设备地址
	private  String StartOperateTime;//投运时间
	private  String EquipmentID;//主设备ID
	private  String IED_IP;//设备ip
	private  Double PosY;//地图y坐标
	private  Double PosX;//地图x坐标
	private  String IEC61850LD_LN;//61850id
	private  String StopSoundAlarm;//关闭告警音
	private  String StopUse;//是否停用
	private  String Remark;//备注
	private  String Space;//设备区域
	private  String Phase;//设备相别
	private  String Smperiod;//采样周期
	
}

