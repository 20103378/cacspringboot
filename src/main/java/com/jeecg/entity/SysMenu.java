package com.jeecg.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class SysMenu extends BaseEntity {
	
	
	private Integer id;//   主键
	private String name;//   菜单名称
	private String url;//   系统url
	private Integer parentId;//   父id 关联sys_menu.id
	private Integer deleted;//   是否删除,0=未删除，1=已删除
	private java.sql.Timestamp createTime;//   创建时间
	private java.sql.Timestamp updateTime;//   修改时间
	private Integer rank;//   排序
	private String actions; //注册Action 按钮|分隔
	
	private int subCount;//子菜单总数
	
	//菜单按钮
	private List<SysMenuBtn> btns;
}
