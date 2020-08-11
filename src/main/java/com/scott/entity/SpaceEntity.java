package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

/**
 *
 * <br>
 * <b>功能：</b>设备实体类<br>
 * <b>作者：</b>xjiawei<br>
 * <b>日期：</b> 9, 7, 2015 <br>
 */
@Data
public class SpaceEntity extends BaseEntity {
	private String GIS;//GIS区域
	private String ZYB;//站用变
	private String ZLC;//直流场
	private String DD;//低端换流变
	private String GD;//高端换流变
	private String JL;//交流滤波场
}

