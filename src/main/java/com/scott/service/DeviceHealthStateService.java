package com.scott.service;

import com.base.page.BasePage;
import com.base.service.BaseService;
import com.scott.dao.DeviceHealthStateDao;
import com.scott.entity.*;
import com.scott.page.HistoryPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>JeecgPersonService<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Service("deviceHealthStateService")
public class DeviceHealthStateService<T> extends BaseService<T> {


    @Autowired
    private DeviceHealthStateDao<T> dao;


    @Override
    public DeviceHealthStateDao<T> getDao() {
        return dao;
    }

    /**
     * 查询数据油色谱类型数据实时信息
     * @return
     */
    public List<StomYxEntity> getStomDetail() {
        return getDao().getStomDetail();
    }

    public List<Sf6YxEntity> getSf6Detail() {
        return getDao().getSf6Detail();
    }

    public List<Sf6YxEntity> getSf6DetailDate() {
        return getDao().getSf6DetailDate();
    }

    public List<SmoamYxEntity> getSmoamDetail() {
        return getDao().getSmoamDetail();
    }

    public List<SmoamYxEntity> getSmoamDetailDate() {
        return getDao().getSmoamDetailDate();
    }

    public List<ScomYxEntity> getScomDetail() {
        return getDao().getScomDetail();
    }

    public List<ScomYxEntity> getScomDetailDate() {
        return getDao().getScomDetailDate();
    }

    public List<SpdmYxEntity> getSpdmDetail() {
        return getDao().getSpdmDetail();
    }

    public List<SpdmYxEntity> getSpdmDetailDate() {
        return getDao().getSpdmDetailDate();
    }

    /**
     * 根据时间查询数据油色谱类型数据实时信息
     * @param _time
     * @return
     */
    public List<StomYxEntity> getStomDetailByDate(String _time) {
        return getDao().getStomDetailByDate(_time);
    }

    public List<Sf6YxEntity> getSf6DetailByDate(String _time) {
        return getDao().getSf6DetailByDate(_time);
    }

    public List<Sf6YxEntity> getSf6DetailDateByDate(String _time) {
        return getDao().getSf6DetailDateByDate(_time);
    }

    public List<SmoamYxEntity> getSmoamDetailByDate(String _time) {
        return getDao().getSmoamDetailByDate(_time);
    }

    public List<SmoamYxEntity> getSmoamDetailDateByDate(String _time) {
        return getDao().getSmoamDetailDateByDate(_time);
    }

    public List<ScomYxEntity> getScomDetailByDate(String _time) {
        return getDao().getScomDetailByDate(_time);
    }

    public List<ScomYxEntity> getScomDetailDateByDate(String _time) {
        return getDao().getScomDetailDateByDate(_time);
    }

    public List<SpdmYxEntity> getSpdmDetailByDate(String _time) {
        return getDao().getSpdmDetailByDate(_time);
    }

    public List<SpdmYxEntity> getSpdmDetailDateByDate(String _time) {
        return getDao().getSpdmDetailDateByDate(_time);
    }

    /**
     * DetailBySpace
     *
     * @param Spaceng
     * @return
     * @
     */
    public List<StomYxEntity> getStomDetailBySpace(String Spaceng) {
        return getDao().getStomDetailBySpace(Spaceng);
    }

    public List<Sf6YxEntity> getSf6DetailBySpace(String Spaceng) {
        return getDao().getSf6DetailBySpace(Spaceng);
    }

    public List<SmoamYxEntity> getSmoamDetailBySpace(String Spaceng) {
        return getDao().getSmoamDetailBySpace(Spaceng);
    }

    public List<ScomYxEntity> getScomDetailBySpace(String Spaceng) {
        return getDao().getScomDetailBySpace(Spaceng);
    }

    public List<SpdmYxEntity> getSpdmDetailBySpace(String Spaceng) {
        return getDao().getSpdmDetailBySpace(Spaceng);
    }

    /**
     * DetailDateB
     *
     * @param Spaceng
     * @return
     * @
     */
    public List<StomYxEntity> getStomDetailDateBySpace(String Spaceng) {
        return getDao().getStomDetailDateBySpace(Spaceng);
    }

