package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

/**
*
* <br>
* <b>功能：</b>测量点名称实体类<br>
* <b>作者：</b>gehanyun<br>
* <b>日期：</b> 2, 27, 2017 <br>
*/
@Data
public class Refname_descEntity extends BaseEntity {
	private String refname;	//测量点
	private String refdesc; // 描述

	public Refname_descEntity(String refname, String refdesc) {
		this.refname = refname;
		this.refdesc = refdesc;
	}
}
