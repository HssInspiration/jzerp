package co.dc.ccpt.modules.programmanage.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.programmanage.entity.Program;

/**
 * 子项目工程管理Entity
 * 
 * @author lxh
 * @version 2018-03-27
 */
public class SubpackageProgram extends DataEntity<SubpackageProgram> {

	private static final long serialVersionUID = 1L;
	private String subpackageProgramNum; // 子项目编号
	private String subpackageProgramName; // 子项目名称
	private String subpackageProgramCont; // 子项目内容
	private Program program; // 父项目对象
	private String status; // 子项目状态
	private String tenderCompany; // 招标单位
	private Integer subProgramType; // 分包项目类别
	private Date planToStart; // 计划开始时间
	private Date planToEnd; // 计划结束时间
	private ProContract proContract;// 总包合同对象
	private String subproAddr;// 分包工程地址
	private Integer isTender;// 是否招标
	private Company company;//参投单位

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Integer getIsTender() {
		return isTender;
	}

	public void setIsTender(Integer isTender) {
		this.isTender = isTender;
	}

	public SubpackageProgram() {
		super();
	}

	public SubpackageProgram(String id) {
		super(id);
	}

	public String getTenderCompany() {
		return tenderCompany;
	}

	public Program getProgram() {
		return program;
	}

	public Integer getSubProgramType() {
		return subProgramType;
	}

	public void setSubProgramType(Integer subProgramType) {
		this.subProgramType = subProgramType;
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

	public void setProgram(Program program) {
		this.program = program;
	}

	public ProContract getProContract() {
		return proContract;
	}

	public void setProContract(ProContract proContract) {
		this.proContract = proContract;
	}

	public void setTenderCompany(String tenderCompany) {
		this.tenderCompany = tenderCompany;
	}

	@ExcelField(title = "子项目编号", align = 2, sort = 7)
	public String getSubpackageProgramNum() {
		return subpackageProgramNum;
	}

	public void setSubpackageProgramNum(String subpackageProgramNum) {
		this.subpackageProgramNum = subpackageProgramNum;
	}

	@ExcelField(title = "子项目名称", align = 2, sort = 8)
	public String getSubpackageProgramName() {
		return subpackageProgramName;
	}

	public void setSubpackageProgramName(String subpackageProgramName) {
		this.subpackageProgramName = subpackageProgramName;
	}

	@ExcelField(title = "子项目内容", align = 2, sort = 9)
	public String getSubpackageProgramCont() {
		return subpackageProgramCont;
	}

	public void setSubpackageProgramCont(String subpackageProgramCont) {
		this.subpackageProgramCont = subpackageProgramCont;
	}

	// @ExcelField(title="所属项目id", align=2, sort=10)
	// public String getParentId() {
	// return parentId;
	// }
	//
	// public void setParentId(String parentId) {
	// this.parentId = parentId;
	// }

	@ExcelField(title = "子项目状态", dictType = "child_programstatus", align = 2, sort = 11)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubproAddr() {
		return subproAddr;
	}

	public void setSubproAddr(String subproAddr) {
		this.subproAddr = subproAddr;
	}

}