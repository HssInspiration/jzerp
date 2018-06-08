package co.dc.ccpt.modules.contractmanagement.procontract.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.sys.entity.User;

/**
 * 总包合同实体类ProContract
 * 
 * @author Administrator
 * @version 2018-05-11
 */
public class ProContract extends DataEntity<ProContract> {
	private static final long serialVersionUID = 1L;
	private Integer approvalStatus;// 审批状态
	private Date beginContractDate;// 开始 合同签订日期
	private String buildDate;// 工期
	private Date completeDate;// 竣工日期
	private Date contractDate;// 合同签订日期
	private String contractName;// 总包合同名称
	private String contractNum;// 合同编号
	private Integer contractStatus;// 合同状态
	private Double contractTotalPrice;// 合同总价
	private Date endContractDate;// 结束 合同签订日期
	private Integer isStamp;// 用章状态
	private String phoneNum;// 联系人号码
	private Program program;// 项目对象
	private String programAddr;// 开工地址
	private String programConnector;// 工程联系人
	private Date startDate;// 开工日期
	private User user;// 用户对象（合同拟草人）

	public ProContract() {
		super();
	}

	public ProContract(String id) {
		super(id);
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public Date getBeginContractDate() {
		return beginContractDate;
	}

	public String getBuildDate() {
		return buildDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCompleteDate() {
		return completeDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getContractDate() {
		return contractDate;
	}

	public String getContractName() {
		return contractName;
	}

	public String getContractNum() {
		return contractNum;
	}

	public Integer getContractStatus() {
		return contractStatus;
	}

	public Double getContractTotalPrice() {
		return contractTotalPrice;
	}

	public Date getEndContractDate() {
		return endContractDate;
	}

	public Integer getIsStamp() {
		return isStamp;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public Program getProgram() {
		return program;
	}

	public String getProgramAddr() {
		return programAddr;
	}

	public String getProgramConnector() {
		return programConnector;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartDate() {
		return startDate;
	}

	public User getUser() {
		return user;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public void setBeginContractDate(Date beginContractDate) {
		this.beginContractDate = beginContractDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public void setContractStatus(Integer contractStatus) {
		this.contractStatus = contractStatus;
	}

	public void setContractTotalPrice(Double contractTotalPrice) {
		this.contractTotalPrice = contractTotalPrice;
	}

	public void setEndContractDate(Date endContractDate) {
		this.endContractDate = endContractDate;
	}

	public void setIsStamp(Integer isStamp) {
		this.isStamp = isStamp;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public void setProgramAddr(String programAddr) {
		this.programAddr = programAddr;
	}

	public void setProgramConnector(String programConnector) {
		this.programConnector = programConnector;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
