package com.scott.entity;

/**
 * iec61850_yx_data_inst,iec61850_yc_data_inst,iec61850_yk_data_inst 表实体类
 */

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class Data_instEntity extends BaseEntity {
	private Integer yx_id ;
	private Integer ied_type_id;
	private String ld_inst_name;
	private String ln_inst_name;
	private String yx_refname;
	private String yx_desc;
	private String fc;
	private String desc;
	private String link_yc_refname;
}
