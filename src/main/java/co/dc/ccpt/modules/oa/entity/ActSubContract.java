package co.dc.ccpt.modules.oa.entity;

import co.dc.ccpt.core.persistence.ActEntity;
import co.dc.ccpt.modules.contractmanagement.contracttext.entity.ContractText;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.sys.entity.Office;
import co.dc.ccpt.modules.sys.entity.User;

public class ActSubContract extends ActEntity<ActSubContract> {
	private static final long serialVersionUID = 1L;
	private String exeDate; // 执行时间
	private String leadText;// 经营部领导意见
	private String mainLeadText;// 总经理意见
	private SubProContract subProContract;// 分包合同对象
	private String subLeadText;// 分公司领导意见
	private String enclosureCont;// 附件内容
	private String contractTextCont;// 合同正文内容
	private ContractText contractText;// 合同正文对象
	private String contractContToPdf;// 合同正文Pdf
	private User user; // 归属用户
	private String userId; // 用户id
	private Office office; // 归属部门

	public ActSubContract() {
		super();
	}

	public ActSubContract(String id) {
		super(id);
	}

	public String getExeDate() {
		return exeDate;
	}

	public void setExeDate(String exeDate) {
		this.exeDate = exeDate;
	}

	public String getLeadText() {
		return leadText;
	}

	public void setLeadText(String leadText) {
		this.leadText = leadText;
	}

	public String getMainLeadText() {
		return mainLeadText;
	}

	public void setMainLeadText(String mainLeadText) {
		this.mainLeadText = mainLeadText;
	}

	public SubProContract getSubProContract() {
		return subProContract;
	}

	public void setSubProContract(SubProContract subProContract) {
		this.subProContract = subProContract;
	}

	public String getSubLeadText() {
		return subLeadText;
	}

	public void setSubLeadText(String subLeadText) {
		this.subLeadText = subLeadText;
	}

	public String getEnclosureCont() {
		return enclosureCont;
	}

	public void setEnclosureCont(String enclosureCont) {
		this.enclosureCont = enclosureCont;
	}

	public String getContractTextCont() {
		return contractTextCont;
	}

	public void setContractTextCont(String contractTextCont) {
		this.contractTextCont = contractTextCont;
	}

	public ContractText getContractText() {
		return contractText;
	}

	public void setContractText(ContractText contractText) {
		this.contractText = contractText;
	}

	public String getContractContToPdf() {
		return contractContToPdf;
	}

	public void setContractContToPdf(String contractContToPdf) {
		this.contractContToPdf = contractContToPdf;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public ActSubContract(String exeDate, String leadText, String mainLeadText, SubProContract subProContract,
			String subLeadText, String enclosureCont, String contractTextCont, ContractText contractText,
			String contractContToPdf, User user, String userId, Office office) {
		super();
		this.exeDate = exeDate;
		this.leadText = leadText;
		this.mainLeadText = mainLeadText;
		this.subProContract = subProContract;
		this.subLeadText = subLeadText;
		this.enclosureCont = enclosureCont;
		this.contractTextCont = contractTextCont;
		this.contractText = contractText;
		this.contractContToPdf = contractContToPdf;
		this.user = user;
		this.userId = userId;
		this.office = office;
	}

	public String toString() {
		return "ActSubContract [exeDate=" + exeDate + ", leadText=" + leadText + ", mainLeadText=" + mainLeadText
				+ ", subProContract=" + subProContract + ", subLeadText=" + subLeadText + ", enclosureCont="
				+ enclosureCont + ", contractTextCont=" + contractTextCont + ", contractText=" + contractText
				+ ", contractContToPdf=" + contractContToPdf + ", user=" + user + ", userId=" + userId + ", office="
				+ office + "]";
	}

}
