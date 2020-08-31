package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class ScomAlarmEntity extends BaseEntity {
	private  String deviceID;// 设备ID
	//本体主油箱压力式油位上限
	private  Double preOilUpThresHold;
	//本体主油箱压力式油位下限(mA)
	private  Double preOilDownThresHold;
	//网侧绕组温度上限(mA)
	private  Double tmp1ThresHold;
	//顶层油温2上限(mA)
	private  Double tmp3ThresHold;
	//顶层油温1上限(mA)
	private  Double tmp4ThresHold;
	//阀侧首端套管SF6压力下限(mA)
	private  Double pre1ThresHold;
	//阀侧末端套管SF6压力下限(mA)
	private  Double pre2ThresHold;
	//本体主油箱磁力式油位上限(mA)
	private  Double mainOilUpThresHold;
	//本体主油箱磁力式油位下限(mA)
	private  Double mainOilDownThresHold;
	//有载开关油位上限(mA)
	private  Double sltcOilUpThresHold;
	//有载开关油位下限(mA)
	private  Double sltcOilDownThresHold;

}
