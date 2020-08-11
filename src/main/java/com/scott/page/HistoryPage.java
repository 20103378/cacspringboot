package com.scott.page;

import com.base.page.BasePage;
import lombok.Data;

/**
 *
 * <br>
 * <b>功能：</b>历史页面<br>
 * <b>作者：</b>XJW<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Data
public class HistoryPage extends BasePage {
	private String id;//
	private String startTime;//
	private String endTime;//
	private String state;//
    private String ln_inst_name;
    private String ld_inst_name;
    private String refname;

}