    public List<Sf6YxEntity> getSf6DetailDateBySpace(String Spaceng) {
        return getDao().getSf6DetailDateBySpace(Spaceng);
    }

    public List<SmoamYxEntity> getSmoamDetailDateBySpace(String Spaceng) {
        return getDao().getSmoamDetailDateBySpace(Spaceng);
    }

    public List<ScomYxEntity> getScomDetailDateBySpace(String Spaceng) {
        return getDao().getScomDetailDateBySpace(Spaceng);
    }

    public List<SpdmYxEntity> getSpdmDetailDateBySpace(String Spaceng) {
        return getDao().getSpdmDetailDateBySpace(Spaceng);
    }

    /**
     * 油色谱历史数据查询
     *
     * @return
     */
    public int YSPHistoryCount(BasePage page) {
        return getDao().YSPHistoryCount(page);
    }

    public List<stom_dataEntity> getYSPHistory(BasePage page) {
        //getDao().deleteStom();
        Integer rowCount = YSPHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getYSPHistory(page);
    }

    public List<stom_dataEntity> exportYSPHistory(BasePage page) {
        Integer rowCount = YSPHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().exportYSPHistory(page);
    }

    public String getDeviceName(String id) {
        return getDao().getDeviceName(id);
    }

    /**
     * SF6历史数据查询
     *
     * @return
     */
    public int SF6HistoryCount(BasePage page) {
        return getDao().SF6HistoryCount(page);
    }

    public List<Sf6_dataEntity> getSF6History(BasePage page) {
        Integer rowCount = SF6HistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getSF6History(page);
    }

    public List<Sf6_dataEntity> exportSF6History(BasePage page) {
        Integer rowCount = SF6HistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().exportSF6History(page);
    }

    /**
     * 红外测温历史数据查询
     *
     * @return
     */
    public int InfraredHistoryCount(BasePage page) {
        return getDao().InfraredHistoryCount(page);
    }

    public List<Sf6_dataEntity> getInfraredHistory(BasePage page) {
        Integer rowCount = InfraredHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getInfraredHistory(page);
    }

    public List<Sf6_dataEntity> exportInfraredHistory(BasePage page) {
        Integer rowCount = InfraredHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().exportInfraredHistory(page);
    }

    /**
     * 避雷器历史数据查询
     *
     * @return
     */
    public int SMOAMHistoryCount(BasePage page) {
        return getDao().SMOAMHistoryCount(page);
    }

    public List<SMOAM_dataEntity> getSMOAMHistory(BasePage page) {
        Integer rowCount = SMOAMHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getSMOAMHistory(page);
    }

    public List<SMOAM_dataEntity> exportSMOAMHistory(BasePage page) {
        Integer rowCount = SMOAMHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().exportSMOAMHistory(page);
    }

    /**
     * 铁芯历史数据查询
     *
     * @return
     */
    public int SCOMHistoryCount(BasePage page) {
        return getDao().SCOMHistoryCount(page);
    }

    public List<SCOM_dataEntity> getSCOMHistory(BasePage page) {
        Integer rowCount = SCOMHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getSCOMHistory(page);
    }

    public List<SCOM_dataEntity> exportSCOMHistory(BasePage page) {
        Integer rowCount = SCOMHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().exportSCOMHistory(page);
    }

    /**
     * 工况历史数据查询
     *
     * @return
     */
    public int SpdmHistoryCount(BasePage page) {
        return getDao().SpdmHistoryCount(page);
    }

    public List<Spdm_dataEntity> getSpdmHistoryData(BasePage page) {
        Integer rowCount = SpdmHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getSpdmHistoryData(page);
    }

    public List<Spdm_dataEntity> exportSpdmHistoryData(BasePage page) {
        Integer rowCount = SpdmHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().exportSpdmHistoryData(page);
    }

    public List<DeviceEntity> getDeviceByType(String DeviceType) {
        return getDao().getDeviceByType(DeviceType);
    }

    public DeviceEntity getDeviceByDeviceId(String deviceID) {
        return getDao().getDeviceByDeviceId(deviceID);
    }

    /**
     * 选择设备类型获取红外测温设备
     */
    public List<DeviceEntity> getInfraredByType() {
        return getDao().getInfraredByType();
    }

