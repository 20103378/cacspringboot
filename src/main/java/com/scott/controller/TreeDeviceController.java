package com.scott.controller;

import com.base.cac.TreeViewUtil;
import com.base.entity.PubDeviceTypeEnum;
import com.base.entity.TreeNode;
import com.base.util.HtmlUtil;
import com.base.web.BaseAction;
import com.scott.entity.*;
import com.scott.service.TreeDeviceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <br>
 * <b>功能：</b>JeecgPersonController<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Controller
@RequestMapping("/treeDevice")
public class TreeDeviceController extends BaseAction {

    // Servrice start
    @Autowired(required = false)
    // 自动注入，不需要生成set方法了，required=false表示没有实现类，也不会报错。
    private TreeDeviceService<TreeDeviceEntity, DeviceStateEntity, Sf6_dataEntity, stom_dataEntity, SMOAM_dataEntity, SCOM_dataEntity, Spdm_dataEntity> treeDeviceService;

    /**
     * 创建树节点（主设备与设备节点关系）4.
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getZoneEmuList")
    @ResponseBody
    public List getZoneEmuList() {
        TreeViewUtil<TreeDeviceEntity> util = new TreeViewUtil<>();
        List<TreeDeviceEntity> entityList = new ArrayList<>();
//        //获取单位信息
        List<TreeDeviceEntity> InfoName = treeDeviceService.getUnitinfo();
        //获取空间名称
        List<TreeDeviceEntity> spaceName = treeDeviceService.getPubspaceName();
        //获取设备
        List<TreeDeviceEntity> equipment = treeDeviceService.getEquipmentName();
        //获去所有的设备名称
        List<TreeDeviceEntity> deviceName = treeDeviceService.getZoneEmuList();

        entityList.addAll(InfoName);
        entityList.addAll(spaceName);
        entityList.addAll(equipment);
        entityList.addAll(deviceName);
        List<TreeNode> lst = util.getTreeNodeByDatas(false, "", entityList);
        return lst;
    }

    @RequestMapping("/getDeviceTypeEnum")
    @ResponseBody
    public Map getDeviceTypeEnum() {
//        //设备区域
//        List<TreeDeviceEntity> space = treeDeviceService.getPubspaceName();
        //设备类型
        List<Integer> values = treeDeviceService.getPubDeviceTypeList();
        List<Map> typeEnums = PubDeviceTypeEnum.getDeviceTypeEnumsByValues(values);
        Map map = new HashMap(3);
//        map.put("space", space);
        map.put("deviceType", typeEnums);
        return map;
    }

    /**
     * 生成设备健康状态，设备名称，设备状态
     *
     * @param equipmentID
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getEmuListData")
    public void getEmuListData(String equipmentID, HttpServletResponse response) {
        // String equipmentID = "M001";// id=request.getParameter("id");
        Map<String, Object> param = new HashMap<String, Object>();
        List<DeviceStateEntity> EntityList;
        param.put("equipmentID", equipmentID);
        if (equipmentID.equals(""))
            EntityList = treeDeviceService.getEmuListData();
        else
            EntityList = treeDeviceService.getEmuListDataID(param);
        HtmlUtil.writerJson(response, EntityList);
    }

    @RequestMapping("/getSpaceNameByType")
    @ResponseBody
    public List getSpaceNameByType(String Type) {
        List<String> spaceName = treeDeviceService.getPubspaceNameByType(Type);
        return spaceName;

    }

    @RequestMapping("/getOtherImgList")
    @ResponseBody
    public Map getOtherImgList() {
        Map map = new HashMap(3);
        List<TreeDeviceEntity> entityList = treeDeviceService.getOtherImgList();
        computDeviceImgStatus(entityList);
        map.put("entityList", entityList);
        return map;
    }


    /**
     * 计算设备运行状态
     *
     * @param entityList
     */
    private void computDeviceImgStatus(List<TreeDeviceEntity> entityList) {
        for (int i = 0; i < entityList.size(); i++) {
            String type = entityList.get(i).getNodetype();
            String time = entityList.get(i).getValue();
            if (StringUtils.isNotEmpty(time)) {
                Long runTime = 0L;
                if ("1".equals(type)) {
                    runTime = 24 * 100 * 100L;
                } else {
                    runTime = 2 * 100 * 100L;

                }
                String nowHour = com.base.util.edit.DateUtil.formatFullTime(LocalDateTime.now());
                String timeHour = time.replaceAll("-", "")
                        .replaceAll(":", "").
                                replaceAll(" ", "");
                if (timeHour.contains(".")) {
                    timeHour = timeHour.split("\\.")[0];
                }
                Long intTime = Long.parseLong(nowHour) - Long.parseLong(timeHour);
                if (intTime < runTime) {
                    entityList.get(i).setImgPath("/images/state/Green_big.gif");
                }
            }
            entityList.get(i).setImgPath("/images/state/Yellow_big.gif");
        }
    }

    /**
     * 获取油色谱及微水实时数据
     *
     * @param DeviceID
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getStomData")
    public void getstomData(String DeviceID, HttpServletResponse response) {
        Map<String, Object> param = new HashMap<String, Object>(2);
        param.put("DeviceID", DeviceID);
        List<stom_dataEntity> entityList = treeDeviceService.getStomListData(param);
        HtmlUtil.writerJson(response, entityList);
    }

    /**
     * 获取六氟化硫实时数据
     *
     * @param DeviceID
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getSf6Data")
    public void getSf6Data(String DeviceID, HttpServletResponse response) {
        Map<String, Object> param = new HashMap<String, Object>(2);
        List<Sf6_dataEntity> EntityList;
        param.put("DeviceID", DeviceID);
        EntityList = treeDeviceService.getSf6ListData(param);
        HtmlUtil.writerJson(response, EntityList);
    }

    /**
     * 获取避雷器泄漏电流实时数据
     *
     * @param DeviceID
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getSmoamData")
    public void getSmoamData(String DeviceID, HttpServletResponse response) {
        Map<String, Object> param = new HashMap<String, Object>(2);
        List<SMOAM_dataEntity> EntityList;
        param.put("DeviceID", DeviceID);
        EntityList = treeDeviceService.getSmoamListData(param);
        HtmlUtil.writerJson(response, EntityList);
    }

    /**
     * 获取铁芯泄漏电流实时数据
     *
     * @param DeviceID
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getScomData")
    public void getScomData(String DeviceID, HttpServletResponse response) {
        Map<String, Object> param = new HashMap<String, Object>();
        List<SCOM_dataEntity> EntityList;
        param.put("DeviceID", DeviceID);
        EntityList = treeDeviceService.getScomListData(param);
        HtmlUtil.writerJson(response, EntityList);
    }

    /**
     * 获取换流变工况实时数据
     *
     * @param DeviceID
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getSpdmData")
    public void getSpdmData(String DeviceID, HttpServletResponse response) {
        Map<String, Object> param = new HashMap<String, Object>();
        List<Spdm_dataEntity> EntityList;
        param.put("DeviceID", DeviceID);
        EntityList = treeDeviceService.getSpdmData(param);
        HtmlUtil.writerJson(response, EntityList);
    }
}
