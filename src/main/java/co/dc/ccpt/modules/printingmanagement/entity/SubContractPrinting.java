package co.dc.ccpt.modules.printingmanagement.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;

public class SubContractPrinting extends DataEntity<SubContractPrinting> {

	private static final long serialVersionUID = 1L;
	private Date beginPrintDate;// 用章开始日期
	private Integer contractType;// 合同类别
	private Date endPrintDate;// 用章开始日期
	private String isStamp;// 是否已用章
	private Date printDate;// 用章日期
	private String printNum;// 用章编号
	private Integer printType;// 用章类别
	private SubProContract subProContract;// 分包合同对象
	private String times;// 用章次数

	public SubContractPrinting() {
		super();
	}

	public SubContractPrinting(String id) {
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginPrintDate() {
		return beginPrintDate;
	}

	public Integer getContractType() {
		return contractType;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndPrintDate() {
		return endPrintDate;
	}

	public String getIsStamp() {
		return isStamp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPrintDate() {
		return printDate;
	}

	public String getPrintNum() {
		return printNum;
	}

	public Integer getPrintType() {
		return printType;
	}

	public SubProContract getSubProContract() {
		return subProContract;
	}

	public String getTimes() {
		return times;
	}

	public void setBeginPrintDate(Date beginPrintDate) {
		this.beginPrintDate = beginPrintDate;
	}

	public void setContractType(Integer contractType) {
		this.contractType = contractType;
	}

	public void setEndPrintDate(Date endPrintDate) {
		this.endPrintDate = endPrintDate;
	}

	public void setIsStamp(String isStamp) {
		this.isStamp = isStamp;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public void setPrintNum(String printNum) {
		this.printNum = printNum;
	}

	public void setPrintType(Integer printType) {
		this.printType = printType;
	}

	public void setSubProContract(SubProContract subProContract) {
		this.subProContract = subProContract;
	}

	public void setTimes(String times) {
		this.times = times;
	}

}
