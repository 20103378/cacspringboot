package com.scott.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.entity.BaseEntity;
import com.base.entity.PubDeviceTypeEnum;
import com.base.entity.dto.*;
import com.base.page.BasePage;
import com.base.util.HtmlUtil;
import com.base.util.edit.BeanUtil;
import com.base.util.edit.ICDUtils;
import com.base.util.UrlUtil;
import com.base.util.excel.RefNameCell;
import com.base.util.excel.ExcelUtils;
import com.base.web.BaseAction;
import com.scott.entity.*;
import com.scott.page.DevicePage;
import com.scott.service.LEDConfigurationService;
import com.scott.service.SystemConfigurationService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.Boolean;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <br>
 * <b>功能：</b>JeecgPersonController<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Controller
@RequestMapping("/systemConfiguration")
public class SystemConfigurationController extends BaseAction {

    // Servrice start
    @Autowired(required = false)
    // 自动注入，不需要生成set方法了，required=false表示没有实现类，也不会报错。
    private SystemConfigurationService<BaseEntity> systemConfigurationService;
    @Autowired(required = false)
    private LEDConfigurationService<TreeDeviceEntity> LEDService;

    @RequestMapping("/list")
    public String list() {
        return "scott/demo/systemConfiguration";
    }

    /**
     * 获取站点数据
     */
    @RequestMapping("/getStation")
    @ResponseBody
    public Station_InfoEntity getStation() {
        List<Station_InfoEntity> dataList = systemConfigurationService.getStation();
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        return dataList.get(0);
    }

    /**
     * 修改站点数据
     */
    @RequestMapping("/updateStation")
    @ResponseBody
    public boolean updateStation(Station_InfoEntity entity) {
        if (entity.getId() != null) {
            systemConfigurationService.updateStation(entity);
        } else {
            systemConfigurationService.insertStation(entity);
        }
        return true;
    }

    /**
     * 插入space区域数据
     */
    @RequestMapping(value = "/insertSpace", produces = "application/json;charset=utf-8")
    @ResponseBody
    public void insertSpace(EquipmentSpaceEntity space) {
        int insertFlag = systemConfigurationService.getinsertFlag_space(space.getSpaceId());
        if (insertFlag > 0)
            systemConfigurationService.updateSpace(space);
        else
            systemConfigurationService.insertSpace(space);
    }

    /**
     * 删除space区域数据
     */
    @RequestMapping("/deleteSpace")
    @ResponseBody
    public void deleteSpace(String spaceId) {
        systemConfigurationService.deleteSpace(spaceId);
    }

    /**
     * 获取space区域
     *
     * @return
     */
    @RequestMapping("/getAllSpace")
    @ResponseBody
    public Map getAllSpace() throws Exception {
        Map<String, Object> map = new HashMap<>(4);
        //区域位置
        map.put("space", systemConfigurationService.findSpace());
        //相位
        List phase = new ArrayList();
        phase.add("无相别");
        phase.add("A相别");
        phase.add("B相别");
        phase.add("C相别");
        map.put("phase", phase);
        //主设备类型
        List<Integer> values = new ArrayList<>();
        values.add(2);
        values.add(1);
        values.add(3);
        values.add(4);
        values.add(5);
        values.add(6);
        values.add(7);
        List<Map> deviceTypes = PubDeviceTypeEnum.getDeviceTypeEnumsByValues(values);
        map.put("deviceTypes", deviceTypes);


        //LDevice编码
        Map<String, List> lDeviceMap = ICDUtils.getLdLnMap();
        map.put("lDeviceMap", lDeviceMap);
        map.put("LDDevices", lDeviceMap.keySet());
        return map;
    }

    /**
     * IED接入配置，测量量映射配置跳转点
     *
     * @param response
     * @param request
     */

