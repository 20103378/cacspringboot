package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

/**
 *
 * <br>
 * <b>功能：</b>遥信遥信实体类<br>
 * <b>作者：</b>xjiawei<br>
 * <b>日期：</b> 9, 7, 2015 <br>
 */
@Data
public class SpdmYxEntity extends BaseEntity {
	//
	private String DeviceName;
	/**
	 * 局放声学水平
	 */
	private Object AcuPaDsch;
	/**
	 * 局放UHF水平
	 */
	private Object UhfPaDsch;
	/**
	 * 放电相位
	 */
	private Object Phase;
	/**
	 * 脉冲次数
	 */
	private Object PlsNum;

	private String PaDschAlm;
	private String SupDevRun;
	private String MoDevConf;
	private String Health;
	private String DeviceID;
	private String SampleTime;
	private String Type;
	private String Remark;
}

