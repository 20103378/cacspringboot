package com.scott.entity;

/**
 * iec61850_ln_inst表实体类
 */

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class InstNodeEntity extends BaseEntity {
	private String ied_name;
	private String ied_desc;
	private String ld_inst_name;
	private String ld_inst_desc;
	private String ln_inst_name;
	private String ln_inst_desc;
	
}
