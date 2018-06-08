package co.dc.ccpt.modules.contractmanagement.contractStatistics.entity;

import java.util.Date;

/**
 * 辅助类：合同对象 总包、分包合同中重复的字段
 * 
 * @author Administrator
 *
 */
public class Contract {
	private Integer approvalStatus;// 审核状态
	private String connector;// 工程联系人
	private Date contractDate;// 签订日期
	private String draftsman;// 拟草人
	private String name;// 合同名称
	private String num;// 合同编号
	private String phoneNum;// 号码
	private String proAddr;// 工程地址
	private String proName;// 项目名称
	private Integer status;// 合同状态
	private Double totalPrice;// 总价

	public Contract() {

	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public String getConnector() {
		return connector;
	}

	public Date getContractDate() {
		return contractDate;
	}

	public String getDraftsman() {
		return draftsman;
	}

	public String getName() {
		return name;
	}

	public String getNum() {
		return num;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public String getProAddr() {
		return proAddr;
	}

	public String getProName() {
		return proName;
	}

	public Integer getStatus() {
		return status;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public void setConnector(String connector) {
		this.connector = connector;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}

	public void setDraftsman(String draftsman) {
		this.draftsman = draftsman;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setProAddr(String proAddr) {
		this.proAddr = proAddr;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
