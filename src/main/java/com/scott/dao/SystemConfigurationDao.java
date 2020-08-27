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

    int updateStation(Station_InfoEntity entity);
    int insertStation(Station_InfoEntity entity);

    int EquipmentListCount(BasePage page);


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

    /**
     * 分页获取主设备列表
     * @param page
     * @return
     */
    List<EquipmentEntity> getEquipmentList(BasePage page);

    /**
     *
     * @return
     */

    List<String> getEquipmentIED61850LDsList();
    /**
     *
     * @return
     */

    List<String> getDeviceIED61850LD_LNsList(String equipmentID);

    /**
     * 更新主设备
     * @param entity
     */
    void update_equipment(EquipmentEntity entity);

    /**
     * 添加主设备
     * @param entity
     */
    void add_equipment(EquipmentEntity entity);

    /**
     * 通过主设备编码iec61850LD查询主设备是否存在
     * @param iec61850LD
     */
    EquipmentEntity findEquipmentByIEC61850LD(String iec61850LD);
    /**
     * 删除主设备
     * @param entity
     */
    void delete_equipment(EquipmentEntity entity);

    /**
     * 获取主设备所在space区域位置列表
     * @return
     */
    List<EquipmentSpaceEntity> getEquipmentSpace();

    //	 String getMAXSapceID();
    int getinsertFlag_space(String spaceId);

    /**
     * 插入space数据  插入区域数据
     * @param space
     */
    void updateSpace(EquipmentSpaceEntity space);

    /**
     * 插入space数据  插入区域数据
     * @param space
     */
    void insertSpace(EquipmentSpaceEntity space);

    /**
     * 删除space数据
     * @param spaceId
     */
    void deleteSpace(String spaceId);

    /**
     * 查询所有space
     * @return
     */
    List<EquipmentSpaceEntity> findSpace();

    List<String> getNextEquipmentID();

    /**
     * 删除设备
     * @param deviceID
     */
    void delete_device(String deviceID);

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

    /**
     * pubdevice_zj103，根据IEC61850LD_LN查询是否存在
     * @param IEC61850LD_LN
     * @return
     */
    int getZJ103CountByln(String IEC61850LD_LN);

    void updateZJ103(ZJ103Entity DeviceID);

    void insertZJ103(ZJ103Entity DeviceID);

    /**
     * 查询pubdevice_zj103最大的deviceId
     * @return
     */
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

    int updateyc(Data_instEntity entity);

    //	 void insertycList(List<Data_instEntity> ycInst);
    int getykCount(Data_instEntity entity);

    int updateyk(Data_instEntity entity);

    int insertyk(Data_instEntity entity);

    int insertSGCByk(Data_instEntity entity);

    /**
     * 对iec61850_ln_inst表进行操作,根据ld_inst_name，ln_inst_name查询设备名称是否存在
     * @param ld_inst_name
     * @param ln_inst_name
     * @return
     */
    int getLnCount(String ld_inst_name, String ln_inst_name);

    int updateLn(InstNodeEntity entity);

    int insertLn(InstNodeEntity entity);

    //根据ln获取点序号
    List<YclysEntity> getyxByld(YclysEntity entity);

    List<YclysEntity> getycByld(YclysEntity entity);

    List<YclysEntity> getykByld(YclysEntity entity);


    List<DataEntity> getYXDataRefname();

    List<String> getNextDeviceID();


}
