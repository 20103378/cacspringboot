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
	
	public void update_osicfg(OsicfgEntity entity){
		getDao().update_osicfg(entity);
	}
	public List<OsicfgEntity> findOsicfg(){
		return getDao().findOsicfg();
	}

	public String getId(){
		return getDao().getId();
	}
	public int findIfUsed(String AR_Name_old){
		return getDao().findIfUsed(AR_Name_old);
	}
	
	public void add_osicfg(OsicfgEntity entity){
		getDao().add_osicfg(entity);
	}
	public void del_osicfg(OsicfgEntity entity){
		getDao().del_osicfg(entity);
	}
	public void del_yc_inst(OsicfgEntity entity){
		getDao().del_yc_inst(entity);
	}
	public void del_yx_inst(OsicfgEntity entity){
		getDao().del_yx_inst(entity);
	}
	public void del_yk_inst(OsicfgEntity entity){
		getDao().del_yk_inst(entity);
	}
	
	
}
