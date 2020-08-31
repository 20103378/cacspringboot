package com.base.entity.dto;

import lombok.Data;

@Data
public class ScomAlarmRequestDTO {
    private  String deviceID;// 设备ID
    //本体主油箱压力式油位上限
    private  String preOilUpThresHold;
    //本体主油箱压力式油位下限(mA)
    private  String preOilDownThresHold;
    //网侧绕组温度上限(mA)
    private  String tmp1ThresHold;
    //顶层油温1上限(mA)
    private  String tmp4ThresHold;
    //阀侧首端套管SF6压力下限(mA)
    private  String pre1ThresHold;
    //阀侧末端套管SF6压力下限(mA)
    private  String pre2ThresHold;
    //顶层油温2上限(mA)
    private  String tmp3ThresHold;
    //本体主油箱磁力式油位上限(mA)
    private  String mainOilUpThresHold;
    //本体主油箱磁力式油位下限(mA)
    private  String mainOilDownThresHold;
    //有载开关油位上限(mA)
    private  String sltcOilUpThresHold;
    //有载开关油位下限(mA)
    private  String sltcOilDownThresHold;
}
