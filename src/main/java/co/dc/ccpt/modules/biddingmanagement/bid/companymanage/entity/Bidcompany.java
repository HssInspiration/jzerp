/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.entity.Bidtable;
import co.dc.ccpt.modules.coreperson.basicinfo.entity.CorePerson;
import co.dc.ccpt.modules.programmanage.entity.Company;
import co.dc.ccpt.modules.programmanage.entity.Program;

/**
 * 参投单位管理Entity
 * 
 * @author lxh
 * @version 2018-02-08
 */
public class Bidcompany extends DataEntity<Bidcompany> {

	private static final long serialVersionUID = 1L;
	private Date beginBidDate; // 开始投标时间
	private Date bidDate; // 投标时间
	private String bidOldCompanyId; // 参投单位原id
	private Integer buildDate;      // 工期
	private Double bidPrice; // 投标价
	private Bidtable bidtable; // 投标对象
	private String comBid; // 商务标
	private CorePerson comBidName; // 商务标负责人
	private Company company; // 单位对象
	private String companyId; // 单位id
	private CorePerson constructoor; // 建造师名称
	private String constructorId; // 建造师
	private CorePerson constrworker; // 施工员名称

	private String constrworkerId; // 施工员
	private CorePerson coster; // 造价员名称
	private String costerId; // 造价员
	private Double deposit; // 递交保证金金额
	private String depositEnclosure;// 相关附件

	private CorePerson director; // 技术负责人
	private String directorId; // 技术负责人
	private Double discountRate; // 让利幅度
	private CorePerson documenter; // 资料员名称
	private String documenterId; // 资料员

	private String ecoBid; // 经济标
	private CorePerson ecoBidName; // 经济标负责人
	private Date endBidDate; // 结束投标时间
	private CorePerson inspector; // 质检员名称
	private String inspectorId; // 质检员

	private Integer isBid; // 是否中标
	private CorePerson laboor; // 劳务员名称
	private String laboorId; // 劳务员

	private Double laborCost; // 劳务费
	private CorePerson machinister; // 机械员名称
	private String machinisterId; // 机械员
	private CorePerson meterialer; // 材料员名称
	private String meterialerId; // 材料员

	private Double meterialExpense; // 材料费
	private Date openBidDate; // 计划开标时间(用于验证投标时间<开标时间)
	private CorePerson otherWorkers; // 其他人员
	private String otherWorkersId; // 其他人员
	private Program program; // 项目工程对象

	private String programId; // 项目id
	private String queryBeginDate; // 开始投标时间
	private String queryEndDate; // 结束投标时间
	private CorePerson saver; // 安全员名称
	private String saverId; // 安全员

	private CorePerson standarder; // 标准员名称
	private String standarderId; // 标准员
	private String tecBid; // 技术标
	private CorePerson tecBidName; // 技术标负责人
	private String corePersonId; //人员id---方便查询具体的人员在建项目
	private String quality;//质量

	public Bidcompany() {
		super();
	}

	public Bidcompany(String id) {
		super(id);
	}

	public Date getBeginBidDate() {
		return beginBidDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message = "投标时间不能为空")
	@ExcelField(title = "投标时间", align = 2, sort = 4)
	public Date getBidDate() {
		return bidDate;
	}

	public String getBidOldCompanyId() {
		return bidOldCompanyId;
	}

	// @NotNull(message="投标价不能为空")
	@ExcelField(title = "投标价", align = 2, sort = 3)
	public Double getBidPrice() {
		return bidPrice;
	}

	public Bidtable getBidtable() {
		return bidtable;
	}

	@ExcelField(title = "商务标", dictType = "", align = 2, sort = 8)
	public String getComBid() {
		return comBid;
	}

	public CorePerson getComBidName() {
		return comBidName;
	}

	public Company getCompany() {
		return company;
	}

	@ExcelField(title = "单位id", align = 2, sort = 1)
	public String getCompanyId() {
		return companyId;
	}

	public CorePerson getConstructoor() {
		return constructoor;
	}

