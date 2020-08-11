package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class I2TableEntity extends BaseEntity {

	private String i2id;  //i2id
	private String i1type;  //i1类型
	private String i1id;  //i1id
	private String i2_refname;//名称
	private String i2_desc;//备注

	public I2TableEntity(String i2id, String i1type, String i1id, String i2_refname, String i2_desc) {
		this.i2id = i2id;
		this.i1type = i1type;
		this.i1id = i1id;
		this.i2_refname = i2_refname;
		this.i2_desc = i2_desc;
	}
}
