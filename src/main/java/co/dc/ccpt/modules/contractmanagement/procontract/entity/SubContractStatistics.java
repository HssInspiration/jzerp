package co.dc.ccpt.modules.contractmanagement.procontract.entity;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.BidStatistics;

public class SubContractStatistics extends DataEntity<BidStatistics> {
	private static final long serialVersionUID = 1L;
	private Double totalSubContractPrice;// 分包合同总价
	private Double effectSubContractPrice;// 生效分包合同总价
	private Integer effectSubContractCount;// 生效分包合同数量
	private Integer subContractCount;// 分包合同数量

	public SubContractStatistics() {
		super();
	}

	public SubContractStatistics(String id) {
		super(id);
	}

	public Double getTotalSubContractPrice() {
		return totalSubContractPrice;
	}

	public void setTotalSubContractPrice(Double totalSubContractPrice) {
		this.totalSubContractPrice = totalSubContractPrice;
	}

	public Double getEffectSubContractPrice() {
		return effectSubContractPrice;
	}

	public void setEffectSubContractPrice(Double effectSubContractPrice) {
		this.effectSubContractPrice = effectSubContractPrice;
	}

	public Integer getEffectSubContractCount() {
		return effectSubContractCount;
	}

	public void setEffectSubContractCount(Integer effectSubContractCount) {
		this.effectSubContractCount = effectSubContractCount;
	}

	public Integer getSubContractCount() {
		return subContractCount;
	}

	public void setSubContractCount(Integer subContractCount) {
		this.subContractCount = subContractCount;
	}

}