    @RequestMapping("/getIcdExistFlag")
    @ResponseBody
    public Boolean getIcdExistFlag(String dIRName) {
        File file = new File(UrlUtil.getUrlUtil().getOsicfg() + dIRName + File.separator);
        // 如果文件夹不存在则直接返回
        if (!file.exists() && !file.isDirectory()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 测量量映射配置
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    @RequestMapping("/getSelXml")
    @ResponseBody
    public Map getSelXml(String fileName) throws Exception {
        // 创建遥测量映射对象列表
        List<YclysEntity> entityList = ICDUtils.getSelXml(fileName);
        List<YclysEntity> icdData = entityList.stream().filter(s -> !s.getTypeName().equals("q") && !s.getTypeName().equals("t")).collect(Collectors.toList());
        Map map = new HashMap(16);
        map.put("icd_data", icdData);
        map.put("ap_name", icdData.get(0).getApName());

        Set<String> lnData = new HashSet<>(16);
        Map<String, List<YclysEntity>> ldInstMap = new HashMap<>();


        for (YclysEntity icd : icdData) {
            List<YclysEntity> tmpList = ldInstMap.get(icd.getLdinst());
            if (tmpList == null) {
                tmpList = new ArrayList<>();
            }
            tmpList.add(icd);
            ldInstMap.put(icd.getLdinst(), tmpList);
            lnData.add(icd.getLnType() + "(" + icd.getLnClass() + "," + icd.getLninst() + ")");
        }

        map.put("ld_data", ldInstMap.keySet());
        map.put("ldInstMap", ldInstMap);
        map.put("ln_data", lnData);

        Set<String> fcData = new HashSet<>(16);
        fcData.add("ST");
        fcData.add("MX");
        fcData.add("SG");
        fcData.add("CO");
        map.put("fc_data", fcData);
        return map;
    }

    /**
     * 获取主设备IED61850LDs数据
     */
    @RequestMapping("/getDeviceIED61850LD_LNsList")
    @ResponseBody
    public List getDeviceIED61850LD_LNsList(String equipmentID, String[] iec61850LD_LNs) {
        List ldlnDevices = new ArrayList();
        for (int i = 0; i < iec61850LD_LNs.length; i++) {
            ldlnDevices.add(iec61850LD_LNs[i]);
        }
        List IED61850LDLNs = systemConfigurationService.getDeviceIED61850LD_LNsList(equipmentID);
        ldlnDevices.removeAll(IED61850LDLNs);
        return ldlnDevices;
    }

    /**
     * 获取主设备IED61850LDs数据
     */
    @RequestMapping("/getEquipmentIED61850LDsList")
    @ResponseBody
    public List getEquipmentIED61850LDsList(String[] LDDevices) {
//        List ldDevices =  CollectionUtils.arrayToList(LDDevices);
        List ldDevices = new ArrayList();
        for (int i = 0; i < LDDevices.length; i++) {
            ldDevices.add(LDDevices[i]);
        }
        List IED61850LDs = systemConfigurationService.getEquipmentIED61850LDsList();
        ldDevices.removeAll(IED61850LDs);
        return ldDevices;
    }

    /**
     * 获取主设备数据
     */
    @RequestMapping("/getEquipmentList")
    @ResponseBody
    public Map<String, Object> getEquipmentList(BasePage page) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<EquipmentEntity> dataList = systemConfigurationService.getEquipmentList(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        return jsonMap;
    }

    /**
     * 获取设备数据
     */
    @RequestMapping("/getDeviceList")
    @ResponseBody
    public Map<String, Object> getDeviceList(DevicePage page) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<DeviceEntity> dataList = systemConfigurationService.getDeviceList(page, page.getEquipmentID());
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        return jsonMap;
    }

    /**
     * 更新设备数据
     */
    @RequestMapping("/update_device")
    @ResponseBody
    public void update_device(DeviceEntity entity) {
        systemConfigurationService.update_device(entity);
    }

    /**
     * 插入设备数据
     */
    @RequestMapping("/insert_device")
    @ResponseBody
    public void insert_device(DeviceEntity entity) {
        String a = systemConfigurationService.DeviceMaxId();
        String id = null;
        if (a != null && !("".equals(a))) {
            int idint = Integer.parseInt(a.substring(1)) + 1;
            if (idint < 10) {
                id = "D000" + idint;
            } else if (idint < 100) {
                id = "D00" + idint;
            } else if (idint < 1000) {
                id = "D0" + idint;
            } else {
                id = "D" + idint;
            }
        } else {
            id = "D0001";
        }
        entity.setDeviceID(id);
        systemConfigurationService.insert_device(entity);
    }

    /**
     * 获取测量点
     */
    @RequestMapping("/getrefname")
    @ResponseBody
    public Map getrefname(BasePage page) {
        List<Refname_descEntity> dataList = systemConfigurationService.getrefname(page);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        return jsonMap;
    }

    /**
     * 添加/修改 测量点
     */
    @RequestMapping("/add_refname")
    @ResponseBody
    public Boolean add_refname(Refname_descEntity entity) {
        int F = systemConfigurationService.getrefnameFlag(entity.getRefname());
        if (F > 0) {
            systemConfigurationService.update_refname(entity.getRefname(), entity.getRefdesc());
        } else {
            systemConfigurationService.add_refname(entity.getRefname(), entity.getRefdesc());
        }
        return true;
    }

    /**
     * 删除测量点
     */
    @RequestMapping("/delete_refname")
    @ResponseBody
    public Boolean delete_refname(String refname) {
        systemConfigurationService.delete_refname(refname);
        return true;
    }

    /**
     * 修改主设备信息
     */
    @RequestMapping("/update_equipment")
    @ResponseBody
    public void update_equipment(EquipmentEntity entity) {
        EquipmentEntity equipmentEntity = systemConfigurationService.findEquipmentByIEC61850LD(entity.getIec61850LD());
        if (equipmentEntity == null) {
            systemConfigurationService.add_equipment(entity);
        } else {
            systemConfigurationService.update_equipment(entity);
        }
    }


    /**
     * 删除主设备信息
     */
    @RequestMapping("/delete_equipment")
    @ResponseBody
    public String delete_equipment(EquipmentEntity entity) {
        Integer count = systemConfigurationService.getDeviceCountByEquipmentID(entity.getEquipmentID());
        if (count != null && count > 0) {
            return "请先删除逻辑主设备下的装置";
        } else {
            systemConfigurationService.delete_equipment(entity);
            return "逻辑主设备删除完成";
        }
    }

    /**
     * 获取主设备区域
     */
    @RequestMapping("/getEquipmentSpace")
    @ResponseBody
    public List getEquipmentSpace() {
        List<EquipmentSpaceEntity> dataList = systemConfigurationService.getEquipmentSpace();
        return dataList;
    }


    /**
     * 获取下一个主设备ID
     */
    @RequestMapping("/getNextEquipmentID")
    @ResponseBody
    public List<String> getNextEquipmentID() {
        List<String> dataList = systemConfigurationService.getNextEquipmentID();
        return dataList;
    }

    /**
     * @return
     */
    @RequestMapping("/getNextDeviceID")
    @ResponseBody
    public List<String> getNextDeviceID() {
        List<String> dataList = systemConfigurationService.getNextDeviceID();
        return dataList;
    }

    /**
     * 删除设备
     */
    @RequestMapping("/delete_device")
    @ResponseBody
    public void delete_device(String deviceID) {
        systemConfigurationService.delete_device(deviceID);
    }


    /**
     * 获取油色谱压力告警信息1
     */
    @RequestMapping("/getStomMonitor")
    @ResponseBody
    public List getStomMonitor(String deviceID) {
        List<StomAlarmEntity> list = systemConfigurationService.getStomMonitor(deviceID);
        List<BaseMonitorEntity> list2 = new ArrayList<BaseMonitorEntity>();
        String[] arrName = {"氢气上限值(ppm)", "乙炔上限值(ppm)", "总烃上限值(ppm)",
                "甲烷上限值(ppm)", "一氧化碳上限值(ppm)", "二氧化碳上限值(ppm)",
                "氧气上限值(ppm)", "乙烯上限值(ppm)", "乙烷上限值(ppm)",
//                "氢气变化率(ppm/day)", "乙炔变化率(ppm/5day)", "总烃变化率(ppm/day)",
                "微水上限值(mg/m3)"};
        if (list.size() != 0) {
            Double[] arrValue = {
                    list.get(0).getH2ThresHold(),
                    list.get(0).getC2h2ThresHold(),
                    list.get(0).getThThresHold(),
                    list.get(0).getCh4ThresHold(),
                    list.get(0).getCoThresHold(),
                    list.get(0).getCo2ThresHold(),
                    list.get(0).getO2ThresHold(),
                    list.get(0).getC2h4ThresHold(),
                    list.get(0).getC2h6ThresHold(),
                    list.get(0).getMstThresHold()
            };
            for (int i = 0; i < arrValue.length; i++) {
                BaseMonitorEntity e = new BaseMonitorEntity();
                e.setStrName(arrName[i]);
                e.setStrValue(arrValue[i]);
                list2.add(e);
            }
        } else {
            for (int i = 0; i < arrName.length; i++) {
                BaseMonitorEntity e = new BaseMonitorEntity();
                e.setStrName(arrName[i]);
                e.setStrValue(null);
                list2.add(e);
            }
        }
        return list2;
    }

    /**
     * 获取SF6压力告警信息2
     */
    @RequestMapping("/getSf6Monitor")
    @ResponseBody
    public List getSf6Monitor(Sf6AlarmEntity entity) {
        List<Sf6AlarmEntity> list = systemConfigurationService
                .getSf6Monitor(entity.getDeviceID());
        List<BaseMonitorEntity> list2 = new ArrayList<BaseMonitorEntity>();
        String[] arrName = {"sf6气体压力阈值(MPa)", "sf6气体压力变化率阈值(MPa/day)", "sf6气体压力变化率阈值(MPa/week)"};
        if (list.size() != 0) {
            Double[] arrValue = {
                    list.get(0).getPressureThreshold(),
                    list.get(0).getPressChgRateThreshold(),
                    list.get(0).getWeekChgRateThreshold()
            };

            for (int i = 0; i < arrName.length; i++) {
                BaseMonitorEntity e = new BaseMonitorEntity();
                e.setStrName(arrName[i]);
                e.setStrValue(arrValue[i]);
                list2.add(e);
            }
        } else {
            for (int i = 0; i < arrName.length; i++) {
                BaseMonitorEntity e = new BaseMonitorEntity();
                e.setStrName(arrName[i]);
                e.setStrValue(null);
                list2.add(e);
            }
        }
        return list2;
    }

    /**
     * 获取避雷器告警信息 3
     */
    @RequestMapping("/getSmoamMonitor")
    public List getSmoamMonitor(SmoamAlarmEntity entity) {
        List<SmoamAlarmEntity> list = systemConfigurationService
                .getSmoamMonitor(entity.getDeviceID());
        List<BaseMonitorEntity> list2 = new ArrayList<BaseMonitorEntity>();
        String[] arrName = {"泄漏电流上限(mA)"};
        if (list.size() != 0) {
            Double[] arrValue = {list.get(0).getTotAThresHold()};
            for (int i = 0; i < arrName.length; i++) {
                BaseMonitorEntity e = new BaseMonitorEntity();
                e.setStrName(arrName[i]);
                e.setStrValue(arrValue[i]);
                list2.add(e);
            }
        } else {
            for (int i = 0; i < arrName.length; i++) {
                BaseMonitorEntity e = new BaseMonitorEntity();
                e.setStrName(arrName[i]);
                e.setStrValue(null);
                list2.add(e);
            }
        }
        return list2;
    }

    /**
     * 获取铁芯告警信息 4
     */
    @RequestMapping("/getScomMonitor")
    public List getScomMonitor(ScomAlarmEntity entity) {
        List<ScomAlarmEntity> list = systemConfigurationService
                .getScomMonitor(entity.getDeviceID());
        List<BaseMonitorEntity> list2 = new ArrayList<BaseMonitorEntity>();
        String[] arrName = {"本体主油箱压力式油位上限(mA)", "本体主油箱压力式油位下限(mA)", "阀侧首端套管SF6压力下限(mA)"
                , "阀侧末端套管SF6压力下限(mA)", "网侧绕组温度上限(mA)", "顶层油温2上限(mA)", "顶层油温1上限(mA)"
                , "本体主油箱磁力式油位上限(mA)", "本体主油箱磁力式油位下限(mA)", "有载开关油位上限(mA)", "有载开关油位下限(mA)"};
        if (list.size() != 0) {
            Double[] arrValue = {list.get(0).getPreOilUpThresHold(),
                    list.get(0).getPreOilDownThresHold(),
                    list.get(0).getPre1ThresHold(),
                    list.get(0).getPre2ThresHold(),
                    list.get(0).getTmp1ThresHold(),
                    list.get(0).getTmp3ThresHold(),
                    list.get(0).getTmp4ThresHold(),
                    list.get(0).getMainOilUpThresHold(),
                    list.get(0).getMainOilDownThresHold(),
                    list.get(0).getSltcOilUpThresHold(),
                    list.get(0).getSltcOilDownThresHold()
            };

            for (int i = 0; i < arrName.length; i++) {
                BaseMonitorEntity e = new BaseMonitorEntity();
                e.setStrName(arrName[i]);
                e.setStrValue(arrValue[i]);
                list2.add(e);
            }
        } else {
            for (int i = 0; i < arrName.length; i++) {
                BaseMonitorEntity e = new BaseMonitorEntity();
                e.setStrName(arrName[i]);
                e.setStrValue(null);
                list2.add(e);
            }
        }
        return list2;
    }


    /**
     * 获取复选框信息，将告警设置应用到同类装置，
     */
    @RequestMapping("/getCheckBox")
    @ResponseBody
    public List getCheckBox(DeviceRequestDTO deviceRequestDTO) {
        List<DeviceEntity> dataList = systemConfigurationService.getCheckBox(deviceRequestDTO);
        return dataList;
    }


    /**
     * 获取油色谱压力告警ID列表--type-1
     */
    @RequestMapping("/getStomMonitorID")
    @ResponseBody
    public List getStomMonitorID(String deviceID) {
        List<StomAlarmEntity> list = systemConfigurationService
                .getStomMonitor(deviceID);
        return list;
    }

    /**
     * 获取SF6压力告警ID列表 type-2
     */
    @RequestMapping("/getSf6MonitorID")
    @ResponseBody
    public List getSf6MonitorID(String deviceID) {
        List<Sf6AlarmEntity> list = systemConfigurationService
                .getSf6Monitor(deviceID);
        return list;
    }

    /**
     * 获取避雷器告警ID列表 type-3
     */
    @RequestMapping("/getSmoamMonitorID")
    @ResponseBody
    public List getSmoamMonitorID(String deviceID) {
        List<SmoamAlarmEntity> list = systemConfigurationService
                .getSmoamMonitor(deviceID);
        return list;
    }

    /**
     * 获取铁芯告警ID列表 type-4
     */
    @RequestMapping("/getScomMonitorID")
    @ResponseBody
    public List getScomMonitorID(String deviceID) {
        List<ScomAlarmEntity> list = systemConfigurationService
                .getScomMonitor(deviceID);
        return list;
    }

    /**
     * 修改Stom告警信息1
     */
    @RequestMapping("/updateStomMonitor")
    @ResponseBody
    public Boolean updateStomMonitor(StomAlarmRequestDTO stomAlarmRequestDTO) {
        StomAlarmEntity entity = BeanUtil.stomAlarmRequestDTOToStomAlarmEntity(stomAlarmRequestDTO);
        return systemConfigurationService.updateStomMonitor(entity) > 0;
    }

    /**
     * 插入Stom告警信息1
     */
    @RequestMapping("/insertStomMonitor")
    @ResponseBody
    public Boolean insertStomMonitor(StomAlarmRequestDTO stomAlarmRequestDTO) {
        StomAlarmEntity entity = BeanUtil.stomAlarmRequestDTOToStomAlarmEntity(stomAlarmRequestDTO);
        return systemConfigurationService.insertStomMonitor(entity) > 0;

    }

    /**
     * 修改SF6告警信息2
     */
    @RequestMapping("/updateSf6Monitor")
    public void updateSf6Monitor(Sf6AlarmRequestDTO sf6AlarmRequestDTO) {
        Sf6AlarmEntity entity = BeanUtil.sf6AlarmRequestDTOToStomAlarmEntity(sf6AlarmRequestDTO);
        systemConfigurationService.updateSf6Monitor(entity);
    }

    /**
     * 插入SF6告警信息2
     */
    @RequestMapping("/insertSf6Monitor")
    public void insertSf6Monitor(Sf6AlarmRequestDTO sf6AlarmRequestDTO) {
        Sf6AlarmEntity entity = BeanUtil.sf6AlarmRequestDTOToStomAlarmEntity(sf6AlarmRequestDTO);
        systemConfigurationService.insertSf6Monitor(entity);
    }

    /**
     * 修改smoam告警信息 3
     */
    @RequestMapping("/updateSmoamMonitor")
    public void updateSmoamMonitor(SmoamAlarmRequestDTO stomAlarmRequestDTO) {
        SmoamAlarmEntity entity = BeanUtil.smoamAlarmRequestDTOToSmoamAlarmEntity(stomAlarmRequestDTO);
        systemConfigurationService.updateSmoamMonitor(entity);
    }

    /**
     * 插入smoam告警信息3
     */
    @RequestMapping("/insertSmoamMonitor")
    public void insertSmoamMonitor(SmoamAlarmRequestDTO stomAlarmRequestDTO) {
        SmoamAlarmEntity entity = BeanUtil.smoamAlarmRequestDTOToSmoamAlarmEntity(stomAlarmRequestDTO);
        systemConfigurationService.insertSmoamMonitor(entity);

    }

    /**
     * 修改scom告警信息4
     */
    @RequestMapping("/updateScomMonitor")
    public void updateScomMonitor(ScomAlarmRequestDTO scomAlarmRequestDTO) {
        ScomAlarmEntity entity = BeanUtil.scomAlarmRequestDTOToScomAlarmEntity(scomAlarmRequestDTO);
        systemConfigurationService.updateScomMonitor(entity);
    }

    /**
     * 插入Scom告警信息4
     */
    @RequestMapping("/insertScomMonitor")
    public void insertScomMonitor(ScomAlarmRequestDTO scomAlarmRequestDTO) {
        ScomAlarmEntity entity = BeanUtil.scomAlarmRequestDTOToScomAlarmEntity(scomAlarmRequestDTO);
        systemConfigurationService.insertScomMonitor(entity);
    }

    /**
     * 获取设备数据
     */
    @RequestMapping("/getExportList")
    @ResponseBody
    public Map getExportList() {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<DeviceEntity> dataList = systemConfigurationService.getExportList();
        jsonMap.put("dataList", dataList);
        return jsonMap;
    }

    /**
     * 获取i1toi2_data_inst表数据
     */
    @RequestMapping("/getI2Data")
    @ResponseBody
    public Map getI2Data(BasePage page) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<I2TableEntity> dataList = systemConfigurationService.getI2Data(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        return jsonMap;
    }


    @RequestMapping("/getIEC61850LD_LN")
    @ResponseBody
    public Map getIEC61850LD_LN(I2TableEntity entity) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String str_i1type = entity.getI1type();
        List<YcDataInstEntity> dataList = null;
        if (str_i1type.equals("1")) {
            dataList = systemConfigurationService.getycIEC61850LD_LN(entity);
        } else if (str_i1type.equals("2")) {
            dataList = systemConfigurationService.getyxIEC61850LD_LN(entity);
        } else if (str_i1type.equals("3")) {
            dataList = systemConfigurationService.getykIEC61850LD_LN(entity);
        }
        jsonMap.put("rows", dataList);
        return jsonMap;
    }


