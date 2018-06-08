package co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.entity;

import java.util.Date;

public class Submit {
	private String companyName; // 单位名称
	private Date submitDate;    // 递交时间
	private String submiter;    // 递交人
	private String submitTel;   // 联系电话

	public Submit() {
		super();
	}

	public Submit(String companyName, String submiter, Date submitDate,
			String submitTel) {
		super();
		this.companyName = companyName;
		this.submiter = submiter;
		this.submitDate = submitDate;
		this.submitTel = submitTel;
	}

	public String getCompanyName() {
		return companyName;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public String getSubmiter() {
		return submiter;
	}

	public String getSubmitTel() {
		return submitTel;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public void setSubmiter(String submiter) {
		this.submiter = submiter;
	}

	public void setSubmitTel(String submitTel) {
		this.submitTel = submitTel;
	}

	public String toString() {
		return "Submit [companyName=" + companyName + ", submiter=" + submiter
				+ ", submitDate=" + submitDate + ", submitTel=" + submitTel
				+ "]";
	}
}