    /**
     * *用于获取曲线图中所需的SF6数据
     *
     * @return
     */
    public List<Sf6_dataEntity> getSF6Chart_history(Map<String, Object> param) {
        return getDao().getSF6Chart_history(param);
    }

    public List<Sf6_dataEntity> getSF6Chart_dealt(Map<String, Object> param) {
        return getDao().getSF6Chart_dealt(param);
    }

    /**
     * *用于获取曲线图中所需的红外测温数据
     *
     * @return
     */
    public List<Sf6_dataEntity> getInfraredChart_history(Map<String, Object> param) {
        return getDao().getInfraredChart_history(param);
    }

    /**
     * *用于获取曲线图中所需的油色谱数据
     *
     * @return
     */
    public List<stom_dataEntity> getStomChart_history(Map<String, Object> param) {
        return getDao().getStomChart_history(param);
    }

    /**
     * *用于获取曲线图中所需的避雷器数据
     *
     * @return
     */
    public List<SMOAM_dataEntity> getSmoamChart_history(Map<String, Object> param) {
        return getDao().getSmoamChart_history(param);
    }

    /**
     * *用于获取曲线图中所需的铁芯数据
     *
     * @return
     */
    public List<SCOM_dataEntity> getScomChart_history(Map<String, Object> param) {
        return getDao().getScomChart_history(param);
    }

    /**
     * *用于获取曲线图中所需的局放数据
     *
     * @return
     */
    public List<Spdm_dataEntity> getSpdmChart_history(Map<String, Object> param) {
        return getDao().getSpdmChart_history(param);
    }

    /**
     * *用于LN
     *
     * @return
     */
    public String getDeviceLN(String id) {
        return getDao().getDeviceLN(id);
    }

    public String getyxLDLN(String IEC61850LD_LN) {
        return getDao().getyxLDLN(IEC61850LD_LN);
    }

    public String getyxDesc(String refname) {
        return getDao().getyxDesc(refname);
    }

    public List<IEC61850cRealDataEntity> getYXData(String IEC61850LD_LN) {
        return getDao().getYXData(IEC61850LD_LN);
    }

    //遥信历史数据
    public int StomYXHistoryCount(BasePage page) {
        return getDao().StomYXHistoryCount(page);
    }

    public List<YXhistroyData> getstomYXHistoryData(BasePage page) {
        Integer rowCount = StomYXHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getstomYXHistoryData(page);
    }

    public int Sf6YXHistoryCount(BasePage page) {
        return getDao().Sf6YXHistoryCount(page);
    }

    public List<YXhistroyData> getSf6YXHistoryData(BasePage page) {
        Integer rowCount = Sf6YXHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getSf6YXHistoryData(page);
    }

    public int SmoamYXHistoryCount(BasePage page) {
        return getDao().SmoamYXHistoryCount(page);
    }

    public List<YXhistroyData> getSmoamYXHistoryData(BasePage page) {
        Integer rowCount = SmoamYXHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getSmoamYXHistoryData(page);
    }

    public int ScomYXHistoryCount(BasePage page) {
        return getDao().ScomYXHistoryCount(page);
    }

    public List<YXhistroyData> getScomYXHistoryData(BasePage page) {
        Integer rowCount = ScomYXHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getScomYXHistoryData(page);
    }

    public int SpdmYXHistoryCount(BasePage page) {
        return getDao().SpdmYXHistoryCount(page);
    }

    public List<YXhistroyData> getSpdmYXHistoryData(BasePage page) {
        Integer rowCount = SpdmYXHistoryCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getSpdmYXHistoryData(page);
    }

    //套管
    public List<SbushDataEntity> getSbushHistoryData(HistoryPage page) {
        Integer rowCount = getSbushHistoryDataCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getSbushHistoryData(page);
    }

    private Integer getSbushHistoryDataCount(HistoryPage page) {
        return getDao().getSbushHistoryDataCount(page);
    }

    //局放
    public List<SbushDataEntity> getSpdcHistoryData(HistoryPage page) {
        Integer rowCount = getSpdcHistoryDataCount(page);
        page.getPager().setRowCount(rowCount);
        return getDao().getSpdcHistoryData(page);
    }

    private Integer getSpdcHistoryDataCount(HistoryPage page) {
        return getDao().getSpdcHistoryDataCount(page);
    }
}
