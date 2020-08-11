package com.jeecg.page;

import com.base.page.BasePage;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class SysMenuModel extends BasePage {
	

	private Integer id;//   主键
	private String name;//   菜单名称
	private String url;//   系统url
	private Integer parentId;//   父id 关联sys_menu.id
	private Integer deleted;//   是否删除,0=未删除，1=已删除
	private Timestamp createTime;//   创建时间
	private Timestamp updateTime;//   修改时间
	private Integer rank;//   排序
	private String actions; //注册Action 按钮|分隔
}