	// @NotNull(message="建造师不能为空")
	@ExcelField(title = "建造师", dictType = "", align = 2, sort = 10)
	public String getConstructorId() {
		return constructorId;
	}

	public CorePerson getConstrworker() {
		return constrworker;
	}

	@ExcelField(title = "施工员", dictType = "", align = 2, sort = 13)
	public String getConstrworkerId() {
		return constrworkerId;
	}

	public CorePerson getCoster() {
		return coster;
	}

	@ExcelField(title = "造价员", dictType = "", align = 2, sort = 16)
	public String getCosterId() {
		return costerId;
	}

	public Double getDeposit() {
		return deposit;
	}

	@ExcelField(title = "相关附件", align = 2, sort = 19)
	public String getDepositEnclosure() {
		return depositEnclosure;
	}

	public CorePerson getDirector() {
		return director;
	}

	@ExcelField(title = "技术负责人", dictType = "", align = 2, sort = 11)
	public String getDirectorId() {
		return directorId;
	}

	public Double getDiscountRate() {
		return discountRate;
	}

	public CorePerson getDocumenter() {
		return documenter;
	}

	public String getDocumenterId() {
		return documenterId;
	}

	@ExcelField(title = "经济标", dictType = "", align = 2, sort = 9)
	public String getEcoBid() {
		return ecoBid;
	}

	public CorePerson getEcoBidName() {
		return ecoBidName;
	}

	public Date getEndBidDate() {
		return endBidDate;
	}

	public CorePerson getInspector() {
		return inspector;
	}

	@ExcelField(title = "质检员", dictType = "", align = 2, sort = 14)
	public String getInspectorId() {
		return inspectorId;
	}

	@NotNull(message = "是否中标不能为空")
	@ExcelField(title = "是否中标", dictType = "yes_no", align = 2, sort = 17)
	public Integer getIsBid() {
		return isBid;
	}

	public CorePerson getLaboor() {
		return laboor;
	}

	public String getLaboorId() {
		return laboorId;
	}

	// @NotNull(message="劳务费不能为空")
	@ExcelField(title = "劳务费", align = 2, sort = 5)
	public Double getLaborCost() {
		return laborCost;
	}

	public CorePerson getMachinister() {
		return machinister;
	}

	public String getMachinisterId() {
		return machinisterId;
	}

	public CorePerson getMeterialer() {
		return meterialer;
	}

	@ExcelField(title = "材料员", dictType = "", align = 2, sort = 15)
	public String getMeterialerId() {
		return meterialerId;
	}

	@ExcelField(title = "材料费", align = 2, sort = 6)
	public Double getMeterialExpense() {
		return meterialExpense;
	}

	public Date getOpenBidDate() {
		return openBidDate;
	}

	public CorePerson getOtherWorkers() {
		return otherWorkers;
	}

	public String getOtherWorkersId() {
		return otherWorkersId;
	}

	public Program getProgram() {
		return program;
	}

	@ExcelField(title = "项目id", align = 2, sort = 2)
	public String getProgramId() {
		return programId;
	}

	public String getQueryBeginDate() {
		return queryBeginDate;
	}

	public String getQueryEndDate() {
		return queryEndDate;
	}

	public CorePerson getSaver() {
		return saver;
	}

	@ExcelField(title = "安全员", dictType = "", align = 2, sort = 12)
	public String getSaverId() {
		return saverId;
	}

	public CorePerson getStandarder() {
		return standarder;
	}

	public String getStandarderId() {
		return standarderId;
	}

	@ExcelField(title = "技术标", dictType = "", align = 2, sort = 7)
	public String getTecBid() {
		return tecBid;
	}

	public CorePerson getTecBidName() {
		return tecBidName;
	}

	public void setBeginBidDate(Date beginBidDate) {
		this.beginBidDate = beginBidDate;
	}

	public void setBidDate(Date bidDate) {
		this.bidDate = bidDate;
	}

	public void setBidOldCompanyId(String bidOldCompanyId) {
		this.bidOldCompanyId = bidOldCompanyId;
	}

