package co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity;

import co.dc.ccpt.core.persistence.DataEntity;

public class Company extends DataEntity<Company> {
	private static final long serialVersionUID = 1L;
	private String companyNum;  //公司编号
	private String companyName; //公司名称
	private String companyCont; //公司详情
	public Company() {
		super();
	}
	
	public Company(String companyNum, String companyName, String companyCont) {
		super();
		this.companyNum = companyNum;
		this.companyName = companyName;
		this.companyCont = companyCont;
	}

	public String getCompanyNum() {
		return companyNum;
	}

	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyCont() {
		return companyCont;
	}

	public void setCompanyCont(String companyCont) {
		this.companyCont = companyCont;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String toString() {
		return "Company [companyNum=" + companyNum + ", companyName="
				+ companyName + ", companyCont=" + companyCont + "]";
	}
	
}
