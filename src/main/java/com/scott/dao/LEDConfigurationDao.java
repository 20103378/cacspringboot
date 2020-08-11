package com.scott.dao;


import com.base.dao.BaseDao;
import com.scott.entity.OsicfgEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * <br>
 * <b>功能：</b>JeecgPersonDao<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Repository
 public interface LEDConfigurationDao<T> extends BaseDao<T> {
	
	 void update_osicfg(OsicfgEntity entity);
	 List<OsicfgEntity> findOsicfg();
	 String getId();
	 int findIfUsed(String AR_Name_old);
	 void add_osicfg(OsicfgEntity entity);
	 void del_osicfg(OsicfgEntity entity);
	 void del_yc_inst(OsicfgEntity entity);
	 void del_yx_inst(OsicfgEntity entity);
	 void del_yk_inst(OsicfgEntity entity);
	 void del_yc_instAll();
	
}
