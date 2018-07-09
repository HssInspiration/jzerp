package co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.coreperson.basicinfo.entity.CorePerson;
import co.dc.ccpt.modules.programmanage.entity.Company;
import co.dc.ccpt.modules.programmanage.entity.Program;
import co.dc.ccpt.modules.sys.entity.DictValue;

/**
 * 参投单位表Entity
 * @author lxh
 * @version 2018-03-10
 */
public class BidCompanyManage extends DataEntity<BidCompanyManage> {
	private static final long serialVersionUID = 1L;
	
	private Date bidDate;		         // 投标时间
	private Double bidPrice;		     // 投标价
	private BidtableQuery bidtableQuery; // 投标信息表id 父类
	private String comBid;		         // 商务标
	private String constructorId;		 // 建造师
	
	private String constrworkerId;		 // 施工员
	private String costerId;		     // 造价员
	private String deposit;		         // 递交保证金金额
	private String depositEnclosure;	 // 相关附件
	
	private String directorId;		     // 技术负责人
	private String ecoBid;		         // 经济标
	private String inspectorId;		     // 质检员
	private Integer isBid;		         // 是否中标
	private Double laborCost;		     // 劳务费
	private Double discountRate;		 // 让利幅度
	
	private String meterialerId;		 // 材料员
	private Double meterialExpense;		 // 材料费
	private String saverId;		         // 安全员
	private String tecBid;		         // 技术标
	
	private Program program;             //项目工程对象
	private Company company;             //单位对象
	
	/*人员名称*/
	private CorePerson tecBidName;      // 技术标负责人
	private CorePerson comBidName;      // 商务标负责人
	private CorePerson ecoBidName;      // 经济标负责人
	private CorePerson constructoor;    // 建造师名称
	private CorePerson director;        // 技术负责人
	
	private CorePerson saver;           // 安全员名称
	private CorePerson constrworker;    // 施工员名称
	private CorePerson inspector;       // 质检员名称
	private CorePerson meterialer;      // 材料员名称
	private CorePerson coster;          // 造价员名称
//	private Worker tecBidName;      // 技术标负责人
//	private Worker comBidName;      // 商务标负责人
//	private Worker ecoBidName;      // 经济标负责人
//	private Worker constructoor;    // 建造师名称
//	private Worker director;        // 技术负责人
//	
//	private Worker saver;           // 安全员名称
//	private Worker constrworker;    // 施工员名称
//	private Worker inspector;       // 质检员名称
//	private Worker meterialer;      // 材料员名称
//	private Worker coster;          // 造价员名称
	
	
	private DictValue dictValue;    // 字典值对象 
	
	public BidCompanyManage() {
		super();
	}

	public BidCompanyManage(BidtableQuery bidtableQuery){
		this.bidtableQuery = bidtableQuery;
	}

	public BidCompanyManage(String id){
		super(id);
	}
	
	public DictValue getDictValue() {
		return dictValue;
	}