	public void setBidPrice(Double bidPrice) {
		this.bidPrice = bidPrice;
	}

	public void setBidtable(Bidtable bidtable) {
		this.bidtable = bidtable;
	}

	public void setComBid(String comBid) {
		this.comBid = comBid;
	}

	public void setComBidName(CorePerson comBidName) {
		this.comBidName = comBidName;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public void setConstructoor(CorePerson constructoor) {
		this.constructoor = constructoor;
	}

	public void setConstructorId(String constructorId) {
		this.constructorId = constructorId;
	}

	public void setConstrworker(CorePerson constrworker) {
		this.constrworker = constrworker;
	}

	public void setConstrworkerId(String constrworkerId) {
		this.constrworkerId = constrworkerId;
	}

	public void setCoster(CorePerson coster) {
		this.coster = coster;
	}

	public void setCosterId(String costerId) {
		this.costerId = costerId;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}

	public void setDepositEnclosure(String depositEnclosure) {
		this.depositEnclosure = depositEnclosure;
	}

	public void setDirector(CorePerson director) {
		this.director = director;
	}

	public void setDirectorId(String directorId) {
		this.directorId = directorId;
	}

	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}

	public void setDocumenter(CorePerson documenter) {
		this.documenter = documenter;
	}

	public void setDocumenterId(String documenterId) {
		this.documenterId = documenterId;
	}

	public void setEcoBid(String ecoBid) {
		this.ecoBid = ecoBid;
	}

	public void setEcoBidName(CorePerson ecoBidName) {
		this.ecoBidName = ecoBidName;
	}

	public void setEndBidDate(Date endBidDate) {
		this.endBidDate = endBidDate;
	}

	public void setInspector(CorePerson inspector) {
		this.inspector = inspector;
	}

	public void setInspectorId(String inspectorId) {
		this.inspectorId = inspectorId;
	}

	public void setIsBid(Integer isBid) {
		this.isBid = isBid;
	}

	public void setLaboor(CorePerson laboor) {
		this.laboor = laboor;
	}

	public void setLaboorId(String laboorId) {
		this.laboorId = laboorId;
	}

	public void setLaborCost(Double laborCost) {
		this.laborCost = laborCost;
	}

	public void setMachinister(CorePerson machinister) {
		this.machinister = machinister;
	}

	public void setMachinisterId(String machinisterId) {
		this.machinisterId = machinisterId;
	}

	public void setMeterialer(CorePerson meterialer) {
		this.meterialer = meterialer;
	}

	public void setMeterialerId(String meterialerId) {
		this.meterialerId = meterialerId;
	}

	public void setMeterialExpense(Double meterialExpense) {
		this.meterialExpense = meterialExpense;
	}

	public void setOpenBidDate(Date openBidDate) {
		this.openBidDate = openBidDate;
	}

	public void setOtherWorkers(CorePerson otherWorkers) {
		this.otherWorkers = otherWorkers;
	}

	public void setOtherWorkersId(String otherWorkersId) {
		this.otherWorkersId = otherWorkersId;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public void setQueryBeginDate(String queryBeginDate) {
		this.queryBeginDate = queryBeginDate;
	}

	public void setQueryEndDate(String queryEndDate) {
		this.queryEndDate = queryEndDate;
	}

	public void setSaver(CorePerson saver) {
		this.saver = saver;
	}

	public void setSaverId(String saverId) {
		this.saverId = saverId;
	}

	public void setStandarder(CorePerson standarder) {
		this.standarder = standarder;
	}

	public void setStandarderId(String standarderId) {
		this.standarderId = standarderId;
	}

	public void setTecBid(String tecBid) {
		this.tecBid = tecBid;
	}

	public void setTecBidName(CorePerson tecBidName) {
		this.tecBidName = tecBidName;
	}

	public String getCorePersonId() {
		return corePersonId;
	}

	public void setCorePersonId(String corePersonId) {
		this.corePersonId = corePersonId;
	}

	public Integer getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(Integer buildDate) {
		this.buildDate = buildDate;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

}