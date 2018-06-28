package co.dc.ccpt.modules.contractmanagement.contracttemp.entity;

import co.dc.ccpt.core.persistence.DataEntity;

public class ContractTemp extends DataEntity<ContractTemp> {
	private static final long serialVersionUID = 1L;
	private String tempName;// 模板名称
	private String tempNum;// 模板编号
	private Integer tempType;// 模板类别

	public ContractTemp() {
		super();
	}

	public ContractTemp(String id) {
		super(id);
	}

	public String getTempName() {
		return tempName;
	}

	public String getTempNum() {
		return tempNum;
	}

	public Integer getTempType() {
		return tempType;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public void setTempNum(String tempNum) {
		this.tempNum = tempNum;
	}

	public void setTempType(Integer tempType) {
		this.tempType = tempType;
	}

}
