package com.scott.entity;

/**
 * 遥测量映射实体类
 */

import com.base.entity.BaseEntity;
import lombok.Data;

@Data
public class YclysEntity extends BaseEntity {
	/**
	 * ied名称
	 */
	private String iedName;
	/**
	 *节点名称
	 */
	private String apName;
	/**
	 *
	 */
	private String sel_zh;   //组号
	private String sel_dxh;   //点序号
	/**
	 * LDevice inst 名称
	 */
	private String ldinst;  //设备inst
	/**
	 * LN中的lnClass
	 */
	private String lnClass;  //LN中的lnClass
	/**
	 * LN inst
	 */
	private String lninst;  //LN inst
	/**
	 * ln中的lnType
	 */
	private String lnType;  //ln中的lnType
	/**
	 *LNodeType下面的DO的name
	 */
	private String doName;  //do中的name

	/**
	 * DOType下面的DA中的fc
	 */
	private String fc;      // da中的fc
	/**
	 * DOType下面的DA中的name
	 */
	private String typeName; //DA中的name
	/**
	 *
	 */
	private String DatypeName; //DAType中的name(da中有type的时候)
	/**
	 *
	 */
	private String DbaType; //Dba有type的时候
	/**
	 *LN下面的DOI的DAI值
	 */
	private String Desc; //DU有DESC的时候
}
