package co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity;

import co.dc.ccpt.core.persistence.DataEntity;
/**
 * 金卓参投统计Entity
 * @author lxh
 * @version 2018-03-29
 */
public class BidStatistics extends DataEntity<BidStatistics>{
	private static final long serialVersionUID = 1L;
	private Double totalBeterialPrice;
	private Double totalBidLaborCost;
	private Double totalBidPrice;
	private Double totalDeposit;
	public BidStatistics() {
		super();
	}
	public BidStatistics(Double totalBidLaborCost, Double totalBidPrice,
			Double totalBeterialPrice, Double totalDeposit) {
		super();
		this.totalBidLaborCost = totalBidLaborCost;
		this.totalBidPrice = totalBidPrice;
		this.totalBeterialPrice = totalBeterialPrice;
		this.totalDeposit = totalDeposit;
	}
	public BidStatistics(String id) {
		super(id);
	}
	public Double getTotalBeterialPrice() {
		return totalBeterialPrice;
	}
	public Double getTotalBidLaborCost() {
		return totalBidLaborCost;
	}
	public Double getTotalBidPrice() {
		return totalBidPrice;
	}
	public Double getTotalDeposit() {
		return totalDeposit;
	}
	public void setTotalBeterialPrice(Double totalBeterialPrice) {
		this.totalBeterialPrice = totalBeterialPrice;
	}
	public void setTotalBidLaborCost(Double totalBidLaborCost) {
		this.totalBidLaborCost = totalBidLaborCost;
	}
	public void setTotalBidPrice(Double totalBidPrice) {
		this.totalBidPrice = totalBidPrice;
	}
	public void setTotalDeposit(Double totalDeposit) {
		this.totalDeposit = totalDeposit;
	}
	public String toString() {
		return "BidStatistics [totalBidLaborCost=" + totalBidLaborCost
				+ ", totalBidPrice=" + totalBidPrice + ", totalBeterialPrice="
				+ totalBeterialPrice + ", totalDeposit=" + totalDeposit + "]";
	}
	
}
