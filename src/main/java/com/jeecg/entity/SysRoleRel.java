package com.jeecg.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class SysRoleRel extends BaseEntity {
	
	private Integer roleId;//   角色主键 sys_role.id
	private Integer objId;//   关联主键 type=0管理sys_menu.id, type=1关联sys_user.id
	private Integer relType;//   关联类型 0=菜单,1=用户
	
	/**
 	 * 枚举
 	 * @author  www.jeecg.org
 	 *
 	 */
 	public static enum RelType {
		MENU(0, "菜单"), USER(1,"用户"),BTN(2,"按钮");
		public int key;
		public String value;
		private RelType(int key, String value) {
			this.key = key;
			this.value = value;
		}
		public static RelType get(int key) {
			RelType[] values = RelType.values();
			for (RelType object : values) {
				if (object.key == key) {
					return object;
				}
			}
			return null;
		}
	}
}
