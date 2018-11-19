package co.dc.ccpt.modules.contractmanagement.procontract.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.entity.SubBidCompany;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.entity.Tender;
import co.dc.ccpt.modules.programmanage.entity.Company;
import co.dc.ccpt.modules.programmanage.entity.SubpackageProgram;
import co.dc.ccpt.modules.sys.entity.User;

/**
 * 分包合同对象SubProContract
 * 
 * @author Administrator
 *
 */
public class SubProContract extends DataEntity<SubProContract> {
	private static final long serialVersionUID = 1L;
	private Integer approvalStatus;// 审批状态
	private Date beginContractDate;// 开始 签订时间
	private String buildDate;// 工期（开工与竣工之差）
	private Date completeDate;// 竣工日期
	private String connector;// 工程联系人
	private Integer contractStatus;// 审批状态
	private String employer;// 发包方（默认金卓）
	private Date endContractDate;// 结束 签订时间
	private String phoneNum;// 联系人电话
	private ProContract proContract;// 总包合同对象
	private Date startDate;// 开工日期
	private SubBidCompany subBidCompany;// 投标记录对象（便于承包方名称的获取）
	private SubpackageProgram subpackageProgram;// 分包项目对象
	private String subProAddr;// 工程地址
	private Date subProContractDate;// 分包合同签订日期
	private String subProContractName;// 分包合同名称
	private String subProContractNum;// 分包合同编号
	private Double subProTotalPrice;// 分包合同总价
	private User user;// 用户对象（拟草人）
	private Tender tender;// 招标对象
	private Company company;// 分包参投单位对象
	private String subCompId;// 分包承包单位id 

	public SubProContract() {
		super();
	}

	public SubProContract(String id) {
		super(id);
	}

	public SubProContract(ProContract proContract) {
		this.proContract = proContract;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginContractDate() {
		return beginContractDate;
	}

	public String getBuildDate() {
		return buildDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCompleteDate() {
		return completeDate;
	}

	public String getConnector() {
		return connector;
	}

	public String getEmployer() {
		return employer;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndContractDate() {
		return endContractDate;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public ProContract getProContract() {
		return proContract;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartDate() {
		return startDate;
	}

	public SubBidCompany getSubBidCompany() {
		return subBidCompany;
	}

	public SubpackageProgram getSubpackageProgram() {
		return subpackageProgram;
	}

	public String getSubProAddr() {
		return subProAddr;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSubProContractDate() {
		return subProContractDate;
	}

	public String getSubProContractName() {
		return subProContractName;
	}

	public String getSubProContractNum() {
		return subProContractNum;
	}

	public Double getSubProTotalPrice() {
		return subProTotalPrice;
	}

	public User getUser() {
		return user;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public void setBeginContractDate(Date beginContractDate) {
		this.beginContractDate = beginContractDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public void setConnector(String connector) {
		this.connector = connector;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public void setEndContractDate(Date endContractDate) {
		this.endContractDate = endContractDate;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setProContract(ProContract proContract) {
		this.proContract = proContract;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setSubBidCompany(SubBidCompany subBidCompany) {
		this.subBidCompany = subBidCompany;
	}

	public void setSubpackageProgram(SubpackageProgram subpackageProgram) {
		this.subpackageProgram = subpackageProgram;
	}

	public void setSubProAddr(String subProAddr) {
		this.subProAddr = subProAddr;
	}

	public void setSubProContractDate(Date subProContractDate) {
		this.subProContractDate = subProContractDate;
	}

	public void setSubProContractName(String subProContractName) {
		this.subProContractName = subProContractName;
	}

	public void setSubProContractNum(String subProContractNum) {
		this.subProContractNum = subProContractNum;
	}

	public void setSubProTotalPrice(Double subProTotalPrice) {
		this.subProTotalPrice = subProTotalPrice;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(Integer contractStatus) {
		this.contractStatus = contractStatus;
	}

	public Tender getTender() {
		return tender;
	}

	public void setTender(Tender tender) {
		this.tender = tender;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getSubCompId() {
		return subCompId;
	}

	public void setSubCompId(String subCompId) {
		this.subCompId = subCompId;
	}

}
