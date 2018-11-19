package co.dc.ccpt.modules.contractmanagement.contracttext.entity;

import co.dc.ccpt.core.persistence.DataEntity;

public class ContractText extends DataEntity<ContractText> {
	private static final long serialVersionUID = 1L;
	private String contractTextCont;// 合同正文内容
	private String contractTextName;// 合同正文
	private String contractTextNum;// 合同正文编号
	private Integer contractTextStatus;// 合同正文状态
	private String contractId;// 合同id
	// private ProContract proContract;//总包合同对象

	public ContractText() {

	}

	public ContractText(String contractTextCont, String contractTextName, String contractTextNum,
			Integer contractTextStatus, String contractId) {
		super();
		this.contractTextCont = contractTextCont;
		this.contractTextName = contractTextName;
		this.contractTextNum = contractTextNum;
		this.contractTextStatus = contractTextStatus;
		this.contractId = contractId;
	}

	public String getContractTextCont() {
		return contractTextCont;
	}

	public void setContractTextCont(String contractTextCont) {
		this.contractTextCont = contractTextCont;
	}

	public String getContractTextName() {
		return contractTextName;
	}

	public void setContractTextName(String contractTextName) {
		this.contractTextName = contractTextName;
	}

	public String getContractTextNum() {
		return contractTextNum;
	}

	public void setContractTextNum(String contractTextNum) {
		this.contractTextNum = contractTextNum;
	}

	public Integer getContractTextStatus() {
		return contractTextStatus;
	}

	public void setContractTextStatus(Integer contractTextStatus) {
		this.contractTextStatus = contractTextStatus;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

}
