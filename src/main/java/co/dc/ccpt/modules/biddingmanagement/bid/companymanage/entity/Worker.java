/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity;

import javax.validation.constraints.NotNull;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;

/**
 * 人员信息Entity
 * 
 * @author lxh
 * @version 2018-02-08
 */
public class Worker extends DataEntity<Worker> {

	private static final long serialVersionUID = 1L;
	private String workerNum; // 人员编号
	private String workerName; // 人员名称
	private Integer workerPosition; // 人员职位

	public Worker() {
		super();
	}

	public Worker(String id) {
		super(id);
	}

	@ExcelField(title = "人员编号", align = 2, sort = 1)
	public String getWorkerNum() {
		return workerNum;
	}

	public void setWorkerNum(String workerNum) {
		this.workerNum = workerNum;
	}

	@ExcelField(title = "人员名称", align = 2, sort = 2)
	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	@NotNull(message = "人员职位不能为空")
	@ExcelField(title = "人员职位", dictType = "position", align = 2, sort = 3)
	public Integer getWorkerPosition() {
		return workerPosition;
	}

	public void setWorkerPosition(Integer workerPosition) {
		this.workerPosition = workerPosition;
	}

	public String toString() {
		return "Worker [workerNum=" + workerNum + ", workerName=" + workerName
				+ ", workerPosition=" + workerPosition + "]";
	}

}