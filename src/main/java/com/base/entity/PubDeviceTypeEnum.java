package com.base.entity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum PubDeviceTypeEnum {
    OIL_CHROMATOGRAPHY_MICRO_WATER("1","油色谱及微水"),
    SF6_GAS_PRESSURE("2","SF6气体压力"),
    LIGHTNING_ARRESTER_AND_NUMBER_ACTIONS("3","避雷器及动作次数"),
    CORE_LEAKAGE_CURRENT("4","铁芯泄露电流"),
    CONVERTER_OPERATING_CONDITIONS("5","换流变运行工况"),
    SF6_GAS_LEAKAGE("6","SF6气体泄漏"),
    CASING("7","套管"),
    WEATHER_INFORMATION("8","微气象"),
    PARTIAL_RELEASE("19","局放"),
    MAIN_TRANSFORMER_PARTIAL_RELEASE("20","主变局放"),
    INFRARED_TEMPERATURE_MEASUREMENT("hwcw","红外测温"),
    AUXILIARY_EQUIPMENT("amc","辅助设备"),
    UNDEFINE(null,"未定义");


//    OIL_CHROMATOGRAPHY_MICRO_WATER("9","分压器"),
//    SF6_GAS_PRESSURE("10","套管局放"),
//    LIGHTNING_ARRESTER_AND_NUMBER_ACTIONS("11","GIS"),
//    CORE_LEAKAGE_CURRENT("12","开关柜"),
//    CONVERTER_OPERATING_CONDITIONS("13","CT"),
//    SF6_GAS_LEAKAGE("14","环境"),
//    CASING("15","CVT"),
//    WEATHER_INFORMATION("16","电抗器"),
//    WEATHER_INFORMATION("17","PT"),
//    WEATHER_INFORMATION("18","微水"),
//
//    OIL_CHROMATOGRAPHY_MICRO_WATER("21","GIS局放"),
//    SF6_GAS_PRESSURE("22","耦合电容器"),
//    LIGHTNING_ARRESTER_AND_NUMBER_ACTIONS("23","油温"),
//    CORE_LEAKAGE_CURRENT("24","母线"),
//    CONVERTER_OPERATING_CONDITIONS("25","母线CVT"),
//    SF6_GAS_LEAKAGE("26","绝缘子"),
//    CASING("27","线路CVT/OY"),
//    WEATHER_INFORMATION("28","发电机"),
//    WEATHER_INFORMATION("29","线路"),
//    WEATHER_INFORMATION("30","刀闸"),
//    WEATHER_INFORMATION("31","油位"),

    @Getter
    @Setter
    private String value;    //类型
    @Getter
    @Setter
    private String text;    //描述

    PubDeviceTypeEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }
    private String value() {
        return this.value;
    }
    private String text() {
        return this.text;
    }
    public static PubDeviceTypeEnum getValueEnum(String value) {
        PubDeviceTypeEnum[] carvalueEnums = values();
        for (PubDeviceTypeEnum carvalueEnum : carvalueEnums) {
            if (value.equalsIgnoreCase(carvalueEnum.value())) {
                return carvalueEnum;
            }
        }
        UNDEFINE.setValue(value);
        return UNDEFINE;
    }
    public static List<Map> getDeviceTypeEnumsByValues(List<Integer> values) {
        List<Map> list = new ArrayList<>();
        for (Integer value:values){
            PubDeviceTypeEnum tmp = getValueEnum(value.toString());
            Map map = new HashMap();
            map.put("text",tmp.text);
            map.put("value",tmp.value);
            list.add(map);
        }
        return list;
    }
    public static void main(String[] args) {
        System.out.println("Auxiliary_equipment".toUpperCase());
    }

}
