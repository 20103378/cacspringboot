package com.scott.service;

import com.base.entity.dto.DeviceRequestDTO;
import com.base.page.BasePage;
import com.base.service.BaseService;
import com.scott.dao.SystemConfigurationDao;
import com.scott.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>JeecgPersonService<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Service("SystemConfigurationService")
public class SystemConfigurationService<T> extends BaseService<T> {


    @Autowired
    private SystemConfigurationDao<T> dao;

    @Override
    public SystemConfigurationDao<T> getDao() {
        return dao;
    }

    public List<Station_InfoEntity> getStation() {
        return getDao().getStation();
    }

    public int updateStation(Station_InfoEntity entity) {
        return getDao().updateStation(entity);
    }
    public int insertStation(Station_InfoEntity entity) {
        return getDao().insertStation(entity);
    }
    /**
     * 查询主设备数据
     *
     * @return
     */
    public int EquipmentListCount() {
        return getDao().equipmentListCount();
    }

    public List<EquipmentEntity> getEquipmentList(BasePage page) {
        Integer rowCount = EquipmentListCount();
        page.getPager().setRowCount(rowCount);
        return getDao().getEquipmentList(page);
    }
    public List<String> getEquipmentIED61850LDsList() {
        return getDao().getEquipmentIED61850LDsList();
    }
    public List<String> getDeviceIED61850LD_LNsList(String equipmentID) {
        return getDao().getDeviceIED61850LD_LNsList(equipmentID);
    }

    /**
     * 查询测量点名称
     *
     * @return
     */
    public List<Refname_descEntity> getrefname() {
        return getDao().getrefname();
    }

    ;

    public int getrefnameFlag(String refname) {
        return getDao().getrefnameFlag(refname);
    }

    ;

    public void update_refname(String refName,String refDesc) {
        getDao().update_refname(refName,refDesc);
    }

    public void add_refname(String refName,String refDesc) {
        getDao().add_refname(refName,refDesc);
    }

    public void delete_refname(String refname) {
        getDao().delete_refname(refname);
    }

    /**
     * 根据主设备ID查询设备数据
     *
     * @return
     */
    public int DeviceListCount(String equipmentID) {
        return getDao().DeviceListCount(equipmentID);
    }

    public List<DeviceEntity> getDeviceList(BasePage page,String equipmentID) {
        Integer rowCount = DeviceListCount(equipmentID);
        page.getPager().setRowCount(rowCount);
        return getDao().getDeviceList(page);
    }
    public  Integer getDeviceCountByEquipmentID(String equipmentID){
        return getDao().getDeviceCountByEquipmentID(equipmentID);
    }

    public void update_device(DeviceEntity entity) {
        getDao().update_device(entity);
    }

    public void insert_device(DeviceEntity entity) {
        getDao().insert_device(entity);
    }

    public String DeviceMaxId() {
        return getDao().DeviceMaxId();
    }

    public void update_equipment(EquipmentEntity entity) {
        getDao().update_equipment(entity);
    }

    public void add_equipment(EquipmentEntity entity) {
        getDao().add_equipment(entity);
    }
    public EquipmentEntity findEquipmentByIEC61850LD(String iec61850LD) {
        return getDao().findEquipmentByIEC61850LD(iec61850LD);
    }
    public void delete_equipment(EquipmentEntity entity) {
        getDao().delete_equipment(entity);
    }

    public List<EquipmentSpaceEntity> getEquipmentSpace() {
        return getDao().getEquipmentSpace();
    }

//    public String getMAXSapceID() {
//        return getDao().getMAXSapceID();
//    }

    public int getinsertFlag_space(String spaceId) {
        return getDao().getinsertFlag_space(spaceId);
    }

    public void updateSpace(EquipmentSpaceEntity space) {
        getDao().updateSpace(space);
    }

    public void insertSpace(EquipmentSpaceEntity space) {
        getDao().insertSpace(space);
    }

    public void deleteSpace(String spaceId) {
        getDao().deleteSpace(spaceId);
    }
    public List<EquipmentSpaceEntity> findSpace() {
        return getDao().findSpace();
    }
    public List<String> getNextEquipmentID() {
        return getDao().getNextEquipmentID();
    }

