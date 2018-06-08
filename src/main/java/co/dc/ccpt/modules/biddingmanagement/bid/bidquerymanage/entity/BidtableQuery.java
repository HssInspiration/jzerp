package co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Company;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

/**
 * 投标综合查询Entity
 * @author lxh
 * @version 2018-03-10
 */
public class BidtableQuery extends DataEntity<BidtableQuery> {
	private static final long serialVersionUID = 1L;
	private String bidNum;		   // 投标编号
	private String programId;	   // 项目工程id
	private String status;		   // 状态
	private String bidEnclosure;   // 投标附件
	private Company company;       // 单位对象
	private Program program;	   // 项目对象
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
	private List<BidCompanyManage> bidCompanyManageList = Lists.newArrayList();		// 子表列表
	private List<String> programStatusList;//项目状态集合 
	private String programStatus;
	private BidCompanyManage bidCompanyManage; //子表对象
	
	
	public BidtableQuery() {
		super();
	}

	public BidCompanyManage getBidCompanyManage() {
		return bidCompanyManage;
	}

	public void setBidCompanyManage(BidCompanyManage bidCompanyManage) {
		this.bidCompanyManage = bidCompanyManage;
	}



	public List<String> getProgramStatusList() {
		return programStatusList;
	}

	public void setProgramStatusList(List<String> programStatusList) {
		this.programStatusList = programStatusList;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getProgramStatus() {
		return programStatus;
	}

	public void setProgramStatus(String programStatus) {
		this.programStatus = programStatus;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
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

	public String getProvideMeterial() {
		return provideMeterial;
	}

	public void setProvideMeterial(String provideMeterial) {
		this.provideMeterial = provideMeterial;
	}

	public BidtableQuery(String id){
		super(id);
	}

	@ExcelField(title="投标编号", align=2, sort=6)
	public String getBidNum() {
		return bidNum;
	}

	public void setBidNum(String bidNum) {
		this.bidNum = bidNum;
	}
	
	@ExcelField(title="项目工程id", dictType="", align=2, sort=7)
	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}
	
	@ExcelField(title="状态", dictType="bidstatus", align=2, sort=8)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="投标附件", align=2, sort=9)
	public String getBidEnclosure() {
		return bidEnclosure;
	}

	public void setBidEnclosure(String bidEnclosure) {
		this.bidEnclosure = bidEnclosure;
	}
	
	public List<BidCompanyManage> getBidCompanyManageList() {
		return bidCompanyManageList;
	}

	public void setBidCompanyManageList(List<BidCompanyManage> bidCompanyManageList) {
		this.bidCompanyManageList = bidCompanyManageList;
	}
}