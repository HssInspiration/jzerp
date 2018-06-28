package co.dc.ccpt.modules.oa.entity;

import co.dc.ccpt.core.persistence.ActEntity;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;

public class ActSubContract extends ActEntity<ActSubContract> {
	private static final long serialVersionUID = 1L;
	private String exeDate; // 执行时间
	private String leadText;// 经营部领导意见
	private String mainLeadText;// 总经理意见
	private SubProContract subProContract;// 分包合同对象
	private String subLeadText;// 分公司领导意见
	private String enclosureCont;// 附件内容

	public ActSubContract() {
		super();
	}

	public ActSubContract(String id) {
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

	public void setSubLeadText(String subLeadText) {
		this.subLeadText = subLeadText;
	}

	public String getEnclosureCont() {
		return enclosureCont;
	}

	public void setEnclosureCont(String enclosureCont) {
		this.enclosureCont = enclosureCont;
	}

	public SubProContract getSubProContract() {
		return subProContract;
	}

	public void setSubProContract(SubProContract subProContract) {
		this.subProContract = subProContract;
	}
	
}