    public void delete_device(String deviceID) {
        getDao().delete_device(deviceID);
    }

    public List<Sf6AlarmEntity> getSf6Monitor(String deviceID) {
        return getDao().getSf6Monitor(deviceID);
    }

    public List<StomAlarmEntity> getStomMonitor(String deviceID) {
        return getDao().getStomMonitor(deviceID);
    }

    public List<SmoamAlarmEntity> getSmoamMonitor(String deviceID) {
        return getDao().getSmoamMonitor(deviceID);
    }

    public List<ScomAlarmEntity> getScomMonitor(String deviceID) {
        return getDao().getScomMonitor(deviceID);
    }

//    public List<SconditionAlarmEntity> getSconditionMonitor(SconditionAlarmEntity entity) {
//        return getDao().getSconditionMonitor(entity);
//    }

    public List<DeviceEntity> getCheckBox(DeviceRequestDTO deviceRequestDTO) {
        return getDao().getCheckBox(deviceRequestDTO);
    }

    //修改和插入sf6告警值
    public void updateSf6Monitor(Sf6AlarmEntity entity) {
        getDao().updateSf6Monitor(entity);
    }

    public void insertSf6Monitor(Sf6AlarmEntity entity) {
        getDao().insertSf6Monitor(entity);
    }

    //修改和插入stom告警值
    public int updateStomMonitor(StomAlarmEntity entity) {
       return getDao().updateStomMonitor(entity);
    }

    public int insertStomMonitor(StomAlarmEntity entity) {
        return getDao().insertStomMonitor(entity);
    }

    //修改和插入smoam告警值
    public void updateSmoamMonitor(SmoamAlarmEntity entity) {
        getDao().updateSmoamMonitor(entity);
    }

    public void insertSmoamMonitor(SmoamAlarmEntity entity) {
        getDao().insertSmoamMonitor(entity);
    }

    //修改和插入scom告警值
    public void updateScomMonitor(ScomAlarmEntity entity) {
        getDao().updateScomMonitor(entity);
    }

    public void insertScomMonitor(ScomAlarmEntity entity) {
        getDao().insertScomMonitor(entity);
    }

//    //修改和插入工况告警值
//    public void updateSconditionMonitor(SconditionAlarmEntity entity) {
//        getDao().updateSconditionMonitor(entity);
//    }
//
//    public void insertSconditionMonitor(SconditionAlarmEntity entity) {
//        getDao().insertSconditionMonitor(entity);
//    }

    public List<DeviceEntity> getExportList() {
        return getDao().getExportList();
    }

    //生成地图XML文件
    public List<DeviceEntity> getDeviceBySpace(String Space) {
        return getDao().getDeviceBySpace(Space);
    }

    public List<DeviceEntity> getDeviceBySpace_ft(String Space) {
        return getDao().getDeviceBySpace_ft(Space);
    }

    //查询主设备数据
    public int I2ListCount(BasePage page) {
        return getDao().I2ListCount(page);
    }

    public List<I2TableEntity> getI2Data(BasePage page) {
        Integer rowCount = I2ListCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getI2Data(page);
    }

    public void updateZJ103(ZJ103Entity DeviceID) {
        getDao().updateZJ103(DeviceID);
    }

    public void insertZJ103(ZJ103Entity DeviceID) {
        getDao().insertZJ103(DeviceID);
    }

    public int ZJ103Count(BasePage page) {
        return getDao().ZJ103Count(page);
    }

    public String ZJ103DeviceIDMax() {
        return getDao().ZJ103DeviceIDMax();
    }

    public int getZJ103CountByln(String IEC61850LD_LN) {
        return getDao().getZJ103CountByln(IEC61850LD_LN);
    }

