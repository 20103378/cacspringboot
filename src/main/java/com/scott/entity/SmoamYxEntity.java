package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

/**
 *
 * <br>
 * <b>功能：</b>油色谱遥信实体类<br>
 * <b>作者：</b>xjiawei<br>
 * <b>日期：</b> 9, 7, 2015 <br>
 */
@Data
public class SmoamYxEntity extends BaseEntity {
	//
	private Object TotA;
	private Object RisA;
	private Object RisCaRte;
	private Object LastLigTm;
	private Object LigCnt;
	private String DeviceName;
	//
	private String SupDevRun;  //监测设备运行异常
	private String MoDevConf;
	private String Health;
	private String TotAAlm;
	private String DeviceID;
	private String SampleTime;
	private String Type;
	private String Remark;

    public SmoamYxEntity(int l) {
        this.TotA="无数据";
        this.RisA="无数据";
        this.RisCaRte="无数据";
        this.LastLigTm="无数据";
        this.LigCnt="无数据";
    }

}

