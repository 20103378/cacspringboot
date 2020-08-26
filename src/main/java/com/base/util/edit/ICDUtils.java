package com.base.util.edit;

import com.base.util.UrlUtil;
import com.scott.entity.OsicfgEntity;
import com.scott.entity.TreeDeviceEntity;
import com.scott.entity.YclysEntity;
import com.scott.service.LEDConfigurationService;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ICDUtils {
    /**
     * 根据fileName创建文件目录,icd,datamap和startup
     * @param dirName
     * @param fileName
     */
    public static void createDir(String dirName, String fileName) {
        // 获取根目录
        String path =
                UrlUtil.getUrlUtil().getOsicfg() + dirName
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
     *
     * @param path
     * @param list
     * @return
     */
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

    /**
     *
     * @param resourcesPath
     * @param targetPath
     * @throws Exception
     */
    public static void compressedFile(String resourcesPath, String targetPath)
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
        ICDUtils.createCompressedFile(out, resourcesFile, "");
        out.close();
    }
    public static void createCompressedFile(ZipOutputStream out, File file, String dir)
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
    public static void writeExcel(String filePath) throws Exception {
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

    public static void commitled(OsicfgEntity entity, LEDConfigurationService<TreeDeviceEntity> server) throws Exception {
        InputStream inSt = new FileInputStream(UrlUtil.getUrlUtil().getOsicfg() + "osicfg.xml");
        SAXBuilder bulider = new SAXBuilder();
        Document document = bulider.build(inSt);
        Element root = document.getRootElement();        //获取根节点对象
        List<Element> networklist = root.getChildren("NetworkAddressing");

        int flag = server.findIfUsed(entity.getArNameOld());
        if (flag == 1) {
            //开始修改xml
            for (Element el : networklist) {
                List<Element> RemoteAddressList = el.getChildren("RemoteAddressList");
                for (Element el2 : RemoteAddressList) {
                    List<Element> RemoteAddress = el2.getChildren("RemoteAddress");
                    for (Element el3 : RemoteAddress) {
                        Element AR_Name = el3.getChild("AR_Name");
                        Element NetAddr = el3.getChild("NetAddr");
                        if (AR_Name.getText().equals(entity.getArNameOld())) {
                            AR_Name.setText(entity.getArName());
                            NetAddr.setText(entity.getNetAddr());
                        }
                    }
                }
            }
            //开始更新数据库
            server.update_iec61850_ied_inst(entity);
        }
        if (flag == 0) {
            Element new_el = new Element("RemoteAddress");
            Element new_AR_Name = new Element("AR_Name");
            new_AR_Name.setText(entity.getArName());
            Element new_AP_Title = new Element("AP_Title");
            new_AP_Title.setText("1 3 9999 23");
            Element new_AE_Qualifier = new Element("AE_Qualifier");
            new_AE_Qualifier.setText("23");
            Element new_Psel = new Element("Psel");
            new_Psel.setText("00 00 00 01");
            Element new_Ssel = new Element("Ssel");
            new_Ssel.setText("00 01");
            Element new_Tsel = new Element("Tsel");
            new_Tsel.setText("00 01");
            Element new_NetAddr = new Element("NetAddr");
            new_NetAddr.setText(entity.getNetAddr());
            new_NetAddr.setAttribute("Type", "IPADDR");
            new_el.addContent(new_AR_Name);
            new_el.addContent(new_AP_Title);
            new_el.addContent(new_AE_Qualifier);
            new_el.addContent(new_Psel);
            new_el.addContent(new_Ssel);
            new_el.addContent(new_Tsel);
            new_el.addContent(new_NetAddr);
            //开始添加xml
            for (Element el : networklist) {
                List<Element> remoteAddressList = el.getChildren("RemoteAddressList");
                int count=0;
                for (Element el2 : remoteAddressList) {
                    List<Element> remoteAddress = el2.getChildren("RemoteAddress");
                    for (Element el3 : remoteAddress) {
                        Element AR_Name = el3.getChild("AR_Name");
                        Element NetAddr = el3.getChild("NetAddr");
                        if (AR_Name.getText().equals(entity.getArNameOld())) {
                            AR_Name.setText(entity.getArName());
                            NetAddr.setText(entity.getNetAddr());
                            count++;
                        }
                    }
                }
                if(count==0){
                    remoteAddressList.get(0).addContent(new_el);
                }
            }
            //开始更新数据库
            //获取加入序号
            String id = getId(server);
            entity.setIedId(id);
            server.add_iec61850_ied_inst(entity);
        }
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        out.output(document, new FileOutputStream(UrlUtil.getUrlUtil().getOsicfg() + "osicfg.xml"));
    }
    public static String getId(LEDConfigurationService<TreeDeviceEntity> server){
        int i_id;
        String id = server.getId();
        if (id == null) {
            i_id = 1;
        } else {
            i_id = Integer.parseInt(id.substring(1)) + 1;
        }
        if (i_id < 10) {
            id = "I00" + i_id;
        } else if (i_id < 100) {
            id = "I0" + i_id;
        } else {
            id = "I" + i_id;
        }
        return id;
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


    public static Map getLdLnMap() throws Exception {
        Map map = new HashMap(16);
        // 创建遥测量映射对象列表
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
                            List<String> entityList = new ArrayList<>();
                            for (Element el20 : lnList) {
                                ln_inst_name = el20.getAttributeValue("lnClass") + el20.getAttributeValue("inst");
                                entityList.add(ld_inst_name + ";" + ln_inst_name);
                            }
                            map.put(ld_inst_name,entityList);
                        }
                    }
                }
            }
        }
        return map;
    }
    // 根据startup.cfg获取文件名
    private static String getXmlName(String dir) {
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
     * 解析IED文件
     * @param fileName
     * @return
     * @throws Exception
     */
    public static List getSelXml(String fileName) throws Exception {
        // 创建遥测量映射对象列表
        List<YclysEntity> entityList = new ArrayList<YclysEntity>();
        // 文档加载开始
        String xmlName = getXmlName(UrlUtil.getUrlUtil().getOsicfg() + fileName);
        SAXBuilder bulider = new SAXBuilder();
        InputStream inSt = new FileInputStream(UrlUtil.getUrlUtil().getOsicfg() + fileName + xmlName);
        Document document = bulider.build(inSt);
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
        return entityList;
    }
}
