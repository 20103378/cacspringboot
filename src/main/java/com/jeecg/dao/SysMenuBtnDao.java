package com.jeecg.dao;

import com.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SysMenuBtn Mapper
 * @author Administrator
 *
 */
@Repository
public interface SysMenuBtnDao<T> extends BaseDao<T> {
	
	public List<T> queryByMenuid(Integer menuid);
	
	public List<T> queryByMenuUrl(String url); 
	
	public void deleteByMenuid(Integer menuid);
	
	public List<T> getMenuBtnByUser(Integer userid); 
	
	
	
	public List<T> queryByAll();
}
