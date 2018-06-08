package co.dc.ccpt.modules.contractmanagement.subcontractapproval.entity;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.contractmanagement.subprocontract.entity.SubProContract;
import co.dc.ccpt.modules.sys.entity.User;

/**
 * 分包合同审核entity
 * 
 * @author Administrator
 * @version 2018-05-17
 */
public class SubContractApproval extends DataEntity<SubContractApproval> {

	private static final long serialVersionUID = 1L;
	private Integer approvalStatus;// 审核状态
	private Integer subContractStatus;// 分包合同状态
	private SubProContract subProContract;// 分包合同对象
	private User user;// 用户对象

	public SubContractApproval() {

	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}


	public SubProContract getSubProContract() {
		return subProContract;
	}

	public User getUser() {
		return user;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Integer getSubContractStatus() {
		return subContractStatus;
	}

	public void setSubContractStatus(Integer subContractStatus) {
		this.subContractStatus = subContractStatus;
	}

	public void setSubProContract(SubProContract subProContract) {
		this.subProContract = subProContract;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
