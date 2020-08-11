package com.scott.entity;

import com.base.entity.BaseEntity;
import lombok.Data;

/**
 *
 * <br>
 * <b>功能：</b>JeecgPersonEntity<br>
 * <b>作者：</b>www.jeecg.org<br>F
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Data
public class SbushDataEntity extends BaseEntity {
	private   String id;
	private   String sampleTime;
	private   Double leakAA;
	private   Double absReactA;
	private   Double dieLossA;
	private   Double leakAB;
	private   Double absReactB;
	private   Double dieLossB;
	private   Double leakAC;
	private   Double absReactC;
	private   Double dieLossC;
	private   Double totRelA;
	private   Integer remark;
	
	private   Double appPaDsch;
	private   Double acuPaDsch;
	private   Double avDsch;
	private   Double maxDsch;
	private   Integer dschCnt;
	
	private   Double lscAmp;
	private   Double equCa;
	private   Double losFact;
	
}

