package co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.entity;



import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.entity.SubBidCompany;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.entity.Tender;
import co.dc.ccpt.modules.programmanage.entity.SubpackageProgram;

/**
 * 清评标管理Entity
 * @author lxh
 * @version 2018-04-22
 */
public class ClearEvaluate extends DataEntity<ClearEvaluate> {
	
	private static final long serialVersionUID = 1L;
	private String subpackageProgramId;		// 子项目id
	private String certificate;		// 施工企业资质证书
	private String writeCircumstances;		// 投标书填写情况
	private String secretCircumstances;		// 投标书密封情况
	private String performance; //业绩
	private String buildDate;  //工期
	private String bidCompanyName;//参投单位名称
	private Double bidPrice;//投标价格
	private String design;//施工组织设计
	private Date accessDate;//标书送达时间
	private SubpackageProgram subpackageProgram; //子项目对象
	private SubBidCompany subBidCompany; //投标单位对象（投标记录管理对应）
	private Tender tender;    // 招标对象
	private Integer isBid;    // 是否中标 
	private List<EvaluateWorker> evaluateUserList = Lists.newArrayList();		// 子表列表
	
	public ClearEvaluate() {
		super();
	}

	public ClearEvaluate(String id){
		super(id);
	}


	public String getPerformance() {
		return performance;
	}

	public void setPerformance(String performance) {
		this.performance = performance;
	}

	public String getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	public String getBidCompanyName() {
		return bidCompanyName;
	}

	public void setBidCompanyName(String bidCompanyName) {
		this.bidCompanyName = bidCompanyName;
	}

	public Double getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(Double bidPrice) {
		this.bidPrice = bidPrice;
	}

	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAccessDate() {
		return accessDate;
	}

	public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
	}

	public SubpackageProgram getSubpackageProgram() {
		return subpackageProgram;
	}

	public void setSubpackageProgram(SubpackageProgram subpackageProgram) {
		this.subpackageProgram = subpackageProgram;
	}

	public SubBidCompany getSubBidCompany() {
		return subBidCompany;
	}

	public void setSubBidCompany(SubBidCompany subBidCompany) {
		this.subBidCompany = subBidCompany;
	}

	public Tender getTender() {
		return tender;
	}

	public void setTender(Tender tender) {
		this.tender = tender;
	}

	public Integer getIsBid() {
		return isBid;
	}

	public void setIsBid(Integer isBid) {
		this.isBid = isBid;
	}

	@ExcelField(title="子项目id", align=2, sort=8)
	public String getSubpackageProgramId() {
		return subpackageProgramId;
	}

	public void setSubpackageProgramId(String subpackageProgramId) {
		this.subpackageProgramId = subpackageProgramId;
	}
	
	@ExcelField(title="施工企业资质证书", align=2, sort=9)
	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	
	@ExcelField(title="投标书填写情况", align=2, sort=10)
	public String getWriteCircumstances() {
		return writeCircumstances;
	}

	public void setWriteCircumstances(String writeCircumstances) {
		this.writeCircumstances = writeCircumstances;
	}
	
	@ExcelField(title="投标书密封情况", align=2, sort=11)
	public String getSecretCircumstances() {
		return secretCircumstances;
	}

	public void setSecretCircumstances(String secretCircumstances) {
		this.secretCircumstances = secretCircumstances;
	}

	public List<EvaluateWorker> getEvaluateUserList() {
		return evaluateUserList;
	}

	public void setEvaluateUserList(List<EvaluateWorker> evaluateUserList) {
		this.evaluateUserList = evaluateUserList;
	}
	
}