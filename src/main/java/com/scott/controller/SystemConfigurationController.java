package com.scott.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.entity.BaseEntity;
import com.base.entity.PubDeviceTypeEnum;
import com.base.page.BasePage;
import com.base.util.HtmlUtil;
import com.base.util.edit.ICDUtils;
import com.base.util.UrlUtil;
import com.base.web.BaseAction;
import com.scott.entity.*;
import com.scott.page.DevicePage;
import com.scott.service.LEDConfigurationService;
import com.scott.service.SystemConfigurationService;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.*;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.Boolean;
import java.text.SimpleDateFormat;
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
     * @return
     */
    @RequestMapping("/getAllSpace")
    @ResponseBody
    public Map getAllSpace()  throws Exception{
        Map<String,Object> map = new HashMap<>(4);
        //区域位置
        map.put("space",systemConfigurationService.findSpace());
        //相位
        List phase = new ArrayList();
        phase.add("全部");
        phase.add("A");
        phase.add("B");
        phase.add("C");
        map.put("phase",phase);
//        //主设备类型
//        List<Integer> values = new ArrayList<>();
//        values.add(1);values.add(2);values.add(3);
//        values.add(4);values.add(5);values.add(6);
//        values.add(7);
//        List<Map> deviceTypes = PubDeviceTypeEnum.getDeviceTypeEnumsByValues(values);
//        map.put("deviceTypes",deviceTypes);
        //LDevice编码
        Map<String,List> lDeviceMap = ICDUtils.getLdLnMap();
//        map.put("lDeviceMap",lDeviceMap);
        map.put("LDDevices", lDeviceMap.keySet());
//        List list = systemConfigurationService.getEquipmentIED61850LDsList();
//        Set set = lDeviceMap.keySet();
//        set.removeAll(list);
//        map.put("LDDevices", set);
        return map;
    }




    // 创建遥测量映射对象列表，从icd文件中获取台账ld_ln列表
    @RequestMapping("/getLd_Ln")
    @ResponseBody
    public List getLd_Ln() throws Exception {
        // 创建遥测量映射对象列表
        List<String> entityList = new ArrayList<>();
        SAXBuilder bulider = new SAXBuilder();
        InputStream inSt = new FileInputStream(UrlUtil.getUrlUtil().getOsicfg() + "osicfg.xml");
        Document document = bulider.build(inSt);
        Element root = document.getRootElement(); // 获取根节点对象
        List<Element> networklist = root.getChildren("NetworkAddressing");
        for (Element el : networklist) {
            List<Element> remoteAddressList = el.getChildren("RemoteAddressList");
            for (Element el2 : remoteAddressList) {
                List<Element> remoteAddress = el2.getChildren("RemoteAddress");
                for (Element el3 : remoteAddress) {
                    List<Element> AR_Name = el3.getChildren("AR_Name");
                    // 文档加载开始
                    String fileName0 = AR_Name.get(0).getText();
                    String xmlName0 = getXmlName(UrlUtil.getUrlUtil().getOsicfg() + fileName0);
                    SAXBuilder bulider0 = new SAXBuilder();
                    InputStream inSt0 = new FileInputStream(UrlUtil.getUrlUtil().getOsicfg() + fileName0 + xmlName0);
                    Document document0 = bulider0.build(inSt0);

                    // 文档创建完成,开始解析文档到遥测量映射列表中
                    Element root0 = document0.getRootElement(); // 获取根节点对象
                    Namespace ns = root0.getNamespace();
                    Element Communication = root0.getChild("Communication", ns);
                    Element SubNetwork = Communication.getChild("SubNetwork", ns);
                    Element ConnectedAP = SubNetwork.getChild("ConnectedAP", ns);
                    String ied_name = "";
                    String ied_desc = "";
                    String ld_inst_name = "";
                    String ld_inst_desc = "";
                    String ln_inst_name = "";
                    String ln_inst_desc = "";
                    List<Element> IEDList = root0.getChildren("IED", ns);
                    ied_name = IEDList.get(0).getAttributeValue("name");
                    ied_desc = IEDList.get(0).getAttributeValue("desc");
                    Element dataTypeTemplates = root.getChild("DataTypeTemplates", ns);
                    for (Element el0 : IEDList) {
                        Element AccessPoint = el0.getChild("AccessPoint", ns);
                        Element Server = AccessPoint.getChild("Server", ns);
                        List<Element> LDevice = Server.getChildren("LDevice", ns);
                        for (Element el_ld : LDevice) {
                            // 获取ldname和desc
                            ld_inst_name = ied_name + el_ld.getAttributeValue("inst");
                            List<Element> lnList = el_ld.getChildren("LN", ns);
                            // 开始遍历每个LN
                            for (Element el20 : lnList) {
                                ln_inst_name = el20.getAttributeValue("lnClass") + el20.getAttributeValue("inst");
                                entityList.add(ld_inst_name + ";" + ln_inst_name);
                            }
                        }
                    }
                }
            }
        }
        return entityList;
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
        List<YclysEntity> icdData = entityList.stream().filter(s->!s.getTypeName().equals("q")&&!s.getTypeName().equals("t")).collect(Collectors.toList());
        Map map = new HashMap(16);
        map.put("icd_data",icdData);
        map.put("ap_name",icdData.get(0).getApName());

        Set<String> lnData = new HashSet<>(16);
        Map<String, List<YclysEntity>> ldInstMap = new HashMap<>();


        for(YclysEntity icd:icdData){
            List<YclysEntity> tmpList =  ldInstMap.get(icd.getLdinst());
            if(tmpList == null){
                tmpList = new ArrayList<>();
            }
            tmpList.add(icd);
            ldInstMap.put(icd.getLdinst(),tmpList);
            lnData.add(icd.getLnType()+"("+icd.getLnClass()+","+icd.getLninst()+")");
        }

        map.put("ld_data",ldInstMap.keySet());
        map.put("ldInstMap",ldInstMap);
        map.put("ln_data",lnData);

        Set<String> fcData = new HashSet<>(16);
        fcData.add("ST");fcData.add("MX");
        fcData.add("SG");fcData.add("CO");
        map.put("fc_data",fcData);
        return map;
    }

