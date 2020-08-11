package com.scott.dao;

import com.base.dao.BaseDao;
import com.scott.entity.CacCagConnStateEntity;
import com.scott.entity.CacExeStateEntity;
import com.scott.entity.CacIedConnStateEntity;
import com.scott.entity.TreeConnEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 
 * <br>
 * <b>功能：</b>JeecgPersonDao<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Repository
public interface ConnStateDao<T> extends BaseDao<T> {
	
	
	 List<TreeConnEntity> getEmuListData();
	
	 List<TreeConnEntity> getDataByID(String id);
	
	 List<TreeConnEntity> getHistoryData(Map<String, Object> jsonMap);

	 List<TreeConnEntity> getAMCListData();

	 List<TreeConnEntity> get104connData();

	//得到程序运行状态
	 List<CacExeStateEntity> getProgramListData();
	//cac与各装置通讯日志
	 List<CacIedConnStateEntity> getCACStateListData();
	//cac与cag通讯日志
	 List<CacCagConnStateEntity> getCAGStateListData();
}