    @RequestMapping("/updataXml")//todo
    @ResponseBody
    public List updataXml(String iedName, String jsonList) {
        JSONArray jsonArray = JSON.parseArray(jsonList);
        if (jsonArray.size() == 0) {
            return null;
        }
        List<YclysEntity> list = new ArrayList<YclysEntity>();
        for (int i = 0; i < jsonArray.size(); i++) {
            for (int k = 0; k < 3; k++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                YclysEntity temp_entity = new YclysEntity();
                temp_entity.setSel_dxh(jsonObj.get("dxh").toString());
                temp_entity.setSel_zh(jsonObj.get("dh").toString());
                if (k == 0) {
                    temp_entity.setLnType(jsonObj.get("type").toString());
                }
                if (k == 1) {

                    if ("MX".equals(jsonObj.get("type").toString().split("[$]")[1])) {
                        temp_entity.setLnType(jsonObj.get("type").toString().replace("mag$f", "q"));
                        // temp_entity.setLnType(str.substring(0,str.indexOf("[$]",2)+1)+"q");
                    } else {
                        temp_entity.setLnType(jsonObj.get("type").toString().replace("stVal", "q"));
                    }
                    System.out.println(jsonObj.get("type").toString().split("\\$")[1]);

                }
                if (k == 2) {
                    if ("MX".equals(jsonObj.get("type").toString().split("[$]")[1])) {
                        temp_entity.setLnType(jsonObj.get("type").toString().replace("mag$f", "t"));
                        // temp_entity.setLnType(str+str.substring(str.indexOf("[$]",2)+1)+"t");
                    } else {
                        temp_entity.setLnType(jsonObj.get("type").toString().replace("stVal", "t"));
                    }
                }
                temp_entity.setIedName(jsonObj.get("iedName").toString());
                list.add(temp_entity);
            }

        }
        String dirName = list.get(0).getIedName();
        int meas_flag = dirName.indexOf(iedName);

        if (meas_flag > -1) {
            // 将list注入cfg文件 写入配置文件
            String path = UrlUtil.getUrlUtil().getOsicfg() + iedName;
            ICDUtils.readCfgFile(path, list);
        } else {
            list.removeAll(list);
        }
        return list;
    }

