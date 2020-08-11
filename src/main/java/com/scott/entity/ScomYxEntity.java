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
public class ScomYxEntity extends BaseEntity {
	//
	private Object CGAmp;
	//
	private String DeviceName;
	private String SupDevRun;  //监测设备运行异常
	private String MoDevConf;
	private String CGAlm;
	private String Amp1;//夹件电流
	private String MainOil;//本体主油箱磁力式油位
	private String PreOil;//本体主油箱压力式油位
	private String SltcOil;//"有载开关油位"
	private String Tmp1;//"网侧绕组温度
	private String Tmp2;//"油面温度
	private String Tmp3;//"备用
	private String Tmp4;//"顶层油温1
	private String Tmp5;//"顶层油温2
	private String Tmp6;//"底层油温1
	private String Tmp7;//"底层油温2
	private String Tmp8;//"环境温度
	private String Pre1;//"阀侧首端套管SF6压力
	private String Pre2;//"阀侧末端套管SF6压力
	private String Health;
	private String DeviceID;
	private String SampleTime;
	private String Type;
	private String Remark;
	public ScomYxEntity(int l) {
        this.CGAmp="无数据";
    }
    
}

