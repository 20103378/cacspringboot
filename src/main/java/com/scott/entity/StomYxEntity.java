package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * <br>
 * <b>功能：</b>油色谱遥信实体类<br>
 * <b>作者：</b>xjiawei<br>
 * <b>日期：</b> 9, 7, 2015 <br>
 */
@Data
@NoArgsConstructor
public class StomYxEntity extends BaseEntity {

	//
	private String DeviceName;
	private Object H2ppm;
	private Object COppm;
	private Object CO2ppm;
	private Object CH4ppm;
	private Object C2H2ppm;
	private Object C2H4ppm;
	private Object C2H6ppm;
	private Object TotHyd;//总烃
	private Object N2;
	private Object OilTmp;
	private Object GasPres;//载气压力
	private Object Mst;//微水
	//
	private String GasUnPresAlm;
	private String SupDevRun;
	private String MoDevConf;
	private String GasLowPresAlm;
	private String ActCyGasSta;
	private String GasBot;
	private String Health;
	private String H2Alm;
	private String C2H2Alm;
	private String TotHydcAlm;
	private String DeviceID;
	private String SampleTime;
	/**
	 * 前端展示类型
	 */
	private String Type;
	private String Remark;
}

