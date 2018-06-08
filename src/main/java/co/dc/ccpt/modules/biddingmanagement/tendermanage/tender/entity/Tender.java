package co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.entity;

import java.util.Date;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.entity.SubpackageProgram;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * 招标信息管理Entity
 * @author lxh
 * @version 2018-03-27
 */
public class Tender extends DataEntity<Tender> {
	
	private static final long serialVersionUID = 1L;
	private String tenderiNum;		// 招标编号
	private String subpackageProgramId;		// 子项目工程id
	private String arrange;		// 招标范围
	private String quality;		// 质量要求
	private String buildDate;		// 工期要求
	private Date openBidDate;		// 投标截止时间
	private String openBidAddr;		// 开标地点
	private String evaluateMethod;		// 评标办法
	private Date beginOpenBidDate;		// 开始 开标时间
	private Date endOpenBidDate;		// 结束 开标时间
//	private Double tenderCtrlPrice;     // 招标控制价
	private String tenderCtrlPrice;     // 招标控制价（描述）
	private String tenderDirector;      // 招标负责人
	private SubpackageProgram subpackageProgram; //分包项目对象 
	private Double deposit; //保证金
	private String projectProfile;//工程概况 
	
	public Tender() {
		super();
	}

	public Tender(String id){
		super(id);
	}

	public SubpackageProgram getSubpackageProgram() {
		return subpackageProgram;
	}

	public void setSubpackageProgram(SubpackageProgram subpackageProgram) {
		this.subpackageProgram = subpackageProgram;
	}

	public Double getDeposit() {
		return deposit;
	}

	public String getProjectProfile() {
		return projectProfile;
	}

	public void setProjectProfile(String projectProfile) {
		this.projectProfile = projectProfile;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}

	@ExcelField(title="招标编号", align=2, sort=1)
	public String getTenderiNum() {
		return tenderiNum;
	}

	
//	public Double getTenderCtrlPrice() {
//		return tenderCtrlPrice;
//	}
//
//	public void setTenderCtrlPrice(Double tenderCtrlPrice) {
//		this.tenderCtrlPrice = tenderCtrlPrice;
//	}

	public String getTenderDirector() {
		return tenderDirector;
	}

	public String getTenderCtrlPrice() {
		return tenderCtrlPrice;
	}

	public void setTenderCtrlPrice(String tenderCtrlPrice) {
		this.tenderCtrlPrice = tenderCtrlPrice;
	}

	public void setTenderDirector(String tenderDirector) {
		this.tenderDirector = tenderDirector;
	}

	public void setTenderiNum(String tenderiNum) {
		this.tenderiNum = tenderiNum;
	}
	
	@ExcelField(title="子项目工程id", align=2, sort=2)
	public String getSubpackageProgramId() {
		return subpackageProgramId;
	}

	public void setSubpackageProgramId(String subpackageProgramId) {
		this.subpackageProgramId = subpackageProgramId;
	}
	
	@ExcelField(title="招标范围", align=2, sort=3)
	public String getArrange() {
		return arrange;
	}

	public void setArrange(String arrange) {
		this.arrange = arrange;
	}
	
	@ExcelField(title="质量要求", align=2, sort=4)
	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}
	
	@ExcelField(title="工期要求", align=2, sort=5)
	public String getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="开标时间", align=2, sort=6)
	public Date getOpenBidDate() {
		return openBidDate;
	}

	public void setOpenBidDate(Date openBidDate) {
		this.openBidDate = openBidDate;
	}
	
	@ExcelField(title="开标地点", align=2, sort=7)
	public String getOpenBidAddr() {
		return openBidAddr;
	}

	public void setOpenBidAddr(String openBidAddr) {
		this.openBidAddr = openBidAddr;
	}
	
	@ExcelField(title="评标办法", align=2, sort=8)
	public String getEvaluateMethod() {
		return evaluateMethod;
	}

	public void setEvaluateMethod(String evaluateMethod) {
		this.evaluateMethod = evaluateMethod;
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
		
}