package com.base.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TreeNode {

	private String id;


	private String dataId;

	private String text;

	private String url;

	private String state;// value closed,open

	private boolean checked; //

	private boolean disabled;

	private String parentId;
	// 预留属性
	private String iconCls;

	private Map<String, Object> attributes = new HashMap<String, Object>();

	private List<TreeNode> children;




//	private Integer id2;
//	private String field2;
//	private String field3;
//	private String field4;
//	private String field5;
//	private String field6;
//	private String field7;
//	private String field8;
//	private String field9;
//	private String field10;
//	private String field11;
//	private String field12;
//	private String field13;
//	private String field14;
//	private String field15;
//	private String field16;
//	private String field17;
//	private String field18;
//	private String field19;
//	private String field20;
//	private String field21;
//	private String field22;
//	private String field23;
//	private String field24;
//	private String field25;
//	private String field26;
//	private String field27;
//	private String field28;
//	private String field29;
//	private String field30;
//	private String field31;
//	private String field32;
//	private String field33;
//	private String field34;
//	private String field35;
//	private String field36;
//	private String field37;
//	private String field38;
//	private String field39;
//	private String field40;


}
