package com.jeecg.dao;

import com.base.dao.BaseDao;
import com.jeecg.entity.SysRoleRel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SysRoleRel Mapper
 * @author Administrator
 *
 */
@Repository
public interface SysRoleRelDao<T> extends BaseDao<T> {
	
	public void deleteByRoleId(java.util.Map<String, Object> param);
	
	public void deleteByObjId(java.util.Map<String, Object> param);
	
	
	public List<SysRoleRel> queryByRoleId(java.util.Map<String, Object> param);
	
	
	public List<SysRoleRel> queryByObjId(java.util.Map<String, Object> param);
	
	
}
