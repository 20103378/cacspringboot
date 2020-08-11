package com.scott.dao;


import com.base.dao.BaseDao;
import com.base.page.BasePage;
import com.scott.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>JeecgPersonDao<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Repository
public interface SystemConfigurationDao<T> extends BaseDao<T> {

    List<DeviceEntity> getDeviceBySpace(String Space);

    List<DeviceEntity> getDeviceBySpace_ft(String Space);

    List<Station_InfoEntity> getStation();

    void updateStation(Station_InfoEntity entity);

    int EquipmentListCount(BasePage page);

    List<EquipmentEntity> getEquipmentList(BasePage page);

    List<Refname_descEntity> getrefname();

    int getrefnameFlag(Refname_descEntity entity);

    void update_refname(Refname_descEntity entity);

    void add_refname(Refname_descEntity entity);

    void delete_refname(Refname_descEntity entity);

    int DeviceListCount(BasePage page);

    List<DeviceEntity> getDeviceList(BasePage page);

    void update_device(DeviceEntity entity);

    void insert_device(DeviceEntity entity);

    String DeviceMaxId();

    void update_equipment(EquipmentEntity entity);

    void add_equipment(EquipmentEntity entity);

    void delete_equipment(EquipmentEntity entity);

    List<EquipmentSpaceEntity> getEquipmentSapce();

    //	 String getMAXSapceID();
    int getinsertFlag_space(EquipmentSpaceEntity space);

    void updatespace(EquipmentSpaceEntity space);

    void insertspace(EquipmentSpaceEntity space);

    void delete_space(EquipmentSpaceEntity space);

    List<String> getNextEquipmentID();

    void delete_device(DeviceEntity entity);

    List<Sf6AlarmEntity> getSf6Monitor(Sf6AlarmEntity entity);

    List<StomAlarmEntity> getStomMonitor(StomAlarmEntity entity);

    List<SmoamAlarmEntity> getSmoamMonitor(SmoamAlarmEntity entity);

    List<ScomAlarmEntity> getScomMonitor(ScomAlarmEntity entity);

    List<SconditionAlarmEntity> getSconditionMonitor(SconditionAlarmEntity entity);

    List<DeviceEntity> getCheckBox(DeviceEntity entity);

    void insertSf6Monitor(Sf6AlarmEntity entity);

    void updateSf6Monitor(Sf6AlarmEntity entity);

    void insertStomMonitor(StomAlarmEntity entity);

    void updateStomMonitor(StomAlarmEntity entity);

    void insertSmoamMonitor(SmoamAlarmEntity entity);

    void updateSmoamMonitor(SmoamAlarmEntity entity);

    void insertScomMonitor(ScomAlarmEntity entity);

    void updateScomMonitor(ScomAlarmEntity entity);

    void insertSconditionMonitor(SconditionAlarmEntity entity);

    void updateSconditionMonitor(SconditionAlarmEntity entity);

    List<DeviceEntity> getExportList();

    int I2ListCount(BasePage page);

    int ZJ103Count(BasePage page);

    int getZJ103CountByln(ZJ103Entity DeviceID);

    void updateZJ103(ZJ103Entity DeviceID);

    void insertZJ103(ZJ103Entity DeviceID);

    String ZJ103DeviceIDMax();

    List<I2TableEntity> getI2Data(BasePage page);

    List<ZJ103Entity> getI2Data_103(BasePage page);

    void Updata_103(ZJ103Entity DeviceID);

    void submit_103_devPhase(ZJ103Entity DeviceID);

    void delete_103(ZJ103Entity DeviceID);

    List<I2TableEntity> getI2Data_export(BasePage page);

    List<YcDataInstEntity> getycNameList(YcDataInstEntity entity);

    List<YcDataInstEntity> getyxNameList(YcDataInstEntity entity);

    List<YcDataInstEntity> getykNameList(YcDataInstEntity entity);

    Integer getInfraredFlag(InfraredTableEntity entity);

    void insertInfraredTable(InfraredTableEntity entity);

    void updateInfraredTable(InfraredTableEntity entity);

    Integer getinsertFlag(I2TableEntity entity);

    void insertI2Table(I2TableEntity entity);

    void updateI2Table(I2TableEntity entity);

    void delete_I2(I2TableEntity entity);

    List<YcDataInstEntity> getycIEC61850LD_LN(I2TableEntity entity);

    List<YcDataInstEntity> getyxIEC61850LD_LN(I2TableEntity entity);

    List<YcDataInstEntity> getykIEC61850LD_LN(I2TableEntity entity);

    int getyxCount(Data_instEntity entity);

    int updateyx(Data_instEntity entity);

    int insertyx(Data_instEntity entity);

    //	 void insertyxList(List<Data_instEntity> yxInst);
    int getycCount(Data_instEntity entity);

    int insertyc(Data_instEntity entity);

    //	 void insertycList(List<Data_instEntity> ycInst);
    int getykCount(Data_instEntity entity);

    int updateyk(Data_instEntity entity);

    int insertyk(Data_instEntity entity);

    int insertSGCByk(Data_instEntity entity);

    int getLnCount(InstNodeEntity entity);

    int updateLn(InstNodeEntity entity);

    int insertLn(InstNodeEntity entity);

    //根据ln获取点序号
    List<YclysEntity> getyxByld(YclysEntity entity);

    List<YclysEntity> getycByld(YclysEntity entity);

    List<YclysEntity> getykByld(YclysEntity entity);


    List<DataEntity> getYXDataRefname();

    List<String> getNextDeviceID();


}
