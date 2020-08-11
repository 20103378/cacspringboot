package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * <br>
 * <b>功能：</b>设备实体类<br>
 * <b>作者：</b>xjiawei<br>
 * <b>日期：</b> 9, 7, 2015 <br>
 */
@Data
@NoArgsConstructor
public class OsicfgEntity extends BaseEntity {
	private String iedid;
	private String NetAddr;
	private String AR_Name;
	private String AR_Name_old;
}

