package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class YcDataInstEntity extends BaseEntity {
	private  String yc_id;
	private  String ied_type_id;
	private  String ld_inst_name;
	private  String ln_inst_name;
	private  String yc_refname;
	private  String yc_desc;
//	//todo 新加入
//	private String table_data_type;
}
