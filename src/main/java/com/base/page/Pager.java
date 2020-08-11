package com.base.page;

import lombok.Data;

/**
 * 分页参数
 * @version 2.0
 */
// oracle,sqlserver,mysql分页技术
@Data
public class Pager {

	private int pageId = 1; // 当前页
	private int rowCount = 0; // 总行数
	private int pageSize = 10; // 页大小
	private int pageCount = 0; // 总页数
	private int pageOffset = 0;// 当前页起始记录
	private int pageTail = 0;// 当前页到达的记录
	private String orderField;
	private boolean orderDirection;

	// 页面显示分页按钮个数
	private int length = 6;
	// 开始分页数字
	private int startIndex = 0;
	// 结束分页数字
	private int endIndex = 0;

	private int[] indexs;

	public int getStartIndex() {
		startIndex = pageId - (length / 2);
		if (startIndex < 1) {
			startIndex = 1;
		}
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		if (getStartIndex() < 1) {
			setStartIndex(1);
		}
		endIndex = (getStartIndex() + length) <= getPageCount() ? (getStartIndex() + length)
				: getPageCount();
		return endIndex;
	}

	public Pager() {
		this.orderDirection = true;
	}

	protected void doPage() {
		this.pageCount = this.rowCount / this.pageSize + 1;
		// 如果模板==0，且总数大于1，则减一
		if ((this.rowCount % this.pageSize == 0) && pageCount > 1)
			this.pageCount--;

		// //如果输入也页面编号（pageId）大于总页数，将pageId设置为pageCount;
		// if(this.pageId> this.pageCount)
		// this.pageId = this.pageCount;
		// this.pageOffset=(this.pageId-1)*this.pageSize+1;

		// this.pageTail=this.pageOffset+this.pageSize-1;

		// Mysql 算法
		this.pageOffset = (this.pageId - 1) * this.pageSize;
		this.pageTail = this.pageOffset + this.pageSize;
		if ((this.pageOffset + this.pageSize) > this.rowCount)
			this.pageTail = this.rowCount;
	}

	public String getOrderCondition() {
		String condition = "";
		if (this.orderField != null && this.orderField.length() != 0) {
			condition = " order by " + orderField
					+ (orderDirection ? " " : " desc ");
		}
		return condition;
	}

	public String getMysqlQueryCondition() {
		String condition = "";
		condition = " limit " + pageOffset + "," + pageSize;
		return condition;
	}


	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}


	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
		this.doPage();
	}
}