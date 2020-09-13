package com.scott.service;

import com.base.service.BaseService;
import com.scott.dao.TreeDeviceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>JeecgPersonService<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Service("treeDeviceService")
public class TreeDeviceService<T,T1,T2,T3,T4,T5,T6> extends BaseService<T> {

/*	@Service("baseTbldevicechannelService")
	public class BaseTbldevicechannelService<T1,T2,T3> {
		private final static Logger log= Logger.getLogger(BaseTbldevicechannelService.class);

		@Autowired
	    private BaseTbldevicechannelDao<T1,T2,T3> dao;
		public BaseTbldevicechannelDao<T1,T2,T3> getDao() {
			return dao;
		}*/

	@Autowired
    private TreeDeviceDao<T,T1,T2,T3,T4,T5,T6> dao;

	@Override
	public TreeDeviceDao<T,T1,T2,T3,T4,T5,T6> getDao() {
		return dao;
	}
	//injectXml获取ID和Type
	public List<T> getIDandTypeToinjectXmlInSpace(){
		return getDao().getIDandTypeToinjectXmlInSpace();
	}
	//injectXml获取ID和Type
	public List<T> getIDandTypeToinjectXml(String space){
		return getDao().getIDandTypeToinjectXml(space);
	}
    public List<T> getPubspaceNameByType(String Type)
    {
        return getDao().getPubspaceNameByType(Type);
    }
	public List<Integer> getPubDeviceTypeList(){
		return getDao().getPubDeviceTypeList();
	}

	/*public List<T> getAmcImgList(java.util.Map<String, Object> param)
    {
	    return getDao().getAmcImgList(param);
	}*/
	/*public List<T> getOtherAmcImgList(String SampleTime)
    {
        return getDao().getOtherAmcImgList(SampleTime);
    }*/
	public List<T> getOtherImgList()
    {
        return getDao().getOtherImgList();
    }
	public List<String> getRemarkImgList()
    {
        return getDao().getRemarkImgList();
    }

    public List<T> getImgListBySelect(String Space,String DeviceType){
        return getDao().getImgListBySelect(Space,DeviceType);
    }
    public List<T> getOtherImgListBySelect(String space,String deviceType){
        return getDao().getOtherImgListBySelect(space,deviceType);
    }

	public List<T> getZoneEmuList()
	{
		List<T> t1=getDao().getZoneEmuList();
		return t1;
	}
    public List<T> getPubspaceName()
    {
        return getDao().getPubspaceName();
    }
    public List<T> getUnitinfo()
    {
        return getDao().getUnitinfo();
    }
    public List<T> getEquipmentName()
    {
        return getDao().getEquipmentName();
    }

	public List<T1> getEmuListDataID(java.util.Map<String, Object> param)
	{
		return getDao().getEmuListDataID(param);
	}

	public List<T1> getEmuListData()
	{
		return getDao().getEmuListData();
	}

	public List<T2> getSf6ListData(java.util.Map<String, Object> param)
	{
		return getDao().getSf6ListData(param);
	}
	public List<T3> getStomListData(java.util.Map<String, Object> param)
	{
		return getDao().getStomListData(param);
	}
	public List<T4> getSmoamListData(java.util.Map<String, Object> param)
	{
		return getDao().getSmoamListData(param);
	}
	public List<T5> getScomListData(java.util.Map<String, Object> param)
	{
		return getDao().getScomListData(param);
	}
	public List<T6> getSpdmData(java.util.Map<String, Object> param)
	{
		return getDao().getSpdmData(param);
	}

	public List<T2> getSf6AllData()
	{
		return getDao().getSf6AllData();
	}
	public List<T3> getStomAllData()
	{
		return getDao().getStomAllData();
	}
	public List<T4> getSmoamAllData()
	{
		return getDao().getSmoamAllData();
	}
	public List<T5> getScomAllData()
	{
		return getDao().getScomAllData();
	}
}
