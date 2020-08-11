package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class SCOM_dataEntity extends BaseEntity {
	private String DeviceName;
	private  String DeviceID;// 设备ID
	private  String SampleTime;// 采集时间
	private  Double CGAmp;//铁芯接地电流
	private  Double ClpGAmp;//夹件接地电流
	private  Double Amp1;//夹件电流
	private  Double MainOil;//本体主油箱磁力式油位
	private  Double PreOil;//本体主油箱压力式油位
	private  Double SltcOil;//"有载开关油位"
	private  Double Tmp1;//"网侧绕组温度
	private  Double Tmp2;//"油面温度
	private  Double Tmp3;//"备用
	private  Double Tmp4;//"顶层油温1
	private  Double Tmp5;//"顶层油温2
	private  Double Tmp6;//"底层油温1
	private  Double Tmp7;//"底层油温2
	private  Double Tmp8;//"环境温度
	private  Double Pre1;//"阀侧首端套管SF6压力
	private  Double Pre2;//"阀侧末端套管SF6压力

	private  Integer Remark;//状态
	


}
