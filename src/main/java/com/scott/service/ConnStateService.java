package com.scott.service;


import com.base.service.BaseService;
import com.scott.dao.ConnStateDao;
import com.scott.entity.CacCagConnStateEntity;
import com.scott.entity.CacExeStateEntity;
import com.scott.entity.CacIedConnStateEntity;
import com.scott.entity.TreeConnEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * <br>
 * <b>功能：</b>JeecgPersonService<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Service("ConnStateService")
public class ConnStateService<T> extends BaseService<T> {




	@Autowired
    private ConnStateDao<T> dao;

	@Override
	public ConnStateDao<T> getDao() {return dao;}

	public List<TreeConnEntity> getEmuListData()throws Exception{
		return getDao().getEmuListData();
	}

	public List<TreeConnEntity> getDataByID(String id)throws Exception{
		return getDao().getDataByID(id);
	}
	public List<TreeConnEntity> getHistoryData(Map<String, Object> jsonMap)throws Exception{
		return getDao().getHistoryData(jsonMap);
	}

	public List<TreeConnEntity> getAMCListData()throws Exception{
		return getDao().getAMCListData();
	}

	public List<TreeConnEntity> get104connData()throws Exception{
		return getDao().get104connData();
	}

	//得到程序运行状态
	public List<CacExeStateEntity> getProgramListData() {
		return getDao().getProgramListData();
	}
	//cac与各装置通讯日志
	public List<CacIedConnStateEntity> getCACStateListData() {
		return getDao().getCACStateListData();
	}
	//cac与cag通讯日志
	public List<CacCagConnStateEntity> getCAGStateListData() {
		return getDao().getCAGStateListData();
	}
}