	public void setDictValue(DictValue dictValue) {
		this.dictValue = dictValue;
	}


	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="投标时间不能为空")
	@ExcelField(title="投标时间", align=2, sort=4)
	public Date getBidDate() {
		return bidDate;
	}
	
	public Double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}

	@NotNull(message="投标价不能为空")
	@ExcelField(title="投标价", align=2, sort=3)
	public Double getBidPrice() {
		return bidPrice;
	}

	public BidtableQuery getBidtableQuery() {
		return bidtableQuery;
	}
	
	@ExcelField(title="商务标", dictType="", align=2, sort=8)
	public String getComBid() {
		return comBid;
	}

	@ExcelField(title="建造师", dictType="", align=2, sort=10)
	public String getConstructorId() {
		return constructorId;
	}

	@ExcelField(title="施工员", dictType="", align=2, sort=13)
	public String getConstrworkerId() {
		return constrworkerId;
	}
	
	@ExcelField(title="造价员", dictType="", align=2, sort=16)
	public String getCosterId() {
		return costerId;
	}

	@ExcelField(title="递交保证金金额", align=2, sort=18)
	public String getDeposit() {
		return deposit;
	}
	
	@ExcelField(title="相关附件", align=2, sort=19)
	public String getDepositEnclosure() {
		return depositEnclosure;
	}

	@ExcelField(title="技术负责人", dictType="", align=2, sort=11)
	public String getDirectorId() {
		return directorId;
	}
	
	@ExcelField(title="经济标", dictType="", align=2, sort=9)
	public String getEcoBid() {
		return ecoBid;
	}

	@ExcelField(title="质检员", dictType="", align=2, sort=14)
	public String getInspectorId() {
		return inspectorId;
	}
	
	@NotNull(message="是否中标不能为空")
	@ExcelField(title="是否中标", dictType="yes_no", align=2, sort=17)
	public Integer getIsBid() {
		return isBid;
	}

	@NotNull(message="劳务费不能为空")
	@ExcelField(title="劳务费", align=2, sort=5)
	public Double getLaborCost() {
		return laborCost;
	}
	
	@ExcelField(title="材料员", dictType="", align=2, sort=15)
	public String getMeterialerId() {
		return meterialerId;
	}

	@ExcelField(title="材料费", align=2, sort=6)
	public Double getMeterialExpense() {
		return meterialExpense;
	}
	
	@ExcelField(title="安全员", dictType="", align=2, sort=12)
	public String getSaverId() {
		return saverId;
	}
	
	@ExcelField(title="技术标", dictType="", align=2, sort=7)
	public String getTecBid() {
		return tecBid;
	}

	public void setBidDate(Date bidDate) {
		this.bidDate = bidDate;
	}
	
	public void setBidPrice(Double bidPrice) {
		this.bidPrice = bidPrice;
	}

	public void setBidtableQuery(BidtableQuery bidtableQuery) {
		this.bidtableQuery = bidtableQuery;
	}
	
	public void setComBid(String comBid) {
		this.comBid = comBid;
	}
	
	public void setConstructorId(String constructorId) {
		this.constructorId = constructorId;
	}

	public void setConstrworkerId(String constrworkerId) {
		this.constrworkerId = constrworkerId;
	}
	
	public void setCosterId(String costerId) {
		this.costerId = costerId;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}
	
	public void setDepositEnclosure(String depositEnclosure) {
		this.depositEnclosure = depositEnclosure;
	}

	public void setDirectorId(String directorId) {
		this.directorId = directorId;
	}
	
	public void setEcoBid(String ecoBid) {
		this.ecoBid = ecoBid;
	}

	public void setInspectorId(String inspectorId) {
		this.inspectorId = inspectorId;
	}
	
	public void setIsBid(Integer isBid) {
		this.isBid = isBid;
	}

	public void setLaborCost(Double laborCost) {
		this.laborCost = laborCost;
	}
	
	public void setMeterialerId(String meterialerId) {
		this.meterialerId = meterialerId;
	}

	public void setMeterialExpense(Double meterialExpense) {
		this.meterialExpense = meterialExpense;
	}


	public void setSaverId(String saverId) {
		this.saverId = saverId;
	}

	public void setTecBid(String tecBid) {
		this.tecBid = tecBid;
	}

	public CorePerson getTecBidName() {
		return tecBidName;
	}

	public void setTecBidName(CorePerson tecBidName) {
		this.tecBidName = tecBidName;
	}

	public CorePerson getComBidName() {
		return comBidName;
	}

	public void setComBidName(CorePerson comBidName) {
		this.comBidName = comBidName;
	}

	public CorePerson getEcoBidName() {
		return ecoBidName;
	}

	public void setEcoBidName(CorePerson ecoBidName) {
		this.ecoBidName = ecoBidName;
	}

	public CorePerson getConstructoor() {
		return constructoor;
	}

	public void setConstructoor(CorePerson constructoor) {
		this.constructoor = constructoor;
	}

	public CorePerson getDirector() {
		return director;
	}

	public void setDirector(CorePerson director) {
		this.director = director;
	}

	public CorePerson getSaver() {
		return saver;
	}

	public void setSaver(CorePerson saver) {
		this.saver = saver;
	}

	public CorePerson getConstrworker() {
		return constrworker;
	}

	public void setConstrworker(CorePerson constrworker) {
		this.constrworker = constrworker;
	}

	public CorePerson getInspector() {
		return inspector;
	}

	public void setInspector(CorePerson inspector) {
		this.inspector = inspector;
	}

	public CorePerson getMeterialer() {
		return meterialer;
	}

	public void setMeterialer(CorePerson meterialer) {
		this.meterialer = meterialer;
	}

	public CorePerson getCoster() {
		return coster;
	}

	public void setCoster(CorePerson coster) {
		this.coster = coster;
	}


}