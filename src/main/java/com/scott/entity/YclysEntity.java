package com.scott.entity;

/**
 * 遥测量映射实体类
 */

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class YclysEntity extends BaseEntity {
	private String iedName;
	private String apName;
	private String sel_zh;   //组号
	private String sel_dxh;   //点序号
	private String ldinst;  //设备inst
	private String lnClass;  //LN中的lnClass
	private String lninst;  //LN inst
	private String lnType;  //ln中的lnType
	private String fc;      // da中的fc
	private String doName;  //do中的name
	private String typeName; //DA中的name
	private String DatypeName; //DAType中的name(da中有type的时候)
	private String DbaType; //Dba有type的时候
	private String Desc; //DU有DESC的时候
}
