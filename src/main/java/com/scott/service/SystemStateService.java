package com.scott.service;


import com.base.service.BaseService;
import com.scott.dao.SystemStateDao;
import com.scott.entity.ProcstateEntity;
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
@Service("SystemStateService")
public class SystemStateService<T> extends BaseService<T> {

	@Autowired
    private SystemStateDao<T> dao;
	
	@Override
	public SystemStateDao<T> getDao() {return dao;}
	
	public List<ProcstateEntity> getData(){
		return getDao().getData();
	}
	public List<ProcstateEntity> getOsData(){
		return getDao().getOsData();
	}
	public List<ProcstateEntity> getCpData(){
		return getDao().getCpData();
	}
	
}
