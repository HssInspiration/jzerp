/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Company;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;

/**
 * 投标管理Entity
 * @author lxh
 * @version 2018-02-07
 */
public class Bidtable extends DataEntity<Bidtable> {
	
	private static final long serialVersionUID = 1L;
	private String bidEnclosure;   // 投标附件
	private String bidNum;		   // 投标编号
	private String bidOldNum;	   // 投标原编号
	private Company company;       // 单位对象
	
	private Program program;	   // 项目对象
	private String status;		   // 状态
	private Date openBidDate;      // 开标时间
	private Date beginOpenBidDate; // 开始开标日期
	private Date endOpenBidDate;   // 结束开标日期
	
	private String openBidAddr;    // 开标地点
	private Double deposit;        // 保证金
	private Double ctrlPrice;      // 控制价
	private Double floorPrice;     // 标底价
	private Double provisionPrice; // 暂列金额
	
	private String recordWorker;   // 开标记录人员
	private String provideMeterial;// 所需材料
	
	public Bidtable() {
		super();
	}

	public Bidtable(String id){
		super(id);
	}

	public String getBidOldNum() {
		return bidOldNum;
	}

	public void setBidOldNum(String bidOldNum) {
		this.bidOldNum = bidOldNum;
	}

	public Bidtable(String bidEnclosure, String bidNum, String bidOldNum,
			Company company, Program program, String status, Date openBidDate,
			Date beginOpenBidDate, Date endOpenBidDate, String openBidAddr,
			Double deposit, Double ctrlPrice, Double floorPrice,
			Double provisionPrice, String recordWorker, String provideMeterial) {
		super();
		this.bidEnclosure = bidEnclosure;
		this.bidNum = bidNum;
		this.bidOldNum = bidOldNum;
		this.company = company;
		this.program = program;
		this.status = status;
		this.openBidDate = openBidDate;
		this.beginOpenBidDate = beginOpenBidDate;
		this.endOpenBidDate = endOpenBidDate;
		this.openBidAddr = openBidAddr;
		this.deposit = deposit;
		this.ctrlPrice = ctrlPrice;
		this.floorPrice = floorPrice;
		this.provisionPrice = provisionPrice;
		this.recordWorker = recordWorker;
		this.provideMeterial = provideMeterial;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="开标时间不能为空")
	@ExcelField(title="开标时间", align=2, sort=4)
	public Date getOpenBidDate() {
		return openBidDate;
	}

	public void setOpenBidDate(Date openBidDate) {
		this.openBidDate = openBidDate;
	}

	public Date getBeginOpenBidDate() {
		return beginOpenBidDate;
	}

	public void setBeginOpenBidDate(Date beginOpenBidDate) {
		this.beginOpenBidDate = beginOpenBidDate;
	}

	public Date getEndOpenBidDate() {
		return endOpenBidDate;
	}

	public void setEndOpenBidDate(Date endOpenBidDate) {
		this.endOpenBidDate = endOpenBidDate;
	}


	public String getOpenBidAddr() {
		return openBidAddr;
	}

	public void setOpenBidAddr(String openBidAddr) {
		this.openBidAddr = openBidAddr;
	}

	@ExcelField(title="投标附件", align=2, sort=9)
	public String getBidEnclosure() {
		return bidEnclosure;
	}


	public Double getDeposit() {
		return deposit;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}

	public Double getCtrlPrice() {
		return ctrlPrice;
	}

	public void setCtrlPrice(Double ctrlPrice) {
		this.ctrlPrice = ctrlPrice;
	}

	public Double getFloorPrice() {
		return floorPrice;
	}

	public void setFloorPrice(Double floorPrice) {
		this.floorPrice = floorPrice;
	}

	public Double getProvisionPrice() {
		return provisionPrice;
	}

	public void setProvisionPrice(Double provisionPrice) {
		this.provisionPrice = provisionPrice;
	}

	public String getRecordWorker() {
		return recordWorker;
	}

	public void setRecordWorker(String recordWorker) {
		this.recordWorker = recordWorker;
	}
	@ExcelField(title = "所需材料", dictType = "providemeterial", align = 2, sort = 17)
	public String getProvideMeterial() {
		return provideMeterial;
	}

	public void setProvideMeterial(String provideMeterial) {
		this.provideMeterial = provideMeterial;
	}

	@ExcelField(title="投标编号", align=2, sort=6)
	public String getBidNum() {
		return bidNum;
	}

	public Company getCompany() {
		return company;
	}

	public Program getProgram() {
		return program;
	}

	@ExcelField(title="状态", dictType="bidstatus", align=2, sort=8)
	public String getStatus() {
		return status;
	}

	public void setBidEnclosure(String bidEnclosure) {
		this.bidEnclosure = bidEnclosure;
	}
	
	public void setBidNum(String bidNum) {
		this.bidNum = bidNum;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	public void setProgram(Program program) {
		this.program = program;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String toString() {
		return "Bidtable [bidEnclosure=" + bidEnclosure + ", bidNum=" + bidNum
				+ ", bidOldNum=" + bidOldNum + ", company=" + company
				+ ", program=" + program + ", status=" + status
				+ ", openBidDate=" + openBidDate + ", beginOpenBidDate="
				+ beginOpenBidDate + ", endOpenBidDate=" + endOpenBidDate
				+ ", openBidAddr=" + openBidAddr + ", deposit=" + deposit
				+ ", ctrlPrice=" + ctrlPrice + ", floorPrice=" + floorPrice
				+ ", provisionPrice=" + provisionPrice + ", recordWorker="
				+ recordWorker + ", provideMeterial=" + provideMeterial + "]";
	}
	
}