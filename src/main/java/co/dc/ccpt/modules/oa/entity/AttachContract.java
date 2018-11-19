package co.dc.ccpt.modules.oa.entity;

import co.dc.ccpt.core.persistence.ActEntity;
import co.dc.ccpt.modules.contractmanagement.contracttext.entity.ContractText;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;

public class AttachContract extends ActEntity<AttachContract> {
	private static final long serialVersionUID = 1L;
	private String exeDate; // 执行时间
	private String leadText;// 经营部领导意见
	private String mainLeadText;// 总经理意见
	private ProContract proContract;// 合同对象
	private String subLeadText;// 分公司领导意见
	private String enclosureCont;// 附件内容
	private String contractTextCont;// 合同正文内容
	private ContractText contractText;// 合同正文对象
	private String contractContToPdf;// 合同正文pdf

	public AttachContract() {
		super();
	}

	public AttachContract(String id) {
		super(id);
	}

	public String getExeDate() {
		return exeDate;
	}

	public String getLeadText() {
		return leadText;
	}

	public String getMainLeadText() {
		return mainLeadText;
	}

	public ProContract getProContract() {
		return proContract;
	}

	public String getSubLeadText() {
		return subLeadText;
	}

	public void setExeDate(String exeDate) {
		this.exeDate = exeDate;
	}

	public void setLeadText(String leadText) {
		this.leadText = leadText;
	}

	public void setMainLeadText(String mainLeadText) {
		this.mainLeadText = mainLeadText;
	}

	public void setProContract(ProContract proContract) {
		this.proContract = proContract;
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

	public String getContractContToPdf() {
		return contractContToPdf;
	}

	public void setContractContToPdf(String contractContToPdf) {
		this.contractContToPdf = contractContToPdf;
	}

	public void setContractText(ContractText contractText) {
		this.contractText = contractText;
	}

}
