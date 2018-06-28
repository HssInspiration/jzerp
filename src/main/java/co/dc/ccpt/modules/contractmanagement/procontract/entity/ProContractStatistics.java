package co.dc.ccpt.modules.contractmanagement.procontract.entity;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.BidStatistics;

/**
 * 总包合同统计 entity
 * @author Administrator
 *
 */
public class ProContractStatistics extends DataEntity<BidStatistics> {
	private static final long serialVersionUID = 1L;
	private Double totalProContractPrice;// 总包合同总价
	private Double marketProContractPrice;// 市场投标总价
	private Double appointProContractPrice;// 业主指定总价
	private Double effectProContractPrice;// 生效总包合同总价
	private Integer effectProContractCount;// 生效总包合同数量
	private Integer notEffectProContractCount;// 生效总包合同数量
	private Integer proContractCount;// 总包合同数量
	private Double marketAndEffectedPrice;// 市场招标且生效总价
	private Double appointAndEffectedtPrice;// 业主指定且生效总价
	private Double notEffectProContractPrice;// 未生效合同总价

	public ProContractStatistics() {
		super();
	}

	public ProContractStatistics(String id) {
		super(id);
	}

	public Double getTotalProContractPrice() {
		return totalProContractPrice;
	}

	public void setTotalProContractPrice(Double totalProContractPrice) {
		this.totalProContractPrice = totalProContractPrice;
	}

	public Double getMarketProContractPrice() {
		return marketProContractPrice;
	}

	public void setMarketProContractPrice(Double marketProContractPrice) {
		this.marketProContractPrice = marketProContractPrice;
	}

	public Double getAppointProContractPrice() {
		return appointProContractPrice;
	}

	public void setAppointProContractPrice(Double appointProContractPrice) {
		this.appointProContractPrice = appointProContractPrice;
	}

	public Double getEffectProContractPrice() {
		return effectProContractPrice;
	}

	public void setEffectProContractPrice(Double effectProContractPrice) {
		this.effectProContractPrice = effectProContractPrice;
	}

	public Integer getEffectProContractCount() {
		return effectProContractCount;
	}

	public void setEffectProContractCount(Integer effectProContractCount) {
		this.effectProContractCount = effectProContractCount;
	}

	public Double getMarketAndEffectedPrice() {
		return marketAndEffectedPrice;
	}

	public void setMarketAndEffectedPrice(Double marketAndEffectedPrice) {
		this.marketAndEffectedPrice = marketAndEffectedPrice;
	}

	public Double getAppointAndEffectedtPrice() {
		return appointAndEffectedtPrice;
	}

	public void setAppointAndEffectedtPrice(Double appointAndEffectedtPrice) {
		this.appointAndEffectedtPrice = appointAndEffectedtPrice;
	}

	public Integer getProContractCount() {
		return proContractCount;
	}

	public void setProContractCount(Integer proContractCount) {
		this.proContractCount = proContractCount;
	}

	public Double getNotEffectProContractPrice() {
		return notEffectProContractPrice;
	}

	public void setNotEffectProContractPrice(Double notEffectProContractPrice) {
		this.notEffectProContractPrice = notEffectProContractPrice;
	}

	public Integer getNotEffectProContractCount() {
		return notEffectProContractCount;
	}

	public void setNotEffectProContractCount(Integer notEffectProContractCount) {
		this.notEffectProContractCount = notEffectProContractCount;
	}

}