//    /**
//     * 重启
//     *
//     * @throws IOException
//     * @throws InterruptedException
//     */
//    @RequestMapping("/restart")
//    public void restart(HttpServletResponse response, HttpServletRequest request) throws InterruptedException, IOException {
//        // String Sttoppath="/CAC/I1DataMgr_stop.sh"; //停止程序路径
//        String shpath = "reboot"; // SHD启动程序路径
//        // Runtime.getRuntime().exec(Sttoppath);
//        Runtime.getRuntime().exec(shpath);
//        System.out.println("已停止I1DataMgr_stop.sh程序");
//    }

    /**
     * 获取主设备IED61850LDs数据
     */
    @RequestMapping("/getEquipmentIED61850LDsList")
    @ResponseBody
    public List getEquipmentIED61850LDsList(String[] LDDevices) {
//        List ldDevices =  CollectionUtils.arrayToList(LDDevices);
        List ldDevices = new ArrayList();
        for(int i=0;i<LDDevices.length;i++){
            ldDevices.add(LDDevices[i]);
        }
        List IED61850LDs = systemConfigurationService.getEquipmentIED61850LDsList();
        ldDevices.removeAll(IED61850LDs);
        return  ldDevices;
    }
    /**
     * 获取主设备数据
     */
    @RequestMapping("/getEquipmentList")
    @ResponseBody
    public  Map<String, Object> getEquipmentList(BasePage page) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<EquipmentEntity> dataList = systemConfigurationService.getEquipmentList(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        return  jsonMap;
    }
    /**
     * 获取设备数据
     */
    @RequestMapping("/getDeviceList")
    @ResponseBody
    public Map<String, Object> getDeviceList(DevicePage page){
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<DeviceEntity> dataList = systemConfigurationService.getDeviceList(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        return jsonMap;
    }

    /**
     * 更新设备数据
     */
    @RequestMapping("/update_device")
    @ResponseBody
    public void update_device(DeviceEntity entity){
        systemConfigurationService.update_device(entity);
    }

    /**
     * 插入设备数据
     */
    @RequestMapping("/insert_device")
    @ResponseBody
    public void insert_device(DeviceEntity entity){
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
    public void getrefname(HttpServletResponse response, HttpServletRequest request) throws Exception {
        List<Refname_descEntity> dataList = systemConfigurationService.getrefname();
        HtmlUtil.writerJson(response, dataList);
    }

    /**
     * 修改测量点信息
     */
    @RequestMapping("/update_refname")
    public void update_refname(Refname_descEntity entity, HttpServletResponse response, HttpServletRequest request) throws Exception {

    }

    /**
     * 添加/修改 测量点
     */
    @RequestMapping("/add_refname")
    public void add_refname(Refname_descEntity entity, HttpServletResponse response, HttpServletRequest request) throws Exception {
        int F = systemConfigurationService.getrefnameFlag(entity);
        if (F > 0) {
            systemConfigurationService.update_refname(entity);
        } else {
            systemConfigurationService.add_refname(entity);
        }
    }

    /**
     * 删除测量点
     */
    @RequestMapping("/delete_refname")
    public void delete_refname(Refname_descEntity entity, HttpServletResponse response, HttpServletRequest request) throws Exception {
        systemConfigurationService.delete_refname(entity);
    }

    /**
     * 修改主设备信息
     */
    @RequestMapping("/update_equipment")
    @ResponseBody
    public void update_equipment(EquipmentEntity entity){
        EquipmentEntity equipmentEntity = systemConfigurationService.findEquipmentByIEC61850LD(entity.getIec61850LD());
        if(equipmentEntity == null){
            systemConfigurationService.add_equipment(entity);
        }else {
            systemConfigurationService.update_equipment(entity);
        }
    }

//    /**
//     * 添加主设备
//     */
//    @RequestMapping("/add_equipment")
//    @ResponseBody
//    public Boolean add_equipment(EquipmentEntity entity) {
//        EquipmentEntity equipmentEntity = systemConfigurationService.findEquipmentByIEC61850LD(entity.getIec61850LD());
//        if(equipmentEntity ==null){
//            systemConfigurationService.add_equipment(entity);
//            return true;
//        }
//        return false;
//    }

    /**
     * 删除主设备信息
     */
    @RequestMapping("/delete_equipment")
    @ResponseBody
    public void delete_equipment(EquipmentEntity entity){
        systemConfigurationService.delete_equipment(entity);
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
     * 导入红外测温设备
     */
    @RequestMapping("/upload_Infrared_Excel")
    public void upload_Infrared_Excel(HttpServletResponse response, HttpServletRequest request) throws Exception {
        // Map<String, Object> jsonMap = new HashMap<String, Object>();
        // List<DeviceEntity> dataList
        // =systemConfigurationService.getDeviceList(page);
        // jsonMap.put("total", page.getPager().getRowCount());
        // jsonMap.put("rows", dataList);
        // HtmlUtil.writerJson(response, jsonMap);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        request.setCharacterEncoding("utf-8");
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// 可以方便地修改日期格式
        String DayTime = dateFormat.format(now);
        String fileName = "";
        String Uploader = "";
        // 获取文件夹名
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(2 * 1024 * 1024);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("utf-8");

        List<FileItem> fileList = upload.parseRequest(request);// 获取request的文件
        // Iterator iter = fileItems.iterator()取其迭代器
        // iter.hasNext()检查序列中是否还有元素
        for (Iterator iter = fileList.iterator(); iter.hasNext(); ) {
            // 获得序列中的下一个元素
            FileItem item = (FileItem) iter.next();
            String s = item.getString("utf-8");
            // 上传文件的名称和完整路径
            fileName = item.getName();
            if (fileName == null) {
                Uploader = s;
            } else {
                long size = item.getSize();
                // 判断是否选择了文件
                if ((fileName == null || fileName.equals("")) && size == 0) {
                    continue;
                }
                // 截取文件名字符串
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
                fileName = DayTime + "-" + fileName;
                String path = UrlUtil.getUrlUtil().getOsicfg() + File.separator;
                File file = new File(path);
                // 如果不存在则创建 startup.cfg
                File i1toi2_Import = new File(path + "pubdevice_Infrared");
                // 目录不存在则创建
                if (!i1toi2_Import.exists() && !i1toi2_Import.isDirectory()) {
                    i1toi2_Import.mkdirs();
                }
                // 保存文件在服务器的物理磁盘中：第一个参数是：完整路径（不包括文件名）第二个参数是：文件名称
                // item.write(file);
                // 修改文件名和物料名一致，且强行修改了文件扩展名为gif
                // item.write(new File(uploadPath, itemNo + ".gif"));
                // 将文件保存到目录下，不修改文件名
                // createExl(dir_iedName, fileName);
                File Exl_InFile = new File(UrlUtil.getUrlUtil().getOsicfg() + File.separator + "pubdevice_Infrared");
                item.write(new File(Exl_InFile, fileName));
                jsonMap.put("File", 1);
            }
        }

        // 根据创建出来的Exl文件入数据库
        String DeviceID = null;
        String DeviceName = null;
        Workbook readwb = null;
        // 读取数据流

        File Exl_OutFile = new File(UrlUtil.getUrlUtil().getOsicfg() + File.separator + "pubdevice_Infrared" + File.separator + fileName);
        InputStream instream = new FileInputStream(Exl_OutFile);
        if (instream != null) {
            jsonMap.put("Excel", 2);
        }
        // JXL的读取
        readwb = Workbook.getWorkbook(instream);
        // 获取第X个Sheet表0代表Sheet1
        Sheet readsheet = readwb.getSheet(0);
        // 获取Sheet表中所包含的总列数
        int rsColumns = readsheet.getColumns();
        // 获取Sheet表中所包含的总行数
        int rsRows = readsheet.getRows();
        // 获取指定单元格的对象引用
        for (int i = 1; i < rsRows; i++) {
            for (int j = 0; j < rsColumns; j++) {
                Cell cell = readsheet.getCell(j, i);
                if (j == 0) {
                    DeviceID = cell.getContents();
                    if (DeviceID.indexOf("A") != 0) {
                        int ii = Integer.parseInt(DeviceID);
                        if (ii < 10) {
                            DeviceID = "A000" + DeviceID;
                        } else if (ii < 100) {
                            DeviceID = "A00" + DeviceID;
                        } else if (ii < 1000) {
                            DeviceID = "A0" + DeviceID;
                        } else {
                            DeviceID = "A" + DeviceID;
                        }
                    }
                }
                if (j == 1) {
                    DeviceName = cell.getContents();
                }
                System.out.print(cell.getContents() + " ");
            }
            // 将逐条读取到的EXCEL逐条插入到数据库中
            InfraredTableEntity entity = new InfraredTableEntity(DeviceID, DeviceName);
            int insertFlag = systemConfigurationService.getInfraredFlag(entity);
            if (insertFlag > 0)
                systemConfigurationService.updateInfraredTable(entity);
            else
                systemConfigurationService.insertInfraredTable(entity);

        }
        // 删除Excel文件
        File file = new File(UrlUtil.getUrlUtil().getOsicfg() + File.separator
                + "pubdevice_Infrared" + File.separator + fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getInfrared_Excel")
    public void getInfrared_Excel(HttpServletResponse response,
                                  HttpServletRequest request) throws Exception {
        String fileName_xml = "";
        // 根据index选择不同xml文件
        String srcFileName = request.getSession().getServletContext().getRealPath("");
        new UrlUtil();
        srcFileName = srcFileName + UrlUtil.getUrlUtil().getMapPath();
        String destFileName = srcFileName + "ZIP";
        System.out.println(destFileName);
        // 复制文件
        // if("GIS".equals(select_name)){
        // fileName_xml = "data_gis.xml";
        // fileName_swf = "DevMap_gis.swf";
        // }else if("JLC".equals(select_name)){
        // fileName_xml = "data_jlc.xml";
        // fileName_swf = "DevMap_jlc.swf";
        // }else if("FT".equals(select_name)){
        // fileName_xml = "data_ft.xml";
        // fileName_swf = "DevMap_ft.swf";
        // }else if("ZLC".equals(select_name)){
        // fileName_xml = "data_zlc.xml";
        // fileName_swf = "DevMap_zlc.swf";
        // }
        File srcFile_xml = new File(UrlUtil.getUrlUtil().getOsicfg() + File.separator + "pubdevice_Infrared" + File.separator + "data.xls");
        // File srcFile_swf = new File(srcFileName+"/"+fileName_swf);
        // File destFile_xml = new File(destFileName+"/"+fileName_xml);
        // File destFile_swf = new File(destFileName+"/"+fileName_swf);
        File destFile = new File(destFileName);
        File ZipFile = new File(srcFileName + "MapZip.zip");
        System.out.println(ZipFile);
        if (destFile.isDirectory() || destFile.exists()) {
            File[] files = destFile.listFiles();// 声明目录下所有的文件 files[];
            for (int i = 0; i < files.length; i++) {// 遍历目录下所有的文件
                files[i].delete();// 把每个文件用这个方法进行迭代
            }
        }
        if (!destFile.exists()) {
            destFile.mkdirs();
        }
        if (ZipFile.exists()) {
            ZipFile.delete();
        }

        int byteread = 0; // 读取的字节数
        InputStream in_xml = null;
        InputStream in_swf = null;
        OutputStream out_xml = null;
        OutputStream out_swf = null;
        try {
            in_xml = new FileInputStream(srcFile_xml);
            // in_swf = new FileInputStream(srcFile_swf);
            // out_xml = new FileOutputStream(destFile_xml);
            // out_swf = new FileOutputStream(destFile_swf);
            byte[] buffer = new byte[1024];
            while ((byteread = in_xml.read(buffer)) != -1) {
                out_xml.write(buffer, 0, byteread);
            }
            while ((byteread = in_swf.read(buffer)) != -1) {
                out_swf.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out_xml != null)
                    out_xml.close();
                if (out_swf != null)
                    out_swf.close();
                if (in_xml != null)
                    in_xml.close();
                if (in_swf != null)
                    in_swf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ICDUtils.compressedFile(destFileName, srcFileName);
        // List<EquipmentSpaceEntity>
        // dataList=systemConfigurationService.getEquipmentSapce();
        // HtmlUtil.writerJson(response, dataList);
    }

    /**
     * 生成XML文件
     */
    @RequestMapping("/getDeviceBySpace")
    public void getDeviceBySpace(String select_name, String path, HttpServletResponse response, HttpServletRequest request) throws Exception {
        select_name = new String(select_name.getBytes("iso-8859-1"), "utf-8");
        List<DeviceEntity> DeviceList = new ArrayList<DeviceEntity>();
        String rootPath = path;
        // String rootPath2="d:/";
        String rootPath2 = request.getSession().getServletContext().getRealPath("");
        new UrlUtil();
        rootPath2 = rootPath2 + UrlUtil.getUrlUtil().getMapPath();
        // fileName表示你创建的文件名；为xml类型；
        String fileName = "books.xml";
        // String fileName2 ="books.xml";
        if (select_name.equals("GIS区域")) {
            DeviceList = systemConfigurationService.getDeviceBySpace(select_name);
            fileName = "data_gis.xml";
        } else if (select_name.equals("低端换流变") || select_name.equals("高端换流变") || select_name.equals("站用变")) {
            DeviceList = systemConfigurationService.getDeviceBySpace_ft(select_name);
            fileName = "data_ft.xml";
        } else if (select_name.equals("直流场")) {
            DeviceList = systemConfigurationService.getDeviceBySpace(select_name);
            fileName = "data_zlc.xml";
        } else if (select_name.equals("交流场")) {
            DeviceList = systemConfigurationService.getDeviceBySpace(select_name);
            fileName = "data_jlc.xml";
        }
        // else if(select_name.equals("站用变")){
        // fileName="data_zyb.xml";
        // }
        System.out.println(rootPath);
        File file = new File(rootPath2, fileName);
        // 创建根节点 并设置它的属性 ;
        Element root = new Element("map");
        // 将根节点添加到文档中；
        Document Doc = new Document(root);
        // 判断是否存在xml文件
        if (file.isFile() && file.exists()) {
            file.delete();
        }
        CreatMapInfoFile(DeviceList, rootPath2, fileName);
    }

    /**
     * 更换XML文件
     */
    @RequestMapping("/ModDeviceBySpace")
    public void ModDeviceBySpace(String select_name, String path, HttpServletResponse response, HttpServletRequest request) throws Exception {
        select_name = new String(select_name.getBytes("iso-8859-1"), "utf-8");
        List<DeviceEntity> DeviceList = systemConfigurationService.getDeviceBySpace(select_name);
        String rootPath = path;
        // String rootPath2="d:/";
        String rootPath2 = request.getSession().getServletContext().getRealPath("");
        new UrlUtil();
        rootPath2 = rootPath2 + UrlUtil.getUrlUtil().getMapPath();
        // fileName表示你创建的文件名；为xml类型；
        String fileName = "books.xml";
        // String fileName2 ="books.xml";
        if (select_name.equals("GIS区域")) {
            fileName = "data_gis.xml";
        } else if (select_name.equals("低端换流变") || select_name.equals("高端换流变")) {
            fileName = "data_ft.xml";
        } else if (select_name.equals("直流场")) {
            fileName = "data_zlc.xml";
        } else if (select_name.equals("交流场")) {
            fileName = "data_jlc.xml";
        } else if (select_name.equals("站用变")) {
            fileName = "data_zyb.xml";
        }
        System.out.println(rootPath);

        File file = new File(rootPath2, fileName);
        // 创建根节点 并设置它的属性 ;
        Element root = new Element("map");
        // 将根节点添加到文档中；
        Document Doc = new Document(root);
        // 判断是否存在xml文件
        if (file.isFile() && file.exists()) {
            RepMapInfoFile(DeviceList, rootPath2, fileName);
        } else {
            CreatMapInfoFile(DeviceList, rootPath2, fileName);
        }
    }

    /**
     * 生成XML文件标签部分
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void CreatMapInfoFile(List<DeviceEntity> DeviceList, String rootPath, String fileName) throws JDOMException, IOException {
        // 创建根节点 并设置它的属性 ;
        Element root = new Element("map");
        // 将根节点添加到文档中；
        Document Doc = new Document(root);
        Element elements_1 = new Element("state").setAttribute("id", "default_point");
        elements_1.addContent(new Element("color").setText("0xFF0000"));
        elements_1.addContent(new Element("size").setText("5"));
        root.addContent(elements_1);
        Element elements_2 = new Element("state").setAttribute("id", "refresh_rate");
        elements_2.addContent(new Element("color").setText("1000"));
        root.addContent(elements_2);
        Element elements_3 = new Element("state").setAttribute("id", "background_color");
        elements_3.addContent(new Element("color").setText("1000"));
        root.addContent(elements_3);
        Element elements_4 = new Element("state").setAttribute("id", "map_switch");
        elements_4.addContent(new Element("color").setText("1"));
        root.addContent(elements_4);
        for (int i = 0; i < DeviceList.size(); i++) {
            // 创建节点 state;
            Element elements = new Element("state").setAttribute("id", "point");
            // 给 state 节点添加子节点并赋值；
            elements.addContent(new Element("devid").setText(DeviceList.get(i).getDeviceID()));
            elements.addContent(new Element("name").setText(DeviceList.get(i).getDeviceName() + "," + DeviceList.get(i).getStartOperateTime()));
            elements.addContent(new Element("dtype").setText(String.valueOf(DeviceList.get(i).getDeviceType())));
            elements.addContent(new Element("info").setText(DeviceList.get(i).getRemark()));
            elements.addContent(new Element("data").setText("0,0,0,0,0,0,0,0,0,0,0"));
            int X = i / 10 + 15;
            int Y = i % 10 + 90;
            String XYpoint = "-" + X + "," + Y;
            elements.addContent(new Element("loc").setText(XYpoint));
            //
            root.addContent(elements);
        }
        Format format = Format.getPrettyFormat();
        XMLOutputter XMLOut = new XMLOutputter(format);

        // InputStream inSt = new FileInputStream(file_path+"boss.xml");
        XMLOut.output(Doc, new FileOutputStream(rootPath + "/" + fileName));

    }

    /**
     * 替换XML文件标签部分
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void RepMapInfoFile(List<DeviceEntity> DeviceList,
                                      String rootPath, String fileName) throws JDOMException, IOException {
        // 如不存在则新增

        for (int i = 0; i < DeviceList.size(); i++) {// 循环数据库设备
            Document document;
            SAXBuilder bulider = new SAXBuilder();
            InputStream inSt = new FileInputStream(rootPath + "/" + fileName);
            document = bulider.build(inSt);
            Element root_find = document.getRootElement(); // 获取根节点对象
            List<Element> RemoteAddressList = root_find.getChildren("state");
            boolean aa = false;
            for (Element el2 : RemoteAddressList) {
                Element devid = el2.getChild("devid");
                Element devname = el2.getChild("name");
                Element dtype = el2.getChild("dtype");
                if (devid != null) {
                    if (devid.getText().equals(DeviceList.get(i).getDeviceID())) {// 判断XML点在数据库中是否存在，如存在则修改
                        System.out.println("XML文件的DevidId是" + devid.getText());
                        System.out.println("数据库的DevidId是" + DeviceList.get(i).getDeviceID());
                        // System.out.println("开始将"+devid.getText()+"替换成"+DeviceList.get(i).getDeviceID());
                        // devid.setText(DeviceList.get(i).getDeviceID());
                        System.out.println("开始将" + devname.getText() + "替换成" + DeviceList.get(i).getDeviceName());
                        devname.setText(DeviceList.get(i).getDeviceName());
                        System.out.println("开始将" + dtype.getText() + "替换成" + DeviceList.get(i).getDeviceType());
                        dtype.setText(String.valueOf(DeviceList.get(i).getDeviceType()));
                        aa = true;
                        break;// 如果替换则跳出当前for循环寻找下一个
                    }
                }
            }
            if (aa == false) {

                // 创建节点 state;
                Element elements = new Element("state").setAttribute("id", "point");
                // 给 state 节点添加子节点并赋值；
                elements.addContent(new Element("devid").setText(DeviceList.get(i).getDeviceID()));
                elements.addContent(new Element("name").setText(DeviceList.get(i).getDeviceName()));
                elements.addContent(new Element("dtype").setText(String.valueOf(DeviceList.get(i).getDeviceType())));
                elements.addContent(new Element("info").setText("0"));
                elements.addContent(new Element("data").setText("0,0,0,0,0,0,0,0,0,0,0"));
                int X = i / 10 + 15;
                int Y = i % 10 + 90;
                String XYpoint = "-" + X + "," + Y;
                elements.addContent(new Element("loc").setText(XYpoint));
                //
                root_find.addContent(elements.detach());
            }
        }
    }

    @RequestMapping("/getchangeXmlSwitchTo0")
    public void getchangeXmlSwitchTo0(String select_name, HttpServletResponse response, HttpServletRequest request) throws Exception {
        select_name = new String(select_name.getBytes("iso-8859-1"), "utf-8");
        String rootPath2 = request.getSession().getServletContext().getRealPath("");
        new UrlUtil();
        rootPath2 = rootPath2 + UrlUtil.getUrlUtil().getMapPath();
        // fileName表示你创建的文件名；为xml类型；
        String fileName = "data.xml";

        if (select_name.equals("GIS区域")) {
            fileName = "data_gis.xml";
        } else if (select_name.equals("低端换流变") || select_name.equals("高端换流变")) {
            fileName = "data_ft.xml";
        } else if (select_name.equals("直流场")) {
            fileName = "data_zlc.xml";
        } else if (select_name.equals("交流场")) {
            fileName = "data_jlc.xml";
        } else if (select_name.equals("站用变")) {
            fileName = "data_zyb.xml";
        }

        Document document;
        SAXBuilder bulider = new SAXBuilder();
        InputStream inSt = new FileInputStream(rootPath2 + "/" + fileName);
        document = bulider.build(inSt);
        Element root_find = document.getRootElement(); // 获取根节点对象

        List<Element> RemoteAddressList = root_find.getChildren("state");

        for (Element el2 : RemoteAddressList) {
            if ("map_switch".equals(el2.getAttributeValue("id"))) {
                Element color0 = el2.getChild("color");
                color0.setText("0");
                break;
            }
        }
        Format format = Format.getPrettyFormat();
        XMLOutputter XMLOut = new XMLOutputter(format);

        // InputStream inSt = new FileInputStream(file_path+"boss.xml");
        XMLOut.output(document, new FileOutputStream(rootPath2 + "/" + fileName));
    }

    @RequestMapping("/getchangeXmlSwitchTo1")
    public void getchangeXmlSwitchTo1(String select_name,
                                      HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        select_name = new String(select_name.getBytes("iso-8859-1"), "utf-8");

        String rootPath2 = request.getSession().getServletContext()
                .getRealPath("");
        new UrlUtil();
        rootPath2 = rootPath2 + UrlUtil.getUrlUtil().getMapPath();
        // fileName表示你创建的文件名；为xml类型；
        String fileName = "data.xml";

        if (select_name.equals("GIS区域")) {
            fileName = "data_gis.xml";
        } else if (select_name.equals("低端换流变") || select_name.equals("高端换流变")) {
            fileName = "data_ft.xml";
        } else if (select_name.equals("直流场")) {
            fileName = "data_zlc.xml";
        } else if (select_name.equals("交流场")) {
            fileName = "data_jlc.xml";
        } else if (select_name.equals("站用变")) {
            fileName = "data_zyb.xml";
        }

        Document document;
        SAXBuilder bulider = new SAXBuilder();
        InputStream inSt = new FileInputStream(rootPath2 + "/" + fileName);
        document = bulider.build(inSt);
        Element root_find = document.getRootElement(); // 获取根节点对象
        List<Element> RemoteAddressList = root_find.getChildren("state");
        for (Element el2 : RemoteAddressList) {
            if ("map_switch".equals(el2.getAttributeValue("id"))) {
                Element color1 = el2.getChild("color");
                color1.setText("1");
                break;
            }
        }
        Format format = Format.getPrettyFormat();
        XMLOutputter XMLOut = new XMLOutputter(format);
        // InputStream inSt = new FileInputStream(file_path+"boss.xml");
        XMLOut.output(document, new FileOutputStream(rootPath2 + "/" + fileName));
    }

    @RequestMapping("/getSpaceZip")
    public void getSpaceZip(String select_name, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String fileName_xml = "";
        String fileName_swf = "";
        // 根据index选择不同xml文件
        String srcFileName = request.getSession().getServletContext().getRealPath("");
        new UrlUtil();
        srcFileName = srcFileName + UrlUtil.getUrlUtil().getMapPath();
        String destFileName = srcFileName + "ZIP";
        System.out.println(destFileName);
        // 复制文件
        if ("GIS".equals(select_name)) {
            fileName_xml = "data_gis.xml";
            fileName_swf = "DevMap_gis.swf";
        } else if ("JLC".equals(select_name)) {
            fileName_xml = "data_jlc.xml";
            fileName_swf = "DevMap_jlc.swf";
        } else if ("FT".equals(select_name)) {
            fileName_xml = "data_ft.xml";
            fileName_swf = "DevMap_ft.swf";
        } else if ("ZLC".equals(select_name)) {
            fileName_xml = "data_zlc.xml";
            fileName_swf = "DevMap_zlc.swf";
        }
        File srcFile_xml = new File(srcFileName + "/" + fileName_xml);
        File srcFile_swf = new File(srcFileName + "/" + fileName_swf);
        File destFile_xml = new File(destFileName + "/" + fileName_xml);
        File destFile_swf = new File(destFileName + "/" + fileName_swf);
        File destFile = new File(destFileName);
        File ZipFile = new File(srcFileName + "MapZip.zip");
        System.out.println(ZipFile);
        if (destFile.isDirectory() || destFile.exists()) {
            File[] files = destFile.listFiles();// 声明目录下所有的文件 files[];
            for (int i = 0; i < files.length; i++) {// 遍历目录下所有的文件
                files[i].delete();// 把每个文件用这个方法进行迭代
            }
        }
        if (!destFile.exists()) {
            destFile.mkdirs();
        }
        if (ZipFile.exists()) {
            ZipFile.delete();
        }

        int byteread = 0; // 读取的字节数
        InputStream in_xml = null;
        InputStream in_swf = null;
        OutputStream out_xml = null;
        OutputStream out_swf = null;
        try {
            in_xml = new FileInputStream(srcFile_xml);
            in_swf = new FileInputStream(srcFile_swf);
            out_xml = new FileOutputStream(destFile_xml);
            out_swf = new FileOutputStream(destFile_swf);
            byte[] buffer = new byte[1024];
            while ((byteread = in_xml.read(buffer)) != -1) {
                out_xml.write(buffer, 0, byteread);
            }
            while ((byteread = in_swf.read(buffer)) != -1) {
                out_swf.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out_xml != null)
                    out_xml.close();
                if (out_swf != null)
                    out_swf.close();
                if (in_xml != null)
                    in_xml.close();
                if (in_swf != null)
                    in_swf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ICDUtils.compressedFile(destFileName, srcFileName);
        // List<EquipmentSpaceEntity>
        // dataList=systemConfigurationService.getEquipmentSapce();
        // HtmlUtil.writerJson(response, dataList);
    }




    /**
     * 上传SpaceMap
     */
    @RequestMapping("/getSpaceMap")
    public void getSpaceMap(String select_name, HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String Graphsurl = request.getSession().getServletContext().getRealPath("");
        new UrlUtil();
        Graphsurl = Graphsurl + UrlUtil.getUrlUtil().getMapPath();
        request.setCharacterEncoding("utf-8");
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// 可以方便地修改日期格式
        String DayTime = dateFormat.format(now);
        String fileName = "";
        String fileName_xml = "";
        String fileName_swf = "";
        // 获取文件夹名
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(2 * 1024 * 1024);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("utf-8");
        if ("GIS".equals(select_name)) {
            fileName_xml = "data_gis.xml";
            fileName_swf = "DevMap_gis.swf";
        } else if ("JLC".equals(select_name)) {
            fileName_xml = "data_jlc.xml";
            fileName_swf = "DevMap_jlc.swf";
        } else if ("FT".equals(select_name)) {
            fileName_xml = "data_ft.xml";
            fileName_swf = "DevMap_ft.swf";
        } else if ("ZLC".equals(select_name)) {
            fileName_xml = "data_zlc.xml";
            fileName_swf = "DevMap_zlc.swf";
        }
        File afile_xml = new File(Graphsurl + fileName_xml);
        File afile_swf = new File(Graphsurl + fileName_swf);
        File afile_file = new File(Graphsurl + "version");
        if (!afile_file.exists() && !afile_file.isDirectory()) {
            afile_file.mkdirs();
        }
        afile_xml.renameTo(new File(afile_file + "/" + DayTime + "-" + fileName_xml));
        afile_swf.renameTo(new File(afile_file + "/" + DayTime + "-" + fileName_swf));

        List<FileItem> fileList = upload.parseRequest(request);// 获取request的文件
        // Iterator iter = fileItems.iterator()取其迭代器
        // iter.hasNext()检查序列中是否还有元素
        for (Iterator iter = fileList.iterator(); iter.hasNext(); ) {
            // 获得序列中的下一个元素
            FileItem item = (FileItem) iter.next();
            String s = item.getString("utf-8");
            // 上传文件的名称和完整路径
            fileName = item.getName();
            long size = item.getSize();
            // 判断是否选择了文件
            if ((fileName == null || fileName.equals("")) && size == 0) {
                continue;
            }
            // 保存文件在服务器的物理磁盘中：第一个参数是：完整路径（不包括文件名）第二个参数是：文件名称
            // item.write(file);
            // 修改文件名和物料名一致，且强行修改了文件扩展名为gif
            // item.write(new File(uploadPath, itemNo + ".gif"));
            // 将文件保存到目录下，不修改文件名
            // createExl(dir_iedName, fileName);
            File Exl_InFile = new File(Graphsurl);
            // 截取文件名字符串
            fileName = fileName.substring(fileName.length() - 3, fileName.length());
            if ("xml".equals(fileName)) {
                item.write(new File(Exl_InFile, fileName_xml));
            } else if ("swf".equals(fileName)) {
                item.write(new File(Exl_InFile, fileName_swf));
            }
            jsonMap.put("File", 1);
        }
        HtmlUtil.writerJson(response, jsonMap);
    }



    /**
     * 获取下一个主设备ID
     */
    @RequestMapping("/getNextEquipmentID")
    @ResponseBody
    public List<String> getNextEquipmentID(){
        List<String> dataList = systemConfigurationService.getNextEquipmentID();
        return dataList;
    }

    /**
     *
     * @return
     */
    @RequestMapping("/getNextDeviceID")
    @ResponseBody
    public List<String> getNextDeviceID(){
        List<String> dataList = systemConfigurationService.getNextDeviceID();
        return dataList;
    }

    /**
     * 删除设备
     */
    @RequestMapping("/delete_device")
    @ResponseBody
    public void delete_device(DeviceEntity entity) {
        systemConfigurationService.delete_device(entity);
    }

    /**
     * 获取SF6压力告警信息
     */
    @RequestMapping("/getSf6Monitor")
    public void getSf6Monitor(Sf6AlarmEntity entity,
                              HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        List<Sf6AlarmEntity> list = systemConfigurationService
                .getSf6Monitor(entity);
        List<BaseMonitorEntity> list2 = new ArrayList<BaseMonitorEntity>();
        String[] arrName = {"sf6气体压力阈值(MPa)"};
        if (list.size() != 0) {
            String[] arrValue = {"" + list.get(0).getPressureThreshold()};
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
                e.setStrValue("");
                list2.add(e);
            }
        }
        HtmlUtil.writerJson(response, list2);
    }

    /**
     * 获取油色谱压力告警信息
     */
    @RequestMapping("/getStomMonitor")
    public void getStomMonitor(StomAlarmEntity entity,
                               HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        List<StomAlarmEntity> list = systemConfigurationService
                .getStomMonitor(entity);
        List<BaseMonitorEntity> list2 = new ArrayList<BaseMonitorEntity>();
        String[] arrName = {"氢气上限值(ppm)", "乙炔上限值(ppm)", "总烃上限值(ppm)",
                "氢气变化率(ppm/day)", "乙炔变化率(ppm/5day)", "总烃变化率(ppm/day)",
                "微水上限值(mg/m3)"};
        if (list.size() != 0) {
            String[] arrValue = {"" + list.get(0).getH2ThresHold(),
                    "" + list.get(0).getC2H2ThresHold(),
                    "" + list.get(0).getTHThresHold(),
                    "" + list.get(0).getH2Change(),
                    "" + list.get(0).getC2H2Change(),
                    "" + list.get(0).getTHChange(),
                    "" + list.get(0).getMstThresHold()
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
                e.setStrValue("");
                list2.add(e);
            }
        }

        HtmlUtil.writerJson(response, list2);
    }

    /**
     * 获取避雷器告警信息
     */
    @RequestMapping("/getSmoamMonitor")
    public void getSmoamMonitor(SmoamAlarmEntity entity,
                                HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        List<SmoamAlarmEntity> list = systemConfigurationService
                .getSmoamMonitor(entity);
        List<BaseMonitorEntity> list2 = new ArrayList<BaseMonitorEntity>();
        String[] arrName = {"泄漏电流上限(mA)"};
        if (list.size() != 0) {
            String[] arrValue = {"" + list.get(0).getTotalCurrentThresHold()};
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
                e.setStrValue("");
                list2.add(e);
            }
        }
        HtmlUtil.writerJson(response, list2);
    }

    /**
     * 获取铁芯告警信息
     */
    @RequestMapping("/getScomMonitor")
    public void getScomMonitor(ScomAlarmEntity entity,
                               HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        List<ScomAlarmEntity> list = systemConfigurationService
                .getScomMonitor(entity);
        List<BaseMonitorEntity> list2 = new ArrayList<BaseMonitorEntity>();
        String[] arrName = {"泄漏电流上限(mA)"};
        if (list.size() != 0) {
            String[] arrValue = {"" + list.get(0).getCGAmpThresHold()};
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
                e.setStrValue("");
                list2.add(e);
            }
        }
        HtmlUtil.writerJson(response, list2);
    }

    /**
     * 获取工况压力告警信息
     */
    @RequestMapping("/getSconditionMonitor")
    public void getSconditionMonitor(SconditionAlarmEntity entity,
                                     HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        List<SconditionAlarmEntity> list = systemConfigurationService
                .getSconditionMonitor(entity);
        List<BaseMonitorEntity> list2 = new ArrayList<BaseMonitorEntity>();
        String[] arrName = {"顶层油温上限(℃)"};
        if (list.size() != 0) {
            String[] arrValue = {"" + list.get(0).getOilTempThresHold()};
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
                e.setStrValue("");
                list2.add(e);
            }
        }
        HtmlUtil.writerJson(response, list2);
    }

    /**
     * 获取SF6泄漏告警信息
     */
    @RequestMapping("/getSF6concentrationMonitor")
    public void getSF6concentrationMonitor(SconditionAlarmEntity entity,
                                           HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        List<BaseMonitorEntity> list = new ArrayList<BaseMonitorEntity>();
        HtmlUtil.writerJson(response, list);

    }

    /**
     * 获取复选框信息
     */
    @RequestMapping("/getCheckBox")
    public void getCheckBox(DeviceEntity entity, HttpServletResponse response,
                            HttpServletRequest request) throws Exception {
        List<DeviceEntity> dataList = systemConfigurationService
                .getCheckBox(entity);
        HtmlUtil.writerJson(response, dataList);
    }

    /**
     * 获取SF6压力告警ID列表
     */
    @RequestMapping("/getSf6MonitorID")
    public void getSf6MonitorID(Sf6AlarmEntity entity,
                                HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        List<Sf6AlarmEntity> list = systemConfigurationService
                .getSf6Monitor(entity);
        HtmlUtil.writerJson(response, list);
    }

    /**
     * 获取油色谱压力告警ID列表
     */
    @RequestMapping("/getStomMonitorID")
    public void getStomMonitorID(StomAlarmEntity entity,
                                 HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        List<StomAlarmEntity> list = systemConfigurationService
                .getStomMonitor(entity);
        HtmlUtil.writerJson(response, list);
    }

    /**
     * 获取铁芯告警ID列表
     */
    @RequestMapping("/getScomMonitorID")
    public void getScomMonitorID(ScomAlarmEntity entity,
                                 HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        List<ScomAlarmEntity> list = systemConfigurationService
                .getScomMonitor(entity);
        HtmlUtil.writerJson(response, list);
    }

    /**
     * 获取避雷器告警ID列表
     */
    @RequestMapping("/getSmoamMonitorID")
    public void getSmoamMonitorID(SmoamAlarmEntity entity,
                                  HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        List<SmoamAlarmEntity> list = systemConfigurationService
                .getSmoamMonitor(entity);
        HtmlUtil.writerJson(response, list);
    }

    /**
     * 获取工况告警ID列表
     */
    @RequestMapping("/getSconditionMonitorID")
    public void getSconditionMonitorID(SconditionAlarmEntity entity,
                                       HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        List<SconditionAlarmEntity> list = systemConfigurationService
                .getSconditionMonitor(entity);
        HtmlUtil.writerJson(response, list);
    }

    /**
     * 获取sf6泄漏告警ID列表
     */
    @RequestMapping("/getSF6concentrationMonitorID")
    public void getSF6concentrationMonitorID(SconditionAlarmEntity entity,
                                             HttpServletResponse response, HttpServletRequest request)
            throws Exception {

    }

    /**
     * 修改SF6告警信息
     */
    @RequestMapping("/updateSf6Monitor")
    public void updateSf6Monitor(Sf6AlarmEntity entity){
        systemConfigurationService.updateSf6Monitor(entity);
    }

    /**
     * 插入SF6告警信息
     */
    @RequestMapping("/insertSf6Monitor")
    public void insertSf6Monitor(Sf6AlarmEntity entity) {
        systemConfigurationService.insertSf6Monitor(entity);
    }

    /**
     * 修改Stom告警信息
     */
    @RequestMapping("/updateStomMonitor")
    public void updateStomMonitor(StomAlarmEntity entity) {
        systemConfigurationService.updateStomMonitor(entity);
    }

    /**
     * 插入Stom告警信息
     */
    @RequestMapping("/insertStomMonitor")
    public void insertStomMonitor(StomAlarmEntity entity) {
        systemConfigurationService.insertStomMonitor(entity);

    }

    /**
     * 修改smoam告警信息
     */
    @RequestMapping("/updateSmoamMonitor")
    public void updateSmoamMonitor(SmoamAlarmEntity entity){

        systemConfigurationService.updateSmoamMonitor(entity);
    }

    /**
     * 插入smoam告警信息
     */
    @RequestMapping("/insertSmoamMonitor")
    public void insertSmoamMonitor(SmoamAlarmEntity entity){
        systemConfigurationService.insertSmoamMonitor(entity);

    }

    /**
     * 修改scom告警信息
     */
    @RequestMapping("/updateScomMonitor")
    public void updateScomMonitor(ScomAlarmEntity entity){
        systemConfigurationService.updateScomMonitor(entity);
    }

    /**
     * 插入Scom告警信息
     */
    @RequestMapping("/insertScomMonitor")
    public void insertScomMonitor(ScomAlarmEntity entity){
        systemConfigurationService.insertScomMonitor(entity);

    }

    /**
     * 修改工况告警信息
     */
    @RequestMapping("/updateSconditionMonitor")
    public void updateSconditionMonitor(SconditionAlarmEntity entity){
        systemConfigurationService.updateSconditionMonitor(entity);
    }

    /**
     * 插入工况告警信息
     */
    @RequestMapping("/insertSconditionMonitor")
    public void insertSconditionMonitor(SconditionAlarmEntity entity){
        systemConfigurationService.insertSconditionMonitor(entity);

    }

    /**
     * 获取设备数据
     */
    @RequestMapping("/getExportList")
    public void getExportList(HttpServletResponse response){
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<DeviceEntity> dataList = systemConfigurationService
                .getExportList();
        jsonMap.put("dataList", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * I1TOI2获取所有设备 由于获取数据一样,直接调用getExportList方法
     */
    @RequestMapping("/getAllDevice")
    public void getAllDevice(HttpServletResponse response){
        getExportList(response);
    }

    /**
     * 获取i1toi2_data_inst表数据
     */
    @RequestMapping("/getI2Data")
    public void getI2Data(BasePage page, HttpServletResponse response,
                          HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<I2TableEntity> dataList = systemConfigurationService
                .getI2Data(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getI2Data_103")
    public void getI2Data_103(BasePage page, HttpServletResponse response,
                              HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<ZJ103Entity> dataList = systemConfigurationService
                .getI2Data_103(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/delete_103")
    public void delete_103(BasePage page, String[] DeviceIDs,
                           HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        int aa = 0;
        for (int i = 0; i < DeviceIDs.length; i++) {
            ZJ103Entity DeviceID = new ZJ103Entity();
            DeviceID.setDeviceID(DeviceIDs[i]);
            aa++;
            systemConfigurationService.delete_103(DeviceID);
        }
        List<ZJ103Entity> dataList = systemConfigurationService
                .getI2Data_103(page);
        jsonMap.put("aa", aa);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/updata_103")
    public void updata_103(BasePage page, String[] DeviceIDs, String[] CommAds,
                           HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        for (int i = 0; i < DeviceIDs.length; i++) {
            ZJ103Entity DeviceID = new ZJ103Entity();
            DeviceID.setDeviceID(DeviceIDs[i]);
            DeviceID.setCommAddress(Integer.parseInt(CommAds[i]));
            systemConfigurationService.Updata_103(DeviceID);
        }
        List<ZJ103Entity> dataList = systemConfigurationService
                .getI2Data_103(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/submit_103_devPhase")
    public void submit_103_devPhase(BasePage page, String[] DeviceIDs,
                                    String[] devPhases, String[] CommAds, HttpServletResponse response,
                                    HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        for (int i = 0; i < DeviceIDs.length; i++) {
            ZJ103Entity DeviceID = new ZJ103Entity();
            DeviceID.setDeviceID(DeviceIDs[i]);
            DeviceID.setDevPhase(Integer.parseInt(devPhases[i]));
            DeviceID.setCommAddress(Integer.parseInt(CommAds[i]));
            systemConfigurationService.submit_103_devPhase(DeviceID);
        }
        List<ZJ103Entity> dataList = systemConfigurationService
                .getI2Data_103(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 获取i1toi2_data_inst表数据 用来导出Exl
     */
    @RequestMapping("/getI2Data_export")
    public void getI2Data_export(BasePage page, HttpServletResponse response,
                                 HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<I2TableEntity> dataList = systemConfigurationService
                .getI2Data_export(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("dataList", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 获取I1中的refname
     *
     * @param ld_name和ln_name
     *
     */
    /**
     * 获取I1中的refname
     *
     * @param ld_name和ln_name
     */
    @RequestMapping("/getycNameList")
    public void getycNameList(YcDataInstEntity entity,
                              HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<YcDataInstEntity> dataList = systemConfigurationService
                .getycNameList(entity);
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getyxNameList")
    public void getyxNameList(YcDataInstEntity entity,
                              HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<YcDataInstEntity> dataList = systemConfigurationService
                .getyxNameList(entity);
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getykNameList")
    public void getykNameList(YcDataInstEntity entity,
                              HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<YcDataInstEntity> dataList = systemConfigurationService
                .getykNameList(entity);
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /*
     * 插入I2数据
     */
    @RequestMapping("/i2TableCommmit")
    public void insertI2Table(I2TableEntity entity){
        int insertFlag = systemConfigurationService.getinsertFlag(entity);
        if (insertFlag > 0)
            systemConfigurationService.updateI2Table(entity);
        else
            systemConfigurationService.insertI2Table(entity);
    }

    /*
     * 删除I2数据
     */
    @RequestMapping("/delete_I2")
    public void delete_I2(I2TableEntity entity){
        systemConfigurationService.delete_I2(entity);
    }

    @RequestMapping("/getIEC61850LD_LN")
    public void getIEC61850LD_LN(I2TableEntity entity,
                                 HttpServletResponse response, HttpServletRequest request)
            throws Exception {
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
        HtmlUtil.writerJson(response, jsonMap);
    }


    @RequestMapping("/updataXml")//todo
    @ResponseBody
    public List updataXml(String iedName,String  jsonList ){
        JSONArray jsonArray = JSON.parseArray(jsonList);
        if (jsonArray.size() == 0) { return null; }
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
            temp_entity.setLn_inst_desc(jsonObj.get("ln_inst_desc").toString());

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
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("/updataDB")
    @ResponseBody
    public Boolean updataDB(String list){
        JSONArray jsonArray = JSON.parseArray(list);
        if (jsonArray.size()==0) { return true;}
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
            if (("SG").equals(arr_type[1])||("SE").equals(arr_type[1])||("SP").equals(arr_type[1])||("CO").equals(arr_type[1])) {
                temp_entity.setFc(arr_type[1]);
                if(arr_type[1].equals("SP")){
                    temp_entity.setYx_refname(arr_type[3]);
                }else if(arr_type[1].equals("CO")){
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
     * 导入Excel到数据库
     *
     * @param File
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("/uploadExcel")
    public void uploadExcel(HttpServletResponse response,
                            HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        request.setCharacterEncoding("utf-8");
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss");// 可以方便地修改日期格式
        String DayTime = dateFormat.format(now);
        String fileName = "";
        String Uploader = "";
        // 获取文件夹名
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(2 * 1024 * 1024);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("utf-8");

        List<FileItem> fileList = upload.parseRequest(request);// 获取request的文件
        // Iterator iter = fileItems.iterator()取其迭代器
        // iter.hasNext()检查序列中是否还有元素
        for (Iterator iter = fileList.iterator(); iter.hasNext(); ) {
            // 获得序列中的下一个元素
            FileItem item = (FileItem) iter.next();
            String s = item.getString("utf-8");
            // 上传文件的名称和完整路径
            fileName = item.getName();
            if (fileName == null) {
                Uploader = s;
            } else {
                long size = item.getSize();
                // 判断是否选择了文件
                if ((fileName == null || fileName.equals("")) && size == 0) {
                    continue;
                }
                // 截取文件名字符串
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1,
                        fileName.length());
                fileName = Uploader + "-" + DayTime + "-" + fileName;
                String path = UrlUtil.getUrlUtil().getOsicfg() + File.separator;
                File file = new File(path);
                // 如果不存在则创建 startup.cfg
                File i1toi2_Import = new File(path + "i1toi2_Import");
                // 目录不存在则创建
                if (!i1toi2_Import.exists() && !i1toi2_Import.isDirectory()) {
                    i1toi2_Import.mkdirs();
                }
                // 保存文件在服务器的物理磁盘中：第一个参数是：完整路径（不包括文件名）第二个参数是：文件名称
                // item.write(file);
                // 修改文件名和物料名一致，且强行修改了文件扩展名为gif
                // item.write(new File(uploadPath, itemNo + ".gif"));
                // 将文件保存到目录下，不修改文件名
                // createExl(dir_iedName, fileName);
                File Exl_InFile = new File(UrlUtil.getUrlUtil().getOsicfg()
                        + File.separator + "i1toi2_Import");
                item.write(new File(Exl_InFile, fileName));
                jsonMap.put("File", 1);
            }
        }

        // 根据创建出来的Exl文件入数据库
        String i2id = null;
        String i1type = null;
        String i1id = null;
        String i2_refname = null;
        String i2_desc = null;
        Workbook readwb = null;
        // 读取数据流
        File Exl_OutFile = new File(UrlUtil.getUrlUtil().getOsicfg()
                + File.separator + "i1toi2_Import" + File.separator + fileName);
        InputStream instream = new FileInputStream(Exl_OutFile);
        if (instream != null) {
            jsonMap.put("Excel", 2);
        }
        // JXL的读取
        readwb = Workbook.getWorkbook(instream);
        // 获取第X个Sheet表0代表Sheet1
        Sheet readsheet = readwb.getSheet(0);
        // 获取Sheet表中所包含的总列数
        int rsColumns = readsheet.getColumns();
        // 获取Sheet表中所包含的总行数
        int rsRows = readsheet.getRows();
        // 获取指定单元格的对象引用
        for (int i = 1; i < rsRows; i++) {
            for (int j = 0; j < rsColumns; j++) {
                Cell cell = readsheet.getCell(j, i);
                if (j == 0) {
                    i2id = cell.getContents();
                }
                if (j == 1) {
                    i1type = cell.getContents();
                }
                if (j == 2) {
                    i1id = cell.getContents();
                }
                if (j == 3) {
                    i2_refname = cell.getContents();
                }
                if (j == 4) {
                    i2_desc = cell.getContents();
                }
                System.out.print(cell.getContents() + " ");
            }
            // 将逐条读取到的EXCEL逐条插入到数据库中
            I2TableEntity entity = new I2TableEntity(i2id, i1type, i1id,
                    i2_refname, i2_desc);
            int insertFlag = systemConfigurationService.getinsertFlag(entity);
            if (insertFlag > 0)
                systemConfigurationService.updateI2Table(entity);
            else
                systemConfigurationService.insertI2Table(entity);
            System.out.println();
        }
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * IED接入配置，倒入icd文件
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

    // 从数据库读取点序号
    @RequestMapping("/getDxhFromDB")
    @ResponseBody
    public List getDxhFromDB(YclysEntity entity) {
        List<YclysEntity> list = new ArrayList<YclysEntity>();
        if (entity.getFc().equals("ST")) {
            list = systemConfigurationService.getyxByld(entity);
        } else if (entity.getFc().equals("MX")) {
            list = systemConfigurationService.getycByld(entity);
        } else if (entity.getFc().equals("SG/SE")) {
            list = systemConfigurationService.getykByld(entity);
        } else if (entity.getFc().equals("CO")) {
            entity.setLdinst(entity.getLdinst().replace("MONT", ""));
            list = systemConfigurationService.getykByld(entity);
        }
        return list;
    }

    // 从数据库读取点序号
    @RequestMapping("/getDxhFromCFG")
    @ResponseBody
    public List getDxhFromCFG(YclysEntity entity) {
        List<YcDataInstEntity> list = new ArrayList<YcDataInstEntity>();
        String index = "";
        // 获取文件夹路径
        String cfg = "/datamap.cfg";
        String file_path = UrlUtil.getUrlUtil().getOsicfg() + entity.getLdinst().substring(0, entity.getLdinst().indexOf("MONT")) + cfg;
        if (entity.getFc().equals("ST")) {
            index = "DI";
        } else if (entity.getFc().equals("MX")) {
            index = "AI";
        } else if (entity.getFc().equals("CO")) {
            index = "FV";
        }
        try {
            File file = new File(file_path);
            FileInputStream fs = new FileInputStream(file);
            InputStreamReader read = new InputStreamReader(fs, "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            String line;
            String[] arr;
            while ((line = reader.readLine()) != null) {
                String[] arr_ln;
                String[] arr_id;
                YcDataInstEntity temp = new YcDataInstEntity();
                if (index.equals("")) {
                } else if (line.indexOf(index) < 0) {
                } else {
                    arr = line.split("\t");
                    temp.setLd_inst_name(arr[0]);
                    arr_ln = arr[1].split("\\$");
                    temp.setLn_inst_name(arr_ln[0]);
                    temp.setYc_refname(arr_ln[2]);
                    arr_id = arr[2].split("\\$");
                    int _zh = Integer.parseInt(arr_id[1]);
                    int _dh = Integer.parseInt(arr_id[2]);
                    temp.setYc_id("" + (_zh * 2048 + _dh));
                    temp.setIed_type_id(arr[1]);
                    list.add(temp);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         return list;
    }

    private void createExl(String dirName, String fileName) {
        // 获取根目录
        String path = UrlUtil.getUrlUtil().getOsicfg() + dirName
                + File.separator;
        // 创建Exl文件
        File icdFile = new File(path + fileName);
        // 目录不存在则创建
        if (!icdFile.exists()) {
            try {
                icdFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 导入Excel到数据库
     *
     * @param File
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("/getRefname_upload")
    public void getRefname_upload(HttpServletResponse response,
                                  HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        request.setCharacterEncoding("utf-8");

        String fileName = "";
        String Uploader = "";
        // 获取文件夹名
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(2 * 1024 * 1024);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("utf-8");

        List<FileItem> fileList = upload.parseRequest(request);// 获取request的文件
        // Iterator iter = fileItems.iterator()取其迭代器
        // iter.hasNext()检查序列中是否还有元素
        for (Iterator iter = fileList.iterator(); iter.hasNext(); ) {
            // 获得序列中的下一个元素
            FileItem item = (FileItem) iter.next();
            String s = item.getString("utf-8");
            // 上传文件的名称和完整路径
            fileName = item.getName();
            if (fileName == null) {
                Uploader = s;
            } else {
                long size = item.getSize();
                // 判断是否选择了文件
                if ((fileName == null || fileName.equals("")) && size == 0) {
                    continue;
                }
                // 截取文件名字符串
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1,
                        fileName.length());
                fileName = Uploader + "-" + fileName;
                String path = UrlUtil.getUrlUtil().getOsicfg() + File.separator;
                File file = new File(path);
                // 如果不存在则创建 startup.cfg
                File i1toi2_Import = new File(path + "i1toi2_Import");
                // 目录不存在则创建
                if (!i1toi2_Import.exists() && !i1toi2_Import.isDirectory()) {
                    i1toi2_Import.mkdirs();
                }
                // 保存文件在服务器的物理磁盘中：第一个参数是：完整路径（不包括文件名）第二个参数是：文件名称
                // item.write(file);
                // 修改文件名和物料名一致，且强行修改了文件扩展名为gif
                // item.write(new File(uploadPath, itemNo + ".gif"));
                // 将文件保存到目录下，不修改文件名
                // createExl(dir_iedName, fileName);
                File Exl_InFile = new File(UrlUtil.getUrlUtil().getOsicfg()
                        + File.separator + "i1toi2_Import");
                item.write(new File(Exl_InFile, fileName));
                jsonMap.put("File", 1);
            }
        }

        // 根据创建出来的Exl文件入数据库
        String refname = null;
        String refdesc = null;
        Workbook readwb = null;
        // 读取数据流
        File Exl_OutFile = new File(UrlUtil.getUrlUtil().getOsicfg()
                + File.separator + "i1toi2_Import" + File.separator + fileName);
        InputStream instream = new FileInputStream(Exl_OutFile);
        if (instream != null) {
            jsonMap.put("Excel", 2);
        }
        // JXL的读取
        readwb = Workbook.getWorkbook(instream);
        // 获取第X个Sheet表0代表Sheet1
        Sheet readsheet = readwb.getSheet(0);
        // 获取Sheet表中所包含的总列数
        int rsColumns = readsheet.getColumns();
        // 获取Sheet表中所包含的总行数
        int rsRows = readsheet.getRows();
        // 获取指定单元格的对象引用
        for (int i = 1; i < rsRows; i++) {
            for (int j = 0; j < rsColumns; j++) {
                Cell cell = readsheet.getCell(j, i);
                if (j == 0) {
                    refname = cell.getContents();
                }
                if (j == 1) {
                    refdesc = cell.getContents();
                }
                System.out.print(cell.getContents() + " ");
            }
            // 将逐条读取到的EXCEL逐条插入到数据库中
            Refname_descEntity entity = new Refname_descEntity(refname, refdesc);

            int insertFlag = systemConfigurationService.getrefnameFlag(entity);
            if (insertFlag > 0)
                systemConfigurationService.update_refname(entity);
            else
                systemConfigurationService.add_refname(entity);
            System.out.println();
        }
        if (!Exl_OutFile.exists()) {
            System.out.println("文件不存在");

        } else {
            System.out.println("存在文件");
            Exl_OutFile.delete();// 删除文件
        }
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getRefnameZip")
    public void getRefnameZip(HttpServletResponse response,
                              HttpServletRequest request) throws Exception {
        List<DataEntity> dataList = systemConfigurationService
                .getYXDataRefname();
        List<String> refs = new ArrayList<String>();
        for (int i = 0; i < dataList.size(); i++) {
            String refname = dataList.get(i).getRefname();
            String[] refnames = refname.split("\\$");
            refname = refnames[(refnames.length - 1)];
            refs.add(refname);
        }
        // 去重复

        for (int i = 0; i < refs.size() - 1; i++) {
            for (int j = refs.size() - 1; j > i; j--) {
                if (refs.get(j).equals(refs.get(i))) {
                    refs.remove(j);
                }
            }
        }
        // System.out.println(refs);
        // String path=UrlUtil.getUrlUtil().getXlsUrl()+File.separator;
        String path = "/CAC/web/SCAC-3000/jsp/com.scott" + File.separator;
        path = path + "out.xls";
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件不存在");

        } else {
            System.out.println("存在文件");
            file.delete();// 删除文件
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        WritableWorkbook wwb;
        // 新建立一个jxl文件,即在d盘下生成testJXL.xls
        // OutputStream os = new FileOutputStream(path);
        // JxlWrite jxlwrite = new JxlWrite();
        // jxlwrite.writeExcel(path);
        // wwb=Workbook.createWorkbook(os);
        String worksheet = "测量点名称配置";// 输出的excel文件工作表名
        String[] title = {"测量点名", "描述"};// excel工作表的标题
        WritableWorkbook workbook;
        try {
            // 创建可写入的Excel工作薄,运行生成的文件在tomcat/bin下
            // workbook = Workbook.createWorkbook(new File("output.xls"));
            System.out.println("begin");

            OutputStream os = new FileOutputStream(path);
            workbook = Workbook.createWorkbook(os);

            WritableSheet sheet = workbook.createSheet(worksheet, 0); // 添加第一个工作表
            // WritableSheet sheet1 = workbook.createSheet("MySheet1", 1);
            // //可添加第二个工作
            /*
             * jxl.write.Label label = new jxl.write.Label(0, 2,
             * "A label record"); //put a label in cell A3, Label(column,row)
             * sheet.addCell(label);
             */

            Label label;
            for (int i = 0; i < title.length; i++) {
                // Label(列号,行号 ,内容 )
                label = new Label(i, 0, title[i]); // put the title in
                // row1
                sheet.addCell(label);
            }
            for (int i = 0; i < refs.size(); i++) {
                label = new Label(0, i + 1, refs.get(i)); // put the
                // title in
                // row1
                sheet.addCell(label);
            }

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("end");
        Runtime r = Runtime.getRuntime();
        Process p = null;
        // String cmd[]={"notepad","exec.java"};
        String cmd[] = {path, "out.xls"};
        try {
            p = r.exec(cmd);
        } catch (Exception e) {
            System.out.println("error executing: " + cmd[0]);
        }
    }


}
