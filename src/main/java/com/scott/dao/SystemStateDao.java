package com.scott.dao;

import com.base.dao.BaseDao;
import com.scott.entity.ProcstateEntity;
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
public interface SystemStateDao<T> extends BaseDao<T> {

	/**
	 * cpu使用率
	 * @return
	 */
	 List<ProcstateEntity> getData();

	/**
	 * 内存使用率
	 * @return
	 */
	 List<ProcstateEntity> getOsData();

	/**
	 * 内存cpu使用率
	 * @return
	 */
	 List<ProcstateEntity> getCpData();
}
