package com.jeecg.dao;

import com.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SysRole Mapper
 * @author Administrator
 *
 */
@Repository
public interface SysRoleDao<T> extends BaseDao<T> {
	
	/**
	 *查询全部有效的权限
	 * @return
	 */
	public List<T> queryAllList();
	
	
	/**
	 *根据用户Id查询权限
	 * @return
	 */
	public List<T> queryByUserid(Integer userid);
}