    public List<ZJ103Entity> getI2Data_103(BasePage page) {
        Integer rowCount = ZJ103Count(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getI2Data_103(page);
    }

    public void Updata_103(ZJ103Entity DeviceID) {
        getDao().Updata_103(DeviceID);
    }

    public void submit_103_devPhase(ZJ103Entity DeviceID) {
        getDao().submit_103_devPhase(DeviceID);
    }

    public void delete_103(ZJ103Entity DeviceID) {
        getDao().delete_103(DeviceID);
    }

    //导出EXCEL
    public List<I2TableEntity> getI2Data_export(BasePage page) {
        Integer rowCount = I2ListCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getI2Data_export(page);
    }

    //查询i1表中的字段名
    public List<YcDataInstEntity> getycNameList(YcDataInstEntity entity) {
        return getDao().getycNameList(entity);
    }

    public List<YcDataInstEntity> getyxNameList(YcDataInstEntity entity) {
        return getDao().getyxNameList(entity);
    }

    public List<YcDataInstEntity> getykNameList(YcDataInstEntity entity) {
        return getDao().getykNameList(entity);
    }

    //红外测温增删改
    public Integer getInfraredFlag(InfraredTableEntity entity) {
        return getDao().getInfraredFlag(entity);
    }

    public void insertInfraredTable(InfraredTableEntity entity) {
        getDao().insertInfraredTable(entity);
    }

    public void updateInfraredTable(InfraredTableEntity entity) {
        getDao().updateInfraredTable(entity);
    }

    //增删改
    public Integer getinsertFlag(I2TableEntity entity) {
        return getDao().getinsertFlag(entity);
    }

    public void insertI2Table(I2TableEntity entity) {
        getDao().insertI2Table(entity);
    }

    public void updateI2Table(I2TableEntity entity) {
        getDao().updateI2Table(entity);
    }

    public void delete_I2(I2TableEntity entity) {
        getDao().delete_I2(entity);
    }

    //获取遥信遥测遥控的61850ln_id
    public List<YcDataInstEntity> getycIEC61850LD_LN(I2TableEntity entity) {
        return getDao().getycIEC61850LD_LN(entity);
    }

    public List<YcDataInstEntity> getyxIEC61850LD_LN(I2TableEntity entity) {
        return getDao().getyxIEC61850LD_LN(entity);
    }

    public List<YcDataInstEntity> getykIEC61850LD_LN(I2TableEntity entity) {
        return getDao().getykIEC61850LD_LN(entity);
    }

    //是否已经存在id从而进行修改或者添加操作
    public int getyxCount(Data_instEntity entity) {
        return getDao().getyxCount(entity);
    }

    public void updateyx(Data_instEntity entity) {
        getDao().updateyx(entity);
    }


    public void insertyx(Data_instEntity entity) {
        getDao().insertyx(entity);
    }

//    public void insertyxList(List<Data_instEntity> yxInst) {
//        getDao().insertyxList(yxInst);
//    }

    public int getycCount(Data_instEntity entity) {
        return getDao().getycCount(entity);
    }

    public void insertyc(Data_instEntity entity) {
        getDao().insertyc(entity);
    }
    public void updateyc(Data_instEntity entity) {
        getDao().updateyc(entity);
    }

//    public void insertycList(List<Data_instEntity> ycInst) {
//        getDao().insertycList(ycInst);
//    }

    public int getykCount(Data_instEntity entity) {
        return getDao().getykCount(entity);
    }

    public void updateyk(Data_instEntity entity) {
        getDao().updateyk(entity);
    }

    public void insertyk(Data_instEntity entity) {
        getDao().insertyk(entity);
    }

    public void insertSGCByk(Data_instEntity entity) {
        getDao().insertSGCByk(entity);
    }

    //对ln表进行操作
    public int getLnCount(String ld_inst_name, String ln_inst_name) {
        return getDao().getLnCount(ld_inst_name,ln_inst_name);
    }

    public void updateLn(InstNodeEntity entity) {
        getDao().updateLn(entity);
    }

    public void insertLn(InstNodeEntity entity) {
        getDao().insertLn(entity);
    }

    //根据ln获取点序号
    public List<YclysEntity> getyxByld(YclysEntity entity) {
        return getDao().getyxByld(entity);
    }

    public List<YclysEntity> getycByld(YclysEntity entity) {
        return getDao().getycByld(entity);
    }

    public List<YclysEntity> getykByld(YclysEntity entity) {
        return getDao().getykByld(entity);
    }

    public List<String> getYXDataRefname() {
        return getDao().getYXDataRefname();
    }

    public List<String> getNextDeviceID() {
        return getDao().getNextDeviceID();
    }


}
