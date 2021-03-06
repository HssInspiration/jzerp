package co.dc.ccpt.modules.printingmanagement.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;

public class ContractPrinting extends DataEntity<ContractPrinting> {
	private static final long serialVersionUID = 1L;
	private Date beginPrintDate;// 用章开始日期
	private Integer contractType;// 合同类别
	private Date endPrintDate;// 用章开始日期
	private String isStamp;// 是否用章
	private Date printDate;// 用章日期
	private String printNum;// 用章编号
	private String printDetailType;// 用章详细类别
	private Integer printType;// 用章类型
	private ProContract proContract;// 总包合同对象
	private String times;// 用章次数

	public ContractPrinting() {
		super();
	}

	public ContractPrinting(String id) {
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

	public ProContract getProContract() {
		return proContract;
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

	public void setProContract(ProContract proContract) {
		this.proContract = proContract;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	@NotNull(message="用章类别不能为空")
	@ExcelField(title="用章类别", dictType="print_detail_type")
	public String getPrintDetailType() {
		return printDetailType;
	}

	public void setPrintDetailType(String printDetailType) {
		this.printDetailType = printDetailType;
	}

}
