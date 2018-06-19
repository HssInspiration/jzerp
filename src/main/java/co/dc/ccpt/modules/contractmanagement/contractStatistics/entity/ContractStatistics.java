package co.dc.ccpt.modules.contractmanagement.contractStatistics.entity;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
/**
 * 合同统计查询实体ContractStatistics
 * @author Administrator
 * @version 2018-05-17
 */
public class ContractStatistics extends DataEntity<ContractStatistics>{

	private static final long serialVersionUID = 1L;
	private ProContract proContract;// 总包合同对象
	private SubProContract subProContract;// 分包合同对象
	private Contract contract;// 合同对象

	public ContractStatistics() {
		
	}
	
	public ContractStatistics(String id) {
		super(id);
	}

	public ProContract getProContract() {
		return proContract;
	}

	public void setProContract(ProContract proContract) {
		this.proContract = proContract;
	}

	public SubProContract getSubProContract() {
		return subProContract;
	}

	public void setSubProContract(SubProContract subProContract) {
		this.subProContract = subProContract;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
	
}
