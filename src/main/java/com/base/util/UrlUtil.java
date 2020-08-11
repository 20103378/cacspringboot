package com.base.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/*
 * 专门用于存放61850信息
 */
public class UrlUtil {
	private String Osicfg;
	private String cfgpath;
	private String yspinf;
	private String sf6inf;
	private String blqinf;
	private String jfinf;
	private String scominf;
	private String locip;
	private String mapPath;
	private String xlsUrl;
	//项目名
	private String proj;
	private static UrlUtil urlUtil;
	//单例防止重复读取
	public static synchronized UrlUtil getUrlUtil() {
		 if (urlUtil == null) {
			 urlUtil = new UrlUtil();
		 }
		 return urlUtil;
	}
	public UrlUtil() {
		super();
		//读取配置文件
		ResourceBundle resourcesTable = ResourceBundle.getBundle("config");
		this.Osicfg = resourcesTable.getString("Osicfg");
		this.cfgpath = resourcesTable.getString("cfgpath");
		this.yspinf = resourcesTable.getString("yspinf");
		this.sf6inf = resourcesTable.getString("sf6inf");
		this.blqinf = resourcesTable.getString("blqinf");
		this.jfinf = resourcesTable.getString("jfinf");
		this.scominf = resourcesTable.getString("scominf");
		this.locip = resourcesTable.getString("locIp");
		this.proj = resourcesTable.getString("proj");
//		this.mapPath = resourcesTable.getString("mapPath");
//		this.xlsUrl = resourcesTable.getString("xlsUrl");
	}

//	public final static String Osicfg="/CAC/iec61850/";
//	public final static String Osicfg="C:/iec61850/";  //61850路径
//	public final static String yspinf="/CAC/iec61850/fileserv/HZSH_YSP024_s/";
//	public final static String sf6inf="/CAC/iec61850/fileserv/HZSH_SF6121/";
//	public final static String blqinf="/CAC/iec61850/fileserv/HZSH_BLQ192/";
//	public final static String jfinf="/CAC/iec61850/fileserv/HZSH_JF034/";
//	public final static String scominf="/CAC/iec61850/fileserv/SCOM3000/";
//	public final static String cfgpath="/datamap.cfg"; //cfg写入点路径

	public String getOsicfg() {
		return Osicfg;
	}
	public String getXlsUrl() {
		return xlsUrl;
	}
	public void setXlsUrl(String xlsUrl) {
		this.xlsUrl = xlsUrl;
	}
	public String getMapPath() {
		return mapPath;
	}
	public void setMapPath(String mapPath) {
		this.mapPath = mapPath;
	}
	public static void setUrlUtil(UrlUtil urlUtil) {
		UrlUtil.urlUtil = urlUtil;
	}
	public String getProj() {
		return proj;
	}
	public void setProj(String proj) {
		this.proj = proj;
	}
	public String getLocip() {
		return locip;
	}
	public void setLocip(String locip) {
		this.locip = locip;
	}
	public void setOsicfg(String osicfg) {
		Osicfg = osicfg;
	}
	public String getCfgpath() {
		return cfgpath;
	}
	public void setCfgpath(String cfgpath) {
		this.cfgpath = cfgpath;
	}
	public String getYspinf() {
		return yspinf;
	}
	public void setYspinf(String yspinf) {
		this.yspinf = yspinf;
	}
	public String getSf6inf() {
		return sf6inf;
	}
	public void setSf6inf(String sf6inf) {
		this.sf6inf = sf6inf;
	}
	public String getBlqinf() {
		return blqinf;
	}
	public void setBlqinf(String blqinf) {
		this.blqinf = blqinf;
	}
	public String getJfinf() {
		return jfinf;
	}
	public void setJfinf(String jfinf) {
		this.jfinf = jfinf;
	}
	public String getScominf() {
		return scominf;
	}
	public void setScominf(String scominf) {
		this.scominf = scominf;
	}
	public static Map<String,String>  getMap(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("q", "BVstring13");
		map.put("t", "Utctime");
		map.put("stVal", "Byte");
		map.put("f", "Float");
		map.put("i", "Long");
		map.put("f", "Float");
		map.put("ctlModel", "Byte");
		map.put("vendor", "Vstring255");
		map.put("swRev", "Vstring255");
		map.put("d", "Vstring255");
		map.put("LActTm", "Utctime");
		map.put("CnfEdit", "Bool");
		map.put("EditSG", "Ubyte");
		map.put("ActSG", "Ubyte");
		map.put("NumOfSG", "Ubyte");
		map.put("setVal", "Long");
		//CO中使用的
		map.put("ctlVal", "Bool");
		map.put("orCat", "Byte");
		map.put("orIdent", "OVstring64");
		map.put("ctlNum", "Ubyte");
		map.put("T", "Utctime");
		map.put("Test", "Bool");
		map.put("Check", "BVstring2");
		return map;
	}
}
