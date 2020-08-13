package com.scott.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.entity.BaseEntity;
import com.base.page.BasePage;
import com.base.util.HtmlUtil;
import com.base.util.UrlUtil;
import com.base.web.BaseAction;
import com.scott.entity.*;
import com.scott.page.DevicePage;
import com.scott.service.LEDConfigurationService;
import com.scott.service.SystemConfigurationService;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    public String list(){
        return "scott/demo/systemConfiguration";
    }

    /**
     * 获取站点数据
     */
    @RequestMapping("/getStation")
    @ResponseBody
    public List getStation() {
        List<Station_InfoEntity> dataList = systemConfigurationService.getStation();
        return  dataList;
    }

    /**
     * 修改站点数据
     */
    @RequestMapping("/updateStation")
    public boolean updateStation(Station_InfoEntity entity){
        return systemConfigurationService.updateStation(entity)>0;
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
     * 获取主设备数据
     */
    @RequestMapping("/getEquipmentList")
    public void getEquipmentList(BasePage page, HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<EquipmentEntity> dataList = systemConfigurationService.getEquipmentList(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 获取设备数据
     */
    @RequestMapping("/getDeviceList")
    public void getDeviceList(DevicePage page, HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<DeviceEntity> dataList = systemConfigurationService.getDeviceList(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 更新设备数据
     */
    @RequestMapping("/update_device")
    public void update_device(DeviceEntity entity, HttpServletResponse response, HttpServletRequest request) throws Exception {
        systemConfigurationService.update_device(entity);
    }

    /**
     * 插入设备数据
     */
    @RequestMapping("/insert_device")
    public void insert_device(DeviceEntity entity, HttpServletResponse response, HttpServletRequest request) throws Exception {
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
    public void update_equipment(EquipmentEntity entity, HttpServletResponse response, HttpServletRequest request) throws Exception {
        systemConfigurationService.update_equipment(entity);
    }

    /**
     * 添加主设备
     */
    @RequestMapping("/add_equipment")
    public void add_equipment(EquipmentEntity entity, HttpServletResponse response, HttpServletRequest request) throws Exception {
        systemConfigurationService.add_equipment(entity);
    }

    /**
     * 删除主设备信息
     */
    @RequestMapping("/delete_equipment")
    public void delete_equipment(EquipmentEntity entity, HttpServletResponse response, HttpServletRequest request) throws Exception {
        systemConfigurationService.delete_equipment(entity);
    }

    /**
     * 获取主设备区域
     */
    @RequestMapping("/getEquipmentSapce")
    public void getEquipmentSapce(HttpServletResponse response, HttpServletRequest request) throws Exception {
        List<EquipmentSpaceEntity> dataList = systemConfigurationService.getEquipmentSapce();
        HtmlUtil.writerJson(response, dataList);
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
        compressedFile(destFileName, srcFileName);
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
            elements.addContent(new Element("dtype").setText(DeviceList.get(i).getDeviceType()));
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
                        dtype.setText(DeviceList.get(i).getDeviceType());
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
                elements.addContent(new Element("dtype").setText(DeviceList.get(i).getDeviceType()));
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
        compressedFile(destFileName, srcFileName);
        // List<EquipmentSpaceEntity>
        // dataList=systemConfigurationService.getEquipmentSapce();
        // HtmlUtil.writerJson(response, dataList);
    }

    public void compressedFile(String resourcesPath, String targetPath)
            throws Exception {
        File resourcesFile = new File(resourcesPath); // 源文件
        File targetFile = new File(targetPath); // 目的
        // 如果目的路径不存在，则新建
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        String targetName = "Map" + resourcesFile.getName() + ".zip"; // 目的压缩文件名
        FileOutputStream outputStream = new FileOutputStream(targetPath + "/" + targetName);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(outputStream));
        createCompressedFile(out, resourcesFile, "");
        out.close();
    }

    public void createCompressedFile(ZipOutputStream out, File file, String dir)
            throws Exception {
        // 如果当前的是文件夹，则进行进一步处理
        if (file.isDirectory()) {
            // 得到文件列表信息
            File[] files = file.listFiles();
            // 将文件夹添加到下一级打包目录
            out.putNextEntry(new ZipEntry(dir + "/"));
            dir = dir.length() == 0 ? "" : dir + "/";
            // 循环将文件夹中的文件打包
            for (int i = 0; i < files.length; i++) {
                createCompressedFile(out, files[i], dir + files[i].getName()); // 递归处理
            }
        } else { // 当前的是文件，打包处理
            // 文件输入流
            FileInputStream fis = new FileInputStream(file);
            out.putNextEntry(new ZipEntry(dir));
            // 进行写操作
            int j = 0;
            byte[] buffer = new byte[1024];
            while ((j = fis.read(buffer)) > 0) {
                out.write(buffer, 0, j);
            }
            // 关闭输入流
            fis.close();
        }
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
     * 重命名
     *
     * @param path
     * @param oldname
     * @param newname
     */
    public void renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (!oldfile.exists()) {
                return;// 重命名文件不存在
            }
            if (newfile.exists())// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                System.out.println(newname + "已经存在！");
            else {
                oldfile.renameTo(newfile);
            }
        } else {
            System.out.println("新文件名和旧文件名相同...");
        }
    }

    /**
     * 插入space数据  插入区域数据
     */
    @RequestMapping("/insertspace")
    public void insertspace(EquipmentSpaceEntity space, HttpServletResponse response, HttpServletRequest request) throws Exception {
        int insertFlag = systemConfigurationService.getinsertFlag_space(space);
        if (insertFlag > 0)
            systemConfigurationService.updatespace(space);
        else
            systemConfigurationService.insertspace(space);
    }

    /**
     * 删除space数据
     */
    @RequestMapping("/delete_space")
    public void delete_space(EquipmentSpaceEntity space, HttpServletResponse response, HttpServletRequest request) throws Exception {
        systemConfigurationService.delete_space(space);
    }

    /**
     * 获取下一个主设备ID
     */
    @RequestMapping("/getNextEquipmentID")
    public void getNextEquipmentID(HttpServletResponse response,
                                   HttpServletRequest request) throws Exception {
        List<String> dataList = systemConfigurationService
                .getNextEquipmentID();
        HtmlUtil.writerJson(response, dataList);
    }

    @RequestMapping("/getNextDeviceID")
    public void getNextDeviceID(HttpServletResponse response,
                                HttpServletRequest request) throws Exception {
        List<String> dataList = systemConfigurationService
                .getNextDeviceID();
        HtmlUtil.writerJson(response, dataList);
    }

    /**
     * 删除设备
     */
    @RequestMapping("/delete_device")
    public void delete_device(DeviceEntity entity,
                              HttpServletResponse response, HttpServletRequest request)
            throws Exception {
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
    public void updateSf6Monitor(Sf6AlarmEntity entity,
                                 HttpServletResponse response, HttpServletRequest request)
            throws Exception {

        systemConfigurationService.updateSf6Monitor(entity);
    }

    /**
     * 插入SF6告警信息
     */
    @RequestMapping("/insertSf6Monitor")
    public void insertSf6Monitor(Sf6AlarmEntity entity,
                                 HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        systemConfigurationService.insertSf6Monitor(entity);

    }

    /**
     * 修改Stom告警信息
     */
    @RequestMapping("/updateStomMonitor")
    public void updateStomMonitor(StomAlarmEntity entity,
                                  HttpServletResponse response, HttpServletRequest request)
            throws Exception {

        systemConfigurationService.updateStomMonitor(entity);
    }

    /**
     * 插入Stom告警信息
     */
    @RequestMapping("/insertStomMonitor")
    public void insertStomMonitor(StomAlarmEntity entity,
                                  HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        systemConfigurationService.insertStomMonitor(entity);

    }

    /**
     * 修改smoam告警信息
     */
    @RequestMapping("/updateSmoamMonitor")
    public void updateSmoamMonitor(SmoamAlarmEntity entity,
                                   HttpServletResponse response, HttpServletRequest request)
            throws Exception {

        systemConfigurationService.updateSmoamMonitor(entity);
    }

    /**
     * 插入smoam告警信息
     */
    @RequestMapping("/insertSmoamMonitor")
    public void insertSmoamMonitor(SmoamAlarmEntity entity,
                                   HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        systemConfigurationService.insertSmoamMonitor(entity);

    }

    /**
     * 修改scom告警信息
     */
    @RequestMapping("/updateScomMonitor")
    public void updateScomMonitor(ScomAlarmEntity entity,
                                  HttpServletResponse response, HttpServletRequest request)
            throws Exception {

        systemConfigurationService.updateScomMonitor(entity);
    }

    /**
     * 插入Scom告警信息
     */
    @RequestMapping("/insertScomMonitor")
    public void insertScomMonitor(ScomAlarmEntity entity,
                                  HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        systemConfigurationService.insertScomMonitor(entity);

    }

    /**
     * 修改工况告警信息
     */
    @RequestMapping("/updateSconditionMonitor")
    public void updateSconditionMonitor(SconditionAlarmEntity entity,
                                        HttpServletResponse response, HttpServletRequest request)
            throws Exception {

        systemConfigurationService.updateSconditionMonitor(entity);
    }

    /**
     * 插入工况告警信息
     */
    @RequestMapping("/insertSconditionMonitor")
    public void insertSconditionMonitor(SconditionAlarmEntity entity,
                                        HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        systemConfigurationService.insertSconditionMonitor(entity);

    }

    /**
     * 获取设备数据
     */
    @RequestMapping("/getExportList")
    public void getExportList(HttpServletResponse response,
                              HttpServletRequest request) throws Exception {
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
    public void getAllDevice(HttpServletResponse response,
                             HttpServletRequest request) throws Exception {
        getExportList(response, request);
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
    public void insertI2Table(I2TableEntity entity,
                              HttpServletResponse response, HttpServletRequest request)
            throws Exception {
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
    public void delete_I2(I2TableEntity entity, HttpServletResponse response,
                          HttpServletRequest request) throws Exception {
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

    @RequestMapping("/getSelXml")
    public void getSelXml(HttpServletResponse response, HttpServletRequest request) throws Exception {
        // 创建遥测量映射对象列表
        List<YclysEntity> entityList = new ArrayList<YclysEntity>();
        // 文档加载开始
        Document document;
        String file_path = "";
        file_path = UrlUtil.getUrlUtil().getOsicfg();
        String fileName = request.getParameter("xmlName");
        String xmlName = getXmlName(file_path + fileName);
        SAXBuilder bulider = new SAXBuilder();
        InputStream inSt = new FileInputStream(file_path + fileName + xmlName);
        document = bulider.build(inSt);
        // 文档创建完成,开始解析文档到遥测量映射列表中
        Element root = document.getRootElement(); // 获取根节点对象
        Namespace ns = root.getNamespace();
        Element Communication = root.getChild("Communication", ns);
        Element SubNetwork = Communication.getChild("SubNetwork", ns);
        Element ConnectedAP = SubNetwork.getChild("ConnectedAP", ns);
        String apName = ConnectedAP.getAttributeValue("apName");
        // Element address = ConnectedAP.getChild("Address",ns);
        // List<Element> addressList=address.getChildren("P",ns);
        // for(Element e1:addressList){
        // String tpe = e1.getAttributeValue("type");
        // if(e1.getAttributeValue("type").equals("IP")){
        // System.out.println(e1.getText());
        // }
        // }
        List<Element> IEDList = root.getChildren("IED", ns);
        Element DataTypeTemplates = root.getChild("DataTypeTemplates", ns);
        for (Element el : IEDList) {
            // 开始循环, 创建一个YclysEntity对象

            Element AccessPoint = el.getChild("AccessPoint", ns);
            Element Server = AccessPoint.getChild("Server", ns);
            List<Element> LDevice = Server.getChildren("LDevice", ns);
            for (Element el_ld : LDevice) {
                // 获取ldinst
                String ld_inst = el_ld.getAttributeValue("inst");
                /*
                 * if(ld_inst.equals("MONT03")){ continue; }
                 */
                List<Element> lnList = el_ld.getChildren("LN", ns);
                // 开始遍历每个LN
                for (Element el2 : lnList) {
                    String prefix = el2.getAttributeValue("prefix");// 1
                    String lnType = el2.getAttributeValue("lnType");
                    String lnClass = el2.getAttributeValue("lnClass");// 2
                    String inst = el2.getAttributeValue("inst");// 3
                    // 根据lnType再遍历LNodeType
                    List<Element> LNodeTypeList = DataTypeTemplates.getChildren("LNodeType", ns);
                    for (Element el3 : LNodeTypeList) {
                        String nodeType = el3.getAttributeValue("id");
                        if (!("LPHD".equals(nodeType)) && !("RDRE".equals(nodeType)) && (nodeType.equals(lnType))) {
                            List<Element> DOList = el3.getChildren("DO", ns);
                            for (Element el4 : DOList) {
                                String DOType = el4.getAttributeValue("type");
                                String DOName = el4.getAttributeValue("name");
                                String DODesc = el4.getAttributeValue("desc");
                                List<Element> DOTypeList = DataTypeTemplates.getChildren("DOType", ns);
                                // 根据DoType值遍历DoType节点
                                for (Element el5 : DOTypeList) {
                                    if (el5.getAttributeValue("id").equals(DOType) && !("PhyHealth".equals(DOName)) && !("Health".equals(DOName)) && !("Mod".equals(DOName)) && !("Proxy".equals(DOName)) && !("Beh".equals(DOName))) {
                                        String DOTypeDesc = el5.getAttributeValue("desc");
                                        List<Element> DAList = el5.getChildren("DA", ns);
                                        // 遍历DA节点 ,获得fc和name值
                                        for (Element el6 : DAList) {
                                            String fc = el6.getAttributeValue("fc");
                                            String bType = el6.getAttributeValue("bType");
                                            if (fc.equals("MX") || fc.equals("ST") || fc.equals("SG")) {
                                                if (el6.getAttributeValue("type") != null && !"Enum".equals(bType)) {
                                                    List<Element> DATypeList = DataTypeTemplates.getChildren("DAType", ns);
                                                    for (Element el7 : DATypeList) {
                                                        if (el7.getAttributeValue("id").equals(el6.getAttributeValue("type"))) {
                                                            YclysEntity temp_yclysentity = new YclysEntity();
                                                            temp_yclysentity.setIedName(fileName);
                                                            temp_yclysentity.setApName(apName);
                                                            temp_yclysentity.setLdinst(ld_inst);
                                                            if (prefix != "" && prefix != null) {
                                                                String lnClass2 = prefix + lnClass;
                                                                temp_yclysentity.setLnClass(lnClass2);
                                                            } else {
                                                                temp_yclysentity.setLnClass(lnClass);
                                                            }
                                                            // temp_yclysentity.setLnClass(lnClass);//1
                                                            temp_yclysentity.setLninst(inst);// 2
                                                            List<Element> DOIList = el2.getChildren("DOI", ns);
                                                            for (Element el8 : DOIList) {
                                                                String DOIName = el8.getAttributeValue("name");
                                                                String DOIDesc = el8.getAttributeValue("desc");
                                                                if (DOName.equals(DOIName)) {
                                                                    if (DOIDesc == null || DOIDesc == "") {
                                                                        List<Element> DAIList = el8.getChildren("DAI", ns);
                                                                        for (Element el9 : DAIList) {
                                                                            String DAIName = el9.getAttributeValue("name");
                                                                            List<Element> DAIVal = el9.getChildren("Val", ns);
                                                                            // String asd=DAIVal.get(0).getText();
                                                                            if (DAIVal.size() > 0) {
                                                                                temp_yclysentity.setDesc(DAIVal.get(0).getText());
                                                                            }
                                                                        }
                                                                    } else {
                                                                        temp_yclysentity.setDesc(DOIDesc);
                                                                    }
                                                                }
                                                            }
                                                            temp_yclysentity.setLnType(lnType);
                                                            temp_yclysentity.setFc(el6.getAttributeValue("fc"));
                                                            temp_yclysentity.setDoName(DOName);
                                                            List<Element> _temp = el7.getChildren("BDA", ns);
                                                            temp_yclysentity.setTypeName(el6.getAttributeValue("name"));
                                                            temp_yclysentity.setDatypeName(_temp.get(0).getAttributeValue("name"));
                                                            entityList.add(temp_yclysentity);
                                                        }
                                                    }
                                                } else {
                                                    YclysEntity temp_yclysentity = new YclysEntity();
                                                    temp_yclysentity.setIedName(fileName);
                                                    temp_yclysentity.setApName(apName);
                                                    temp_yclysentity.setLdinst(ld_inst);
                                                    if (prefix != "" && prefix != null) {
                                                        String lnClass2 = prefix + lnClass;
                                                        temp_yclysentity.setLnClass(lnClass2);
                                                    } else {
                                                        temp_yclysentity.setLnClass(lnClass);
                                                    }
                                                    // temp_yclysentity.setLnClass(lnClass);
                                                    temp_yclysentity.setLninst(inst);
                                                    List<Element> DOIList = el2.getChildren("DOI", ns);
                                                    for (Element el8 : DOIList) {
                                                        String DOIName = el8.getAttributeValue("name");
                                                        String DOIDesc = el8.getAttributeValue("desc");
                                                        if (DOName.equals(DOIName)) {
                                                            if (DOIDesc == null || DOIDesc == "") {
                                                                List<Element> DAIList = el8.getChildren("DAI", ns);
                                                                for (Element el9 : DAIList) {
                                                                    String DAIName = el9.getAttributeValue("name");
                                                                    List<Element> DAIVal = el9.getChildren("Val", ns);
                                                                    // String asd=DAIVal.get(0).getText();
                                                                    if (DAIVal.size() > 0) {
                                                                        temp_yclysentity.setDesc(DAIVal.get(0).getText());
                                                                    }
                                                                }

                                                            } else {
                                                                temp_yclysentity.setDesc(DOIDesc);
                                                            }

                                                        }
                                                    }
                                                    temp_yclysentity.setLnType(lnType);
                                                    temp_yclysentity.setFc(el6.getAttributeValue("fc"));
                                                    temp_yclysentity.setDoName(DOName);
                                                    temp_yclysentity.setTypeName(el6.getAttributeValue("name"));
                                                    entityList.add(temp_yclysentity);

                                                }
                                            }
                                            if (fc.equals("CO")) {// CO需要做特殊处理
                                                if (el6.getAttributeValue("type") != null && !"Enum".equals(bType)) {
                                                    List<Element> DATypeList = DataTypeTemplates.getChildren("DAType", ns);
                                                    // 遍历DAtype节点
                                                    for (Element el7 : DATypeList) {
                                                        // 如果DAtype下仍有type节点则继续遍历
                                                        if (el7.getAttributeValue("id").equals(el6.getAttributeValue("type"))) {
                                                            List<Element> _temp = el7.getChildren("BDA", ns);
                                                            for (Element bda : _temp) {
                                                                // 如果BDA下仍然有type则继续遍历
                                                                if (bda.getAttributeValue("type") != null) {
                                                                    String bda_type = bda.getAttributeValue("type");
                                                                    // 重新创建DAType列表
                                                                    List<Element> DATypeList2 = DataTypeTemplates.getChildren("DAType", ns);
                                                                    // 如果bdatype和Datype的id一致则取出bda节点的name属性
                                                                    for (Element da2 : DATypeList2) {
                                                                        if (da2.getAttributeValue("id").equals(bda_type)) {
                                                                            // 找到这个da后继续遍历DBA,取出dba的name
                                                                            List<Element> _temp2 = da2.getChildren("BDA", ns);
                                                                            for (Element dba2 : _temp2) {
                                                                                YclysEntity temp_yclysentity = new YclysEntity();// 创建一个遥测量映射的对象
                                                                                temp_yclysentity.setIedName(fileName);
                                                                                temp_yclysentity.setApName(apName);
                                                                                temp_yclysentity.setLdinst(ld_inst);
                                                                                if (prefix != "" && prefix != null) {
                                                                                    String lnClass2 = prefix + lnClass;
                                                                                    temp_yclysentity.setLnClass(lnClass2);
                                                                                } else {
                                                                                    temp_yclysentity.setLnClass(lnClass);
                                                                                }
                                                                                // temp_yclysentity.setLnClass(lnClass);
                                                                                temp_yclysentity.setLninst(inst);
                                                                                List<Element> DOIList = el2.getChildren("DOI", ns);
                                                                                for (Element el8 : DOIList) {
                                                                                    String DOIName = el8.getAttributeValue("name");
                                                                                    String DOIDesc = el8.getAttributeValue("desc");
                                                                                    if (DOName.equals(DOIName)) {
                                                                                        if (DOIDesc == null || DOIDesc == "") {
                                                                                            List<Element> DAIList = el8.getChildren("DAI", ns);
                                                                                            for (Element el9 : DAIList) {
                                                                                                String DAIName = el9.getAttributeValue("name");
                                                                                                List<Element> DAIVal = el9.getChildren("Val", ns);
                                                                                                // String asd=DAIVal.get(0).getText();
                                                                                                if (DAIVal.size() > 0) {
                                                                                                    temp_yclysentity.setDesc(DAIVal.get(0).getText());
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            temp_yclysentity.setDesc(DOIDesc);
                                                                                        }
                                                                                    }
                                                                                }
                                                                                temp_yclysentity.setLnType(lnType);
                                                                                temp_yclysentity.setFc(el6.getAttributeValue("fc"));
                                                                                temp_yclysentity.setDoName(DOName);
                                                                                temp_yclysentity.setTypeName(el6.getAttributeValue("name"));
                                                                                temp_yclysentity.setDatypeName(bda.getAttributeValue("name"));
                                                                                temp_yclysentity.setDbaType(dba2.getAttributeValue("name"));
                                                                                entityList.add(temp_yclysentity);
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    YclysEntity temp_yclysentity = new YclysEntity();// 创建一个遥测量映射的对象
                                                                    temp_yclysentity.setIedName(fileName);
                                                                    temp_yclysentity.setApName(apName);
                                                                    temp_yclysentity.setLdinst(ld_inst);
                                                                    if (prefix != "" && prefix != null) {
                                                                        String lnClass2 = prefix + lnClass;
                                                                        temp_yclysentity.setLnClass(lnClass2);
                                                                    } else {
                                                                        temp_yclysentity.setLnClass(lnClass);
                                                                    }
                                                                    // temp_yclysentity.setLnClass(lnClass);
                                                                    temp_yclysentity.setLninst(inst);
                                                                    List<Element> DOIList = el2.getChildren("DOI", ns);
                                                                    for (Element el8 : DOIList) {
                                                                        String DOIName = el8.getAttributeValue("name");
                                                                        String DOIDesc = el8.getAttributeValue("desc");
                                                                        if (DOName.equals(DOIName)) {
                                                                            if (DOIDesc == null || DOIDesc == "") {
                                                                                List<Element> DAIList = el8.getChildren("DAI", ns);
                                                                                for (Element el9 : DAIList) {
                                                                                    String DAIName = el9.getAttributeValue("name");
                                                                                    List<Element> DAIVal = el9.getChildren("Val", ns);
                                                                                    // String asd=DAIVal.get(0).getText();
                                                                                    if (DAIVal.size() > 0) {
                                                                                        temp_yclysentity.setDesc(DAIVal.get(0).getText());
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                temp_yclysentity.setDesc(DOIDesc);
                                                                            }

                                                                        }
                                                                    }
                                                                    temp_yclysentity.setLnType(lnType);
                                                                    temp_yclysentity.setFc(el6.getAttributeValue("fc"));
                                                                    temp_yclysentity.setDoName(DOName);
                                                                    temp_yclysentity.setTypeName(el6.getAttributeValue("name"));
                                                                    temp_yclysentity.setDatypeName(bda.getAttributeValue("name"));
                                                                    entityList.add(temp_yclysentity);

                                                                }
                                                            }

                                                        }
                                                    }
                                                } else {
                                                    YclysEntity temp_yclysentity = new YclysEntity();
                                                    temp_yclysentity.setIedName(fileName);
                                                    temp_yclysentity.setApName(apName);
                                                    temp_yclysentity.setLdinst(ld_inst);
                                                    if (prefix != "" && prefix != null) {
                                                        String lnClass2 = prefix + lnClass;
                                                        temp_yclysentity.setLnClass(lnClass2);
                                                    } else {
                                                        temp_yclysentity.setLnClass(lnClass);
                                                    }
                                                    // temp_yclysentity.setLnClass(lnClass);
                                                    temp_yclysentity.setLninst(inst);
                                                    List<Element> DOIList = el2.getChildren("DOI", ns);
                                                    for (Element el8 : DOIList) {
                                                        String DOIName = el8.getAttributeValue("name");
                                                        String DOIDesc = el8.getAttributeValue("desc");
                                                        if (DOName.equals(DOIName)) {
                                                            if (DOIDesc == null || DOIDesc == "") {
                                                                List<Element> DAIList = el8.getChildren("DAI", ns);
                                                                for (Element el9 : DAIList) {
                                                                    String DAIName = el9.getAttributeValue("name");
                                                                    List<Element> DAIVal = el9.getChildren("Val", ns);
                                                                    // String asd =DAIVal.get(0).getText();
                                                                    if (DAIVal.size() > 0) {
                                                                        temp_yclysentity.setDesc(DAIVal.get(0).getText());
                                                                    }
                                                                }

                                                            } else {
                                                                temp_yclysentity.setDesc(DOIDesc);
                                                            }
                                                        }
                                                    }
                                                    temp_yclysentity.setLnType(lnType);
                                                    temp_yclysentity.setFc(el6.getAttributeValue("fc"));
                                                    temp_yclysentity.setDoName(DOName);
                                                    temp_yclysentity.setTypeName(el6.getAttributeValue("name"));
                                                    entityList.add(temp_yclysentity);

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        HtmlUtil.writerJson(response, entityList);
    }

    @RequestMapping("/updataXml")//todo
    public void updataXml(String iedName, HttpServletResponse response,
                          HttpServletRequest request) throws Exception {
        // request.getServletPath();
        String jsonList = request.getParameter("list");
        JSONArray jsonArray =JSON.parseArray(jsonList);
        int iSize = jsonArray.size();
        if (iSize == 0) {
            return;
        }
        List<YclysEntity> list = new ArrayList<YclysEntity>();
        for (int i = 0; i < iSize; i++) {
            for (int k = 0; k < 3; k++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                YclysEntity temp_entity = new YclysEntity();
                temp_entity.setSel_dxh(jsonObj.get("dxh").toString());
                temp_entity.setSel_zh(jsonObj.get("dh").toString());
                String str = jsonObj.get("type").toString();
                String str0 = jsonObj.get("type").toString().split("[$]")[1];
                String str1 = jsonObj.get("type").toString()
                        .replace("mag$f", "q");
                if (k == 0) {
                    temp_entity.setLnType(jsonObj.get("type").toString());
                }
                if (k == 1) {

                    if ("MX".equals(jsonObj.get("type").toString().split("[$]")[1])) {
                        temp_entity.setLnType(jsonObj.get("type").toString()
                                .replace("mag$f", "q"));
                        // temp_entity.setLnType(str.substring(0,str.indexOf("[$]",2)+1)+"q");
                    } else {
                        temp_entity.setLnType(jsonObj.get("type").toString()
                                .replace("stVal", "q"));
                    }
                    System.out.println(jsonObj.get("type").toString()
                            .split("\\$")[1]);

                }
                if (k == 2) {
                    if ("MX".equals(jsonObj.get("type").toString().split("[$]")[1])) {
                        temp_entity.setLnType(jsonObj.get("type").toString()
                                .replace("mag$f", "t"));
                        // temp_entity.setLnType(str+str.substring(str.indexOf("[$]",2)+1)+"t");
                    } else {
                        temp_entity.setLnType(jsonObj.get("type").toString()
                                .replace("stVal", "t"));
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
            readCfgFile(path, list);
        } else {
            list.removeAll(list);
        }
        HtmlUtil.writerJson(response, list);
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

    @RequestMapping("/getJdList")
    public void getJdList(HttpServletResponse response,
                          HttpServletRequest request) throws Exception {
        // 创建遥测量映射对象列表
        List<InstNodeEntity> entityList = new ArrayList<InstNodeEntity>();
        // 文档加载开始
        Document document;
        String file_path = "";
        file_path = UrlUtil.getUrlUtil().getOsicfg();
        String fileName = request.getParameter("xmlName");
        String xmlName = getXmlName(file_path + fileName);
        SAXBuilder bulider = new SAXBuilder();
        InputStream inSt = new FileInputStream(file_path + fileName + xmlName);
        document = bulider.build(inSt);
        // 文档创建完成,开始解析文档到遥测量映射列表中
        Element root = document.getRootElement(); // 获取根节点对象
        Namespace ns = root.getNamespace();
        Element Communication = root.getChild("Communication", ns);
        Element SubNetwork = Communication.getChild("SubNetwork", ns);
        Element ConnectedAP = SubNetwork.getChild("ConnectedAP", ns);
        String ied_name = "";
        String ied_desc = "";
        String ld_inst_name = "";
        String ld_inst_desc = "";
        String ln_inst_name = "";
        String ln_inst_desc = "";
        // ied_name = ConnectedAP.getAttributeValue("apName");
        // //获取ip
        // Element address = ConnectedAP.getChild("Address",ns);
        // List<Element> addressList=address.getChildren("P",ns);
        // for(Element e1:addressList){
        // String tpe = e1.getAttributeValue("type");
        // if(e1.getAttributeValue("type").equals("IP")){
        // ied_desc=e1.getText();
        // }
        // }
        List<Element> IEDList = root.getChildren("IED", ns);
        ied_name = IEDList.get(0).getAttributeValue("name");
        ied_desc = IEDList.get(0).getAttributeValue("desc");
        Element DataTypeTemplates = root.getChild("DataTypeTemplates", ns);
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
        HtmlUtil.writerJson(response, entityList);
    }

    @RequestMapping("/getLd_Ln")
    public void getLd_Ln(HttpServletResponse response,
                         HttpServletRequest request) throws Exception {

        Document document;
        String file_path = "";
        // 创建遥测量映射对象列表
        List<InstNodeEntity> entityList = new ArrayList<InstNodeEntity>();
        file_path = UrlUtil.getUrlUtil().getOsicfg();
        String xmlName = "osicfg.xml";
        SAXBuilder bulider = new SAXBuilder();
        InputStream inSt = new FileInputStream(file_path + xmlName);
        document = bulider.build(inSt);
        Element root = document.getRootElement(); // 获取根节点对象
        List<Element> Networklist = root.getChildren("NetworkAddressing");
        for (Element el : Networklist) {
            List<Element> RemoteAddressList = el.getChildren("RemoteAddressList");
            for (Element el2 : RemoteAddressList) {
                List<Element> RemoteAddress = el2.getChildren("RemoteAddress");
                for (Element el3 : RemoteAddress) {
                    List<Element> AR_Name = el3.getChildren("AR_Name");
                    // 文档加载开始

                    Document document0;
                    String file_path0 = "";
                    file_path0 = UrlUtil.getUrlUtil().getOsicfg();
                    String fileName0 = AR_Name.get(0).getText();
                    String xmlName0 = getXmlName(file_path0 + fileName0);
                    SAXBuilder bulider0 = new SAXBuilder();
                    InputStream inSt0 = new FileInputStream(file_path0
                            + fileName0 + xmlName0);
                    document0 = bulider0.build(inSt0);

                    // Document document0;
                    // String fileName=AR_Name.get(0).getText();
                    // SAXBuilder bulider0 = new SAXBuilder();
                    // InputStream inSt0 = new
                    // FileInputStream(file_path+fileName+xmlName);
                    // document0 = bulider0.build(inSt0);
                    // 文档创建完成,开始解析文档到遥测量映射列表中
                    Element root0 = document0.getRootElement(); // 获取根节点对象
                    Namespace ns = root0.getNamespace();
                    Element Communication = root0.getChild("Communication", ns);
                    Element SubNetwork = Communication.getChild("SubNetwork",
                            ns);
                    Element ConnectedAP = SubNetwork
                            .getChild("ConnectedAP", ns);
                    String ied_name = "";
                    String ied_desc = "";
                    String ld_inst_name = "";
                    String ld_inst_desc = "";
                    String ln_inst_name = "";
                    String ln_inst_desc = "";
                    List<Element> IEDList = root0.getChildren("IED", ns);
                    ied_name = IEDList.get(0).getAttributeValue("name");
                    ied_desc = IEDList.get(0).getAttributeValue("desc");
                    Element DataTypeTemplates = root.getChild(
                            "DataTypeTemplates", ns);
                    for (Element el0 : IEDList) {
                        Element AccessPoint = el0.getChild("AccessPoint", ns);
                        Element Server = AccessPoint.getChild("Server", ns);
                        List<Element> LDevice = Server.getChildren("LDevice",
                                ns);
                        for (Element el_ld : LDevice) {
                            // 获取ldname和desc
                            ld_inst_name = ied_name
                                    + el_ld.getAttributeValue("inst");
                            List<Element> lnList = el_ld.getChildren("LN", ns);
                            // 开始遍历每个LN
                            for (Element el20 : lnList) {
                                ln_inst_name = el20
                                        .getAttributeValue("lnClass")
                                        + el20.getAttributeValue("inst");
                                InstNodeEntity ent = new InstNodeEntity();
                                ent.setLd_inst_name(ld_inst_name + ";"
                                        + ln_inst_name);
                                entityList.add(ent);
                            }
                        }
                    }
                }
            }
        }
        HtmlUtil.writerJson(response, entityList);
    }

    @RequestMapping("/JdListTODB")
    public void JdListTODB(HttpServletResponse response,
                           HttpServletRequest request) throws Exception {
        String jsonList = request.getParameter("list");
        JSONArray jsonArray = JSON.parseArray(jsonList);
        int iSize = jsonArray.size();
        int jj = 0;
        if (iSize == 0) {
            return;
        }
        for (int i = 0; i < iSize; i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            InstNodeEntity temp_entity = new InstNodeEntity();
            ZJ103Entity ZJ103Entity = new ZJ103Entity();
            ZJ103Entity Ln_inst = new ZJ103Entity();
            temp_entity.setIed_name(jsonObj.get("ied_name").toString());
            temp_entity.setIed_desc(jsonObj.get("ied_desc").toString()
                    .replace('△', 'D'));
            temp_entity.setLd_inst_name(jsonObj.get("ld_inst_name").toString());
            String ld_inst_desc = jsonObj.get("ld_inst_desc").toString();
            if (ld_inst_desc == null || ld_inst_desc == "null")
                ld_inst_desc = "";
            temp_entity.setLd_inst_desc(ld_inst_desc);
            temp_entity.setLn_inst_name(jsonObj.get("ln_inst_name").toString());
            String ln_inst_desc = jsonObj.get("ln_inst_desc").toString();
            if (ln_inst_desc == null || ln_inst_desc == "null")
                ln_inst_desc = "";
            temp_entity.setLn_inst_desc(ln_inst_desc);
            Ln_inst.setIEC61850LD_LN(temp_entity.getLn_inst_name().substring(0,
                    4));
            int ii = 1;
            String i_Device = systemConfigurationService.ZJ103DeviceIDMax();
            if (i_Device != null) {
                ii = Integer.parseInt(i_Device.substring(1, 5)) + 1;
            }
            /*
             * String Dt=systemConfigurationService.ZJ103_ln(Ln_inst);
             * if(Dt!=null){ ZJ103Entity.setDeviceType(Integer.parseInt(Dt));
             * }else{ ZJ103Entity.setDeviceType(0); }
             */

            ZJ103Entity.setIEC61850LD_LN(temp_entity.getLd_inst_name() + "/"
                    + temp_entity.getLn_inst_name());
            String DeviceID = null;
            if (ii < 10) {
                DeviceID = "D000" + ii;
            } else if (ii < 100) {
                DeviceID = "D00" + ii;
            } else if (ii < 1000) {
                DeviceID = "D0" + ii;
            } else if (ii < 10000) {
                DeviceID = "D" + ii;
            } else {
                DeviceID = "D" + ii;
            }
            ZJ103Entity.setDeviceID(DeviceID);
            // 更新至数据库
            int count = systemConfigurationService.getLnCount(temp_entity);
            int count_103 = systemConfigurationService
                    .getZJ103CountByln(ZJ103Entity);
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

    public static String readCfgFile(String path, List<YclysEntity> list) {
        System.out.println(path);
        String cfg = "/datamap.cfg";
        path = path + cfg;
        StringBuilder sb = new StringBuilder();
        // 由于list在循环中不好删除,建立下标数组,用于记录是否存在
        int len = list.size();
        boolean[] addFlags = new boolean[len];
        try {
            File file = new File(path);
            FileInputStream fs = new FileInputStream(file);
            InputStreamReader read = new InputStreamReader(fs, "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            System.out.println(path);
            File outfile = new File(path);
            String line;
            String line2;
            String line3;
            String line4;
            String line5;
            List<String> osList = new ArrayList<String>();
            int nn = 1;
            while ((line = reader.readLine()) != null) {
                // 遍历修改已经存在的修改完后从list中移除
                for (int i = 0; i < list.size(); i++) {
                    if (line.indexOf(list.get(i).getLnType()) > -1) {// 表示已经存在,则添加
                        // 已经查到待操作行,正在进行操作
                        if (list.get(i).getLnType().indexOf("ST") > -1) {
                            String typeEnd = String
                                    .valueOf(list
                                            .get(i)
                                            .getLnType()
                                            .charAt(list.get(i).getLnType()
                                                    .length() - 1));
                            String ins = "";
                            if (typeEnd.equals("q") || typeEnd.equals("t")) {
                                if ("".equals(list.get(i).getSel_zh())
                                        || "".equals(list.get(i).getSel_dxh())) {
                                    ins = "\"FV$\"";
                                } else {
                                    ins = "\"DI$" + list.get(i).getSel_zh()
                                            + "$" + list.get(i).getSel_dxh()
                                            + "$" + typeEnd + "\"";
                                }
                            } else {
                                if ("".equals(list.get(i).getSel_zh())
                                        || "".equals(list.get(i).getSel_dxh())) {
                                    ins = "\"FV$\"";
                                } else {
                                    ins = "\"DI$" + list.get(i).getSel_zh()
                                            + "$" + list.get(i).getSel_dxh()
                                            + "$v\"";
                                }
                            }
                            line = line.substring(0, line.indexOf("\""))
                                    + ins
                                    + line.substring(line.lastIndexOf("\"") + 1);
                        } else if (list.get(i).getLnType().indexOf("MX") > -1) {
                            String typeEnd = String
                                    .valueOf(list
                                            .get(i)
                                            .getLnType()
                                            .charAt(list.get(i).getLnType()
                                                    .length() - 1));
                            String ins = "";
                            if (typeEnd.equals("q") || typeEnd.equals("t")) {
                                if ("".equals(list.get(i).getSel_zh())
                                        || "".equals(list.get(i).getSel_dxh())) {
                                    ins = "\"FV$\"";
                                } else {
                                    ins = "\"AI$" + list.get(i).getSel_zh()
                                            + "$" + list.get(i).getSel_dxh()
                                            + "$" + typeEnd + "\"";
                                }
                            } else {
                                if ("".equals(list.get(i).getSel_zh())
                                        || "".equals(list.get(i).getSel_dxh())) {
                                    ins = "\"FV$\"";
                                } else {
                                    ins = "\"AI$" + list.get(i).getSel_zh()
                                            + "$" + list.get(i).getSel_dxh()
                                            + "$v\"";
                                }
                            }
                            line = line.substring(0, line.indexOf("\""))
                                    + ins
                                    + line.substring(line.lastIndexOf("\"") + 1);
                        } else if (list.get(i).getLnType().indexOf("CO") > -1) {
                            String typeEnd = String
                                    .valueOf(list
                                            .get(i)
                                            .getLnType()
                                            .charAt(list.get(i).getLnType()
                                                    .length() - 1));
                            String ins = "";
                            if (typeEnd.equals("q") || typeEnd.equals("t")) {
                                if ("".equals(list.get(i).getSel_zh())
                                        || "".equals(list.get(i).getSel_dxh())) {
                                    ins = "\"FV$\"";
                                } else {
                                    ins = "\"FV$" + list.get(i).getSel_zh()
                                            + "$" + list.get(i).getSel_dxh()
                                            + "$" + typeEnd + "\"";
                                }
                            } else {
                                if ("".equals(list.get(i).getSel_zh())
                                        || "".equals(list.get(i).getSel_dxh())) {
                                    ins = "\"FV$\"";
                                } else {
                                    ins = "\"FV$" + list.get(i).getSel_zh()
                                            + "$" + list.get(i).getSel_dxh()
                                            + "$v\"";
                                }
                            }
                            line = line.substring(0, line.indexOf("\""))
                                    + ins
                                    + line.substring(line.lastIndexOf("\"") + 1);
                        } else if (list.get(i).getLnType().indexOf("SGCB") > -1) {
                            String ins = "";
                            if ("".equals(list.get(i).getSel_zh())
                                    || "".equals(list.get(i).getSel_dxh())) {
                                ins = "\"FV$\"";
                            } else {
                                ins = "\"AI$" + list.get(i).getSel_zh() + "$"
                                        + list.get(i).getSel_dxh() + "$v\"";
                            }
                            line = line.substring(0, line.indexOf("\""))
                                    + ins
                                    + line.substring(line.lastIndexOf("\"") + 1);
                        } else if (list.get(i).getLnType().indexOf("SG") > -1) {
                            String ins = "";
                            if ("".equals(list.get(i).getSel_zh())
                                    || "".equals(list.get(i).getSel_dxh())) {
                                ins = "\"FV$\"";
                            } else {
                                ins = "\"AI$" + list.get(i).getSel_zh() + "$"
                                        + list.get(i).getSel_dxh() + "$v\"";
                            }
                            line = line.substring(0, line.indexOf("\""))
                                    + ins
                                    + line.substring(line.lastIndexOf("\"") + 1);
                            // String line2=line.replaceAll("SG", "SE");
                            // line=line+"\r\n"+line2;
                        }
                        ;
                        addFlags[i] = true;
                    }
                }
                // if(list.get(0).getLnType().indexOf("SGCB")>-1){
                // if(nn==1){
                // String ins="AI$0$20"+nn+"$v";
                // int abc=line.indexOf("\"");
                // line2=line.substring(0,
                // line.indexOf("\""))+"\tLLN0$SP$SGCB$NumOfSG\t\"";
                // System.out.println(line2);
                // line3=ins;
                // System.out.println(line3);
                // line4=line.substring(line.lastIndexOf("\"")+1);
                // System.out.println(line4);
                // }
                // if(nn==2){
                // String ins="AI$0$20"+nn+"$v";
                // line2=line.substring(0,
                // line.indexOf("\\"))+"\tLLN0$SP$SGCB$ActSG\t\"";
                // System.out.println(line2);
                // line3=ins;
                // System.out.println(line3);
                // line4=line.substring(line.lastIndexOf("\"")+1);
                // System.out.println(line4);
                // }
                // if(nn==3){
                // String ins="AI$0$20"+nn+"$v";
                // line2=line.substring(0,
                // line.indexOf("\\"))+"\tLLN0$SP$SGCB$EditSG\t\"";
                // System.out.println(line2);
                // line3=ins;
                // System.out.println(line3);
                // line4=line.substring(line.lastIndexOf("\"")+1);
                // System.out.println(line4);
                // }
                // if(nn==4){
                // String ins="AI$0$20"+nn+"$v";
                // line2=line.substring(0,
                // line.indexOf("\\"))+"\tLLN0$SP$SGCB$CnfEdit\t\"";
                // System.out.println(line2);
                // line3=ins;
                // System.out.println(line3);
                // line4=line.substring(line.lastIndexOf("\"")+1);
                // System.out.println(line4);
                // }
                // if(nn==5){
                // String ins="AI$0$20"+nn+"$v";
                // line2=line.substring(0,
                // line.indexOf("\\"))+"\tLLN0$SP$SGCB$LActTm\t\"";
                // System.out.println(line2);
                // line3=ins;
                // System.out.println(line3);
                // line4=line.substring(line.lastIndexOf("\"")+1);
                // System.out.println(line4);
                // }
                //
                // nn++;
                // }
                osList.add(line + "\n");
            }
            // 剩下的list中元素做添加操作
            // 取出已经做过修改操作的
            Map<String, String> map = UrlUtil.getMap();
            for (int i = 0; i < list.size(); i++) {
                if (addFlags[i] == true) {
                    continue;
                }
                String _lntype = list.get(i).getLnType();
                String _temp = list.get(i).getIedName() + "\t" + _lntype;
                // 获取lntype 类型信息
                String _type = _lntype.substring(_lntype.lastIndexOf("$") + 1);
                String ins = "";
                if (_type.equals("q") || _type.equals("t")) {
                    if ("".equals(list.get(i).getSel_zh())
                            || "".equals(list.get(i).getSel_dxh())) {
                        ins = "\t\"FV$\"\ttype=" + map.get(_type);
                    } else {
                        ins = "\t\"DI$" + list.get(i).getSel_zh() + "$"
                                + list.get(i).getSel_dxh() + "$" + _type
                                + "\"\ttype=" + map.get(_type);
                    }
                } else {
                    if ("".equals(list.get(i).getSel_zh())
                            || "".equals(list.get(i).getSel_dxh())) {
                        ins = "\t\"FV$\"\ttype=" + map.get(_type);
                    } else {
                        ins = "\t\"DI$" + list.get(i).getSel_zh() + "$"
                                + list.get(i).getSel_dxh() + "$v\"\ttype="
                                + map.get(_type);
                    }
                }
                if (list.get(i).getLnType().indexOf("ST") > -1) {
                    if (_type.equals("q") || _type.equals("t")) {
                        if ("".equals(list.get(i).getSel_zh())
                                || "".equals(list.get(i).getSel_dxh())) {
                            ins = "\t\"FV$\"\ttype=" + map.get(_type);
                        } else {
                            ins = "\t\"DI$" + list.get(i).getSel_zh() + "$"
                                    + list.get(i).getSel_dxh() + "$" + _type
                                    + "\"\ttype=" + map.get(_type);
                        }
                    } else {
                        if ("".equals(list.get(i).getSel_zh())
                                || "".equals(list.get(i).getSel_dxh())) {
                            ins = "\t\"FV$\"\ttype=" + map.get(_type);
                        } else {
                            ins = "\t\"DI$" + list.get(i).getSel_zh() + "$"
                                    + list.get(i).getSel_dxh() + "$v\"\ttype="
                                    + map.get(_type);
                        }
                    }
                    // ins="\t\"DI$"+list.get(i).getSel_zh()+"$"+list.get(i).getSel_dxh()+"$v\"\ttype="+map.get(_type);
                } else {
                    if (_type.equals("q") || _type.equals("t")) {
                        if ("".equals(list.get(i).getSel_zh())
                                || "".equals(list.get(i).getSel_dxh())) {
                            ins = "\t\"FV$\"\ttype=" + map.get(_type);
                        } else {
                            ins = "\t\"AI$" + list.get(i).getSel_zh() + "$"
                                    + list.get(i).getSel_dxh() + "$" + _type
                                    + "\"\ttype=" + map.get(_type);
                        }
                    } else {
                        if ("".equals(list.get(i).getSel_zh())
                                || "".equals(list.get(i).getSel_dxh())) {
                            ins = "\t\"FV$\"\ttype=" + map.get(_type);
                        } else {
                            ins = "\t\"AI$" + list.get(i).getSel_zh() + "$"
                                    + list.get(i).getSel_dxh() + "$v\"\ttype="
                                    + map.get(_type);
                        }
                    }
                    // ins="\t\"AI$"+list.get(i).getSel_zh()+"$"+list.get(i).getSel_dxh()+"$v\"\ttype="+map.get(_type);
                }
                osList.add(_temp + ins + "\n");
                // if(_lntype.indexOf("SG")>-1){
                // _temp=_temp.replaceAll("SG", "SE");
                // osList.add(_temp+ins+"\r\n");
                // }
            }
            reader.close();
            read.close();
            fs.close();
            OutputStreamWriter os = new OutputStreamWriter(
                    new FileOutputStream(outfile), "UTF-8");
            PrintWriter pw = new PrintWriter(os);
            for (int i = 0; i < osList.size(); i++) {
                pw.write(osList.get(i).toString());
            }
            pw.close();
            os.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @RequestMapping("/updataDB")
    public void updataDB(HttpServletResponse response,
                         HttpServletRequest request) throws Exception {
        String jsonList = request.getParameter("list");
        JSONArray jsonArray = JSON.parseArray(jsonList);
        int iSize = jsonArray.size();
        if (iSize == 0) {
            return;
        }
        for (int i = 0; i < iSize; i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Data_instEntity temp_entity = new Data_instEntity();
            // 这里的dh其实是组号,dxh是点序号
            int _zh = Integer.parseInt(jsonObj.get("dh").toString());
            int _dxh = Integer.parseInt(jsonObj.get("dxh").toString());
            // 过滤相同点序号
            if (i == 0) {

            } else if (jsonArray.getJSONObject(i).get("dxh")
                    .equals(jsonArray.getJSONObject(i - 1).get("dxh"))) {
                continue;
            }
            // yxid算法
            temp_entity.setYx_id((_zh * 2048) + _dxh);
            temp_entity.setLd_inst_name(jsonObj.get("iedName").toString());
            // 截取ln
            String type = jsonObj.get("type").toString();
            String desc = jsonObj.get("desc").toString();
            String[] arr_type = type.split("\\$");
            temp_entity.setLn_inst_name(arr_type[0]);
            temp_entity.setYx_refname(arr_type[2]);
            temp_entity.setDesc(desc);
            // //是否已经存在id从而进行修改或者添加操作
            if (arr_type[1].equals("ST")) {
                // st则对yx表进行操作
                int count = systemConfigurationService.getyxCount(temp_entity);
                if (count > 0) {
                    // 已存在id则修改
                    systemConfigurationService.updateyx(temp_entity);
                } else {
                    systemConfigurationService.insertyx(temp_entity);
                }
            }
            if (arr_type[1].equals("MX")) {
                // st则对yx表进行操作
                int count = systemConfigurationService.getycCount(temp_entity);
                if (count > 0) {
                    // 已存在id则修改
                    // systemConfigurationService.updateyc(temp_entity);
                    Map<String, Object> jsonMap = new HashMap<String, Object>();
                    jsonMap.put("message", "false");
                    HtmlUtil.writerJson(response, jsonMap);
                } else {
                    systemConfigurationService.insertyc(temp_entity);
                }
            }
            if (arr_type[1].equals("SG")) {
                temp_entity.setFc("SG");
                int count = systemConfigurationService.getykCount(temp_entity);
                if (count > 0) {
                    // 已存在id则修改
                    systemConfigurationService.updateyk(temp_entity);
                } else {
                    systemConfigurationService.insertyk(temp_entity);
                }
            }
            if (arr_type[1].equals("SE")) {
                temp_entity.setFc("SE");
                int count = systemConfigurationService.getykCount(temp_entity);
                if (count > 0) {
                    // 已存在id则修改
                    systemConfigurationService.updateyk(temp_entity);
                } else {
                    systemConfigurationService.insertyk(temp_entity);
                }
            }
            if (arr_type[1].equals("SP")) {
                temp_entity.setYx_refname(arr_type[3]);
                // st则对yx表进行操作
                int count = systemConfigurationService.getykCount(temp_entity);
                if (count > 0) {
                    // 已存在id则修改
                    systemConfigurationService.updateyk(temp_entity);
                } else {
                    systemConfigurationService.insertSGCByk(temp_entity);
                }
            }
            if (arr_type[1].equals("CO")) {
                temp_entity.setFc("CO");
                temp_entity.setYx_refname(type);
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

    @RequestMapping("/getIcdExistFlag")
    public void getIcdExistFlag(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String file_path = "";
        file_path = UrlUtil.getUrlUtil().getOsicfg();
        String fileName = request.getParameter("dIRName");
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        File file = new File(file_path + fileName + File.separator);
        // 如果文件夹不存在则直接返回
        if (!file.exists() && !file.isDirectory()) {
            jsonMap.put("message", "false");
            HtmlUtil.writerJson(response, jsonMap);
        } else {
            jsonMap.put("message", "true");
            HtmlUtil.writerJson(response, jsonMap);
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

    @RequestMapping("/uploadIcd")
    public void uploadIcd(HttpServletResponse response,
                          HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("UTF-8");
        // 根据icd文件创建解析出来的iedname和IP写入数据库和Osifg.xml
        String dir_iedName = "";
        String dir_ip = "";
        // 获取文件夹名
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(124 * 1024 * 1024);//设置缓冲区大小
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置总文件大小
        upload.setSizeMax(1024 * 1024 * 1024);
        // 设置单个文件上传的最大值
        upload.setFileSizeMax(1024 * 1024 * 1024);
        //设置编码格式
        upload.setHeaderEncoding("UTF-8");
        try {
            List<FileItem> fileList = upload.parseRequest(request);// 这里可以有多个文件，（解析请求正文内容）
            InputStream is = null;
            String fileName = null;
            System.out.println(fileList.size());
            if (fileList.size() <= 999999) {
                for (FileItem item : fileList) {
                    fileName = item.getName();// 上传文件的名字
                    is = item.getInputStream();// 上传文件流
                    System.out.println(fileName + ":fileName1");
                    BufferedReader in2 = new BufferedReader(
                            new InputStreamReader(is, "utf-8"));
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
                    createDir(dir_iedName, fileName);
                    new LEDConfigurationController().commitled(dir_iedName,
                            dir_ip, LEDService);
                    File icdFile = new File(UrlUtil.getUrlUtil().getOsicfg()
                            + dir_iedName + File.separator + fileName);
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
                    HtmlUtil.writerJson(response, "创建成功!");
                }
            } else {
//             System.out.println("数量太大");
                throw new Exception("数量太大");
            }
            // 根据icd文件创建解析出来的iedname和IP写入数据库和Osifg.xml

        } catch (FileUploadException e) {
            e.printStackTrace();
        }
    }

    // 从数据库读取点序号
    @RequestMapping("/getDxhFromDB")
    public void getDxhFromDB(YclysEntity entity, HttpServletResponse response)
            throws Exception {
        List<YclysEntity> lists = new ArrayList<YclysEntity>();
        if (entity.getFc().equals("ST")) {
            lists = systemConfigurationService.getyxByld(entity);
        } else if (entity.getFc().equals("MX")) {
            lists = systemConfigurationService.getycByld(entity);
        } else if (entity.getFc().equals("SG/SE")) {
            lists = systemConfigurationService.getykByld(entity);
        } else if (entity.getFc().equals("CO")) {
            entity.setLdinst(entity.getLdinst().replace("MONT", ""));
            lists = systemConfigurationService.getykByld(entity);
        }

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        // jsonMap.put("rows",lists);
        HtmlUtil.writerJson(response, lists);
    }

    // 从数据库读取点序号
    @RequestMapping("/getDxhFromCFG")
    public void getDxhFromCFG(YclysEntity entity, HttpServletResponse response)
            throws Exception {
        List<YcDataInstEntity> lists = new ArrayList<YcDataInstEntity>();
        String index = "";
        // 获取文件夹路径
        String cfg = "/datamap.cfg";
        String file_path = UrlUtil.getUrlUtil().getOsicfg()
                + entity.getLdinst().substring(0,
                entity.getLdinst().indexOf("MONT"))
                + cfg;
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
                    lists.add(temp);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        // jsonMap.put("rows",lists);
        HtmlUtil.writerJson(response, lists);
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

    private void createDir(String dirName, String fileName) {
        // 获取根目录
        String path = UrlUtil.getUrlUtil().getOsicfg() + dirName
                + File.separator;
        File file = new File(path);
        // 目录不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        // 如果不存在则创建 startup.cfg
        File startupcfg = new File(path + "startup.cfg");
        // 目录不存在则创建
        if (!startupcfg.exists()) {
            try {
                startupcfg.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 向startup.cfg写入数据
        PrintWriter pw = null;
        OutputStreamWriter os;
        try {
            os = new OutputStreamWriter(new FileOutputStream(startupcfg),
                    "UTF-8");
            pw = new PrintWriter(os);

            // 编辑内容
            List<String> osList = new ArrayList<String>();
            osList.add("SCLFileName\t" + fileName + "\n");
            osList.add("IEDName\t" + dirName + "\n");
            osList.add("AccessPointName\tS1\n");
            osList.add("ReportScanRate\t2.0\n");
            osList.add("#NOTE: BRCBBufferSize was not configurable before. Now it is.\n");
            osList.add("BRCBBufferSize\t1000000\n");
            osList.add("#NOTE: The old function \"scl2_ld_create_all\" ignores LogScanRateSeconds and LogMaxEntries.\n");
            osList.add("#      You must use the new function \"scl2_ld_create_all_scd\" to use these.\n");
            osList.add("LogScanRateSeconds\t2.0\n");
            osList.add("LogMaxEntries\t1000\n");
            for (int i = 0; i < osList.size(); i++) {
                Object aa = osList.get(i).toString();
                System.out.println(aa);
                pw.write(osList.get(i).toString());
            }
            os.close();
            pw.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果datamap不存在则创建
        File datamapcfg = new File(path + "datamap.cfg");
        // 目录不存在则创建
        if (!datamapcfg.exists()) {
            try {
                datamapcfg.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 创建icd文件
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

    private void writeExcel(String filePath) throws Exception {
        OutputStream os = null;
        try {
            // 构建Workbook对象
            os = new FileOutputStream(filePath);
            WritableWorkbook wwb = Workbook.createWorkbook(os);
            // 构建Excel sheet
            WritableSheet sheet = wwb.createSheet("test write sheet", 0);
            // 设置标题格式
            WritableFont wfTitle = new WritableFont(
                    WritableFont.ARIAL, 18, WritableFont.BOLD, true);
            WritableCellFormat wcfTitle = new WritableCellFormat(wfTitle);
            // 设置水平对齐方式
            wcfTitle.setAlignment(Alignment.CENTRE);
            // 设置垂直对齐方式
            wcfTitle.setVerticalAlignment(VerticalAlignment.CENTRE);
            // 设置是否自动换行
            wcfTitle.setWrap(true);
            // 合并A1->C2
            // sheet.mergeCells(0, 0, 2, 1);
            // Label titleCell = new Label(0, 0, "Title Cell ", wcfTitle);
            // sheet.addCell(titleCell);
            // WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
            // WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
            // Colour.BLUE);
            // WritableCellFormat wcf = new WritableCellFormat(wf);

            // A3
            Label labelCell = new Label(0, 2, "Label Cell ");
            sheet.addCell(labelCell);
            // B3
            // Label labelCellFmt = new Label(1, 2,
            // "Label Cell with WritableCellFormat ", wcf);
            // sheet.addCell(labelCellFmt);
            // A4 添加jxl.write.Number对象
            jxl.write.Number labelN = new jxl.write.Number(0, 3, 3.1415926);
            sheet.addCell(labelN);
            // B4 添加Number对象 jxl.write.NumberFormat
            // NumberFormat nf = new NumberFormat("#.##");
            // WritableCellFormat wcfN = new WritableCellFormat(nf);
            // jxl.write.Number labelNF = new jxl.write.Number(1, 3, 3.1415926,
            // wcfN);
            // sheet.addCell(labelNF);
            // A5 添加jxl.write.Boolean对象
            // jxl.write.Boolean labelB = new jxl.write.Boolean(0, 4, true);
            // sheet.addCell(labelB);
            // A6 添加 jxl.write.DateTime对象
            // jxl.write.DateTime labelDT = new jxl.write.DateTime(0, 5,
            // new Date());
            // sheet.addCell(labelDT);
            // B6 添加DateTime对象 jxl.write.DateFormat
            // jxl.write.DateFormat df = new jxl.write.DateFormat(
            // "yyyy-MM-dd HH:mm:ss");
            // WritableCellFormat wcfDF = new WritableCellFormat(df);
            // jxl.write.DateTime labelDTF = new jxl.write.DateTime(1, 5,
            // new Date(), wcfDF);
            // sheet.addCell(labelDTF);
            // 先调用write();再调用close();
            wwb.write();
            wwb.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                os.close();
            }
        }
    }


}
