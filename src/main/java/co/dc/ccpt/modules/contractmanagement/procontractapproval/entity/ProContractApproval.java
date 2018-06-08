package co.dc.ccpt.modules.contractmanagement.procontractapproval.entity;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Bidcompany;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.sys.entity.User;

/**
 * 总包合同审核对象
 * 
 * @author Administrator
 * @version 2018-05-16
 */
public class ProContractApproval extends DataEntity<ProContractApproval> {

	private static final long serialVersionUID = 1L;
	private Integer approvalStatus;// 审核状态
	private Bidcompany bidcompany;// 参投单位对象
	private Integer contractStatus;// 合同状态
	private ProContract proContract;// 总包合同对象
	private User user;// 用户对象--审核人

	public ProContractApproval() {

	}

	public ProContractApproval(String id) {
		super(id);
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public Bidcompany getBidcompany() {
		return bidcompany;
	}

	public Integer getContractStatus() {
		return contractStatus;
	}

	public ProContract getProContract() {
		return proContract;
	}

	public User getUser() {
		return user;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public void setBidcompany(Bidcompany bidcompany) {
		this.bidcompany = bidcompany;
	}

	public void setContractStatus(Integer contractStatus) {
		this.contractStatus = contractStatus;
	}

	public void setProContract(ProContract proContract) {
		this.proContract = proContract;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