    // 根据startup.cfg获取文件名
    private String getXmlName(String dir) {
        String fileName = "";
        String path = dir + "/startup.cfg";
        try {
            File file = new File(path);
            InputStreamReader read = new InputStreamReader(new FileInputStream(
                    file), "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("SCLFileName") > -1) {
                    fileName = "/" + line.substring(line.indexOf("\t") + 1);
                    System.out.println(fileName);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 从ied文件获取节点信息列表
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    @RequestMapping("/getJdList")
    @ResponseBody
    public List getJdList(String fileName) throws Exception {
        // 创建遥测量映射对象列表
        List<InstNodeEntity> entityList = new ArrayList<InstNodeEntity>();
        // 文档加载开始
        String xmlName = getXmlName(UrlUtil.getUrlUtil().getOsicfg() + fileName);
        SAXBuilder bulider = new SAXBuilder();
        InputStream inSt = new FileInputStream(UrlUtil.getUrlUtil().getOsicfg() + fileName + xmlName);
        Document document = bulider.build(inSt);
        // 文档创建完成,开始解析文档到遥测量映射列表中
        Element root = document.getRootElement(); // 获取根节点对象
        Namespace ns = root.getNamespace();
        String ied_name = "";
        String ied_desc = "";
        String ld_inst_name = "";
        String ld_inst_desc = "";
        String ln_inst_name = "";
        String ln_inst_desc = "";
        List<Element> IEDList = root.getChildren("IED", ns);
        ied_name = IEDList.get(0).getAttributeValue("name");
        ied_desc = IEDList.get(0).getAttributeValue("desc");
        for (Element el : IEDList) {
            Element AccessPoint = el.getChild("AccessPoint", ns);
            Element Server = AccessPoint.getChild("Server", ns);
            List<Element> LDevice = Server.getChildren("LDevice", ns);
            for (Element el_ld : LDevice) {
                // 获取ldname和desc
                ld_inst_name = ied_name + el_ld.getAttributeValue("inst");
                ld_inst_desc = el_ld.getAttributeValue("desc");
                List<Element> lnList = el_ld.getChildren("LN", ns);
                // 开始遍历每个LN
                for (Element el2 : lnList) {
                    ln_inst_desc = el2.getAttributeValue("desc");
                    ln_inst_name = el2.getAttributeValue("lnClass")
                            + el2.getAttributeValue("inst");
                    InstNodeEntity ent = new InstNodeEntity();
                    ent.setIed_desc(ied_desc);
                    ent.setIed_name(ied_name);
                    ent.setLd_inst_desc(ld_inst_desc);
                    ent.setLd_inst_name(ld_inst_name);
                    ent.setLn_inst_desc(ln_inst_desc);
                    ent.setLn_inst_name(ln_inst_name);
                    entityList.add(ent);
                }
            }
        }
        return entityList;
    }


    /**
     * 同步节点信息到数据库
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("/JdListTODB")
    @ResponseBody
    public void JdListTODB(String list) {
        JSONArray jsonArray = JSON.parseArray(list);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            InstNodeEntity temp_entity = new InstNodeEntity();
            temp_entity.setIed_name(jsonObj.get("ied_name").toString());
            temp_entity.setIed_desc(jsonObj.get("ied_desc").toString().replace('△', 'D'));
            temp_entity.setLd_inst_name(jsonObj.get("ld_inst_name").toString());
            temp_entity.setLd_inst_desc(jsonObj.get("ld_inst_desc").toString());
            temp_entity.setLn_inst_name(jsonObj.get("ln_inst_name").toString());
            temp_entity.setLn_inst_desc(jsonObj.get("ln_inst_desc") == null ? null : jsonObj.get("ln_inst_desc").toString());

            int ii = 1;
            String i_Device = systemConfigurationService.ZJ103DeviceIDMax();
            if (i_Device != null) {
                ii = Integer.parseInt(i_Device.substring(1, 5)) + 1;
            }
            String deviceID = null;
            if (ii < 10) {
                deviceID = "D000" + ii;
            } else if (ii < 100) {
                deviceID = "D00" + ii;
            } else if (ii < 1000) {
                deviceID = "D0" + ii;
            } else if (ii < 10000) {
                deviceID = "D" + ii;
            } else {
                deviceID = "D" + ii;
            }
            ZJ103Entity ZJ103Entity = new ZJ103Entity();
            ZJ103Entity.setIEC61850LD_LN(temp_entity.getLd_inst_name() + "/" + temp_entity.getLn_inst_name());
            ZJ103Entity.setDeviceID(deviceID);
            // 更新至数据库
            int count = systemConfigurationService.getLnCount(temp_entity.getLd_inst_name(), temp_entity.getLn_inst_name());
            int count_103 = systemConfigurationService.getZJ103CountByln(ZJ103Entity.getIEC61850LD_LN());
            if (count > 0) {
                // 已存在id则修改
                systemConfigurationService.updateLn(temp_entity);
            } else {
                systemConfigurationService.insertLn(temp_entity);
            }
            if (count_103 > 0) {
                // 已存在id则修改
                systemConfigurationService.updateZJ103(ZJ103Entity);
            } else {
                systemConfigurationService.insertZJ103(ZJ103Entity);
            }
        }
    }

    /**
     * 测量量映射配置同步到数据库
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("/updataDB")
    @ResponseBody
    public Boolean updataDB(String list) {
        JSONArray jsonArray = JSON.parseArray(list);
        if (jsonArray.size() == 0) {
            return true;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Data_instEntity temp_entity = new Data_instEntity();
//            // 这里的dh其实是组号,dxh是点序号
//            int _zh = Integer.parseInt(jsonObj.get("dh").toString());
//            int _dxh = Integer.parseInt(jsonObj.get("dxh").toString());
//            // 过滤相同点序号
//            if (i == 0) { } else if (jsonArray.getJSONObject(i).get("dxh")
//                    .equals(jsonArray.getJSONObject(i - 1).get("dxh"))) {
//                continue;
//            }
//            // yxid算法
//            temp_entity.setYx_id((_zh * 2048) + _dxh);
            temp_entity.setLd_inst_name(jsonObj.get("iedName").toString());
            // 截取ln
            String type = jsonObj.get("type").toString();
            String desc = "";
            if (jsonObj.get("desc") != null) {
                desc = jsonObj.get("desc").toString();
            }
            String[] arr_type = type.split("\\$");
            temp_entity.setLn_inst_name(arr_type[0]);
            temp_entity.setYx_refname(arr_type[2]);
            temp_entity.setDesc(desc);
            temp_entity.setIed_type_id(type);
            // //是否已经存在id从而进行修改或者添加操作
            if (("ST").equals(arr_type[1])) {
                // st则对yx表进行操作
                int count = systemConfigurationService.getyxCount(temp_entity);
                if (count > 0) {
                    // 已存在id则修改
                    systemConfigurationService.updateyx(temp_entity);
                } else {
                    systemConfigurationService.insertyx(temp_entity);
                }
            }

            if (("MX").equals(arr_type[1])) {
                // st则对yx表进行操作
                int count = systemConfigurationService.getycCount(temp_entity);
                if (count > 0) {
                    // 已存在id则修改
                    systemConfigurationService.updateyc(temp_entity);
                } else {
                    systemConfigurationService.insertyc(temp_entity);
                }
            }
            if (("SG").equals(arr_type[1]) || ("SE").equals(arr_type[1]) || ("SP").equals(arr_type[1]) || ("CO").equals(arr_type[1])) {
                temp_entity.setFc(arr_type[1]);
                if (arr_type[1].equals("SP")) {
                    temp_entity.setYx_refname(arr_type[3]);
                } else if (arr_type[1].equals("CO")) {
                    temp_entity.setYx_refname(type);
                }
                // st则对yx表进行操作
                int count = systemConfigurationService.getykCount(temp_entity);
                if (count > 0) {
                    // 已存在id则修改
                    systemConfigurationService.updateyk(temp_entity);
                } else {
                    systemConfigurationService.insertyk(temp_entity);
                }

            }
        }
        return true;
    }

    @RequestMapping("/exportCfg")
    public void exportCfg(HttpServletResponse response,
                          HttpServletRequest request) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Content-disposition",
                "attachment;filename = clm.txt");
        response.setContentType("text/plain");
        // 用流读取放到字符串里面
        PrintWriter pw = response.getWriter();
        String cfg = "/datamap.cfg";
        String path = UrlUtil.getUrlUtil().getOsicfg()
                + request.getParameter("dirName").toString()
                + cfg;
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(path);
            FileInputStream fs = new FileInputStream(file);
            InputStreamReader read = new InputStreamReader(fs, "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                pw.println(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pw.close();
    }


    /**
     * IED接入配置，导入icd文件
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadIcd")
    @ResponseBody
    public String uploadIcd(HttpServletRequest request) throws Exception {
        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        Iterator<String> iterator = req.getFileNames();
        InputStream is = null;
        String dir_iedName = "";
        String dir_ip = "";
        String fileName;
        while (iterator.hasNext()) {
            MultipartFile file = req.getFile(iterator.next());
            is = file.getInputStream();// 上传文件流
            fileName = file.getOriginalFilename();
            BufferedReader in2 = new BufferedReader(new InputStreamReader(is, "utf-8"));
            System.out.println("in2:" + in2);
            String y = "";
            List<String> str_output = new ArrayList<String>();
            while ((y = in2.readLine()) != null) {// 一行一行读

                if (y.indexOf("iedName") > -1) {
                    String a = y.substring(y.indexOf("iedName=\"") + 9, y.indexOf("\"", y.indexOf("iedName=\"") + 9));
                    dir_iedName = a;
                    System.out.println("dir_iedName:" + dir_iedName);

                } else {
                    if (y.indexOf("<IED") > -1) {
                        String a = y.substring(y.indexOf("name=\"") + 6, y.indexOf("\"", y.indexOf("name=\"") + 6));
                        dir_iedName = a;
                        System.out.println("dir_iedName:" + dir_iedName);
                    }
                }

                if (y.indexOf("type=\"IP\"") > -1) {
                    int start = y.indexOf(">", y.indexOf("<P") + 1) + 1;
                    int end = y.indexOf("<", y.indexOf("<P") + 1);
                    String a = y.substring(start, end);
                    dir_ip = a;
                    System.out.println("dir_ip:" + dir_ip);
                }
                str_output.add(y);
            }

            // 根据fileName创建文件目录,icd,datamap和startup
            ICDUtils.createDir(dir_iedName, fileName);

            // 更新iec61850_ied_inst数据库和osicfg.xml文件
            OsicfgEntity entity = new OsicfgEntity();
            entity.setArName(dir_iedName);
            entity.setArNameOld(dir_iedName);
            entity.setNetAddr(dir_ip);
            ICDUtils.commitled(entity, LEDService);
            //写入icd文件
            File icdFile = new File(UrlUtil.getUrlUtil().getOsicfg() + dir_iedName + File.separator + fileName);
            OutputStreamWriter os = new OutputStreamWriter(
                    new FileOutputStream(icdFile), "UTF-8");
            PrintWriter pw = new PrintWriter(os);
            in2.close();
            is.close();
            for (String s : str_output) {
                pw.println(s);
            }
            pw.close();
            os.close();
        }
        return "创建成功";
    }


    /**
     * 测量点描述导出
     *
     * @param response
     */
    @RequestMapping("/getRefnameZip")
    public void excelDownload(HttpServletResponse response) {

        List<String> dataList = systemConfigurationService.getYXDataRefname();
        Set<String> refs = new HashSet<>();
        for (int i = 0; i < dataList.size(); i++) {
            String refname = dataList.get(i);
            String[] refnames = refname.split("\\$");
            refname = refnames[(refnames.length - 1)];
            refs.add(refname);
        }
        List<RefNameCell> resultList = new ArrayList<RefNameCell>();
        for (String str : refs) {
            RefNameCell refnameCell = new RefNameCell();
            refnameCell.setRefName(str);
            refnameCell.setRefDesc("");
            resultList.add(refnameCell);
        }
        String sheet1Name = "测量点名称配置";
        String excelName = "测量点名称配置";
        ExcelUtils.writeExcel(response, excelName, sheet1Name, resultList, RefNameCell.class);
    }


    /**
     * 测量点描述导入
     *
     * @param file
     */
    @RequestMapping(value = "/getRefname_upload", method = RequestMethod.POST)
    public void readExcel(@RequestParam(value = "uploadFile", required = false) MultipartFile file) {
        List<RefNameCell> list = ExcelUtils.readExcel("", RefNameCell.class, file);
        for (RefNameCell refNameCell : list) {
            int insertFlag = systemConfigurationService.getrefnameFlag(refNameCell.getRefName());
            if (insertFlag > 0)
                systemConfigurationService.update_refname(refNameCell.getRefName(), refNameCell.getRefDesc());
            else
                systemConfigurationService.add_refname(refNameCell.getRefName(), refNameCell.getRefDesc());
        }
    }


    //    /**
//     * 生成XML文件
//     */
//    @RequestMapping("/getDeviceBySpace")
//    public void getDeviceBySpace(String select_name, String path, HttpServletResponse response, HttpServletRequest request) throws Exception {
//        select_name = new String(select_name.getBytes("iso-8859-1"), "utf-8");
//        List<DeviceEntity> DeviceList = new ArrayList<DeviceEntity>();
//        String rootPath = path;
//        // String rootPath2="d:/";
//        String rootPath2 = request.getSession().getServletContext().getRealPath("");
//        new UrlUtil();
//        rootPath2 = rootPath2 + UrlUtil.getUrlUtil().getMapPath();
//        // fileName表示你创建的文件名；为xml类型；
//        String fileName = "books.xml";
//        // String fileName2 ="books.xml";
//        if (select_name.equals("GIS区域")) {
//            DeviceList = systemConfigurationService.getDeviceBySpace(select_name);
//            fileName = "data_gis.xml";
//        } else if (select_name.equals("低端换流变") || select_name.equals("高端换流变") || select_name.equals("站用变")) {
//            DeviceList = systemConfigurationService.getDeviceBySpace_ft(select_name);
//            fileName = "data_ft.xml";
//        } else if (select_name.equals("直流场")) {
//            DeviceList = systemConfigurationService.getDeviceBySpace(select_name);
//            fileName = "data_zlc.xml";
//        } else if (select_name.equals("交流场")) {
//            DeviceList = systemConfigurationService.getDeviceBySpace(select_name);
//            fileName = "data_jlc.xml";
//        }
//        // else if(select_name.equals("站用变")){
//        // fileName="data_zyb.xml";
//        // }
//        System.out.println(rootPath);
//        File file = new File(rootPath2, fileName);
//        // 创建根节点 并设置它的属性 ;
//        Element root = new Element("map");
//        // 将根节点添加到文档中；
//        Document Doc = new Document(root);
//        // 判断是否存在xml文件
//        if (file.isFile() && file.exists()) {
//            file.delete();
//        }
//        CreatMapInfoFile(DeviceList, rootPath2, fileName);
//    }
//
//    /**
//     * 更换XML文件
//     */
//    @RequestMapping("/ModDeviceBySpace")
//    public void ModDeviceBySpace(String select_name, String path, HttpServletResponse response, HttpServletRequest request) throws Exception {
//        select_name = new String(select_name.getBytes("iso-8859-1"), "utf-8");
//        List<DeviceEntity> DeviceList = systemConfigurationService.getDeviceBySpace(select_name);
//        String rootPath = path;
//        // String rootPath2="d:/";
//        String rootPath2 = request.getSession().getServletContext().getRealPath("");
//        new UrlUtil();
//        rootPath2 = rootPath2 + UrlUtil.getUrlUtil().getMapPath();
//        // fileName表示你创建的文件名；为xml类型；
//        String fileName = "books.xml";
//        // String fileName2 ="books.xml";
//        if (select_name.equals("GIS区域")) {
//            fileName = "data_gis.xml";
//        } else if (select_name.equals("低端换流变") || select_name.equals("高端换流变")) {
//            fileName = "data_ft.xml";
//        } else if (select_name.equals("直流场")) {
//            fileName = "data_zlc.xml";
//        } else if (select_name.equals("交流场")) {
//            fileName = "data_jlc.xml";
//        } else if (select_name.equals("站用变")) {
//            fileName = "data_zyb.xml";
//        }
//        System.out.println(rootPath);
//
//        File file = new File(rootPath2, fileName);
//        // 创建根节点 并设置它的属性 ;
//        Element root = new Element("map");
//        // 将根节点添加到文档中；
//        Document Doc = new Document(root);
//        // 判断是否存在xml文件
//        if (file.isFile() && file.exists()) {
//            RepMapInfoFile(DeviceList, rootPath2, fileName);
//        } else {
//            CreatMapInfoFile(DeviceList, rootPath2, fileName);
//        }
//    }

//    /**
//     * 生成XML文件标签部分
//     *
//     * @throws IOException
//     * @throws FileNotFoundException
//     */
//    public static void CreatMapInfoFile(List<DeviceEntity> DeviceList, String rootPath, String fileName) throws JDOMException, IOException {
//        // 创建根节点 并设置它的属性 ;
//        Element root = new Element("map");
//        // 将根节点添加到文档中；
//        Document Doc = new Document(root);
//        Element elements_1 = new Element("state").setAttribute("id", "default_point");
//        elements_1.addContent(new Element("color").setText("0xFF0000"));
//        elements_1.addContent(new Element("size").setText("5"));
//        root.addContent(elements_1);
//        Element elements_2 = new Element("state").setAttribute("id", "refresh_rate");
//        elements_2.addContent(new Element("color").setText("1000"));
//        root.addContent(elements_2);
//        Element elements_3 = new Element("state").setAttribute("id", "background_color");
//        elements_3.addContent(new Element("color").setText("1000"));
//        root.addContent(elements_3);
//        Element elements_4 = new Element("state").setAttribute("id", "map_switch");
//        elements_4.addContent(new Element("color").setText("1"));
//        root.addContent(elements_4);
//        for (int i = 0; i < DeviceList.size(); i++) {
//            // 创建节点 state;
//            Element elements = new Element("state").setAttribute("id", "point");
//            // 给 state 节点添加子节点并赋值；
//            elements.addContent(new Element("devid").setText(DeviceList.get(i).getDeviceID()));
//            elements.addContent(new Element("name").setText(DeviceList.get(i).getDeviceName() + "," + DeviceList.get(i).getStartOperateTime()));
//            elements.addContent(new Element("dtype").setText(String.valueOf(DeviceList.get(i).getDeviceType())));
//            elements.addContent(new Element("info").setText(DeviceList.get(i).getRemark()));
//            elements.addContent(new Element("data").setText("0,0,0,0,0,0,0,0,0,0,0"));
//            int X = i / 10 + 15;
//            int Y = i % 10 + 90;
//            String XYpoint = "-" + X + "," + Y;
//            elements.addContent(new Element("loc").setText(XYpoint));
//            //
//            root.addContent(elements);
//        }
//        Format format = Format.getPrettyFormat();
//        XMLOutputter XMLOut = new XMLOutputter(format);
//
//        // InputStream inSt = new FileInputStream(file_path+"boss.xml");
//        XMLOut.output(Doc, new FileOutputStream(rootPath + "/" + fileName));
//
//    }
//
//    /**
//     * 替换XML文件标签部分
//     *
//     * @throws IOException
//     * @throws FileNotFoundException
//     */
//    public static void RepMapInfoFile(List<DeviceEntity> DeviceList,
//                                      String rootPath, String fileName) throws JDOMException, IOException {
//        // 如不存在则新增
//
//        for (int i = 0; i < DeviceList.size(); i++) {// 循环数据库设备
//            Document document;
//            SAXBuilder bulider = new SAXBuilder();
//            InputStream inSt = new FileInputStream(rootPath + "/" + fileName);
//            document = bulider.build(inSt);
//            Element root_find = document.getRootElement(); // 获取根节点对象
//            List<Element> RemoteAddressList = root_find.getChildren("state");
//            boolean aa = false;
//            for (Element el2 : RemoteAddressList) {
//                Element devid = el2.getChild("devid");
//                Element devname = el2.getChild("name");
//                Element dtype = el2.getChild("dtype");
//                if (devid != null) {
//                    if (devid.getText().equals(DeviceList.get(i).getDeviceID())) {// 判断XML点在数据库中是否存在，如存在则修改
//                        System.out.println("XML文件的DevidId是" + devid.getText());
//                        System.out.println("数据库的DevidId是" + DeviceList.get(i).getDeviceID());
//                        // System.out.println("开始将"+devid.getText()+"替换成"+DeviceList.get(i).getDeviceID());
//                        // devid.setText(DeviceList.get(i).getDeviceID());
//                        System.out.println("开始将" + devname.getText() + "替换成" + DeviceList.get(i).getDeviceName());
//                        devname.setText(DeviceList.get(i).getDeviceName());
//                        System.out.println("开始将" + dtype.getText() + "替换成" + DeviceList.get(i).getDeviceType());
//                        dtype.setText(String.valueOf(DeviceList.get(i).getDeviceType()));
//                        aa = true;
//                        break;// 如果替换则跳出当前for循环寻找下一个
//                    }
//                }
//            }
//            if (aa == false) {
//
//                // 创建节点 state;
//                Element elements = new Element("state").setAttribute("id", "point");
//                // 给 state 节点添加子节点并赋值；
//                elements.addContent(new Element("devid").setText(DeviceList.get(i).getDeviceID()));
//                elements.addContent(new Element("name").setText(DeviceList.get(i).getDeviceName()));
//                elements.addContent(new Element("dtype").setText(String.valueOf(DeviceList.get(i).getDeviceType())));
//                elements.addContent(new Element("info").setText("0"));
//                elements.addContent(new Element("data").setText("0,0,0,0,0,0,0,0,0,0,0"));
//                int X = i / 10 + 15;
//                int Y = i % 10 + 90;
//                String XYpoint = "-" + X + "," + Y;
//                elements.addContent(new Element("loc").setText(XYpoint));
//                //
//                root_find.addContent(elements.detach());
//            }
//        }
//    }
//
//    @RequestMapping("/getchangeXmlSwitchTo0")
//    public void getchangeXmlSwitchTo0(String select_name, HttpServletResponse response, HttpServletRequest request) throws Exception {
//        select_name = new String(select_name.getBytes("iso-8859-1"), "utf-8");
//        String rootPath2 = request.getSession().getServletContext().getRealPath("");
//        new UrlUtil();
//        rootPath2 = rootPath2 + UrlUtil.getUrlUtil().getMapPath();
//        // fileName表示你创建的文件名；为xml类型；
//        String fileName = "data.xml";
//
//        if (select_name.equals("GIS区域")) {
//            fileName = "data_gis.xml";
//        } else if (select_name.equals("低端换流变") || select_name.equals("高端换流变")) {
//            fileName = "data_ft.xml";
//        } else if (select_name.equals("直流场")) {
//            fileName = "data_zlc.xml";
//        } else if (select_name.equals("交流场")) {
//            fileName = "data_jlc.xml";
//        } else if (select_name.equals("站用变")) {
//            fileName = "data_zyb.xml";
//        }
//
//        Document document;
//        SAXBuilder bulider = new SAXBuilder();
//        InputStream inSt = new FileInputStream(rootPath2 + "/" + fileName);
//        document = bulider.build(inSt);
//        Element root_find = document.getRootElement(); // 获取根节点对象
//
//        List<Element> RemoteAddressList = root_find.getChildren("state");
//
//        for (Element el2 : RemoteAddressList) {
//            if ("map_switch".equals(el2.getAttributeValue("id"))) {
//                Element color0 = el2.getChild("color");
//                color0.setText("0");
//                break;
//            }
//        }
//        Format format = Format.getPrettyFormat();
//        XMLOutputter XMLOut = new XMLOutputter(format);
//
//        // InputStream inSt = new FileInputStream(file_path+"boss.xml");
//        XMLOut.output(document, new FileOutputStream(rootPath2 + "/" + fileName));
//    }
//
//    @RequestMapping("/getchangeXmlSwitchTo1")
//    public void getchangeXmlSwitchTo1(String select_name,
//                                      HttpServletResponse response, HttpServletRequest request)
//            throws Exception {
//        select_name = new String(select_name.getBytes("iso-8859-1"), "utf-8");
//
//        String rootPath2 = request.getSession().getServletContext()
//                .getRealPath("");
//        new UrlUtil();
//        rootPath2 = rootPath2 + UrlUtil.getUrlUtil().getMapPath();
//        // fileName表示你创建的文件名；为xml类型；
//        String fileName = "data.xml";
//
//        if (select_name.equals("GIS区域")) {
//            fileName = "data_gis.xml";
//        } else if (select_name.equals("低端换流变") || select_name.equals("高端换流变")) {
//            fileName = "data_ft.xml";
//        } else if (select_name.equals("直流场")) {
//            fileName = "data_zlc.xml";
//        } else if (select_name.equals("交流场")) {
//            fileName = "data_jlc.xml";
//        } else if (select_name.equals("站用变")) {
//            fileName = "data_zyb.xml";
//        }
//
//        Document document;
//        SAXBuilder bulider = new SAXBuilder();
//        InputStream inSt = new FileInputStream(rootPath2 + "/" + fileName);
//        document = bulider.build(inSt);
//        Element root_find = document.getRootElement(); // 获取根节点对象
//        List<Element> RemoteAddressList = root_find.getChildren("state");
//        for (Element el2 : RemoteAddressList) {
//            if ("map_switch".equals(el2.getAttributeValue("id"))) {
//                Element color1 = el2.getChild("color");
//                color1.setText("1");
//                break;
//            }
//        }
//        Format format = Format.getPrettyFormat();
//        XMLOutputter XMLOut = new XMLOutputter(format);
//        // InputStream inSt = new FileInputStream(file_path+"boss.xml");
//        XMLOut.output(document, new FileOutputStream(rootPath2 + "/" + fileName));
//    }
//
//    @RequestMapping("/getSpaceZip")
//    public void getSpaceZip(String select_name, HttpServletResponse response, HttpServletRequest request) throws Exception {
//        String fileName_xml = "";
//        String fileName_swf = "";
//        // 根据index选择不同xml文件
//        String srcFileName = request.getSession().getServletContext().getRealPath("");
//        new UrlUtil();
//        srcFileName = srcFileName + UrlUtil.getUrlUtil().getMapPath();
//        String destFileName = srcFileName + "ZIP";
//        System.out.println(destFileName);
//        // 复制文件
//        if ("GIS".equals(select_name)) {
//            fileName_xml = "data_gis.xml";
//            fileName_swf = "DevMap_gis.swf";
//        } else if ("JLC".equals(select_name)) {
//            fileName_xml = "data_jlc.xml";
//            fileName_swf = "DevMap_jlc.swf";
//        } else if ("FT".equals(select_name)) {
//            fileName_xml = "data_ft.xml";
//            fileName_swf = "DevMap_ft.swf";
//        } else if ("ZLC".equals(select_name)) {
//            fileName_xml = "data_zlc.xml";
//            fileName_swf = "DevMap_zlc.swf";
//        }
//        File srcFile_xml = new File(srcFileName + "/" + fileName_xml);
//        File srcFile_swf = new File(srcFileName + "/" + fileName_swf);
//        File destFile_xml = new File(destFileName + "/" + fileName_xml);
//        File destFile_swf = new File(destFileName + "/" + fileName_swf);
//        File destFile = new File(destFileName);
//        File ZipFile = new File(srcFileName + "MapZip.zip");
//        System.out.println(ZipFile);
//        if (destFile.isDirectory() || destFile.exists()) {
//            File[] files = destFile.listFiles();// 声明目录下所有的文件 files[];
//            for (int i = 0; i < files.length; i++) {// 遍历目录下所有的文件
//                files[i].delete();// 把每个文件用这个方法进行迭代
//            }
//        }
//        if (!destFile.exists()) {
//            destFile.mkdirs();
//        }
//        if (ZipFile.exists()) {
//            ZipFile.delete();
//        }
//
//        int byteread = 0; // 读取的字节数
//        InputStream in_xml = null;
//        InputStream in_swf = null;
//        OutputStream out_xml = null;
//        OutputStream out_swf = null;
//        try {
//            in_xml = new FileInputStream(srcFile_xml);
//            in_swf = new FileInputStream(srcFile_swf);
//            out_xml = new FileOutputStream(destFile_xml);
//            out_swf = new FileOutputStream(destFile_swf);
//            byte[] buffer = new byte[1024];
//            while ((byteread = in_xml.read(buffer)) != -1) {
//                out_xml.write(buffer, 0, byteread);
//            }
//            while ((byteread = in_swf.read(buffer)) != -1) {
//                out_swf.write(buffer, 0, byteread);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out_xml != null)
//                    out_xml.close();
//                if (out_swf != null)
//                    out_swf.close();
//                if (in_xml != null)
//                    in_xml.close();
//                if (in_swf != null)
//                    in_swf.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        ICDUtils.compressedFile(destFileName, srcFileName);
//        // List<EquipmentSpaceEntity>
//        // dataList=systemConfigurationService.getEquipmentSapce();
//        // HtmlUtil.writerJson(response, dataList);
//    }
//
//
//    /**
//     * 上传SpaceMap
//     */
//    @RequestMapping("/getSpaceMap")
//    public void getSpaceMap(String select_name, HttpServletResponse response, HttpServletRequest request) throws Exception {
//        Map<String, Object> jsonMap = new HashMap<String, Object>();
//        String Graphsurl = request.getSession().getServletContext().getRealPath("");
//        new UrlUtil();
//        Graphsurl = Graphsurl + UrlUtil.getUrlUtil().getMapPath();
//        request.setCharacterEncoding("utf-8");
//        Date now = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// 可以方便地修改日期格式
//        String DayTime = dateFormat.format(now);
//        String fileName = "";
//        String fileName_xml = "";
//        String fileName_swf = "";
//        // 获取文件夹名
//        DiskFileItemFactory factory = new DiskFileItemFactory();
//        factory.setSizeThreshold(2 * 1024 * 1024);
//        ServletFileUpload upload = new ServletFileUpload(factory);
//        upload.setHeaderEncoding("utf-8");
//        if ("GIS".equals(select_name)) {
//            fileName_xml = "data_gis.xml";
//            fileName_swf = "DevMap_gis.swf";
//        } else if ("JLC".equals(select_name)) {
//            fileName_xml = "data_jlc.xml";
//            fileName_swf = "DevMap_jlc.swf";
//        } else if ("FT".equals(select_name)) {
//            fileName_xml = "data_ft.xml";
//            fileName_swf = "DevMap_ft.swf";
//        } else if ("ZLC".equals(select_name)) {
//            fileName_xml = "data_zlc.xml";
//            fileName_swf = "DevMap_zlc.swf";
//        }
//        File afile_xml = new File(Graphsurl + fileName_xml);
//        File afile_swf = new File(Graphsurl + fileName_swf);
//        File afile_file = new File(Graphsurl + "version");
//        if (!afile_file.exists() && !afile_file.isDirectory()) {
//            afile_file.mkdirs();
//        }
//        afile_xml.renameTo(new File(afile_file + "/" + DayTime + "-" + fileName_xml));
//        afile_swf.renameTo(new File(afile_file + "/" + DayTime + "-" + fileName_swf));
//
//        List<FileItem> fileList = upload.parseRequest(request);// 获取request的文件
//        // Iterator iter = fileItems.iterator()取其迭代器
//        // iter.hasNext()检查序列中是否还有元素
//        for (Iterator iter = fileList.iterator(); iter.hasNext(); ) {
//            // 获得序列中的下一个元素
//            FileItem item = (FileItem) iter.next();
//            String s = item.getString("utf-8");
//            // 上传文件的名称和完整路径
//            fileName = item.getName();
//            long size = item.getSize();
//            // 判断是否选择了文件
//            if ((fileName == null || fileName.equals("")) && size == 0) {
//                continue;
//            }
//            // 保存文件在服务器的物理磁盘中：第一个参数是：完整路径（不包括文件名）第二个参数是：文件名称
//            // item.write(file);
//            // 修改文件名和物料名一致，且强行修改了文件扩展名为gif
//            // item.write(new File(uploadPath, itemNo + ".gif"));
//            // 将文件保存到目录下，不修改文件名
//            // createExl(dir_iedName, fileName);
//            File Exl_InFile = new File(Graphsurl);
//            // 截取文件名字符串
//            fileName = fileName.substring(fileName.length() - 3, fileName.length());
//            if ("xml".equals(fileName)) {
//                item.write(new File(Exl_InFile, fileName_xml));
//            } else if ("swf".equals(fileName)) {
//                item.write(new File(Exl_InFile, fileName_swf));
//            }
//            jsonMap.put("File", 1);
//        }
//        HtmlUtil.writerJson(response, jsonMap);
//    }

}

