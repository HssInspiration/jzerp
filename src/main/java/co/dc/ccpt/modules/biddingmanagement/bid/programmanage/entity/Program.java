/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Bidcompany;
import co.dc.ccpt.modules.sys.entity.Office;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 项目工程管理Entity
 * @author lxh
 * @version 2018-02-01
 */
public class Program extends DataEntity<Program> {
	
	private static final long serialVersionUID = 1L;
	private String programNum;		// 项目工程编号
	private String programOldNum;	// 项目工程编号
	private String programName;		// 项目工程名称
	private String programType;		// 项目工程类别
	private String proDescription;	// 项目详细描述
	private Integer status;		    // 项目状态
	private String enclosure;		// 项目附件
	private String programNo;		// 项目外部编号
	private Date callBidDate;		// 发标日期
	private Date beginCallBidDate;	// 开始 发标日期
	private Date endCallBidDate;	// 结束 发标日期
	private Company company;        // 发标单位
	private Office office;          // 机构
	private Date planToStart;       // 计划开始时间 
	private Date planToEnd;         // 计划结束时间
	private Bidcompany bidCompany;  // 参投单位对象
	private Date beginPlanToStart;  // 开始
	private Date endPlanToStart;    // 结束
	private Integer getMethod;      // 承接方式


	public Program() {
		super();
	}

	public Program(String id){
		super(id);
	}
	
	public String getProgramOldNum() {
		return programOldNum;
	}

	public void setProgramOldNum(String programOldNum) {
		this.programOldNum = programOldNum;
	}
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}


	public Bidcompany getBidCompany() {
		return bidCompany;
	}

	public void setBidCompany(Bidcompany bidCompany) {
		this.bidCompany = bidCompany;
	}

	@NotNull(message="项目工程类别不能为空")
	@ExcelField(title="项目工程编号", align=2, sort=7)
	public String getProgramNum() {
		return programNum;
	}

	public void setProgramNum(String programNum) {
		this.programNum = programNum;
	}
	@NotNull(message="项目工程类别不能为空")
	@ExcelField(title="项目工程名称", align=2, sort=8)
	public String getProgramName() {
		return programName;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPlanToStart() {
		return planToStart;
	}

	public void setPlanToStart(Date planToStart) {
		this.planToStart = planToStart;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPlanToEnd() {
		return planToEnd;
	}

	public void setPlanToEnd(Date planToEnd) {
		this.planToEnd = planToEnd;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}
	
	public Date getBeginPlanToStart() {
		return beginPlanToStart;
	}

	public void setBeginPlanToStart(Date beginPlanToStart) {
		this.beginPlanToStart = beginPlanToStart;
	}

	public Date getEndPlanToStart() {
		return endPlanToStart;
	}

	public void setEndPlanToStart(Date endPlanToStart) {
		this.endPlanToStart = endPlanToStart;
	}

	@NotNull(message="项目工程类别不能为空")
	@ExcelField(title="项目工程类别", dictType="programtype", align=2, sort=10)
	public String getProgramType() {
		return programType;
	}

	@NotNull(message="发标单位不能为空")
	@ExcelField(title="发标单位", dictType="company", align=2, sort=10)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}
	
	@ExcelField(title="项目详细描述", align=2, sort=11)
	public String getProDescription() {
		return proDescription;
	}

	public void setProDescription(String proDescription) {
		this.proDescription = proDescription;
	}
	
	//@NotNull(message="项目状态不能为空")
	//@ExcelField(title="项目状态", dictType="programstatus", align=2, sort=12)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@ExcelField(title="项目附件", align=2, sort=13)
	public String getEnclosure() {
		return enclosure;
	}

	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}
	
	@ExcelField(title="项目外部编号", align=2, sort=14)
	public String getProgramNo() {
		return programNo;
	}

	public void setProgramNo(String programNo) {
		this.programNo = programNo;
	}
	
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="发标日期", align=2, sort=15)
	public Date getCallBidDate() {
		return callBidDate;
	}

	public void setCallBidDate(Date callBidDate) {
		this.callBidDate = callBidDate;
	}
	
	public Date getBeginCallBidDate() {
		return beginCallBidDate;
	}

	public void setBeginCallBidDate(Date beginCallBidDate) {
		this.beginCallBidDate = beginCallBidDate;
	}
	
	public Date getEndCallBidDate() {
		return endCallBidDate;
	}

	public void setEndCallBidDate(Date endCallBidDate) {
		this.endCallBidDate = endCallBidDate;
	}

	public Integer getGetMethod() {
		return getMethod;
	}

	public void setGetMethod(Integer getMethod) {
		this.getMethod = getMethod;
	}

}