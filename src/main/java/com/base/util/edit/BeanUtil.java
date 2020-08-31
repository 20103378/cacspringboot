package com.base.util.edit;


import com.base.entity.dto.ScomAlarmRequestDTO;
import com.base.entity.dto.Sf6AlarmRequestDTO;
import com.base.entity.dto.SmoamAlarmRequestDTO;
import com.base.entity.dto.StomAlarmRequestDTO;
import com.scott.entity.ScomAlarmEntity;
import com.scott.entity.Sf6AlarmEntity;
import com.scott.entity.SmoamAlarmEntity;
import com.scott.entity.StomAlarmEntity;
import org.springframework.beans.BeanUtils;

public class BeanUtil<Dto, Do> {

    /**
     * dto 转换为Do 工具类
     *
     * @param dtoEntity
     * @param doClass
     * @return
     */
    public static <Do> Do dtoToDo(Object dtoEntity, Class<Do> doClass) {
        // 判断dto是否为空!
        if (dtoEntity == null) {
            return null;
        }
        // 判断DoClass 是否为空
        if (doClass == null) {
            return null;
        }
        try {
            Do newInstance = doClass.newInstance();
            BeanUtils.copyProperties(dtoEntity, newInstance);
            // Dto转换Do
            return newInstance;
        } catch (Exception e) {
            return null;
        }
    }
    public static StomAlarmEntity stomAlarmRequestDTOToStomAlarmEntity(StomAlarmRequestDTO stomAlarmRequestDTO) {
        // 判断dto是否为空!
        if (stomAlarmRequestDTO == null) {
            return null;
        }
        StomAlarmEntity stomAlarmEntity = new StomAlarmEntity();
        stomAlarmEntity.setDeviceID(stomAlarmRequestDTO.getDeviceID());
        stomAlarmEntity.setMstThresHold(stomAlarmRequestDTO.getMstThresHold()==""? null:Double.parseDouble(stomAlarmRequestDTO.getMstThresHold()));
        stomAlarmEntity.setThThresHold(stomAlarmRequestDTO.getThThresHold()==""?null:Double.parseDouble(stomAlarmRequestDTO.getThThresHold()));
        stomAlarmEntity.setH2ThresHold(stomAlarmRequestDTO.getH2ThresHold()==""?null:Double.parseDouble(stomAlarmRequestDTO.getH2ThresHold()));
        stomAlarmEntity.setO2ThresHold(stomAlarmRequestDTO.getO2ThresHold()==""?null:Double.parseDouble(stomAlarmRequestDTO.getO2ThresHold()));
        stomAlarmEntity.setCoThresHold(stomAlarmRequestDTO.getCoThresHold()==""?null:Double.parseDouble(stomAlarmRequestDTO.getCoThresHold()));
        stomAlarmEntity.setCo2ThresHold(stomAlarmRequestDTO.getCo2ThresHold()==""?null:Double.parseDouble(stomAlarmRequestDTO.getCo2ThresHold()));
        stomAlarmEntity.setCh4ThresHold(stomAlarmRequestDTO.getCh4ThresHold()==""?null:Double.parseDouble(stomAlarmRequestDTO.getCh4ThresHold()));
        stomAlarmEntity.setC2h6ThresHold(stomAlarmRequestDTO.getC2h6ThresHold()==""?null:Double.parseDouble(stomAlarmRequestDTO.getC2h6ThresHold()));
        stomAlarmEntity.setC2h4ThresHold(stomAlarmRequestDTO.getC2h4ThresHold()==""?null:Double.parseDouble(stomAlarmRequestDTO.getC2h4ThresHold()));
        stomAlarmEntity.setC2h2ThresHold(stomAlarmRequestDTO.getC2h2ThresHold()==""?null:Double.parseDouble(stomAlarmRequestDTO.getC2h2ThresHold()));

        return stomAlarmEntity;
    }
    public static Sf6AlarmEntity sf6AlarmRequestDTOToStomAlarmEntity(Sf6AlarmRequestDTO sf6AlarmRequestDTO) {
        // 判断dto是否为空!
        if (sf6AlarmRequestDTO == null) {
            return null;
        }
        Sf6AlarmEntity sf6AlarmEntity = new Sf6AlarmEntity();
        sf6AlarmEntity.setDeviceID(sf6AlarmRequestDTO.getDeviceID());
        sf6AlarmEntity.setPressChgRateThreshold(sf6AlarmRequestDTO.getPressChgRateThreshold()==""? null:Double.parseDouble(sf6AlarmRequestDTO.getPressChgRateThreshold()));
        sf6AlarmEntity.setPressureThreshold(sf6AlarmRequestDTO.getPressureThreshold()==""? null:Double.parseDouble(sf6AlarmRequestDTO.getPressureThreshold()));
        sf6AlarmEntity.setWeekChgRateThreshold(sf6AlarmRequestDTO.getWeekChgRateThreshold()==""? null:Double.parseDouble(sf6AlarmRequestDTO.getWeekChgRateThreshold()));
        return sf6AlarmEntity;
    }
    public static SmoamAlarmEntity smoamAlarmRequestDTOToSmoamAlarmEntity(SmoamAlarmRequestDTO smoamAlarmRequestDTO) {
        // 判断dto是否为空!
        if (smoamAlarmRequestDTO == null) {
            return null;
        }
        SmoamAlarmEntity smoamAlarmEntity = new SmoamAlarmEntity();
        smoamAlarmEntity.setDeviceID(smoamAlarmRequestDTO.getDeviceID());
        smoamAlarmEntity.setTotAThresHold(smoamAlarmRequestDTO.getTotAThresHold()==""? null:Double.parseDouble(smoamAlarmRequestDTO.getTotAThresHold()));
        return smoamAlarmEntity;
    }
    public static ScomAlarmEntity scomAlarmRequestDTOToScomAlarmEntity(ScomAlarmRequestDTO scomAlarmRequestDTO) {
        // 判断dto是否为空!
        if (scomAlarmRequestDTO == null) {
            return null;
        }
        ScomAlarmEntity scomAlarmEntity = new ScomAlarmEntity();
        scomAlarmEntity.setDeviceID(scomAlarmRequestDTO.getDeviceID());
        scomAlarmEntity.setMainOilDownThresHold(scomAlarmRequestDTO.getMainOilDownThresHold()==""? null:Double.parseDouble(scomAlarmRequestDTO.getMainOilDownThresHold()));
        scomAlarmEntity.setMainOilUpThresHold(scomAlarmRequestDTO.getMainOilUpThresHold()==""? null:Double.parseDouble(scomAlarmRequestDTO.getMainOilUpThresHold()));
        scomAlarmEntity.setPre1ThresHold(scomAlarmRequestDTO.getPre1ThresHold()==""? null:Double.parseDouble(scomAlarmRequestDTO.getPre1ThresHold()));
        scomAlarmEntity.setPre2ThresHold(scomAlarmRequestDTO.getPre2ThresHold()==""? null:Double.parseDouble(scomAlarmRequestDTO.getPre2ThresHold()));
        scomAlarmEntity.setPreOilDownThresHold(scomAlarmRequestDTO.getPreOilDownThresHold()==""? null:Double.parseDouble(scomAlarmRequestDTO.getPreOilDownThresHold()));
        scomAlarmEntity.setPreOilUpThresHold(scomAlarmRequestDTO.getPreOilUpThresHold()==""? null:Double.parseDouble(scomAlarmRequestDTO.getPreOilUpThresHold()));
        scomAlarmEntity.setSltcOilDownThresHold(scomAlarmRequestDTO.getSltcOilDownThresHold()==""? null:Double.parseDouble(scomAlarmRequestDTO.getSltcOilDownThresHold()));
        scomAlarmEntity.setSltcOilUpThresHold(scomAlarmRequestDTO.getSltcOilUpThresHold()==""? null:Double.parseDouble(scomAlarmRequestDTO.getSltcOilUpThresHold()));
        scomAlarmEntity.setTmp1ThresHold(scomAlarmRequestDTO.getTmp1ThresHold()==""? null:Double.parseDouble(scomAlarmRequestDTO.getTmp1ThresHold()));
        scomAlarmEntity.setTmp3ThresHold(scomAlarmRequestDTO.getTmp3ThresHold()==""? null:Double.parseDouble(scomAlarmRequestDTO.getTmp3ThresHold()));
        scomAlarmEntity.setTmp4ThresHold(scomAlarmRequestDTO.getTmp4ThresHold()==""? null:Double.parseDouble(scomAlarmRequestDTO.getTmp4ThresHold()));
        return scomAlarmEntity;
    }
    /**
     * do 转换为Dto 工具类
     *
     * @param doEntity
     * @param dtoClass
     * @return
     */
    public static <Dto> Dto doToDto(Object doEntity, Class<Dto> dtoClass) {
        // 判断do是否为空!
        if (doEntity == null) {
            return null;
        }
        // 判断DtoClass 是否为空
        if (dtoClass == null) {
            return null;
        }
        try {
            Dto newInstance = dtoClass.newInstance();
            BeanUtils.copyProperties(doEntity, newInstance);
            // Do转换 Dto
            return newInstance;
        } catch (Exception e) {
            return null;
        }
    }
}

