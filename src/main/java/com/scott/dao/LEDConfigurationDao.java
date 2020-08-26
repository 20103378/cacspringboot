package com.scott.dao;


import com.base.dao.BaseDao;
import com.scott.entity.OsicfgEntity;
import org.apache.ibatis.annotations.Param;
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
	/**
	 * 更新update_iec61850_ied_inst
	 * @param entity
	 */
	 void update_iec61850_ied_inst(OsicfgEntity entity);

	/**
	 * 查询 iec61850_ied_inst
	 * @return
	 */
	 List<OsicfgEntity> find_iec61850_ied_inst();

	/**
	 * 查询最大的主键id号
	 * @return
	 */
	String getId();

	/**
	 * 查询iec61850_ied_inst是否已经使用
	 * @param arNameOld
	 * @return
	 */
	int findIfUsed(String arNameOld);

	/**
	 * 添加iec61850_ied_inst
	 * @param entity
	 */
	 void add_iec61850_ied_inst(OsicfgEntity entity);

	/**
	 * 通过arName同步删除iec61850_ied_inst数据库
	 * @param arName
	 */
	void del_iec61850_ied_inst(@Param("arName") String arName);


	/**
	 * 通过arName同步删除iec61850_yc_data_inst数据库
	 * @param arName
	 */
	void del_yc_inst(@Param("arName")String arName);

	/**
	 * 通过arName同步删除iec61850_yx_data_inst数据库
	 * @param arName
	 */
	 void del_yx_inst(@Param("arName")String arName);

	/**
	 *  通过arName同步删除iec61850_yk_data_inst数据库
	 * @param arName
	 */
	 void del_yk_inst(@Param("arName")String arName);
}
