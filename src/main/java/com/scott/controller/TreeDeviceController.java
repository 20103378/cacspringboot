package com.scott.controller;

import com.base.cac.TreeViewUtil;
import com.base.entity.PubDeviceTypeEnum;
import com.base.entity.TreeNode;
import com.base.util.HtmlUtil;
import com.base.util.UrlUtil;
import com.base.web.BaseAction;
import com.scott.entity.*;
import com.scott.service.TreeDeviceService;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
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
     * 获取XML文件修改时间并且修改对应的未修改时间大于10分钟的XML文件
     */
    @RequestMapping("/injectXml")
    public void injectXml(String[] Xmlopen, HttpServletResponse response, HttpServletRequest request) throws Exception {
        System.out.println(Xmlopen);
        String rootPath = request.getSession().getServletContext().getRealPath("");
        new UrlUtil();
        rootPath = rootPath + UrlUtil.getUrlUtil().getMapPath();
        String path;
        //获取当前系统时间
        Date myTime = new Date();
        System.out.println(myTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(new Date()));// new Date()为获取当前系统时间
        long millionSeconds = myTime.getTime();//毫秒
        System.out.println(millionSeconds);

        if ("1".equals(Xmlopen[0])) {//如果有地图打开
            //给一个path
            path = "data_gis.xml";
            //获取XML文件的修改时间
            File f = new File(rootPath + path);
            Calendar cal = Calendar.getInstance();
            long time = f.lastModified();
            cal.setTimeInMillis(time);
            System.out.println(cal.getTime());
            System.out.println("修改时间： " + formatter.format(cal.getTime()));

            if (millionSeconds - time > 1 * 60 * 1000) {
                //获取数据库数据
                List<TreeDeviceEntity> EntityList = treeDeviceService.getIDandTypeToinjectXml("GIS区域");//获取ID和Type
                Map<String, Object> param = getNodetype(EntityList);
                //传递数据与path给inject方法写入XML文件中
                inject(rootPath + path, param);
            }

        }
        if ("1".equals(Xmlopen[1])) {
            //给一个path
            path = "data_ft.xml";
            //获取XML文件的修改时间
            File f = new File(rootPath + path);
            Calendar cal = Calendar.getInstance();
            long time = f.lastModified();
            cal.setTimeInMillis(time);
            System.out.println(cal.getTime());
            System.out.println("修改时间： " + formatter.format(cal.getTime()));

            if (millionSeconds - time > 1 * 60 * 1000) {
                //获取数据库数据
                List<TreeDeviceEntity> EntityList = treeDeviceService.getIDandTypeToinjectXmlInSpace();//获取ID和Type
                Map<String, Object> param = getNodetype(EntityList);
                //传递数据与path给inject方法写入XML文件中
                inject(rootPath + path, param);
            }

        }
        if ("1".equals(Xmlopen[2])) {//如果有地图打开
            //给一个path
            path = "data_zlc.xml";
            //获取XML文件的修改时间
            File f = new File(rootPath + path);
            Calendar cal = Calendar.getInstance();
            long time = f.lastModified();
            cal.setTimeInMillis(time);
            System.out.println(cal.getTime());
            System.out.println("修改时间： " + formatter.format(cal.getTime()));
            if (millionSeconds - time > 1 * 60 * 1000) {
                //获取数据库数据
                List<TreeDeviceEntity> EntityList = treeDeviceService.getIDandTypeToinjectXml("直流场");//获取ID和Type
                Map<String, Object> param = getNodetype(EntityList);
                //传递数据与path给inject方法写入XML文件中
                inject(rootPath + path, param);
            }
        }
        if ("1".equals(Xmlopen[3])) {
            //给一个path
            path = "data_jlc.xml";
            //获取XML文件的修改时间
            File f = new File(rootPath + path);
            Calendar cal = Calendar.getInstance();
            long time = f.lastModified();
            cal.setTimeInMillis(time);
            System.out.println(cal.getTime());
            System.out.println("修改时间： " + formatter.format(cal.getTime()));

            if (millionSeconds - time > 1 * 60 * 1000) {
                //获取数据库数据
                List<TreeDeviceEntity> EntityList = treeDeviceService.getIDandTypeToinjectXml("交流场");//获取ID和Type
                Map<String, Object> param = getNodetype(EntityList);
                //传递数据与path给inject方法写入XML文件中
                inject(rootPath + path, param);
            }
        }
    }

    public Map<String, Object> getNodetype(List<TreeDeviceEntity> EntityList) {
        Map<String, Object> param = new HashMap<String, Object>(16);
        boolean stom = false;
        boolean sf6 = false;
        boolean smoam = false;
        boolean scom = false;

        for (int n = 0; n < EntityList.size(); n++) {//根据Type选择Data表
            String type = EntityList.get(n).getNodetype();
            if ("1".equals(type) && stom == false) {
                List<stom_dataEntity> XMLdate_stom = treeDeviceService.getStomAllData();
                param.put("stom_dataList", XMLdate_stom);
                stom = true;
            } else if ("2".equals(type) && sf6 == false) {
                List<Sf6_dataEntity> XMLdate_sf6 = treeDeviceService.getSf6AllData();
                param.put("Sf6_dataList", XMLdate_sf6);
                sf6 = true;
            } else if ("3".equals(type) && smoam == false) {
                List<SMOAM_dataEntity> XMLdate_smoam = treeDeviceService.getSmoamAllData();
                param.put("SMOAM_dataList", XMLdate_smoam);
                smoam = true;
            } else if ("4".equals(type) && scom == false) {
                List<SCOM_dataEntity> XMLdate_scom = treeDeviceService.getScomAllData();
                param.put("SCOM_dataList", XMLdate_scom);
                scom = true;
            }
//    		else if("5".equals(type)){
//    			List<SMOAM_dataEntity> XMLdate_smoam = treeDeviceService.getSconditionListData(param);
//    		}
        }
        return param;
    }

    public void inject(String path, Map<String, Object> param) throws JDOMException, IOException {
        //

        //获取到XML文件
        SAXBuilder bulider = new SAXBuilder();
        InputStream inSt = new FileInputStream(path);
        Document document = bulider.build(inSt);
        Element root = document.getRootElement();        //获取根节点对象
        List<Element> Networklist = root.getChildren("state");    //读取所有STATE 节点 的LIST
        //从MAP取出各LIST
        List<stom_dataEntity> XMLdate_stom = (List<stom_dataEntity>) param.get("stom_dataList");
        List<Sf6_dataEntity> XMLdate_sf6 = (List<Sf6_dataEntity>) param.get("Sf6_dataList");
        List<SMOAM_dataEntity> XMLdate_smoam = (List<SMOAM_dataEntity>) param.get("SMOAM_dataList");
        List<SCOM_dataEntity> XMLdate_scom = (List<SCOM_dataEntity>) param.get("SCOM_dataList");

        String names[] = new String[]{};
        //循环ID//将对应ID的设备更新入XML文件中
        for (Element el : Networklist) {
            Element devid = el.getChild("devid");
            Element name = el.getChild("name");

            if (name != null) {
                names = name.getText().split(",");
            }
            Element data = el.getChild("data");
            Element info = el.getChild("info");
            Element dtype = el.getChild("dtype");
            if (dtype != null && "1".equals(dtype.getText())) {
                for (int n = 0; n < XMLdate_stom.size(); n++) {
                    stom_dataEntity stom = XMLdate_stom.get(n);
                    if (devid.getText().equals(stom.getDeviceID())) {
                        name.setText(names[0] + "," + stom.getSampleTime());
                        data.setText(stom.getH2ppm() + "," + stom.getCO2ppm() + ","
                                + stom.getCOppm() + "," + stom.getCH4ppm() + ","
                                + stom.getC2H2ppm() + "," + stom.getC2H4ppm() + ","
                                + stom.getC2H6ppm() + "," + stom.getTotHyd() + ","
                                + stom.getMst());
                        info.setText(stom.getRemark() + "");
                        break;
                    }
                }
            }
            if (dtype != null && "2".equals(dtype.getText())) {
                for (int n = 0; n < XMLdate_sf6.size(); n++) {
                    Sf6_dataEntity sf6 = XMLdate_sf6.get(n);
                    if (devid.getText().equals(sf6.getDeviceID())) {
                        name.setText(names[0] + "," + sf6.getSampleTime());
                        data.setText("" + sf6.getPres());
                        info.setText(sf6.getRemark() + "");
                        break;
                    }
                }
            }
            if (dtype != null && "3".equals(dtype.getText())) {
                for (int n = 0; n < XMLdate_smoam.size(); n++) {
                    SMOAM_dataEntity smoam = XMLdate_smoam.get(n);
                    if (devid.getText().equals(smoam.getDeviceID())) {
                        name.setText(names[0] + "," + smoam.getSampleTime());
                        data.setText("" + smoam.getTotA());
                        info.setText(smoam.getRemark() + "");
                        break;
                    }
                }
            }
            if (dtype != null && "4".equals(dtype.getText())) {
                for (int n = 0; n < XMLdate_scom.size(); n++) {
                    SCOM_dataEntity scom = XMLdate_scom.get(n);
                    if (devid.getText().equals(scom.getDeviceID())) {
                        name.setText(names[0] + "," + scom.getSampleTime());
                        data.setText("" + scom.getCGAmp());
                        info.setText(scom.getRemark() + "");
                        break;
                    }
                }
            }

//			if(devid.getText().equals(entity.getAR_Name_old())){
//				AR_Name.setText(entity.getAR_Name());
//				NetAddr.setText(entity.getNetAddr());
//			}
        }
        //写入XML
        Format format = Format.getPrettyFormat();
        XMLOutputter XMLOut = new XMLOutputter(format);

        XMLOut.output(document, new FileOutputStream(path));
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
