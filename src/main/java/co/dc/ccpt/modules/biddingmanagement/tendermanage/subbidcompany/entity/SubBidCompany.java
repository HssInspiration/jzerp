package co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Company;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.entity.SubpackageProgram;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.entity.Tender;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 子项目参投单位管理Entity
 * @author lxh
 * @version 2018-03-27
 */
public class SubBidCompany extends DataEntity<SubBidCompany> {
	
	private static final long serialVersionUID = 1L;
	private String subBidNum;		    // 投标编号
	private String subBidPrice;		    // 投标价
	private Date subBidDate;		    // 投标日期
	private Double meterialCost;		// 材料费
	private String ctrlPrice;		    // 控制价
	private Double floorPrice;		    // 标底价
	private Double provisionPrice;		// 暂列金额
	private String meterials;		    // 现场所需材料
	private String saverId;		        // 安全员id
	private String presentDireId;		// 现场负责人id
	private String presentDireTel;		// 现场负责人号码
	private String programDireId;		// 项目负责人id
	private String programDireTel;		// 项目负责人号码
	private Double laborCost;		    // 劳务费
	private Date beginSubBidDate;		// 开始 投标日期
	private Date endSubBidDate;		    // 结束 投标日期
	private String subProgramName;      // 子项目名称
	private String saverTel;            //安全员手机号码
	private Company company;            //公司对象
	private SubpackageProgram subpackageProgram; //分包项目对象
	private Tender tender;// 招标id
	private String submiter; //递交人
	private String submitTel; //递交人联系方式
	
	public SubBidCompany() {
		super();
	}

	public SubBidCompany(String id){
		super(id);
	}

	@ExcelField(title="投标编号", align=2, sort=7)
	public String getSubBidNum() {
		return subBidNum;
	}

	public Tender getTender() {
		return tender;
	}

	public void setTender(Tender tender) {
		this.tender = tender;
	}

	public String getSubProgramName() {
		return subProgramName;
	}

	public void setSubProgramName(String subProgramName) {
		this.subProgramName = subProgramName;
	}

	public String getSubmiter() {
		return submiter;
	}

	public void setSubmiter(String submiter) {
		this.submiter = submiter;
	}

	public String getSubmitTel() {
		return submitTel;
	}

	public void setSubmitTel(String submitTel) {
		this.submitTel = submitTel;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public SubpackageProgram getSubpackageProgram() {
		return subpackageProgram;
	}

	public void setSubpackageProgram(SubpackageProgram subpackageProgram) {
		this.subpackageProgram = subpackageProgram;
	}

	public void setSubBidNum(String subBidNum) {
		this.subBidNum = subBidNum;
	}
	
//	@NotNull(message="投标价不能为空")
//	@ExcelField(title="投标价", align=2, sort=8)
//	public Double getSubBidPrice() {
//		return subBidPrice;
//	}

	public String getSaverTel() {
		return saverTel;
	}

	public String getSubBidPrice() {
		return subBidPrice;
	}

	public void setSubBidPrice(String subBidPrice) {
		this.subBidPrice = subBidPrice;
	}

	public void setSaverTel(String saverTel) {
		this.saverTel = saverTel;
	}

//	public void setSubBidPrice(Double subBidPrice) {
//		this.subBidPrice = subBidPrice;
//	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="投标日期不能为空")
	@ExcelField(title="投标日期", align=2, sort=9)
	public Date getSubBidDate() {
		return subBidDate;
	}

	public void setSubBidDate(Date subBidDate) {
		this.subBidDate = subBidDate;
	}
	
	@ExcelField(title="材料费", align=2, sort=10)
	public Double getMeterialCost() {
		return meterialCost;
	}

	public void setMeterialCost(Double meterialCost) {
		this.meterialCost = meterialCost;
	}
	
//	@ExcelField(title="控制价", align=2, sort=11)
//	public Double getCtrlPrice() {
//		return ctrlPrice;
//	}
//
//	public void setCtrlPrice(Double ctrlPrice) {
//		this.ctrlPrice = ctrlPrice;
//	}
	
	@ExcelField(title="标底价", align=2, sort=12)
	public Double getFloorPrice() {
		return floorPrice;
	}

	public String getCtrlPrice() {
		return ctrlPrice;
	}

	public void setCtrlPrice(String ctrlPrice) {
		this.ctrlPrice = ctrlPrice;
	}

	public void setFloorPrice(Double floorPrice) {
		this.floorPrice = floorPrice;
	}
	
	@ExcelField(title="暂列金额", align=2, sort=13)
	public Double getProvisionPrice() {
		return provisionPrice;
	}

	public void setProvisionPrice(Double provisionPrice) {
		this.provisionPrice = provisionPrice;
	}
	
	@ExcelField(title="现场所需材料", align=2, sort=14)
	public String getMeterials() {
		return meterials;
	}

	public void setMeterials(String meterials) {
		this.meterials = meterials;
	}
	
	@ExcelField(title="安全员id", align=2, sort=15)
	public String getSaverId() {
		return saverId;
	}

	public void setSaverId(String saverId) {
		this.saverId = saverId;
	}
	
	@ExcelField(title="现场负责人id", align=2, sort=16)
	public String getPresentDireId() {
		return presentDireId;
	}

	public void setPresentDireId(String presentDireId) {
		this.presentDireId = presentDireId;
	}
	
	@ExcelField(title="现场负责人号码", align=2, sort=17)
	public String getPresentDireTel() {
		return presentDireTel;
	}

	public void setPresentDireTel(String presentDireTel) {
		this.presentDireTel = presentDireTel;
	}
	
	@ExcelField(title="项目负责人id", align=2, sort=18)
	public String getProgramDireId() {
		return programDireId;
	}

	public void setProgramDireId(String programDireId) {
		this.programDireId = programDireId;
	}
	
	@ExcelField(title="项目负责人号码", align=2, sort=19)
	public String getProgramDireTel() {
		return programDireTel;
	}

	public void setProgramDireTel(String programDireTel) {
		this.programDireTel = programDireTel;
	}
	
	@ExcelField(title="劳务费", align=2, sort=20)
	public Double getLaborCost() {
		return laborCost;
	}

	public void setLaborCost(Double laborCost) {
		this.laborCost = laborCost;
	}
	
	public Date getBeginSubBidDate() {
		return beginSubBidDate;
	}

	public void setBeginSubBidDate(Date beginSubBidDate) {
		this.beginSubBidDate = beginSubBidDate;
	}
	
	public Date getEndSubBidDate() {
		return endSubBidDate;
	}

	public void setEndSubBidDate(Date endSubBidDate) {
		this.endSubBidDate = endSubBidDate;
	}
		
}