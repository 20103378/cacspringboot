package com.scott.service;


import com.base.service.BaseService;
import com.scott.dao.LEDConfigurationDao;
import com.scott.entity.OsicfgEntity;
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
@Service("LEDConfigurationService")
public class LEDConfigurationService<T> extends BaseService<T>{

	@Autowired
    private LEDConfigurationDao<T> dao;
	
	@Override
	public LEDConfigurationDao<T> getDao() {
		return dao;
	}
	
	public void update_iec61850_ied_inst(OsicfgEntity entity){
		getDao().update_iec61850_ied_inst(entity);
	}

	public String getId(){
		return getDao().getId();
	}
	public int findIfUsed(String arNameOld){
		return getDao().findIfUsed(arNameOld);
	}

	public List<OsicfgEntity> find_iec61850_ied_inst(){
		return getDao().find_iec61850_ied_inst();
	}
	public void add_iec61850_ied_inst(OsicfgEntity entity){
		getDao().add_iec61850_ied_inst(entity);
	}
	public void del_iec61850_ied_inst(String arName){
		getDao().del_iec61850_ied_inst(arName);
	}
	public void del_yc_inst(String arName){
		getDao().del_yc_inst(arName);
	}
	public void del_yx_inst(String arName){
		getDao().del_yx_inst(arName);
	}
	public void del_yk_inst(String arName){
		getDao().del_yk_inst(arName);
	}
	
	
}
